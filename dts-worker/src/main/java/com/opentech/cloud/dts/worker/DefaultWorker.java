package com.opentech.cloud.dts.worker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskStatus;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService.Event;
import com.opentech.cloud.dts.utils.JVMUtils;
import com.opentech.cloud.dts.utils.NetworkUtils;
import com.opentech.cloud.dts.worker.task.executor.DefaultTaskExecutor;
import com.opentech.cloud.dts.worker.task.executor.TaskExecutor;


/**
 * 
 * @author sihai
 *
 */
public class DefaultWorker implements Worker {
	
	public static final int DEFAULT_TASK_EXECUTOR_THREAD_POOL_CORE_SIZE = 32;
	
	public static final int DEFAULT_TASK_EXECUTOR_THREAD_POOL_MAX_SIZE = 64;
	
	public static final int DEFAULT_TASK_EXECUTOR_THREAD_POOL_KEEP_ALVE = 60;
	
	public static final int DEFAULT_TASK_EXECUTOR_THREAD_POOL_QUEUE_CAPACITY = 32;
	
	public static final int DEFAULT_TASK_FETCHER_THREAD_COUNT = 4;

	/**
	 * 
	 */
	private com.opentech.cloud.dts.common.worker.Worker self;
	
	/**
	 * 
	 */
	private RuntimeMetadataService rms;
	
	/**
	 * 
	 */
	private ExecutorService ttes;
	
	/**
	 * 
	 */
	private TaskExecutor te;
	
	/**
	 * 
	 * @param rms
	 */
	public DefaultWorker(String group, RuntimeMetadataService rms) {
		this.rms = rms;
		this.self = new com.opentech.cloud.dts.common.worker.Worker();
		this.self.setGroup(group);
		this.self.setIp(NetworkUtils.getLocalIp());
		this.self.setPid(String.valueOf(JVMUtils.getPid()));
	}
	
	@Override
	public void initialize() {
		
		//
		this.ttes = Executors.newFixedThreadPool(DEFAULT_TASK_FETCHER_THREAD_COUNT);
		
		// 
		this.te = new DefaultTaskExecutor(DEFAULT_TASK_EXECUTOR_THREAD_POOL_CORE_SIZE, DEFAULT_TASK_EXECUTOR_THREAD_POOL_MAX_SIZE, DEFAULT_TASK_EXECUTOR_THREAD_POOL_KEEP_ALVE, DEFAULT_TASK_EXECUTOR_THREAD_POOL_QUEUE_CAPACITY);
		
		// 发布自己
		this.publishSelf();
				
		// 监听任务变化
		this.monitorTaskGroupChange();
		// 尝试监听自己的任务组
		this.monitorMyGroup(this.self.getGroup());
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		if(null != this.ttes) {
			this.ttes.shutdown();
		}
		if(null != this.te) {
			this.te.shutdown();
		}
	}

	/**
	 * 发布自己
	 */
	private void publishSelf() {
		rms.getWorkerMetadataService().join(this.self);
	}
	
	/**
	 * 监听任务组
	 */
	private void monitorTaskGroupChange() {
		this.rms.getTaskMetadataService().subscribeGroup(new TaskMetadataService.Listener() {
			
			@Override
			public void fire(Event event) {
				DefaultWorker.this.monitorMyGroup();
				DefaultWorker.this.monitorTaskGroupChange();
			}
		});
	}
	
	/**
	 * 
	 */
	private void monitorMyGroup() {
		List<String> glist = this.rms.getTaskMetadataService().getAllGroup();
		for(String g : glist) {
			if(StringUtils.equals(g, this.self.getGroup())) {
				this.monitorMyGroup(g);
			}
		}
	}
	
	/**
	 * 
	 * @param g
	 */
	private void monitorMyGroup(final String g) {
		List<String> slist = DefaultWorker.this.rms.getTaskMetadataService().getAllSchedulers(g);
		
		for(String s : slist) {
			this.monitorMyGroupScheduler(g, s);
		}
	}
	
	/**
	 * 
	 * @param g
	 * @param s
	 */
	private void monitorMyGroupScheduler(final String g, final String s) {
		this.rms.getTaskMetadataService().subscribeTaskOnGroupScheduler(g, s, TaskStatus.READY, new TaskMetadataService.Listener() {
			
			@Override
			public void fire(Event event) {
				DefaultWorker.this.tryTaskOnGroupAndScheduler(g, s, TaskStatus.READY);
				DefaultWorker.this.monitorMyGroupScheduler(g, s);
			}
			
		});
	}
	
	/**
	 * 
	 * @param g
	 * @param s
	 */
	private void tryTaskOnGroupAndScheduler(final String g, final String s, final TaskStatus ts) {
		this.ttes.execute(new Runnable() {

			@Override
			public void run() {
				List<Task> tlist = DefaultWorker.this.rms.getTaskMetadataService().getTasks(g, s, ts);
				for(Task t : tlist) {
					if(DefaultWorker.this.rms.getTaskMetadataService().delete(t)) {
						DefaultWorker.this.rms.getTaskMetadataService().transform(t, TaskStatus.EXECUTING);
						DefaultWorker.this.te.execute(t, new TaskExecutor.Listener() {
							
							@Override
							public void succeed(Task t) {
								// TODO
								DefaultWorker.this.rms.getTaskMetadataService().transform(t, TaskStatus.EXECUTED);
							}
							
							@Override
							public void failed(Task t, Throwable e) {
								// TODO
								DefaultWorker.this.rms.getTaskMetadataService().transform(t, TaskStatus.FAILED);
							}
						});
					}
				}
			}
			
		});
	}
}

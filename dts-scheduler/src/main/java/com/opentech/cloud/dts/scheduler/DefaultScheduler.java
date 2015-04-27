package com.opentech.cloud.dts.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opentech.cloud.dts.api.task.TaskService;
import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskException;
import com.opentech.cloud.dts.common.task.TaskStatus;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlan;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;
import com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService.Event;
import com.opentech.cloud.dts.scheduler.plan.PlanExecutor;
import com.opentech.cloud.dts.scheduler.plan.quartz.DefaultPlanExecutor;
import com.opentech.cloud.dts.utils.JVMUtils;
import com.opentech.cloud.dts.utils.NetworkUtils;

/**
 * 
 * 
 * @author sihai
 *
 */
public class DefaultScheduler implements Scheduler {

	private static final Log logger = LogFactory.getLog(DefaultScheduler.class);
	
	/**
	 * 
	 */
	private long sequence;
	
	/**
	 * 
	 */
	private com.opentech.cloud.dts.common.scheduler.Scheduler self;
	
	/**
	 * 
	 */
	private RuntimeMetadataService rms;
	
	/**
	 * 
	 */
	private TaskService ts;
	
	/**
	 * 
	 */
	private PlanExecutor pe;
	
	/**
	 * 
	 */
	private Set<com.opentech.cloud.dts.common.scheduler.Scheduler> ss;
	
	/**
	 * 
	 * @param rms
	 */
	public DefaultScheduler(RuntimeMetadataService rms) {
		this.rms = rms;
		this.self = new com.opentech.cloud.dts.common.scheduler.Scheduler();
		this.self.setIp(NetworkUtils.getLocalIp());
		this.self.setMaster(false);
		this.self.setPid(String.valueOf(JVMUtils.getPid()));
		
		this.pe = new DefaultPlanExecutor(new PlanExecutor.Listener(){

			@Override
			public void fired(TaskExecutePlan plan, Object context) {
				DefaultScheduler.this.rms.getTaskMetadataService().transform((Task)context, TaskStatus.READY);
			}
			
		});
		this.ss = new HashSet<com.opentech.cloud.dts.common.scheduler.Scheduler>();
	}
	
	@Override
	public void initialize() {
		
		// 发布自己
		this.publishSelf();
				
		// 尝试成为master 
		this.tryMaster();
		
		// 监听任务
		this.listenGroup();
	}

	@Override
	public void start() {
		if(null != this.pe) {
			((DefaultPlanExecutor)this.pe).start();
		}
	}

	@Override
	public void stop() {
		if(null != this.pe) {
			((DefaultPlanExecutor)this.pe).stop();
		}
	}
	
	/**
	 * 尝试成为master
	 */
	private void tryMaster() {
		// master
		this.voteMaster();
		// 监听scheduler变化
		this.monitorScheduler();
	}
	
	/**
	 * 
	 */
	private void voteMaster() {
		logger.warn(String.format("Try to vote master: my sequence: %d", this.sequence));
		if(this.rms.getSchedulerMetadataService().voteMaster(this.sequence)) {
			// 自己成为了master, 监听scheduler加入, 退出
			logger.warn(String.format("I am master: my sequence: %d", this.sequence));
			this.self.setMaster(true);
		} else {
			// 监听master元数据, 只要master挂了, 自己尝试接管
			logger.warn(String.format("I am not master: my sequence: %d", this.sequence));
			this.self.setMaster(false);
		}
	}

	/**
	 * 发布自己
	 */
	private void publishSelf() {
		this.sequence = rms.getSchedulerMetadataService().join(this.self);
		this.ss.add(this.self);
	}
	
	/**
	 * 监控schedulers
	 */
	private void monitorScheduler() {
		this.rms.getSchedulerMetadataService().subscribe(new SchedulerMetadataService.Listener() {
			
			@Override
			public void fire(com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService.Event event) {
				// 竞选master
				DefaultScheduler.this.voteMaster();
				if(DefaultScheduler.this.self.isMaster()) {
					// 迁移已经离开的scheduler
					DefaultScheduler.this.migrateLeaveScheduler();
				}
				
				// 再次监听
				DefaultScheduler.this.monitorScheduler();
			}
		});
	}
	
	/**
	 * 
	 */
	private void migrateLeaveScheduler() {
		List<com.opentech.cloud.dts.common.scheduler.Scheduler> leaves = new ArrayList<com.opentech.cloud.dts.common.scheduler.Scheduler>();
		Set<com.opentech.cloud.dts.common.scheduler.Scheduler> ss = DefaultScheduler.this.rms.getSchedulerMetadataService().getAllScheduler();
		// 比较变化 主要是识别出有scheduler离开
		for(com.opentech.cloud.dts.common.scheduler.Scheduler s : DefaultScheduler.this.ss) {
			if(ss.contains(s)) {
				continue;
			}
			leaves.add(s);
		}
		DefaultScheduler.this.ss.clear();
		DefaultScheduler.this.ss = ss;
		
		// 迁移任务
		for(com.opentech.cloud.dts.common.scheduler.Scheduler s : leaves) {
			DefaultScheduler.this.migrateOneLeaveScheduler(s);
		}
	}
	
	/**
	 * 迁移离开的Scheduler
	 * @param s
	 */
	private void migrateOneLeaveScheduler(com.opentech.cloud.dts.common.scheduler.Scheduler s) {
		List<String> groups = this.rms.getTaskMetadataService().getAllGroup();
		for(String g : groups) {
			for(TaskStatus ts : TaskStatus.values()) {
				List<Task> tlist = this.rms.getTaskMetadataService().getTasks(g, s, ts);
				if(this.rms.getTaskMetadataService().deleteTasks(g, s, ts)) {
					try {
						this.ts.schedule(tlist);
					} catch (TaskException e) {
						logger.error("migrate leave scheduler, reschedule tasks failed", e);
					}
				}
			} 
		} 
	}
	
	/**
	 * 监听任务组
	 */
	private void listenGroup() {
		
		DefaultScheduler.this.scheduleOnGroups();
		
		this.rms.getTaskMetadataService().subscribeGroup(new TaskMetadataService.Listener() {
			
			@Override
			public void fire(Event event) {
				DefaultScheduler.this.scheduleOnGroups();
			}
		});
	}
	
	/**
	 * 
	 * @param group
	 */
	private void scheduleOnGroups() {
		List<String> glist = DefaultScheduler.this.rms.getTaskMetadataService().getAllGroup();
		for(String g : glist) {
			if(!DefaultScheduler.this.rms.getTaskMetadataService().isSchedulerOnGroup(DefaultScheduler.this.self, g)) {
				DefaultScheduler.this.rms.getTaskMetadataService().scheduleOnGroup(DefaultScheduler.this.self, g);
			}
			DefaultScheduler.this.listenOneGroupTask(g);
		}
	}
	
	/**
	 * 
	 * @param g
	 */
	private void listenOneGroupTask(final String g) {
		this.rms.getTaskMetadataService().subscribeTaskOnGroupScheduler(g, this.self, TaskStatus.WAITIN_SCHEDULE, new TaskMetadataService.Listener() {

			@Override
			public void fire(Event event) {
				List<Task> tlist = DefaultScheduler.this.rms.getTaskMetadataService().getTasks(g, DefaultScheduler.this.self, TaskStatus.WAITIN_SCHEDULE);
				for(Task t: tlist) {
					pe.execute(t.getPlan(), t);
					DefaultScheduler.this.rms.getTaskMetadataService().transform(t, TaskStatus.WAITIN_TRIGGERED);
				}
				DefaultScheduler.this.listenOneGroupTask(g);
			}
			
		});
	}
}

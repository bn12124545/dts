package com.opentech.cloud.dts.scheduler;

import com.opentech.cloud.dts.runtime.RuntimeMetadataService;
import com.opentech.cloud.dts.runtime.scheduler.SchedulerMasterMetadataService;
import com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService.Event;
import com.opentech.cloud.dts.utils.JVMUtils;
import com.opentech.cloud.dts.utils.NetworkUtils;

/**
 * 
 * @author sihai
 *
 */
public class DefaultScheduler implements Scheduler {

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
	 * @param rms
	 */
	public DefaultScheduler(RuntimeMetadataService rms) {
		this.rms = rms;
		this.self = new com.opentech.cloud.dts.common.scheduler.Scheduler();
		this.self.setIp(NetworkUtils.getLocalIp());
		this.self.setMaster(false);
		this.self.setPid(String.valueOf(JVMUtils.getPid()));
	}
	
	@Override
	public void initialize() {
		
		// 尝试成为master 
		this.tryMaster();
		
		// 发布自己
		this.publishSelf();
		
		// 监听任务
		this.listenTask();
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}
	
	/**
	 * 尝试成为master
	 */
	private void tryMaster() {
		// master
		if(this.rms.getSchedulerMasterMetadataService().voteMaster(this.self)) {
			// 自己成为了master, 监听scheduler加入, 退出
			rms.getSchedulerMetadataService().subscribe(new SchedulerMetadataService.Listener() {
				
				@Override
				public void fire(com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService.Event event) {
					
				}
			});
			this.self.setMaster(true);
		} else {
			// 监听master元数据, 只要master挂了, 自己尝试接管
			rms.getSchedulerMasterMetadataService().subscribe(new SchedulerMasterMetadataService.Listener() {
				
				@Override
				public void fire(com.opentech.cloud.dts.runtime.scheduler.SchedulerMasterMetadataService.Event event) {
					// TODO
				}
			});
			this.self.setMaster(false);
		}
	}

	/**
	 * 发布自己
	 */
	private void publishSelf() {
		rms.getSchedulerMetadataService().join(this.self);
	}
	
	/**
	 * 监听任务
	 */
	private void listenTask() {
		this.rms.getTaskMetadataService().subscribe(new TaskMetadataService.Listener() {
			
			@Override
			public void fire(Event event) {
				// TODO
			}
		});
	}
}

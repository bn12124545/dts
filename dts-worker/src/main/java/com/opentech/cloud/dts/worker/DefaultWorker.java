package com.opentech.cloud.dts.worker;

import com.opentech.cloud.dts.runtime.RuntimeMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService.Event;
import com.opentech.cloud.dts.utils.JVMUtils;
import com.opentech.cloud.dts.utils.NetworkUtils;


/**
 * 
 * @author sihai
 *
 */
public class DefaultWorker implements Worker {

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
		// 发布自己
		this.publishSelf();
				
		// 监听任务
		this.listenTask();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	/**
	 * 发布自己
	 */
	private void publishSelf() {
		rms.getWorkerMetadataService().join(this.self);
	}
	
	/**
	 * 监听任务
	 */
	private void listenTask() {
		this.rms.getTaskMetadataService().subscribeGroup(new TaskMetadataService.Listener() {
			
			@Override
			public void fire(Event event) {
				// TODO
			}
		});
	}
}

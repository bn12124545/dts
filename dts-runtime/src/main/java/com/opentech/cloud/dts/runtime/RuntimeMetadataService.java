package com.opentech.cloud.dts.runtime;

import com.opentech.cloud.dts.runtime.scheduler.SchedulerMasterMetadataService;
import com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService;
import com.opentech.cloud.dts.runtime.worker.WorkerMetadataService;


/**
 * 运行时元数据服务
 * @author sihai
 *
 */
public interface RuntimeMetadataService {
	
	/**
	 * 
	 * @return
	 */
	SchedulerMetadataService getSchedulerMetadataService();
	
	/**
	 * 
	 * @return
	 */
	SchedulerMasterMetadataService getSchedulerMasterMetadataService();
	
	/**
	 * 
	 * @return
	 */
	WorkerMetadataService getWorkerMetadataService();
	
	/**
	 * 
	 * @return
	 */
	TaskMetadataService getTaskMetadataService();
}

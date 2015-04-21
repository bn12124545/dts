package com.opentech.cloud.dts.runtime.task;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskStatus;

/**
 * Task元数据服务
 * @author sihai
 *
 */
public interface TaskMetadataService {
	
	/**
	 * 订阅事件
	 * @param l
	 */
	void subscribe(Listener l);
	
	/**
	 * 提交任务
	 * @param task
	 */
	void submit(Task task);
	
	/**
	 * 任务状态转换
	 * @param task
	 * @param newStatus
	 */
	void transform(Task task, TaskStatus newStatus);
	
	/**
	 * 删除任务
	 * @param task
	 */
	void delete(Task task);
	
	
	
	//=================================================================================
	//
	//=================================================================================
	/**
	 * 
	 * @author sihai
	 *
	 */
	class Event {
		
	}
	
	interface Listener {
		/**
		 * 
		 * @param event
		 */
		void fire(Event event);
	}
}

package com.opentech.cloud.dts.runtime.task;

import java.util.List;

import com.opentech.cloud.dts.common.scheduler.Scheduler;
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
	void subscribeGroup(Listener l);
	
	/**
	 * 
	 * @param g
	 * @param s
	 * @param l
	 */
	void subscribeSchedulerOnGroupTask(String g, Scheduler s, Listener l);
	
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
	
	/**
	 * 获取所有的分组
	 * @return
	 */
	List<String> getAllGroup();
	
	/**
	 * 
	 * @param group
	 * @param scheduler
	 * @param status
	 * @return
	 */
	List<Task> getTasks(String group, Scheduler scheduler, TaskStatus status);
	
	/**
	 * 
	 * @param group
	 * @param scheduler
	 * @param status
	 * @return
	 */
	boolean deleteTasks(String group, Scheduler scheduler, TaskStatus status);
	
	/**
	 * 
	 * @param scheduler
	 * @param group
	 * @return
	 */
	boolean isSchedulerOnGroup(Scheduler scheduler, String group);
	
	/**
	 * 
	 * @param scheduler
	 * @param group
	 */
	void scheduleOnGroup(Scheduler scheduler, String group);
	
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

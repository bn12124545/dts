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
	 * @param ts
	 * @param l
	 */
	void subscribeTaskOnGroupScheduler(String g, Scheduler s, TaskStatus ts, Listener l);
	
	/**
	 * 
	 * @param g
	 * @param s
	 * @param ts
	 * @param l
	 */
	void subscribeTaskOnGroupScheduler(String g, String s, TaskStatus ts, Listener l);
	
	/**
	 * 
	 * @param t
	 */
	void schedule(Task t);
	
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
	boolean delete(Task task);
	
	/**
	 * 获取所有的分组
	 * @return
	 */
	List<String> getAllGroup();
	
	/**
	 * 获取某一group下的所有scheduler
	 * @param group
	 * @return
	 */
	List<String> getAllSchedulers(String group);
	
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
	List<Task> getTasks(String group, String scheduler, TaskStatus status);
	
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

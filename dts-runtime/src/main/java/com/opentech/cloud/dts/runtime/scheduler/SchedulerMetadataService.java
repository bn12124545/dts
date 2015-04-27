package com.opentech.cloud.dts.runtime.scheduler;

import java.util.Set;

import com.opentech.cloud.dts.common.scheduler.Scheduler;

/**
 * Scheduler元数据服务
 * @author sihai
 *
 */
public interface SchedulerMetadataService {
	
	/**
	 * 订阅事件
	 * @param l
	 */
	void subscribe(Listener l);

	/**
	 * scheduler加入
	 * @param worker
	 */
	long join(Scheduler scheduler);
	
	/**
	 * scheduler离开
	 * @param worker
	 */
	void leave(Scheduler scheduler);
	
	/**
	 * 获取所有的scheduler
	 * @return
	 */
	Set<Scheduler> getAllScheduler();
	
	/**
	 * 
	 * @param sequence
	 * @return
	 */
	boolean voteMaster(long sequence);
	
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

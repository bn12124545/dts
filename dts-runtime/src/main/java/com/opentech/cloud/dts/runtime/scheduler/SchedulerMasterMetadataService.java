package com.opentech.cloud.dts.runtime.scheduler;

import com.opentech.cloud.dts.common.scheduler.Scheduler;

/**
 * 
 * @author sihai
 *
 */
public interface SchedulerMasterMetadataService {

	/**
	 * 订阅事件
	 * @param l
	 */
	void subscribe(Listener l);
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	boolean voteMaster(Scheduler s);
	
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

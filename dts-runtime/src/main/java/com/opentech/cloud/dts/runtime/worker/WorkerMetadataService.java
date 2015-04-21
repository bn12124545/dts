package com.opentech.cloud.dts.runtime.worker;

import com.opentech.cloud.dts.common.worker.Worker;

/**
 * Worker元数据服务
 * @author sihai
 *
 */
public interface WorkerMetadataService {

	/**
	 * 订阅事件
	 * @param l
	 */
	void subscribe(Listener l);
	
	/**
	 * worker加入
	 * @param worker
	 */
	void join(Worker worker);
	
	/**
	 * worker离开
	 * @param worker
	 */
	void leave(Worker worker);
	
	/**
	 * 获取所有的工作组
	 * @return
	 */
	String[] getAllWorkGroup();
	
	/**
	 * 获取指定组的所有worker
	 * @param group
	 * @return
	 */
	Worker[] getWorker(String group);
	
	
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

package com.opentech.cloud.dts.worker.task.executor;

import com.opentech.cloud.dts.common.task.Task;

/**
 * 
 * @author sihai
 *
 */
public interface TaskExecutor {

	/**
	 * 
	 * @author sihai
	 *
	 */
	interface Listener {
		
		/**
		 * 
		 * @param t
		 */
		void succeed(Task t);
		
		/**
		 * 
		 * @param t
		 * @param e
		 */
		void failed(Task t, Throwable e);
	}
	
	/**
	 * 
	 * @param t
	 */
	void execute(Task t, Listener l);
	
	/**
	 * 
	 */
	void shutdown();
}

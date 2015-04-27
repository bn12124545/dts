package com.opentech.cloud.dts.scheduler.plan;

import com.opentech.cloud.dts.common.task.plan.TaskExecutePlan;

/**
 * 
 * @author sihai
 *
 */
public interface PlanExecutor {

	//
	static interface Listener {
		
		/**
		 * 
		 * @param plan
		 * @param context
		 */
		void fired(TaskExecutePlan plan, Object context);
	}
	
	/**
	 * 
	 * @param plan
	 * @param context
	 */
	void execute(TaskExecutePlan plan, Object context);
}

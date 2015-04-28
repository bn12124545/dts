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
		void onTriggered(TaskExecutePlan plan, Object context);
		
		/**
		 * 
		 * @param plan
		 * @param context
		 */
		void onCompleted(TaskExecutePlan plan, Object context);
	}
	
	/**
	 * 判断调度计划是否已经完成
	 * @param plan
	 * @return
	 */
	boolean isCompeleted(TaskExecutePlan plan);
	
	/**
	 * 调度执行计划
	 * @param plan
	 * @param context
	 */
	void schedule(TaskExecutePlan plan, Object context);
}

package com.opentech.cloud.dts.common.task.plan;

import java.util.Date;

/**
 * 
 * @author sihai
 *
 */
public class TaskExecutePlanFactory {

	/**
	 * 
	 * @param startTime
	 * @param repeatCount
	 * @param repeatInterval
	 * @return
	 */
	public static final SimpleTaskExecutePlan simple(Date startTime, int repeatCount, int repeatInterval) {
		return simple(TaskPriority.TASK_PRIORITY_DEFAULT, startTime, repeatCount, repeatInterval);
	}
	
	/**
	 * 
	 * @param priority
	 * @param startTime
	 * @param repeatCount
	 * @param repeatInterval
	 * @return
	 */
	public static final SimpleTaskExecutePlan simple(TaskPriority priority, Date startTime, int repeatCount, int repeatInterval) {
		return new SimpleTaskExecutePlan(startTime, repeatCount, repeatInterval, priority);
	}
	
	/**
	 * 
	 * @param priority
	 * @param expression
	 * @return
	 */
	public static final CronTaskExecutePlan cron(String expression) {
		return cron(TaskPriority.TASK_PRIORITY_DEFAULT, expression);
	}
	
	/**
	 * 
	 * @param priority
	 * @param expression
	 * @return
	 */
	public static final CronTaskExecutePlan cron(TaskPriority priority, String expression) {
		return new CronTaskExecutePlan(expression, priority);
	}
}

package com.opentech.cloud.dts.common.task.plan;

/**
 * 任务优先级
 * @author sihai
 *
 */
public enum TaskPriority {
	
	/**
	 * LOW
	 */
	TASK_PRIORITY_LOW(0),
	
	/**
	 * DEFAULT
	 */
	TASK_PRIORITY_DEFAULT(5),
	
	/**
	 * HIGHT
	 */
	TASK_PRIORITY_HIGHT(10);
	
	/**
	 * 
	 */
	private final int value;

	private TaskPriority(int v) {
		this.value = v;
	}
	
	public int value() {
		return value;
	}
}

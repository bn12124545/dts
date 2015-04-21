package com.opentech.cloud.dts.common.task;

/**
 * 任务状态枚举
 * @author sihai
 *
 */
public enum TaskStatus {
	
	/**
	 * 待调度
	 */
	WAITIN_SCHEDULE,
	
	/**
	 * 已经被调度, 等待定时执行
	 */
	WAITIN_TRIGGERED,
	
	/**
	 * 定时已经触发, 等待worker执行
	 */
	READY,
	
	/**
	 * worker执行中
	 */
	EXECUTING,
	
	/**
	 * 执行完成
	 */
	EXECUTED,
	
	/**
	 * 任务完成
	 */
	COMPELETED,
	
	/**
	 * 失败
	 */
	FAILED,
}

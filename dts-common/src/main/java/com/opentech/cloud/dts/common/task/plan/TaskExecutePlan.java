package com.opentech.cloud.dts.common.task.plan;

import java.io.Serializable;

/**
 * 任务执行计划
 * @author sihai
 *
 */
public interface TaskExecutePlan extends Serializable {

	/**
	 * 获取优先级
	 * @return
	 */
	TaskPriority getPriority();
	
	/**
	 * 设置优先级
	 * @param priority
	 */
	void setPriority(TaskPriority priority);
}

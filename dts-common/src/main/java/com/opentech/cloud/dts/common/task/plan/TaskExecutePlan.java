package com.opentech.cloud.dts.common.task.plan;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

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
	
	/**
	 * 获取运行时信息
	 * @return
	 */
	Map<String, Object> getRuntime();
	
	/**
	 * 获取指定项目的运行时信息
	 * @param key
	 * @return
	 */
	Object getRuntimeItem(String key);
	
	/**
	 * 设定指定项目的运行时信息
	 * @param key
	 * @param value
	 */
	void setRuntimeItem(String key, Object value);
	
	/**
	 * 设置最后一次触发时间
	 * @param time
	 */
	void setLastTriggeredTime(Date time);
	
	/**
	 * 增加触发次数
	 */
	void incTriggeredTimes();
	
	/**
	 * 增加执行次数
	 */
	void incExecutedTimes();
	
	/**
	 * 增加成功执行次数
	 */
	void incSucceedExecutedTimes();
}

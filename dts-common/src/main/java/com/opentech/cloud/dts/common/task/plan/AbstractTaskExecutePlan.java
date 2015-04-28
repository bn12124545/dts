package com.opentech.cloud.dts.common.task.plan;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象任务执行计划
 * @author sihai
 *
 */
public class AbstractTaskExecutePlan implements TaskExecutePlan {

	public static final String TASK_EXECUTE_PLAN_RUNTIME_LAST_TRIGGERED_TIME = "lastTriggeredTime";						// 最后一次触发时间
	public static final String TASK_EXECUTE_PLAN_RUNTIME_TRIGGERED_TIMES = "triggeredTimes";							// 到目前为止总共触发多少次
	public static final String TASK_EXECUTE_PLAN_RUNTIME_EXECUTED_TIMES = "executedTimes";								// 到目前为止总共执行了多少次
	public static final String TASK_EXECUTE_PLAN_RUNTIME_SUCCEED_EXECUTED_TIMES = "succeedExecutedTimes";				// 到目前为止总共执行了多少次
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9119021545498225989L;
	
	/**
	 * 计划优先级
	 */
	protected TaskPriority priority;
	
	// 统计信息
	protected final Map<String, Object> runtime;

	public AbstractTaskExecutePlan() {
		this(TaskPriority.TASK_PRIORITY_DEFAULT);
	}
	
	public AbstractTaskExecutePlan(TaskPriority priority) {
		this.runtime = new HashMap<String, Object>();
		this.priority = priority;
	}
	
	@Override
	public TaskPriority getPriority() {
		return priority;
	}

	@Override
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
	public Map<String, Object> getRuntime() {
		return runtime;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getRuntimeItem(String key) {
		return this.runtime.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setRuntimeItem(String key, Object value) {
		this.runtime.put(key, value);
	}
	
	/**
	 * 
	 * @param time
	 */
	public void setLastTriggeredTime(Date time) {
		this.runtime.put(TASK_EXECUTE_PLAN_RUNTIME_LAST_TRIGGERED_TIME, time);
	}
	
	/**
	 * 
	 */
	public void incTriggeredTimes() {
		Long v = (Long)this.getRuntimeItem(TASK_EXECUTE_PLAN_RUNTIME_TRIGGERED_TIMES);
		v = null == v ? 1L : v + 1;
		this.setRuntimeItem(TASK_EXECUTE_PLAN_RUNTIME_TRIGGERED_TIMES, v);
	}
	
	/**
	 * 
	 */
	public void incExecutedTimes() {
		Long v = (Long)this.getRuntimeItem(TASK_EXECUTE_PLAN_RUNTIME_EXECUTED_TIMES);
		v = null == v ? 1L : v + 1;
		this.setRuntimeItem(TASK_EXECUTE_PLAN_RUNTIME_EXECUTED_TIMES, v);
	}
	
	/**
	 * 
	 */
	public void incSucceedExecutedTimes() {
		Long v = (Long)this.getRuntimeItem(TASK_EXECUTE_PLAN_RUNTIME_SUCCEED_EXECUTED_TIMES);
		v = null == v ? 1L : v + 1;
		this.setRuntimeItem(TASK_EXECUTE_PLAN_RUNTIME_SUCCEED_EXECUTED_TIMES, v);
	}
}

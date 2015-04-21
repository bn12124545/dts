package com.opentech.cloud.dts.common.task.plan;

/**
 * 抽象任务执行计划
 * @author sihai
 *
 */
public class AbstractTaskExecutePlan implements TaskExecutePlan {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9119021545498225989L;
	
	/**
	 * 计划优先级
	 */
	protected TaskPriority priority;
	
	public AbstractTaskExecutePlan() {
		this(TaskPriority.TASK_PRIORITY_DEFAULT);
	}
	
	public AbstractTaskExecutePlan(TaskPriority priority) {
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
}

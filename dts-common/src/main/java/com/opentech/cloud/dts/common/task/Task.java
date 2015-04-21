package com.opentech.cloud.dts.common.task;

import java.io.Serializable;
import java.util.Map;

import com.opentech.cloud.dts.common.task.plan.TaskExecutePlan;

/**
 * 
 * @author sihai
 *
 */
public class Task implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6575653957180760631L;

	/**
	 * task id
	 */
	private Long id;
	
	/**
	 * task name
	 */
	private String name;
	
	/**
	 * 任务所属分组
	 */
	private String group;
	
	/**
	 * 任务执行计划
	 */
	private TaskExecutePlan plan;
	
	/**
	 * 任务状态
	 */
	private TaskStatus status;

	/**
	 * 任务上下文
	 */
	private Map<String, Object> context;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public TaskExecutePlan getPlan() {
		return plan;
	}

	public void setPlan(TaskExecutePlan plan) {
		this.plan = plan;
	}
	
	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
}

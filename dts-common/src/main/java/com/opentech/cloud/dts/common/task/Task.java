package com.opentech.cloud.dts.common.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlan;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlanFactory;
import com.opentech.cloud.dts.common.task.plan.TaskPriority;

/**
 * 
 * @author sihai
 *
 */
public class Task implements Serializable {
	
	public static final String DEFAULT_GROUP = "default";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6575653957180760631L;

	/**
	 * task id
	 */
	private String id;
	
	/**
	 * task name
	 */
	private String name;
	
	/**
	 * 任务所属分组
	 */
	private String group = DEFAULT_GROUP;
	
	/**
	 * 任务执行计划
	 */
	private TaskExecutePlan plan;
	
	/**
	 * 任务状态
	 */
	private TaskStatus status = TaskStatus.WAITIN_SCHEDULE;

	/**
	 * 任务上下文
	 */
	private Map<String, Object> context;
	
	public Task() {
		this.id = UUID.randomUUID().toString();
		this.context = new HashMap<String, Object>();
	}

	public String getId() {
		return id;
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
	
	/**
	 * 生成key
	 */
	public String generateKey() {
		return String.format("%s@%s$%s", this.getName(), this.getGroup(), this.getId());
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Task)) {
			return false;
		}
		return StringUtils.equals(this.id, ((Task)obj).id);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Task fromJSON(String str) {
		Task t = new Task();
		JSONObject js = JSON.parseObject(str);
		
		t.id = js.getString("id");
		t.name = js.getString("name");
		t.group = js.getString("group");
		JSONObject jp = js.getJSONObject("plan");
		if(null != jp.getString("cron")) {
			t.plan = TaskExecutePlanFactory.cron(TaskPriority.valueOf(jp.getString("priority")), jp.getString("cron"));
		} else if(null != jp.getDate("startTime")) {
			t.plan = TaskExecutePlanFactory.simple(TaskPriority.valueOf(jp.getString("priority")), jp.getDate("startTime"), jp.getIntValue("repeatCount"), jp.getIntValue("repeatInterval"));
		}
		t.status = TaskStatus.valueOf(js.getString("status"));
		t.context = js.getObject("context", HashMap.class);
		return t;
	}
}

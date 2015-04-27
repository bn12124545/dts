package com.opentech.cloud.dts.common.worker;

import org.apache.commons.lang.StringUtils;

import com.opentech.cloud.dts.common.task.Task;

/**
 * Worker
 * @author sihai
 *
 */
public class Worker {
	
	/**
	 * scheduler ip
	 */
	private String ip;
	
	/**
	 * process id
	 */
	private String pid;
	
	/**
	 * workgroup
	 */
	private String group = Task.DEFAULT_GROUP;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * 生成key
	 */
	public String generateKey() {
		return String.format("%s.worker@%s$%s", this.getGroup(), this.getIp(), this.getPid());
	}

	@Override
	public int hashCode() {
		return this.generateKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Worker)) {
			return false;
		}
		return StringUtils.equals(this.generateKey(), ((Worker)obj).generateKey());
	}
}

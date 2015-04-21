package com.opentech.cloud.dts.common.worker;

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
	private String group;

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
}

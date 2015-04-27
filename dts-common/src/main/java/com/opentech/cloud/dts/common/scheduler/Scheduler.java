package com.opentech.cloud.dts.common.scheduler;

import org.apache.commons.lang.StringUtils;

/**
 * Scheduler调度
 * @author sihai
 *
 */
public class Scheduler {

	/**
	 * scheduler ip
	 */
	private String ip;
	
	/**
	 * process id
	 */
	private String pid;

	/**
	 * is master
	 */
	private boolean isMaster;

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

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	@Override
	public int hashCode() {
		return (this.ip + "$" + this.pid).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Scheduler)) {
			return false;
		}
		return StringUtils.equals(((Scheduler)obj).ip, this.ip) && StringUtils.equals(((Scheduler)obj).pid, this.pid);
	}
	
	/**
	 * 生成key
	 */
	public String generateKey() {
		return String.format("scheduler@%s$%s", this.getIp(), this.getPid());
	}
}

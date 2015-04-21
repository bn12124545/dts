package com.opentech.cloud.dts.common.scheduler;

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
}

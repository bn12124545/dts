package com.opentech.cloud.dts.scheduler;

/**
 * 
 * @author sihai
 *
 */
public interface Scheduler {
	
	/**
	 * 初始化
	 */
	void initialize();
	
	/**
	 * 启动
	 */
	void start();
	
	/**
	 * 停止
	 */
	void stop();
}

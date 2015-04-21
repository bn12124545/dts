package com.opentech.cloud.dts.worker;

/**
 * Worker
 * @author sihai
 *
 */
public interface Worker {
	
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

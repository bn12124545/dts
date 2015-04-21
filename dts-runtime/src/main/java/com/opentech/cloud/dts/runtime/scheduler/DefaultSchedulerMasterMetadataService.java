package com.opentech.cloud.dts.runtime.scheduler;

import com.opentech.cloud.dts.common.scheduler.Scheduler;
import com.opentech.cloud.dts.runtime.zookeeper.ZookeeperClient;

/**
 * 
 * @author sihai
 *
 */
public class DefaultSchedulerMasterMetadataService implements SchedulerMasterMetadataService {

	public static final String SCHEDULER_MASTER_ZOOKEEPER_NODE = "master";
	
	/**
	 * 
	 */
	private ZookeeperClient zkc;
	
	/**
	 * 
	 * @param zkc
	 */
	public DefaultSchedulerMasterMetadataService(ZookeeperClient zkc) {
		this.zkc = zkc;
	}
	
	/**
	 * 初始化
	 */
	public void initialize() {
		zkc.createPersistentNodeIfNotExists(SCHEDULER_MASTER_ZOOKEEPER_NODE);
	}
	
	@Override
	public void subscribe(Listener l) {
		// TODO
	}

	@Override
	public boolean voteMaster(Scheduler s) {
		return zkc.createEphemeralNode(String.format("%s/scheduler@%s$%s", SCHEDULER_MASTER_ZOOKEEPER_NODE, s.getIp(), s.getPid()));
	}
}

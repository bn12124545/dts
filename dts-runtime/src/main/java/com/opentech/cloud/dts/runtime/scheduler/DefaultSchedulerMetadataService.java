package com.opentech.cloud.dts.runtime.scheduler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.opentech.cloud.dts.common.scheduler.Scheduler;
import com.opentech.cloud.dts.runtime.zookeeper.ZookeeperClient;

/**
 * 
 * @author sihai
 *
 */
public class DefaultSchedulerMetadataService implements SchedulerMetadataService {

	public static final String SCHEDULER_ZOOKEEPER_NODE = "schedulers";
	
	/**
	 * 
	 */
	private ZookeeperClient zkc;
	
	/**
	 * 
	 * @param zkc
	 */
	public DefaultSchedulerMetadataService(ZookeeperClient zkc) {
		this.zkc = zkc;
	}
	
	/**
	 * 初始化
	 */
	public void initialize() {
		zkc.createPersistentNodeIfNotExists(SCHEDULER_ZOOKEEPER_NODE);
	}
	
	@Override
	public void subscribe(Listener l) {
		// TODO
	}

	@Override
	public void join(Scheduler scheduler) {
		zkc.createEphemeralNode(getSchedulerNodePath(scheduler), JSON.toJSONString(scheduler).getBytes());
	}

	@Override
	public void leave(Scheduler scheduler) {
		zkc.deleteNode(getSchedulerNodePath(scheduler));
	}

	@Override
	public Scheduler[] getAllScheduler() {
		int i = 0;
		List<String> chidren = zkc.getChidren(SCHEDULER_ZOOKEEPER_NODE);
		Scheduler[] ss = new Scheduler[chidren.size()];
		for(String child : chidren) {
			ss[i] = JSON.parseObject(new String(zkc.getNode(child)), Scheduler.class);
		}
		return ss;
	}

	/**
	 * 
	 * @param scheduler
	 * @return
	 */
	private String getSchedulerNodePath(Scheduler scheduler) {
		return String.format("%s/scheduler@%s$%s", SCHEDULER_ZOOKEEPER_NODE, scheduler.getIp(), scheduler.getPid());
	}
}

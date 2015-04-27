package com.opentech.cloud.dts.runtime.scheduler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public void subscribe(final Listener l) {
		// TODO
		zkc.registerChildrenListener(new com.opentech.cloud.dts.runtime.zookeeper.AbstractListener(SCHEDULER_ZOOKEEPER_NODE) {

			@Override
			public void onCreated() {
				// TODO Auto-generated method stub
				super.onCreated();
			}

			@Override
			public void onDeleted() {
				// TODO Auto-generated method stub
				super.onDeleted();
			}

			@Override
			public void onChildrenChanged() {
				// TODO Auto-generated method stub
				super.onChildrenChanged();
				l.fire(new Event());
			}
		});
	}

	@Override
	public long join(Scheduler scheduler) {
		return this.getSequence(zkc.createEphemeralSequenceNode(getSchedulerNodePath(scheduler), JSON.toJSONString(scheduler).getBytes()));
	}

	@Override
	public void leave(Scheduler scheduler) {
		zkc.deleteNode(getSchedulerNodePath(scheduler));
	}

	@Override
	public Set<Scheduler> getAllScheduler() {
		List<String> chidren = zkc.getChildren(SCHEDULER_ZOOKEEPER_NODE);
		Set<Scheduler> ss = new HashSet<Scheduler>(chidren.size());
		for(String child : chidren) {
			ss.add(JSON.parseObject(new String(zkc.getNode(SCHEDULER_ZOOKEEPER_NODE + "/" + child)), Scheduler.class));
		}
		return ss;
	}
	
	@Override
	public boolean voteMaster(long sequence) {
		
		List<String> children = this.zkc.getChildren(SCHEDULER_ZOOKEEPER_NODE);
		
		for(String child : children) {
			if(sequence > getSequence(child)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private long getSequence(String path) {
		String s = path.substring(path.indexOf(ZookeeperClient.SEQUENCE_SEPARATOR) + ZookeeperClient.SEQUENCE_SEPARATOR.length());
		return Long.valueOf(s);
	}

	/**
	 * 
	 * @param scheduler
	 * @return
	 */
	private String getSchedulerNodePath(Scheduler scheduler) {
		return String.format("%s/%s%s", SCHEDULER_ZOOKEEPER_NODE, scheduler.generateKey(), ZookeeperClient.SEQUENCE_SEPARATOR);
	}
}

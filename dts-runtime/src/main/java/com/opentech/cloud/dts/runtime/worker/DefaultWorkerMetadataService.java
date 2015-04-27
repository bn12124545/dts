package com.opentech.cloud.dts.runtime.worker;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.opentech.cloud.dts.common.worker.Worker;
import com.opentech.cloud.dts.runtime.zookeeper.ZookeeperClient;

/**
 * 
 * @author sihai
 *
 */
public class DefaultWorkerMetadataService implements WorkerMetadataService {

	public static final String ROOT_WORKER_ZOOKEEPER_NODE = "workers";
	
	/**
	 * 
	 */
	private ZookeeperClient zkc;
	
	/**
	 * 
	 * @param zkc
	 */
	public DefaultWorkerMetadataService(ZookeeperClient zkc) {
		this.zkc = zkc;
	}
	
	/**
	 * 初始化
	 */
	public void initialize() {
		zkc.createPersistentNodeIfNotExists(ROOT_WORKER_ZOOKEEPER_NODE);
	}
	
	@Override
	public void subscribe(Listener l) {
		// TODO
	}
	
	@Override
	public void join(Worker worker) {
		zkc.createPersistentNodeIfNotExists(getGroupNodePath(worker), null);
		zkc.createEphemeralNode(this.getWorkerNodePath(worker), JSON.toJSONString(worker).getBytes());
	}

	@Override
	public void leave(Worker worker) {
		zkc.deleteNode(this.getWorkerNodePath(worker));
	}

	@Override
	public String[] getAllWorkGroup() {
		List<String> chidren = zkc.getChildren(ROOT_WORKER_ZOOKEEPER_NODE);
		return chidren.toArray(new String[chidren.size()]);
	}

	@Override
	public Worker[] getWorker(String group) {
		int i = 0;
		List<String> chidren = zkc.getChildren(String.format("%s/%s", ROOT_WORKER_ZOOKEEPER_NODE, group));
		Worker[] ws = new Worker[chidren.size()];
		for(String child : chidren) {
			ws[i] = JSON.parseObject(new String(zkc.getNode(child)), Worker.class);
		}
		return ws;
	}
	
	/**
	 * 
	 * @param worker
	 * @return
	 */
	private String getGroupNodePath(Worker worker) {
		return String.format("%s/%s", ROOT_WORKER_ZOOKEEPER_NODE, worker.getGroup());
	}

	/**
	 * 
	 * @param worker
	 * @return
	 */
	private String getWorkerNodePath(Worker worker) {
		return String.format("%s/%s/%s", ROOT_WORKER_ZOOKEEPER_NODE, worker.getGroup(), worker.generateKey());
	}
}

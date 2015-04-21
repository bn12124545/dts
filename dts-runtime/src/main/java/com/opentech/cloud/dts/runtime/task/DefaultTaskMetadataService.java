package com.opentech.cloud.dts.runtime.task;

import com.alibaba.fastjson.JSON;
import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskStatus;
import com.opentech.cloud.dts.runtime.zookeeper.ZookeeperClient;

/**
 * 
 * @author sihai
 *
 */
public class DefaultTaskMetadataService implements TaskMetadataService {

	public static final String ROOT_TASK_ZOOKEEPER_NODE = "tasks";
	
	/**
	 * 
	 */
	private ZookeeperClient zkc;
	
	/**
	 * 
	 * @param zkc
	 */
	public DefaultTaskMetadataService(ZookeeperClient zkc) {
		this.zkc = zkc;
	}
	
	/**
	 * 初始化
	 */
	public void initialize() {
		zkc.createPersistentNodeIfNotExists(ROOT_TASK_ZOOKEEPER_NODE);
	}
	
	@Override
	public void subscribe(Listener l) {
		// TODO
	}
	
	@Override
	public void submit(Task task) {
		zkc.createPersistentNodeIfNotExists(getTaskNodePath(task), JSON.toJSONString(task).getBytes());
	}

	@Override
	public void transform(Task task, TaskStatus newStatus) {
		zkc.deleteNode(getTaskNodePath(task));
		task.setStatus(newStatus);
		zkc.createPersistentNodeIfNotExists(getTaskNodePath(task), JSON.toJSONString(task).getBytes());
	}

	@Override
	public void delete(Task task) {
		zkc.deleteNode(getTaskNodePath(task));
	}

	/**
	 * 
	 * @param task
	 * @return
	 */
	private String getTaskNodePath(Task task) {
		return String.format("%s/%s/%s/%s@%s$%d", ROOT_TASK_ZOOKEEPER_NODE, task.getGroup(), task.getStatus(), task.getName(), task.getGroup(), task.getId());
	}
}

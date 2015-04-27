package com.opentech.cloud.dts.runtime.task;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.opentech.cloud.dts.common.Environment;
import com.opentech.cloud.dts.common.scheduler.Scheduler;
import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskStatus;
import com.opentech.cloud.dts.runtime.zookeeper.AbstractListener;
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
		this.zkc.createPersistentNodeIfNotExists(ROOT_TASK_ZOOKEEPER_NODE);
		this.zkc.createPersistentNodeIfNotExists(String.format("%s/%s", ROOT_TASK_ZOOKEEPER_NODE, Task.DEFAULT_GROUP));
	}
	
	@Override
	public void subscribeGroup(final Listener l) {
		this.zkc.registerChildrenListener(new AbstractListener(ROOT_TASK_ZOOKEEPER_NODE) {

			@Override
			public void onChildrenChanged() {
				super.onChildrenChanged();
				l.fire(new Event());
			}
			
		});
	}
	
	@Override
	public void subscribeSchedulerOnGroupTask(String g, Scheduler s, final Listener l) {
		this.zkc.registerChildrenListener(new AbstractListener(String.format("%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, g, s.generateKey(), TaskStatus.WAITIN_SCHEDULE)) {

			@Override
			public void onChildrenChanged() {
				super.onChildrenChanged();
				l.fire(new Event());
			}
			
		});
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
	
	@Override
	public List<String> getAllGroup() {
		return zkc.getChildren(ROOT_TASK_ZOOKEEPER_NODE);
	}
	
	@Override
	public List<Task> getTasks(String group, Scheduler scheduler, TaskStatus status) {
		List<String> children = this.zkc.getChildren(String.format("%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler.generateKey(), status.name()));
		List<Task> tlist = new ArrayList<Task>();
		try {
			for(String s : children) {
				tlist.add(JSON.parseObject(new String(this.zkc.getNode(String.format("%s/%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler.generateKey(), status.name(), s)), Environment.CHARSET), Task.class));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return tlist;
	}
	
	@Override
	public boolean deleteTasks(String group, Scheduler scheduler, TaskStatus status) {
		return this.zkc.deleteNodeRecursive(String.format("%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler.generateKey(), status.name()));
	}

	@Override
	public boolean isSchedulerOnGroup(Scheduler scheduler, String group) {
		return this.zkc.exists(String.format("%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler.generateKey()));
	}

	@Override
	public void scheduleOnGroup(Scheduler scheduler, String group) {
		String path = String.format("%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler.generateKey());
		this.zkc.createPersistentNodeIfNotExists(path);
		for(TaskStatus ts : TaskStatus.values()) {
			this.zkc.createPersistentNodeIfNotExists(path + "/" + ts.name());
		}
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

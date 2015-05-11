package com.opentech.cloud.dts.runtime.task;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.opentech.cloud.dts.common.Environment;
import com.opentech.cloud.dts.common.scheduler.Scheduler;
import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskStatus;
import com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService;
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
	 */
	private SchedulerMetadataService sms;
	
	/**
	 * 
	 * @param zkc
	 */
	public DefaultTaskMetadataService(ZookeeperClient zkc, SchedulerMetadataService sms) {
		this.zkc = zkc;
		this.sms = sms;
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
	public void subscribeTaskOnGroupScheduler(String g, Scheduler s, TaskStatus ts, final Listener l) {
		this.subscribeTaskOnGroupScheduler(g, s.generateKey(), ts, l);
	}
	
	@Override
	public void subscribeTaskOnGroupScheduler(String g, String s, TaskStatus ts, final Listener l) {
		this.zkc.registerChildrenListener(new AbstractListener(String.format("%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, g, s, ts)) {

			@Override
			public void onChildrenChanged() {
				super.onChildrenChanged();
				l.fire(new Event());
			}
			
		});
	}
	
	@Override
	public void schedule(Task t) {
		zkc.createPersistentNodeIfNotExists(getTaskNodePath(t), JSON.toJSONString(t).getBytes());
	}

	@Override
	public void transform(Task task, TaskStatus newStatus) {
		zkc.deleteNode(getTaskNodePath(task));
		task.setStatus(newStatus);
		zkc.createPersistentNodeIfNotExists(getTaskNodePath(task), JSON.toJSONString(task).getBytes());
	}

	@Override
	public boolean delete(Task task) {
		return zkc.deleteNode(getTaskNodePath(task));
	}
	
	@Override
	public List<String> getAllGroup() {
		return zkc.getChildren(ROOT_TASK_ZOOKEEPER_NODE);
	}
	
	@Override
	public List<String> getAllSchedulers(String group) {
		return this.zkc.getChildren(this.getGroupNodePath(group));
	}
	
	@Override
	public List<Task> getTasks(String group, Scheduler scheduler, TaskStatus status) {
		return this.getTasks(group, scheduler.generateKey(), status);
	}
	
	@Override
	public List<Task> getTasks(String group, String scheduler, TaskStatus status) {
		List<String> children = this.zkc.getChildren(String.format("%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler, status.name()));
		List<Task> tlist = new ArrayList<Task>();
		try {
			for(String s : children) {
				tlist.add(Task.fromJSON(new String(this.zkc.getNode(String.format("%s/%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group, scheduler, status.name(), s)), Environment.CHARSET)));
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
	 * @param group
	 * @return
	 */
	private String getGroupNodePath(String group) {
		return String.format("%s/%s", ROOT_TASK_ZOOKEEPER_NODE, group);
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	private String getTaskNodePath(Task t) {
		Set<Scheduler> ss = this.sms.getAllScheduler();
		if(ss.isEmpty()) {
			//throw new TaskException(ErrorCode.NONE_SCHEDULER_AVAILABLE, "no scheduler available");
			throw new RuntimeException("no scheduler available");
		}
		Scheduler[] sa = ss.toArray(new Scheduler[ss.size()]);
		Scheduler s = sa[t.hashCode() % ss.size()];
		return String.format("%s/%s/%s/%s/%s", ROOT_TASK_ZOOKEEPER_NODE, t.getGroup(), s.generateKey(), t.getStatus().name(), t.generateKey());
	}
}

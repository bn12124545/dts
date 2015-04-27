package com.opentech.cloud.dts.runtime.zookeeper;

/**
 * 
 * @author sihai
 *
 */
public interface Listener {

	/**
	 * 要监听的节点路径å
	 * @return
	 */
	String getPath();
	
	/**
	 * 节点被创建
	 */
	void onCreated();
	
	/**
	 * 节点被删除 
	 */
	void onDeleted();

	/**
	 * 节点数据变化
	 */
	void onDataChanged();
	
	/**
	 * 子节点变化
	 */
	void onChildrenChanged();
}

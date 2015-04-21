package com.opentech.cloud.dts.runtime.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 
 * @author sihai
 *
 */
public class ZookeeperClient {

	/**
	 * 
	 */
	private String root;
	
	/**
	 * 
	 */
	private ZooKeeper zk;
	
	/**
	 * 
	 * @param connectString
	 * @param sessionTimeout
	 * @param root
	 */
	public ZookeeperClient(String connectString, int sessionTimeout, String root) {
		try {
			this.zk = new ZooKeeper(connectString, sessionTimeout, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
					String path = event.getPath();
				};
			
			});
			this.root = root;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 初始化
	 */
	public void initialize() {
		this._createPersistentNodeIfNotExists(this.root, null);
	}
	
	/**
	 * PERSISTENT
	 * @param path
	 */
	public void createPersistentNodeIfNotExists(String path) {
		this.createPersistentNodeIfNotExists(path, null); 
	}

	/**
	 * 
	 * @param path
	 * @param data
	 */
	public void createPersistentNodeIfNotExists(String path, byte[] data) {
		this._createPersistentNodeIfNotExists(this.getFullPath(path), data);
	}
	
	/**
	 * 
	 * @param path
	 */
	public boolean createEphemeralNode(String path) {
		return this.createEphemeralNode(path, null);
	}

	/**
	 * 
	 * @param path
	 * @param data
	 */
	public boolean createEphemeralNode(String path, byte[] data) {
		try {
			zk.create(getFullPath(path), data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			return true;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			throw new RuntimeException(e);
		}
		//return false;
	}
	
	/**
	 * 
	 * @param path
	 */
	public void deleteNode(String path) {
		try {
			zk.delete(path, -1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getNode(String path) {
		try {
			return zk.getData(path, false, new Stat());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public List<String> getChidren(String path) {
		try {
			return zk.getChildren(path, false);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String getFullPath(String path) {
		StringBuilder sb = new StringBuilder(root);
		if(!path.startsWith("/")) {
			sb.append("/");
		}
		sb.append(path);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param fullPath
	 * @param data
	 */
	private void _createPersistentNodeIfNotExists(String fullPath, byte[] data) {
		try {
			if(null == zk.exists(fullPath, false)) {
				zk.create(fullPath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			throw new RuntimeException(e);
		}
	}
}

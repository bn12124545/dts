package com.opentech.cloud.dts.runtime.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 
 * @author sihai
 *
 */
public class ZookeeperClient {

	public static final String SEQUENCE_SEPARATOR = "#";
	
	private static final Log logger = LogFactory.getLog(ZookeeperClient.class);
	
	/**
	 * 
	 */
	private String root;

	/**
	 * 
	 */
	private ZooKeeper zk;
	
	/**
	 * 注册的监听器
	 */
	private ConcurrentHashMap<String, Listener> listeners;
	
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
					
					logger.warn("Zookeeper event: " + event);
					
					if(KeeperState.Disconnected == event.getState()) {
						// TODO
						return;
					}
					
					if(KeeperState.SyncConnected == event.getState()) {
						return;
					}
					
					String path = event.getPath();
					EventType type = event.getType();
					Listener l = ZookeeperClient.this.listeners.get(path);
					
					if(null == l) {
						return;
					}
					
					if(EventType.NodeCreated == type) {
						
					} else if(EventType.NodeDeleted == type) {
						l.onDeleted();
					} else if(EventType.NodeDataChanged == type)	{
						l.onDataChanged();
					} else if(EventType.NodeChildrenChanged == type) {
						l.onChildrenChanged();
					} else {
						
					}
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
		this.listeners = new ConcurrentHashMap<String, Listener>();
		this._createPersistentNodeIfNotExists(this.root, null);
	}
	
	public String getRoot() {
		return root;
	}
	
	/**
	 * 
	 * @param l
	 */
	public void registerChildrenListener(final Listener l) {
		String path = this.getFullPath(l.getPath());
		//this.listeners.put(path, l);
		try {
			zk.getChildren(path, new Watcher() {

				@Override
				public void process(WatchedEvent event) {
					if(EventType.NodeChildrenChanged == event.getType()) {
						l.onChildrenChanged();
					}
				}
				
			});
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			throw new RuntimeException(e);
		}
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
	 * @return
	 */
	public String createEphemeralSequenceNode(String path) {
		return this.createEphemeralSequenceNode(path, null);
	}
	
	/**
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	public String createEphemeralSequenceNode(String path, byte[] data) {
		try {
			return this.zk.create(getFullPath(path), data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
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
	 */
	public boolean deleteNode(String path) {
		try {
			zk.delete(this.getFullPath(path), -1);
			return true;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (KeeperException e) {
			if(e instanceof NoNodeException) {
				return false;
			}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteNodeRecursive(String path) {
		List<String> children = this.getChildren(path);
		for(String s : children) {
			this.deleteNode(path + "/" + s);
		}
		return this.deleteNode(path);
	}
	
	public byte[] getNode(String path) {
		try {
			return zk.getData(this.getFullPath(path), false, new Stat());
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
	public List<String> getChildren(String path) {
		try {
			return zk.getChildren(this.getFullPath(path), false);
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
	public boolean exists(String path) {
		try {
			return null != this.zk.exists(this.getFullPath(path), false);
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

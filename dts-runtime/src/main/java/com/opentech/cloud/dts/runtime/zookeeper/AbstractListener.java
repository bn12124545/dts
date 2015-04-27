package com.opentech.cloud.dts.runtime.zookeeper;

/**
 * 
 * @author sihai
 *
 */
public class AbstractListener implements Listener {

	/**
	 * 
	 */
	protected String path;
	
	/**
	 * 
	 * @param path
	 */
	public AbstractListener(String path) {
		this.path = path;
	}
	
	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void onCreated() {

	}

	@Override
	public void onDeleted() {

	}

	@Override
	public void onDataChanged() {

	}

	@Override
	public void onChildrenChanged() {

	}
}

package com.opentech.cloud.dts.runtime;

import com.opentech.cloud.dts.runtime.scheduler.DefaultSchedulerMetadataService;
import com.opentech.cloud.dts.runtime.scheduler.SchedulerMetadataService;
import com.opentech.cloud.dts.runtime.task.DefaultTaskMetadataService;
import com.opentech.cloud.dts.runtime.task.TaskMetadataService;
import com.opentech.cloud.dts.runtime.worker.DefaultWorkerMetadataService;
import com.opentech.cloud.dts.runtime.worker.WorkerMetadataService;
import com.opentech.cloud.dts.runtime.zookeeper.ZookeeperClient;


/**
 * 运行时元数据服务
 * @author sihai
 *
 */
public class DefaultRuntimeMetadataService implements RuntimeMetadataService {
	
	public static final String DTS_ZOOKEEPER_ROOT_NODE = "/dts@opentech.com";
	
	public static final int DEFAULT_ZOOKEEPER_SESSION_TIMEOUT = 60 * 1000;
	
	private ZookeeperClient zkc;
	
	/**
	 * 
	 */
	private SchedulerMetadataService sms;
	
	/**
	 * 
	 */
	private WorkerMetadataService wms;
	
	/**
	 * 
	 */
	private TaskMetadataService tms;
	
	/**
	 * 
	 * @param zkAddress
	 */
	public DefaultRuntimeMetadataService(String zkAddress) {
		this.zkc = new ZookeeperClient(zkAddress, DEFAULT_ZOOKEEPER_SESSION_TIMEOUT, DTS_ZOOKEEPER_ROOT_NODE);
	}
	
	/**
	 * 初始化
	 */
	public void initialize() {
		this.zkc.initialize();
		this.sms = new DefaultSchedulerMetadataService(zkc);
		((DefaultSchedulerMetadataService)this.sms).initialize();
		this.wms = new DefaultWorkerMetadataService(zkc);
		((DefaultWorkerMetadataService)this.wms).initialize();
		this.tms = new DefaultTaskMetadataService(zkc);
		((DefaultTaskMetadataService)this.tms).initialize();
	}
	
	@Override
	public SchedulerMetadataService getSchedulerMetadataService() {
		return sms;
	}
	
	@Override
	public WorkerMetadataService getWorkerMetadataService() {
		return wms;
	}
	
	@Override
	public TaskMetadataService getTaskMetadataService() {
		return tms;
	}
}

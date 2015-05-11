package com.opentech.cloud.dts.integrated.test;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opentech.cloud.dts.api.task.TaskService;
import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskException;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlanFactory;
import com.opentech.cloud.dts.runtime.DefaultRuntimeMetadataService;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;
import com.opentech.cloud.dts.scheduler.DefaultScheduler;
import com.opentech.cloud.dts.scheduler.Scheduler;
import com.opentech.cloud.dts.worker.DefaultWorker;
import com.opentech.cloud.dts.worker.Worker;

/**
 * 功能集成测试case
 * @author sihai
 *
 */
public class FunctionTest {

	private RuntimeMetadataService rms;
	
	private Scheduler scheduler;
	
	private Worker worker;
	
	private TaskService ts;
	
	private Thread tg;
	
	@Before
	public void setup() {
		this.rms = new DefaultRuntimeMetadataService("127.0.0.1:2181");
		((DefaultRuntimeMetadataService)this.rms).initialize();
	}
	
	@After
	public void stop() {
		this.stopScheduler();
		this.stopWorker();
		this.stopTaskGenerator();
	}
	
	/**
	 * 
	 */
	private void startScheduler() {
		DefaultScheduler ds = new DefaultScheduler(this.rms);
		ds.initialize();
		ds.start();
		this.scheduler = ds;
	}
	
	/**
	 * 
	 */
	private void stopScheduler() {
		if(null != this.scheduler) {
			this.scheduler.stop();
		}
	}
	
	/**
	 * 
	 */
	private void startWorker() {
		DefaultWorker dw = new DefaultWorker(Task.DEFAULT_GROUP, this.rms);
		dw.initialize();
		dw.start();
		this.worker = dw;
	}
	
	/**
	 * 
	 */
	private void stopWorker() {
		if(null != this.worker) {
			this.worker.stop();
		}
	}
	
	/**
	 * 
	 */
	private void startTaskGenerator() {
		this.tg = new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 0;
				while(!Thread.interrupted()) {
					Task task = new Task();
					task.setName("task_" + i++);
					task.setPlan(TaskExecutePlanFactory.simple(new Date(), 10, 60));
					task.setGroup(Task.DEFAULT_GROUP);
					try {
						FunctionTest.this.ts.schedule(task);
					} catch (TaskException e) {
						throw new RuntimeException(e);
					}
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
			}
			
		});
		this.tg.start();
	}
	
	/**
	 * 
	 */
	private void stopTaskGenerator() {
		if(null != this.tg) {
			this.tg.interrupt();
		}
	}
	
	@Test
	public void test() throws Exception {
		this.startScheduler();
		this.startWorker();
		this.startTaskGenerator();
	
		Thread.sleep(1000 * 60 * 30);
	}
}

package com.opentech.cloud.dts.worker.task.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.thread.DTSThreadFactory;

/**
 * 
 * @author sihai
 *
 */
public class DefaultTaskExecutor implements TaskExecutor {

	private static final Log logger = LogFactory.getLog(DefaultTaskExecutor.class);
	
	/**
	 * 
	 */
	private ThreadPoolExecutor threadpool;
	
	/**
	 * 
	 */
	private BlockingQueue<Runnable> workqueue;
	
	/**
	 * 
	 * @param coreThreadSize
	 * @param maxThreadSize
	 * @param keepAlive
	 * @param queueCapacity
	 */
	public DefaultTaskExecutor(int coreThreadSize, int maxThreadSize, int keepAlive, int queueCapacity) {
		this.workqueue = new ArrayBlockingQueue<Runnable>(queueCapacity);
		this.threadpool = new ThreadPoolExecutor(coreThreadSize, maxThreadSize, keepAlive, TimeUnit.SECONDS, workqueue, new DTSThreadFactory("dts.worker.threadpool"));
	}
	
	@Override
	public void execute(final Task t, final Listener l) {
		
		while(!Thread.currentThread().isInterrupted()) {
			try {
				this.threadpool.execute(new Runnable() {
		
					@Override
					public void run() {
						// TODO
						l.succeed(t);
					}
					
				});
				break;
			} catch (RejectedExecutionException e) {
				logger.warn("DefaultTaskExecutor.threadpool is busy");
				try {
					Thread.sleep(1);
				} catch (InterruptedException x) {
					logger.warn("Interrupted", x);
					Thread.currentThread().interrupt();
				}
				//LockSupport.park();
			}
		}
	}

	@Override
	public void shutdown() {
		if(null != this.threadpool) {
			this.threadpool.shutdown();
		}
		if(null != this.workqueue) {
			this.workqueue.clear();
		}
	}
}

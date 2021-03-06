package com.opentech.cloud.dts.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author sihai
 *
 */
public class DTSThreadFactory implements ThreadFactory {

	private final String prefix;		// 线程名前缀
	private ThreadGroup group;			// 线程组
	private boolean  isDaemon;			// 是否设置为精灵线程
	final AtomicInteger tNo;			// 线程编号, 线程名的一部分
	
	public DTSThreadFactory(String prefix) {
		this(prefix, null, false);
	}
	
	public DTSThreadFactory(String prefix, ThreadGroup group, boolean isDaemon) {
		tNo = new AtomicInteger(0);
		this.prefix = prefix;
		if(null != group) {
			this.group = group;
		} else {
			SecurityManager sm = System.getSecurityManager();
			group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
		}
		this.isDaemon = isDaemon;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, new StringBuilder(prefix).append(".thread-").append(tNo.getAndIncrement()).toString());
		t.setDaemon(isDaemon);
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

}

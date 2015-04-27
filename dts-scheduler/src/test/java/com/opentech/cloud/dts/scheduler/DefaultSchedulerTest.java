package com.opentech.cloud.dts.scheduler;

import org.junit.Before;
import org.junit.Test;

import com.opentech.cloud.dts.runtime.DefaultRuntimeMetadataService;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;

/**
 * 
 * @author sihai
 *
 */
public class DefaultSchedulerTest {

	private RuntimeMetadataService rms;
	
	@Before
	public void setup() {
		this.rms = new DefaultRuntimeMetadataService("127.0.0.1:2181");
		((DefaultRuntimeMetadataService)this.rms).initialize();
	}
	
	@Test
	public void test() throws Exception {
		
		DefaultScheduler ds = new DefaultScheduler(this.rms);
		ds.initialize();
		ds.start();
		
		Thread.sleep(120 * 1000);
		
		ds.stop();
	}

}

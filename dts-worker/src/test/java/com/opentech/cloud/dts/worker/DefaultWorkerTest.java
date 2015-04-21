package com.opentech.cloud.dts.worker;

import org.junit.Before;
import org.junit.Test;

import com.opentech.cloud.dts.runtime.DefaultRuntimeMetadataService;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;

/**
 * 
 * @author sihai
 *
 */
public class DefaultWorkerTest {

	private RuntimeMetadataService rms;
	
	@Before
	public void setup() {
		this.rms = new DefaultRuntimeMetadataService("127.0.0.1:2121");
		((DefaultRuntimeMetadataService)this.rms).initialize();
	}
	
	@Test
	public void test() throws Exception {
		DefaultWorker ds = new DefaultWorker("default", this.rms);
		ds.initialize();
		ds.start();
		
		Thread.sleep(120 * 1000);
		
		ds.stop();
	}

}

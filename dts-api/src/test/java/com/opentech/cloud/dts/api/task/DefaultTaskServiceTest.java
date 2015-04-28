package com.opentech.cloud.dts.api.task;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlanFactory;
import com.opentech.cloud.dts.runtime.DefaultRuntimeMetadataService;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;

public class DefaultTaskServiceTest {

	private RuntimeMetadataService rms;
	
	@Before
	public void setup() {
		this.rms = new DefaultRuntimeMetadataService("127.0.0.1:2181");
		((DefaultRuntimeMetadataService)this.rms).initialize();
	}
	
	@Test
	public void test() throws Exception {
		
		DefaultTaskService dts = new DefaultTaskService(this.rms);
		
		Task task = new Task();
		task.setName("task_0");
		task.setPlan(TaskExecutePlanFactory.simple(new Date(), 10, 60));
		task.setGroup(Task.DEFAULT_GROUP);
		dts.schedule(task);
		
		Thread.sleep(360 * 1000);
	}
}

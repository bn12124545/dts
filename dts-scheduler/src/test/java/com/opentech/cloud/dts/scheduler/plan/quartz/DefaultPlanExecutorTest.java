package com.opentech.cloud.dts.scheduler.plan.quartz;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opentech.cloud.dts.common.task.plan.TaskExecutePlan;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlanFactory;
import com.opentech.cloud.dts.scheduler.plan.PlanExecutor;

public class DefaultPlanExecutorTest {

	private DefaultPlanExecutor dpe;
	
	@Before
	public void setup() {
		this.dpe = new DefaultPlanExecutor(new PlanExecutor.Listener() {
			
			@Override
			public void fired(TaskExecutePlan plan, Object context) {
				System.out.println(plan);
				System.out.println(context);
			}
		});
		this.dpe.start();
	}
	
	@After 
	public void after() {
		this.dpe.stop();
	}
	
	@Test
	public void test() throws Exception {
		
		//this.dpe.execute(TaskExecutePlanFactory.simple(new Date(), 10, 60), "1234");
		this.dpe.execute(TaskExecutePlanFactory.cron("*/30 * * * * ?"), "1234");
		Thread.sleep(360 * 1000);
	}
}

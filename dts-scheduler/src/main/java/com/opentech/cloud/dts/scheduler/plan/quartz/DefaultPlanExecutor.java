package com.opentech.cloud.dts.scheduler.plan.quartz;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.opentech.cloud.dts.common.task.plan.CronTaskExecutePlan;
import com.opentech.cloud.dts.common.task.plan.SimpleTaskExecutePlan;
import com.opentech.cloud.dts.common.task.plan.TaskExecutePlan;
import com.opentech.cloud.dts.scheduler.plan.PlanExecutor;

/**
 * 
 * @author sihai
 *
 */
public class DefaultPlanExecutor implements PlanExecutor {
	
	public static final String JOB_DATA_KEY_EXECUTOR = "executor";
	public static final String JOB_DATA_KEY_EXECUTE_PLAN = "plan";
	public static final String JOB_DATA_KEY_CONTEXT = "listener";
	public static final String QUARTZ_GROUP_NAME = "dts-quartz-group";
	
	private static final Log logger = LogFactory.getLog(DefaultPlanExecutor.class);
	
	private Listener listener;
	
	private Scheduler scheduler;	
	
	private AtomicLong idGenerator;
	
	/**
	 * 
	 * @param listener
	 */
	public DefaultPlanExecutor(Listener listener) {
		try {
			this.listener = listener;
			this.scheduler = new StdSchedulerFactory().getScheduler();
			this.idGenerator = new AtomicLong(0);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	public void start() {
		try {
			this.scheduler.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		try {
			this.scheduler.shutdown();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void execute(TaskExecutePlan plan, Object context) {
		TriggerKey triggerKey = makeQuartzTriggerKey(plan);
		JobKey jobKey = makeQuartzJobKey(plan);
		Trigger trigger = null;
		JobDetail qJob = JobBuilder.newJob(QuartzJob.class).withIdentity(jobKey).build();
		if(plan instanceof SimpleTaskExecutePlan) {
			SimpleTaskExecutePlan splan = (SimpleTaskExecutePlan)plan;
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerKey)
					.withPriority(plan.getPriority().value())
					.startAt(splan.getStartTime())
					.withSchedule(SimpleScheduleBuilder.simpleSchedule()
										.withIntervalInSeconds(splan.getRepeatInterval())
										.withRepeatCount(splan.getRepeatCount())
										.withMisfireHandlingInstructionFireNow())
					.build();
		} else if(plan instanceof CronTaskExecutePlan) {
			CronTaskExecutePlan cplan = (CronTaskExecutePlan)plan;
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerKey)
					.withPriority(plan.getPriority().value())
					.withSchedule(CronScheduleBuilder.cronSchedule(cplan.getCron())
					.withMisfireHandlingInstructionFireAndProceed())
					.build();
		} else {
			throw new IllegalArgumentException("Unsupported plan type: " + plan.getClass().getName());
		}
		
		try {
			qJob.getJobDataMap().put(JOB_DATA_KEY_EXECUTOR, this);
			qJob.getJobDataMap().put(JOB_DATA_KEY_EXECUTE_PLAN, plan);
			qJob.getJobDataMap().put(JOB_DATA_KEY_CONTEXT, context);
			scheduler.scheduleJob(qJob, trigger);
		} catch(SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private JobKey makeQuartzJobKey(TaskExecutePlan plan) {
		return new JobKey("quartz_job_" + String.valueOf(this.nextId()), QUARTZ_GROUP_NAME);
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private TriggerKey makeQuartzTriggerKey(TaskExecutePlan plan) {
		return new TriggerKey("quartz_trigger_" + String.valueOf(this.nextId()), QUARTZ_GROUP_NAME);
	}
	
	/**
	 * 
	 * @return
	 */
	private Long nextId() {
		return this.idGenerator.getAndIncrement();
	}
	
	/**
	 * 
	 * @param plan
	 * @param context
	 */
	private void fired(TaskExecutePlan plan, Object context) {
		this.listener.fired(plan, context);
	}
	
	/**
	 * 
	 * @author sihai
	 *
	 */
	public static class QuartzJob implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobDataMap data = context.getJobDetail().getJobDataMap();
			DefaultPlanExecutor dpe = (DefaultPlanExecutor)data.get(JOB_DATA_KEY_EXECUTOR);
			TaskExecutePlan plan = (TaskExecutePlan)data.get(JOB_DATA_KEY_EXECUTE_PLAN);
			Object c = data.get(JOB_DATA_KEY_CONTEXT);
			dpe.fired(plan, c);
		}
	}
}

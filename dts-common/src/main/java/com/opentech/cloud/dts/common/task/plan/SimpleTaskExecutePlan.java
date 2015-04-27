package com.opentech.cloud.dts.common.task.plan;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author sihai
 *
 */
public class SimpleTaskExecutePlan extends AbstractTaskExecutePlan {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8972075487406584685L;

	/**
	 * 任务首次执行时间
	 */
	private Date startTime;
	
	/**
	 * 重复执行次数
	 */
	private int  repeatCount;
	
	/**
	 * 重复执行时间间隔, 单位秒
	 */
	private int repeatInterval;
	
	/**
	 * 创建简单执行计划
	 * @param startTime
	 * @param repeatCount
	 * @param repeatInterval
	 */
	public SimpleTaskExecutePlan(Date startTime, int repeatCount, int repeatInterval) {
		this(startTime, repeatCount, repeatInterval, TaskPriority.TASK_PRIORITY_DEFAULT);
	}
	
	/**
	 * 创建简单执行计划
	 * @param startTime			首次执行时间
	 * @param repeatCount		重复执行次数
	 * @param repeatInterval	重复时间间隔
	 * @param priority			优先级
	 */
	public SimpleTaskExecutePlan(Date startTime, int repeatCount, int repeatInterval, TaskPriority priority) {
		super(priority);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		//calendar.set(Calendar.MILLISECOND, 0);
		//calendar.set(Calendar.SECOND, 0);
		//calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
		this.startTime = calendar.getTime();
		this.repeatCount = repeatCount;
		this.repeatInterval = repeatInterval;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public int getRepeatInterval() {
		return repeatInterval;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + repeatCount;
		result = prime * result + repeatInterval;
		result = prime * result + priority.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		
		SimpleTaskExecutePlan p = (SimpleTaskExecutePlan) obj;
		if(startTime == null) {
			if(p.startTime != null) {
				return false;
			}
		}else if(!startTime.equals(p.startTime)) {
			return false;
		}
		
		if (repeatCount != p.repeatCount || repeatInterval != p.repeatInterval) {
			return false;
		}
		
		if(priority != p.priority) {
			return false;
		}

		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("simple: ");
		sb.append(startTime);
		sb.append(",");
		sb.append(repeatCount);
		sb.append(",");
		sb.append(repeatInterval);
		sb.append(",");
		sb.append(priority);
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		TaskExecutePlan plan0 = new SimpleTaskExecutePlan(new Date(), 100, 120);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		TaskExecutePlan plan1 = new SimpleTaskExecutePlan(new Date(), 100, 120);
		System.out.println(plan0.hashCode() == plan1.hashCode());
		System.out.println(plan0.equals(plan1));
		
		
		System.out.println(1332146296856L | 0xFFF);
		System.out.println(String.format("%x", 1332146296856L | 0xFFF));
		System.out.println(String.format("%x", 1332146296856L | 0xFFF));
		
		System.out.println(String.format("%x", 0xFFF));
		
		
		Map<TaskExecutePlan, Object> map = new HashMap<TaskExecutePlan, Object>();
		
		for(int i = 0 ; i < 1024; i++) {
			TaskExecutePlan plan = new SimpleTaskExecutePlan(new Date(), 100, 120);
			if(!map.containsKey(plan)) {
				map.put(plan, new Object());
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("map.size = " + map.size());
	}
}

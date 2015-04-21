package com.opentech.cloud.dts.common.task.plan;

/**
 * 
 * @author sihai
 *
 */
public class CronTaskExecutePlan extends AbstractTaskExecutePlan {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 629817490110828221L;
	
	private String cron;			// cron表达式
	
	public CronTaskExecutePlan(String cron) {
		this(cron, TaskPriority.TASK_PRIORITY_DEFAULT);
	}
	
	public CronTaskExecutePlan(String cron, TaskPriority priority) {
		super(priority);
		this.cron = cron;
	}
	
	public void setCron(String cron) {
		this.cron = cron;
	}
	
	public String getCron() {
		return cron;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cron == null) ? 0 : cron.hashCode());
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
		
		CronTaskExecutePlan p = (CronTaskExecutePlan) obj;
		if(cron == null) {
			if(p.cron != null) {
				return false;
			}
		}else if(!cron.equals(p.cron)) {
			return false;
		}
		
		if(priority != p.priority) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("cron,");
		sb.append(cron);
		sb.append(",");
		sb.append(priority);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		TaskExecutePlan plan0 = new CronTaskExecutePlan(new StringBuilder("0 */").append(1).append(" * * * ? *").toString());
		TaskExecutePlan plan1 = new CronTaskExecutePlan(new StringBuilder("0 */").append(1).append(" * * * ? *").toString());
		
		System.out.println(plan0.hashCode() == plan1.hashCode());
		System.out.println(plan0.equals(plan1));
	}
}

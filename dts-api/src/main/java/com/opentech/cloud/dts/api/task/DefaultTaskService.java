package com.opentech.cloud.dts.api.task;

import java.util.List;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskException;
import com.opentech.cloud.dts.runtime.RuntimeMetadataService;

/**
 * 
 * @author sihai
 *
 */
public class DefaultTaskService implements TaskService {

	/**
	 * 
	 */
	private RuntimeMetadataService rms;
	
	/**
	 * 
	 * @param rms
	 */
	public DefaultTaskService(RuntimeMetadataService rms) {
		this.rms = rms;
	}
	
	@Override
	public void schedule(Task task) throws TaskException {
		this.rms.getTaskMetadataService().schedule(task);
	}

	@Override
	public void schedule(List<Task> tlist) throws TaskException {
		for(Task t : tlist) {
			this.schedule(t);
		}
	}

	@Override
	public void pause(Long id) throws TaskException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long id) throws TaskException {
		// TODO Auto-generated method stub

	}
}

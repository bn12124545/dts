package com.opentech.cloud.dts.api.task;

import java.util.List;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskException;

/**
 * Task接口
 * @author sihai
 *
 */
public interface TaskService {

	/**
	 * 调度新任务
	 * @param task
	 * @throws TaskException
	 */
	void schedule(Task task) throws TaskException;
	
	/**
	 * 批量调度任务
	 * @param tlist
	 * @throws TaskException
	 */
	void schedule(List<Task> tlist) throws TaskException;
	
	/**
	 * 暂停任务
	 * @param id
	 * @throws TaskException
	 */
	void pause(Long id) throws TaskException;
	
	/**
	 * 删除任务
	 * @param id
	 * @throws TaskException
	 */
	void remove(Long id) throws TaskException;
}

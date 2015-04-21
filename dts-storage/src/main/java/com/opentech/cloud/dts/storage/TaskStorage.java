package com.opentech.cloud.dts.storage;

import java.util.List;

import com.opentech.cloud.dts.common.task.Task;
import com.opentech.cloud.dts.common.task.TaskStatus;

/**
 * task 存储
 * @author sihai
 *
 */
public interface TaskStorage {

	/**
	 * 保存
	 * @param task
	 */
	void save(Task task);
	
	/**
	 * 更新
	 * @param task
	 */
	void update(Task task);
	
	/**
	 * 删除
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 分页查询
	 * @param group
	 * @param status
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	List<Task> query(String group, TaskStatus status, int currentPage, int pageSize);
	
	/**
	 * 查询总数
	 * @param group
	 * @param status
	 * @return
	 */
	int count(String group, TaskStatus status);
}

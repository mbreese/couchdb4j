/**
 * Copyright (c) 2011 Cummings Engineering Consultants, Inc. All Rights Reserved
 */
package com.fourspaces.couchdb;

/**
 * This class encapsulates the data for a task running on a couch server (as returned from the query to "_active_tasks")
 * 
 * @author anthony.payne
 *
 */
public class CouchTask {
	
	/** Key of JSON field for the task type */
	public static final String TASK_TYPE_KEY = "type";
	/** Key of the JSON field for the task */
	public static final String TASK_TASK_KEY = "task";
	/** Key for the JSON field for the status of the task */
	public static final String TASK_STATUS_KEY = "status";
	/** Key for the JSON field for the pid field of the task */
	public static final String TASK_PID_KEY = "pid";
	
	/** Type of task */
	protected String type;
	/** Details of the task */
	protected String task;
	/** Status of the task */
	protected String status;
	/** PID of the task */
	protected String pid;
	
	
	/** 
	 * @param type Type of task
	 * @param task Task details
	 * @param status Status of task
	 * @param pid PID of task
	 */
	public CouchTask(final String type, final String task, final String status, final String pid) {
		this.type = type;
		this.task = task;
		this.status = status;
		this.pid = pid;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}
}

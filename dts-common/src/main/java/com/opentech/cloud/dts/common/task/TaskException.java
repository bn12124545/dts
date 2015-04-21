package com.opentech.cloud.dts.common.task;

/**
 * 
 * @author sihai
 *
 */
public class TaskException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2809776222896474563L;

	/**
	 * 
	 */
	private final ErrorCode errorCode;
	
	/**
	 * 
	 */
	private final String errorMsg;

	public TaskException(ErrorCode errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
	@Override
	public String getMessage() {
		return this.getErrorMsg();
	}
}

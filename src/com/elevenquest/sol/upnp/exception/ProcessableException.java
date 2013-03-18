package com.elevenquest.sol.upnp.exception;

public class ProcessableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2267396822433817912L;

	public ProcessableException(String message) {
		super(message);
	}
	
	public ProcessableException(String message, Object errorInfo) {
		super(message);
		this.setErrorInfoObject(errorInfo);
	}
	
	Object errorInfoObject = null;
	
	public void setErrorInfoObject(Object errorInfo) {
		this.errorInfoObject = errorInfo;
	}
	
	public Object getErrorInfoObject() {
		return this.errorInfoObject;
	}
	
}

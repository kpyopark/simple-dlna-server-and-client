package com.elevenquest.sol.upnp.action;

public class ActionError extends Throwable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int ACTION_ERROR_NO_ERROR = 200;
	
	public static int ACTION_ERROR_INVALID_ACTION = 401;
	public static int ACTION_ERROR_INVALID_ARGS = 402;
	public static int ACTION_ERROR_INVALID_VAR = 404;
	
	public static int ACTION_ERROR_ACTION_FAILED = 501;

	public static int ACTION_ERROR_TRANSITION_NOT_AVAILIABLE = 701;
	public static int ACTION_ERROR_NO_CONTENTS = 702;
	public static int ACTION_ERROR_READ_ERROR = 703;
	public static int ACTION_ERROR_FORMAT_NOT_SUPPORTED_FOR_PLAYBACK = 704;
	public static int ACTION_ERROR_TRANSPORT_IS_LOCKED = 705;
	public static int ACTION_ERROR_WRITE_ERROR = 706;
	public static int ACTION_ERROR_MEDIA_IS_PROTECTED_OR_NOT_WRITABLE = 707;
	public static int ACTION_ERROR_FORMAT_NOT_SUPPORTED_FOR_RECORDING = 708;
	public static int ACTION_ERROR_MEDIA_IS_FULL = 709;
	public static int ACTION_ERROR_SEEK_MODE_NOT_SUPPORTED = 710;
	public static int ACTION_ERROR_ILLEGAL_SEEK_TARGET = 711;
	public static int ACTION_ERROR_PLAY_MODE_NOT_SUPPORTED = 712;
	public static int ACTION_ERROR_RECORD_QUALITY_NOT_SUPPORTED = 713;
	public static int ACTION_ERROR_ILLEGAL_MIME_TYPE = 714;
	public static int ACTION_ERROR_CONTENT_BUSY = 715;
	public static int ACTION_ERROR_RESOURCE_NOT_FOUND = 716;
	public static int ACTION_ERROR_PLAY_SPEED_NOT_SUPPORTED = 717;
	public static int ACTION_ERROR_INVALID_INSTANCE_ID = 718;
	
	public static int ACTION_ERROR_UPNP_TECHNICAL_COMMITTEE = 600; // 600 ~ 699
	
	public static int ACTION_ERROR_UPNP_WORKING_COMMITTEE = 701; // 701 ~ 799
	
	public static int ACTION_ERROR_VENDOR_SPECIFIED = 800; // 800 ~ 899
	
	private int errorCode = ACTION_ERROR_NO_ERROR;
	private int realErrorCode = ACTION_ERROR_NO_ERROR;
	
	private static java.util.HashMap<Integer, String>errorMessageMap = null;
	
	static {
		errorMessageMap = new java.util.HashMap<Integer,String>();
		errorMessageMap.put(200,"ACTION_ERROR_NO_ERROR");
		errorMessageMap.put(401,"ACTION_ERROR_INVALID_ACTION");
		errorMessageMap.put(402,"ACTION_ERROR_INVALID_ARGS");
		errorMessageMap.put(404,"ACTION_ERROR_INVALID_VAR");
		errorMessageMap.put(501,"ACTION_ERROR_ACTION_FAILED");
		errorMessageMap.put(701,"ACTION_ERROR_TRANSITION_NOT_AVAILIABLE");
		errorMessageMap.put(702,"ACTION_ERROR_NO_CONTENTS");
		errorMessageMap.put(703,"ACTION_ERROR_READ_ERROR");
		errorMessageMap.put(704,"ACTION_ERROR_FORMAT_NOT_SUPPORTED_FOR_PLAYBACK");
		errorMessageMap.put(705,"ACTION_ERROR_TRANSPORT_IS_LOCKED");
		errorMessageMap.put(706,"ACTION_ERROR_WRITE_ERROR");
		errorMessageMap.put(707,"ACTION_ERROR_MEDIA_IS_PROTECTED_OR_NOT_WRITABLE");
		errorMessageMap.put(708,"ACTION_ERROR_FORMAT_NOT_SUPPORTED_FOR_RECORDING");
		errorMessageMap.put(709,"ACTION_ERROR_MEDIA_IS_FULL");
		errorMessageMap.put(710,"ACTION_ERROR_SEEK_MODE_NOT_SUPPORTED");
		errorMessageMap.put(711,"ACTION_ERROR_ILLEGAL_SEEK_TARGET");
		errorMessageMap.put(712,"ACTION_ERROR_PLAY_MODE_NOT_SUPPORTED");
		errorMessageMap.put(713,"ACTION_ERROR_RECORD_QUALITY_NOT_SUPPORTED");
		errorMessageMap.put(714,"ACTION_ERROR_ILLEGAL_MIME_TYPE");
		errorMessageMap.put(715,"ACTION_ERROR_CONTENT_BUSY");
		errorMessageMap.put(716,"ACTION_ERROR_RESOURCE_NOT_FOUND");
		errorMessageMap.put(717,"ACTION_ERROR_PLAY_SPEED_NOT_SUPPORTED");
		errorMessageMap.put(718,"ACTION_ERROR_INVALID_INSTANCE_ID");
		errorMessageMap.put(600,"ACTION_ERROR_UPNP_TECHNICAL_COMMITTEE");
		errorMessageMap.put(701,"ACTION_ERROR_UPNP_WORKING_COMMITTEE");
		errorMessageMap.put(800,"ACTION_ERROR_VENDOR_SPECIFIED");
	}
	public int getErrorCode() {
		return errorCode;
	}
	
	public int getRealErrorCode() {
		return realErrorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.realErrorCode = errorCode;
		if ( errorCode >= 600 && errorCode < 700 ) {
			this.errorCode = ACTION_ERROR_UPNP_TECHNICAL_COMMITTEE;
		} else if ( errorCode > 700 && errorCode < 800 ) {
			this.errorCode = ACTION_ERROR_UPNP_WORKING_COMMITTEE;
		} else if ( errorCode > 800 && errorCode < 900 ) {
			this.errorCode = ACTION_ERROR_VENDOR_SPECIFIED;
		} else {
			this.errorCode = errorCode;
		}
	}
	
	public String getErrorMessage() {
		String msg = errorMessageMap.get(this.getErrorCode());
		return ( msg != null ) ? msg : "NO MESSAGE MATCHED WITH ERRORCODE[" + this.getErrorCode() + "]";
	}
	
	public ActionError(String errorMessage) {
		super(errorMessage);
	}
	
	public ActionError(int errorCode) {
		this.setErrorCode(errorCode);
	}
}

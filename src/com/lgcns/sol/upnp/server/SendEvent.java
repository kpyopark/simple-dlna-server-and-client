package com.lgcns.sol.upnp.server;

public class SendEvent {
	
	// TODO: If you want use java specification 1.5, you'd better modify this class into enumeration type.
	
	public static int SEND_EVENT_TYPE_ONCE = 1;
	public static int SEND_EVENT_TYPE_TIME_UNLIMINITED = 2;
	public static int SEND_EVENT_TYPE_TIME_LIMITED = 3;
	
	int type;
	int delayTimeInMilliSec = 100000;
	
	public SendEvent(int type, int delayTimeInMilliSec) {
		this.type = type;
		this.delayTimeInMilliSec = delayTimeInMilliSec;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getDelayTimeInMillisec() {
		return this.delayTimeInMilliSec;
	}
	
}

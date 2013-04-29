package com.elevenquest.sol.upnp.model;

public class UPnPChangeStatusValue {
	
	public static UPnPChangeStatusValue CHANGE_ADD;
	public static UPnPChangeStatusValue CHANGE_REMOVE;
	public static UPnPChangeStatusValue CHANGE_UPDATE;
	
	static {
		CHANGE_ADD = new UPnPChangeStatusValue('C');
		CHANGE_REMOVE = new UPnPChangeStatusValue('R');
		CHANGE_UPDATE = new UPnPChangeStatusValue('U');
	}

	char status;
	
	private UPnPChangeStatusValue(char status) {
		this.status = status;
	}

	public String toString() {
		return "status value:[" + this.status + "]";
	}
}

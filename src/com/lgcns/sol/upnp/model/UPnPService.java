package com.lgcns.sol.upnp.model;

import java.util.Vector;

public class UPnPService {
	
	public static int UPNP_SERVICE_TYPE_CDS = 1;
	
	String serviceType;
	String serviceId;
	String scpdUrl;
	String controlUrl;
	String eventsubUrl;
	
	String versionMajor;
	String versionMinor;
	
	UPnPDevice device;
	
	Vector<UPnPStateVariable> stateVariable = new Vector<UPnPStateVariable>();
	
	public UPnPService(UPnPDevice device) {
		this.device = device;
	}
	
	Vector<UPnPAction> actionList = new Vector<UPnPAction>();
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getScpdUrl() {
		return scpdUrl;
	}
	public void setScpdUrl(String scpdUrl) {
		this.scpdUrl = scpdUrl;
	}
	public String getControlUrl() {
		return controlUrl;
	}
	public void setControlUrl(String controlUrl) {
		this.controlUrl = controlUrl;
	}
	public String getEventsubUrl() {
		return eventsubUrl;
	}
	public void setEventsubUrl(String eventsubUrl) {
		this.eventsubUrl = eventsubUrl;
	}
}

package com.lgcns.sol.upnp.model;

import java.util.HashMap;
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
	
	public UPnPService(UPnPDevice device) {
		this.device = device;
	}
	
	// We use HashMap instead of Vector cause of performance issue.
	HashMap<String /* action name */, UPnPAction> actionList = new HashMap<String, UPnPAction>();
	HashMap<String /* state variable name */, UPnPStateVariable> variableList = new HashMap<String, UPnPStateVariable>();
	
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
	
	public void registerAction(UPnPAction action) {
		this.actionList.put(action.getActionName(), action);
	}
	
	public UPnPAction getAction(String actionName) {
		return this.actionList.get(actionName);
	}
	
	public void registerStateVariable(UPnPStateVariable variable) {
		this.variableList.put(variable.getName(), variable);
	}
	
	public UPnPStateVariable getStateVariable(String variableName) {
		return this.variableList.get(variableName);
	}
}

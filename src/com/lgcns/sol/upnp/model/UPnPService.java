package com.lgcns.sol.upnp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class UPnPService extends UPnPBase {
	
	public static int UPNP_SERVICE_TYPE_CDS = 1;
	public static int UPNP_SERVICE_TYPE_CMS = 2;
	
	public static String UPNP_SERVICE_ID_CDS = "urn:upnp-org:serviceId:ContentDirectory";
	public static String UPNP_SERVICE_ID_CMS = "urn:upnp-org:serviceId:ConnectionManager";
	
	String serviceType;
	String serviceId;
	String scpdUrl;
	String controlUrl;
	String eventsubUrl;
	
	String versionMajor;
	String versionMinor;
	
	UPnPDevice device;
	
	boolean isRemote = true;
	boolean isReadyToUse = false;
	boolean isProgressingToRetrieve = false;
	
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
	
	public Collection<UPnPAction> getActionList() {
		return this.actionList.values();
	}
	
	public void registerStateVariable(UPnPStateVariable variable) {
		this.variableList.put(variable.getName(), variable);
	}
	
	public UPnPStateVariable getStateVariable(String variableName) {
		return this.variableList.get(variableName);
	}
	
	public Collection<UPnPStateVariable> getStateVariableList() {
		return this.variableList.values();
	}
	
	public boolean isRemote() {
		return isRemote;
	}
	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}
	public boolean isReadyToUse() {
		return isReadyToUse;
	}
	public void setReadyToUse(boolean isReadyToUse) {
		this.isReadyToUse = isReadyToUse;
	}
	public boolean isProgressingToRetrieve() {
		return isProgressingToRetrieve;
	}
	public void setProgressingToRetrieve(boolean isProgressingToRetrieve) {
		this.isProgressingToRetrieve = isProgressingToRetrieve;
	}
	public UPnPDevice getDevice() {
		return device;
	}
	public void setDevice(UPnPDevice device) {
		this.device = device;
	}

	public void printAllDescirption(){
		if(this.versionMajor!="") System.out.println(this.versionMajor);
		if(this.versionMinor!="") System.out.println(this.versionMinor);
		if(this.serviceId!="") System.out.println(this.serviceId);
		if(this.scpdUrl!="") System.out.println(this.scpdUrl);
		if(this.controlUrl!="") System.out.println(this.controlUrl);
		if(this.eventsubUrl!="") System.out.println(this.eventsubUrl);
		System.out.println(this.isRemote);
		System.out.println(this.isReadyToUse);
		System.out.println(this.isProgressingToRetrieve);
	}
}

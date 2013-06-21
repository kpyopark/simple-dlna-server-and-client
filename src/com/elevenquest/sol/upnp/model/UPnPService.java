package com.elevenquest.sol.upnp.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.common.UPnPUtils;

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
	boolean isSubscribed = false;
	
	public boolean isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	ArrayList<IUPnPServiceStatusChangeListener> listeners = null;
	
	String subscribeId;
	
	public String getSubscribeId() {
		return subscribeId;
	}

	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}

	public UPnPService(UPnPDevice device) {
		this.device = device;
		this.listeners = new ArrayList<IUPnPServiceStatusChangeListener>();
	}
	
	private String getAbsoluteURL(String pathOrUrl) {
		if (pathOrUrl.startsWith("http"))
			return pathOrUrl;
		else {
			return "http://" + this.device.getBaseHost() + (pathOrUrl.charAt(0) == '/' ? "" : "/" ) + pathOrUrl;
		}
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
		return getAbsoluteURL(this.scpdUrl);
	}
	public void setScpdUrl(String scpdUrl) {
		this.scpdUrl = scpdUrl;
	}
	public String getControlUrl() {
		return getAbsoluteURL(this.controlUrl);
	}
	public void setControlUrl(String controlUrl) {
		this.controlUrl = controlUrl;
	}
	public String getEventsubUrl() {
		return getAbsoluteURL(this.eventsubUrl);
	}
	public void setEventsubUrl(String eventsubUrl) {
		this.eventsubUrl = eventsubUrl;
	}
	
	public void registerAction(UPnPAction action) {
		synchronized(action) {
			this.actionList.put(action.getActionName(), action);
		}
	}
	
	public UPnPAction getAction(String actionName) {
		return this.actionList.get(actionName);
	}
	
	public Collection<UPnPAction> getActionList() {
		return this.actionList.values();
	}
	
	public void registerStateVariable(UPnPStateVariable variable) {
		synchronized(variable) {
			this.variableList.put(variable.getName(), variable);
			variable.setService(this);
		}
	}
	
	public UPnPStateVariable getStateVariable(String variableName) {
		return this.variableList.get(variableName);
	}
	
	public Collection<UPnPStateVariable> getStateVariableList() {
		return this.variableList.values();
	}
	
	public void addServiceStateChangeListener(IUPnPServiceStatusChangeListener listener) {
		if ( !this.listeners.contains(listener) ) {
			synchronized(listeners) {
				this.listeners.add(listener);
			}
		}
	}
	
	public void removeServiceStatusChangeListener(IUPnPServiceStatusChangeListener listener) {
		synchronized(listeners) {
			this.listeners.remove(listener);
		}
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
		if ( isReadyToUse ) {
			synchronized ( listeners ) {
				IUPnPServiceStatusChangeListener[] listenerList = listeners.toArray(new IUPnPServiceStatusChangeListener[0]);
				for ( int cnt = 0; ( listenerList != null ) && ( cnt < listenerList.length ) ; cnt++) {
					listenerList[cnt].updateServiceStatus(UPnPChangeStatusValue.CHANGE_UPDATE, this);
				}
			}
		}
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
		if(this.versionMajor!="") Logger.println(Logger.INFO, this.versionMajor);
		if(this.versionMinor!="") Logger.println(Logger.INFO, this.versionMinor);
		if(this.serviceId!="") Logger.println(Logger.INFO, this.serviceId);
		if(this.scpdUrl!="") Logger.println(Logger.INFO, this.scpdUrl);
		if(this.controlUrl!="") Logger.println(Logger.INFO, this.controlUrl);
		if(this.eventsubUrl!="") Logger.println(Logger.INFO, this.eventsubUrl);
		Logger.println(Logger.INFO, "isRemote Service:"+this.isRemote);
		Logger.println(Logger.INFO, "isReadyToUse :"+this.isReadyToUse);
		Logger.println(Logger.INFO, "isProgressingToRetrieve :"+this.isProgressingToRetrieve);
	}
	
	public String toString() {
		return getServiceId() + ":actions-" + ( this.getActionList() != null ? this.getActionList().size() : 0 ) + ":state varaible-" +
			(this.getStateVariableList() != null ? this.getStateVariableList().size() : 0 );
	}
	
	public String getDeliveryUrl() {
		// It should be used for local service. not for remote service.
		HashMap<InetAddress, NetworkInterface> ipAndNic = UPnPUtils.getAvailiableIpAndNicList();
		Set<InetAddress> ips = ipAndNic.keySet();
		InetAddress mainIp = null;
		for ( InetAddress ip : ips ) {
			NetworkInterface nic = ipAndNic.get(ip);
			if ( nic.equals(this.device.getNetworkInterface()) ) {
				mainIp = ip;
			}
		}
		if ( mainIp == null )
			return null;
		return "http://" + mainIp.getHostAddress() + "/notify.do?device_id=" + this.device.getUuid() + "&service_id=" + this.getServiceId();
	}
}

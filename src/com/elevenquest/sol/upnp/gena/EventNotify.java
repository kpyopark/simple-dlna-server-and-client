package com.elevenquest.sol.upnp.gena;

import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.network.HttpRequest;

public class EventNotify extends HttpRequest {

	UPnPDeviceManager manager = null;
	
	String propertyName = null;
	String propertyValue = null;
	
	public EventNotify() {
		super();
		manager = UPnPDeviceManager.getDefaultDeviceManager();
	}
	
	public EventNotify(HttpRequest request) {
		super(request);
		manager = UPnPDeviceManager.getDefaultDeviceManager();
	}
	
	public UPnPStateVariable getUPnPStateVariable() {
		return null;
		
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}

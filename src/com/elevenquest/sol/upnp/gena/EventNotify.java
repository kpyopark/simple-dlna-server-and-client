package com.elevenquest.sol.upnp.gena;

import java.util.Enumeration;
import java.util.Hashtable;

import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.network.HttpRequest;

public class EventNotify extends HttpRequest {

	UPnPDeviceManager manager = null;

	Hashtable<String, String> propertyNameAndValue = null;
	
	public EventNotify() {
		super();
		manager = UPnPDeviceManager.getDefaultDeviceManager();
		propertyNameAndValue = new Hashtable<String,String>();
	}
	
	public EventNotify(HttpRequest request) {
		super(request);
		manager = UPnPDeviceManager.getDefaultDeviceManager();
		propertyNameAndValue = new Hashtable<String,String>();
	}
	
	public UPnPStateVariable getUPnPStateVariable() {
		return null;
		
	}

	public Enumeration<String> getPropertyNameList() {
		return propertyNameAndValue.keys();
	}

	public void setPropertyNameAndValue(String propertyName, String propertyValue) {
		if ( propertyValue == null )
			this.propertyNameAndValue.put(propertyName, "");
		else
			this.propertyNameAndValue.put(propertyName, propertyValue);
	}

	public String getPropertyValue(String propertyName) {
		return this.propertyNameAndValue.get(propertyName);
	}

}

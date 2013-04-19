package com.elevenquest.sol.upnp.gena;

import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.network.HttpRequest;

public class EventNotify extends HttpRequest {

	public
	
	UPnPDeviceManager manager = null;
	
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
}

package com.elevenquest.sol.upnp.model;

public interface IUPnPDeviceServiceListChangeListener {
	
	public void updateServiceList(UPnPChangeStatusValue value, UPnPDevice device, UPnPService serviceUpdated);
	
}

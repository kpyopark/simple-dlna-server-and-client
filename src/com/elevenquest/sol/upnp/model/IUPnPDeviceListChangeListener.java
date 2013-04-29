package com.elevenquest.sol.upnp.model;

public interface IUPnPDeviceListChangeListener {

	public void updateDeviceList(UPnPChangeStatusValue value, UPnPDevice device);
	
}

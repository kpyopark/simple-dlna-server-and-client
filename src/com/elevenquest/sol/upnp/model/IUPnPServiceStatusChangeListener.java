package com.elevenquest.sol.upnp.model;

public interface IUPnPServiceStatusChangeListener {
	public void updateServiceStatus(UPnPChangeStatusValue value, UPnPService service);
}

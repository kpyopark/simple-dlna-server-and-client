package com.elevenquest.sol.upnp.service.avtransport;

import com.elevenquest.sol.upnp.model.UPnPBase;

public class TransportInfoItem extends UPnPBase {
	
	String currentTransportState;
	String currentTransportStatus;
	String currentSpeed;
	
	public String getCurrentTransportState() {
		return currentTransportState;
	}
	public void setCurrentTransportState(String currentTransportState) {
		this.currentTransportState = currentTransportState;
	}
	public String getCurrentTransportStatus() {
		return currentTransportStatus;
	}
	public void setCurrentTransportStatus(String currentTransportStatus) {
		this.currentTransportStatus = currentTransportStatus;
	}
	public String getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(String currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
}

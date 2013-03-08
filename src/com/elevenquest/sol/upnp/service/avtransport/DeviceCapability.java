package com.elevenquest.sol.upnp.service.avtransport;

import com.elevenquest.sol.upnp.model.UPnPBase;

public class DeviceCapability extends UPnPBase {
	String playMedia;
	String recMedia;
	String recQualityModes;
	
	public String getPlayMedia() {
		return playMedia;
	}
	public void setPlayMedia(String playMedia) {
		this.playMedia = playMedia;
	}
	public String getRecMedia() {
		return recMedia;
	}
	public void setRecMedia(String recMedia) {
		this.recMedia = recMedia;
	}
	public String getRecQualityModes() {
		return recQualityModes;
	}
	public void setRecQualityModes(String recQualityModes) {
		this.recQualityModes = recQualityModes;
	}
}

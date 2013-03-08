package com.elevenquest.sol.upnp.service.avtransport;

import java.util.ArrayList;

import com.elevenquest.sol.upnp.model.UPnPBase;

public class TransportSetting extends UPnPBase {
	
	String playMode;
	String recQualityMode;
	ArrayList<String> actions;
	public String getPlayMode() {
		return playMode;
	}
	public void setPlayMode(String playMode) {
		this.playMode = playMode;
	}
	public String getRecQualityMode() {
		return recQualityMode;
	}
	public void setRecQualityMode(String recQualityMode) {
		this.recQualityMode = recQualityMode;
	}
	public ArrayList<String> getActions() {
		return actions;
	}
	public void setActions(ArrayList<String> actions) {
		this.actions = actions;
	}
	
}

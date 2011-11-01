package com.lgcns.sol.upnp.model;

import java.util.Vector;

public class UPnPAction {
	UPnPService service;
	
	String actionName;
	boolean isUsable;
	
	Vector<UPnPStateVariable> inArgs = new Vector<UPnPStateVariable>();
	Vector<UPnPStateVariable> outArgs = new Vector<UPnPStateVariable>();
	
	public UPnPAction(UPnPService service) {
		this.service = service;
	}
	
	public UPnPService getService() {
		return this.service;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public void addInArgument(UPnPStateVariable inArg) {
		inArgs.add(inArg);
	}
	
	public Vector<UPnPStateVariable> getInArguments() {
		return this.inArgs;
	}
	
	public void addOutArgument(UPnPStateVariable outArg) {
		outArgs.add(outArg);
	}
	
	public Vector<UPnPStateVariable> getOutArguments() {
		return this.outArgs;
	}
	
	public boolean isUsable() {
		return this.isUsable;
	}
	
	public void setIsUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}
	
}

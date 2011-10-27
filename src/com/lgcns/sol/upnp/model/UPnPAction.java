package com.lgcns.sol.upnp.model;

import java.util.Vector;

public class UPnPAction {
	UPnPService service;
	
	String actionName;
	
	Vector<UPnPStateVariable> inArgs = new Vector<UPnPStateVariable>();
	Vector<UPnPStateVariable> outArgs = new Vector<UPnPStateVariable>();
	
	public UPnPAction(UPnPService service) {
		this.service = service;
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
	
}

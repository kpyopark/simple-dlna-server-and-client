package com.elevenquest.sol.upnp.model;

import java.util.Vector;

public class UPnPAction extends UPnPBase {
	UPnPService service;
	
	String actionName;
	boolean isUsable;
	
	Vector<UPnPStateVariable> inArgs = new Vector<UPnPStateVariable>();
	Vector<UPnPStateVariable> outArgs = new Vector<UPnPStateVariable>();
	
	static class UPnPNullActionArgument extends UPnPStateVariable {
		public void setNeedToSendEvent(boolean needToSendEvent) {
			//this.needToSendEvent = needToSendEvent;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setMulticastEvent(boolean isMulticastEvent) {
			//this.isMulticastEvent = isMulticastEvent;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setType(UPnPDataType type) {
			//this.type = type;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setValue(Object value) {
			//this.value = value;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setDefaultValue(Object defaultValue) {
			//this.defaultValue = defaultValue;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setName(String name) {
			//this.name = name;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setIsCSVFormat(boolean isCSVFormat) {
			//this.isCSVFormat = isCSVFormat;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setIsUsable(boolean isUsable) {
			//this.isUsable = isUsable;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
		public void setArgumentName(String argumentName) {
			//this.argumentName = argumentName;
			System.out.println("[WARN] UPnPNullActionArgument : This method couldn't be processed.");
		}
	}
	
	static UPnPStateVariable NULL_ARGUMENT = null;
	
	static {
		NULL_ARGUMENT = new UPnPNullActionArgument();
	}
	
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
	
	public UPnPStateVariable getInArgument(String argumentName) {
		int inx = 0;
		for ( ; inx < inArgs.size() ; inx++ ) {
			if ( inArgs.get(inx).getArgumentName().equalsIgnoreCase(argumentName) )
				return inArgs.get(inx);
		}
		System.out.println("NULL ARGUMENT RETURNED:" + argumentName);
		return NULL_ARGUMENT;
	}
	
	public void addOutArgument(UPnPStateVariable outArg) {
		outArgs.add(outArg);
	}
	
	public Vector<UPnPStateVariable> getOutArguments() {
		return this.outArgs;
	}
	
	public UPnPStateVariable getOutArgument(String argumentName) {
		int inx = 0;
		for ( ; inx < outArgs.size() ; inx++ ) {
			if ( outArgs.get(inx).getArgumentName().equals(argumentName) )
				return outArgs.get(inx);
		}
		return NULL_ARGUMENT;
	}
	
	public boolean isUsable() {
		return this.isUsable;
	}
	
	public void setIsUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}
	
}

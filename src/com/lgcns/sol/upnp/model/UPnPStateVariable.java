package com.lgcns.sol.upnp.model;

public class UPnPStateVariable {
	boolean needToSendEvent;
	boolean isMulticastEvent;
	UPnPDataType type;
	Object value;
	Object defaultValue;
	String name;
	
	public boolean isNeedToSendEvent() {
		return needToSendEvent;
	}
	public void setNeedToSendEvent(boolean needToSendEvent) {
		this.needToSendEvent = needToSendEvent;
	}
	public boolean isMulticastEvent() {
		return isMulticastEvent;
	}
	public void setMulticastEvent(boolean isMulticastEvent) {
		this.isMulticastEvent = isMulticastEvent;
	}
	public UPnPDataType getType() {
		return type;
	}
	public void setType(UPnPDataType type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

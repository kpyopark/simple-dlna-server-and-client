package com.elevenquest.sol.upnp.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.elevenquest.sol.upnp.common.UPnPUtils;

public class UPnPStateVariable extends UPnPBase {
	boolean needToSendEvent;
	boolean isMulticastEvent;
	boolean isCSVFormat;
	UPnPDataType type;
	String value;
	Object defaultValue;
	String name;
	boolean isUsable;
	String argumentName;
	UPnPAllowedValueRange valueRange;
	ArrayList<Object> allowedValueList;
	UPnPStateVariable relatedStateVariable;
	UPnPService service = null;
	
	public UPnPStateVariable() {
		allowedValueList = new ArrayList<Object>();
	}
	
	public UPnPStateVariable(String name, UPnPDataType type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
		if ( relatedStateVariable != null )
			relatedStateVariable.setValue(value);
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
	public boolean isCSVFormat() {
		return this.isCSVFormat;
	}
	public void setIsCSVFormat(boolean isCSVFormat) {
		this.isCSVFormat = isCSVFormat;
	}
	public boolean isUsable() {
		return this.isUsable;
	}
	public void setIsUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}
	
	// Method for Data type : CSV String. CSV heterogeneous.
	static public ArrayList<String> getCSVStateVariableStrings(String csvValue) {
		return UPnPUtils.getTokenizedCSVElements(csvValue);
	}
	
	// Method for Data type : CSV int. CSV i4
	static public ArrayList<Integer> getCSVStateVariableIntegers(String csvValue) {
		ArrayList<String> stringList = UPnPUtils.getTokenizedCSVElements(csvValue);
		ArrayList<Integer> returnValue = new ArrayList<Integer>();
		for ( String oneValue : stringList) {
			try {
				returnValue.add(new Integer(oneValue));
			} catch ( Exception e ) {
				e.printStackTrace();
				// Number format Exception occurred.
			}
		}
		return returnValue;
	}

	public String getArgumentName() {
		return argumentName;
	}

	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}
	
	public void addAllowedValue(Object allowedValue) {
		this.allowedValueList.add(allowedValue);
	}
	
	public ArrayList<Object> getAllowedValueList() {
		return this.allowedValueList;
	}
	
	public void clearAllowedVaueList() {
		this.allowedValueList.clear();
	}
	
	public void setRelatedStateVariable(UPnPStateVariable relatedStateVariable) {
		this.relatedStateVariable = relatedStateVariable;
		this.setType(relatedStateVariable.getType());
		this.setDefaultValue(relatedStateVariable.getDefaultValue());
		this.setAllowedValueRange(relatedStateVariable.getAllowedValueRange());
		this.setAllowedValueList(relatedStateVariable.getAllowedValueList());
	}
	
	public UPnPStateVariable getRelatedStateVariable() {
		return this.relatedStateVariable;
	}
	
	public void setAllowedValueRange(UPnPAllowedValueRange range) {
		this.valueRange = range;
	}
	
	public UPnPAllowedValueRange getAllowedValueRange() {
		return this.valueRange;
	}
	
	public void setAllowedValueList(ArrayList<Object> valueList) {
		this.allowedValueList = valueList;
	}
	
	public void setService(UPnPService service) {
		this.service = service;
	}
	
	public UPnPService getService() {
		return this.service;
	}
	
}

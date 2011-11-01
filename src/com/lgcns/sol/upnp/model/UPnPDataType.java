package com.lgcns.sol.upnp.model;

import java.util.HashMap;

public class UPnPDataType {
	
	static HashMap<String, UPnPDataType> DEFAULT_TYPE_MAPPER = new HashMap<String, UPnPDataType>();
	
	static {
		DEFAULT_TYPE_MAPPER.put("string", new UPnPDataType("string",String.class));
		DEFAULT_TYPE_MAPPER.put("ui4", new UPnPDataType("ui4", Integer.class));
		DEFAULT_TYPE_MAPPER.put("uri", new UPnPDataType("uri",String.class));
	};
	
	String xmlType;
	Class javaType;
	
	public UPnPDataType(String xmlType, Class javaType) {
		this.xmlType = xmlType;
		this.javaType = javaType;
	}
	
	public String getXmlType() {
		return this.xmlType;
	}
	
	public Class getJavaType() {
		return this.javaType;
	}
	
}

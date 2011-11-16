package com.lgcns.sol.upnp.model;

import java.util.HashMap;

public class UPnPDataType extends UPnPBase {
	
	static HashMap<String, UPnPDataType> DEFAULT_TYPE_MAPPER = new HashMap<String, UPnPDataType>();
	
	public static final String UPNP_DATATYPE_NAME_STRING = "string";
	public static final String UPNP_DATATYPE_NAME_UI4 = "ui4";
	public static final String UPNP_DATATYPE_NAME_I4 = "i4";
	public static final String UPNP_DATATYPE_NAME_URI = "uri";
	
	static {
		DEFAULT_TYPE_MAPPER.put(UPNP_DATATYPE_NAME_STRING, new UPnPDataType(UPNP_DATATYPE_NAME_STRING,String.class));
		DEFAULT_TYPE_MAPPER.put(UPNP_DATATYPE_NAME_UI4, new UPnPDataType(UPNP_DATATYPE_NAME_UI4, Integer.class));
		DEFAULT_TYPE_MAPPER.put(UPNP_DATATYPE_NAME_I4, new UPnPDataType(UPNP_DATATYPE_NAME_I4,String.class));
		DEFAULT_TYPE_MAPPER.put(UPNP_DATATYPE_NAME_URI, new UPnPDataType(UPNP_DATATYPE_NAME_URI,String.class));
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
	
	public static UPnPDataType getUPnPDataType(String upnpDataTypeName) {
		return DEFAULT_TYPE_MAPPER.get(upnpDataTypeName);
	}
	
}

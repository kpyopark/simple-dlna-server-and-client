package com.elevenquest.sol.upnp.service;

public class ContentDirectoryItem {

	// Common properties in both Container & Item elements.
	int type;
	String id;	// it can be a container ID or a Item ID.
	String parentId;
	boolean isRestricted = false;
	String title;
	String creator;
	String upnpCreateClass;
	String upnpClassName;
	
	// properties for Container element.
	boolean isSearchable = false;
	String upnpSearchClass;
	
	// properties for Item element.
	String date;
	String resProtocolInfo;
	String resValue;
	String refId;	// it might be used only in item element.
	String resSize;

	static public int CDS_TYPE_CONTAINER = 1;		// Mapping to <container> element in DIDL-lite.
	static public int CDS_TYPE_ITEM = 2;			// Mapping to <item> element in DIDL-Lite.

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isRestricted() {
		return isRestricted;
	}
	public void setRestricted(boolean isRestricted) {
		this.isRestricted = isRestricted;
	}
	public boolean isSearchable() {
		return isSearchable;
	}
	public void setSearchable(boolean isSearchable) {
		this.isSearchable = isSearchable;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getResProtocolInfo() {
		return resProtocolInfo;
	}
	public void setResProtocolInfo(String resProtocolInfo) {
		this.resProtocolInfo = resProtocolInfo;
	}
	public String getResValue() {
		return resValue;
	}
	public void setResValue(String resValue) {
		this.resValue = resValue;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getUpnpClassName() {
		return upnpClassName;
	}
	public void setUpnpClassName(String upnpClassName) {
		this.upnpClassName = upnpClassName;
	}
	public String getUpnpSearchClass() {
		return upnpSearchClass;
	}
	public void setUpnpSearchClass(String upnpSearchClass) {
		this.upnpSearchClass = upnpSearchClass;
	}
	public String getUpnpCreateClass() {
		return upnpCreateClass;
	}
	public void setUpnpCreateClass(String upnpCreateClass) {
		this.upnpCreateClass = upnpCreateClass;
	}
	public String getResSize() {
		return resSize;
	}
	public void setResSize(String resSize) {
		this.resSize = resSize;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getCreator()).append(":");
		sb.append(this.getDate()).append(":");
		sb.append(this.getId()).append(":");
		sb.append(this.getParentId()).append(":");
		sb.append(this.getRefId()).append(":");
		sb.append(this.getResProtocolInfo()).append(":");
		sb.append(this.getResSize()).append(":");
		sb.append(this.getResValue()).append(":");
		sb.append(this.getTitle()).append(":");
		sb.append(this.getType()).append(":");
		sb.append(this.getUpnpClassName()).append(":");
		sb.append(this.getUpnpCreateClass()).append(":");
		sb.append(this.getUpnpSearchClass()).append(":");
		sb.append(this.getClass()).append(":");
		sb.append("END");
		return sb.toString();
	}

}

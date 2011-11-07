package com.lgcns.sol.upnp.service;

public class ContentDirectoryItem {
	String id;	// it can be a container ID or a Item ID.
	String parentId;
	boolean isRestricted = false;
	boolean isSearchable = false;
	String title;
	String creator;
	//Not used yet.
	//String upnpClassName;
	//String upnpSearchClass;
	String resProtocolInfo;
	String resValue;
	String refId;	// it might be used only in item element.
	
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
}

package com.lgcns.sol.upnp.service;

import java.util.ArrayList;

public class ContentDirectoryList {
	int numberOfReturned;
	int totalMatches;
	String updateID;
	ArrayList<ContentDirectoryItem> itemList = new ArrayList<ContentDirectoryItem>();
	
	public int getNumberOfReturned() {
		return numberOfReturned;
	}
	public void setNumberOfReturned(int numberOfReturned) {
		this.numberOfReturned = numberOfReturned;
	}
	public int getTotalMatches() {
		return totalMatches;
	}
	public void setTotalMatches(int totalMatches) {
		this.totalMatches = totalMatches;
	}
	public String getUpdateID() {
		return updateID;
	}
	public void setUpdateID(String updateID) {
		this.updateID = updateID;
	}
	public ArrayList<ContentDirectoryItem> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<ContentDirectoryItem> itemList) {
		this.itemList = itemList;
	}
	public void addItemList(ContentDirectoryItem item) {
		this.itemList.add(item);
	}
}

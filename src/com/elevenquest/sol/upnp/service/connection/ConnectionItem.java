package com.elevenquest.sol.upnp.service.connection;

import java.util.ArrayList;

public class ConnectionItem {
	int connectionID = -1;
	int AVTransportID = 0;
	int rcsID = -1;
	String protocolInfo;
	String peerConnectionManager;
	int peerConnectionID = -1;
	String direction = "NONE";
	String status;
	
	
	public int getConnectionID() {
		return connectionID;
	}
	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}
	public int getAVTransportID() {
		return AVTransportID;
	}
	public void setAVTransportID(int aVTransportID) {
		AVTransportID = aVTransportID;
	}
	public int getRcsID() {
		return rcsID;
	}
	public void setRcsID(int rcsID) {
		this.rcsID = rcsID;
	}
	
	public String getProtocolInfo() {
		return protocolInfo;
	}
	public void setProtocolInfo(String protocolInfo) {
		this.protocolInfo = protocolInfo;
	}
	public String getPeerConnectionManager() {
		return peerConnectionManager;
	}
	public void setPeerConnectionManager(String peerConnectionManager) {
		this.peerConnectionManager = peerConnectionManager;
	}
	public int getPeerConnectionID() {
		return peerConnectionID;
	}
	public void setPeerConnectionID(int peerConnectionID) {
		this.peerConnectionID = peerConnectionID;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getConnectionID()).append(":");
		sb.append(this.getAVTransportID()).append(":");
		sb.append(this.getRcsID()).append(":");
		sb.append(this.getDirection()).append(":");
		sb.append(this.getPeerConnectionID()).append(":");
		sb.append(this.getPeerConnectionManager()).append(":");
		sb.append(this.getProtocolInfo()).append(":");
		sb.append(this.getRcsID()).append(":");
		sb.append(this.getStatus()).append(":");
		sb.append("END");
		return sb.toString();
	}
}

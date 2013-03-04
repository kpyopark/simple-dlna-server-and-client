package com.elevenquest.sol.upnp.service.connection;

public class ConnectionItem {
	int connectionID = -1;
	int AVTransportID = -1;
	int rcsID = -1;
	
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getConnectionID()).append(":");
		sb.append(this.getAVTransportID()).append(":");
		sb.append(this.getRcsID()).append(":");
		sb.append("END");
		return sb.toString();
	}	
	
}

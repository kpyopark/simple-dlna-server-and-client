package com.elevenquest.sol.upnp.gena;

import java.net.NetworkInterface;

import com.elevenquest.sol.upnp.model.UPnPBase;

/**
 * 
 * @author kyungpyo.park
 * 
 * The event publisher should maintan a list of subscriber which has 4 attributes.
 * Refer to Page 86 in UPnP-arch_DeviceArchitecture-v1.1
 */
public class Subscriber extends UPnPBase {
	
	private String uuid;
	private String url;
	private String eventKey;	// It represents sequence of event list.
	private int duration;
	private String httpVersion;
	
	NetworkInterface nwInterface;
	
	public NetworkInterface getNwInterface() {
		return nwInterface;
	}
	public void setNwInterface(NetworkInterface nwInterface) {
		this.nwInterface = nwInterface;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getHttpVersion() {
		return httpVersion;
	}
	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}
	
}

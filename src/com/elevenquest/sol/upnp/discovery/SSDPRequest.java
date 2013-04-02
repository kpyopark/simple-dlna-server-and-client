package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HTTPRequest;

public class SSDPRequest extends HTTPRequest {
	UPnPDevice device = null;
	
	public final static String ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG = "BOOTID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_NEXTBOTID_UPNP_ORG = "NEXTBOOTID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG = "CONFIGID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG = "SEARCHPORT.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_HOST = "HOST";
	public final static String ID_UPNP_DISCOVERY_CACHECONTROL = "CACHE-CONTROL";
	public final static String ID_UPNP_DISCOVERY_LOCATION = "LOCATION";
	public final static String ID_UPNP_DISCOVERY_NOTIFICATION_TYPE = "NT";
	public final static String ID_UPNP_DISCOVERY_NT_SUBTYPE = "NTS";
	public final static String ID_UPNP_DISCOVERY_SERVER = "SERVER";
	public final static String ID_UPNP_DISCOVERY_USN = "USN";
	
	public final static String ID_START_LINE_COMMAND_NOTIFY = "NOTIFY";
	public final static String ID_START_LINE_COMMAND_SEARCH = "M-SEARCH";
	
	public final static String ID_NT_SUBTYPE_SSDPALIVE = "ssdp:alive";
	public final static String ID_NT_SUBTYPE_SSDPBYEBYE = "ssdp:byebye";
	public final static String ID_NT_SUBTYPE_SSDPUPDATE = "ssdp:update";
	
	public final static String REQUIRED_ID_LIST[] = {
		ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG, ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG, ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG};
	
	public SSDPRequest() {
		super();
	}
	
	/**
	 * 	This constructor will be used when to extract UPnPDevice info from HTTP Request sent by remote device.
	 * 
	 * @param request
	 */
	public SSDPRequest(HTTPRequest request) {
		super(request);
	}
	
	/**
	 * This constructor would be used when to make new HTTPRequest message from a UPnPDevice info already registered in UPnPDeviceManager.
	 * New HTTP request message would be broadcasted to notify itself.
	 * 
	 * @param device
	 */
	public SSDPRequest(UPnPDevice device) {
		super();
		this.device = device;
		this.setCommand(ID_START_LINE_COMMAND_NOTIFY);
		this.setHttpVer("HTTP/1.1");
		this.setUrlPath("*");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_HOST, DefaultConfig.ID_UPNP_DISCOVERY_HOST_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CACHECONTROL, DefaultConfig.ID_UPNP_DISCOVERY_CACHECONTROL_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_LOCATION, DefaultConfig.ID_UPNP_DISCOVERY_LOCATION_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NOTIFICATION_TYPE,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NT_SUBTYPE, SSDPMessage.ID_NT_SUBTYPE_SSDPALIVE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SERVER,DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_USN,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG,"1");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG,"1");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG,this.device.getMulticastPort() + "");	
	}
	
	/**
	 * When to extract new UPnP device base info from UPnP request sent by remote device.
	 * 
	 * @return
	 */
	public UPnPDevice getDeviceBaseInfo() {
		UPnPDevice device = new UPnPDevice();

		device.setUsn(this.getHeaderValue(ID_UPNP_DISCOVERY_USN));
		device.setHost(this.getHeaderValue(ID_UPNP_DISCOVERY_HOST));
		device.setLocation(this.getHeaderValue(ID_UPNP_DISCOVERY_LOCATION));
		{
			String cacheValue = this.getHeaderValue(ID_UPNP_DISCOVERY_CACHECONTROL);
			device.setCacheControl(Integer.parseInt(cacheValue.substring(cacheValue.indexOf("max-age=")+8).trim()));
		}
		
		device.setNts(this.getHeaderValue(ID_UPNP_DISCOVERY_NT_SUBTYPE));
		device.setNt(this.getHeaderValue(ID_UPNP_DISCOVERY_NOTIFICATION_TYPE));
		device.setServer(this.getHeaderValue(ID_UPNP_DISCOVERY_SERVER));
		device.setRemote(true);
		device.setReadyToUse(false);
		
		Logger.println(Logger.DEBUG, "Device Info listened by ssdp handler:\n" + device.toString());
		
		return device;
	}
	
}

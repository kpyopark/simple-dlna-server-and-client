package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HTTPRequest;

public class SSDPRequest extends HTTPRequest {
	UPnPDevice device = null;
	
	public SSDPRequest() {
		super();
	}
	
	/**
	 * This constructor will be used when to extract UPnPDevice info from HTTP Request sent by remote device.
	 * 
	 * ** We are going to make two constructors in all message wrapper classes related with HTTP Protocol in our package.
	 * ** Because, all of them is used for two methods by constructing a message or extracting from a message. 
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
		this.setCommand(SSDPMessage.ID_START_LINE_COMMAND_NOTIFY);
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

		device.setUsn(this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_USN));
		device.setHost(this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_HOST));
		device.setLocation(this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_LOCATION));
		{
			String cacheValue = this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CACHECONTROL);
			device.setCacheControl(Integer.parseInt(cacheValue.substring(cacheValue.indexOf("max-age=")+8).trim()));
		}
		
		device.setNts(this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NT_SUBTYPE));
		device.setNt(this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NOTIFICATION_TYPE));
		device.setServer(this.getHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SERVER));
		device.setRemote(true);
		device.setReadyToUse(false);
		
		Logger.println(Logger.DEBUG, "Device Info listened by ssdp handler:\n" + device.toString());
		
		return device;
	}
	
}

package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;

public class SSDPMsearchResponse extends HttpResponse {
	UPnPDevice device = null;
	
	public SSDPMsearchResponse() {
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
	public SSDPMsearchResponse(HttpResponse response) {
		super(response);
	}
	
	/**
	 * This constructor would be used when to make new HTTPRequest message from a UPnPDevice info already registered in UPnPDeviceManager.
	 * New HTTP request message would be broadcasted to notify itself.
	 * 
	 * @param device
	 */
	public SSDPMsearchResponse(UPnPDevice device, String searchType) {
		super();
		this.device = device;
		this.setHttpVer("HTTP/1.1");
		this.setStatusCode("200");
		this.setReasonPhrase("OK");
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_CACHECONTROL, DefaultConfig.ID_UPNP_DISCOVERY_CACHECONTROL_VALUE);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_LOCATION, DefaultConfig.ID_UPNP_DISCOVERY_LOCATION_VALUE);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_NOTIFICATION_TYPE,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_EXT, "");
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_ST, searchType);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_SERVER,DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_USN,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(HttpHeaderName.ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG,"1");
		this.setHeaderValue(HttpHeaderName.ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG,"1");
		this.setHeaderValue(HttpHeaderName.ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG,this.device.getMulticastPort() + "");	
	}
	
	/**
	 * When to extract new UPnP device base info from UPnP request sent by remote device.
	 * 
	 * @return
	 */
	public UPnPDevice getDeviceBaseInfo() {
		UPnPDevice device = new UPnPDevice();
		if ( this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_USN) == null ||
				this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_USN).length() == 0 ) {
			// To make anonymous USN value.
			device.setUsn("usn:" + this.getHeaderValue("location"));
		} else {
			device.setUsn(this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_USN));
		}
		device.setHost(this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_POST));
		device.setLocation(this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_LOCATION));
		{
			String cacheValue = this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_CACHECONTROL);
			if ( cacheValue != null)
				device.setCacheControl(Integer.parseInt(cacheValue.substring(cacheValue.indexOf("max-age=")+8).trim()));
		}
		
		//device.setS(this.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_ST));
		device.setRemote(true);
		device.setReadyToUse(false);
		
		Logger.println(Logger.DEBUG, "Device Info listened by ssdp handler:\n" + device.toString());
		
		return device;
	}
	
	public String toString() {
		return super.toString();
	}
}

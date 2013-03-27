package com.elevenquest.sol.upnp.gena;

import java.net.NetworkInterface;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPBase;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.HTTPRequest;
import com.elevenquest.sol.upnp.network.ICommonReceiveHandler;
import com.elevenquest.sol.upnp.network.ICommonSendHandler;

/**
 * 
 * @author kyungpyo.park
 * 
 * The event publisher should maintan a list of subscriber which has 4 attributes.
 * Refer to Page 86 in UPnP-arch_DeviceArchitecture-v1.1
 */
public class Subscriber extends UPnPBase implements ICommonReceiveHandler, ICommonSendHandler  {

	HashMap<String, String> headerList = new HashMap<String, String>();

	public static String ID_UPNP_SUBSCRIBE_SUBSCRIBE = "SUBSCRIBE";
	public static String ID_UPNP_SUBSCRIBE_HOST = "HOST";
	public static String ID_UPNP_SUBSCRIBE_USER_AGENT = "USER-AGENT";
	public static String ID_UPNP_SUBSCRIBE_CALLBAC = "CALLBACK";
	public static String ID_UPNP_SUBSCRIBE_NT = "NT";
	public static String ID_UPNP_SUBSCRIBE_TIMEOUT = "TIMEOUT";
	
	public static String ID_UPNP_SUBSCRIBE_DATE = "DATE";
	public static String ID_UPNP_SUBSCRIBE_SERVER = "SERVER";
	public static String ID_UPNP_SUBSCRIBE_SID = "SID";
	public static String ID_UPNP_SUBSCRIBE_CONTENT_LENGTH = "CONTENT-LLENGTH";
	
	public static String ID_UPNP_SUBSCRIBE_UNSUBSCRIBE = "UNSUBSCRIBE";
	
	public static int TIMEOUT_DURATION_DEFAULT = 180; // sec 
	
	public String purblisherPath = null;
	public String host = null;
	public String userAgent = null;
	public String callback = null;
	public String timeoutSec = null;
	
	UPnPService service = null;
	boolean wantToSubscribe = true;
	
	public Subscriber(UPnPService service,boolean wantToSubscribe) {
		this.service = service;
		this.wantToSubscribe = wantToSubscribe;
		URL url = null;
		try {
			url = new URL(this.service.getEventsubUrl());
			this.purblisherPath = url.getPath();
			if ( url.getHost() != null && url.getHost().length() > 0 )
				this.host = url.getHost() + ":" + url.getPort();
			else {
				this.host = this.service.getDevice().getBaseHost();
			}
			this.userAgent = DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE;
			//this.callback = this.service.getDevice().getNetworkInterface().getInetAddresses()
		} catch ( Exception e ) {
			Logger.println(Logger.ERROR, "event sub URL of service[" + service.getServiceType() +"] of device[" + service.getDevice().getUuid() + "] is invalid.[" + service.getEventsubUrl() + "]");
		}
		
	}
	public String getHeaderValue(String headerId) {
		return headerList.get(headerId);
	}
	
	public void setHeaderValue(String headerId, String value) {
		headerList.put(headerId.toUpperCase(), value);
	}
	
	public Iterator<String> getHeaderKeyIterator() {
		return this.headerList.keySet().iterator();
	}
	@Override
	public Object getSendObject() throws Exception {
		StringBuffer fullMessage = new StringBuffer();
		if ( this.wantToSubscribe ) {
			fullMessage.append(ID_UPNP_SUBSCRIBE_SUBSCRIBE).append(" ");
			
			// append other header list.
			for ( Iterator<String> keyIter = headerList.keySet().iterator() ; keyIter.hasNext() ;) {
				String key = keyIter.next();
				fullMessage.append(key).append(':').append(headerList.get(key)).append('\n');
			}
		} else {
			// append other header list.
			for ( Iterator<String> keyIter = headerList.keySet().iterator() ; keyIter.hasNext() ;) {
				String key = keyIter.next();
				fullMessage.append(key).append(':').append(headerList.get(key)).append('\n');
			}
		}
		return fullMessage.toString().getBytes();
	}
	@Override
	public Object processAfterSend(Object returnValue) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void process(HTTPRequest request) {
		// TODO Auto-generated method stub
		
	}
	
}

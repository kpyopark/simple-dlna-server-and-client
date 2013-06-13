package com.elevenquest.sol.upnp.gena;

import java.io.ByteArrayInputStream;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.description.ServiceDescription;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPBase;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.HttpTcpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.xml.DDSXMLParser;

/**
 * 
 * @author kyungpyo.park
 * 
 * The event publisher should maintan a list of subscriber which has 4 attributes.
 * Refer to Page 86 in UPnP-arch_DeviceArchitecture-v1.1
 */
public class Subscriber extends UPnPBase implements IHttpRequestHandler, IHttpRequestSuplier  {

	HashMap<String, String> headerList = new HashMap<String, String>();

	public static String ID_UPNP_SUBSCRIBE_SUBSCRIBE = "SUBSCRIBE";
	public static String ID_UPNP_SUBSCRIBE_HOST = "HOST";
	public static String ID_UPNP_SUBSCRIBE_USER_AGENT = "USER-AGENT";
	public static String ID_UPNP_SUBSCRIBE_CALLBACK = "CALLBACK";
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
	public HttpResponse process(HttpRequest request) {
		HttpResponse response = new HttpResponse();
		if (request.getCommand().equals(ID_UPNP_SUBSCRIBE_SUBSCRIBE)) {
			response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200);
			response.setReasonPhrase(HttpResponse.HTTP_RESPONSE_REASON_PHRASE_200);
			response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
			// TODO:
		} else if (request.getCommand().equals(ID_UPNP_SUBSCRIBE_UNSUBSCRIBE)) {
			response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200);
			response.setReasonPhrase(HttpResponse.HTTP_RESPONSE_REASON_PHRASE_200);
			response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
			// TODO:
		} else {
			response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_401);
			response.setReasonPhrase(HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401);
			response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
		}
		return response;
	}
	@Override
	public HttpRequest getHTTPRequest() throws Exception {
		HttpRequest request = new HttpRequest();
		request.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
		request.setUrlPath("*");
		if ( this.wantToSubscribe ) {
			request.setCommand(ID_UPNP_SUBSCRIBE_SUBSCRIBE);
			if ( this.service.isRemote() && !this.service.isProgressingToRetrieve() ) {
				if ( this.service.getSubscribeId() != null &&
				this.service.getSubscribeId().length() > 5 ) {
					// it's already subscribe service. so it need to renew subscription id.
					URL eventSubUrl = null;
					try {
						eventSubUrl = new URL(this.service.getEventsubUrl());
						request.setHeaderValue("HOST", eventSubUrl.getHost() + ":" + eventSubUrl.getPort() );
					} catch ( Exception e ) {
						Logger.println(Logger.ERROR, e.getLocalizedMessage());
					}
					request.setHeaderValue("USER-AGENT", DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
					request.setHeaderValue("SID", this.service.getSubscribeId());
				} else {
					// When to subscribe to remote device 
					URL eventSubUrl = null;
					try {
						eventSubUrl = new URL(this.service.getEventsubUrl());
						request.setHeaderValue("HOST", eventSubUrl.getHost() + ":" + eventSubUrl.getPort() );
					} catch ( Exception e ) {
						Logger.println(Logger.ERROR, e.getLocalizedMessage());
					}
					request.setHeaderValue("USER-AGENT", DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
					request.setHeaderValue(ID_UPNP_SUBSCRIBE_CALLBACK, "<" + this.service.getDeliveryUrl() + ">");
					request.setHeaderValue("NT", "upnp:event");
					request.setHeaderValue("TIMEOUT", "Second-180");
					Logger.println(Logger.DEBUG, "[SUBSCRIBER] Callback :[" + request.getHeaderValue(ID_UPNP_SUBSCRIBE_CALLBACK) + "]");
				}
			} else {
				throw new AbnormalException("This service[" + this.service.getServiceType() + ":" + this.service.getServiceId() + "] isn't availiable service to listen event request.");
			}
		} else {
			request.setCommand(ID_UPNP_SUBSCRIBE_UNSUBSCRIBE);
			URL eventSubUrl = null;
			try {
				eventSubUrl = new URL(this.service.getEventsubUrl());
				request.setHeaderValue("HOST", eventSubUrl.getHost() + ":" + eventSubUrl.getPort() );
			} catch ( Exception e ) {
				Logger.println(Logger.ERROR, e.getLocalizedMessage());
			}
			request.setHeaderValue("USER-AGENT", DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
			request.setHeaderValue("SID", this.service.getSubscribeId());
		}
		return request;
	}
	@Override
	public void processAfterSend(HttpResponse response) {
		try {
			System.out.println("Subscribe event response Status value:" + response.getStatusCode() );
			/* Test Code */
			java.util.List<String> headers = response.getHeaderNames();
			for ( String header : headers ) Logger.println(Logger.INFO, "<GENA> response [" + header + "]:[" + response.getHeaderValue(header) + "]");
			/*           */
			if ( response.getStatusCode().equals(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200) ) {
				if ( response.getHeaderValue("SID") != null && response.getHeaderValue("SID").length() > 5 ) {
					this.service.setSubscribeId(response.getHeaderValue("SID"));
					this.service.setSubscribed(true);
				} else {
					this.service.setSubscribeId(null);
					this.service.setSubscribed(false);
				}
			}
			else
			{
				System.out.println("To subscribe to device[" + this.host + "] failed. cause : " + response.toString() );
			}
		} catch ( Exception e ) {
			// TODO : Exception processing is required.
			e.printStackTrace();
		}
	}
	
}

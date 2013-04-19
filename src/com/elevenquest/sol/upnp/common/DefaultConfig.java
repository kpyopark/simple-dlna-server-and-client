package com.elevenquest.sol.upnp.common;

public class DefaultConfig {
	public final static int DEFAULT_UPNP_MULTICAST_PORT = 1900; // port number used in UPNP discovery message.
	
	public final static String ID_UPNP_DISCOVERY_HOST_VALUE = "239.255.255.250:1900";
	public final static String ID_UPNP_DISCOVERY_CACHECONTROL_VALUE = "300";
	public final static String ID_UPNP_DISCOVERY_LOCATION_VALUE = "http://www.korea.com";
	public final static String ID_UPNP_DISCOVERY_SERVER_VALUE = "WindowsNT UPnP/1.1 simpledlna/draft";
	
	public final static String ID_UPNP_DEVICE_PLACE_HOLDER = "DEVICE_ID";
	public final static String ID_UPNP_SERVICE_PLACE_HOLDER = "SERVICE_ID";
	
	public final static String ID_UPNP_DESCRIPTION_DDD = "ddd.xml";
	public final static String ID_UPNP_DESCRIPTION_SDD = "sdd.xml";
	public final static String ID_UPNP_DESCRIPTION_URL_PATH_DDD = "/" + ID_UPNP_DEVICE_PLACE_HOLDER + "/" + ID_UPNP_DESCRIPTION_DDD;
	public final static String ID_UPNP_DESCRIPTION_URL_PATH_SDD = "/" + ID_UPNP_DEVICE_PLACE_HOLDER + "/" + ID_UPNP_DESCRIPTION_SDD;
	
	public final static String ID_UPNP_CONTROL_ACTION = "action.do";
	public final static String ID_UPNP_CONTROL_URL_PATH_ACTION = "/" + ID_UPNP_DEVICE_PLACE_HOLDER + "/" + ID_UPNP_SERVICE_PLACE_HOLDER + "/" + ID_UPNP_CONTROL_ACTION;

	public final static String ID_UPNP_EVENT_SUBSCRIBE = "subscribe.do";
	public final static String ID_UPNP_EVENT_URL_PATH_SUBSCRIBE = "/" + ID_UPNP_DEVICE_PLACE_HOLDER + "/" + ID_UPNP_EVENT_SUBSCRIBE;
	public final static String ID_UPNP_EVENT_NOTIFY = "notify.do";
	public final static String ID_UPNP_EVENT_URL_PATH_NOTIFY = "/" + ID_UPNP_EVENT_NOTIFY;

	public final static String ID_UPNP_PRESENTATION_MAIN = "main.html";
	public final static String ID_UPNP_PRESENTATION_URL_PATH = "/" + ID_UPNP_PRESENTATION_MAIN;
}

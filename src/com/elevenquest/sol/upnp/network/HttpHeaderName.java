package com.elevenquest.sol.upnp.network;


public class HttpHeaderName {
	
	public final static String ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG = "BOOTID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_NEXTBOTID_UPNP_ORG = "NEXTBOOTID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG = "CONFIGID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG = "SEARCHPORT.UPNP.ORG";

	public final static String ID_UPNP_HTTP_HEADER_POST = "HOST";
	public final static String ID_UPNP_HTTP_HEADER_CACHECONTROL = "CACHE-CONTROL";
	public final static String ID_UPNP_HTTP_HEADER_LOCATION = "LOCATION";
	public final static String ID_UPNP_HTTP_HEADER_NOTIFICATION_TYPE = "NT";
	public final static String ID_UPNP_HTTP_HEADER_NT_SUBTYPE = "NTS";
	public final static String ID_UPNP_HTTP_HEADER_SERVER = "SERVER";
	public final static String ID_UPNP_HTTP_HEADER_USN = "USN";
	public final static String ID_UPNP_HTTP_HEADER_MAN = "MAN";
	public final static String ID_UPNP_HTTP_HEADER_MX = "MX";
	public final static String ID_UPNP_HTTP_HEADER_ST = "ST";
	public final static String ID_UPNP_HTTP_HEADER_CONTENTLENGTH = "CONTENT-LENGTH";
	public final static String ID_UPNP_HTTP_HEADER_TRANSFERENCODING = "TRANSFER-ENCODING";
	public final static String ID_UPNP_HTTP_HEADER_CONTENTTYPE = "CONTENT-TYPE";
	public final static String ID_UPNP_HTTP_HEADER_SID = "SID";
	public final static String ID_UPNP_HTTP_HEADER_SEQ = "SEQ";
	
	public final static String ID_START_LINE_COMMAND_NOTIFY = "NOTIFY";
	public final static String ID_START_LINE_COMMAND_SEARCH = "M-SEARCH";
	
	public final static String ID_START_LINE_NOTIFY = "NOTIFY * HTTP/1.1";
	public final static String ID_START_LINE_SEARCH = "M-SEARCH * HTTP/1.1";
	public final static String ID_START_LINE_NORMAL = "HTTP/1.1 200 OK";
	
	public final static String ID_NT_SUBTYPE_SSDPALIVE = "ssdp:alive";
	public final static String ID_NT_SUBTYPE_SSDPBYEBYE = "ssdp:byebye";
	public final static String ID_NT_SUBTYPE_SSDPUPDATE = "ssdp:update";
	public final static String ID_NT_SUBTYPE_SSDPDISCOVER = "ssdp:discover";
	
	public final static String STARTLINE_ID_LIST[] = {
		ID_START_LINE_NOTIFY, ID_START_LINE_SEARCH, ID_START_LINE_NORMAL
	};
	
	public final static String REQUIRED_ID_LIST[] = {
		ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG, ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG, ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG};
	
}

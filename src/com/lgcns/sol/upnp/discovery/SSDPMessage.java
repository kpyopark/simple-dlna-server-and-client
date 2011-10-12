package com.lgcns.sol.upnp.discovery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class SSDPMessage {
	String startLine = null;
	HashMap<String, String> headerList = new HashMap<String, String>();
	boolean needValidation = false;
	
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
	
	public final static String ID_START_LINE_NOTIFY = "NOTIFY * HTTP/1.1";
	public final static String ID_START_LINE_SEARCH = "M-SEARCH * HTTP/1.1";
	public final static String ID_START_LINE_NORMAL = "HTTP/1.1 200 OK";
	
	public final static String ID_NT_SUBTYPE_SSDPALIVE = "ssdp:alive";
	
	public final static String STARTLINE_ID_LIST[] = {
		ID_START_LINE_NOTIFY, ID_START_LINE_SEARCH, ID_START_LINE_NORMAL
	};
	
	public final static String REQUIRED_ID_LIST[] = {
		ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG, ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG, ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG};
	
	public boolean needBootIdUpdated(UPnPDevice device) {
		// if the device has changed its IP, it must change BOOTID value also.
		// if the device has rebooted, it must change BOOTID value also. (so, BOOID value has some time stamp value to differ older state.)
		return false;
	}
	
	public String getStartLine() {
		return this.startLine;
	}
	
	public void setStartLine(String startLine) {
		this.startLine= startLine;
	}
	
	public String getHeaderValue(String headerId) {
		return headerList.get(headerId);
	}
	
	public void setHeaderValue(String headerId, String value) {
		headerList.put(headerId, value);
	}
	
	public Iterator<String> getHeaderKeyIterator() {
		return this.headerList.keySet().iterator();
	}
	
	public byte[] toBytes() {
		StringBuffer fullMessage = new StringBuffer();
		// append start header.
		fullMessage.append(startLine).append('\n');
		// append other header list.
		for ( Iterator<String> keyIter = headerList.keySet().iterator() ; keyIter.hasNext() ;) {
			String key = keyIter.next();
			fullMessage.append(key).append(':').append(headerList.get(key)).append('\n');
		}
		return fullMessage.toString().getBytes();
	}
	
	public boolean parse(byte[] content) throws Exception {
		
		boolean isValid = true;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content)));
			this.startLine = "";
			this.headerList.clear();
			String keyAndValue = null;
			this.startLine = br.readLine();
			if ( this.startLine == null )
				return false;
			while( (keyAndValue = br.readLine()) != null ) {
				int totalLength = keyAndValue.length();
				for ( int pos = 0; pos < totalLength; pos++ ) {
					if ( keyAndValue.charAt(pos) == ':' ) {
						this.headerList.put(keyAndValue.substring(0,pos), (totalLength > (pos + 1))? keyAndValue.substring(pos+1) : "" );
						break;
					}
				}
			}
			// validate the contents of header list.
			if ( needValidation ) {
				isValid = false;
				for( String startLine : STARTLINE_ID_LIST ) {
					isValid = this.startLine.equals(startLine);
					if ( isValid )
						break;
				}
				if ( isValid ) {
					for( String mandatoryKey : REQUIRED_ID_LIST) {
						if ( !this.headerList.containsKey(mandatoryKey) ) {
							isValid = false;
							break;
						}
					}
				}
			}
		} finally {
			if ( br != null ) try {
				br.close();
			} catch ( Exception e ) { e.printStackTrace(); }
		}
		return isValid;
	}
}

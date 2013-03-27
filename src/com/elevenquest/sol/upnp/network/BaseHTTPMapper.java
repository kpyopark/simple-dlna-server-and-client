package com.elevenquest.sol.upnp.network;

import java.util.ArrayList;
import java.util.Iterator;

import com.elevenquest.sol.upnp.model.UPnPDevice;

public class BaseHTTPMapper {
	String startLine = null;
	boolean needValidation = false;
	UPnPDevice device = null;
	ArrayList<String> headerNames = null;
	ArrayList<String> headerValues = null;

	public String getStartLine() {
		return this.startLine;
	}
	
	public void setStartLine(String startLine) {
		this.startLine= startLine;
	}
	
	public String getHeaderValue(String headerId) {
		int cnt = headerNames.indexOf(headerId);
		if ( cnt < 0 )
			return null;
		return headerValues.get(cnt);
	}
	
	public ArrayList<String> getHeaderValues(String headerId) {
		ArrayList<String> rtnHeaderValues = new ArrayList<String>();
		for ( int cnt = 0; cnt < this.headerNames.size(); cnt++ ) {
			if ( headerId != null && headerId.equals(this.headerNames.get(cnt)) )
				rtnHeaderValues.add(this.headerValues.get(cnt));
		}
		return rtnHeaderValues;
	}
	
	public void addHeaderValue(String headerName, String value) {
		this.headerNames.add(headerName);
		this.headerValues.add(value);
	}
	
	public boolean containsHeader(String headerName) {
		return this.headerNames.contains(headerName);
	}
	
	public void setHeaderValue(String headerName, String value) {
		if ( this.containsHeader(headerName) ) {
			int cnt = this.headerNames.indexOf(headerName);
			this.headerNames.set(cnt, headerName);
			this.headerValues.set(cnt, value);
		} else {
			addHeaderValue(headerName, value);
		}
	}
	
	public ArrayList<String> getHeaderNames() {
		return this.headerNames;
	}
	
	public ArrayList<String> getHeaderValues() {
		return this.headerValues;
	}

	public HTTPRequest getHTTPRequest() throws Exception {
		HTTPRequest request = new HTTPRequest();
		StringBuffer fullMessage = new StringBuffer();
		// append start header.
		fullMessage.append(startLine).append('\n');
		// append other header list.
		for ( Iterator<String> keyIter = headerList.keySet().iterator() ; keyIter.hasNext() ;) {
			String key = keyIter.next();
			fullMessage.append(key).append(':').append(headerList.get(key)).append('\n');
		}
		return request;
	}

}

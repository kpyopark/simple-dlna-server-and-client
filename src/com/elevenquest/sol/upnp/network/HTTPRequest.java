package com.elevenquest.sol.upnp.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.elevenquest.sol.upnp.common.Logger;

public class HTTPRequest {
	InputStream streamBody = null;
	byte[] arrayBody = null;
	
	ArrayList<String> headerNames = null;
	ArrayList<String> headerValues = null;
	int headerCount = 0;
	
	String command = "";
	String urlPath = "";
	String httpVer = "";
	
	Exception processingException = null;
	
	public static String HTTP_REQUEST_COMMAND_GET = "GET";
	public static String HTTP_REQUEST_COMMAND_POST = "POST";

	
	// When to use same keys in one http connection, so we can't use HashMap class (in java)
	
	public HTTPRequest() {
		this(HTTP_REQUEST_COMMAND_GET);
	}
	
	public HTTPRequest(HTTPRequest oldOne) {
		this.streamBody = oldOne.streamBody;
		this.arrayBody = oldOne.arrayBody;
		this.headerNames = oldOne.headerNames;
		this.headerValues = oldOne.headerValues;
		this.headerCount = oldOne.headerCount;
		this.command = oldOne.command;
		this.urlPath = oldOne.urlPath;
		this.httpVer = oldOne.httpVer;
		this.processingException = oldOne.processingException;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public HTTPRequest(String command) {
		this.command = command;
		headerNames = new ArrayList<String>();
		headerValues = new ArrayList<String>();
		headerCount = 0;
		processingException = null;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public void addHeader(String headerName, String headerValue) {
		headerNames.add(headerName);
		headerValues.add(headerValue);
		headerCount++;
	}
	
	public int getHeaderCount() {
		return headerCount;
	}
	
	public ArrayList<String> getHeaderNames() {
		return this.headerNames;
	}
	
	public ArrayList<String> getHeaderValues() {
		return this.headerValues;
	}

	public String[] getHeaderList(String headerName) {
		ArrayList<String> list = new ArrayList<String>();
		for (int cnt = 0; cnt < headerNames.size() ; cnt++ ) {
			if ( headerNames.get(cnt).equals(headerName) )
				list.add(headerValues.get(cnt));
		}
		return list.toArray(new String[0]);
	}
	
	public String getHeaderValue(String headerName) {
		for (int cnt = 0; cnt < headerNames.size() ; cnt++ ) {
			if ( headerNames.get(cnt).equals(headerName) )
				return headerValues.get(cnt);
		}
		return null;
	}
	
	public String getHeaderName(int cnt) {
		return this.headerNames.get(cnt);
	}
	
	public String getHeaderValue(int cnt) {
		return this.headerValues.get(cnt);
	}
	
	public byte[] getBodyArray() {
		if ( arrayBody != null ) {
			return arrayBody;
		} else if ( streamBody != null ) {
			byte[] buffer = new byte[1024];
			int length = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				while( (length = streamBody.read(buffer, 0, 1024) ) != -1 ) {
					baos.write(buffer, 0, length);
				}
			} catch ( Exception e ) {
				// If there is an error before to send data. It causes troubles to remote devices.
				// so, we should prevent to send these data.
				Logger.println(Logger.ERROR, "Cause Exception:" + e.getMessage());
			}
			arrayBody = baos.toByteArray();
		} else {
			arrayBody = new byte[0];
		}
		return arrayBody;
	}
	
	public Exception getProcessingException() {
		return this.processingException;
	}
	
	public InputStream getBodyInputStream() {
		if ( this.streamBody != null ) {
			return this.streamBody;
		} else if ( arrayBody != null ) {
			this.streamBody =  new ByteArrayInputStream(this.arrayBody);
		} else {
			this.streamBody = new ByteArrayInputStream(new byte[0]);
		}
		return this.streamBody;
	}
	
	public void setBodyArray(byte[] bodyArray) {
		this.arrayBody = bodyArray;
	}
	
	public void setBodyInputStream(InputStream is) {
		this.streamBody = is;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getHttpVer() {
		return httpVer;
	}

	public void setHttpVer(String httpVer) {
		this.httpVer = httpVer;
	}
	
	public void setHeaderValue(String headerName, String headerValue) {
		boolean isOverwrite = false;
		for (int cnt = 0; cnt < headerNames.size() ; cnt++ ) {
			if ( headerNames.get(cnt).equals(headerName) ) {
				headerValues.set(cnt, headerValue);
				isOverwrite = true;
			}
		}
		if ( !isOverwrite ) {
			headerNames.add(headerName);
			headerValues.add(headerValue);
		}
	}
	
}

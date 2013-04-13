package com.elevenquest.sol.upnp.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.elevenquest.sol.upnp.common.Logger;

public class HttpResponse {
	InputStream streamBody = null;
	byte[] arrayBody = null;
	
	ArrayList<String> headerNames = null;
	ArrayList<String> headerValues = null;
	int headerCount = 0;
	
	String httpVer = null;
	String statusCode = null;
	String reasonPhrase = null;
	
	public static String HTTP_RESPONSE_STATUS_CODE_200 = "200";
	public static String HTTP_RESPONSE_STATUS_CODE_401 = "401";

	public static String HTTP_RESPONSE_REASON_PHRASE_200 = "OK";
	public static String HTTP_RESPONSE_REASON_PHRASE_401 = "NOT IMPLEMENTED YET";
	
	
	Exception processingException = null;
	
	// When to use same keys in one http connection, so we can't use HashMap class (in java)
	public HttpResponse() {
		headerNames = new ArrayList<String>();
		headerValues = new ArrayList<String>();
		headerCount = 0;
		processingException = null;
	}
	
	public void addHeader(String headerName, String headerValue) {
		headerNames.add(headerName);
		headerValues.add(headerValue);
		headerCount++;
	}
	
	public int getHeaderCount() {
		return headerCount;
	}
	
	public String[] getHeaderList(String headerName) {
		ArrayList<String> list = new ArrayList<String>();
		for (int cnt = 0; cnt < headerNames.size() ; cnt++ ) {
			if ( headerNames.get(cnt).equalsIgnoreCase(headerName) )
				list.add(headerValues.get(cnt));
		}
		return list.toArray(new String[0]);
	}
	
	public String getHeaderValue(String headerName) {
		for (int cnt = 0; cnt < headerNames.size() ; cnt++ ) {
			if ( headerNames.get(cnt).equalsIgnoreCase(headerName) )
				return headerValues.get(cnt);
		}
		return null;
	}
	
	public ArrayList<String> getHeaderNames() {
		return this.headerNames;
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
		if ( arrayBody != null ) {
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
		this.getBodyArray();
	}

	public String getHttpVer() {
		return httpVer;
	}

	public void setHttpVer(String httpVer) {
		this.httpVer = httpVer;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
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

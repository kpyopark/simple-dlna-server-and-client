package com.elevenquest.sol.upnp.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.elevenquest.sol.upnp.common.Logger;

/**
 * When to receive http request from client,we should parse and process it.
 * This class has capabilities of divide request int two parts - header parts & body parts -.
 * 
 * @author kyungpyo.park
 *
 */
public class HttpParser {
	
	InputStream inputStream = null;
	int length = 0;
	HttpBaseStructure baseInfo = null;
	HttpRequest request = null;
	HttpResponse response = null;
	
	public HttpParser(InputStream is) {
		this.inputStream = is;
		try {
			this.length = this.inputStream.available();
		} catch ( Exception e ) {
			Logger.println(Logger.ERROR, "To retrieve availiable bytes size from input stream failed.");
		}
	}
	
	public HttpParser(byte[] contents) {
		this.inputStream = new ByteArrayInputStream(contents);
		try {
			this.length = this.inputStream.available();
		} catch ( Exception e ) {
			Logger.println(Logger.ERROR, "To retrieve availiable bytes size from input stream failed.");
		}
	}
	
	private String readLine() throws IOException {
		int curByte = -1;
		String rtn = null;
		ByteArrayOutputStream baos = null;
		boolean isPrevCharCr = false;
		try {
			while( ( curByte = this.inputStream.read() ) != -1 ) {
				if ( baos == null )
					baos = new ByteArrayOutputStream();
				if ( curByte == '\n' && isPrevCharCr ) {
					break;
				}
				if ( curByte == '\r' )
					isPrevCharCr = true;
				else
					isPrevCharCr = false;
				baos.write(curByte);
			}
			if ( baos == null )
				return null;
			else if ( curByte == -1 )
				rtn = "";
			else
				rtn = baos.toString().trim();
		} finally {
			if ( baos != null ) try { baos.close(); } catch ( Exception e1 ) { e1.printStackTrace(); } 
		}
		//Logger.println(Logger.DEBUG, "a line read from packet.:" + rtn);
		return rtn;
	}

	private byte[] getBody() throws IOException {
		ByteArrayOutputStream baos = null;
		byte[] rtn = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ( ( len = this.inputStream.read(buffer, 0, 1024)) != -1 ) {
				baos.write(buffer,0,len);
			}
			baos.flush();
			rtn = baos.toByteArray();
		} finally {
			if ( baos != null ) try { baos.close(); } catch ( Exception e1 )  { e1.printStackTrace(); }
		}
		return rtn;
	}
	
	private void close() {
		if ( inputStream != null ) try { inputStream.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	}
	
	public HttpBaseStructure parse() throws IOException {
		baseInfo = parseHttpLines();
		String startLine = baseInfo.startLine;
		String firstToken, secondToken, thirdToken;
		int startPosition = 0, lastPosition = 0, totalLength = startLine.length();
		lastPosition = startLine.indexOf(' ', startPosition);
		if ( lastPosition == -1 ) {
			Logger.println(Logger.WARNING, "[HTTP Parser] This is not http start line.[" + baseInfo.startLine + "]");
			this.close();
			return baseInfo;
		}
		firstToken = startLine.substring(startPosition, (lastPosition - startPosition));
		startPosition = lastPosition + 1;
		if ( startPosition >= totalLength ) {
			Logger.println(Logger.WARNING, "[HTTP Parser] This is not http start line.[" + baseInfo.startLine + "]");
			this.close();
			return baseInfo;
		}
		lastPosition = startLine.indexOf(' ',startPosition);
		if ( lastPosition == -1 ) {
			Logger.println(Logger.WARNING, "[HTTP Parser] This is not http start line.[" + baseInfo.startLine + "]");
			this.close();
			return baseInfo;
		}
		secondToken = startLine.substring(startPosition, lastPosition);
		startPosition = lastPosition + 1;
		if ( startPosition >= totalLength ) {
			Logger.println(Logger.WARNING, "[HTTP Parser] This is not http start line.[" + baseInfo.startLine + "]");
			this.close();
			return baseInfo;
		}
		thirdToken = startLine.substring(startPosition);
		
		if ( firstToken.equalsIgnoreCase("HTTP/1.0") || firstToken.equalsIgnoreCase("HTTP/1.1") ) {
			// It is supposed to be request data.
			response = new HttpResponse();
			request = null;
			response.headerNames = baseInfo.headerNames;
			response.headerValues = baseInfo.headerValues;
			response.setHttpVer(firstToken);
			response.setStatusCode(secondToken);
			response.setReasonPhrase(thirdToken);
			response.setBodyArray(baseInfo.body);
			baseInfo.response = response;
			baseInfo.request = null;
		} else {
			// It is supposed to be request data.
			request = new HttpRequest();
			response = null;
			request.headerNames = baseInfo.headerNames;
			request.headerValues = baseInfo.headerValues;
			request.setCommand(firstToken);
			request.setUrlPath(secondToken);
			request.setHttpVer(thirdToken);
			request.setBodyArray(baseInfo.body);
			baseInfo.request = request;
			baseInfo.response = null;
		}
		this.close();
		return baseInfo;
	}
	
	public boolean isHTTPRequest() {
		return ( request != null ) ? true : false;
	}

	public HttpRequest getHTTPRequest() throws Exception {
		return request;
	}
	
	public boolean isHTTPResponse() {
		return ( response != null ) ? true : false;
	}

	public HttpResponse getHTTPResponse() throws IOException {
		return response;
	}

	protected HttpBaseStructure parseHttpLines() throws IOException {
		HttpBaseStructure baseStruct = new HttpBaseStructure();
		String aLine = "";
		String key = "";
		String value = "";
		boolean isHeader = true;
		boolean isStartLine = true;
		try {
			while ( ( aLine = readLine() ) != null ) {
				if ( aLine.trim().length() == 0 ) {		// Header / Body separated by knew line character.
					isHeader = false;
				}
				if ( isHeader) {
					// start line
					if ( isStartLine ) {
						isStartLine = false;
						baseStruct.startLine = aLine;
						Logger.println(Logger.DEBUG, "current time : [" + System.currentTimeMillis() + "] start line:" + aLine + " length :" + this.length);
					} else {
						boolean isValid = false;
						if ( aLine.length() > 0 && ( aLine.charAt(0) == ' ' || aLine.charAt(0) == '\t' ) ) {
							// It's a line of multiline text.
							value += "\n" + aLine;
							if ( key != null && key.length() > 0 ) {
								int pos = baseStruct.headerNames.indexOf(key);
								baseStruct.headerValues.set(pos, value);
							}
						} else {
							for ( int pos = 0 ; pos < aLine.length() ; pos++ ) {
								if ( aLine.charAt(pos) == ':' ) {
									key = aLine.substring(0,pos);
									value = (pos + 1 < aLine.length()) ? aLine.substring(pos+1).trim() : ""; 
									isValid = true;
									break;
								}
							}
							if (isValid) {
								baseStruct.headerNames.add(key);
								baseStruct.headerValues.add(value);
							} else {
								key = "";
								value = "";
							}
						}
					}
				} else {
					// TODO : Need to be modified for preparation of chunked contents.
					baseStruct.body = this.getBody();
				}
			}
		} finally {
			this.close();
		}
		return baseStruct;
	}
	
}

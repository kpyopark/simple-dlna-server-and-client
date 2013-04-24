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
	
	class HttpBaseStructure {
		String startLine = null;
		ArrayList<String> headerNames = new ArrayList<String>();
		ArrayList<String> headerValues = new ArrayList<String>();
		byte[] body = null;
	}
	
	protected HttpBaseStructure parseHttpLines() throws IOException {
		HttpBaseStructure baseStruct = new HttpBaseStructure();
		String aLine = "";
		String key = "";
		String value = "";
		boolean isHeader = true;
		boolean isStartLine = true;
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
				baseStruct.body = this.getBody();
			}
		}
		this.close();
		return baseStruct;
	}

	public HttpRequest parseHTTPRequest() throws Exception {
		
		HttpRequest request = new HttpRequest();
		HttpBaseStructure baseInfo = parseHttpLines();
		request.headerNames = baseInfo.headerNames;
		request.headerValues = baseInfo.headerValues;
		StringTokenizer st = new StringTokenizer(baseInfo.startLine, " ");
		boolean isValidRequest = true;
		if ( isValidRequest && st.hasMoreTokens() )
			request.setCommand(st.nextToken());
		else {
			isValidRequest = false;
			Logger.println(Logger.ERROR, "In http request, there is no url path. command is " + request.getCommand() + "." );
		}
		if ( isValidRequest && st.hasMoreTokens() )
			request.setUrlPath(st.nextToken());
		else {
			isValidRequest = false;
			Logger.println(Logger.ERROR, "In http request, there is no url path. command is " + request.getCommand() + "." );
		}
		if ( isValidRequest && st.hasMoreTokens() )
			request.setHttpVer(st.nextToken());
		else {
			isValidRequest = false;
			Logger.println(Logger.ERROR, "In http request, there is no url path. command is " + request.getCommand() + "." );
		}
		return request;
	}

	public HttpResponse parseHTTPResponse() throws Exception {
		
		HttpResponse response = new HttpResponse();
		HttpBaseStructure baseInfo = parseHttpLines();
		response.headerNames = baseInfo.headerNames;
		response.headerValues = baseInfo.headerValues;
		StringTokenizer st = new StringTokenizer(baseInfo.startLine, " ");
		boolean isValidResponse = true;
		if ( isValidResponse && st.hasMoreTokens() )
			response.setHttpVer(st.nextToken());
		else {
			isValidResponse = false;
			Logger.println(Logger.ERROR, "In http response, there is no http version. status line is " + baseInfo.startLine + "." );
		}
		if ( isValidResponse && st.hasMoreTokens() ) {
			String strStatusCode = st.nextToken();
			try {
				int statusCode = Integer.parseInt(strStatusCode);
				response.setStatusCode(strStatusCode);
			} catch (NumberFormatException nfe) {
				isValidResponse = false;
				Logger.println(Logger.ERROR, "In http response, there is invalid status code. status code is "+ strStatusCode + ".");
			}
		}
		else {
			isValidResponse = false;
			Logger.println(Logger.ERROR, "In http response, there is no status code. status line is " + baseInfo.startLine + "." );
		}
		if ( isValidResponse && st.hasMoreTokens() )
			response.setReasonPhrase(st.nextToken());
		else {
			isValidResponse = false;
			Logger.println(Logger.ERROR, "In http response, there is status reason. status line is " + baseInfo.startLine + "." );
		}
		this.close();
		return response;
	}
}

package com.elevenquest.sol.upnp.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import com.elevenquest.sol.upnp.common.Logger;

/**
 * When to receive http request from client,we should parse and process it.
 * This class has capabilities of divide request int two parts - header parts & body parts -.
 * 
 * @author kyungpyo.park
 *
 */
public class HTTPParser {
	
	InputStream inputStream = null;
	
	public HTTPParser(InputStream is) {
		this.inputStream = is;
	}
	
	public HTTPParser(byte[] contents) {
		this.inputStream = new ByteArrayInputStream(contents);
	}
	
	private String readLine() throws Exception {
		int curByte = -1;
		String rtn = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			while( ( curByte = this.inputStream.read() ) != -1 ) {
				Logger.println(Logger.DEBUG,  curByte );
				if ( curByte != '\n' )
					break;
				baos.write(curByte);
			}
			if ( baos.size() == 0 && curByte == -1 )
				rtn = null;
			else
				rtn = baos.toString();
		} finally {
			if ( baos != null ) try { baos.close(); } catch ( Exception e1 ) { e1.printStackTrace(); } 
		}
		return rtn;
	}

	private byte[] getBody() throws Exception {
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

	public HTTPRequest parse() throws Exception {
		
		HTTPRequest request = new HTTPRequest();
		String aLine = "";
		boolean isHeader = true;
		while ( ( aLine = readLine() ) != null ) {
			if ( aLine.trim().length() == 0 ) {		// Header / Body separated by knew line character.
				isHeader = false;
			}
			if ( isHeader) {
				String key = "";
				String value = "";
				for ( int pos = 0 ; pos < aLine.length() ; pos++ ) {
					if ( aLine.charAt(pos) == ':' )
						key = aLine.substring(0,pos);
						value = (pos + 1 < aLine.length()) ? aLine.substring(pos+1) : ""; 
				}
				// start line
				if ( key.length() == 0 ) {
					StringTokenizer st = new StringTokenizer(aLine, " ");
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
				} else if ( key.length() > 0 ) {
					request.addHeader(key, value);
				}
			} else {
				request.setBodyArray(this.getBody());
			}
		}
		return request;
	}
	
}

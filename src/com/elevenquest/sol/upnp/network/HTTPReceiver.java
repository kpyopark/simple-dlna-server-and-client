package com.elevenquest.sol.upnp.network;

import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPReceiver {
	NetworkInterface intf;
	int port;
	
	/**
	 * @param intf
	 * @param port
	 * @param targetAddr
	 */
	public HTTPReceiver(NetworkInterface intf, int port) {
		this.intf = intf;
		this.port = port;
	}
	
	public byte[] receiveData(ArrayList<String> keyList, HashMap<String, String> properties) throws Exception {
		
		ServerSocket serverSoc = null;
		Socket clientSoc = null;
		HTTPReader reader = null;
		byte[] body = null;
		try {
			serverSoc = new ServerSocket(this.port);
			clientSoc = serverSoc.accept();
			reader = new HTTPReader(clientSoc);
			String aLine = "";
			boolean isHeader = true;
			while ( ( aLine = reader.readLine() ) != null ) {
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
					if ( key.length() > 0 ) {
						keyList.add(key);
						properties.put(key, value);
					}
				} else {
					body = reader.getBody();
				}
			}
		} finally {
			if ( reader != null ) try { reader.close(); } catch ( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( clientSoc != null) try { clientSoc.close(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( serverSoc != null) try { serverSoc.close(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}
		return body;
	}
}

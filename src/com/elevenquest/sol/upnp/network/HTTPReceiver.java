package com.elevenquest.sol.upnp.network;

import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;

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
	
	public HTTPRequest listen() throws Exception {
		ServerSocket serverSoc = null;
		Socket clientSoc = null;
		HTTPParser reader = null;
		HTTPRequest request = null;
		try {
			serverSoc = new ServerSocket(this.port);
			clientSoc = serverSoc.accept();
			reader = new HTTPParser(clientSoc.getInputStream());
			request = reader.parseHTTPRequest();
		} finally {
			if ( clientSoc != null) try { clientSoc.close(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( serverSoc != null) try { serverSoc.close(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}
		return request;
	}
}

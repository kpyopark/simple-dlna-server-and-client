package com.elevenquest.sol.upnp.network;

import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;

import com.elevenquest.sol.upnp.common.Logger;

public class HttpTcpReceiver extends HttpRequestReceiver {
	NetworkInterface intf;
	int port;
	ServerSocket serverSoc = null;

	/**
	 * @param intf
	 * @param port
	 * @param targetAddr
	 */
	public HttpTcpReceiver(NetworkInterface intf, int port) {
		this.intf = intf;
		this.port = port;
	}
	
	public void initSocket() {
		try {
			serverSoc = new ServerSocket(this.port);
		} catch (Exception e) {
			Logger.println(Logger.ERROR, "To init socket failed. listen port[" + port + "].\n");
		}
	}
	
	public HttpRequest listen() throws Exception {
		HttpParser parser = null;
		HttpRequest request = null;
		Socket clientSoc = null;
		try {
			if ( serverSoc == null ) {
				initSocket();
			}
			clientSoc = serverSoc.accept();
			parser = new HttpParser(clientSoc.getInputStream());
			parser.parse();
			if ( parser.isHTTPRequest() ) {
				request = parser.getHTTPRequest();
			}
		} finally {
			if ( clientSoc != null) try { clientSoc.close(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}
		return request;
	}
	
	public void close() {
		if ( serverSoc != null) {
			try { 
				serverSoc.close();
				serverSoc = null;
			} catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}
	}

}

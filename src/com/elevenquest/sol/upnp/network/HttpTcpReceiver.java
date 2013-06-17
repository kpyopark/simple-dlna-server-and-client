package com.elevenquest.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

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
		HttpParser reader = null;
		HttpRequest request = null;
		Socket clientSoc = null;
		try {
			if ( serverSoc == null ) {
				initSocket();
			}
			clientSoc = serverSoc.accept();
			reader = new HttpParser(clientSoc.getInputStream());
			request = reader.parseHTTPRequest();
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

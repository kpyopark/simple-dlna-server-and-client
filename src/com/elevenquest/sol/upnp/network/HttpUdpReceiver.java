package com.elevenquest.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

import com.elevenquest.sol.upnp.common.Logger;


public class HttpUdpReceiver extends HttpRequestReceiver {
	NetworkInterface intf;
	int port;
	InetAddress listenAddr = null;
	DatagramSocket serverSocket = null;
	DatagramPacket packet = null;
	
	public HttpUdpReceiver(NetworkInterface intf, InetAddress listenAddr, int port) {
		this.intf = intf;
		this.listenAddr = listenAddr;
		this.port = port;
	}
	
	public HttpRequest listen() throws Exception {
		HttpRequest request = null;
		DatagramPacket packet = null;
		if ( serverSocket != null ) {
			serverSocket.close();
			serverSocket = null;
		}
		if ( listenAddr.isMulticastAddress() ) {
			packet = new DatagramPacket(new byte[4096],4096);
			serverSocket = new MulticastSocket(port);
			((MulticastSocket)serverSocket).joinGroup(listenAddr);
		} else {
			serverSocket = new DatagramSocket(port);
			packet = new DatagramPacket(new byte[4096],4096, listenAddr, port);
		}
		Logger.println(Logger.DEBUG, "before.:" + Thread.currentThread() );
		serverSocket.receive(packet);
		Logger.println(Logger.DEBUG, "after.:" + Thread.currentThread() );
		HttpParser parser = new HttpParser(packet.getData());
		request = parser.parseHTTPRequest();
		return request;
	}
	
}

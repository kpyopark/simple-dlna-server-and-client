package com.elevenquest.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;

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
	
	public void initSocket() {
		try {
			if ( listenAddr.isMulticastAddress() ) {
				packet = new DatagramPacket(new byte[4096],4096);
				//SocketAddress address = new InetSocketAddress(port);
				serverSocket = new MulticastSocket(port);
				((MulticastSocket)serverSocket).joinGroup(listenAddr);
			} else {
				serverSocket = new DatagramSocket(port);
				packet = new DatagramPacket(new byte[4096],4096, listenAddr, port);
			}
		} catch (Exception e) {
			Logger.println(Logger.ERROR, "To init multicast socket failed.\n");
		}
	}
	
	public HttpRequest listen() throws Exception {
		HttpRequest request = null;
		if ( serverSocket == null ) {
			initSocket();
		}
		Logger.println(Logger.DEBUG, "waiting for udp packet.:" + Thread.currentThread() );
		serverSocket.receive(packet);
		Logger.println(Logger.DEBUG, "accept one packet.:" + Thread.currentThread() );
		HttpParser parser = new HttpParser(packet.getData());
		request = parser.parseHTTPRequest();
		return request;
	}
	
	public void close() {
		serverSocket.close();
		serverSocket = null;
	}
	
}

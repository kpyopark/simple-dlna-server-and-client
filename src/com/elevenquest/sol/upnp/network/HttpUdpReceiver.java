package com.elevenquest.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;

import com.elevenquest.sol.upnp.common.Logger;


public class HttpUdpReceiver extends HttpReceiver {
	NetworkInterface intf;
	int port;
	InetAddress listenAddr = null;
	InetAddress bindAddr = null;
	DatagramSocket serverSocket = null;
	DatagramPacket packet = null;
	
	public HttpUdpReceiver(NetworkInterface intf, InetAddress listenAddr, InetAddress bindAddress, int port) {
		this.intf = intf;
		this.listenAddr = listenAddr;
		this.bindAddr = bindAddress;
		this.port = port;
	}
	
	public void initSocket() {
		try {
			if ( listenAddr.isMulticastAddress() ) {
				packet = new DatagramPacket(new byte[4096],4096);
				//SocketAddress address = new InetSocketAddress(port);
				SocketAddress socBindAddr = new InetSocketAddress(bindAddr, port);
				serverSocket = new MulticastSocket(socBindAddr);
				((MulticastSocket)serverSocket).joinGroup(listenAddr);
			} else {
				// in unicast. windows system has weak host model for listening packet. so we don't need to use inetaddress to bind.
				serverSocket = new DatagramSocket(port, listenAddr);
				packet = new DatagramPacket(new byte[4096],4096, listenAddr, port);
			}
		} catch (Exception e) {
			Logger.println(Logger.ERROR, "To init multicast socket failed. listenAddr[" + listenAddr.getCanonicalHostName() + "] bindAddr[" + bindAddr.getCanonicalHostName() + "] port[" + port + "].\n");
		}
	}
	
	public HttpBaseStructure listen() throws Exception {
		HttpBaseStructure requestOrResponse = null;
		if ( serverSocket == null ) {
			initSocket();
		}
		Logger.println(Logger.DEBUG, "waiting for udp packet.:" + Thread.currentThread() );
		serverSocket.receive(packet);
		Logger.println(Logger.DEBUG, "accept one packet.:" + Thread.currentThread() );
		HttpParser parser = new HttpParser(packet.getData());
		requestOrResponse = parser.parse();
		return requestOrResponse;
	}
	
	public void close() {
		serverSocket.close();
		serverSocket = null;
	}
	
}

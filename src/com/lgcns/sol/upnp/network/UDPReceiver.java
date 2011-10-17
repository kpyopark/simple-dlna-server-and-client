package com.lgcns.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Vector;

public class UDPReceiver {
	NetworkInterface intf;
	int port;
	InetAddress listenAddr = null;
	DatagramSocket serverSocket = null;
	DatagramPacket packet = null;
	Vector<UDPReceiverHandler> handlerList = new Vector<UDPReceiverHandler>();
	
	public UDPReceiver(NetworkInterface intf, InetAddress listenAddr, int port) {
		this.intf = intf;
		this.listenAddr = listenAddr;
		this.port = port;
	}
	
	public void addUDPReceiveHandler(UDPReceiverHandler handler) {
		this.handlerList.add(handler);
	}
	
	public void clearHandler() {
		this.handlerList.clear();
	}
	
	public void listen() throws Exception {
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
		serverSocket.receive(packet);
		process(packet);
	}
	
	public void process(DatagramPacket packet) {
		for ( UDPReceiverHandler handler : this.handlerList ) {
			handler.process(packet);
		}
	}
}

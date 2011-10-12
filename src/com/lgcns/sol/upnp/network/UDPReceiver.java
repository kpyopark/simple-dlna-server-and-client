package com.lgcns.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.Vector;

public class UDPReceiver extends CommonReceiver {
	NetworkInterface intf;
	int port;
	DatagramSocket serverSocket = null;
	DatagramPacket packet = null;
	Vector<UDPReceiverHandler> handlerList = new Vector<UDPReceiverHandler>();
	
	public UDPReceiver(NetworkInterface intf, int port) {
		this.intf = intf;
		this.port = port;
	}
	
	public void addUDPReceiveHandler(UDPReceiverHandler handler) {
		this.handlerList.add(handler);
	}
	
	public void clearHandler() {
		this.handlerList.clear();
	}
	
	public void listen() throws Exception {
		if ( serverSocket != null ) {
			serverSocket.close();
		}
		// TODO : Getting inetaddress by using beloew intf.getInetAddresses() is not accurate process.
		//        You Do modify below lines.
		SocketAddress socAddr = new InetSocketAddress(intf.getInetAddresses().nextElement() ,port);
		serverSocket = new DatagramSocket(socAddr);
		DatagramPacket packet = new DatagramPacket(new byte[4096],4096);
		serverSocket.receive(packet);
		process(packet);
	}
	
	public void process(DatagramPacket packet) {
		for ( UDPReceiverHandler handler : this.handlerList ) {
			handler.process(packet);
		}
	}
}

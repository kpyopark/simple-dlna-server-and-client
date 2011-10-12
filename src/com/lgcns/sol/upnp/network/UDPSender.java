package com.lgcns.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;

public class UDPSender {
	NetworkInterface intf;
	int port;
	InetAddress targetAddr;
	
	public UDPSender(NetworkInterface intf, int port, InetAddress targetAddr) {
		this.intf = intf;
		this.port = port;
		this.targetAddr = targetAddr;
	}
	
	public void send(byte[] sendData) throws Exception {
		SocketAddress addr = null;
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		try {
			// TODO : modify below methods : intf.getInetAddress().
			addr = new InetSocketAddress(intf.getInetAddresses().nextElement(), port);
			socket = new DatagramSocket();
			packet = new DatagramPacket(sendData, sendData.length, addr);
			socket.send(packet);
		} catch (Exception e) {
			throw e;
		} finally {
			if ( socket != null ) try {
				socket.close();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
}

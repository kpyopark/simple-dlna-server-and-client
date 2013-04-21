package com.elevenquest.sol.upnp.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.Observable;

import com.elevenquest.sol.upnp.common.Logger;

public class HttpUdpSender extends HttpRequestSender {
	NetworkInterface intf;
	int port;
	InetAddress targetAddr;
	
	public HttpUdpSender(NetworkInterface intf, InetAddress targetAddr, int port) {
		this.intf = intf;
		this.port = port;
		this.targetAddr = targetAddr;
	}
	
	@Override
	protected void send(HttpRequest request) throws Exception {
		SocketAddress addr = null;
		DatagramSocket socket = null;
		java.net.MulticastSocket multiSocket = null;
		DatagramPacket packet = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append(request.getCommand()).append(" ").append(request.getUrlPath()).append(" ").append(request.getHttpVer()).append("\r\n");
			for ( int cnt = 0 ; cnt < request.getHeaderCount() ; cnt++ )
				buffer.append(request.getHeaderName(cnt)).append(":").append(request.getHeaderValue(cnt)).append("\r\n");
			buffer.append("\r\n");
			byte[] bytesSend = buffer.toString().getBytes();
			if ( this.targetAddr.isMulticastAddress() ) {
				Logger.println(Logger.DEBUG, "sent by multicasting.");
				// Multicasting.
				addr = new InetSocketAddress(this.targetAddr, port);
				multiSocket = new MulticastSocket(port);
				multiSocket.joinGroup(targetAddr);
				packet = new DatagramPacket(bytesSend, bytesSend.length, addr);
				multiSocket.send(packet);
			} else {
				Logger.println(Logger.DEBUG, "sent by unicasting.");
				// unicasting.
				// TODO : MODIFY THE BELOW LINES. intf.getInetAddresses().nextElement()
				addr = new InetSocketAddress(intf.getInetAddresses().nextElement(), port);
				socket = new DatagramSocket(port, targetAddr);
				packet = new DatagramPacket(bytesSend, bytesSend.length, addr);
				socket.send(packet);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if ( socket != null ) try {
				socket.close();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			if ( multiSocket != null ) try {
				multiSocket.close();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	public void update(Observable o, Object arg) {
		// TODO : This method used for the timing event.
	}
	
}

package com.elevenquest.sol.upnp.server;

import java.net.NetworkInterface;
import java.util.ArrayList;

import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.control.ControlPoint;
import com.elevenquest.sol.upnp.discovery.SSDPMessage;
import com.elevenquest.sol.upnp.discovery.SSDPReceiveHandler;
import com.elevenquest.sol.upnp.discovery.SSDPSearchSendHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HttpUdpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.HttpRequestReceiver;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;

public class SsdpControlPointServer {
	
	ArrayList<CommonServer> receiveServerList = null;
	ArrayList<CommonServer> senderServerList = null;
	ControlPoint cp = null;

	public void SsdpControlPointServer(ControlPoint cp) {
		this.cp = cp;
	}
	
	public void start() {
		startReceiveServer();
		startSendServer();
	}
	
	public void startReceiveServer() {
		if ( receiveServerList != null ) 
			stopReceiveServer();
		receiveServerList = new ArrayList<CommonServer>();
		ArrayList<NetworkInterface> interfaces = UPnPUtils.getAvailiableNetworkInterfaces();
		for ( NetworkInterface intf : interfaces ) {
			try {
				// 1. Next, create ssdp message receiver & handler instance.
				HttpRequestReceiver receiver = new HttpUdpReceiver(intf, UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS, 
						UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
				IHttpRequestHandler handler = new SSDPReceiveHandler();
				receiver.setReceiveHandler(handler);
				// 2. Create Common server
				CommonServer receiveServer = new CommonServer();
				// 3. set receiver into server.
				receiveServer.setReceiver(receiver);
				// 4. start server.
				receiveServer.startServer();
				
				receiveServerList.add(receiveServer);
			} catch ( AbnormalException abe ) {
				abe.printStackTrace();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public void startSendServer() {
		if ( senderServerList != null )
			stopSendServer();
		senderServerList = new ArrayList<CommonServer>();
		ArrayList<NetworkInterface> interfaces = UPnPUtils.getAvailiableNetworkInterfaces();
		for ( NetworkInterface intf : interfaces ) {
			try {
				// 1. Next, create ssdp message supplier
				HttpRequestSender sender = new HttpUdpSender(intf, UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS, 
						UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
				IHttpRequestSuplier suplier = new SSDPSearchSendHandler(cp);
				sender.setSenderHandler(suplier);
				// 2. Create Common server
				CommonServer sendServer = new CommonServer();
				// 3. set sender into server.
				sendServer.setSender(sender, new SendEvent(3000, SendEvent.SEND_EVENT_TYPE_TIME_UNLIMINITED));
				// 4. start server.
				sendServer.startServer();
				
				senderServerList.add(sendServer);
				
			} catch ( AbnormalException abe ) {
				abe.printStackTrace();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		stopReceiveServer();
		stopSendServer();
	}
	
	public void stopReceiveServer() {
		if ( receiveServerList != null )
			for ( CommonServer server : receiveServerList ) server.stopServer();
		receiveServerList = null;
	}
	
	public void stopSendServer() {
		if ( senderServerList != null )
			for ( CommonServer server : senderServerList ) server.stopServer();
		senderServerList = null;
	}
	
}

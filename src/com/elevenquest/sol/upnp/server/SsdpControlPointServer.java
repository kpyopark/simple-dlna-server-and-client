package com.elevenquest.sol.upnp.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.control.ControlPoint;
import com.elevenquest.sol.upnp.discovery.SSDPReceiveHandler;
import com.elevenquest.sol.upnp.discovery.SSDPSearchSendHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HttpReceiver;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpSimpleServer;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpSender;
import com.elevenquest.sol.upnp.network.IHttpReceiveHandler;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;

public class SsdpControlPointServer {
	
	ArrayList<HttpSimpleServer> receiveServerList = null;
	ArrayList<CommonServer> senderServerList = null;
	ControlPoint cp = null;

	public SsdpControlPointServer() {
		
	}
	
	public SsdpControlPointServer(ControlPoint cp) {
		this.cp = cp;
	}
	
	public void start() {
		startReceiveServer();
		startSendServer();
	}
	
	public void startReceiveServer() {
		if ( receiveServerList != null ) 
			stopReceiveServer();
		receiveServerList = new ArrayList<HttpSimpleServer>();
		//ArrayList<NetworkInterface> interfaces = UPnPUtils.getAvailiableNetworkInterfaces();
		HashMap<InetAddress, NetworkInterface> ipAndNic = UPnPUtils.getAvailiableIpAndNicList();
		Set<InetAddress> ips = ipAndNic.keySet();
		for ( InetAddress bindIp : ips ) {
			try {
				// 1. Next, create ssdp message receiver & handler instance.
				HttpReceiver receiver = new HttpUdpReceiver(ipAndNic.get(bindIp), UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS,
						bindIp,
						UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
				IHttpReceiveHandler handler = new SSDPReceiveHandler();
				receiver.setReceiveHandler(handler);
				// 2. Create Common server
				HttpSimpleServer receiveServer = new HttpSimpleServer();
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
			for ( HttpSimpleServer server : receiveServerList ) server.stopServer();
		receiveServerList = null;
	}
	
	public void stopSendServer() {
		if ( senderServerList != null )
			for ( CommonServer server : senderServerList ) server.stopServer();
		senderServerList = null;
	}
	
}

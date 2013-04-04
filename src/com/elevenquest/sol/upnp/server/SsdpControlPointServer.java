package com.elevenquest.sol.upnp.server;

import java.net.NetworkInterface;
import java.util.ArrayList;

import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.discovery.SSDPMessage;
import com.elevenquest.sol.upnp.discovery.SSDPReceiveHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.HttpRequestReceiver;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;

public class SsdpControlPointServer {
	
	ArrayList<CommonServer> receiveServerList = null;
	ArrayList<HttpRequestSender> senderServerList = null;

	public void start() {
		startReceiveServer();
	}
	
	public void startReceiveServer() {
		if ( receiveServerList != null ) 
			stop();
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
	
	public void stop() {
		stopReceiveServer();
	}
	
	public void stopReceiveServer() {
		if ( receiveServerList != null )
			for ( CommonServer server : receiveServerList ) server.stopServer();
		receiveServerList = null;
	}
	
}

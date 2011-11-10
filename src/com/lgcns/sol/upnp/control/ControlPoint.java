package com.lgcns.sol.upnp.control;

import java.net.NetworkInterface;

import com.lgcns.sol.upnp.discovery.SSDPMessage;
import com.lgcns.sol.upnp.exception.AbnormalException;
import com.lgcns.sol.upnp.model.UPnPDevice;
import com.lgcns.sol.upnp.network.CommonReceiveHandler;
import com.lgcns.sol.upnp.network.CommonReceiver;
import com.lgcns.sol.upnp.network.CommonSendHandler;
import com.lgcns.sol.upnp.network.CommonSender;
import com.lgcns.sol.upnp.network.HTTPSender;
import com.lgcns.sol.upnp.network.UDPReceiver;
import com.lgcns.sol.upnp.network.UDPSender;
import com.lgcns.sol.upnp.server.CommonServer;
import com.lgcns.sol.upnp.server.SendEvent;

public class ControlPoint {
	
	public void start() {
		startSsdpServer();
		startGenaServer();
	}
	
	public void startSsdpServer() {
		try {
			// 1. first get the default network interface. (such as eth0)
			NetworkInterface intf = NetworkInterface.getNetworkInterfaces().nextElement();

			UPnPDevice device = new UPnPDevice();
			
			device.addNetworkInterface(intf);
			device.setModelSerial("abcdefg");
			device.setMulticastPort(UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
			device.setUpc("1029101");
			device.setUuid("1234567890");
			device.setMultiCastAddress(UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS);

			CommonServer receiveServer = null;
			CommonServer sendServer = null;
			// Sample Code for receiver.
			{
				// 2. Next, create receiver & handler instance.
				CommonReceiver receiver = new UDPReceiver(intf, UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS, 
						UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
				CommonReceiveHandler handler = new SSDPMessage();
				receiver.addReceiveHandler(handler);
				
				// 3. Create Common server
				receiveServer = new CommonServer();
				
				// 4. set receiver into server.
				receiveServer.setReceiver(receiver);
				
				// 5. start server.
				receiveServer.startServer();
			}
			/*
			// Sample Code for sender.
			{
				CommonSender sender = new UDPSender(intf, device.getMultiCastAddress(), device.getMulticastPort());
				CommonSendHandler handler = new SSDPMessage(device);
				sender.setSenderHandler(handler);
				
				sendServer = new CommonServer();
				
				sendServer.setSender(sender, new SendEvent(SendEvent.SEND_EVENT_TYPE_TIME_UNLIMINITED, 500));
				
				sendServer.startServer();
			}
			*/

			Thread.sleep(50 * 1000);	// After 10 sec.
			receiveServer.stopServer();
			//sendServer.stopServer();

		} catch ( Exception e ) {
			e.printStackTrace();
		} catch ( AbnormalException abe ) {
			abe.printStackTrace();
		}
	}
	
	public void startGenaServer() {
		
	}
	
	/**
	 * For Testing.
	 * @param args
	 */
	public static void main(String[] args) {
		ControlPoint cp = new ControlPoint();
		cp.start();
	}

	
}

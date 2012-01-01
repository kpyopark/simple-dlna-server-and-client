package com.elevenquest.sol.upnp.control;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Set;

import com.elevenquest.sol.upnp.discovery.SSDPMessage;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.CommonReceiveHandler;
import com.elevenquest.sol.upnp.network.CommonReceiver;
import com.elevenquest.sol.upnp.network.CommonSendHandler;
import com.elevenquest.sol.upnp.network.CommonSender;
import com.elevenquest.sol.upnp.network.HTTPSender;
import com.elevenquest.sol.upnp.network.UDPReceiver;
import com.elevenquest.sol.upnp.network.UDPSender;
import com.elevenquest.sol.upnp.server.CommonServer;
import com.elevenquest.sol.upnp.server.SendEvent;
import com.elevenquest.sol.upnp.server.SsdpControlPointServer;
import com.elevenquest.sol.upnp.service.ContentDirectoryItem;
import com.elevenquest.sol.upnp.service.ContentDirectoryService;

/**
 * 
 * Retrieves device and service descriptions, sends actions to services, polls for service state variables, and receives events from services
 * 
 * @author Administrator
 * 
 *
 */
public class ControlPoint {
	
	SsdpControlPointServer ssdpServer = null;

	public void start() {
		startSsdpServer();
		startGenaServer();
	}
	
	/**
	 * It's not used. just for testment.
	 * @deprecated
	 */
	public void testSsdpSenderServer() {
		try {
			NetworkInterface intf = NetworkInterface.getNetworkInterfaces().nextElement();
			UPnPDevice device = new UPnPDevice();
			
			device.addNetworkInterface(intf);
			device.setModelSerial("abcdefg");
			device.setMulticastPort(UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
			device.setUpc("1029101");
			device.setUuid("1234567890");
			device.setMultiCastAddress(UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS);
			CommonServer sendServer = null;
			// Sample Code for sender.
			{
				CommonSender sender = new UDPSender(intf, device.getMultiCastAddress(), device.getMulticastPort());
				CommonSendHandler handler = new SSDPMessage(device);
				sender.setSenderHandler(handler);
				
				sendServer = new CommonServer();
				
				sendServer.setSender(sender, new SendEvent(SendEvent.SEND_EVENT_TYPE_TIME_UNLIMINITED, 500));
				
				sendServer.startServer();
			}
		} catch ( AbnormalException abe ) {
			abe.printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void startSsdpServer() {
		if ( ssdpServer != null )
			ssdpServer.stop();
		ssdpServer = new SsdpControlPointServer();
		ssdpServer.start();
	}
	
	private void testBrowseRecursively(ContentDirectoryService cds, ContentDirectoryItem parent,int depth) throws Exception {
		String padding = "";
		for ( int cnt = 0 ; cnt < depth ; cnt++ ) {
			padding += "--";
		}
		System.out.println(padding + parent);
		if ( parent.getType() == ContentDirectoryItem.CDS_TYPE_CONTAINER ) {
			ArrayList<ContentDirectoryItem> items = cds.browse(parent.getId() , "BrowseDirectChildren", "*", 0, 0, "");
			for ( ContentDirectoryItem child : items ) {
				testBrowseRecursively(cds, child, depth+1);
			}
		}
	}
	
	public void startGenaServer() {
		
	}
	
	private void printDeviceState() {
		try {
			Thread.sleep(10 * 1000);	// After 10 sec.
			Set<String> uuids = UPnPDeviceManager.getDefaultDeviceManager().getUuidList();
			for ( String uuid : uuids ) {
				System.out.println("--->>>uuid:" + uuid);
				UPnPDevice remoteDevice = UPnPDeviceManager.getDefaultDeviceManager().getDevice(uuid); 
				System.out.println(remoteDevice);
				UPnPService service = null;
				if ( ( service = remoteDevice.getUPnPService(UPnPService.UPNP_SERVICE_ID_CDS) ) != null ) {
					ContentDirectoryService cds = (ContentDirectoryService)service;
					try {
						ArrayList<ContentDirectoryItem> list = cds.browse("0", "BrowseMetadata", "*", 0, 0, "");
						for ( ContentDirectoryItem item : list ) {
							testBrowseRecursively(cds,item,0);
						}
					} catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		printDeviceState();
		if ( ssdpServer != null )
			ssdpServer.stop();
	}
	
	/**
	 * For Testing.
	 * @param args
	 */
	public static void main(String[] args) {
		ControlPoint cp = new ControlPoint();
		cp.start();
		cp.stop();
	}

	
}

package com.elevenquest.sol.upnp.control;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.discovery.SSDPSearchSendHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.HttpRequestReceiver;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpTcpSender;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpSender;
import com.elevenquest.sol.upnp.server.CommonServer;
import com.elevenquest.sol.upnp.server.SendEvent;
import com.elevenquest.sol.upnp.server.SsdpControlPointServer;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryItem;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryService;

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
				Logger.println(Logger.INFO, "--->>>uuid:" + uuid);
				UPnPDevice remoteDevice = UPnPDeviceManager.getDefaultDeviceManager().getDevice(uuid); 
				Logger.println(Logger.INFO, remoteDevice);
				Vector<UPnPService> serviceList = remoteDevice.getSerivces();
				for ( UPnPService service : serviceList )
					//System.out.println( service );
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
		if ( ssdpServer != null )
			ssdpServer.stop();
	}
	
	public void sendDeviceSearchMessage() {
		try {
			NetworkInterface intf = NetworkInterface.getNetworkInterfaces().nextElement();
			UPnPDevice device = new UPnPDevice();
			
			device.setMulticastPort(UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
			device.setMultiCastAddress(UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS);
			CommonServer sendServer = null;
			// Sample Code for sender.
			{
				HttpRequestSender sender = new HttpUdpSender(intf, device.getMultiCastAddress(), device.getMulticastPort());
				IHttpRequestSuplier handler = new SSDPSearchSendHandler(this);
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

	static class TestCP implements Observer {

		ControlPoint cp = null;
		
		public TestCP(final ControlPoint cp) {
			this.cp = cp;
		}
		@Override
		public void update(Observable arg0, Object arg1) {
			// TODO Auto-generated method stub
			Logger.println(Logger.INFO, "DeviceManager is updated.");
			cp.printDeviceState();
		}
		
	}
	/**
	 * For Testing.
	 * @param args
	 */
	public static void main(String[] args) {
		ControlPoint cp = new ControlPoint();
		UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
		manager.addObserver(new TestCP(cp));
		cp.start();
		try {
			Thread.sleep(60 * 1000000);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		cp.stop();
	}

	
}

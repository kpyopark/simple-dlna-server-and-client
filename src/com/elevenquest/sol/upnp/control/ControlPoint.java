package com.elevenquest.sol.upnp.control;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.discovery.SSDPSearchSendHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.IUPnPDeviceListChangeListener;
import com.elevenquest.sol.upnp.model.IUPnPDeviceServiceListChangeListener;
import com.elevenquest.sol.upnp.model.UPnPChangeStatusValue;
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
import com.elevenquest.sol.upnp.server.GenaServer;
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
	GenaServer genaServer = null;

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
	
	public void startGenaServer() {
		if ( genaServer != null )
			genaServer.stop();
		genaServer = new GenaServer();
		genaServer.start();
	}
	
	private void testBrowseRecursively(ContentDirectoryService cds, ContentDirectoryItem parent,int depth) throws Exception {
		String padding = "";
		for ( int cnt = 0 ; cnt < depth ; cnt++ ) {
			padding += "--";
		}
		System.out.println(padding + parent);
		if ( parent.getType() == ContentDirectoryItem.CDS_TYPE_CONTAINER ) {
			ArrayList<ContentDirectoryItem> items = cds.browse(parent , "BrowseDirectChildren", "*", 0, 0, "");
			for ( ContentDirectoryItem child : items ) {
				testBrowseRecursively(cds, child, depth+1);
			}
		}
	}
	
	private void printDeviceState() {
		try {
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
					ContentDirectoryItem rootItem = new ContentDirectoryItem();
					rootItem.setId("0");
					try {
						ArrayList<ContentDirectoryItem> list = cds.browse(rootItem, "BrowseMetadata", "*", 0, 0, "");
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
		if ( genaServer != null )
			genaServer.stop();
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

	static class PrintDeviceInfo implements IUPnPDeviceServiceListChangeListener, IUPnPDeviceListChangeListener {

		ControlPoint cp = null;
		Collection<UPnPDevice> deviceList = null;
		
		public PrintDeviceInfo(final ControlPoint cp) {
			this.cp = cp;
			this.deviceList = new ArrayList<UPnPDevice>();
		}
		
		@Override
		public void updateDeviceList(UPnPChangeStatusValue value, UPnPDevice device) {
			Logger.println(Logger.INFO, "DeviceManager is updated.");
			if ( !deviceList.contains(device) ) {
				deviceList.add(device);
				device.addServiceChangeListener(PrintDeviceInfo.this);
			}
		}
		
		private void testBrowseRecursively(ContentDirectoryService cds, ContentDirectoryItem parent,int depth) throws Exception {
			String padding = "";
			for ( int cnt = 0 ; cnt < depth ; cnt++ ) {
				padding += "--";
			}
			System.out.println(padding + parent);
			if ( parent.getType() == ContentDirectoryItem.CDS_TYPE_CONTAINER ) {
				ArrayList<ContentDirectoryItem> items = cds.browse(parent , "BrowseDirectChildren", "*", 0, 0, "");
				for ( ContentDirectoryItem child : items ) {
					testBrowseRecursively(cds, child, depth+1);
				}
			}
		}
		
		@Override
		public void updateServiceList(UPnPChangeStatusValue value, UPnPDevice device, UPnPService serviceUpdated) {
			Logger.println(Logger.INFO, "Device is updated. updated service [" + serviceUpdated + "] status :[" + value + "]" );
			if ( value == UPnPChangeStatusValue.CHANGE_ADD || value == UPnPChangeStatusValue.CHANGE_UPDATE ) {
				if ( serviceUpdated.getServiceId().equals(UPnPService.UPNP_SERVICE_ID_CDS) ) {
					ContentDirectoryService cds = (ContentDirectoryService)serviceUpdated;
					try {
						ContentDirectoryItem rootItem = new ContentDirectoryItem();
						rootItem.setId("0");
						ArrayList<ContentDirectoryItem> list = cds.browse(rootItem, "BrowseMetadata", "*", 0, 0, "");
						for ( ContentDirectoryItem item : list ) {
							testBrowseRecursively(cds,item,0);
						}
					} catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}
		}

	}
	
}

package com.elevenquest.sol.upnp.control;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
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
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpUdpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.service.avtransport.AVTransportService;
import com.elevenquest.sol.upnp.service.connection.ConnectionItem;
import com.elevenquest.sol.upnp.service.connection.ConnectionManagementService;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryItem;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryService;
import com.elevenquest.sol.upnp.service.rederingcontrol.RenderingControlService;
import com.elevenquest.sol.upnp.threadpool.CommonThreadPool;
import com.elevenquest.sol.upnp.threadpool.GenaServer;
import com.elevenquest.sol.upnp.threadpool.SendEvent;
import com.elevenquest.sol.upnp.threadpool.SsdpControlPointServer;

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
	
	UPnPService cds = null;		// ContentDirectory service
	UPnPService rcs = null;		// Rendering service
	
	ConnectionItem sourceConnection = null;
	ConnectionItem sinkConnection = null;
	
	ArrayList<ContentDirectoryItem> lastDisplayedItem = new ArrayList<ContentDirectoryItem>();

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
	
	public void testBrowseRecursively(ContentDirectoryService cds, ContentDirectoryItem parent,int depth) throws Exception {
		String padding = "";
		for ( int cnt = 0 ; cnt < depth ; cnt++ ) {
			padding += "--";
		}
		if ( parent.getResValue() != null && parent.getResValue().length() > 0 && !lastDisplayedItem.contains(parent) )
			lastDisplayedItem.add(parent);
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
			CommonThreadPool sendServer = null;
			// Sample Code for sender.
			{
				HttpRequestSender sender = new HttpUdpSender(intf, device.getMultiCastAddress(), device.getMulticastPort());
				IHttpRequestSuplier handler = new SSDPSearchSendHandler(this);
				sender.setSenderHandler(handler);
				
				sendServer = new CommonThreadPool();
				
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
	
	public void setContentDirectoryService(ContentDirectoryService cds) {
		this.cds = cds;
	}
	
	public void setRenderingService(RenderingControlService rcs) {
		this.rcs = rcs;
	}
	
	public boolean canBeRendered() {
		if ( this.cds == null || this.rcs == null )
			return false;
		ConnectionManagementService sourceCms = (ConnectionManagementService)this.cds.getDevice().getUPnPService(UPnPService.UPNP_SERVICE_ID_CMS);
		ConnectionManagementService sinkCms = (ConnectionManagementService)this.rcs.getDevice().getUPnPService(UPnPService.UPNP_SERVICE_ID_RCS);
		if ( sourceCms == null || sinkCms == null )
			return false;
		try {
			ArrayList<String> sourceProtocols = sourceCms.getSourceProtocolInfo();
			ArrayList<String> sinkProtocols = sinkCms.getSinkProtocolInfo();
			for ( int cnt = 0 ; sourceProtocols != null && cnt < sourceProtocols.size() ; cnt++ ) {
				String sourceProtocol = sourceProtocols.get(cnt);
				for ( int subInx = 0; sinkProtocols != null && subInx < sinkProtocols.size(); subInx++ ) {
					String sinkProtocol = sinkProtocols.get(subInx);
					if ( sourceProtocol.equals(sinkProtocol) )
						return true;
				}
			}
		} catch ( Exception e ) {
			Logger.println(Logger.ERROR, e.getLocalizedMessage());
		}
		return false;
	}
	
	private static boolean isMatchedProtocol(String source, String sink) {
		if ( source == null || sink == null )
			return false;
		String[] sourceProtocolElement = source.split(":");
		String[] sinkProtocolElement = sink.split(":");
		if ( sourceProtocolElement.length < 3 || sinkProtocolElement.length < 3 )
			return false;
		return sourceProtocolElement[0].equalsIgnoreCase(sinkProtocolElement[0]) && sourceProtocolElement[2].equalsIgnoreCase(sinkProtocolElement[2]);
	}
	
	private static boolean hasMatchedProtcol(ArrayList<String> protocols, String protocol) {
		for ( String comparedProtocol : protocols ) {
			if ( isMatchedProtocol(comparedProtocol, protocol) )
				return true;
		}
		return false;
	}
	
	public boolean playContentItem(ContentDirectoryItem item) {
		Logger.println(Logger.DEBUG, "1. validate whether rcs/cds is set.");
		if ( this.cds == null || this.rcs == null ) {
			Logger.println(Logger.WARNING,"[Control Point] There are some invalid devices. cds[" + this.cds + "] rcs[" + this.rcs + "]");
			return false;
		}
		String sourceProtocol = item.getResProtocolInfo();
		ConnectionManagementService sourceCms = (ConnectionManagementService)this.cds.getDevice().getUPnPService(UPnPService.UPNP_SERVICE_ID_CMS);
		ConnectionManagementService sinkCms = (ConnectionManagementService)this.rcs.getDevice().getUPnPService(UPnPService.UPNP_SERVICE_ID_CMS);

		if ( sinkCms == null ) {
			Logger.println(Logger.WARNING,"[Control Point] There are some invalid services. sink connection manager[" + sinkCms + "]");
			return false;
		}

		ArrayList<String> sinkProtocols = null;	
		Logger.println(Logger.DEBUG, "2. retreive sink protocols.");
		try {
			sinkProtocols = sinkCms.getSinkProtocolInfo();
		} catch ( Exception e ) {
			e.printStackTrace();
			Logger.println(Logger.ERROR, e.getLocalizedMessage());
			return false;
		}
		String sinkProtocol = null;
		
		for ( String protocol : sinkProtocols ) {
			if ( isMatchedProtocol( protocol, sourceProtocol ) )
					sinkProtocol = protocol;
		}
		
		if ( sinkProtocol == null )
			sinkProtocol = sourceProtocol;

		if ( sourceProtocol == null && sinkProtocol == null ) {
			Logger.println(Logger.WARNING,"[Control Point] There is no protocol info in both sink and source devices.");
			return false;
		}
		
		Logger.println(Logger.WARNING,"[Control Point] There is no protocol matched betweeen source and sink. source[" + sourceProtocol + "] sink[" + sinkProtocols + "]");
		sinkProtocol = sinkProtocols.get(0);
		
		Logger.println(Logger.DEBUG, "3. request for sink to prepare connection.");
		if ( sinkCms.hasPrepareForConnectionAction() ) {
			try {
				sinkConnection = sinkCms.prepareForConnection( sourceProtocol, 
						sourceCms.getPeerConnectionManagerID(), -1, "Input");
			} catch ( Exception e ) {
				e.printStackTrace();
				Logger.println(Logger.ERROR, e.getLocalizedMessage());
			}
		} else {
			Logger.println(Logger.DEBUG, "Not implemented yet - prepareConnection for sink.");
		}
		if ( sinkConnection == null )
			sinkConnection = new ConnectionItem();
		
		Logger.println(Logger.DEBUG, "4. request for source to prepare connection.");
		if ( sourceCms.hasPrepareForConnectionAction() ) {
			try {
				sourceConnection = sourceCms.prepareForConnection( sinkProtocol, 
						sinkCms.getPeerConnectionManagerID(), sinkConnection.getPeerConnectionID(), "Output");
			} catch ( Exception e ) {
				e.printStackTrace();
				Logger.println(Logger.ERROR, e.getLocalizedMessage());
			}
		} else {
			Logger.println(Logger.DEBUG, "Not implemented yet - prepareConnection for source.");
		}
		if ( sourceConnection == null )
			sourceConnection = new ConnectionItem();
		
		AVTransportService sinkTrans = (AVTransportService)sinkCms.getDevice().getUPnPService(UPnPService.UPNP_SERVICE_ID_AVT);
		AVTransportService sourceTrans = (AVTransportService)sourceCms.getDevice().getUPnPService(UPnPService.UPNP_SERVICE_ID_AVT);
		
		Logger.println(Logger.DEBUG, "5. set uri to sink.");
		if ( sinkTrans == null ) {
			Logger.println(Logger.WARNING,"[Control Point] There is no transport service in renderer.");
			return false;
		} else {
			try {
				sinkTrans.SetAVTransportURI(sinkConnection.getAVTransportID(), item.getResValue(), item.getDesc());
			} catch ( Exception e ) {
				Logger.println(Logger.WARNING,"[Control Point] We couldn't set current uri[" + item.getResValue() + "] in sink(renderer).");
				return false;
			}
		}
		
		Logger.println(Logger.DEBUG, "6. set uri to source.");
		if ( sourceTrans == null ) {
			Logger.println(Logger.WARNING,"[Control Point] There is no transport service in content server.");
		} else {
			try {
				sourceTrans.SetAVTransportURI(sourceConnection.getAVTransportID(), item.getResValue(), item.getDesc());
			} catch ( Exception e ) {
				Logger.println(Logger.WARNING,"[Control Point] We couldn't set current uri[" + item.getResValue() + "] in source(content server).");
				return false;
			}
		}
		
		try {
			sinkTrans.Play(sinkConnection.getAVTransportID(), "1");
		} catch ( Exception e ) {
			Logger.println(Logger.WARNING,"[Control Point] We couldn't play it.");
		}
		
		return true;
	}
}

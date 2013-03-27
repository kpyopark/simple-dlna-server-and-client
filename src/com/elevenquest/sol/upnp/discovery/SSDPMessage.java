package com.elevenquest.sol.upnp.discovery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.network.BaseHTTPMapper;
import com.elevenquest.sol.upnp.network.HTTPRequest;
import com.elevenquest.sol.upnp.network.ICommonReceiveHandler;
import com.elevenquest.sol.upnp.network.ICommonSendHandler;

public class SSDPMessage extends BaseHTTPMapper implements ICommonReceiveHandler, ICommonSendHandler {
	String startLine = null;
	boolean needValidation = false;
	UPnPDevice device = null;
	ArrayList<String> headerNames = null;
	ArrayList<String> headerValues = null;
	
	public final static String ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG = "BOOTID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_NEXTBOTID_UPNP_ORG = "NEXTBOOTID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG = "CONFIGID.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG = "SEARCHPORT.UPNP.ORG";
	public final static String ID_UPNP_DISCOVERY_HOST = "HOST";
	public final static String ID_UPNP_DISCOVERY_CACHECONTROL = "CACHE-CONTROL";
	public final static String ID_UPNP_DISCOVERY_LOCATION = "LOCATION";
	public final static String ID_UPNP_DISCOVERY_NOTIFICATION_TYPE = "NT";
	public final static String ID_UPNP_DISCOVERY_NT_SUBTYPE = "NTS";
	public final static String ID_UPNP_DISCOVERY_SERVER = "SERVER";
	public final static String ID_UPNP_DISCOVERY_USN = "USN";
	
	
	
	public final static String ID_START_LINE_NOTIFY = "NOTIFY * HTTP/1.1";
	public final static String ID_START_LINE_SEARCH = "M-SEARCH * HTTP/1.1";
	public final static String ID_START_LINE_NORMAL = "HTTP/1.1 200 OK";
	
	public final static String ID_NT_SUBTYPE_SSDPALIVE = "ssdp:alive";
	public final static String ID_NT_SUBTYPE_SSDPBYEBYE = "ssdp:byebye";
	public final static String ID_NT_SUBTYPE_SSDPUPDATE = "ssdp:update";
	
	public final static String STARTLINE_ID_LIST[] = {
		ID_START_LINE_NOTIFY, ID_START_LINE_SEARCH, ID_START_LINE_NORMAL
	};
	
	public final static String REQUIRED_ID_LIST[] = {
		ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG, ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG, ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG};
	
	public boolean needBootIdUpdated(UPnPDevice device) {
		// if the device has changed its IP, it must change BOOTID value also.
		// if the device has rebooted, it must change BOOTID value also. (so, BOOID value has some time stamp value to differ older state.)
		this.device = device;
		return false;
	}
	
	public SSDPMessage() {
		super();
	}
	
	public SSDPMessage(UPnPDevice device) {
		this();
		this.device = device;
		// TODO : modify below lines.
		this.setStartLine(SSDPMessage.ID_NT_SUBTYPE_SSDPALIVE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_HOST, DefaultConfig.ID_UPNP_DISCOVERY_HOST_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CACHECONTROL, DefaultConfig.ID_UPNP_DISCOVERY_CACHECONTROL_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_LOCATION, DefaultConfig.ID_UPNP_DISCOVERY_LOCATION_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NOTIFICATION_TYPE,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NT_SUBTYPE, SSDPMessage.ID_NT_SUBTYPE_SSDPALIVE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SERVER,DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_USN,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG,"1");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG,"1");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG,this.device.getMulticastPort() + "");	
	}
	
	public boolean parse(byte[] content) throws Exception {
		
		boolean isValid = true;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content)));
			this.startLine = "";
			this.headerList.clear();
			String keyAndValue = null;
			this.startLine = br.readLine();
			if ( this.startLine == null )
				return false;
			while( (keyAndValue = br.readLine()) != null ) {
				int totalLength = keyAndValue.length();
				Logger.println(Logger.DEBUG, keyAndValue);
				for ( int pos = 0; pos < totalLength; pos++ ) {
					if ( keyAndValue.charAt(pos) == ':' ) {
						this.setHeaderValue(keyAndValue.substring(0,pos), (totalLength > (pos + 1))? keyAndValue.substring(pos+1).trim() : "" );
						break;
					}
				}
			}
			// validate the contents of header list.
			if ( needValidation ) {
				isValid = false;
				for( String startLine : STARTLINE_ID_LIST ) {
					isValid = this.startLine.equals(startLine);
					if ( isValid )
						break;
				}
				if ( isValid ) {
					for( String mandatoryKey : REQUIRED_ID_LIST) {
						if ( !this.headerList.containsKey(mandatoryKey) ) {
							isValid = false;
							break;
						}
					}
				}
			}
		} finally {
			if ( br != null ) try {
				br.close();
			} catch ( Exception e ) { e.printStackTrace(); }
		}
		return isValid;
	}

	public void process(HTTPRequest request) {
		SSDPMessage message = new SSDPMessage();
 		UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
		try {
			message.parse(dgPacket.getData());
			if ( ID_NT_SUBTYPE_SSDPALIVE.equalsIgnoreCase(message.getHeaderValue(ID_UPNP_DISCOVERY_NT_SUBTYPE)) ) {
				// If [live] message received.
				manager.addDevice(message.getDeviceBaseInfo());
			} else if (ID_NT_SUBTYPE_SSDPBYEBYE.equalsIgnoreCase(message.getHeaderValue(ID_UPNP_DISCOVERY_NT_SUBTYPE))) {
				manager.removeDevice(message.getDeviceBaseInfo().getUuid());
			} else if (ID_NT_SUBTYPE_SSDPUPDATE.equalsIgnoreCase(message.getHeaderValue(ID_UPNP_DISCOVERY_NT_SUBTYPE))) {
				manager.updateDevice(message.getDeviceBaseInfo().getUuid());
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public Object processAfterSend(Object returnValue) {
		// There is no actions after to send "ssdp:alive" message into the network.
		return null;
	}
	
	public UPnPDevice getDeviceBaseInfo() {
		UPnPDevice device = new UPnPDevice();
		for ( Iterator<String> keyIter = this.getHeaderKeyIterator() ; keyIter.hasNext() ; ) {
			String key = keyIter.next();
			Logger.println(Logger.INFO, key+":"+this.getHeaderValue(key));
		}
		device.setUsn(this.getHeaderValue(ID_UPNP_DISCOVERY_USN));
		device.setHost(this.getHeaderValue(ID_UPNP_DISCOVERY_HOST));
		device.setLocation(this.getHeaderValue(ID_UPNP_DISCOVERY_LOCATION));
		{
			String cacheValue = this.getHeaderValue(ID_UPNP_DISCOVERY_CACHECONTROL);
			device.setCacheControl(Integer.parseInt(cacheValue.substring(cacheValue.indexOf("max-age=")+8).trim()));
		}
		
		device.setNts(this.getHeaderValue(ID_UPNP_DISCOVERY_NT_SUBTYPE));
		device.setNt(this.getHeaderValue(ID_UPNP_DISCOVERY_NOTIFICATION_TYPE));
		device.setServer(this.getHeaderValue(ID_UPNP_DISCOVERY_SERVER));
		device.setRemote(true);
		device.setReadyToUse(false);
		
		Logger.println(Logger.DEBUG, "New Device Info:\n" + device.toString());
		
		return device;
	}
}

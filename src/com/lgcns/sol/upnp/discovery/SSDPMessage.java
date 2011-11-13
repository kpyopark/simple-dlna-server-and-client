package com.lgcns.sol.upnp.discovery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Iterator;

import com.lgcns.sol.upnp.model.UPnPDevice;
import com.lgcns.sol.upnp.model.UPnPDeviceManager;
import com.lgcns.sol.upnp.network.CommonReceiveHandler;
import com.lgcns.sol.upnp.network.CommonSendHandler;

public class SSDPMessage implements CommonReceiveHandler, CommonSendHandler {
	String startLine = null;
	HashMap<String, String> headerList = new HashMap<String, String>();
	boolean needValidation = false;
	UPnPDevice device = null;
	
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
		
	}
	
	public SSDPMessage(UPnPDevice device) {
		this.device = device;
		// TODO : modify below lines.
		this.setStartLine(SSDPMessage.ID_NT_SUBTYPE_SSDPALIVE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_HOST, "239.255.255.250:1900");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CACHECONTROL, "300");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_LOCATION, "http://www.korea.com");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NOTIFICATION_TYPE,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_NT_SUBTYPE, SSDPMessage.ID_NT_SUBTYPE_SSDPALIVE);
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SERVER,"WindowsNT UPnP/1.1 simpledlna/draft");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_USN,"uuid:" + this.device.getUuid() );
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_BOOTID_UPNP_ORG,"1");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_CONFIGID_UPNP_ORG,"1");
		this.setHeaderValue(SSDPMessage.ID_UPNP_DISCOVERY_SEARCHPORT_UPNP_ORG,this.device.getMulticastPort() + "");	
	}
	
	public String getStartLine() {
		return this.startLine;
	}
	
	public void setStartLine(String startLine) {
		this.startLine= startLine;
	}
	
	public String getHeaderValue(String headerId) {
		return headerList.get(headerId);
	}
	
	public void setHeaderValue(String headerId, String value) {
		headerList.put(headerId, value);
	}
	
	public Iterator<String> getHeaderKeyIterator() {
		return this.headerList.keySet().iterator();
	}
	
	public byte[] toBytes() {
		StringBuffer fullMessage = new StringBuffer();
		// append start header.
		fullMessage.append(startLine).append('\n');
		// append other header list.
		for ( Iterator<String> keyIter = headerList.keySet().iterator() ; keyIter.hasNext() ;) {
			String key = keyIter.next();
			fullMessage.append(key).append(':').append(headerList.get(key)).append('\n');
		}
		return fullMessage.toString().getBytes();
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
				for ( int pos = 0; pos < totalLength; pos++ ) {
					if ( keyAndValue.charAt(pos) == ':' ) {
						this.headerList.put(keyAndValue.substring(0,pos), (totalLength > (pos + 1))? keyAndValue.substring(pos+1).trim() : "" );
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

	public void process(Object packet) {
		DatagramPacket dgPacket = (DatagramPacket)packet;
		SSDPMessage message = new SSDPMessage();
		UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
		try {
			message.parse(dgPacket.getData());
			manager.addDevice(message.getDeviceBaseInfo());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public Object getSendObject() throws Exception {
		return this.toBytes();
	}

	public Object processAfterSend(Object returnValue) {
		// There is no actions after to send "ssdp:alive" message into the network.
		return null;
	}
	
	public UPnPDevice getDeviceBaseInfo() {
		UPnPDevice device = new UPnPDevice();
		
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
		
		System.out.println("New Device Info:\n" + device.toString());
		
		return device;
	}
}

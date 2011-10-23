package com.lgcns.sol.upnp.discovery;

import java.net.NetworkInterface;
import java.util.Vector;

public class UPnPDevice {
	
	//private int SEARCHPORT_UPNP_ORG = MulticastSocket.DEFAULT_UPNP_MULTICAST_PORT;
	private Vector<NetworkInterface> interfaces = new Vector<NetworkInterface>();
	
	private Vector<UPnPService> services = new Vector<UPnPService>();
	
	//private UDPReceiver multicastReceiver;
	String modelSerial;
	String uuid;
	String upc;
	
	public Vector<NetworkInterface> getNetworkInterfaceList() {
		return interfaces;
	}
	
	public void addNetworkInterface(NetworkInterface intf) {
		if ( !interfaces.contains(intf) )
			interfaces.add(intf);
	}

	public String getModelSerial() {
		return modelSerial;
	}

	public void setModelSerial(String modelSerial) {
		this.modelSerial = modelSerial;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}
	
}

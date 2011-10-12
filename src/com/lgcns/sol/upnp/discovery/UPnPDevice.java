package com.lgcns.sol.upnp.discovery;

import java.net.NetworkInterface;
import java.util.Vector;

import com.lgcns.sol.upnp.common.DefaultConfig;
import com.lgcns.sol.upnp.network.*;

public class UPnPDevice {
	
	private int SEARCHPORT_UPNP_ORG = DefaultConfig.DEFAULT_UPNP_MULTICAST_PORT;
	private Vector<NetworkInterface> interfaces = new Vector<NetworkInterface>();
	private UDPReceiver multicastReceiver;
	
	public Vector<NetworkInterface> getNetworkInterfaceList() {
		return interfaces;
	}
	
	public void addNetworkInterface(NetworkInterface intf) {
		if ( !interfaces.contains(intf) )
			interfaces.add(intf);
	}
	
}

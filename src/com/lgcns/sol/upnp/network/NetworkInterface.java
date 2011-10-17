package com.lgcns.sol.upnp.network;

import java.net.*;
import java.util.*;

public class NetworkInterface {
	
	InetAddress address = null;
	static Vector<NetworkInterface> g_intfList = null;
	
	public NetworkInterface(InetAddress address) {
		this.address = address;
	}
	
	public static Vector<NetworkInterface> getLocalNetworkInterfaces() throws Exception {
		try {
			NetworkInterface newOne = new NetworkInterface(InetAddress.getLocalHost());
			if ( g_intfList == null )
				g_intfList = new Vector<NetworkInterface>();
			if ( !g_intfList.contains(newOne) )
				g_intfList.add(newOne);
		} catch (UnknownHostException e) {
			throw e;
		}
		return g_intfList;
	}
	
	public InetAddress getInetAddress() {
		return this.address;
	}
}

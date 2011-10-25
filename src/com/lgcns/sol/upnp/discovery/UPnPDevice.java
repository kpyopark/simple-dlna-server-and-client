package com.lgcns.sol.upnp.discovery;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Vector;

public class UPnPDevice {

	public static int DEFAULT_UPNP_MULTICAST_PORT = 1900;
	public static InetAddress DEFAULT_UPNP_MULTICAST_ADDRESS = null;
	static {
		try {
			DEFAULT_UPNP_MULTICAST_ADDRESS = InetAddress.getByAddress(new byte[]{(byte)239,(byte)255,(byte)255,(byte)250});
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	
	//private int SEARCHPORT_UPNP_ORG = MulticastSocket.DEFAULT_UPNP_MULTICAST_PORT;
	private Vector<NetworkInterface> interfaces = new Vector<NetworkInterface>();
	
	private Vector<UPnPService> services = new Vector<UPnPService>();
	
	//private UDPReceiver multicastReceiver;
	String modelSerial;
	String uuid;
	String upc;
	int multicastPort = DEFAULT_UPNP_MULTICAST_PORT;
	InetAddress multiCastAddress = null;
	
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

	public int getMulticastPort() {
		return multicastPort;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public InetAddress getMultiCastAddress() {
		return multiCastAddress;
	}

	public void setMultiCastAddress(InetAddress multiCastAddress) {
		this.multiCastAddress = multiCastAddress;
	}
	
}

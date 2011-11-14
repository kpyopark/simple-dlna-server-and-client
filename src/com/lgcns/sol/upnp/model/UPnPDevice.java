package com.lgcns.sol.upnp.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.StringTokenizer;
import java.util.Vector;


public class UPnPDevice extends UPnPBase {

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
	
	// private UDPReceiver multicastReceiver;
	String modelSerial;
	String uuid;
	String upc;
	int multicastPort = DEFAULT_UPNP_MULTICAST_PORT;
	InetAddress multiCastAddress = null;
	int cacheControl = 180;
	String location;		// url for getting description.
	String usn;
	String nts;
	String nt;
	String server;
	boolean isRemote = true;
	boolean isReadyToUse = false;
	boolean isProgressingToRetrieve = false;
	NetworkInterface networkInterface = null; 
	
	public boolean isProgressingToRetrieve() {
		return isProgressingToRetrieve;
	}

	public void setProgressingToRetrieve(boolean isProgressingToRetrieve) {
		this.isProgressingToRetrieve = isProgressingToRetrieve;
	}

	public boolean isRemote() {
		return isRemote;
	}

	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}

	public String getUsn() {
		return usn;
	}

	public void setUsn(String usn) {
		this.usn = usn;
		StringTokenizer st = new StringTokenizer(usn,":");
		if ( st.nextToken().equals("uuid") && st.hasMoreTokens() ) {
			setUuid(st.nextToken());
		}
	}

	public void addService(UPnPService service) {
		services.add(service);
	}
	
	public Vector<UPnPService> getSerivces() {
		return this.services;
	}
	
	public UPnPService getUPnPService(String serviceId) {
		UPnPService service = null;
		for ( int cnt = 0 ; cnt < this.services.size() ; cnt++ ) {
			if ( this.services.get(cnt).getServiceId().equals(serviceId))
				service = this.services.get(cnt);
		}
		return service;
	}
	
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
	
	public void setHost(String host) {
		StringTokenizer st = new StringTokenizer(host.trim(),":");
		String address, port;
		if ( st.hasMoreTokens() && (address = st.nextToken()) != null ) {
			if ( st.hasMoreTokens() && ( port = st.nextToken()) != null ) {
				try {
					System.out.println("address:[" + address +  "]:port:[" + port + "]");
					setMultiCastAddress(InetAddress.getByName(address));
					setMulticastPort(Integer.parseInt(port));
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(int cacheControl) {
		this.cacheControl = cacheControl;
	}

	public String getNts() {
		return nts;
	}

	public void setNts(String nts) {
		this.nts = nts;
	}

	public String getNt() {
		return nt;
	}

	public void setNt(String nt) {
		this.nt = nt;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public boolean isReadyToUse() {
		return isReadyToUse;
	}

	public void setReadyToUse(boolean isReadyToUse) {
		this.isReadyToUse = isReadyToUse;
	}

	public NetworkInterface getNetworkInterface() {
		return networkInterface;
	}

	public void setNetworkInterface(NetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
}

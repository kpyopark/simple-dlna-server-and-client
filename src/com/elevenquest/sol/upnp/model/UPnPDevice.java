package com.elevenquest.sol.upnp.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.common.UPnPUtils;


public class UPnPDevice extends UPnPBase implements IUPnPServiceStatusChangeListener {

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
	private ArrayList<IUPnPDeviceServiceListChangeListener> serviceChangeListeners = new ArrayList<IUPnPDeviceServiceListChangeListener>();
	
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
	InetAddress localIP = null;
	ArrayList<UPnPDeviceImage> imageList = null;

	String configNumber;
	String specMajor;
	String specMinor;
	String friendlyName;
	String manufacturerName;
	String manufacturerUrl;
	String model;
	String modelName;
	String modelDescription;
	String modelNumber;
	String modelUrl;
	String deviceType;
	String presentationURL;
	
	HashMap<String, String> additionalHeaderValueList= null;
	
	public InetAddress getLocalIP() {
		return localIP;
	}

	public void setLocalIP(InetAddress localIP) {
		this.localIP = localIP;
	}

	String user;
	String password;
	
	String baseURL;
	String baseHost;
	
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
	
	public void addServiceChangeListener(IUPnPDeviceServiceListChangeListener listener) {
		if ( !this.serviceChangeListeners.contains(listener) )
			this.serviceChangeListeners.add(listener);
	}
	
	public void removeServiceChangeListener(IUPnPDeviceServiceListChangeListener listener) {
		this.serviceChangeListeners.remove(listener);
	}

	public void setUsn(String usn) {
		this.usn = usn;
		StringTokenizer st = new StringTokenizer(usn,":");
		if ( st.nextToken().equals("uuid") && st.hasMoreTokens() ) {
			setUuid(st.nextToken());
		}
	}

	public void addService(UPnPService service) {
		if ( !services.contains(service) ) {
			services.add(service);
			service.addServiceStateChangeListener(this);
		}
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
		if ( host == null ) {
			Logger.println(Logger.WARNING, "Invalid value(null) is passed into host parameter. Its uuid is [" + this.uuid + "]");
			return;
		}
		StringTokenizer st = new StringTokenizer(host.trim(),":");
		String address, port;
		if ( st.hasMoreTokens() && (address = st.nextToken()) != null ) {
			if ( st.hasMoreTokens() && ( port = st.nextToken()) != null ) {
				try {
					Logger.println(Logger.DEBUG, "address:[" + address +  "]:port:[" + port + "]");
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
		try {
			URL url = new URL(location);
			this.baseHost = url.getHost() + ":" + url.getPort();
			this.baseURL = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
		} catch ( Exception e ) {
			this.baseURL = location;
			this.baseHost = location;
		}
				
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
	
	public void setUserAndPassword(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	public String getAuthorizationStr() {
		if ( this.user != null && this.password != null ) {
			return UPnPUtils.base64encode( this.user + ":" + this.password );
		}
		return null;
	}
	
	public String getAbsoluteURL(String orgUrl) {
		if ( orgUrl.indexOf("http") == 0 ) {
			return orgUrl;
		}
		if ( orgUrl.charAt(0) == '/' ) {
			return this.baseURL + orgUrl;
		}
		return this.baseURL + "/" + orgUrl;
	}
	
	public String getBaseHost() {
		return this.baseHost;
	}
	
	public String toString() {
		return this.getUuid() + ":" + this.getLocation();
	}

	@Override
	public void updateServiceStatus(UPnPChangeStatusValue value, UPnPService service) {
		if ( service.isReadyToUse ) {
			synchronized( serviceChangeListeners ) {
				for ( IUPnPDeviceServiceListChangeListener listener : serviceChangeListeners ) {
					listener.updateServiceList(value, this, service);
				}
			}
		}
		service.removeServiceStatusChangeListener(this);
	}
	
	public void addDeviceImage(UPnPDeviceImage image) {
		if ( this.imageList == null )
			this.imageList = new ArrayList<UPnPDeviceImage>();
		if ( this.imageList.contains(image)) {
			Logger.println(Logger.WARNING, "[UPNPDevice] The image description info[" + image.getUrl() + "] already exists in this device[" + this.getUuid() + "].");
		} else {
			this.imageList.add(image);
		}
	}
	
	public ArrayList<UPnPDeviceImage> getDeviceImageList() {
		return this.imageList;
	}

	public String getConfigNumber() {
		return configNumber;
	}

	public void setConfigNumber(String configNumber) {
		this.configNumber = configNumber;
	}

	public String getSpecMajor() {
		return specMajor;
	}

	public void setSpecMajor(String specMajor) {
		this.specMajor = specMajor;
	}

	public String getSpecMinor() {
		return specMinor;
	}

	public void setSpecMinor(String specMinor) {
		this.specMinor = specMinor;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerUrl() {
		return manufacturerUrl;
	}

	public void setManufacturerUrl(String manufacturerUrl) {
		this.manufacturerUrl = manufacturerUrl;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getPresentationURL() {
		return presentationURL;
	}

	public void setPresentationURL(String presentationURL) {
		this.presentationURL = presentationURL;
	}
	
	public void setAdditionalHeaderValue(String header, String value) {
		if ( this.additionalHeaderValueList == null )
			this.additionalHeaderValueList = new HashMap<String,String>();
	}
	
	public String getAdditionalHeaderValue(String header) {
		return this.additionalHeaderValueList.get(header);
	}
	
}

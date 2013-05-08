package com.elevenquest.sol.upnp.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.description.DeviceDescription;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpTcpSender;
import com.elevenquest.sol.upnp.server.CommonServer;
import com.elevenquest.sol.upnp.server.SendEvent;

public class UPnPDeviceManager {
	
	private static UPnPDeviceManager singletone = null;
	
	public static UPnPDeviceManager getDefaultDeviceManager() {
		if ( singletone == null ) {
			singletone = new UPnPDeviceManager();
		}
		return singletone;
	}
	
	// private attributes;
	HashMap<String, UPnPDevice> deviceList = null;
	HashMap<String, Integer> deviceStatus = null;
	ArrayList<IUPnPDeviceListChangeListener> listenerList = null;
	
	private UPnPDeviceManager() {
		deviceList = new HashMap<String,UPnPDevice>();
		listenerList = new ArrayList<IUPnPDeviceListChangeListener>();
	}
	
	public void clearAll() {
		this.deviceList.clear();
	}
	
	public void notifyDeviceListChangeListeners(UPnPChangeStatusValue value, UPnPDevice device) {
		synchronized( listenerList ) {
			for (IUPnPDeviceListChangeListener listener : listenerList) {
				listener.updateDeviceList(value, device);
			}
		}
	}
	
	public void addDeviceListChangeListener(IUPnPDeviceListChangeListener listener) {
		if ( !this.listenerList.contains(listener) )
			this.listenerList.add(listener);
	}
	
	public void removeDeviceListChangeListener(IUPnPDeviceListChangeListener listener) {
		this.listenerList.remove(listener);
	}
	
	public void addDevice(UPnPDevice device) {
		if ( this.deviceList.get(device.getUuid()) != null ) {
			// If same UUID exists in local Device List.
			// 1. Replace the device info.
			Logger.println(Logger.WARNING, "Same UUID[" + device.getUuid() + "] is used.");
		} else {
			Logger.println(Logger.INFO, "Add device UUID[" + device.getUuid() + "] is used.");
			this.deviceList.put(device.getUuid(), device);
			this.updateRemoteDeviceInfo();
			notifyDeviceListChangeListeners(UPnPChangeStatusValue.CHANGE_ADD, device);
		}
	}
	
	public UPnPDevice getDevice(String uuid) {
		return this.deviceList.get(uuid);
	}
	
	public int getListSize() {
		return this.deviceList.size();
	}
	
	public void removeDevice(String uuid) {
		UPnPDevice device = this.getDevice(uuid);
		this.deviceList.remove(uuid);
		this.notifyDeviceListChangeListeners(UPnPChangeStatusValue.CHANGE_REMOVE, device);
	}
	
	public void updateDevice(String uuid) {
		if ( this.deviceList.get(uuid) != null ) {
			UPnPDevice device = this.deviceList.get(uuid);
			device.setProgressingToRetrieve(false);
			device.setReadyToUse(false);
			this.updateRemoteDeviceInfo();
		}
	}
	
	public Set<String> getUuidList() {
		return this.deviceList.keySet();
	}
	
	public Collection<UPnPDevice> getDeviceList() {
		return this.deviceList.values();
	}
	
	static class IpNicPair {
		InetAddress localIp;
		NetworkInterface nic;
	}
	
	static class UpdateRemoteDeviceInfoThread extends Thread {
		UPnPDevice innerDevice = null;

		static HashMap<InetAddress, NetworkInterface> localIpNicMap = null;
		
		static void listUpLocalIp() {
			localIpNicMap = new HashMap<InetAddress, NetworkInterface>();
			try {
				Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
				while( interfaceList.hasMoreElements() ) {
					NetworkInterface intf = interfaceList.nextElement();
					Enumeration<InetAddress> ipList = intf.getInetAddresses();
					while( ipList.hasMoreElements() ) {
						InetAddress localIp = ipList.nextElement();
						if ( localIp.isAnyLocalAddress() ) {
							// skip. such like 0.0.0.0
						//} else if ( localIp.isLoopbackAddress() ) {
							// skip. such like 127.0.0.1
						} else if ( localIp.isMulticastAddress() ) {
							// skip. such like 239.255.255.250
						} else {
							Logger.println(Logger.INFO, "usalbe local ip:" + localIp.getCanonicalHostName() + " usable interface :" + intf.getDisplayName());
							localIpNicMap.put(localIp, intf);
						}
					}
				}
			} catch ( SocketException se ) {
				Logger.println(Logger.ERROR, "There is no hardware interfaces to connect with other devices.");
			}
		}
		
		public UpdateRemoteDeviceInfoThread(UPnPDevice outerDevice) {
			innerDevice = outerDevice;
			if ( localIpNicMap == null ) {
				listUpLocalIp();
			}
		}
		
		public IpNicPair getLocalInetAddressToTargetHost(InetAddress remoteHost, int port) {
			InetAddress localIpUsed = null;
			NetworkInterface nicUsed = null;
			Socket soc = null;
			try {
				soc = new Socket(remoteHost, port);
				localIpUsed = soc.getLocalAddress();
				nicUsed = localIpNicMap.get(localIpUsed);
			} catch ( IOException ioe ) {
				Logger.println(Logger.WARNING, "The remote host[" + remoteHost.getCanonicalHostName() + "," + port + "] can't be rechable.");
			} finally {
				if ( soc != null ) try { soc.close(); } catch ( Exception e ) { }
			}
			if ( localIpUsed == null || nicUsed == null )
				return null;
			IpNicPair pair = new IpNicPair();
			pair.localIp = localIpUsed;
			pair.nic = nicUsed;
			return pair;
		}
		
		public void run() {
			try {
				URL url = new URL(innerDevice.getLocation());
				IpNicPair ipAndNic= getLocalInetAddressToTargetHost(InetAddress.getByName(url.getHost()), url.getPort());
				if ( ipAndNic == null ) {
					Logger.println( Logger.WARNING, "<Device Manager> This device[ip:" + url.getHost() + ":" + innerDevice.getUuid() + "] exists in unreachable network." );
				} else {
					innerDevice.setLocalIP(ipAndNic.localIp);
					innerDevice.setNetworkInterface(ipAndNic.nic);
					Logger.println( Logger.DEBUG, "<Device Manager> Looking for a deivce[sid:" + innerDevice.getUuid() + "] using nic[" + innerDevice.getNetworkInterface().getDisplayName() + "] with local ip[" + innerDevice.getLocalIP().getCanonicalHostName() + "]");
					HttpRequestSender sender = new HttpTcpSender(innerDevice.getNetworkInterface(),innerDevice.getLocation());
					IHttpRequestSuplier handler = new DeviceDescription(innerDevice);
					sender.setSenderHandler(handler);
					sender.sendData();
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateRemoteDeviceInfo() {
		for ( int inx = 0 ; inx < this.deviceList.size() ; inx++ ) {
			UPnPDevice device = this.deviceList.values().iterator().next();
			if ( !device.isReadyToUse() && device.isRemote() && device.isProgressingToRetrieve() == false ) {
				Logger.println(Logger.INFO, "Update Remote Device..[" + device.getUuid() + "]" );
				// TODO : Some DMS product need user id & password for authorization.
				// So, we need to support simple http authorization.
				//device.setUserAndPassword("user_id", "password");
				device.setProgressingToRetrieve(true);
				Thread oneTimeThread = new UpdateRemoteDeviceInfoThread(device);
				oneTimeThread.start();
			} else {
				Logger.println(Logger.DEBUG, "Skipped device:" + device.getUuid());
				Logger.println(Logger.DEBUG, "isReadyToUse:[" + device.isReadyToUse + "] isRemote:[" + device.isRemote + "] isProgressingToRetrieve[" + device.isProgressingToRetrieve + "]" );
			}
		}
	}

}

package com.elevenquest.sol.upnp.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.description.DeviceDescription;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpTcpSender;
import com.elevenquest.sol.upnp.threadpool.CommonThreadPool;
import com.elevenquest.sol.upnp.threadpool.SendEvent;

public class UPnPDeviceManager {
	
	private static UPnPDeviceManager singletone = null;
	
	public static UPnPDeviceManager getDefaultDeviceManager() {
		if ( singletone == null ) {
			singletone = new UPnPDeviceManager();
		}
		return singletone;
	}
	
	private void storeCurrentDeviceList() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream("sample.bin", false);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(deviceList);
			oos.writeObject(deviceStatus);
		} catch ( Exception e ) {
			e.printStackTrace();
			System.out.println("Error occured during storing current device list to file.");
		} finally {
			if ( oos != null )
				try { oos.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
		}
	}
	
	private void loadPreviousDeviceList() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream("sample.bin");
			ois = new ObjectInputStream(fis);
			Object oldDeviceList = ois.readObject();
			if ( oldDeviceList instanceof HashMap ) {
				deviceList = (HashMap<String, UPnPDevice>)oldDeviceList;
			}
			Object oldDeviceStatus = ois.readObject();
			if ( oldDeviceStatus instanceof HashMap ) {
				deviceStatus = (HashMap<String, Integer>)oldDeviceStatus;
			}
		} catch ( Exception e ) {
			System.out.println("It Failed to load previous device list.");
		} finally {
			if ( ois != null )
				try { ois.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
		}
	}
	
	// private attributes;
	HashMap<String, UPnPDevice> deviceList = null;
	HashMap<String, Integer> deviceStatus = null;
	ArrayList<IUPnPDeviceListChangeListener> listenerList = null;
	
	private UPnPDeviceManager() {
		loadPreviousDeviceList();
		if ( deviceList == null )
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
		//storeCurrentDeviceList();
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

		
		public UpdateRemoteDeviceInfoThread(UPnPDevice outerDevice) {
			innerDevice = outerDevice;
		}
		
		public IpNicPair getLocalInetAddressToTargetHost(InetAddress remoteHost, int port) {
			InetAddress localIpUsed = null;
			NetworkInterface nicUsed = null;
			Socket soc = null;
			HashMap<InetAddress, NetworkInterface> localIpNicMap = UPnPUtils.getAvailiableIpAndNicList();
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
		Set<String> uuidList = this.deviceList.keySet();
		for ( String uuid : uuidList ) {
			UPnPDevice device = this.deviceList.get(uuid);
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

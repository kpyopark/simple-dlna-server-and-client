package com.elevenquest.sol.upnp.model;

import java.util.HashMap;
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
	
	private UPnPDeviceManager() {
		deviceList = new HashMap<String,UPnPDevice>();
	}
	
	public void clearAll() {
		this.deviceList.clear();
	}
	
	public void addDevice(UPnPDevice device) {
		if ( this.deviceList.get(device.getUuid()) != null ) {
			// If same UUID exists in local Device List.
			// 1. Replace the device info.
			Logger.println(Logger.WARNING, "Same UUID[" + device.getUuid() + "] is used.");
		} else {
			this.deviceList.put(device.getUuid(), device);
			this.updateRemoteDeviceInfo();
		}
	}
	
	public UPnPDevice getDevice(String uuid) {
		return this.deviceList.get(uuid);
	}
	
	public int getListSize() {
		return this.deviceList.size();
	}
	
	public void removeDevice(String uuid) {
		this.deviceList.remove(uuid);
		//this.updateRemoteDeviceInfo();
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
	
	static class SampleTread extends Thread {
		UPnPDevice innerDevice = null;
		public SampleTread(UPnPDevice outerDevice) {
			innerDevice = outerDevice;
		}
		
		public void run() {
			try {
				HttpRequestSender sender = new HttpTcpSender(innerDevice.getNetworkInterface(),innerDevice.getLocation());
				IHttpRequestSuplier handler = new DeviceDescription(innerDevice);
				sender.setSenderHandler(handler);
				sender.sendData();
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
				Thread oneTimeThread = new SampleTread(device);
				oneTimeThread.start();
			}
		}
	}
	
}

package com.lgcns.sol.upnp.model;

import java.util.HashMap;
import java.util.Set;

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
			System.out.println("Same UUID[" + device.getUuid() + "] is used.");
		}
		this.deviceList.put(device.getUuid(), device);
	}
	
	public UPnPDevice getDevice(String uuid) {
		return this.deviceList.get(uuid);
	}
	
	public int getListSize() {
		return this.deviceList.size();
	}
	
	public Set<String> getUuidList() {
		return this.deviceList.keySet();
	}
	
}

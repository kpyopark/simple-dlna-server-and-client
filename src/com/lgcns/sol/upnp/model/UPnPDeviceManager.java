package com.lgcns.sol.upnp.model;

import java.util.HashMap;
import java.util.Set;

import com.lgcns.sol.upnp.discovery.SSDPMessage;
import com.lgcns.sol.upnp.network.CommonSendHandler;
import com.lgcns.sol.upnp.network.CommonSender;
import com.lgcns.sol.upnp.network.HTTPSender;
import com.lgcns.sol.upnp.server.CommonServer;
import com.lgcns.sol.upnp.server.SendEvent;
import com.lgcns.sol.upnp.description.DeviceDescription;
import com.lgcns.sol.upnp.exception.AbnormalException;

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
		this.updateRemoteDeviceInfo();
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
				CommonSender sender = new HTTPSender(innerDevice.getNetworkInterface(),innerDevice.getLocation());
				CommonSendHandler handler = new DeviceDescription(innerDevice);
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
				System.out.println("Update Remote Device..[" + device.getUuid() + "]" );
				device.setProgressingToRetrieve(true);
				Thread oneTimeThread = new SampleTread(device);
				oneTimeThread.start();
				/*
				CommonSender sender = new HTTPSender(device.getNetworkInterface(),device.getLocation());
				CommonSendHandler handler = new DeviceDescription(device);
				sender.setSenderHandler(handler);
				CommonServer sendServer = null;
				sendServer = new CommonServer();
				sendServer.setSender(sender, new SendEvent(SendEvent.SEND_EVENT_TYPE_ONCE, 500));
				try {
					sendServer.startServer();
				} catch ( AbnormalException abne ) {
					abne.printStackTrace();
				}
				*/
			}
		}
	}
	
}

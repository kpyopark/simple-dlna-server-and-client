package com.elevenquest.sol.upnp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.description.DeviceDescription;
import com.elevenquest.sol.upnp.gena.Subscriber;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpTcpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;

public class UPnPEventManager extends UPnPBase {

	class UPnPSubscribeEvent {
		UPnPService service = null;
		long creationTimeStamp = 0;
		long updateTimeStamp = 0;
		boolean needToUnregister = false;
	}
	
	class SubscribeWaitingThread extends Thread {
		
		boolean needToExit = false;
		UPnPEventManager manager = null;
		
		public SubscribeWaitingThread() {
			manager = UPnPEventManager.getUPnPEventManager();
		}
		
		public void requestToExitThread() {
			needToExit = true;
		}
		
		public void run() {
			
			while( !needToExit ) {
				try {
					UPnPService service = manager.takeWaitingServiceToBeRegistered();
					Thread subscribeThread = new SubscribeRequestThread(service);
					subscribeThread.start();
				} catch ( InterruptedException ie ) {
					ie.printStackTrace();
					Logger.println(Logger.ERROR, "[SubscribeRequestThread]" + ie.getMessage());
				}
			}
		}
	}

	class SubscribeRequestThread extends Thread {
		UPnPService innerService = null;
		public SubscribeRequestThread(UPnPService outerService) {
			innerService = outerService;
		}
		
		public void run() {
			try {
				HttpRequestSender sender = new HttpTcpSender(innerService.getDevice().getNetworkInterface(),
						innerService.getEventsubUrl() );
				Subscriber handler = new Subscriber(innerService, true);
				sender.setSenderHandler(handler);
				sender.sendData();
				if ( innerService.isSubscribed() ) {
					addActiveGenaServiceList(innerService.getSubscribeId(), innerService);
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	class SubscribeUpdateThread extends Thread {
		
		boolean needToExit = false;
		UPnPEventManager manager = null;
		
		public SubscribeUpdateThread() {
			manager = UPnPEventManager.getUPnPEventManager();
		}
		
		public void requestToExitThread() {
			needToExit = true;
		}
		
		public void run() {
			
			while( !needToExit ) {
				try {
					Collection<UPnPSubscribeEvent> list = manager.getActiveServiceList();
					Iterator<UPnPSubscribeEvent> iter = list.iterator();
					long currentTime = System.currentTimeMillis();
					while( iter.hasNext() ) {
						UPnPSubscribeEvent event = iter.next();
						if ( ( currentTime - event.updateTimeStamp ) > 150 * 1000 ) {
							HttpRequestSender sender = new HttpTcpSender(event.service.getDevice().getNetworkInterface(),
									event.service.getEventsubUrl() );
							Subscriber handler = new Subscriber(event.service, true);
							sender.setSenderHandler(handler);
							try {
								sender.sendData();
								if ( event.service.isSubscribed() ) {
									removeActiveGenaService(event.service);
								}
							} catch ( Exception e ) {
								e.printStackTrace();
								Logger.println(Logger.ERROR, "Update Subscription failed." + e.getLocalizedMessage());
							}
						}
					}
					Thread.sleep(10000);
				} catch ( InterruptedException ie ) {
					ie.printStackTrace();
					Logger.println(Logger.ERROR, "[SubscribeRequestThread]" + ie.getMessage());
				}
			}
		}
	}

	static UPnPEventManager defaultManager = null;
	
	SubscribeWaitingThread requestThreadMain = null;
	SubscribeUpdateThread updateThreadMain = null;
	HashMap<String, UPnPSubscribeEvent> activeServiceList = null;
	BlockingQueue<UPnPService> waitingQueue = null;
	
	private UPnPEventManager() {
		activeServiceList = new HashMap<String,UPnPSubscribeEvent>();
		waitingQueue = new ArrayBlockingQueue<UPnPService>(10);
	}
	
	public static UPnPEventManager getUPnPEventManager() {
		if ( defaultManager == null )
			defaultManager = new UPnPEventManager();
		return defaultManager;
	}
	
	public void startGenaThread() {
		if ( requestThreadMain != null ) {
			requestThreadMain.requestToExitThread();
			requestThreadMain.interrupt();
		}
		requestThreadMain = new SubscribeWaitingThread();
		requestThreadMain.start();
		if ( updateThreadMain != null ) {
			updateThreadMain.requestToExitThread();
			updateThreadMain.interrupt();
		}
		updateThreadMain = new SubscribeUpdateThread();
		updateThreadMain.start();
	}
	
	public void addServiceToBeRegistered(UPnPService service) {
		service.setSubscribed(true);
		service.setSubscribeId(null);
		waitingQueue.offer(service);
	}
	
	public UPnPService takeWaitingServiceToBeRegistered() throws InterruptedException {
		return waitingQueue.take();
	}
	
	public void addActiveGenaServiceList(String sid, UPnPService service) {
		UPnPSubscribeEvent subscriptionEvent = new UPnPSubscribeEvent();
		subscriptionEvent.creationTimeStamp = System.currentTimeMillis();
		subscriptionEvent.updateTimeStamp = System.currentTimeMillis();
		subscriptionEvent.service = service;
		subscriptionEvent.needToUnregister = false;
		activeServiceList.put(sid, subscriptionEvent);
	}
	
	public void removeActiveGenaService(String sid) {
		activeServiceList.remove(sid);
	}
	
	public void removeActiveGenaService(UPnPService service) {
		Set<Entry<String,UPnPSubscribeEvent>> entrySet = activeServiceList.entrySet();
		for ( Iterator<Entry<String,UPnPSubscribeEvent>> entryIter = entrySet.iterator() ; entryIter.hasNext() ; ) {
			Entry<String,UPnPSubscribeEvent> entry = entryIter.next();
			if ( entry.getValue().equals(service) ) {
				Logger.println(Logger.INFO, "Service ssid[" + entry.getKey() + "] found in active gena queue. We'll remove it from the list." );
				activeServiceList.remove(entry.getKey());
				break;
			}
		}
	}
	
	public UPnPService getActiveGenaService(String serviceId) {
		return getActiveEventList(serviceId).service;
	}
	
	protected UPnPSubscribeEvent getActiveEventList(String serviceId) {
		return this.activeServiceList.get(serviceId);
	}
	
	public Collection<UPnPSubscribeEvent> getActiveServiceList() {
		return activeServiceList.values();
	}

}

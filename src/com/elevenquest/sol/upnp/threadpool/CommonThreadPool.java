package com.elevenquest.sol.upnp.threadpool;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.HttpRequestReceiver;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpSender;

public class CommonThreadPool {

	static int CORE_INI_THREAD = 5;
	static int CORE_MAX_THREAD = 8;
	static int KEEP_ALIVE_TIME = 5;	// 5 sec.
	static TimeUnit BASE_TIME_UNIT = TimeUnit.SECONDS;  

	HttpRequestReceiver receiver = null;
	HttpRequestSender sender = null;
	BlockingQueue<Runnable> queue = null;
	ThreadPoolExecutor threadPool = null;
	
	SendEvent event = null;
	
	boolean needStop = false;
	
	public CommonThreadPool() {
		queue = new ArrayBlockingQueue<Runnable>(CORE_MAX_THREAD);
		threadPool = new ThreadPoolExecutor(CORE_INI_THREAD, CORE_MAX_THREAD, KEEP_ALIVE_TIME, BASE_TIME_UNIT, queue);
	}
	
	public void setReceiver(HttpRequestReceiver receiver) {
		this.receiver = receiver;
	}
	
	public void setSender(HttpRequestSender sender, SendEvent event) {
		this.sender = sender;
		this.event = event;
	}
	
	int numberOfErrors = 0;

	public void startServer() throws AbnormalException {
		Logger.println(Logger.INFO, "Start Server...." + threadPool.toString() + ":" + receiver + ":" + sender );
		if ( receiver == null && sender == null ) {
			throw new AbnormalException("A listener or a Sender isn't set. Before use this api, you should set listener or sender in this class.");
		}
		new Thread(new Runnable() {
			public void run() {
				if ( receiver != null ) {
					needStop = false;
					threadPool.execute(new Runnable() {
						public void run() {
							try {
								receiver.initSocket();
								while( !needStop ) {
									try {
										receiver.receiveData();
										//Thread.sleep(1000);
									} catch ( Exception e ) {
										numberOfErrors++;
										Logger.println(Logger.ERROR, "Listener can't Listen.");
										e.printStackTrace();
									}
									if ( numberOfErrors > 3 )
										needStop = true;
								}
								receiver.close();
								receiver.clearHandler();
							} catch ( Exception e ) {
								Logger.println(Logger.ERROR, "There are some errors in Thread Pool.");
								e.printStackTrace();
							}
						}
					});
				}
				if ( sender != null ) {
					needStop = false;
					threadPool.execute(new Runnable() {
						public void run() {
							int cnt = 0;
							do {
								try {
									sender.sendData();
									if ( !needStop && event.getType() == SendEvent.SEND_EVENT_TYPE_TIME_UNLIMINITED )
										Thread.sleep(event.getDelayTimeInMillisec());
									Logger.println(Logger.DEBUG, "Repeating count:" + cnt++);
								} catch ( Exception e ) {
									numberOfErrors++;
									Logger.println(Logger.ERROR, "Sender can't send.");
									e.printStackTrace();
								}
								if ( numberOfErrors > 3 )
									needStop = true;
							} while( !needStop && event.getType() == SendEvent.SEND_EVENT_TYPE_TIME_UNLIMINITED );
						}
					});
				}
			}
		}).start();
	}
	
	public void stopServer() {
		needStop = true;
		List<Runnable> unreleasedTasks = threadPool.shutdownNow();
		threadPool.purge();
		threadPool.shutdown();
		for ( Runnable oneTask : unreleasedTasks ) {
			try {
				System.out.println("Unreleased task:" + oneTask);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		Logger.println(Logger.INFO, "Stop Server...." + threadPool.toString() + ":" + receiver + ":" + sender );
	}

	
}

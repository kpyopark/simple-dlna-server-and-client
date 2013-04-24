package com.elevenquest.sol.upnp.network;

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

public class HttpSimpleServer {

	static int CORE_INI_THREAD = 5;
	static int CORE_MAX_THREAD = 8;
	static int KEEP_ALIVE_TIME = 5;	// 5 sec.
	static TimeUnit BASE_TIME_UNIT = TimeUnit.SECONDS;  

	HttpRequestReceiver receiver = null;
	BlockingQueue<Runnable> queue = null;
	ThreadPoolExecutor threadPool = null;
	
	boolean needStop = false;
	
	public HttpSimpleServer() {
		queue = new ArrayBlockingQueue<Runnable>(CORE_MAX_THREAD);
		threadPool = new ThreadPoolExecutor(CORE_INI_THREAD, CORE_MAX_THREAD, KEEP_ALIVE_TIME, BASE_TIME_UNIT, queue);
	}
	
	public void setReceiver(HttpRequestReceiver receiver) {
		this.receiver = receiver;
	}
	
	int numberOfErrors = 0;

	public void startServer() throws AbnormalException {
		Logger.println(Logger.INFO, "Start Server...." + threadPool.toString() + ":" + receiver );
		if ( receiver == null ) {
			throw new AbnormalException("A listener or a Sender isn't set. Before use this api, you should set listener or sender in this class.");
		}
		needStop = false;
		while( !needStop ) {
			try {
				final HttpRequest request = receiver.listen();
				new Thread(new Runnable() {
					public void run() {
						receiver.process(request);
					}
				}).start();
				/*
				threadPool.execute(new Runnable() {
					public void run() {
						receiver.process(request);
					}
				});
				*/
			} catch ( Exception e ) {
				numberOfErrors++;
				Logger.println(Logger.ERROR, "Listener can't Listen. error count:" + numberOfErrors);
				e.printStackTrace();
			}
		}
	}
	
	public void stopServer() {
		needStop = true;
		threadPool.purge();
		threadPool.shutdown();
		/*
		List<Runnable> unreleasedTasks = threadPool.shutdownNow();
		for ( Runnable oneTask : unreleasedTasks ) {
			try {
				// TODO : How to kill an unreleased runnable thread. I don't know. 
				System.out.println("Unreleased task:" + oneTask);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		*/
		Logger.println(Logger.INFO, "Stop Server...." + threadPool.toString() + ":" + receiver );
	}

	
}

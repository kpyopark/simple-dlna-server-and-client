package com.lgcns.sol.upnp.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lgcns.sol.upnp.discovery.SSDPMessage;
import com.lgcns.sol.upnp.exception.AbnormalException;
import com.lgcns.sol.upnp.exception.ProcessableException;
import com.lgcns.sol.upnp.network.CommonHandler;
import com.lgcns.sol.upnp.network.CommonReceiver;
import com.lgcns.sol.upnp.network.UDPReceiver;

public class CommonServer {

	static int CORE_INI_THREAD = 3;
	static int CORE_MAX_THREAD = 5;
	static int KEEP_ALIVE_TIME = 5;	// 5 sec.
	static TimeUnit BASE_TIME_UNIT = TimeUnit.SECONDS;  

	CommonReceiver receiver = null;
	BlockingQueue<Runnable> queue = null;
	ThreadPoolExecutor threadPool = null;
	boolean needStop = false;
	
	public CommonServer() {
		queue = new ArrayBlockingQueue<Runnable>(CORE_MAX_THREAD);
		threadPool = new ThreadPoolExecutor(CORE_INI_THREAD, CORE_MAX_THREAD, KEEP_ALIVE_TIME, BASE_TIME_UNIT, queue);
	}
	
	public void setReceiver(CommonReceiver receiver) {
		this.receiver = receiver;
	}
	
	int numberOfErrors = 0;

	public void startServer() throws AbnormalException {
		if ( receiver == null ) {
			throw new AbnormalException("Listener isn't set. Before use this api, you should set listener in this class.");
		}
		needStop = false;
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					threadPool.execute(new Runnable() {
						public void run() {
							while( !needStop ) {
								try {
									receiver.listen();
									Thread.sleep(1000);
								} catch ( Exception e ) {
									numberOfErrors++;
									System.out.println("Listener can't Listen.");
									e.printStackTrace();
								}
								if ( numberOfErrors > 3 )
									needStop = true;
							}
						}
					});
				} catch ( Exception e ) {
					System.out.println("There are some errors in Thread Pool.");
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stopServer() {
		needStop = true;
		List<Runnable> unreleasedTasks = threadPool.shutdownNow();
		for ( Runnable oneTask : unreleasedTasks ) {
			try {
				// TODO : How to kill an unreleased runnable thread. I don't know. 
				System.out.println("Unreleased task:" + oneTask);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For Testing.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// 1. first get the default network interface. (such as eth0)
			NetworkInterface intf = NetworkInterface.getNetworkInterfaces().nextElement();
			InetAddress address = intf.getInetAddresses().nextElement();
			
			// 2. Next, create receiver & handler instance.
			CommonReceiver receiver = new UDPReceiver(intf, address, 1901);
			CommonHandler handler = new SSDPMessage();
			receiver.addReceiveHandler(handler);
			
			// 3. Create Common server
			CommonServer server = new CommonServer();
			
			// 4. set receiver into server.
			server.setReceiver(receiver);
			
			// 5. start server.
			server.startServer();
		} catch ( Exception e ) {
			e.printStackTrace();
		} catch ( AbnormalException abe ) {
			abe.printStackTrace();
		}
	}
	
}

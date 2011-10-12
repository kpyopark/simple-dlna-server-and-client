package com.lgcns.sol.upnp.network;

import java.util.ArrayList;

public class ReceiverManager {

	boolean needExit = false;
	
	ArrayList<CommonReceiver> receiverList = new ArrayList<CommonReceiver>();
	
	public void stop() {
		needExit = true;
	}
	
	public void start() throws Exception {
		
	}
	
}

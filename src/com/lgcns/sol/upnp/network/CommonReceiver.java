package com.lgcns.sol.upnp.network;

import java.util.Vector;

public abstract class CommonReceiver {

	Vector<CommonHandler> handlerList = new Vector<CommonHandler>();

	public void addUDPReceiveHandler(CommonHandler handler) {
		this.handlerList.add(handler);
	}
	
	public void clearHandler() {
		this.handlerList.clear();
	}

	abstract public void listen() throws Exception;
	
	public void clear() {
		clearHandler();
	}
	
}

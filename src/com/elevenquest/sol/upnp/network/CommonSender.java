package com.elevenquest.sol.upnp.network;

import java.util.Observer;


public abstract class CommonSender implements Observer {
	
	ICommonSendHandler handler = null;
	
	public void setSenderHandler(ICommonSendHandler handler) {
		this.handler = handler;
	}
	
	public void clearHandler() {
		handler = null;
	}
	
	public void sendData() throws Exception {
		HTTPRequest rtnValue = handler.getHTTPRequest();
		send(rtnValue);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	abstract protected void send(HTTPRequest sendData) throws Exception;
	
	public void clear() {
		clearHandler();
	}

}

package com.elevenquest.sol.upnp.network;

import java.util.Observer;


public abstract class HttpRequestSender implements Observer {
	
	IHttpRequestSuplier handler = null;
	
	public void setSenderHandler(IHttpRequestSuplier handler) {
		this.handler = handler;
	}
	
	public void clearHandler() {
		handler = null;
	}
	
	public void sendData() throws Exception {
		HttpRequest rtnValue = handler.getHTTPRequest();
		send(rtnValue);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	abstract protected void send(HttpRequest sendData) throws Exception;
	
	public void clear() {
		clearHandler();
	}

}

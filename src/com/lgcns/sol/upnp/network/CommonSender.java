package com.lgcns.sol.upnp.network;

import java.util.Observer;


public abstract class CommonSender implements Observer {
	
	CommonSendHandler handler = null;
	
	public void setSenderHandler(CommonSendHandler handler) {
		this.handler = handler;
	}
	
	public void clearHandler() {
		handler = null;
	}
	
	public void sendData() throws Exception {
		Object rtnValue = handler.getSendObject();
		send(rtnValue);
	}
	
	/**
	 * 실제로 network layer에 있는 자료를 보내주는 역활을 하는 메쏘드.
	 * 
	 * 주의 : 해당 메쏘드는 non-Blocking method로 간주한다.
	 *       따라서, Event가 발생되는 경우에만, 자료를 보내는 역활을 하는 것으로 간주한다.
	 * 
	 * @throws Exception
	 */
	abstract protected void send(Object sendData) throws Exception;
	
	public void clear() {
		clearHandler();
	}

}

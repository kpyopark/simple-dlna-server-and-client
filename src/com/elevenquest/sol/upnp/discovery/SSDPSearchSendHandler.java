package com.elevenquest.sol.upnp.discovery;

import java.util.Iterator;

import com.elevenquest.sol.upnp.control.ControlPoint;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.network.HTTPRequest;
import com.elevenquest.sol.upnp.network.ICommonSendHandler;

public class SSDPSearchSendHandler implements ICommonSendHandler {
	
	ControlPoint cp = null;
	
	SSDPSearchSendHandler(ControlPoint cp) {
		this.cp = cp;
	}
	
	public Object getSendObject() throws Exception {
		StringBuffer fullMessage = new StringBuffer();
		// append start header.
		fullMessage.append(SSDPMessage.ID_START_LINE_SEARCH).append('\n');
		// append other header list.
		fullMessage.append("").append(":").append("").append('\n');
		return fullMessage.toString().getBytes();
	}

	public Object processAfterSend(Object returnValue) {
		return null;
	}

	@Override
	public HTTPRequest getHTTPRequest() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

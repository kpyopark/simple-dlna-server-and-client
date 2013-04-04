package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.control.ControlPoint;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;

public class SSDPSearchSendHandler implements IHttpRequestSuplier {
	
	ControlPoint cp = null;
	
	public SSDPSearchSendHandler(ControlPoint cp) {
		this.cp = cp;
	}
	
	public Object processAfterSend(Object returnValue) {
		return null;
	}

	@Override
	public HttpRequest getHTTPRequest() throws Exception {
		HttpRequest request = new HttpRequest();
		request.setCommand(SSDPMessage.ID_START_LINE_COMMAND_SEARCH);
		request.setUrlPath("*");
		request.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
		return request;
	}

	@Override
	public void processAfterSend(HttpResponse returnValue) {
		// TODO Auto-generated method stub
		
	}

}

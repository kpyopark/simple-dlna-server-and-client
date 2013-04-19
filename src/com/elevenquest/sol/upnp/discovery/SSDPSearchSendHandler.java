package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.control.ControlPoint;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;

public class SSDPSearchSendHandler extends HttpRequest implements IHttpRequestSuplier {
	
	ControlPoint cp = null;
	
	public SSDPSearchSendHandler(ControlPoint cp) {
		this.cp = cp;
		this.setCommand(HttpHeaderName.ID_START_LINE_COMMAND_SEARCH);
		this.setUrlPath("*");
		this.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_POST, DefaultConfig.ID_UPNP_DISCOVERY_HOST_VALUE);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_MAN, HttpHeaderName.ID_NT_SUBTYPE_SSDPDISCOVER);
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_MX,"1");
		this.setHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_ST,"ssdp:all");	
	}
	
	public Object processAfterSend(Object returnValue) {
		return null;
	}

	@Override
	public HttpRequest getHTTPRequest() throws Exception {
		return this;
	}

	@Override
	public void processAfterSend(HttpResponse returnValue) {
		// TODO Auto-generated method stub
		
	}

}

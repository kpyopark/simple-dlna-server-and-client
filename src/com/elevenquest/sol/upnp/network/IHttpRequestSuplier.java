package com.elevenquest.sol.upnp.network;

public interface IHttpRequestSuplier {
	abstract HttpRequest getHTTPRequest() throws Exception;
	abstract void processAfterSend(HttpResponse returnValue);
}

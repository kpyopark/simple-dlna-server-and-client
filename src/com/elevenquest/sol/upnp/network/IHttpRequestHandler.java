package com.elevenquest.sol.upnp.network;



public interface IHttpRequestHandler {
	public HttpResponse process(HttpRequest packet);
}

package com.elevenquest.sol.upnp.network;

public interface ICommonSendHandler {
	abstract HTTPRequest getHTTPRequest() throws Exception;
	abstract Object processAfterSend(Object returnValue);
}

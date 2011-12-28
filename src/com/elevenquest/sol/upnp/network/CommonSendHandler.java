package com.elevenquest.sol.upnp.network;

public interface CommonSendHandler {
	abstract Object getSendObject() throws Exception;
	abstract Object processAfterSend(Object returnValue);
}

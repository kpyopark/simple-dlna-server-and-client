package com.elevenquest.sol.upnp.network;

public interface ICommonSendHandler {
	abstract Object getSendObject() throws Exception;
	abstract Object processAfterSend(Object returnValue);
}

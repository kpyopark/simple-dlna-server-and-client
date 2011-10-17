package com.lgcns.sol.upnp.network;

public abstract class CommonReceiver {
	
	abstract public Runnable listen() throws Exception;
	
}

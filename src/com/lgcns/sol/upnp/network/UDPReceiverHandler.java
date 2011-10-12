package com.lgcns.sol.upnp.network;

import java.net.DatagramPacket;

public interface UDPReceiverHandler {
	public void process(DatagramPacket packet);
}

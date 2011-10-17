package com.lgcns.sol.upnp.network;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPSender {
	NetworkInterface intf;
	int port;
	InetAddress target;
	String uri;

	String targetURL;
	
	/**
	 * @deprecated 
	 * @param intf
	 * @param port
	 * @param targetAddr
	 */
	public HTTPSender(NetworkInterface intf, int port, InetAddress targetAddr, String uri) {
		this.intf = intf;
		this.port = port;
		this.target = targetAddr;
		this.uri = uri;
		this.targetURL = null;
	}
	
	public HTTPSender(NetworkInterface intf, String url) {
		this.intf = intf;
		this.targetURL = url;
	}
	
	public void sendData(ArrayList<String> keyList, HashMap<String, String> properties, byte[] body) throws Exception {
		if ( this.targetURL == null ) {
			this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port + this.uri;
		}
		java.net.URL url = new URL(this.targetURL);
		HttpURLConnection urlCon = null;
		BufferedOutputStream bos = null;
		try {
			urlCon = (HttpURLConnection)url.openConnection();
			for ( String key: keyList ) {
				String value = properties.get(key);
				urlCon.addRequestProperty(key, value);
			}
			urlCon.setDoOutput(true);
			urlCon.connect();
			bos = new BufferedOutputStream(urlCon.getOutputStream());
			bos.write(body);
			bos.flush();
		} finally {
			if ( bos != null ) try { bos.close(); } catch ( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( urlCon != null) try { urlCon.disconnect(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}
	}
}

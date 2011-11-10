package com.lgcns.sol.upnp.network;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

public class HTTPSender extends CommonSender {
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

	@Override
	protected void send(Object sendData) throws Exception {
		// TODO Auto-generated method stub
		BasicHttpEntityEnclosingRequest request = (BasicHttpEntityEnclosingRequest)sendData;
		if ( this.targetURL == null ) {
			this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port + this.uri;
		}
		java.net.URL url = new URL(this.targetURL);
		HttpURLConnection urlCon = null;
		BufferedOutputStream bos = null;
		try {
			urlCon = (HttpURLConnection)url.openConnection();
			Header[] headers = request.getAllHeaders();
			for ( int inx = 0 ; headers != null && inx < headers.length ; inx++ ) {
				urlCon.addRequestProperty(headers[inx].getName(), headers[inx].getValue());
			}
			urlCon.setDoOutput(true);
			urlCon.connect();
			bos = new BufferedOutputStream(urlCon.getOutputStream());
			byte buffer[] = new byte[1024];
			int size = 0;
			while( ( size = request.getEntity().getContent().read(buffer, 0, 1024) ) != -1 ) {
				bos.write(buffer,0,size);
			}
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

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}

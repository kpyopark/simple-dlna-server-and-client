package com.elevenquest.sol.upnp.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Observable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
		
		// 1. At first, retrieving target URL.
		if ( this.targetURL == null ) {
			this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port + this.uri;
		}

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		if ( sendData instanceof HttpGet ) {
			HttpGet getRequest = (HttpGet)sendData;
			response = client.execute(getRequest);
		}
		else if ( sendData instanceof HttpPost ) {
			HttpPost postRequest = (HttpPost)sendData;
			response = client.execute(postRequest);
		}
		
		/*
		java.net.URL url = new URL(this.targetURL);
		HttpURLConnection urlCon = null;
		BufferedOutputStream bos = null;
		try {
			// 2. Open Connection
			urlCon = (HttpURLConnection)url.openConnection();
			
			// 3. Set header to request
			Header[] headers = request.getAllHeaders();
			for ( int inx = 0 ; headers != null && inx < headers.length ; inx++ ) {
				urlCon.addRequestProperty(headers[inx].getName(), headers[inx].getValue());
			}
			urlCon.setDoOutput(true);
			urlCon.connect();
			
			// 4. send request body.
			bos = new BufferedOutputStream(urlCon.getOutputStream());
			byte buffer[] = new byte[1024];
			int size = 0;
			while( ( size = request.getEntity().getContent().read(buffer, 0, 1024) ) != -1 ) {
				bos.write(buffer,0,size);
			}
			
			// 5. flush & output stream close.
			bos.flush();
			// bos.close(); -- it doesn't need.
			// 6. retrieving the response from the server.
			
			
		} finally {
			if ( bos != null ) try { bos.close(); } catch ( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( urlCon != null) try { urlCon.disconnect(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}
		*/
		handler.processAfterSend(response);
	}

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}

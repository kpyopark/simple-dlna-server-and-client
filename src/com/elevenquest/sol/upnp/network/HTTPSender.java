package com.elevenquest.sol.upnp.network;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
	protected void send(HTTPRequest request) throws Exception {

		HTTPResponse response = new HTTPResponse();
		
		// 1. At first, retrieving target URL.
		if ( this.targetURL == null ) {
			this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port + this.uri;
		}

		URL url = new URL(this.targetURL);
		HttpURLConnection urlCon = null;
		BufferedOutputStream bos = null;
		try {
			// 2. Open Connection
			urlCon = (HttpURLConnection)url.openConnection();
			
			// 3. Set header to request
			urlCon.setRequestMethod(request.getCommand());
			ArrayList<String> headerNames = request.getHeaderNames();
			ArrayList<String> headerValues = request.getHeaderValues();
			for ( int cnt = 0 ; cnt < request.getHeaderCount() ; cnt++ ) {
				urlCon.addRequestProperty(headerNames.get(cnt), headerValues.get(cnt));
			}
			urlCon.setDoOutput(true);
			urlCon.connect();
			
			// 4. send request body.
			bos = new BufferedOutputStream(urlCon.getOutputStream());
			byte buffer[] = new byte[1024];
			int size = 0;
			while( ( size = request.getBodyInputStream().read(buffer, 0, 1024) ) != -1 ) {
				bos.write(buffer,0,size);
			}
			
			// 5. flush & output stream close.
			bos.flush();
			bos.close();

			// 6. retrieving the response from the server.
			int status = urlCon.getResponseCode();
			for (Entry<String, List<String>> header : urlCon.getHeaderFields().entrySet()) {
				for (String value : header.getValue().toArray(new String[0]) )
					response.addHeader(header.getKey(), value);
			}
			response.setBodyInputStream(urlCon.getInputStream());
			
		} finally {
			if ( bos != null ) try { bos.close(); } catch ( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( urlCon != null) try { urlCon.disconnect(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
		}

		handler.processAfterSend(response);
	}

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}

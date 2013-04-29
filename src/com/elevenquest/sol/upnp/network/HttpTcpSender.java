package com.elevenquest.sol.upnp.network;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.StringTokenizer;

import com.elevenquest.sol.upnp.common.Logger;

public class HttpTcpSender extends HttpRequestSender {
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
	public HttpTcpSender(NetworkInterface intf, int port, InetAddress targetAddr, String uri) {
		this.intf = intf;
		this.port = port;
		this.target = targetAddr;
		this.uri = uri;
		this.targetURL = null;
	}
	
	public HttpTcpSender(NetworkInterface intf, String url) {
		this.intf = intf;
		this.targetURL = url;
		try {
			URL tempUrl = new URL(url);
			this.target = InetAddress.getByName(tempUrl.getHost());
			this.port = tempUrl.getPort();
			this.uri = tempUrl.getPath();
		} catch (UnknownHostException uhe) {
			Logger.println(Logger.WARNING, "This host of url[" + url + "] is unknown.");
		} catch (MalformedURLException mfe) {
			Logger.println(Logger.WARNING, "This form of url[" + url + "] is malformed.");
		}
	}

	@Override
	protected void send(HttpRequest request) throws Exception {

		// 1. At first, retrieving target URL.
		if ( this.targetURL == null ) {
			if ( this.uri == null || this.uri.length() <= 0 ) {
				Logger.println(Logger.WARNING, "There is no URI in this request.");
				this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port;
			} else {
				if ( this.uri.charAt(0) == '/' ) {
					this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port + this.uri.substring(1);
				} else {
					this.targetURL = "http://" + this.target.getHostAddress() + ":" + this.port + this.uri;
				}
			}
		}
		HttpResponse response = new HttpResponse();

		HttpURLConnection urlCon = null;
		BufferedOutputStream bos = null;
		try {
			// 2. Open Connection
			if ( !request.getCommand().equals(HttpRequest.HTTP_REQUEST_COMMAND_POST) ) {
				// Concatenate parameter pairs into URI.
				StringBuffer parameterPairs = new StringBuffer();
				for ( int cnt = 0; cnt < request.getHeaderCount(); cnt++ ) {
					// TODO : using URLEncoder
					parameterPairs.append("&").append(request.getHeaderName(cnt)).append(":").append(request.getHeaderValue(cnt));
				}
				if ( !this.uri.contains("?") ) {
					parameterPairs.setCharAt(0, '?');
				}
				if ( parameterPairs.length() > 255 ) {
					Logger.println(Logger.WARNING, "This request has invalid header fields. The length of parameter exceeds 255 characters.[" + parameterPairs.length() + "]");
				}
			}
			URL url = new URL(this.targetURL);
			urlCon = (HttpURLConnection)url.openConnection();
			
			// 3. Set header to request
			urlCon.setRequestMethod(request.getCommand());
			ArrayList<String> headerNames = request.getHeaderNames();
			ArrayList<String> headerValues = request.getHeaderValues();
			for ( int cnt = 0 ; cnt < request.getHeaderCount() ; cnt++ ) {
				urlCon.addRequestProperty(headerNames.get(cnt), headerValues.get(cnt));
			}
			
			if ( request.getCommand().equals(HttpRequest.HTTP_REQUEST_COMMAND_POST) ) {
				urlCon.setDoOutput(true);
				urlCon.setDoInput(true);
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
			}
			urlCon.setUseCaches(false);
			urlCon.setDefaultUseCaches(false);
			urlCon.connect();
			

			// 6. retrieving the response from the server.
			int status = urlCon.getResponseCode();
			response.setStatusCode(status+"");
			for (Entry<String, List<String>> header : urlCon.getHeaderFields().entrySet()) {
				for (String value : header.getValue().toArray(new String[0]) ) {
					if ( header.getKey() == null ) {
						// This is start line.
						StringTokenizer st = new StringTokenizer(value," ");
						if ( st.countTokens() < 3 ) {
							Logger.println(Logger.ERROR, "The response has invalid start line[" + value + "].");
						} else {
							String httpVer = st.nextToken();
							String statusCode = st.nextToken();
							String reasonPhrase = st.nextToken();
							response.setHttpVer(httpVer);
							response.setReasonPhrase(reasonPhrase);
							response.setStatusCode(statusCode);
						}
					} else {
						response.addHeader(header.getKey(), value);
					}
				}
			}
			if (status == 200) {
				response.setBodyInputStream(urlCon.getInputStream());
				//Logger.println(Logger.INFO, "BODY:" + new String(response.getBodyArray()));
			} else {
				response.setBodyInputStream(urlCon.getErrorStream());
				Logger.println(Logger.WARNING, "ERROR BODY:" + new String(response.getBodyArray()));
			}
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
	
	public static void main(String[] args) {
		URL url = null;
		HttpURLConnection urlCon = null;
		BufferedReader br = null;
		try {
			// 2. Open Connection
			url = new URL("http://192.168.56.1:2869/upnphost/udhisapi.dll?content=uuid:23d14937-c5ca-4e91-8a7f-cebe884be026");
			urlCon = (HttpURLConnection)url.openConnection();
			
			// 3. Set header to request
			urlCon.setRequestMethod("GET");
			urlCon.addRequestProperty("Content-Type", "text/html;charset=UTF8");
			urlCon.addRequestProperty("User-Agent", "Simple DLNA Server");
			//urlCon.setDoOutput(true);
			//urlCon.setDoInput(true);
			urlCon.setUseCaches(false);
			urlCon.setDefaultUseCaches(false);
			
			//urlCon.connect();
			
			// 4. send request body.
			/*
			bos = new BufferedOutputStream(urlCon.getOutputStream());
			byte buffer[] = new byte[1024];
			int size = 0;
			while( ( size = request.getBodyInputStream().read(buffer, 0, 1024) ) != -1 ) {
				bos.write(buffer,0,size);
			}
			
			// 5. flush & output stream close.
			bos.flush();
			bos.close();
			*/
			// 6. retrieving the response from the server.
			int status = urlCon.getResponseCode();
			System.out.println("status code:" + status);
			for (Entry<String, List<String>> header : urlCon.getHeaderFields().entrySet()) {
				for (String value : header.getValue().toArray(new String[0]) )
					System.out.println(header.getKey() + ":" + value );
			}
			br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String aLine = null;
			while( ( aLine = br.readLine() ) != null ) {
				System.out.println(aLine);
			}
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		} finally {
			if ( urlCon != null) try { urlCon.disconnect(); } catch( Exception e1 ) {
				e1.printStackTrace();
			}
			if ( br != null ) try { br.close(); } catch ( Exception e2 ) {
				e2.printStackTrace();
			}
		}
		
	}
}

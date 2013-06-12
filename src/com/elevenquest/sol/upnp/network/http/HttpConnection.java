package com.elevenquest.sol.upnp.network.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;

public class HttpConnection {

	Socket clientSoc = null;
	String url = null;
	
	InetAddress targetIp = null;
	int targetPort = 80;
	
	String path;

	String method = null;
	
	HttpRequest request = null;
	HttpResponse response = null;
	ArrayList<String> headerNames = null;
	ArrayList<String> headerValues = null;
	boolean isDoOutput = false;
	boolean isDoInput = false;
	boolean isOpened = false;
	
	public HttpConnection(String url, HttpRequest request) {
		this.url = url;
		this.request = request;
		headerNames = new ArrayList<String>();
		headerValues = new ArrayList<String>();
	}
	
	public boolean openConnection() {
		try {
			// 1. parse url
			if ( parseURL() ) {
				// 2. setting socket parameter & open.
				clientSoc = new Socket(this.targetIp, this.targetPort);
				this.isOpened = true;
			} else {
				Logger.println(Logger.WARNING, "[HTTP CLIENT] There are invalid parameters when to parse URL." );
				this.isOpened = false;
			}
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
			Logger.println(Logger.ERROR, "[HTTP CLIENT]" + ioe.getMessage());
			this.isOpened = false;
		}
		return this.isOpened;
	}
	
	private boolean parseURL() {
		
		boolean result = true;
		
		if ( this.url.indexOf("http://") != 0 )
			return false;
		String addressPortPath = this.url.substring(7);
		String addressPort = addressPortPath.substring(addressPortPath.indexOf('/'));
		String address = null;
		if ( addressPort.indexOf(':') > 0 ) {
			address = addressPort.substring(addressPort.indexOf(':'));
			this.targetPort = Integer.parseInt(addressPort.substring(address.length() + 1));
		} else {
			address = addressPort;
		}
		try {
			this.targetIp = InetAddress.getByName(address);
			this.path = addressPortPath.substring(addressPortPath.indexOf('/'));
		} catch ( UnknownHostException uhe ) {
			uhe.printStackTrace();
			Logger.println(Logger.WARNING, "[HTTP CLINET]" + uhe.getMessage());
			result = false;
		}
		return result;
	}
	
	public void setRequestMethod(String method) {
		this.method = method;
	}
	
	public void addRequestProperty(String name, String value) {
		headerNames.add(name);
		headerValues.add(value);
	}
	
	public void setDoOutput(boolean flag) {
		this.isDoOutput = flag;
	}
	
	public void setDoInput(boolean flag) {
		this.isDoInput = flag;
	}
	
	public void setUseCaches(boolean flag) {
		// TODO:
	}
	
	public void setDefaultUseCaches(boolean flag) {
		// TODO:
	}
	
	private static String readLine(InputStream is) throws IOException {
		int oneByte = 0;
		boolean isCr = false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ( (oneByte = is.read()) != -1 ) {
			if ( oneByte == '\r' )
				isCr = true;
			if ( isCr == true && oneByte == '\n' ) {
				break;
			}
			baos.write(oneByte);
		}
		return new String(baos.toByteArray());
	}
	
	public void connect() throws IOException {
		BufferedOutputStream bos = null;
		InputStream is = null;
		try {
			// 1. Send parameters in Http request.
			bos = new BufferedOutputStream(this.clientSoc.getOutputStream());
			String requestLine = this.method + " " + this.url + " " + this.request.getHttpVer() + "\r\n";
			bos.write(requestLine.getBytes());
			String headerAndValue = null;
			ArrayList<String> names = request.getHeaderNames();
			for ( String name : names ) {
				if ( this.isDoOutput && name.equalsIgnoreCase("Content-Length")) {
					// skip..
				} else {
					headerAndValue = name + ": " + request.getHeaderValue(name) + "\r\n";
					bos.write(headerAndValue.getBytes());
				}
			}
			if ( this.isDoOutput ) {
				headerAndValue = "Content-Length: " + this.request.getBodyArray().length + "\r\n";
				bos.write(headerAndValue.getBytes());
			}
			bos.write("\r\n".getBytes());
			if ( this.isDoOutput ) {
				bos.write(this.request.getBodyArray());
			}
			bos.flush();
			bos.close();
			
			// 2. Receive fields in Http Response.
			
			is = this.clientSoc.getInputStream();
			response = new HttpResponse();
			String statusLine = readLine(is);
			response.setStatusLine(statusLine);
			String aLine = null;
			while ( !( aLine == null || aLine.length() == 0 || aLine.equalsIgnoreCase("\r\n")) ) {
				response.setHeaderField(aLine);
			}
			// end of header fields.
			String contentLength = null;
			String transferCoding = null;
			if ( (contentLength = response.getHeaderValue("Content-length")) != null ) {
				int targetLength = Integer.parseInt(contentLength);
				int downloadLength = 0;
				int length = -1;
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ( ( length = is.read(buffer, 0, 1024) ) != -1 && ( downloadLength < targetLength ) ) {
					downloadLength += length;
					baos.write(buffer, 0, length);
				}
				response.setBodyArray(baos.toByteArray());
			} else if ( ( transferCoding = response.getHeaderValue("Transfer-Encoding") ) != null && transferCoding.equalsIgnoreCase("chunked") ) {
				// chunked transfering.
				// ToDO :
				Logger.println(Logger.ERROR, "[HTTP RESPONSE] There is no implementation for chunked tranfering.");
			}
		} finally {
			if ( bos != null ) try { bos.close(); } catch ( Exception e ) { e.printStackTrace(); }
			if ( is != null ) try { is.close(); } catch ( Exception e ) { e.printStackTrace(); }
		}
		
	}
	
	public int getResponseCode() {
		return 200;
	}
	
	public Hashtable<String, List<String>> getHeaderFields() {
		return null;
	}
	
	public InputStream getInputStream() {
		return null;
	}
	
	public OutputStream getOutputStream() {
		return null;
	}
	
	public OutputStream getErrorStream() {
		return null;
	}
	
	public HttpResponse getHttpResponse() {
		return this.response;
	}
	
	public void disconnect() {
		try {
			clientSoc.close();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
			Logger.println(Logger.WARNING, "[HTTP CONNECTION] There is some issues during disconnecting with server.[" + this.url + "]");
		}
	}
}

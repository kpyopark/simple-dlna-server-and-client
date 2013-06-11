package com.elevenquest.sol.upnp.network.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.elevenquest.sol.upnp.network.HttpRequest;

public class HttpConnection {

	Socket clientSoc = null;
	HttpRequest request = null;
	String method = null;
	ArrayList<String> headerNames = null;
	ArrayList<String> headerValues = null;
	boolean isDoOutput = false;
	boolean isDoInput = false;
	
	public HttpConnection(HttpRequest request) {
		this.request = request;
		headerNames = new ArrayList<String>();
		headerValues = new ArrayList<String>();
	}
	
	public void openConnection() {
		
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
		
	}
	
	public void setDefaultUseCaches(boolean flag) {
		
	}
	
	public void connect() {
		
	}
	
	public String getResponseCode() {
		return null;
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

}

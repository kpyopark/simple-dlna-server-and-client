package com.elevenquest.sol.upnp.network;

import java.util.ArrayList;

public class HttpBaseStructure {
	String startLine = null;
	ArrayList<String> headerNames = new ArrayList<String>();
	ArrayList<String> headerValues = new ArrayList<String>();
	byte[] body = null;
	HttpResponse response = null;
	HttpRequest request = null;
	
	public String getStartLine() {
		return startLine;
	}
	public void setStartLine(String startLine) {
		this.startLine = startLine;
	}
	public ArrayList<String> getHeaderNames() {
		return headerNames;
	}
	public void setHeaderNames(ArrayList<String> headerNames) {
		this.headerNames = headerNames;
	}
	public ArrayList<String> getHeaderValues() {
		return headerValues;
	}
	public void setHeaderValues(ArrayList<String> headerValues) {
		this.headerValues = headerValues;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public HttpResponse getResponse() {
		return response;
	}
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	public HttpRequest getRequest() {
		return request;
	}
	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	
}


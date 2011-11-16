package com.lgcns.sol.upnp.description;

import java.io.ByteArrayInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;

import com.lgcns.sol.upnp.model.UPnPAction;
import com.lgcns.sol.upnp.model.UPnPService;
import com.lgcns.sol.upnp.model.UPnPStateVariable;
import com.lgcns.sol.upnp.xml.SDSXMLParser;

public class ServiceDescription implements com.lgcns.sol.upnp.network.CommonSendHandler {
	
	String specMajor;
	String specMinor;
	
	UPnPService service = null;
	
	public ServiceDescription(UPnPService service) {
		if ( service != null )
			this.service = service;
	}
	
	public void addAction(UPnPAction action) {
		if ( service != null )
			this.service.registerAction(action);
	}
	
	public void addStateVariable(UPnPStateVariable statVar) {
		if ( service != null )
			this.service.registerStateVariable(statVar);
	}
	
	public UPnPStateVariable getStateVariable(String name) {
		return this.service.getStateVariable(name);
	}

	public Object getSendObject() throws Exception {
		HttpPost request = new HttpPost(this.service.getDevice().getAbsoluteURL(this.service.getScpdUrl()));
		String osVersion = "WindowsNT";
		String productVersion = "simpledlna/1.0";
		request.addHeader("USER-AGENT", osVersion + " UPnP/1.1 " + productVersion );
		request.addHeader("HOST", this.service.getDevice().getBaseHost() );
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream(this.getRequestBody().getBytes("utf-8")));
		if ( this.service.getDevice().getAuthorizationStr() != null )
			request.addHeader("Authorization", "Basic " + this.service.getDevice().getAuthorizationStr() );
		return request;
	}
	
	private String getRequestBody() {
		return "";
	}

	public Object processAfterSend(Object returnValue) {
		HttpResponse response = (HttpResponse)returnValue;
		try {
			if ( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
				HttpEntity entity = response.getEntity();
				SDSXMLParser parser = new SDSXMLParser(this, entity.getContent());
				parser.execute();
				
			}
		} catch ( Exception e ) {
			
		}
		return null;
	}
	
	public UPnPService getService() {
		return service;
	}

	public void setService(UPnPService service) {
		this.service = service;
	}

	public String getSpecMajor() {
		return specMajor;
	}

	public void setSpecMajor(String specMajor) {
		this.specMajor = specMajor;
	}

	public String getSpecMinor() {
		return specMinor;
	}

	public void setSpecMinor(String specMinor) {
		this.specMinor = specMinor;
	}
	
}

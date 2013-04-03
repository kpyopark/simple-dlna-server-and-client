package com.elevenquest.sol.upnp.description;

import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.network.HTTPRequest;
import com.elevenquest.sol.upnp.network.HTTPResponse;
import com.elevenquest.sol.upnp.xml.SDSXMLParser;

public class ServiceDescription implements com.elevenquest.sol.upnp.network.ICommonSendHandler {
	
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

	private String getRequestBody() {
		return "";
	}

	public Object processAfterSend(Object returnValue) {
		HTTPResponse response = (HTTPResponse)returnValue;
		try {
			if ( response.getStatusCode().equals("200") ) {
				SDSXMLParser parser = new SDSXMLParser(this, response.getBodyInputStream());
				parser.execute();
				
				if(this.service!=null)
					this.service.printAllDescirption();
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

	@Override
	public HTTPRequest getHTTPRequest() throws Exception {
		HTTPRequest request = new HTTPRequest();
		request.setHttpVer("HTTP/1.1");
		request.setCommand("GET");
		request.setUrlPath(this.service.getDevice().getLocation());
		String osVersion = "WindowsNT";
		String productVersion = "simpledlna/1.0";
		request.addHeader("USER-AGENT", osVersion + " UPnP/1.1 " + productVersion );
		request.addHeader("HOST", this.service.getDevice().getBaseHost() );
		request.setBodyArray(this.getRequestBody().getBytes("utf-8"));
		if ( this.service.getDevice().getAuthorizationStr() != null )
			request.addHeader("Authorization", "Basic " + this.service.getDevice().getAuthorizationStr() );
		return request;
	}

}

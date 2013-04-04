package com.elevenquest.sol.upnp.description;

import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.xml.SDSXMLParser;

public class ServiceDescription implements com.elevenquest.sol.upnp.network.IHttpRequestSuplier {
	
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
	public HttpRequest getHTTPRequest() throws Exception {
		HttpRequest request = new HttpRequest();
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

	@Override
	public void processAfterSend(HttpResponse returnValue) {
		HttpResponse response = (HttpResponse)returnValue;
		try {
			if ( response.getStatusCode().equals(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200) ) {
				SDSXMLParser parser = new SDSXMLParser(this, response.getBodyInputStream());
				parser.execute();
				
				if(this.service!=null)
					this.service.printAllDescirption();
			}
		} catch ( Exception e ) {
			
		}
	}

}

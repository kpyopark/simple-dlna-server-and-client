package com.lgcns.sol.upnp.description;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

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

	public Object getSendObject() throws Exception {
		// TODO Auto-generated method stub
		return null;
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


	
}

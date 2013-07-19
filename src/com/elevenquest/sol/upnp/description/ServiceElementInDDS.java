package com.elevenquest.sol.upnp.description;

import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.service.avtransport.AVTransportService;
import com.elevenquest.sol.upnp.service.connection.ConnectionManagementService;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryService;
import com.elevenquest.sol.upnp.service.rederingcontrol.RenderingControlService;

public class ServiceElementInDDS implements ICommonDescription {
	
	static final String SD_REPLACEABLE_PART_SERVICE_TYPE = "#SERVICE_TYPE#";
	static final String SD_REPLACEABLE_PART_SERVICE_ID = "#SERVICE_ID#";
	static final String SD_REPLACEABLE_PART_SCPD_URL = "#SCPD_URL#";
	static final String SD_REPLACEABLE_PART_CONTROL_URL = "#CONTROL_URL#";
	static final String SD_REPLACEABLE_PART_EVENTSUB_URL = "#EVENT_URL#";
	
	static final String DEVICE_DESCRIPTION_SERVICE_TEMPLATE =
		"<service>" +
		"<serviceType>" + SD_REPLACEABLE_PART_SERVICE_TYPE + "</serviceType>" +
		"<serviceId>" + SD_REPLACEABLE_PART_SERVICE_ID + "</serviceId>" +
		"<SCPDURL>" + SD_REPLACEABLE_PART_SCPD_URL + "</SCPDURL>" +
		"<controlURL>" + SD_REPLACEABLE_PART_CONTROL_URL + "</controlURL>" +
		"<eventSubURL>" + SD_REPLACEABLE_PART_EVENTSUB_URL + "</eventSubURL>" +
		"</service>";
	
	String serviceType;
	String serviceId;
	String scpdUrl;
	String controlUrl;
	String eventsubUrl;
	
	public String getDescription() {
		return DEVICE_DESCRIPTION_SERVICE_TEMPLATE.replace(SD_REPLACEABLE_PART_SERVICE_TYPE, this.serviceType)
				.replace(SD_REPLACEABLE_PART_SERVICE_ID, this.serviceId)
				.replace(SD_REPLACEABLE_PART_SCPD_URL, this.scpdUrl)
				.replace(SD_REPLACEABLE_PART_CONTROL_URL, this.controlUrl)
				.replace(SD_REPLACEABLE_PART_EVENTSUB_URL, this.controlUrl);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getScpdUrl() {
		return scpdUrl;
	}

	public void setScpdUrl(String scpdUrl) {
		this.scpdUrl = scpdUrl;
	}

	public String getControlUrl() {
		return controlUrl;
	}

	public void setControlUrl(String controlUrl) {
		this.controlUrl = controlUrl;
	}

	public String getEventsubUrl() {
		return eventsubUrl;
	}

	public void setEventsubUrl(String eventsubUrl) {
		this.eventsubUrl = eventsubUrl;
	}
	
	public UPnPService getDefaultUPnPService(UPnPDevice device) {
		// TODO : modify below lines. It should be made by using factory pattern.
		UPnPService service = null;
		if ( this.getServiceId().equals(UPnPService.UPNP_SERVICE_ID_CDS) ) {
			service = new ContentDirectoryService(device);
		} else if ( this.getServiceId().equals(UPnPService.UPNP_SERVICE_ID_CMS) ) {
			service = new ConnectionManagementService(device);
		} else if ( this.getServiceId().equals(UPnPService.UPNP_SERVICE_ID_RCS) ) {
			service = new RenderingControlService(device);
		} else if ( this.getServiceId().equals(UPnPService.UPNP_SERVICE_ID_AVT) ) {
			service = new AVTransportService(device);
		} else {
			service = new UPnPService(device);
		}
		service.setScpdUrl(this.getScpdUrl());
		service.setControlUrl(this.getControlUrl());
		service.setEventsubUrl(this.getEventsubUrl());
		service.setServiceId(this.getServiceId());
		service.setServiceType(this.getServiceType());
		return service;
	}
}

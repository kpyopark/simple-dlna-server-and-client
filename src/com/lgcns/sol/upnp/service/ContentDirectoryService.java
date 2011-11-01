package com.lgcns.sol.upnp.service;

import com.lgcns.sol.upnp.model.UPnPDevice;
import com.lgcns.sol.upnp.model.UPnPService;

/**
 * There are two type of services.
 * 
 * One is local service. If the device which contains this service is local one, you should call implemented java method for using service's functionality.
 * The other is remote service, If the device which contains this service is remote one, you should call SOAP action instead of java's method. 
 * 
 * BUT, we don't care now. In all cases, we use SOAP action.
 * later, I hope someone help us to enlarge these functionalities.
 *   
 * @author toheavener01@gmail.com
 *
 */
public class ContentDirectoryService extends UPnPService {

	public ContentDirectoryService(UPnPDevice device) {
		super(device);
	}
	
	/*
	GetSearchCapabilities
	R
	GetSortCapabilities
	R
	GetSystemUpdateID
	R
	Browse
	R
	Search
	O
	CreateObject
	O
	DestroyObject
	O
	UpdateObject
	O
	ImportResource
	O
	ExportResource
	O
	StopTransferResource
	O
	GetTransferProgress
	O
	*/
	
}

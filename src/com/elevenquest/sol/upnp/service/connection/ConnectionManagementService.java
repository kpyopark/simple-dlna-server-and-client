package com.elevenquest.sol.upnp.service.connection;

import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPService;

public class ConnectionManagementService extends UPnPService {

	public ConnectionManagementService(UPnPDevice device) {
		super(device);
		// TODO Auto-generated constructor stub
	}
	
	// definition of state variables.
	static final String SV_NAME_CMS_SourceProtocolInfo = "SourceProtocolInfo";
	static final String SV_NAME_CMS_SinkProtocolInfo = "SinkProtocolInfo";
	static final String SV_NAME_CMS_CurrentConnectionIDs = "CurrentConnectionIDs";

	static final String SV_NAME_CDS_A_ARG_TYPE_ConnectionStatus = "A_ARG_TYPE_ConnectionStatus";
	static final String SV_NAME_CDS_A_ARG_TYPE_ConnectionManager = "A_ARG_TYPE_ConnectionManager";
	static final String SV_NAME_CDS_A_ARG_TYPE_Direction = "A_ARG_TYPE_Direction";
	static final String SV_NAME_CDS_A_ARG_TYPE_ProtocolInfo = "A_ARG_TYPE_ProtocolInfo";
	static final String SV_NAME_CDS_A_ARG_TYPE_ConnectionID = "A_ARG_TYPE_ConnectionID";
	static final String SV_NAME_CDS_A_ARG_TYPE_AVTransportID = "A_ARG_TYPE_AVTransportID";
	static final String SV_NAME_CDS_A_ARG_TYPE_RcsID = "A_ARG_TYPE_RcsID";

	// List of parameters used in each UPnPAction.
	static final String ACTION_ARG_NAME_Source = "Source";
	static final String ACTION_ARG_NAME_Sink = "Sink";
	static final String ACTION_ARG_NAME_Id = "Id";
	static final String ACTION_ARG_NAME_ObjectID = "ObjectID";
	
	// List of Action Name.
	static final String ACTION_NAME_CMS_GetProtocolInfo = "GetProtocolInfo";
	static final String ACTION_NAME_CMS_PrepareForConnection = "PrepareForConnection";
	static final String ACTION_NAME_CMS_ConnectionComplete = "ConnectionComplete";
	static final String ACTION_NAME_CMS_GetCurrentConnectionIDs = "GetCurrentConnectionIDs";
	static final String ACTION_NAME_CMS_GetCurrentConnectionInfo = "GetCurrentConnectionInfo";
	
}

package com.elevenquest.sol.upnp.service.connection;

import java.util.ArrayList;

import com.elevenquest.sol.upnp.action.ActionExecutor;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryItem;

public class ConnectionManagementService extends UPnPService {

	public ConnectionManagementService(UPnPDevice device) {
		super(device);
		// TODO Auto-generated constructor stub
	}
	
	// definition of state variables.
	static final String SV_NAME_CMS_SourceProtocolInfo = "SourceProtocolInfo";		// CSV2 (string)
	static final String SV_NAME_CMS_SinkProtocolInfo = "SinkProtocolInfo";			// CSV (string)
	static final String SV_NAME_CMS_CurrentConnectionIDs = "CurrentConnectionIDs";	// CSV (ui4)

	static final String SV_NAME_CMS_A_ARG_TYPE_ConnectionStatus = "A_ARG_TYPE_ConnectionStatus";	// "OK", "ContentFormatMismatch", "InsufficientBandwidth", "UnreliableChannel", "Unknown"
	static final String SV_NAME_CMS_A_ARG_TYPE_ConnectionManager = "A_ARG_TYPE_ConnectionManager";	// string
	static final String SV_NAME_CMS_A_ARG_TYPE_Direction = "A_ARG_TYPE_Direction";	// "Output", "Input"
	static final String SV_NAME_CMS_A_ARG_TYPE_ProtocolInfo = "A_ARG_TYPE_ProtocolInfo";	// string
	static final String SV_NAME_CMS_A_ARG_TYPE_ConnectionID = "A_ARG_TYPE_ConnectionID";	// i4
	static final String SV_NAME_CMS_A_ARG_TYPE_AVTransportID = "A_ARG_TYPE_AVTransportID";	// i4
	static final String SV_NAME_CMS_A_ARG_TYPE_RcsID = "A_ARG_TYPE_RcsID";					// i4

	// List of parameters used in each UPnPAction.
	static final String ACTION_ARG_NAME_Source = "Source";
	static final String ACTION_ARG_NAME_Sink = "Sink";
	static final String ACTION_ARG_NAME_RemoteProtocolInfo = "RemoteProtocolInfo";
	static final String ACTION_ARG_NAME_PeerConnectionManager = "PeerConnectionManager";
	static final String ACTION_ARG_NAME_PeerConnectionID = "PeerConnectionID";
	static final String ACTION_ARG_NAME_Direction = "Direction";
	static final String ACTION_ARG_NAME_ConnectionID = "ConnectionID";
	static final String ACTION_ARG_NAME_AVTransportID = "AVTransportID";
	static final String ACTION_ARG_NAME_RcsID = "RcsID";
	static final String ACTION_ARG_NAME_ConnectionIDs = "ConnectionIDs";
	static final String ACTION_ARG_NAME_ProtocolInfo = "ProtocolInfo";
	static final String ACTION_ARG_NAME_Status = "Status";
	
	// List of Action Name.
	static final String ACTION_NAME_CMS_GetProtocolInfo = "GetProtocolInfo";
	static final String ACTION_NAME_CMS_PrepareForConnection = "PrepareForConnection";
	static final String ACTION_NAME_CMS_ConnectionComplete = "ConnectionComplete";
	static final String ACTION_NAME_CMS_GetCurrentConnectionIDs = "GetCurrentConnectionIDs";
	static final String ACTION_NAME_CMS_GetCurrentConnectionInfo = "GetCurrentConnectionInfo";

	private ArrayList<String> getStateVariableSourceProtocolInfo() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CMS_SourceProtocolInfo);
		ArrayList<String> returnValue = ( value == null ) ? new ArrayList<String>() : UPnPStateVariable.getCSVStateVariableStrings((String)value.getValue());
		return returnValue;
	}
	
	private ArrayList<String> getStateVariableSinkProtocolInfo() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CMS_SinkProtocolInfo);
		ArrayList<String> returnValue = ( value == null ) ? new ArrayList<String>() : UPnPStateVariable.getCSVStateVariableStrings((String)value.getValue());
		return returnValue;
	}

	private ArrayList<Integer> getStateVariableCurrentConnectionIDs() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CMS_CurrentConnectionIDs);
		ArrayList<Integer> returnValue = ( value == null ) ? new ArrayList<Integer>() : UPnPStateVariable.getCSVStateVariableIntegers((String)value.getValue());
		return returnValue;
	}
	
	public ArrayList<String> getSourceProtocolInfo() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CMS_GetProtocolInfo);
		ActionExecutor executor = new ActionExecutor(targetAction);
		executor.execute();
		return getStateVariableSourceProtocolInfo();
	}
	
	public ArrayList<String> getSinkProtocolInfo() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CMS_GetProtocolInfo);
		ActionExecutor executor = new ActionExecutor(targetAction);
		executor.execute();
		return getStateVariableSinkProtocolInfo();
	}

	public ConnectionItem prepareForConnection(String remoteProtocolInfo, String connectionManager, int peerConnectionID, String direction) throws Exception {
		ConnectionItem item = new ConnectionItem();
		UPnPAction targetAction = this.getAction(ACTION_NAME_CMS_PrepareForConnection);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_RemoteProtocolInfo).setValue(remoteProtocolInfo);
			targetAction.getInArgument(ACTION_ARG_NAME_PeerConnectionManager).setValue(connectionManager);
			targetAction.getInArgument(ACTION_ARG_NAME_PeerConnectionID).setValue(peerConnectionID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_Direction).setValue(direction);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			int connectionID = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_ConnectionID).getValue());
			int AVTransportID = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_AVTransportID).getValue());
			int rcsID = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_AVTransportID).getValue());
			
			item.setConnectionID(connectionID);
			item.setAVTransportID(AVTransportID);
			item.setRcsID(rcsID);

		} else {
			System.out.println("There is no prepareForConnection action in this device.");
		}
		return item;
	}
	
	public void connectionComplete(int connectionID) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CMS_ConnectionComplete);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_ConnectionID).setValue(connectionID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
		} else {
			System.out.println("There is no prepareForConnection action in this device.");
		}
	}
	
	public ArrayList<Integer> getCurrentConnectionIDs() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CMS_GetCurrentConnectionIDs);
		ActionExecutor executor = new ActionExecutor(targetAction);
		executor.execute();
		return getStateVariableCurrentConnectionIDs();
	}
	
	public ConnectionItem getCurrentConnectionInfo(int connectionID) throws Exception {
		ConnectionItem item = new ConnectionItem();
		UPnPAction targetAction = this.getAction(ACTION_NAME_CMS_GetCurrentConnectionInfo);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_ConnectionID).setValue(connectionID + "");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			item.setRcsID(Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_RcsID).getValue()));
			item.setAVTransportID(Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_AVTransportID).getValue()));
			item.setProtocolInfo(targetAction.getOutArgument(ACTION_ARG_NAME_AVTransportID).getValue());
			// TODO: 3-5 일 여기까지 완성. 
			int AVTransportID = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_AVTransportID).getValue());
			int rcsID = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_AVTransportID).getValue());
			
			item.setConnectionID(connectionID);
			item.setAVTransportID(AVTransportID);
			item.setRcsID(rcsID);

		} else {
			System.out.println("There is no prepareForConnection action in this device.");
		}
		return item;
	}

}

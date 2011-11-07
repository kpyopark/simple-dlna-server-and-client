package com.lgcns.sol.upnp.service;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.lgcns.sol.upnp.action.ActionExecutor;
import com.lgcns.sol.upnp.model.UPnPAction;
import com.lgcns.sol.upnp.model.UPnPDataType;
import com.lgcns.sol.upnp.model.UPnPDevice;
import com.lgcns.sol.upnp.model.UPnPService;
import com.lgcns.sol.upnp.model.UPnPStateVariable;

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
	
	// You should know the difference between argument's name and state variable's name.
	
	static final String SV_NAME_CDS_TRANSFERIDS = "TransferIDs";
	static final String SV_NAME_CDS_A_ARG_TYPE_ObjectID = "A_ARG_TYPE_ObjectID";
	static final String SV_NAME_CDS_A_ARG_TYPE_Result = "A_ARG_TYPE_Result";
	static final String SV_NAME_CDS_A_ARG_TYPE_SearchCriteria = "A_ARG_TYPE_SearchCriteria";
	static final String SV_NAME_CDS_A_ARG_TYPE_BrowseFlag = "A_ARG_TYPE_BrowseFlag";
	static final String SV_NAME_CDS_A_ARG_TYPE_Filter = "A_ARG_TYPE_Filter";
	static final String SV_NAME_CDS_A_ARG_TYPE_SortCriteria = "A_ARG_TYPE_SortCriteria";
	static final String SV_NAME_CDS_A_ARG_TYPE_Index = "A_ARG_TYPE_Index";
	static final String SV_NAME_CDS_A_ARG_TYPE_Count = "A_ARG_TYPE_Count";
	static final String SV_NAME_CDS_A_ARG_TYPE_UpdateID = "A_ARG_TYPE_UpdateID";
	static final String SV_NAME_CDS_A_ARG_Type_TransferID = "A_ARG_Type_TransferID";
	static final String SV_NAME_CDS_A_ARG_Type_TransferStatus = "A_ARG_Type_TransferStatus";
	static final String SV_NAME_CDS_A_ARG_Type_TransferLength = "A_ARG_Type_TransferLength";
	static final String SV_NAME_CDS_A_ARG_Type_TransferTotal = "A_ARG_Type_TransferTotal";
	static final String SV_NAME_CDS_A_ARG_TYPE_TagValueList = "A_ARG_TYPE_TagValueList";
	static final String SV_NAME_CDS_A_ARG_TYPE_URI = "A_ARG_TYPE_URI";
	static final String SV_NAME_CDS_SearchCapabilities = "SearchCapabilities";
	static final String SV_NAME_CDS_SortCapabilities = "SortCapabilities";
	static final String SV_NAME_CDS_SystemUpdateID = "SystemUpdateID";
	static final String SV_NAME_CDS_ContainerUpdateIDs = "ContainerUpdateIDs";
	
	static final String ACTION_ARG_NAME_SearchCaps = "SearchCaps";
	static final String ACTION_ARG_NAME_SortCaps = "SortCaps";
	static final String ACTION_ARG_NAME_Id = "Id";
	static final String ACTION_ARG_NAME_ObjectID = "ObjectID";
	static final String ACTION_ARG_NAME_BrowseFlag = "BrowseFlag";
	static final String ACTION_ARG_NAME_Filter = "Filter";
	static final String ACTION_ARG_NAME_StartingIndex = "StartingIndex";
	static final String ACTION_ARG_NAME_RequestedCount = "RequestedCount";
	static final String ACTION_ARG_NAME_SortCriteria = "SortCriteria";
	static final String ACTION_ARG_NAME_Result = "Result";
	static final String ACTION_ARG_NAME_NumberReturned = "NumberReturned";
	static final String ACTION_ARG_NAME_TotalMatches = "TotalMatches";
	static final String ACTION_ARG_NAME_UpdateID = "UpdateID";
	static final String ACTION_ARG_NAME_ContainerID = "ContainerID";
	static final String ACTION_ARG_NAME_SearchCriteria = "SearchCriteria";
	static final String ACTION_ARG_NAME_Elements = "Elements";
	static final String ACTION_ARG_NAME_CurrentTagValue = "CurrentTagValue";
	static final String ACTION_ARG_NAME_NewTagValue = "NewTagValue";
	static final String ACTION_ARG_NAME_SourceURI = "SourceURI";
	static final String ACTION_ARG_NAME_DestinationURI = "DestinationURI";
	static final String ACTION_ARG_NAME_TransferID = "TransferID";
	static final String ACTION_ARG_NAME_TransferStatus = "TransferStatus";
	static final String ACTION_ARG_NAME_TransferLength = "TransferLength";
	static final String ACTION_ARG_NAME_TransferTotal = "TransferTotal";
	static final String ACTION_ARG_NAME_NewID = "NewID";
	
	static final String ACTION_NAME_CDS_GetSearchCapabilities = "GetSearchCapabilities";
	static final String ACTION_NAME_CDS_GetSortCapabilities = "GetSortCapabilities";
	static final String ACTION_NAME_CDS_GetSystemUpdateID = "GetSystemUpdateID";
	static final String ACTION_NAME_CDS_Browse = "Browse";
	static final String ACTION_NAME_CDS_Search = "Search";
	static final String ACTION_NAME_CDS_CreateObject = "CreateObject";
	static final String ACTION_NAME_CDS_DestroyObject = "DestroyObject";
	static final String ACTION_NAME_CDS_UpdateObject = "UpdateObject";
	static final String ACTION_NAME_CDS_ImportResource = "ImportResource";
	static final String ACTION_NAME_CDS_ExportResource = "ExportResource";
	static final String ACTION_NAME_CDS_StopTransferResource = "StopTransferResource";
	static final String ACTION_NAME_CDS_GetTransferProgress = "GetTransferProgress";

	private ArrayList<Integer> getStateVariableTransferIDs() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_TRANSFERIDS);
		ArrayList<Integer> returnValue = value == null ? new ArrayList<Integer>() : UPnPStateVariable.getCSVStateVariableIntegers((String)value.getValue());
		return returnValue;
	}
	
	private ArrayList<String> getStateVariableSearchCapabilities() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_SearchCapabilities);
		ArrayList<String> returnValue = value == null ? new ArrayList<String>() : UPnPStateVariable.getCSVStateVariableStrings((String)value.getValue());
		return returnValue;
	}
	
	private ArrayList<String> getStateVariableSortCapabilities() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_SortCapabilities);
		ArrayList<String> returnValue = value == null ? new ArrayList<String>() : UPnPStateVariable.getCSVStateVariableStrings((String)value.getValue());
		return returnValue;
	}
	
	private String getStateVariableSystemUpdateID() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_SystemUpdateID);
		return (String)value.getValue();
	}
	
	public String getStateVariableContainerUpdateIDs() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_ContainerUpdateIDs);
		return (String)value.getValue();
	}
	
	public ArrayList<String> getSearchCapabilities() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_GetSearchCapabilities);
		ActionExecutor executor = new ActionExecutor(targetAction);
		executor.execute();
		return getStateVariableSearchCapabilities();
	}
	
	public ArrayList<String> getSortCapabilities() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_GetSortCapabilities);
		ActionExecutor executor = new ActionExecutor(targetAction);
		executor.execute();
		return getStateVariableSortCapabilities();
	}
	
	public String getSystemUpdateID() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_GetSystemUpdateID);
		ActionExecutor executor = new ActionExecutor(targetAction);
		executor.execute();
		return getStateVariableSystemUpdateID();
	}
	
	public ContentDirectoryItem browse(String objectID, String browseFlag, String filter, int startingIndex, int requestCount, String sortCriteria) throws Exception {
		ContentDirectoryItem resultRoot = new ContentDirectoryItem();
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_Browse);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_ObjectID).setValue(objectID);
			targetAction.getInArgument(ACTION_ARG_NAME_BrowseFlag).setValue(browseFlag);
			targetAction.getInArgument(ACTION_ARG_NAME_Filter).setValue(filter);
			targetAction.getInArgument(ACTION_ARG_NAME_StartingIndex).setValue(startingIndex+"");
			targetAction.getInArgument(ACTION_ARG_NAME_RequestedCount).setValue(requestCount+"");
			targetAction.getInArgument(ACTION_ARG_NAME_SortCriteria).setValue(sortCriteria);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			int numberOfReturned = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_NumberReturned).getValue());
			String result = targetAction.getOutArgument(ACTION_ARG_NAME_Result).getValue();
			int totalMatches = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_TotalMatches).getValue());
			String updateID = targetAction.getOutArgument(ACTION_ARG_NAME_UpdateID).getValue();
			// 3. parse result XML into ContentDirectoryItems.
			resultRoot = parseResponseXML(result);
		} else {
			System.out.println("There is no browse action in this device.");
		}
		return resultRoot;
	}
	
	static private ContentDirectoryItem parseResponseXML(String responseXML) {
		ContentDirectoryItem resultRoot = new ContentDirectoryItem();
		return resultRoot;
	}
	
	/*
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

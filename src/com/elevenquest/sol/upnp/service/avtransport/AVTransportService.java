package com.elevenquest.sol.upnp.service.avtransport;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.elevenquest.sol.upnp.action.ActionExecutor;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.service.connection.ConnectionItem;

public class AVTransportService extends UPnPService {

	public AVTransportService(UPnPDevice device) {
		super(device);
		// TODO Auto-generated constructor stub
	}

	// definition of state variables.
	static final String SV_NAME_ATS_TransportStateo = "TransportState";		// string, "STOPPED", "PLAYING", "TRANSITIONING", "PAUSED_PLAYBACK", "PAUSED_RECORDING", "RECORDING", "NO_MEDIA_PRESENT"
	static final String SV_NAME_ATS_TransportStatus = "TransportStatus";	// string, "OK", "ERROR_OCCURRED"
	static final String SV_NAME_ATS_PlaybackStorageMedium = "PlaybackStorageMedium";	// string, "UNKNOW", "DV", "MINI-DV", "VHS", "W-VHS", "S-VHS", "D-VHS", .... "HDD", "MICRO-MV", "NETWORK", "NONE", "NOT_IMPLEMENTED", Vendor-defined.
	static final String SV_NAME_ATS_RecordStorageMedium = "RecordStorageMedium";	// string
	static final String SV_NAME_ATS_PossiblePlaybackStorageMedia = "PossiblePlaybackStorageMedia";	// string (CSV)
	static final String SV_NAME_ATS_PossibleRecordStorageMedia = "PossibleRecordStorageMedia";	// string (CSV)
	static final String SV_NAME_ATS_CurrentPlayMode = "CurrentPlayMode";	// string, "NORMAL", "SHUFFLE", "REPEAT_ONE", "REPEAT_ALL", "RANDOM", "DIRECT_1", "INTRO", Vendor-defined.
	static final String SV_NAME_ATS_TransportPlaySpeed = "TransportPlaySpeed";	// string "1"
	static final String SV_NAME_ATS_RecordMediumWriteStatus = "RecordMediumWriteStatus";	// string, "WRITABLE", "PROTECTED", "NOT_WRITABLE", "UNKNOWN", "NOT_IMPLEMENTED"
	static final String SV_NAME_ATS_CurrentRecordQualityModem = "CurrentRecordQualityMode";	// string, "0:EP", "1:LP", "2:SP", "0:BASIC", "1:MEDIUM", "2:HGH", "NOT IMPLEMENTED", Vendor-defined.
	static final String SV_NAME_ATS_PossibleRecordQualityModes = "PossibleRecordQualityModes";	// string (CSV)
	static final String SV_NAME_ATS_NumberOfTracks = "NumberOfTracks";	// ui4 minimum:0 maximum:vendor-defined.
	static final String SV_NAME_ATS_CurrentTrack = "CurrentTrack";	// ui4 minimum:0 maximum:vendor-defined step:1
	static final String SV_NAME_ATS_CurrentTrackDuration = "CurrentTrackDuration";	// string
	static final String SV_NAME_ATS_CurrentMediaDuration = "CurrentMediaDuration";	// string
	static final String SV_NAME_ATS_CurrentTrackMetaData = "CurrentTrackMetaData";	// string
	static final String SV_NAME_ATS_CurrentTrackURI = "CurrentTrackURI";	// string
	static final String SV_NAME_ATS_AVTransportURI = "AVTransportURI";	// string
	static final String SV_NAME_ATS_AVTransportURIMetaData = "AVTransportURIMetaData";	// string
	static final String SV_NAME_ATS_NextAVTransportURI = "NextAVTransportURI";	// string
	static final String SV_NAME_ATS_NextAVTransportURIMetaData = "NextAVTransportURIMetaData";	// string
	static final String SV_NAME_ATS_RelativeTimePosition = "RelativeTimePosition";	// string
	static final String SV_NAME_ATS_AbsoluteTimePosition = "AbsoluteTimePosition";	// string
	static final String SV_NAME_ATS_RelativeCounterPosition = "RelativeCounterPosition";	// i4
	static final String SV_NAME_ATS_AbsoluteCounterPosition = "AbsoluteCounterPosition";	// i4
	static final String SV_NAME_ATS_CurrentTransportActions = "CurrentTransportActions";	// string (CSV)
	static final String SV_NAME_ATS_LastChange = "LastChange";	// string
	
	static final String SV_NAME_ATS_A_ARG_TYPE_SeekMode = "A_ARG_TYPE_SeekMode";	// "TRACK_NR", "ABS_TIME", "REL_TIME", "ABS_COUNT", "REL_COUNT", "CHANNEL_FREQ", "TAPE-INDEX", "FRAME"
	static final String SV_NAME_ATS_A_ARG_TYPE_SeekTarget = "A_ARG_TYPE_SeekTarget";	// string
	static final String SV_NAME_ATS_A_ARG_TYPE_InstanceID = "A_ARG_TYPE_InstanceID";	// ui4

	// List of parameters used in each UPnPAction.
	static final String ACTION_ARG_NAME_InstanceID = "Source";						// A_ARG_TYPE_InstanceID
	static final String ACTION_ARG_NAME_CurrentURI = "CurrentURI";					// AVTransportURI
	static final String ACTION_ARG_NAME_CurrentURIMetaData = "CurrentURIMetaData";	// AVTransportURIMetaData
	static final String ACTION_ARG_NAME_NextURI = "NextURI";	// NextAVTransportURI
	static final String ACTION_ARG_NAME_NextURIMetaData = "NextURIMetaData";	// NextAVTransportURIMetaData
	static final String ACTION_ARG_NAME_NrTracks = "NrTracks";	// NumberOfTracks
	static final String ACTION_ARG_NAME_MediaDuration = "MediaDuration";	// CurrentMediaDuration
	static final String ACTION_ARG_NAME_PlayMedium = "PlayMedium";	// PlaybackStorageMedium
	static final String ACTION_ARG_NAME_RecordMedium = "RecordMedium";	// RecordStorageMedium
	static final String ACTION_ARG_NAME_WriteStatus = "WriteStatus";	// RecordMediumWriteStatus
	static final String ACTION_ARG_NAME_CurrentTransportState = "CurrentTransportState";	// TransportState
	static final String ACTION_ARG_NAME_CurrentTransportStatus = "CurrentTransportStatus";	// TransportStatus
	static final String ACTION_ARG_NAME_CurrentSpeed = "CurrentSpeed";	// TransportPlaySpeed
	static final String ACTION_ARG_NAME_Track = "Track";	// CurrentTrack
	static final String ACTION_ARG_NAME_TrackDuration = "TrackDuration";	// CurrentTrackDuration
	static final String ACTION_ARG_NAME_TrackMetaData = "TrackMetaData";	// CurrentTrackMetaData
	static final String ACTION_ARG_NAME_TrackURI = "TrackURI";	// CurrentTrackURI
	static final String ACTION_ARG_NAME_RelTime = "RelTime";	// RelativeTimePosition
	static final String ACTION_ARG_NAME_AbsTime = "AbsTime";	// AbsoluteTimePosition
	static final String ACTION_ARG_NAME_RelCount = "RelCount";	// RelativeCounterPosition
	static final String ACTION_ARG_NAME_AbsCount = "AbsCount";	// AbsoluteCounterPosition
	static final String ACTION_ARG_NAME_PlayMedia = "PlayMedia";	// PossiblePlaybackStorageMedia
	static final String ACTION_ARG_NAME_RecMedia = "RecMedia";	// PossibleRecordStorageMedia
	static final String ACTION_ARG_NAME_RecQualityModes = "RecQualityModes";	// PossibleRecordQualityModes
	static final String ACTION_ARG_NAME_PlayMode = "PlayMode";	// CurrentPlayMode
	static final String ACTION_ARG_NAME_RecQualityMode = "RecQualityMode";	// CurrentRecordQualityMode
	static final String ACTION_ARG_NAME_Speed = "Speed";	// TransportPlaySpeed
	static final String ACTION_ARG_NAME_Unit = "Unit";	// A_ARG_TYPE_SeekMode
	static final String ACTION_ARG_NAME_Target = "Target";	// A_ARG_TYPE_SeekTarget
	static final String ACTION_ARG_NAME_NewPlayMode = "NewPlayMode";	// CurrentPlayMode
	static final String ACTION_ARG_NAME_NewRecordQualityMode = "NewRecordQualityMode";	// CurrentRecordQualityMode
	static final String ACTION_ARG_NAME_Actions = "Actions";	// CurrentTransportActions
	
	// List of Action Name.
	static final String ACTION_NAME_ATS_SetAVTransportURI = "SetAVTransportURI";
	static final String ACTION_NAME_ATS_SetNextAVTransportURI = "SetNextAVTransportURI";
	static final String ACTION_NAME_ATS_GetMediaInfo = "GetMediaInfo";
	static final String ACTION_NAME_ATS_GetTransportInfo = "GetTransportInfo";
	static final String ACTION_NAME_ATS_GetPositionInfo = "GetPositionInfo";
	static final String ACTION_NAME_ATS_GetDeviceCapabilities = "GetDeviceCapabilities";
	static final String ACTION_NAME_ATS_GetTransportSettings = "GetTransportSettings";
	static final String ACTION_NAME_ATS_Stop = "Stop";
	static final String ACTION_NAME_ATS_Play = "Play";
	static final String ACTION_NAME_ATS_Pause = "Pause";
	static final String ACTION_NAME_ATS_Record = "Record";
	static final String ACTION_NAME_ATS_Seek = "Seek";
	static final String ACTION_NAME_ATS_Next = "Next";
	static final String ACTION_NAME_ATS_Previous = "Previous";
	static final String ACTION_NAME_ATS_SetPlayMode = "SetPlayMode";
	static final String ACTION_NAME_ATS_SetRecordQualityMode = "SetRecordQualityMode";
	static final String ACTION_NAME_ATS_GetCurrentTransportActions = "GetCurrentTransportActions";

	public void SetAVTransportURI(int instanceID, String currentURI, String currentURIMetaData) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_SetAVTransportURI);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_CurrentURI).setValue(currentURI);
			targetAction.getInArgument(ACTION_ARG_NAME_CurrentURIMetaData).setValue(currentURIMetaData);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
		} else {
			Logger.println(Logger.ERROR, "There is no SetAVTransportURI action in this device.");
		}
	}

	public void SetNextAVTransportURI(int instanceID, String nextURI, String nextURIMetaData) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_SetNextAVTransportURI);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_NextURI).setValue(nextURI);
			targetAction.getInArgument(ACTION_ARG_NAME_NextURIMetaData).setValue(nextURIMetaData);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
		} else {
			Logger.println(Logger.ERROR, "There is no SetNextAVTransportURI action in this device.");
		}
	}

	public MediaInfoItem GetMediaInfo(int instanceID) throws Exception {
		MediaInfoItem item = new MediaInfoItem();
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_GetMediaInfo);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
			int nrTracks = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_NrTracks).getValue());
			String mediaDuration = targetAction.getOutArgument(ACTION_ARG_NAME_MediaDuration).getValue();
			String currentURI = targetAction.getOutArgument(ACTION_ARG_NAME_CurrentURI).getValue();
			String currentURIMetaData = targetAction.getOutArgument(ACTION_ARG_NAME_CurrentURIMetaData).getValue();
			String nextURI = targetAction.getOutArgument(ACTION_ARG_NAME_NextURI).getValue();
			String nextURIMetaData = targetAction.getOutArgument(ACTION_ARG_NAME_NextURIMetaData).getValue();
			String playMedium = targetAction.getOutArgument(ACTION_ARG_NAME_PlayMedium).getValue();
			String recordMedium = targetAction.getOutArgument(ACTION_ARG_NAME_RecordMedium).getValue();
			String writeStatus = targetAction.getOutArgument(ACTION_ARG_NAME_WriteStatus).getValue();
			
			item.setNrTracks(nrTracks);
			item.setMediaDuration(mediaDuration);
			item.setCurrentURI(currentURI);
			item.setCurrentURIMetaData(currentURIMetaData);
			item.setNextURI(nextURI);
			item.setNextURIMetaData(nextURIMetaData);
			item.setPlayMedium(playMedium);
			item.setRecordMedium(recordMedium);
			item.setWriteStatus(writeStatus);
			
		} else {
			Logger.println(Logger.ERROR, "There is no GetMediaInfo action in this device.");
		}
		return item;
	}

	public TransportInfoItem GetTransportInfo(int instanceID) throws Exception {
		TransportInfoItem item = new TransportInfoItem();
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_GetTransportInfo);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
			String currentTransportState = targetAction.getOutArgument(ACTION_ARG_NAME_CurrentTransportState).getValue();
			String currentTransportStatus = targetAction.getOutArgument(ACTION_ARG_NAME_CurrentTransportStatus).getValue();
			String currentSpeed = targetAction.getOutArgument(ACTION_ARG_NAME_CurrentSpeed).getValue();
			
			item.setCurrentTransportState(currentTransportState);
			item.setCurrentTransportStatus(currentTransportStatus);
			item.setCurrentSpeed(currentSpeed);
			
		} else {
			Logger.println(Logger.ERROR, "There is no GetTransportInfo action in this device.");
		}
		return item;
	}

	public PositionInfo GetPositionInfo(int instanceID) throws Exception {
		PositionInfo item = new PositionInfo();
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_GetPositionInfo);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
			int track = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_Track).getValue());
			String trackDuration = targetAction.getOutArgument(ACTION_ARG_NAME_TrackDuration).getValue();
			String trackMetaData = targetAction.getOutArgument(ACTION_ARG_NAME_TrackMetaData).getValue();
			String trackURI = targetAction.getOutArgument(ACTION_ARG_NAME_TrackURI).getValue();
			String relTime = targetAction.getOutArgument(ACTION_ARG_NAME_RelTime).getValue();
			String absTime = targetAction.getOutArgument(ACTION_ARG_NAME_AbsTime).getValue();
			int relCount = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_RelCount).getValue());
			int absCount = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_AbsCount).getValue());
			
			item.setTrack(track);
			item.setTrackDuration(trackDuration);
			item.setTrackMetaData(trackMetaData);
			item.setTrackURI(trackURI);
			item.setRelTime(relTime);
			item.setAbsTime(absTime);
			item.setRelCount(relCount);
			item.setAbsCount(absCount);
			
		} else {
			Logger.println(Logger.ERROR, "There is no GetPositionInfo action in this device.");
		}
		return item;
	}

	public DeviceCapability GetDeviceCapabilities(int instanceID) throws Exception {
		DeviceCapability item = new DeviceCapability();
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_GetDeviceCapabilities);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
			String playMedia = targetAction.getOutArgument(ACTION_ARG_NAME_PlayMedia).getValue();
			String recMedia = targetAction.getOutArgument(ACTION_ARG_NAME_RecMedia).getValue();
			String recQualityModes = targetAction.getOutArgument(ACTION_ARG_NAME_RecQualityModes).getValue();
			
			item.setPlayMedia(playMedia);
			item.setRecMedia(recMedia);
			item.setRecQualityModes(recQualityModes);
			
		} else {
			Logger.println(Logger.ERROR, "There is no GetPositionInfo action in this device.");
		}
		return item;
	}
	
	public TransportSetting GetTransportSettings(int instanceID) throws Exception {
		TransportSetting item = new TransportSetting();
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_GetTransportSettings);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
			String playMode = targetAction.getOutArgument(ACTION_ARG_NAME_PlayMode).getValue();
			String recQualityMode = targetAction.getOutArgument(ACTION_ARG_NAME_RecQualityMode).getValue();
			
			item.setPlayMode(playMode);
			item.setRecQualityMode(recQualityMode);
			
		} else {
			Logger.println(Logger.ERROR, "There is no GetTransportSettings action in this device.");
		}
		return item;
	}

	public void Stop(int instanceID) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Stop);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is no Stop action in this device.");
		}
	}

	public void Play(int instanceID, String speed) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Play);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_Speed).setValue(speed);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is no Play action in this device.");
		}
	}

	public void Pause(int instanceID) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Pause);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is no Pause action in this device.");
		}
	}
	
	public void Record(int instanceID) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Record);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is no Record action in this device.");
		}
	}
	
	public void Seek(int instanceID, String unit, String target) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Seek);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_Unit).setValue(unit);
			targetAction.getInArgument(ACTION_ARG_NAME_Target).setValue(target);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is no Seek action in this device.");
		}
	}
	
	public void Next(int instanceID) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Next);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is no Next action in this device.");
		}
	}

	public void Previous(int instanceID) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_Previous);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is Previous action in this device.");
		}
	}

	public void SetPlayMode(int instanceID, String newPlayMode) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_SetPlayMode);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_NewPlayMode).setValue(newPlayMode);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is SetPlayMode action in this device.");
		}
	}

	public void SetRecordQualityMode(int instanceID, String newRecordQualityMode) throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_SetRecordQualityMode);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			targetAction.getInArgument(ACTION_ARG_NAME_NewRecordQualityMode).setValue(newRecordQualityMode);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			
		} else {
			Logger.println(Logger.ERROR, "There is SetPlayMode action in this device.");
		}
	}

	public ArrayList<String> GetCurrentTransportActions(int instanceID) throws Exception {
		ArrayList<String> actionList = new ArrayList<String>();
		UPnPAction targetAction = this.getAction(ACTION_NAME_ATS_GetCurrentTransportActions);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_InstanceID).setValue(instanceID+"");
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();

			String actions = targetAction.getOutArgument(ACTION_ARG_NAME_Actions).getValue();
			StringTokenizer st = new StringTokenizer(actions);
			String action = null;
			while( (action = st.nextToken()) != null )
				actionList.add(action);
		} else {
			Logger.println(Logger.ERROR, "There is SetPlayMode action in this device.");
		}
		return actionList;
	}
}

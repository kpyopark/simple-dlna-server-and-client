package com.elevenquest.sol.upnp.service.avtransport;

import com.elevenquest.sol.upnp.model.UPnPBase;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;

public class MediaInfoItem extends UPnPBase {
	
	int nrTracks;
	String mediaDuration;
	String currentURI;
	String currentURIMetaData;	// TYPE OF DIDL XML
	String nextURI;
	String nextURIMetaData;
	String playMedium;
	String recordMedium;
	String writeStatus;
	
	public int getNrTracks() {
		return nrTracks;
	}
	public void setNrTracks(int nrTracks) {
		this.nrTracks = nrTracks;
	}
	public String getMediaDuration() {
		return mediaDuration;
	}
	public void setMediaDuration(String mediaDuration) {
		this.mediaDuration = mediaDuration;
	}
	public String getCurrentURI() {
		return currentURI;
	}
	public void setCurrentURI(String currentURI) {
		this.currentURI = currentURI;
	}
	public String getCurrentURIMetaData() {
		return currentURIMetaData;
	}
	public void setCurrentURIMetaData(String currentURIMetaData) {
		this.currentURIMetaData = currentURIMetaData;
	}
	public String getNextURI() {
		return nextURI;
	}
	public void setNextURI(String nextURI) {
		this.nextURI = nextURI;
	}
	public String getNextURIMetaData() {
		return nextURIMetaData;
	}
	public void setNextURIMetaData(String nextURIMetaData) {
		this.nextURIMetaData = nextURIMetaData;
	}
	public String getPlayMedium() {
		return playMedium;
	}
	public void setPlayMedium(String playMedium) {
		this.playMedium = playMedium;
	}
	public String getRecordMedium() {
		return recordMedium;
	}
	public void setRecordMedium(String recordMedium) {
		this.recordMedium = recordMedium;
	}
	public String getWriteStatus() {
		return writeStatus;
	}
	public void setWriteStatus(String writeStatus) {
		this.writeStatus = writeStatus;
	}
}

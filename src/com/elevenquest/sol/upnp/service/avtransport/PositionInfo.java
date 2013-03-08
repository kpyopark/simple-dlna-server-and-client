package com.elevenquest.sol.upnp.service.avtransport;

import com.elevenquest.sol.upnp.model.UPnPBase;

public class PositionInfo extends UPnPBase {
	int track;
	String trackDuration;
	String trackMetaData;
	String trackURI;
	String relTime;
	String absTime;
	int relCount;
	int absCount;
	public int getTrack() {
		return track;
	}
	public void setTrack(int track) {
		this.track = track;
	}
	public String getTrackDuration() {
		return trackDuration;
	}
	public void setTrackDuration(String trackDuration) {
		this.trackDuration = trackDuration;
	}
	public String getTrackMetaData() {
		return trackMetaData;
	}
	public void setTrackMetaData(String trackMetaData) {
		this.trackMetaData = trackMetaData;
	}
	public String getTrackURI() {
		return trackURI;
	}
	public void setTrackURI(String trackURI) {
		this.trackURI = trackURI;
	}
	public String getRelTime() {
		return relTime;
	}
	public void setRelTime(String relTime) {
		this.relTime = relTime;
	}
	public String getAbsTime() {
		return absTime;
	}
	public void setAbsTime(String absTime) {
		this.absTime = absTime;
	}
	public int getRelCount() {
		return relCount;
	}
	public void setRelCount(int relCount) {
		this.relCount = relCount;
	}
	public int getAbsCount() {
		return absCount;
	}
	public void setAbsCount(int absCount) {
		this.absCount = absCount;
	}
	
}

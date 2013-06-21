package com.elevenquest.sol.upnp.model;

public class UPnPDeviceImage extends UPnPBase {

	UPnPDevice device = null;

	String mimeType;
	int width;
	int height;
	int depth;
	String color;
	String url;
	
	public UPnPDeviceImage() {
		
	}
	
	public void setDevice(UPnPDevice device) {
		this.device = device;
	}
	
	public UPnPDevice getDevice() {
		return this.device;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

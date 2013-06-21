package com.elevenquest.sol.upnp.description;

import com.elevenquest.sol.upnp.model.UPnPDeviceImage;

public class ImageElementInDDS implements ICommonDescription {

	static final String IMG_REPLACEABLE_PART_IMAGE_MIME_TYPE = "#MIME_TYPE#";
	static final String IMG_REPLACEABLE_PART_IMAGE_WIDTH = "#IMAGE_WIDTH#";
	static final String IMG_REPLACEABLE_PART_IMAGE_HEGITH = "#IMAGE_HEGITH#";
	static final String IMG_REPLACEABLE_PART_IMAGE_COLOR = "#IMAGE_COLOR#";
	static final String IMG_REPLACEABLE_PART_IMAGE_URL = "#IMAGE_URL#";

	static final String DEVICE_DESCRIPTION_SERVICE_TEMPLATE =
		"<icon>" +
		"<mimetype>" + IMG_REPLACEABLE_PART_IMAGE_MIME_TYPE + "</mimetype>" +
		"<width>" + IMG_REPLACEABLE_PART_IMAGE_WIDTH + "</width>" +
		"<height>" + IMG_REPLACEABLE_PART_IMAGE_HEGITH + "</height>" +
		"<depth>" + IMG_REPLACEABLE_PART_IMAGE_COLOR + "</depth>" +
		"<url>" + IMG_REPLACEABLE_PART_IMAGE_URL + "</url>" +
		"</icon>";
	
	String mimeType;
	int width;
	int height;
	int depth;
	String color;
	String url;
	
	public ImageElementInDDS() {
		
	}
	
	public ImageElementInDDS(String mimeType, int width, int height, String colorDepth, String url) {
		this.mimeType = mimeType;
		this.width = width;
		this.height = height;
		this.color = colorDepth;
		this.url = url;
	}
	
	public String getDescription() {
		// TODO: Replace all replaceable parts.
		return DEVICE_DESCRIPTION_SERVICE_TEMPLATE;
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
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public UPnPDeviceImage getDeviceImageStructure() {
		UPnPDeviceImage image = new UPnPDeviceImage();
		image.setColor(this.color);
		image.setDepth(this.depth);
		image.setHeight(this.height);
		image.setMimeType(this.mimeType);
		image.setUrl(this.url);
		image.setWidth(this.width);
		return image;
	}

}

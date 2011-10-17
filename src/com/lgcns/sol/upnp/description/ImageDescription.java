package com.lgcns.sol.upnp.description;

public class ImageDescription implements ICommonDescription {

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
	String color;
	String url;
	
	public ImageDescription(String mimeType, int width, int height, String colorDepth, String url) {
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
}

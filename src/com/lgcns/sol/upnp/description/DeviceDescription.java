package com.lgcns.sol.upnp.description;

import java.util.ArrayList;

import com.lgcns.sol.upnp.model.UPnPDevice;

public class DeviceDescription {
	static final String DEVICE_DESCRIPTION_CONFIG_NUMBER = "0";
	static final String DEVICE_DESCRIPTION_SPECVER_MAJOR = "1";
	static final String DEVICE_DESCRIPTION_SPECVER_MINOR = "1";
	
	static final String DEVICE_DESCRIPTION_FRIENDLY_NAME = "Default Device Description.";
	static final String DEVICE_DESCRIPTION_MANUFACTURER_NAME = "LG";
	static final String DEVICE_DESCRIPTION_MANUFACTURER_URL = "http://www.lg.com";
	
	static final String DEVICE_DESCRIPTION_MODEL = "Test Device";
	
	static final String DEVICE_DESCRIPTION_MODEL_NAME = "LGTSTDEVICE";
	static final String DEVICE_DESCRIPTION_MODEL_NUMBER = "LGDEVICE000";
	static final String DEVICE_DESCRIPTION_MODEL_URL = "http://www.lg.com";
	
	
	static final String DDS_REPLACEABLE_PART_MODEL_SERIAL = "#MODEL_SERIAL#";
	static final String DDS_REPLACEABLE_PART_UUID = "#UUID#";
	static final String DDS_REPLACEABLE_PART_UPC = "#UPC#";
	
	static final String DDS_REPLACEABLE_SERVICE_LIST = "#SERVICE_LIST#";
	static final String DDS_REPLACEABLE_IMAGE_LIST = "#IMAGE_LIST#";
	
	// THIS PART IS NOT USED FOR MAKING DESCRIPTIONS. 
	// JUST FOR STORING THE PARSED DESCRIPTION DATA.
	String configNumber;
	String specMajor;
	String specMinor;
	String friendlyName;
	String manufacturerName;
	String manufacturerUrl;
	String model;
	String modelName;
	String modelNumber;
	String modelUrl;
	
	// BELOW PART IS VARIALBES. SO YOU CAN SET THE VALUES INTO THAT BY USING CLASS CONSTRUCTORS.
	UPnPDevice device;
	
	private boolean imageListUpdated = false;
	private boolean serviceListUpdated = false;
	
	private String deviceDescription = null;
	private String imageListDescription = null;
	private String serviceListDescription = null;
	
	private ArrayList<ImageDescription> imageList = new ArrayList<ImageDescription>();
	private ArrayList<ServiceDescription> serviceList = new ArrayList<ServiceDescription>();
	
	static final String DDS_REPLACEABLE_PART_SERVICE_PART = "#SERVICE_PART#";
	static final String DEVICE_DESCRIPTION_PRESENETATION_URL = "http://www.lg.com";
	
	static final String DEVICE_DESCRIPTION_TEMPLATE = 
		"<?xml version=\"1.0\"?>" +
		"<root xmlns=\"urn:schemas-upnp-org:device-1-0\" configId=\"" + DEVICE_DESCRIPTION_CONFIG_NUMBER + "\">" +
		"<specVersion>" +
		"<major>" + DEVICE_DESCRIPTION_SPECVER_MAJOR + "</major>" +
		"<minor>" + DEVICE_DESCRIPTION_SPECVER_MINOR + "</minor>" +
		"</specVersion>" +
		"<device>" +
		"<deviceType>urn:schemas-upnp-org:device:deviceType:v</deviceType>" +
		"<friendlyName>" + DEVICE_DESCRIPTION_FRIENDLY_NAME + "</friendlyName>" +
		"<manufacturer>" + DEVICE_DESCRIPTION_MANUFACTURER_NAME + "</manufacturer>" +
		"<manufacturerURL>" + DEVICE_DESCRIPTION_MANUFACTURER_URL + "</manufacturerURL>" +
		"<modelDescription>" + DEVICE_DESCRIPTION_MODEL + "</modelDescription>" +
		"<modelName>" + DEVICE_DESCRIPTION_MODEL_NAME + "</modelName>" +
		"<modelNumber>" + DEVICE_DESCRIPTION_MODEL_NUMBER + "</modelNumber>" +
		"<modelURL>" + DEVICE_DESCRIPTION_MODEL_URL + "</modelURL>" +
		"<serialNumber>" + DDS_REPLACEABLE_PART_MODEL_SERIAL + "</serialNumber>" +
		"<UDN>uuid:" + DDS_REPLACEABLE_PART_UUID + "</UDN>" +
		"<UPC>" + DDS_REPLACEABLE_PART_UPC + "</UPC>" +
		"<iconList>" +
		DDS_REPLACEABLE_IMAGE_LIST +
		// below part must come from the registered device images.
		/*
		"<icon>" +
		"<mimetype>" + IMG_REPLACEABLE_PART_IMAGE_MIME_TYPE + "</mimetype>" +
		"<width>" + IMG_REPLACEABLE_PART_IMAGE_WIDTH + "</width>" +
		"<height>" + IMG_REPLACEABLE_PART_IMAGE_HEGITH + "</height>" +
		"<depth>" + IMG_REPLACEABLE_PART_IMAGE_COLOR + "</depth>" +
		"<url>" + IMG_REPLACEABLE_PART_IMAGE_URL + "</url>";
		"</icon>" +
		*/
		"<!-- XML to declare other icons, if any, go here -->" +
		"</iconList>" +
		"<serviceList>" +
		DDS_REPLACEABLE_SERVICE_LIST +
		// below part must come from the registered services.
		/* 
		"<service>" +
		"<serviceType>urn:schemas-upnp-org:service:serviceType:v</serviceType>" +
		"<serviceId>urn:upnp-org:serviceId:REPLACE_SERVICEID</serviceId>" +
		"<SCPDURL>URL to service description</SCPDURL>" +
		"<controlURL>URL for control</controlURL>" +
		"<eventSubURL>URL for eventing</eventSubURL>" +
		"</service>" +
		*/
		"</serviceList>" +
		"<deviceList>" +
		"</deviceList>" +
		"<presentationURL>" + DEVICE_DESCRIPTION_PRESENETATION_URL + "</presentationURL>" +
		"</device>" +
		"</root>";
	
	public DeviceDescription() {
		
	}
	
	public DeviceDescription(
			String configNumber,
			String specMajor,
			String specMinor,
			String friendlyName,
			String manufacturerName,
			String manufacturerUrl,
			String model,
			String modelName,
			String modelNumber,
			String modelUrl,
			String modelSerial,
			String uuid,
			String upc) {
		 this.configNumber = configNumber;
		 this.specMajor = specMajor;
		 this.specMinor = specMinor;
		 this.friendlyName = friendlyName;
		 this.manufacturerName = manufacturerName;
		 this.manufacturerUrl = manufacturerUrl;
		 this.model = model;
		 this.modelName = modelName;
		 this.modelNumber = modelNumber;
		 this.modelUrl = modelUrl;
		 device = new UPnPDevice();
		 this.device.setModelSerial(modelSerial);
		 this.device.setUuid(uuid);
		 this.device.setUpc(upc);	
	}

	public String getDeviceDescription() {
		synchronized(deviceDescription) {
			if ( deviceDescription == null || imageListUpdated || serviceListUpdated ) {
				if ( imageListUpdated ) {
					StringBuffer buffer = new StringBuffer();
					for ( ImageDescription imageDesc : this.imageList ) {
						buffer.append( imageDesc.getDescription() );
					}
					this.imageListDescription = buffer.toString();
					this.imageListUpdated = false; 
				}
				if ( serviceListUpdated ) {
					StringBuffer buffer = new StringBuffer();
					for ( ServiceDescription serviceDesc : this.serviceList ) {
						buffer.append( serviceDesc.getDescription() );
					}
					this.serviceListDescription = buffer.toString();
					this.serviceListUpdated = false;
				}
				deviceDescription = DEVICE_DESCRIPTION_TEMPLATE.replaceAll(DDS_REPLACEABLE_PART_MODEL_SERIAL, this.device.getModelSerial())
				.replaceAll(DDS_REPLACEABLE_PART_UUID, this.device.getUuid())
				.replaceAll(DDS_REPLACEABLE_PART_UPC, this.device.getUpc())
				.replaceAll(DDS_REPLACEABLE_SERVICE_LIST, this.serviceListDescription )
				.replaceAll(DDS_REPLACEABLE_IMAGE_LIST, this.imageListDescription );
			}
		}
		return deviceDescription;
	}
	
	public void addImage(ImageDescription imageInfo) {
		this.imageList.add(imageInfo);
		this.imageListUpdated = true;
	}
	
	public void addImage(ArrayList<ImageDescription> imageList) {
		this.imageList.addAll(imageList);
		this.imageListUpdated = true;
	}
	
	public void addService(ServiceDescription serviceInfo) {
		this.serviceList.add(serviceInfo);
		this.serviceListUpdated = true;
	}
	
	public void addService(ArrayList<ServiceDescription> serviceList) {
		this.serviceList.addAll(serviceList);
		this.serviceListUpdated = true;
	}
	
	public static UPnPDevice getDeviceInfoFromDescription(byte[] xmlBody) {
		UPnPDevice rtn = new UPnPDevice();
		// Using DOM or SAX Parser.
		return rtn;
	}

	public String getConfigNumber() {
		return configNumber;
	}

	public void setConfigNumber(String configNumber) {
		this.configNumber = configNumber;
	}

	public String getSpecMajor() {
		return specMajor;
	}

	public void setSpecMajor(String specMajor) {
		this.specMajor = specMajor;
	}

	public String getSpecMinor() {
		return specMinor;
	}

	public void setSpecMinor(String specMinor) {
		this.specMinor = specMinor;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerUrl() {
		return manufacturerUrl;
	}

	public void setManufacturerUrl(String manufacturerUrl) {
		this.manufacturerUrl = manufacturerUrl;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	public String getModelSerial() {
		return this.device.getModelSerial();
	}

	public void setModelSerial(String modelSerial) {
		this.device.setModelSerial(modelSerial);
	}

	public String getUuid() {
		return this.device.getUuid();
	}

	public void setUuid(String uuid) {
		this.device.setUuid(uuid);
	}

	public String getUpc() {
		return this.device.getUpc();
	}

	public void setUpc(String upc) {
		this.device.setUpc(upc);
	}
	
	public static void main(String[] args0) {
		
	}

	public UPnPDevice getDevice() {
		return device;
	}

	public void setDevice(UPnPDevice device) {
		this.device = device;

		// Rest elements of device description is ought to be set DEFAULT values.
		this.setConfigNumber(DEVICE_DESCRIPTION_CONFIG_NUMBER);
		this.setSpecMajor(DEVICE_DESCRIPTION_SPECVER_MAJOR);
		this.setSpecMinor(DEVICE_DESCRIPTION_SPECVER_MINOR);
		
		this.setFriendlyName(DEVICE_DESCRIPTION_FRIENDLY_NAME);
		this.setManufacturerName(DEVICE_DESCRIPTION_MANUFACTURER_NAME);
		this.setManufacturerUrl(DEVICE_DESCRIPTION_MANUFACTURER_URL);

		this.setModel(DEVICE_DESCRIPTION_MODEL);
		
		this.setModelName(DEVICE_DESCRIPTION_MODEL_NAME);
		this.setModelNumber(DEVICE_DESCRIPTION_MODEL_NUMBER);
		this.setModelUrl(DEVICE_DESCRIPTION_MODEL_URL);
	}
}

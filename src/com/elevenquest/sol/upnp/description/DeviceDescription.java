package com.elevenquest.sol.upnp.description;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BasicHttpEntity;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.CommonSender;
import com.elevenquest.sol.upnp.network.HTTPSender;
import com.elevenquest.sol.upnp.network.ICommonSendHandler;
import com.elevenquest.sol.upnp.xml.DDSXMLParser;

public class DeviceDescription implements com.elevenquest.sol.upnp.network.ICommonSendHandler {
	
	static final String DEVICE_DESCRIPTION_CONFIG_NUMBER = "0";
	static final String DEVICE_DESCRIPTION_SPECVER_MAJOR = "1";
	static final String DEVICE_DESCRIPTION_SPECVER_MINOR = "1";
	
	static final String DEVICE_DESCRIPTION_FRIENDLY_NAME = "Default Device Description.";
	static final String DEVICE_DESCRIPTION_MANUFACTURER_NAME = "elevenquest";
	static final String DEVICE_DESCRIPTION_MANUFACTURER_URL = "http://www.elevenquest.com";
	
	static final String DEVICE_DESCRIPTION_MODEL = "Test Device";
	
	static final String DEVICE_DESCRIPTION_MODEL_NAME = "ELEVENQUESTDEVICE";
	static final String DEVICE_DESCRIPTION_MODEL_NUMBER = "11_DEVICE000";
	static final String DEVICE_DESCRIPTION_MODEL_URL = "http://www.eleventquest.com";
	
	
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
	String modelDescription;
	String modelNumber;
	String modelUrl;
	String deviceType;
	String presentationURL;
	
	String udn;
	
	// BELOW PART IS VARIALBES. SO YOU CAN SET THE VALUES INTO THAT BY USING CLASS CONSTRUCTORS.
	UPnPDevice device;
	
	private boolean imageListUpdated = false;
	private boolean serviceListUpdated = false;
	
	private String deviceDescription = null;
	private String imageListDescription = null;
	private String serviceListDescription = null;
	
	private ArrayList<ImageElementInDDS> imageList = new ArrayList<ImageElementInDDS>();
	private ArrayList<ServiceElementInDDS> serviceList = new ArrayList<ServiceElementInDDS>();
	
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
	
	
	
	public DeviceDescription(UPnPDevice device) {
		this.device = device;
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
					for ( ImageElementInDDS imageDesc : this.imageList ) {
						buffer.append( imageDesc.getDescription() );
					}
					this.imageListDescription = buffer.toString();
					this.imageListUpdated = false; 
				}
				if ( serviceListUpdated ) {
					StringBuffer buffer = new StringBuffer();
					for ( ServiceElementInDDS serviceDesc : this.serviceList ) {
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
	
	public void addImage(ImageElementInDDS imageInfo) {
		this.imageList.add(imageInfo);
		this.imageListUpdated = true;
	}
	
	public void addImage(ArrayList<ImageElementInDDS> imageList) {
		this.imageList.addAll(imageList);
		this.imageListUpdated = true;
	}
	
	public void addService(ServiceElementInDDS serviceInfo) {
		this.serviceList.add(serviceInfo);
		if ( this.device != null ) {
			// Check duplication of services. 
			Logger.println(Logger.DEBUG, "Target Service Element in DDS :" + serviceInfo.getDescription());
			UPnPService newService = serviceInfo.getDefaultUPnPService(this.device);
			String newServiceId = newService.getServiceId();
			Vector<UPnPService> services = this.device.getSerivces();
			boolean isNewService = true;
			for ( int inx = 0; newServiceId != null && inx < services.size() ; inx++ ) {
				if ( newServiceId.equals( services.get(inx).getServiceId() ) ) {
					Logger.println(Logger.INFO, "Same service already exists.[" + newServiceId + "]" );
					isNewService = false;
					break;
				}
			}
			if ( isNewService ) {
				Logger.println(Logger.INFO, "new service is added.[" + newServiceId + "] into device[" + this.device.getUuid() + "]");
				Logger.println(Logger.DEBUG, "service are below actions & state variables");
				Logger.println(Logger.DEBUG, "-------------------------------------------");
				Logger.println(Logger.DEBUG, newService.toString());
				Logger.println(Logger.DEBUG, "-------------------------------------------");
				this.device.addService(newService);
				this.serviceListUpdated = true;
			}
		}
	}
	
	/*
	public void addService(ArrayList<ServiceElementInDDS> serviceList) {
		this.serviceList.addAll(serviceList);
		this.serviceListUpdated = true;
	}
	*/
	
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

	public Object getSendObject() throws Exception {
		HttpGet request = new HttpGet(this.device.getLocation());
		// TODO : modify below line which to retreive the version of OS.
		String osVersion = "WindowsNT";
		String productVersion = "simpledlna/1.0";
		request.addHeader("USER-AGENT", osVersion + " UPnP/1.1 " + productVersion );
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream(this.getRequestBody().getBytes("utf-8")));
		if ( this.device.getAuthorizationStr() != null )
			request.addHeader("Authorization", "Basic " + this.device.getAuthorizationStr() );
		return request;
	}
	
	private String getRequestBody() {
		return "";
	}

	public Object processAfterSend(Object receivedData) {
		// Parsing DeviceDescription XML and fill full information of that device.
		try {
			HttpResponse response = (HttpResponse)receivedData;
			System.out.println("Device Description Response Status value:" + response.getStatusLine().getStatusCode() );
			if ( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
				HttpEntity entity = response.getEntity();
				Header[] headers = response.getAllHeaders();
				for ( int cnt = 0 ; cnt < headers.length; cnt++ ) {
					System.out.println( headers[cnt].getName() + ":" + headers[cnt].getValue());
				}
				byte[] contentBytes = new byte[(int)entity.getContentLength()];
				entity.getContent().read(contentBytes,0, (int)entity.getContentLength());
				String trimmedContents = new String(contentBytes).trim();
				ByteArrayInputStream ais = new ByteArrayInputStream(trimmedContents.getBytes());
				DDSXMLParser parser = new DDSXMLParser(this, ais);
				parser.execute();
				PrintAllValue();
				
				Vector<UPnPService> services = this.getDevice().getSerivces();
				
				for ( int inx = 0 ; inx < services.size() ; inx++ ) {
					UPnPService service = services.get(inx);
					if ( service.isRemote() && !service.isReadyToUse() && !service.isProgressingToRetrieve() ) {
						// 원칙적으로 여기에서 Service Description을 가지고 오는것이 아니라,
						// UPnPDevice에서 새로이 등록된 Service가 있는 경우, Serivce를 Update하는게 이치적으로 맞음
						// But. 구현시 서버가 추가되어야 하므로 여기서 바로 얻어서 Update하는 것으로 로직 구성
						// 이럴 경우, Hang 현상 발생가능.
						
						class ServiceDescriptionSendThread extends Thread {
							UPnPService inner = null;
							public ServiceDescriptionSendThread(UPnPService outer) {
								inner = outer;
							}
							public void run() {
								try {
									CommonSender sender = new HTTPSender(inner.getDevice().getNetworkInterface(),inner.getScpdUrl());
									ICommonSendHandler handler = new ServiceDescription(inner);
									sender.setSenderHandler(handler);
									sender.sendData();
								} catch ( Exception e ) {
									e.printStackTrace();
								}
							}
						}
						
						ServiceDescriptionSendThread thread = new ServiceDescriptionSendThread(service);
						thread.run();
						
						
					}
				}
			}
			else
			{
				System.out.println("To retrieve Device Description failed. cause : " + response.toString() );
			}
		} catch ( Exception e ) {
			// TODO : Exception processing is required.
			e.printStackTrace();
		}
		this.device.setProgressingToRetrieve(false);
		return null;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getUdn() {
		return udn;
	}

	public void setUdn(String udn) {
		this.udn = udn;
	}

	public String getPresentationURL() {
		return presentationURL;
	}

	public void setPresentationURL(String presentationURL) {
		this.presentationURL = presentationURL;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Field[] fields = this.getClass().getDeclaredFields();
		for ( int inx = 0; inx < fields.length ; inx++ ) {
			try {
				sb.append(fields[inx].getName()).append(":").append((fields[inx].get(this) != null ) ? fields[inx].get(this).toString() : "null").append("\n");
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void PrintAllValue(){
		if(this.configNumber!="") Logger.println(Logger.INFO, this.configNumber);
		if(this.specMajor!="") Logger.println(Logger.INFO, this.specMajor);
		if(this.specMinor!="") Logger.println(Logger.INFO, this.specMinor);
		if(this.friendlyName!="") Logger.println(Logger.INFO, this.friendlyName);
		if(this.manufacturerName!="") Logger.println(Logger.INFO, this.manufacturerName);
		if(this.manufacturerUrl!="") Logger.println(Logger.INFO, this.manufacturerUrl);
		if(this.model!="") Logger.println(Logger.INFO, this.model);
		if(this.modelName!="") Logger.println(Logger.INFO, this.modelName);
		if(this.modelNumber!="") Logger.println(Logger.INFO, this.modelNumber);
		if(this.modelUrl!="") Logger.println(Logger.INFO, this.modelUrl);
		if(this.modelDescription!="") Logger.println(Logger.INFO, this.modelDescription);
		if(this.deviceType!="")Logger.println(Logger.INFO, this.deviceType);
		if(this.presentationURL!="") Logger.println(Logger.INFO, this.presentationURL);
		if(this.configNumber!="") Logger.println(Logger.INFO, this.configNumber);
		if(this.configNumber!="") Logger.println(Logger.INFO, this.configNumber);
	}
	
}

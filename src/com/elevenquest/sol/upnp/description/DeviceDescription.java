package com.elevenquest.sol.upnp.description;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceImage;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.HttpTcpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.xml.DDSXMLParser;

public class DeviceDescription implements com.elevenquest.sol.upnp.network.IHttpRequestSuplier {
	
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
		 device = new UPnPDevice();
		 this.device.setConfigNumber(configNumber);
		 this.device.setSpecMajor(specMajor);
		 this.device.setSpecMinor(specMinor);
		 this.device.setFriendlyName(friendlyName);
		 this.device.setManufacturerName(manufacturerName);
		 this.device.setManufacturerUrl(manufacturerUrl);
		 this.device.setModel(model);
		 this.device.setModelName(modelName);
		 this.device.setModelNumber(modelNumber);
		 this.device.setModelUrl(modelUrl);
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
		UPnPDeviceImage image = imageInfo.getDeviceImageStructure();
		image.setDevice(this.device);
		Logger.println(Logger.DEBUG, "[Device Description] image info:" + image.getUrl() );
		this.device.addDeviceImage(image);
	}
	
	/*
	public void addImage(ArrayList<ImageElementInDDS> imageList) {
		this.imageList.addAll(imageList);
		this.imageListUpdated = true;
	}
	*/
	
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
		return this.device.getConfigNumber();
	}

	public void setConfigNumber(String configNumber) {
		this.device.setConfigNumber(configNumber);
	}

	public String getSpecMajor() {
		return this.device.getSpecMajor();
	}

	public void setSpecMajor(String specMajor) {
		this.device.setSpecMajor(specMajor);
	}

	public String getSpecMinor() {
		return this.device.getSpecMinor();
	}

	public void setSpecMinor(String specMinor) {
		this.device.setSpecMinor(specMinor);
	}

	public String getFriendlyName() {
		return this.device.getFriendlyName();
	}

	public void setFriendlyName(String friendlyName) {
		this.device.setFriendlyName(friendlyName);
	}

	public String getManufacturerName() {
		return this.device.getManufacturerName();
	}

	public void setManufacturerName(String manufacturerName) {
		this.device.setManufacturerName(manufacturerName);
	}

	public String getManufacturerUrl() {
		return this.device.getManufacturerUrl();
	}

	public void setManufacturerUrl(String manufacturerUrl) {
		this.device.setManufacturerUrl(manufacturerUrl);
	}

	public String getModel() {
		return this.device.getModel();
	}

	public void setModel(String model) {
		this.device.setModel(model);
	}

	public String getModelName() {
		return this.device.getModelName();
	}

	public void setModelName(String modelName) {
		this.device.setModelName(modelName);
	}

	public String getModelNumber() {
		return this.device.getModelNumber();
	}

	public void setModelNumber(String modelNumber) {
		this.device.setModelNumber(modelNumber);
	}

	public String getModelUrl() {
		return this.device.getModelUrl();
	}

	public void setModelUrl(String modelUrl) {
		this.device.setModelUrl(modelUrl);
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

	private String getRequestBody() {
		return "";
	}

	public String getDeviceType() {
		return this.device.getDeviceType();
	}

	public void setDeviceType(String deviceType) {
		this.device.setDeviceType(deviceType);
	}

	public String getModelDescription() {
		return this.device.getModelDescription();
	}

	public void setModelDescription(String modelDescription) {
		this.device.setModelDescription(modelDescription);
	}

	public String getUdn() {
		return udn;
	}

	public void setUdn(String udn) {
		this.udn = udn;
	}

	public String getPresentationURL() {
		return this.device.getPresentationURL();
	}

	public void setPresentationURL(String presentationURL) {
		this.device.setPresentationURL(presentationURL);
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
		if(this.getConfigNumber() !="") Logger.println(Logger.INFO, getConfigNumber());
		if(this.getSpecMajor() !="") Logger.println(Logger.INFO, getSpecMajor());
		if(this.getSpecMinor() !="") Logger.println(Logger.INFO, getSpecMinor());
		if(this.getFriendlyName() !="") Logger.println(Logger.INFO, getFriendlyName());
		if(this.getManufacturerName() !="") Logger.println(Logger.INFO, getManufacturerName());
		if(this.getManufacturerUrl() !="") Logger.println(Logger.INFO, getManufacturerUrl());
		if(this.getModel() !="") Logger.println(Logger.INFO, getModel());
		if(this.getModelName() !="") Logger.println(Logger.INFO, getModelName());
		if(this.getModelNumber() !="") Logger.println(Logger.INFO, getModelNumber());
		if(this.getModelUrl() !="") Logger.println(Logger.INFO, getModelUrl());
		if(this.getModelDescription() !="") Logger.println(Logger.INFO, getModelDescription());
		if(this.getDeviceType() !="")Logger.println(Logger.INFO, getDeviceType());
		if(this.getPresentationURL() !="") Logger.println(Logger.INFO, getPresentationURL());
	}

	@Override
	public HttpRequest getHTTPRequest() throws Exception {
		HttpRequest request = new HttpRequest();
		request.setHttpVer("HTTP/1.1");
		request.setUrlPath("*");
		request.setCommand("GET");
		request.setUrlPath(this.device.getLocation());
		request.addHeader("USER-AGENT", DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE);
		request.setBodyArray(this.getRequestBody().getBytes("utf-8"));
		if ( this.device.getAuthorizationStr() != null )
			request.addHeader("Authorization", "Basic " + this.device.getAuthorizationStr() );
		return request;
	}

	@Override
	public void processAfterSend(
			HttpResponse response) {
		// Parsing DeviceDescription XML and fill full information of that device.
		try {
			System.out.println("Device Description Response Status value:" + response.getStatusCode() );
			if ( response.getStatusCode().equals(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200) ) {
				ArrayList<String> headerNames = response.getHeaderNames();
				for ( int cnt = 0 ; cnt < response.getHeaderCount(); cnt++ ) {
					System.out.println( headerNames.get(cnt) + ":" + response.getHeaderValue(headerNames.get(cnt)));
					this.device.setAdditionalHeaderValue(headerNames.get(cnt), response.getHeaderValue(headerNames.get(cnt)));
				}
				String trimmedContents = new String(response.getBodyArray()).trim();
				ByteArrayInputStream ais = new ByteArrayInputStream(trimmedContents.getBytes());
				DDSXMLParser parser = new DDSXMLParser(this, ais);
				parser.execute();
				PrintAllValue();
				
				Vector<UPnPService> services = this.getDevice().getSerivces();
				
				for ( int inx = 0 ; inx < services.size() ; inx++ ) {
					UPnPService service = services.get(inx);
					if ( service.isRemote() && !service.isReadyToUse() && !service.isProgressingToRetrieve() ) {
						// ��Ģ������ ���⿡�� Service Description�� ������ ���°��� �ƴ϶�,
						// UPnPDevice���� ������ ��ϵ� Service�� �ִ� ���, Serivce�� Update�ϴ°� ��ġ������ ����
						// But. ������ ������ �߰��Ǿ�� �ϹǷ� ���⼭ �ٷ� �� Update�ϴ� ������ ���� ����
						// �̷� ���, Hang ���� �߻��.
						
						class ServiceDescriptionSendThread extends Thread {
							UPnPService inner = null;
							public ServiceDescriptionSendThread(UPnPService outer) {
								inner = outer;
							}
							public void run() {
								try {
									HttpRequestSender sender = new HttpTcpSender(inner.getDevice().getNetworkInterface(),inner.getScpdUrl());
									IHttpRequestSuplier handler = new ServiceDescription(inner);
									sender.setSenderHandler(handler);
									sender.sendData();
								} catch ( Exception e ) {
									e.printStackTrace();
								}
							}
						}
						ServiceDescriptionSendThread thread = new ServiceDescriptionSendThread(service);
						thread.run();
					} else {
						
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
	}
	
}

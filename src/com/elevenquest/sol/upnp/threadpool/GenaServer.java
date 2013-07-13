package com.elevenquest.sol.upnp.threadpool;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.description.DeviceDescription;
import com.elevenquest.sol.upnp.description.ServiceDescription;
import com.elevenquest.sol.upnp.discovery.SSDPReceiveHandler;
import com.elevenquest.sol.upnp.discovery.SSDPSearchSendHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.gena.EventNotify;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPEventManager;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpRequestReceiver;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.HttpTcpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;
import com.elevenquest.sol.upnp.xml.NotifyXMLParser;

public class GenaServer {
	
	ArrayList<CommonThreadPool> receiveServerList = null;
	ArrayList<CommonThreadPool> senderServerList = null;
	
	public void start() {
		startReceiveServer();
		startSendServer();
	}
	
	static class CommonHttpRequestHandler implements IHttpRequestHandler {

		boolean validateHeaderfield(HttpRequest request) {
			if ( request.getHeaderValue("User-Agent") != null && request.getHeaderValue("User_Agent").length() == 0 ) {
				return false;
			} if ( request.getHeaderValue("USER-AGENT") != null && request.getHeaderValue("USER-AGENT").length() == 0 ) {
				return false;
			}
			return true;
		}

		static HashMap<String, String> PERCENT_ENCODED_LIST = new HashMap<String,String>() {
			{
			put("0A", " ");
			put("0D", " ");
			put("20", " ");
			put("22", "\"");
			put("25", "%");
			put("2D", "-");
			put("2E", ".");
			put("3C", "<");
			put("3E", ">");
			put("5C", "\\");
			put("5E", "^");
			put("60", "`");
			put("7B", "{");
			put("7C", "|");
			put("7D", "}");
			put("7E", "~");
			}
		};
		
		HashMap<String, String> parameters = new HashMap<String,String>();
		
		static int hexValue(char value) {
			int ret = 0;
			switch ( value ) {
			case '0' :
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' :
			case '8' :
			case '9' :
				ret = value - '0';
				break;
			case 'a' :
			case 'b' :
			case 'c' :
			case 'd' :
			case 'e' :
			case 'f' :
				ret = value - 'a';
				break;
			case 'A' :
			case 'B' :
			case 'C' :
			case 'D' :
			case 'E' :
			case 'F' :
				ret = value - 'A';
				break;
			default :
				Logger.println(Logger.WARNING, "It's not hex value.");
				break;
			}
			return ret;
		}
		
		static String decodePercent(String encoded) {
			boolean hasPercent = false;
			for ( int position = 0 ; position < encoded.length() ; position++ ) {
				if ( encoded.indexOf(position) == '%' ) {
					hasPercent = true;
					break;
				}
			}
			if ( !hasPercent )
				return encoded;
			
			StringBuffer sb = new StringBuffer();
			for ( int position = 0 ; position < encoded.length() ; position++ ) {
				if ( encoded.charAt(position) == '%' ) {
					if ( position + 2 < encoded.length() ) {
						String asciiValue = encoded.substring(position + 1, position +3).toUpperCase();
						if ( PERCENT_ENCODED_LIST.containsKey(asciiValue) ) {
							sb.append(PERCENT_ENCODED_LIST.get(asciiValue));
						} else {
							int asciiCode = hexValue(asciiValue.charAt(0)) * 16 + hexValue(asciiValue.charAt(1));
							if ( asciiCode < 0x20 ) {
								sb.append(" ");
							} else if ( asciiCode < 0x80 ) {
								sb.append(new Character((char)asciiCode));
							}
						}
						position += 2;
					} else {
						Logger.println(Logger.WARNING, "There is invalid percent character.");
					}
				} else {
					sb.append(encoded.charAt(position));
				}
			}
			return sb.toString();
		}
		
		void parseParameters(HttpRequest request) {
			String keyValuePairs = null;
			int startPosition = request.getUrlPath().indexOf('?');
			if ( startPosition < 1 ) {
				Logger.println(Logger.WARNING, "Request from client doesn't have parameters.");
				if ( request.getCommand().equals(HttpRequest.HTTP_REQUEST_COMMAND_POST) ) {
					keyValuePairs = new String(request.getBodyArray());
				} else {
					keyValuePairs = "";
				}
			} else {
				keyValuePairs = request.getUrlPath().substring(startPosition);
			}
			String key = null;
			String value = null;
			boolean isParameterRegion = false;
			int lastTokenPosition = 0;
			for ( int position = 0 ; position < keyValuePairs.length() ; position ++ ) {
				if ( isParameterRegion && keyValuePairs.charAt(position) == '=' ) {
					key = keyValuePairs.substring(lastTokenPosition, position );
					lastTokenPosition = position + 1;
				} else if ( isParameterRegion && keyValuePairs.charAt(position) == '&' ) {
					value = keyValuePairs.substring(lastTokenPosition, position );
					lastTokenPosition = position + 1;
					if ( key != null && key.length() > 0 ) {
						value = decodePercent(value);
						parameters.put(key, value);
					} else {
						Logger.println(Logger.WARNING, "There is no key in KeyValuePairs.");
					}
					key = null;
					value = null;
				} else if ( keyValuePairs.charAt(position) == '?' ) {
					lastTokenPosition = position + 1;
					isParameterRegion = true;
				}
			}
			if ( key != null && key.length() > 0 ) {
				value = keyValuePairs.substring(lastTokenPosition);
				value = decodePercent(value);
				parameters.put(key, value);
			}
		}
		
		@Override
		public HttpResponse process(HttpRequest request) {
			HttpResponse response = new HttpResponse();
			if ( !validateHeaderfield(request) ) {
				response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
				response.setStatusCode("401");
				response.setReasonPhrase("Header field dones't have user-agent field.");
			}
			parseParameters(request);
			// TODO : Should be modified
			String did = parameters.get("device_id");
			String sid = parameters.get("service_id");
			if ( request.getUrlPath().contains("ddd.xml")) {
				UPnPDevice device = UPnPDeviceManager.getDefaultDeviceManager().getDevice(did);
				if ( device != null ) {
					if ( device.isRemote() ) {
						Logger.println(Logger.WARNING, "[WEB SERVER] We can't support description.xml for remote device[" + did  + "]");
					} else {
						DeviceDescription description = new DeviceDescription(device);
						response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200);
						response.setReasonPhrase(HttpResponse.HTTP_RESPONSE_REASON_PHRASE_200);
						response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
						response.setBodyArray(description.getDeviceDescription().getBytes());
					}
				} else {
					Logger.println(Logger.WARNING, "[WEB SERVER] There is no device[" + did  + "] requested by client.");
					response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_401);
					response.setReasonPhrase("There is no device has uuid[" + did + "] requested.");
					response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
				}
			} else if ( request.getUrlPath().contains("sdd.xml")) {
				UPnPDevice device = UPnPDeviceManager.getDefaultDeviceManager().getDevice(did);
				if ( device != null ) {
					if ( device.isRemote() ) {
						Logger.println(Logger.WARNING, "[WEB SERVER] We can't support description.xml for remote device[" + did  + "]");
					} else {
						UPnPService service = device.getUPnPService(sid);
						if ( service != null ) {
							ServiceDescription description = new ServiceDescription(service);
							response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_200);
							response.setReasonPhrase(HttpResponse.HTTP_RESPONSE_REASON_PHRASE_200);
							response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
							response.setBodyArray(description.getServiceDescription().getBytes());
						} else {
							Logger.println(Logger.WARNING, "[WEB SERVER] There is no serivce[" + sid  + "] requested by client in this device[" + did + "].");
							response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_401);
							response.setReasonPhrase("There is no device has uuid[" + did + "] requested.");
							response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
						}
					}
				} else {
					Logger.println(Logger.WARNING, "[WEB SERVER] There is no device[" + did  + "] requested by client.");
					response.setStatusCode(HttpResponse.HTTP_RESPONSE_STATUS_CODE_401);
					response.setReasonPhrase("There is no device has uuid[" + did + "] requested.");
					response.setHttpVer(HttpRequest.HTTP_VERSION_1_1);
				}
			} else if ( request.getUrlPath().contains("action.do")) {
				// TODO : For response to the SOAP action control.
			} else if ( request.getUrlPath().contains("subscribe.do")) {
				// TODO : For response to the subscription from a CP.
			} else if ( request.getUrlPath().contains("notify.do")) {
				String responseCode = HttpResponse.HTTP_RESPONSE_STATUS_CODE_200;
				String responsePhrase = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_200;
				if ( !request.getCommand().equalsIgnoreCase("NOTIFY")) {
					Logger.println(Logger.WARNING, "[WEB SERVER] There is no such command[" + request.getCommand() + "] in this url.");
					responseCode = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
					responsePhrase = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
				} else if ( !request.getHeaderValue("NT").equals("upnp:event") ) {
					Logger.println(Logger.WARNING, "[WEB SERVER] There is no handler to deal with nt value[" + request.getHeaderValue("NT") + "] in this request.");
					responseCode = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
					responsePhrase = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
				} else if ( !request.getHeaderValue("NTS").equals("upnp:propchange") ) {
					Logger.println(Logger.WARNING, "[WEB SERVER] There is no handler to deal with nts value[" + request.getHeaderValue("NTS") + "] in this request.");
					responseCode = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
					responsePhrase = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
				} else {
					UPnPDevice device = UPnPDeviceManager.getDefaultDeviceManager().getDevice(did);
					EventNotify notify = new EventNotify(request);
					NotifyXMLParser parser = new NotifyXMLParser(notify, request.getBodyInputStream());
					parser.execute();
					if ( device != null ) {
						UPnPService service = device.getUPnPService(sid);
						if ( service != null ) {
							if ( service.getDevice().getUuid().equals(did) ) {
								Enumeration<String> propertyNames = notify.getPropertyNameList();
								while( propertyNames.hasMoreElements() ) {
									String propertyName = propertyNames.nextElement();
									Logger.println(Logger.DEBUG,"[GENA SERVER] notify.do [property]:" + propertyName + "] [value]:[" + notify.getPropertyValue(propertyName) + "]");
									UPnPStateVariable variable = service.getStateVariable(propertyName);
									if ( variable == null ) {
										Logger.println(Logger.WARNING,"[GENA SERVER] There is no matching property[" + propertyName + "] in this service[" + sid + "] of device[" + did + "]");
									} else {
										variable.setValue(notify.getPropertyValue(propertyName));
									}
								}
							} else {
								Logger.println(Logger.WARNING, "[WEB SERVER] There is no service matched with device id[" + did +"] and service_id [" + sid + "]");
								responseCode = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
								responsePhrase = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
							}
						} else {
							Logger.println(Logger.WARNING, "[WEB SERVER] There is no service to deal with service_id[" + sid + "] in this request.");
							responseCode = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
							responsePhrase = HttpResponse.HTTP_RESPONSE_REASON_PHRASE_401;
						}
					}
				}
				response.setStatusCode(responseCode);
				response.setReasonPhrase(responsePhrase);
			} else if ( request.getUrlPath().contains("mail.html")) {
				// TODO :
			}
			return response;
		}
		
	}
	
	public void startReceiveServer() {
		if ( receiveServerList != null ) 
			stopReceiveServer();
		receiveServerList = new ArrayList<CommonThreadPool>();
		ArrayList<NetworkInterface> interfaces = UPnPUtils.getAvailiableNetworkInterfaces();
		for ( NetworkInterface intf : interfaces ) {
			try {
				// 1. Normal HTTP Request
				HttpRequestReceiver receiver = new HttpTcpReceiver(intf, 80);
				IHttpRequestHandler handler = new CommonHttpRequestHandler();
				receiver.setReceiveHandler(handler);
				// 2. Create Common server
				CommonThreadPool receiveServer = new CommonThreadPool();
				// 3. set receiver into server.
				receiveServer.setReceiver(receiver);
				// 4. start server.
				receiveServer.startServer();
				
				receiveServerList.add(receiveServer);
			} catch ( AbnormalException abe ) {
				abe.printStackTrace();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public void startSendServer() {
		if ( senderServerList != null )
			stopSendServer();
		senderServerList = new ArrayList<CommonThreadPool>();
		
	}
	
	public void stop() {
		stopReceiveServer();
		stopSendServer();
	}
	
	public void stopReceiveServer() {
		if ( receiveServerList != null )
			for ( CommonThreadPool server : receiveServerList ) server.stopServer();
		receiveServerList = null;
	}
	
	public void stopSendServer() {
		if ( senderServerList != null )
			for ( CommonThreadPool server : senderServerList ) server.stopServer();
		senderServerList = null;
	}
	
	public static void main(String[] args) {
		GenaServer.CommonHttpRequestHandler handler = new GenaServer.CommonHttpRequestHandler();
		HttpRequest request = new HttpRequest();
		request.setCommand("NOTIFY");
		request.setUrlPath("/notify.do?device_id=05765fc0-cbde-41ef-8859-a7a0766d0759&service_id=urn:upnp-org:serviceId:ConnectionManager");
		request.setHttpVer("HTTP/1.1");
		handler.parseParameters(request);
		HashMap<String, String>  headers = handler.parameters;
		for( Iterator<String> iter = headers.keySet().iterator() ; iter != null && iter.hasNext() ; ) {
			String name = iter.next();
			System.out.println( name + ":" + headers.get(name));
		}
	}
}

package com.elevenquest.sol.upnp.server;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.description.DeviceDescription;
import com.elevenquest.sol.upnp.description.ServiceDescription;
import com.elevenquest.sol.upnp.discovery.SSDPReceiveHandler;
import com.elevenquest.sol.upnp.discovery.SSDPSearchSendHandler;
import com.elevenquest.sol.upnp.exception.AbnormalException;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpRequestReceiver;
import com.elevenquest.sol.upnp.network.HttpRequestSender;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.HttpTcpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpReceiver;
import com.elevenquest.sol.upnp.network.HttpUdpSender;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;
import com.elevenquest.sol.upnp.network.IHttpRequestSuplier;

public class GenaServer {
	
	ArrayList<CommonServer> receiveServerList = null;
	ArrayList<CommonServer> senderServerList = null;
	
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
			if ( request.getCommand().equals(HttpRequest.HTTP_REQUEST_COMMAND_GET) ) {
				int startPosition = request.getUrlPath().indexOf('?');
				if ( startPosition < 1 ) {
					Logger.println(Logger.WARNING, "Request from client doesn't have parameters.");
					keyValuePairs = "";
				} else {
					keyValuePairs = request.getUrlPath().substring(startPosition);
				}
			} else if ( request.getCommand().equals(HttpRequest.HTTP_REQUEST_COMMAND_POST) ) {
				keyValuePairs = new String(request.getBodyArray());
			}
			String key = null;
			String value = null;
			int lastTokenPosition = 0;
			for ( int position = 0 ; position < keyValuePairs.length() ; position ++ ) {
				if ( keyValuePairs.charAt(position) == '=' ) {
					key = keyValuePairs.substring(lastTokenPosition, ( position - lastTokenPosition));
					lastTokenPosition = position + 1;
				} else if ( keyValuePairs.charAt(position) == '&' ) {
					value = keyValuePairs.substring(lastTokenPosition, ( position - lastTokenPosition));
					lastTokenPosition = position + 1;
					if ( key != null && key.length() > 0 ) {
						value = decodePercent(value);
						parameters.put(key, value);
					} else {
						Logger.println(Logger.WARNING, "There is no key in KeyValuePairs.");
					}
					key = null;
					value = null;
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
				// TODO :
			} else if ( request.getUrlPath().contains("subscribe.do")) {
				// TODO :
			} else if ( request.getUrlPath().contains("notify.do")) {
				
			} else if ( request.getUrlPath().contains("mail.html")) {
				// TODO :
			}
			return response;
		}
		
	}
	
	public void startReceiveServer() {
		if ( receiveServerList != null ) 
			stopReceiveServer();
		receiveServerList = new ArrayList<CommonServer>();
		ArrayList<NetworkInterface> interfaces = UPnPUtils.getAvailiableNetworkInterfaces();
		for ( NetworkInterface intf : interfaces ) {
			try {
				// 1. Normal HTTP Request
				HttpRequestReceiver receiver = new HttpTcpReceiver(intf, 80);
				IHttpRequestHandler handler = new CommonHttpRequestHandler();
				receiver.setReceiveHandler(handler);
				// 2. Create Common server
				CommonServer receiveServer = new CommonServer();
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
		senderServerList = new ArrayList<CommonServer>();
		ArrayList<NetworkInterface> interfaces = UPnPUtils.getAvailiableNetworkInterfaces();
		/*
		for ( NetworkInterface intf : interfaces ) {
			try {
				// 1. Next, create ssdp message supplier
				HttpRequestSender sender = new HttpUdpSender(intf, UPnPDevice.DEFAULT_UPNP_MULTICAST_ADDRESS, 
						UPnPDevice.DEFAULT_UPNP_MULTICAST_PORT);
				IHttpRequestSuplier suplier = new SSDPSearchSendHandler(cp);
				sender.setSenderHandler(suplier);
				// 2. Create Common server
				CommonServer sendServer = new CommonServer();
				// 3. set sender into server.
				sendServer.setSender(sender, new SendEvent(3000, SendEvent.SEND_EVENT_TYPE_TIME_UNLIMINITED));
				// 4. start server.
				sendServer.startServer();
				
				senderServerList.add(sendServer);
			} catch ( AbnormalException abe ) {
				abe.printStackTrace();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		*/
	}
	
	public void stop() {
		stopReceiveServer();
		stopSendServer();
	}
	
	public void stopReceiveServer() {
		if ( receiveServerList != null )
			for ( CommonServer server : receiveServerList ) server.stopServer();
		receiveServerList = null;
	}
	
	public void stopSendServer() {
		if ( senderServerList != null )
			for ( CommonServer server : senderServerList ) server.stopServer();
		senderServerList = null;
	}	
}

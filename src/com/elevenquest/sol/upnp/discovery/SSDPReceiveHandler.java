package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.network.HttpBaseStructure;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.IHttpReceiveHandler;

public class SSDPReceiveHandler implements IHttpReceiveHandler {

	@Override
	public HttpBaseStructure process(HttpBaseStructure requestOrResponse) {
		try {
			if ( requestOrResponse.getRequest() != null ) {
				HttpRequest request = requestOrResponse.getRequest();
				SSDPNotifyRequest message = new SSDPNotifyRequest(request);
		 		UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
				if ( request.getCommand().equalsIgnoreCase("NOTIFY") ) {
					if ( HttpHeaderName.ID_NT_SUBTYPE_SSDPALIVE.equalsIgnoreCase(
							message.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_NT_SUBTYPE)) ) {
						manager.addDevice(message.getDeviceBaseInfo());
					} else if (HttpHeaderName.ID_NT_SUBTYPE_SSDPBYEBYE.equalsIgnoreCase(
							message.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_NT_SUBTYPE))) {
						manager.removeDevice(message.getDeviceBaseInfo().getUuid());
					} else if (HttpHeaderName.ID_NT_SUBTYPE_SSDPUPDATE.equalsIgnoreCase(
							message.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_NT_SUBTYPE))) {
						manager.updateDevice(message.getDeviceBaseInfo().getUuid());
					} else if (message.getHeaderValue(HttpHeaderName.ID_UPNP_HTTP_HEADER_ST) != null && message.getHeaderValue("location") != null ) {
						manager.addDevice(message.getDeviceBaseInfo());
					}
				} else if ( request.getCommand().equalsIgnoreCase("M-SEARCH") ){
					// TODO : M-Search request handler.
					Logger.println(Logger.DEBUG, "[SSDP Receiver] The message which doesnt' contain NOTIFY header was received.");
				}
			} else if ( requestOrResponse.getResponse() != null ) {
				HttpResponse response = requestOrResponse.getResponse();
				if ( response.getStatusCode().equals("200") ) {
					SSDPMsearchResponse message = new SSDPMsearchResponse(response);
					UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
					manager.addDevice(message.getDeviceBaseInfo());
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

}

package com.elevenquest.sol.upnp.discovery;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.network.HttpHeaderName;
import com.elevenquest.sol.upnp.network.HttpRequest;
import com.elevenquest.sol.upnp.network.HttpResponse;
import com.elevenquest.sol.upnp.network.IHttpRequestHandler;

public class SSDPReceiveHandler implements IHttpRequestHandler {

	@Override
	public HttpResponse process(HttpRequest request) {
		SSDPNotifyRequest message = new SSDPNotifyRequest(request);
 		UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
		try {
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
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

}

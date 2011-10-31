package com.lgcns.sol.upnp.action;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lgcns.sol.upnp.exception.AbnormalException;
import com.lgcns.sol.upnp.model.UPnPAction;
import com.lgcns.sol.upnp.model.UPnPStateVariable;
import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;

public class ActionExecutor {
	
	UPnPAction action = null;
	
	String targetURL = null;
	URL url = null;
	HttpURLConnection conn = null;
	OutputStreamWriter requestWriter = null;
	InputStreamReader responseReader = null;
	String responseXML = null;
	
	public ActionExecutor(UPnPAction targetAction) {
		this.action = targetAction;
	}
	
	public void execute() throws Exception {
	    System.out.println("Start sending " + action.getActionName() + " request");
	    try {
			// 1. find target URL.
			findTargetUrl();
			// 2. Open connection and make Http Header.
		    conn = (HttpURLConnection)url.openConnection();   
		    conn.setRequestMethod("POST");   
		    conn.setDoOutput( true );   
		    conn.setDoInput( true );    
		    conn.setRequestProperty( "Content-Type", "text/xml; charset=utf-8" );   
			// 3. Create SOAP body
		    String reqStr = makeSoapBody();  
		    int len = reqStr.length();   
		    conn.setRequestProperty( "Content-Length", Integer.toString( len ) );   
		    conn.setRequestProperty("SOAPAction", action.getService().getServiceType() + "#" + action.getActionName() );   
	
		    // 4. Flush
			conn.connect();       
			requestWriter = new OutputStreamWriter( conn.getOutputStream() );    
			requestWriter.write( reqStr, 0, len );   
			requestWriter.flush();
			
			// 5. Get Response
			System.out.println("Request sent, reading response ");   
			responseReader = new InputStreamReader( conn.getInputStream() );   
			
			// 6. Parsing & Exception process.
			StringBuilder sb = new StringBuilder();      
			int ch = responseReader.read();   
		    while( ch != -1 ){   
				sb.append((char)ch);   
				ch = responseReader.read();   
			}
		    responseXML = sb.toString();
		    parseReponse();
		} finally {
	    	if ( responseReader != null ) try { responseReader.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    	if ( requestWriter != null ) try { requestWriter.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    	if ( conn != null ) try { conn.disconnect(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    }
	}
	
	private void findTargetUrl() throws MalformedURLException {
		this.targetURL = this.action.getService().getControlUrl();
	    this.url = new URL( targetURL );   
	}
	
	private String makeSoapBody() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>\n")
		.append("<s:Envelope\n")
		.append("xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"\n")
		.append("s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n")
		.append("<s:Body>\n")
		.append("<u:" + action.getActionName() + " xmlns:u=\"urn:schemas-upnp-org:service:" + action.getService().getServiceType() + "\">\n");
		for ( UPnPStateVariable arg : action.getInArguments() ) {
			sb.append("<" + arg.getName() + ">" + arg.getValue().toString() + "</" + arg.getName() + ">\n");
		}
		sb.append("</u:" + action.getActionName() + ">\n")
		.append("</s:Body>\n")
		.append("</s:Envelope>");
		return sb.toString();
	}
	
	private void parseReponse() {
		// TODO : Make your own response parser.
		/*
		HTTP/1.0 200 OK
		CONTENT-TYPE: text/xml; charset="utf-8"
		DATE: when response was generated
		SERVER: OS/version UPnP/1.1 product/version
		CONTENT-LENGTH: bytes in body
		<?xml version="1.0"?>
		<s:Envelope
		xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
		s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
		<s:Body>
		<u:actionNameResponse xmlns:u="urn:schemas-upnp-org:service:serviceType:v">
		<argumentName>out arg value</argumentName>
		<!-- other out args and their values go here, if any -->
		</u:actionNameResponse>
		</s:Body>
		</s:Envelope>
		*/
	}
	
}

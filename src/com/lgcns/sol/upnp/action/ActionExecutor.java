package com.lgcns.sol.upnp.action;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lgcns.sol.upnp.common.UPnPUtils;
import com.lgcns.sol.upnp.model.UPnPAction;
import com.lgcns.sol.upnp.model.UPnPStateVariable;

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
			
			// 5. Retrieve Response & Parsing.
		    parseReponse();
		    
		} finally {
	    	if ( responseReader != null ) try { responseReader.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    	if ( requestWriter != null ) try { requestWriter.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    	if ( conn != null ) try { conn.disconnect(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    }
	}
	
	private void findTargetUrl() throws MalformedURLException {
		this.targetURL = this.action.getService().getDevice().getAbsoluteURL(this.action.getService().getControlUrl());
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
		System.out.println("ACTION :" + action.getActionName() );
		for ( UPnPStateVariable arg : action.getInArguments() ) {
			System.out.println("IN ARG :" + arg.getArgumentName() + ":" + arg.getValue());
			sb.append("<" + arg.getArgumentName() + ">" + ( ( arg.getValue() != null ) ? arg.getValue().toString() : "" ) + "</" + arg.getArgumentName() + ">\n");
		}
		sb.append("</u:" + action.getActionName() + ">\n")
		.append("</s:Body>\n")
		.append("</s:Envelope>");
		return sb.toString();
	}
	
	private void parseReponse() throws Exception {

		/*
		HTTP/1.0 200 OK				---> only this field used.
		CONTENT-TYPE: text/xml; charset="utf-8"
		DATE: when response was generated
		SERVER: OS/version UPnP/1.1 product/version
		CONTENT-LENGTH: bytes in body
		*/
		if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser;
			Document doc;
			NodeList actionResponseNode;
			Element actionResponseElement;
			try {
				/*
				<?xml version="1.0"?>
				<s:Envelope
				xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
				s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
				<s:Body>
				<u:actionNameResponse xmlns:u="urn:schemas-upnp-org:service:serviceType:v">	---> Only these fields are used in parsing.
				<argumentName>out arg value</argumentName>									--->
				</u:actionNameResponse>
				</s:Body>
				</s:Envelope>
				*/
				parser = factory.newDocumentBuilder();
				doc = parser.parse(conn.getInputStream());
				actionResponseNode = doc.getElementsByTagName(action.getActionName() + "Response");
				if ( actionResponseNode != null ) {
					actionResponseElement = (Element)actionResponseNode.item(0);
					NodeList outArgs = actionResponseElement.getChildNodes();
					if ( outArgs != null ) {
						for(int inx = 0 ; inx < outArgs.getLength() ; inx++ ) {
							Node node = outArgs.item(inx);
							if ( node.getNodeType() == Node.ELEMENT_NODE ) {
								String argName = node.getNodeName();
								String argValue = UPnPUtils.escapeXML(node.getFirstChild().getNodeValue());
								UPnPStateVariable outArg = action.getOutArgument(argName);
								outArg.setValue(argValue);
							}
						}
					}
				} else {
					throw new Exception("No action response[" + action.getActionName() + "Response" + "]");
				}
			} catch(ParserConfigurationException e) {
				e.printStackTrace();
				throw e;
			} catch(SAXException ex) {
				ex.printStackTrace();
				throw ex;
			}
		} else {
			throw new Exception(conn.getResponseMessage());
		}
	}
	
}

package com.elevenquest.sol.upnp.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.elevenquest.sol.upnp.common.DefaultConfig;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.exception.ProcessableException;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;

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
	    Logger.println(Logger.DEBUG, "Start sending " + action.getActionName() + " request");
	    try {
			// 1. find target URL.
			findTargetUrl();
			// 2. Open connection and make Http Header.
		    conn = (HttpURLConnection)url.openConnection();   
		    conn.setRequestMethod("POST");   
		    conn.setDoOutput( true );   
		    conn.setDoInput( true );    
			conn.setRequestProperty("HOST", this.action.getService().getDevice().getBaseHost() );
		    conn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"" );   
		    conn.setRequestProperty("USER-AGENT", DefaultConfig.ID_UPNP_DISCOVERY_SERVER_VALUE );
			if ( this.action.getService().getDevice().getAuthorizationStr() != null )
				conn.setRequestProperty("Authorization", "Basic " + this.action.getService().getDevice().getAuthorizationStr() );

			// 3. Create SOAP body
		    String reqStr = makeSoapBody();  
		    int len = reqStr.length();   
		    conn.setRequestProperty("Content-Length", Integer.toString( len ) );   
		    conn.setRequestProperty("SOAPAction", "\"" + action.getService().getServiceType() + "#" + action.getActionName() + "\"" );   
		    
		    Map<String,List<String>> props = conn.getRequestProperties();
		    Iterator<String> keyIter = props.keySet().iterator();
		    while ( keyIter.hasNext() ) {
		    	String key = keyIter.next();
		    	List<String> values = props.get(key);
		    	for ( int cnt = 0 ; cnt < values.size() ; cnt++ ) {
		    		Logger.println(Logger.DEBUG, "--->[" + key + "]:[" + values.get(cnt) + "]" );
		    	}
		    }
		    Logger.println(Logger.DEBUG, reqStr);
		    
		    // 4. Flush
			conn.connect();       
			requestWriter = new OutputStreamWriter( conn.getOutputStream() );    
			requestWriter.write( reqStr, 0, len );   
			requestWriter.flush();
			
			// 5. Retrieve Response & Parsing.
			try {
				parseReponse();
			} catch (ActionError actionError) {
				throw new ProcessableException(actionError.getMessage(), actionError);
			} catch (Throwable unhandledError) {
				unhandledError.printStackTrace();
				throw new Exception(unhandledError.getMessage());
			}
		    
		} finally {
	    	if ( responseReader != null ) try { responseReader.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    	if ( requestWriter != null ) try { requestWriter.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    	if ( conn != null ) try { conn.disconnect(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	    }
	}
	
	private void findTargetUrl() throws MalformedURLException {
		this.targetURL = this.action.getService().getDevice().getAbsoluteURL(this.action.getService().getControlUrl());
		Logger.println(Logger.DEBUG, "-----> action url:" + this.targetURL );
	    this.url = new URL( targetURL );   
	}
	
	private String makeSoapBody() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>\n")
		.append("<s:Envelope\n")
		.append("xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"\n")
		.append("s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n")
		.append("<s:Body>\n")
		.append("<u:" + action.getActionName() + " xmlns:u=\"" + action.getService().getServiceType() + "\">\n");
		Logger.println(Logger.DEBUG, "ACTION :" + action.getActionName() );
		for ( UPnPStateVariable arg : action.getInArguments() ) {
			Logger.println(Logger.DEBUG, "IN ARG :" + arg.getArgumentName() + ":" + arg.getValue());
			sb.append("<" + arg.getArgumentName() + ">" + ( ( arg.getValue() != null ) ? arg.getValue().toString() : "" ) + "</" + arg.getArgumentName() + ">\n");
		}
		sb.append("</u:" + action.getActionName() + ">\n")
		.append("</s:Body>\n")
		.append("</s:Envelope>");
		return sb.toString();
	}
	
	private void parseReponse() throws Throwable {

		/*
		HTTP/1.0 200 OK				---> only this field used.
		CONTENT-TYPE: text/xml; charset="utf-8"
		DATE: when response was generated
		SERVER: OS/version UPnP/1.1 product/version
		CONTENT-LENGTH: bytes in body
		*/
		if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
			InputStream is = null;
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

				factory.setNamespaceAware(true);
				parser = factory.newDocumentBuilder();
				is = conn.getInputStream();
				// For debuging. We want to see the response of dlna server.
				{
					Map<String,List<String>> props = conn.getHeaderFields();
					Iterator<String> keyIter = props.keySet().iterator();
					while( keyIter.hasNext() ) {
						String key = keyIter.next();
						Logger.println(Logger.DEBUG, "----> Response:[" + key + "]:[" + props.get(key));
					}
				}
				{
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int length = 0;
					try {
						while( (length = is.read(buffer, 0, 1024)) >= 0) {
							baos.write(buffer, 0, length);
						}
						baos.flush();
					} catch ( IOException ioe ) {
						ioe.printStackTrace();
					}
					is = new ByteArrayInputStream(baos.toByteArray());
					String xmlText = new String(baos.toByteArray());
					Logger.println(Logger.DEBUG, "xml text:" + ( ( xmlText.length() ) > 2000 ? xmlText.substring(0,2000) : xmlText ) );
				}
				doc = parser.parse(is);
				actionResponseNode = doc.getElementsByTagNameNS("*",action.getActionName() + "Response");
				if ( actionResponseNode != null ) {
					actionResponseElement = (Element)actionResponseNode.item(0);
					Logger.println(Logger.DEBUG, "[Action Executor] actionResponseElement:" + actionResponseElement.getNodeName());
					NodeList outArgs = actionResponseElement.getChildNodes();
					if ( outArgs != null ) {
						for(int inx = 0 ; inx < outArgs.getLength() ; inx++ ) {
							Node node = outArgs.item(inx);
							if ( node.getNodeType() == Node.ELEMENT_NODE ) {
								String argName = node.getNodeName();
								String argValue = ( node.getFirstChild() != null ) ? node.getFirstChild().getNodeValue() : ""; //UPnPUtils.unescapeXML(node.getFirstChild().getNodeValue());
								UPnPStateVariable outArg = action.getOutArgument(argName);
								outArg.setValue(argValue);
								Logger.println(Logger.DEBUG, "[Action Executor] Response property:[" + outArg.getArgumentName() + "] Response value:[" + ( ( argValue != null && argValue.length() > 1000 ) ? argValue.substring(0,1000) : argValue )  + "]");
							}
						}
					} else {
						Logger.println(Logger.WARNING,"[Action Executor] There is no response element.");
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
			Logger.println(Logger.DEBUG, "---> response code:" + conn.getResponseCode());
			BufferedReader br = null;
			int errorCode = conn.getResponseCode();
			try {
				// 1. print HTTP header.
				Map<String,List<String>> props = conn.getHeaderFields();
				Iterator<String> iterKey = props.keySet().iterator();
				while( iterKey.hasNext() ) {
					String key = iterKey.next();
					Logger.println(Logger.ERROR, "---->> ERROR:[" + key + "]:" + props.get(key));
				}
				// 2. print body.
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String aLine = null;
				while( ( aLine = br.readLine() ) != null ) {
					Logger.println(Logger.ERROR, "---->> ERROR:" + aLine );
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			} finally {
				try { 
					if ( br != null ) br.close();
				} catch ( Exception ie ) {}
			}
			
			if (errorCode != 200)
				throw new ActionError(errorCode);
		}
	}
	
}

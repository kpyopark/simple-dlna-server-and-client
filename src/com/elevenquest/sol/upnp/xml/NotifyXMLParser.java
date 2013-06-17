package com.elevenquest.sol.upnp.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.description.ServiceDescription;
import com.elevenquest.sol.upnp.gena.EventNotify;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPAllowedValueRange;
import com.elevenquest.sol.upnp.model.UPnPDataType;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;

public class NotifyXMLParser {

	EventNotify event = null;
	InputStream xmlInputStream = null;

	public NotifyXMLParser(EventNotify event, InputStream is) {
		this.event = event;
		this.xmlInputStream = is;
		printInputStream();
	}

	private void printInputStream() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		try {
			while( (length = xmlInputStream.read(buffer, 0, 1024)) >= 0) {
				baos.write(buffer, 0, length);
			}
			baos.flush();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		this.xmlInputStream = new ByteArrayInputStream(baos.toByteArray());
		Logger.println(Logger.DEBUG, "xml text:" + new String(baos.toByteArray()));
	}

	public void execute() {
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//dbf.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlInputStream);
			doc.getDocumentElement().normalize();
			Logger.println(Logger.DEBUG, "Root element "
					+ doc.getDocumentElement().getNodeName());
			
			NodeList nodeProperty = doc.getDocumentElement().getChildNodes();
			for ( int cnt = 0; cnt < nodeProperty.getLength() ; cnt++) {
				Logger.println(Logger.DEBUG, "[Notify Parser] child of root element:" + nodeProperty.item(cnt));
				Node fstNodeProperty = nodeProperty.item(cnt);
				Logger.println(Logger.DEBUG, "fstNodeProperty node type:" + fstNodeProperty.getNodeType());
				if (fstNodeProperty.getNodeType() == Node.ELEMENT_NODE) {
					Element propertyElement = (Element)fstNodeProperty;
					NodeList propertyChildren = propertyElement.getChildNodes();
					for ( int subcnt = 0 ; subcnt < propertyChildren.getLength() ; subcnt++ ) {
						Node propertyChild = propertyChildren.item(subcnt);
						if ( propertyChild.getNodeType() == Node.ELEMENT_NODE ) {
							String propertyName =  propertyChild.getNodeName();
							event.setPropertyNameAndValue(propertyName, XMLParserUtility.getFirstNodeValue(propertyElement, propertyName));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		EventNotify event = new EventNotify();
		String xml = "<e:propertyset xmlns:e=\"urn:schemas-upnp-org:event-1-0\">\n\r" +
"<e:property>\n\r" +
"<SystemUpdateID>0</SystemUpdateID>\n\r" +
"</e:property>\n\r" +
"<e:property>\n\r" +
"<ContainerUpdateIDs></ContainerUpdateIDs>\n\r" +
"</e:property>\n\r" +
"</e:propertyset>\n\r";
		ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
		NotifyXMLParser parser = new NotifyXMLParser(event, is);
		parser.execute();
		Enumeration<String> propertyNames = event.getPropertyNameList();
		while( propertyNames.hasMoreElements() ) {
			String propertyName = propertyNames.nextElement();
			Logger.println(Logger.INFO, "[Notify Parser] property name:" + propertyName + " value:" + event.getPropertyValue(propertyName) );
		}
	}
}

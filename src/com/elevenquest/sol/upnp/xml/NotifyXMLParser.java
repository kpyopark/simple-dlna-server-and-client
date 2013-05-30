package com.elevenquest.sol.upnp.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
		//Logger.println(Logger.DEBUG, "xml text:" + new String(baos.toByteArray()));
	}

	public void execute() {
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlInputStream);
			doc.getDocumentElement().normalize();
			Logger.println(Logger.DEBUG, "Root element "
					+ doc.getDocumentElement().getNodeName());

			NodeList nodeProperty = doc.getElementsByTagName("property");
			if (nodeProperty.getLength() > 0) {
				Node fstNodeProperty = nodeProperty.item(0);
				if (fstNodeProperty.getNodeType() == Node.ELEMENT_NODE) {
					Element propertyElement = (Element)fstNodeProperty;
					NodeList propertyChildren = propertyElement.getChildNodes();
					for ( int cnt = 0 ; cnt < propertyChildren.getLength() ; cnt++ ) {
						Node propertyChild = propertyChildren.item(cnt);
						if ( propertyChild.getNodeType() == Node.ELEMENT_NODE ) {
							event.setPropertyName(propertyChild.getNodeName());
							event.setPropertyValue(XMLParserUtility.getFirstNodeValue(propertyElement, event.getPropertyName()));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

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
import com.elevenquest.sol.upnp.description.DeviceDescription;
import com.elevenquest.sol.upnp.description.ImageElementInDDS;
import com.elevenquest.sol.upnp.description.ServiceElementInDDS;
import com.elevenquest.sol.upnp.model.UPnPDevice;

public class DDSXMLParser {

	DeviceDescription description = null;

	InputStream xmlInputStream = null;

	public DDSXMLParser(DeviceDescription desc, InputStream is) {
		this.description = desc;
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

	public UPnPDevice getDeviceDescription() {
		return this.getDeviceDescription();
	}

	public void execute() {
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlInputStream);
			doc.getDocumentElement().normalize();
			Logger.println(Logger.DEBUG, "Base URI :" + doc.getBaseURI());
			Logger.println(Logger.DEBUG, "Root element "
					+ doc.getDocumentElement().getNodeName());

			/* Spec Version */
			NodeList nodeLstSpecVer = doc.getElementsByTagName("specVersion");
			Node fstNodeSpecVer = nodeLstSpecVer.item(0);

			if (fstNodeSpecVer.getNodeType() == Node.ELEMENT_NODE) {
				/* major */
				description.setSpecMajor(XMLParserUtility.getFirstNodeValue((Element)fstNodeSpecVer, "major"));
				/* minor */
				description.setSpecMajor(XMLParserUtility.getFirstNodeValue((Element)fstNodeSpecVer, "minor"));
			}
			/* Spec Version */

			/* Device */
			NodeList nodeDevice = doc.getElementsByTagName("device");
			Node fstnodeDevice = nodeDevice.item(0);

			if (fstnodeDevice.getNodeType() == Node.ELEMENT_NODE) {
				Element deviceElement = (Element) fstnodeDevice;
				/* deviceType */
				description.setDeviceType(XMLParserUtility.getFirstNodeValue(deviceElement, "deviceType"));
				/* friendlyName */
				description.setFriendlyName(XMLParserUtility.getFirstNodeValue(deviceElement, "friendlyName"));
				/* manufacturer */
				description.setManufacturerName(XMLParserUtility.getFirstNodeValue(deviceElement, "manufacturer"));
				/* manufacturerURL */
				description.setManufacturerUrl(XMLParserUtility.getFirstNodeValue(deviceElement, "manufacturerURL"));
				/* modelDescription */
				description.setModelDescription(XMLParserUtility.getFirstNodeValue(deviceElement, "modelDescription"));
				/* modelName */
				description.setModelName(XMLParserUtility.getFirstNodeValue(deviceElement, "modelName"));
				/* modelNumber */
				description.setModelNumber(XMLParserUtility.getFirstNodeValue(deviceElement, "modelNumber"));
				/* modelURL */
				description.setModelUrl(XMLParserUtility.getFirstNodeValue(deviceElement, "modelURL"));
				/* serialNumber */
				description.setModelSerial(XMLParserUtility.getFirstNodeValue(deviceElement, "serialNumber"));
				/* UDN */
				description.setUdn(XMLParserUtility.getFirstNodeValue(deviceElement, "UDN"));
				/* UPC */
				description.setUpc(XMLParserUtility.getFirstNodeValue(deviceElement, "UPC"));
				/* iconList */
				NodeList nodeLsticon = doc.getElementsByTagName("icon");
				Logger.println(Logger.DEBUG, "--Information of all icons start --");

				for (int s = 0; s < nodeLsticon.getLength(); s++) {
					Node fstNode = nodeLsticon.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						ImageElementInDDS imageInfo = new ImageElementInDDS();
						Element iconfstElmnt = (Element) fstNode;
						/* mime type */
						imageInfo.setMimeType(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "mimetype"));
						/* width */
						imageInfo.setWidth(Integer.parseInt(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "width")));
						/* height */
						imageInfo.setHeight(Integer.parseInt(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "height")));
						/* depth */
						imageInfo.setDepth(Integer.parseInt(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "depth")));
						/* url */
						imageInfo.setUrl(description.getDevice().getAbsoluteURL(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "url")));
						description.addImage(imageInfo);
					}
					
				}
				Logger.println(Logger.DEBUG, "--Information of all icons end--");
				/* iconList */
				/* serviceList */
				NodeList serviceLst = doc.getElementsByTagName("service");
				System.out
						.println("--Information of all sevice lists start --");

				for (int s = 0; s < serviceLst.getLength(); s++) {
					Node fstNode = serviceLst.item(s);

					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						ServiceElementInDDS service = new ServiceElementInDDS();
						Element iconfstElmnt = (Element) fstNode;
						/* service type */
						service.setServiceType(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "serviceType"));
						/* serviceId */
						service.setServiceId(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "serviceId"));
						/* SCPDURL */
						service.setScpdUrl(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "SCPDURL"));
						/* controlURL */
						service.setControlUrl(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "controlURL"));
						/* url */
						service.setEventsubUrl(XMLParserUtility.getFirstNodeValue(iconfstElmnt, "eventSubURL"));
						description.addService(service);
					}
				}
				Logger.println(Logger.DEBUG, "--Information of all icons end--");
				/* serviceList */
				
				// TODO : We wouldn't process embedded devices yet. Someone help us to process them.
				/* deviceList */
				/* PresentationURL */
				description.setPresentationURL(XMLParserUtility.getFirstNodeValue(deviceElement, "PresentationURL"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
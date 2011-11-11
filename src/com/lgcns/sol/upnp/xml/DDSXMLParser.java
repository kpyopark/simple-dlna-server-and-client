package com.lgcns.sol.upnp.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lgcns.sol.upnp.description.DeviceDescription;
import com.lgcns.sol.upnp.description.ImageElementInDDS;
import com.lgcns.sol.upnp.description.ServiceElementInDDS;
import com.lgcns.sol.upnp.model.UPnPDevice;

public class DDSXMLParser {

	DeviceDescription description = null;

	InputStream xmlInputStream = null;

	public DDSXMLParser(DeviceDescription desc, InputStream is) {
		this.description = desc;
		this.xmlInputStream = is;
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
			System.out.println("Root element "
					+ doc.getDocumentElement().getNodeName());

			/* Spec Version */
			NodeList nodeLstSpecVer = doc.getElementsByTagName("specVersion");
			Node fstNodeSpecVer = nodeLstSpecVer.item(0);

			if (fstNodeSpecVer.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNodeSpecVer;
				/* major */
				NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("major");
				if (fstNmElmntLst.getLength() > 0) {
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = fstNmElmnt.getChildNodes();
					description.setSpecMajor(((Node) fstNm.item(0)).getNodeValue());
					//System.out.println("major : " + SpecVerMaj);
				}
				/* minor */
				NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("minor");
				if (lstNmElmntLst.getLength() > 0) {
					Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
					NodeList lstNm = lstNmElmnt.getChildNodes();
					description.setSpecMinor(((Node) lstNm.item(0)).getNodeValue());
					//System.out.println("minor : " + SpecVerMin);
				}
			}
			/* Spec Version */

			/* Device */
			NodeList nodeDevice = doc.getElementsByTagName("device");
			Node fstnodeDevice = nodeDevice.item(0);

			if (fstnodeDevice.getNodeType() == Node.ELEMENT_NODE) {
				/* deviceType */
				Element fstElmnt = (Element) fstnodeDevice;
				NodeList fstNmElmntLst = fstElmnt
						.getElementsByTagName("deviceType");
				if (fstNmElmntLst.getLength() > 0) {
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = fstNmElmnt.getChildNodes();
					description.setDeviceType(((Node) fstNm.item(0)).getNodeValue());
					//System.out.println("deviceType : " + deviceType);
				}
				/* deviceType */
				/* friendlyName */
				Element secElmnt = (Element) fstnodeDevice;
				NodeList secNmElmntLst = secElmnt
						.getElementsByTagName("friendlyName");
				if (secNmElmntLst.getLength() > 0) {
					Element secNmElmnt = (Element) secNmElmntLst.item(0);
					NodeList secNm = secNmElmnt.getChildNodes();
					description.setFriendlyName(((Node) secNm.item(0)).getNodeValue());
					//System.out.println("friendlyName : " + friendlyName);
				}
				/* friendlyName */
				/* manufacturer */
				Element trdElmnt = (Element) fstnodeDevice;
				NodeList trdNmElmntLst = trdElmnt
						.getElementsByTagName("manufacturer");
				if (trdNmElmntLst.getLength() > 0) {
					Element trdNmElmnt = (Element) trdNmElmntLst.item(0);
					NodeList trdNm = trdNmElmnt.getChildNodes();
					description.setManufacturerName(((Node) trdNm.item(0)).getNodeValue());
					//System.out.println("manufacturer : " + manufacturer);
				}
				/* manufacturer */
				/* manufacturerURL */
				Element frthElmnt = (Element) fstnodeDevice;
				NodeList frthNmElmntLst = frthElmnt
						.getElementsByTagName("manufacturerURL");
				if (frthNmElmntLst.getLength() > 0) {
					Element trtfNmElmnt = (Element) frthNmElmntLst.item(0);
					NodeList frthNm = trtfNmElmnt.getChildNodes();
					description.setManufacturerUrl(((Node) frthNm.item(0)).getNodeValue());
					//System.out.println("manufacturerURL : " + manufacturerURL);
				}
				/* manufacturerURL */
				/* modelDescription */
				Element fifthElmnt = (Element) fstnodeDevice;
				NodeList fifthNmElmntLst = fifthElmnt
						.getElementsByTagName("modelDescription");
				if (fifthNmElmntLst.getLength() > 0) {
					Element fifthNmElmnt = (Element) fifthNmElmntLst.item(0);
					NodeList fifthNm = fifthNmElmnt.getChildNodes();
					description.setModelDescription(((Node) fifthNm.item(0)).getNodeValue());
				}
				/* modelDescription */
				/* modelName */
				Element sixthElmnt = (Element) fstnodeDevice;
				NodeList sixthNmElmntLst = sixthElmnt
						.getElementsByTagName("modelName");
				if (sixthNmElmntLst.getLength() > 0) {
					Element sixthNmElmnt = (Element) sixthNmElmntLst.item(0);
					NodeList sixthNm = sixthNmElmnt.getChildNodes();
					description.setModelName(((Node) sixthNm.item(0)).getNodeValue());
				}
				/* modelName */
				/* modelNumber */
				Element sevthElmnt = (Element) fstnodeDevice;
				NodeList sevthNmElmntLst = sevthElmnt
						.getElementsByTagName("modelNumber");
				if (sevthNmElmntLst.getLength() > 0) {
					Element sevthNmElmnt = (Element) sevthNmElmntLst.item(0);
					NodeList sevthNm = sevthNmElmnt.getChildNodes();
					description.setModelNumber(((Node) sevthNm.item(0)).getNodeValue());
				}
				/* modelNumber */
				/* modelURL */
				Element eigthElmnt = (Element) fstnodeDevice;
				NodeList eigthNmElmntLst = eigthElmnt
						.getElementsByTagName("modelURL");
				if (eigthNmElmntLst.getLength() > 0) {
					Element eigthNmElmnt = (Element) eigthNmElmntLst.item(0);
					NodeList eigthNm = eigthNmElmnt.getChildNodes();
					description.setModelUrl(((Node) eigthNm.item(0)).getNodeValue());
				}
				/* modelURL */
				/* serialNumber */
				Element ninthElmnt = (Element) fstnodeDevice;
				NodeList ninthNmElmntLst = ninthElmnt
						.getElementsByTagName("serialNumber");
				if (ninthNmElmntLst.getLength() > 0) {
					Element ninthNmElmnt = (Element) ninthNmElmntLst.item(0);
					NodeList ninthNm = ninthNmElmnt.getChildNodes();
					description.setModelSerial(((Node) ninthNm.item(0)).getNodeValue());
				}
				/* serialNumber */

				/* UDN */
				Element tenthElmnt = (Element) fstnodeDevice;
				NodeList tenthNmElmntLst = tenthElmnt
						.getElementsByTagName("UDN");
				if (tenthNmElmntLst.getLength() > 0) {
					Element tenthNmElmnt = (Element) tenthNmElmntLst.item(0);
					NodeList tenthNm = tenthNmElmnt.getChildNodes();
					description.setUdn(((Node) tenthNm.item(0)).getNodeValue());
				}
				/* UDN */
				/* UPC */
				Element elevnthElmnt = (Element) fstnodeDevice;
				NodeList elevnthNmElmntLst = elevnthElmnt
						.getElementsByTagName("UPC");
				if (elevnthNmElmntLst.getLength() > 0) {
					Element elevnthNmElmnt = (Element) elevnthNmElmntLst
							.item(0);
					NodeList elevnthNm = elevnthNmElmnt.getChildNodes();
					description.setUpc(((Node) elevnthNm.item(0)).getNodeValue());
				}
				/* UPC */
				/* iconList */
				NodeList nodeLsticon = doc.getElementsByTagName("icon");
				System.out.println("--Information of all icons start --");

				for (int s = 0; s < nodeLsticon.getLength(); s++) {
					Node fstNode = nodeLsticon.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						ImageElementInDDS imageInfo = new ImageElementInDDS();
						Element iconfstElmnt = (Element) fstNode;
						/* mime type */
						NodeList iconfstNmElmntLst = iconfstElmnt
								.getElementsByTagName("mimeType");
						if (iconfstNmElmntLst.getLength() > 0) {
							Element iconfstNmElmnt = (Element) iconfstNmElmntLst
									.item(0);
							NodeList iconfstNm = iconfstNmElmnt.getChildNodes();
							imageInfo.setMimeType(((Node) iconfstNm.item(0)).getNodeValue());
						}

						/* width */
						NodeList icon2ndElmntLst = iconfstElmnt
								.getElementsByTagName("width");
						if (icon2ndElmntLst.getLength() > 0) {
							Element icon2ndNmElmnt = (Element) icon2ndElmntLst
									.item(0);
							NodeList icon2ndNm = icon2ndNmElmnt.getChildNodes();
							try {
							imageInfo.setWidth(Integer.parseInt(((Node) icon2ndNm.item(0))
									.getNodeValue()));
							} catch ( Exception e ) {
								e.printStackTrace();
							}
						}

						/* height */
						NodeList icon3rdElmntLst = iconfstElmnt
								.getElementsByTagName("height");
						if (icon3rdElmntLst.getLength() > 0) {
							Element icon3rdNmElmnt = (Element) icon3rdElmntLst
									.item(0);
							NodeList icon3rdNm = icon3rdNmElmnt.getChildNodes();
							imageInfo.setHeight(Integer.parseInt(((Node) icon3rdNm.item(0)).getNodeValue()));
						}

						/* depth */
						NodeList icon4thElmntLst = iconfstElmnt
								.getElementsByTagName("depth");
						if (icon4thElmntLst.getLength() > 0) {
							Element icon4thNmElmnt = (Element) icon4thElmntLst
									.item(0);
							NodeList icon4thNm = icon4thNmElmnt.getChildNodes();
							imageInfo.setDepth(Integer.parseInt(((Node) icon4thNm.item(0)).getNodeValue()));
						}

						/* url */
						NodeList icon5thElmntLst = iconfstElmnt
								.getElementsByTagName("url");
						if (icon5thElmntLst.getLength() > 0) {
							Element icon5thNmElmnt = (Element) icon5thElmntLst
									.item(0);
							NodeList icon5thNm = icon5thNmElmnt.getChildNodes();
							imageInfo.setUrl(((Node) icon5thNm.item(0)).getNodeValue());
						}
						description.addImage(imageInfo);
					}
					
				}
				System.out.println("--Information of all icons end--");
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
						NodeList iconfstNmElmntLst = iconfstElmnt
								.getElementsByTagName("serviceType");
						if (iconfstNmElmntLst.getLength() > 0) {
							Element iconfstNmElmnt = (Element) iconfstNmElmntLst
									.item(0);
							NodeList iconfstNm = iconfstNmElmnt.getChildNodes();
							service.setServiceType(((Node) iconfstNm.item(0)).getNodeValue());
						}

						/* serviceId */
						NodeList icon2ndElmntLst = iconfstElmnt
								.getElementsByTagName("serviceId");
						if (icon2ndElmntLst.getLength() > 0) {
							Element icon2ndNmElmnt = (Element) icon2ndElmntLst
									.item(0);
							NodeList icon2ndNm = icon2ndNmElmnt.getChildNodes();
							service.setServiceId(((Node) icon2ndNm.item(0)).getNodeValue());
						}

						/* SCPDURL */
						NodeList icon3rdElmntLst = iconfstElmnt
								.getElementsByTagName("SCPDURL");
						if (icon3rdElmntLst.getLength() > 0) {
							Element icon3rdNmElmnt = (Element) icon3rdElmntLst
									.item(0);
							NodeList icon3rdNm = icon3rdNmElmnt.getChildNodes();
							service.setScpdUrl(((Node) icon3rdNm.item(0)).getNodeValue());
						}

						/* controlURL */
						NodeList icon4thElmntLst = iconfstElmnt
								.getElementsByTagName("controlURL");
						if (icon4thElmntLst.getLength() > 0) {
							Element icon4thNmElmnt = (Element) icon4thElmntLst
									.item(0);
							NodeList icon4thNm = icon4thNmElmnt.getChildNodes();
							service.setControlUrl(((Node) icon4thNm.item(0)).getNodeValue());
						}

						/* url */
						NodeList icon5thElmntLst = iconfstElmnt
								.getElementsByTagName("eventSubURL");
						if (icon5thElmntLst.getLength() > 0) {
							Element icon5thNmElmnt = (Element) icon5thElmntLst
									.item(0);
							NodeList icon5thNm = icon5thNmElmnt.getChildNodes();
							service.setEventsubUrl(((Node) icon5thNm.item(0)).getNodeValue());
						}
						description.addService(service);
					}
				}
				System.out.println("--Information of all icons end--");
				/* serviceList */
				
				// TODO : We wouldn't process embedded devices yet. Someone help us to process them.
				/* deviceList */
				/* PresentationURL */
				Element twlvthElmnt = (Element) fstnodeDevice;
				NodeList twlvthNmElmntLst = twlvthElmnt
						.getElementsByTagName("PresentationURL");
				if (twlvthNmElmntLst.getLength() > 0) {
					Element twlvthNmElmnt = (Element) twlvthNmElmntLst.item(0);
					NodeList twlthNm = twlvthNmElmnt.getChildNodes();
					description.setPresentationURL(((Node) twlthNm.item(0)).getNodeValue());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
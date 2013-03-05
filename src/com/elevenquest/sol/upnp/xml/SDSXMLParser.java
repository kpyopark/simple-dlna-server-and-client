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

import com.elevenquest.sol.upnp.description.ServiceDescription;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPAllowedValueRange;
import com.elevenquest.sol.upnp.model.UPnPDataType;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;

public class SDSXMLParser {
	String SpecVerMaj;
	String SpecVerMin;

	ServiceDescription description = null;
	InputStream xmlInputStream = null;

	public SDSXMLParser(ServiceDescription desc, InputStream is) {
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
		System.out.println("xml text:" + new String(baos.toByteArray()));
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
			if (nodeLstSpecVer.getLength() > 0) {
				Node fstNodeSpecVer = nodeLstSpecVer.item(0);

				if (fstNodeSpecVer.getNodeType() == Node.ELEMENT_NODE) {
					Element specElement = (Element) fstNodeSpecVer;
					/* major */
					this.description.setSpecMajor(XMLParserUtility.getFirstNodeValue(specElement, "major"));
					/* minor */
					this.description.setSpecMinor(XMLParserUtility.getFirstNodeValue(specElement, "minor"));
				}
			}
			/* Spec Version */

			/* Device */
			NodeList nodeDevice = doc.getElementsByTagName("device");
			Node fstnodeDevice = nodeDevice.item(0);

			/* serviceStateTable */
			NodeList nodeLsticon = doc.getElementsByTagName("stateVariable");
			System.out
					.println("  --Information of all stateVariable list start --");

			for (int s = 0; s < nodeLsticon.getLength(); s++) {
				Node fstNode = nodeLsticon.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					UPnPStateVariable statVar = new UPnPStateVariable();
					Element stateVariableElement = (Element) fstNode;
					/* name */
					statVar.setName(XMLParserUtility.getFirstNodeValue(stateVariableElement, "name"));
					/* dataType */
					String upnpDataType = XMLParserUtility.getFirstNodeValue(stateVariableElement, "dataType");
					if ( upnpDataType != null ) {
						UPnPDataType type = UPnPDataType.getUPnPDataType(upnpDataType);
						if ( type != null ) {
							statVar.setType(type);
						} else {
							System.out.println("[ERROR] There is no matching UPnPDataType. You must define the new type[" + upnpDataType + "] in the UPnPDataType Class." );
						}
					}
					/* defaultValue */
					statVar.setDefaultValue(XMLParserUtility.getFirstNodeValue(stateVariableElement, "defaultValue"));
					/* allowdValueRange */
					System.out.println("   --Information of all allowdValueRange start --");
					NodeList childOfstateVariable = stateVariableElement.getElementsByTagName("allowedValueRange");
					for ( int count = 0; count < childOfstateVariable.getLength(); count++ ) {
						if (childOfstateVariable.item(count).getNodeType() == Node.ELEMENT_NODE) {
							Element allowedValueRange = (Element)childOfstateVariable.item(count);
							UPnPAllowedValueRange valueRange = new UPnPAllowedValueRange();
							/* MIN */
							valueRange.setMinimum(XMLParserUtility.getFirstNodeValue(allowedValueRange, "minimum"));
							/* MAX */
							valueRange.setMaximum(XMLParserUtility.getFirstNodeValue(allowedValueRange, "maximum"));
							/* STEP */
							valueRange.setStep(XMLParserUtility.getFirstNodeValue(allowedValueRange, "step"));
						}
					}
					/* allowed value */
					NodeList allowdValLsticon = doc
							.getElementsByTagName("allowdValueList");
					System.out
							.println("    --Information of all allowed value list start --");

					for (int r = 0; r < allowdValLsticon.getLength(); r++) {
						Node allowdValListNode = nodeLsticon.item(r);

						if (allowdValListNode.getNodeType() == Node.ELEMENT_NODE) {
							Element allowdValElmnt = (Element) allowdValListNode;

							/* allowedValue */
							String allowedValue = XMLParserUtility.getFirstNodeValue(allowdValElmnt, "allowdValue");
							if ( allowedValue != null && allowedValue.length() > 0 )
								statVar.addAllowedValue(allowedValue);
						}
					}
					this.description.addStateVariable(statVar);
					System.out
							.println("    --Information of all allowed value list end --");
				}
			}
			System.out
					.println("  --Information of all stateVariable list end--");
			/* serviceStateTable */

			/* ActionList */
			NodeList nodeActionList = doc.getElementsByTagName("action");
			System.out.println("--Information of Action List start --");

			for (int s = 0; s < nodeActionList.getLength(); s++) {
				Node fstNode = nodeActionList.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element actionNode = (Element)fstNode;
					UPnPAction action = new UPnPAction(this.description.getService());
					NodeList children = fstNode.getChildNodes();
					boolean hasActionName = false;
					for ( int actionNameIndex = 0 ; actionNameIndex < children.getLength() ; actionNameIndex++ ) {
						Node child = children.item(actionNameIndex);
						if ( child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("name") ) {
							hasActionName = true;
							action.setActionName(child.getFirstChild().getNodeValue());
						}
					}
					System.out.println("-----> action name:" + action.getActionName());
					if ( hasActionName ) {
						/* Arguement List */
						NodeList nodeArgList = actionNode.getElementsByTagName("argument");
						for (int q = 0; q < nodeArgList.getLength(); q++) {
							Node argNode = nodeArgList.item(q);

							if (argNode.getNodeType() == Node.ELEMENT_NODE) {
								UPnPStateVariable arg = new UPnPStateVariable();
								Element argElement = (Element) argNode;
								/* name */
								arg.setArgumentName(XMLParserUtility.getFirstNodeValue(argElement, "name"));
								/* direction */
								String direction = XMLParserUtility.getFirstNodeValue(argElement, "direction");
								if ( direction != null) {
									if ( direction.equalsIgnoreCase("in") ) {
										action.addInArgument(arg);
									} else {
										action.addOutArgument(arg);
									}
								}
								/* relatedStateVariable */
								String relatedStateVariableName = XMLParserUtility.getFirstNodeValue(argElement, "relatedStateVariable");
								if ( relatedStateVariableName != null ) {
									UPnPStateVariable relStatVar = this.description.getStateVariable(relatedStateVariableName);
									if ( relStatVar != null )
										arg.setRelatedStateVariable(relStatVar);
								}
								System.out.println("--------->" + arg.getArgumentName()+":" + direction + ":" + arg.getRelatedStateVariable().getName() );
							}
						}
					}
					/* Arguement List */
					this.description.addAction(action);
				}
			}
			/* ActionList */


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

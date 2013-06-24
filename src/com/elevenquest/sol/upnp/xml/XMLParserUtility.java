package com.elevenquest.sol.upnp.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.elevenquest.sol.upnp.common.Logger;

public class XMLParserUtility {
	
	public static String getFirstNodeValue(Element parentElement, String childTagName) {
		NodeList childNodeList = parentElement.getElementsByTagName(childTagName);
		if (childNodeList != null) {
			switch ( childNodeList.getLength() ) {
			case 0 :
				Logger.println(Logger.WARNING, "There is no matching child element[" + childTagName + "]" + " in parent element[" + parentElement.getTagName() + "]");
				break;
			default :
				if ( childNodeList.getLength() > 1 )
					Logger.println(Logger.INFO, "There are many childNodes [" + childNodeList.getLength() + "]");
				Element childFirstElement = (Element)childNodeList.item(0);
				NodeList valueNode = childFirstElement.getChildNodes();
				if ( valueNode != null ) {
					switch( valueNode.getLength() ) {
					case 0 :
						Logger.println(Logger.WARNING, "There is no value in child element[" + childTagName + "]" + " of parent element[" + parentElement.getTagName() + "]");
						break;
					case 1 :
					default :
						return valueNode.item(0).getNodeValue();
					}
				}
			}
		} else {
			Logger.println(Logger.WARNING, "There is no matching child element[" + childTagName + "]" + " in parent element[" + parentElement.getTagName() + "]");
		}
		return null;
	}
	
	public static String getFirstNodeAttibValue(Element parentElement, String childTagName, String attribName) {
		NodeList childNodeList = parentElement.getElementsByTagName(childTagName);
		if (childNodeList != null) {
			switch ( childNodeList.getLength() ) {
			case 0 :
				Logger.println(Logger.WARNING, "There is no matching child element[" + childTagName + "]" + " in parent element[" + parentElement.getTagName() + "]");
				break;
			default :
				if ( childNodeList.getLength() > 1 )
					Logger.println(Logger.INFO, "There are many childNodes [" + childNodeList.getLength() + "]");
				Element childFirstElement = (Element)childNodeList.item(0);
				return childFirstElement.getAttribute(attribName);
			}
		} else {
			Logger.println(Logger.WARNING, "There is no matching child element[" + childTagName + "]" + " in parent element[" + parentElement.getTagName() + "]");
		}
		return null;
	}
}

package com.elevenquest.sol.upnp.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParserUtility {
	
	static String getFirstNodeValue(Element parentElement, String childTagName) {
		NodeList childNodeList = parentElement.getElementsByTagName(childTagName);
		if (childNodeList != null) {
			switch ( childNodeList.getLength() ) {
			case 0 :
				System.out.println("[WARNING] There is no matching child element[" + childTagName + "]" + " in parent element[" + parentElement.getTagName() + "]");
				break;
			default :
				if ( childNodeList.getLength() > 1 )
					System.out.println("[INFO] There are many childNodes [" + childNodeList.getLength() + "]");
				Element childFirstElement = (Element)childNodeList.item(0);
				NodeList valueNode = childFirstElement.getChildNodes();
				if ( valueNode != null ) {
					switch( valueNode.getLength() ) {
					case 0 :
						System.out.println("[WARNING] There is no value in child element[" + childTagName + "]" + " of parent element[" + parentElement.getTagName() + "]");
						break;
					case 1 :
					default :
						return valueNode.item(0).getNodeValue();
					}
				}
			}
		} else {
			System.out.println("[WARNING] There is no matching child element[" + childTagName + "]" + " in parent element[" + parentElement.getTagName() + "]");
		}
		return null;
	}
}

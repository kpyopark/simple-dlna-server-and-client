package com.lgcns.sol.upnp.model;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DDSXMLParser {
	static String SpecVerMaj; 
	static String SpecVerMin;
	static String deviceType;
	static String friendlyName;
	static String manufacturer;
	static String manufacturerURL;
	static String modelDescription;
	static String modelName;
	static String modelNumber;
	static String modelURL;
	static String serialNumber;
	static String UDN;
	static String UPC;
	static String PresentationURL;

	File DeviceDescriptionXML;
	
	public void execute(){
		try {
			/*아래는 parser가 제대로 동작하는지 test를 위한 code 입니다. */
			/*실제 XML을 parsing 하기 위해서는 DeviceDescriptionXML 를 넣으면 됩니다. */
		  File file = new File("c:\\DeviceDescription.xml");
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		  System.out.println("Root element " + doc.getDocumentElement().getNodeName());

/*Spec Version*/
		  NodeList nodeLstSpecVer = doc.getElementsByTagName("specVersion");
		  Node fstNodeSpecVer = nodeLstSpecVer.item(0);

	      if (fstNodeSpecVer.getNodeType() == Node.ELEMENT_NODE) {
	    	  Element fstElmnt = (Element) fstNodeSpecVer;
	    	  /*major*/
	    	  NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("major");
	    	  if(fstNmElmntLst.getLength()>0)
	    	  {
		    	  Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		    	  NodeList fstNm = fstNmElmnt.getChildNodes();
			      SpecVerMaj = ((Node) fstNm.item(0)).getNodeValue();
		    	  System.out.println("major : "  + SpecVerMaj);
	    	  }
	    	  /*minor*/
		      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("minor");
		      if(lstNmElmntLst.getLength()>0)
		      {
			      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
			      NodeList lstNm = lstNmElmnt.getChildNodes();
			      SpecVerMin = ((Node) lstNm.item(0)).getNodeValue();
			      System.out.println("minor : " + SpecVerMin);
		      }
	      }		  
/*Spec Version*/		  

/*Device*/	      
		  NodeList nodeDevice = doc.getElementsByTagName("device");
		  Node fstnodeDevice = nodeDevice.item(0);

		  if (fstnodeDevice.getNodeType() == Node.ELEMENT_NODE) {
 /*deviceType*/
			  Element fstElmnt = (Element) fstnodeDevice;
			  NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("deviceType");
			  if(fstNmElmntLst.getLength()>0)
			  {
				  Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
				  NodeList fstNm = fstNmElmnt.getChildNodes();
				  deviceType = ((Node) fstNm.item(0)).getNodeValue();
		    	  System.out.println("deviceType : "  + deviceType);
			  }
 /*deviceType*/
 /*friendlyName*/
			  Element secElmnt = (Element) fstnodeDevice;
			  NodeList secNmElmntLst = secElmnt.getElementsByTagName("friendlyName");
			  if(secNmElmntLst.getLength()>0)
			  {
				  Element secNmElmnt = (Element) secNmElmntLst.item(0);
				  NodeList secNm = secNmElmnt.getChildNodes();
				  friendlyName = ((Node) secNm.item(0)).getNodeValue();
		    	  System.out.println("friendlyName : "  + friendlyName);
			  }
 /*friendlyName*/	    	  
 /*manufacturer*/
			  Element trdElmnt = (Element) fstnodeDevice;
			  NodeList trdNmElmntLst = trdElmnt.getElementsByTagName("manufacturer");
			  if(trdNmElmntLst.getLength()>0)
			  {
				  Element trdNmElmnt = (Element) trdNmElmntLst.item(0);
				  NodeList trdNm = trdNmElmnt.getChildNodes();
				  manufacturer = ((Node) trdNm.item(0)).getNodeValue();
		    	  System.out.println("manufacturer : "  + manufacturer);
			  }
 /*manufacturer*/
 /*manufacturerURL*/
			  Element frthElmnt = (Element) fstnodeDevice;
			  NodeList frthNmElmntLst = frthElmnt.getElementsByTagName("manufacturerURL");
			  if(frthNmElmntLst.getLength()>0)
			  {
				  Element trtfNmElmnt = (Element) frthNmElmntLst.item(0);
				  NodeList frthNm = trtfNmElmnt.getChildNodes();
				  manufacturerURL = ((Node) frthNm.item(0)).getNodeValue();
		    	  System.out.println("manufacturerURL : "  + manufacturerURL);
			  }
 /*manufacturerURL*/	    	  
 /*modelDescription*/
			  Element fifthElmnt = (Element) fstnodeDevice;
			  NodeList fifthNmElmntLst = fifthElmnt.getElementsByTagName("modelDescription");
			  if(fifthNmElmntLst.getLength()>0)
			  {
				  Element fifthNmElmnt = (Element) fifthNmElmntLst.item(0);
				  NodeList fifthNm = fifthNmElmnt.getChildNodes();
				  modelDescription = ((Node) fifthNm.item(0)).getNodeValue();
		    	  System.out.println("modelDescription : "  + modelDescription);
			  }
 /*modelDescription*/
 /*modelName*/	    
			  Element sixthElmnt = (Element) fstnodeDevice;
			  NodeList sixthNmElmntLst = sixthElmnt.getElementsByTagName("modelName");
			  if(sixthNmElmntLst.getLength()>0)
			  {
				  Element sixthNmElmnt = (Element) sixthNmElmntLst.item(0);
				  NodeList sixthNm = sixthNmElmnt.getChildNodes();
				  modelName = ((Node) sixthNm.item(0)).getNodeValue();
		    	  System.out.println("modelName : "  + modelName);
			  }
 /*modelName*/	    	  
 /*modelNumber*/
			  Element sevthElmnt = (Element) fstnodeDevice;
			  NodeList sevthNmElmntLst = sevthElmnt.getElementsByTagName("modelNumber");
			  if(sevthNmElmntLst.getLength()>0)
			  {
				  Element sevthNmElmnt = (Element) sevthNmElmntLst.item(0);
				  NodeList sevthNm = sevthNmElmnt.getChildNodes();
				  modelNumber = ((Node) sevthNm.item(0)).getNodeValue();
		    	  System.out.println("modelNumber : "  + modelNumber);
			  }
 /*modelNumber*/
 /*modelURL*/	
			  Element eigthElmnt = (Element) fstnodeDevice;
			  NodeList eigthNmElmntLst = eigthElmnt.getElementsByTagName("modelURL");
			  if(eigthNmElmntLst.getLength()>0)
			  {
				  Element eigthNmElmnt = (Element) eigthNmElmntLst.item(0);
				  NodeList eigthNm = eigthNmElmnt.getChildNodes();
				  modelURL = ((Node) eigthNm.item(0)).getNodeValue();
		    	  System.out.println("modelURL : "  + modelURL);
			  }
 /*modelURL*/	    	  
 /*serialNumber*/
			  Element ninthElmnt = (Element) fstnodeDevice;
			  NodeList ninthNmElmntLst = ninthElmnt.getElementsByTagName("serialNumber");
			  if(ninthNmElmntLst.getLength()>0)
			  {
				  Element ninthNmElmnt = (Element) ninthNmElmntLst.item(0);
				  NodeList ninthNm = ninthNmElmnt.getChildNodes();
				  serialNumber = ((Node) ninthNm.item(0)).getNodeValue();
		    	  System.out.println("serialNumber : "  + serialNumber);
			  }
 /*serialNumber*/

 /*UDN*/
			  Element tenthElmnt = (Element) fstnodeDevice;
			  NodeList tenthNmElmntLst = tenthElmnt.getElementsByTagName("UDN");
			  if(tenthNmElmntLst.getLength()>0)
			  {
				  Element tenthNmElmnt = (Element) tenthNmElmntLst.item(0);
				  NodeList tenthNm = tenthNmElmnt.getChildNodes();
				  UDN = ((Node) tenthNm.item(0)).getNodeValue();
		    	  System.out.println("UDN : "  + UDN);
			  }
 /*UDN*/	    	  
 /*UPC*/	   
			  Element elevnthElmnt = (Element) fstnodeDevice;
			  NodeList elevnthNmElmntLst = elevnthElmnt.getElementsByTagName("UPC");
			  if(elevnthNmElmntLst.getLength()>0)
			  {
				  Element elevnthNmElmnt = (Element) elevnthNmElmntLst.item(0);
				  NodeList elevnthNm = elevnthNmElmnt.getChildNodes();
				  UPC = ((Node) elevnthNm.item(0)).getNodeValue();
		    	  System.out.println("UPC : "  + UPC);
			  }
 /*UPC*/
 /*iconList*/
			  NodeList nodeLsticon = doc.getElementsByTagName("icon");
			  System.out.println("--Information of all icons start --");
			  
			  for (int s = 0; s < nodeLsticon.getLength(); s++) {
				    Node fstNode = nodeLsticon.item(s);
				    
				    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						  
					      Element iconfstElmnt = (Element) fstNode;
					      /*mime type*/
					      NodeList iconfstNmElmntLst = iconfstElmnt.getElementsByTagName("mimeType");
					      if(iconfstNmElmntLst.getLength()>0)
					      {
						      Element iconfstNmElmnt = (Element) iconfstNmElmntLst.item(0);
						      NodeList iconfstNm = iconfstNmElmnt.getChildNodes();
						      System.out.println("mimeType : "  + ((Node) iconfstNm.item(0)).getNodeValue());
					      }
					      
					      /*width*/
					      NodeList icon2ndElmntLst = iconfstElmnt.getElementsByTagName("width");
					      if(icon2ndElmntLst.getLength()>0)
					      {
						      Element icon2ndNmElmnt = (Element) icon2ndElmntLst.item(0);
						      NodeList icon2ndNm = icon2ndNmElmnt.getChildNodes();
						      System.out.println("width : " + ((Node) icon2ndNm.item(0)).getNodeValue());
					      }
					      
					      /*height*/
					      NodeList icon3rdElmntLst = iconfstElmnt.getElementsByTagName("height");
					      if(icon3rdElmntLst.getLength()>0)
					      {
						      Element icon3rdNmElmnt = (Element) icon3rdElmntLst.item(0);
						      NodeList icon3rdNm = icon3rdNmElmnt.getChildNodes();
						      System.out.println("height : " + ((Node) icon3rdNm.item(0)).getNodeValue());
					      }
					      
					      /*depth*/
					      NodeList icon4thElmntLst = iconfstElmnt.getElementsByTagName("depth");
					      if(icon4thElmntLst.getLength()>0)
					      {
						      Element icon4thNmElmnt = (Element) icon4thElmntLst.item(0);
						      NodeList icon4thNm = icon4thNmElmnt.getChildNodes();
						      System.out.println("depth :" + ((Node) icon4thNm.item(0)).getNodeValue());
					      }
					      
					      /*url*/
					      NodeList icon5thElmntLst = iconfstElmnt.getElementsByTagName("url");
					      if(icon5thElmntLst.getLength()>0)
					      {
						      Element icon5thNmElmnt = (Element) icon5thElmntLst.item(0);
						      NodeList icon5thNm = icon5thNmElmnt.getChildNodes();
						      System.out.println("url :" + ((Node) icon5thNm.item(0)).getNodeValue());
					      }
					    }
			  }
			  System.out.println("--Information of all icons end--");
 /*iconList*/	
 /*serviceList*/
			  NodeList serviceLst = doc.getElementsByTagName("service");
			  System.out.println("--Information of all sevice lists start --");
			  
			  for (int s = 0; s < serviceLst.getLength(); s++) {
				    Node fstNode = serviceLst.item(s);
				    
				    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						  
					      Element iconfstElmnt = (Element) fstNode;
					      /*mime type*/
					      NodeList iconfstNmElmntLst = iconfstElmnt.getElementsByTagName("serviceType");
					      if(iconfstNmElmntLst.getLength()>0)
					      {
						      Element iconfstNmElmnt = (Element) iconfstNmElmntLst.item(0);
						      NodeList iconfstNm = iconfstNmElmnt.getChildNodes();
						      System.out.println("serviceType : "  + ((Node) iconfstNm.item(0)).getNodeValue());
					      }
						      
					      /*width*/
					      NodeList icon2ndElmntLst = iconfstElmnt.getElementsByTagName("serviceTd");
					      if(icon2ndElmntLst.getLength()>0)
					      {
						      Element icon2ndNmElmnt = (Element) icon2ndElmntLst.item(0);
						      NodeList icon2ndNm = icon2ndNmElmnt.getChildNodes();
						      System.out.println("serviceTd : " + ((Node) icon2ndNm.item(0)).getNodeValue());
					      }
					      
					      /*height*/
					      NodeList icon3rdElmntLst = iconfstElmnt.getElementsByTagName("SCPDURL");
					      if(icon3rdElmntLst.getLength()>0)
					      {
						      Element icon3rdNmElmnt = (Element) icon3rdElmntLst.item(0);
						      NodeList icon3rdNm = icon3rdNmElmnt.getChildNodes();
						      System.out.println("SCPDURL : " + ((Node) icon3rdNm.item(0)).getNodeValue());
					      }
					      
					      /*depth*/
					      NodeList icon4thElmntLst = iconfstElmnt.getElementsByTagName("controlURL");
					      if(icon4thElmntLst.getLength()>0)
					      {
						      Element icon4thNmElmnt = (Element) icon4thElmntLst.item(0);
						      NodeList icon4thNm = icon4thNmElmnt.getChildNodes();
						      System.out.println("controlURL :" + ((Node) icon4thNm.item(0)).getNodeValue());
					      }
					      
					      /*url*/
					      NodeList icon5thElmntLst = iconfstElmnt.getElementsByTagName("eventSubURL");
					      if(icon5thElmntLst.getLength()>0)
					      {
						      Element icon5thNmElmnt = (Element) icon5thElmntLst.item(0);
						      NodeList icon5thNm = icon5thNmElmnt.getChildNodes();
						      System.out.println("eventSubURL :" + ((Node) icon5thNm.item(0)).getNodeValue());
					      }
					    }
			  }
			  System.out.println("--Information of all icons end--");
 /*serviceList*/
 /*deviceList*/
 /*deviceList*/
 /*PresentationURL*/
			  Element twlvthElmnt = (Element) fstnodeDevice;
			  NodeList twlvthNmElmntLst = twlvthElmnt.getElementsByTagName("PresentationURL");
			  if(twlvthNmElmntLst.getLength()>0)
			  {
				  Element twlvthNmElmnt = (Element) twlvthNmElmntLst.item(0);
				  NodeList twlthNm = twlvthNmElmnt.getChildNodes();
				  PresentationURL = ((Node) twlthNm.item(0)).getNodeValue();
		    	  System.out.println("PresentationURL : "  + PresentationURL);
			  }
 /*PresentationURL*/	    	  
    	  
		  }
		  } catch (Exception e) {
		    e.printStackTrace();
		  }

		 }
	public void setFile(File file){
		try{
			DeviceDescriptionXML = file;
			}
		catch (Exception e) {
		    e.printStackTrace();
		  }
	}
}
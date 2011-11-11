package com.lgcns.sol.upnp.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SDSXMLParser {
	static String SpecVerMaj; 
	static String SpecVerMin;
	
	File ServiceDescriptionXML;
	
	public void execute(){
		try {
			
			/*아래는 parser가 제대로 동작하는지 test를 위한 code 입니다. */
			/*실제 XML을 parsing 하기 위해서는 ServiceDescriptionXML 를 넣으면 됩니다. */
		  File file = new File("c:\\seviceDescription3.xml");
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		  System.out.println("Root element " + doc.getDocumentElement().getNodeName());

/*Spec Version*/
		  NodeList nodeLstSpecVer = doc.getElementsByTagName("specVersion");
		  if(nodeLstSpecVer.getLength()>0)
		  {
			  Node fstNodeSpecVer = nodeLstSpecVer.item(0);

		      if (fstNodeSpecVer.getNodeType() == Node.ELEMENT_NODE) {
		    	  Element fstElmnt = (Element) fstNodeSpecVer;
		    	  /*major*/
		    	  NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("major");
		    	  if(fstNmElmntLst.getLength()>0)
		    	  {
			    	  Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			    	  NodeList fstNm = fstNmElmnt.getChildNodes();
			    	  if(((Node) fstNm.item(0)).getNodeValue()!=null)
			    	  {
			    		  SpecVerMaj = ((Node) fstNm.item(0)).getNodeValue();
			    		  System.out.println("major : "  + SpecVerMaj);
			    	  }
		    	  }
		    	  
		    	  /*minor*/
			      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("minor");
			      if(lstNmElmntLst.getLength()>0)
			      {
				      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
				      NodeList lstNm = lstNmElmnt.getChildNodes();
				      if(((Node) lstNm.item(0)).getNodeValue()!=null)
				      {
					      SpecVerMin = ((Node) lstNm.item(0)).getNodeValue();
					      System.out.println("minor : " + SpecVerMin);
				      }
			      }
		      }		
		  }
/*Spec Version*/		  

/*Device*/	      
		  NodeList nodeDevice = doc.getElementsByTagName("device");
		  Node fstnodeDevice = nodeDevice.item(0);

		  /*ActionList*/
		  NodeList nodeActionList = doc.getElementsByTagName("action");
		  System.out.println("--Information of Action List start --");
		  
		  for (int s = 0; s < nodeActionList.getLength(); s++) {
			    Node fstNode = nodeActionList.item(s);
			    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

			    	/*Arguement List*/
			    	NodeList nodeArgList = doc.getElementsByTagName("argument");

			    	for (int q = 0; q < nodeArgList.getLength(); q++) {
					    Node argNode = nodeArgList.item(q);			    	
					    System.out.println("  --Information of argumentList"+q+" start --");
					    
					    if (argNode.getNodeType() == Node.ELEMENT_NODE) {
					    	 Element iconfstElmnt = (Element) argNode;
					    	
						      /*name*/
						      NodeList iconfstNmElmntLst = iconfstElmnt.getElementsByTagName("name");
						      Element iconfstNmElmnt = (Element) iconfstNmElmntLst.item(0);
						      NodeList iconfstNm = iconfstNmElmnt.getChildNodes();
						      if(((Node)iconfstNm.item(0)).getNodeValue()!=null)
						    	  System.out.println("name : "  + ((Node) iconfstNm.item(0)).getNodeValue());

						      /*direction*/
						      NodeList icon2ndElmntLst = iconfstElmnt.getElementsByTagName("direction");
						      Element icon2ndNmElmnt = (Element) icon2ndElmntLst.item(0);
						      NodeList icon2ndNm = icon2ndNmElmnt.getChildNodes();
						      if(((Node) icon2ndNm.item(0)).getNodeValue()!=null)
						    	  System.out.println("direction : " + ((Node) icon2ndNm.item(0)).getNodeValue());
						      
						      /*relatedStateVariable*/
						      NodeList icon3rdElmntLst = iconfstElmnt.getElementsByTagName("relatedStateVariable");
						      Element icon3rdNmElmnt = (Element) icon3rdElmntLst.item(0);
						      NodeList icon3rdNm = icon3rdNmElmnt.getChildNodes();
						      if(((Node) icon3rdNm.item(0)).getNodeValue()!=null)
						    	  System.out.println("relatedStateVariable : " + ((Node) icon3rdNm.item(0)).getNodeValue());					    	
					    }
					    System.out.println("  --Information of argumentList"+q+" end --");
			    	}
			    }
			    /*Arguement List*/
   	  		  }
		  /*ActionList*/
		  
		  /*serviceStateTable*/
		  NodeList nodeLsticon = doc.getElementsByTagName("stateVariable");
		  System.out.println("  --Information of all stateVariable list start --");
		  
		  for (int s = 0; s < nodeLsticon.getLength(); s++) {
			    Node fstNode = nodeLsticon.item(s);
			    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					  
				      Element iconfstElmnt = (Element) fstNode;
				      /*name*/
				      NodeList iconfstNmElmntLst = iconfstElmnt.getElementsByTagName("name");
				      if(iconfstNmElmntLst.getLength()>0)
				      {
					      Element iconfstNmElmnt = (Element) iconfstNmElmntLst.item(0);
					      NodeList iconfstNm = iconfstNmElmnt.getChildNodes();
					      if(((Node) iconfstNm.item(0)).getNodeValue()!=null)
					    	  System.out.println("name : "  + ((Node) iconfstNm.item(0)).getNodeValue());
				      }
				      
				      /*dataType*/
				      NodeList icon2ndElmntLst = iconfstElmnt.getElementsByTagName("dataType");
				      if(icon2ndElmntLst.getLength()>0)
				      {
					      Element icon2ndNmElmnt = (Element) icon2ndElmntLst.item(0);
					      NodeList icon2ndNm = icon2ndNmElmnt.getChildNodes();
					      if(((Node) icon2ndNm.item(0)).getNodeValue()!=null)
					    	  System.out.println("dataType : " + ((Node) icon2ndNm.item(0)).getNodeValue());
				      }
				      
				      /*defaultValue*/
				      NodeList icon3rdElmntLst = iconfstElmnt.getElementsByTagName("defaultValue");
				      if(icon3rdElmntLst.getLength()>0)
				      {
					      Element icon3rdNmElmnt = (Element) icon3rdElmntLst.item(0);
					      NodeList icon3rdNm = icon3rdNmElmnt.getChildNodes();
					      if(((Node) icon3rdNm.item(0)).getNodeValue()!=null)
					    	  System.out.println("defaultValue : " + ((Node) icon3rdNm.item(0)).getNodeValue());
				      }
				      
				      /*allowdValueRange*/
				      System.out.println("   --Information of all allowdValueRange start --");
				      
				      	  /*MIN*/	
					      NodeList range1ElmntLst = iconfstElmnt.getElementsByTagName("minimum");
					      if(range1ElmntLst.getLength()>0)
					      {
						      Element range1thNmElmnt = (Element) range1ElmntLst.item(0);
						      NodeList range1thNm = range1thNmElmnt.getChildNodes();
						      if(((Node) range1thNm.item(0)).getNodeValue()!=null)
						    	  System.out.println("minimum :" + ((Node) range1thNm.item(0)).getNodeValue());
					      }
					      
				      	  /*MAX*/	
					      NodeList range2ElmntLst = iconfstElmnt.getElementsByTagName("maximum");
					      if(range2ElmntLst.getLength()>0)
					      {
						      Element range2thNmElmnt = (Element) range2ElmntLst.item(0);
						      NodeList range2thNm = range2thNmElmnt.getChildNodes();
						      if(((Node) range2thNm.item(0)).getNodeValue()!=null)
						    	  System.out.println("maximum :" + ((Node) range2thNm.item(0)).getNodeValue());
					      }
					      
				      	  /*MIN*/	
					      NodeList range3ElmntLst = iconfstElmnt.getElementsByTagName("step");
					      if(range3ElmntLst.getLength()>0)
					      {
						      Element range3thNmElmnt = (Element) range3ElmntLst.item(0);
						      NodeList range3thNm = range3thNmElmnt.getChildNodes();
						      if(((Node) range3thNm.item(0)).getNodeValue()!=null)
						    	  System.out.println("step :" + ((Node) range3thNm.item(0)).getNodeValue());
					      }
				      /*allowdValueRange*/
					      
				      /*allowed value*/
					  NodeList allowdValLsticon = doc.getElementsByTagName("allowdValue");    
				      System.out.println("    --Information of all allowed value list start --");

					  for (int r = 0; r < allowdValLsticon.getLength(); r++) {
						    Node allowdValListNode = nodeLsticon.item(r);

						    if (allowdValListNode.getNodeType() == Node.ELEMENT_NODE){
						    	 Element allowdValElmnt = (Element) allowdValListNode;
							    	
							      /*name*/
							      NodeList AVfstNmElmntLst = allowdValElmnt.getElementsByTagName("allowdValue");
							      if(AVfstNmElmntLst.getLength()>0)
							      {
							    	  Element AAVfstNmElmnt = (Element) AVfstNmElmntLst.item(0);
								      NodeList AVfstNm = AAVfstNmElmnt.getChildNodes();
								      if(((Node) AVfstNm.item(0)).getNodeValue()!=null)
								    	  System.out.println("allowdValue : "  + ((Node) AVfstNm.item(0)).getNodeValue());
							      }
						    }
					  }
				      System.out.println("    --Information of all allowed value list end --");
				    }
		  }
		  System.out.println("  --Information of all stateVariable list end--");
		  /*serviceStateTable*/
	  
		} catch (Exception e) {
		    e.printStackTrace();
  	    }

	 }
	public void setFile(File file){
		try{
			ServiceDescriptionXML = file;
			}
		catch (Exception e) {
		    e.printStackTrace();
		  }
	}
}

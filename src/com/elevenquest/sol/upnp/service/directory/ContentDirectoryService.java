package com.elevenquest.sol.upnp.service.directory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.elevenquest.sol.upnp.action.ActionExecutor;
import com.elevenquest.sol.upnp.common.UPnPUtils;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPDataType;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;

/**
 * There are two type of services.
 * 
 * One is local service. If the device which contains this service is local one, you should call implemented java method for using service's functionality.
 * The other is remote service, If the device which contains this service is remote one, you should call SOAP action instead of java's method. 
 * 
 * BUT, we don't care now. In all cases, we use SOAP action.
 * later, I hope someone help us to enlarge these functionalities.
 *   
 * @author toheavener01@gmail.com
 *
 */
public class ContentDirectoryService extends UPnPService {

	public static final String NAMESPACE_UPNP_ELEMENT = "urn:schemas-upnp-org:metadata-1-0/upnp/";
	public static final String NAMESPACE_DC_ELEMENT = "http://purl.org/dc/elements/1.1/";
	
	public ContentDirectoryService(UPnPDevice device) {
		super(device);
	}
	
	// You should know the difference between argument's name and state variable's name.
	
	static final String SV_NAME_CDS_TRANSFERIDS = "TransferIDs";
	static final String SV_NAME_CDS_A_ARG_TYPE_ObjectID = "A_ARG_TYPE_ObjectID";
	static final String SV_NAME_CDS_A_ARG_TYPE_Result = "A_ARG_TYPE_Result";
	static final String SV_NAME_CDS_A_ARG_TYPE_SearchCriteria = "A_ARG_TYPE_SearchCriteria";
	static final String SV_NAME_CDS_A_ARG_TYPE_BrowseFlag = "A_ARG_TYPE_BrowseFlag";
	static final String SV_NAME_CDS_A_ARG_TYPE_Filter = "A_ARG_TYPE_Filter";
	static final String SV_NAME_CDS_A_ARG_TYPE_SortCriteria = "A_ARG_TYPE_SortCriteria";
	static final String SV_NAME_CDS_A_ARG_TYPE_Index = "A_ARG_TYPE_Index";
	static final String SV_NAME_CDS_A_ARG_TYPE_Count = "A_ARG_TYPE_Count";
	static final String SV_NAME_CDS_A_ARG_TYPE_UpdateID = "A_ARG_TYPE_UpdateID";
	static final String SV_NAME_CDS_A_ARG_Type_TransferID = "A_ARG_Type_TransferID";
	static final String SV_NAME_CDS_A_ARG_Type_TransferStatus = "A_ARG_Type_TransferStatus";
	static final String SV_NAME_CDS_A_ARG_Type_TransferLength = "A_ARG_Type_TransferLength";
	static final String SV_NAME_CDS_A_ARG_Type_TransferTotal = "A_ARG_Type_TransferTotal";
	static final String SV_NAME_CDS_A_ARG_TYPE_TagValueList = "A_ARG_TYPE_TagValueList";
	static final String SV_NAME_CDS_A_ARG_TYPE_URI = "A_ARG_TYPE_URI";
	static final String SV_NAME_CDS_SearchCapabilities = "SearchCapabilities";
	static final String SV_NAME_CDS_SortCapabilities = "SortCapabilities";
	static final String SV_NAME_CDS_SystemUpdateID = "SystemUpdateID";
	static final String SV_NAME_CDS_ContainerUpdateIDs = "ContainerUpdateIDs";
	
	// List of parameters used in each UPnPAction.
	static final String ACTION_ARG_NAME_SearchCaps = "SearchCaps";
	static final String ACTION_ARG_NAME_SortCaps = "SortCaps";
	static final String ACTION_ARG_NAME_Id = "Id";
	static final String ACTION_ARG_NAME_ObjectID = "ObjectID";
	static final String ACTION_ARG_NAME_BrowseFlag = "BrowseFlag";
	static final String ACTION_ARG_NAME_Filter = "Filter";
	static final String ACTION_ARG_NAME_StartingIndex = "StartingIndex";
	static final String ACTION_ARG_NAME_RequestedCount = "RequestedCount";
	static final String ACTION_ARG_NAME_SortCriteria = "SortCriteria";
	static final String ACTION_ARG_NAME_Result = "Result";
	static final String ACTION_ARG_NAME_NumberReturned = "NumberReturned";
	static final String ACTION_ARG_NAME_TotalMatches = "TotalMatches";
	static final String ACTION_ARG_NAME_UpdateID = "UpdateID";
	static final String ACTION_ARG_NAME_ContainerID = "ContainerID";
	static final String ACTION_ARG_NAME_SearchCriteria = "SearchCriteria";
	static final String ACTION_ARG_NAME_Elements = "Elements";
	static final String ACTION_ARG_NAME_CurrentTagValue = "CurrentTagValue";
	static final String ACTION_ARG_NAME_NewTagValue = "NewTagValue";
	static final String ACTION_ARG_NAME_SourceURI = "SourceURI";
	static final String ACTION_ARG_NAME_DestinationURI = "DestinationURI";
	static final String ACTION_ARG_NAME_TransferID = "TransferID";
	static final String ACTION_ARG_NAME_TransferStatus = "TransferStatus";
	static final String ACTION_ARG_NAME_TransferLength = "TransferLength";
	static final String ACTION_ARG_NAME_TransferTotal = "TransferTotal";
	static final String ACTION_ARG_NAME_NewID = "NewID";
	
	// List of Action Name.
	static final String ACTION_NAME_CDS_GetSearchCapabilities = "GetSearchCapabilities";
	static final String ACTION_NAME_CDS_GetSortCapabilities = "GetSortCapabilities";
	static final String ACTION_NAME_CDS_GetSystemUpdateID = "GetSystemUpdateID";
	static final String ACTION_NAME_CDS_Browse = "Browse";
	static final String ACTION_NAME_CDS_Search = "Search";
	static final String ACTION_NAME_CDS_CreateObject = "CreateObject";
	static final String ACTION_NAME_CDS_DestroyObject = "DestroyObject";
	static final String ACTION_NAME_CDS_UpdateObject = "UpdateObject";
	static final String ACTION_NAME_CDS_ImportResource = "ImportResource";
	static final String ACTION_NAME_CDS_ExportResource = "ExportResource";
	static final String ACTION_NAME_CDS_StopTransferResource = "StopTransferResource";
	static final String ACTION_NAME_CDS_GetTransferProgress = "GetTransferProgress";

	private ArrayList<Integer> getStateVariableTransferIDs() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_TRANSFERIDS);
		ArrayList<Integer> returnValue = value == null ? new ArrayList<Integer>() : UPnPStateVariable.getCSVStateVariableIntegers((String)value.getValue());
		return returnValue;
	}
	
	private ArrayList<String> getStateVariableSearchCapabilities() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_SearchCapabilities);
		ArrayList<String> returnValue = value == null ? new ArrayList<String>() : UPnPStateVariable.getCSVStateVariableStrings((String)value.getValue());
		return returnValue;
	}
	
	private ArrayList<String> getStateVariableSortCapabilities() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_SortCapabilities);
		ArrayList<String> returnValue = value == null ? new ArrayList<String>() : UPnPStateVariable.getCSVStateVariableStrings((String)value.getValue());
		return returnValue;
	}
	
	private String getStateVariableSystemUpdateID() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_SystemUpdateID);
		return (String)value.getValue();
	}
	
	public String getStateVariableContainerUpdateIDs() {
		UPnPStateVariable value = this.getStateVariable(SV_NAME_CDS_ContainerUpdateIDs);
		return (String)value.getValue();
	}
	
	public ArrayList<String> getSearchCapabilities() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_GetSearchCapabilities);
		if ( targetAction != null ) {
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
		} else {
			System.out.println("[Warning] There is no action which name is 'getSearchCapabilities'.");
		}
		return getStateVariableSearchCapabilities();
	}
	
	public ArrayList<String> getSortCapabilities() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_GetSortCapabilities);
		if ( targetAction != null ) {
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
		} else {
			System.out.println("[Warning] There is no action which name is 'getSortCapabilities'.");
		}
		return getStateVariableSortCapabilities();
	}
	
	public String getSystemUpdateID() throws Exception {
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_GetSystemUpdateID);
		if ( targetAction != null ) {
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
		} else {
			System.out.println("[Warning] There is no action which name is 'getSystemUpdateID'.");
		}
		return getStateVariableSystemUpdateID();
	}
	
	public ArrayList<ContentDirectoryItem> browse(String objectID, String browseFlag, String filter, int startingIndex, int requestCount, String sortCriteria) throws Exception {
		ArrayList<ContentDirectoryItem> resultRoot = null;
		UPnPAction targetAction = this.getAction(ACTION_NAME_CDS_Browse);
		if ( targetAction != null ) {
			// 1. Parameter Settings.
			targetAction.getInArgument(ACTION_ARG_NAME_ObjectID).setValue(objectID);
			targetAction.getInArgument(ACTION_ARG_NAME_BrowseFlag).setValue(browseFlag);
			targetAction.getInArgument(ACTION_ARG_NAME_Filter).setValue(filter);
			targetAction.getInArgument(ACTION_ARG_NAME_StartingIndex).setValue(startingIndex+"");
			targetAction.getInArgument(ACTION_ARG_NAME_RequestedCount).setValue(requestCount+"");
			targetAction.getInArgument(ACTION_ARG_NAME_SortCriteria).setValue(sortCriteria);
			// 2. execute SOAP Action & SOAP response parsing and saving.
			ActionExecutor executor = new ActionExecutor(targetAction);
			executor.execute();
			int numberOfReturned = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_NumberReturned).getValue());
			String result = targetAction.getOutArgument(ACTION_ARG_NAME_Result).getValue();
			int totalMatches = Integer.parseInt(targetAction.getOutArgument(ACTION_ARG_NAME_TotalMatches).getValue());

			// TODO : UpdateID variable should be used. when ? idon't know.
			String updateID = targetAction.getOutArgument(ACTION_ARG_NAME_UpdateID).getValue();
			// 3. parse result XML into ContentDirectoryItems.
			resultRoot = parseDIDLXML(result);
		} else {
			System.out.println("There is no browse action in this device.");
		}
		return resultRoot;
	}
	
	static class TolerableErrorHandler extends DefaultHandler implements LexicalHandler {
		
		private String currentEntity = null;

		@Override
		public void error(SAXParseException exception) throws SAXException {
			// TODO Auto-generated method stub
			exception.printStackTrace();
		}

		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			// TODO Auto-generated method stub
			exception.printStackTrace();
		}

		@Override
		public void warning(SAXParseException exception) throws SAXException {
			// TODO Auto-generated method stub
			exception.printStackTrace();
		}
		
		@Override
	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
	        String content = new String(ch, start, length);
	        if (currentEntity != null) {
	            content = "&" + currentEntity + content;
	            currentEntity = null;
	        }
	        System.out.print(content);
	    }

		@Override
		public void startEntity(String name) throws SAXException {
			currentEntity = name;
		}

		@Override
	    public void endEntity(String name) throws SAXException {
	    }

		@Override
	    public void startDTD(String name, String publicId, String systemId)
	            throws SAXException {
	    }

	    @Override
	    public void endDTD() throws SAXException {
	    }

	    @Override
	    public void startCDATA() throws SAXException {
	    }

	    @Override
	    public void endCDATA() throws SAXException {
	    }

	    @Override
	    public void comment(char[] ch, int start, int length) throws SAXException {
	    }

	}
	
	static TolerableErrorHandler teh = null;
	
	static {
		teh = new TolerableErrorHandler();
	}
	
	static private ArrayList<ContentDirectoryItem> parseDIDLXML(String didlXML) throws Exception {
		ArrayList<ContentDirectoryItem> resultRoot = new ArrayList<ContentDirectoryItem>();
		/* resul xml is such like this.
			
		*/
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;
		Document doc;
		Element documentElement;
		NodeList resultItemList;
		try {
			factory.setExpandEntityReferences(false);
			factory.setNamespaceAware(true);
			factory.setIgnoringComments(true);
			factory.setValidating(false);
			factory.setXIncludeAware(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error",true);

			parser = factory.newDocumentBuilder();
			parser.setErrorHandler(teh);
			System.out.println("DIDLXML1:" + didlXML);
			doc = parser.parse(new ByteArrayInputStream(didlXML.getBytes("utf-8")));
			System.out.println("DIDLXML2:" + didlXML);
			documentElement = doc.getDocumentElement()/* <DIDL-Lite> tag */;
			System.out.println("DIDLXML3:" + didlXML);
			resultItemList = documentElement.getChildNodes();
			//NodeList title = doc.getElementsByTagNameNS("urn:schemas-upnp-org:metadata-1-0/upnp/", "searchClass");
			System.out.println("DIDLXML4:" + didlXML);
			NodeList title = doc.getElementsByTagNameNS("*", "searchClass");
			System.out.println("title count:" + title.getLength() + ":value:" + ( title.getLength() > 0 ? title.item(0).getNodeValue() : "empty" ) );
			
			for ( int inx = 0 ; inx < resultItemList.getLength() ; inx++ ) {
				if ( resultItemList.item(inx).getNodeType() == Node.ELEMENT_NODE ) {
					Element item = (Element)resultItemList.item(inx);
					
					if ( item.getNodeName().equals("container") ) {
						// Make a container node.
						ContentDirectoryItem aContainer = new ContentDirectoryItem();
						// Setting element attributes into container.
						aContainer.setType(ContentDirectoryItem.CDS_TYPE_CONTAINER);
						aContainer.setId(item.getAttribute("id"));
						aContainer.setParentId(item.getAttribute("parentID"));
						aContainer.setRestricted(item.getAttribute("restricted").equals("0") ? false : true);
						aContainer.setSearchable(item.getAttribute("searchable").equals("0") ? false : true);
						// Setting child element values into container.
						NodeList childNode = item.getChildNodes();
						for ( int propInx = 0 ; propInx < childNode.getLength() ; propInx++ ) {
							Node childElement = childNode.item(propInx);
							System.out.println("node name:" + childElement.getNodeName());
							if ( childElement.getNodeType() == Node.ELEMENT_NODE ) {
								if ( NAMESPACE_DC_ELEMENT.equals(childElement.getNamespaceURI())
										&& childElement.getLocalName().equals("title") ) {
									aContainer.setTitle(childElement.getFirstChild().getNodeValue());
								} else if ( NAMESPACE_DC_ELEMENT.equals(childElement.getNamespaceURI()) 
										&& childElement.getLocalName().equals("creator") ) {
									aContainer.setCreator(childElement.getFirstChild().getNodeValue());
								} else if ( NAMESPACE_UPNP_ELEMENT.equals(childElement.getNamespaceURI()) 
										&& childElement.getLocalName().equals("class") ) {
									aContainer.setUpnpClassName(childElement.getFirstChild().getNodeValue());
								} else if ( NAMESPACE_UPNP_ELEMENT.equals(childElement.getNamespaceURI()) 
										&& childElement.getLocalName().equals("searchClass") ) {
									aContainer.setUpnpSearchClass(childElement.getFirstChild().getNodeValue());
								} else if ( NAMESPACE_UPNP_ELEMENT.equals(childElement.getNamespaceURI()) 
										&& childElement.getLocalName().equals("createClass") ) {
									aContainer.setUpnpCreateClass(childElement.getFirstChild().getNodeValue());
								}
							}
						}
						resultRoot.add(aContainer);
					} else if ( item.getNodeName().equals("item") ) {
						// Make a item node.
						ContentDirectoryItem oneItem = new ContentDirectoryItem();
						oneItem.setType(ContentDirectoryItem.CDS_TYPE_ITEM);
						oneItem.setId(item.getAttribute("id"));
						oneItem.setParentId(item.getAttribute("parentID"));
						oneItem.setRestricted(item.getAttribute("restricted").equals("0") ? false : true);
						NodeList childNode = item.getChildNodes();
						for ( int propInx = 0 ; propInx < childNode.getLength() ; propInx++ ) {
							Node childElement = childNode.item(propInx);
							System.out.println("node name:" + childElement.getNodeName());
							if ( NAMESPACE_DC_ELEMENT.equals(childElement.getNamespaceURI()) 
									&& childElement.getLocalName().equals("title") ) {
								oneItem.setTitle(childElement.getFirstChild().getNodeValue());
							} else if ( NAMESPACE_DC_ELEMENT.equals(childElement.getNamespaceURI()) 
									&& childElement.getLocalName().equals("date") ) {
								oneItem.setDate(childElement.getFirstChild().getNodeValue());
							} else if ( NAMESPACE_UPNP_ELEMENT.equals(childElement.getNamespaceURI()) 
									&& childElement.getLocalName().equals("class") ) {
								oneItem.setUpnpClassName(childElement.getFirstChild().getNodeValue());
							} else if ( childElement.getNodeName().equals("res") ) {
								oneItem.setResProtocolInfo(((Element)childElement).getAttribute("protocolInfo"));
								oneItem.setResSize(((Element)childElement).getAttribute("size"));
								oneItem.setResValue(childElement.getFirstChild().getNodeValue());
							} else if ( NAMESPACE_UPNP_ELEMENT.equals(childElement.getNamespaceURI()) 
									&& childElement.getLocalName().equals("createClass") ) {
								oneItem.setUpnpCreateClass(childElement.getFirstChild().getNodeValue());
							}
						}
						resultRoot.add(oneItem);
					}
				}
			}
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch(SAXException ex) {
			ex.printStackTrace();
			throw ex;
		}
		return resultRoot;
	}
	
	public static void main(String[] args) {
		
		StringBuffer sampleResultXML = new StringBuffer();
		/*
		sampleResultXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sampleResultXML.append("<DIDL-Lite").append("\n");
		sampleResultXML.append("xmlns:dc=\"http://purl.org/dc/elements/1.1/\"").append("\n");
		sampleResultXML.append("xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\"").append("\n");
		sampleResultXML.append("xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"").append("\n");
		sampleResultXML.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"").append("\n");
		sampleResultXML.append("xsi:schemaLocation=\"").append("\n");
		sampleResultXML.append("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/").append("\n");
		sampleResultXML.append("http://www.upnp.org/schemas/av/didl-lite-v2-20060531.xsd").append("\n");
		sampleResultXML.append("urn:schemas-upnp-org:metadata-1-0/upnp/").append("\n");
		sampleResultXML.append("http://www.upnp.org/schemas/av/upnp-v2-20060531.xsd\">").append("\n");
		sampleResultXML.append("<container id=\"4\" parentID=\"1\" childCount=\"3\" restricted=\"0\">").append("\n");
		sampleResultXML.append("<dc:title>Brand New Day</dc:title>").append("\n");
		sampleResultXML.append("<dc:creator>Sting</dc:creator>").append("\n");
		sampleResultXML.append("<upnp:class>object.container.album.musicAlbum</upnp:class>").append("\n");
		sampleResultXML.append("<upnp:searchClass includeDerived=\"0\">").append("\n");
		sampleResultXML.append("object.item.audioItem.musicTrack").append("\n");
		sampleResultXML.append("</upnp:searchClass>").append("\n");
		sampleResultXML.append("<upnp:createClass includeDerived=\"0\">").append("\n");
		sampleResultXML.append("object.item.audioItem.musicTrack").append("\n");
		sampleResultXML.append("</upnp:createClass>").append("\n");
		sampleResultXML.append("</container>").append("\n");
		sampleResultXML.append("<container id=\"3\" parentID=\"1\" childCount=\"4\" restricted=\"0\">").append("\n");
		sampleResultXML.append("<dc:title>Singles Soundtrack</dc:title>").append("\n");
		sampleResultXML.append("<dc:creator>Various Artists</dc:creator>").append("\n");
		sampleResultXML.append("<upnp:class>object.container.album.musicAlbum</upnp:class>").append("\n");
		sampleResultXML.append("<upnp:searchClass includeDerived=\"0\">").append("\n");
		sampleResultXML.append("object.item.audioItem.musicTrack").append("\n");
		sampleResultXML.append("</upnp:searchClass>").append("\n");
		sampleResultXML.append("<upnp:createClass includeDerived=\"0\">").append("\n");
		sampleResultXML.append("object.item.audioItem.musicTrack").append("\n");
		sampleResultXML.append("</upnp:createClass>").append("\n");
		sampleResultXML.append("</container>").append("\n");
		sampleResultXML.append("</DIDL-Lite>").append("\n");
		*/
		sampleResultXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sampleResultXML.append("<DIDL-Lite").append("\n");
		sampleResultXML.append("xmlns:dc=\"http://purl.org/dc/elements/1.1/\"").append("\n");
		sampleResultXML.append("xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\"").append("\n");
		sampleResultXML.append("xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"").append("\n");
		sampleResultXML.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"").append("\n");
		sampleResultXML.append("xsi:schemaLocation=\"").append("\n");
		sampleResultXML.append("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/").append("\n");
		sampleResultXML.append("http://www.upnp.org/schemas/av/didl-lite-v2-20060531.xsd").append("\n");
		sampleResultXML.append("urn:schemas-upnp-org:metadata-1-0/upnp/").append("\n");
		sampleResultXML.append("http://www.upnp.org/schemas/av/upnp-v2-20060531.xsd\">").append("\n");
		sampleResultXML.append("<item id=\"14\" parentID=\"12\" restricted=\"0\">").append("\n");
		sampleResultXML.append("<dc:title>Sunset on the beach</dc:title>").append("\n");
		sampleResultXML.append("<dc:date>2001-10-20</dc:date>").append("\n");
		sampleResultXML.append("<upnp:class>object.item.imageItem.photo</upnp:class>").append("\n");
		sampleResultXML.append("<res protocolInfo=\"http-get:*:image/jpeg:*\" size=\"20000\">").append("\n");
		sampleResultXML.append("http://10.0.0.1/getcontent.asp?id=14").append("\n");
		sampleResultXML.append("</res>").append("\n");
		sampleResultXML.append("</item>").append("\n");
		sampleResultXML.append("<item id=\"15\" parentID=\"12\" restricted=\"0\">").append("\n");
		sampleResultXML.append("<dc:title>Playing in the pool</dc:title>").append("\n");
		sampleResultXML.append("<dc:date>2001-10-25</dc:date>").append("\n");
		sampleResultXML.append("<upnp:class>object.item.imageItem.photo</upnp:class>").append("\n");
		sampleResultXML.append("<res protocolInfo=\"http-get:*:image/jpeg:*\" size=\"25000\">").append("\n");
		sampleResultXML.append("http://10.0.0.1/getcontent.asp?id=15").append("\n");
		sampleResultXML.append("</res>").append("\n");
		sampleResultXML.append("</item>").append("\n");
		sampleResultXML.append("<item id=\"20\" refID=\"15\" parentID=\"13\" restricted=\"0\">").append("\n");
		sampleResultXML.append("<dc:title>Playing in the pool</dc:title>").append("\n");
		sampleResultXML.append("<dc:date>2001-10-25</dc:date>").append("\n");
		sampleResultXML.append("<upnp:class>object.item.imageItem.photo</upnp:class>").append("\n");
		sampleResultXML.append("<res protocolInfo=\"http-get:*:image/jpeg:*\" size=\"25000\">").append("\n");
		sampleResultXML.append("http://10.0.0.1/getcontent.asp?id=15").append("\n");
		sampleResultXML.append("</res>").append("\n");
		sampleResultXML.append("</item>").append("\n");
		sampleResultXML.append("</DIDL-Lite>").append("\n");


		try {
			ArrayList<ContentDirectoryItem> itemList = parseDIDLXML(sampleResultXML.toString());
			for ( int inx = 0; inx < itemList.size() ; inx++ ) {
				System.out.println(itemList.get(inx).toString());
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/*
	Search
	O
	CreateObject
	O
	DestroyObject
	O
	UpdateObject
	O
	ImportResource
	O
	ExportResource
	O
	StopTransferResource
	O
	GetTransferProgress
	O
	*/
	
}

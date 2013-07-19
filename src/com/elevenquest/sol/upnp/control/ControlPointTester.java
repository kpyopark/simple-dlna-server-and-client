package com.elevenquest.sol.upnp.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import com.elevenquest.sol.upnp.action.ActionExecutor;
import com.elevenquest.sol.upnp.common.Logger;
import com.elevenquest.sol.upnp.control.ControlPoint.PrintDeviceInfo;
import com.elevenquest.sol.upnp.model.UPnPAction;
import com.elevenquest.sol.upnp.model.UPnPDevice;
import com.elevenquest.sol.upnp.model.UPnPDeviceManager;
import com.elevenquest.sol.upnp.model.UPnPEventManager;
import com.elevenquest.sol.upnp.model.UPnPService;
import com.elevenquest.sol.upnp.model.UPnPStateVariable;
import com.elevenquest.sol.upnp.service.connection.ConnectionManagementService;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryItem;
import com.elevenquest.sol.upnp.service.directory.ContentDirectoryService;
import com.elevenquest.sol.upnp.service.rederingcontrol.RenderingControlService;

public class ControlPointTester {
	public static void printUsage() {
		System.out.println("0. exit");
		System.out.println("1. start control point");
		System.out.println("2. stop control point");
		System.out.println("3. list up devices");
		System.out.println("4. choose a device");
		System.out.println("5. list up services");
		System.out.println("6. choose a service");
		System.out.println("7. enable print devices");
		System.out.println("8. disable print devices");
		System.out.println("9. print usage");
		System.out.println("10. list up services");
		System.out.println("11. execute a service");
		System.out.println("12. activate GENA");
		System.out.println("13. choose renderer service");
		System.out.println("14. choose content directory service");
		System.out.println("15. list up items");
		System.out.println("16. play an item");
		System.out.println("17. retrieve protocols from selected device.");
	}
	
	/**
	 * For Testing.
	 * @param args
	 */
	public static void main(String[] args) {
		ControlPoint cp = new ControlPoint();
		UPnPDeviceManager manager = UPnPDeviceManager.getDefaultDeviceManager();
		PrintDeviceInfo deviceInfoPrinter = new PrintDeviceInfo(cp);
		int status = 0;
		UPnPDevice selDev = null;
		UPnPService selServ = null;
		UPnPAction selAction = null;
		boolean isExit = false;

		
		printUsage();
		try {
			while( !isExit ) {
				Scanner scanner = new Scanner(System.in);
				String choiceString = null;
				int choiceNumber = 0;
				try {
					
					choiceString = scanner.nextLine();
					choiceNumber = Integer.parseInt(choiceString);
				} catch ( NumberFormatException nfe ) {
					//nfe.printStackTrace();
					if ( status != 16 )
						continue;
				}
				switch (status) {
				case 4 : // choose a device.
					if ( 0 <= choiceNumber && choiceNumber < manager.getListSize() ) {
						selDev = manager.getDeviceList().toArray(new UPnPDevice[0])[choiceNumber];
						System.out.println("select device is " + selDev );
					} else {
						System.out.println("error the device list size is " + manager.getListSize() );
					}
					status = 0;
					break;
				case 6 :
					if ( selDev == null ) {
						System.out.println("There is no selected device.");
					} else {
						if ( 0 <= choiceNumber && choiceNumber < selDev.getSerivces().size() ) {
							selServ = selDev.getSerivces().get(choiceNumber);
							System.out.println("select device is " + selDev + ". selected service is " + selServ );
						} else {
							System.out.println("error the service list size is " + selDev.getSerivces().size() );
						}
					}
					status = 0;
					break;
				case 11 :
					if ( selDev == null || selServ == null ) {
						System.out.println("There is no selected service.");
					} else {
						UPnPAction[] actions = selServ.getActionList().toArray(new UPnPAction[0]);
						if ( 0 <= choiceNumber && choiceNumber < actions.length ) {
							selAction = actions[choiceNumber];
							System.out.println("--- action parameters ---");
							Vector<UPnPStateVariable> inArgs = selAction.getInArguments();
							for ( int cnt = 0 ; cnt < inArgs.size() ; cnt++ ) {
								System.out.println(cnt + ":" + inArgs.get(cnt));
								System.out.println("Enter parameter value:");
								inArgs.get(cnt).setValue(scanner.next());
							}
							ActionExecutor executor = new ActionExecutor(selAction);
							executor.execute();
							System.out.println("--- outcomes ---");
							Vector<UPnPStateVariable> outArgs = selAction.getOutArguments();
							for ( int cnt = 0 ; cnt < outArgs.size() ; cnt++ ) {
								System.out.println(cnt + ":" + outArgs.get(cnt));
							}
						} else {
							System.out.println("error the service list size is " + actions.length);
						}
					}
					status = 0;
					break;
				case 12 :
					if ( selDev == null || selServ == null ) {
						System.out.println("There is no selected service.");
					} else {
						UPnPEventManager.getUPnPEventManager().addServiceToBeRegistered(selServ);
						UPnPEventManager.getUPnPEventManager().startGenaThread();
						System.out.println("Activate GENA for the service[" + selServ.getServiceId() + "]");
					}
					status = 0;
					break;
				case 13 :
					if ( selDev == null ) {
						System.out.println("There is no selected device.");
					} else {
						if ( 0 <= choiceNumber && choiceNumber < selDev.getSerivces().size() ) {
							UPnPService selectedOne = selDev.getSerivces().get(choiceNumber);
							if ( selectedOne instanceof RenderingControlService ) {
								cp.rcs = selectedOne;
								System.out.println("select device is " + selDev + ". selected service is " + selectedOne );
							} else {
								System.out.println("selected device is " + selDev + ". selected service is not rendering service control. ignore your selection." );
							}
						} else {
							System.out.println("error the service list size is " + selDev.getSerivces().size() );
						}
					}
					status = 0;
					break;					
				case 14 :
					if ( selDev == null ) {
						System.out.println("There is no selected device.");
					} else {
						if ( 0 <= choiceNumber && choiceNumber < selDev.getSerivces().size() ) {
							UPnPService selectedOne = selDev.getSerivces().get(choiceNumber);
							if ( selectedOne instanceof ContentDirectoryService ) {
								cp.cds = selectedOne;
								System.out.println("select device is " + selDev + ". selected service is " + selectedOne );
							} else {
								System.out.println("selected device is " + selDev + ". selected service is not content directory service control. ignore your selection." );
							}
						} else {
							System.out.println("error the service list size is " + selDev.getSerivces().size() );
						}
					}
					status = 0;
					break;
				case 16 :
					ArrayList<ContentDirectoryItem> lastDisplayedItemList = cp.lastDisplayedItem;
					System.out.println("Your choice is " + choiceString);
					for ( ContentDirectoryItem item : lastDisplayedItemList ) {
						if ( item.getId().equals(choiceString) )
						{
							cp.playContentItem(item);
							break;
						}
					}
					status = 0;
					break;
				default :
					switch (choiceNumber) {
					case 0 :
						isExit = true;
						break;
					case 1 :
						cp.start();
						break;
					case 2 :
						cp.stop();
						break;
					case 3 :
						Collection<UPnPDevice> list = manager.getDeviceList();
						int cnt = 0;
						System.out.println("--- devices ---");
						for ( UPnPDevice device : list ) {
							System.out.println( cnt + ":" +device );
							cnt++;
						}
						break;
					case 4 :
						System.out.println("Choose a device.");
						status = 4;
						break;
					case 5 :
						if ( selDev == null ) {
							System.out.println("There is no selected device.");
						} else {
							System.out.println("--- services ---");
							Vector<UPnPService> services = selDev.getSerivces();
							for ( cnt = 0 ; cnt < services.size() ; cnt++) {
								System.out.println( cnt + ":" + services.get(cnt).getServiceId() );
							}
						}
						break;
					case 6 :
						System.out.println("Choose a service.");
						status = 6;
						break;
					case 7 :
						manager.addDeviceListChangeListener(deviceInfoPrinter);
						System.out.println("Register updated handler.");
						break;
					case 8 :
						manager.removeDeviceListChangeListener(deviceInfoPrinter);
						System.out.println("Unregister updated handler.");
						break;
					case 9 :
						printUsage();
						break;
					case 10 :
						if ( selDev == null || selServ == null ) {
							System.out.println("There is no selected service.");
						} else {
							System.out.println("--- state variables ---");
							Collection<UPnPStateVariable> variables = selServ.getStateVariableList();
							cnt = 0;
							for ( UPnPStateVariable var : variables ) {
								System.out.println( cnt + ":" + var );
								cnt ++;
							}
							System.out.println("--- actions ---");
							cnt = 0;
							Collection<UPnPAction> actions = selServ.getActionList();
							for ( UPnPAction action : actions ) {
								System.out.println( cnt + ":" + action );
								cnt++;
							}
						}
						break;
					case 11 :
						System.out.println("input parameters.");
						status = 11;
						break;
					case 12 :
						if ( selDev == null ) {
							System.out.println("There is no selected device.");
						} else {
							System.out.println("--- services ---");
							Vector<UPnPService> services = selDev.getSerivces();
							for ( cnt = 0 ; cnt < services.size() ; cnt++) {
								System.out.println( cnt + ":" + services.get(cnt).getServiceId() );
							}
						}
						System.out.println("choose sevice.");
						status = 12;
						break;
					case 13 :
						System.out.println("Choose a renderer service.");
						status = 13;
						break;
					case 14 :
						System.out.println("Choose a content directory service.");
						status = 14;
						break;
					case 15 :
						if ( cp.cds == null ) {
							System.out.println("There is no selected device.");
						} else {
							System.out.println("--- content directory item ---");
							try {
								ContentDirectoryService cds = (ContentDirectoryService)cp.cds;
								ContentDirectoryItem rootItem = new ContentDirectoryItem();
								rootItem.setId("0");
								ArrayList<ContentDirectoryItem> contentList = cds.browse(rootItem, "BrowseMetadata", "*", 0, 0, "");
								for ( ContentDirectoryItem item : contentList ) {
									cp.testBrowseRecursively(cds,item,0);
								}
							} catch ( Exception e1 ) {
								e1.printStackTrace();
							}
						}
						break;
					case 16 :
						if ( cp.cds == null || cp.rcs == null ) {
							System.out.println("There is no selected rendering/content directory service.");
						} else {
							status = 16;
						}
						break;
					case 17 :
						if ( selDev == null ) {
							System.out.println("There is no selected device.");
						} else {
							ConnectionManagementService cms = (ConnectionManagementService)selDev.getUPnPService(UPnPService.UPNP_SERVICE_ID_CMS);
							if ( cms == null ) {
								System.out.println("There is no connection management service.");
							} else {
								try {
									ArrayList<String> protocols = cms.getSinkProtocolInfo();
									for ( String protocol : protocols ) {
										System.out.println("Sink Protocol:" + protocol);
									}
									protocols = cms.getSourceProtocolInfo();
									for ( String protocol : protocols ) {
										System.out.println("Source Protocol:" + protocol);
									}
								} catch ( Exception e1 ) {
									e1.printStackTrace();
								}
							}
						}
					default :
						break;
					}
					break;
				}
			}
			//Thread.sleep(60 * 1000000);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		cp.stop();
	}
}

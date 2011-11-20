package com.lgcns.sol.uuid;

/**
 *  @deprecated - Use com.lgcns.sol.upnp.common.UPnPUtils.
 * 
 */
public class UUIDGen {
	/**
		 Our goal is to make general DLNA server & client in independent platforms.
		 java.rmi package can be used only in JDK ( jdk version 1.4 > ), but not in android.
		 We should make our own UUID algorithm.
		 Please, make your code in com.lgcns.sol.common.UPnPUtils class.
		 There is also getRandomUuid method which isn't implemented yet.
	 * 
	 * @return a new "globally" unique identifier
	 */
	public String nextUUID() {
		return "";
		//return new java.rmi.dgc.VMID().toString();
	}

}

package com.lgcns.sol.uuid;

public class UUIDGen {
	  /**
	   * Creates a new UUID. The algorithm used is the one in {@link UUID}, the
	   * open group algorithm took too damn long.
	   *
	   * @return a new "globally" unique identifier
	   */
	  public String nextUUID() {
	    return new java.rmi.dgc.VMID().toString();
	  }

}

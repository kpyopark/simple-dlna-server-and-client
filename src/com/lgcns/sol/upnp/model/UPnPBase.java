package com.lgcns.sol.upnp.model;

import java.lang.reflect.Field;

public class UPnPBase {

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String className = this.getClass().getName();
		sb.append("UPNP MODEL STRUCTURE:").append(className).append(":Hash Codoe:").append(super.toString()).append("\n");
		Field[] fields = this.getClass().getDeclaredFields();
		for ( int inx = 0; inx < fields.length ; inx++ ) {
			try {
				if ( fields[inx].isAccessible() ) {
					sb.append(className).append(":").append(fields[inx].getName()).append(":").append((fields[inx].get(this) != null ) ? fields[inx].get(this).toString() : "null").append("\n");
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
	

}

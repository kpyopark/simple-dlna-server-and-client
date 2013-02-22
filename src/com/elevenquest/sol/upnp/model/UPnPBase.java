package com.elevenquest.sol.upnp.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;

public class UPnPBase {

	public String toString() {
		return toString(1);
	}
	
	public static String toString(Object obj, int depth) {
		if ( obj instanceof UPnPBase ) {
			return ((UPnPBase)obj).toString(depth);
		}
		return obj.toString();
	}
	
	public String toString(int depth) {
		StringBuffer sb = new StringBuffer();
		String className = this.getClass().getName();
		sb.append("UPNP MODEL STRUCTURE:").append(className).append(
				":Hash Code:").append(super.toString()).append(":methods:").append(this.getClass().getDeclaredMethods().length).append("\n");
		if ( depth > 3 )
			return "";
		depth++;
		Method[] methods = this.getClass().getDeclaredMethods();
		for (int inx = 0; inx < methods.length; inx++) {
			try {
				//System.out.println("method:" + methods[inx].getName() + ":isAccessible:" + methods[inx].isAccessible());
				if (!methods[inx].isAccessible()) {
					String canonicalNameOfReturnType = methods[inx].getReturnType().getCanonicalName();
					if ( ( canonicalNameOfReturnType.equals("java.util.Vector") ||
							canonicalNameOfReturnType.equals("java.util.ArrayList") ||
							canonicalNameOfReturnType.equals("java.util.Collection")
							)
							&& methods[inx].getName().indexOf("get") == 0 ) {
						if ( Modifier.isPublic(methods[inx].getModifiers()) || Modifier.isProtected(methods[inx].getModifiers()) ) {
							Collection<Object> vec = (Collection<Object>) methods[inx].invoke(this, null);
							try {
								sb.append(className).append(":").append(
										methods[inx].getName()).append(":");
								if ( vec == null ) {
									sb.append("null");
								} else {
									for (Iterator<Object> iter = vec.iterator(); iter.hasNext() ; ) {
										sb.append("[").append(toString(iter.next(),depth))
												.append("]");
									}
								}
							} catch (Exception e1) {
								System.out.println(e1.getMessage());
							}
						} else {
							System.out.println("methid[" + methods[inx].getName() + "] is not public or protected.");
						}
						//System.out.println("after appending:" + methods[inx].getName() );
					} else if ( methods[inx].getName().indexOf("get") == 0 ) {
						//System.out.println("before appending:" + methods[inx].getName() );
						try {
							Object rtnValue = methods[inx].invoke(this,	null);
							if ( rtnValue instanceof UPnPDevice ) {
								sb
								.append(className)
								.append(":")
								.append(methods[inx].getName())
								.append(":")
								.append("UPnPDevice").append("\n");
							} else {
								sb
										.append(className)
										.append(":")
										.append(methods[inx].getName())
										.append(":")
										.append(
												( rtnValue != null) ? toString(rtnValue,depth)
														: "null").append("\n");
							}
						} catch (Exception e1) {
							System.out.println(e1.getMessage());
						}
						//System.out.println("after appending:" + methods[inx].getName() );
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}

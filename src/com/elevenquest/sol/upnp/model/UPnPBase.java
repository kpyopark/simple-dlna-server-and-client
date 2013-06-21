package com.elevenquest.sol.upnp.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;

import com.elevenquest.sol.upnp.common.Logger;

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
				if ( !Modifier.isPublic(methods[inx].getModifiers()) && !Modifier.isProtected(methods[inx].getModifiers()) ) {
					Logger.println(Logger.DEBUG, "method[" + methods[inx].getName() + "] is not public or protected.");
					continue;
				}
				Class[] parameters = methods[inx].getParameterTypes();
				if ( parameters != null && parameters.length > 0 ) {
					// It need parameters. So it should be skipped.
					sb.append(className).append(":").append(
							methods[inx].getName()).append(":skipped.");
					continue;
				}
				if ( methods[inx].getName().indexOf("get") != 0 ) {
					continue;
				}
				String canonicalNameOfReturnType = methods[inx].getReturnType().getCanonicalName();
				if ( ( canonicalNameOfReturnType.equals("java.util.Vector") ||
						canonicalNameOfReturnType.equals("java.util.ArrayList") ||
						canonicalNameOfReturnType.equals("java.util.Collection")
						) ) {
					Collection<Object> vec = (Collection<Object>) methods[inx].invoke(this, (Object[])null);
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
						e1.printStackTrace();
						Logger.println(Logger.ERROR, e1.getMessage());
					}
				} else {
					Object rtnValue = methods[inx].invoke(this,	(Object[])null);
					if ( rtnValue instanceof UPnPDevice ) {
						sb.append(className).append(":").append(methods[inx].getName()).append(":").append("UPnPDevice").append("\n");
					} else {
						sb.append(className).append(":").append(methods[inx].getName()).append(":").append(
										( rtnValue != null) ? toString(rtnValue,depth) : "null").append("\n");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}

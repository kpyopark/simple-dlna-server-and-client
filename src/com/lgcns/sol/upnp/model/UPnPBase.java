package com.lgcns.sol.upnp.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class UPnPBase {

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String className = this.getClass().getName();
		sb.append("UPNP MODEL STRUCTURE:").append(className).append(
				":Hash Code:").append(super.toString()).append(":methods:").append(this.getClass().getDeclaredMethods().length).append("\n");
		Method[] methods = this.getClass().getDeclaredMethods();
		for (int inx = 0; inx < methods.length; inx++) {
			try {
				//System.out.println("method:" + methods[inx].getName() + ":isAccessible:" + methods[inx].isAccessible());
				if (!methods[inx].isAccessible()) {
					if (methods[inx].getReturnType().getCanonicalName().equals(
							"java.util.Vector")
							&& methods[inx].getName().indexOf("get") == 0) {
						System.out.println("before appending:" + methods[inx].getName() );
						try {
							Vector<Object> vec = (Vector<Object>) methods[inx]
									.invoke(this, null);
							sb.append(className).append(":").append(
									methods[inx].getName()).append(":");
							for (int childInx = 0; vec != null
									&& childInx < vec.size(); childInx++) {
								sb.append("[").append(vec.get(childInx))
										.append("]");
							}
						} catch (Exception e1) {
							System.out.println(e1.getMessage());
						}
						System.out.println("after appending:" + methods[inx].getName() );
					} else if (methods[inx].getReturnType().getCanonicalName()
							.equals("java.util.ArrayList")
							&& methods[inx].getName().indexOf("get") == 0) {
						System.out.println("before appending:" + methods[inx].getName() );
						try {
							ArrayList<Object> vec = (ArrayList<Object>) methods[inx]
									.invoke(this, null);
							sb.append(className).append(":").append(
									methods[inx].getName()).append(":");
							for (int childInx = 0; vec != null
									&& childInx < vec.size(); childInx++) {
								sb.append("[").append(vec.get(childInx))
										.append("]");
							}
						} catch (Exception e1) {
							System.out.println(e1.getMessage());
						}
						System.out.println("after appending:" + methods[inx].getName() );
					} else if (methods[inx].getReturnType().getCanonicalName()
							.equals("java.util.Collection")
							&& methods[inx].getName().indexOf("get") == 0) {
						System.out.println("before appending:" + methods[inx].getName() );
						try {
							Collection<Object> vec = (Collection<Object>) methods[inx]
									.invoke(this, null);
							sb.append(className).append(":").append(
									methods[inx].getName()).append(":");
							if ( vec == null ) {
								sb.append("null");
							} else {
								for (Iterator<Object> iter = vec.iterator(); iter.hasNext() ; ) {
									sb.append("[").append(iter.next())
											.append("]");
								}
							}
						} catch (Exception e1) {
							System.out.println(e1.getMessage());
						}
						System.out.println("after appending:" + methods[inx].getName() );
					} else if ( methods[inx].getName().indexOf("get") == 0 ) {
						try {
							System.out.println("before appending:" + methods[inx].getName() );
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
												( rtnValue != null) ? rtnValue
														.toString()
														: "null").append("\n");
							}
							System.out.println("after appending:" + methods[inx].getName() );
						} catch (Exception e1) {
							System.out.println(e1.getMessage());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}

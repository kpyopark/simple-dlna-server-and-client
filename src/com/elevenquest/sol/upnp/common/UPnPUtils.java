package com.elevenquest.sol.upnp.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class UPnPUtils {
	
	public static String getRandomUuid() {
		// TODO : Modify this. below text is just a sample. but its format is right.
		return "2fac1234-31f8-11b4-a222-08002b34c003";
	}
	
	public static ArrayList<String> getTokenizedCSVElements(String csvValue) {
		ArrayList<String> returnValue = new ArrayList<String>();
		if ( csvValue != null ) {
			// TODO : Modify below lines. String Tokenzier couldn't recognize the escape character '\' used is CSV file format.
			//        So, you must make your own string tokenizer.
			
			StringTokenizer st = new StringTokenizer(csvValue,",");
			while( st.hasMoreTokens() ) {
				returnValue.add(st.nextToken());
			}
		}
		return returnValue;
	}
	
	public static String escapeXML(String s) {
		/*
		StringBuffer str = new StringBuffer();
		int len = (s != null) ? s.length() : 0;
		for (int i=0; i<len; i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case '<': str.append("&lt;"); break;
				case '>': str.append("&gt;"); break;
				case '&': str.append("&amp;"); break;
				case '"': str.append("&quot;"); break;
				case '\'': str.append("&apos;"); break;
				default: str.append(ch);
			}
		}
		return str.toString();
		*/
		// replaced with apache's common utils.
		return org.apache.commons.lang.StringEscapeUtils.escapeXml(s);
	}
	
	public static String unescapeXML(String s) {
		return org.apache.commons.lang.StringEscapeUtils.unescapeXml(s);
	}
	
	public static String base64encode(String org) {
		return Base64Coder.encodeString(org);
	}

	
}

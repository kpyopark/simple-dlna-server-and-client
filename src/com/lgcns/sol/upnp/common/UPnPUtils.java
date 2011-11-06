package com.lgcns.sol.upnp.common;

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
	
}

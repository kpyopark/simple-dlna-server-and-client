package com.elevenquest.sol.upnp.common;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class UPnPUtils {
	
	private static void fillHexStringFromByte(StringBuffer sb, byte value) {
		byte high = (byte)(value >> 8);
		byte low = (byte)(value & 0x0f);
		if ( high > 9 ) {
			sb.append('a' + high - 10);
		} else {
			sb.append('0' + high );
		}
		if ( low > 9 ) {
			sb.append('a' + low - 10);
		} else {
			sb.append('0' + low);
		}
	}
	
	public static String getRandomUuid() {
		NetworkInterface validOne = null;
		byte[] uuidBytes = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		try {
			Enumeration<NetworkInterface> intfs = NetworkInterface.getNetworkInterfaces();
			while( intfs.hasMoreElements() ) {
				validOne = intfs.nextElement();
				if ( !validOne.isLoopback() && !validOne.isVirtual() ) {
					break;
				}
			}
		} catch ( Exception e ) {
			// If you can't take the valid network interface,
			// use common network interface;
		}
		int pos = 0;
		byte[] mac = {0,0,0,0,0,0,0,0}; 
		if ( validOne != null ) {
			try {
				mac = validOne.getHardwareAddress();
			} catch ( Exception e ) {
				// If you can't take the valid MAC address from the hardware
				// use common MAC address.
			}
		}
		// 1. setting the mac address
		for ( int inx = 0 ; inx < mac.length ; inx++ ) {
			uuidBytes[pos++] = mac[inx];
		}
		// 2. calculate the datetime stamp.
		long timestamp = System.currentTimeMillis();
		uuidBytes[8] = (byte)((timestamp >> 56) & 0xff);
		uuidBytes[9] = (byte)((timestamp >> 48) & 0xff);
		uuidBytes[10] = (byte)((timestamp >> 40) & 0xff);
		uuidBytes[11] = (byte)((timestamp >> 32) & 0xff);
		uuidBytes[12] = (byte)((timestamp >> 24) & 0xff);
		uuidBytes[13] = (byte)((timestamp >> 16) & 0xff);
		uuidBytes[14] = (byte)((timestamp >> 8) & 0xff);
		uuidBytes[15] = (byte)((timestamp >> 0) & 0xff);
		
		// 3. convert to string
		StringBuffer sb = new StringBuffer();
		fillHexStringFromByte(sb,uuidBytes[0]);
		fillHexStringFromByte(sb,uuidBytes[1]);
		fillHexStringFromByte(sb,uuidBytes[2]);
		fillHexStringFromByte(sb,uuidBytes[3]);
		sb.append("-");
		fillHexStringFromByte(sb,uuidBytes[4]);
		fillHexStringFromByte(sb,uuidBytes[5]);
		sb.append("-");
		fillHexStringFromByte(sb,uuidBytes[6]);
		fillHexStringFromByte(sb,uuidBytes[7]);
		sb.append("-");
		fillHexStringFromByte(sb,uuidBytes[8]);
		fillHexStringFromByte(sb,uuidBytes[9]);
		sb.append("-");
		fillHexStringFromByte(sb,uuidBytes[10]);
		fillHexStringFromByte(sb,uuidBytes[11]);
		fillHexStringFromByte(sb,uuidBytes[12]);
		fillHexStringFromByte(sb,uuidBytes[13]);
		fillHexStringFromByte(sb,uuidBytes[14]);
		fillHexStringFromByte(sb,uuidBytes[15]);
		
		return sb.toString();
		//return "2fac1234-31f8-11b4-a222-08002b34c003";
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
		// replaced with apache's common utils.
		// return org.apache.commons.lang.StringEscapeUtils.escapeXml(s);
	}
	
	public static String unescapeXML(String s) {
		//return org.apache.commons.lang.StringEscapeUtils.unescapeXml(s);
		StringBuffer str = new StringBuffer();
		int len =  (s != null) ? s.length() : 0;
		for (int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			if ( ch == '&' ) {
				if ( ( len - i ) > 5 ) {
					if ( s.charAt(i+1) == 'a' && s.charAt(i+2) == 'p' && s.charAt(i+3) == 'o' && s.charAt(i+4) == 's' && s.charAt(i+5) == ';') {
						str.append('\'');
						i += 6;
					} else if ( s.charAt(i+1) == 'q' && s.charAt(i+2) == 'u' && s.charAt(i+3) == 'o' && s.charAt(i+4) == 't' && s.charAt(i+5) == ';') {
						str.append('"');
						i += 6;
					} else {
						str.append(ch);
					}
				} else if ( ( len - i ) > 4 ) {
					if ( s.charAt(i+1) == 'a' && s.charAt(i+2) == 'm' && s.charAt(i+3) == 'p' && s.charAt(i+4) == ';' ) {
						str.append('&');
						i += 5;
					} else {
						str.append(ch);
					}
				} else if ( ( len - i ) > 3 ) {
					if ( s.charAt(i+1) == 'l' && s.charAt(i+2) == 't' && s.charAt(i+3) == ';' ) {
						str.append('<');
						i += 4;
					} else if ( s.charAt(i+1) == 'g' && s.charAt(i+2) == 't' && s.charAt(i+3) == ';' ) {
						str.append('>');
						i += 4;
					} else {
						str.append(ch);
					}
				} else {
					str.append(ch);
				}
			}
		}
		return str.toString();
	}
	
	public static String base64encode(String org) {
		return Base64Coder.encodeString(org);
	}
	
	static ArrayList<NetworkInterface> AVAIL_INTERFACES = new ArrayList<NetworkInterface>();

	public static ArrayList<NetworkInterface> getAvailiableNetworkInterfaces() {
		if ( AVAIL_INTERFACES.size() == 0 ) {
			// TODO : MODIFY BELOW LINES.
			try {
				AVAIL_INTERFACES.add(NetworkInterface.getNetworkInterfaces().nextElement());
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			//AVAIL_INTERFACES.addAll(NetworkInterface.getNetworkInterfaces());
		}
		return AVAIL_INTERFACES;
	}
	
}

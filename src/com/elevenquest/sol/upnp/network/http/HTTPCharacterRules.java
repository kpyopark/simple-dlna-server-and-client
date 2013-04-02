package com.elevenquest.sol.upnp.network.http;

/**
 * 2.2 Basic Rules
 * 
 *        OCTET          = <any 8-bit sequence of data>
 *        CHAR           = <any US-ASCII character (octets 0 - 127)>
 *        UPALPHA        = <any US-ASCII uppercase letter "A".."Z">
 *        LOALPHA        = <any US-ASCII lowercase letter "a".."z">
 *        ALPHA          = UPALPHA | LOALPHA
 *        DIGIT          = <any US-ASCII digit "0".."9">
 *        CTL            = <any US-ASCII control character
 *        (octets 0 - 31) and DEL (127)>
 *        CR             = <US-ASCII CR, carriage return (13)>
 *        LF             = <US-ASCII LF, linefeed (10)>
 *        SP             = <US-ASCII SP, space (32)>
 *        HT             = <US-ASCII HT, horizontal-tab (9)>
 *        <">            = <US-ASCII double-quote mark (34)>
 * 
 * NOT IMPLEMENTED YET.
 * 
 * We are going to make HTTP1.1 Full support HTTP connection.
 * So, we are preparing the basic classes suitable for HTTP1.1 spec.
 * 
 * @author user
 *
 */
public class HTTPCharacterRules {
	
	public static String HTTP_RULES_OCTET = "OCTET";
	public static String HTTP_RULES_CHAR = "CHAR";
	public static String HTTP_RULES_UPALPHA = "UPALPHA";
	public static String HTTP_RULES_LOALPHA = "LOALPHA";
	public static String HTTP_RULES_DIGIT = "DIGIT";
	public static String HTTP_RULES_CTL = "CTL";
	public static String HTTP_RULES_CR = "CR";
	public static String HTTP_RULES_LF = "LF";
	public static String HTTP_RULES_SP = "SP";
	public static String HTTP_RULES_HT = "HT";
	public static String HTTP_RULES_DOUBLE_QUOTE = "<\">";
	
	
}

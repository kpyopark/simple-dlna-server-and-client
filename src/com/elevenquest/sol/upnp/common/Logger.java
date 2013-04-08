package com.elevenquest.sol.upnp.common;

public class Logger {
	
	public static int DEBUG = 0;
	public static int INFO = 1;
	public static int WARNING = 2;
	public static int ERROR = 3;
	
	private static String getLevelTitle(int level) {
		return ( level == DEBUG ) ? "[DEBUG]" :
			(level == INFO ) ? "[INFO]" :
			(level == WARNING ) ? "[WARNING]" :
			(level == ERROR ) ? "[ERROR]" : "[DEBUG]";
	}
	
	private static boolean isLoggingTarget(int level) {
		return (level >= DEBUG);
	}
	
	public static void println(int level, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLevelTitle(level));
		sb.append(msg);
		if ( isLoggingTarget(level) )
			System.out.println(sb.toString());
	}

	public static void println(int level, int msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLevelTitle(level));
		sb.append(msg);
		if ( isLoggingTarget(level) )
			System.out.println(sb.toString());
	}

	public static void println(int level, Object msg) {
		if ( msg instanceof Throwable ) {
			StringBuffer sb = new StringBuffer();
			Throwable error = (Throwable)msg;
			sb.append(getLevelTitle(level));
			sb.append(getLevelTitle(ERROR));
			sb.append(error.getMessage());
			if ( isLoggingTarget(level) )
				System.out.println(sb.toString());
			error.printStackTrace();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(getLevelTitle(level));
			sb.append(msg);
			if ( isLoggingTarget(level) )
				System.out.println(sb.toString());
		}
	}
}

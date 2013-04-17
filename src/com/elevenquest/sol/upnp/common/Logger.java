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
	
	/*
	private static void android_log(int level, String msg) {
		String tag = "ANONY";
		switch ( level ) {
		case 0 :
			Log.d(tag, msg);
			break;
		case 1 :
			Log.i(tag, msg);
			break;
		case 2 :
			Log.w(tag, msg);
			break;
		case 3 :
			Log.e(tag, msg);
			break;
		}
	}
	*/
	
	public static void println(int level, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLevelTitle(level));
		sb.append(msg);
		if ( isLoggingTarget(level) )
			System.out.println(sb.toString());
			//android_log(level, msg);
	}

	public static void println(int level, int msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLevelTitle(level));
		sb.append(msg);
		if ( isLoggingTarget(level) )
			System.out.println(sb.toString());
			//android_log(level, msg + "");
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
				//android_log(level, msg.toString());
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

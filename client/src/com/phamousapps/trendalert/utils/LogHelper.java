package com.phamousapps.trendalert.utils;


public class LogHelper {

	public static final boolean ENABLE_LOGS = true;
	
	public static boolean isLoggable(String tag){
		//return Log.isLoggable(tag, Log.DEBUG) && ENABLE_LOGS;
		return ENABLE_LOGS;
	}
	
}

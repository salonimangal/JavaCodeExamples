package com.mckesson.smora.util;

import java.text.SimpleDateFormat;


public class SchedulerUtil {
	public final static String EMPTY_STRING = "";
	public static int intOf(String intString, int defaultValue) {
		int result = defaultValue;
		try {
			result = Integer.valueOf(intString).intValue();
		} catch (Exception e) {
			result = defaultValue;
		}
		return (result);
	}

	public static long longOf(String longString, long defaultValue) {
		long result = defaultValue;
		try {
			result = Long.valueOf(longString).longValue();
		} catch (Exception e) {
			result = defaultValue;
		}
		return (result);
	}

	public static String trim(String string) {
		if (string == null) {
			return (EMPTY_STRING);
		}
		return (string.trim());
	}
	 public static String formatAutoSendDate(java.util.Date s, String formatString) {
	    	SimpleDateFormat ft = new SimpleDateFormat(formatString);
	    	if(s != null) {
	    		String str = ft.format(s);
	    	    return str;
	    	} else {
	            return "";
	    	}
	    }
	 
	
	 public static String convertToString(String[] list){
		 StringBuffer sb=new StringBuffer();
		 for(int i=0;i<list.length;i++){
			sb=sb.append(list[i]);
		 }
		 return sb.toString();
	 }
	

	 
}

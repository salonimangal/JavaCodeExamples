package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.mckesson.smora.database.dao.CriteriaTemplateDB;
import com.mckesson.smora.database.dao.SchedularDB;
import com.mckesson.smora.exception.SMORAException;

public class TimeZoneDetail {
	
	/**
	 * The CLASSNAME.
	 */
	private final static String CLASSNAME = "TimeZoneDetail";
	/**

	/**
	 * The template DB.
	 */
	private static SchedularDB schedularDB = null;
	
	 
        public static Map getZoneCombo() throws SMORAException {
		
		final String METHODNAME = "getZoneCombo";
		try{
			schedularDB = new SchedularDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
			return schedularDB.getZoneInfo();
		
	
	  }

	     public static Map getTimeCombo() throws SMORAException {
    		
    		final String METHODNAME = "getTimeCombo";
    		try{
    			schedularDB = new SchedularDB();
    			}
    			catch(Exception e)
    			{
    				throw new SMORAException(e,CLASSNAME,METHODNAME);
    			}
    			return schedularDB.getTimeInfo();
    		
    	
    	}

}

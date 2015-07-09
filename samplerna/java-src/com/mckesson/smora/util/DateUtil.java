/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The Class DateUtil.
 */
    public class DateUtil  {

	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "DateUtil";
    /**
     * The log.
     */
    private static Log log = LogFactory.getLog(DateUtil.class);
    /**
     * The date pattern.
     */
    private static String datePattern = "MM/dd/yyyy";
    /**
     * The time pattern.
     */
    private static String timePattern = datePattern + " HH:MM a";
    /**
     * The default date pattern.
     */
    private static String defaultDatePattern = "EEE MMM dd HH:MM:ss z yyyy";

    /**
     * This method gets the date pattern.
     * 
     * @return the date pattern
     */
    public static String getDatePattern() {
        return datePattern;
    }

    /**
     * This method gets the date.
     * 
     * @param aDate the a date
     * 
     * @return the date
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method converts the string to date.
     * 
     * @param aMask the a mask
     * @param strDate the str date
     * 
     * @return the date
     * 
     * @throws ParseException the parse exception
     */
    public static final Date convertStringToDate(String aMask, String strDate)
    throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" +
                      aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            if (log.isDebugEnabled()) {
                log.debug("conversion failed, trying default date format");
            }

            df = new SimpleDateFormat(defaultDatePattern);

            try {
                date = df.parse(strDate);
            } catch (ParseException pe2) {
                throw new ParseException(pe2.getMessage(), pe2.getErrorOffset());
            }

            if (log.isDebugEnabled()) {
                log.debug("formatted date successfully!");
            }
        }

        return (date);
    }

    /**
     * This method gets the time now.
     * 
     * @param theTime the the time
     * 
     * @return the time now
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method gets the today.
     * 
     * @return the today
     * 
     * @throws ParseException the parse exception
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method gets the date time.
     * 
     * @param aMask the a mask
     * @param aDate the a date
     * 
     * @return the date time
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method converts the date to string.
     * 
     * @param aDate the a date
     * 
     * @return the string
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
    }

    /**
     * This method converts the string to date.
     * 
     * @param strDate the str date
     * 
     * @return the date
     * 
     * @throws ParseException the parse exception
     */
    public static Date convertStringToDate(String strDate)
    throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + datePattern);
            }

            aDate = convertStringToDate(datePattern, strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate +
                      "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return aDate;
    }
    public static Calendar gettodayDate(){
        Calendar today = Calendar.getInstance();

        int year = today.get(today.YEAR);
        int month = today.get(today.MONTH);
        int date = today.get(today.DAY_OF_MONTH);

        today.set(Calendar.YEAR, year);
        today.set(Calendar.MONTH, month);
        today.set(Calendar.DAY_OF_MONTH, date);
        return today;
      }
   
    
}

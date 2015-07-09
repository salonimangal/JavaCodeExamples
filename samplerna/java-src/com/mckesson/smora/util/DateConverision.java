/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * The Class DateConverision.
 */
public class DateConverision
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "DateConverision";
	
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(DateConverision.class);
	
	/**
	 * The month and year.
	 */
	private ArrayList monthAndYear= new ArrayList();
	
	/**
	 * The month VS same month last year list.
	 */
	private ArrayList monthVSSameMonthLastYearList = new ArrayList();

	/**
	 * The qtr VS same qtr last year list.
	 */
	private ArrayList qtrVSSameQtrLastYearList = new ArrayList();

	/**
	 * The month VS previous month list.
	 */
	private ArrayList monthVSPreviousMonthList = new ArrayList();

	/**
	 * The qtr VS previour qtr list.
	 */
	private ArrayList qtrVSPreviourQtrList = new ArrayList();

	/**
	 * The year VS previou year list.
	 */
	private ArrayList yearVSPreviouYearList = new ArrayList();
	
	/**
	 * The date pattern.
	 */
	private String    datePattern = "MM/dd/yyyy";
	
	/**
	 * The default date pattern.
	 */
	private String defaultDatePattern = "EEE MMM dd HH:MM:ss z yyyy";

	/**
	 * This method gets the today.
	 * 
	 * @return the today
	 * 
	 * @throws ParseException the parse exception
	 */
	public Calendar getToday() throws ParseException {
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
	 * This method converts the string to date.
	 * 
	 * @param strDate the str date
	 * 
	 * @return the date
	 * 
	 * @throws ParseException the parse exception
	 */
	public Date convertStringToDate(String strDate)
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
	
	public String formatDate(String strDate){
		try{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = convertStringToDate("MM/dd/yyyy",strDate);
        strDate = df.format(date);
		return strDate;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public String formatDate(String strDate, String datePattern){
		try{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = convertStringToDate(datePattern,strDate);
        strDate = df.format(date);
		return strDate;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * This method added in
	 * SO -0953 to format the custom date
	 * @param strDate
	 * @param datePattern
	 * @return
	 */
	public String formatCustomDate(String strDate, String datePattern){
		try{
			SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
			Date date = convertStringToDate(datePattern,strDate);
			strDate = df.format(date);
			return strDate;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
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
	public final Date convertStringToDate(String aMask, String strDate)
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
	 * This method gets the last months.
	 * 
	 * @return the last months
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getLastMonths() throws Exception
	{
		ArrayList lastMonths = new ArrayList();
		Calendar calendar= DateUtil.getToday(); 
		//java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMMMMMMMM yyyy");
		int month = calendar.get(Calendar.MONTH) + 1;
		for(int i = 0; i <= 27; i++)
		{
			lastMonths.add(String.valueOf(i+1));
		}
		return lastMonths;
	}
	
	/**
	 * This method gets the last months.
	 * 
	 * @return the last months
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getLastMonthsQuarterly() throws Exception
	{
		ArrayList lastMonths = new ArrayList();
		Calendar calendar= DateUtil.getToday(); 
		//java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMMMMMMMM yyyy");
		int month = calendar.get(Calendar.MONTH) + 1;
		for(int i = 0; i <= 11; i++)
		{
			lastMonths.add(String.valueOf(i+1));
		}
		return lastMonths;
	}

	
	/**
	 * This method gets the month and year.
	 * 
	 * @return the month and year
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getMonthAndYear()throws Exception
	{
		ArrayList monthAndYear = new ArrayList();
		Calendar calendar = DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMMMMMMMM yyyy");
		monthAndYear.add("");
		for(int i = 0; i < 29; i++)
		{
			if(i == 0)
			{
				calendar.add(Calendar.MONTH, +1);
			}
			else
			{
				calendar.add(Calendar.MONTH, -1);
				monthAndYear.add(formatter.format(calendar.getTime()) + "");
			}
		}
		return monthAndYear;
	}
	
	public static ArrayList getStartDateList()throws Exception
	{
		ArrayList startDateList = new ArrayList();
		Calendar calendar = DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
		startDateList.add("");
		for(int i = 0; i < 29; i++)
		{
			if(i == 0)
			{
				calendar.add(Calendar.MONTH, +1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
			}
			else
			{
				calendar.add(Calendar.MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				startDateList.add(formatter.format(calendar.getTime()) + "");
			}
		}
		return startDateList;
	}
	
	public static ArrayList getEndDateList()throws Exception
	{
		ArrayList endDateList = new ArrayList();
		Calendar calendar = DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
		endDateList.add("");
		for(int i = 0; i < 29; i++)
		{
			if(i == 0)
			{
				calendar.add(Calendar.MONTH, +1);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			}
			else
			{
				calendar.add(Calendar.MONTH, -1);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDateList.add(formatter.format(calendar.getTime()) + "");
			}
		}
		return endDateList;
	}
	
	/**
	 * This method gets the MHS month and year.
	 * 
	 * @return the MHS month and year
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getMHSMonthAndYear()throws Exception
	{
		ArrayList monthAndYear = new ArrayList();
		Calendar calendar = DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMMMMMMMM yyyy");
		monthAndYear.add("");
		for(int i = 0; i < 4; i++)
		{
			if(i == 0)
			{
				calendar.add(Calendar.MONTH, 0);
			}
			else
			{
				calendar.add(Calendar.MONTH, -1);
				monthAndYear.add(formatter.format(calendar.getTime()) + "");
			}
		}
		return monthAndYear;
	}
	/**
	 * This method gets the month VS previous month list.
	 * 
	 * @return the month VS previous month list
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getMonthVSPreviousMonthList()throws Exception
	{
		ArrayList monthVSPreviousMonthList=new ArrayList();
		Calendar calendar= DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMM yyyy");
		
		String monthVSPreviousMonth=null;
		
		for(int i=0;i<27;i++)
		{
			monthVSPreviousMonth= formatter.format(calendar.getTime())  +" Vs " ;
			calendar.add(Calendar.MONTH,-1);
			monthVSPreviousMonth=monthVSPreviousMonth+formatter.format(calendar.getTime());
			monthVSPreviousMonthList.add(monthVSPreviousMonth);
		}
			
		return monthVSPreviousMonthList;
	}

	/**
	 * This method gets the month VS same month last year list.
	 * 
	 * @return the month VS same month last year list
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getMonthVSSameMonthLastYearList()throws Exception
	{
		ArrayList monthVSSameMonthLastYearList=new ArrayList();
		
		Calendar calendar1= DateUtil.getToday(); 
		Calendar calendar2= DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMM yyyy");
		
		calendar2.add(Calendar.YEAR,-1);
		String monthVSSameMonthLastYear=null;
		
		for(int i=0;i<16;i++)
		{
			monthVSSameMonthLastYear= formatter.format(calendar1.getTime())  +" Vs " + formatter.format(calendar2.getTime());
			calendar1.add(Calendar.MONTH,-1);
			calendar2.add(Calendar.MONTH,-1);
			monthVSSameMonthLastYearList.add(monthVSSameMonthLastYear);
		}
		return monthVSSameMonthLastYearList;
	}

	/**
	 * This method gets the qtr VS previour qtr list.
	 * 
	 * @return the qtr VS previour qtr list
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getQtrVSPreviourQtrList()throws Exception
	{
		ArrayList qtrVSPreviourQtrList=new ArrayList();
		
		Calendar calendar1= DateUtil.getToday(); 
		Calendar calendar2= DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy");
		
		calendar2.add(Calendar.YEAR,-1);
		String qtrVSPreviourQtr=null;
		int month;
		int quater;
		int j=1;
		Calendar cal = DateUtil.getToday();
		cal.add(Calendar.MONTH, -28);
		if((cal.get(Calendar.MONTH) == Calendar.FEBRUARY) ||(cal.get(Calendar.MONTH) == Calendar.MAY)||(cal.get(Calendar.MONTH) == Calendar.AUGUST)||(cal.get(Calendar.MONTH) == Calendar.NOVEMBER))
		{
			j = 0;
		}
		for(int i=0;i<8+j;i++)
		{
			month=calendar1.get(Calendar.MONTH);
			if(month == 0)
			{
				quater=(month+1/3)+1;
			}
			else
			{
			quater=(month/3)+1;
			}
			qtrVSPreviourQtr= "Q"+quater+" "+formatter.format(calendar1.getTime())  +" Vs " ;
			calendar1.add(Calendar.MONTH,-3);
			month=calendar1.get(Calendar.MONTH);
			quater=(month/3)+1;
			
			qtrVSPreviourQtr= qtrVSPreviourQtr + "Q"+quater+" "+formatter.format(calendar1.getTime());
           	qtrVSPreviourQtrList.add(qtrVSPreviourQtr);
		}
		return qtrVSPreviourQtrList;
	}

	/**
	 * This method gets the qtr VS same qtr last year list.
	 * 
	 * @return the qtr VS same qtr last year list
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getQtrVSSameQtrLastYearList()throws Exception
	{
		ArrayList qtrVSSameQtrLastYearList=new ArrayList();
		
		Calendar calendar1= DateUtil.getToday(); 
		Calendar calendar2= DateUtil.getToday(); 
        
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy");
		
		calendar2.add(Calendar.YEAR,-1);
		String qtrVSSameQtrLastYear=null;
		int month;
		int quater;
		int j=1;
		Calendar cal = DateUtil.getToday();
		cal.add(Calendar.MONTH, -28);
		if((cal.get(Calendar.MONTH) == Calendar.FEBRUARY) ||(cal.get(Calendar.MONTH) == Calendar.MAY)||(cal.get(Calendar.MONTH) == Calendar.AUGUST)||(cal.get(Calendar.MONTH) == Calendar.NOVEMBER))
		{
			j = 0;
		}
		for(int i=0;i<(5+j);i++)
		{
			month=calendar1.get(Calendar.MONTH);
			if(month == 0)
			{
				quater=(month+1/3)+1;
			}
			else
			{
			quater=(month/3)+1;
			}
			
			qtrVSSameQtrLastYear= "Q"+quater+" "+formatter.format(calendar1.getTime())  +" Vs " +"Q"+quater+" "+formatter.format(calendar2.getTime()) ;
			calendar1.add(Calendar.MONTH,-3);
			calendar2.add(Calendar.MONTH,-3);
			qtrVSSameQtrLastYearList.add(qtrVSSameQtrLastYear);
		}
		return qtrVSSameQtrLastYearList;
	}

	/**
	 * This method gets the year VS previou year list.
	 * 
	 * @return the year VS previou year list
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getYearVSPreviouYearList()throws Exception
	{
		ArrayList yearVSPreviouYearList=new ArrayList();	
		
		Calendar calendar1= DateUtil.getToday(); 
		Calendar calendar2= DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy");
		
		calendar2.add(Calendar.YEAR,-1);
		String yearVSPreviouYear=null;
		int j=0;
		Calendar cal = DateUtil.getToday();
		cal.add(Calendar.MONTH, -28);
		if(cal.get(Calendar.MONTH) == Calendar.DECEMBER)
		{
			j = 3;
		}
		else
		{
			j = 2;
		}
		for(int i=0;i<j;i++)
		{
			yearVSPreviouYear= formatter.format(calendar1.getTime())  +" Vs " + formatter.format(calendar2.getTime());
			calendar1.add(Calendar.YEAR,-1);
			calendar2.add(Calendar.YEAR,-1);
			yearVSPreviouYearList.add(yearVSPreviouYear);
		}
		return yearVSPreviouYearList;
	}
	          
	/**
	 * This method gets the PDD month and year.
	 * 
	 * @return the PDD month and year
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getPDDMonthAndYear()throws Exception
	{
		ArrayList monthAndYear = new ArrayList();
		Calendar calendar = DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMMMMMMMMM yyyy");
		monthAndYear.add("");
		for(int i = 0; i < 29; i++)
		{
			if(i == 0)
			{
				calendar.add(Calendar.MONTH, 0);
			}
			else
			{
				calendar.add(Calendar.MONTH, -1);
				monthAndYear.add(formatter.format(calendar.getTime()) + "");
			}
		}
		return monthAndYear;
	}
	
	/**
	 * This method gets the month and year for AssetMgt obsolete.
	 * 
	 * @return the month and year
	 * 
	 * @throws Exception the exception
	 */
	public static ArrayList getAssetMgtMonthAndYear()throws Exception
	{
		ArrayList monthAndYear = new ArrayList();
		Calendar calendar = DateUtil.getToday(); 
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMM yyyy");
		for(int i = 0; i < 28; i++)
		{
			calendar.add(Calendar.MONTH, -1);
			monthAndYear.add(formatter.format(calendar.getTime()) + "");
		}
		return monthAndYear;
	}
}
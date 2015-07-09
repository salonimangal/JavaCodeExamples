/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Iterator;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;


import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.AccountGroupDB;
import com.mckesson.smora.database.dao.ReportStatusDB;
import com.mckesson.smora.database.model.TApplAcctGrp;
import com.mckesson.smora.database.util.DBUtil;
import com.mckesson.smora.database.util.HibernateTemplate;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.util.CriteriaTemplateUtil;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.MHSReportFormatVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.MHSCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CustomReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.ReportStatusVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORADatabaseException;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.appl.reportsengine.ReportGenerator;
import com.mckesson.smora.appl.reportsengine.ReportManager;
import com.mckesson.smora.appl.util.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * The Class ReportFileGenerateBatch.
 */

public class ReportFileGenerateBatch
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ReportFileGenerateBatch";
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(ReportFileGenerateBatch.class);
	/**
	 * The report status DB.
	 */
	private ReportStatusDB reportStatusDB;
	
	private ReportStatusVO reportStatusVO;
	
	/**
	 * The template.
	 */
	private HibernateTemplate template = null;
	/**
	 * The report details VO list.
	 */
	ArrayList reportDetailsVOList = new ArrayList();
	/**
	 * The start month.
	 */
	String startMonth="";
	
	String heading="";	
	/**
	 * The end month.
	 */
	String endMonth="";
	/**
	 * The submit date.
	 */
	String submitDate="";
	/**
	 * The account list.
	 */
	String accountList = "";
	/**
	 * The custom heading.
	 */
	String customHeading = "";
	/**
	 * The cog adjustment.
	 */
	String cogAdjustment = "";
	/**
	 * The header map.
	 */
	Map headerMap = new HashMap();
	/**
	 * The report format.
	 */
	String reportFormat;
	/**
	 * The count.
	 */
	int count;
	/**
	 * The outputstream
	 */
	private OutputStream outputStream = null;
	
	int[] daysInMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	
	ArrayList reportUserRolesVOList = new ArrayList();
	/**
	 * Getreport ID list.
	 * 
	 * @return the report ID list
	 */
	protected List getreportIDList() throws SMORAException	
	{
		final String METHODNAME = "getreportIDList";
		List reportList = null;
		reportStatusDB = new ReportStatusDB();
		try
		{	
			log.info("Retriving the Report List");
			reportList = reportStatusDB.getReportStatusForSubmarine(ReportManagerConstants.SUBMARINE_REPORT);
			
		}
		catch(SMORADatabaseException e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Failed to get the ReportList from Database");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		return reportList;
	}
	
	
	/**
	 * This method gets the user ID list.
	 * 
	 * @return the user ID list
	 */
	protected List getUserIDList() throws SMORAException	
	{
		final String METHODNAME = "getUserIDList";
		List reportList = null;
		Date date = new Date();
		
		reportStatusDB = new ReportStatusDB();
		try
		{	
			log.info("Retriving the ReportUserIDs");
			reportList = reportStatusDB.getReportUserIDForSubmarine(ReportManagerConstants.SUBMARINE_REPORT);
			
		}
		catch(SMORADatabaseException e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Failed to get the ReportUserList from Database");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		return reportList;
	}

	/**
	 * This method gets the value object.
	 * 
	 * @param reportIDList the report ID list
	 * 
	 * @return the value object
	 */
	protected ArrayList getValueObject(ArrayList reportIDList)throws SMORAException
	{
		final String METHODNAME = "getValueObject";
		int reportID;
		String xmlData;
		ArrayList voList = new ArrayList();
		ReportBaseVO rptBaseVO = null;
		ReportStatusDB reportStatusDB = new ReportStatusDB();
		
		try
		{
			log.info("Retriving the ReportBaseVO from ReportIDs");
			for(int i=0;i<reportIDList.size();i++)
			{
				reportID =  (Integer)reportIDList.get(i);
				reportDetailsVOList.add(reportStatusDB.getByPrimaryKey(reportID));
				xmlData = reportStatusDB.getCriteriaTemplateXml(reportID);
				rptBaseVO = CriteriaTemplateUtil.convertXMLToVO(xmlData);
			    voList.add(rptBaseVO);
			}
		}
		catch(SMORADatabaseException e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Failed to retrive the Value Objects");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		return voList;
	}
	
	
	
	 /**
	 * This method gets the cust details.
	 * 
	 * @param rptUserIDList the rpt user ID list
	 * @param rptBaseVOList the rpt base VO list
	 * 
	 * @return the cust details
	 */
	protected ArrayList getCustDetails(ArrayList rptBaseVOList,ArrayList rptUserIDList) throws SMORAException	
	{
		final String METHODNAME = "getCustDetails";
			ArrayList reportCustAccntList = new ArrayList();
			Date date = new Date();
			CustomReportVO customRprtVO;
			MHSCriteriaVO mhsCriteriaVO;
			CustomerVO custVO; 
			AccountDetailsListVO accntDetailsListVO;
			ReportBaseVO reportBsVO;
			DateSelectionAndComparisonVO dateselectionVO;
			Iterator rptBaseIterator = rptBaseVOList.iterator();
			ArrayList accountDetailsList = null;
			UserRolesVO userRolesVO;
			AccountDB accountDB = new AccountDB();
			
			try
			{
				log.info("Retriving the CustomerDetails");
				for(int k=0;k<rptBaseVOList.size();k++)
				{
					List custAccntDetaillist = new ArrayList();
					reportBsVO = (ReportBaseVO)rptBaseVOList.get(k);
					customRprtVO = reportBsVO.getCustomReportVO();
					CriteriaVO criteriaVO = customRprtVO.getCustReportCriteriaVO();
					custVO = criteriaVO.getCustomerVO();
					accntDetailsListVO = custVO.getAccountDetailsListVO();
					
					if(accntDetailsListVO.getAccountDetailsVOList()!=null)
					{
						accountDetailsList = accntDetailsListVO.getAccountDetailsVOList();
											
							for(int i=0;i<accountDetailsList.size();i++)
							{
								AccountDetailsVO accountDetailVO = null;
								accountDetailVO = (AccountDetailsVO)accountDetailsList.get(i);
								custAccntDetaillist.add(i,accountDetailVO.getAccountNum());
							}
					}
										
					userRolesVO=custVO.getRoles();
					reportUserRolesVOList.add(userRolesVO);
					ArrayList cidList = accountDB.getAccountInfo(userRolesVO); 
					AccountDetailsVO accDtlsVO = null;
					for(int i=0;i<cidList.size();i++)
					{
						accDtlsVO = (AccountDetailsVO)cidList.get(i);
						custAccntDetaillist.add(accDtlsVO.getAccountNum());
					}
					reportCustAccntList.add(custAccntDetaillist);					
				}
			}
			catch(SMORADatabaseException e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
			catch(Exception e)
			{
				log.error("Failed to retrive the CustomerDetails");
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
			
			
			return reportCustAccntList;
		}
	
	
	
	
	 /**
 	 * This method gets the cust selected month.
 	 * 
 	 * @param reportBaseVOList the report base VO list
 	 * 
 	 * @return the cust selected month
 	 */
 	protected ArrayList getCustSelectedMonth(ArrayList reportBaseVOList) throws SMORAException
	 {
 		final String METHODNAME = "getCustSelectedMonth";
		 	ArrayList custMonthSelectedList = new ArrayList();
		 	CustomReportVO customRprtVO;
			CriteriaVO criteriaVO;
			CustomerVO custVO; 
			AccountDetailsListVO accntDetailsListVO;
			ReportBaseVO reportBsVO;
			DateSelectionAndComparisonVO dateselectionVO;
			TimePeriodListVO timePeriodListVO;
			TimePeriodVO timePeriodVO;
			String startDate;
			String endDate;
			String strDate;
			Date newDate = new Date();
			String[] dateCollection = new String[2];
			String fromFormat = "MMMM yyyy";
			String toFormat = "MM/dd/yyyy";
			SimpleDateFormat formatter = new SimpleDateFormat(fromFormat);
			SimpleDateFormat toFormatter = new SimpleDateFormat(toFormat);
			Date fromParseDate;
			Date toParseDate;
			String fromFormatDate;
			String toFormatDate;
			
			Iterator rptBaseIterator = reportBaseVOList.iterator();
			
			log.info("Retriving the CustomerSelectedMonth");
			while(rptBaseIterator.hasNext())
			{
				reportBsVO = (ReportBaseVO) rptBaseIterator.next();
				customRprtVO = reportBsVO.getCustomReportVO();
				criteriaVO = customRprtVO.getCustReportCriteriaVO();
				dateselectionVO = criteriaVO.getDateSelectionAndComparisonVO();
				timePeriodListVO = dateselectionVO.getTimePeriodsVOList();
				ArrayList timePeriodList = timePeriodListVO.getTimeperiodVOList();
				try
				{
				
					for(int i=0;i<timePeriodList.size();i++)
					{
						timePeriodVO = (TimePeriodVO)timePeriodList.get(i);
						startDate = timePeriodVO.getStartDate();
						endDate = timePeriodVO.getEndDate();
						
						if(startDate!=""&&endDate!="")
						{
							fromParseDate= formatter.parse(startDate);
							toParseDate= formatter.parse(endDate);
							
							fromFormatDate = toFormatter.format(fromParseDate);
							toFormatDate = this.getLastDate(toFormatter.format(toParseDate));
							try
							{
								dateCollection[0]= fromFormatDate;
								dateCollection[1]= toFormatDate;
							}
							catch(Exception e)
							{
								log.error("Failed to parse the Date");
							}
							custMonthSelectedList.add(i,dateCollection);
						}
					}
				}
				catch(Exception e)
				{
					log.error("Failed to get the CustomerMonthSelection");
					throw new SMORAException(e,CLASSNAME,METHODNAME);
				}
			}
		 
		 return custMonthSelectedList;
	 }
	 
 	/**
 	 * This Method returns the last day of the Selected Month
 	 * @param date
 	 * @return date
 	 */
 	private String getLastDate(String date) throws SMORAException
 	{
 		final String METHODNAME = "getLastDate";
 		String modifiedDate=null;
 		int month = Integer.parseInt(date.substring(0,2));
 		int year = Integer.parseInt(date.substring(6,10));
  		int dateInt=0;
 		
 		try
 		{
	 		if(month==0)
	 		{
	 			dateInt = daysInMonths(year,11);
			}
			else
			{
				dateInt = daysInMonths(year,month-1);
			}	 		
 		}
 		catch (Exception e)
		{
			throw new SMORAException(e, CLASSNAME, METHODNAME);
		}
 		if(month <10)
 		{
 			modifiedDate = "0"+month+"/"+dateInt+"/"+year;
 		}
 		else
 		{
 			modifiedDate = ""+month+"/"+dateInt+"/"+year;
 		}
 		
 		return modifiedDate;
 	}
 	
 	/**
 	 * This Method calculates the Days in a Month
 	 * @param year
 	 * @param month
 	 * @return int
 	 * @throws SMORAException
 	 */
 	public int daysInMonths(int year, int month) throws SMORAException
	{
		final String METHODNAME = "daysInMonths";
		try
		{
			daysInMonths[1] += checkLeapYear(year + "") ? 1 : 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SMORAException(e, CLASSNAME, METHODNAME);
		}
		return daysInMonths[month];

	}
 	
 	/**
 	 * This method checks the year for Leap Year
 	 * @param year
 	 * @return boolean
 	 * @throws SMORAException
 	 */
	boolean checkLeapYear(String year) throws SMORAException
	{
		final String METHODNAME = "checkLeapYear";
		try
		{
			long yearAsLong = Long.parseLong(year);
			boolean results[] = { false, false, false, false, true, false, false, true };
			if (results[((((yearAsLong % 4) == 0) ? 1 : 0) << 2) + ((((yearAsLong % 100) == 0) ? 1 : 0) << 1) + ((((yearAsLong % 400) == 0) ? 1 : 0) << 0)])
			{
				return true;
			}
			else
			{
				return false;
			}

		}
		catch (Exception e)
		{
			throw new SMORAException(e, CLASSNAME, METHODNAME);
		}

	}
 	
	/**
	 * Compile jasper report.
	 * 
	 * @param format the format
	 * 
	 * @return the jasper report
	 * 
	 * @throws JRException the JR exception
	 */
	private JasperReport compileJasperReport() throws JRException 
	{
			log.info("Compiling the JasperReports");
			InputStream inStream = ReportFileGenerateBatch.class.getClassLoader().getResourceAsStream("MHSBreakAccountReport.jrxml");
			return JasperCompileManager.compileReport(inStream);
			//log.info("Compiling the JasperReports");
			//JasperCompileManager jcompile= new JasperCompileManager() ;
			//return jcompile.compileReport("/web/app/smornaDomain/properties/templates/MHSBreakAccountReport.jrxml");
	}
	
	
	/**
	 * This method gets the jasper report object.
	 * 
	 * @param rptFormat the rpt format
	 * 
	 * @return the jasper report object
	 * 
	 * @throws JRException the JR exception
	 */
	private JasperReport getJasperReportObject() throws JRException 
	{
		log.info("Getting the ReportFormat");
		return this.compileJasperReport();
	}
	
		
	
	/**
	 * This method gets the report detail data.
	 * 
	 * @param endMonth the end month
	 * @param startMonth the start month
	 * @param custID the cust ID
	 * 
	 * @return the report detail data
	 */
	private List getReportDetailData(ReportBaseVO rptBaseVO,List custID,String startMonth,String endMonth,String userID) throws SMORAException	
	{
		final String METHODNAME = "getReportDetailData";
		List<Object[]> detailsList = null;
		Object[] detailsObj;
		String query = null;
		String str = "";
		ReportStatusDB reportStatusDB = new ReportStatusDB();
		CustomerVO customerVO = null; 
		UserRolesVO userRolesVO = null;
		StringBuffer finalQuery = new StringBuffer();
		
		try
		{
						
			log.info("Retriving the ReportDetails");
			if(rptBaseVO != null)
			{				
				if(rptBaseVO.getCustomReportVO()!= null)
				{
					if(rptBaseVO.getCustomReportVO().getCustReportCriteriaVO()!=null)
					{
						if(rptBaseVO.getCustomReportVO().getCustReportCriteriaVO().getCustomerVO() != null)
						{
							customerVO = rptBaseVO.getCustomReportVO().getCustReportCriteriaVO().getCustomerVO();
						}
					}
				}
						
			}

			if(customerVO != null)
			{
				if(customerVO.getRoles() != null)
				{
					userRolesVO = customerVO.getRoles();
				}
			}
			
			String CustomerSelectionTabHigh = getCustomerSelectionTabWhereClauseHigh(userRolesVO);
			String CustomerSelectionTabLow = getCustomerSelectionTabWhereClauseForLow(custID);
			
			
			if(startMonth!=""&&endMonth!="")
			{		
				finalQuery.append(getFormedInitialQuery(startMonth,endMonth,userID));
			}
			
			if(userRolesVO != null)
			{
				if(userRolesVO.chnRoles != null || userRolesVO.cidRoles !=null || userRolesVO.hspRoles != null || userRolesVO.slsRoles != null)
				{

					if(CustomerSelectionTabHigh != null && !CustomerSelectionTabHigh.equals(""))
					{
						finalQuery.append(CustomerSelectionTabHigh);
						log.info("Inside the Customer High Selection");
					}
				}
				else
				{
					finalQuery.append(getCustAccountId(userID));
					log.info("Inside the Customer High Default Selection ");
				}
			}
			else if(customerVO != null)
			{
				if(CustomerSelectionTabLow != null && !CustomerSelectionTabLow.equals(""))
				{
					finalQuery.append(CustomerSelectionTabLow);
					log.info("Inside the Customer low Selection ");
				}
				else
				{
					finalQuery.append(getCustAccountId(userID));
					log.info("Inside the Customer low Default Selection ");
				}
			}				
				
			finalQuery.append(getEndQuery());
			query = finalQuery.toString();
			log.info("MHS Report Query: "+query);
			detailsList = reportStatusDB.getReportResult(query);				
			
			
		}
		catch(SMORADatabaseException e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Failed to get the ReportDetails");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		return detailsList;
	}
	
	private String getEndQuery()
	{
		String str = " and PH.sls_proc_wrk_dt between CRS.cntrc_beg_dt and CRS.cntrc_end_dt "+ 
		" ORDER BY ph.cust_acct_id, itm.gnrc_nam, itm.gnrc_dose_form_dscr, pkg_sz, mfg_sz";
		log.info("Inside the Final Query ");
		return str;
	}
	
	/**
	 * This method generates the report.
	 * 
	 * @param rptDetailList the rpt detail list
	 * @param count the count
	 * @param rptDetailFields the rpt detail fields
	 * @param reportFormat the report format
	 * @param headerMap the header map
	 */
	private void generateReport(List<Object[]> rptDetailList,String[] rptDetailFields,Map headerMap,Integer reportID,String userID,Date reportDate)  throws SMORAException
	{
		final String METHODNAME = "generateReport";
		String rptID = reportID.toString();
		try
		{
			
			log.info("Generating the MHSOneStopRealized Savings Report");
			String dateString = this.getDate();
			HibernateQueryResultDataSource hibernateDataSource = new HibernateQueryResultDataSource(rptDetailList,rptDetailFields);
			reportStatusDB = new ReportStatusDB();
			reportStatusVO = new ReportStatusVO();
			reportStatusVO.setReportId(reportID.intValue());
			reportStatusVO.setStartDate(new Date());
			reportStatusDB.updateIfFieldIsNotNull(reportStatusVO);
			JasperPrint jasperPrint = null;
			if(rptDetailList.size()>0)
			{
				jasperPrint = JasperFillManager.fillReport(this.getJasperReportObject(),headerMap,hibernateDataSource);
			}
			else
			{
				jasperPrint = JasperFillManager.fillReport(this.getJasperReportObject(),headerMap, new JREmptyDataSource());
			}
			outputStream = ReportManager.createPDFReport(userID, rptID, ReportManagerConstants.PDF_REPORT_EXTENSION, reportDate);
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
			pdfExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,outputStream);
			pdfExporter.exportReport();
			outputStream.close();			
		}
		catch (JRException e)
		{
			log.error("Failed to Generate the Report");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch (IOException e)
		{
			log.error("Failed to close the PDF outputStream");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Error Occused while generating the Report");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
	}
	
	/**
	 * This method gets the date.
	 * 
	 * @return the date
	 */
	private String getDate()
	{
		log.info("Getting the Date");
		String todayDateandTime;
		Date date = new Date();
		date.getDate();
		String format = "MM/dd/yyyy hh:mm";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		todayDateandTime= formatter.format(date);
		return todayDateandTime;
	}
	
	/**
	 * This method retruns the date.
	 * 
	 * @return the date
	 */
	private Date getCurrentDate()
	{
		final String METHODNAME = "getCurrentDate";
		log.info("Getting the Date");
		String todayDateandTime;
		Date newDate = null;
		Date date = new Date();
		date.getDate();
		try
		{
			String format = "MM/dd/yyyy hh:mm";
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			todayDateandTime= formatter.format(date);
			newDate = formatter.parse(todayDateandTime);
			
		}
		catch(Exception e)
		{
			log.error("Error while parsing the Date");
		}
		return newDate;		
	}
	
	/**
	 * 
	 * @param reportBaseVO
	 * @return customerString
	 */
	private ArrayList<String> getCustomerInfo(ReportBaseVO reportBaseVO)
	{
		ArrayList accountDetails = new ArrayList();
		ArrayList accountDetailsCommaSepList = new ArrayList();
		AccountDetailsListVO accountDetailsListVO = reportBaseVO.getCustomReportVO().getCustReportCriteriaVO().getCustomerVO().getAccountDetailsListVO();
		UserRolesVO userRolesVO = reportBaseVO.getCustomReportVO().getCustReportCriteriaVO().getCustomerVO().getRoles();
		if (accountDetailsListVO != null && accountDetailsListVO.getAccountDetailsVOList() != null)
		{
			ArrayList accountDetailsVOList = accountDetailsListVO.getAccountDetailsVOList();
			Iterator accountDetailsIterator = accountDetailsVOList.iterator();
			if(accountDetailsVOList.size() < 10)
			{
			    while (accountDetailsIterator.hasNext())
			    {
					AccountDetailsVO accountDetailsVO = (AccountDetailsVO) accountDetailsIterator.next();
					accountDetails.add(accountDetailsVO.getAccountNum() + " " + accountDetailsVO.getAccountName());
				}
			}
			else
			{
				while (accountDetailsIterator.hasNext())
			    {
					AccountDetailsVO accountDetailsVO = (AccountDetailsVO) accountDetailsIterator.next();
					accountDetails.add(accountDetailsVO.getAccountNum());
				}

			}
			Collections.sort(accountDetails);
			Iterator accountsIterator = accountDetails.iterator();
	    	while(accountsIterator.hasNext())
	    	{
	    		accountDetailsCommaSepList.add(accountsIterator.next());
	    		if(accountsIterator.hasNext())
	    		{
	    			accountDetailsCommaSepList.add(", ");
	    		}
	    	}
	    	return accountDetailsCommaSepList;
		}
		else if (userRolesVO != null)
		{
			ArrayList<RoleVO> roleVOList = new ArrayList();
			String heading = null;
			ArrayList nationalGroups = null; 
            ArrayList nationalSubgroups = null;
			ArrayList regions = null;
			ArrayList districts = null;
			
            ArrayList chains = null;
            ArrayList chainRegion = null;
            ArrayList chainDistrict = null;
            
			ArrayList dCs = null;
			ArrayList territories = null;
			
			ArrayList cid = null;
			
			try
			{   roleVOList = userRolesVO.getHspRoles();
				if(roleVOList != null)
				{
				  nationalGroups = new ArrayList();
				  nationalSubgroups = new ArrayList();
				  regions = new ArrayList();
				  districts = new ArrayList();
				  
				  Iterator roleVOListIterator = roleVOList.iterator();
				  while (roleVOListIterator.hasNext())
				  {
					  RoleVO nationalRoleVO = (RoleVO) roleVOListIterator.next();
                      String nationalKey = nationalRoleVO.getKey();
					  if(nationalKey != null)
					  {
							String[] nationalCode = (nationalKey).split(",");
							if(nationalCode.length == 2)
							{
								nationalGroups.add(nationalCode[1]);
							}
							else if(nationalCode.length == 3)
							{
								nationalSubgroups.add(nationalCode[2]);
							}
							else if(nationalCode.length == 4)
							{
								regions.add(nationalCode[3]);
							}
							else if(nationalCode.length == 5)
							{
								districts.add(nationalCode[4]);
							}
		              }
					}
				}
				roleVOList = userRolesVO.getChnRoles();
				if(roleVOList != null)
				{
				  chains = new ArrayList();
		          chainRegion = new ArrayList();
		          chainDistrict = new ArrayList();
				  Iterator roleVOListIterator = roleVOList.iterator();
				  while (roleVOListIterator.hasNext())
					{
						RoleVO chainRoleVO = (RoleVO) roleVOListIterator.next();
                        String chainKey = chainRoleVO.getKey();
						if(chainKey != null)
						{
							String[] chainCode = (chainKey).split(",");
							if(chainCode.length == 2)
							{
								chains.add(chainCode[1]);
							}
							else if(chainCode.length == 3)
							{
								chainRegion.add(chainCode[2]);
							}
							else if(chainCode.length == 4)
							{
								chainDistrict.add(chainCode[3]);
							}
						}
					  }
					}
				roleVOList = userRolesVO.getSlsRoles();
				if(roleVOList != null)
				{
				  dCs = new ArrayList();
				  territories = new ArrayList();
				  Iterator roleVOListIterator = roleVOList.iterator();
				  while (roleVOListIterator.hasNext())
					{
						RoleVO slsRoleVO = (RoleVO) roleVOListIterator.next();
                        String dcKey = slsRoleVO.getKey();
						if(dcKey != null)
						{
							String[] dcCode = (dcKey).split(",");
							if(dcCode.length == 2)
							{
								dCs.add(dcCode[1]);
							}
							else if(dcCode.length == 3)
							{
								territories.add(dcCode[2]);
							}
						}
					   }
					}
				roleVOList = userRolesVO.getCidRoles();
				if(roleVOList != null)
				{
				  cid = new ArrayList();
				  Iterator roleVOListIterator = roleVOList.iterator();
				  while (roleVOListIterator.hasNext())
					{
						RoleVO cidRoleVO = (RoleVO) roleVOListIterator.next();
                        String cidKey = cidRoleVO.getKey();
						if(cidKey != null)
						{
							String[] cidCode = (cidKey).split(",");
							cid.add(cidCode[1]);
						}
						
					 }
				 }
				if(nationalGroups != null && nationalGroups.size()>0)
                {
                	Collections.sort(nationalGroups);
                	heading = "National Groups - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,nationalGroups,heading);
               	}
                if(nationalSubgroups != null && nationalSubgroups.size()>0)
                {
                	Collections.sort(nationalSubgroups);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "National Subgroups - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,nationalSubgroups,heading);
                	
				}
                if(regions != null && regions.size()>0)
                {
                	Collections.sort(regions);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Regions - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,regions,heading);
				}
                if(districts != null && districts.size()>0)
                {
                	Collections.sort(districts);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Districts - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,districts,heading);
				}
                if(chains != null && chains.size()>0)
                {
                	Collections.sort(chains);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Chains - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,chains,heading);
                	
                }
                if(chainRegion != null && chainRegion.size()>0)
                {
                	Collections.sort(chainRegion);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Regions - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,chainRegion,heading);
                }
                if(chainDistrict != null && chainDistrict.size()>0)
                {
                	Collections.sort(chainDistrict);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Districts - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,chainDistrict,heading);
                }
                if(dCs != null && dCs.size()>0)
                {
                	Collections.sort(dCs);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "DCs - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,dCs,heading);
                	
                }
                if(territories != null && territories.size()>0)
                {
                	Collections.sort(territories);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Territories - ";
                	accountDetails = this.addToCustomerArrayList(accountDetails,territories,heading);
                	
                }
                if(cid != null && cid.size()>0)
                {
                	Collections.sort(cid);
                	if(accountDetails != null && accountDetails.size()>0)
                    {
                    	accountDetails.add(";");
                    }
                	heading = "Account Number -";
                	accountDetails = this.addToCustomerArrayList(accountDetails,cid,heading);
                }
                return accountDetails;
			 }
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}

		}
		else
		{
			return null;
		}
	
	}
	/**
	 * This method forms the Customer list.
	 * 
	 * @return the customer list
	 */
	public ArrayList addToCustomerArrayList(ArrayList accountDetails,ArrayList groups,String heading) 
	{
		accountDetails.add(heading);
		Iterator groupsIterator = groups.iterator();
    	while(groupsIterator.hasNext())
    	{
    		accountDetails.add(groupsIterator.next());
    		if(groupsIterator.hasNext())
    		{
    			accountDetails.add(", ");
    		}
    	}
    	return accountDetails;
	}
	/**
	 * This method gets the customer list.
	 * 
	 * @return the customer list
	 */
	public String getCustomerList(ReportBaseVO reportBaseVO)
	{
		StringWriter customerData = new StringWriter();
		ArrayList customers = this.getCustomerInfo(reportBaseVO);
		
		if(customers != null)
		{
			if (customers.size()!=0)
			{
				Iterator ite = customers.iterator();
				while (ite.hasNext())
				{
					customerData.append((String) ite.next());
				}
				String customerDataString = customerData.toString();
				if(customerDataString.length() > 175)
				{
					customerDataString = (customerDataString.substring(0, 168))+" more..";
					
				}
				return customerDataString;
			}
		
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * This method gets the SMO file properties.
	 * 
	 * @return the SMO file properties
	 */
	private Properties getSMOFileProperties()	
	{
		SMOFileProps fileProps = new SMOFileProps();
		return fileProps.getFileProperties();
	}
	
		
	/**
	 * This method gets the header info.
	 * 
	 * @param reportBaseVOList the report base VO list
	 * 
	 * @return the header info
	 */
	private List getHeaderInfo(List reportBaseVOList) throws SMORAException	
	{
		final String METHODNAME = "getHeaderInfo";
		List headerList = new ArrayList();
		String reportGeneratedDate;
		ReportBaseVO reportBaseVO = new ReportBaseVO();
		CustomReportVO customRprtVO;
		CriteriaVO criteriaVO;
		TimePeriodVO timePeriodVO;
		String custInfo = null;
		ArrayList timeperiodVOList = null; 
		DateSelectionAndComparisonVO dateselectionVO;
		
		reportGeneratedDate = this.getDate();
		
		log.info("Retriving the HeaderInformation");
		try
		{
			for(int i=0;i<reportBaseVOList.size();i++)
			{		
				Map headerInfoMap = new HashMap();
				reportBaseVO = (ReportBaseVO)reportBaseVOList.get(i); 
				customRprtVO = reportBaseVO.getCustomReportVO();
				criteriaVO = customRprtVO.getCustReportCriteriaVO();
				dateselectionVO = criteriaVO.getDateSelectionAndComparisonVO();
				timeperiodVOList = (ArrayList)dateselectionVO.getTimePeriodsVOList().getTimeperiodVOList();
				
				for(int j=0;j<timeperiodVOList.size();j++)
				{
					timePeriodVO =  (TimePeriodVO)timeperiodVOList.get(j);
					startMonth =timePeriodVO.getStartDate();
				}
				heading=reportBaseVO.getCustomHeading();
				custInfo = this.getCustomerList(reportBaseVO);
				
				if(custInfo == null)
				{
					custInfo = "ALL";
				}
				
				headerInfoMap.put("CUSTOM_HEADING",heading);
				headerInfoMap.put("MONTH_SELECTED",startMonth);
				headerInfoMap.put("REPORT_CREATED",reportGeneratedDate);
				headerInfoMap.put("CUSTOMER_INFO",custInfo);	
				headerInfoMap.put("BaseDir", new File(getSMOFileProperties().getProperty("REPORT_IMAGE_PATH")));
				headerInfoMap.put("ImageFile", getSMOFileProperties().getProperty("REPORT_IMAGE_NAME"));
				headerInfoMap.put("INCLUDE_PAGE_NUMBER", "TRUE");
				headerInfoMap.put("INCLUDE_IMAGE_ON_REPORT", "TRUE");
				headerInfoMap.put("REPORT_IMAGE_DIR",new File(getSMOFileProperties().getProperty("REPORT_IMAGE")));
				headerInfoMap.put("REPORT_IMAGE_NAME","hdr_MHS1stpRealezedSvngs.gif");
				headerList.add(headerInfoMap);					
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("Failed to retrive the Header Information");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		return headerList;
	}
	
	
	/**
	 * This method updates the report status as completed.
	 * 
	 * @param statusType the status type
	 * @param reportID the report ID
	 * 
	 * @throws SMORAException the SMORA exception
	 */

	private void updateReportStatusAsCompleted(Integer reportID,int recordCount) throws SMORAException
	{
		final String METHODNAME = "updateReportStatusAsCompleted";
		try
		{
			log.info("Updating the Report Status");
			reportStatusDB = new ReportStatusDB();
			reportStatusVO = new ReportStatusVO();
			reportStatusVO.setReportId(reportID);
			reportStatusVO.setStatusId(4);
			reportStatusVO.setPdfStatus(ReportManagerConstants.REPORT_REQUEST_COMPLETED);
			reportStatusVO.setQueryCmpltDts(new Date());
			reportStatusVO.setDataProcDts(new Date());
			reportStatusVO.setNumRows(recordCount);
			reportStatusVO.setEndDate(new Date());
			reportStatusDB.updateIfFieldIsNotNull(reportStatusVO);
		}
		catch(SMORADatabaseException e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Failed while updating the Report Status");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
	}
	
	private String getFormedInitialQuery(String monthStart, String monthEnd, String userID) throws SMORAException
	{
		final String METHODNAME = "getFormedQuery";
		String query = null;
		
		try
		{
		monthStart=monthStart.replaceAll("-","/");
		monthEnd=monthEnd.replaceAll("-","/");

		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select ");
		//Grouping Level 1: account
		strBuffer.append("PH.CUST_ACCT_ID, "); 
		strBuffer.append("CA.CUST_ACCT_NAM, ");
		strBuffer.append("CA.ACCT_DLVRY_ADDR Acct_addr, ");
		strBuffer.append("CA.ACCT_DLVRY_CTY_NAM Acct_city, ");
		strBuffer.append("CA.ACCT_DLVRY_ST_ABRV Acct_state, ");
		strBuffer.append("CA.ACCT_DLVRY_ZIP Acct_zip, ");
		//Grouping Level 2: Generic Name, Pkg Size, Mfg Size
		strBuffer.append("ITM.gnrc_nam, ");
		strBuffer.append("ITM.gnrc_dose_form_dscr, ");
		//Pkg_sz field is below 
		strBuffer.append("ITM.MFG_PROD_SIZ Mfg_sz, ");
		strBuffer.append("CA.ACCT_DLVRY_CTY_NAM||' '||CA.ACCT_DLVRY_ST_ABRV||' '||CA.ACCT_DLVRY_ZIP as FirstGroup_Display, ");
		strBuffer.append("ITM.gnrc_nam||' '||ITM.gnrc_dose_form_dscr||'  Pkg Sz:'||DECODE(TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT)||'  Mfg Size:'||ITM.MFG_PROD_SIZ as SecondGroup_Display, ");//newly added test
		//purchased
		strBuffer.append("DECODE(TRIM(ITM.NDC_NUM), null, ITM.UPC_NUM, ITM.NDC_NUM) as NDC_UPC_NUM1, "); 
		strBuffer.append("PH.em_item_num, "); 
		strBuffer.append("SA.SPLR_ACCT_NAM, "); 
		strBuffer.append("ITM.sell_dscr, "); 
		strBuffer.append("PH.sls_id, "); 
		strBuffer.append("PH.invc_dt, "); 
		strBuffer.append("DECODE(TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT) as Pkg_sz, "); 
		strBuffer.append("PH.sls_qty, ");
		strBuffer.append("decode( PH.sls_qty, 0, 0, PH.sls_amt / PH.sls_qty ) as PURCH_PRC, "); 
		strBuffer.append("PH.sls_amt, ");
		strBuffer.append("CASE WHEN PH.sls_qty = 0 THEN 0 ");
		strBuffer.append("WHEN NVL(DECODE(TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT), 0) = 0 THEN 0 ");
		strBuffer.append("ELSE (PH.sls_amt / PH.sls_qty) / DECODE(TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT) ");
		strBuffer.append("END as PRC_PER_DOS, ");
		//alternative contract
		strBuffer.append("DECODE(ITM.NDC_NUM, ' ', ITM.UPC_NUM, ITM.NDC_NUM) as NDC_UPC_NUM2, "); 
		strBuffer.append("CRS.ITEM_NUM, "); 
		strBuffer.append("SA.SPLR_ACCT_NAM, "); 
		strBuffer.append("ITM.sell_dscr, "); 
		strBuffer.append("CRS.CNTRC_LEAD_ID, "); 
		strBuffer.append("CL.CNTRC_LEAD_NAM, ");
		strBuffer.append("DECODE( TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT) as PKG_SZ2, ");
		strBuffer.append("CRS.CNTRC_BID_PRC, ");
		strBuffer.append("CRS.CNTRC_BID_PRC * PH.sls_qty as ALT_CNTRC_TOT_DLRS, ");
		strBuffer.append("CASE WHEN NVL(DECODE( TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT), 0) = 0 THEN 0 ");
		strBuffer.append("ELSE CRS.cntrc_bid_prc / DECODE( TRIM(CIA.USER_PKG_SIZE_AMT), null, ITM.MFG_PROD_SIZ, CIA.USER_PKG_SIZE_AMT) ");
		strBuffer.append("END as ALT_CNTRC_PRC_PER_DOS, ");
		strBuffer.append("CRS.cntrc_bid_prc * PH.sls_qty - PH.sls_amt as RLZD_SVGS, ");
		//alternative equivalent item
		strBuffer.append("DECODE(ITM2.NDC_NUM, ' ', ITM2.UPC_NUM, ITM2.NDC_NUM) as NDC_UPC_NUM3, "); 
		strBuffer.append("CAEI.ALT_EQ_ITEM_NUM, "); 
		strBuffer.append("SA2.SPLR_ACCT_NAM, "); 
		strBuffer.append("ITM2.sell_dscr, "); 
		strBuffer.append("CAEI.ALT_EQ_CNTRC_LEAD_ID, ");
		strBuffer.append("CL2.CNTRC_LEAD_NAM, ");
		strBuffer.append("DECODE(TRIM(CIA2.USER_PKG_SIZE_AMT), null, ITM2.MFG_PROD_SIZ, CIA2.USER_PKG_SIZE_AMT) as Pkg_sz3, "); 
		strBuffer.append("CAEI.ALT_EQ_ITEM_PRC, ");
		strBuffer.append("CAEI.ALT_EQ_ITEM_PRC * PH.sls_qty as ALT_EQV_ITEM_TOT_DLRS, ");
		strBuffer.append("CASE WHEN NVL(DECODE(TRIM(CIA2.USER_PKG_SIZE_AMT), null, ITM2.MFG_PROD_SIZ, CIA2.USER_PKG_SIZE_AMT), 0) = 0 THEN 0 ");
		strBuffer.append("ELSE CAEI.ALT_EQ_ITEM_PRC / DECODE(TRIM(CIA2.USER_PKG_SIZE_AMT), null, ITM2.MFG_PROD_SIZ, CIA2.USER_PKG_SIZE_AMT) "); 
		strBuffer.append("END as ALT_EQV_ITEM_PRC_PER_DOS, ");
		strBuffer.append("CAEI.ALT_EQ_ITEM_PRC * PH.sls_qty - PH.sls_amt as ALT_RLZD_SVGS, ");
		// DO NOT display the below field.  Use this field to calculate the SUM of Realized Savings
		// Also used to determine which value (RLZD_SVGS or ALT_RLZD_SVGS) is bolded
		strBuffer.append("CASE WHEN CAEI.ALT_EQ_ITEM_PRC is null or CRS.cntrc_bid_prc >= CAEI.ALT_EQ_ITEM_PRC THEN CRS.cntrc_bid_prc * PH.sls_qty - PH.sls_amt ");
		strBuffer.append("ELSE CAEI.ALT_EQ_ITEM_PRC * PH.sls_qty - PH.sls_amt ");
		strBuffer.append("END Sum_This_Rlzd_Svgs ");
		//--
		strBuffer.append("from "); 
		strBuffer.append("S_SALE_ITEM PH ");
		strBuffer.append("INNER JOIN S_CUST_ACCT CA on PH.cust_acct_id = CA.cust_acct_id ");
		strBuffer.append("INNER JOIN S_APPL_ONE_STOP_LEAD OSL on PH.cntrc_lead_tp_id = OSL.cntrc_lead_id ");
		strBuffer.append("LEFT OUTER JOIN S_CMS_RLZD_SAV CRS on PH.cust_acct_id = CRS.cust_acct_id and PH.em_item_num = CRS.item_num ");
		strBuffer.append("INNER JOIN S_ITEM ITM on PH.em_item_num = ITM.em_item_num ");
		strBuffer.append("INNER JOIN S_IW_SPLR_ACCT SA on ITM.splr_acct_id = SA.splr_acct_id ");
		strBuffer.append("LEFT OUTER JOIN S_CUST_ITEM_ATRB CIA on PH.cust_acct_id = CIA.cust_acct_id and PH.em_item_num = CIA.em_item_num ");
		strBuffer.append("LEFT OUTER JOIN S_IW_CNTRC_LEAD CL on CRS.cntrc_lead_id = CL.cntrc_lead_id ");
		strBuffer.append("LEFT OUTER JOIN S_CMS_ALT_EQ_ITEM CAEI on PH.cust_acct_id = CAEI.cust_acct_id and PH.em_item_num = CAEI.orig_item_num ");
		strBuffer.append("LEFT OUTER JOIN S_ITEM ITM2 on CAEI.alt_eq_item_num = ITM2.em_item_num ");
		strBuffer.append("LEFT OUTER JOIN S_IW_SPLR_ACCT SA2 on ITM2.splr_acct_id = SA2.splr_acct_id ");
		strBuffer.append("LEFT OUTER JOIN S_CUST_ITEM_ATRB CIA2 on CAEI.cust_acct_id = CIA2.cust_acct_id and CAEI.alt_eq_item_num = CIA2.em_item_num ");
		strBuffer.append("LEFT OUTER JOIN S_IW_CNTRC_LEAD CL2 on CAEI.alt_eq_cntrc_lead_id = CL2.cntrc_lead_id ");
		//--
		strBuffer.append("where "); 
		// PH.sls_proc_wrk_dt between '<start_dt>' and '<end_dt>' --retrieve start and end dates from the report criteria
		
		strBuffer.append("PH.sls_proc_wrk_dt between TO_DATE('" + monthStart + "','mm-dd-yyyy') and TO_DATE('" + monthEnd + "','mm-dd-yyyy') ");
		//strBuffer.append("ph.sls_proc_wrk_dt BETWEEN '1-MAY-06' AND '31-JUL-06'");
				
		query = strBuffer.toString();
		
		}
		catch(Exception e)
		{
			log.error("Failed while updating the Report Status");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		return query;
		/*Old MHS Query */
		/*if(custAcctIds.size()!=0 && custAcctIds.size()< 1000)
		{
			reportResultList = session.createSQLQuery("select n.CUST_ACCT_ID,n.CUST_ACCT_NAM,n.ACCT_DLVRY_ADDR,n.ACCT_DLVRY_CTY_NAM,n.ACCT_DLVRY_ST_ABRV,n.ACCT_DLVRY_ZIP,g.GNRC_NAM,g.GNRC_DOSE_FORM_DSCR,g.MFG_SIZ_QTY,g.GNRC_MFR_SIZ_AMT,e.USER_PKG_SIZE_AMT,DECODE(a.NDC_CD, ' ', a.UPC_CD, a.NDC_CD) as NDC_UPC_NUM1,a.em_item_num,d.SPLR_ACCT_ID,d.sell_dscr,a.sls_id,a.invc_dt,DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as Pkg_sz,a.sls_qty,a.sls_amt / a.sls_qty as PURCH_PRC,a.sls_amt,(a.sls_amt / a.sls_qty) / DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as PRC_PER_DOS,DECODE(a.NDC_CD, ' ', a.UPC_CD, a.NDC_CD) as NDC_UPC_NUM2,b.item_num,d.SPLR_ACCT_ID as ALT_SPLR_ACCT_ID,d.sell_dscr as ALT_SELL_DSCR,b.CNTRC_LEAD_ID,f.CNTRC_LEAD_NAM,DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as Pkg_sz2,b.cntrc_bid_prc,b.cntrc_bid_prc * a.sls_qty as ALT_CNTRC_TOT_DLRS,b.cntrc_bid_prc / DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as ALT_CNTRC_PRC_PER_DOS,b.cntrc_bid_prc * a.sls_qty-a.sls_amt as RLZD_SVGS,DECODE(g.NDC_NUM, ' ', g.UPC_NUM, g.NDC_NUM) as NDC_UPC_NUM3,j. ALT_EQ_ITEM_NUM,g.SPLR_ACCT_id as ALT_EQ_SPLR_ACCT_ID,g.sell_dscr as ALT_EQ_SELL_DSCR,k.CNTRC_LEAD_ID as ALT_EQ_CNTRC_LEAD_ID,m.CNTRC_LEAD_NAM as ALT_EQ_CNTRC_LEAD_NAM,DECODE(h.USER_PKG_SIZE_AMT, ' ',g.MFG_PROD_SIZ, h.USER_PKG_SIZE_AMT) as Pkg_sz3,k.CNTRC_BID_PRC as ALT_EQ_CNTRC_BID_PRC,k.CNTRC_BID_PRC * a.sls_qty as ALT_EQV_ITEM_TOT_DLRS,k.CNTRC_BID_PRC / DECODE(h.USER_PKG_SIZE_AMT, ' ',g.MFG_PROD_SIZ, h.USER_PKG_SIZE_AMT) as ALT_EQV_ITEM_PRC_PER_DOS,k.CNTRC_BID_PRC * a.sls_qty-a.sls_amt as ALT_RLZD_SVGS from S_SALE_ITEM a,S_CMS_RLZD_SAV b,S_ITEM d,S_CUST_ITEM_ATRB e,S_IW_CNTRC_LEAD f,S_ITEM g,S_CUST_ITEM_ATRB h,S_APPL_ONE_STOP_LEAD i,S_CMS_ALT_EQ_ITEM j,S_DRUG_ITEM_PRC k,S_IW_CNTRC_LEAD m,S_CUST_ACCT n where a.SLS_PROC_WRK_DT between TO_DATE('" + monthStart + "','MM/dd/yyyy') and TO_DATE('" + monthEnd + "','MM/dd/yyyy') and a.cust_acct_id in (" + DBUtil.convertAsCommaSeparatedValuesWithQuotes(custAcctIds) + ") and a.SLS_PROC_WRK_DT between b.cntrc_beg_dt and b.cntrc_end_dt and a.cust_acct_id = b.cust_acct_id and a.em_item_num = b.item_num and a.em_item_num = d.em_item_num and a.em_item_num = e.em_item_num and a.cust_acct_id = e.cust_acct_id and b.cntrc_lead_id = f.cntrc_lead_id and b.item_num = g.em_item_num and b.item_num = h.em_item_num and h.cust_acct_id = b.cust_acct_id and a.cntrc_lead_tp_id = i.cntrc_lead_id and a.cust_acct_id = j. cust_acct_id and a.em_item_num = j.item_num and j.alt_eq_item_num = k.item_num and a.cust_acct_id = k.cust_acct_id and k.cntrc_lead_id = m.cntrc_lead_id and a.em_item_num = g.em_item_num and a.cust_acct_id = n.cust_acct_id").list();
		}
		else
		{
			reportResultList = session.createSQLQuery("select n.CUST_ACCT_ID,n.CUST_ACCT_NAM,n.ACCT_DLVRY_ADDR,n.ACCT_DLVRY_CTY_NAM,n.ACCT_DLVRY_ST_ABRV,n.ACCT_DLVRY_ZIP,g.GNRC_NAM,g.GNRC_DOSE_FORM_DSCR,g.MFG_SIZ_QTY,g.GNRC_MFR_SIZ_AMT,e.USER_PKG_SIZE_AMT,DECODE(a.NDC_CD, ' ', a.UPC_CD, a.NDC_CD) as NDC_UPC_NUM1,a.em_item_num,d.SPLR_ACCT_ID,d.sell_dscr,a.sls_id,a.invc_dt,DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as Pkg_sz,a.sls_qty,a.sls_amt / a.sls_qty as PURCH_PRC,a.sls_amt,(a.sls_amt / a.sls_qty) / DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as PRC_PER_DOS,DECODE(a.NDC_CD, ' ', a.UPC_CD, a.NDC_CD) as NDC_UPC_NUM2,b.item_num,d.SPLR_ACCT_ID as ALT_SPLR_ACCT_ID,d.sell_dscr as ALT_SELL_DSCR,b.CNTRC_LEAD_ID,f.CNTRC_LEAD_NAM,DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as Pkg_sz2,b.cntrc_bid_prc,b.cntrc_bid_prc * a.sls_qty as ALT_CNTRC_TOT_DLRS,b.cntrc_bid_prc / DECODE(e.USER_PKG_SIZE_AMT, ' ',d.MFG_PROD_SIZ, e.USER_PKG_SIZE_AMT) as ALT_CNTRC_PRC_PER_DOS,b.cntrc_bid_prc * a.sls_qty-a.sls_amt as RLZD_SVGS,DECODE(g.NDC_NUM, ' ', g.UPC_NUM, g.NDC_NUM) as NDC_UPC_NUM3,j. ALT_EQ_ITEM_NUM,g.SPLR_ACCT_id as ALT_EQ_SPLR_ACCT_ID,g.sell_dscr as ALT_EQ_SELL_DSCR,k.CNTRC_LEAD_ID as ALT_EQ_CNTRC_LEAD_ID,m.CNTRC_LEAD_NAM as ALT_EQ_CNTRC_LEAD_NAM,DECODE(h.USER_PKG_SIZE_AMT, ' ',g.MFG_PROD_SIZ, h.USER_PKG_SIZE_AMT) as Pkg_sz3,k.CNTRC_BID_PRC as ALT_EQ_CNTRC_BID_PRC,k.CNTRC_BID_PRC * a.sls_qty as ALT_EQV_ITEM_TOT_DLRS,k.CNTRC_BID_PRC / DECODE(h.USER_PKG_SIZE_AMT, ' ',g.MFG_PROD_SIZ, h.USER_PKG_SIZE_AMT) as ALT_EQV_ITEM_PRC_PER_DOS,k.CNTRC_BID_PRC * a.sls_qty-a.sls_amt as ALT_RLZD_SVGS from S_SALE_ITEM a,S_CMS_RLZD_SAV b,S_ITEM d,S_CUST_ITEM_ATRB e,S_IW_CNTRC_LEAD f,S_ITEM g,S_CUST_ITEM_ATRB h,S_APPL_ONE_STOP_LEAD i,S_CMS_ALT_EQ_ITEM j,S_DRUG_ITEM_PRC k,S_IW_CNTRC_LEAD m,S_CUST_ACCT n where a.SLS_PROC_WRK_DT between TO_DATE('" + monthStart + "','MM/dd/yyyy') and TO_DATE('" + monthEnd + "','MM/dd/yyyy') and a.cust_acct_id in (" + getCustAccountId(userID) + ") and a.SLS_PROC_WRK_DT between b.cntrc_beg_dt and b.cntrc_end_dt and a.cust_acct_id = b.cust_acct_id and a.em_item_num = b.item_num and a.em_item_num = d.em_item_num and a.em_item_num = e.em_item_num and a.cust_acct_id = e.cust_acct_id and b.cntrc_lead_id = f.cntrc_lead_id and b.item_num = g.em_item_num and b.item_num = h.em_item_num and h.cust_acct_id = b.cust_acct_id and a.cntrc_lead_tp_id = i.cntrc_lead_id and a.cust_acct_id = j. cust_acct_id and a.em_item_num = j.item_num and j.alt_eq_item_num = k.item_num and a.cust_acct_id = k.cust_acct_id and k.cntrc_lead_id = m.cntrc_lead_id and a.em_item_num = g.em_item_num and a.cust_acct_id = n.cust_acct_id").list();
		}*/
	}
	
	/**
	 * This method gets the cust account id.
	 *
	 * @param accountList the account list
	 *
	 * @return the cust account id
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private String getCustAccountId(String userId) throws SMORAException
	{
		final String METHODNAME = "getCustAccountId";
		int  customerLength=0;
				
		List<String> defaultAccountList = null;
		StringBuffer custAcctIds= new StringBuffer();
		String defaultCustomerQuery="";
		userId = userId.trim();
		// If the user does'nt choose any fields then all his accessible
		// accounts need to be displayed.
		//Variable scope is increased for QC-11211
		Session session = null;
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(" SELECT CUST_ACCT_ID FROM( ");
		strBuffer.append(" SELECT act.cust_acct_id \n");
		strBuffer.append(" FROM s_cust_acct act,  \n");
		strBuffer.append(" (SELECT * FROM s_iw_user_access \n"); 
		strBuffer.append(" WHERE  user_id = RPAD('"+ userId +"', 10) \n");
		strBuffer.append("   AND  sys_id         = 'STK2') accss \n");
		strBuffer.append(" WHERE  \n");
		strBuffer.append(" (      accss.access_role_cd = RPAD('CID', 5) \n");
		strBuffer.append(" AND act.cust_acct_id = accss.lvl1_valu)  \n");
		strBuffer.append(" UNION \n");
		strBuffer.append(" -- Check Home Distribution Center / Sales Territory ID formats \n");
		strBuffer.append(" SELECT act.cust_acct_id \n");
		strBuffer.append(" FROM s_cust_acct act,  \n");
		strBuffer.append(" (SELECT * FROM s_iw_user_access \n"); 
		strBuffer.append(" WHERE  user_id = RPAD('"+ userId +"', 10) \n");
		strBuffer.append("   AND  sys_id         = 'STK2') accss \n");
		strBuffer.append(" WHERE  \n");
		strBuffer.append(" (      accss.access_role_cd = RPAD('SLS', 5) \n");
		strBuffer.append("  AND act.home_dc_id       = accss.lvl1_valu \n");
		strBuffer.append("  AND (accss.lvl2_valu IS NULL \n");
		strBuffer.append("      OR  \n");
		strBuffer.append("      act.sls_terr_id = accss.lvl2_valu) \n");
		strBuffer.append(" ) \n");
		strBuffer.append(" UNION \n");
		strBuffer.append(" -- Check Customer Chain ID formats \n");
		strBuffer.append(" SELECT act.cust_acct_id \n");
		strBuffer.append(" FROM s_cust_acct act,  \n");
		strBuffer.append(" (SELECT * FROM s_iw_user_access \n"); 
		strBuffer.append(" WHERE  user_id = RPAD('"+ userId +"', 10) \n");
		strBuffer.append("   AND  sys_id         = 'STK2') accss \n");
		strBuffer.append(" WHERE  \n");
		strBuffer.append(" (      accss.access_role_cd = RPAD('CHN', 5) \n");
		strBuffer.append(" AND act.cust_chn_id      = accss.lvl1_valu \n");
		strBuffer.append(" AND (accss.lvl2_valu IS NULL  \n");
		strBuffer.append("      OR  \n");
		strBuffer.append("     (act.cust_rgn_num = accss.lvl2_valu \n");
		strBuffer.append(" 	 AND \n");
		strBuffer.append("       (accss.lvl3_valu IS NULL \n");
		strBuffer.append(" 	  OR act.cust_dstrct_num = accss.lvl3_valu))) \n");
		strBuffer.append(" ) \n");
		strBuffer.append(" UNION \n");
		strBuffer.append(" -- Check Hospital (National Group / Subgroup / Region / District) formats \n");
		strBuffer.append(" SELECT act.cust_acct_id \n");
		strBuffer.append(" FROM s_cust_acct act,  \n");
		strBuffer.append(" (SELECT * FROM s_iw_user_access \n"); 
		strBuffer.append(" WHERE  user_id = RPAD('"+ userId +"', 10) \n");
		strBuffer.append("   AND  sys_id         = 'STK2') accss \n");
		strBuffer.append(" WHERE  \n");
		strBuffer.append(" (      accss.access_role_cd = RPAD('HSP', 5) \n");
		strBuffer.append("  AND act.natl_grp_cd      = accss.lvl1_valu \n");
		strBuffer.append("  AND (accss.lvl2_valu IS NULL  \n");
		strBuffer.append("      OR  \n");
		strBuffer.append("     (act.natl_sub_grp_cd = accss.lvl2_valu \n");
		strBuffer.append(" 	 AND \n");
		strBuffer.append("         (accss.lvl3_valu IS NULL \n");
		strBuffer.append(" 	    OR \n"); 
		strBuffer.append(" 	   (act.cust_rgn_num = accss.lvl3_valu \n");
		strBuffer.append(" 	    AND \n");
		strBuffer.append("            (accss.lvl4_valu IS NULL \n");
		strBuffer.append(" 	       OR  \n");
		strBuffer.append(" 		   act.cust_dstrct_num = accss.lvl4_valu))))) \n");
		strBuffer.append(" ) \n");
		strBuffer.append(" )");
		String defaultQuery = strBuffer.toString();
		try
		{
			template = DBUtil.getTemplate(template);
			session = template.getSession();
			defaultAccountList = session.createSQLQuery(defaultQuery).list();

			int accountSize=0;

			if(defaultAccountList != null)
			{			

				int defaultAccountListSize = defaultAccountList.size();
				customerLength=defaultAccountListSize;

				for(int j = 0;j < defaultAccountListSize ; j++)
				{
					accountSize++;
					if(accountSize==defaultAccountListSize)
					{
						custAcctIds.append(" '" + defaultAccountList.get(j) + "' " );
					}
					else
					{
						custAcctIds.append(" '" + defaultAccountList.get(j) + "' " +",");
					}

				}
				custAcctIds.deleteCharAt(custAcctIds.length() - 1);				
			}



			defaultCustomerQuery+=(" and PH.CUST_ACCT_ID IN ( "+ custAcctIds + ")");			
			
		}
		catch(SMORADatabaseException dbe)
		{
			log.error("DataBase Exception is raised:"+dbe);
			throw new SMORAException();
		}
		catch(Exception e)
		{
			log.error("Exception is raised:"+e);
			throw new SMORAException();
		}
		 //QC-11211 Finally block added
		finally
		{
			if(template!=null)
			template.finallySessionClosing(CLASSNAME, METHODNAME, session);
		}
		return defaultCustomerQuery;
	}
	
	public String getCustomerSelectionTabWhereClauseForLow(List custAcctIds)
	{
		String lowCustString = null;
		System.out.println("lowcust: "+lowCustString);
		 //and PH.cust_acct_id in ( <account #> ) --retrieve accounts selected from the report criteria
		if(custAcctIds.size()!=0 && custAcctIds.size()< 1000)
		{
			lowCustString = " and PH.cust_acct_id in ( "+ DBUtil.convertAsCommaSeparatedValuesWithQuotes(custAcctIds) +")";
			//lowCustString = " AND ph.cust_acct_id IN ('317185', '156397', '568299', '560648')"; 
		}
		System.out.println(lowCustString);
		
		return lowCustString;
			
	}
	
	/**
	 * 
	 * @param userRolesVO
	 * @return
	 */
	public String getCustomerSelectionTabWhereClauseHigh(UserRolesVO userRolesVO)
	{
		StringBuffer custAcctId = new StringBuffer("");
		StringBuffer hspString = new StringBuffer("");
		StringBuffer chnString = new StringBuffer("");
		StringBuffer slsString = new StringBuffer("");
		StringBuffer returnString = new StringBuffer("");
		// Commented out the below line based on the Ricky's modified query
		// returnString.append(" INNER JOIN S_CUST_ACCT CA ON PH.CUST_ACCT_ID =
		// CA.CUST_ACCT_ID \n");
		returnString.append(" AND ");

		// Get CustAccountId from CID RoleVO.
		if (userRolesVO != null)
		{
			List<RoleVO> cidVOList = userRolesVO.getCidRoles();
			if (cidVOList != null && cidVOList.size() > 0)
			{
				custAcctId.append(" PH.CUST_ACCT_ID IN (");
				for (int i = 0; i < cidVOList.size(); i++)
				{
					RoleVO roleVO = cidVOList.get(i);
					custAcctId.append("'" + roleVO.getKey().substring(roleVO.getKey().indexOf(",") + 1, roleVO.getKey().length()) + "'" + ",");
				}
			}
			// TO trim the comma at the end of the String
			if (custAcctId.length() > 0 && custAcctId.charAt(custAcctId.length() - 1) == ',')
			{
				custAcctId.deleteCharAt(custAcctId.length() - 1);
				custAcctId.append(")");
			}
			else if (custAcctId.length() > 0)
			{
				custAcctId.delete(0, custAcctId.length() - 1);
			}
			// To append the custAcctId query String to the return String.
			if (custAcctId.length() > 0)
			{
				returnString.append(custAcctId);
			}
			// Get NatlGrpCd,NatlSubGrpCd,CustRgnNum,CustDstrctNum from HSP
			// RoleVO.
			if (userRolesVO.getHspRoles() != null)
			{
				List<RoleVO> hspVOList = userRolesVO.getHspRoles();
				if (hspVOList != null && hspVOList.size() > 0)
				{
					for (int i = 0; i < hspVOList.size(); i++)
					{
						RoleVO roleVO = hspVOList.get(i);
						String key = roleVO.getKey();
						String hspRoleCode = null;
						String level1Value = null;
						String level2Value = null;
						String level3Value = null;
						String level4Value = null;

						StringTokenizer tokenizer = new StringTokenizer(key, ",");
						while (tokenizer.hasMoreElements())
						{
							if (hspRoleCode == null)
							{
								hspRoleCode = tokenizer.nextToken();
							}
							else if (level1Value == null)
							{
								level1Value = tokenizer.nextToken();
							}
							else if (level2Value == null)
							{
								level2Value = tokenizer.nextToken();
							}
							else if (level3Value == null)
							{
								level3Value = tokenizer.nextToken();
							}
							else if (level4Value == null)
							{
								level4Value = tokenizer.nextToken();
							}
						}
						// Where clause building....
						if (level1Value != null && level2Value == null && level3Value == null && level4Value == null)
						{
							if (hspString.length() <= 0)
							{
								hspString.append("  (CA.NATL_GRP_CD = '" + level1Value + "')");
							}
							else
							{
								hspString.append(" OR (CA.NATL_GRP_CD = '" + level1Value + "')");
							}
						}
						else if (level1Value != null && level2Value != null && level3Value == null && level4Value == null)
						{
							if (hspString.length() <= 0)
							{
								hspString.append("  (CA.NATL_GRP_CD = '" + level1Value + "' AND CA.NATL_SUB_GRP_CD = '" + level2Value + "')");
							}
							else
							{
								hspString.append(" OR (CA.NATL_GRP_CD = '" + level1Value + "' AND CA.NATL_SUB_GRP_CD = '" + level2Value + "')");
							}
						}
						else if (level1Value != null && level2Value != null && level3Value != null && level4Value == null)
						{
							if (hspString.length() <= 0)
							{
								hspString.append("  (CA.NATL_GRP_CD = '" + level1Value + "' AND CA.NATL_SUB_GRP_CD = '" + level2Value + "' AND CA.CUST_RGN_NUM = '" + level3Value + "')");
							}
							else
							{
								hspString.append(" OR (CA.NATL_GRP_CD = '" + level1Value + "' AND CA.NATL_SUB_GRP_CD = '" + level2Value + "' AND CA.CUST_RGN_NUM = '" + level3Value + "')");
							}
						}
						else if (level1Value != null && level2Value != null && level3Value != null && level4Value != null)
						{
							if (hspString.length() <= 0)
							{
								hspString.append("  (CA.NATL_GRP_CD = '" + level1Value + "' AND CA.NATL_SUB_GRP_CD = '" + level2Value + "' AND CA.CUST_RGN_NUM = '" + level3Value + "' AND CA.CUST_DSTRCT_NUM = '" + level4Value + "') ");
							}
							else
							{
								hspString.append(" OR (CA.NATL_GRP_CD = '" + level1Value + "' AND CA.NATL_SUB_GRP_CD = '" + level2Value + "' AND CA.CUST_RGN_NUM = '" + level3Value + "' AND CA.CUST_DSTRCT_NUM = '" + level4Value + "') ");
							}
						}
					}
				}
			}

			// Append the HSP String to the return String.
			if (hspString.length() > 0 && returnString.length() > 9)
			{
				returnString.append(" OR " + hspString);
			}
			else if (hspString.length() > 0)
			{
				returnString.append(hspString);
			}
			// CHN
			if (userRolesVO.getChnRoles() != null)
			{
				List<RoleVO> chnVOList = userRolesVO.getChnRoles();
				if (chnVOList != null && chnVOList.size() > 0)
				{
					for (int i = 0; i < chnVOList.size(); i++)
					{
						RoleVO roleVO = chnVOList.get(i);
						String key = roleVO.getKey();
						String chnRoleCode = null;
						String level1Value = null;
						String level2Value = null;
						String level3Value = null;
						StringTokenizer tokenizer = new StringTokenizer(key, ",");
						while (tokenizer.hasMoreElements())
						{
							if (chnRoleCode == null)
							{
								chnRoleCode = tokenizer.nextToken();
							}
							else if (level1Value == null)
							{
								level1Value = tokenizer.nextToken();
							}
							else if (level2Value == null)
							{
								level2Value = tokenizer.nextToken();
							}
							else if (level3Value == null)
							{
								level3Value = tokenizer.nextToken();
							}
						}
						// Where clause building....
						if (level1Value != null && level2Value == null && level3Value == null)
						{
							if (chnString.length() <= 0)
							{
								chnString.append("  (CA.CUST_CHN_ID = '" + level1Value + "')");
							}
							else
							{
								chnString.append(" OR (CA.CUST_CHN_ID = '" + level1Value + "')");
							}
						}
						else if (level1Value != null && level2Value != null && level3Value == null)
						{
							if (chnString.length() <= 0)
							{
								chnString.append("  (CA.CUST_CHN_ID = '" + level1Value + "' AND CA.CUST_RGN_NUM = '" + level2Value + "')");
							}
							else
							{
								chnString.append(" OR (CA.CUST_CHN_ID = '" + level1Value + "' AND CA.CUST_RGN_NUM = '" + level2Value + "')");
							}
						}
						else if (level1Value != null && level2Value != null && level3Value != null)
						{
							if (chnString.length() <= 0)
							{
								chnString.append("  (CA.CUST_CHN_ID = '" + level1Value + "' AND CA.CUST_RGN_NUM = '" + level2Value + "' AND CA.CUST_DSTRCT_NUM = '" + level3Value + "') ");
							}
							else
							{
								chnString.append(" OR (CA.CUST_CHN_ID = '" + level1Value + "' AND CA.CUST_RGN_NUM = '" + level2Value + "' AND CA.CUST_DSTRCT_NUM = '" + level3Value + "') ");
							}
						}
					}
				}
			}
			if (chnString.length() > 0 && returnString.length() > 9)
			{
				returnString.append(" OR " + chnString);
			}
			else if (chnString.length() > 0)
			{
				returnString.append(chnString);
			}
			// SLS
			if (userRolesVO.getSlsRoles() != null)
			{
				List<RoleVO> slsVOList = userRolesVO.getSlsRoles();
				if (slsVOList != null && slsVOList.size() > 0)
				{
					for (int i = 0; i < slsVOList.size(); i++)
					{
						RoleVO roleVO = slsVOList.get(i);
						String key = roleVO.getKey();
						String slsRoleCode = null;
						String level1Value = null;
						String level2Value = null;

						StringTokenizer tokenizer = new StringTokenizer(key, ",");
						while (tokenizer.hasMoreElements())
						{
							if (slsRoleCode == null)
							{
								slsRoleCode = tokenizer.nextToken();
							}
							else if (level1Value == null)
							{
								level1Value = tokenizer.nextToken();
							}
							else if (level2Value == null)
							{
								level2Value = tokenizer.nextToken();
							}
						}
						// Where clause building....
						if (level1Value != null && level2Value == null)
						{
							if (slsString.length() <= 0)
							{
								slsString.append(" (CA.HOME_DC_ID = '" + level1Value + "')");
							}
							else
							{
								slsString.append(" OR (CA.HOME_DC_ID = '" + level1Value + "')");
							}
						}
						else if (level1Value != null && level2Value != null)
						{
							if (slsString.length() <= 0)
							{
								slsString.append(" (CA.HOME_DC_ID = '" + level1Value + "' AND CA.SLS_TERR_ID = '" + level2Value + "') ");
							}
							else
							{
								slsString.append(" OR (CA.HOME_DC_ID = '" + level1Value + "' AND CA.SLS_TERR_ID = '" + level2Value + "') ");
							}
						}
					}
				}
			}
		}
		if (slsString.length() > 0 && returnString.length() > 9)
		{
			returnString.append(" OR " + slsString);
		}
		else if (slsString.length() > 0)
		{
			returnString.append(slsString);
		}
		//returnString.append(" )");
		return returnString.toString();
	}
	
	
	/**
	 * The main method.
	 * 
	 * @param args the args
	 */
	public static void main(String args[])
	{
		ReportFileGenerateBatch obj = new ReportFileGenerateBatch();
		List reportIDList;
		ArrayList reportVOList = new ArrayList();
		ArrayList reportCustAcctDetailsList;
		List reportHeaderMapList;
		ArrayList custAcctDetailsList;
		ArrayList userRolesVOList;
		Map headerMap;
		String[] reportFormat;
		ArrayList reportMonth;
		String reportStartMonth="";
		String reportEndMonth="";
		String[] monthArray;
		Date reportDate = obj.getCurrentDate();
		ReportStatusVO reportStatusVO = new ReportStatusVO();
		int REPORT_COMPLETED = 4;
		String query = null;
		ReportStatusDB rptStatusDB = new ReportStatusDB();
		
		//BigDecimal big = new BigDecimal("2");
		//big.
		
		
		/*String detailFields[] = {"CUST_ACCT_ID","CUST_ACCT_NAM","ACCT_DLVRY_ADDR","ACCT_DLVRY_CTY_NAM","ACCT_DLVRY_ST_ABRV","ACCT_DLVRY_ZIP","GNRC_NAM","GNRC_DOSE_FORM_DSCR","MFG_SIZ_QTY","GNRC_MFR_SIZ_AMT","USER_PKG_SIZE_AMT",
				"NDC_UPC_NUM1","em_item_num","SPLR_ACCT_ID","SELL_DSCR","SLS_ID","INVC_DT","Pkg_sz","sls_qty",
				"PURCH_PRC","sls_amt","PRC_PER_DOS","NDC_UPC_NUM2","item_num","ALT_SPLR_ACCT_ID","ALT_SELL_DSCR",
				"CNTRC_LEAD_ID","CNTRC_LEAD_NAM","Pkg_sz2","CNTRC_BID_PRC","ALT_CNTRC_TOT_DLRS","ALT_CNTRC_PRC_PER_DOS","RLZD_SVGS",
				"NDC_UPC_NUM3","ALT_EQ_ITEM_NUM","ALT_EQ_SPLR_ACCT_ID","ALT_EQ_SELL_DSCR","ALT_EQ_CNTRC_LEAD_ID","ALT_EQ_CNTRC_LEAD_NAM",
				"Pkg_sz3","ALT_EQ_CNTRC_BID_PRC","ALT_EQV_ITEM_TOT_DLRS","ALT_EQV_ITEM_PRC_PER_DOS","ALT_RLZD_SVGS"};
		*/
		String detailFields[] = {"CUST_ACCT_ID","CUST_ACCT_NAM","ACCT_ADDR","ACCT_CITY","ACCT_STATE","ACCT_ZIP","GNRC_NAM","GNRC_DOSE_FORM_DSCR","MFG_SZ","FirstGroup_Display","SecondGroup_Display",
				"NDC_UPC_NUM1","em_item_num","SPLR_ACCT_NAM","SELL_DSCR","SLS_ID","INVC_DT","PKG_SZ","sls_qty",
				"PURCH_PRC","sls_amt","PRC_PER_DOS","NDC_UPC_NUM2","item_num","SPLR_ACCT_NAM_1","SELL_DSCR_1",
				"CNTRC_LEAD_ID","CNTRC_LEAD_NAM","PKG_SZ2","CNTRC_BID_PRC","ALT_CNTRC_TOT_DLRS","ALT_CNTRC_PRC_PER_DOS","RLZD_SVGS",
				"NDC_UPC_NUM3","ALT_EQ_ITEM_NUM","SPLR_ACCT_NAM_2","SELL_DSCR_2","ALT_EQ_CNTRC_LEAD_ID","CNTRC_LEAD_NAM_1",
				"PKG_SZ3","ALT_EQ_ITEM_PRC","ALT_EQV_ITEM_TOT_DLRS","ALT_EQV_ITEM_PRC_PER_DOS","ALT_RLZD_SVGS","Sum_This_Rlzd_Svgs"};
		
		String headerFields[] = {"CUSTOM_HEADING","MONTH_SELECTED","REPORT_CREATED","CUSTOMER_INFO","BaseDir","ImageFile","INCLUDE_PAGE_NUMBER","INCLUDE_IMAGE_ON_REPORT","REPORT_IMAGE_DIR","REPORT_IMAGE_NAME"};
			
		try
		{
			reportIDList = obj.getreportIDList();
			ArrayList reportUserIDList;
			reportUserIDList = (ArrayList)obj.getUserIDList();
			reportVOList = obj.getValueObject((ArrayList)reportIDList);
			reportCustAcctDetailsList = obj.getCustDetails(reportVOList,reportUserIDList);
			reportHeaderMapList = obj.getHeaderInfo(reportVOList);
			reportMonth = obj.getCustSelectedMonth(reportVOList);
			
			if(reportCustAcctDetailsList.size()!=0)
			{
				for(int i=0;i<reportCustAcctDetailsList.size();i++)
				{	
					try
					{
						if(reportHeaderMapList.size()!=0)
						{
							headerMap = (Map)reportHeaderMapList.get(i);
							custAcctDetailsList=(ArrayList)reportCustAcctDetailsList.get(i);
							
							for(int j=0;j<reportMonth.size();j++)
							{
								monthArray = (String[])reportMonth.get(j);
								reportStartMonth = monthArray[0];
								reportEndMonth = monthArray[1];
							}
	
							List<Object[]> detailList = obj.getReportDetailData((ReportBaseVO)reportVOList.get(i),custAcctDetailsList,reportStartMonth,reportEndMonth,(String)reportUserIDList.get(i));
							
							if(detailList!= null)
							{						
								obj.generateReport(detailList,detailFields,headerMap,(Integer)reportIDList.get(i),(String)reportUserIDList.get(i),reportDate);
								obj.updateReportStatusAsCompleted((Integer)reportIDList.get(i),detailList.size());
								log.info("MHS Realized Savings Report Generated Successfully....");	
							}
						}
						else
						{
							log.info("No Records are Available to Generate the MHS Realized Savings Report");
						}
					}
					catch (SMORADatabaseException e)
					{
						log.error("Failed to execute the Query");
						reportStatusVO.setReportId((Integer)reportIDList.get(i));
						reportStatusVO.setStatusId(REPORT_COMPLETED);
						reportStatusVO.setEndDate(new Date());
						reportStatusVO.setErrorText("Failed to execute the Query");
						reportStatusVO.setErrDts(new Date());
						reportStatusVO.setPdfStatus(ReportManagerConstants.REPORT_ERROR);
						rptStatusDB.updateIfFieldIsNotNull(reportStatusVO);
						throw new SMORADatabaseException("Failed to execute the Query");
					}
					catch (Exception e)
					{
						log.error("Failed to Generate the Report");
						reportStatusVO.setReportId((Integer)reportIDList.get(i));
						reportStatusVO.setStatusId(REPORT_COMPLETED);
						reportStatusVO.setEndDate(new Date());
						reportStatusVO.setErrorText("Failed to Generate the Report");
						reportStatusVO.setErrDts(new Date());
						reportStatusVO.setPdfStatus(ReportManagerConstants.REPORT_ERROR);
						rptStatusDB.updateIfFieldIsNotNull(reportStatusVO);
						throw new SMORAException("Failed to Generate the Report");
					}
					
				}
			}
			else
			{
				log.info("No Records are Available to Generate the MHS Realized Savings Report");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("Failed to Generate the MHS Realized Savings Report");
			System.exit(1);
		}
	}
}

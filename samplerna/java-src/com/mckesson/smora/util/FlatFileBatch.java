/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Iterator;

import com.mckesson.smora.appl.reportsengine.ReportGenerator;
import com.mckesson.smora.appl.util.FileUtils;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.AccountGroupDB;
import com.mckesson.smora.database.dao.ReportStatusDB;
import com.mckesson.smora.database.model.TApplAcctGrp;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.dto.CustomReportVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORADatabaseException;
import com.mckesson.smora.exception.SMORAException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class FlatFileBatch.
 */

public class FlatFileBatch
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "FlatFileBatch";
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(FlatFileBatch.class);
	/**
	 * The report status DB.
	 */
	private ReportStatusDB reportStatusDB = new ReportStatusDB();
	/**
	 * The month date.
	 */
	String monthDate;
	/**
	 * The report details VO list.
	 */
	ArrayList reportDetailsVOList = new ArrayList();
		
		
	/*
	 * This Method returns the reportID List  
	 * Sets the Flat file Month and Date
	 * 
	 * @param
	 * @return 
	 * */
	/**
	 * Getreport ID list.
	 * 
	 * @return the report ID list
	 */
	protected List getreportIDList() throws SMORAException	
	{
		final String METHODNAME = "getreportIDList";
		List reportList = null;
		Date date = new Date();
		
		
		try
		{	
			log.info("Retriving the ReportIDs");
			reportList = reportStatusDB.getReportStatusForSubmarine(ReportManagerConstants.SUBMARINE_REPORT);
			
		}
		catch(SMORADatabaseException e)
		{
			log.error("Failed to get the ReportList from Database");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		return reportList;
	}
	
	/*
	 * This Method returns the reportUserID List  
	 * 
	 * 
	 * @param
	 * @return 
	 * */
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
			log.error("Failed to get the ReportUserList from Database");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
			
		}
		catch(Exception e)
		{
			log.error("Failed to get the ReportUserList");
		}
		
		return reportList;
	}
	
	
	
	/*
	 * This Method returns the ValueObject for a Given reportID 
	 * 
	 * 
	 * @param
	 * @return 
	 * */
	/**
	 * This method gets the value object.
	 * 
	 * @param reportIDList the report ID list
	 * 
	 * @return the value object
	 */
	protected ArrayList getValueObject(ArrayList reportIDList) throws SMORAException
	{
		final String METHODNAME = "getValueObject";
		int reportID;
		String xmlData;
		ArrayList voList = new ArrayList();
		ReportBaseVO rptBaseVO = null;
		ReportStatusDB reportStatusDB = new ReportStatusDB();
		
		try
		{
			log.info("Retriving the ValueObjects");
			
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
			log.error("Failed to get the ReportIDs from Database");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			log.error("Failed to get the ReportIDs");
		}
		return voList;
	}
	
	
	/*
	 * This Method gets the CustomerDetails for a Given Value Object
	 * 
	 * 
	 * @param
	 * @return 
	 * */
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
			ArrayList custAccntDetailsList=new ArrayList();
			CustomReportVO customRprtVO;
			CriteriaVO criteriaVO;
			CustomerVO custVO; 
			AccountDetailsListVO accntDetailsListVO;
			ReportBaseVO reportBsVO;
			DateSelectionAndComparisonVO dateselectionVO;
			UserRolesVO userRolesVO;
			AccountDB accountDB = new AccountDB();
						
			
			try
			{
				log.info("Retriving the CustomerDetails");
				for(int k=0;k<rptBaseVOList.size();k++)
				{
					reportBsVO = (ReportBaseVO)rptBaseVOList.get(k);
					customRprtVO = reportBsVO.getCustomReportVO();
					criteriaVO = customRprtVO.getCustReportCriteriaVO();
					custVO = criteriaVO.getCustomerVO();
					
					accntDetailsListVO = custVO.getAccountDetailsListVO();
					dateselectionVO = criteriaVO.getDateSelectionAndComparisonVO();
					dateselectionVO.getDateSelection();
				    ArrayList accountDetailsList = accntDetailsListVO.getAccountDetailsVOList();
				    
					if(accountDetailsList!=null)
					{
						Iterator accountDetailsIterator=accountDetailsList.iterator();
						
						while(accountDetailsIterator.hasNext())
						{
							AccountDetailsVO accountDetailVO = null;
							accountDetailVO=(AccountDetailsVO)accountDetailsIterator.next();
							custAccntDetailsList.add(accountDetailVO.getAccountNum());
							
						}
					}
					else
					{									
						List allAccountList = accountDB.getAccountsForUser((String)rptUserIDList.get(k));
						custAccntDetailsList.addAll(allAccountList);
						
					}
					
				}
			}
			catch(Exception e)
			{
				
				log.error("Failed to get the Customer Account DetailsList");
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
			
			return custAccntDetailsList;
			
		}
	 /*
		 * This Method gets the SelectedMonthDetails for a Given Value Object
		 * 
		 * 
		 * @param
		 * @return 
		 * */
	 
	 /**
 	 * This method gets the cust selected month.
 	 * 
 	 * @param reportBaseVOList the report base VO list
 	 * 
 	 * @return the cust selected month
 	 */
 	protected ArrayList getCustSelectedMonth(ArrayList reportBaseVOList)  throws SMORAException
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
			String toFormat = "yyyyMM";
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
							toFormatDate = toFormatter.format(toParseDate);
							try
							{
								dateCollection[0]= fromFormatDate;
								dateCollection[1]= toFormatDate;
							}
							catch(Exception e)
							{
								log.error("Failed to parse the selected Date");
							}
							custMonthSelectedList.add(i,dateCollection);
						}
					}
				
				}
			
			catch(Exception e)
			{
				log.error("Failed to get the Customer Selected Month");
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
	 }
		 
		 return custMonthSelectedList;
	 }
	 
	 
	 /**
 	 * This method gets the cust year month.
 	 * 
 	 * @param timeDetails the time details
 	 * 
 	 * @return the cust year month
 	 */
 	protected String getCustYearMonth(ArrayList timeDetails)
	 {
		 String yearMonth = null;
         String[] custMonthSelected;
		
         for(int i=0;i<timeDetails.size();i++)
         {
        	 custMonthSelected = (String[])timeDetails.get(i);
        	 yearMonth = custMonthSelected[0];
         }
		 
		 return yearMonth; 
	 }
	 
	 	
	/*
	 * This Method updates the T_APPL_TEMP_CMS_RLZD_SAV table
	 * 
	 * 
	 * @param
	 * @return 
	 * */	
	/**
	 * This method updates the temp table.
	 * 
	 * @param custYearMonthString the cust year month string
	 * @param customerDetails the customer details
	 * 
	 * @return true, if update temp table
	 */
	protected boolean updateTempTable(ArrayList customerDetails,String custYearMonthString) throws SMORAException	
	{
		final String METHODNAME = "updateTempTable";
		boolean status = false;
		String custAccntID; 
	
				
		try
		{
			Set custAcctSet = new HashSet(customerDetails);
			ArrayList uniqueCustDetail = new ArrayList(custAcctSet);
					
			log.info("Updating the T_APPL_TEMP_CMS_RLZD_SAV table");
			
			for(int i=0;i<uniqueCustDetail.size();i++)
			{
				custAccntID = uniqueCustDetail.get(i).toString();
				status = reportStatusDB.create(custAccntID,custYearMonthString);
			}
		}
		catch(SMORADatabaseException e)
		{
			log.error("Failed to update the T_APPL_TEMP_CMS_RLZD_SAV table ");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		return status;
	}
	
	/**
	 * This method updates the temp table.
	 * 
	 * @param custYearMonthString the cust year month string
	 * @param customerDetails the customer details
	 * 
	 * @return true, if update temp table
	 */
	protected boolean updateTempMHSAccountTable() throws SMORAException	
	{
		final String METHODNAME = "updateTempMHSAccountTable";
		boolean status = false;
		String custAccntID; 
	
		try
		{
			log.info("Updating the S_APPL_TEMP_MHS_ACCT  table");
			status = reportStatusDB.createTempMHSAccount();			
		}
		catch(SMORADatabaseException e)
		{
			log.error("Failed to update the S_APPL_TEMP_MHS_ACCT  table ");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		return status;
	}
	
	
	/*
	 * This Method fetches the CustomerDetails from the 
	 * T_APPL_TEMP_CMS_RLZD_SAV and T_MCK_MTH_PURCH_HIST table Join
	 * 
	 * @param
	 * @return 
	 * */		
    /**
	 * This method gets the customer item.
	 * 
	 * @return the customer item
	 */
	protected ArrayList getCustomerItem() throws SMORAException	
    {		
		final String METHODNAME = "getCustomerItem";
		ArrayList custItemList = null;
		log.info("Retriving the CustomerItem");
		try
		{
			custItemList = (ArrayList)reportStatusDB.getDistinctCustomerAndItem();
		}
		catch(SMORADatabaseException e)
		{
			log.error("Failed to fetch the DistinctCustomerItemList");
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		catch(Exception e)
		{			
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		return custItemList;
		
	} 

	
	/*
	 * This Method generates the CustomerItemID
	 *  
	 * @param
	 * @return 
	 * */	
	/**
	 * This method generates the customer item ID.
	 * 
	 * @param custItemList the cust item list
	 * 
	 * @return the string[]
	 */
	protected String[] generateCustomerItemID(ArrayList custItemList) throws SMORAException
	{
		final String METHODNAME = "generateCustomerItemID";
		log.info("Retriving the CustItemID");
		String[] finalArray = null;	
		try
		{
			if(custItemList!=null && custItemList.size()!=0)
			{
				String[] customerItemArray = new String[custItemList.size()];
				for(int i=0;i<custItemList.size();i++)
				{
					Object [] obj = (Object[])custItemList.get(i);
					customerItemArray[i] = obj[0] + "|" + obj[1]; 				
					if(reportStatusDB.insertCmsAcctOrigItem((String)obj[0],(String)obj[1]))
					{
						log.info("CMS_ACCT_ORIG_ITEM Table has been updated Successfully");
					}
					else
					{
						log.info("CMS_ACCT_ORIG_ITEM Table has not been Updated");
					}
				}
				finalArray = customerItemArray;
			}
			else
			{
				String[] customerItemArray = new String[1];
				customerItemArray[0] = "999999|9999999";				
				finalArray = customerItemArray;
				log.info("CMS_ACCT_ORIG_ITEM Table has not been Updated - No Customer Item Combination");
			}
		}
		catch(Exception e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		return finalArray;
	}
	
	
	/* 
	 * This Method writes the content in a Flat File
	 * 
	 * @param custItem,mothDate
	 * @return
	 * */
	/**
	 * This method writes text file.
	 * 
	 * @param custItem the cust item
	 */
	protected int writeTextFile(String[] custItem) throws SMORAException	
	{
		final String METHODNAME = "writeTextFile";
		Date fileDate = new Date();
		String reportName = null;
		FileUtils fileUtil = new FileUtils();
		log.info("Writing the TextFile");
		try
		{
			String filePath = fileUtil.getRelativeBatchFileSystemPath();
			
			fileDate.getTime();
			String fileDateFormat = "MMdd";
			SimpleDateFormat fileDateFormatter = new SimpleDateFormat(fileDateFormat);
			String monthDate = fileDateFormatter.format(fileDate);
			reportName = filePath+"/RARS"+ monthDate + ".txt";
		
			FileOutputStream fos = new FileOutputStream(reportName);
			PrintWriter pw = new PrintWriter(fos);
			
			for(int i=0;i<custItem.length;i++)
			{
				if(custItem[i]==null)
				{
					custItem[i] = "9999999999999";
				}				
				pw.println(custItem[i]);
				pw.flush();
			}
			
			return 1;
		}
		catch(IOException ioe)
		{
			log.error("Failed to create/write the FlatFile");
			return 0;
		}
		catch(Exception e)
		{
			log.error("Failed to Generate the FlatFile");			
			throw new SMORAException(e,CLASSNAME,METHODNAME);			
		}
		
		
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the args
	 */
	public static void main(String args[])
	{
		FlatFileBatch obj = new FlatFileBatch();
		List reportIDList;
		ArrayList reportUserIDList;
		ArrayList reportVOList = new ArrayList();
		ArrayList custAcctDetailsList = new ArrayList();
		ArrayList custTimeSelectedDetailsList = new ArrayList();
		String yearMonth = null;
		ReportStatusDB rptStatusDB = new ReportStatusDB();
		try
		{				
			reportIDList = obj.getreportIDList();
	/* Below lines are commented for Generating the dummy flat file even if there are no records in the database 
	   Issue No.127 by Venkatesh Perumal on 21/12/2006
	*/ 
	  		
		//	if(reportIDList.size()!=0)
		//	{
				reportUserIDList = (ArrayList)obj.getUserIDList();
				reportVOList = obj.getValueObject((ArrayList)reportIDList);
 				custAcctDetailsList = obj.getCustDetails(reportVOList,reportUserIDList);
				custTimeSelectedDetailsList = obj.getCustSelectedMonth(reportVOList);
				yearMonth = obj.getCustYearMonth(custTimeSelectedDetailsList);
				// Deletes the Contents from S_APPL_TEMP_CMS_RLZD_SAV and S_APPL_TEMP_MHS_ACCT Table
				if(rptStatusDB.deleteMHSContent("TApplTempCmsRlzdSav"))
				{
					log.info("Data deleted from S_APPL_TEMP_CMS_RLZD_SAV Table Successfully");
				}
				if(rptStatusDB.deleteMHSContent("TApplTempMhsAcct"))
				{
					log.info("Data deleted from S_APPL_TEMP_MHS_ACCT Table Successfully");
				}
				if(rptStatusDB.deleteMHSContent("TCmsAcctOrigItem"))
				{
					log.info("Data deleted from S_CMS_ACCT_ORIG_ITEM Table Successfully");
				}
				
				//Account and Date information inserted into the temporary table T_APPL_TEMP_CMS_RLZD_SAV 
				if(!obj.updateTempTable(custAcctDetailsList,yearMonth))
				{
					log.info("T_APPL_TEMP_CMS_RLZD_SAV Table has not been Updated");
				}
				else
				{
					log.info("T_APPL_TEMP_CMS_RLZD_SAV Table Updated Successfully");
				}
				
				/*S_APPL_TEMP_CMS_RLZD_SAV table should be joined with the S_ACCT_CNTRC_LEAD_TYP table
				and the information is inserted into the S_APPL_TEMP_MHS_ACCT table*/
				
				if(obj.updateTempMHSAccountTable())
				{
					log.info("S_APPL_TEMP_MHS_ACCT Table Updated Successfully");
				}
				else
				{
					log.info("S_APPL_TEMP_MHS_ACCT Table has not been Updated");
				}
				
				ArrayList custItemList = obj.getCustomerItem();
				String[] custItemID = obj.generateCustomerItemID(custItemList); 
				if(obj.writeTextFile(custItemID)==1)
				{
					log.info("MHS Flat file Generated Successfully..");
				}
				else
				{
					log.error("Failed to Generate the MHS FlatFile");
				}
				
		//	}
		//	else
		//	{
		//		log.info("No Records are Available to Generate the MHS Flat file ");
		//	}
			
			
		}
		catch(Exception e)
		{			
			log.error("Failed to Generate the Flat File");
			System.exit(1);
		}
		
	}


	
}

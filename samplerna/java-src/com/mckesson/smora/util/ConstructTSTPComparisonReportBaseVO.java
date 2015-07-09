/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.database.util.TSTPComparisonQueryBuilder;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.TSTPFormattingVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.TimeSeriesGroupVO;
import com.mckesson.smora.dto.TimeSeriesTPComparisonVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.TimeSeriesTPComparisonForm;
import com.mckesson.smora.ui.form.TimeSeriesTPComparisonFormattingSelectionForm;

/**
 * 
 * This Class is used to Construct the ReportBaseVO for Time Series  - Time Period Comparison Reports
 * 
 */

public class ConstructTSTPComparisonReportBaseVO extends ConstructCannedReportBaseVO
{
	
	protected static Log log = LogFactory.getLog(ConstructTSTPComparisonReportBaseVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructTSTPComparisonReportBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static TimeSeriesTPComparisonVO timeSeriesTPComparisonVO = null;
	
	private static CriteriaVO criteriaVO = null;
	
	private static TSTPFormattingVO tSTPFormattingVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
	private static TimeSeriesGroupVO timeSeriesGroupVO = null;

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateTSTPReportBaseVO(TimeSeriesTPComparisonForm timeSeriesTPComparisonForm , String userId) throws SMORAException
	{
		List<TApplCnRptFldMetaData> metaDataList = null;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		timeSeriesGroupVO = new TimeSeriesGroupVO();
		timeSeriesTPComparisonVO = new TimeSeriesTPComparisonVO();
		criteriaVO = new CriteriaVO();
		DateConverision dateConversion = new DateConverision();
		
		TimeSeriesTPComparisonFormattingSelectionForm timeSeriesTPComparisonFormattingSelectionForm = timeSeriesTPComparisonForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = timeSeriesTPComparisonForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = timeSeriesTPComparisonForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = timeSeriesTPComparisonForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = timeSeriesTPComparisonForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = timeSeriesTPComparisonForm.getAdvancedFiltersForm();
		
		/*
		 * SO-0953 :  Code added to set the calendar selected dates 
		 */
		if(dateSelectionForm.getStartCompareTime11()!= null && dateSelectionForm.getStartCompareTime11().trim()!=""){
			String formattedStartDate11 = dateConversion.formatCustomDate(dateSelectionForm.getStartCompareTime11(), "MM/dd/yyyy");
			dateSelectionForm.setStartCompareTime11(formattedStartDate11);
		}
		
		if(dateSelectionForm.getEndCompareTime11()!= null && dateSelectionForm.getEndCompareTime11().trim()!=""){
			String formattedEndDate11 = dateConversion.formatCustomDate(dateSelectionForm.getEndCompareTime11(), "MM/dd/yyyy");
			dateSelectionForm.setEndCompareTime11(formattedEndDate11);
		}
		
		if(dateSelectionForm.getStartCompareTime21()!= null && dateSelectionForm.getStartCompareTime21().trim()!=""){
			String formattedStartDate21 = dateConversion.formatCustomDate(dateSelectionForm.getStartCompareTime21(), "MM/dd/yyyy");
			dateSelectionForm.setStartCompareTime21(formattedStartDate21);
		}
		
		if(dateSelectionForm.getEndCompareTime21()!= null && dateSelectionForm.getEndCompareTime21().trim()!=""){
			String formattedEndDate21 = dateConversion.formatCustomDate(dateSelectionForm.getEndCompareTime21(), "MM/dd/yyyy");
			dateSelectionForm.setEndCompareTime21(formattedEndDate21);
		}
		
		/*
		 * SO-0953 :  Code ends
		 */
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(timeSeriesTPComparisonFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(timeSeriesTPComparisonFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(timeSeriesTPComparisonFormattingSelectionForm.getCustomHeading());
		baseVO.setHtml(timeSeriesTPComparisonFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(timeSeriesTPComparisonFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(timeSeriesTPComparisonFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(timeSeriesTPComparisonFormattingSelectionForm.isResultsDisplayCSV());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.TIMESERIES_TP_COMPARISON);
		
		log.info("The selected format is :" + timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel());
		
		//Setting the report Group Id based on the format
		if (timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel().equals("Full Detail"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_GENERIC);
		}
		else
		{			
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_GENERIC);
		}	
		log.info(" Report Group ID for Timeperiod comparison report is : "+ baseVO.getReportGroupID());
		
		//populating the Time Series Comparison and Time Series Group VOs
		tSTPFormattingVO = constructCannedReportVO(timeSeriesTPComparisonFormattingSelectionForm);
		timeSeriesTPComparisonVO.setTSTPFormattingVO(tSTPFormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		timeSeriesGroupVO.setTimeSeriesTPComparisonVO(timeSeriesTPComparisonVO);
		
		//populating the Criteria VO
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		//populating the FieldsList
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, tSTPFormattingVO, userId);
		log.info("fieldsList from populateTSTPReportBaseVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		
		//populating the Canned reportVO and ReportBaseVO
		cannedReportVO.setTimeSeriesGroupVO(timeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("43,46");
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	/**
	 * This method constructs the TSTP FormattingVO
	 *
	 * @param timeSeriesTPComparisonFormattingSelectionForm the TimeSeriesTPComparisonFormattingSelectionForm
	 *
	 * @return TSTPFormattingVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private static TSTPFormattingVO constructCannedReportVO(TimeSeriesTPComparisonFormattingSelectionForm timeSeriesTPComparisonFormattingSelectionForm) throws SMORAException
	{
		if (tSTPFormattingVO== null)
		{
			tSTPFormattingVO = new TSTPFormattingVO();
		}
		
		tSTPFormattingVO.setAccountFormat(timeSeriesTPComparisonFormattingSelectionForm.getAccountFormat());
		tSTPFormattingVO.setFormat(timeSeriesTPComparisonFormattingSelectionForm.getFormat());
		tSTPFormattingVO.setDetailLevel(timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel());
		tSTPFormattingVO.setSortOptions(timeSeriesTPComparisonFormattingSelectionForm.getSortOptions());
		return tSTPFormattingVO;
	}
	
	/**
	 * This method constructs the DateSelectionAndComparisonVO
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return DateSelectionAndComparisonVO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{

		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
		dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();
		
		if (dateSelectionForm.getStartCompareTime11() != null)
		{
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			comparePeriod1TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime11());
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime11());
			timePeriodList.add(comparePeriod1TimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime21() != null)
		{
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			comparePeriod2TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime21());
			comparePeriod2TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime21());
			timePeriodList.add(comparePeriod2TimePeriodVO);
		}
		timePeriodListVO.setTimeperiodVOList(timePeriodList);

		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);
		
		return dateSelectionAndComparisonVO;
	}
	
	
	
	//TODO This method is already implemented in the DAO.Need to move it to a common place.
	/**
	 * This method gets the Number of Account for an User
	 */
	public static int getNumberOfAccounts(CustomerVO customerVO, String userId)
	{
		log.info("Begin: getNumberOfAccounts");
		int noOfAccnts = 0;
		ArrayList<AccountDetailsVO> accountDetailsVO =  null;
		UserRolesVO userRolesVO = customerVO.getRoles();
		if (userRolesVO != null)
		{
			log.info("Getting the number of accounts from UserRolesVO");
			ArrayList<RoleVO> cidRoles = userRolesVO.getCidRoles();						
			ArrayList<RoleVO> hspRoles = userRolesVO.getHspRoles();						
			ArrayList<RoleVO> chnRoles = userRolesVO.getChnRoles();						
			ArrayList<RoleVO> slsRoles = userRolesVO.getSlsRoles();
			if ((cidRoles != null && cidRoles.size() > 0) || 
				(hspRoles != null && hspRoles.size() > 0) || 
				(chnRoles != null && chnRoles.size() > 0) || 
				(slsRoles != null && slsRoles.size() > 0)) 
			{ 
				AccountDB accountDB = new AccountDB();
				try{
					accountDetailsVO =  accountDB.getAccountInfo(userRolesVO); //TODO needs to confirm on rowlimits parameter
				}catch(SMORAException e)
				{
					log.info("SMORAException while getting the numger of accounts : " + e);
					noOfAccnts = 0; //TODO this needs to be verified again
				}
				if(accountDetailsVO != null & accountDetailsVO.size() > 0)
				{
					noOfAccnts = accountDetailsVO.size();
				}
			}	
			else
			{
				log.info("Getting the number of accounts from AccountDetailsListVO");
				AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
				accountDetailsVO = accountDetailsListVO.getAccountDetailsVOList();
				if(accountDetailsVO != null)
				{
					noOfAccnts = accountDetailsVO.size();
				}
				
			}
			
		}
		else
		{
			log.info("Getting the number of accounts from AccountDetailsListVO");
			AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
			accountDetailsVO = accountDetailsListVO.getAccountDetailsVOList();
			if(accountDetailsVO != null)
			{
				noOfAccnts = accountDetailsVO.size();
			}

		}
		
		if (noOfAccnts <= 0)
		{
			try
			{
				TSTPComparisonQueryBuilder comparisonQueryBuilder = new TSTPComparisonQueryBuilder();
				List<String> accountList = comparisonQueryBuilder.getDefaultAccountsValues(userId);
				if (accountList != null)
				{
					noOfAccnts = accountList.size();
				}	
				else
				{
					noOfAccnts =0;
				}	
			}
			catch (SMORAException sme)
			{				
				log.error("Exception in getNumberOfAccounts() : " + sme);
			}			
		}
		log.info("Number of Accounts = " + noOfAccnts);
		if (noOfAccnts > 10)
		{
			log.info("Number of Accounts are greater than 10");
			noOfAccnts = 10;
		}
		log.info("No of Accounts from getNumberOfAccounts method : \n " + noOfAccnts);
		log.info("End: getNumberOfAccounts");
		return noOfAccnts;
	}
	
	/**
	 * This method returns fieldVOs List which should be populated in the Jasper report 
	 * @param metaDataList Meta List from T_Appl_Cn_Rpt_FldMeta_Data table
	 * @param tSTPFormattingVO Time Series Time period Comparison FormattingVO
	 * @param userId User Id
	 * @return Fields VO Array List
	 */
	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,TSTPFormattingVO tSTPFormattingVO , String userId)
	{
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		int noOfAccounts = 0;
		if (tSTPFormattingVO.getAccountFormat().equals("Break By Account"))
		{
			   noOfAccounts = getNumberOfAccounts(criteriaVO.getCustomerVO() , userId);
		}
		if (metaDataList != null && metaDataList.size() > 0)
		{
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
				if (tApplCnRptFldMetaData.getIsColHdr().equals("Y"))
				{
					int fieldId = tApplCnRptFldMetaData.getId().getFldId();
					FieldsVO fieldsVO = new FieldsVO();
					fieldsVO.setFldId(fieldId);
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
					
					//For Generic 
					if (tSTPFormattingVO.getFormat().equals("Generic"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Generic Code"))
						{
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Generic Description"))
						{
							fieldsList.add(fieldsVO);
						}
					}
					
					//For Therapeutic
					else if (tSTPFormattingVO.getFormat().equals("Therapeutic"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Therapeutic Code"))
						{
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Therapeutic Description"))
						{
							fieldsList.add(fieldsVO);
						}
					}
					
					//For Item
					else if (tSTPFormattingVO.getFormat().equals("Item"))
					{
						 if (tApplCnRptFldMetaData.getFldDscr().equals("McKesson Item #"))
						{
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Item Description"))
						{
								fieldsList.add(fieldsVO);
						}
					}
					
					//For Local Department
					else if (tSTPFormattingVO.getFormat().equals("Local Department"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Local Department"))
						{
							fieldsList.add(fieldsVO);
						}
					}
					//For Ordering Department
					else if (tSTPFormattingVO.getFormat().equals("Ordering Department"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Dept. ID"))
						{
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Ordering Department Name"))
						{
								fieldsList.add(fieldsVO);
						}
						
					}
				}
			}
			
			//For Break by  Accounts
			if (tSTPFormattingVO.getAccountFormat().equals("Break By Account"))
			{
				for (int j = 1; j <= noOfAccounts; j++)
				{
					for (int i=0; i < metaDataListSize; i++)
					{
						tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
						int fieldId = tApplCnRptFldMetaData.getId().getFldId();
						FieldsVO fieldsVO = new FieldsVO();
						fieldsVO.setFldId(fieldId);					
						if (tApplCnRptFldMetaData.getFldDscr().equals("Account Number"))
						{
							fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
							fieldsList.add(fieldsVO);						
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Account Name"))
						{
							fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
							fieldsList.add(fieldsVO);						
						}
						
						// For Full Detail reports  Only
						if (!(tSTPFormattingVO.getDetailLevel().equals("$$ Change Only")))
						{
							if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 1> Net $$"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("PER1_ON_CNTRC_AMT"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 1> % on Contract"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 2> Net $$"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("PER2_ON_CNTRC_AMT"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 2> % on Contract"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
						}
						
						if (tApplCnRptFldMetaData.getFldDscr().equals("$$ Diff"))
						{
							fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
							fieldsList.add(fieldsVO);
						}
						
						//For Full Detail reports Only
						if (!(tSTPFormattingVO.getDetailLevel().equals("$$ Change Only")))
						{
							if (tApplCnRptFldMetaData.getFldDscr().equals("% Change Net $$"))
							{
								fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias()+ "_"+ j);
								fieldsList.add(fieldsVO);
							}
						}	
					}
				}
			}
								
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
				int fieldId = tApplCnRptFldMetaData.getId().getFldId();
				FieldsVO fieldsVO = new FieldsVO();
				fieldsVO.setFldId(fieldId);
				fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
				
				//For Full Detail reports only
				if (!(tSTPFormattingVO.getDetailLevel().equals("$$ Change Only")))
				{
					if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 1> Net $$"))
					{
						fieldsList.add(fieldsVO);
					}			
					else if (tApplCnRptFldMetaData.getFldDscr().equals("PER1_ON_CNTRC_AMT"))
					{
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 1> % on Contract"))
					{
						fieldsList.add(fieldsVO);
					}				
					else if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 2> Net $$"))
					{
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("PER2_ON_CNTRC_AMT"))
					{
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("<Timeperiod 2> % on Contract"))
					{
						fieldsList.add(fieldsVO);
					}			
				}
				if (tApplCnRptFldMetaData.getFldDscr().equals("$$ Diff"))
				{
					fieldsList.add(fieldsVO);
				}
				
				//For Full Detail reports only
				if (!(tSTPFormattingVO.getDetailLevel().equals("$$ Change Only")))
				{
					if (tApplCnRptFldMetaData.getFldDscr().equals("% Change Net $$"))
					{
						fieldsList.add(fieldsVO);
					}
				}	
			}
			
		}
		return fieldsList;
	}
}
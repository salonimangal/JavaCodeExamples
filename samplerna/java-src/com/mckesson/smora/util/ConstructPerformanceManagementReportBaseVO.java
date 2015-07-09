/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;


import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.database.util.TSTPComparisonQueryBuilder;
//import com.mckesson.smora.database.util.TSTPComparisonQueryBuilder;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GenericListVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.ReportPerformanceManagementTop200FormattingVO;
import com.mckesson.smora.dto.ReportPerformanceManagementTop200VO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TherapeuticListVO;
//import com.mckesson.smora.dto.TSTPFormattingVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
//import com.mckesson.smora.dto.TimeSeriesReportDetailFormattingVO;
//import com.mckesson.smora.dto.TimeSeriesSummaryVO;
//import com.mckesson.smora.dto.TimeSeriesTPComparisonVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
//import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
//import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportPerformanceManagementTop200FormattingSelectionForm ;
import com.mckesson.smora.ui.form.ReportPerformanceManagementTop200Form ;
//import com.mckesson.smora.ui.form.TimeSeriesReportDetailFormattingSelectionForm;
//import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;


/**
 * 
 * This Class is used to Construct the ReportBaseVO for Performance management Reports
 * 
 */



public class ConstructPerformanceManagementReportBaseVO extends  ConstructCannedReportBaseVO{
	
	protected static Log log = LogFactory.getLog(ConstructPerformanceManagementReportBaseVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPerformanceManagementReportBaseVO";
	/**
	 * The cust report criteria VO.
	 */
	private static ReportPerformanceManagementTop200VO reportPerformanceManagementTop200VO = null;
	
	private static CriteriaVO criteriaVO = null;
	
	private static ReportPerformanceManagementTop200FormattingVO reportPerformanceManagementTop200FormattingVO = null ;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
//	private static TimeSeriesGroupVO timeSeriesGroupVO = null;
	
	
	
	/**
	 * This method populates the report base VO.
	 *
	 * @param PerformanceManagement  reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populatePerformanceManagementReportBaseVO(ReportPerformanceManagementTop200Form  reportPerformanceManagementTop200Form , String userId) throws SMORAException
	{
		
		
		List<TApplCnRptFldMetaData> metaDataList = null;
		
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		
		ArrayList<FirstGroupLevelVO> firstGroupLevelVOList = new ArrayList<FirstGroupLevelVO>();
		ArrayList<String> subTotalArray = new ArrayList<String>();
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<String> fieldDescArray = new ArrayList();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
	
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		reportPerformanceManagementTop200VO = new ReportPerformanceManagementTop200VO() ;
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		ReportPerformanceManagementTop200FormattingSelectionForm  formattingSelectionForm=reportPerformanceManagementTop200Form.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportPerformanceManagementTop200Form.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportPerformanceManagementTop200Form.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportPerformanceManagementTop200Form.getSupplierSelectionForm();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(formattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(formattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		
		baseVO.setCustomHeading(formattingSelectionForm.getCustomHeading());
		baseVO.setHtml(formattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(formattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(formattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(formattingSelectionForm.isResultsDisplayCSV());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_PERFORMANCE_TOP200);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_PERFORMANCE_TOP200);
//		populating the Time Series Comparison and Time Series Group VOs
		reportPerformanceManagementTop200FormattingVO = constructCannedReportVO(formattingSelectionForm);
		reportPerformanceManagementTop200VO.setReportPerformanceManagementTop200FormattingVO(reportPerformanceManagementTop200FormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		String format =formattingSelectionForm.getAccountFormat();
		
		if(!format.equals("Combine Account"))
		{
			
			subTotalArray.add("Return Quantity");
			subTotalArray.add("Return Dollars");
			
			
			fieldDescArray.add("Return Quantity");
			fieldDescArray.add("Return Dollars");
						
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account #");
			firstGroupLevelVO.setFieldName("Account #");
			firstGroupLevelVO.setFieldWidth(100);
			firstGroupLevelVO.setSubTotal(subTotalArray);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);
		
		}
		else
		{	
			subTotalArray.add("Return Quantity");
			subTotalArray.add("Return Dollars");
			
			
			fieldDescArray.add("Return Quantity");
			fieldDescArray.add("Return Dollars");
			
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Rank");
			secondGroupLevelVO.setFieldName("Rank");
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			
			
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);
		}
		
		
		
		//log.info("The selected format is :" + reportPerformanceManagementTop200Form.);

		
		
		
			//	populating the Performance management VO
		reportPerformanceManagementTop200FormattingVO = constructCannedReportVO(formattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		//populating the Criteria VO
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);		
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		
		ItemVO itemVO = new ItemVO();
		ItemNumbersListVO itemNumbersListVO = new ItemNumbersListVO();
		itemNumbersListVO.setItemNumberList(null);		
		itemVO.setItemNumbersList(itemNumbersListVO);
		
		GenericListVO genericVO = new GenericListVO();
		genericVO.setGenericVOList(null);
		itemVO.setGenericGroupsList(genericVO);
		
		itemVO.setItemGroupId(null);
		TherapeuticListVO theraListVO = new TherapeuticListVO();
		theraListVO.setTherapeuticList(null);
		itemVO.setTherapeuticList(theraListVO);
		
		criteriaVO.setItemVO(itemVO);
		reportPerformanceManagementTop200VO.setReportPerformanceManagementTop200FormattingVO(reportPerformanceManagementTop200FormattingVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, reportPerformanceManagementTop200VO, userId);
		log.info("fieldsList from populatePerformanceTop200ReportBaseVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
	
		//populating the Canned reportVO and ReportBaseVO
		cannedReportVO.setReportPerformanceManagementTop200VO(reportPerformanceManagementTop200VO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("35,33");
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	
	}


	
	
	
	
	/**
	 * This method gets the Number of Account for an User
	 */
/*	public static int getNumberOfAccounts(CustomerVO customerVO, String userId)
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
				//TSTPComparisonQueryBuilder comparisonQueryBuilder = new TSTPComparisonQueryBuilder();
				List<String> accountList = comparisonQueryBuilder.getDefaultAccounts(userId);
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
	}*/
	
	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{

		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();

		if (dateSelectionForm.getStartSelectedTime2() != null && dateSelectionForm.getEndSelectedTime2() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
			timePeriodList.add(standardTimePeriodVO);
		}
		timePeriodListVO.setTimeperiodVOList(timePeriodList);


		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setIncludeCurrentMonth(dateSelectionForm.getIncludeCurrentMonth());

		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setLastXMonths(Integer.parseInt(dateSelectionForm.getLastXMonths()));

		dateSelectionAndComparisonVO.setSelectComparison(dateSelectionForm.getDateComparison());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);


		
		return dateSelectionAndComparisonVO;
	}

	/**
	 * This method constructs the canned report VO.
	 * 
	 * @param ReportPerformanceManagementTop200FormattingSelectionForm
	 *            the reportPerformanceManagementTop200selection form
	 * @return eportPerformanceManagementTop200Vo
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	private static ReportPerformanceManagementTop200FormattingVO constructCannedReportVO(ReportPerformanceManagementTop200FormattingSelectionForm  reportPerformanceManagementTop200FormattingSelectionForm ) throws SMORAException
	{
		if (reportPerformanceManagementTop200FormattingVO == null)
		{
			reportPerformanceManagementTop200FormattingVO = new ReportPerformanceManagementTop200FormattingVO();
		}	

		reportPerformanceManagementTop200FormattingVO.setAccountFormat(reportPerformanceManagementTop200FormattingSelectionForm .getAccountFormat());
		//performancemanagementFormattingVO.setFormat(timeSeriesReportDetailFormattingSelectionForm.getFormat());
		//performancemanagementFormattingVO.setSortOptions(timeSeriesReportDetailFormattingSelectionForm.getSortOptions());
		//performancemanagementFormattingVO.setIncludeOnly(timeSeriesReportDetailFormattingSelectionForm.getIncludeOnly());
	//	if((timeSeriesReportDetailFormattingSelectionForm.getCumulativePercentage()!=null) &&!(timeSeriesReportDetailFormattingSelectionForm.getCumulativePercentage().equals("")))
		//performancemanagementFormattingVO.setCumulativePercentage(Integer.parseInt(timeSeriesReportDetailFormattingSelectionForm.getCumulativePercentage()));
	//	if((timeSeriesReportDetailFormattingSelectionForm.getTopNumberofRows()!=null) &&!(timeSeriesReportDetailFormattingSelectionForm.getTopNumberofRows().equals("")))
		//performancemanagementFormattingVO.setTopNumberOfRows(Integer.parseInt(timeSeriesReportDetailFormattingSelectionForm.getTopNumberofRows()));
		//performancemanagementFormattingVO.setGroupingLevel(timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel());
		return reportPerformanceManagementTop200FormattingVO;
	}
	
	/**
	 * This method returns fieldVOs List which should be populated in the Jasper report 
	 * @param metaDataList Meta List from T_Appl_Cn_Rpt_FldMeta_Data table
	 * @param tSTPFormattingVO Time Series Time period Comparison FormattingVO
	 * @param userId User Id
	 * @return Fields VO Array List
	 */
	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,ReportPerformanceManagementTop200VO reportPerformanceManagementTop200VO , String userId)
	{
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();

		if (metaDataList != null && metaDataList.size() > 0)
		{
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);

				int fieldId = tApplCnRptFldMetaData.getId().getFldId();
				FieldsVO fieldsVO = new FieldsVO();
				fieldsVO.setFldId(fieldId);
				fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());

				fieldsList.add(fieldsVO);
			}


		}
		return fieldsList;
	}





}

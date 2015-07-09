/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 */

package com.mckesson.smora.util;
/*
 * Author Manikandan.R
 * date: 2/2/07
 */

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.QuickItemPurchHistoryFormattingVO;
import com.mckesson.smora.dto.QuickItemPurchHistoryGroupVO;
import com.mckesson.smora.dto.ReportQuickItemPurchHistoryVO ;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.action.ReportQuickItemPurchHistoryAction;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.DescDollarFormattingSelectionForm;
import com.mckesson.smora.ui.form.DescendingDollarForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportQuickItemPurchHistoryForm;
import com.mckesson.smora.ui.form.ReportQuickItemPurchHistoryFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;


public class ConstructQuickItemBaseVO extends ConstructCannedReportBaseVO
{

	private static ReportQuickItemPurchHistoryVO  reportQuickItemPurchHistoryVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
	private static QuickItemPurchHistoryGroupVO quickItemPurchHistoryGroupVO = null;

	private  static QuickItemPurchHistoryFormattingVO quickItemPurchHistoryFormattingVO = null;

	private static CriteriaVO criteriaVO = null;

	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;
	private static Log log = LogFactory.getLog(ConstructQuickItemBaseVO.class);

	/**
	 * This method populates the report base VO.
	 *
	 * @param ReportQuickItemPurchHistoryForm
	 *           ReportQuickItemPurchHistoryForm
	 * @return the report base VO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public static  ReportBaseVO populateQuickItemBaseVO(ReportQuickItemPurchHistoryForm quickItemPurchHistoryForm) throws SMORAException
	{
		log.debug(" Start of  Method populateQuickItemBaseVO -Populate the ReportBaseVO");
		  
		  reportQuickItemPurchHistoryVO = new  ReportQuickItemPurchHistoryVO();
		  quickItemPurchHistoryGroupVO = new QuickItemPurchHistoryGroupVO();
		  criteriaVO = new CriteriaVO();
		  cannedReportCriteriaVO = new CannedReportCriteriaVO();
		String format = null;
		
		
		ReportQuickItemPurchHistoryFormattingSelectionForm formattingSelectionForm =quickItemPurchHistoryForm.getFormattingSelectionForm();
		format=formattingSelectionForm.getFormat();
		DateSelectionForm dateSelectionForm = quickItemPurchHistoryForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = quickItemPurchHistoryForm.getCustomerSelectionForm();
		ItemSelectionForm itemSelectionForm = quickItemPurchHistoryForm.getItemSelectionForm();
		

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();

		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
				
		FieldsListVO fieldsListVO = new FieldsListVO();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
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
		baseVO.setReportType("CANNED_REPORT");
		String dateVal=formattingSelectionForm.getDateValue();
		if(dateVal!=null && dateVal.length()>0 && !dateVal.toString().equalsIgnoreCase("null"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT);
		}
		else
		{
		if(format.equals("Invoice Level"))
		{
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY);
		}
		else if(format.equals("Summary Level Across Accounts"))
		{
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_ACROSS_ACCOUNT);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_ACROSS_ACCOUNT);
		}
		else if(format.equals("Summary Level by Account"))
		{
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT);
		}
		}
		
		
		
		baseVO.setHtml(formattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(formattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(formattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(formattingSelectionForm.isResultsDisplayCSV());

			
		
			
		quickItemPurchHistoryFormattingVO = constructCannedReportVO(formattingSelectionForm);
	    dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm,formattingSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
	    criteriaVO = constructCannedReportVO(customerSelectionForm);
		
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
		reportQuickItemPurchHistoryVO.setQuickItemPurchHistoryFormattingVO(quickItemPurchHistoryFormattingVO);
		quickItemPurchHistoryGroupVO.setReportQuickItemPurchHistoryVO(reportQuickItemPurchHistoryVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping 
			
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		
		cannedReportVO.setQuickItemPurchHistoryGroupVO(quickItemPurchHistoryGroupVO);
		cannedReportVO.setCannedReportTitle("26,30");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		log.debug("End of  Method populateQuickItemBaseVO -Populate the ReportBaseVO");
		return baseVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param ReportQuickItemPurchHistoryFormattingSelectionForm
	 *          
	 * @return quickItemPurchHistoryFormattingVO 
	 * @throws SMORAException
	 *             the SMORA exception
	 */


	private static QuickItemPurchHistoryFormattingVO  constructCannedReportVO(ReportQuickItemPurchHistoryFormattingSelectionForm formattingSelectionForm) throws SMORAException
	{
		log.debug(" Start of  Method constructCannedReportVO -Populate the quickItemPurchHistoryFormattingVO");

		quickItemPurchHistoryFormattingVO = new  QuickItemPurchHistoryFormattingVO();
		quickItemPurchHistoryFormattingVO.setFormat(formattingSelectionForm.getFormat());
		quickItemPurchHistoryFormattingVO.setDateValue(formattingSelectionForm.getDateValue());
		quickItemPurchHistoryFormattingVO.setUrl(formattingSelectionForm.getUrl());
		log.debug(" End of  Method constructCannedReportVO -Populate the formattingVO");
		return quickItemPurchHistoryFormattingVO ;
		
	}


	/**
	 * This method constructs the canned report VO.
	 *
	 * @param dateSelectionForm
	 *          
	 * @return dateSelectionAndComparisonVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */

	private static  DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm,ReportQuickItemPurchHistoryFormattingSelectionForm formattingSelectionForm)
	{
		log.debug(" Start of  Method constructCannedReportVO -Populate the DateSelectionAndComparisonVO");
       String format=formattingSelectionForm.getFormat();

		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();
		if(format.equals("Summary Level Across Accounts")||format.equals("Summary Level by Account") )
		{
		if (dateSelectionForm.getStartSelectedTime1() != null && dateSelectionForm.getEndSelectedTime1() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime1());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime1());
			timePeriodList.add(standardTimePeriodVO);
		}
		}
		else
		{
	 if(dateSelectionForm.getStartSelectedTime2() != null && dateSelectionForm.getEndSelectedTime2() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
			timePeriodList.add(standardTimePeriodVO);
		}
		}
		timePeriodListVO.setTimeperiodVOList(timePeriodList);

		
		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setIncludeCurrentMonth(dateSelectionForm.getIncludeCurrentMonth());

		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setLastXMonths(Integer.parseInt(dateSelectionForm.getLastXMonths()));

		dateSelectionAndComparisonVO.setSelectComparison(dateSelectionForm.getDateComparison());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);


		log.debug(" End  of  Method constructCannedReportVO -Populate the DateSelectionAndComparisonVO");
		return dateSelectionAndComparisonVO;
	}










}

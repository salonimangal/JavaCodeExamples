/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportPurchaseCostVarianceVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.action.UploadGroupsAction;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportPurchaseCostVarianceForm;
import com.mckesson.smora.ui.form.ReportPurchaseCostVarianceFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 *
 * @author Binu
 * @chanages 08/02/2007
 *
 */

public class ConstructPurchaseCostVarianceBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPurchaseCostVarianceBaseVO";
	
	private static ReportPurchaseCostVarianceVO reportPurchaseCostVarianceVO = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	private static CriteriaVO criteriaVO = null;
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(UploadGroupsAction.class);
	
	/**
	 * This method populates the Purcahse cost variance  base VO.
	 * @param reportPurchaseCostVarianceForm
	 * @return
	 * @throws SMORAException
	 */
	public static ReportBaseVO populatePurchaseCostVarianceBaseVO(ReportPurchaseCostVarianceForm reportPurchaseCostVarianceForm) throws SMORAException
	{
		final String METHOD_NAME = "populatePurchaseCostVarianceBaseVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		criteriaVO = new CriteriaVO();
		
		ReportPurchaseCostVarianceFormattingSelectionForm reportPurchaseCostVarianceFormattingSelectionForm = reportPurchaseCostVarianceForm.getReportPurchaseCostVarianceFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportPurchaseCostVarianceForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportPurchaseCostVarianceForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportPurchaseCostVarianceForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportPurchaseCostVarianceForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportPurchaseCostVarianceForm.getAdvancedFiltersForm();
		
		/*
		 * SO-0953 :  Code added to set the calendar selected dates 
		 */
		DateSelectionUtil dateSelUnit = new DateSelectionUtil();
		if(dateSelectionForm.getCustomStartDate96Mnth()!= null && dateSelectionForm.getCustomStartDate96Mnth().trim()!="")
		{
			String strCustomStartDate = dateSelUnit.convertCustomStartDate(dateSelectionForm.getCustomStartDate96Mnth().trim());
			if(strCustomStartDate!=null && strCustomStartDate.trim()!="")
				dateSelectionForm.setStartSelectedTime1(strCustomStartDate);
		}
			
		if(dateSelectionForm.getCustomEndDate96Mnth()!= null && dateSelectionForm.getCustomEndDate96Mnth().trim()!="")
		{
			String strCustomEndDate = dateSelUnit.convertCustomEndDate(dateSelectionForm.getCustomEndDate96Mnth().trim());
			if(strCustomEndDate!=null && strCustomEndDate.trim()!="")
				dateSelectionForm.setEndSelectedTime1(strCustomEndDate);
		}
		/*
		 * SO-0953 :  Code ends
		 */
		
		ArrayList<FirstGroupLevelVO> firstGroupLevelVOList = new ArrayList<FirstGroupLevelVO>();
		ArrayList<String> subTotalArray = new ArrayList<String>();
		ArrayList<String> fieldDescArray = new ArrayList<String>();
		
		CustomReportDB customReportDB = new CustomReportDB();
		FieldsListVO fieldsListVO = new FieldsListVO();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportPurchaseCostVarianceFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportPurchaseCostVarianceFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportPurchaseCostVarianceFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE);
		baseVO.setHtml(reportPurchaseCostVarianceFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportPurchaseCostVarianceFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportPurchaseCostVarianceFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportPurchaseCostVarianceFormattingSelectionForm.isResultsDisplayCSV());
		
		reportPurchaseCostVarianceVO = constructCannedReportVO(reportPurchaseCostVarianceFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		String format = reportPurchaseCostVarianceFormattingSelectionForm.getAccountFormat();
		
		
				
		if(format != null && !format.equals("Combine Account"))
		{
			subTotalArray.add("Total Dollars");
			subTotalArray.add("Monthly Avg");
			subTotalArray.add("Total $ Last Month");
			subTotalArray.add("Total Diff $");
			
			fieldDescArray.add("Total Dollars");
			fieldDescArray.add("Monthly Avg");
			fieldDescArray.add("Total $ Last Month");
			fieldDescArray.add("Total Diff $");
			
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account");
			firstGroupLevelVO.setFieldName("Acct #:");
			firstGroupLevelVO.setFieldWidth(52);
			firstGroupLevelVO.setSubTotal(subTotalArray);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Dept");
			secondGroupLevelVO.setFieldName("Dept");
			secondGroupLevelVO.setFieldWidth(52);
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);
		}
		else
		{
			subTotalArray.add("Total Dollars");
			subTotalArray.add("Monthly Avg");
			subTotalArray.add("Total $ Last Month");
			subTotalArray.add("Total Diff $");
			
			fieldDescArray.add("Total Dollars");
			fieldDescArray.add("Monthly Avg");
			fieldDescArray.add("Total $ Last Month");
			fieldDescArray.add("Total Diff $");
			
			ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
			thirdGroupLevelVO.setClassType("java.lang.String");
			thirdGroupLevelVO.setFieldDescription("McK Item #");
			thirdGroupLevelVO.setFieldName("McK Item #");
			thirdGroupLevelVO.setFieldWidth(52);
			thirdGroupLevelVO.setSubTotal(subTotalArray);
			thirdGroupLevelVO.setSubTotal(fieldDescArray);
			
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);
		}
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping
		
		cannedReportVO.setReportPurchaseCostVarianceVO(reportPurchaseCostVarianceVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportVO.setCannedReportTitle("38");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param reportPurchaseCostVarianceFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	/**
	 * @param reportPurchaseCostVarianceFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	private static ReportPurchaseCostVarianceVO constructCannedReportVO(ReportPurchaseCostVarianceFormattingSelectionForm reportPurchaseCostVarianceFormattingSelectionForm) throws SMORAException
	{
		final String METHOD_NAME = "constructCannedReportVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		if (reportPurchaseCostVarianceVO == null)
		{
			reportPurchaseCostVarianceVO = new ReportPurchaseCostVarianceVO();
		}
		
		reportPurchaseCostVarianceVO.setAccountFormat(reportPurchaseCostVarianceFormattingSelectionForm.getAccountFormat());
		reportPurchaseCostVarianceVO.setSortOptions(reportPurchaseCostVarianceFormattingSelectionForm.getSortOptions());
		
		return reportPurchaseCostVarianceVO;
	}
	
	
	/**
	 * This method constructs the custom report VO.
	 * @param dateSelectionForm the date selection form
	 * @return the cust report criteria VO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{
		final String METHOD_NAME = "constructCannedReportVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		if (reportPurchaseCostVarianceVO == null)
		{
			reportPurchaseCostVarianceVO = new ReportPurchaseCostVarianceVO();
		}
		
		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();
		
		if (dateSelectionForm.getStartSelectedTime1() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime1());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime1());
			timePeriodList.add(standardTimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime12() != null)
		{
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			comparePeriod1TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime12());
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime12());
			timePeriodList.add(comparePeriod1TimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime22() != null)
		{
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			comparePeriod2TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime22());
			comparePeriod2TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime22());
			timePeriodList.add(comparePeriod2TimePeriodVO);
		}
		
		timePeriodListVO.setTimeperiodVOList(timePeriodList);
		
		dateSelectionAndComparisonVO.setIncludeCurrentMonth(dateSelectionForm.getIncludeCurrentMonth());
		dateSelectionAndComparisonVO.setDisplayPurchasesByMonth(dateSelectionForm.getDisplayPurchasesByMonth());
		
		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		
		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setLastXMonths(Integer.parseInt(dateSelectionForm.getLastXMonths()));
		
		dateSelectionAndComparisonVO.setSelectComparison(dateSelectionForm.getDateComparison());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);
		
		return dateSelectionAndComparisonVO;
	}
	
	
}
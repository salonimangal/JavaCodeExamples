/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.mckesson.smora.dto.ReportPurchaseCostVarianceTherSumVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TSTPFormattingVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.action.UploadGroupsAction;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportPurchaseCostVarianceTherSumForm;
import com.mckesson.smora.ui.form.ReportPurchaseCostVarianceTherSumFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 *
 * @author Binu
 * @chanages 08/02/2007
 *
 */

public class ConstructPurchaseCostVarianceTheraBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPurchaseCostVarianceTheraBaseVO";
	
	private static ReportPurchaseCostVarianceTherSumVO reportPurchaseCostVarianceTherSumVO = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;	
	private static CriteriaVO criteriaVO = null;
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(UploadGroupsAction.class);
	
	/**
	 * This method populates the Purcahse cost variance thera base VO.
	 * @param reportPurchaseCostVarianceTherSumForm
	 * @return
	 * @throws SMORAException
	 */
	public static ReportBaseVO populatePurchaseCostVarianceTheraBaseVO(ReportPurchaseCostVarianceTherSumForm reportPurchaseCostVarianceTherSumForm) throws SMORAException
	{
		final String METHOD_NAME = "populatePurchaseCostVarianceTheraBaseVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		criteriaVO = new CriteriaVO();
		
		ReportPurchaseCostVarianceTherSumFormattingSelectionForm reportPurchaseCostVarianceTherSumFormattingSelectionForm = reportPurchaseCostVarianceTherSumForm.getReportPurchaseCostVarianceTherSumFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportPurchaseCostVarianceTherSumForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportPurchaseCostVarianceTherSumForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportPurchaseCostVarianceTherSumForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportPurchaseCostVarianceTherSumForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportPurchaseCostVarianceTherSumForm.getAdvancedFiltersForm();
		
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
		if(reportPurchaseCostVarianceTherSumFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportPurchaseCostVarianceTherSumFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportPurchaseCostVarianceTherSumFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE_THERA);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE_THERA);
		baseVO.setHtml(reportPurchaseCostVarianceTherSumFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportPurchaseCostVarianceTherSumFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportPurchaseCostVarianceTherSumFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportPurchaseCostVarianceTherSumFormattingSelectionForm.isResultsDisplayCSV());
		
		reportPurchaseCostVarianceTherSumVO = constructCannedReportVO(reportPurchaseCostVarianceTherSumFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		String format = reportPurchaseCostVarianceTherSumFormattingSelectionForm.getAccountFormat(); 
		
		
		if(!format.equals("Combine Account"))
		{
			subTotalArray.add("Total $");
			subTotalArray.add("Monthly Avg");
			subTotalArray.add("Total $ Last Month");
			subTotalArray.add("Total Diff $");
			
			fieldDescArray.add("Total $");
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
			subTotalArray.add("Total $");
			subTotalArray.add("Monthly Avg");
			subTotalArray.add("Total $ Last Month");
			subTotalArray.add("Total Diff $");
			
			fieldDescArray.add("Total $");
			fieldDescArray.add("Monthly Avg");
			fieldDescArray.add("Total $ Last Month");
			fieldDescArray.add("Total Diff $");
			
			ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
			thirdGroupLevelVO.setClassType("java.lang.String");
			thirdGroupLevelVO.setFieldDescription("Therapeutic Code");
			thirdGroupLevelVO.setFieldName("Therapeutic Code");
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
		
		cannedReportVO.setReportPurchaseCostVarianceTherSumVO(reportPurchaseCostVarianceTherSumVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);	
		cannedReportVO.setCannedReportTitle("39");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param reportPurchaseCostVarianceTherSumFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	/**
	 * @param reportPurchaseCostVarianceTherSumFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	private static ReportPurchaseCostVarianceTherSumVO constructCannedReportVO(ReportPurchaseCostVarianceTherSumFormattingSelectionForm reportPurchaseCostVarianceTherSumFormattingSelectionForm) throws SMORAException
	{
		final String METHOD_NAME = "constructCannedReportVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		if (reportPurchaseCostVarianceTherSumVO == null)
		{
			reportPurchaseCostVarianceTherSumVO = new ReportPurchaseCostVarianceTherSumVO();
		}
		
		reportPurchaseCostVarianceTherSumVO.setAccountFormat(reportPurchaseCostVarianceTherSumFormattingSelectionForm.getAccountFormat());
		reportPurchaseCostVarianceTherSumVO.setSortOptions(reportPurchaseCostVarianceTherSumFormattingSelectionForm.getSortOptions());
		
		return reportPurchaseCostVarianceTherSumVO;
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
		if (reportPurchaseCostVarianceTherSumVO == null)
		{
			reportPurchaseCostVarianceTherSumVO = new ReportPurchaseCostVarianceTherSumVO();
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
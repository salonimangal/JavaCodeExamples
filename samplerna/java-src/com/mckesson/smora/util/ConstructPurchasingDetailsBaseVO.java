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
import com.mckesson.smora.dto.ReportPurchasingDetailsVO;
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
import com.mckesson.smora.ui.form.ReportPurchaseCostVarianceForm;
import com.mckesson.smora.ui.form.ReportPurchaseCostVarianceFormattingSelectionForm;
import com.mckesson.smora.ui.form.ReportPurchasingDetailsForm;
import com.mckesson.smora.ui.form.ReportPurchasingDetailsFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 *
 * @author Supriya
 *  *
 */

public class ConstructPurchasingDetailsBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPurchasingDetailsBaseVO";
	
	private static ReportPurchasingDetailsVO  reportPurchasingDetailsVO  = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	private static CriteriaVO criteriaVO = null;
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(UploadGroupsAction.class);
	
	/**
	 * This method populates the Purcahse cost variance  base VO.
	 * @param reportPurchasingDetailsForm
	 * @return
	 * @throws SMORAException
	 */
	public static ReportBaseVO populatePurchasingDetailsBaseVO(ReportPurchasingDetailsForm reportPurchasingDetailsForm) throws SMORAException
	{
		final String METHOD_NAME = "populatePurchasingDetailsBaseVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		criteriaVO = new CriteriaVO();
		boolean SummaryByAccount=false;
		
		ReportPurchasingDetailsFormattingSelectionForm reportPurchasingDetailsFormattingSelectionForm = reportPurchasingDetailsForm.getReportPurchasingDetailsFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportPurchasingDetailsForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportPurchasingDetailsForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportPurchasingDetailsForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportPurchasingDetailsForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportPurchasingDetailsForm.getAdvancedFiltersForm();
		
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
		if(reportPurchasingDetailsFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportPurchasingDetailsFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		
		baseVO.setCustomHeading(reportPurchasingDetailsFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_PURCHASE_DETAILS);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_PURCHASE_DETAILS);
		baseVO.setHtml(reportPurchasingDetailsFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportPurchasingDetailsFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportPurchasingDetailsFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportPurchasingDetailsFormattingSelectionForm.isResultsDisplayCSV());
		
		/*
		 * QC-11225 :  Code added to set the calendar selected dates 
		 */
		DateSelectionUtil dateSelUnit = new DateSelectionUtil();
		if(itemSelectionForm.isNonMckItems() && dateSelectionForm.getCustomStartDate96Mnth()!= null && dateSelectionForm.getCustomStartDate96Mnth().trim()!="")//getItemDetailsVO().
		{
			String strCustomStartDate = dateSelUnit.convertCustomStartDate(dateSelectionForm.getCustomStartDate96Mnth().trim());
			if(strCustomStartDate!=null && strCustomStartDate.trim()!="")
				dateSelectionForm.setStartSelectedTime1(strCustomStartDate);
		}
			
		if(itemSelectionForm.isNonMckItems() && dateSelectionForm.getCustomEndDate96Mnth()!= null && dateSelectionForm.getCustomEndDate96Mnth().trim()!="")
		{
			String strCustomEndDate = dateSelUnit.convertCustomEndDate(dateSelectionForm.getCustomEndDate96Mnth().trim());
			if(strCustomEndDate!=null && strCustomEndDate.trim()!="")
				dateSelectionForm.setEndSelectedTime1(strCustomEndDate);
		}
		/*
		 * QC-11225 :  Code ends
		 */
		
		
		reportPurchasingDetailsVO = constructCannedReportVO(reportPurchasingDetailsFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
	
		SummaryByAccount=reportPurchasingDetailsFormattingSelectionForm.isSummaryByAccount();
		System.out.println("++++++SummaryByAccount+++++++"+SummaryByAccount);
		if(SummaryByAccount)
		{
		subTotalArray.add("Ordered");
		subTotalArray.add("Fill");
		subTotalArray.add("Return");
		subTotalArray.add("Net");
		subTotalArray.add("Ext.Price");
		
		fieldDescArray.add("Ordered");
		fieldDescArray.add("Fill");
		fieldDescArray.add("Return");
		fieldDescArray.add("Net");
		fieldDescArray.add("Ext.Price");
		
		
		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("CUST_ACCT_ID");
		firstGroupLevelVO.setFieldName("CUST_ACCT_ID");
		firstGroupLevelVO.setFieldWidth(52);
		firstGroupLevelVO.setSubTotal(subTotalArray);
		firstGroupLevelVO.setSubTotal(fieldDescArray);
		
		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		secondGroupLevelVO.setClassType("java.lang.String");
		secondGroupLevelVO.setFieldDescription("CUST_NAM");
		secondGroupLevelVO.setFieldName("CUST_NAM");
		secondGroupLevelVO.setFieldWidth(100);
		secondGroupLevelVO.setSubTotal(subTotalArray);
		secondGroupLevelVO.setSubTotal(fieldDescArray);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(null);
		
		}
		
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping
		cannedReportVO.setReportPurchasingDetailsVO(reportPurchasingDetailsVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportVO.setCannedReportTitle("51");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param reportPurchasingDetailsFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	/**
	 * @param reportPurchasingDetailsFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	private static ReportPurchasingDetailsVO constructCannedReportVO(ReportPurchasingDetailsFormattingSelectionForm reportPurchasingDetailsFormattingSelectionForm) throws SMORAException
	{
		final String METHOD_NAME = "constructCannedReportVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		if (reportPurchasingDetailsVO == null)
		{
			reportPurchasingDetailsVO = new ReportPurchasingDetailsVO();
		}
		
		reportPurchasingDetailsVO.setSortOptions(reportPurchasingDetailsFormattingSelectionForm.getSortOptions());
		reportPurchasingDetailsVO.setIncludePO(reportPurchasingDetailsFormattingSelectionForm.getIncludePO());
		reportPurchasingDetailsVO.setSummaryByAccount(reportPurchasingDetailsFormattingSelectionForm.isSummaryByAccount());
		reportPurchasingDetailsVO.setReportName(reportPurchasingDetailsFormattingSelectionForm.getReportName());
		return reportPurchasingDetailsVO;
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
		if (reportPurchasingDetailsVO == null)
		{
			reportPurchasingDetailsVO = new ReportPurchasingDetailsVO();
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
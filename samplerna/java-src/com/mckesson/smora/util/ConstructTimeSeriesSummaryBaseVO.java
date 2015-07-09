/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;

import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TSSFormattingVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.TimeSeriesGroupVO;
import com.mckesson.smora.dto.TimeSeriesSummaryVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.TimeSeriesSummaryForm;
import com.mckesson.smora.ui.form.TimeSeriesSummaryFormattingSelectionForm;

/**
 * @author mohana.ramachandran
 * @changes 23 November 2006 This Class is used to Construct the ReportBaseVO
 *          for TimeSeriesSummary ReportGroup
 */

/**
 * The CLASSNAME ConstructTimeSeriesSummaryBaseVO
 */

public class ConstructTimeSeriesSummaryBaseVO extends
		ConstructCannedReportBaseVO
{
	
	private final String CLASSNAME = "ConstructTimeSeriesSummaryBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static TimeSeriesSummaryVO timeSeriesSummaryVO = null;
	/**
	 * The tSS Formatting VO
	 */
	private static TSSFormattingVO tSSFormattingVO = null;
	/**
	 * The date Selection And Comparison VO
	 */
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	/**
	 * The criteria VO
	 */
	private static CriteriaVO criteriaVO = null;
	/**
	 * The canned Report Criteria VO
	 */
	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;

	/**
	 * This method populates the Time Series Summary Base VO
	 * 
	 * @param customReportingForm the custom reporting form
	 * @return the report base VO
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateTimeSeriesSummaryBaseVO(TimeSeriesSummaryForm timeSeriesSummaryForm) throws SMORAException
	{
		
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		
		timeSeriesSummaryVO = new TimeSeriesSummaryVO();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		TimeSeriesSummaryFormattingSelectionForm timeSeriesSummaryFormattingSelectionForm = timeSeriesSummaryForm.getTimeSeriesSummaryFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = timeSeriesSummaryForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = timeSeriesSummaryForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = timeSeriesSummaryForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = timeSeriesSummaryForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = timeSeriesSummaryForm.getAdvancedFiltersForm();
		
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

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		TimeSeriesGroupVO timeSeriesGroupVO = new TimeSeriesGroupVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(timeSeriesSummaryFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(timeSeriesSummaryFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(timeSeriesSummaryFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(Integer.parseInt(timeSeriesSummaryFormattingSelectionForm.getFormat()));
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_SUMMARY_GROUP);
		baseVO.setHtml(timeSeriesSummaryFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(timeSeriesSummaryFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(timeSeriesSummaryFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(timeSeriesSummaryFormattingSelectionForm.isResultsDisplayCSV());
		String format = timeSeriesSummaryFormattingSelectionForm.getAccountFormat(); 
		 
		if(!format.equals("Combine Account")&&(!(baseVO.getReportSubtype()==ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY)))
		{
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account #");
			firstGroupLevelVO.setFieldName("Account #");
			firstGroupLevelVO.setFieldWidth(100);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);
		}
		else
		{
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);
		}
		
		tSSFormattingVO = constructCannedReportVO(timeSeriesSummaryFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
		timeSeriesSummaryVO.setTSSFormattingVO(tSSFormattingVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);

		ArrayList <String>sequenceFields = new ArrayList();
		sequenceFields.add("CUST_ACCT_ID");
		sequenceFields.add("CUST_ACCT_NAM");
		sequenceFields.add("TOT_AMT");
		sequenceFields.add("TOT_AMT_FOR_ACCT");
		sequenceFields.add("TOT_AMT_PERCENT");
 
		ArrayList<FieldsVO> fieldsVOList = new ArrayList();
		FieldsListVO fieldsListVO = new FieldsListVO();
		CustomReportDB customReportDB  = new CustomReportDB();
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();
 		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		timeSeriesGroupVO.setTimeSeriesSummaryVO(timeSeriesSummaryVO);
		cannedReportVO.setTimeSeriesGroupVO(timeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("43,45");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
	
		return baseVO;
	}

	/**
	 * This method constructs the canned report VO.
	 * 
	 * @param timeSeriesSummaryFormattingSelectionForm
	 *            the timeSeriesSummaryFormatting selection form
	 * @return timeSeriesSummaryVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	private static TSSFormattingVO constructCannedReportVO(TimeSeriesSummaryFormattingSelectionForm timeSeriesSummaryFormattingSelectionForm) throws SMORAException
	{
		tSSFormattingVO = new TSSFormattingVO();
		int format = Integer.parseInt(timeSeriesSummaryFormattingSelectionForm.getFormat());
		if(format==ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY)
		{
			tSSFormattingVO.setAccountFormat("Break By Account");
		}
		else
		{
			tSSFormattingVO.setAccountFormat(timeSeriesSummaryFormattingSelectionForm.getAccountFormat());
		}
		tSSFormattingVO.setFormat(format);
		tSSFormattingVO.setSortOptions(timeSeriesSummaryFormattingSelectionForm.getSortOptions());
		tSSFormattingVO.setIncludeOnly(timeSeriesSummaryFormattingSelectionForm.getIncludeOnly());
		
		if((timeSeriesSummaryFormattingSelectionForm.getCumulativePercentage()!=null) &&!(timeSeriesSummaryFormattingSelectionForm.getCumulativePercentage().equals("")))
		tSSFormattingVO.setCumulativePercentage(timeSeriesSummaryFormattingSelectionForm.getCumulativePercentage());
		if((timeSeriesSummaryFormattingSelectionForm.getTopNumberofRows()!=null) &&!(timeSeriesSummaryFormattingSelectionForm.getTopNumberofRows().equals("")))
		tSSFormattingVO.setTopNumberOfRows(timeSeriesSummaryFormattingSelectionForm.getTopNumberofRows());
		return tSSFormattingVO;
	}
	
	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{
		if (timeSeriesSummaryVO == null)
		{
			timeSeriesSummaryVO = new TimeSeriesSummaryVO();
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
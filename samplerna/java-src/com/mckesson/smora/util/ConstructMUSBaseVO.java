/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;

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
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.MUSForm;
import com.mckesson.smora.ui.form.MUSFormattingSelectionForm;

/**
 * @author mohana.ramachandran
 * @changes 23 November 2006 This Class is used to Construct the ReportBaseVO
 *          for  ReportGroup
 */
public class ConstructMUSBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "MUSBaseVO";

	/**
	 * The date Selection And Comparison VO
	 */
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	/**
	 * The criteria VO
	 */
	private static CriteriaVO criteriaVO = null;
	/**
	 * The mus Form
	 */
	private static MUSForm musForm = null;
	/**
	 * The canned Report Criteria VO
	 */
	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;

	/**
	 * This method populates the report base VO.
	 * 
	 * @param customReportingForm
	 *            the custom reporting form
	 * @return the report base VO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public static ReportBaseVO populateMUSBaseVO(MUSForm musForm) throws SMORAException
	{
		
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();         
		MUSFormattingSelectionForm musFormattingSelectionForm = musForm.getMusFormattingSelectionForm();
         
		DateSelectionForm dateSelectionForm = musForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = musForm.getCustomerSelectionForm();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		SupplierSelectionForm supplierSelectionForm = musForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = musForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = musForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(musFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(musFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(musFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_MUS_CONTRACT_OMIT);
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_MUS_CONTRACT_OMIT);
		baseVO.setHtml(musFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(musFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(musFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(musFormattingSelectionForm.isResultsDisplayCSV());
		baseVO.setCompareSamePackSize(musFormattingSelectionForm.isCompareSamePackSize()); // req 35

		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		
		//Modified by Infosys for Issue 415
		//firstGroupLevelVO.setFieldDescription("$F{CUST_ACCT_ID}");
		//firstGroupLevelVO.setFieldName("Acct #");
		firstGroupLevelVO.setFieldDescription("$F{Suppl_Nam}");
		firstGroupLevelVO.setFieldName("Supplier Name");
		
		firstGroupLevelVO.setFieldWidth(100);
		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		secondGroupLevelVO.setClassType("java.lang.String");
		secondGroupLevelVO.setFieldDescription("$F{CUST_ACCT_ID} + $F{CUST_ACCT_NAM}");
		secondGroupLevelVO.setFieldName("Supplier Number");
		secondGroupLevelVO.setFieldWidth(100);
		
		ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
		thirdGroupLevelVO.setClassType("java.lang.String");
		thirdGroupLevelVO.setFieldDescription("$F{Suppl_Nam}");
		thirdGroupLevelVO.setFieldName("Supplier Name");
		thirdGroupLevelVO.setFieldWidth(100);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);
		
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
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
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("32");
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
	 
	/**private static MUSFormattingVO constructCannedReportVO(MUSFormattingSelectionForm musFormattingSelectionForm) throws SMORAException
	{
		if (musFormattingVO == null)
		{
			musFormattingVO = new MUSFormattingVO();
		}

		int format = Integer.parseInt(musFormattingSelectionForm.getFormat());
		if(format==ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY)
		{
			musFormattingVO.setAccountFormat("Break");
		}
		else
		{
			musFormattingVO.setAccountFormat(musFormattingSelectionForm.getAccountFormat());
		}
		musFormattingVO.setFormat(format);
		musFormattingVO.setSortOptions(musFormattingSelectionForm.getSortOptions());
		musFormattingVO.setIncludeOnly(musFormattingSelectionForm.getIncludeOnly());
		if((musFormattingSelectionForm.getCumulativePercentage()!=null) &&!(musFormattingSelectionForm.getCumulativePercentage().equals("")))
		musFormattingVO.setCumulativePercentage(Integer.parseInt(musFormattingSelectionForm.getCumulativePercentage()));
		if((musFormattingSelectionForm.getTopNumberofRows()!=null) &&!(musFormattingSelectionForm.getTopNumberofRows().equals("")))
		musFormattingVO.setTopNumberOfRows(Integer.parseInt(musFormattingSelectionForm.getTopNumberofRows()));
		return musFormattingVO;
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

		
		/**
		 * ************************************** DateSelection completed
		 * *********************
		 */
		// DateSelectionForm dateSelectionForm =
		// customReportingForm.getDateSelectionForm();
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
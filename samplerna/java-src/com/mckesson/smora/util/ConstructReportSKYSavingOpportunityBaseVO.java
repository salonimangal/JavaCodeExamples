/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.database.model.TApplCnRptFldMetaDataId;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustReportCriteriaVO;
import com.mckesson.smora.dto.CustomReportVO;
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
import com.mckesson.smora.ui.form.ReportSKYSavingOpportunityForm;
import com.mckesson.smora.ui.form.ReportSKYSavingOpportunityFormattingSelectionForm;

/**
 * @author mohana.ramachandran
 * @changes 23 November 2006 This Class is used to Construct the ReportBaseVO
 *          for  ReportGroup
 */
public class ConstructReportSKYSavingOpportunityBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportSKYSavingOpportunityBaseVO";

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
	private static ReportSKYSavingOpportunityForm reportSKYSavingOpportunityForm = null;
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
	public static ReportBaseVO populateReportSKYSavingOpportunityBaseVO(ReportSKYSavingOpportunityForm reportSKYSavingOpportunityForm) throws SMORAException
	{
		
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();         
		ReportSKYSavingOpportunityFormattingSelectionForm reportSKYSavingOpportunityFormattingSelectionForm = reportSKYSavingOpportunityForm.getReportSKYSavingOpportunityFormattingSelectionForm();
         
		DateSelectionForm dateSelectionForm = reportSKYSavingOpportunityForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportSKYSavingOpportunityForm.getCustomerSelectionForm();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		SupplierSelectionForm supplierSelectionForm = reportSKYSavingOpportunityForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportSKYSavingOpportunityForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportSKYSavingOpportunityForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportSKYSavingOpportunityFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportSKYSavingOpportunityFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportSKYSavingOpportunityFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
	//	baseVO.setReportGroupID(ReportManagerConstants.REPORT_SKY_SAVINGS);
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_SKY_SAVINGS);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_SKY_SAVINGS);
		baseVO.setHtml(reportSKYSavingOpportunityFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportSKYSavingOpportunityFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportSKYSavingOpportunityFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportSKYSavingOpportunityFormattingSelectionForm.isResultsDisplayCSV());

		
		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("Account # ");
		firstGroupLevelVO.setFieldName("Account # ");
		firstGroupLevelVO.setFieldWidth(100);
	//	firstGroupLevelVO.setSubTotal(fieldDescArray);
		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		secondGroupLevelVO.setClassType("java.lang.String");
		secondGroupLevelVO.setFieldDescription("$F{GEN_DESC}");
		secondGroupLevelVO.setFieldName("Generic Description");
		secondGroupLevelVO.setFieldWidth(100);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(null);
		
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
	//	cannedReportVO.setCannedReportTitle("51");
		String title= String.valueOf(ReportManagerConstants.REPORT_SKY_SAVINGS);
		cannedReportVO.setCannedReportTitle(title);
		//baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setRowLimits("1000");
		baseVO.setCannedReportVO(cannedReportVO);	

		return baseVO;
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

		if (dateSelectionForm.getStartSelectedTime2() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
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
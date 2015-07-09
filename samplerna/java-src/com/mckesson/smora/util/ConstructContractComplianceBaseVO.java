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
import com.mckesson.smora.dto.ReportAccComparisonISFormattingVO;
import com.mckesson.smora.dto.ReportAccComparisonISVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AccComparisonISFormattingSelectionForm;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.ContractComplianceForm;
import com.mckesson.smora.ui.form.ContractComplianceFormattingSelectionForm;
import com.mckesson.smora.dto.ReportContractComplianceVO;
import com.mckesson.smora.dto.ReportContractComplianceFormattingVO;
import com.mckesson.smora.util.ConstructCannedReportBaseVO;

/**
 * @author mohana.ramachandran
 * @changes 23 November 2006 This Class is used to Construct the ReportBaseVO
 *          for  ReportGroup
 */
public class ConstructContractComplianceBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructContractComplianceBaseVO";

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
	private static ContractComplianceForm contractComplianceForm = null;
	/**
	 * The canned Report Criteria VO
	 */
	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;
	private static ReportContractComplianceVO reportContractComplianceVO = null;
	private static ReportContractComplianceFormattingVO reportContractComplianceFormattingVO = null;
	
	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm
	 *            the custom reporting form
	 * @return the report base VO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public static ReportBaseVO populateContractComplianceBaseVO(ContractComplianceForm contractComplianceForm) throws SMORAException
	{

		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		ContractComplianceFormattingSelectionForm contractComplianceFormattingSelectionForm = contractComplianceForm.getContractComplianceFormattingSelectionForm();
		reportContractComplianceVO = new ReportContractComplianceVO();
		
		DateSelectionForm dateSelectionForm = contractComplianceForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = contractComplianceForm.getCustomerSelectionForm();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		SupplierSelectionForm supplierSelectionForm = contractComplianceForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = contractComplianceForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = contractComplianceForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(contractComplianceFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(contractComplianceFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(contractComplianceFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_CONTRACT_COMPLIANCE);
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_CONTRACT_COMPLIANCE);
		baseVO.setHtml(contractComplianceFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(contractComplianceFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(contractComplianceFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(contractComplianceFormattingSelectionForm.isResultsDisplayCSV());

		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("$F{ACCT_ID}");
		firstGroupLevelVO.setFieldName("Account #");
		firstGroupLevelVO.setFieldWidth(100);
		
		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		secondGroupLevelVO.setClassType("java.lang.String");
		secondGroupLevelVO.setFieldDescription("$F{GEN_DESC}");
		secondGroupLevelVO.setFieldName("Generic Description");
		secondGroupLevelVO.setFieldWidth(100);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(null);
		
		reportContractComplianceFormattingVO = constructCannedReportVO(contractComplianceFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		reportContractComplianceVO.setReportContractComplianceFormattingVO(reportContractComplianceFormattingVO);
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
		cannedReportVO.setReportContractComplianceVO(reportContractComplianceVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		//cannedReportVO.setCannedReportTitle("43,45");
		String title= String.valueOf(ReportManagerConstants.REPORT_CONTRACT_COMPLIANCE);
		cannedReportVO.setCannedReportTitle(title);
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
	private static ReportContractComplianceFormattingVO constructCannedReportVO(ContractComplianceFormattingSelectionForm contractComplianceFormattingSelectionForm) throws SMORAException
	{
		ArrayList<String> includeFieldsList = new ArrayList<String>();
		String[] includeFields = contractComplianceFormattingSelectionForm.getIncludeFields();
		reportContractComplianceFormattingVO = new ReportContractComplianceFormattingVO();
		System.out.println("reportContractComplianceFormattingVO in side construct base VO before setting contract compliance" +reportContractComplianceFormattingVO);
		
		//reportContractComplianceFormattingVO.setAccountFormat(contractComplianceFormattingSelectionForm.getAccountFormat());
		reportContractComplianceFormattingVO.setContractCompliance(contractComplianceFormattingSelectionForm.getContractCompliance());
		// QC-8421
		reportContractComplianceFormattingVO.setReportType((contractComplianceFormattingSelectionForm.getReportType() == null) ? "Detail" : 
			contractComplianceFormattingSelectionForm.getReportType());
		if (includeFields != null)
		{	
			for (int i = 0; i < includeFields.length; i++)
			{
				includeFieldsList.add(includeFields[i]);
			}
		reportContractComplianceFormattingVO.setIncludeFields(includeFieldsList);
		}
		System.out.println("reportContractComplianceFormattingVO in side construct base VO" +reportContractComplianceFormattingVO);
		//QC-9981 - set minSaving and sortOption of FormattingVO with Form Value
		reportContractComplianceFormattingVO.setMinSaving(contractComplianceFormattingSelectionForm.getMinSaving());
		reportContractComplianceFormattingVO.setSortOptions(contractComplianceFormattingSelectionForm.getSortOptions());
		return reportContractComplianceFormattingVO;
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
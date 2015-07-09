/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.ReportAccComparisonGGFormattingVO;
import com.mckesson.smora.dto.ReportAccComparisonGGVO;
import com.mckesson.smora.dto.ReportAccComparisonGroupVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.ReportAccComparisonISFormattingVO;
import com.mckesson.smora.dto.ReportAccComparisonISVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AccComparisonGGForm;
import com.mckesson.smora.ui.form.AccComparisonGGFormattingSelectionForm;
import com.mckesson.smora.ui.form.AccComparisonISForm;
import com.mckesson.smora.ui.form.AccComparisonISFormattingSelectionForm;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 * @author kumargaurav.sahoo
 * @created 30/01/2007
 */

public class ConstructAccComparisonGroupBaseVO extends ConstructCannedReportBaseVO 
{
	private final String CLASSNAME = "ConstructAccComparisonGroupBaseVO";
	private static ReportAccComparisonGroupVO reportAccComparisonGroupVO = null;
	private static ReportAccComparisonISVO reportAccComparisonISVO = null;
	private static ReportAccComparisonISFormattingVO reportAccComparisonISFormattingVO = null;
	private static ReportAccComparisonGGVO reportAccComparisonGGVO = null;
	private static ReportAccComparisonGGFormattingVO reportAccComparisonGGFormattingVO = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	private static CriteriaVO criteriaVO = null;
	
	/**
	 * This method populates the Report AccComparisonIS BaseVO
	 *
	 * @param accComparisonISForm the acc ComparisonIS Form
	 * 
	 * @return baseVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReportAccComparisonISBaseVO(AccComparisonISForm accComparisonISForm) throws SMORAException
	{
		criteriaVO = new CriteriaVO();
		reportAccComparisonGroupVO = new ReportAccComparisonGroupVO();
		reportAccComparisonISVO = new ReportAccComparisonISVO();
		
		AccComparisonISFormattingSelectionForm accComparisonISFormattingSelectionForm = accComparisonISForm.getAccComparisonISFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = accComparisonISForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = accComparisonISForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = accComparisonISForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = accComparisonISForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = accComparisonISForm.getAdvancedFiltersForm();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(accComparisonISFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(accComparisonISFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(accComparisonISFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM);
		baseVO.setHtml(accComparisonISFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(accComparisonISFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(accComparisonISFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(accComparisonISFormattingSelectionForm.isResultsDisplayCSV());

		reportAccComparisonISVO = constructCannedReportVO(accComparisonISFormattingSelectionForm);
		reportAccComparisonGroupVO.setReportAccComparisonISVO(reportAccComparisonISVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
		cannedReportVO.setReportAccComparisonGroupVO(reportAccComparisonGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		String title1= String.valueOf(ReportManagerConstants.REPORT_ACC_COMPARISON_GROUP);
		String title2= String.valueOf(ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM);
		cannedReportVO.setCannedReportTitle(title1+","+title2);
		
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);

		return baseVO;
	}
	
	/**
	 * This method populates the report AccComparisonGG BaseVO
	 *
	 * @param accComparisonGGForm the acc ComparisonGG Form
	 * 
	 * @return baseVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReportAccComparisonGGBaseVO(AccComparisonGGForm accComparisonGGForm) throws SMORAException
	{
		criteriaVO = new CriteriaVO();
		reportAccComparisonGroupVO = new ReportAccComparisonGroupVO();
		reportAccComparisonGGVO = new ReportAccComparisonGGVO();
		
		AccComparisonGGFormattingSelectionForm accComparisonGGFormattingSelectionForm = accComparisonGGForm.getAccComparisonGGFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = accComparisonGGForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = accComparisonGGForm.getCustomerSelectionForm();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		SupplierSelectionForm supplierSelectionForm = accComparisonGGForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = accComparisonGGForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = accComparisonGGForm.getAdvancedFiltersForm();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(accComparisonGGFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(accComparisonGGFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(accComparisonGGFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_ACC_COMPARISON_GENERIC);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM);
		baseVO.setHtml(accComparisonGGFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(accComparisonGGFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(accComparisonGGFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(accComparisonGGFormattingSelectionForm.isResultsDisplayCSV());

		reportAccComparisonGGVO = constructCannedReportVO(accComparisonGGFormattingSelectionForm);
		reportAccComparisonGroupVO.setReportAccComparisonGGVO(reportAccComparisonGGVO);
		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("$F{Gnrc_Dscr}");
		firstGroupLevelVO.setFieldName("Generic Description");
		firstGroupLevelVO.setFieldWidth(100);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
		cannedReportVO.setReportAccComparisonGroupVO(reportAccComparisonGroupVO);
	//	cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		//cannedReportVO.setCannedReportTitle("13,14");
		String title1= String.valueOf(ReportManagerConstants.REPORT_ACC_COMPARISON_GROUP);
		String title2= String.valueOf(ReportManagerConstants.REPORT_ACC_COMPARISON_GENERIC);
		cannedReportVO.setCannedReportTitle(title1+","+title2);
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);

		return baseVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param accComparisonISFormattingSelectionForm the acc ComparisonIS Formatting Selection Form
	 * 
	 * @return reportAccComparisonISVO
	 */
	private static ReportAccComparisonISVO constructCannedReportVO(AccComparisonISFormattingSelectionForm accComparisonISFormattingSelectionForm) throws SMORAException
	{
		if (reportAccComparisonISVO == null)
		{
			reportAccComparisonISVO = new ReportAccComparisonISVO();
		}
		ArrayList includeFieldsList = new ArrayList();
		String[] includeFields = accComparisonISFormattingSelectionForm.getIncludeFields();
		reportAccComparisonISFormattingVO = new ReportAccComparisonISFormattingVO();
		reportAccComparisonISFormattingVO.setFormat(accComparisonISFormattingSelectionForm.getFormat());
		reportAccComparisonISFormattingVO.setSortOptions(accComparisonISFormattingSelectionForm.getSortOptions());
		for (int i = 0; i < includeFields.length; i++)
		{
			includeFieldsList.add(includeFields[i]);
		}
		reportAccComparisonISFormattingVO.setIncludeFields(includeFieldsList);
		reportAccComparisonISVO.setReportAccComparisonISFormattingVO(reportAccComparisonISFormattingVO);
		
		return reportAccComparisonISVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param accComparisonGGFormattingSelectionForm the acc ComparisonGG Formatting Selection Form
	 * 
	 * @return reportAccComparisonGGVO
	 */
	private static ReportAccComparisonGGVO constructCannedReportVO(AccComparisonGGFormattingSelectionForm accComparisonGGFormattingSelectionForm) throws SMORAException
	{
		if (reportAccComparisonGGVO == null)
		{
			reportAccComparisonGGVO = new ReportAccComparisonGGVO();
		}
		ArrayList includeFieldsList = new ArrayList();
		String[] includeFields = accComparisonGGFormattingSelectionForm.getIncludeFields();

		reportAccComparisonGGFormattingVO = new ReportAccComparisonGGFormattingVO();
		reportAccComparisonGGFormattingVO.setSortGroupBy(accComparisonGGFormattingSelectionForm.getSortGroupBy());
		reportAccComparisonGGFormattingVO.setFormat(accComparisonGGFormattingSelectionForm.getFormat());
		reportAccComparisonGGFormattingVO.setSortOptions(accComparisonGGFormattingSelectionForm.getSortOptions());
		for (int i = 0; i < includeFields.length; i++)
		{
			includeFieldsList.add(includeFields[i]);
		}
		reportAccComparisonGGFormattingVO.setIncludeFields(includeFieldsList);
		reportAccComparisonGGVO.setReportAccComparisonGGFormattingVO(reportAccComparisonGGFormattingVO);
		
		return reportAccComparisonGGVO;
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
		if (reportAccComparisonGroupVO == null)
		{
			reportAccComparisonGroupVO = new ReportAccComparisonGroupVO();
		}

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

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
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.UsageDetailGenericFormattingVO;
import com.mckesson.smora.dto.UsageDetailGenericGroupVO;
import com.mckesson.smora.dto.UsageDetailGenericVO;
import com.mckesson.smora.dto.UsageDetailTheraTimeSeriesFormattingVO;
import com.mckesson.smora.dto.UsageDetailTheraTimeSeriesGroupVO;
import com.mckesson.smora.dto.UsageDetailTheraTimeSeriesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.action.UploadGroupsAction;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.UsageDetailGenericFormattingSelectionForm;
import com.mckesson.smora.ui.form.UsageDetailTheraTimeSeriesForm;
import com.mckesson.smora.ui.form.UsageDetailTheraTimeSeriesFormattingSelectionForm;

/**
 *
 * @author Binu
 * @chanages 12/01/2007
 *
 */

public class ConstructUsageDetailTimeSeriesBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructUsageDetailTimeSeriesBaseVO";
	
	private static UsageDetailTheraTimeSeriesGroupVO usageDetailTheraTimeSeriesGroupVO = null;
	private static UsageDetailGenericGroupVO usageDetailGenericGroupVO = null;
	private static UsageDetailTheraTimeSeriesVO usageDetailTheraTimeSeriesVO = null;
	private static UsageDetailGenericVO usageDetailGenericVO =null;
	private static UsageDetailTheraTimeSeriesFormattingVO usageDetailTheraTimeSeriesFormattingVO = null;
	private static UsageDetailGenericFormattingVO usageDetailGenericFormattingVO = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;	
	private static CriteriaVO criteriaVO = null;
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(UploadGroupsAction.class);
	
	/**
	 * This method populates the Usage detail time series generic category base VO.
	 *
	 * @param report8020DescendingWithUnitsForm the report8020 descending with units form
	 * @return baseVO
	 */
	public static ReportBaseVO populateUsageDetailTheraTimeSeriesBaseVO(UsageDetailTheraTimeSeriesForm usageDetailTheraTimeSeriesForm) throws SMORAException
	{
		final String METHOD_NAME = "populateUsageDetailTheraTimeSeriesBaseVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		criteriaVO = new CriteriaVO();
		usageDetailTheraTimeSeriesGroupVO = new UsageDetailTheraTimeSeriesGroupVO();
		usageDetailTheraTimeSeriesVO = new UsageDetailTheraTimeSeriesVO();
		
		UsageDetailTheraTimeSeriesFormattingSelectionForm usageDetailTheraTimeSeriesFormattingSelectionForm = usageDetailTheraTimeSeriesForm.getUsageDetailTheraTimeSeriesFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = usageDetailTheraTimeSeriesForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = usageDetailTheraTimeSeriesForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = usageDetailTheraTimeSeriesForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = usageDetailTheraTimeSeriesForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = usageDetailTheraTimeSeriesForm.getAdvancedFiltersForm();
		
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
		if(usageDetailTheraTimeSeriesFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(usageDetailTheraTimeSeriesFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(usageDetailTheraTimeSeriesFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES);
		//baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES);
		String flavourName =usageDetailTheraTimeSeriesFormattingSelectionForm.getFormat();
		if(flavourName !=null && flavourName.equalsIgnoreCase("Descending Dollar by Category") )
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES_DESC_BY_CATEGORY);
		}else if (flavourName !=null && flavourName.equalsIgnoreCase("Therapeutic Code"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES_CODE);
		}
		else if (flavourName !=null && flavourName.equalsIgnoreCase("Therapeutic Description"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES_DESC);
		}
		
		
		baseVO.setHtml(usageDetailTheraTimeSeriesFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(usageDetailTheraTimeSeriesFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(usageDetailTheraTimeSeriesFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(usageDetailTheraTimeSeriesFormattingSelectionForm.isResultsDisplayCSV());
		
		usageDetailTheraTimeSeriesVO = constructCannedReportVO(usageDetailTheraTimeSeriesFormattingSelectionForm);
		usageDetailTheraTimeSeriesGroupVO.setUsageDetailTheraTimeSeriesVO(usageDetailTheraTimeSeriesVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		String format = usageDetailTheraTimeSeriesFormattingSelectionForm.getAccountFormat(); 
		
		
		if(!format.equals("Combine Account"))
		{
			subTotalArray.add("Total$");
			subTotalArray.add("Prct_Tot");
			
			fieldDescArray.add("Total$");
			fieldDescArray.add("Prct_Tot");
			
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account");
			firstGroupLevelVO.setFieldName("Acct Name");
			firstGroupLevelVO.setFieldWidth(52);
			firstGroupLevelVO.setSubTotal(subTotalArray);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Category");
			secondGroupLevelVO.setFieldName("Therapeutic Category");
			secondGroupLevelVO.setFieldWidth(52);
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			
			
			ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
			thirdGroupLevelVO.setClassType("java.lang.String");
			thirdGroupLevelVO.setFieldDescription("Class");
			thirdGroupLevelVO.setFieldName("Therapeutic Class");
			thirdGroupLevelVO.setFieldWidth(52);
			thirdGroupLevelVO.setSubTotal(subTotalArray);
			thirdGroupLevelVO.setSubTotal(fieldDescArray);
			
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);
		}
		else
		{
			subTotalArray.add("Total$");
			subTotalArray.add("Prct_Tot");
			
			fieldDescArray.add("Total$");
			fieldDescArray.add("Prct_Tot");
			
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account");
			firstGroupLevelVO.setFieldName("Acct Name");
			firstGroupLevelVO.setFieldWidth(52);
			firstGroupLevelVO.setSubTotal(subTotalArray);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Category");
			secondGroupLevelVO.setFieldName("Therapeutic Category");
			secondGroupLevelVO.setFieldWidth(52);
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			
			
			ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
			thirdGroupLevelVO.setClassType("java.lang.String");
			thirdGroupLevelVO.setFieldDescription("Class");
			thirdGroupLevelVO.setFieldName("Therapeutic Class");
			thirdGroupLevelVO.setFieldWidth(52);
			thirdGroupLevelVO.setSubTotal(subTotalArray);
			thirdGroupLevelVO.setSubTotal(fieldDescArray);
			
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);
		}
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping 
		
		cannedReportVO.setUsageDetailTheraTimeSeriesGroupVO(usageDetailTheraTimeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);	
		cannedReportVO.setCannedReportTitle("50");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param usageDetailTheraTimeSeriesFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	private static UsageDetailTheraTimeSeriesVO constructCannedReportVO(UsageDetailTheraTimeSeriesFormattingSelectionForm usageDetailTheraTimeSeriesFormattingSelectionForm) throws SMORAException
	{
		final String METHOD_NAME = "constructCannedReportVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		if (usageDetailTheraTimeSeriesVO == null)
		{
			usageDetailTheraTimeSeriesVO = new UsageDetailTheraTimeSeriesVO();
		}
		
		usageDetailTheraTimeSeriesFormattingVO = new UsageDetailTheraTimeSeriesFormattingVO();
		usageDetailTheraTimeSeriesFormattingVO.setAccountFormat(usageDetailTheraTimeSeriesFormattingSelectionForm.getAccountFormat());
		usageDetailTheraTimeSeriesFormattingVO.setFormat(usageDetailTheraTimeSeriesFormattingSelectionForm.getFormat());
		usageDetailTheraTimeSeriesFormattingVO.setSortOptions(usageDetailTheraTimeSeriesFormattingSelectionForm.getSortOptions());
		usageDetailTheraTimeSeriesVO.setUsageDetailTheraTimeSeriesFormattingVO(usageDetailTheraTimeSeriesFormattingVO);
		
		return usageDetailTheraTimeSeriesVO;
	}
	
	/**
	 * Constructs canned report vo out of format slection form tab..
	 * @param usageDetailGenericFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	private static UsageDetailGenericVO constructCannedReportVO(UsageDetailGenericFormattingSelectionForm usageDetailGenericFormattingSelectionForm) throws SMORAException
	{
		final String METHOD_NAME = "constructCannedReportVO";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);		
		if (usageDetailGenericVO == null)
		{
			usageDetailGenericVO = new UsageDetailGenericVO();
		}
		
		usageDetailGenericFormattingVO = new UsageDetailGenericFormattingVO();
		usageDetailGenericFormattingVO.setAccountFormat(usageDetailGenericFormattingSelectionForm.getAccountFormat());
		usageDetailGenericFormattingVO.setFormat(usageDetailGenericFormattingSelectionForm.getFormat());
		usageDetailGenericFormattingVO.setSortOptions(usageDetailGenericFormattingSelectionForm.getSortOptions());
		usageDetailGenericVO.setUsageDetailGenericFormattingVO(usageDetailGenericFormattingVO);
		
		return usageDetailGenericVO;
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
		if (usageDetailTheraTimeSeriesGroupVO == null)
		{
			usageDetailTheraTimeSeriesGroupVO = new UsageDetailTheraTimeSeriesGroupVO();
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
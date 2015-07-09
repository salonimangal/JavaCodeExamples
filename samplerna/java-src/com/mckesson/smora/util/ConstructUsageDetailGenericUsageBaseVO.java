/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;

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
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.UsageDetailGenericForm;
import com.mckesson.smora.ui.form.UsageDetailGenericFormattingSelectionForm;


/**
 *
 * @author Shailendra
 * @chanages 07/02/2007
 *
 */

public class ConstructUsageDetailGenericUsageBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructUsageDetailGenericUsageBaseVO";

	
	private static UsageDetailGenericGroupVO usageDetailGenericGroupVO = null;
	private static UsageDetailGenericVO usageDetailGenericVO =null;
	private static UsageDetailGenericFormattingVO usageDetailGenericFormattingVO = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;	
	private static CriteriaVO criteriaVO = null;

	/**
	 * This method populates the Usage detail time series generic category base VO.
	 *
	 * @param report8020DescendingWithUnitsForm the report8020 descending with units form
	 * @return baseVO
	 *
	 * 
	 */
	
	public static ReportBaseVO populateUsageDetailGenericBaseVO(UsageDetailGenericForm usageDetailGenericForm) throws SMORAException
	{
		criteriaVO = new CriteriaVO();
		usageDetailGenericGroupVO = new UsageDetailGenericGroupVO();
		usageDetailGenericVO = new UsageDetailGenericVO();

		UsageDetailGenericFormattingSelectionForm usageDetailGenericFormattingSelectionForm = usageDetailGenericForm.getUsageDetailGenericFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = usageDetailGenericForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = usageDetailGenericForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = usageDetailGenericForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = usageDetailGenericForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = usageDetailGenericForm.getAdvancedFiltersForm();

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
		if(usageDetailGenericFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(usageDetailGenericFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(usageDetailGenericFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_GENERIC_USAGE);
		//baseVO.setReportGroupID(ReportManagerConstants.REPORT_GENERIC_USAGE);
		String flavourName =usageDetailGenericFormattingSelectionForm.getFormat();
		
		if(flavourName !=null && flavourName.equalsIgnoreCase("Descending Dollar by Category") )
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_GENERIC_USAGE_DESC_BY_CATEGORY);
		}else if (flavourName !=null && flavourName.equalsIgnoreCase("Generic Code"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_GENERIC_USAGE_GENERIC_CODE);
		}
		else if (flavourName !=null && flavourName.equalsIgnoreCase("Generic Description"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_GENERIC_USAGE_GENERIC_DESC);
		}
		
		baseVO.setHtml(usageDetailGenericFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(usageDetailGenericFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(usageDetailGenericFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(usageDetailGenericFormattingSelectionForm.isResultsDisplayCSV());

		usageDetailGenericVO = constructCannedReportVO(usageDetailGenericFormattingSelectionForm);
		usageDetailGenericGroupVO.setUsageDetailGenericVO(usageDetailGenericVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		String accFormat = usageDetailGenericFormattingSelectionForm.getAccountFormat(); 
		String format =  usageDetailGenericFormattingSelectionForm.getFormat();
		

		if(!accFormat.equals("Combine Account"))
		{

			subTotalArray.add("Total $");
			//subTotalArray.add("Total Qty");

			fieldDescArray.add("Total $");
			//fieldDescArray.add("Total Qty");


			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account:");
			firstGroupLevelVO.setFieldName("Acct Name");
			firstGroupLevelVO.setFieldWidth(52);
			firstGroupLevelVO.setSubTotal(subTotalArray);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			
			if(!format.equals("Generic Code"))
			{
			
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Generic Desc:");
			secondGroupLevelVO.setFieldName("Generic Description");
			secondGroupLevelVO.setFieldWidth(52);
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			}
			else
			{
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Generic Code:");
			secondGroupLevelVO.setFieldName("Generic Code");
			secondGroupLevelVO.setFieldWidth(52);
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			}
			
			/*ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
			thirdGroupLevelVO.setClassType("java.lang.String");
			thirdGroupLevelVO.setFieldDescription("Generic Code:");
			thirdGroupLevelVO.setFieldName("Generic Code");
			thirdGroupLevelVO.setFieldWidth(52);
			thirdGroupLevelVO.setSubTotal(subTotalArray);
			thirdGroupLevelVO.setSubTotal(fieldDescArray);*/


			
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			
		}
		else
		{
			subTotalArray.add("Total $");
			fieldDescArray.add("Total $");

			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("Generic Desc:");
			secondGroupLevelVO.setFieldName("Generic Description");
			secondGroupLevelVO.setFieldWidth(52);
			secondGroupLevelVO.setSubTotal(subTotalArray);
			secondGroupLevelVO.setSubTotal(fieldDescArray);

			ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
			thirdGroupLevelVO.setClassType("java.lang.String");
			thirdGroupLevelVO.setFieldDescription("Generic Code:");
			thirdGroupLevelVO.setFieldName("Generic Code");
			thirdGroupLevelVO.setFieldWidth(52);
			thirdGroupLevelVO.setSubTotal(subTotalArray);
			thirdGroupLevelVO.setSubTotal(fieldDescArray);


			if(!format.equals("Generic Code"))
			{
				groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			}
			else
			{	
				groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);
			}
		}
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping 

		cannedReportVO.setCannedReportTitle(String.valueOf(ReportManagerConstants.REPORT_GENERIC_USAGE));
		cannedReportVO.setUsageDetailGenericGroupVO(usageDetailGenericGroupVO);


		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);	
		cannedReportVO.setCannedReportTitle("48");

		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		return baseVO;
	}
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param usageDetailGenericFormattingSelectionForm the usageDetailGenericformatting selection form
	 * @return UsageDetailGenericVO
	 *
	 * 
	 */
	private static UsageDetailGenericVO constructCannedReportVO(UsageDetailGenericFormattingSelectionForm usageDetailGenericFormattingSelectionForm) throws SMORAException
	{
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
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{
		if (usageDetailGenericGroupVO == null)
		{
			usageDetailGenericGroupVO = new UsageDetailGenericGroupVO();
		}

		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();

		if (dateSelectionForm.getStartSelectedTime2() != null && dateSelectionForm.getEndSelectedTime2() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
			timePeriodList.add(standardTimePeriodVO);
		}
		timePeriodListVO.setTimeperiodVOList(timePeriodList);

		
		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setIncludeCurrentMonth(dateSelectionForm.getIncludeCurrentMonth());

		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setLastXMonths(Integer.parseInt(dateSelectionForm.getLastXMonths()));

		dateSelectionAndComparisonVO.setSelectComparison(dateSelectionForm.getDateComparison());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);

		return dateSelectionAndComparisonVO;
	}
}
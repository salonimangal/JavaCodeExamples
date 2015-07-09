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
import com.mckesson.smora.dto.UsageDetailTherapeuticFormattingVO;
import com.mckesson.smora.dto.UsageDetailTherapeuticGroupVO;
import com.mckesson.smora.dto.UsageDetailTherapeuticVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.UsageDetailTherapeuticForm;
import com.mckesson.smora.ui.form.UsageDetailTherapeuticFormattingSelectionForm;

/**
*
* @author siva
* @chanages 08/01/2007
*
*/

public class ConstructUsageDetailGroupBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructUsageDetailGroupBaseVO";

	private static UsageDetailTherapeuticGroupVO usageDetailTherapeuticGroupVO = null;
	private static UsageDetailTherapeuticVO usageDetailTherapeuticVO = null;
	private static UsageDetailTherapeuticFormattingVO usageDetailTherapeuticFormattingVO = null;
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;	
	private static CriteriaVO criteriaVO = null;
	
	/**
	 * This method populates the report8020 generic category base VO.
	 *
	 * @param report8020DescendingWithUnitsForm the report8020 descending with units form
	 * @return baseVO
	 *
	 * 
	 */
	public static ReportBaseVO populateUsageDetailTherapeuticBaseVO(UsageDetailTherapeuticForm usageDetailTherapeuticForm) throws SMORAException
	{
		criteriaVO = new CriteriaVO();
		usageDetailTherapeuticGroupVO = new UsageDetailTherapeuticGroupVO();
		usageDetailTherapeuticVO = new UsageDetailTherapeuticVO();
		
		UsageDetailTherapeuticFormattingSelectionForm usageDetailTherapeuticFormattingSelectionForm = usageDetailTherapeuticForm.getUsageDetailTherapeuticFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = usageDetailTherapeuticForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = usageDetailTherapeuticForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = usageDetailTherapeuticForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = usageDetailTherapeuticForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = usageDetailTherapeuticForm.getAdvancedFiltersForm();
		
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
		if(usageDetailTherapeuticFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(usageDetailTherapeuticFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(usageDetailTherapeuticFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		String flavourName =usageDetailTherapeuticFormattingSelectionForm.getFormat();
		
		if(flavourName !=null && flavourName.equalsIgnoreCase("Descending Dollar by Category") )
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC_DESC_BY_CATEGORY);	
		}else if (flavourName !=null && flavourName.equalsIgnoreCase("Therapeutic Code"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC_CODE);
		}
		else if (flavourName !=null && flavourName.equalsIgnoreCase("Therapeutic Description"))
		{
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC_DESC);
		}
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC);
		
		baseVO.setHtml(usageDetailTherapeuticFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(usageDetailTherapeuticFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(usageDetailTherapeuticFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(usageDetailTherapeuticFormattingSelectionForm.isResultsDisplayCSV());

		usageDetailTherapeuticVO = constructCannedReportVO(usageDetailTherapeuticFormattingSelectionForm);
		usageDetailTherapeuticGroupVO.setUsageDetailTherapeuticVO(usageDetailTherapeuticVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		String format = usageDetailTherapeuticFormattingSelectionForm.getAccountFormat(); 
		
		
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
		
		cannedReportVO.setUsageDetailTherapeuticGroupVO(usageDetailTherapeuticGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);	
		cannedReportVO.setCannedReportTitle("49");
	
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);

		return baseVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param report8020GenericCategoryFormattingSelectionForm the report8020formatting selection form
	 * @return report8020GenericCategoryVO
	 *
	 * 
	 */
	private static UsageDetailTherapeuticVO constructCannedReportVO(UsageDetailTherapeuticFormattingSelectionForm usageDetailTherapeuticFormattingSelectionForm) throws SMORAException
	{
		if (usageDetailTherapeuticVO == null)
		{
			usageDetailTherapeuticVO = new UsageDetailTherapeuticVO();
		}
		
		usageDetailTherapeuticFormattingVO = new UsageDetailTherapeuticFormattingVO();
		usageDetailTherapeuticFormattingVO.setAccountFormat(usageDetailTherapeuticFormattingSelectionForm.getAccountFormat());
		usageDetailTherapeuticFormattingVO.setFormat(usageDetailTherapeuticFormattingSelectionForm.getFormat());
		usageDetailTherapeuticFormattingVO.setSortOptions(usageDetailTherapeuticFormattingSelectionForm.getSortOptions());
		usageDetailTherapeuticVO.setUsageDetailTherapeuticFormattingVO(usageDetailTherapeuticFormattingVO);
		
		return usageDetailTherapeuticVO;
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
		if (usageDetailTherapeuticGroupVO == null)
		{
			usageDetailTherapeuticGroupVO = new UsageDetailTherapeuticGroupVO();
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
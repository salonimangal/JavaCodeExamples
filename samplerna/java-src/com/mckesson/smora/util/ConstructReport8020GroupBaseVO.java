/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.Report8020ConsolidatedViewFormattingVO;
import com.mckesson.smora.dto.Report8020ConsolidatedViewVO;
import com.mckesson.smora.dto.Report8020DescendingWithUnitsFormattingVO;
import com.mckesson.smora.dto.Report8020DescendingWithUnitsVO;
import com.mckesson.smora.dto.Report8020GenericCategoryFormattingVO;
import com.mckesson.smora.dto.Report8020GenericCategoryVO;
import com.mckesson.smora.dto.Report8020GroupVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.Report8020ConsolidatedViewForm;
import com.mckesson.smora.ui.form.Report8020ConsolidatedViewFormattingSelectionForm;
import com.mckesson.smora.ui.form.Report8020DescendingWithUnitsForm;
import com.mckesson.smora.ui.form.Report8020DescendingWithUnitsFormattingSelectionForm;
import com.mckesson.smora.ui.form.Report8020GenericCategoryForm;
import com.mckesson.smora.ui.form.Report8020GenericCategoryFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;


/**
 * @author venkatesh.v.perumal
 * @changes 20 November 2006
 *
 * This Class is used to Construct the ReportBaseVO for 8020 GroupReports
 *
 */

public class ConstructReport8020GroupBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReport8020GroupBaseVO";

	private static Report8020GroupVO report8020GroupVO = null;

	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;

	private static Report8020GenericCategoryVO report8020GenericCategoryVO = null;

	private static Report8020ConsolidatedViewVO report8020ConsolidatedViewVO = null;

	private static Report8020GenericCategoryFormattingVO report8020GenericCategoryFormattingVO = null;

	private static Report8020ConsolidatedViewFormattingVO report8020ConsolidatedViewFormattingVO = null;

	private static Report8020DescendingWithUnitsVO report8020DescendingWithUnitsVO = null;

	private static Report8020DescendingWithUnitsFormattingVO report8020DescendingWithUnitsFormattingVO = null;

	private static CriteriaVO criteriaVO = null;

	/**
	 * This method populates the report8020 descending with units base VO
	 *
	 * @param report8020DescendingWithUnitsForm the report8020 descending with units form
	 *
	 * @return baseVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReport8020GenericCategoryBaseVO(Report8020DescendingWithUnitsForm report8020DescendingWithUnitsForm) throws SMORAException
	{
		criteriaVO = new CriteriaVO();
		report8020GroupVO = new Report8020GroupVO();
		report8020DescendingWithUnitsVO = new Report8020DescendingWithUnitsVO();

		Report8020DescendingWithUnitsFormattingSelectionForm report8020DescendingWithUnitsFormattingSelectionForm = report8020DescendingWithUnitsForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = report8020DescendingWithUnitsForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = report8020DescendingWithUnitsForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = report8020DescendingWithUnitsForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = report8020DescendingWithUnitsForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = report8020DescendingWithUnitsForm.getAdvancedFiltersForm();
		
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

		ArrayList<String> fieldDescArray = new ArrayList<String>();
		GroupLevelVO groupLevelVO = new GroupLevelVO();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();

		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(report8020DescendingWithUnitsFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(report8020DescendingWithUnitsFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(report8020DescendingWithUnitsFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_8020_DESCENDING_WITH_UNITS);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_8020_DESCENDING_WITH_UNITS);
		baseVO.setHtml(report8020DescendingWithUnitsFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(report8020DescendingWithUnitsFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(report8020DescendingWithUnitsFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(report8020DescendingWithUnitsFormattingSelectionForm.isResultsDisplayCSV());

		report8020DescendingWithUnitsVO = constructCannedReportVO(report8020DescendingWithUnitsFormattingSelectionForm);
		report8020GroupVO.setReport8020DescendingWithUnitsVO(report8020DescendingWithUnitsVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);

		String format = report8020DescendingWithUnitsFormattingSelectionForm.getAccountFormat();

		if(!format.equals("Combine Accounts"))
		{
			fieldDescArray.add("Total $");
			fieldDescArray.add("Total Qty");
			fieldDescArray.add("% Total");

			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("$F{acct_name}");
			firstGroupLevelVO.setFieldName("Account Number");
			firstGroupLevelVO.setFieldWidth(52);
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

		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping

		cannedReportVO.setReport8020GroupVO(report8020GroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportVO.setCannedReportTitle("11");

		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);

		return baseVO;
	}

	/**
	 * This method populates the report8020 generic category base VO.
	 *
	 * @param report8020GenericCategoryForm the report8020 generic category form
	 * 
	 * @return baseVO
	 */
	public static ReportBaseVO populateReport8020GenericCategoryBaseVO(Report8020GenericCategoryForm report8020GenericCategoryForm) throws SMORAException
	{
		String format = null;
		String reportformat =  null;
		criteriaVO = new CriteriaVO();
		report8020GroupVO = new Report8020GroupVO();
		report8020GenericCategoryVO = new Report8020GenericCategoryVO();

		ArrayList<String> fieldDescArray = new ArrayList<String>();
		GroupLevelVO groupLevelVO = new GroupLevelVO();

		Report8020GenericCategoryFormattingSelectionForm report8020GenericCategoryFormattingSelectionForm = report8020GenericCategoryForm.getReport8020GenericCategoryFormattingSelectionForm();
		CustomerSelectionForm customerSelectionForm = report8020GenericCategoryForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = report8020GenericCategoryForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = report8020GenericCategoryForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = report8020GenericCategoryForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();

		baseVO.setCustomHeading(report8020GenericCategoryFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		reportformat = report8020GenericCategoryFormattingSelectionForm.getFormat();

		if(reportformat.equals("Generic Code"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_CODE);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GROUP);
		}
		else if(reportformat.equals("Generic Description"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_DESCRIPTION);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GROUP);
		}
		else if(reportformat.equals("Descending Dollar"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_DESCENDING_DOLLAR);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GROUP);
		}
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(report8020GenericCategoryFormattingSelectionForm.getTemplate_Name()!=null)
		{
		baseVO.setTemplateName(report8020GenericCategoryFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setHtml(report8020GenericCategoryFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(report8020GenericCategoryFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(report8020GenericCategoryFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(report8020GenericCategoryFormattingSelectionForm.isResultsDisplayCSV());
		format = report8020GenericCategoryFormattingSelectionForm.getAccountFormat();
		String reportType = report8020GenericCategoryFormattingSelectionForm.getFormat();

		if(!format.equals("Combine Accounts"))
		{
			//Field Id : 17
			fieldDescArray.add("Prev_YTD_Amt");
			//Field Id : 19
			fieldDescArray.add("Curr_YTD_Amt");
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("$F{acct_name}");
			firstGroupLevelVO.setFieldName("Account #");
			firstGroupLevelVO.setFieldWidth(52);

			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			
			if(reportType.equals("Generic Code"))
			{				
				secondGroupLevelVO.setClassType("java.lang.String");
				secondGroupLevelVO.setFieldDescription("$F{gnrc_dscr}");
				secondGroupLevelVO.setFieldName("Generic Code");
				secondGroupLevelVO.setFieldWidth(52);
			}
			else
			{
				secondGroupLevelVO.setClassType("java.lang.String");
				secondGroupLevelVO.setFieldDescription("$F{gnrc_dscr}");
				secondGroupLevelVO.setFieldName("Generic Description");
				secondGroupLevelVO.setFieldWidth(52);
			}

			firstGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);
		}
		else if(format.equals("Combine Accounts"))
		{
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			if(reportType.equals("Generic Code"))
			{				
				secondGroupLevelVO.setClassType("java.lang.String");
				secondGroupLevelVO.setFieldDescription("$F{gnrc_dscr}");
				secondGroupLevelVO.setFieldName("Generic Code");
				secondGroupLevelVO.setFieldWidth(52);
			}
		else
			{
				secondGroupLevelVO.setClassType("java.lang.String");
				secondGroupLevelVO.setFieldDescription("$F{gnrc_dscr}");
				secondGroupLevelVO.setFieldName("Generic Description");
				secondGroupLevelVO.setFieldWidth(52);
			}
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);
		}
		else
		{
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);
		}

		report8020GenericCategoryVO = constructCannedReportVO(report8020GenericCategoryFormattingSelectionForm);
		report8020GroupVO.setReport8020GenericCategoryVO(report8020GenericCategoryVO);

		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);

		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping
		cannedReportVO.setReport8020GroupVO(report8020GroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("9");
		
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}

/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 * @return baseVO
	 *
	 *
	 */
	public static ReportBaseVO populateReport8020GenericCategoryBaseVO(Report8020ConsolidatedViewForm report8020ConsolidatedViewForm) throws SMORAException
	{

		criteriaVO = new CriteriaVO();
		report8020GroupVO = new Report8020GroupVO();
		report8020ConsolidatedViewVO = new Report8020ConsolidatedViewVO();
		

		ArrayList<FieldsVO> fieldsVOList = new ArrayList<FieldsVO>();
		ArrayList<FirstGroupLevelVO> firstGroupLevelVOList = new ArrayList<FirstGroupLevelVO>();

		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();


		CustomReportDB customReportDB = new CustomReportDB();
		FieldsListVO fieldsListVO = new FieldsListVO();
		GroupLevelVO groupLevelVO = new GroupLevelVO();

		Report8020ConsolidatedViewFormattingSelectionForm report8020ConsolidatedViewFormattingSelectionForm = report8020ConsolidatedViewForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = report8020ConsolidatedViewForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = report8020ConsolidatedViewForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = report8020ConsolidatedViewForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = report8020ConsolidatedViewForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = report8020ConsolidatedViewForm.getAdvancedFiltersForm();
		
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
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(report8020ConsolidatedViewFormattingSelectionForm.getTemplate_Name()!=null )
		{
			baseVO.setTemplateName(report8020ConsolidatedViewFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(report8020ConsolidatedViewFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setHtml(report8020ConsolidatedViewFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(report8020ConsolidatedViewFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(report8020ConsolidatedViewFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(report8020ConsolidatedViewFormattingSelectionForm.isResultsDisplayCSV());
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_8020_CONSOLIDATED_VIEW);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_8020_CONSOLIDATED_VIEW);

		String format = report8020ConsolidatedViewFormattingSelectionForm.getAccountFormat();
	

		if(!format.equals("Combine Accounts"))
		{
			fieldDescArray.add("Average Total $ per month");
			fieldDescArray.add("Total Dollars for period");

			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("$F{acct_name}");
			firstGroupLevelVO.setFieldName("Account Number");
			firstGroupLevelVO.setFieldWidth(52);
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


		report8020ConsolidatedViewVO = constructCannedReportVO(report8020ConsolidatedViewFormattingSelectionForm);
		report8020GroupVO.setReport8020ConsolidatedViewVO(report8020ConsolidatedViewVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);

		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		criteriaVO = constructCannedReportVO(customerSelectionForm);

		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		cannedReportVO.setReport8020GroupVO(report8020GroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("10");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);

		return baseVO;
	}

	/**
	 * This method constructs the canned report VO for 8020 Descending With Units Report.
	 *
	 * @param report8020DescendingWithUnitsFormattingSelectionForm the report8020DescendingWithUnitsFormatting Selection Form
	 * @return report8020DescendingWithUnitsVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private static Report8020DescendingWithUnitsVO constructCannedReportVO(Report8020DescendingWithUnitsFormattingSelectionForm report8020DescendingWithUnitsFormattingSelectionForm) throws SMORAException
	{
		if (report8020DescendingWithUnitsVO == null)
		{
			report8020DescendingWithUnitsVO = new Report8020DescendingWithUnitsVO();
		}

		report8020DescendingWithUnitsFormattingVO = new Report8020DescendingWithUnitsFormattingVO();
		report8020DescendingWithUnitsFormattingVO.setAccountFormat(report8020DescendingWithUnitsFormattingSelectionForm.getAccountFormat());
		report8020DescendingWithUnitsFormattingVO.setFiscalYearStartMonth(report8020DescendingWithUnitsFormattingSelectionForm.getFiscalYearStartMonth());
		report8020DescendingWithUnitsVO.setReport8020DescendingWithUnitsFormattingVO(report8020DescendingWithUnitsFormattingVO);

		return report8020DescendingWithUnitsVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param report8020GenericCategoryFormattingSelectionForm the report8020formatting selection form
	 * @return report8020GenericCategoryVO
	 *
	 *
	 */
	private static Report8020GenericCategoryVO constructCannedReportVO(Report8020GenericCategoryFormattingSelectionForm report8020GenericCategoryFormattingSelectionForm) throws SMORAException
	{
		if (report8020GenericCategoryVO == null)
		{
			report8020GenericCategoryVO = new Report8020GenericCategoryVO();
		}

		report8020GenericCategoryFormattingVO = new Report8020GenericCategoryFormattingVO();

		report8020GenericCategoryFormattingVO.setAccountFormat(report8020GenericCategoryFormattingSelectionForm.getAccountFormat());
		report8020GenericCategoryFormattingVO.setFormat(report8020GenericCategoryFormattingSelectionForm.getFormat());
		report8020GenericCategoryFormattingVO.setDetailSortOptions(report8020GenericCategoryFormattingSelectionForm.getDetailSortOptions());
		//report8020GenericCategoryFormattingVO.setSelectedMonth(report8020GenericCategoryFormattingSelectionForm.getSelectedMonth());
		report8020GenericCategoryFormattingVO.setSelectedTime(report8020GenericCategoryFormattingSelectionForm.getSelectedTime());

		report8020GenericCategoryVO.setReport8020GenericCategoryFormattingVO(report8020GenericCategoryFormattingVO);

		return report8020GenericCategoryVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param report8020ConsolidatedFormattingSelectionForm the report8020formatting selection form
	 * @return report8020ConsolidatedViewVO
	 *
	 */
	private static Report8020ConsolidatedViewVO constructCannedReportVO(Report8020ConsolidatedViewFormattingSelectionForm report8020ConsolidatedViewFormattingSelectionForm) throws SMORAException
	{
		if (report8020ConsolidatedViewVO == null)
		{
			report8020ConsolidatedViewVO = new Report8020ConsolidatedViewVO();
		}

		report8020ConsolidatedViewFormattingVO = new Report8020ConsolidatedViewFormattingVO();

		report8020ConsolidatedViewFormattingVO.setAccountFormat(report8020ConsolidatedViewFormattingSelectionForm.getAccountFormat());
		report8020ConsolidatedViewFormattingVO.setSelectedMonth(report8020ConsolidatedViewFormattingSelectionForm.getSelectedMonth());

		report8020ConsolidatedViewVO.setReport8020ConsolidatedViewFormattingVO(report8020ConsolidatedViewFormattingVO);
		return report8020ConsolidatedViewVO;
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
		if (report8020GroupVO == null)
		{
			report8020GroupVO = new Report8020GroupVO();
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

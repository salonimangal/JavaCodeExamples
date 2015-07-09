/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 */

package com.mckesson.smora.util;
/*
 * Author Manikandan.R
 * date 25/12/06
 */

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.DescDollarFormattingVO;
import com.mckesson.smora.dto.DescendingDollarGroupVO;
import com.mckesson.smora.dto.DescendingDollarVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.DescDollarFormattingSelectionForm;
import com.mckesson.smora.ui.form.DescendingDollarForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;


public class ConstructDescendingDollarBaseVO extends ConstructCannedReportBaseVO
{

	private static DescendingDollarVO  descendingDollarVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
	private static DescendingDollarGroupVO descendingDollarGroupVO = null;

	private  static DescDollarFormattingVO descDollarFormattingVO = null;

	private static CriteriaVO criteriaVO = null;

	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;
	
	private static Log log = LogFactory.getLog(ConstructDescendingDollarBaseVO.class);

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm
	 *            the custom reporting form
	 * @return the report base VO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public static  ReportBaseVO populateDescendingDollarBaseVO(DescendingDollarForm descendingDollarForm) throws SMORAException
	{
		log.info(" Start of  Method populateDescendingDollarBaseVO -Populate the ReportBaseVO");
		descendingDollarVO = new DescendingDollarVO();
		descendingDollarGroupVO = new DescendingDollarGroupVO();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		String format = null;
		
		DescDollarFormattingSelectionForm descDollarFormattingSelectionForm = descendingDollarForm.getDescDollarFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = descendingDollarForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = descendingDollarForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = descendingDollarForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = descendingDollarForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = descendingDollarForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();

		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
				
		FieldsListVO fieldsListVO = new FieldsListVO();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(descDollarFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(descDollarFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(descDollarFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		format = descDollarFormattingSelectionForm.getFormat();
		
		if(format.equals("Generic Code"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_CODE);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_CODE);
		}
		else if(format.equals("Generic Description"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_DESCRIPTION);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_DESCRIPTION);
		}
		else if(format.equals("Therapeutic Class"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_THERAPEUTIC);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_THERAPEUTIC);
		}
		else if(format.equals("Item"))
		{
			baseVO.setReportSubtype(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_ITEM_CODE);
			baseVO.setReportGroupID(ReportManagerConstants.REPORT_DESCENDING_DOLLAR_ITEM_CODE);
		}
		
		baseVO.setHtml(descDollarFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(descDollarFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(descDollarFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(descDollarFormattingSelectionForm.isResultsDisplayCSV());

			
		if(!descDollarFormattingSelectionForm.getAccountFormat().equals("Combine Accounts"))
		{
			fieldDescArray.add("Total Dollars");
			fieldDescArray.add("Curr %");
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("$F{Acct_Name}");
			firstGroupLevelVO.setFieldName("Account#");
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
		
		
		descDollarFormattingVO = constructCannedReportVO(descDollarFormattingSelectionForm);
	    dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
	    criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
		descendingDollarVO.setDescDollarFormattingVO(descDollarFormattingVO);
		descendingDollarGroupVO.setDescendingDollarVO(descendingDollarVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping 
			
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setDescendingDollarGroupVO(descendingDollarGroupVO);
		cannedReportVO.setCannedReportTitle("24");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		log.info(" End  of  Method populateDescendingDollarBaseVO -Populate the ReportBaseVO");
		return baseVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param descDollarFormattingSelectionForm
	 *          
	 * @return descDollarFormattingVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */


	private static DescDollarFormattingVO constructCannedReportVO(DescDollarFormattingSelectionForm descDollarFormattingSelectionForm) throws SMORAException
	{
		log.info(" Start of  Method constructCannedReportVO (DescDollarFormattingSelectionForm)");
			 descDollarFormattingVO = new  DescDollarFormattingVO();


		 descDollarFormattingVO.setAccountFormat(descDollarFormattingSelectionForm.getAccountFormat());
		 descDollarFormattingVO.setFormat(descDollarFormattingSelectionForm.getFormat());
		
		 descDollarFormattingVO.setIncludeOnly(descDollarFormattingSelectionForm.getIncludeOnly());
		if((descDollarFormattingSelectionForm.getCumulativePercentage()!=null) &&!(descDollarFormattingSelectionForm.getCumulativePercentage().equals("")))
		 descDollarFormattingVO.setCumulativePercentage(descDollarFormattingSelectionForm.getCumulativePercentage());
		if((descDollarFormattingSelectionForm.getTopNumberOfRows()!=null) &&!(descDollarFormattingSelectionForm.getTopNumberOfRows().equals("")))
		 descDollarFormattingVO.setTopNumberOfRows(descDollarFormattingSelectionForm.getTopNumberOfRows());
		log.info(" End  of  Method constructCannedReportVO (DescDollarFormattingSelectionForm)");
		return descDollarFormattingVO;
	}


	/**
	 * This method constructs the canned report VO.
	 *
	 * @param dateSelectionForm
	 *          
	 * @return dateSelectionAndComparisonVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */

	private static  DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{

		log.info(" Start of  Method constructCannedReportVO (DateSelectionForm)");
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


		log.info(" End  of  Method constructCannedReportVO (DateSelectionForm)");
		return dateSelectionAndComparisonVO;
	}










}

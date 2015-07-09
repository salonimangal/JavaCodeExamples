/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.ColumnsListVO;
import com.mckesson.smora.dto.ColumnsVO;
import com.mckesson.smora.dto.ControlledSubstanceFormattingVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.CustReportCriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.Report8020ConsolidatedViewVO;
import com.mckesson.smora.dto.Report8020DescendingWithUnitsVO;
import com.mckesson.smora.dto.Report8020DescendingWithUnitsFormattingVO;
import com.mckesson.smora.dto.Report8020GroupVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportQuarterlyDrugUtilizationFormattingVO;
import com.mckesson.smora.dto.ReportQuarterlyDrugUtilizationVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;

import com.mckesson.smora.exception.SMORAException;

import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.Report8020ConsolidatedViewForm;
import com.mckesson.smora.ui.form.Report8020ConsolidatedViewFormattingSelectionForm;
import com.mckesson.smora.ui.form.Report8020DescendingWithUnitsForm;
import com.mckesson.smora.ui.form.Report8020DescendingWithUnitsFormattingSelectionForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportQuarterlyDrugUtilizationForm;
import com.mckesson.smora.ui.form.ReportQuarterlyDrugUtilizationFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.common.ReportManagerConstants;
/**
 * @author MG.Razvi
 * @changes 01 February 2006
 * 
 * This Class is used to Construct the ReportBaseVO for 8020 GroupReports 
 * 
 */
public class ConstructReportQuarterlyDrugUtilizationVO extends ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = " ConstructReport8020GroupBaseVO";
	
	private static Report8020GroupVO report8020GroupVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;

	private static ReportQuarterlyDrugUtilizationVO reportQuarterlyDrugUtilizationVO = null;
	
	private static ReportQuarterlyDrugUtilizationFormattingVO reportQuarterlyDrugUtilizationFormattingVO = null;
	
	private static CriteriaVO criteriaVO = null;
	
	private static ReportQuarterlyDrugUtilizationVO constructCannedReportVO(ReportQuarterlyDrugUtilizationFormattingSelectionForm reportQuarterlyDrugUtilizationFormattingSelectionForm) throws SMORAException
	{
		if (reportQuarterlyDrugUtilizationVO == null)
		{
			reportQuarterlyDrugUtilizationVO = new ReportQuarterlyDrugUtilizationVO();
		}
	
		reportQuarterlyDrugUtilizationFormattingVO = new ReportQuarterlyDrugUtilizationFormattingVO();
	
		reportQuarterlyDrugUtilizationFormattingVO.setAccountFormat(reportQuarterlyDrugUtilizationFormattingSelectionForm.getAccountFormat());
		//reportQuarterlyDrugUtilizationFormattingVO.setFormat(reportQuarterlyDrugUtilizationFormattingSelectionForm.getF .getFormat());
		reportQuarterlyDrugUtilizationFormattingVO.setDetailSortOptions(reportQuarterlyDrugUtilizationFormattingSelectionForm.getDetailSortOptions());
		//report8020GenericCategoryFormattingVO.setSelectedMonth(report8020GenericCategoryFormattingSelectionForm.getSelectedMonth());
		//reportQuarterlyDrugUtilizationFormattingVO.setSelectedTime(reportQuarterlyDrugUtilizationFormattingSelectionForm.getSelectedTime());
	
	
	
	reportQuarterlyDrugUtilizationVO.setReportQuarterlyDrugUtilizationFormattingVO(reportQuarterlyDrugUtilizationFormattingVO);
	
	return reportQuarterlyDrugUtilizationVO;
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
	public static ReportBaseVO populateReportQuarterlyDrugUtilizationBaseVO(ReportQuarterlyDrugUtilizationForm reportQuarterlyDrugUtilizationForm,String userId) throws SMORAException
	{
		
		criteriaVO = new CriteriaVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		List<TApplCnRptFldMetaData> metaDataList = null;
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		//report8020GroupVO = new Report8020GroupVO();
		reportQuarterlyDrugUtilizationVO = new ReportQuarterlyDrugUtilizationVO();
		
		ArrayList<FieldsVO> fieldsVOList = new ArrayList<FieldsVO>();
		
		ArrayList<String> subTotalArray = new ArrayList();

		FieldsListVO fieldsListVO = new FieldsListVO();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		DateConverision dateConversion = new DateConverision();
		
		ReportQuarterlyDrugUtilizationFormattingSelectionForm reportQuarterlyDrugUtilizationFormattingSelectionForm = reportQuarterlyDrugUtilizationForm.getReportQuarterlyDrugUtilizationFormattingSelectionForm();
		
		DateSelectionForm dateSelectionForm = reportQuarterlyDrugUtilizationForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportQuarterlyDrugUtilizationForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportQuarterlyDrugUtilizationForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportQuarterlyDrugUtilizationForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportQuarterlyDrugUtilizationForm.getAdvancedFiltersForm();
		
		/*
		 * SO-0953 :  Code added to set the calendar selected dates 
		 */
		if(dateSelectionForm.getCustomStartDate96Mnth()!= null && dateSelectionForm.getCustomStartDate96Mnth().trim()!="")
		{
			String formattedStartDate = dateConversion.formatCustomDate(dateSelectionForm.getCustomStartDate96Mnth(), "MM/dd/yyyy");
			dateSelectionForm.setStartSelectedTime1(formattedStartDate);
		}	
		if(dateSelectionForm.getCustomEndDate96Mnth()!= null && dateSelectionForm.getCustomEndDate96Mnth().trim()!="")
		{
			String formattedEndDate = dateConversion.formatCustomDate(dateSelectionForm.getCustomEndDate96Mnth(), "MM/dd/yyyy");
			dateSelectionForm.setEndSelectedTime1(formattedEndDate);
		}
		/*
		 * SO-0953 :  Code ends
		 */
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportQuarterlyDrugUtilizationFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportQuarterlyDrugUtilizationFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportQuarterlyDrugUtilizationFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setHtml(reportQuarterlyDrugUtilizationFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportQuarterlyDrugUtilizationFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportQuarterlyDrugUtilizationFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportQuarterlyDrugUtilizationFormattingSelectionForm.isResultsDisplayCSV());
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_QUATERLY_DRUG_UTILIZATION);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_QUATERLY_DRUG_UTILIZATION);
				
		String format = reportQuarterlyDrugUtilizationFormattingSelectionForm.getAccountFormat(); 

		
		if(!format.equals("Combine Accounts"))
		{

			subTotalArray.add("CUST_ACCT_ID");			
	
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account #");
			firstGroupLevelVO.setFieldName("Account #");
			firstGroupLevelVO.setFieldWidth(52);
			firstGroupLevelVO.setSubTotal(subTotalArray);
		//	firstGroupLevelVO.setSubTotal(fieldDescArray);
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
		
		
		reportQuarterlyDrugUtilizationVO = constructCannedReportVO(reportQuarterlyDrugUtilizationFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);	
		fieldsListVO.setFieldsVOList(fieldsVOList); // Added for Grouping
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO); // Added for Grouping
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);// Added for Grouping 
//		cannedReportCriteriaVO.setColumnsListVO(columnsListVO);// Added for Grouping 
	
		
		reportQuarterlyDrugUtilizationVO.setReportQuarterlyDrugUtilizationFormattingVO(reportQuarterlyDrugUtilizationFormattingVO);
		cannedReportVO.setReportQuarterlyDrugUtilizationVO(reportQuarterlyDrugUtilizationVO);

		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		fieldsList = getFieldsList(metaDataList, reportQuarterlyDrugUtilizationFormattingVO, userId);
	
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("41");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	/**
	 * This method returns fieldVOs List which should be populated in the Jasper report 
	 * @param metaDataList Meta List from T_Appl_Cn_Rpt_FldMeta_Data table
	 * @param tSTPFormattingVO Time Series Time period Comparison FormattingVO
	 * @param userId User Id
	 * @return Fields VO Array List
	 */
	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,ReportQuarterlyDrugUtilizationFormattingVO reportQuarterlyDrugUtilizationFormattingVO , String userId)
	{
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		String accountFormat = reportQuarterlyDrugUtilizationFormattingVO.getAccountFormat();
		String detailSortOptions = reportQuarterlyDrugUtilizationFormattingVO.getDetailSortOptions();
		
		if (metaDataList != null && metaDataList.size() > 0)
		{
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
				if(accountFormat.equals("Combine Accounts"))
				{
					if (!(tApplCnRptFldMetaData.getFldDscr().equals("Account #")))
					{
						int fieldId = tApplCnRptFldMetaData.getId().getFldId();
						FieldsVO fieldsVO = new FieldsVO();
						fieldsVO.setFldId(fieldId);
						fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
						fieldsList.add(fieldsVO);
					}
				}				
				else 
				{
					int fieldId = tApplCnRptFldMetaData.getId().getFldId();
					FieldsVO fieldsVO = new FieldsVO();
					fieldsVO.setFldId(fieldId);
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
					fieldsList.add(fieldsVO);
				}
			}
		}
		return fieldsList;
	}
	
}

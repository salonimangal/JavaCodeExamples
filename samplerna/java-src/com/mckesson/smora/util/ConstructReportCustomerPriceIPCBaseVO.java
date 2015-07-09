package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import com.mckesson.smora.appl.util.DateSelectionUtil;
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
import com.mckesson.smora.dto.ReportCustomerPriceCPCFormattingVO;
import com.mckesson.smora.dto.ReportCustomerPriceIPCFormattingVO;
import com.mckesson.smora.dto.ReportCustomerPriceIPCVO;
import com.mckesson.smora.dto.TSSFormattingVO;
import com.mckesson.smora.dto.TSTPFormattingVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.TimeSeriesGroupVO;
import com.mckesson.smora.dto.TimeSeriesSummaryVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.CustomerPriceIPCForm;
import com.mckesson.smora.ui.form.CustomerPriceIPCFormattingSelectionForm;

/**
 * @author mohana.ramachandran
 * @changes 23 November 2006 This Class is used to Construct the ReportBaseVO
 *          for ReportCustomerPriceIPC ReportGroup
 */
public class ConstructReportCustomerPriceIPCBaseVO extends
		ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportCustomerPriceIPCBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static ReportCustomerPriceIPCVO reportCustomerPriceIPCVO = null;
	
	private static ReportCustomerPriceIPCFormattingVO ipcFormattingVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
	private static CriteriaVO criteriaVO = null;
	
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
	public static ReportBaseVO populateReportCustomerPriceIPCBaseVO(CustomerPriceIPCForm customerPriceIPCForm) throws SMORAException
	{
		
		
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		reportCustomerPriceIPCVO = new ReportCustomerPriceIPCVO();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		CustomerPriceIPCFormattingSelectionForm customerPriceIPCFormattingSelectionForm = customerPriceIPCForm.getCustomerPriceIPCFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = customerPriceIPCForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = customerPriceIPCForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = customerPriceIPCForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = customerPriceIPCForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = customerPriceIPCForm.getAdvancedFiltersForm();
		
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
		
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(customerPriceIPCFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(customerPriceIPCFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		
		baseVO.setCustomHeading(customerPriceIPCFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_CUSTOMER_PRICE_IPC);
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_CUSTOMER_PRICE_IPC);
		baseVO.setHtml(customerPriceIPCFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(customerPriceIPCFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(customerPriceIPCFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(customerPriceIPCFormattingSelectionForm.isResultsDisplayCSV());
		String allowedVariance = customerPriceIPCFormattingSelectionForm.getAllowedVariance(); 
		
		
		ipcFormattingVO = constructCannedReportVO(customerPriceIPCFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		reportCustomerPriceIPCVO.setReportCustomerPriceIPCFormattingVO(ipcFormattingVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		ArrayList <String>sequenceFields = new ArrayList();
		
		
		ArrayList<FieldsVO> fieldsVOList = new ArrayList();
		FieldsListVO fieldsListVO = new FieldsListVO();
		CustomReportDB customReportDB  = new CustomReportDB();
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();


		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("$F{Section_Num}");
		firstGroupLevelVO.setFieldName("GroupName");
		firstGroupLevelVO.setFieldWidth(200);
	
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(null);
		groupLevelVO.setThirdGroupLevel(null);
		
		
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		cannedReportVO.setReportCustomerPriceIPCVO(reportCustomerPriceIPCVO);
		cannedReportVO.setCannedReportTitle("21,22");
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
			
		baseVO.setCannedReportVO(cannedReportVO);
		return baseVO;
	

		
	}

	/**
	 * This method constructs the canned report VO.
	 * 
	 * @param ReportCustomerPriceIPCFormattingSelectionForm
	 *            the ReportCustomerPriceIPCFormatting selection form
	 * @return ReportCustomerPriceIPCVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	private static ReportCustomerPriceIPCFormattingVO constructCannedReportVO(CustomerPriceIPCFormattingSelectionForm customerPriceIPCFormattingSelectionForm) throws SMORAException
	{
		
		if (ipcFormattingVO == null)
		{
			ipcFormattingVO = new ReportCustomerPriceIPCFormattingVO();
		}
		
		String allowedVariance = customerPriceIPCFormattingSelectionForm.getAllowedVariance();
		String sortOptions=customerPriceIPCFormattingSelectionForm.getSortOptions();
		
		ipcFormattingVO.setAllowedVariance(allowedVariance);
		ipcFormattingVO.setSortOptions(sortOptions);
		
		
		ipcFormattingVO.setSortOptions(customerPriceIPCFormattingSelectionForm.getSortOptions());
		ipcFormattingVO.setIncludeSectionsPricesDiffer(customerPriceIPCFormattingSelectionForm.isIncludeSectionsPricesDiffer());
		ipcFormattingVO.setIncludeSectionsInvoice(customerPriceIPCFormattingSelectionForm.isIncludeSectionsInvoice());
		return ipcFormattingVO;
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

		//if (reportCustomerPriceIPCVO == null)
		//{
		//	reportCustomerPriceIPCVO = new ReportCustomerPriceIPCVO();
		//}
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

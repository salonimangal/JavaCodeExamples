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
import com.mckesson.smora.dto.PreferRxSavingsFormattingVO;
import com.mckesson.smora.dto.PreferRxSavingsVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.PreferRxSavingsVO;
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
import com.mckesson.smora.ui.form.PreferRxSavingsForm;
import com.mckesson.smora.ui.form.PreferRxSavingsFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.TimeSeriesSummaryForm;
import com.mckesson.smora.ui.form.TimeSeriesSummaryFormattingSelectionForm;

/**
 * @author mohana.ramachandran
 * @changes 23 November 2006 This Class is used to Construct the ReportBaseVO
 *          for TimeSeriesSummary ReportGroup
 */
public class ConstructPreferRxSavingsVO extends
		ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPreferRxSavingsVO";

	/**
	 * The cust report criteria VO.
	 */
	private static PreferRxSavingsVO preferRxSavingsVO = null;
	
	private static PreferRxSavingsFormattingVO preferRxSavingsFormattingVO = null;
	
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
	public static ReportBaseVO populatePreferRxSavingsVO(PreferRxSavingsForm preferRxSavingsForm) throws SMORAException
	{
		
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		PreferRxSavingsVO preferRxSavingsVO = new PreferRxSavingsVO();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		
		PreferRxSavingsFormattingSelectionForm preferRxSavingsFormattingSelectionForm = preferRxSavingsForm.getPreferRxSavingsFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = preferRxSavingsForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = preferRxSavingsForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = preferRxSavingsForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = preferRxSavingsForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = preferRxSavingsForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		TimeSeriesGroupVO timeSeriesGroupVO = new TimeSeriesGroupVO();

		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(preferRxSavingsFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(preferRxSavingsFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		
		baseVO.setCustomHeading(preferRxSavingsFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_PREFER_RX_SAVINGS);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_PREFER_RX_SAVINGS);
		baseVO.setHtml(preferRxSavingsFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(preferRxSavingsFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(preferRxSavingsFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(preferRxSavingsFormattingSelectionForm.isResultsDisplayCSV());
		String format = preferRxSavingsFormattingSelectionForm.getAccountFormat(); 
		System.out.println("############ReportFormat: "+format);
		
			//subTotalArray.add("Avg_Amt");
			//subTotalArray.add("Tot_Amt");
			
			//fieldDescArray.add("Average Total $ per month");
			//fieldDescArray.add("Total Dollars for Period");
		if(!format.equals("Combine Account"))
		{
			
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account #");
			firstGroupLevelVO.setFieldName("Account #");
			firstGroupLevelVO.setFieldWidth(100);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);
				
		}	
		preferRxSavingsFormattingVO = constructCannedReportVO(preferRxSavingsFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		preferRxSavingsVO.setPreferRxSavingsFormattingVO(preferRxSavingsFormattingVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		ArrayList <String>sequenceFields = new ArrayList();
		
		
		ArrayList<FieldsVO> fieldsVOList = new ArrayList();
		FieldsListVO fieldsListVO = new FieldsListVO();
		CustomReportDB customReportDB  = new CustomReportDB();
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();

		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		
		cannedReportVO.setPreferRxSavingsVO(preferRxSavingsVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle(String.valueOf(ReportManagerConstants.REPORT_PREFER_RX_SAVINGS));
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
	private static PreferRxSavingsFormattingVO constructCannedReportVO(PreferRxSavingsFormattingSelectionForm preferRxSavingsFormattingSelectionForm) throws SMORAException
	{
		preferRxSavingsFormattingVO = new PreferRxSavingsFormattingVO();
		
		preferRxSavingsFormattingVO.setAccountFormat(preferRxSavingsFormattingSelectionForm.getAccountFormat());
		
		
		
		return preferRxSavingsFormattingVO;
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

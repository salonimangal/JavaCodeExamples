/**
 *
 * This Class is used to Construct the ReportBaseVO for MarketShareReport
 *
 */
/**
 * @author Ganesh.NR
 */
/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GenericListVO;
import com.mckesson.smora.dto.ItemDetailsVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.MarketShareVO;
import com.mckesson.smora.dto.PerformanceManagementVO;
import com.mckesson.smora.dto.ReportPerformanceManagementVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TherapeuticListVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ReportPerformanceManagementForm;
import com.mckesson.smora.ui.form.ReportPerformanceManagementFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;


/**
 *
 * This Class is used to Construct the ReportBaseVO for MarketShareReport ReportGroup
 *
 */

public class ConstructPerformanceMgtVO extends ConstructCannedReportBaseVO
{

	protected static Log log = LogFactory.getLog(ConstructPerformanceMgtVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPerformanceMgtVO";

	/**
	 * The cust report criteria VO.
	 */

	private static CriteriaVO criteriaVO = null;
	
   private static PerformanceManagementVO performanceManagementVO = null;
   
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;

	//private static TimeSeriesGroupVO timeSeriesGroupVO = null;

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	
	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populatePerformanceMgtReportBaseVO(ReportPerformanceManagementForm reportPerformanceManagementForm , String userId,String reportName) throws SMORAException
	{

		List<TApplCnRptFldMetaData> metaDataList = null;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		ReportPerformanceManagementVO reportPerformanceManagementVO = new ReportPerformanceManagementVO();
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		criteriaVO = new CriteriaVO();
		
		ReportPerformanceManagementFormattingSelectionForm reportPerformanceManagementFormattingSelectionForm = reportPerformanceManagementForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportPerformanceManagementForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportPerformanceManagementForm.getCustomerSelectionForm();
	

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportPerformanceManagementFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportPerformanceManagementFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		
		baseVO.setCustomHeading(reportPerformanceManagementFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		
		if(reportName != null)
		{
			if(reportName.equalsIgnoreCase("ReturnAnalysis")){
				baseVO.setReportSubtype(ReportManagerConstants.REPORT_RETURNS_ANALYSIS);
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_RETURNS_ANALYSIS);
				cannedReportVO.setCannedReportTitle("36,33");
			}
			else if(reportName.equalsIgnoreCase("Credit"))
			{
				System.out.println("Inside Credit Report");
				baseVO.setReportSubtype(ReportManagerConstants.REPORT_CREDIT_LESS_THAN_100_PERCENT);
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_CREDIT_LESS_THAN_100_PERCENT);
				cannedReportVO.setCannedReportTitle("34,33");
				fieldDescArray.add("Orig Amount");
				fieldDescArray.add("Handling Charge");
				FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
				firstGroupLevelVO.setClassType("java.lang.String");
				firstGroupLevelVO.setFieldDescription("$F{Acct_Name}");
				firstGroupLevelVO.setFieldName("Account #");
				firstGroupLevelVO.setFieldWidth(52);
				firstGroupLevelVO.setSubTotal(fieldDescArray);
				groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
				groupLevelVO.setSecondGroupLevel(null);
				groupLevelVO.setThirdGroupLevel(null);
				cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
			}
		}
		baseVO.setHtml(reportPerformanceManagementFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportPerformanceManagementFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportPerformanceManagementFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportPerformanceManagementFormattingSelectionForm.isResultsDisplayCSV());

		ItemVO itemVO = new ItemVO();
		ItemNumbersListVO itemNumbersListVO = new ItemNumbersListVO();
		ArrayList<ItemDetailsVO> itemNumberList = new ArrayList();
		itemNumbersListVO.setItemNumberList(itemNumberList);		
		itemVO.setItemNumbersList(itemNumbersListVO);
		
		GenericListVO genericVO = new GenericListVO();
		genericVO.setGenericVOList(null);
		itemVO.setGenericGroupsList(genericVO);
		
		itemVO.setItemGroupId(null);
		TherapeuticListVO theraListVO = new TherapeuticListVO();
		theraListVO.setTherapeuticList(null);
		itemVO.setTherapeuticList(theraListVO);
		

		
		performanceManagementVO = constructCannedReportVO(reportPerformanceManagementFormattingSelectionForm);
		reportPerformanceManagementVO.setPerformanceManagementVO(performanceManagementVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		criteriaVO.setItemVO(itemVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);

		log.info(" Report Group ID for Performance Management report is : "+ baseVO.getReportGroupID());
		//		populating the FieldsList
		if(reportName != null)
		{
			if(reportName.equalsIgnoreCase("ReturnAnalysis")){
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, reportPerformanceManagementVO, userId);
		log.info("fieldsList from populateMarketShareReportBaseVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
			}
		}
	//	cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setReportPerformanceManagementVO(reportPerformanceManagementVO);
		baseVO.setCannedReportVO(cannedReportVO);

		log.info("populatePerformanceMgtReportBaseVO --- END");
		
		return baseVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param marketShareByGenericNameFormattingSelectionForm the marketShareByGenericNameFormattingSelectionForm
	 *
	 * @return reportMarketShareByGenericNameVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private static PerformanceManagementVO constructCannedReportVO(ReportPerformanceManagementFormattingSelectionForm reportPerformanceManagementFormattingSelectionForm) throws SMORAException
	{
		if (performanceManagementVO== null)
		{
			performanceManagementVO = new PerformanceManagementVO();
		}

		
		return performanceManagementVO;
	}

	
	
	
	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,ReportPerformanceManagementVO reportPerformanceManagementVO , String userId)
	{
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();

		if (metaDataList != null && metaDataList.size() > 0)
		{
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);

				int fieldId = tApplCnRptFldMetaData.getId().getFldId();
				FieldsVO fieldsVO = new FieldsVO();
				fieldsVO.setFldId(fieldId);
				fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());

				fieldsList.add(fieldsVO);
			}


		}
		return fieldsList;
	}

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

	
	
	
}
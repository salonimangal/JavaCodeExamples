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
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GenericListVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ItemDetailsVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.MarketShareVO;
import com.mckesson.smora.dto.NewlyPurchasedItemsVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportMarketShareByGenericNameVO;
import com.mckesson.smora.dto.ReportNewlyPurchasedItemsVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TherapeuticListVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.MarketShareByGenericNameFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.ReportNewlyPurchasedItemsFormattingSelectionForm;
import com.mckesson.smora.ui.form.ReportNewlyPurchasedItemsForm;

public class ConstructNewlyPurchasedItemsVO extends ConstructCannedReportBaseVO
{
	
	protected static Log log = LogFactory.getLog(ConstructNewlyPurchasedItemsVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructNewlyPurchasedItemsVO";

	/**
	 * The cust report criteria VO.
	 */
		
	private static CriteriaVO criteriaVO = null;
	
	private static ReportNewlyPurchasedItemsVO reportNewlyPurchasedItemsVO = null;
	
	private static NewlyPurchasedItemsVO newlyPurchasedItemsVO = null ;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;

	
	
	/*
	  This method populates the report base VO.
	 
	  @param customReportingForm the custom reporting form
	 
	  @return the report base VO
	 
	  @throws SMORAException the SMORA exception
	 */
	
	public static ReportBaseVO populateNewlyPurchasedItemsReportBaseVO(ReportNewlyPurchasedItemsForm reportNewlyPurchasedItemsForm , String userId) throws SMORAException
	{
		
		List<TApplCnRptFldMetaData> metaDataList = null;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		//timeSeriesGroupVO = new TimeSeriesGroupVO();
		
		reportNewlyPurchasedItemsVO = new ReportNewlyPurchasedItemsVO();
		
		newlyPurchasedItemsVO = new NewlyPurchasedItemsVO();
		
		criteriaVO = new CriteriaVO();
		int noOfAccounts = 0;
		ReportNewlyPurchasedItemsFormattingSelectionForm reportNewlyPurchasedItemsFormattingSelectionForm = reportNewlyPurchasedItemsForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportNewlyPurchasedItemsForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportNewlyPurchasedItemsForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportNewlyPurchasedItemsForm.getSupplierSelectionForm();
		
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportNewlyPurchasedItemsFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportNewlyPurchasedItemsFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportNewlyPurchasedItemsFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_NEWLY_PURCHASED_ITEMS);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_NEWLY_PURCHASED_ITEMS);
		//TODO below logic for getting the reportsubtype can be moved to separate method
	//	StringBuffer rptName = new StringBuffer("Market Share - Report ");
		
	
	
		
		baseVO.setHtml(reportNewlyPurchasedItemsFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportNewlyPurchasedItemsFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportNewlyPurchasedItemsFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportNewlyPurchasedItemsFormattingSelectionForm.isResultsDisplayCSV());
		
		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("$F{CUST_ACCT_ID}");
		firstGroupLevelVO.setFieldName("Account Number");
		firstGroupLevelVO.setFieldWidth(100);
		firstGroupLevelVO.setSubTotal(fieldDescArray);
		secondGroupLevelVO.setClassType("java.lang.String");
		secondGroupLevelVO.setFieldDescription("$F{OSD_DEPT}");
		secondGroupLevelVO.setFieldName("Department");
		secondGroupLevelVO.setFieldWidth(100);
		secondGroupLevelVO.setSubTotal(fieldDescArray);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(null);
		
		
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

		reportNewlyPurchasedItemsVO = constructCannedReportVO(reportNewlyPurchasedItemsFormattingSelectionForm);
		newlyPurchasedItemsVO.setReportNewlyPurchasedItemsVO(reportNewlyPurchasedItemsVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO.setItemVO(itemVO);
	//	criteriaVO = constructCannedReportVO(itemSelectionForm);
	//	criteriaVO = constructCannedReportVO(advancedFiltersForm);

		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		//marketShareVO.setCriteriaVO(criteriaVO);	
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		

		log.info(" Report Group ID for Report Newly Purchased Item report is : "+ baseVO.getReportGroupID());
		
		
//		populating the FieldsList
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, newlyPurchasedItemsVO, userId);
		log.info("fieldsList from populateNewlyPurchasedItemsVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		//populating the Canned reportVO and ReportBaseVOcannedReportVO.setTimeSeriesGroupVO(timeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("26,29");
		cannedReportVO.setNewlyPurchasedItemsVO(newlyPurchasedItemsVO);
		baseVO.setCannedReportVO(cannedReportVO);		
		
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
	 */private static ReportNewlyPurchasedItemsVO constructCannedReportVO(ReportNewlyPurchasedItemsFormattingSelectionForm reportNewlyPurchasedItemsFormattingSelectionForm) throws SMORAException
		{
			if (reportNewlyPurchasedItemsVO== null)
			{
				reportNewlyPurchasedItemsVO = new ReportNewlyPurchasedItemsVO();
			}
			
			reportNewlyPurchasedItemsVO.setAccountFormat(reportNewlyPurchasedItemsFormattingSelectionForm.getAccountFormat());
			reportNewlyPurchasedItemsVO.setSortOptions(reportNewlyPurchasedItemsFormattingSelectionForm.getSortOptions());
			
			//QC-9038 - Code added to set the preferred Indicator
			reportNewlyPurchasedItemsVO.setPrefIndicator(reportNewlyPurchasedItemsFormattingSelectionForm.isPrefIndicator());
			
			return reportNewlyPurchasedItemsVO;
		}
		
		private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
		{

			if (criteriaVO == null)
			{
				criteriaVO = new CriteriaVO();
			}
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
				log.info("THE START DATE IS ::"+dateSelectionForm.getStartSelectedTime2());
				standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
	            log.info("THE END DATE IS ::"+dateSelectionForm.getEndSelectedTime2());
				timePeriodList.add(standardTimePeriodVO);
			}
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
		public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,NewlyPurchasedItemsVO newlyPurchasedItemsVO , String userId)
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

}
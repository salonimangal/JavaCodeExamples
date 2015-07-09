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
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.MarketShareVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportMarketShareByGenericNameVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.MarketShareByGenericNameFormattingSelectionForm;
import com.mckesson.smora.ui.form.ReportMarketShareForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 *
 * This Class is used to Construct the ReportBaseVO for MarketShareReport ReportGroup
 *
 */

public class ConstructReportMarketShareVO extends ConstructCannedReportBaseVO
{

	protected static Log log = LogFactory.getLog(ConstructReportMarketShareVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportMarketShareVO";

	/**
	 * The cust report criteria VO.
	 */
	private static MarketShareVO marketShareVO = null;

	private static CriteriaVO criteriaVO = null;

	private static ReportMarketShareByGenericNameVO reportMarketShareFormattingVO = null;

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
	public static ReportBaseVO populateMarketShareReportBaseVO(ReportMarketShareForm reportmarketShareForm , String userId) throws SMORAException
	{

		List<TApplCnRptFldMetaData> metaDataList = null;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		//timeSeriesGroupVO = new TimeSeriesGroupVO();
		marketShareVO = new MarketShareVO();
		criteriaVO = new CriteriaVO();

		MarketShareByGenericNameFormattingSelectionForm marketshareByGenericNameFormattingSelectionForm = reportmarketShareForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportmarketShareForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportmarketShareForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportmarketShareForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportmarketShareForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportmarketShareForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(marketshareByGenericNameFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(marketshareByGenericNameFormattingSelectionForm.getTemplate_Name());	
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(marketshareByGenericNameFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_MARKET_SHARE_GROUP);
		//TODO below logic for getting the reportsubtype can be moved to separate method
		//StringBuffer rptName = new StringBuffer("Market Share - Report ");




		baseVO.setHtml(marketshareByGenericNameFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(marketshareByGenericNameFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(marketshareByGenericNameFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(marketshareByGenericNameFormattingSelectionForm.isResultsDisplayCSV());


		String format = marketshareByGenericNameFormattingSelectionForm.getAccountFormat();
		log.info("ReportFormat: "+format);

		if(format.equals("Combine Accounts"))
		{

			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("$F{GNRC_NAM}");
			secondGroupLevelVO.setFieldName("Generic Description");
			secondGroupLevelVO.setFieldWidth(100);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);
		}

		if(format.equals("Break by Account"))
		{
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("$F{CUST_ACCT_NAM}");
			firstGroupLevelVO.setFieldName("Account Name");
			firstGroupLevelVO.setFieldWidth(100);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			secondGroupLevelVO.setClassType("java.lang.String");
			secondGroupLevelVO.setFieldDescription("$F{GNRC_NAM}");
			secondGroupLevelVO.setFieldName("Generic Description");
			secondGroupLevelVO.setFieldWidth(100);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);

		}
		/*else
		{
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setSecondGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);

		}*/
		reportMarketShareFormattingVO = constructCannedReportVO(marketshareByGenericNameFormattingSelectionForm);
		marketShareVO.setReportMarketShareByGenericNameVO(reportMarketShareFormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		//marketShareVO.setCriteriaVO(criteriaVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		//baseVO.setReportSubtype(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_ACTUAl);
		
		
		//		Added by Anil for Req. 51
		
		if (marketshareByGenericNameFormattingSelectionForm.getFormat().equals("Manufacturer Wholesale Price"))
		{
			if (reportMarketShareFormattingVO.getAccountFormat().equals("Combine Accounts"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_MWP);
			}
			else if (reportMarketShareFormattingVO.getAccountFormat().equals("Break by Account"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_MWP);
			}
		}
		
		if (marketshareByGenericNameFormattingSelectionForm.getFormat().equals("Regular Price"))
		{
			if (reportMarketShareFormattingVO.getAccountFormat().equals("Combine Accounts"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_RP);
			}
			else if (reportMarketShareFormattingVO.getAccountFormat().equals("Break by Account"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_RP);
			}
		}
		
		if (marketshareByGenericNameFormattingSelectionForm.getFormat().equals("Actual Dollars"))
		{
			if (reportMarketShareFormattingVO.getAccountFormat().equals("Combine Accounts"))
			{
			  baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_ACTUAl);
			}
			else if (reportMarketShareFormattingVO.getAccountFormat().equals("Break by Account"))
			{
				 baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_ACTUAl);
			}
		}

		if (marketshareByGenericNameFormattingSelectionForm.getFormat().equals("AWP"))
		{
			if (reportMarketShareFormattingVO.getAccountFormat().equals("Combine Accounts"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_AWP);
			}
			else if (reportMarketShareFormattingVO.getAccountFormat().equals("Break by Account"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_AWP);
			}
		}

		if (marketshareByGenericNameFormattingSelectionForm.getFormat().equals("Quantity Purchased"))
		{
			if (reportMarketShareFormattingVO.getAccountFormat().equals("Combine Accounts"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_QTY);
			}
			else if (reportMarketShareFormattingVO.getAccountFormat().equals("Break by Account"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_QTY);
			}

		}


		log.info(" Report Group ID for Report Market Share comparison report is : "+ baseVO.getReportGroupID());


//		populating the FieldsList
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, marketShareVO, userId);
		log.info("fieldsList from populateMarketShareReportBaseVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		//populating the Canned reportVO and ReportBaseVOcannedReportVO.setTimeSeriesGroupVO(timeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("31");
		cannedReportVO.setMarketShareVO(marketShareVO);
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
	 */
	private static ReportMarketShareByGenericNameVO constructCannedReportVO(MarketShareByGenericNameFormattingSelectionForm marketshareByGenericNameFormattingSelectionForm) throws SMORAException
	{
		if (reportMarketShareFormattingVO== null)
		{
			reportMarketShareFormattingVO = new ReportMarketShareByGenericNameVO();
		}

		reportMarketShareFormattingVO.setAccountFormat(marketshareByGenericNameFormattingSelectionForm.getAccountFormat());
		reportMarketShareFormattingVO.setFormat(marketshareByGenericNameFormattingSelectionForm.getFormat());

		return reportMarketShareFormattingVO;
	}

	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */

	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,MarketShareVO marketShareVO , String userId)
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
		ArrayList<TimePeriodVO> timePeriodList = new ArrayList<TimePeriodVO>();

		if (dateSelectionForm.getStartSelectedTime2() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			log.info("THE START DATE IS ::"+dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
            log.info("THE END DATE IS ::"+dateSelectionForm.getEndSelectedTime2());
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
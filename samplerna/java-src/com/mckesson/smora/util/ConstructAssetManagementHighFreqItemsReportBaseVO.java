/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.dto.AssetManagementGroupVO;
import com.mckesson.smora.dto.AssetManagementHighFreqItemsFormattingVO;
import com.mckesson.smora.dto.AssetManagementHighFreqItemsVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.AssetManagementHighFreqItemsForm;
import com.mckesson.smora.ui.form.AssetManagementHighFreqItemsFormattingSelectionForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 * 
 * This Class is used to Construct the ReportBaseVO for Asset Management High Frequency Items
 * 
 */

public class ConstructAssetManagementHighFreqItemsReportBaseVO extends ConstructCannedReportBaseVO
{
	
	protected static Log log = LogFactory.getLog(ConstructAssetManagementHighFreqItemsReportBaseVO.class);
	/**
	 * The CLASSNAME.
	 */
	private static final String CLASSNAME = "ConstructAssetManagementHighFreqItemsReportBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static CriteriaVO criteriaVO = null;
	
	private static AssetManagementHighFreqItemsFormattingVO assetManagementHighFreqItemsFormattingVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateAssetManagementHighFreqItemsReportBaseVO(AssetManagementHighFreqItemsForm assetManagementHighFreqItemsForm) throws SMORAException
	{
		ReportBaseVO baseVO = null;
		final String METHODNAME = "populateAssetManagementHighFreqItemsReportBaseVO";
		try
		{
		AssetManagementGroupVO assetManagementGroupVO = new AssetManagementGroupVO();
		AssetManagementHighFreqItemsVO assetManagementHighFreqItemsVO = new AssetManagementHighFreqItemsVO();
		criteriaVO = new CriteriaVO();
		AssetManagementHighFreqItemsFormattingSelectionForm assetManagementHighFreqItemsFormattingSelectionForm = assetManagementHighFreqItemsForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = assetManagementHighFreqItemsForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = assetManagementHighFreqItemsForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = assetManagementHighFreqItemsForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = assetManagementHighFreqItemsForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = assetManagementHighFreqItemsForm.getAdvancedFiltersForm();
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
		baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(assetManagementHighFreqItemsFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(assetManagementHighFreqItemsFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(assetManagementHighFreqItemsFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_ASSET_MANAGEMENT_HIGH_FREQUENCY_ITEMS);
		baseVO.setHtml(assetManagementHighFreqItemsFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(assetManagementHighFreqItemsFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(assetManagementHighFreqItemsFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(assetManagementHighFreqItemsFormattingSelectionForm.isResultsDisplayCSV());
		assetManagementHighFreqItemsFormattingVO = constructCannedReportVO(assetManagementHighFreqItemsFormattingSelectionForm );
		assetManagementHighFreqItemsVO.setAssetManagementHighFreqItemsFormattingVO(assetManagementHighFreqItemsFormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		//ArrayList<String> aliasFields = new ArrayList<String> ();
		//aliasFields = getFieldsList(assetManagementHighFreqItemsFormattingSelectionForm);
		
		List<TApplCnRptFldMetaData> metaDataList = null;
		ArrayList<FieldsVO> fieldsVOList = new ArrayList<FieldsVO>();
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();

		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(baseVO.getReportSubtype());

		if (metaDataList != null && metaDataList.size() > 0)
		{
			baseVO.setReportGroupID(metaDataList.get(0).getId().getRptId());
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = metaDataList.get(i);
				if (tApplCnRptFldMetaData.getIsColHdr().equals("Y"))
				{
					int fieldId = tApplCnRptFldMetaData.getId().getFldId();
					FieldsVO fieldsVO = new FieldsVO();
					fieldsVO.setFldId(fieldId);
					fieldsVOList.add(fieldsVO);
								
					if (tApplCnRptFldMetaData.getFldDscr().equals("Acct #"))
					{
						fieldsVO.setFieldName("CUST_ACCT_ID");
						fieldsList.add(fieldsVO);						
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("McK Item #"))
					{
						fieldsVO.setFieldName("ITEM_NUM");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("NDC/UPC"))
					{
						fieldsVO.setFieldName("NDC_UPC");
						fieldsList.add(fieldsVO);
					}
					
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Item Description"))
					{
						fieldsVO.setFieldName("SELL_DSCR_TXT");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Supplier Name"))
					{
						fieldsVO.setFieldName("SPLR_ACCT_NAM");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Velocity Ind"))
					{
						fieldsVO.setFieldName("VELOCITY_IND");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Avg Freq"))
					{
							fieldsVO.setFieldName("AVG_FREQUENCY");
							fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Total Qty"))
					{
						fieldsVO.setFieldName("ORDER_QTY");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Total $$"))
					{
						fieldsVO.setFieldName("TOTAL_AMT");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("Avg Price Paid"))
					{
						fieldsVO.setFieldName("AVG_PRC_PAID");
						fieldsList.add(fieldsVO);
					}
				
				}			
			}
		}
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		assetManagementGroupVO.setAssetManagementHighFreqItemsVO(assetManagementHighFreqItemsVO);
		cannedReportVO.setAssetManagementGroupVO(assetManagementGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("15,16");
		baseVO.setCannedReportVO(cannedReportVO);
			}
			catch(SMORAException e)
			{
				log.debug(e+CLASSNAME+METHODNAME);
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return baseVO;
	}
	
	/*private static String isMckessonAndNonMckesson(CriteriaVO criteriaVO)
	{
		String returnString = null;
		ItemNumbersListVO itemNumbersListVO = null;
		ItemVO itemVO = null;
		if (criteriaVO != null)
		{
			itemVO = criteriaVO.getItemVO();
			if (itemVO != null)
			{
				itemNumbersListVO = itemVO.getItemNumbersList();
			}
			// Mckesson only.
			if (itemNumbersListVO != null && itemNumbersListVO.isMckItems())
			{
				if (itemNumbersListVO != null && itemNumbersListVO.isNonMckItems())
				{
					returnString = "both";
				}
				else
				{
					returnString = "isMckesson Only";
				}
			}
			else if (itemNumbersListVO != null && itemNumbersListVO.isNonMckItems())
			{
				returnString = "isNonMckesson Only";
			}
		}
		return returnString;
	}*/
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param AssetManagementHighFreqItemsFormattingSelectionForm
	 *
	 * @return assetManagementHighFreqItemsFormattingVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private static AssetManagementHighFreqItemsFormattingVO constructCannedReportVO(AssetManagementHighFreqItemsFormattingSelectionForm assetManagementHighFreqItemsFormattingSelectionForm) throws SMORAException
	{
		final String METHODNAME = "AssetManagementHighFreqItemsFormattingVO";
		try
		{
		if (assetManagementHighFreqItemsFormattingVO== null)
		{
			assetManagementHighFreqItemsFormattingVO = new AssetManagementHighFreqItemsFormattingVO();
		}
		
		assetManagementHighFreqItemsFormattingVO.setMonthlyOrderfreqmin(assetManagementHighFreqItemsFormattingSelectionForm.getMonthlyOrderfreqmin());
		assetManagementHighFreqItemsFormattingVO.setSortOptions(assetManagementHighFreqItemsFormattingSelectionForm.getSortOptions());
		return assetManagementHighFreqItemsFormattingVO;
		}
		catch(Exception e)
		{
			throw new SMORAException(e+CLASSNAME+METHODNAME);
		}
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

		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
		dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();
		
		if ((dateSelectionForm.getStartSelectedTime1() != null) && (dateSelectionForm.getEndSelectedTime1() != null))
		{
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			comparePeriod1TimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime1());
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime1());
			timePeriodList.add(comparePeriod1TimePeriodVO);
		}
	/*	if (dateSelectionForm.getStartCompareTime21() != null)
		{
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			comparePeriod2TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime21());
			comparePeriod2TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime21());
			timePeriodList.add(comparePeriod2TimePeriodVO);
		}
	*/
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
	
	
	public static ArrayList<String> getFieldsList(AssetManagementHighFreqItemsFormattingSelectionForm assetManagementHighFreqItemsFormattingSelectionForm)
	{
		ArrayList<String> fieldsList = new ArrayList<String> ();
		
			fieldsList.add("CUST_ACCT_ID");
			fieldsList.add("ITEM_NUM");
			fieldsList.add("NDC_UPC");
			fieldsList.add("SELL_DSCR_TXT");
			fieldsList.add("SPLR_ACCT_NAM");
			fieldsList.add("VELOCITY_IND");
			fieldsList.add("AVG_FREQUENCY");
			fieldsList.add("ORDER_QTY");
			fieldsList.add("TOTAL_AMT");
			fieldsList.add("AVG_PRC_PAID");
		return fieldsList;
	}

}

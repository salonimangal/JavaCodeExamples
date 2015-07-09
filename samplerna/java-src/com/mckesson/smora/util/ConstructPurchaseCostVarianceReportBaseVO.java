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
import com.mckesson.smora.database.util.TSTPComparisonQueryBuilder;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.PurchaseCostVarianceFormattingVO;
import com.mckesson.smora.dto.PurchaseCostVarianceVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.TSTPFormattingVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.TimeSeriesGroupVO;
import com.mckesson.smora.dto.TimeSeriesTPComparisonVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.PurchaseCostVarianceForm;
import com.mckesson.smora.ui.form.PurchaseCostVarianceFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.TimeSeriesTPComparisonForm;
import com.mckesson.smora.ui.form.TimeSeriesTPComparisonFormattingSelectionForm;

/**
 * 
 * This Class is used to Construct the ReportBaseVO for 8020GenericCategory ReportGroup 
 * 
 */

public class ConstructPurchaseCostVarianceReportBaseVO extends ConstructCannedReportBaseVO
{
	
	protected static Log log = LogFactory.getLog(ConstructPurchaseCostVarianceReportBaseVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructPurchaseCostVarianceReportBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static PurchaseCostVarianceVO purchaseCostVarianceVO = null;
	
	private static CriteriaVO criteriaVO = null;
	
	private static PurchaseCostVarianceFormattingVO purchaseCostVarianceFormattingVO = null;
	
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
	public static ReportBaseVO populatePurchaseCostVarianceReportBaseVO(PurchaseCostVarianceForm purchaseCostVarianceForm , String userId) throws SMORAException
	{
		//timeSeriesGroupVO = new TimeSeriesGroupVO();
		purchaseCostVarianceVO = new PurchaseCostVarianceVO();
		criteriaVO = new CriteriaVO();
		int noOfAccounts = 0;
		PurchaseCostVarianceFormattingSelectionForm purchaseCostVarianceFormattingSelectionForm = purchaseCostVarianceForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = purchaseCostVarianceForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = purchaseCostVarianceForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = purchaseCostVarianceForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = purchaseCostVarianceForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = purchaseCostVarianceForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(purchaseCostVarianceFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(purchaseCostVarianceFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(purchaseCostVarianceFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.TIMESERIES_TP_COMPARISON);
		//TODO below logic for getting the reportsubtype can be moved to separate method
		StringBuffer rptName = new StringBuffer("Time Series - Timeperiod Comparison - ");
		//if (purchaseCostVarianceFormattingSelectionForm.getDetailLevel().equals("Full Detail"))
		//{
			
			/*if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Generic"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_GENERIC);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Therapeutic"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_THERA);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Item"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_ITM);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Local Department"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_LCL_DEPT);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Ordering Department"))
			{*/
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_GENERIC);
			//}
		//}
		//else
		//{			
			/*if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Generic"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_GENERIC);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Therapeutic"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_THERA);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Item"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_ITM);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Local Department"))
			{
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_LCL_DEPT);
			}
			else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Ordering Department"))
			{*/
				baseVO.setReportGroupID(ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_GENERIC);
			//}
		//}
		
		baseVO.setHtml(purchaseCostVarianceFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(purchaseCostVarianceFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(purchaseCostVarianceFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(purchaseCostVarianceFormattingSelectionForm.isResultsDisplayCSV());

		purchaseCostVarianceFormattingVO = constructCannedReportVO(purchaseCostVarianceFormattingSelectionForm);
		purchaseCostVarianceVO.setPurchaseCostVarianceFormattingVO(purchaseCostVarianceFormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		//timeSeriesTPComparisonVO.setCriteriaVO(criteriaVO);	
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		//ArrayList<String> aliasFields = new ArrayList<String> ();
		//aliasFields = getFieldsList(timeSeriesTPComparisonFormattingSelectionForm);
		
		List<TApplCnRptFldMetaData> metaDataList = null;
		//ArrayList<FieldsVO> fieldsVOList = new ArrayList<FieldsVO>();
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		if (purchaseCostVarianceFormattingVO.getAccountFormat().equals("Break"))
		{
			   noOfAccounts = getNumberOfAccounts(criteriaVO.getCustomerVO() , userId);
		}
		
		//TODO The following logic of populating the FieldsVO need to be shifted to DAO.
	
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.debug("metaDataList \n" +metaDataList);
		if (metaDataList != null && metaDataList.size() > 0)
		{/*
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
					//fieldsVOList.add(fieldsVO);
					//For Generic
					if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Generic"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Generic Code"))
						{
							fieldsVO.setFieldName("Generic_Code");
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Generic Description"))
						{
							fieldsVO.setFieldName("Description");
							fieldsList.add(fieldsVO);
						}
					}
					//For Therapeutic
					else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Therapeutic"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Therapeutic Code"))
						{
							fieldsVO.setFieldName("Therapeutic_Code");
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Therapeutic Description"))
						{
							fieldsVO.setFieldName("Description");
							fieldsList.add(fieldsVO);
						}
					}
					//For Item
					else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Item"))
					{
						 if (tApplCnRptFldMetaData.getFldDscr().equals("McKesson Item #"))
						{
							fieldsVO.setFieldName("Item_Code");
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Item Description"))
						{
								fieldsVO.setFieldName("Description");
								fieldsList.add(fieldsVO);
						}
					}
					//For Local Dept
					else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Local Department"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Local Department"))
						{
							fieldsVO.setFieldName("Local_Department");
							fieldsList.add(fieldsVO);
						}
					}
					else if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Ordering Department"))
					{
						if (tApplCnRptFldMetaData.getFldDscr().equals("Dept. ID"))
						{
							fieldsVO.setFieldName("Order_Department");
							fieldsList.add(fieldsVO);
						}
						else if (tApplCnRptFldMetaData.getFldDscr().equals("Ordering Department Name"))
						{
								fieldsVO.setFieldName("Description");
								fieldsList.add(fieldsVO);
						}
						
					}
				}
			}
			
			if (timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel().equals("Change Only"))
			{
				if (tSTPFormattingVO.getAccountFormat().equals("Break"))
				{
					for (int j = 1; j <= noOfAccounts; j++)
					{
						for (int i=0; i < metaDataListSize; i++)
						{
							tApplCnRptFldMetaData = metaDataList.get(i);
							int fieldId = tApplCnRptFldMetaData.getId().getFldId();
							FieldsVO fieldsVO = new FieldsVO();
							fieldsVO.setFldId(fieldId);					
							if (tApplCnRptFldMetaData.getFldDscr().equals("Account Number"))
							{
								fieldsVO.setFieldName("CUST_ACCT_ID_"+ j);
								fieldsList.add(fieldsVO);						
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("Account Name"))
							{
								fieldsVO.setFieldName("CUST_ACCT_NAM_"+ j);
								fieldsList.add(fieldsVO);						
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("$ Diff"))
							{
								fieldsVO.setFieldName("DIFF_"+ j);
								fieldsList.add(fieldsVO);
							}					 
						}
					}
				}
				//else
				//{
					for (int i=0; i < metaDataListSize; i++)
					{
						tApplCnRptFldMetaData = metaDataList.get(i);
						int fieldId = tApplCnRptFldMetaData.getId().getFldId();
						FieldsVO fieldsVO = new FieldsVO();
						fieldsVO.setFldId(fieldId);					
						if (tApplCnRptFldMetaData.getFldDscr().equals("$ Diff"))
						{
							fieldsVO.setFieldName("DIFF_TOT");
							fieldsList.add(fieldsVO);
							break;
						}	
					}					
				//}			
			}		
			//For Full Details
			if (timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel().equals("Full Detail")) 
			{
				if (tSTPFormattingVO.getAccountFormat().equals("Break"))
				{
					for (int j = 1; j <= noOfAccounts; j++)
					{
						for (int i=0; i < metaDataListSize; i++)
						{
							tApplCnRptFldMetaData = metaDataList.get(i);
							int fieldId = tApplCnRptFldMetaData.getId().getFldId();
							FieldsVO fieldsVO = new FieldsVO();
							fieldsVO.setFldId(fieldId);					
							if (tApplCnRptFldMetaData.getFldDscr().equals("Account Number"))
							{
								fieldsVO.setFieldName("CUST_ACCT_ID_"+ j);
								fieldsList.add(fieldsVO);						
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("Account Name"))
							{
								fieldsVO.setFieldName("CUST_ACCT_NAM_"+ j);
								fieldsList.add(fieldsVO);						
							}
							
							else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 1) Net $$"))
							{
								fieldsVO.setFieldName("PER1_NET_AMT_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("PER1_ON_CNTRC_AMT"))
							{
								fieldsVO.setFieldName("PER1_ON_CNTRC_AMT_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 1) % on Contract"))
							{
								fieldsVO.setFieldName("PER1_PCT_ON_CNTRC_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 2) Net $$"))
							{
								fieldsVO.setFieldName("PER2_NET_AMT_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("PER2_ON_CNTRC_AMT"))
							{
								fieldsVO.setFieldName("PER2_ON_CNTRC_AMT_"+ j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 2) % on Contract"))
							{
								fieldsVO.setFieldName("PER2_PCT_ON_CNTRC_"+ j);
								fieldsList.add(fieldsVO);
							}
							
							else if (tApplCnRptFldMetaData.getFldDscr().equals("$ Diff"))
							{
								fieldsVO.setFieldName("DIFF_" + j);
								fieldsList.add(fieldsVO);
							}
							else if (tApplCnRptFldMetaData.getFldDscr().equals("% Change Net $$"))
							{
								fieldsVO.setFieldName("PCT_CHNG_NET_" + j);
								fieldsList.add(fieldsVO);
							}
						}
					}
				}
								
				for (int i=0; i < metaDataListSize; i++)
				{
					tApplCnRptFldMetaData = metaDataList.get(i);
					int fieldId = tApplCnRptFldMetaData.getId().getFldId();
					FieldsVO fieldsVO = new FieldsVO();
					fieldsVO.setFldId(fieldId);
					if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 1) Net $$"))
					{
						fieldsVO.setFieldName("PER1_NET_AMT_TOT");
						fieldsList.add(fieldsVO);
					}			
					else if (tApplCnRptFldMetaData.getFldDscr().equals("PER1_ON_CNTRC_AMT"))
					{
						fieldsVO.setFieldName("PER1_ON_CNTRC_AMT_TOT");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 1) % on Contract"))
					{
						fieldsVO.setFieldName("PER1_PCT_ON_CNTRC_TOT");
						fieldsList.add(fieldsVO);
					}				
					else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 2) Net $$"))
					{
						fieldsVO.setFieldName("PER2_NET_AMT_TOT");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("PER2_ON_CNTRC_AMT"))
					{
						fieldsVO.setFieldName("PER2_ON_CNTRC_AMT_TOT");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("(Time Period 2) % on Contract"))
					{
						fieldsVO.setFieldName("PER2_PCT_ON_CNTRC_TOT");
						fieldsList.add(fieldsVO);
					}			
					
					else if (tApplCnRptFldMetaData.getFldDscr().equals("$ Diff"))
					{
						fieldsVO.setFieldName("DIFF_TOT");
						fieldsList.add(fieldsVO);
					}
					else if (tApplCnRptFldMetaData.getFldDscr().equals("% Change Net $$"))
					{
						fieldsVO.setFieldName("PCT_CHNG_NET_TOT");
						fieldsList.add(fieldsVO);
					}			
				}
			}
		*/}
		
		log.debug("fieldsList from populateTSTPReportBaseVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		
		cannedReportVO.setPurchaseCostVarianceVO(purchaseCostVarianceVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param report8020GenericCategoryFormattingSelectionForm the report8020formatting selection form
	 *
	 * @return report8020GenericCategoryVO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private static PurchaseCostVarianceFormattingVO constructCannedReportVO(PurchaseCostVarianceFormattingSelectionForm purchaseCostVarianceFormattingSelectionForm) throws SMORAException
	{
		if (purchaseCostVarianceFormattingVO== null)
		{
			purchaseCostVarianceFormattingVO = new PurchaseCostVarianceFormattingVO();
		}
		
		purchaseCostVarianceFormattingVO.setAccountFormat(purchaseCostVarianceFormattingSelectionForm.getAccountFormat());
		purchaseCostVarianceFormattingVO.setFormat(purchaseCostVarianceFormattingSelectionForm.getFormat());
		purchaseCostVarianceFormattingVO.setDetailLevel(purchaseCostVarianceFormattingSelectionForm.getDetailLevel());
		purchaseCostVarianceFormattingVO.setSortOptions(purchaseCostVarianceFormattingSelectionForm.getSortOptions());
		return purchaseCostVarianceFormattingVO;
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
		
		if (dateSelectionForm.getStartCompareTime11() != null)
		{
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			comparePeriod1TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime11());
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime11());
			timePeriodList.add(comparePeriod1TimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime21() != null)
		{
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			comparePeriod2TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime21());
			comparePeriod2TimePeriodVO.setEndDate(dateSelectionForm.getEndCompareTime21());
			timePeriodList.add(comparePeriod2TimePeriodVO);
		}
		timePeriodListVO.setTimeperiodVOList(timePeriodList);

		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);
		
		return dateSelectionAndComparisonVO;
	}
	
	
	/*public static ArrayList<String> getFieldsList(TimeSeriesTPComparisonFormattingSelectionForm timeSeriesTPComparisonFormattingSelectionForm)
	{
		ArrayList<String> fieldsList = new ArrayList<String> ();
		if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Generic"))
		{
			fieldsList.add("Generic_Code");
			fieldsList.add("Description");
		}
		if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Therapeutic"))
		{
			fieldsList.add("Therapeutic_Code");
			fieldsList.add("Description");
		}
		if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Item"))
		{
			fieldsList.add("Item_Code");
			fieldsList.add("Description");
		}
		if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Local Department"))
		{
			fieldsList.add("Local_Department");
			fieldsList.add("Description");
		}
		if (timeSeriesTPComparisonFormattingSelectionForm.getFormat().equals("Ordering Department"))
		{
			fieldsList.add("Order_Department");
			fieldsList.add("Description");
		}
		
		if (timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel().equals("Change Only"))
		{
			fieldsList.add("DIFF_TOT");
		}
		else if (timeSeriesTPComparisonFormattingSelectionForm.getDetailLevel().equals("Full Detail"))
		{
			
			fieldsList.add("PER1_NET_AMT_TOT");
			fieldsList.add("PER1_ON_CNTRC_AMT_TOT");
			fieldsList.add("PER1_PCT_ON_CNTRC_TOT");
			fieldsList.add("PER2_NET_AMT_TOT");
			fieldsList.add("PER2_ON_CNTRC_AMT_TOT");
			fieldsList.add("PER2_PCT_ON_CNTRC_TOT");
			fieldsList.add("DIFF_TOT");
			fieldsList.add("PCT_CHNG_NET_TOT");
		}
		log.debug("Field List from getFieldsList method : \n " + fieldsList);
		return fieldsList;
	}*/

	//TODO This method is already implemented in the DAO.Need to move it to a common place.
	public static int getNumberOfAccounts(CustomerVO customerVO, String userId)
	{
		log.info("Begin: getNumberOfAccounts");
		int noOfAccnts = 0;
		ArrayList<AccountDetailsVO> accountDetailsVO =  null;
		UserRolesVO userRolesVO = customerVO.getRoles();
		// TODO Logic to 
		if (userRolesVO != null)
		{
			log.info("Getting the number of accounts from UserRolesVO");
			ArrayList<RoleVO> cidRoles = userRolesVO.getCidRoles();						
			ArrayList<RoleVO> hspRoles = userRolesVO.getHspRoles();						
			ArrayList<RoleVO> chnRoles = userRolesVO.getChnRoles();						
			ArrayList<RoleVO> slsRoles = userRolesVO.getSlsRoles();
			if ((cidRoles != null && cidRoles.size() > 0) || 
				(hspRoles != null && hspRoles.size() > 0) || 
				(chnRoles != null && chnRoles.size() > 0) || 
				(slsRoles != null && slsRoles.size() > 0)) 
			{ 
				AccountDB accountDB = new AccountDB();
				try{
					accountDetailsVO =  accountDB.getAccountInfo(userRolesVO); //TODO needs to confirm on rowlimits parameter
				}catch(SMORAException e)
				{
					
					noOfAccnts = 0; //TODO this needs to be verified again
				}
				if(accountDetailsVO != null & accountDetailsVO.size() > 0)
				{
					noOfAccnts = accountDetailsVO.size();
				}
			}	
			else
			{
				log.info("Getting the number of accounts from AccountDetailsListVO");
				AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
				accountDetailsVO = accountDetailsListVO.getAccountDetailsVOList();
				if(accountDetailsVO != null)
				{
					noOfAccnts = accountDetailsVO.size();
				}
				
			}
			
		}
		else
		{
			log.info("Getting the number of accounts from AccountDetailsListVO");
			AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
			accountDetailsVO = accountDetailsListVO.getAccountDetailsVOList();
			if(accountDetailsVO != null)
			{
				noOfAccnts = accountDetailsVO.size();
			}

		}
		
		if (noOfAccnts <= 0)
		{
			try
			{
				TSTPComparisonQueryBuilder comparisonQueryBuilder = new TSTPComparisonQueryBuilder();
				List<String> accountList = comparisonQueryBuilder.getDefaultAccountsValues(userId);
				if (accountList != null)
				{
					noOfAccnts = accountList.size();
				}	
				else
				{
					noOfAccnts =0;
				}	
			}
			catch (SMORAException sme)
			{				
				log.error("Exception in getNumberOfAccounts() : " + sme);
			}			
		}
		log.info("Number of Accounts = " + noOfAccnts);
		if (noOfAccnts > 10)
		{
			log.info("Number of Accounts is greater than 10");
			noOfAccnts = 10;
		}
		log.debug("No of Accounts from getNumberOfAccounts method : \n " + noOfAccnts);
		log.info("End: getNumberOfAccounts");
		return noOfAccnts;
	}
}
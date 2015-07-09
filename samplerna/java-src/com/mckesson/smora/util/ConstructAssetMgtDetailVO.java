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
import com.mckesson.smora.dto.AssetManagement12MonthInventoryTurnsForecastDetailVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.AssetMgt12MonthInventoryDetailVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.AssetManagement12MonthInventoryTurnsForecastDetailForm;
import com.mckesson.smora.ui.form.AssetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm;
import com.mckesson.smora.ui.form.MarketShareByGenericNameFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;


public class ConstructAssetMgtDetailVO extends ConstructCannedReportBaseVO
{

	protected static Log log = LogFactory.getLog(ConstructAssetMgtDetailVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructAssetMgtDetailVO";

	/**
	 * The cust report criteria VO.
	 */
	private static AssetMgt12MonthInventoryDetailVO assetMgt12MonthInventoryDetailVO = null;

	private static CriteriaVO criteriaVO = null;

	private static AssetManagement12MonthInventoryTurnsForecastDetailVO assetManagement12MonthInventoryTurnsForecastDetailVO = null;

	

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	
	public static ReportBaseVO populateAssetMgtDetailReportBaseVO(AssetManagement12MonthInventoryTurnsForecastDetailForm assetManagement12MonthInventoryTurnsForecastDetailForm , String userId,String reportName) throws SMORAException
	{

		List<TApplCnRptFldMetaData> metaDataList = null;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		ArrayList<String> subTotalArray = new ArrayList();
		ArrayList<String> fieldDescArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		assetMgt12MonthInventoryDetailVO = new AssetMgt12MonthInventoryDetailVO();
		criteriaVO = new CriteriaVO();

		AssetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm = assetManagement12MonthInventoryTurnsForecastDetailForm.getFormattingSelectionForm();
	//	DateSelectionForm dateSelectionForm = assetManagement12MonthInventoryTurnsForecastDetailForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = assetManagement12MonthInventoryTurnsForecastDetailForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = assetManagement12MonthInventoryTurnsForecastDetailForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = assetManagement12MonthInventoryTurnsForecastDetailForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = assetManagement12MonthInventoryTurnsForecastDetailForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box 
		if(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
	



		baseVO.setHtml(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.isResultsDisplayCSV());


			
		assetManagement12MonthInventoryTurnsForecastDetailVO = constructCannedReportVO(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm);
		assetMgt12MonthInventoryDetailVO.setAssetManagement12MonthInventoryTurnsForecastDetailVO(assetManagement12MonthInventoryTurnsForecastDetailVO);
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		if(reportName.equalsIgnoreCase("AMdetail"))
		{
		baseVO.setReportSubtype(ReportManagerConstants.FORECASE_REPORTS_DETAIL );
		baseVO.setReportGroupID(ReportManagerConstants.FORECASE_REPORTS_DETAIL );
		//subTotalArray.add("Est Annual $$");
		//subTotalArray.add("Theoretical Stock $$");
		//subTotalArray.add("Est Turns / Yr");
		
		fieldDescArray.add("Est Annual $$");
		fieldDescArray.add("Theoretical Stock $$");
		fieldDescArray.add("Est Turns / Yr");
		
		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		firstGroupLevelVO.setClassType("java.lang.String");
		firstGroupLevelVO.setFieldDescription("$F{Accont_Name}");
		firstGroupLevelVO.setFieldName("Account_Num");
		firstGroupLevelVO.setFieldWidth(52);
		firstGroupLevelVO.setSubTotal(fieldDescArray);
		//firstGroupLevelVO.setSubTotal(fieldDescArray);
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(null);
		groupLevelVO.setThirdGroupLevel(null);
		
		
		
		
		
		
		
		}
		else 
		{
			baseVO.setReportSubtype(ReportManagerConstants.FORECASE_REPORTS_SUMMARY);
			baseVO.setReportGroupID(ReportManagerConstants.FORECASE_REPORTS_SUMMARY);
		}
		log.info(" Report Group ID for Report Asset Mgt Detail report is : "+ baseVO.getReportGroupID());

		//metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		//log.info("metaDataList \n" +metaDataList);
		//log.info("fieldsList from populateAssetMgtDetailReportBaseVO method : \n " + fieldsList);
		//fieldsListVO.setFieldsVOList(fieldsList);
		//cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setAssetMgt12MonthInventoryDetailVO(assetMgt12MonthInventoryDetailVO);
		baseVO.setCannedReportVO(cannedReportVO);

		return baseVO;
	}
	
	
	private static AssetManagement12MonthInventoryTurnsForecastDetailVO constructCannedReportVO(AssetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm) throws SMORAException
	{
		if (assetManagement12MonthInventoryTurnsForecastDetailVO== null)
		{
			assetManagement12MonthInventoryTurnsForecastDetailVO = new AssetManagement12MonthInventoryTurnsForecastDetailVO();
		}
		
			assetManagement12MonthInventoryTurnsForecastDetailVO.setSortOptions(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getSortOptions());
		
	
		assetManagement12MonthInventoryTurnsForecastDetailVO.setSelection(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getSelection());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setMonths(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getMonths());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setDolrVolm(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getDolrVolm());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setDay(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getDay());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setLastXMonths(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getLastXMonths());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setLastXXMonths(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getLastXXMonths());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setRopA(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getRopA());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setRopB(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getRopB());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setRopC(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getRopC());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setRoqA(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getRoqA());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setRoqB(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getRoqB());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setRoqC(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getRoqC());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setVelocityA(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getVelocityA());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setVelocityB(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getVelocityB());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setVelocityC(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getVelocityC());
		assetManagement12MonthInventoryTurnsForecastDetailVO.setIncludeCurrentMonth(assetManagement12MonthInventoryTurnsForecastDetailFormattingSelectionForm.getIncludeCurrentMonth());
		
		return assetManagement12MonthInventoryTurnsForecastDetailVO;
	}

	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	
}
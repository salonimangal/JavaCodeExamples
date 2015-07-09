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
import com.mckesson.smora.dto.ItemGroupVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportItemInformationVO;
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
import com.mckesson.smora.ui.form.ReportItemInformationForm;
import com.mckesson.smora.ui.form.ReportItemInformationFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.TimeSeriesTPComparisonForm;
import com.mckesson.smora.ui.form.TimeSeriesTPComparisonFormattingSelectionForm;

/**
 * 
 * This Class is used to Construct the ReportBaseVO for Time Series  - Time Period Comparison Reports
 * 
 */

public class ConstructReportItemInformationReportBaseVO extends ConstructCannedReportBaseVO
{
	
	protected static Log log = LogFactory.getLog(ConstructReportItemInformationReportBaseVO.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportItemInforamtionReportBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static ReportItemInformationVO reportItemInformationVO = null;
	
	private static CriteriaVO criteriaVO = null;
		
	private static ItemGroupVO itemGroupVO = null;

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReportItemInformationReportBaseVO(ReportItemInformationForm reportItemInformationForm , String userId) throws SMORAException
	{
		List<TApplCnRptFldMetaData> metaDataList = null;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		itemGroupVO = new ItemGroupVO();
		reportItemInformationVO = new ReportItemInformationVO();
		criteriaVO = new CriteriaVO();
		
		ReportItemInformationFormattingSelectionForm reportItemInformationFormattingSelectionForm = reportItemInformationForm.getFormattingSelectionForm();
		
		ItemSelectionForm itemSelectionForm = reportItemInformationForm.getItemSelectionForm();		
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportItemInformationFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportItemInformationFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportItemInformationFormattingSelectionForm.getCustomHeading());
		baseVO.setHtml(reportItemInformationFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportItemInformationFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportItemInformationFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportItemInformationFormattingSelectionForm.isResultsDisplayCSV());
		reportItemInformationVO.setUrl(reportItemInformationFormattingSelectionForm.getUrl());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_ITEM_INFORMATION);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_ITEM_INFORMATION);
		
		itemGroupVO.setReportItemInformationVO(reportItemInformationVO);		
		
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		//populating the FieldsList
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, userId);
		log.info("fieldsList from populateTSTPReportBaseVO method : \n " + fieldsList);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		
		//populating the Canned reportVO and ReportBaseVO
		cannedReportVO.setItemGroupVO(itemGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle(ReportManagerConstants.REPORT_ITEM_INFORMATION + "");
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
	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList , String userId)
	{
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		
		if (metaDataList != null && metaDataList.size() > 0)
		{
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
				if (tApplCnRptFldMetaData.getIsColHdr().equals("Y"))
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
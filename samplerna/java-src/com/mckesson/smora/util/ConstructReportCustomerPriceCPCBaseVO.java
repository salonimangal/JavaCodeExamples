/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportCustomerPriceCPCFormattingVO;


import com.mckesson.smora.dto.ReportCustomerPriceCPCVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;

import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.CustomerPriceCPCForm;
import com.mckesson.smora.ui.form.CustomerPriceCPCFormattingSelectionForm;


public class ConstructReportCustomerPriceCPCBaseVO extends
ConstructCannedReportBaseVO{

	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportCustomerPriceCPCBaseVO";

	/**
	 * The report Customer Price CPC VO
	 */
	private static ReportCustomerPriceCPCVO reportCustomerPriceCPCVO = null;
	/**
	 * The cpc Formatting VO
	 */
	private static ReportCustomerPriceCPCFormattingVO cpcFormattingVO = null;
	/**
	 * The criteria VO
	 */
	private static CriteriaVO criteriaVO = null;
	/**
	 * The canned Report Criteria VO
	 */
	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;

	/**
	 * This method populates the Report Customer Price CPC Base VO
	 *
	 * @param customReportingForm the custom reporting form
	 * @return the report base VO
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReportCustomerPriceCPCBaseVO(CustomerPriceCPCForm customerPriceCPCForm) throws SMORAException
	{
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		reportCustomerPriceCPCVO = new ReportCustomerPriceCPCVO();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		CustomerPriceCPCFormattingSelectionForm customerPriceCPCFormattingSelectionForm = customerPriceCPCForm.getCustomerPriceCPCFormattingSelectionForm();

		CustomerSelectionForm customerSelectionForm = customerPriceCPCForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = customerPriceCPCForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = customerPriceCPCForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = customerPriceCPCForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		ReportCustomerPriceCPCVO reportCustomerPriceCPCVO = new ReportCustomerPriceCPCVO();

		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(customerPriceCPCFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(customerPriceCPCFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(customerPriceCPCFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_CUSTOMER_PRICE_CPC);
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_CUSTOMER_PRICE_CPC);
		baseVO.setHtml(customerPriceCPCFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(customerPriceCPCFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(customerPriceCPCFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(customerPriceCPCFormattingSelectionForm.isResultsDisplayCSV());

		String allowedvariance = customerPriceCPCFormattingSelectionForm.getAllowedVariance();

		cpcFormattingVO = constructCannedReportVO(customerPriceCPCFormattingSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		if(advancedFiltersForm!= null){
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		}
		reportCustomerPriceCPCVO.setReportCustomerPriceCPCFormattingVO(cpcFormattingVO);
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
		cannedReportVO.setReportCustomerPriceCPCVO(reportCustomerPriceCPCVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("21,23");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
	
		baseVO.setCannedReportVO(cannedReportVO);


		return baseVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param reportCustomerPriceCPCFormattingSelectionForm
	 *            the reportCustomerPriceCPCFormatting selection form
	 * @return reportCustomerPriceCPCVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	private static ReportCustomerPriceCPCFormattingVO constructCannedReportVO(CustomerPriceCPCFormattingSelectionForm customerPriceCPCFormattingSelectionForm) throws SMORAException
	{
		if (cpcFormattingVO == null)
		{
			cpcFormattingVO = new ReportCustomerPriceCPCFormattingVO();
		}

		String allowedvariance = customerPriceCPCFormattingSelectionForm.getAllowedVariance();
		String sortoptions=customerPriceCPCFormattingSelectionForm.getSortOptions();

		cpcFormattingVO.setAllowedVariance(allowedvariance);
		cpcFormattingVO.setSortOptions(sortoptions);
		cpcFormattingVO.setIncludeSectionsPricesDiffer(customerPriceCPCFormattingSelectionForm.isIncludeSectionsPricesDiffer());
		cpcFormattingVO.setIncludeSectionsNoCustomerPrice(customerPriceCPCFormattingSelectionForm.isIncludeSectionsNoCustomerPrice());
		cpcFormattingVO.setIncludeSectionsNoContractBidPrice(customerPriceCPCFormattingSelectionForm.isIncludeSectionsNoContractBidPrice());
		return cpcFormattingVO;
	}




}

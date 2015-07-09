 /* Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.dto.AssetManagementGroupVO;
import com.mckesson.smora.dto.AssetManagementObsoleteInventoryVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.AssetManagementObsoleteInventoryForm;
import com.mckesson.smora.ui.form.AssetManagementObsoleteInventoryFormattingSelectionForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 * 
 * This Class is used to Construct the ReportBaseVO for 8020GenericCategory ReportGroup 
 * 
 */

public class ConstructAssetManagementObsoleteInventoryReportBaseVO extends ConstructCannedReportBaseVO
{
	
	
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructAssetManagementObsoleteInventoryReportBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static CriteriaVO criteriaVO = null;
	private static AssetManagementGroupVO assetManagementGroupVO = null;
	private static AssetManagementObsoleteInventoryVO assetManagementObsoleteInventoryVO = null;
	
	
	

	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateAssetManagementObsoleteInventoryReportBaseVO(AssetManagementObsoleteInventoryForm assetManagementObsoleteInventoryForm) throws SMORAException
	{
		
		assetManagementGroupVO = new AssetManagementGroupVO();
		assetManagementObsoleteInventoryVO = new AssetManagementObsoleteInventoryVO();
		criteriaVO = new CriteriaVO();
		AssetManagementObsoleteInventoryFormattingSelectionForm assetManagementObsoleteInventoryFormattingSelectionForm = assetManagementObsoleteInventoryForm.getAssetManagementObsoleteInventoryFormattingSelectionForm();
		CustomerSelectionForm customerSelectionForm = assetManagementObsoleteInventoryForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = assetManagementObsoleteInventoryForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = assetManagementObsoleteInventoryForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = assetManagementObsoleteInventoryForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(assetManagementObsoleteInventoryFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(assetManagementObsoleteInventoryFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(assetManagementObsoleteInventoryFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.ASSETMANAGEMENT_OBSOLETE_INVENTORY);
		baseVO.setReportGroupID(ReportManagerConstants.ASSETMANAGEMENT_OBSOLETE_INVENTORY);
		baseVO.setHtml(assetManagementObsoleteInventoryFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(assetManagementObsoleteInventoryFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(assetManagementObsoleteInventoryFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(assetManagementObsoleteInventoryFormattingSelectionForm.isResultsDisplayCSV());

		assetManagementObsoleteInventoryVO = constructCannedReportVO(assetManagementObsoleteInventoryFormattingSelectionForm);
		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		cannedReportCriteriaVO.setGroupLevelVO(null);//No grouping applied.
		cannedReportVO.setCannedReportTitle("17");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		assetManagementGroupVO.setAssetManagementObsoleteInventoryVO(assetManagementObsoleteInventoryVO);
		cannedReportVO.setAssetManagementGroupVO(assetManagementGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		baseVO.setCannedReportVO(cannedReportVO);
		
		
		return baseVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 * @param assetManagementObsoleteInventoryFormattingSelectionForm
	 * @return
	 * @throws SMORAException
	 */
	private static AssetManagementObsoleteInventoryVO constructCannedReportVO(AssetManagementObsoleteInventoryFormattingSelectionForm assetManagementObsoleteInventoryFormattingSelectionForm) throws SMORAException
	{
		if (assetManagementObsoleteInventoryVO== null)
		{
			assetManagementObsoleteInventoryVO = new AssetManagementObsoleteInventoryVO();
		}
		assetManagementObsoleteInventoryVO.setItemsNotPurchasedSince(assetManagementObsoleteInventoryFormattingSelectionForm.getItemsnotPurchasedsince());
		assetManagementObsoleteInventoryVO.setSortOptions(assetManagementObsoleteInventoryFormattingSelectionForm.getSortOptions());		
		return assetManagementObsoleteInventoryVO;
	}
	
}

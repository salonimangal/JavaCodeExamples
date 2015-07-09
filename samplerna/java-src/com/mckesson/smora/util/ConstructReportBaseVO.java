/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.appl.util.ReportsProcessorUtil;
import com.mckesson.smora.common.PurchaseDrillDownReportConstants;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.common.SMOConstant;
import com.mckesson.smora.database.dao.CannedReportDB;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.dao.CustomReportDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.database.model.TCustAcct;
import com.mckesson.smora.database.model.TNatCodingLkup;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.AccountListVO;
import com.mckesson.smora.dto.AccountVO;
import com.mckesson.smora.dto.AdvancedFiltersVO;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.ContractLeadNumListVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustReportCriteriaVO;
import com.mckesson.smora.dto.CustomReportVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.DosageFormsListVO;
import com.mckesson.smora.dto.DrugScheduleListVO;
import com.mckesson.smora.dto.DrugScheduleVO;
import com.mckesson.smora.dto.ExcludeContractLeadNumListVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FilterListVO;
import com.mckesson.smora.dto.FiltersVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.FrontEdgeListVO;
import com.mckesson.smora.dto.FrontEdgeSubCategoryListVO;
import com.mckesson.smora.dto.FrontEdgeSubCategoryVO;
import com.mckesson.smora.dto.FrontEdgeVO;
import com.mckesson.smora.dto.GenericListVO;
import com.mckesson.smora.dto.GenericVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.IDBAccountListVO;
import com.mckesson.smora.dto.IDBAccountVO;
import com.mckesson.smora.dto.ItemDetailsVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.LabelerCodesListVO;
import com.mckesson.smora.dto.LocalDeptListVO;
import com.mckesson.smora.dto.MHSCriteriaVO;
import com.mckesson.smora.dto.MHSReportFormatVO;
import com.mckesson.smora.dto.MicaListVO;
import com.mckesson.smora.dto.MicaVO;
import com.mckesson.smora.dto.PurchaseDrillDownVO;
import com.mckesson.smora.dto.ReportAccountProfileVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.SortListVO;
import com.mckesson.smora.dto.SortVO;
import com.mckesson.smora.dto.SupplierListVO;
import com.mckesson.smora.dto.SupplierVO;
import com.mckesson.smora.dto.TherapeuticListVO;
import com.mckesson.smora.dto.TherapeuticVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.UserCalcListVO;
import com.mckesson.smora.dto.UserCalcVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomReportingForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.FormattingSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.MHSReportingForm;
import com.mckesson.smora.ui.form.PurchaseDrillDownForm;
import com.mckesson.smora.ui.form.PurchaseDrillDownSelectionForm;
import com.mckesson.smora.ui.form.ReportAccountProfileForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;



/**
 * The Class ConstructReportBaseVO.
 */
public class ConstructReportBaseVO
{
	
	private static HttpSession session = null;
	

	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportBaseVO";

	/**
	 * The Constant cidString.
	 */
	private static final String cidString = "CID";

	/**
	 * The Constant hspString.
	 */
	private static final String hspString = "HSP";

	/**
	 * The Constant slsString.
	 */
	private static final String slsString = "SLS";

	/**
	 * The Constant chnString.
	 */
	private static final String chnString = "CHN";

	private static final String String = null;
	private static CriteriaVO criteriaVO = null;
	
	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;

	/**
	 * The cust report criteria VO.
	 */
	private static CustReportCriteriaVO custReportCriteriaVO = null;
	
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(ReportsProcessorUtil.class);
	
	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReportBaseVO(CustomReportingForm customReportingForm) throws SMORAException
	{
		
		// userID = (String) session.getAttribute("userID");
		// customReportingForm = (CustomReportingForm)
		// session.getAttribute("CustomReportingForm");
		String chckProductImageOnly = null;
		custReportCriteriaVO = new CustReportCriteriaVO();
		FormattingSelectionForm formattingSelectionForm = customReportingForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = customReportingForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = customReportingForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = customReportingForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = customReportingForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = customReportingForm.getAdvancedFiltersForm();
        
		ReportBaseVO baseVO = new ReportBaseVO();
		CustomReportVO customReportVO = new CustomReportVO();
		
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(formattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(formattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(formattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CUSTOM_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.CUSTOM_REPORT);
		baseVO.setRowLimits(formattingSelectionForm.getNumberOfRecordsLimitation());
		//QC-11691
		chckProductImageOnly = formattingSelectionForm.getSequenceFieldsHidden();
		ArrayList<String> sequenceFields = String2Array.strArr(chckProductImageOnly, ",");
		baseVO.setHtml(formattingSelectionForm.isResultsDisplayHTML());
		if(sequenceFields != null && sequenceFields.size() == 1 && sequenceFields.get(0).contains(ReportManagerConstants.IMAGE_FIELD)) {
			formattingSelectionForm.setResultsDisplayPDF(false);
			formattingSelectionForm.setResultsDisplayXLS(false);
			formattingSelectionForm.setResultsDisplayCSV(false);
		} 
		baseVO.setPdf(formattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(formattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(formattingSelectionForm.isResultsDisplayCSV());
		custReportCriteriaVO = constructCustomReportVO(formattingSelectionForm);
		custReportCriteriaVO = constructCustomReportVO(dateSelectionForm);
		custReportCriteriaVO = constructCustomReportVO(customerSelectionForm);
		custReportCriteriaVO = constructCustomReportVO(supplierSelectionForm);
		custReportCriteriaVO = constructCustomReportVO(itemSelectionForm);
		custReportCriteriaVO = constructCustomReportVO(advancedFiltersForm);
		
		//QC-11770 check added
		if(itemSelectionForm.isPurchasedItemOnly()==false)
		{
			custReportCriteriaVO.getItemVO().getItemNumbersList().setMckItems(true);
			custReportCriteriaVO.getItemVO().getItemNumbersList().setNonMckItems(true);
		}
		//QC-11734:Issues with Previous Price field
		custReportCriteriaVO.setPreviousPriceDate(customReportingForm.getPreviousPriceDate());		
		customReportVO.setCustReportCriteriaVO(custReportCriteriaVO);
		baseVO.setCustomReportVO(customReportVO);

		return baseVO;
	}

	/**
	 * This method populates the report base VO.
	 *
	 * @param reportAccountProfileForm the Account Profile form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO populateReportBaseVO(ReportAccountProfileForm reportAccountProfileForm,String reportType,String reportFormat) throws SMORAException
	{
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_ACCOUNT_PROFILE);
		
		if(reportFormat !=null&&reportFormat.equalsIgnoreCase("XLS"))
		{
			baseVO.setXls(true);
			baseVO.setCsv(false);
			baseVO.setHtml(false);
			baseVO.setPdf(false);
		}
		else if(reportFormat !=null&&reportFormat.equalsIgnoreCase("CSV"))
		{
			baseVO.setCsv(true);
			baseVO.setHtml(false);
			baseVO.setPdf(false);
			baseVO.setXls(false);
		}
		else if(reportFormat !=null&&reportFormat.equalsIgnoreCase("PDF"))
		{
			baseVO.setPdf(true);
			baseVO.setCsv(false);
			baseVO.setHtml(false);
			baseVO.setXls(false);
		}
		else{
			baseVO.setHtml(reportAccountProfileForm.isResultsDisplayHTML());
		}
	
		ReportAccountProfileVO reportAccountProfileVO = new ReportAccountProfileVO();
		reportAccountProfileVO.setCustomerNumber(reportAccountProfileForm.getCustomerNumber());
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();
		List<TApplCnRptFldMetaData> metaDataList = null;
		ArrayList<FieldsVO> fieldsVOList = new ArrayList<FieldsVO>();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		FieldsListVO fieldsListVO = new FieldsListVO();
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(ReportManagerConstants.REPORT_ACCOUNT_PROFILE);
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
					reportType="purchase";
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldDscr());
					if(reportType!=null && reportType.equals("purchase")){
						for(int j=1;j<=26;j++){
							if(j==fieldId){
								fieldsList.add(fieldsVO);
						}
						}
					}
				}			
			}
		}
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setReportAccountProfileVO(reportAccountProfileVO);
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);		
		
		return baseVO;
	}
//	 Method for MHS OneStop Report
	/**
 * This method populates the report base VO.
 *
 * @param mhsReportingForm the mhs reporting form
 *
 * @return the report base VO
 *
 * @throws SMORAException the SMORA exception
 */
public static ReportBaseVO populateReportBaseVO(MHSReportingForm mhsReportingForm) throws SMORAException
	{
		custReportCriteriaVO = new CustReportCriteriaVO();
		FormattingSelectionForm formattingSelectionForm = mhsReportingForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = mhsReportingForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = mhsReportingForm.getCustomerSelectionForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CustomReportVO customReportVO = new CustomReportVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		baseVO.setCustomHeading(formattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.SUBMARINE_REPORT);
		baseVO.setRowLimits(formattingSelectionForm.getNumberOfRecordsLimitation());
		baseVO.setHtml(formattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(formattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(formattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(formattingSelectionForm.isResultsDisplayCSV());

		custReportCriteriaVO = constructMHSReportVO(dateSelectionForm);
		custReportCriteriaVO = constructCustomReportVO(customerSelectionForm);

		MHSReportFormatVO mhsReportFormatVO = new MHSReportFormatVO();
		mhsReportFormatVO.setAccountFormat(formattingSelectionForm.getMhsFormat());
		MHSCriteriaVO mhsCriteriaVO = new MHSCriteriaVO();
		mhsCriteriaVO.setMhsreportFormatVO(mhsReportFormatVO);

		cannedReportVO.setMHSCriteriaVO(mhsCriteriaVO);
		customReportVO.setCustReportCriteriaVO(custReportCriteriaVO);
		baseVO.setCustomReportVO(customReportVO);
		baseVO.setCannedReportVO(cannedReportVO);
		return baseVO;
	}
/**
 * This method populates the report base VO.
 *
 * @param purchaseDrillDownForm the mhs reporting form
 *
 * @return the report base VO
 *
 * @throws SMORAException the SMORA exception
 */

	public static ReportBaseVO populateReportBaseVO(PurchaseDrillDownForm purchaseDrillDownForm, HttpServletRequest request , HttpServletResponse response) throws SMORAException
	{   
		session = request.getSession();
		criteriaVO = new CriteriaVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		String currentFilter = null;
		PurchaseDrillDownSelectionForm purchaseDrillDownSelectionForm = purchaseDrillDownForm.getPurchaseDrillDownSelectionForm();
		DateSelectionForm dateSelectionForm = purchaseDrillDownForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = purchaseDrillDownForm.getCustomerSelectionForm();
		PurchaseDrillDownUtil purchaseDrillDownUtil = new PurchaseDrillDownUtil();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		PurchaseDrillDownVO purchaseDrillDownVO = new PurchaseDrillDownVO();
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		String actual_template= (String)session.getAttribute("actual_template");
		log.info("ActualtemplateNaminconstruct :" + actual_template );
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(actual_template!=null)
		{
			baseVO.setTemplateName(actual_template);
		}
		else
		{
			baseVO.setTemplateName("");
		}
		purchaseDrillDownVO.setFormat(purchaseDrillDownSelectionForm.getPddFormat());
		purchaseDrillDownVO.setDrillRepName(purchaseDrillDownForm.getDrillDownReport());
		
		String gName ="";
		if((purchaseDrillDownForm.getDrillDownReport() != null) && ((purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_A)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_AA)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_U))))
		{
			
			String custSelection = null;
			String primaryCustomerNumber = null;
			if ((purchaseDrillDownSelectionForm.getPrimaryCustomerNumber() == null) || (purchaseDrillDownSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
			{
			 
				String selGroup = purchaseDrillDownSelectionForm.getPddGroup();
				
				if(selGroup !=null && selGroup.contains(":"))
				{
				 gName =selGroup.substring(0,selGroup.indexOf(":"));
				}
			
				if (gName.equalsIgnoreCase("Natl Group"))
				{
					String delim = ":";
					String custInfo = null;
					custSelection = purchaseDrillDownForm.getSelectedGroup();
					 //	Req. 6,33,45 changes done by Anil Kumar.
					  if (selGroup.split(delim).length >1)
						{
							String[] sGroup = selGroup.split(delim);
							//Added for SO-3806 SynerGx Rebranding- populating purchaseDrillDownVO
							if (sGroup.length == 2)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"Natl Group"));
							 purchaseDrillDownVO.setNationalGrpCd(groupID);
							}
							if (sGroup.length == 3)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 String subGroupID = sGroup[2].substring(sGroup[2].lastIndexOf("(")+1, sGroup[2].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"Natl Group"));
							 purchaseDrillDownVO.setNationalGrpCd(groupID);
							 purchaseDrillDownVO.setNationalSubGrpCd(subGroupID);
							}
							if (sGroup.length == 4)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 String subGroupID = sGroup[2].substring(sGroup[2].lastIndexOf("(")+1, sGroup[2].lastIndexOf(")"));
							 String regionID = sGroup[3].substring(sGroup[3].lastIndexOf("(")+1, sGroup[3].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"Natl Group"));
							 purchaseDrillDownVO.setNationalGrpCd(groupID);
							 purchaseDrillDownVO.setNationalSubGrpCd(subGroupID);
							 purchaseDrillDownVO.setRegionCd(regionID);
							}
							if (sGroup.length == 5)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 String subGroupID = sGroup[2].substring(sGroup[2].lastIndexOf("(")+1, sGroup[2].lastIndexOf(")"));
							 String regionID = sGroup[3].substring(sGroup[3].lastIndexOf("(")+1, sGroup[3].lastIndexOf(")"));
							 String districtID = sGroup[4].substring(sGroup[4].lastIndexOf("(")+1, sGroup[4].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"Natl Group"));
							 purchaseDrillDownVO.setNationalGrpCd(groupID);
							 purchaseDrillDownVO.setNationalSubGrpCd(subGroupID);
							 purchaseDrillDownVO.setRegionCd(regionID);
							 purchaseDrillDownVO.setDistrictCd(districtID);
							}
							//Changes for SO-3806 Ends
						}
					
				} else if (gName.trim().equalsIgnoreCase("Chain"))
				{
					String delim = ":";
				
					custSelection = purchaseDrillDownForm.getSelectedGroup();	
					 //	Req. 6,33,45 changes done by Anil Kumar.
					  if (selGroup.split(delim).length >1)
						{
							String[] sGroup = selGroup.split(delim);
							//Added for SO-3806 SynerGx Rebranding- populating purchaseDrillDownVO
							if (sGroup.length==2)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 purchaseDrillDownVO.setCustChainId(groupID);
							}
							if (sGroup.length == 3)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 String regionID = sGroup[2].substring(sGroup[2].lastIndexOf("(")+1, sGroup[2].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"Natl Group"));
							 purchaseDrillDownVO.setCustChainId(groupID);
							 purchaseDrillDownVO.setRegionCd(regionID);
							}
							if (sGroup.length == 4)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 String regionID = sGroup[2].substring(sGroup[2].lastIndexOf("(")+1, sGroup[2].lastIndexOf(")"));
							 String districtID = sGroup[3].substring(sGroup[3].lastIndexOf("(")+1, sGroup[3].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"Natl Group"));
							 purchaseDrillDownVO.setCustChainId(groupID);
							 purchaseDrillDownVO.setRegionCd(regionID);
							 purchaseDrillDownVO.setDistrictCd(districtID);
							}
							//Changes for SO-3806 Ends
				//	purchaseDrillDownVO.setCustChainId(purchaseDrillDownForm.getGroupID());
					}
						} else if (gName.trim().equalsIgnoreCase("DC"))
				{
					String delim = ":";
					String custInfo = null;
					
					custSelection = purchaseDrillDownForm.getSelectedGroup();	
					 //	Req. 6,33,45 changes done by Anil Kumar.
					  if (selGroup.split(delim).length >1)
						{
							String[] sGroup = selGroup.split(delim);
							if (sGroup.length>0)
							{
							 String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							 //	Req. 6,33,45 changes done by Anil Kumar.
							 //purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(groupID,"DC"));
							}
						}
				} else if (gName.trim().equalsIgnoreCase("Customer"))
				{
					String delim = ":";
					String custInfo = null;
					if (selGroup.split(delim).length >1)
					{
						String[] sGroup = selGroup.split(delim);
						if (sGroup.length>0)
						{
							String groupID = sGroup[1].substring(sGroup[1].lastIndexOf("(")+1, sGroup[1].lastIndexOf(")"));
							custSelection = purchaseDrillDownUtil.getAccountNameAddress(groupID,"AccName");
							purchaseDrillDownVO.setCurrentFilter(purchaseDrillDownUtil.getAccountNameAddress(groupID,"Address"));
							//	Req. 6,33,45 changes done by Anil Kumar.

							//Added for SO-3806 SynerGx Rebranding-Storing the natCodingLkupObj in  purchaseDrillDownVO
							TCustAcct tCustAcct = purchaseDrillDownUtil.getCustAccount(groupID);
							purchaseDrillDownVO.setCustChainId( tCustAcct.getCustChnId());
							purchaseDrillDownVO.setNationalGrpCd( tCustAcct.getNatlGrpCd());
							purchaseDrillDownVO.setNationalSubGrpCd(tCustAcct.getNatlSubGrpCd());
							purchaseDrillDownVO.setRegionCd(tCustAcct.getCustRgnNum());
							purchaseDrillDownVO.setDistrictCd(tCustAcct.getCustDstrctNum());
						}
					}

				}
				purchaseDrillDownVO.setGroup(purchaseDrillDownSelectionForm.getPddGroup());	
				purchaseDrillDownVO.setCustSelection(custSelection);
				purchaseDrillDownVO.setCurrentFilter(purchaseDrillDownForm.getCurrentFilter());
				purchaseDrillDownVO.setAccGroupName(purchaseDrillDownForm.getAccGroupName());
				purchaseDrillDownVO.setAccGroupID(purchaseDrillDownForm.getAccGroupID());
			} else {
				primaryCustomerNumber = purchaseDrillDownSelectionForm.getPrimaryCustomerNumber();

				purchaseDrillDownVO.setPrimaryCustomerNumber(purchaseDrillDownSelectionForm.getPrimaryCustomerNumber());
				custSelection =  purchaseDrillDownUtil.getAccountNameAddress(primaryCustomerNumber,"AccName");
				//	Req. 6,33,45 changes done by Anil Kumar.
				// purchaseDrillDownVO.setCustChainId( purchaseDrillDownUtil.getAccountNameAddress(primaryCustomerNumber,"Chain"));

				//Added for SO-3806 SynerGx Rebranding-Storing the natCodingLkupObj in  purchaseDrillDownVO
				TCustAcct tCustAcct = purchaseDrillDownUtil.getCustAccount(primaryCustomerNumber);
				purchaseDrillDownVO.setCustChainId( tCustAcct.getCustChnId());
				purchaseDrillDownVO.setNationalGrpCd( tCustAcct.getNatlGrpCd());
				purchaseDrillDownVO.setNationalSubGrpCd(tCustAcct.getNatlSubGrpCd());
				purchaseDrillDownVO.setRegionCd(tCustAcct.getCustRgnNum());
				purchaseDrillDownVO.setDistrictCd(tCustAcct.getCustDstrctNum());

				purchaseDrillDownVO.setCustSelection(custSelection);
			}
		} else {
			purchaseDrillDownVO.setCustSelection(purchaseDrillDownForm.getCustSelection());
			purchaseDrillDownVO.setBeginDate(purchaseDrillDownForm.getStartDate());
			purchaseDrillDownVO.setCurrentFilter(purchaseDrillDownForm.getCurrentFilter());
			purchaseDrillDownVO.setGroup(purchaseDrillDownSelectionForm.getPddGroup());
			purchaseDrillDownVO.setGroupName(purchaseDrillDownForm.getGroupName());
			purchaseDrillDownVO.setGroupID(purchaseDrillDownForm.getGroupID());
			purchaseDrillDownVO.setSelGroupID(purchaseDrillDownForm.getSelGroupID());
			purchaseDrillDownVO.setColHeader(purchaseDrillDownForm.getColHeader());
			purchaseDrillDownVO.setSelectedField(purchaseDrillDownForm.getSelectedField());
			purchaseDrillDownVO.setEndDate(purchaseDrillDownForm.getStartDate());
			purchaseDrillDownVO.setAccGroupName(purchaseDrillDownForm.getAccGroupName());
			purchaseDrillDownVO.setAccGroupID(purchaseDrillDownForm.getAccGroupID());
			purchaseDrillDownVO.setPrimaryCustomerNumber(purchaseDrillDownSelectionForm.getPrimaryCustomerNumber());
			//Modified for RRP 1 Issue 476 - tv193xi
			purchaseDrillDownVO.setSecondaryCustomerNumber(purchaseDrillDownForm.getSecondaryCustomerNumber());
		}
		
		baseVO.setCustomHeading(purchaseDrillDownSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setRowLimits(purchaseDrillDownSelectionForm.getNumberOfRecordsLimitation());
		baseVO.setHtml(purchaseDrillDownSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(purchaseDrillDownSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(purchaseDrillDownSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(purchaseDrillDownSelectionForm.isResultsDisplayCSV());
		int repSubType = ReportManagerConstants.PURCHASE_DRILL_DOWN_SUMMARY;
		baseVO.setReportSubtype(repSubType);
		purchaseDrillDownVO.setRepId(purchaseDrillDownForm.getSubReportId());
		
		//Added for SO-3806 SynerGx Rebranding- loading the nationalCodingLkUp Object
		TNatCodingLkup natCodingLkupObj = null;
		if(gName.trim().equalsIgnoreCase("Chain"))
		{
			if(purchaseDrillDownVO.getCustChainId()!=null)
			{
				if(purchaseDrillDownVO.getRegionCd()==null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNationalCodingForChain(purchaseDrillDownVO.getCustChainId());
				}
				else if(purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNatCodForChnRegion(purchaseDrillDownVO); 
					if(natCodingLkupObj == null)
						natCodingLkupObj = CannedReportDB.getPDDNationalCodingForChain(purchaseDrillDownVO.getCustChainId());
				}
				else if(purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()!=null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNatCodForChnDistrict(purchaseDrillDownVO);
					if(natCodingLkupObj == null)
					{
						natCodingLkupObj = CannedReportDB.getPDDNatCodForChnRegion(purchaseDrillDownVO);
						if(natCodingLkupObj == null)
							natCodingLkupObj = CannedReportDB.getPDDNationalCodingForChain(purchaseDrillDownVO.getCustChainId());
					}

				}
			}
		}
		else if(gName.trim().equalsIgnoreCase("Natl Group"))
		{
			if(purchaseDrillDownVO.getNationalGrpCd()!=null)
			{
				if( purchaseDrillDownVO.getNationalSubGrpCd()==null && 
						purchaseDrillDownVO.getRegionCd()==null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
				}
				else if(purchaseDrillDownVO.getNationalSubGrpCd()!=null 
						&& purchaseDrillDownVO.getRegionCd()==null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatSubGrp(purchaseDrillDownVO);
					if(natCodingLkupObj == null)
						natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
				}
				else if(purchaseDrillDownVO.getNationalSubGrpCd()!=null 
						&& purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNationalCodingForRegion(purchaseDrillDownVO);
					{
						if(natCodingLkupObj == null)
						{
							natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatSubGrp(purchaseDrillDownVO);
							if(natCodingLkupObj == null)
								natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
						}
					}

				}
				else if(purchaseDrillDownVO.getNationalSubGrpCd()!=null 
						&& purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()!=null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNationalCodingForDistrict(purchaseDrillDownVO);
					if(natCodingLkupObj == null)
					{
						natCodingLkupObj = CannedReportDB.getPDDNationalCodingForRegion(purchaseDrillDownVO);
						if(natCodingLkupObj == null)
						{
							natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatSubGrp(purchaseDrillDownVO);
							if(natCodingLkupObj == null)
								natCodingLkupObj = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
						}
					}
				}
			}
		}
		else if(gName.trim().equalsIgnoreCase("Customer")|| ((purchaseDrillDownSelectionForm.getPrimaryCustomerNumber() != null) || (!purchaseDrillDownSelectionForm.getPrimaryCustomerNumber().trim().equals(""))))
		{
			TNatCodingLkup natCodingLkupObj_tmp = null;
			if(purchaseDrillDownVO.getCustChainId()!=null) 
			{
				if(purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()!=null){
					natCodingLkupObj = CannedReportDB.getPDDNatCodForChnDistrict(purchaseDrillDownVO);
					if(natCodingLkupObj == null)
					{
						natCodingLkupObj = CannedReportDB.getPDDNatCodForChnRegion(purchaseDrillDownVO);
						if(natCodingLkupObj == null)
							natCodingLkupObj = CannedReportDB.getPDDNationalCodingForChain(purchaseDrillDownVO.getCustChainId());
					}
				}
				else if(purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNatCodForChnRegion(purchaseDrillDownVO);
					if(natCodingLkupObj == null)
						natCodingLkupObj = CannedReportDB.getPDDNationalCodingForChain(purchaseDrillDownVO.getCustChainId());
				}
				else if(purchaseDrillDownVO.getRegionCd()==null && purchaseDrillDownVO.getDistrictCd()==null)
				{
					natCodingLkupObj = CannedReportDB.getPDDNationalCodingForChain(purchaseDrillDownVO.getCustChainId());
				}
			}
			if(purchaseDrillDownVO.getNationalGrpCd()!=null)
			{
				if(purchaseDrillDownVO.getNationalSubGrpCd()!=null && purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()!=null){					
					natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForDistrict(purchaseDrillDownVO);
					if(natCodingLkupObj_tmp == null)
					{
						natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForRegion(purchaseDrillDownVO);
						if(natCodingLkupObj_tmp == null)
						{
							natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatSubGrp(purchaseDrillDownVO);
							if(natCodingLkupObj_tmp == null)
								natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
						}
					}
				}
				else if(purchaseDrillDownVO.getNationalSubGrpCd()!=null && purchaseDrillDownVO.getRegionCd()!=null && purchaseDrillDownVO.getDistrictCd()==null)
				{					
					natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForRegion(purchaseDrillDownVO);
					if(natCodingLkupObj_tmp == null)
					{
						natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatSubGrp(purchaseDrillDownVO);
						if(natCodingLkupObj_tmp == null)
							natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
					}
				}
				else if(purchaseDrillDownVO.getNationalSubGrpCd()!=null && purchaseDrillDownVO.getRegionCd()==null && purchaseDrillDownVO.getDistrictCd()==null)
				{					
						natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatSubGrp(purchaseDrillDownVO);
						if(natCodingLkupObj_tmp == null)
							natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
				}
				else if(purchaseDrillDownVO.getNationalSubGrpCd()==null && purchaseDrillDownVO.getRegionCd()==null && purchaseDrillDownVO.getDistrictCd()==null)
				{					
						natCodingLkupObj_tmp = CannedReportDB.getPDDNationalCodingForNatGrp(purchaseDrillDownVO.getNationalGrpCd());
				}
			}
			if(natCodingLkupObj_tmp!=null && natCodingLkupObj != null) 
			{
				if(!natCodingLkupObj_tmp.getCustGroup().equals(natCodingLkupObj.getCustGroup()))
				{
					natCodingLkupObj = null;
				}
			}
			else if (natCodingLkupObj_tmp!=null && natCodingLkupObj == null)
				natCodingLkupObj = natCodingLkupObj_tmp;
		}
		if(natCodingLkupObj!=null)
		{
			purchaseDrillDownVO.setNatCodingLkupObj(natCodingLkupObj);
		}
		//Changes for SO-3806 Ends
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
		
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();
		
		
		List<TApplCnRptFldMetaData> metaDataList = null;
		ArrayList<FieldsVO> fieldsVOList = new ArrayList<FieldsVO>();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		FieldsListVO fieldsListVO = new FieldsListVO();
		
		
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(purchaseDrillDownForm.getSubReportId());
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
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldDscr());
					fieldsList.add(fieldsVO);
				}			
			}
		}

		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setPurchaseDrillDownVO(purchaseDrillDownVO);
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);		
		return baseVO;
	}
	/**
	 * This method constructs the custom report VO.
	 *
	 * @param formattingSelectionForm the formatting selection form
	 *
	 * @return the cust report criteria VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	private static CustReportCriteriaVO constructCustomReportVO(FormattingSelectionForm formattingSelectionForm) throws SMORAException
	{
		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
		}
		CustomReportDB customReportDB  = new CustomReportDB();
		// -------------- Inside Sequence page -----------------------
		String sequenceFieldsStr = formattingSelectionForm.getSequenceFieldsHidden();
		ArrayList<String> sequenceFields = String2Array.strArr(sequenceFieldsStr, ",");
		Set sequenceFieldsSet = new LinkedHashSet(); 
		sequenceFieldsSet.addAll(sequenceFields);
		sequenceFields = new ArrayList();
		sequenceFields.addAll(sequenceFieldsSet); 
		// ----------------------------Tree
		// Page-----------------------------------
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
		if(formattingSelectionForm.getGroupBy1().contains(ReportManagerConstants.IMAGE_FIELD)){
			formattingSelectionForm.setGroupBy1("");
		}
		customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getGroupBy1());
		firstGroupLevelVO.setFieldName(formattingSelectionForm.getGroupBy1());
		String[] subTotal1 = formattingSelectionForm.getSubTotal1();
		ArrayList subTotalList1 = new ArrayList();
		for(int i = 0; i < subTotal1.length; i++)
		{
			if (subTotal1[i] != null)
			{
			subTotalList1.add(subTotal1[i]);
		}
		}
		firstGroupLevelVO.setSubTotal(subTotalList1);
		// firstGroupLevelVO.setSubTotal(formattingSelectionForm.getSubTotal1List());
		//QC-11691
		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		if(formattingSelectionForm.getGroupBy2().contains(ReportManagerConstants.IMAGE_FIELD)){
			formattingSelectionForm.setGroupBy2("");
		}
		secondGroupLevelVO.setFieldName(formattingSelectionForm.getGroupBy2());
		String[] subTotal2 = formattingSelectionForm.getSubTotal2();
		ArrayList subTotalList2 = new ArrayList();
		for(int i = 0; i < subTotal2.length; i++)
		{
			if (subTotal2[i] != null)
			{
			subTotalList2.add(subTotal2[i]);
		}
		}
		secondGroupLevelVO.setSubTotal(subTotalList2);
		// secondGroupLevelVO.setSubTotal(formattingSelectionForm.getSubTotal2List());
        //QC-11691
		ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
		if(formattingSelectionForm.getGroupBy3().contains(ReportManagerConstants.IMAGE_FIELD)){
			formattingSelectionForm.setGroupBy3("");
		}
		thirdGroupLevelVO.setFieldName(formattingSelectionForm.getGroupBy3());
		String[] subTotal3 = formattingSelectionForm.getSubTotal3();
		ArrayList subTotalList3 = new ArrayList();
		for(int i = 0; i < subTotal3.length; i++)
		{
			if (subTotal3[i] != null)
			{
			subTotalList3.add(subTotal3[i]);
		}
		}
		thirdGroupLevelVO.setSubTotal(subTotalList3);
		// thirdGroupLevelVO.setSubTotal(formattingSelectionForm.getSubTotal3List());
        //QC-11691
		
		groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);
		

		SortListVO sortListVO = new SortListVO();
		SortVO sortVO = null;
		ArrayList<SortVO> sortVOList = new ArrayList<SortVO>();

		// First Level Sorting
		sortVO = new SortVO();
		// QC-11691
		if (!formattingSelectionForm.getSelectedSort1().contains(ReportManagerConstants.IMAGE_FIELD)){
			sortVO.setSortId(customReportDB.getFieldMetaDataDescForId( formattingSelectionForm.getSelectedSort1()));
		} else {
			sortVO.setSortId(customReportDB.getFieldMetaDataDescForId(""));
		}
		
		sortVO.setSort(formattingSelectionForm.getSelectedSort1());
		sortVO.setOrder(formattingSelectionForm.getType1());
		sortVOList.add(sortVO);
		// Second Level Sorting
		sortVO = new SortVO();
		if(!formattingSelectionForm.getSelectedSort2().contains(ReportManagerConstants.IMAGE_FIELD)) {
			sortVO.setSortId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedSort2()));
		} else{
			sortVO.setSortId(customReportDB.getFieldMetaDataDescForId(""));
		}
		
		sortVO.setSort(formattingSelectionForm.getSelectedSort2());
		sortVO.setOrder(formattingSelectionForm.getType2());
		sortVOList.add(sortVO);
		// Third Level Sorting
		sortVO = new SortVO();
		if(!formattingSelectionForm.getSelectedSort3().contains(ReportManagerConstants.IMAGE_FIELD)){
			sortVO.setSortId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedSort3()));	
		} else {
			sortVO.setSortId(customReportDB.getFieldMetaDataDescForId(""));
		}
		
		sortVO.setSort(formattingSelectionForm.getSelectedSort3());
		sortVO.setOrder(formattingSelectionForm.getType3());
		sortVOList.add(sortVO);
		sortListVO.setSortVOList(sortVOList);

		// fields
		String selectedFormattingTreeData = formattingSelectionForm.getSelectedFormattingTreeData();
		ArrayList<String> formattingAL = String2Array.strArr(selectedFormattingTreeData, "*");
		ArrayList<FieldsVO> fieldsVOList = new ArrayList();
		FieldsListVO fieldsListVO = new FieldsListVO();
		for (int i = 0; i < sequenceFields.size(); i++)
		{
			FieldsVO fieldsVO = new FieldsVO();
			fieldsVO.setFldId(Integer.parseInt(customReportDB.getFieldMetaDataDescForId(sequenceFields.get(i))));
			fieldsVO.setFieldName(sequenceFields.get(i));
			fieldsVOList.add(fieldsVO);
		}
		fieldsListVO.setFieldsVOList(fieldsVOList);

		// filters
		FilterListVO filterListVO = new FilterListVO();
		
		FiltersVO filtersVO = null;
		ArrayList<FiltersVO> filterVOList = new ArrayList();

		filtersVO = new FiltersVO();
		
		String filterCondition = "";
		if(formattingSelectionForm.getAnd1().equalsIgnoreCase("AND")){
			filterCondition = formattingSelectionForm.getAnd1();
		}else{
			filterCondition = formattingSelectionForm.getOr1();
		}
		filterListVO.setFilterCondition(filterCondition);
		
		filtersVO.setFieldId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedField1()));
		filtersVO.setField(formattingSelectionForm.getSelectedField1());
		filtersVO.setAnd(formattingSelectionForm.getAnd1());
		filtersVO.setOr(formattingSelectionForm.getOr1());
		filtersVO.setOperator(formattingSelectionForm.getSelectedOperator1());
		filtersVO.setValue(formattingSelectionForm.getValue1());
		filterVOList.add(filtersVO);

		filtersVO = new FiltersVO();
		filtersVO.setFieldId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedField2()));
		filtersVO.setField(formattingSelectionForm.getSelectedField2());
		filtersVO.setAnd(formattingSelectionForm.getAnd2());
		filtersVO.setOr(formattingSelectionForm.getOr2());
		filtersVO.setOperator(formattingSelectionForm.getSelectedOperator2());
		filtersVO.setValue(formattingSelectionForm.getValue2());
		filterVOList.add(filtersVO);

		filtersVO = new FiltersVO();
		filtersVO.setFieldId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedField3()));
		filtersVO.setField(formattingSelectionForm.getSelectedField3());
		filtersVO.setAnd(formattingSelectionForm.getAnd3());
		filtersVO.setOr(formattingSelectionForm.getOr3());
		filtersVO.setOperator(formattingSelectionForm.getSelectedOperator3());
		filtersVO.setValue(formattingSelectionForm.getValue3());
		filterVOList.add(filtersVO);

		filtersVO = new FiltersVO();
		filtersVO.setFieldId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedField4()));
		filtersVO.setField(formattingSelectionForm.getSelectedField4());
		filtersVO.setAnd(formattingSelectionForm.getAnd4());
		filtersVO.setOr(formattingSelectionForm.getOr4());
		filtersVO.setOperator(formattingSelectionForm.getSelectedOperator4());
		filtersVO.setValue(formattingSelectionForm.getValue4());
		filterVOList.add(filtersVO);

		filtersVO = new FiltersVO();
		filtersVO.setFieldId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedField5()));
		filtersVO.setField(formattingSelectionForm.getSelectedField5());
		filtersVO.setAnd(formattingSelectionForm.getAnd5());
		filtersVO.setOr(formattingSelectionForm.getOr5());
		filtersVO.setOperator(formattingSelectionForm.getSelectedOperator5());
		filtersVO.setValue(formattingSelectionForm.getValue5());
		filterVOList.add(filtersVO);

		filtersVO = new FiltersVO();
		filtersVO.setFieldId(customReportDB.getFieldMetaDataDescForId(formattingSelectionForm.getSelectedField6()));
		filtersVO.setField(formattingSelectionForm.getSelectedField6());
		filtersVO.setAnd(formattingSelectionForm.getAnd6());
		filtersVO.setOr(formattingSelectionForm.getOr6());
		filtersVO.setOperator(formattingSelectionForm.getSelectedOperator6());
		filtersVO.setValue(formattingSelectionForm.getValue6());
		filterVOList.add(filtersVO);
		filterListVO.setFilterVOList(filterVOList);

		// -------------- Inside UserCalc page -----------------------
		UserCalcListVO userCalcListVO = new UserCalcListVO();
		UserCalcVO userCalcVO = null;
		ArrayList<UserCalcVO> userCalcVOList = new ArrayList<UserCalcVO>();
		String[] userCalHidden = formattingSelectionForm.getUserCalHidden();
		int calcNum;
		String formulaName = null;
		String field1 = null;
		String field2 = null;
		String calculation = null;
		for (int i = 0; i < userCalHidden.length; i++)
		{
			// StringTokenizer tokenizer = new StringTokenizer(userCalHidden[i],
			// ",");
			ArrayList userCalculationData = String2Array.strArr(userCalHidden[i], ",");

			if (userCalculationData.size() >= 3)
			{
				userCalcVO = new UserCalcVO();
				String calNum = (String) (userCalculationData.get(0));
				calcNum = Integer.parseInt(calNum.trim());
				formulaName = (String) userCalculationData.get(1);
				calculation = (String) userCalculationData.get(2);


				if (calculation.startsWith(SMOConstant.SUM) || calculation.startsWith(SMOConstant.MAXIMUM) || calculation.startsWith(SMOConstant.MINIMUM) || calculation.startsWith(SMOConstant.AVERAGE))
				{
					ArrayList operatorList = new ArrayList();
					if (calculation.startsWith(SMOConstant.SUM))
					{
						String calcArray = calculation.substring(4);
						userCalcVO.setField1(calcArray);
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(calcArray));
						userCalcVO.setOperator(SMOConstant.SUM);
					}
					else if (calculation.startsWith(SMOConstant.MAXIMUM))
					{
						String calcArray = calculation.substring(4);
						userCalcVO.setField1(calcArray);
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(calcArray));
						userCalcVO.setOperator(SMOConstant.MAXIMUM);
					}
					else if (calculation.startsWith(SMOConstant.MINIMUM))
					{
						String calcArray = calculation.substring(4);
						userCalcVO.setField1(calcArray);
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(calcArray));
						userCalcVO.setOperator(SMOConstant.MINIMUM);
					}
					else if (calculation.startsWith(SMOConstant.AVERAGE))
					{
						String calcArray = calculation.substring(4);
						userCalcVO.setField1(calcArray);
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(calcArray));
						userCalcVO.setOperator(SMOConstant.AVERAGE);
					}
				}
				//QC#11243 change : Add space in both sides for Multiply operator
				else if ((calculation.lastIndexOf(SMOConstant.ADD)>=0) || (calculation.lastIndexOf(SMOConstant.SUBTRACT)>=0) || (calculation.lastIndexOf(" "+SMOConstant.MULTIPLY+" ")>=0) || (calculation.lastIndexOf(SMOConstant.DIVIDE)>=0))
				{
					if (calculation.lastIndexOf(SMOConstant.ADD)>=0)
					{
						ArrayList calcArray = String2Array.strSplit(calculation, SMOConstant.ADD);
						if(calcArray != null && calcArray.size() > 0)
						{
							field1 = calcArray.get(0).toString();
							field2 = calcArray.get(1).toString();
						}
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(field1));
						userCalcVO.setFldId2(customReportDB.getFieldMetaDataDescForId(field2));
						userCalcVO.setField1(field1);
						userCalcVO.setField2(field2);
						userCalcVO.setOperator(SMOConstant.ADD);
					}
					
					//else if (calculation.lastIndexOf(SMOConstant.MULTIPLY)>=0)
					else if (calculation.lastIndexOf(" "+SMOConstant.MULTIPLY+" ")>=0)
					{
						//ArrayList calcArray = String2Array.strSplit(calculation, SMOConstant.MULTIPLY);
						ArrayList calcArray = String2Array.strSplit(calculation," "+SMOConstant.MULTIPLY+" ");
						if(calcArray != null && calcArray.size() > 0)
						{
							field1 = calcArray.get(0).toString();
							field2 = calcArray.get(1).toString();
						}
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(field1));
						userCalcVO.setFldId2(customReportDB.getFieldMetaDataDescForId(field2));
						field1=field1+" ";
						field2=" "+field2;
						userCalcVO.setField1(field1);
						userCalcVO.setField2(field2);
						userCalcVO.setOperator(SMOConstant.MULTIPLY);
					}
					else if (calculation.lastIndexOf(SMOConstant.DIVIDE)>=0)
					{
						/*char theChar = '/';
						int count = 0;
						count = countChars(calculation,theChar);
						System.out.println("count in /:"+count);
						if(count==2 || count == 1){
							int index = calculation.lastIndexOf(SMOConstant.DIVIDE);
							 field1 = calculation.substring(0,index);
							 field2 = calculation.substring(index+1,calculation.length());
							}else if(count >= 3){
								int index1 = calculation.indexOf(SMOConstant.DIVIDE);
								int index2 = calculation.indexOf(SMOConstant.DIVIDE,index1+1);
								int index3 = calculation.lastIndexOf(SMOConstant.DIVIDE);
								System.out.println("index1:"+index1);
								System.out.println("index2:"+index2);
								System.out.println("index3:"+index3);
								 field1 = calculation.substring(0,index2);
								 field2 = calculation.substring(index2+1,calculation.length());

							}*/
						ArrayList calcArray = String2Array.strSplit(calculation, SMOConstant.DIVIDE);
						if(calcArray != null && calcArray.size() > 0)
						{
							field1 = calcArray.get(0).toString();
							field2 = calcArray.get(1).toString();
						}
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(field1));
						userCalcVO.setFldId2(customReportDB.getFieldMetaDataDescForId(field2));
						userCalcVO.setField1(field1);
						userCalcVO.setField2(field2);
						userCalcVO.setOperator(SMOConstant.DIVIDE);
					}
					else if (calculation.lastIndexOf(SMOConstant.SUBTRACT)>=0)
					{
						/*char theChar = '-';
						int count = 0;
						count = countChars(calculation,theChar);
						System.out.println("count in -:"+count);
						if(count==2 || count == 1){
						int index = calculation.lastIndexOf(SMOConstant.SUBTRACT);
						 field1 = calculation.substring(0,index);
						 field2 = calculation.substring(index+1,calculation.length());
						}else if(count >= 3){
							int index1 = calculation.indexOf(SMOConstant.SUBTRACT);
							int index2 = calculation.indexOf(SMOConstant.SUBTRACT,index1+1);
							int index3 = calculation.lastIndexOf(SMOConstant.SUBTRACT);
							System.out.println("index1:"+index1);
							System.out.println("index2:"+index2);
							System.out.println("index3:"+index3);
							 field1 = calculation.substring(0,index2);
							 field2 = calculation.substring(index2+1,calculation.length());

						}*/
						ArrayList calcArray = String2Array.strSplit(calculation, SMOConstant.SUBTRACT);
						if(calcArray != null && calcArray.size() > 0)
						{
							field1 = calcArray.get(0).toString();
							field2 = calcArray.get(1).toString();
						}
						userCalcVO.setFldId1(customReportDB.getFieldMetaDataDescForId(field1));
						userCalcVO.setFldId2(customReportDB.getFieldMetaDataDescForId(field2));
						userCalcVO.setField1(field1);
						userCalcVO.setField2(field2);
						userCalcVO.setOperator(SMOConstant.SUBTRACT);
					}
				}
				userCalcVO.setCalcNum(calcNum);
				userCalcVO.setFormulaName(formulaName);
				userCalcVOList.add(userCalcVO);
			}
		}
		userCalcListVO.setUserCalcVOList(userCalcVOList);

		// setting to custReportCriteriaVO
		custReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		custReportCriteriaVO.setSortVOList(sortListVO);
		custReportCriteriaVO.setFieldsVOList(fieldsListVO);
		custReportCriteriaVO.setFilterVOList(filterListVO);
		custReportCriteriaVO.setSequenceFields(sequenceFields);
		custReportCriteriaVO.setUserCalcsVOList(userCalcListVO);
		return custReportCriteriaVO;
	}

	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	private static CustReportCriteriaVO constructMHSReportVO(DateSelectionForm dateSelectionForm)
	{

		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
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
			standardTimePeriodVO.setEndDate(dateSelectionForm.getStartSelectedTime2());
			timePeriodList.add(standardTimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime12() != null)
		{
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			comparePeriod1TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime12());
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getStartCompareTime12());
			timePeriodList.add(comparePeriod1TimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime22() != null)
		{
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			comparePeriod2TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime22());
			comparePeriod2TimePeriodVO.setEndDate(dateSelectionForm.getStartCompareTime22());
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
		custReportCriteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		return custReportCriteriaVO;
	}
	
	/**
	 * This method constructs the canned report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report DateSelectionAndComparisonVO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{

		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
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
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getStartCompareTime12());
			timePeriodList.add(comparePeriod1TimePeriodVO);
		}
		if (dateSelectionForm.getStartCompareTime22() != null)
		{
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			comparePeriod2TimePeriodVO.setStartDate(dateSelectionForm.getStartCompareTime22());
			comparePeriod2TimePeriodVO.setEndDate(dateSelectionForm.getStartCompareTime22());
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
	
	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	private static CustReportCriteriaVO constructCustomReportVO(DateSelectionForm dateSelectionForm)
	{

		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
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
//11897 Fixed the application errror issue		
		if (dateSelectionForm.getStartSelectedTime2() != null && dateSelectionForm.getStartSelectedTime2() != "" && dateSelectionForm.getStartSelectedTime2().length() > 0)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
			String startDate =dateSelectionForm.getStartSelectedTime2(); 
			String endDate =dateSelectionForm.getEndSelectedTime2(); 
	//		System.out.println("start.....end Time....."+startDate+"..."+endDate);
			int numberofDays=DateDifference(startDate,endDate);
			standardTimePeriodVO.setNumberOfDays(numberofDays);
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
		custReportCriteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		return custReportCriteriaVO;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */

	private static int DateDifference(String startDate,String endDate) {
		// TODO Auto-generated method stub
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    Date sDate = null;
	    Date eDate = null;
	    try {
			sDate=dateFormat.parse(startDate);
			eDate=dateFormat.parse(endDate);	
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		sDate.getTime();
		eDate.getTime();
	    long sDatemillisecond = sDate.getTime();;
	    long eDatemillisecond = eDate.getTime();;
	    long diff = eDatemillisecond - sDatemillisecond;
	    long diffDays = diff / (24 * 60 * 60 * 1000);
		return (int) diffDays;
	}

	/**
	 * This method constructs the custom report VO.
	 *
	 * @param advancedFiltersForm the advanced filters form
	 *
	 * @return the cust report criteria VO
	 */
	public static CustReportCriteriaVO constructCustomReportVO(AdvancedFiltersForm advancedFiltersForm)
	{
		FrontEdgeVO frontEdgeVO = null;
		FrontEdgeSubCategoryVO frontEdgeSubCategoryVO = null;
		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
		}
		/**
		 * ************************************** AdvancedOptions Completed( 1
		 * Issue ) ********
		 */
		// AdvancedFiltersForm advancedFiltersForm =
		// customReportingForm.getAdvancedFiltersForm();
		AdvancedFiltersVO advancedFiltersVO = new AdvancedFiltersVO();

		// dosage form
		String[] dosageForms = advancedFiltersForm.getDosageForms();
		DosageFormsListVO dosageFormsListVO = new DosageFormsListVO();
		ArrayList<String> dosageFormsList = new ArrayList<String>();
		for (int i = 0; i < dosageForms.length; i++)
		{
			if (dosageForms[i] != null)
			{
				 dosageFormsList.add(dosageForms[i]);
			 }
		}
		dosageFormsListVO.setDosageForms(dosageFormsList);
		advancedFiltersVO.setDosageFormsList(dosageFormsListVO);

		// dosage strength
		advancedFiltersVO.setDosageStrength(advancedFiltersForm.getDosageStrength());

		// mica department
		String[] mica = advancedFiltersForm.getMica();
		MicaListVO micaListVO = new MicaListVO();
		MicaVO micaVO = null;
		ArrayList<MicaVO> micaList = new ArrayList<MicaVO>();
		for (int i = 0; i < mica.length; i++)
		{
			micaVO = new MicaVO();
			micaVO.setMicaDeptId(mica[i]);
			micaList.add(micaVO);
		}
		micaListVO.setMica(micaList);
		advancedFiltersVO.setMicaList(micaListVO);

		// front edge category
		String[] frontEdge = advancedFiltersForm.getFrontEdge();
		FrontEdgeListVO frontEdgeListVO = new FrontEdgeListVO();

		ArrayList<FrontEdgeVO> frontEdgeList = new ArrayList<FrontEdgeVO>();
		for (int i = 0; i < frontEdge.length; i++)
		{
			frontEdgeVO = new FrontEdgeVO();
			frontEdgeVO.setEpDeptCtgyId(frontEdge[i]);
			frontEdgeList.add(frontEdgeVO);
		}
		frontEdgeListVO.setFrontEdge(frontEdgeList);
		advancedFiltersVO.setFrontEdgeList(frontEdgeListVO);

		// front edge sub category
		String[] frontEdgeSubCategory = advancedFiltersForm.getFrontEdgeSubCategory();
		FrontEdgeSubCategoryListVO frontEdgeSubCategoryListVO = new FrontEdgeSubCategoryListVO();

		ArrayList<FrontEdgeSubCategoryVO> frontEdgeSubCategoryList = new ArrayList<FrontEdgeSubCategoryVO>();
		for (int i = 0; i < frontEdgeSubCategory.length; i++)
		{
			frontEdgeSubCategoryVO = new FrontEdgeSubCategoryVO();
			frontEdgeSubCategoryVO.setEpDeptCtgyId(frontEdgeSubCategory[i]);
			frontEdgeSubCategoryList.add(frontEdgeSubCategoryVO);
		}
		frontEdgeSubCategoryListVO.setFrontEdgeSubCategory(frontEdgeSubCategoryList);
		advancedFiltersVO.setFrontEdgeSubCategoryList(frontEdgeSubCategoryListVO);

		// Local Dept
		String localDept = advancedFiltersForm.getSelectedLocalDept();
		StringTokenizer localDeptTokenizer = new StringTokenizer(localDept, ",");
		LocalDeptListVO localDeptListVO = new LocalDeptListVO();
		ArrayList<String> localDeptList = new ArrayList();
		while (localDeptTokenizer.hasMoreTokens())
		{
			localDept = localDeptTokenizer.nextToken();
			localDeptList.add(localDept);
		}
		localDeptListVO.setLocalDeptList(localDeptList);
		advancedFiltersVO.setLocalDeptList(localDeptListVO);

		// drug schedule
		String[] drugSchedule = advancedFiltersForm.getDrugSchedule();
		DrugScheduleListVO drugScheduleListVO = new DrugScheduleListVO();
		DrugScheduleVO drugScheduleVO = null;
		ArrayList<DrugScheduleVO> drugScheduleList = new ArrayList();
		for (int i = 0; i < drugSchedule.length; i++)
		{
			drugScheduleVO = new DrugScheduleVO();
			drugScheduleVO.setDrugSchdDscr(drugSchedule[i]);
			drugScheduleList.add(drugScheduleVO);
		}
		drugScheduleListVO.setDrugScheduleList(drugScheduleList);
		advancedFiltersVO.setDrugScheduleList(drugScheduleListVO);

		// labeler codes
		LabelerCodesListVO labelerCodesListVO = new LabelerCodesListVO();
		ArrayList labelerCodesList = new ArrayList();
		String labelerCodes = advancedFiltersForm.getSelectedLabelerCodes();
		StringTokenizer labelerTokenizer = new StringTokenizer(labelerCodes, ",");
		while (labelerTokenizer.hasMoreTokens())
		{
			labelerCodes = labelerTokenizer.nextToken();
			labelerCodesList.add(labelerCodes);
		}
		labelerCodesListVO.setLabelerCodesList(labelerCodesList);
		advancedFiltersVO.setLabelerCodesList(labelerCodesListVO);

		// contract filter
		advancedFiltersVO.setContractFilter(advancedFiltersForm.getContractFilter());

		// contract lead numbers
		ContractLeadNumListVO leadNumListVO = new ContractLeadNumListVO();
		ArrayList contractLeadNumbersList = new ArrayList();

		String contractLeadNumbers = advancedFiltersForm.getSelectedContractLeadNum();
		StringTokenizer leadNumberTokenizer = new StringTokenizer(contractLeadNumbers, ",");
		while (leadNumberTokenizer.hasMoreTokens())
		{
			contractLeadNumbers = leadNumberTokenizer.nextToken();
			contractLeadNumbersList.add(contractLeadNumbers);
		}
		leadNumListVO.setContractLeadNumList(contractLeadNumbersList);
		advancedFiltersVO.setContractLeadNumList(leadNumListVO);
		
//		 exclude contract lead numbers
		ExcludeContractLeadNumListVO excludeLeadNumListVO = new ExcludeContractLeadNumListVO();
		ArrayList excludeContractLeadNumbersList = new ArrayList();

		String excludeContractLeadNumbers = advancedFiltersForm.getExcludedContractLeadNum();
		if (excludeContractLeadNumbers != null)
		{
		StringTokenizer excludeLeadNumberTokenizer = new StringTokenizer(excludeContractLeadNumbers, ",");
		while (excludeLeadNumberTokenizer.hasMoreTokens())
		{
			excludeContractLeadNumbers = excludeLeadNumberTokenizer.nextToken();
			excludeContractLeadNumbersList.add(excludeContractLeadNumbers);
		}
		excludeLeadNumListVO.setExcludeContractLeadNumList(excludeContractLeadNumbersList);
		advancedFiltersVO.setExcludeContractLeadNumList(excludeLeadNumListVO);
		}
		else 
			advancedFiltersVO.setExcludeContractLeadNumList(excludeLeadNumListVO);
			
		// multi source
		advancedFiltersVO.setMultiSource(advancedFiltersForm.getMultiSource());

		// hhc
		advancedFiltersVO.setHhc(advancedFiltersForm.getHhc());

		// private label
		advancedFiltersVO.setPrivateLabel(advancedFiltersForm.getPrivateLabel());

		// drop ship
		advancedFiltersVO.setDropShip(advancedFiltersForm.getDropShip());

		// dc filter
		advancedFiltersVO.setDcFilter(advancedFiltersForm.getDcFilter());
		
		// idb Dates
		advancedFiltersVO.setIdbDates(advancedFiltersForm.getIdbDates());
		
		
		//Preferred Item Indicator PDR Report
		advancedFiltersVO.setPrefItemIndicator(advancedFiltersForm.getPrefItemIndicator());
		System.out.println("advancedFiltersForm.getPrefItemIndicator().........."+advancedFiltersForm.getPrefItemIndicator());
		
		custReportCriteriaVO.setAdvancedFiltersVO(advancedFiltersVO);
		return custReportCriteriaVO;
	}

	/**
	 * This method constructs the custom report VO.
	 *
	 * @param customerSelectionForm the customer selection form
	 *
	 * @return the cust report criteria VO
	 */
	public static CustReportCriteriaVO constructCustomReportVO(CustomerSelectionForm customerSelectionForm)
	{
		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
		}
		/**
		 * ************************************** Customer Low(completed) & High
		 * ***************
		 */
		// CustomerSelectionForm customerSelectionForm =
		// customReportingForm.getCustomerSelectionForm();
		CustomerVO customerVO = new CustomerVO();
		// -------------- Account Table selection -------------------
		AccountDetailsListVO accountDetailsListVO = new AccountDetailsListVO();
		AccountDetailsVO accountDetailsVO = null;
		ArrayList<AccountDetailsVO> accountDetailsVOList = new ArrayList();
		int selectedCustomerRowLength = customerSelectionForm.getSelectedCustomerRowLength();
		String[] selectedCustomerRowData = customerSelectionForm.getSelectedCustomerRowData();
		String accountNum = null;
		String accountName = null;
		for (int i = 0; i < selectedCustomerRowLength; i++)
		{
			accountDetailsVO = new AccountDetailsVO();
			StringTokenizer tokenizer = new StringTokenizer(selectedCustomerRowData[i], "*");
			while (tokenizer.hasMoreElements())
			{

				accountNum = (String) tokenizer.nextElement();
				accountName = (String) tokenizer.nextElement();
				accountDetailsVO.setAccountNum(accountNum);
				accountDetailsVO.setAccountName(accountName);
			}
			accountDetailsVOList.add(accountDetailsVO);
		}
		accountDetailsListVO.setAccountDetailsVOList(accountDetailsVOList);
		customerVO.setAccountDetailsListVO(accountDetailsListVO);

		// -------------- OSDDepartment table selection --------------
		AccountListVO accountListVO = new AccountListVO();
		AccountVO accountVO = null;
		ArrayList<AccountVO> accountVOList = new ArrayList();
		int selectedOSDDepartmentRowLength = customerSelectionForm.getSelectedOSDDepartmentRowLength();
		String[] selectedOSDDepartmentRowData = customerSelectionForm.getSelectedOSDDepartmentRowData();
		String osdDepartNum = null;
		String osdAccountNum = null;
		for (int i = 0; i < selectedOSDDepartmentRowLength; i++)
		{
			accountVO = new AccountVO();
			StringTokenizer tokenizer = new StringTokenizer(selectedOSDDepartmentRowData[i], "*");
			while (tokenizer.hasMoreElements())
			{

				osdDepartNum = (String) tokenizer.nextElement();
				// SO-143
				if (!"<No Department Assigned>".equalsIgnoreCase(osdDepartNum)) {
					osdAccountNum = (String) tokenizer.nextElement();
					accountVO.setDepartName(osdDepartNum);
					accountVO.setAccountNum(osdAccountNum);
				} else {
					osdAccountNum = (String) tokenizer.nextElement();
					accountVO.setDepartName("NULL");
					accountVO.setAccountNum(osdAccountNum);
					

				}
			}
			accountVOList.add(accountVO);
		}
		accountListVO.setAccountVOList(accountVOList);
		customerVO.setAccountsVOList(accountListVO);

		// -------------- IDBDepartment Table selection --------------
		IDBAccountListVO idbAccountListVO = new IDBAccountListVO();
		IDBAccountVO idbAccountVO = null;
		ArrayList<IDBAccountVO> idbAccountVOList = new ArrayList();
		int selectedIDBDepartmentRowLength = customerSelectionForm.getSelectedIDBDepartmentRowLength();
		String[] selectedIDBDepartmentRowData = customerSelectionForm.getSelectedIDBDepartmentRowData();
		String idbAccountNum = null;
		String idbAccountName = null;
		//String mckAccountNum = null;
		for (int i = 0; i < selectedIDBDepartmentRowLength; i++)
		{
			idbAccountVO = new IDBAccountVO();
			StringTokenizer tokenizer = new StringTokenizer(selectedIDBDepartmentRowData[i], "*");
			while (tokenizer.hasMoreElements())
			{

				idbAccountNum = (String) tokenizer.nextElement();
				// for (int j = 0; j < 6; j++)
				// tokenizer.nextElement();
				idbAccountName = (String) tokenizer.nextElement();
				idbAccountVO.setIdbAccountNum(idbAccountNum);
				idbAccountVO.setIdbAccountName(idbAccountName);
				//idbAccountVO.setMckAccountNum(mckAccountNum);
			}
			idbAccountVOList.add(idbAccountVO);
		}
		idbAccountListVO.setIdbAccountList(idbAccountVOList);
		customerVO.setIdbAccountsVOList(idbAccountListVO);

		//customer selection-tree
		int type = customerSelectionForm.getAccountType();
		if (type == 2)
		{
		UserRolesVO userRolesVO =  new UserRolesVO();
		ArrayList<String> customerHighAL = new ArrayList<String>();
		ArrayList<String> customerHighLevel = new ArrayList<String>();

		String[] selectedCustomerHighRowTreeData = customerSelectionForm.getSelectedCustomerHighRowTreeData();
		String[] selectedCustomerLevel = customerSelectionForm.getSelectedCustomerLevel();
		for (int i = 0; i < selectedCustomerHighRowTreeData.length; i++)
		{
			if(selectedCustomerHighRowTreeData[i]!=null){
				customerHighAL.add(selectedCustomerHighRowTreeData[i]);
			}
			if(selectedCustomerLevel[i]!=null){
				customerHighLevel.add(selectedCustomerLevel[i]);
			}
		}
		ArrayList<RoleVO> cidList = new ArrayList<RoleVO>();
		ArrayList<RoleVO> hspList = new ArrayList<RoleVO>();
		ArrayList<RoleVO> chnList = new ArrayList<RoleVO>();
		ArrayList<RoleVO> slsList = new ArrayList<RoleVO>();

		for (int i = 0; i < customerHighAL.size(); i++)
		{
			String[] keyType = (customerHighAL.get(i).toString()).split(",");
			if (keyType.length > 0)
			{
				if (keyType[0].equals(cidString))
				{
					RoleVO roleVO = new RoleVO();
					roleVO.setKey(customerHighAL.get(i).toString());
					roleVO.setRoleCode(cidString);
					cidList.add(roleVO);
				}

				else if (keyType[0].equals(hspString))
				{
					RoleVO roleVO = new RoleVO();
					roleVO.setKey(customerHighAL.get(i).toString());
					roleVO.setRoleCode(hspString);
					hspList.add(roleVO);
				}

				else if (keyType[0].equals(chnString))
				{
					RoleVO roleVO = new RoleVO();
					roleVO.setKey(customerHighAL.get(i).toString());
					roleVO.setRoleCode(chnString);
					chnList.add(roleVO);
				}

				else if (keyType[0].equals(slsString))
				{
					RoleVO roleVO = new RoleVO();
					roleVO.setKey(customerHighAL.get(i).toString());
					roleVO.setRoleCode(slsString);
					slsList.add(roleVO);
				}
				else{
					RoleVO roleVO = new RoleVO();
					roleVO.setKey(customerHighAL.get(i).toString());
					roleVO.setRoleCode(cidString);
					cidList.add(roleVO);
				}
			}

		}
		userRolesVO.setCidRoles(cidList);
		userRolesVO.setHspRoles(hspList);
		userRolesVO.setChnRoles(chnList);
		userRolesVO.setSlsRoles(slsList);
		userRolesVO.setLevel(customerHighLevel);
		customerVO.setRoles(userRolesVO);
		}
		else if (type == 1)
		{
		//Customer High Table View 
		UserRolesVO userRolesVO =  new UserRolesVO();
		int selectedCustomerHighRowLength = customerSelectionForm.getSelectedCustomerHighRowLength();
		String[] selectedCustomerHighRowData = customerSelectionForm.getSelectedCustomerHighRowData();

		/*ArrayList<RoleVO> cidList = new ArrayList<RoleVO>();
		ArrayList<RoleVO> hspList = new ArrayList<RoleVO>();
		ArrayList<RoleVO> chnList = new ArrayList<RoleVO>();
		ArrayList<RoleVO> slsList = new ArrayList<RoleVO>();

		for (int i = 0; i < selectedCustomerHighRowLength; i++)
		{
			idbAccountVO = new IDBAccountVO();
			StringTokenizer tokenizer = new StringTokenizer(selectedCustomerHighRowData[i], "*");
			while (tokenizer.hasMoreElements())
			{
				String key = tokenizer.nextElement().toString();
				String[] keyType = key.split(",");
				if (keyType.length > 0)
				{
					if (keyType[0].equals(cidString))
					{
						RoleVO roleVO = new RoleVO();
						roleVO.setKey(key);
						roleVO.setRoleCode(cidString);
						cidList.add(roleVO);
					}

					else if (keyType[0].equals(hspString))
					{
						RoleVO roleVO = new RoleVO();
						roleVO.setKey(key);
						roleVO.setRoleCode(hspString);
						hspList.add(roleVO);
					}

					else if (keyType[0].equals(chnString))
					{
						RoleVO roleVO = new RoleVO();
						roleVO.setKey(key);
						roleVO.setRoleCode(chnString);
						chnList.add(roleVO);
					}

					else if (keyType[0].equals(slsString))
					{
						RoleVO roleVO = new RoleVO();
						roleVO.setKey(key);
						roleVO.setRoleCode(slsString);
						slsList.add(roleVO);
					}
					else{
						RoleVO roleVO = new RoleVO();
						roleVO.setKey("CID," +key);
						roleVO.setRoleCode(cidString);
						cidList.add(roleVO);
					}
				}else if (key!=null){
					RoleVO roleVO = new RoleVO();
					roleVO.setKey("CID," +key);
					roleVO.setRoleCode(cidString);
					cidList.add(roleVO);
				}
			}

		}
		userRolesVO.setCidRoles(cidList);
		userRolesVO.setHspRoles(hspList);
		userRolesVO.setChnRoles(chnList);
		userRolesVO.setSlsRoles(slsList);
		customerVO.setRoles(userRolesVO);*/
		
		for (int i = 0; i < selectedCustomerHighRowLength; i++)
		{
			accountDetailsVO = new AccountDetailsVO();
			StringTokenizer tokenizer = new StringTokenizer(selectedCustomerHighRowData[i], "*");
			if (tokenizer.hasMoreElements())
			{
				
				accountNum = " ";
				accountName = " ";
				String assocPrimaryAccountNum = " ";
				String primaryFillDC = " ";
				String nationalGroup = " ";
				String subGroup = " ";
				String regionID = " ";
				String district = " ";
				String chainNumber = " ";
				String addr1 = " ";
				String addr2 = " ";
				String state = " ";
				String city = " ";
				String dea = " ";
				String hin = " ";
				if (tokenizer.hasMoreElements())
				{
					String data = " ";
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							accountNum = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							accountName = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							assocPrimaryAccountNum = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							primaryFillDC = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							nationalGroup = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							subGroup = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							regionID = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							district = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							chainNumber = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							addr1 = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							addr2 = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							state = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							city = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							dea = data;
					}
					if (tokenizer.hasMoreElements())
					{
						data = (String) tokenizer.nextElement();
						if(data.indexOf("undefined")<0 && data.trim() !="")
							hin = data;
					}
					
				}
				accountDetailsVO.setAccountNum(accountNum);
				accountDetailsVO.setAccountName(accountName);
				accountDetailsVO.setAssocPrimaryAccountNum(assocPrimaryAccountNum);
				accountDetailsVO.setPrimaryFillDC(primaryFillDC);
				accountDetailsVO.setNationalGroup(nationalGroup);
				accountDetailsVO.setSubGroup(subGroup);
				accountDetailsVO.setRegionID(regionID);
				accountDetailsVO.setDistrict(district);
				accountDetailsVO.setChainNum(chainNumber);
				accountDetailsVO.setAddress1(addr1);
				accountDetailsVO.setAddress2(addr2);
				accountDetailsVO.setState(state);
				accountDetailsVO.setCity(city);
				accountDetailsVO.setDeaNum(dea);
				accountDetailsVO.setHinNum(hin);
				accountDetailsVOList.add(accountDetailsVO);
				
				
			}
			
			
		}
		accountDetailsListVO.setAccountDetailsVOList(accountDetailsVOList);
		customerVO.setAccountDetailsListVO(accountDetailsListVO);
	
		
	}

		custReportCriteriaVO.setCustomerVO(customerVO);

		//customer selection High save

		return custReportCriteriaVO;
	}

	/**
	 * This method constructs the custom report VO.
	 *
	 * @param supplierSelectionForm the supplier selection form
	 *
	 * @return the cust report criteria VO
	 */
	public static CustReportCriteriaVO constructCustomReportVO(SupplierSelectionForm supplierSelectionForm)
	{

		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
		}

		/**
		 * ************************************** Supplier Selection completed
		 * *****************
		 */
		// SupplierSelectionForm supplierSelectionForm =
		// customReportingForm.getSupplierSelectionForm();
		String[] selectedSupplierData = supplierSelectionForm.getSelectedSupplierData();
		int selectedSupplierLength = supplierSelectionForm.getSelectedSupplierLength();
		String supplierView = supplierSelectionForm.getSupplierView();
		SupplierListVO supplierListVO = new SupplierListVO();
		SupplierVO supplierVO = null;
		ArrayList<SupplierVO> supplierVOList = new ArrayList<SupplierVO>();
		String supplierCode = null;
		String supplierName = null;
		if(supplierView.equals("code")){
		for (int i = 0; i < selectedSupplierLength; i++)
		{
			StringTokenizer tokenizer = new StringTokenizer(selectedSupplierData[i], ",");
			while (tokenizer.hasMoreElements())
			{
				supplierVO = new SupplierVO();
				supplierCode = (String) tokenizer.nextElement();
				if(tokenizer.hasMoreElements())
				{
					supplierName = (String) tokenizer.nextElement();
				}
					if(supplierCode.indexOf("undefined")<0 && supplierCode.trim() !="")
				supplierVO.setSupplierCode(supplierCode);
					if(supplierName.indexOf("undefined")<0 && supplierName.trim() !="")
				supplierVO.setSupplierName(supplierName);
			}
			supplierVOList.add(supplierVO);
			}
		}else{
		//	for (int i = 0; i < selectedSupplierLength; i++)
		//	{
		//		StringTokenizer tokenizer = new StringTokenizer(selectedSupplierData[i], ",");
		//		while (tokenizer.hasMoreElements())
		///	{
					
		//			supplierName = (String) tokenizer.nextElement();
			//Issue 4298 changes done by Anil Kumar
					for(int l=0;l<selectedSupplierData.length;l++){
						supplierName=selectedSupplierData[l];
						if(supplierName!=null){
							supplierName=supplierName.substring(1,supplierName.length());
							int length=supplierName.length();
						for(int i=0;i<supplierName.length();i++){
							Character c=supplierName.charAt(i);
							char c1=supplierName.charAt(i);
							if((int)c1>128){
								
								supplierName=supplierName.replace(c1,' ');
							}
						}
						
						
						supplierName=supplierName.replaceAll("  "," ");
						supplierName=supplierName.replaceAll("'","''");
						supplierVO = new SupplierVO();
						
							if(supplierName.indexOf("undefined")<0 && supplierName.trim() !="")
							supplierVO.setSupplierName(supplierName);
							supplierVOList.add(supplierVO);
						}
				}
	//			}
			
	//		}
			
		}
		supplierListVO.setSupplierView(supplierView);
		supplierListVO.setSuppliers(supplierVOList);
		custReportCriteriaVO.setSupplierVO(supplierListVO);
		return custReportCriteriaVO;
	}

	/**
	 * This method constructs the custom report VO.
	 *
	 * @param itemSelectionForm the item selection form
	 *
	 * @return the cust report criteria VO
	 */
	public static CustReportCriteriaVO constructCustomReportVO(ItemSelectionForm itemSelectionForm)
	{

		if (custReportCriteriaVO == null)
		{
			custReportCriteriaVO = new CustReportCriteriaVO();
		}
		/**
		 * ***************Item Selection pages(Tree Pending) **************
		 */
		// ItemSelectionForm itemSelectionForm =
		// customReportingForm.getItemSelectionForm();
		ItemVO itemVO = new ItemVO();
		// -------------- Generic Table selection --------------------
		GenericListVO genericListVO = new GenericListVO();
		GenericVO genericVO = new GenericVO();
		;
		ArrayList<GenericVO> genericVOList = new ArrayList();
		int selectedGenericLength = itemSelectionForm.getSelectedGenericLength();
		String[] selectedGenericData = itemSelectionForm.getSelectedGenericData();
		
		String genericCode = null;
		String genericDescription = null;
		for (int i = 0; i < selectedGenericLength; i++)
		{
			StringTokenizer tokenizer = new StringTokenizer(selectedGenericData[i], "*");
			while (tokenizer.hasMoreElements())
			{
				genericVO = new GenericVO();
				genericCode = (String) tokenizer.nextElement();
				genericDescription = (String) tokenizer.nextElement();
				genericVO.setCode(genericCode);
				genericVO.setDescription(genericDescription);
				genericVOList.add(genericVO);
			}
		}

		genericListVO.setGenericVOList(genericVOList);
		itemVO.setGenericGroupsList(genericListVO);

		// -------------- Item Number Table selection ----------------
		ItemNumbersListVO itemNumbersListVO = new ItemNumbersListVO();
		ItemDetailsVO itemDetailsVO = new ItemDetailsVO();
		ArrayList<ItemDetailsVO> itemDetailsVOList = new ArrayList();
		int selectedItemNumberLength = itemSelectionForm.getSelectedItemNumberLength();
		String[] selectedItemNumberData = itemSelectionForm.getSelectedItemNumberData();
		String itemNum = null;
		for (int i = 0; i < selectedItemNumberLength; i++)
		{
			StringTokenizer tokenizer = new StringTokenizer(selectedItemNumberData[i], "*");
			itemDetailsVO = new ItemDetailsVO();
			String ndc = "";
			String itemDescription ="";
			String genericDesc ="";
			String upc = "";
			String supplierName ="";
			String drugSchCode ="";
			String brandGenericInd = "";
			String mica = "";
			String mfgSize = "";
			if (tokenizer.hasMoreElements())
			{
				String data = "";
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
						itemNum = data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
					
						ndc = data;

				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 itemDescription = data;
				}
				if (tokenizer.hasMoreElements())
				{

					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 genericDesc =data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 upc = data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 supplierName = data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 drugSchCode = data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 brandGenericInd = data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 mica = data;
				}
				if (tokenizer.hasMoreElements())
				{
					data = (String) tokenizer.nextElement();
					if(data.indexOf("undefined")<0)
				 mfgSize = data;
				}

				itemDetailsVO.setItemNum(itemNum);
				itemDetailsVO.setNdc(ndc);
				itemDetailsVO.setItemDescription(itemDescription);
				itemDetailsVO.setGenericDesc(genericDesc);
				itemDetailsVO.setUpc(upc);
				itemDetailsVO.setSupplierName(supplierName);
				itemDetailsVO.setDrugSchCode(drugSchCode);
				itemDetailsVO.setBrandGenericInd(brandGenericInd);
				itemDetailsVO.setMica(mica);
				itemDetailsVO.setMfgSize(mfgSize);
				if(itemNum!=null){
					itemDetailsVOList.add(itemDetailsVO);
				}
			}
		}

		itemNumbersListVO.setItemNumberList(itemDetailsVOList);
		itemNumbersListVO.setPurchasedItemOnly(itemSelectionForm.isPurchasedItemOnly());
		itemNumbersListVO.setMckItems(itemSelectionForm.isMckItems());
		itemNumbersListVO.setNonMckItems(itemSelectionForm.isNonMckItems());
		itemVO.setItemNumbersList(itemNumbersListVO);

		// -------------- Therapeutic Tree selection TO DO
		String selectedTherapeuticTreeData = itemSelectionForm.getSelectedTherapeuticTreeData();
		ArrayList<String> therapeuticAL = String2Array.strArr(selectedTherapeuticTreeData, "*");
		ArrayList therapeuticList = new ArrayList();

		int therapeuticCount = 0;
		ArrayList<TherapeuticVO> therapeuticVOList = new ArrayList();
		TherapeuticVO therapeuticVO = null;
		for (int i = 0; i < therapeuticAL.size(); i++)
		{
			therapeuticVO = new TherapeuticVO();
			String[] therapeuticData = therapeuticAL.get(i).split(":");
			String therapeuticCode = "";
			if (therapeuticData.length > 0)
			{
				//therapeuticCode = Integer.parseInt(therapeuticData[0]);
				therapeuticCode = therapeuticData[0];
			}
			therapeuticList.add(therapeuticCode);
			therapeuticVO.setCode(therapeuticCode);
			therapeuticCount = 0;
			therapeuticVOList.add(therapeuticVO);
			therapeuticCount++;
		}
		TherapeuticListVO therapeuticListVO = new TherapeuticListVO();
		therapeuticListVO.setTherapeuticList(therapeuticVOList);
		itemVO.setTherapeuticList(therapeuticListVO);
		custReportCriteriaVO.setItemVO(itemVO);
		return custReportCriteriaVO;
	}

	/**
	 * This method counts the chars.
	 *
	 * @param theChar the the char
	 * @param theString the the string
	 *
	 * @return the int
	 */
	public static int countChars(String theString, char theChar){
		   int count  = 0;
		   int in = theString.indexOf(theChar);
		   //if(in!=-1)
		   //   count++;
		   while (in != -1){
		      in = theString.indexOf(theChar, in+1);
		      count++;
		   }
		   return count;

		}


}

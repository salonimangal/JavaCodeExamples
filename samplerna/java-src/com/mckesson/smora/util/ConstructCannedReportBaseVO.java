/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.AccountListVO;
import com.mckesson.smora.dto.AccountVO;
import com.mckesson.smora.dto.AdvancedFiltersVO;
import com.mckesson.smora.dto.ContractLeadNumListVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.CustReportCriteriaVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DosageFormsListVO;
import com.mckesson.smora.dto.DrugScheduleListVO;
import com.mckesson.smora.dto.DrugScheduleVO;
import com.mckesson.smora.dto.FrontEdgeListVO;
import com.mckesson.smora.dto.FrontEdgeSubCategoryListVO;
import com.mckesson.smora.dto.FrontEdgeSubCategoryVO;
import com.mckesson.smora.dto.FrontEdgeVO;
import com.mckesson.smora.dto.GenericListVO;
import com.mckesson.smora.dto.GenericVO;
import com.mckesson.smora.dto.IDBAccountListVO;
import com.mckesson.smora.dto.IDBAccountVO;
import com.mckesson.smora.dto.ItemDetailsVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.LabelerCodesListVO;
import com.mckesson.smora.dto.LocalDeptListVO;
import com.mckesson.smora.dto.MicaListVO;
import com.mckesson.smora.dto.MicaVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.SupplierListVO;
import com.mckesson.smora.dto.SupplierVO;
import com.mckesson.smora.dto.TherapeuticListVO;
import com.mckesson.smora.dto.TherapeuticVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;

import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.util.ConstructReportBaseVO;

/**
 * This is the Reusable class for constructing the CannedReport 
 */
public class ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructCannedReportBaseVO";

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

	/**
	 * The cust report criteria VO.
	 */
	public static CriteriaVO criteriaVO = null;

   /**
	 * This method constructs the canned report VO.
	 *
	 * @param advancedFiltersForm the advanced filters form
	 *
	 * @return criteria VO
	 */
	public static CriteriaVO constructCannedReportVO(AdvancedFiltersForm advancedFiltersForm)
	{
		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
		CustReportCriteriaVO custReportCriteriaVO = ConstructReportBaseVO.constructCustomReportVO(advancedFiltersForm);
		criteriaVO.setAdvancedFiltersVO(custReportCriteriaVO.getAdvancedFiltersVO());
		return criteriaVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param customerSelectionForm the customer selection form
	 *
	 * @return criteria VO
	 */
	public static CriteriaVO constructCannedReportVO(CustomerSelectionForm customerSelectionForm)
	{
		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
		CustReportCriteriaVO custReportCriteriaVO = ConstructReportBaseVO.constructCustomReportVO(customerSelectionForm);
		criteriaVO.setCustomerVO(custReportCriteriaVO.getCustomerVO());
		return criteriaVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param supplierSelectionForm the supplier selection form
	 *
	 * @return criteria VO
	 */
	public static CriteriaVO constructCannedReportVO(SupplierSelectionForm supplierSelectionForm)
	{

		if (criteriaVO == null)
		{
			criteriaVO = new CustReportCriteriaVO();
		}

		CustReportCriteriaVO custReportCriteriaVO = ConstructReportBaseVO.constructCustomReportVO(supplierSelectionForm);
		criteriaVO.setSupplierVO(custReportCriteriaVO.getSupplierVO());
		return criteriaVO;
	}

	/**
	 * This method constructs the canned report VO.
	 *
	 * @param itemSelectionForm the item selection form
	 *
	 * @return criteria VO
	 */
	public static CriteriaVO constructCannedReportVO(ItemSelectionForm itemSelectionForm)
	{

		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
		CustReportCriteriaVO custReportCriteriaVO = ConstructReportBaseVO.constructCustomReportVO(itemSelectionForm);
		criteriaVO.setItemVO(custReportCriteriaVO.getItemVO());
		return criteriaVO;
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
	
	/**
	 * This method converts the report base VO to XML.
	 * 
	 * @param reportBaseVO the report base VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String convertReportBaseVOToXML(ReportBaseVO reportBaseVO) throws SMORAException	
	{
		final String METHODNAME = "convertReportBaseVOToXML";
		return CriteriaTemplateUtil.convertVOToXML(reportBaseVO);
	}

}

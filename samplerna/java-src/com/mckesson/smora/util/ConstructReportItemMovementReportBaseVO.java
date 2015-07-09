/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
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
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ItemGroupVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportItemMovementFormattingVO;
import com.mckesson.smora.dto.ReportItemMovementVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportItemMovementForm;
import com.mckesson.smora.ui.form.ReportItemMovementFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

/**
 * This Class is used to Construct the ReportBaseVO for FiscalYTDReport
 */

public class ConstructReportItemMovementReportBaseVO extends
		ConstructCannedReportBaseVO
{

	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportItemMovementReportBaseVO";

	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(ConstructReportItemMovementReportBaseVO.class);

	/**
	 * The cust report criteria VO.
	 */
	private static ReportItemMovementVO reportItemMovementVO = null;

	private static CriteriaVO criteriaVO = null;

	private static ReportItemMovementFormattingVO reportItemMovementFormattingVO = null;

	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;

	// private static FiscalYTDGroupVO FiscalYTDGroupVO = null;

	/**
	 * This method populates the report base VO.
	 * 
	 * @param customReportingForm
	 *            the custom reporting form
	 * @return the report base VO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public static ReportBaseVO populateReportItemMovementReportBaseVO(ReportItemMovementForm reportItemMovementForm) throws SMORAException
	{
		
		reportItemMovementVO = new ReportItemMovementVO();
		criteriaVO = new CriteriaVO();		
		ArrayList<String> subTotalArray = new ArrayList<String>();
		ArrayList<String> fieldDescArray = new ArrayList<String>();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		ReportItemMovementFormattingSelectionForm reportItemMovementFormattingSelectionForm = reportItemMovementForm.getReportItemMovementFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = reportItemMovementForm.getDateSelectionForm();
		log.info("Date Selection start date" + dateSelectionForm.getStartSelectedTime1());
		log.info("Date Selection start date" + dateSelectionForm.getEndSelectedTime1());
		CustomerSelectionForm customerSelectionForm = reportItemMovementForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportItemMovementForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportItemMovementForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportItemMovementForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		ItemGroupVO itemGroupVO = new ItemGroupVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		log.info("custom heading is" + reportItemMovementFormattingSelectionForm.getCustomHeading());
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(reportItemMovementFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(reportItemMovementFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(reportItemMovementFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");		
		
		String accountFormat = reportItemMovementFormattingSelectionForm.getAccountFormat();		

		if (accountFormat.equalsIgnoreCase("Break By Account") /*&& !(baseVO.getReportSubtype() == ReportManagerConstants.REPORT_ITEM_MOVEMENT)*/)
		{
			subTotalArray.add("Total$");
			subTotalArray.add("%Total");

			fieldDescArray.add("Total$");
			fieldDescArray.add("%Total");
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldDescription("Account Number");
			firstGroupLevelVO.setFieldName("Account Number");

			firstGroupLevelVO.setFieldWidth(230);
			firstGroupLevelVO.setSubTotal(subTotalArray);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
		}
		else
		{
			groupLevelVO.setFirstGroupLevel(null);
		}
		

		SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
		secondGroupLevelVO.setClassType("java.lang.String");
		secondGroupLevelVO.setFieldDescription("McK Item #");
		secondGroupLevelVO.setFieldName("McK Item #");
		secondGroupLevelVO.setFieldWidth(73);
		secondGroupLevelVO.setSubTotal(subTotalArray);
		secondGroupLevelVO.setSubTotal(fieldDescArray);

		
		groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);
		groupLevelVO.setThirdGroupLevel(null);

		baseVO.setReportSubtype(ReportManagerConstants.REPORT_ITEM_MOVEMENT);
		baseVO.setHtml(reportItemMovementFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(reportItemMovementFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(reportItemMovementFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(reportItemMovementFormattingSelectionForm.isResultsDisplayCSV());

		reportItemMovementFormattingVO = constructCannedReportVO(reportItemMovementFormattingSelectionForm);

		reportItemMovementVO.setReportItemMovementFormattingVO(reportItemMovementFormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);		
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		log.info("customer group name ***************** \n" + customerSelectionForm.getAccountGroupName());
		log.info("customer group name ***************** \n");
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		List<TApplCnRptFldMetaData> metaDataList = null;		

		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();
		FieldsListVO fieldsListVO = new FieldsListVO();

		log.info("meta group id is " + baseVO.reportSubtype);
		
		metaDataList = cannedReportFieldMetaDataDB.getCnRptFldMetaData(baseVO.getReportSubtype());

		log.info("metaDataList report sub type" + baseVO.getReportSubtype());
		log.info("metaDataList size is" + metaDataList.size());

		if (metaDataList != null && metaDataList.size() > 0)
		{
			log.info("inside if...");
			baseVO.setReportGroupID(metaDataList.get(0).getId().getRptId());
			log.info("metaDataList.get(0).getId().getRptId()..." + metaDataList.get(0).getId().getRptId());
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData tApplCnRptFldMetaData = null;
			log.info("metaDataListSize..." + metaDataListSize);
			for (int i = 0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
				if(accountFormat.equals("Combine Accounts"))
				{
					if (tApplCnRptFldMetaData.getIsColHdr().equals("Y"))
					{
						int fieldId = tApplCnRptFldMetaData.getId().getFldId();
						log.info("fieldId" + fieldId);
						FieldsVO fieldsVO = new FieldsVO();
						fieldsVO.setFldId(fieldId);
						fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
						fieldsList.add(fieldsVO);
					}
				}
				else
				{
					int fieldId = tApplCnRptFldMetaData.getId().getFldId();
					log.info("fieldId" + fieldId);
					FieldsVO fieldsVO = new FieldsVO();
					fieldsVO.setFldId(fieldId);
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
					fieldsList.add(fieldsVO);					
				}
			}
		}

		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		itemGroupVO.setReportItemMovementVO(reportItemMovementVO);
		cannedReportVO.setItemGroupVO(itemGroupVO);
		cannedReportVO.setCannedReportTitle("28");
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		baseVO.setCannedReportVO(cannedReportVO);
		/*
		 * FirstGroupLevelVO firstGroupLevelVO =
		 * baseVO.getCannedReportVO().getCannedReportCriteriaVO().getGroupLevelVO().getFirstGroupLevel();
		 * if(firstGroupLevelVO != null) { String fieldName =
		 * firstGroupLevelVO.getFieldName(); log.info("field name inside
		 * getFirstGroupLevel issssssss "+fieldName); }
		 */

		return baseVO;
	}

	/**
	 * This method constructs the canned report VO.
	 * 
	 * @param report8020GenericCategoryFormattingSelectionForm
	 *            the report8020formatting selection form
	 * @return report8020GenericCategoryVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	private static ReportItemMovementFormattingVO constructCannedReportVO(ReportItemMovementFormattingSelectionForm reportItemMovementFormattingSelectionForm) throws SMORAException
	{
		if (reportItemMovementFormattingVO == null)
		{
			reportItemMovementFormattingVO = new ReportItemMovementFormattingVO();
		}

		// fiscalYTDFormattingVO.setCustomHeading(fiscalYTDFormattingSelectionForm.getCustomHeading());
		reportItemMovementFormattingVO.setAccountFormat(reportItemMovementFormattingSelectionForm.getAccountFormat());
		// fiscalYTDFormattingVO.setFiscalYearStartMonth(reportItemMovementFormattingSelectionForm.getFiscalYearStartMonth());
		reportItemMovementFormattingVO.setLimitResultsTo(reportItemMovementFormattingSelectionForm.getLimitResultsTo());
		// fiscalYTDFormattingVO.setFormat(fiscalYTDFormattingSelectionForm.getFormat());
		// fiscalYTDFormattingVO.setDetailLevel(fiscalYTDFormattingSelectionForm.getDetailLevel());
		reportItemMovementFormattingVO.setSortOptions(reportItemMovementFormattingSelectionForm.getSortOptions());
		return reportItemMovementFormattingVO;
	}

	/**
	 * This method constructs the custom report VO.
	 * 
	 * @param dateSelectionForm
	 *            the date selection form
	 * @return the cust report criteria VO
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
			String startTime = "";
			String endTime = "";
			try{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			if(dateSelectionForm.getStartSelectedTime1() != null && dateSelectionForm.getStartSelectedTime1().length() > 0)
			{	  
                startTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getStartSelectedTime1())) + "";
				//startTime = simpleDateFormat.format(new SimpleDateFormat("MMMMMM yyyy").parse(dateSelectionForm.getStartSelectedTime1())) + "";
			}
			if(dateSelectionForm.getEndSelectedTime1() != null && dateSelectionForm.getEndSelectedTime1().length() > 0)
			{
			 endTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getEndSelectedTime1())) + "";	
             //endTime = simpleDateFormat.format(new SimpleDateFormat("MMMMMM yyyy").parse(dateSelectionForm.getEndSelectedTime1())) + "";
			}
			standardTimePeriodVO.setStartDate(startTime);
			standardTimePeriodVO.setEndDate(endTime);
			timePeriodList.add(standardTimePeriodVO);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if (dateSelectionForm.getStartSelectedTime1() != null)
		{
			try{
			String startTime = "";
			String endTime = "";
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			if(dateSelectionForm.getStartSelectedTime1() != null && dateSelectionForm.getStartSelectedTime1().length() > 0)
			{
				startTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getStartSelectedTime1())) + "";
			}
			if(dateSelectionForm.getEndSelectedTime2() != null && dateSelectionForm.getEndSelectedTime2().length() > 0)
			{
				endTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getEndSelectedTime2())) + "";
			}
			standardTimePeriodVO.setStartDate(startTime);
			standardTimePeriodVO.setEndDate(endTime);
			timePeriodList.add(standardTimePeriodVO);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if (dateSelectionForm.getStartCompareTime12() != null)
		{
			try{
			String startTime = "";
			String endTime = "";
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			if(dateSelectionForm.getStartCompareTime12() != null && dateSelectionForm.getStartCompareTime12().length() > 0)
			{
				startTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getStartCompareTime12())) + "";
			}
			if(dateSelectionForm.getStartCompareTime12() != null && dateSelectionForm.getStartCompareTime12().length() > 0)
			{
				endTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getStartCompareTime12())) + "";
			}
			comparePeriod1TimePeriodVO.setStartDate(startTime);
			comparePeriod1TimePeriodVO.setEndDate(endTime);
			timePeriodList.add(comparePeriod1TimePeriodVO);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if (dateSelectionForm.getStartCompareTime22() != null)
		{
			try{
			String startTime = "";
			String endTime = "";
			TimePeriodVO comparePeriod2TimePeriodVO = new TimePeriodVO();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			if(dateSelectionForm.getStartCompareTime22() != null && dateSelectionForm.getStartCompareTime22().length() > 0)
			{
				startTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getStartCompareTime22())) + "";
			}
			if(dateSelectionForm.getStartCompareTime22() != null && dateSelectionForm.getStartCompareTime22().length() > 0)
			{
				endTime = simpleDateFormat.format(new SimpleDateFormat("MM/dd/yyyy").parse(dateSelectionForm.getStartCompareTime22())) + "";
			}
			comparePeriod2TimePeriodVO.setStartDate(startTime);
			comparePeriod2TimePeriodVO.setEndDate(endTime);
			timePeriodList.add(comparePeriod2TimePeriodVO);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
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

	// TODO This method is already implemented in the DAO.Need to move it to a
	// common place.
	public static int getNumberOfAccounts(CustomerVO customerVO)
	{
		int noOfAccnts = 0;
		ArrayList<AccountDetailsVO> accountDetailsVO = null;
		UserRolesVO userRolesVO = customerVO.getRoles();
		// TODO Logic to
		if (userRolesVO != null)
		{
			ArrayList<RoleVO> cidRoles = userRolesVO.getCidRoles();
			ArrayList<RoleVO> hspRoles = userRolesVO.getHspRoles();
			ArrayList<RoleVO> chnRoles = userRolesVO.getChnRoles();
			ArrayList<RoleVO> slsRoles = userRolesVO.getSlsRoles();
			if ((cidRoles != null && cidRoles.size() > 0) || (hspRoles != null && hspRoles.size() > 0) || (chnRoles != null && chnRoles.size() > 0) || (slsRoles != null && slsRoles.size() > 0))
			{
				AccountDB accountDB = new AccountDB();
				try
				{
					accountDetailsVO = accountDB.getAccountInfo(userRolesVO); // TODO
					// needs
					// to
					// confirm
					// on
					// rowlimits
					// parameter
				}
				catch (SMORAException e)
				{
					log.info("SMORAException : " + e);
					noOfAccnts = 0; // TODO this needs to be verified again
				}
				if (accountDetailsVO != null & accountDetailsVO.size() > 0)
				{
					noOfAccnts = accountDetailsVO.size();
				}
			}
			else
			{
				AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
				accountDetailsVO = accountDetailsListVO.getAccountDetailsVOList();
				if (accountDetailsVO != null)
				{
					noOfAccnts = accountDetailsVO.size();
				}

			}

		}
		else
		{
			AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
			accountDetailsVO = accountDetailsListVO.getAccountDetailsVOList();
			if (accountDetailsVO != null)
			{
				noOfAccnts = accountDetailsVO.size();
			}

		}
		return noOfAccnts;
	}
}
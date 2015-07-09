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
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.ReportControlledSubstanceVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.ThirdGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.ReportControlledSubstanceForm;
import com.mckesson.smora.ui.form.ReportControlledSubstanceFormattingSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.dto.ControlledSubstanceFormattingVO;
/**
 * 
 * This Class is used to Construct the ReportBaseVO for Time Series  - Time Period Comparison Reports
 * 
 */
public class ConstructReportControlledSubstanceReportBaseVO extends ConstructCannedReportBaseVO
{
	/**
	 * This method populates the report base VO.
	 *
	 * @param customReportingForm the custom reporting form
	 *
	 * @return the report base VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	protected static Log log = LogFactory.getLog(ConstructReportControlledSubstanceReportBaseVO.class);
	
	private static ControlledSubstanceFormattingVO controlledSubstanceFormattingVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
	
	public static ReportBaseVO populateControlledSubstanceReportBaseVO(ReportControlledSubstanceForm reportControlledSubstanceForm , String userId) throws SMORAException
	{
		ArrayList<String> fieldDescArray = new ArrayList<String>();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		List<TApplCnRptFldMetaData> metaDataList = null;
		ReportControlledSubstanceVO controlledSubstanceVO = new ReportControlledSubstanceVO();;
		FieldsListVO fieldsListVO = new FieldsListVO();
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB = new CannedReportFieldMetaDataDB();
		
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();
		criteriaVO = new CriteriaVO();		
		controlledSubstanceFormattingVO = new ControlledSubstanceFormattingVO();
		ReportControlledSubstanceFormattingSelectionForm formattingSelectionForm = reportControlledSubstanceForm.getFormattingSelectionForm();
		controlledSubstanceFormattingVO = constructCannedReportVO(formattingSelectionForm);
		DateSelectionForm dateSelectionForm = reportControlledSubstanceForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = reportControlledSubstanceForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = reportControlledSubstanceForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = reportControlledSubstanceForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = reportControlledSubstanceForm.getAdvancedFiltersForm();
		
		//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(formattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(formattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(formattingSelectionForm.getCustomHeading());
		baseVO.setHtml(formattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(formattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(formattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(formattingSelectionForm.isResultsDisplayCSV());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_CONTROLLED_SUBSTANCE_GRP_ID);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_CONTROLLED_SUBSTANCE_GRP_ID);
		
		String format = formattingSelectionForm.getAccountFormat();		
		/**
		 * if Break is selected in the Account format(in formatting tab)
		 */
		if(format.equals("Break By Account"))
		{
			/**
			 * Set classtype,fieldName,fieldwidth and empty subtotal for Cust_acct_id ,in firstGroupLevelVO.
			 */
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldName("Acct #");
			firstGroupLevelVO.setFieldWidth(100);
			firstGroupLevelVO.setSubTotal(fieldDescArray);
			/**
			 * Set FirstGroupLevelVO in to groupLevelVO,For this report we are not using ThirdGroupLevelVO,
			 * so set ThirdGroupLevel as null in groupLevelVO.
			 */
			groupLevelVO.setFirstGroupLevel(firstGroupLevelVO);
			groupLevelVO.setThirdGroupLevel(null);
		
		}
		else
		{
			/**
			 * if Break by is not selected in the Account format(in formatting tab),set FirstGroupLevel as null in groupLevelVO.
			 */
			groupLevelVO.setFirstGroupLevel(null);
			groupLevelVO.setThirdGroupLevel(null);
		}
		
		if(controlledSubstanceFormattingVO.getGroupBy().equals("No Grouping"))
		{
			/**
			 * if No Grouping is selected set SecondGroupLevel as null in groupLevelVO.
			 */
			groupLevelVO.setSecondGroupLevel(null);	
			
		}
		else
		{
			/***
			 * if anything is selected in the GroupingLevel except "No Grouping"(in Formatting tab),
			 * Set classtype,fieldName,fieldwidth and empty subtotal (for which selected in GroupingLevel)in secondGroupLevelVO.
			 */
			SecondGroupLevelVO secondGroupLevelVO = new SecondGroupLevelVO();
			secondGroupLevelVO.setClassType("java.lang.String");
			if(controlledSubstanceFormattingVO.getGroupBy().equals("Invoice Date"))
			{
				secondGroupLevelVO.setFieldDescription("Inv Date");
				secondGroupLevelVO.setFieldName("Inv Date");
			}
			else if(controlledSubstanceFormattingVO.getGroupBy().equals("Drug Schedule Code"))
			{
				secondGroupLevelVO.setFieldDescription("Sch");
				secondGroupLevelVO.setFieldName("Sch");
			}
			else
			{	
			secondGroupLevelVO.setFieldDescription(controlledSubstanceFormattingVO.getGroupBy());
			secondGroupLevelVO.setFieldName(controlledSubstanceFormattingVO.getGroupBy());			
			secondGroupLevelVO.setFieldWidth(200);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			}
			/**
			 * Set secondGroupLevelVO in to groupLevelVO,
			 */
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);	
		}		
		
		ThirdGroupLevelVO thirdGroupLevelVO = new ThirdGroupLevelVO();
		thirdGroupLevelVO.setClassType("java.lang.String");

		thirdGroupLevelVO.setFieldDescription("McK Item #");
		thirdGroupLevelVO.setFieldName("McK Item #");
		thirdGroupLevelVO.setFieldWidth(200);
		thirdGroupLevelVO.setSubTotal(fieldDescArray);
		groupLevelVO.setThirdGroupLevel(thirdGroupLevelVO);

		//populating the Time Series Comparison and Time Series Group VOs
		controlledSubstanceFormattingVO =  constructCannedReportVO(formattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		
		controlledSubstanceVO.setControlledSubstanceFormattingVO(controlledSubstanceFormattingVO);
		
		//populating the Criteria VO
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		
		
		
		//populating the FieldsList
		metaDataList  = cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.info("metaDataList \n" +metaDataList);
		fieldsList = getFieldsList(metaDataList, controlledSubstanceFormattingVO, userId);
		fieldsListVO.setFieldsVOList(fieldsList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		
		//populating the Canned reportVO and ReportBaseVO
//		cannedReportVO.setTimeSeriesGroupVO(timeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("20");
		cannedReportVO.setControlledSubstanceVO(controlledSubstanceVO);
		baseVO.setCannedReportVO(cannedReportVO);
		
		return baseVO;
	}
	
	private static ControlledSubstanceFormattingVO constructCannedReportVO(ReportControlledSubstanceFormattingSelectionForm reportControlledSubstanceFormattingSelectionForm) throws SMORAException
	{
		if (controlledSubstanceFormattingVO== null)
		{
			controlledSubstanceFormattingVO = new ControlledSubstanceFormattingVO();
		}
		
		controlledSubstanceFormattingVO.setAccountFormat(reportControlledSubstanceFormattingSelectionForm.getAccountFormat());
		controlledSubstanceFormattingVO.setFormat(reportControlledSubstanceFormattingSelectionForm.getFormat());
		controlledSubstanceFormattingVO.setGroupBy(reportControlledSubstanceFormattingSelectionForm.getGroupBy());
		controlledSubstanceFormattingVO.setSortOptions(reportControlledSubstanceFormattingSelectionForm.getSortOptions());
		return controlledSubstanceFormattingVO;
	}
	/**
	 * This method constructs the DateSelectionAndComparisonVO
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return DateSelectionAndComparisonVO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{

		if (criteriaVO == null)
		{
			criteriaVO = new CriteriaVO();
		}
		dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList<TimePeriodVO> timePeriodList = new ArrayList<TimePeriodVO>();
		
		if (dateSelectionForm.getStartSelectedTime2() != null)
		{
			TimePeriodVO comparePeriod1TimePeriodVO = new TimePeriodVO();
			comparePeriod1TimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			comparePeriod1TimePeriodVO.setEndDate(dateSelectionForm.getEndSelectedTime2());
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
		dateSelectionAndComparisonVO.setIncludeCurrentMonth(dateSelectionForm.getIncludeCurrentMonth());
		dateSelectionAndComparisonVO.setSelectedSelectComparisonList(dateSelectionForm.getSelectedSelectComparisonList());
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);
		if(dateSelectionForm.getLastXMonths() != null)
		{
			dateSelectionAndComparisonVO.setLastXMonths(Integer.parseInt(dateSelectionForm.getLastXMonths()));
		}
		return dateSelectionAndComparisonVO;
	}
	/**
	 * This method returns fieldVOs List which should be populated in the Jasper report 
	 * @param metaDataList Meta List from T_Appl_Cn_Rpt_FldMeta_Data table
	 * @param tSTPFormattingVO Time Series Time period Comparison FormattingVO
	 * @param userId User Id
	 * @return Fields VO Array List
	 */
	public static ArrayList<FieldsVO> getFieldsList (List<TApplCnRptFldMetaData> metaDataList ,ControlledSubstanceFormattingVO controlledSubstanceFormattingVO , String userId)
	{
		ArrayList<FieldsVO> fieldsList = new ArrayList<FieldsVO>();	
		String accountFormat = controlledSubstanceFormattingVO.getAccountFormat();
		String format = controlledSubstanceFormattingVO.getFormat();
		
		if (metaDataList != null && metaDataList.size() > 0)
		{
			int metaDataListSize = metaDataList.size();
			TApplCnRptFldMetaData  tApplCnRptFldMetaData = null;
			for (int i=0; i < metaDataListSize; i++)
			{
				tApplCnRptFldMetaData = (TApplCnRptFldMetaData)metaDataList.get(i);
				if(accountFormat.equals("Combine Accounts"))
				{
					if ((tApplCnRptFldMetaData.getIsGrpHdr1().equals("N") && tApplCnRptFldMetaData.getIsTimeSrsFrmla().trim().equals("N") || tApplCnRptFldMetaData.getFldDscr().trim().equals("Acct #")))
					{
						int fieldId = tApplCnRptFldMetaData.getId().getFldId();
						FieldsVO fieldsVO = new FieldsVO();
						fieldsVO.setFldId(fieldId);
						fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
						fieldsList.add(fieldsVO);
					}
				}				
				else if(tApplCnRptFldMetaData.getIsTimeSrsFrmla().trim().equals("N"))
				{
					int fieldId = tApplCnRptFldMetaData.getId().getFldId();
					FieldsVO fieldsVO = new FieldsVO();
					fieldsVO.setFldId(fieldId);
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());
					fieldsList.add(fieldsVO);
				}
			}
			if(format.trim().equals("Invoice Detail With Monthly Summary Quantities"))
			{
				for(int i = 0;i < 12; i++)
				{
					//M1-M12
					FieldsVO fieldsVO = new FieldsVO();
					// Since The Field ID ends with 22 for CUST_PO_NUM which is the last field. So we are adding 21 
					fieldsVO.setFldId(22);
					fieldsVO.setFieldName("M" + (i + 1));
					fieldsList.add(fieldsVO);
					// Q1-Q12
					FieldsVO fieldsVO1 = new FieldsVO();
					// Since The Field ID ends with 22 for CUST_PO_NUM which is the last field. So we are adding 21 
					fieldsVO1.setFldId(23);
					fieldsVO1.setFieldName("Q" + (i + 1));
					fieldsList.add(fieldsVO1);
				}
			}
			
		}
		return fieldsList;
	}

}

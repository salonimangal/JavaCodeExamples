package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.appl.jms.JMSMessageBroker;
import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.model.TApplCnRptFldMetaData;
import com.mckesson.smora.database.model.TApplCnRptFldMetaDataId;
import com.mckesson.smora.dto.CannedReportCriteriaVO;
import com.mckesson.smora.dto.CannedReportVO;
import com.mckesson.smora.dto.CriteriaVO;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.FirstGroupLevelVO;
import com.mckesson.smora.dto.GroupLevelVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.SecondGroupLevelVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.TimeSeriesGroupVO;
import com.mckesson.smora.dto.TimeSeriesReportDetailFormattingVO;
import com.mckesson.smora.dto.TimeSeriesReportDetailVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.TimeSeriesReportDetailForm;
import com.mckesson.smora.ui.form.TimeSeriesReportDetailFormattingSelectionForm;


public class ConstructTimeSeriesReportDetailBaseVO extends
		ConstructCannedReportBaseVO
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructTimeSeriesReportDetailBaseVO";
	/***
	 * log
	 */
	private static Log log = LogFactory.getLog(JMSMessageBroker.class);
	
	private static TimeSeriesReportDetailVO timeSeriesReportDetailVO = null;
	
	private static TimeSeriesReportDetailFormattingVO timeSeriesReportDetailFormattingVO = null;
	
	private static DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
	
	private static CriteriaVO criteriaVO = null;
	
	private static CannedReportCriteriaVO cannedReportCriteriaVO = null;

	/**
	 * This method populates the report base VO.
	 * 
	 * @param customReportingForm
	 *            the custom reporting form
	 * @return the report base VO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public static ReportBaseVO populateTimeSeriesReportDetailBaseVO(TimeSeriesReportDetailForm timeSeriesReportDetailForm) throws SMORAException
	{
		
		ArrayList<String> fieldDescArray = new ArrayList();
		ArrayList<String> subTotalArray = new ArrayList();
		GroupLevelVO groupLevelVO = new GroupLevelVO();
		timeSeriesReportDetailVO = new TimeSeriesReportDetailVO();
		criteriaVO = new CriteriaVO();
		timeSeriesReportDetailFormattingVO = new TimeSeriesReportDetailFormattingVO();
		dateSelectionAndComparisonVO =new DateSelectionAndComparisonVO();
		cannedReportCriteriaVO = new CannedReportCriteriaVO();
		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		TimeSeriesGroupVO timeSeriesGroupVO = new TimeSeriesGroupVO();
		TimeSeriesReportDetailFormattingSelectionForm timeSeriesReportDetailFormattingSelectionForm = timeSeriesReportDetailForm.getTimeSeriesReportDetailFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = timeSeriesReportDetailForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = timeSeriesReportDetailForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = timeSeriesReportDetailForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = timeSeriesReportDetailForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = timeSeriesReportDetailForm.getAdvancedFiltersForm();
		
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
		
		cannedReportVO.setCannedReportTitle("43,44");
		// QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(timeSeriesReportDetailFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(timeSeriesReportDetailFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
		}
		baseVO.setCustomHeading(timeSeriesReportDetailFormattingSelectionForm.getCustomHeading());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.TIME_SERIES_REPORT_DETAIL);
		timeSeriesReportDetailFormattingVO = constructCannedReportVO(timeSeriesReportDetailFormattingSelectionForm);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		timeSeriesReportDetailVO.setTimeSeriesReportDetailFormattingVO(timeSeriesReportDetailFormattingVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);
		if(timeSeriesReportDetailFormattingVO.getFormat().equals("Quantity"))
		{
		//THE GROUP ID for TimeSeriesDetail-Quantity 
		baseVO.setReportGroupID(ReportManagerConstants.TIME_SERIES_REPORT_DETAIL);
		}
		else
			if(timeSeriesReportDetailFormattingVO.getFormat().equals("Dollar"))
			{
			//THE GROUP ID for TimeSeriesDetail-Dollar 
			baseVO.setReportGroupID(ReportManagerConstants.TIME_SERIES_REPORT_DETAIL_DOLLAR);
			}
			else
				if(timeSeriesReportDetailFormattingVO.getFormat().equals("Both"))
				{
				//THE GROUP ID for TimeSeriesDetail-Both 
				baseVO.setReportGroupID(ReportManagerConstants.TIME_SERIES_REPORT_DETAIL_BOTH);
				}
		baseVO.setHtml(timeSeriesReportDetailFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(timeSeriesReportDetailFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(timeSeriesReportDetailFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(timeSeriesReportDetailFormattingSelectionForm.isResultsDisplayCSV());
		String format = timeSeriesReportDetailFormattingSelectionForm.getAccountFormat();
		String grouping = timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel();
		log.debug("############ReportFormat: "+format);
		log.debug("groupingLevel is :"+grouping);
		
		/**
		 * if Break is selected in the Account format(in formatting tab)
		 */
		if(format.equals("Break by Account"))
		{
			/**
			 * Set classtype,fieldName,fieldwidth and empty subtotal for Cust_acct_id ,in firstGroupLevelVO.
			 */
			FirstGroupLevelVO firstGroupLevelVO = new FirstGroupLevelVO();
			firstGroupLevelVO.setClassType("java.lang.String");
			firstGroupLevelVO.setFieldName("Account #");
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
		
		if(timeSeriesReportDetailFormattingVO.getGroupingLevel().equals("No Grouping"))
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
			
			secondGroupLevelVO.setFieldDescription(timeSeriesReportDetailFormattingVO.getGroupingLevel());
			secondGroupLevelVO.setFieldName(timeSeriesReportDetailFormattingVO.getGroupingLevel());
			
			secondGroupLevelVO.setFieldWidth(200);
			secondGroupLevelVO.setSubTotal(fieldDescArray);
			/**
			 * Set secondGroupLevelVO in to groupLevelVO,
			 */
			groupLevelVO.setSecondGroupLevel(secondGroupLevelVO);	
		}
		
		
		ArrayList<FieldsVO> fieldsVOList = new ArrayList();
		FieldsListVO fieldsListVO = new FieldsListVO();
		FieldsVO fieldsVO = new FieldsVO();
		/***
		 * populate the field list from CannedReport Meta Data table
		 */
		CannedReportFieldMetaDataDB cannedReportFieldMetaDataDB =new CannedReportFieldMetaDataDB();
	    List<TApplCnRptFldMetaData> TApplCnRptFldMetaData=  cannedReportFieldMetaDataDB.getCnRptFldMetaData(new Long(baseVO.getReportGroupID()).intValue());
		log.debug("TApplCnRptFldMetaData" +TApplCnRptFldMetaData );
		if (TApplCnRptFldMetaData != null && TApplCnRptFldMetaData.size() > 0)
		{
			baseVO.setReportGroupID(TApplCnRptFldMetaData.get(0).getId().getRptId());
			log.debug("TApplCnRptFldMetaData.get(0).getId().getRptId()" +TApplCnRptFldMetaData.get(0).getId().getRptId() );
			for (int i = 0; i < TApplCnRptFldMetaData.size(); i++)
			{ 
				TApplCnRptFldMetaData tApplCnRptFldMetaData=TApplCnRptFldMetaData.get(i);
				TApplCnRptFldMetaDataId tApplCnRptFldMetaDataId=tApplCnRptFldMetaData.getId();
				int fldId=tApplCnRptFldMetaDataId.getFldId();
				
				fieldsVO.setFldId(fldId);
				log.debug(" fieldsVO.setFldId()" + fldId);
				
				/***
				 * added fields to the fieldsVO when break by selected in the formatting tab.
				 */
				if (tApplCnRptFldMetaData.getIsGrpHdr1().equals("Y"))
				{
					if (timeSeriesReportDetailFormattingVO.getAccountFormat().equals("Break by Account"))
					{
						fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());			
						fieldsVOList.add(fieldsVO);
						log.debug("if break is selected :"+fieldsVOList);
					}
				}
				else if (tApplCnRptFldMetaData.getIsGrpHdr2().equals("Y") && tApplCnRptFldMetaData.getIsColHdr().equals("N"))
				{
					/***
					 * added fields to the fieldsVO when grouping is selected in the formatting tab.
					 */
					if (timeSeriesReportDetailFormattingVO.getGroupingLevel() != null && timeSeriesReportDetailFormattingVO.equals("No Grouping"))
					{
						fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());			
						log.debug("if grouping is slected,Field Alias is :"+tApplCnRptFldMetaData.getFldAlias());
						fieldsVOList.add(fieldsVO);
					}
				}
				else
				{
					fieldsVO.setFieldName(tApplCnRptFldMetaData.getFldAlias());			
					fieldsVOList.add(fieldsVO);
				}
			}
		}
		/***
		 * setting fieldsVOList in to the CannedReportVO.
		 */
		fieldsListVO.setFieldsVOList(fieldsVOList);
		cannedReportCriteriaVO.setFieldsVOList(fieldsListVO);
		cannedReportCriteriaVO.setGroupLevelVO(groupLevelVO);
		timeSeriesGroupVO.setTimeSeriesReportDetailVO(timeSeriesReportDetailVO);
		cannedReportVO.setTimeSeriesGroupVO(timeSeriesGroupVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		//baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
		/***
		 * setting CannedReportVO in to the BaseVO.
		 */
		baseVO.setCannedReportVO(cannedReportVO);
		return baseVO;
	}

	/**
	 * This method constructs the canned report VO.
	 * 
	 * @param timeSeriesReportDetailFormattingSelectionForm
	 *            the timeSeriesReportDetailFormatting selection form
	 * @return timeSeriesReportDetailFormattingVO
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	private static TimeSeriesReportDetailFormattingVO constructCannedReportVO(TimeSeriesReportDetailFormattingSelectionForm timeSeriesReportDetailFormattingSelectionForm) throws SMORAException
	{
		if (timeSeriesReportDetailFormattingVO == null)
		{
			timeSeriesReportDetailFormattingVO = new TimeSeriesReportDetailFormattingVO();
		}
		/***
		 * setting all the values (which ever we selected in the formatting tab) into the TimeSeriesReportDetailFormattingVO.
		 */
		timeSeriesReportDetailFormattingVO.setAccountFormat(timeSeriesReportDetailFormattingSelectionForm.getAccountFormat());
		timeSeriesReportDetailFormattingVO.setFormat(timeSeriesReportDetailFormattingSelectionForm.getFormat());
		timeSeriesReportDetailFormattingVO.setSortOptions(timeSeriesReportDetailFormattingSelectionForm.getSortOptions());
		timeSeriesReportDetailFormattingVO.setIncludeOnly(timeSeriesReportDetailFormattingSelectionForm.getIncludeOnly());
		if((timeSeriesReportDetailFormattingSelectionForm.getCumulativePercentage()!=null) &&!(timeSeriesReportDetailFormattingSelectionForm.getCumulativePercentage().equals("")))
		timeSeriesReportDetailFormattingVO.setCumulativePercentage(Integer.parseInt(timeSeriesReportDetailFormattingSelectionForm.getCumulativePercentage()));
		if((timeSeriesReportDetailFormattingSelectionForm.getTopNumberofRows()!=null) &&!(timeSeriesReportDetailFormattingSelectionForm.getTopNumberofRows().equals("")))
		timeSeriesReportDetailFormattingVO.setTopNumberOfRows(Integer.parseInt(timeSeriesReportDetailFormattingSelectionForm.getTopNumberofRows()));
		if (timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel().equals("0"))
		{
			timeSeriesReportDetailFormattingVO.setGroupingLevel("No Grouping");
		}
		if(timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel().equals("1"))
		{
			timeSeriesReportDetailFormattingVO.setGroupingLevel("Generic Description");
		}
		if(timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel().equals("2"))
		{
			timeSeriesReportDetailFormattingVO.setGroupingLevel("Therapeutic Description");
		}
		if(timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel().equals("3"))
		{
			timeSeriesReportDetailFormattingVO.setGroupingLevel("Supplier");
		}
		if(timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel().equals("4"))
		{
			timeSeriesReportDetailFormattingVO.setGroupingLevel("Local Department");
		}
		if(timeSeriesReportDetailFormattingSelectionForm.getGroupingLevel().equals("5"))
		{
			timeSeriesReportDetailFormattingVO.setGroupingLevel("Ordering Department");
		}
		return timeSeriesReportDetailFormattingVO;
	}
	
	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the dateSelectionAndComparisonVO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm)
	{

		if (timeSeriesReportDetailVO == null)
		{
			timeSeriesReportDetailVO = new TimeSeriesReportDetailVO();
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
		dateSelectionAndComparisonVO.setSelectedPeriod(dateSelectionForm.getSelectedPeriod());
		dateSelectionAndComparisonVO.setDateSelection(dateSelectionForm.getDateSelection());
		dateSelectionAndComparisonVO.setLastXMonths(Integer.parseInt(dateSelectionForm.getLastXMonths()));
		dateSelectionAndComparisonVO.setTimePeriodsVOList(timePeriodListVO);
		
		return dateSelectionAndComparisonVO;
	}
}

/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.CannedReportFieldMetaDataDB;
import com.mckesson.smora.database.dao.CustomReportDB;
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
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.FiscalYTDFormattingVO;
import com.mckesson.smora.dto.TimePeriodListVO;
import com.mckesson.smora.dto.TimePeriodVO;
import com.mckesson.smora.dto.TimeSeriesGroupVO;
import com.mckesson.smora.dto.FiscalYTDVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.QueryException;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.DateSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;
import com.mckesson.smora.ui.form.FiscalYTDForm;
import com.mckesson.smora.ui.form.FiscalYTDFormattingSelectionForm;
/**
 *
 * This Class is used to Construct the ReportBaseVO for FiscalYTDReport
 *
 */

public class ConstructFiscalYTDReportBaseVO extends ConstructCannedReportBaseVO
{


	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructFiscalYTDReportBaseVO";

	/**
	 * The cust report criteria VO.
	 */
	private static FiscalYTDVO fiscalYTDVO = null;

	private static CriteriaVO criteriaVO = null;

	private static FiscalYTDFormattingVO fiscalYTDFormattingVO = null;

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
	public static ReportBaseVO populateFiscalYTDReportBaseVO(FiscalYTDForm fiscalYTDForm) throws SMORAException
	{
		fiscalYTDVO = new FiscalYTDVO();
		criteriaVO = new CriteriaVO();

		FiscalYTDFormattingSelectionForm fiscalYTDFormattingSelectionForm = fiscalYTDForm.getFormattingSelectionForm();
		DateSelectionForm dateSelectionForm = fiscalYTDForm.getDateSelectionForm();
		CustomerSelectionForm customerSelectionForm = fiscalYTDForm.getCustomerSelectionForm();
		SupplierSelectionForm supplierSelectionForm = fiscalYTDForm.getSupplierSelectionForm();
		ItemSelectionForm itemSelectionForm = fiscalYTDForm.getItemSelectionForm();
		AdvancedFiltersForm advancedFiltersForm = fiscalYTDForm.getAdvancedFiltersForm();

		ReportBaseVO baseVO = new ReportBaseVO();
		CannedReportVO cannedReportVO = new CannedReportVO();
		CannedReportCriteriaVO cannedReportCriteriaVO = new CannedReportCriteriaVO();

//QC-10063 - Set Template Name in xml to display template name in Save to My Reports box
		if(fiscalYTDFormattingSelectionForm.getTemplate_Name()!=null)
		{
			baseVO.setTemplateName(fiscalYTDFormattingSelectionForm.getTemplate_Name());
		}
		else
		{
			baseVO.setTemplateName("");
	    }
		baseVO.setCustomHeading(fiscalYTDFormattingSelectionForm.getCustomHeading());
		baseVO.setHtml(fiscalYTDFormattingSelectionForm.isResultsDisplayHTML());
		baseVO.setPdf(fiscalYTDFormattingSelectionForm.isResultsDisplayPDF());
		baseVO.setXls(fiscalYTDFormattingSelectionForm.isResultsDisplayXLS());
		baseVO.setCsv(fiscalYTDFormattingSelectionForm.isResultsDisplayCSV());
		baseVO.setReportType("CANNED_REPORT");
		baseVO.setReportSubtype(ReportManagerConstants.REPORT_FISCAL_YTD);
		baseVO.setReportGroupID(ReportManagerConstants.REPORT_FISCAL_YTD);

		fiscalYTDFormattingVO = constructCannedReportVO(fiscalYTDFormattingSelectionForm);
		fiscalYTDVO.setFiscalYTDFormattingVO(fiscalYTDFormattingVO);
		dateSelectionAndComparisonVO = constructCannedReportVO(dateSelectionForm);

		criteriaVO = constructCannedReportVO(customerSelectionForm);
		criteriaVO = constructCannedReportVO(supplierSelectionForm);
		criteriaVO = constructCannedReportVO(itemSelectionForm);
		criteriaVO = constructCannedReportVO(advancedFiltersForm);
		criteriaVO.setDateSelectionAndComparisonVO(dateSelectionAndComparisonVO);
		cannedReportCriteriaVO.setCriteriaVO(criteriaVO);

		cannedReportVO.setFiscalYTDVO(fiscalYTDVO);
		cannedReportVO.setCannedReportCriteriaVO(cannedReportCriteriaVO);
		cannedReportVO.setCannedReportTitle("25");
		baseVO.setRowLimits(ReportManagerConstants.CANNED_REPORT_ROW_LIMITS);
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
	private static FiscalYTDFormattingVO constructCannedReportVO(FiscalYTDFormattingSelectionForm fiscalYTDFormattingSelectionForm) throws SMORAException
	{
		if (fiscalYTDFormattingVO== null)
		{
			fiscalYTDFormattingVO = new FiscalYTDFormattingVO();
		}

		fiscalYTDFormattingVO.setAccountFormat(fiscalYTDFormattingSelectionForm.getAccountFormat());
		fiscalYTDFormattingVO.setFiscalYearStartMonth(fiscalYTDFormattingSelectionForm.getFiscalYearStartMonth());
		fiscalYTDFormattingVO.setLimitFirstRows(fiscalYTDFormattingSelectionForm.getLimitFirstRows());
		fiscalYTDFormattingVO.setSortOptions(fiscalYTDFormattingSelectionForm.getSortOptions());

		return fiscalYTDFormattingVO;
	}
	/**
	 * This method constructs the custom report VO.
	 *
	 * @param dateSelectionForm the date selection form
	 *
	 * @return the cust report criteria VO
	 */
	private static DateSelectionAndComparisonVO constructCannedReportVO(DateSelectionForm dateSelectionForm) throws SMORAException
	{

		if (dateSelectionAndComparisonVO == null)
		{
			dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		}
		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = new DateSelectionAndComparisonVO();
		TimePeriodListVO timePeriodListVO = new TimePeriodListVO();
		ArrayList timePeriodList = new ArrayList();

		if (dateSelectionForm.getStartSelectedTime2() != null)
		{
			TimePeriodVO standardTimePeriodVO = new TimePeriodVO();
			standardTimePeriodVO.setStartDate(dateSelectionForm.getStartSelectedTime2());
			standardTimePeriodVO.setEndDate(getNextYear(dateSelectionForm.getStartSelectedTime2()));
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


		return dateSelectionAndComparisonVO;
	}

	/**
		 * This Method Gets the Previous Year
		 * @param reportBaseVO
		 * @return String
		 * @throws ParseException
		 */
		public static String getNextYear(String currYr) throws SMORAException
		{
			String nextYr = null;

			if(currYr!=null && !currYr.equals(""))
			{
				String[] strstartmonth = currYr.split(" ");
				int iYear = Integer.parseInt(strstartmonth[1]);
				String iMon=strstartmonth[0];
				if(iMon.equals("January"))
				{
				iYear = iYear;
				strstartmonth[0]="December ";
				}
				else if(iMon.equals("February"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="January ";
				}
				else if(iMon.equals("March"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="February ";
				}
				else if(iMon.equals("April"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="March ";
				}
				else if(iMon.equals("May"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="April ";
				}
				else if(iMon.equals("June"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="May ";
				}
				else if(iMon.equals("July"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="June ";
				}
				else if(iMon.equals("August"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="July ";
				}
				else if(iMon.equals("September"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="August ";
				}
				else if(iMon.equals("October"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="September ";
				}
				else if(iMon.equals("November"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="October ";
				}
				else if(iMon.equals("December"))
				{
				iYear = iYear + 1;
				strstartmonth[0]="November ";
				}
				nextYr = strstartmonth[0]+""+iYear;
			}
			else
			{
				nextYr = "";
			}
			return nextYr;
	}
}
/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import com.mckesson.smora.database.dao.*;
import com.mckesson.smora.dto.PreferRxSavingsVO;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccComparisonGGDAO;
import com.mckesson.smora.database.dao.AccComparisonISDAO;
import com.mckesson.smora.database.dao.AssetManagement12MonthInventoryTurnsForecastDetailDAO;
import com.mckesson.smora.database.dao.AssetManagement12MonthInventoryTurnsForecastSummaryDAO;
import com.mckesson.smora.database.dao.AssetManagementObsoleteInventoryDAO;
import com.mckesson.smora.database.dao.AssetMgmtHighFreqItemsDAO;
import com.mckesson.smora.database.dao.BaseDAO;
import com.mckesson.smora.database.dao.ContractComplianceDAO;
import com.mckesson.smora.database.dao.ControlledSubstanceDAO;
import com.mckesson.smora.database.dao.CustomerPriceCPCDAO;
import com.mckesson.smora.database.dao.CustomerPriceIPCDAO;
import com.mckesson.smora.database.dao.DescDollarItemReportDAO;
import com.mckesson.smora.database.dao.DescendingDollarDAO;
import com.mckesson.smora.database.dao.MUSContractOmitDAO;
import com.mckesson.smora.database.dao.MarketShareByGenericNameDAO;
import com.mckesson.smora.database.dao.NewlyPurchasedItemsDAO;
import com.mckesson.smora.database.dao.PreferRxSavingsDAO;
import com.mckesson.smora.database.dao.PurchaseDrillDownDAO;
import com.mckesson.smora.database.dao.Report8020ConsolidatedViewDAO;
import com.mckesson.smora.database.dao.Report8020ConsolidatedViewSAPDAO;
import com.mckesson.smora.database.dao.Report8020GenericCategoryCommonDAO;
import com.mckesson.smora.database.dao.Report8020GenericCategoryCommonSAPDAO;
import com.mckesson.smora.database.dao.Report8020WithUnitsViewDAO;
import com.mckesson.smora.database.dao.ReportAccComparisonGGSAPDAO;
import com.mckesson.smora.database.dao.ReportAccComparisonISSAPDAO;
import com.mckesson.smora.database.dao.ReportContractComplianceSAPDAO;
import com.mckesson.smora.database.dao.ReportCustomerPriceCPCSAPDAO;
import com.mckesson.smora.database.dao.ReportDescDollarItemReportSAPDAO;
import com.mckesson.smora.database.dao.ReportItemInformationDAO;
import com.mckesson.smora.database.dao.ReportItemMovementDAO;
import com.mckesson.smora.database.dao.ReportItemMovementSAPDAO;
import com.mckesson.smora.database.dao.ReportNewlyPurchasedItemsSAPDAO;
import com.mckesson.smora.database.dao.ReportPerformanceManagementCreditDAO;
import com.mckesson.smora.database.dao.ReportPerformanceManagementReturnAnalysisDAO;
import com.mckesson.smora.database.dao.ReportPerformanceManagementTop200DAO;
import com.mckesson.smora.database.dao.ReportPurchaseCostVarianceDAO;
import com.mckesson.smora.database.dao.ReportPurchaseCostVarianceSAPDAO;
import com.mckesson.smora.database.dao.ReportPurchaseCostVarianceTherSumDAO;
import com.mckesson.smora.database.dao.ReportQuaterlyDrugUtilizationCommonDAO;
import com.mckesson.smora.database.dao.ReportQuickItemPurchHistoryDAO;
import com.mckesson.smora.database.dao.ReportSKYSavingOpportunityDAO;
import com.mckesson.smora.database.dao.ReportSKYSavingOpportunitySAPDAO;
import com.mckesson.smora.database.dao.ReportUsageDetailGenericSAPDAO;
import com.mckesson.smora.database.dao.ReportUsageDetailTheraTimeSeriesSAPDAO;
import com.mckesson.smora.database.dao.ReportUsageDetailTherapeuticSAPDAO;
import com.mckesson.smora.database.dao.TimeSeriesAccountSummaryDAO;
import com.mckesson.smora.database.dao.TimeSeriesNonAccountSummaryDAO;
import com.mckesson.smora.database.dao.TimeSeriesReportDetailDAO;
import com.mckesson.smora.database.dao.TimeSeriesTPComparisonDAO;
import com.mckesson.smora.database.dao.UsageDetailGenericDAO;
import com.mckesson.smora.database.dao.UsageDetailTheraTimeSeriesDAO;
import com.mckesson.smora.database.dao.UsageDetailTherapeuticDAO;

public class ResolveDAO
{
	public BaseDAO getDAO(int reportSubtype, boolean sapFlag,String accountFormat)
	{
		// TODO Exceptions needs to be thrown in error conditions
		switch (reportSubtype)
		{

			case ReportManagerConstants.REPORT_8020_CONSOLIDATED_VIEW:
				if(sapFlag)  // modified by Avik for ELA 80/20 Consolidated View Reports
					return new Report8020ConsolidatedViewSAPDAO();
				else
					return new Report8020ConsolidatedViewDAO();

			case ReportManagerConstants.REPORT_8020_DESCENDING_WITH_UNITS:
				return new Report8020WithUnitsViewDAO();

			case ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_CODE:
				if(sapFlag)  // modified by Avik for ELA 80/20 Generic Reports
					return new Report8020GenericCategoryCommonSAPDAO();
				else
					return new Report8020GenericCategoryCommonDAO();

			case ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_DESCRIPTION:
				if(sapFlag)  // modified by Avik for ELA 80/20 Generic Reports
					return new Report8020GenericCategoryCommonSAPDAO();
				else
					return new Report8020GenericCategoryCommonDAO();

			case ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_DESCENDING_DOLLAR:
				if(sapFlag)  // modified by Avik for ELA 80/20 Generic Reports
					return new Report8020GenericCategoryCommonSAPDAO();
				else
					return new Report8020GenericCategoryCommonDAO();

			case ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY:
					return new TimeSeriesAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_SUMMARY:
					return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_DESC_SUMMARY:
					return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_THERA_SUMMARY:
					return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_SUPP_SUMMARY:
					return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_LCL_DEPT_SUMMARY:
					return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_ORD_DEPT_SUMMARY:
					return new TimeSeriesNonAccountSummaryDAO();
				// For Time Series Report Detail Reports
			case ReportManagerConstants.TIME_SERIES_REPORT_DETAIL:
					return new TimeSeriesReportDetailDAO();
				// For Time Series Time Period Comparison Reports
			case ReportManagerConstants.TIMESERIES_TP_COMPARISON:
					return new TimeSeriesTPComparisonDAO();
			case ReportManagerConstants.REPORT_ASSET_MANAGEMENT_HIGH_FREQUENCY_ITEMS:
					return new AssetMgmtHighFreqItemsDAO();
			case ReportManagerConstants.ASSETMANAGEMENT_OBSOLETE_INVENTORY:
					return new AssetManagementObsoleteInventoryDAO();
				// FOR Market Share Report
			case ReportManagerConstants.REPORT_MARKET_SHARE_GROUP:
					return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_ACTUAl:
					return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_AWP:
					return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_QTY:
					return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_ACTUAl:
					return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_AWP:
					return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_QTY:
					return new MarketShareByGenericNameDAO();
				// For Descending Dollar Reports
			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_CODE:
					return new DescendingDollarDAO();
			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_DESCRIPTION:
					return new DescendingDollarDAO();

			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_ITEM_CODE:
			//Change for DRUG ITEM PRICE decommission
               	if(sapFlag && (accountFormat!=null && accountFormat.equals("Break By Account")))  // modified by Devesh for Descending dollar  Report
					return new ReportDescDollarItemReportSAPDAO();
				else
					return new DescDollarItemReportDAO();

			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_THERAPEUTIC:
					return new DescendingDollarDAO();

			case ReportManagerConstants.REPORT_CUSTOMER_PRICE_CPC:
			    if(sapFlag)  // modified by Devesh for ELA CPC Report
					return new ReportCustomerPriceCPCSAPDAO ();
				else
					return new CustomerPriceCPCDAO();

			case ReportManagerConstants.REPORT_CUSTOMER_PRICE_IPC:
					return new CustomerPriceIPCDAO();
			case ReportManagerConstants.REPORT_MUS_CONTRACT_OMIT:
					return new MUSContractOmitDAO();
			case ReportManagerConstants.PURCHASE_DRILL_DOWN_SUMMARY:
					return new PurchaseDrillDownDAO();
			case ReportManagerConstants.REPORT_ITEM_INFORMATION:
					return new ReportItemInformationDAO();

			case ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES:
//				Change for DRUG ITEM PRICE decommission
	            if(sapFlag && accountFormat!=null && accountFormat.equals("Break By Account"))  	 // modified by Devesh for ELA Usage Detail Therapeutic Time Series Report
					return new ReportUsageDetailTheraTimeSeriesSAPDAO();
				else
					return new UsageDetailTheraTimeSeriesDAO();

			case ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC:
//				Change for DRUG ITEM PRICE decommission
	            if(sapFlag && accountFormat!=null && accountFormat.equals("Break By Account"))  // modified by Devesh for ELA Usage Detail Therapeutic Report
					return new ReportUsageDetailTherapeuticSAPDAO();
				else
					return new UsageDetailTherapeuticDAO();

			case ReportManagerConstants.REPORT_GENERIC_USAGE:
                if(sapFlag&& accountFormat!=null && accountFormat.equals("Break By Account"))  // modified by Devesh for ELA Usage Detail Generic Report
					return new ReportUsageDetailGenericSAPDAO();
				else
					return new UsageDetailGenericDAO();

			case ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY:
					return new ReportQuickItemPurchHistoryDAO();
			case ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_ACROSS_ACCOUNT:
					return new ReportQuickItemPurchHistoryDAO();
			case ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT:
					return new ReportQuickItemPurchHistoryDAO();

			case ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM:
            	if(sapFlag)  // modified by Devesh for ELA Account Comparison Report
				    return new ReportAccComparisonISSAPDAO();
				else
					return new AccComparisonISDAO();

			case ReportManagerConstants.REPORT_ACC_COMPARISON_GENERIC:
				if(sapFlag)  // modified by Devesh for ELA Account Comparison Report
					return new ReportAccComparisonGGSAPDAO();
				else
					return new AccComparisonGGDAO();

			case ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE_THERA:
					return new ReportPurchaseCostVarianceTherSumDAO();
			case ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE:
				if(sapFlag)  //	modified by Devesh for ELA Purchase Cost Variance Reports
					return new ReportPurchaseCostVarianceSAPDAO();
				else
					return new ReportPurchaseCostVarianceDAO();

			case ReportManagerConstants.REPORT_PERFORMANCE_TOP200:
					return new ReportPerformanceManagementTop200DAO();
			case ReportManagerConstants.REPORT_CONTROLLED_SUBSTANCE_GRP_ID:
					return new ControlledSubstanceDAO();

					
			case ReportManagerConstants.REPORT_PURCHASE_DETAILS:
				return new ReportPurchasingDetailsDAO();
							
					
		    case ReportManagerConstants.REPORT_RETURNS_ANALYSIS:
			    	return new ReportPerformanceManagementReturnAnalysisDAO();

			case ReportManagerConstants.REPORT_ITEM_MOVEMENT:
				if(sapFlag)  //	modified by Devesh for ELA Item Movement Reports
					return new ReportItemMovementSAPDAO();
				else
					return new ReportItemMovementDAO();

			case ReportManagerConstants.REPORT_PREFER_RX_SAVINGS:
					return new PreferRxSavingsDAO();

			case ReportManagerConstants.REPORT_SKY_SAVINGS:
				if(sapFlag)  // modified by Avik for ELA Sky Savings Reports
					return new ReportSKYSavingOpportunitySAPDAO();
				else
					return new ReportSKYSavingOpportunityDAO();

            case ReportManagerConstants.REPORT_QUATERLY_DRUG_UTILIZATION:
					return new ReportQuaterlyDrugUtilizationCommonDAO();

			case ReportManagerConstants.REPORT_NEWLY_PURCHASED_ITEMS:
				if(sapFlag)  // modified by Devesh for ELA Item Newly Purchased Items Reports
					return new ReportNewlyPurchasedItemsSAPDAO();
				else
					return new NewlyPurchasedItemsDAO();

			case ReportManagerConstants.REPORT_CONTRACT_COMPLIANCE:
				if(sapFlag)
					return new ReportContractComplianceSAPDAO();
				else
					return new ContractComplianceDAO();

			case ReportManagerConstants.REPORT_CREDIT_LESS_THAN_100_PERCENT:
					return new ReportPerformanceManagementCreditDAO();
			case ReportManagerConstants.FORECASE_REPORTS_DETAIL:
					return new AssetManagement12MonthInventoryTurnsForecastDetailDAO();
			case ReportManagerConstants.FORECASE_REPORTS_SUMMARY:
					return new AssetManagement12MonthInventoryTurnsForecastSummaryDAO();

		}
		return null;
	}

	public BaseDAO getDAO(int reportSubtype)
	{
		// TODO Exceptions needs to be thrown in error conditions
		switch (reportSubtype)
		{
			// For Report8020Group Reports
			case ReportManagerConstants.REPORT_8020_CONSOLIDATED_VIEW:
				return new Report8020ConsolidatedViewDAO();
			case ReportManagerConstants.REPORT_8020_DESCENDING_WITH_UNITS:
				return new Report8020WithUnitsViewDAO();
			case ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_CODE:
				return new Report8020GenericCategoryCommonDAO();
			case ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_DESCRIPTION:
				return new Report8020GenericCategoryCommonDAO();
			case ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_DESCENDING_DOLLAR:
				return new Report8020GenericCategoryCommonDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY:
				return new TimeSeriesAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_SUMMARY:
				return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_DESC_SUMMARY:
				return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_THERA_SUMMARY:
				return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_SUPP_SUMMARY:
				return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_LCL_DEPT_SUMMARY:
				return new TimeSeriesNonAccountSummaryDAO();
			case ReportManagerConstants.REPORT_TIME_SERIES_ORD_DEPT_SUMMARY:
				return new TimeSeriesNonAccountSummaryDAO();
				// For Time Series Report Detail Reports
			case ReportManagerConstants.TIME_SERIES_REPORT_DETAIL:
				return new TimeSeriesReportDetailDAO();

				// For Time Series Time Period Comparison Reports
			case ReportManagerConstants.TIMESERIES_TP_COMPARISON:
				return new TimeSeriesTPComparisonDAO();
			case ReportManagerConstants.REPORT_ASSET_MANAGEMENT_HIGH_FREQUENCY_ITEMS:
				return new AssetMgmtHighFreqItemsDAO();
			case ReportManagerConstants.ASSETMANAGEMENT_OBSOLETE_INVENTORY:
				return new AssetManagementObsoleteInventoryDAO();

				// FOR Market Share Report
			case ReportManagerConstants.REPORT_MARKET_SHARE_GROUP:
				return new MarketShareByGenericNameDAO();
			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_ACTUAl:
				return new MarketShareByGenericNameDAO();

			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_AWP:
				return new MarketShareByGenericNameDAO();

			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_QTY:
				return new MarketShareByGenericNameDAO();

			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_ACTUAl:
				return new MarketShareByGenericNameDAO();

			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_AWP:
				return new MarketShareByGenericNameDAO();

			case ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_QTY:
				return new MarketShareByGenericNameDAO();

				// For Descending Dollar Reports
			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_CODE:
				return new DescendingDollarDAO();
			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_DESCRIPTION:
				return new DescendingDollarDAO();
			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_ITEM_CODE:
				return new DescDollarItemReportDAO();
			case ReportManagerConstants.REPORT_DESCENDING_DOLLAR_THERAPEUTIC:
				return new DescendingDollarDAO();
			case ReportManagerConstants.REPORT_CUSTOMER_PRICE_CPC:
				return new CustomerPriceCPCDAO();
			case ReportManagerConstants.REPORT_CUSTOMER_PRICE_IPC:
				return new CustomerPriceIPCDAO();
			case ReportManagerConstants.REPORT_MUS_CONTRACT_OMIT:
				return new MUSContractOmitDAO();
			case ReportManagerConstants.PURCHASE_DRILL_DOWN_SUMMARY:
				return new PurchaseDrillDownDAO();
			case ReportManagerConstants.REPORT_ITEM_INFORMATION:
				return new ReportItemInformationDAO();
			case ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES:
				return new UsageDetailTheraTimeSeriesDAO();
			case ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC:
				return new UsageDetailTherapeuticDAO();
			case ReportManagerConstants.REPORT_GENERIC_USAGE:
				return new UsageDetailGenericDAO();
			case ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY:
				return new ReportQuickItemPurchHistoryDAO();
			case ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_ACROSS_ACCOUNT:
				return new ReportQuickItemPurchHistoryDAO();
			case ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT:
				return new ReportQuickItemPurchHistoryDAO();

			case ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM:
				return new AccComparisonISDAO();

			case ReportManagerConstants.REPORT_ACC_COMPARISON_GENERIC:
				return new AccComparisonGGDAO();
			case ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE_THERA:
				return new ReportPurchaseCostVarianceTherSumDAO();
			case ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE:
				return new ReportPurchaseCostVarianceDAO();
			case ReportManagerConstants.REPORT_PERFORMANCE_TOP200:
				return new ReportPerformanceManagementTop200DAO();
			case ReportManagerConstants.REPORT_CONTROLLED_SUBSTANCE_GRP_ID:
				return new ControlledSubstanceDAO();

		    case ReportManagerConstants.REPORT_RETURNS_ANALYSIS:
			    return new ReportPerformanceManagementReturnAnalysisDAO();

			case ReportManagerConstants.REPORT_ITEM_MOVEMENT:
				return new ReportItemMovementDAO();
			case ReportManagerConstants.REPORT_PREFER_RX_SAVINGS:
				return new PreferRxSavingsDAO();
			case ReportManagerConstants.REPORT_SKY_SAVINGS:
				return new ReportSKYSavingOpportunityDAO();
            case ReportManagerConstants.REPORT_QUATERLY_DRUG_UTILIZATION:
				return new ReportQuaterlyDrugUtilizationCommonDAO();

			case ReportManagerConstants.REPORT_NEWLY_PURCHASED_ITEMS:
				return new NewlyPurchasedItemsDAO();

			case ReportManagerConstants.REPORT_CONTRACT_COMPLIANCE:
				return new ContractComplianceDAO();

			case ReportManagerConstants.REPORT_CREDIT_LESS_THAN_100_PERCENT:
				return new ReportPerformanceManagementCreditDAO();
			case ReportManagerConstants.FORECASE_REPORTS_DETAIL:
				return new AssetManagement12MonthInventoryTurnsForecastDetailDAO();
			case ReportManagerConstants.FORECASE_REPORTS_SUMMARY:
				return new AssetManagement12MonthInventoryTurnsForecastSummaryDAO();
			case ReportManagerConstants.REPORT_ACCOUNT_PROFILE:
				return new AccountProfileDAO();
		}
		return null;
	}
}

/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.mckesson.smora.ui.form.PurchaseDrillDownForm;
import com.mckesson.smora.ui.form.PurchaseDrillDownSelectionForm;

import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.database.dao.AccountGroupDB;
import com.mckesson.smora.database.dao.ReportStatusDB;
import com.mckesson.smora.database.model.TCustAcct;
import com.mckesson.smora.dto.DateSelectionAndComparisonVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.common.PurchaseDrillDownReportConstants;
import com.mckesson.smora.common.ReportManagerConstants;



/**
 * This action class processes search criteria and sends it to the back end to
 * generate the report. It will facilitate the “Submit and
 * View” options from the Criteria Selection page.
 *
 * Date      Author     Changes
 * 03/01/07  Siva / UmaShankar    Created and defined function calls.
 */

public class PurchaseDrillDownUtil
{
	private final String CLASSNAME = "PurchaseDrillDownUtil";
	ReportStatusDB reportStatusDB = new ReportStatusDB();


	/**
	 * This method is invoked if the user selects the product from the format option in the formatting tab.
	 *
	 * @param PurchaseDrillDownForm
	 * @param colHeader
	 * @param colVal
	 *
	 * @return PurchaseDrillDownForm
	 *
	 * @throws SMORAException
 */

	public PurchaseDrillDownForm drillDownReport(PurchaseDrillDownForm purchaseDrillDownForm, String colHeader, String colVal) throws SMORAException
	{
		final String METHODNAME = "drillDownReport";
		String drillDownRepName = null;

		String baseReport = null;
		String drillDownReportFirst = null;
		String drillDownReportSecond = null;
		String drillDownReportThird = null;
		String drillDownReportForth = null;
		String drillDownReportFifth = null;

		PurchaseDrillDownSelectionForm formattingSelectionForm =purchaseDrillDownForm.getPurchaseDrillDownSelectionForm();

		String formatOption = formattingSelectionForm.getPddFormat();

		try
		{
			if ((colHeader != null) && (colHeader.indexOf("'")==0))
			{
				colHeader = colHeader.replace("'", "").trim();
			}
			if ((colVal != null) && (colVal.indexOf("'")==0))
			{
				colVal = colVal.replace("'", "").trim();

			}

			purchaseDrillDownForm.setColHeader(colHeader);
			purchaseDrillDownForm.setColValue(colVal);


			if ((formatOption.equalsIgnoreCase(PurchaseDrillDownReportConstants.PRODUCT)))
			{
				baseReport = PurchaseDrillDownReportConstants.REPORT_A;
				drillDownReportFirst = PurchaseDrillDownReportConstants.REPORT_B;
				drillDownReportSecond = PurchaseDrillDownReportConstants.REPORT_C;
				drillDownReportThird = PurchaseDrillDownReportConstants.REPORT_D;
				drillDownReportForth = PurchaseDrillDownReportConstants.REPORT_E;
				drillDownReportFifth = PurchaseDrillDownReportConstants.REPORT_F;
			} else {
				baseReport = PurchaseDrillDownReportConstants.REPORT_U;
				drillDownReportFirst = PurchaseDrillDownReportConstants.REPORT_V;
				drillDownReportSecond = PurchaseDrillDownReportConstants.REPORT_W;
				drillDownReportThird = PurchaseDrillDownReportConstants.REPORT_X;
				drillDownReportForth = PurchaseDrillDownReportConstants.REPORT_Y;
				drillDownReportFifth = PurchaseDrillDownReportConstants.REPORT_Z;
			}

			if ((purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(baseReport)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase( PurchaseDrillDownReportConstants.REPORT_BB)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase( PurchaseDrillDownReportConstants.REPORT_AA)))
			{
				purchaseDrillDownForm.setSelectedField(colHeader);
				if ((colVal != null) && (colVal.indexOf("(") != -1))
				{
					colVal = colVal.substring(0,colVal.indexOf("("));
				}
				purchaseDrillDownForm.setSelectedValue(colVal);
				drillDownRepName = baseReport ;
			} else {
				drillDownRepName = baseReport ;
			}

			if ((purchaseDrillDownForm.getDrillDownReport() != null) && (!(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_A))) && (!(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_U))))
			{
				purchaseDrillDownForm = reportIsNotBaseReport(purchaseDrillDownForm,colHeader,colVal,drillDownRepName,drillDownReportFirst,drillDownReportSecond,drillDownReportThird,drillDownReportForth,drillDownReportFifth,baseReport,formattingSelectionForm);
			}
			else
			{
				purchaseDrillDownForm = reportIsBaseReport(purchaseDrillDownForm,colHeader,colVal,drillDownRepName,drillDownReportFirst,drillDownReportSecond,drillDownReportThird,drillDownReportForth,drillDownReportFifth,baseReport,formattingSelectionForm);
			}
		}
				catch (Exception e)
				{
					e.printStackTrace();
					throw new SMORAException(e, CLASSNAME, METHODNAME);
				}

				return purchaseDrillDownForm;

	}


	/**
	 * This method is invoked if the user selects the product from the format option in the formatting tab.
	 *
	 * @param PurchaseDrillDownForm
	 * @param colHeader
	 * @param colVal
	 *
	 * @return PurchaseDrillDownForm
	 *
	 * @throws SMORAException
 */

	public PurchaseDrillDownForm drillDownPreviousReport(PurchaseDrillDownForm purchaseDrillDownForm) throws SMORAException
	{
		final String METHODNAME = "drillDownPreviousReport";
		String drillDownRepName = null;

		String baseReport = null;
		String drillDownReportFirst = null;
		String drillDownReportSecond = null;
		String drillDownReportThird = null;
		String drillDownReportForth = null;
		String drillDownReportFifth = null;

		PurchaseDrillDownSelectionForm formattingSelectionForm =purchaseDrillDownForm.getPurchaseDrillDownSelectionForm();

		String formatOption = formattingSelectionForm.getPddFormat();
		drillDownRepName = purchaseDrillDownForm.getDrillDownReport();

		try
		{
			if ((formatOption.equalsIgnoreCase(PurchaseDrillDownReportConstants.PRODUCT)))
			{
				baseReport = PurchaseDrillDownReportConstants.REPORT_A;
				drillDownReportFirst = PurchaseDrillDownReportConstants.REPORT_B;
				drillDownReportSecond = PurchaseDrillDownReportConstants.REPORT_C;
				drillDownReportThird = PurchaseDrillDownReportConstants.REPORT_D;
				drillDownReportForth = PurchaseDrillDownReportConstants.REPORT_E;
				drillDownReportFifth = PurchaseDrillDownReportConstants.REPORT_F;
			} else {
				baseReport = PurchaseDrillDownReportConstants.REPORT_U;
				drillDownReportFirst = PurchaseDrillDownReportConstants.REPORT_V;
				drillDownReportSecond = PurchaseDrillDownReportConstants.REPORT_W;
				drillDownReportThird = PurchaseDrillDownReportConstants.REPORT_X;
				drillDownReportForth = PurchaseDrillDownReportConstants.REPORT_Y;
				drillDownReportFifth = PurchaseDrillDownReportConstants.REPORT_Z;
			}
			String custSelection = null;
			String currentFilter = null;
			String	startDate = null;
			String	groupName = null;
			String	groupID = null;

			String custInfo = "";
			if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(baseReport)))
			{
			   purchaseDrillDownForm.setPreviousReportFlag(1);
			   return purchaseDrillDownForm;
			} else if ((purchaseDrillDownForm.getOrigGroup().equalsIgnoreCase(formattingSelectionForm.getPddGroup())) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(baseReport)))
			{
				purchaseDrillDownForm.setPreviousReportFlag(1);
				return purchaseDrillDownForm;
			} else {
				 purchaseDrillDownForm.setPreviousReportFlag(0);
				 if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
					 custInfo = getAccountNameAddress(formattingSelectionForm.getPrimaryCustomerNumber(),"AccName");
				 } else {
					 custInfo = purchaseDrillDownForm.getSelectedGroup();
				 }

				 String strType = null;
				 if ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))) strType = "Net Purchases";
				 else if (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Mck1stp")) strType = "Mckesson OneStop";
				 //	Req. 6,33,45 changes done by Anil Kumar.
				 //Added for SO-3806 - To check if SynerGx or APCI column is selected
				 else if (purchaseDrillDownForm.getSelectedField().contains(ReportManagerConstants.PDD_APCI_SYNERGX_GRP))
						 {
					 int endIndex = purchaseDrillDownForm.getSelectedField().indexOf("_");
					 strType = purchaseDrillDownForm.getSelectedField().substring(0, endIndex);
						 }
				 else if (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("OthGen")) strType = "Other Generic";
				 else strType = purchaseDrillDownForm.getSelectedField();



				 if ((drillDownRepName.equalsIgnoreCase(drillDownReportFirst)) || (drillDownRepName.equalsIgnoreCase(drillDownReportSecond)) || (drillDownRepName.equalsIgnoreCase(drillDownReportThird)) || (drillDownRepName.equalsIgnoreCase(drillDownReportForth)) || (drillDownRepName.equalsIgnoreCase(drillDownReportFifth)))
				 {


					 custSelection = purchaseDrillDownForm.getCustSelection();
					 currentFilter = "NA";
					 startDate = purchaseDrillDownForm.getStartDate();
					 drillDownRepName = baseReport;
				 } else if(drillDownRepName.equalsIgnoreCase(baseReport)) {
					 String pddGroup = formattingSelectionForm.getPddGroup();
					 String str = pddGroup.substring(pddGroup.lastIndexOf(":")+1, pddGroup.length());
					 String gName = str.substring(0,str.indexOf("(")).trim();
					 String gID = str.substring(str.indexOf("(")+1, str.indexOf(")")).trim();

					 HashMap sMonth = purchaseDrillDownForm.getSelectedMonth();
					 if ((gName != null) && (gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.SUBGROUP)))
					 {
						 startDate = sMonth.get(drillDownReportFirst).toString();
						 currentFilter = getCurrentFilter(startDate,"MMToMon");
						 drillDownRepName = drillDownReportFirst;
						 if(pddGroup !=null)
						 {
							 formattingSelectionForm.setPddGroup(pddGroup.substring(0, pddGroup.lastIndexOf(":")));
						 }


					 } else if ((gName != null) && (gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REGION)))
					 {
						 startDate = sMonth.get(drillDownReportSecond).toString();
						 currentFilter = getCurrentFilter(startDate,"MMToMon");
						 drillDownRepName = drillDownReportSecond;
						 //11856: -changes starts
						 if(pddGroup !=null)
						 {
							 formattingSelectionForm.setPddGroup(pddGroup.substring(0, pddGroup.lastIndexOf(":")));
						 }
					 } else if ((gName != null) && (gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.DISTRICT)))
					 {
						 startDate = sMonth.get(drillDownReportThird).toString();
						 currentFilter = getCurrentFilter(startDate,"MMToMon");
						 drillDownRepName = drillDownReportThird;
						 if(pddGroup !=null)
						 {
							 formattingSelectionForm.setPddGroup(pddGroup.substring(0, pddGroup.lastIndexOf(":")));
						 }
					 } else if ((gName != null) && ((gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.DISTRICT))))
					 {
						 startDate = sMonth.get(drillDownReportFifth).toString();
						 currentFilter = getCurrentFilter(startDate,"MMToMon");
						 drillDownRepName = drillDownReportFifth;
						 if(pddGroup !=null)
						 {
							 formattingSelectionForm.setPddGroup(pddGroup.substring(0, pddGroup.lastIndexOf(":")));
						 }
					 }  else if ((gName != null) && (gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.TERRITORY)))
					 {
						 startDate = sMonth.get(drillDownReportForth).toString();
						 currentFilter = getCurrentFilter(startDate,"MMToMon");
						 drillDownRepName = drillDownReportForth;
						 if(pddGroup !=null)
						 {
							 formattingSelectionForm.setPddGroup(pddGroup.substring(0, pddGroup.lastIndexOf(":")));
						 }
					 } else if ((gName != null) && ((gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.CUSTOMER)) || (gName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.ACCOUNT))))
					 {
						 startDate = sMonth.get(drillDownReportFifth).toString();
						 currentFilter = getCurrentFilter(startDate,"MMToMon");
						 drillDownRepName = drillDownReportFifth;
						 if(pddGroup !=null)
						 {
							 formattingSelectionForm.setPddGroup(pddGroup.substring(0, pddGroup.lastIndexOf(":")));
						 }
						 //11856: -changes ends
					 }
				if (purchaseDrillDownForm.getCustSelection().lastIndexOf(":") != -1)
				{
				custSelection = purchaseDrillDownForm.getCustSelection().substring(0,purchaseDrillDownForm.getCustSelection().lastIndexOf(":"));
				purchaseDrillDownForm.setSelectedGroup(purchaseDrillDownForm.getSelectedGroup().substring(0,purchaseDrillDownForm.getSelectedGroup().lastIndexOf(":")));
				/*if(drillDownRepName != drillDownReportFirst)
				{
				formattingSelectionForm.setPddGroup(formattingSelectionForm.getPddGroup().substring(0,formattingSelectionForm.getPddGroup().lastIndexOf(":")));
				}*/
				} else {
					custSelection = purchaseDrillDownForm.getCustSelection();
				}
			} else if(drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_H))
			{
				startDate = purchaseDrillDownForm.getStartDate();
				groupName = purchaseDrillDownForm.getGroupName();
				groupID  = purchaseDrillDownForm.getGroupID();
				String accNo ="";

				 if ((formattingSelectionForm.getPrimaryCustomerNumber() == null) && (formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
			    	accNo = custInfo.substring(custInfo.lastIndexOf("("), custInfo.lastIndexOf(")"));

			    	custInfo = getAccountNameAddress(accNo,"AccName");
				 }
				custSelection = custInfo;
				currentFilter = custInfo + " for "+ getCurrentFilter(purchaseDrillDownForm.getStartDate(),"MMTOMon");

				drillDownRepName = PurchaseDrillDownReportConstants.REPORT_G;

			} else if (drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_G) || drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_I) ||drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_K)|| drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_L)||drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_M)||drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_S)||drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_AA) || drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_BB))
			{
				custSelection = purchaseDrillDownForm.getCustSelection();
				currentFilter = "NA";
				drillDownRepName = baseReport;

			}
			else if(drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_CC))
			{
				String selectedField= purchaseDrillDownForm.getSelectedField();
				if(selectedField != null && selectedField.equalsIgnoreCase("Non Contract"))
				{
					custSelection = purchaseDrillDownForm.getCustSelection();
					currentFilter = "NA";
					drillDownRepName = baseReport;
				}
				else
				{
					custSelection = purchaseDrillDownForm.getCustSelection();
					currentFilter =strType;
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_BB;
				}


			}
			else if(drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_DD))
			{
				String selectedField= purchaseDrillDownForm.getSelectedField();
				if(selectedField != null && (selectedField.equalsIgnoreCase("Contract") || selectedField.equalsIgnoreCase("Net Prch")) )
				{
					custSelection = purchaseDrillDownForm.getCustSelection();
					currentFilter = "NA";
					drillDownRepName = baseReport;
				}
				else
				{
					custSelection = purchaseDrillDownForm.getCustSelection();
					currentFilter =strType;
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_AA;
				}


			}
			else if(drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_J))
			{
				startDate = purchaseDrillDownForm.getStartDate();
				groupName = purchaseDrillDownForm.getGroupName();
				groupID  = purchaseDrillDownForm.getGroupID();
				custSelection =  purchaseDrillDownForm.getCustSelection();
				currentFilter = strType;
				drillDownRepName = PurchaseDrillDownReportConstants.REPORT_I;

			} else if ((drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_N)) || (drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_O)) || (drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_P))|| (drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_EE)) || (drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_FF)) )
			{
				String selectedField= purchaseDrillDownForm.getSelectedField();
				//Req. 6,33,45 changes done by Anil Kumar.
				 //Added for SO-3806 - To check if SynerGx or APCI column is selected
				if ((selectedField.trim().equalsIgnoreCase("Rx"))  || (selectedField.trim().equalsIgnoreCase("Branded"))  || (selectedField.trim().equalsIgnoreCase("Mck1stp")) || (selectedField.trim().contains(ReportManagerConstants.PDD_APCI_SYNERGX_GRP)) || (selectedField.trim().equalsIgnoreCase("MltSrc"))  || (selectedField.trim().equalsIgnoreCase("OthGen")) || (selectedField.trim().equalsIgnoreCase("PrfRx")) || (selectedField.trim().equalsIgnoreCase("RxPak"))){
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_K;
				} else if ((selectedField.equalsIgnoreCase("OTC")) || (selectedField.equalsIgnoreCase("HHC"))  || (selectedField.equalsIgnoreCase("PrLabl"))){
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_L;
				} else if ((selectedField.equalsIgnoreCase("NetPrch")) || (selectedField.equalsIgnoreCase("Whse")))
				{
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_M;
				} else if (selectedField.equalsIgnoreCase("Non Contract") || (selectedField.equalsIgnoreCase("Single Source")) || (selectedField.equalsIgnoreCase("Multi Source")) || (selectedField.equalsIgnoreCase("Adjusted Not on Contract"))){
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_CC;
				} else if ((selectedField.equalsIgnoreCase("Net Prch")) || (selectedField.equalsIgnoreCase("Contract")) || (selectedField.equalsIgnoreCase("Group")) || (selectedField.equalsIgnoreCase("Committed")) || (selectedField.equalsIgnoreCase("Individual")) || (selectedField.equalsIgnoreCase("Network Net")) || (selectedField.equalsIgnoreCase("OneStop")) || (selectedField.equalsIgnoreCase("Other")) || (selectedField.equalsIgnoreCase("Contract %"))) {
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_DD;
				}

				groupName =purchaseDrillDownForm.getGroupName();
				if ((groupName != null) && (groupName.equalsIgnoreCase("Total")))
				{
					currentFilter = strType;
				}
				else
				{
					currentFilter = strType + " / " + getCurrentFilter(purchaseDrillDownForm.getStartDate(),"MMToMon");
				}

				if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				{
					custSelection = custInfo;
				} else {
					String accName = null;
					String selGroup = formattingSelectionForm.getPddGroup();

					String group = null;
					if(selGroup != null)
					{
						group = selGroup.substring(selGroup.indexOf(":")+1);
						selGroup = selGroup.substring(0,selGroup.indexOf(":"));

					}
					if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					{
					  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
					  accName = getAccountNameAddress(accNum,"AccName");
					  custSelection = accName;
					}
					else
					{
						custSelection = purchaseDrillDownForm.getSelectedGroup();
					}

				}
			} else if(drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_Q))
			{
				groupName =purchaseDrillDownForm.getGroupName();
				String selValue = purchaseDrillDownForm.getSelectedValue();

				if ((groupName != null) && (groupName.equalsIgnoreCase("Total")))
				{
					currentFilter = strType;
				}
				else
				{
					currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
				}
				startDate = getCurrentFilter(selValue,"Date");
				if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				{
					custSelection =  custInfo;
				} else	{
					String accName = null;
					String selGroup = formattingSelectionForm.getPddGroup();

					String group = null;
					if(selGroup != null)
					{
						group = selGroup.substring(selGroup.indexOf(":")+1);
						selGroup = selGroup.substring(0,selGroup.indexOf(":"));

					}
					if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					{
					  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
					  accName = getAccountNameAddress(accNum,"AccName");
					  custSelection = accName;
					}
					else
					{
						custSelection = purchaseDrillDownForm.getSelectedGroup();
					}

				}
				drillDownRepName = PurchaseDrillDownReportConstants.REPORT_N;
			}
			else if((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_T)))
			{
				String accName = null;
				String selGroup = formattingSelectionForm.getPddGroup();

				String group = null;
				if(selGroup != null)
				{
					group = selGroup.substring(selGroup.indexOf(":")+1);
					selGroup = selGroup.substring(0,selGroup.indexOf(":"));

				}
				if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
				{
				  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
				  accName = getAccountNameAddress(accNum,"AccName");
				  custSelection = accName;
				}
				else
				{
					custSelection = purchaseDrillDownForm.getSelectedGroup();
				}
				currentFilter = "Returns for " + getCurrentFilter(purchaseDrillDownForm.getStartDate(),"MMTOMon");
				startDate = purchaseDrillDownForm.getStartDate();
				groupName =purchaseDrillDownForm.getGroupName();
				groupID =purchaseDrillDownForm.getGroupID();
				drillDownRepName = PurchaseDrillDownReportConstants.REPORT_S;
			} else if(drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_R))
			{
				groupName =purchaseDrillDownForm.getGroupName();
				String supplierName = purchaseDrillDownForm.getSupplierName();
			 if ((groupName != null) && (groupName.equalsIgnoreCase("Supplier")))
			 {
				 groupName = purchaseDrillDownForm.getGroupName();
			     groupID = purchaseDrillDownForm.getGroupID();
			     if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
			      custSelection =  custInfo;
				 } else
					{
					 	String accName = null;
						String selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));

						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}


			    	if ((purchaseDrillDownForm.getSelectedField() != null) && ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))))
			    	{
			    		currentFilter = strType + " from " + supplierName +"("+groupID+")";
			    	} else {
			    		currentFilter = strType + " purchases from " + supplierName +"("+groupID+")";
			    	}
					startDate = purchaseDrillDownForm.getStartDate();
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_Q;
			 } else if ((groupName != null) && (groupName.equalsIgnoreCase("Therapeutic")))
			 {
				 groupName = purchaseDrillDownForm.getGroupName();
			     groupID = purchaseDrillDownForm.getGroupID();
			     if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
			      custSelection =custInfo;
				 } else
					{
					 String accName = null;
						String selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));

						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}


			    	if ((purchaseDrillDownForm.getSelectedField() != null) && ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))))
			    	{
			    		currentFilter = strType + " from " + groupName +"("+groupID+")";
			    	} else {
			    		currentFilter = strType + " purchases from " + groupName +"("+groupID+")";
			    	}
					startDate = purchaseDrillDownForm.getStartDate();
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_O;
			 }
			 else if ((groupName != null) && (groupName.equalsIgnoreCase("Generic")))
			 {
				 groupName = purchaseDrillDownForm.getGroupName();
			     groupID = purchaseDrillDownForm.getGroupID();
			     if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
			      custSelection =custInfo;
				 } else
					{
					 String accName = null;
						String selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));

						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}


			    	if ((purchaseDrillDownForm.getSelectedField() != null) && ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))))
			    	{
			    		currentFilter = strType + " from " + groupName +"("+groupID+")";
			    	} else {
			    		currentFilter = strType + " purchases from " + groupName +"("+groupID+")";
			    	}
					startDate = purchaseDrillDownForm.getStartDate();
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_EE;
			 } else if ((groupName != null) && (groupName.equalsIgnoreCase("Contract")))
			 {
				 groupName = purchaseDrillDownForm.getGroupName();
			     groupID = purchaseDrillDownForm.getGroupID();
			     if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
			      custSelection =custInfo;
				 } else
					{
					 String accName = null;
						String selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));

						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}


			    	if ((purchaseDrillDownForm.getSelectedField() != null) && ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))))
			    	{
			    		currentFilter = strType + " from " + groupName +"("+groupID+")";
			    	} else {
			    		currentFilter = strType + " purchases from " + groupName +"("+groupID+")";
			    	}
					startDate = purchaseDrillDownForm.getStartDate();
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_FF;
			 } else if ((groupName != null) && (groupName.equalsIgnoreCase("MICA")))
			 {
				 groupName = purchaseDrillDownForm.getGroupName();
			     groupID = purchaseDrillDownForm.getGroupID();
			     if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				 {
			      custSelection =  custInfo;
				 } else
					{
					 String accName = null;
						String selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));


						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}


			    	if ((purchaseDrillDownForm.getSelectedField() != null) && ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))))
			    	{
			    		currentFilter = strType + " from " + groupName +"("+groupID+")";
			    	} else {
			    		currentFilter = strType + " purchases from " + groupName +"("+groupID+")";
			    	}
					startDate = purchaseDrillDownForm.getStartDate();
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_P;
			 }
			 else
			 {
				 String selectedField= purchaseDrillDownForm.getSelectedField();
				 if (selectedField!= null)
				 {
//					Req. 6,33,45 changes done by Anil Kumar.
					 //Added for SO-3806 - To check if SynerGx or APCI column is selected
					if ((selectedField.trim().equalsIgnoreCase("Rx"))  || (selectedField.trim().equalsIgnoreCase("Branded"))  || (selectedField.trim().equalsIgnoreCase("Mck1stp")) || (selectedField.trim().contains(ReportManagerConstants.PDD_APCI_SYNERGX_GRP))  || (selectedField.trim().equalsIgnoreCase("MltSrc"))  || (selectedField.trim().equalsIgnoreCase("OthGen")) || (selectedField.trim().equalsIgnoreCase("PrfRx")) || (selectedField.trim().equalsIgnoreCase("RxPak"))){
							drillDownRepName = PurchaseDrillDownReportConstants.REPORT_K;
					} else if ((selectedField.equalsIgnoreCase("OTC")) || (selectedField.equalsIgnoreCase("HHC"))  || (selectedField.equalsIgnoreCase("PrLabl"))){
							drillDownRepName = PurchaseDrillDownReportConstants.REPORT_L;
					} else if ((selectedField.equalsIgnoreCase("NetPrch")) || (selectedField.equalsIgnoreCase("Whse")))
					{
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_M;
					} else if (selectedField.equalsIgnoreCase("Non Contract")){
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_CC;
					} else if ((selectedField.equalsIgnoreCase("Net Prch")) || (selectedField.equalsIgnoreCase("Contract"))) {
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_DD;
					}
				 }
				groupName =purchaseDrillDownForm.getGroupName();
				if ((groupName != null) && (groupName.equalsIgnoreCase("Total")))
				{
					currentFilter = strType;
				}
				else
				{
					currentFilter = strType + " / " + getCurrentFilter(purchaseDrillDownForm.getStartDate(),"MMToMon");
				}

				if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				{
					custSelection =  custInfo;
				} else {
					String accName = null;
					String selGroup = formattingSelectionForm.getPddGroup();

					String group = null;
					if(selGroup != null)
					{
						group = selGroup.substring(selGroup.indexOf(":")+1);
						selGroup = selGroup.substring(0,selGroup.indexOf(":"));

					}
					if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					{
					  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
					  accName = getAccountNameAddress(accNum,"AccName");
					  custSelection = accName;
					}
					else
					{
						custSelection = purchaseDrillDownForm.getSelectedGroup();
					}

				}
			 }
			}

			purchaseDrillDownForm.setCurrentFilter(currentFilter);
			purchaseDrillDownForm.setCustSelection(custSelection);
			purchaseDrillDownForm.setDrillDownReport(drillDownRepName);
			purchaseDrillDownForm.setGroupName(groupName);
			purchaseDrillDownForm.setGroupID(groupID);
			purchaseDrillDownForm.setSubReportId(getSubReportID(drillDownRepName));
			purchaseDrillDownForm.setStartDate(startDate);
		 }
		} catch (Exception e) {
					e.printStackTrace();
					throw new SMORAException(e, CLASSNAME, METHODNAME);
		}
		return purchaseDrillDownForm;
	}


	/**
	 * This method is called when the drill down report is not a base report.
	 *
	 * @param custNum the the string
	 *
	 * @return the String
	 */
	public PurchaseDrillDownForm reportIsNotBaseReport(PurchaseDrillDownForm purchaseDrillDownForm,String colHeader,String colVal,String drillDownRepName,String drillDownReportFirst,String drillDownReportSecond,String drillDownReportThird,String drillDownReportForth,String drillDownReportFifth,String baseReport,PurchaseDrillDownSelectionForm formattingSelectionForm) throws SMORAException
	{

		String groupName = null;
		String groupID = null;
		String currentFilter = null;
		String custSelection = null;
		String startDate = null;
		String selGroup = "";
		String strType = null;
		String prevReport = null;
		String suplierName = null;
		String custInfo = null;
		if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
		{
    	  custInfo = getAccountNameAddress(formattingSelectionForm.getPrimaryCustomerNumber(),"AccName");
		}

		    	if ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))) strType = "Net Purchases";
				else if (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Mck1stp")) strType = "Mckesson OneStop";
//		    	Req. 6,33,45 changes done by Anil Kumar.
		    	 //Added for SO-3806 - To check if SynerGx or APCI column is selected
				 else if (purchaseDrillDownForm.getSelectedField().contains(ReportManagerConstants.PDD_APCI_SYNERGX_GRP))
				 {
					 int endIndex = purchaseDrillDownForm.getSelectedField().indexOf("_");
					 strType = purchaseDrillDownForm.getSelectedField().substring(0, endIndex);
				 }
				else if (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("OthGen")) strType = "Other Generic";
				else strType = purchaseDrillDownForm.getSelectedField();

			if ((colHeader != null) && (!(colHeader.equalsIgnoreCase(PurchaseDrillDownReportConstants.MONTH))) &&
					((purchaseDrillDownForm.getDrillDownReport() == baseReport) ||
			(purchaseDrillDownForm.getDrillDownReport() == drillDownReportFirst) ||
			(purchaseDrillDownForm.getDrillDownReport() == drillDownReportSecond) ||
			(purchaseDrillDownForm.getDrillDownReport() == drillDownReportThird) ||
			(purchaseDrillDownForm.getDrillDownReport() == drillDownReportForth) ||
			(purchaseDrillDownForm.getDrillDownReport() == drillDownReportFifth)))
			{
				String selectedGroup = null;
    		if(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(drillDownReportFirst))
				{
					selectedGroup = purchaseDrillDownForm.getSelectedGroup()+" : " +colVal;
				    colVal=colVal.substring(colVal.lastIndexOf("(")+1, colVal.lastIndexOf(")"));
				    selGroup = formattingSelectionForm.getPddGroup()+": " + colHeader+"("+colVal+")";
    		} else if (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(drillDownReportFifth)){
			    	selectedGroup = purchaseDrillDownForm.getSelectedGroup()+": "+getAccountNameAddress(colVal,"AccName");
				    selGroup = formattingSelectionForm.getPddGroup()+": " + colHeader+"("+colVal+")";
				    currentFilter = "NA";
			    } else {
				selGroup = formattingSelectionForm.getPddGroup()+": " + colHeader+"("+colVal+")";
				selectedGroup = purchaseDrillDownForm.getSelectedGroup()+" : " + colHeader+"("+colVal+")";
			    }

    		custSelection = purchaseDrillDownForm.getCustSelection();
					purchaseDrillDownForm.setSelectedGroup(selectedGroup);
					formattingSelectionForm.setPddGroup(selGroup);
			}
			if(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_G))
			{
				groupName = colHeader;
				groupID = colVal;

					String primaryCustomerNumber = formattingSelectionForm.getPrimaryCustomerNumber();

					String custDetails = colVal;
					String[] sGroup = custDetails.split(":");

					String accName = null;
					if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
					{
						currentFilter = getAccountNameAddress(primaryCustomerNumber, "Address")+ "  Account : "+primaryCustomerNumber+" INV DATE: "+ sGroup[1]+" INV/CM : "+sGroup[0];
					} else {
						if (formattingSelectionForm.getPddGroup().indexOf("Account Number") != -1)
						{
							String accId = formattingSelectionForm.getPddGroup().substring(formattingSelectionForm.getPddGroup().lastIndexOf("(")+1, formattingSelectionForm.getPddGroup().lastIndexOf(")"));
							currentFilter = getAccountNameAddress(accId, "Address")+ "  Account : "+accId+" INV DATE: "+ sGroup[1]+" INV/CM : "+sGroup[0];
							accName = getAccountNameAddress(accId,"AccName");
						} else if (purchaseDrillDownForm.getAccGroupName().equalsIgnoreCase(PurchaseDrillDownReportConstants.CUSTOMER))
						{
							currentFilter = getAccountNameAddress(purchaseDrillDownForm.getAccGroupID(), "Address")+ "  Account : "+purchaseDrillDownForm.getAccGroupID()+" INV DATE: "+ sGroup[1]+" INV/CM : "+sGroup[0];
						}
					}
					groupID = sGroup[0];
					//Added for RRP 1 Issue 476 - tv193xi
					//if (sGroup.length > 2 ) {
					//	secondaryCustomerNumber = sGroup[2];
					//}

					purchaseDrillDownForm.setSecondaryCustomerNumber(sGroup[2]);

					startDate = getCurrentFilter(purchaseDrillDownForm.getSelectedValue(),"Date");
					if((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
					{
						custSelection = custInfo;
					} else if (purchaseDrillDownForm.getAccGroupName().equalsIgnoreCase(PurchaseDrillDownReportConstants.CUSTOMER))
					{
						custSelection = purchaseDrillDownForm.getCustSelection();
					}
					else
					{
						custSelection = purchaseDrillDownForm.getSelectedGroup()+" : "+accName;
					}

					drillDownRepName =  PurchaseDrillDownReportConstants.REPORT_H;
		} else if(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_I))
		{
					groupName = colHeader;
					groupID = colVal;
					startDate = getCurrentFilter(colVal,"Date");
					selGroup = formattingSelectionForm.getPddGroup();

					if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
					{
						custSelection = custInfo;
					} else {
						String accName = null;
						selGroup = formattingSelectionForm.getPddGroup();

						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));

						}
						if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
						{
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
						}
						else
						{
							custSelection = purchaseDrillDownForm.getSelectedGroup();
						}
					}
					String selValue = null;
					if(colHeader.equalsIgnoreCase("Contract McKesson")) selValue  = "- Mckesson Contract for";
					else if(colHeader.equalsIgnoreCase("Contract Below Cost")) selValue = "- Supplier Contracts with Chargebacks for";
					else if (colHeader.equalsIgnoreCase("At or Above Cost Cntrct")) selValue = "- Supplier Contracts without Chargebacks for";

					currentFilter = purchaseDrillDownForm.getCurrentFilter()+ " "+selValue+" "+ getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");

					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_J;
		} else if ((purchaseDrillDownForm.getDrillDownReport() != null) && ((purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_K)) ||
						(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_L)) ||
						(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_M)) ||
						(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_CC)) ||
			(purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_DD))))
		{
			if ((colHeader != null) && (colHeader.trim().equals(PurchaseDrillDownReportConstants.SUPPLIER)))
			{
						String selValue = purchaseDrillDownForm.getSelectedValue();
				if ((purchaseDrillDownForm.getGroupName() != null) && (purchaseDrillDownForm.getGroupName().equalsIgnoreCase("Total")))
						{
							currentFilter = strType;
						}
						else
						{
							currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
						}

						startDate = getCurrentFilter(selValue,"Date");
						if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
				    	 custSelection =  custInfo;
						} else
						{
							String accName = null;
							selGroup = formattingSelectionForm.getPddGroup();
							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));


							}
						  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
						  {
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
						  }
						  else
						  {
							  custSelection =  purchaseDrillDownForm.getSelectedGroup();
						  }
						}

						groupName = purchaseDrillDownForm.getGroupName();

						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_N;
			} else if ((colHeader != null) && (colHeader.trim().equals(PurchaseDrillDownReportConstants.THERAPEUTIC)))
			{
				String selValue = purchaseDrillDownForm.getSelectedValue();

				if ((purchaseDrillDownForm.getGroupName() != null) && (purchaseDrillDownForm.getGroupName().equalsIgnoreCase("Total")))
				{
					currentFilter = strType;
				}
				else
				{
					currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
				}

				startDate = getCurrentFilter(selValue,"Date");
				if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
				{
			   	 custSelection =  custInfo;
				} else
				{
					String accName = null;
					selGroup = formattingSelectionForm.getPddGroup();

					String group = null;
					if(selGroup != null)
					{
						group = selGroup.substring(selGroup.indexOf(":")+1);
						selGroup = selGroup.substring(0,selGroup.indexOf(":"));

					}
					if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					{
					  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
					  accName = getAccountNameAddress(accNum,"AccName");
					  custSelection = accName;
					}
					else
					{
						custSelection = purchaseDrillDownForm.getSelectedGroup();
					}
				}

				groupName = purchaseDrillDownForm.getGroupName();
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_O;
			} else if ((colHeader != null) && (colHeader.trim().equals(PurchaseDrillDownReportConstants.ITEMDETAIL)))
			{
						String selValue = purchaseDrillDownForm.getSelectedValue();
						startDate = getCurrentFilter(selValue,"Date");
				if ((purchaseDrillDownForm.getGroupName() != null) && (purchaseDrillDownForm.getGroupName().equalsIgnoreCase("Total")))
						{
							currentFilter = strType;
						}
						else
						{
					if ((purchaseDrillDownForm.getSelectedField() != null) && (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch") || purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch")))
			    	{
						currentFilter = strType + " purchases for " + getCurrentFilter(selValue,"CurrentFilter");
			    	} else {

			    		currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
			    	}
						}
						if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
				    	 custSelection =  custInfo;
						} else
						{

							String accName = null;
							selGroup = formattingSelectionForm.getPddGroup();

							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
							}
							else
							{
								custSelection = purchaseDrillDownForm.getSelectedGroup();
							}

						}
						groupName = purchaseDrillDownForm.getGroupName();
						prevReport = purchaseDrillDownForm.getDrillDownReport();
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_R;
					} else if ((colHeader != null) && (colHeader.trim().equals(PurchaseDrillDownReportConstants.MICA)))
					{
						String selValue = purchaseDrillDownForm.getSelectedValue();
						if ((purchaseDrillDownForm.getGroupName() != null) && (purchaseDrillDownForm.getGroupName().equalsIgnoreCase("Total")))
						{
							currentFilter = strType;
						}
						else
						{
							currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
						}

						startDate = getCurrentFilter(selValue,"Date");
						if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
					   	 custSelection =  custInfo;
						} else
						{
							String accName = null;
							 selGroup = formattingSelectionForm.getPddGroup();

							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
							}
							else
							{
								custSelection = purchaseDrillDownForm.getSelectedGroup();
							}
						}

						groupName = purchaseDrillDownForm.getGroupName();
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_P;
					} else if ((colHeader != null) && (colHeader.trim().equals(PurchaseDrillDownReportConstants.GENERIC)))
					{

						String selValue = purchaseDrillDownForm.getSelectedValue();

						if ((purchaseDrillDownForm.getGroupName() != null) && (purchaseDrillDownForm.getGroupName().equalsIgnoreCase("Total")))
						{
							currentFilter = strType;
						}
						else
						{
							currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
						}

						startDate = getCurrentFilter(selValue,"Date");
						if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
					   	 custSelection =  custInfo;
						} else
						{
							String accName = null;
							selGroup = formattingSelectionForm.getPddGroup();

							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
							}
							else
							{
								custSelection = purchaseDrillDownForm.getSelectedGroup();
							}
						}

						groupName = purchaseDrillDownForm.getGroupName();
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_EE;
					} else if ((colHeader != null) && (colHeader.trim().equals(PurchaseDrillDownReportConstants.CONTRACT))) {


						String selValue = purchaseDrillDownForm.getSelectedValue();

						if ((purchaseDrillDownForm.getGroupName() != null) && (purchaseDrillDownForm.getGroupName().equalsIgnoreCase("Total")))
						{
							currentFilter = strType;
						}
						else
						{
							currentFilter = strType + " for " + getCurrentFilter(selValue,"CurrentFilter");
						}

						startDate = getCurrentFilter(selValue,"Date");
						if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
					   	 custSelection =  custInfo;
						} else
						{
							String accName = null;
							selGroup = formattingSelectionForm.getPddGroup();

							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
							}
							else
							{
								custSelection = purchaseDrillDownForm.getSelectedGroup();
							}
						}

						groupName = purchaseDrillDownForm.getGroupName();
						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_FF;

					}
	//	}  else if ((purchaseDrillDownForm.getDrillDownReport()!= null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_N)))
			// QC11543 Added the colVal check to handle duplicate request on click of Supplier 
	}  else if ((purchaseDrillDownForm.getDrillDownReport()!= null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_N)) && colVal != null && colVal.length()>0)
				{ 
	
					suplierName = colVal.substring(0,colVal.indexOf("("));
			    	colVal=colVal.substring(colVal.lastIndexOf("(")+1, colVal.lastIndexOf(")"));
			    	if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
					{
			    	 custSelection =  custInfo;
					} else
					{
						String accName = null;
						selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));


						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}


	    	if ((purchaseDrillDownForm.getSelectedField() != null) && ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))))
			    	{
			    		currentFilter = strType + " from " + suplierName +"("+colVal+")";
			    	} else {
			    		currentFilter = strType + " purchases from " + suplierName +"("+colVal+")";
			    	}
			    	purchaseDrillDownForm.setSupplierName(suplierName);

					groupName = colHeader;
					groupID = colVal;
					startDate = purchaseDrillDownForm.getStartDate();

					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_Q;
		} else if ((purchaseDrillDownForm.getDrillDownReport() != null) && ((purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_Q)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_O)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_P)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_FF)) || (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_EE))))
				{
			if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_Q)))
					{
						groupID = purchaseDrillDownForm.getGroupID();
						groupName = "Supplier";
						startDate = getCurrentFilter(colVal,"Date");
						//currentFilter = purchaseDrillDownForm.getSupplierName() +" by Supplier ID: "+groupID+" for " +getCurrentFilter(startDate,"MMToMon");
						currentFilter = strType +" by Supplier ID: "+groupID+" for " +getCurrentFilter(startDate,"MMToMon");
					} else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_O)))
					{
						groupID = colVal;
						groupName = "Therapeutic";
						startDate = purchaseDrillDownForm.getStartDate();
						currentFilter = strType +" by Therapeutic Code: "+colVal+" for " +getCurrentFilter(startDate,"MMToMon");
					}
					else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_EE)))
					{
						groupID = colVal;
						groupName = "Generic";
						startDate = purchaseDrillDownForm.getStartDate();
						currentFilter = strType +" by Generic : "+colVal+" for " +getCurrentFilter(startDate,"MMToMon");
					}
					else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_FF)))
					{
						groupID = colVal;
						groupName = "Contract";
						startDate = purchaseDrillDownForm.getStartDate();
						currentFilter = colHeader +" by Contract : "+colVal+" for " +getCurrentFilter(startDate,"MMToMon");
					} else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_P)))
					{
						groupID = colVal;
						groupName = "MICA";
						startDate = purchaseDrillDownForm.getStartDate();
						currentFilter = colHeader +" by MICA Dept ID : "+colVal+" for " +getCurrentFilter(startDate,"MMToMon");
					} else {
			if ((purchaseDrillDownForm.getSelectedField() != null) && (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")||purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch")))
			    	{
						currentFilter = strType + " purchases for " + getCurrentFilter(colVal,"CurrentFilter");

			    	} else {
			    		currentFilter = strType + " for " + getCurrentFilter(colVal,"CurrentFilter");
			    	}
					}
					if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
					{
			    	 custSelection = custInfo;
					} else
					{
						String accName = null;
						selGroup = formattingSelectionForm.getPddGroup();
						String group = null;
						if(selGroup != null)
						{
							group = selGroup.substring(selGroup.indexOf(":")+1);
							selGroup = selGroup.substring(0,selGroup.indexOf(":"));


						}
					  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
					  {
						  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
						  accName = getAccountNameAddress(accNum,"AccName");
						  custSelection = accName;
					  }
					  else
					  {
						  custSelection =  purchaseDrillDownForm.getSelectedGroup();
					  }
					}
					prevReport = purchaseDrillDownForm.getDrillDownReport();
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_R;
		} else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_S)))
				{
	    			custSelection =purchaseDrillDownForm.getSelectedGroup();
	    		if ((colVal != null) && (colVal.trim().equalsIgnoreCase("1")))
	    		     currentFilter = "Pharmacies with Highest Return $ for ";
	    		else
	    			currentFilter = "Pharmacies with Highest Return % for ";


	    		currentFilter = currentFilter + " "+ getCurrentFilter(purchaseDrillDownForm.getStartDate(),"MMTOMon");

	    		String stDate = purchaseDrillDownForm.getStartDate();
	    		//startDate = getCurrentFilter(stDate,"Date");
	    		startDate = stDate;
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_T;
		}
		else if((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_T)))
		{
			String accName = null;
			selGroup = formattingSelectionForm.getPddGroup();

			String group = null;
			if(selGroup != null)
			{
				group = selGroup.substring(selGroup.indexOf(":")+1);
				selGroup = selGroup.substring(0,selGroup.indexOf(":"));

			}
			if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
			{
			  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
			  accName = getAccountNameAddress(accNum,"AccName");
			  custSelection = accName;
			}
			else
			{
				custSelection = purchaseDrillDownForm.getSelectedGroup();
			}
			currentFilter = "Returns for " + getCurrentFilter(purchaseDrillDownForm.getStartDate(),"MMTOMon");
			startDate = purchaseDrillDownForm.getStartDate();
			groupName = colHeader;
			groupID = colVal;
			drillDownRepName = PurchaseDrillDownReportConstants.REPORT_S;
		}
		else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_BB)))
				{
					groupName = colHeader;
					groupID = colVal;

					custSelection = purchaseDrillDownForm.getCustSelection();
					currentFilter = "Non Contract - " + colHeader + "/" + getCurrentFilter(purchaseDrillDownForm.getSelectedValue(),"CurrentFilter");

					startDate = getCurrentFilter(purchaseDrillDownForm.getSelectedValue(),"Date");
					drillDownRepName =  PurchaseDrillDownReportConstants.REPORT_CC;
				}
		else if ((purchaseDrillDownForm.getDrillDownReport() != null) && (purchaseDrillDownForm.getDrillDownReport().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_AA)))
		{
			groupName = colHeader;
			groupID = colVal;
			custSelection = purchaseDrillDownForm.getCustSelection();
			currentFilter = "Contract - " + colHeader + "/" + getCurrentFilter(purchaseDrillDownForm.getSelectedValue(),"CurrentFilter");
			startDate = getCurrentFilter(purchaseDrillDownForm.getSelectedValue(),"Date");
			drillDownRepName =  PurchaseDrillDownReportConstants.REPORT_DD;
		}




    	purchaseDrillDownForm.setCurrentFilter(currentFilter);
    	purchaseDrillDownForm.setCustSelection(custSelection);

    	purchaseDrillDownForm.setDrillDownReport(drillDownRepName);
    	purchaseDrillDownForm.setGroupName(groupName);
    	purchaseDrillDownForm.setGroupID(groupID);
    	purchaseDrillDownForm.setSubReportId(getSubReportID(drillDownRepName));
    	purchaseDrillDownForm.setStartDate(startDate);

		return purchaseDrillDownForm;
	} // end of the method getAccountNameAddress


	/**
	 * This method is called when the drill down report is a base report.
	 *
	 * @param custNum the the string
	 *
	 * @return the String
	 */
	public PurchaseDrillDownForm reportIsBaseReport(PurchaseDrillDownForm purchaseDrillDownForm,String colHeader,String colVal,String drillDownRepName,String drillDownReportFirst,String drillDownReportSecond,String drillDownReportThird,String drillDownReportForth,String drillDownReportFifth,String baseReport,PurchaseDrillDownSelectionForm formattingSelectionForm) throws SMORAException
			{

		String groupName = null;
		String groupID = null;
		String currentFilter = null;
		String custSelection = null;
		String startDate = null;
		String selGroup = "";
		String strType = null;
		String selGroupName = null;
		String selGroupID = null;
		String currFilter = purchaseDrillDownForm.getCurrentFilter();
		String custInfo = null;
		if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
		{
    	  custInfo = getAccountNameAddress(formattingSelectionForm.getPrimaryCustomerNumber(),"AccName");
		}
		
		if (purchaseDrillDownForm.getSelectedField() != null)
		{
		    	if ((purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("NetPrch")) || (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Net Prch"))) strType = "Net Purchases";
				else if (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("Mck1stp")) strType = "Mckesson OneStop";
//		    	Req. 6,33,45 changes done by Anil Kumar.
		    	 //Added for SO-3806 - To check if SynerGx or APCI column is selected
				 else if (purchaseDrillDownForm.getSelectedField().contains(ReportManagerConstants.PDD_APCI_SYNERGX_GRP))
				 {
					 int endIndex = purchaseDrillDownForm.getSelectedField().indexOf("_");
					 strType = purchaseDrillDownForm.getSelectedField().substring(0, endIndex);
				 }
				else if (purchaseDrillDownForm.getSelectedField().equalsIgnoreCase("OthGen")) strType = "Other Generic";
				else strType = purchaseDrillDownForm.getSelectedField();
		}

				PurchaseDrillDownSelectionForm formatSelectionForm = purchaseDrillDownForm.getPurchaseDrillDownSelectionForm();
				if ((colHeader != null) && (colHeader.equalsIgnoreCase(PurchaseDrillDownReportConstants.MONTH)))
				{
					if ((formatSelectionForm.getPrimaryCustomerNumber() != null) && !(formatSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
					{
					groupName = "Account Number";
					groupID = formatSelectionForm.getPrimaryCustomerNumber();

					startDate = getCurrentFilter(colVal,"Date");
					custSelection = custInfo;
					custInfo = getAccountNameAddress(formattingSelectionForm.getPrimaryCustomerNumber(),"Address");
					currentFilter = custInfo + " for "+ getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");

					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_G;
					}
				else
				{
					selGroup = formattingSelectionForm.getPddGroup();
			String custAddress = null;

					String delim = ":";
					String[] sGroup = selGroup.split(delim);
					if (sGroup.length>2)
					{
						String x = sGroup[sGroup.length-1];
						groupName = x.substring(0, x.indexOf("("));
						groupID = x.substring(x.indexOf("(")+1, x.indexOf(")"));
					} else
					{
						groupName = sGroup[0];
						String y = sGroup[1];
						groupID = y.substring(y.indexOf("(")+1, y.indexOf(")"));
					}

				startDate = getCurrentFilter(colVal,"Date");
				HashMap selMonth = purchaseDrillDownForm.getSelectedMonth();
				if ((groupName != null) && (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.NATLGROUP)))
				{
					currentFilter = getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					selMonth.put(drillDownReportFirst,startDate);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					drillDownRepName = drillDownReportFirst;
				} else if ((groupName != null) && ((groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.SUBGROUP)) || (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.CHAIN))))
				{
					currentFilter = getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					selMonth.put(drillDownReportSecond,startDate);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					drillDownRepName = drillDownReportSecond;
				} else if ((groupName != null) && (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REGION)))
				{
					currentFilter = getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					selMonth.put(drillDownReportThird,startDate);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					drillDownRepName = drillDownReportThird;
				} else if ((groupName != null) && ((groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.DISTRICT))))
				{
					currentFilter = getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					selMonth.put(drillDownReportFifth,startDate);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					drillDownRepName = drillDownReportFifth;
				}  else if ((groupName != null) && (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.DC)))
				{
					currentFilter = getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					selMonth.put(drillDownReportForth,startDate);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					drillDownRepName = drillDownReportForth;
				} else if ((groupName != null) && (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.TERRITORY)))
				{
					currentFilter = getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					selMonth.put(drillDownReportFifth,startDate);
					purchaseDrillDownForm.setSelectedMonth(selMonth);
					drillDownRepName = drillDownReportFifth;
				} else if ((groupName != null) && ((groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.CUSTOMER)) || (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.ACCOUNT))))
				{
					custAddress = getAccountNameAddress(groupID,"Address");
					currentFilter = custAddress + " for "+ getCurrentFilter(purchaseDrillDownForm.getColValue(),"CurrentFilter");
					custAddress = getAccountNameAddress(groupID,"AccName");
					selGroupName = PurchaseDrillDownReportConstants.CUSTOMER;
					selGroupID = groupID;
					purchaseDrillDownForm.setAccGroupName(selGroupName);
					purchaseDrillDownForm.setAccGroupID(selGroupID);
					drillDownRepName = PurchaseDrillDownReportConstants.REPORT_G;
				}
				startDate = getCurrentFilter(colVal,"Date");

					String pddGroup = formattingSelectionForm.getPddGroup();
					String gName =selGroup.substring(0,selGroup.indexOf(":"));
			if ((gName != null) && (gName.equalsIgnoreCase("Natl Group")))
					{
						custSelection =  purchaseDrillDownForm.getSelectedGroup();
			} else if ((gName != null) && (gName.trim().equalsIgnoreCase("Chain")))
					{
						custSelection =  purchaseDrillDownForm.getSelectedGroup();
			} else if ((gName != null) && (gName.trim().equalsIgnoreCase("DC")))
					{
						custSelection = purchaseDrillDownForm.getSelectedGroup();
			} else if ((gName != null) && ((gName.trim().equalsIgnoreCase("Customer")) || (groupName.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.ACCOUNT)) ))
			{

						custSelection = "";
			}


			if ((drillDownRepName != null) && (drillDownRepName.equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_G)))
			{


				if(custSelection ==null ||  custSelection.equalsIgnoreCase(""))
 				{

					custSelection = custAddress;
				}



					}
				 }
				} else {

					if ((colVal!=null) && (colVal.trim().equals("")))
					{
						if ((colHeader != null) && (colHeader.trim().equalsIgnoreCase("Non Contract")))
						{
							//custSelection = purchaseDrillDownForm.getCustSelection();
							if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
							{
								custSelection = custInfo;
							} else {
							String accName = null;
							selGroup = formattingSelectionForm.getPddGroup();

							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
							}
							else
							{
								custSelection = purchaseDrillDownForm.getSelectedGroup();
							}
							}
							currentFilter = strType;
							drillDownRepName = PurchaseDrillDownReportConstants.REPORT_BB;
						} else if ((colHeader != null) && (colHeader.trim().equalsIgnoreCase("Contract")))
						{
							//custSelection = purchaseDrillDownForm.getCustSelection();
							//currentFilter = strType;
							//drillDownRepName = PurchaseDrillDownReportConstants.REPORT_AA;

							//custSelection = purchaseDrillDownForm.getCustSelection();
							if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
							{
								custSelection = custInfo;
							} else {
							String accName = null;
							selGroup = formattingSelectionForm.getPddGroup();

							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
							}
							else
							{
								custSelection = purchaseDrillDownForm.getSelectedGroup();
							}
							}
							currentFilter = strType;
							drillDownRepName = PurchaseDrillDownReportConstants.REPORT_AA;


						} else {
						if ((formatSelectionForm.getPrimaryCustomerNumber() != null) && !(formatSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
						  custSelection = custInfo;
						} else {
							String accName = null;
							selGroup = formatSelectionForm.getPddGroup();
							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
						  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
						  {
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
						  } else {
						    custSelection =  purchaseDrillDownForm.getSelectedGroup();
						  }
						}
							currentFilter = strType;

						drillDownRepName = PurchaseDrillDownReportConstants.REPORT_I;
						}
					} else {
				if (colHeader != null) {
//					Req. 6,33,45 changes done by Anil Kumar.
					 //Added for SO-3806 - To check if SynerGx or APCI column is selected
						if ((colHeader.trim().equalsIgnoreCase("Rx"))  || (colHeader.trim().equalsIgnoreCase("Branded"))  || (colHeader.trim().equalsIgnoreCase("Mck1stp")) || (colHeader.trim().contains(ReportManagerConstants.PDD_APCI_SYNERGX_GRP)) || (colHeader.trim().equalsIgnoreCase("MltSrc"))  || (colHeader.trim().equalsIgnoreCase("OthGen")) || (colHeader.trim().equalsIgnoreCase("PrfRx")) || (colHeader.trim().equalsIgnoreCase("RxPak"))){
							startDate = getCurrentFilter(colVal,"Date");
							drillDownRepName = PurchaseDrillDownReportConstants.REPORT_K;
					    } else if ((colHeader.equalsIgnoreCase("OTC")) || (colHeader.equalsIgnoreCase("HHC"))  || (colHeader.equalsIgnoreCase("PrLabl"))){
					    	startDate = getCurrentFilter(colVal,"Date");
					    	drillDownRepName = PurchaseDrillDownReportConstants.REPORT_L;
					    } else if ((colHeader.equalsIgnoreCase("NetPrch")) || (colHeader.equalsIgnoreCase("Whse")))
					    {
					    	startDate = getCurrentFilter(colVal,"Date");
					    	drillDownRepName = PurchaseDrillDownReportConstants.REPORT_M;
					    } else if (colHeader.equalsIgnoreCase("Non Contract")){
					    	startDate = getCurrentFilter(colVal,"Date");
					    	drillDownRepName = PurchaseDrillDownReportConstants.REPORT_CC;

					    } else if ((colHeader.equalsIgnoreCase("Net Prch")) || (colHeader.equalsIgnoreCase("Contract"))) {
					    	startDate = getCurrentFilter(colVal,"Date");
					    	drillDownRepName = PurchaseDrillDownReportConstants.REPORT_DD;
					    }

				String colValue = null;


				if (colVal != null) colValue = colVal;

				if ((colValue != null) && (colValue.equalsIgnoreCase("Total")))
						{
							currentFilter = strType;
							groupName="Total";
						}
						else
						{
							currentFilter = strType + " / " + getCurrentFilter(colValue,"CurrentFilter");
						}
						if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
							custSelection =  custInfo;
						} else {

							String accName = null;
							selGroup = formatSelectionForm.getPddGroup();
							String group = null;
							if(selGroup != null)
							{
								group = selGroup.substring(selGroup.indexOf(":")+1);
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
						  if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
						  {
							  String accNum = group.substring(group.indexOf("(")+1, group.indexOf(")"));
							  accName = getAccountNameAddress(accNum,"AccName");
							  custSelection = accName;
						  }
						  else
						  {

							  custSelection =  purchaseDrillDownForm.getSelectedGroup();
						  }
						}
					}
			}
			if ((colHeader != null) && (colHeader.equalsIgnoreCase("Ret%")))
				    {
				    	if ((formattingSelectionForm.getPrimaryCustomerNumber() != null) && !(formattingSelectionForm.getPrimaryCustomerNumber().trim().equals("")))
						{
							custSelection = custInfo;
						} else {
							selGroup = formattingSelectionForm.getPddGroup();
							if(selGroup != null)
							{
								selGroup = selGroup.substring(0,selGroup.indexOf(":"));

							}
							if(selGroup != null && selGroup.equalsIgnoreCase("Customer"))
							{
								String group = purchaseDrillDownForm.getSelectedGroup();
								String accNum = group.substring(group.indexOf("(")+1,group.indexOf(")"));
								custSelection = getAccountNameAddress(accNum,"AccName");
							}
							else
							{
								custSelection =  purchaseDrillDownForm.getSelectedGroup();
							}
						}

				    	currentFilter = "Returns for ";
				    	currentFilter = currentFilter + getCurrentFilter(colVal,"CurrentFilter");
				    	startDate = getCurrentFilter(colVal,"Date");
				    	drillDownRepName = PurchaseDrillDownReportConstants.REPORT_S;
				    }
				}


				purchaseDrillDownForm.setCurrentFilter(currentFilter);
				purchaseDrillDownForm.setCustSelection(custSelection);

				purchaseDrillDownForm.setDrillDownReport(drillDownRepName);
				purchaseDrillDownForm.setGroupName(groupName);
				purchaseDrillDownForm.setGroupID(groupID);
				purchaseDrillDownForm.setSubReportId(getSubReportID(drillDownRepName));
				purchaseDrillDownForm.setStartDate(startDate);

				return purchaseDrillDownForm;
	} // end of the method getAccountNameAddress




	/**
	 * This method is invoked to retrieve the date selected from the formatting tab.
	 *
	 * @param reportBaseVO
	 *
	 * @return dateRange
 */

	public String getDateRange(ReportBaseVO reportBaseVO)
	{
	try
	{
		DateSelectionUtil dateSelectionUtil = new DateSelectionUtil();
		DateConverision dateConversion = new DateConverision();
		String[] dateRange = new String[2];
		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = reportBaseVO.getCannedReportVO().getCannedReportCriteriaVO().getCriteriaVO().getDateSelectionAndComparisonVO();
		String strCondition =  reportBaseVO.getCannedReportVO().getCannedReportCriteriaVO().getCriteriaVO().getDateSelectionAndComparisonVO().getDateSelection();
		boolean includeCurrentMonth =  reportBaseVO.getCannedReportVO().getCannedReportCriteriaVO().getCriteriaVO().getDateSelectionAndComparisonVO().getIncludeCurrentMonth();
		int noOfMonths =  reportBaseVO.getCannedReportVO().getCannedReportCriteriaVO().getCriteriaVO().getDateSelectionAndComparisonVO().lastXMonths;
		{
			dateRange = dateSelectionUtil.getTimeSeriesDateRange(strCondition, includeCurrentMonth, noOfMonths, dateSelectionAndComparisonVO);
		}


		if (dateRange != null && dateRange.length == 2)
		{
			//return (dateRange[0] + " to " + dateRange[1]);
			return (dateConversion.formatDate(dateRange[0]) + " to " + dateConversion.formatDate(dateRange[1]));
		}

		else
		{
			return "";
		}
	}
	catch (Exception e)
	{
		return "";
	}
  } // end of the method getDateRange


	private  String getCurrentFilter(String ColValue,String type) throws SMORAException
	{
	String currentFilter="";
	String yr =  null;
	if ((type.equalsIgnoreCase("CurrentFilter")) || (type.equalsIgnoreCase("Date")))
	{
	currentFilter = ColValue.substring(0, ColValue.length()-4);
		yr = ColValue.substring(ColValue.length()-4,ColValue.length());
	} else {
		if(ColValue !=null)
		{
		currentFilter = ColValue.substring(ColValue.length()-2, ColValue.length());
		yr = ColValue.substring(0, ColValue.length()-2);
		}
	}
	String beginDate = null;
	if(currentFilter !=null){
		if ((currentFilter.equalsIgnoreCase("JAN")) || (currentFilter.equalsIgnoreCase("01"))) { currentFilter = "January";	beginDate = yr+"01"; }
		else if ((currentFilter.equalsIgnoreCase("FEB")) || (currentFilter.equalsIgnoreCase("02"))) { currentFilter = "February"; beginDate = yr+"02"; }
		else if ((currentFilter.equalsIgnoreCase("MAR"))  || (currentFilter.equalsIgnoreCase("03"))) { currentFilter = "March"; beginDate = yr+"03"; }
		else if ((currentFilter.equalsIgnoreCase("APR"))  || (currentFilter.equalsIgnoreCase("04"))) { currentFilter = "April"; beginDate = yr+"04"; }
		else if ((currentFilter.equalsIgnoreCase("MAY"))  || (currentFilter.equalsIgnoreCase("05"))) { currentFilter = "May"; beginDate = yr+"05"; }
		else if ((currentFilter.equalsIgnoreCase("JUN"))  || (currentFilter.equalsIgnoreCase("06"))) { currentFilter = "June"; beginDate = yr+"06"; }
		else if ((currentFilter.equalsIgnoreCase("JUL"))  || (currentFilter.equalsIgnoreCase("07"))) { currentFilter = "July"; beginDate = yr+"07"; }
		else if ((currentFilter.equalsIgnoreCase("AUG"))  || (currentFilter.equalsIgnoreCase("08"))) { currentFilter = "August"; beginDate = yr+"08"; }
		else if ((currentFilter.equalsIgnoreCase("SEP"))  || (currentFilter.equalsIgnoreCase("09"))) { currentFilter = "September"; beginDate = yr+"09"; }
		else if ((currentFilter.equalsIgnoreCase("OCT"))  || (currentFilter.equalsIgnoreCase("10"))) { currentFilter = "October"; beginDate = yr+"10"; }
		else if ((currentFilter.equalsIgnoreCase("NOV"))  || (currentFilter.equalsIgnoreCase("11"))) { currentFilter = "November"; beginDate = yr+"11"; }
		else if ((currentFilter.equalsIgnoreCase("DEC"))  || (currentFilter.equalsIgnoreCase("12"))) { currentFilter = "December"; beginDate = yr+"12"; }
	}
	if ((type.trim().equalsIgnoreCase("CurrentFilter")) || (type.trim().equalsIgnoreCase("MMTOMon")))
	{
		currentFilter = currentFilter+" "+yr;
		return currentFilter;
	} else {
		return beginDate;
	}
  } // end of the method getCurrentFilter


	/**
	 * This method counts the chars.
	 *
	 * @param custNum the the string
	 *
	 * @return the String
	 */
	
	public  String getAccountNameAddress(String custNum,String type) throws SMORAException
	{
		String address = null;
		String chainId="";
		boolean value=false;
		AccountGroupDB accountGroupDB = null;
		final String METHODNAME = "getAccountNameAddress";
		try{
		accountGroupDB = new AccountGroupDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,"",METHODNAME);
			}
		ArrayList acctDet =	(ArrayList) accountGroupDB.getAccountDetails(custNum,type);

		for(int i=0;i<acctDet.size();i++)
		{
			TCustAcct tCustAcct = (TCustAcct)acctDet.get(i);
			if(tCustAcct.getCustNum().equals(tCustAcct.getCustAcctId()))
			{
				if(type.equalsIgnoreCase("Address"))
				{
					address = tCustAcct.getCustAcctNam()+" ("+custNum+") "+tCustAcct.getAcctDlvryAddr()+" - "+tCustAcct.getAcctDlvryCtyNam()+
					" , "+tCustAcct.getAcctDlvryStAbrv();
				}
				//	Req. 6,33,45 changes done by Anil Kumar.
				else if(type.equalsIgnoreCase("Chain")){
					address=tCustAcct.getCustChnId();
				}
				else if(type.equalsIgnoreCase("DC")){
					address=tCustAcct.getCustChnId();
					//if("907".equalsIgnoreCase(address)){
						//value=true;
						chainId=address;
					//}
				}
				else if(type.equalsIgnoreCase("Natl Group")){
					address=tCustAcct.getCustChnId();
					//if("907".equalsIgnoreCase(address)){
					//	value=true;
						chainId=address;
					//}
				}
				else
				{
					address = tCustAcct.getCustAcctNam()+" ("+custNum+") ";
				}
			}
		}
		if(value==true){
			address=chainId;
		}
		return address;
	} // end of the method getCustomerChainId

//Req.6,33,45 method added by Anil Kumar for gettign the Customer ChainId
	
	public  String getCustomerChainId(String id,String type) throws SMORAException
	{
		String chainId = null;
		AccountGroupDB accountGroupDB = null;
		final String METHODNAME = "getCustomerChainId";
		try{
		accountGroupDB = new AccountGroupDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,"",METHODNAME);
			}
		ArrayList acctDet =	(ArrayList) accountGroupDB.getAccountDetails(id,type);

		for(int i=0;i<acctDet.size();i++)
		{
			TCustAcct tCustAcct = (TCustAcct)acctDet.get(i);
			if(tCustAcct.getCustNum().equals(tCustAcct.getCustAcctId()))
			{
				chainId = tCustAcct.getCustChnId();
			}
		}

		return chainId;
	} // end of the method getCustomerChainId

	
	
	/**
	 * This method returns sub report id.
	 *
	 * @param drillReportName the the string
	 *
	 * @return the String
	 */
	public  int getSubReportID(String drillReport) throws SMORAException
	{
		int subReportId = 0;
		  if (drillReport != null)
		  {
			  if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_A))
			  {
				  subReportId = ReportManagerConstants.PURCHASE_DRILL_DOWN_SUMMARY;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_B))
			  {
				  subReportId = ReportManagerConstants.REPORT_B;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_C))
			  {
				  subReportId = ReportManagerConstants.REPORT_C;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_D))
			  {
				  subReportId = ReportManagerConstants.REPORT_D;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_E))
			  {
				  subReportId = ReportManagerConstants.REPORT_E;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_F))
			  {
				  subReportId = ReportManagerConstants.REPORT_F;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_G))
			  {
				  subReportId = ReportManagerConstants.REPORT_G;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_H))
			  {
				  subReportId = ReportManagerConstants.REPORT_H;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_I))
			  {
				  subReportId = ReportManagerConstants.REPORT_I;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_J))
			  {
				  subReportId = ReportManagerConstants.REPORT_J;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_N))
			  {
				  subReportId = ReportManagerConstants.REPORT_N;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_O))
			  {
				  subReportId = ReportManagerConstants.REPORT_O;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_P))
			  {
				  subReportId = ReportManagerConstants.REPORT_P;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_Q))
			  {
				  subReportId = ReportManagerConstants.REPORT_Q;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_R))
			  {
				  subReportId = ReportManagerConstants.REPORT_R;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_S))
			  {
				  subReportId = ReportManagerConstants.REPORT_S;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_T))
			  {
				  subReportId = ReportManagerConstants.REPORT_T;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_U))
			  {
				  subReportId = ReportManagerConstants.REPORT_U;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_V))
			  {
				  subReportId = ReportManagerConstants.REPORT_V;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_W))
			  {
				  subReportId = ReportManagerConstants.REPORT_W;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_X))
			  {
				  subReportId = ReportManagerConstants.REPORT_X;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_Y))
			  {
				  subReportId = ReportManagerConstants.REPORT_Y;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_Z))
			  {
				  subReportId = ReportManagerConstants.REPORT_Z;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_AA))
			  {
				  subReportId = ReportManagerConstants.REPORT_AA;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_BB))
			  {
				  subReportId = ReportManagerConstants.REPORT_BB;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_EE))
			  {
				  subReportId = ReportManagerConstants.REPORT_EE;
			  } else if (drillReport.trim().equalsIgnoreCase(PurchaseDrillDownReportConstants.REPORT_FF))
			  {
				  subReportId = ReportManagerConstants.REPORT_FF;
			  }
		  }
		return subReportId;
	} // end of the method getAccountNameAddress
	
	/**
	 *
	 * @param custNum the the string
	 *
	 * @return the String
	 */
	public  TCustAcct getCustAccount(String custNum) throws SMORAException
	{
		String nationalGrpCd = null;
		AccountGroupDB accountGroupDB = null;
		final String METHODNAME = "getNationalGroupCd";
		try
		{
			accountGroupDB = new AccountGroupDB();
		}
		catch(Exception e)
		{
			throw new SMORAException(e,"",METHODNAME);
		}
		ArrayList acctDet =	(ArrayList) accountGroupDB.getAccountDetails(custNum,"Account");

		for(int i=0;i<acctDet.size();i++)
		{
			TCustAcct tCustAcct = (TCustAcct)acctDet.get(i);
			if(tCustAcct.getCustNum().equals(tCustAcct.getCustAcctId()))
			{
				return tCustAcct;
			}
		}
		return null;
	} // end of the method getCustAccount
}

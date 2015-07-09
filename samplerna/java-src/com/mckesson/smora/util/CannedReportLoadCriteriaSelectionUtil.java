/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountGroupDB;
import com.mckesson.smora.database.dao.AdvancedFilterDB;
import com.mckesson.smora.database.dao.CriteriaTemplateDB;
import com.mckesson.smora.database.dao.CustomReportFieldsDB;
import com.mckesson.smora.database.dao.ItemDB;
import com.mckesson.smora.database.dao.ItemGroupDB;
import com.mckesson.smora.database.model.TApplAcctGrp;
import com.mckesson.smora.database.model.TApplItmGrp;
import com.mckesson.smora.dto.AdvancedFiltersVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.DrugScheduleVO;
import com.mckesson.smora.dto.FrontEdgeSubCategoryVO;
import com.mckesson.smora.dto.FrontEdgeVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.MicaVO;
import com.mckesson.smora.dto.MultiSourceVO;
import com.mckesson.smora.dto.SupplierListVO;
import com.mckesson.smora.dto.TherapeuticListVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.AdvancedFiltersForm;
import com.mckesson.smora.ui.form.CustomerSelectionForm;
import com.mckesson.smora.ui.form.ItemSelectionForm;
import com.mckesson.smora.ui.form.SupplierSelectionForm;

import com.mckesson.smora.util.AccountUtil;
import com.mckesson.smora.util.CustomerUtil;
import com.mckesson.smora.util.UserRolesUtil;

import com.mckesson.smora.exception.SMORAException;

/**
 * @author venkatesh.v.perumal
 * @changes 17/11/2006
 *
 * This action class prepares and populates the Criteria Selection page with
 * relevant information which are common across all the Canned Reports
 * such as a list of groups and templates, account access,
 * and a field list.
 */

/**
 * The Class CannedReportLoadCriteriaSelectionAction.
 */
public class CannedReportLoadCriteriaSelectionUtil
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "CannedReportLoadCriteriaSelectionUtil";
	/**
	 * The account group DB.
	 */
	private AccountGroupDB accountGroupDB = null;

	/**
	 * The account util.
	 */
	private AccountUtil accountUtil = null;

	/**
	 * The advanced filter DB.
	 */
	private AdvancedFilterDB advancedFilterDB = null;

	/**
	 * The advanced filters form.
	 */
	private AdvancedFiltersForm advancedFiltersForm = null;

	/**
	 * The customer accounts.
	 */
	private HashMap customerAccounts = null;

	/**
	 * The customer selection form.
	 */
	private CustomerSelectionForm customerSelectionForm = null;

	/**
	 * The item DB.
	 */
	private ItemDB itemDB = null;

	/**
	 * The item group DB.
	 */
	private ItemGroupDB itemGroupDB = null;

	/**
	 * The item groups list.
	 */
	private ArrayList itemGroupsList = null;

	/**
	 * The item selection form.
	 */
	private ItemSelectionForm itemSelectionForm = null;

	/**
	 * The supplier selection form.
	 */
	private SupplierSelectionForm supplierSelectionForm = null;

	/**
	 * The template DB.
	 */
	private CriteriaTemplateDB templateDB = null;



	/**
	 * This method generates the roles keys.
	 *
	 * @param userRolesVO the user roles VO
	 *
	 * @return the user roles VO
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public UserRolesVO generateRolesKeys(UserRolesVO userRolesVO) throws SMORAException
	{
		final String METHODNAME = "generateRolesKeys";
		UserRolesUtil userRolesUtil = new UserRolesUtil();
		try{

			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return userRolesUtil.createKeysForTree(userRolesVO);
	}

	/**
	 * This method gets the account access.
	 *
	 * @param userID the user ID
	 * @param rowLimit the row limit
	 *
	 * @return the account access
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public HashMap getAccountAccess(String userID, int rowLimit) throws SMORAException	{
		final String METHODNAME = "getAccountAccess";
		try{
		accountUtil = new AccountUtil();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		//TODO need to pass the userrolevo right now passing as null
		return accountUtil.getAccountAccessForUser(userID, rowLimit, null);
	}

	/**
	 * This method gets the account access.
	 *
	 * @param userID the user ID
	 * @param rowLimit the row limit
	 *
	 * @return the account access
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public HashMap getAccountAccess(String userID, int rowLimit,UserRolesVO userRolesVO) throws SMORAException	{
		final String METHODNAME = "getAccountAccess";
		try{
		accountUtil = new AccountUtil();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return accountUtil.getAccountAccessForUser(userID, rowLimit,userRolesVO);
	}
	/**
	 * This method gets the account groups.
	 *
	 * @param userID the user ID
	 *
	 * @return the account groups
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public ArrayList getAccountGroups(String userID) throws SMORAException	{
		final String METHODNAME = "getAccountGroups";
		try{
		accountGroupDB = new AccountGroupDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return (ArrayList) accountGroupDB.getAccountGroups(userID);

	}

	/**
	 * This method gets the advanced filters field.
	 *
	 * @param userID the user ID
	 *
	 * @return the advanced filters field
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public AdvancedFiltersForm getAdvancedFiltersField(String userID) throws SMORAException	{
		final String METHODNAME = "getAdvancedFiltersField";
		try{
		advancedFiltersForm = new AdvancedFiltersForm();
		advancedFilterDB = new AdvancedFilterDB();
		AdvancedFiltersVO advancedFiltersVO = advancedFilterDB.getAdvancedFiltersVO(userID);

		// mica list
		ArrayList micaList = advancedFiltersVO.getMicaList().getMica();
		ArrayList micaDscrList = new ArrayList();
		ArrayList micaDeptId = new ArrayList();
		for (int i = 0; i < micaList.size(); i++)
		{
			micaDscrList.add(((MicaVO) micaList.get(i)).getMicaDeptId() +" - "+((MicaVO) micaList.get(i)).getMicaDeptDscr());
		}
		for (int i = 0; i < micaList.size(); i++)
		{
			micaDeptId.add(((MicaVO) micaList.get(i)).getMicaDeptId());
		}
		advancedFiltersVO.getMicaList().setMica(micaDscrList);
		advancedFiltersForm.setMicaIDList(micaDeptId);


		// drug shchedule
		ArrayList drugSchedule = advancedFiltersVO.getDrugScheduleList().getDrugScheduleList();
		ArrayList drugScheduleList = new ArrayList();
		ArrayList drugScheduleIdList = new ArrayList();
		for (int i = 0; i < drugSchedule.size(); i++)
		{
			drugScheduleList.add(((DrugScheduleVO) drugSchedule.get(i)).getMckDrugSchdCd()  +" - "+((DrugScheduleVO) drugSchedule.get(i)).getDrugSchdDscr());
		}
		for (int i = 0; i < drugSchedule.size(); i++)
		{
			drugScheduleIdList.add(((DrugScheduleVO) drugSchedule.get(i)).getNonmckDrugSchdCd());
		}
		advancedFiltersVO.getDrugScheduleList().setDrugScheduleList(drugScheduleList);
		advancedFiltersForm.setDrugScheduleIdList(drugScheduleIdList);


		// frontEdge
		ArrayList frontEdge = advancedFiltersVO.getFrontEdgeList().getFrontEdge();
		ArrayList frontEdgeList = new ArrayList();
		ArrayList frontEdgeIdList = new ArrayList();
		for (int i = 0; i < frontEdge.size(); i++)
		{
			frontEdgeList.add(((FrontEdgeVO) frontEdge.get(i)).getEpDeptCtgyDscr());
		}
		for (int i = 0; i < frontEdge.size(); i++)
		{
			frontEdgeIdList.add(((FrontEdgeVO) frontEdge.get(i)).getEpDeptCtgyId());
		}
		// advancedFiltersVO.getFrontEdgeList().s.setDrugScheduleList(frontEdgeList);
		advancedFiltersForm.setFrontEdgeList(frontEdgeList);
		advancedFiltersForm.setFrontEdgeIdList(frontEdgeIdList);

		// frontEdgeSubCategory
		ArrayList frontEdgeSubCategory = advancedFiltersVO.getFrontEdgeSubCategoryList().getFrontEdgeSubCategory();
		ArrayList frontEdgeSubCategoryList = new ArrayList();
		ArrayList frontEdgeSubCategoryIdList = new ArrayList();
		for (int i = 0; i < frontEdgeSubCategory.size(); i++)
		{
			frontEdgeSubCategoryList.add(((FrontEdgeSubCategoryVO) frontEdgeSubCategory.get(i)).getEpDeptSubCtgyDscr());
		}
		for (int i = 0; i < frontEdgeSubCategory.size(); i++)
		{
			frontEdgeSubCategoryIdList.add(((FrontEdgeSubCategoryVO) frontEdgeSubCategory.get(i)).getEpDeptCtgyId());
		}
		// advancedFiltersVO.getFrontEdgeList().s.setDrugScheduleList(frontEdgeList);
		advancedFiltersForm.setFrontEdgeSubCategoryList(frontEdgeSubCategoryList);
		advancedFiltersForm.setFrontEdgeSubCategoryIdList(frontEdgeSubCategoryIdList);

		// Hard coded data
		ArrayList<String> advancedFilterDataList = new ArrayList();
		advancedFilterDataList.add("No Filter Applied");
		advancedFilterDataList.add("Include Only");
		advancedFilterDataList.add("Exclude All");
		advancedFilterDataList.add("Exclude Only");

		ArrayList<String> dcFilter = new ArrayList();
		dcFilter.add("No Filter Applied");
		dcFilter.add("Non-Primary DC purchases only");
		dcFilter.add("RED orders only");

		ArrayList<String> idbDates = new ArrayList();
		idbDates.add("Created Date");
		idbDates.add("Issued Date");
		idbDates.add("Posted Date");

		//Added for SO-3806 SynerGx Rebranding- MultiSource values retrived from DB and removed hardcoding
		ArrayList multiSource = advancedFiltersVO.getMultiSourceList().getMultiSource();
		if(multiSource == null)
			multiSource = new ArrayList();

		ArrayList multiSourceList = new ArrayList();
		ArrayList multiSourceIdList = new ArrayList();
		for (int i = 0; i < multiSource.size(); i++)
		{
			multiSourceList.add(((MultiSourceVO) multiSource.get(i)).getMultiSrcDescription());
			multiSourceIdList.add(((MultiSourceVO) multiSource.get(i)).getMultiSrcId());
		}
		advancedFiltersForm.setMultiSourceList(multiSourceList);
		advancedFiltersForm.setMultiSrcIdList(multiSourceIdList);
		
		//PDR report changes
		ArrayList<String> prefItemIndicator = new ArrayList();
		prefItemIndicator.add("");
		prefItemIndicator.add("Preferred items");
		prefItemIndicator.add("Non Preferred items");
		
		// setting hardcoded data to fields

		advancedFiltersForm.setContractFilterList(advancedFilterDataList);
		//advancedFiltersForm.setMultiSourceList(multiSource);
		advancedFiltersForm.setHhcList(advancedFilterDataList);
		advancedFiltersForm.setPrivateLabelList(advancedFilterDataList);
		advancedFiltersForm.setDropShipList(advancedFilterDataList);
		advancedFiltersForm.setDcFilterList(dcFilter);
		advancedFiltersForm.setIdbDatesList(idbDates);
		advancedFiltersForm.setPrefItemIndicatorList(prefItemIndicator);
		

		advancedFiltersForm.setAdvancedFiltersVO(advancedFiltersVO);
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return advancedFiltersForm;
	}

	/**
	 * This method gets the criteria templates.
	 *
	 * @param userID the user ID
	 *
	 * @return the criteria templates
	 *
	 * @throws SMORAException the SMORA exception
	 */

	public List getCriteriaTemplates(String userID) throws SMORAException	{
		final String METHODNAME = "getCriteriaTemplates";
		try{
		templateDB = new CriteriaTemplateDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
			return templateDB.getCannedTemplatesForUser(userID);
	}

	/**
	 * This method gets the customer selection field.
	 *
	 * @param userID the user ID
	 * @param selectedAccounts the selected accounts
	 * @param limitRecords the limit records
	 *
	 * @return the customer selection field
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public CustomerSelectionForm getCustomerSelectionField(String userID, int limitRecords, String selectedAccounts) throws SMORAException	{
		final String METHODNAME = "getCustomerSelectionField";
		try{
		customerSelectionForm = new CustomerSelectionForm();
		ArrayList accountDataList = getAccountGroups(userID);
		ArrayList accountGroupName = new ArrayList();
		ArrayList accountGroupId = new ArrayList();
		for (int i = 0; i < accountDataList.size(); i++)
		{
			accountGroupName.add(((TApplAcctGrp) accountDataList.get(i)).getGroupName());
			accountGroupId.add(((TApplAcctGrp) accountDataList.get(i)).getGroupId());
		}
		customerSelectionForm.setAccountGroupsList(accountGroupName);
		customerSelectionForm.setAccountGroupId(accountGroupId);
		customerSelectionForm.setAccountGroupName(accountGroupName);
		customerAccounts = getAccountAccess(userID, limitRecords);
		if ((customerAccounts != null) && (customerAccounts.get("ACCOUNTS") != null) && ((ArrayList) customerAccounts.get("ACCOUNTS")).size() != 0)
		{
			customerSelectionForm.setPage("LOW");
			List accountList = (List) customerAccounts.get("ACCOUNTS");
			Iterator accountListIterator = accountList.iterator();
			ArrayList accounts = new ArrayList();
			while (accountListIterator.hasNext())
			{
				accounts.add(accountListIterator.next());
			}
			customerSelectionForm.setAccountDetailsList(accounts);
			if (selectedAccounts != null)
			{
				ArrayList selectedAccountsId = new ArrayList();
				selectedAccountsId.add("007237");
				customerSelectionForm.setSelectedAccountsId(selectedAccountsId);
			}
		}
		else if ((customerAccounts != null) && (customerAccounts.get("ROLES") != null))
		{
			customerSelectionForm.setPage("HIGH");
			CustomerVO customerVO = new CustomerVO();
			customerVO.setRoles((UserRolesVO) customerAccounts.get("ROLES"));
			customerSelectionForm.setCustomerVO(customerVO);
			customerSelectionForm.setSearchBy(new CustomerUtil().getRoles(customerVO.getRoles()));
		}
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return customerSelectionForm;
	}

	/**
	 * This method gets the customer selection field.
	 *
	 * @param userID the user ID
	 * @param selectedAccounts the selected accounts
	 * @param limitRecords the limit records
	 *
	 * @return the customer selection field
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public CustomerSelectionForm getCustomerSelectionField(String userID, int limitRecords, String selectedAccounts, UserRolesVO userRolesVO) throws SMORAException	{
		final String METHODNAME = "getCustomerSelectionField";
		CustomReportFieldsDB customReportFieldsDB = new CustomReportFieldsDB();
		try{
		customerSelectionForm = new CustomerSelectionForm();
		ArrayList accountDataList = getAccountGroups(userID);
		ArrayList accountGroupName = new ArrayList();
		ArrayList accountGroupId = new ArrayList();
		ArrayList searchByList = new ArrayList();
		customReportFieldsDB = new CustomReportFieldsDB();
		Boolean isIDBUser = customReportFieldsDB.isIDBUser(userID);
		customerSelectionForm.setIsIDB(isIDBUser);

		for (int i = 0; i < accountDataList.size(); i++)
		{
			accountGroupName.add(((TApplAcctGrp) accountDataList.get(i)).getGroupName());
			accountGroupId.add(((TApplAcctGrp) accountDataList.get(i)).getGroupId());
		}
		customerSelectionForm.setAccountGroupsList(accountGroupName);
		customerSelectionForm.setAccountGroupId(accountGroupId);
		customerSelectionForm.setAccountGroupName(accountGroupName);
		customerAccounts = getAccountAccess(userID, limitRecords,userRolesVO);
		ArrayList hsp = new ArrayList();
		ArrayList chn = new ArrayList();
		ArrayList cid = new ArrayList();
		ArrayList sls = new ArrayList();
		if((customerAccounts != null) && customerAccounts.get("ROLES") != null){
		UserRolesVO userRoles = (UserRolesVO) customerAccounts.get("ROLES");
		hsp = userRoles.getHspRoles();
		chn = userRoles.getChnRoles();
		cid = userRoles.getCidRoles();
		sls = userRoles.getSlsRoles();
		if(hsp != null && hsp.size()>0)
		{
			searchByList.add("National Group");
			searchByList.add("Sub Group");
		}
		if(chn != null && chn.size()>0)
		{
			searchByList.add("Chain");
		}
		if((chn != null && chn.size()>0) || (hsp != null && hsp.size()>0))
		{
			searchByList.add("Region ID");
			searchByList.add("District");

		}
		if(sls != null && sls.size()>0)
		{
			searchByList.add("DC");
			searchByList.add("Territory");
		}
		searchByList.add("Account Number");
		searchByList.add("Account Name");
		customerSelectionForm.setSearchByList(searchByList);
		}

		if ((customerAccounts != null) && (customerAccounts.get("ROLES") != null)
			&& ((hsp!=null&&hsp.size()!=0)||(chn!=null&&chn.size()!=0)|| (cid!=null&&cid.size()!=0)|| (sls!=null&&sls.size()!=0))
			&&((customerAccounts.get("ACCOUNTS") != null)&&(((ArrayList)customerAccounts.get("ACCOUNTS")).size()==0)))
		{
			customerSelectionForm.setPage("HIGH");
			accountUtil = new AccountUtil();
			customerSelectionForm.setTopLevelNodesCount(accountUtil.getTopLevelCount(userRolesVO));
			//customerSelectionForm.setSearchByList((ArrayList)(accountUtil.getColumnNameForAccountSearch(userID)));
			CustomerVO customerVO = new CustomerVO();
			customerVO.setRoles((UserRolesVO) customerAccounts.get("ROLES"));
			customerSelectionForm.setCustomerVO(customerVO);
			customerSelectionForm.setSearchBy(new CustomerUtil().getRoles(customerVO.getRoles()));
		}
		else if ((customerAccounts != null) && (customerAccounts.get("ACCOUNTS") != null))
		{
			customerSelectionForm.setPage("LOW");
			List accountList = (List) customerAccounts.get("ACCOUNTS");
			Iterator accountListIterator = accountList.iterator();
			ArrayList accounts = new ArrayList();
			while (accountListIterator.hasNext())
			{
				accounts.add(accountListIterator.next());
			}
			customerSelectionForm.setAccountDetailsList(accounts);
			if (selectedAccounts != null)
			{
				ArrayList selectedAccountsId = new ArrayList();
				selectedAccountsId.add("007237");
				customerSelectionForm.setSelectedAccountsId(selectedAccountsId);
			}
		}
		else{
			customerSelectionForm.setPage("LOW");
		}

			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return customerSelectionForm;
	}


	/**
	 * This method gets the item groups.
	 *
	 * @param userID the user ID
	 *
	 * @return the item groups
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public ArrayList getItemGroups(String userID)	throws SMORAException {
		final String METHODNAME = "getItemGroups";
		try{
		itemGroupDB = new ItemGroupDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return itemGroupDB.getItemGroups(userID);
	}

	/**
	 * This method gets the item selection field.
	 *
	 * @param userID the user ID
	 *
	 * @return the item selection field
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public ItemSelectionForm getItemSelectionField(String userID) throws SMORAException	{
		final String METHODNAME = "getItemSelectionField";
		try{
		itemSelectionForm = new ItemSelectionForm();
		ItemVO itemVO = new ItemVO();
		ItemNumbersListVO itemNumbersListVO = new ItemNumbersListVO();
		boolean mckItems = true;
		boolean purchasedItemOnly = true;
		boolean nonMckItems = false;
		ArrayList<String> itemGroupName = new ArrayList();
		ArrayList itemGroupId = new ArrayList();
		itemGroupsList = this.getItemGroups(userID);
		for (int i = 0; i < itemGroupsList.size(); i++)
		{
			itemGroupName.add(((TApplItmGrp) itemGroupsList.get(i)).getGroupName());
			itemGroupId.add(((TApplItmGrp) itemGroupsList.get(i)).getGroupId());
		}

		itemNumbersListVO.setMckItems(mckItems);
		itemNumbersListVO.setPurchasedItemOnly(purchasedItemOnly);
		itemNumbersListVO.setNonMckItems(nonMckItems);
		itemVO.setItemNumbersList(itemNumbersListVO);
		itemVO.setItemGroupName(itemGroupName);
		itemVO.setItemGroupId(itemGroupId);

		// Therapeutic
		ArrayList theraList = getTherapeuticDetails();

		TherapeuticListVO therapeuticListVO = new TherapeuticListVO();
		therapeuticListVO.setTherapeuticList(theraList);
		itemVO.setTherapeuticList(therapeuticListVO);

		itemSelectionForm.setItemVO(itemVO);
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return itemSelectionForm;
	}

	/**
	 * This method gets the supplier selection field.
	 *
	 * @return the supplier selection field
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public SupplierSelectionForm getSupplierSelectionField()	throws SMORAException {
		final String METHODNAME = "getSupplierSelectionField";
		try{
		supplierSelectionForm = new SupplierSelectionForm();
		ArrayList supplierList = new ArrayList();
		SupplierListVO supplierListVO = new SupplierListVO();
		supplierListVO.setSuppliers(supplierList);
		supplierSelectionForm.setSupplierListVO(supplierListVO);
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return supplierSelectionForm;
	}

	/**
	 * This method gets the therapeutic details.
	 *
	 * @return the therapeutic details
	 *
	 * @throws SMORAException the SMORA exception
	 */
	public ArrayList getTherapeuticDetails()	throws SMORAException {
		final String METHODNAME = "getTherapeuticDetails";
		try{
		itemDB = new ItemDB();
			}
			catch(Exception e)
			{
				throw new SMORAException(e,CLASSNAME,METHODNAME);
			}
		return itemDB.getTherapeuticHeirarchy();
	}

}

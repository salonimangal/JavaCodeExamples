/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.dto.*;
import com.mckesson.smora.database.model.TApplAcctGrp;
import com.mckesson.smora.database.model.TApplAcctGrpDtl;
import com.mckesson.smora.database.dao.AccountGroupDB;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.AccountAccessDB;
import com.mckesson.smora.database.dao.LoginDB;
import com.mckesson.smora.exception.SMORADatabaseException;
import com.mckesson.smora.exception.SMORAException;

/*
 * This Class compares all the Accounts available with the accounts available in
 * the T_APPL_IW_USER_ACCESS table and deletes the accounts from the
 * T_APPL_ACCT_GRP_DTL table which are not available in the
 * T_APPL_IW_USER_ACCESS table Author Date Venkatesh.V.Perumal 08/15/2006
 */

/**
 * The Class DeleteAccountsBatch.
 */
public class DeleteAccountsBatch
{

	private static Log log = LogFactory.getLog(DateUtil.class);

	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "DeleteAccountsBatch";

	/**
	 * The global array list.
	 */
	ArrayList globalArrayList;

	/**
	 * The key ID map.
	 */
	HashMap keyIDMap = null;

	/**
	 * The accnt grp roles CID list.
	 */
	ArrayList accntGrpRolesCIDList = null;

	/**
	 * The user access roles VO.
	 */
	UserRolesVO userAccessRolesVO = null;

	/**
	 * The remove accnt list.
	 */
	ArrayList removeAccntList = null;

	/**
	 * The accessible account list.
	 */
	ArrayList accessibleAccountList = null;

	/**
	 * The account DB.
	 */
	AccountDB accountDB = new AccountDB();

	/**
	 * The account group DB.
	 */
	AccountGroupDB accountGroupDB = new AccountGroupDB();

	/**
	 * The delete map.
	 */
	HashMap deleteMap = new HashMap();

	/*
	 * This method retrives all the available Accounts from different Groups and
	 * at the differnt levels for a particular User. @param UserID @return
	 * accntList
	 */

	/**
	 * This method gets the account details.
	 * 
	 * @param userID
	 *            the user ID
	 * @return the account details
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public ArrayList getAccountDetails(String userID) throws SMORAException
	{
		String accntString;
		ArrayList accntGrpList;
		ArrayList accntGrpRolesHSPList;
		ArrayList accntGrpRolesCHNList;

		ArrayList accntGrpRolesSLSList;
		ArrayList accntList = new ArrayList();
		ArrayList accntgroupIds = new ArrayList();

		keyIDMap = new HashMap();
		TApplAcctGrpDtl tapplAcctGrpDtl;
		UserRolesVO userRolesVO;
		RoleVO roleVO;

		try
		{
			accntGrpList = accountGroupDB.getAccountGroups(userID);
			log.info("Delete accounts -- accntGrpList "+accntGrpList);

			for (int i = 0; i < accntGrpList.size(); i++)
			{
				TApplAcctGrp acctGrp = (TApplAcctGrp) accntGrpList.get(i);
				accntgroupIds.add(acctGrp.getGroupId());
				
				log.info("Delete account -- acctGrp.getGroupId() "+acctGrp.getGroupId());
				
				
				ArrayList list = accountGroupDB.getAccountGroupsDetails(acctGrp.getGroupId()+"");
				log.info("List "+list);
				ArrayList keyList = new ArrayList();
				TApplAcctGrpDtl acctGrpDtl = null;
				for (int j = 0; j < list.size(); j++)
				{
					acctGrpDtl = (TApplAcctGrpDtl) list.get(j);
					String key = acctGrpDtl.getRoleCode();
					log.info("Delete accounts -- key "+key);
					if (acctGrpDtl.getLevel1() != null)
					{
						key = key + "," + acctGrpDtl.getLevel1();
					}
					if (acctGrpDtl.getLevel2() != null)
					{
						key = key + "," + acctGrpDtl.getLevel2();
					}
					if (acctGrpDtl.getLevel3() != null)
					{
						key = key + "," + acctGrpDtl.getLevel3();
					}
					if (acctGrpDtl.getLevel4() != null)
					{
						key = key + "," + acctGrpDtl.getLevel4();
					}
					keyList.add(key);
				}
				keyIDMap.put(acctGrp.getGroupId(), keyList);
			}

			if (accntgroupIds.size() > 0)
			{
				userRolesVO = accountGroupDB.getAllAccountsAndRolesForGroups(accntgroupIds, true);
				accntGrpRolesHSPList = userRolesVO.getHspRoles();
				accntGrpRolesCHNList = userRolesVO.getChnRoles();
				accntGrpRolesCIDList = userRolesVO.getCidRoles();
				accntGrpRolesSLSList = userRolesVO.getSlsRoles();

				Iterator accntGrpHSPIterator = accntGrpRolesHSPList.iterator();
				Iterator accntGrpCHNIterator = accntGrpRolesCHNList.iterator();
				Iterator accntGrpCIDIterator = accntGrpRolesCIDList.iterator();
				Iterator accntGrpSLSIterator = accntGrpRolesSLSList.iterator();

				while (accntGrpHSPIterator.hasNext())
				{
					roleVO = (RoleVO) accntGrpHSPIterator.next();
					accntList.add(roleVO.getKey());
				}
				while (accntGrpCHNIterator.hasNext())
				{
					roleVO = (RoleVO) accntGrpCHNIterator.next();
					accntList.add(roleVO.getKey());
				}
				while (accntGrpCIDIterator.hasNext())
				{
					roleVO = (RoleVO) accntGrpCIDIterator.next();
					accntList.add(roleVO.getKey());
				}
				while (accntGrpSLSIterator.hasNext())
				{
					roleVO = (RoleVO) accntGrpSLSIterator.next();
					accntList.add(roleVO.getKey());
				}
			}
		}
		catch (SMORADatabaseException de)
		{	 
				de.printStackTrace();
				 throw new SMORAException(de.getMessage(), de);
		}
		return accntList;
	}

	/*
	 * This method retrives all the Accounts available in the
	 * T_APPL_IW_USER_ACCESS for a particular user. @param UserID @return
	 * accessList
	 */

	/**
	 * This method gets the user access.
	 * 
	 * @param userID
	 *            the user ID
	 * @return the user access
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public ArrayList getUserAccess(String userID) throws SMORAException
	{
		ArrayList accessList = new ArrayList();
		ArrayList accntGrpAccessHSPList;
		ArrayList accntGrpAccessCHNList;
		ArrayList accntGrpAccessCIDList;
		ArrayList accntGrpAccessSLSList;

		AccountAccessDB accountAccessDB = new AccountAccessDB();
		RoleVO roleVO;

		userAccessRolesVO = accountAccessDB.getAccountAccessInfo(userID);

		accntGrpAccessHSPList = userAccessRolesVO.getHspRoles();
		accntGrpAccessCHNList = userAccessRolesVO.getChnRoles();
		accntGrpAccessCIDList = userAccessRolesVO.getCidRoles();
		accntGrpAccessSLSList = userAccessRolesVO.getSlsRoles();

		Iterator accntGrpAccessHSPIterator = accntGrpAccessHSPList.iterator();
		Iterator accntGrpAccessCHNIterator = accntGrpAccessCHNList.iterator();
		Iterator accntGrpAccessCIDIterator = accntGrpAccessCIDList.iterator();
		Iterator accntGrpAccessSLSIterator = accntGrpAccessSLSList.iterator();

		while (accntGrpAccessHSPIterator.hasNext())
		{
			roleVO = (RoleVO) accntGrpAccessHSPIterator.next();
			accessList.add(roleVO.getKey());
		}
		while (accntGrpAccessCHNIterator.hasNext())
		{
			roleVO = (RoleVO) accntGrpAccessCHNIterator.next();
			accessList.add(roleVO.getKey());
		}
		while (accntGrpAccessCIDIterator.hasNext())
		{
			roleVO = (RoleVO) accntGrpAccessCIDIterator.next();
			accessList.add(roleVO.getKey());
		}
		while (accntGrpAccessSLSIterator.hasNext())
		{
			roleVO = (RoleVO) accntGrpAccessSLSIterator.next();
			accessList.add(roleVO.getKey());
		}
		return accessList;
	}

	/*
	 * This method Compares All the Accounts with the Accounts available in the
	 * T_APPL_IW_USER_ACCESS table and returns the Accounts to be removed from
	 * T_APPL_ACCT_GRP_DTL and T_APPL_ACCT_GRP table @param acctList,accessList
	 * @return removeAccntList
	 */

	/**
	 * This method compares the list.
	 * 
	 * @param acctList
	 *            the acct list
	 * @param accessList
	 *            the access list
	 */
	public void compareList(ArrayList acctList, ArrayList accessList)
	{

		removeAccntList = new ArrayList();
		for (int i = 0; i < acctList.size(); i++)
		{

			if (accessList.contains(acctList.get(i)))
			{
				log.info("Account available.." + acctList.get(i));
			}
			else
			{
				log.info("Added to the remove List.." + acctList.get(i));
				log.info("Added to the remove List.." + acctList.get(i));
				removeAccntList.add(acctList.get(i));
			}
		}
	}

	/**
	 * This method compares the at accounts level.
	 * 
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public void compareAtAccountsLevel() throws SMORAException
	{
		if (accntGrpRolesCIDList != null)
		{

			AccountDetailsVO accDtlsVO = null;

			UserRolesVO CIDRolesVO = new UserRolesVO();
			CIDRolesVO.setCidRoles(accntGrpRolesCIDList);
			ArrayList CIDAccountsList = accountDB.getAccountInfo(CIDRolesVO);

			for (Iterator itr = CIDAccountsList.iterator(); itr.hasNext();)
			{
				accDtlsVO = (AccountDetailsVO) itr.next();
				if (!accessibleAccountList.contains(accDtlsVO.getAccountNum()))
				{
					removeAccntList.add(accDtlsVO.getKey());
				}
			}
		}
	}

	/**
	 * This method deletes the accounts.
	 * 
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public void deleteAccounts() throws SMORAException
	{
		ArrayList accountList = new ArrayList();
		ArrayList accessList = new ArrayList();
		ArrayList deleteList = new ArrayList();

		// get all the users from the Accounts tables
		// for each user get the account groups available
		// check the unavailable accounts and then put it in the delete list
		// delete all the unavailable accounts for the group id and user id
		// combination
		String userID = null;
		LoginDB loginDB = new LoginDB();
		ArrayList userList = (ArrayList) loginDB.getAllUsers();
		log.info("userList "+userList);
		for (Iterator itr = userList.iterator(); itr.hasNext();)
		{
			
			userID =  itr.next().toString();
			userID =  userID.substring(0,  userID.indexOf(","));
			log.info("userID "+userID);
			accountList = getAccountDetails(userID);
			accessList = getUserAccess(userID);
			log.info("accessList "+accessList);
			log.info("accountList "+accountList);
			// need to put a DB method in accountDB to get only account numbers
			// in the form of list.
			accessibleAccountList = (ArrayList) accountDB.getAccountNumbers(userAccessRolesVO);

			compareList(accountList, accessList);
			compareAtAccountsLevel();

			deleteAccountsDetails();
			// accountGroupDB.deleteAccountGroupDetails(groupId,
			// removeAccntList);
		}
	}

	/**
	 * This method deletes the accounts details.
	 * 
	 * @throws SMORAException
	 *             the SMORA exception
	 */
	public void deleteAccountsDetails() throws SMORAException
	{
		Object key = null;
		String account = null;
		ArrayList accountsList = null;
		ArrayList deleteAccountsList = null;
		for (Iterator itr = keyIDMap.keySet().iterator(); itr.hasNext();)
		{
			deleteAccountsList = new ArrayList();
			key = itr.next();
			accountsList = (ArrayList) keyIDMap.get(key);
			for (Iterator removeAccItr = removeAccntList.iterator(); removeAccItr.hasNext();)
			{

				account = (String) removeAccItr.next();
				if (accountsList.contains(account))
					deleteAccountsList.add(account);
			}
			deleteMap.put(key, deleteAccountsList);
		}
		accountGroupDB.deleteAccountGroupDetails(deleteMap);
	}

	public static void main(String[] args)
	{
		log.info("Delete accounts -- Start");
		try
		{
			DeleteAccountsBatch batch = new DeleteAccountsBatch();
			batch.deleteAccounts();
			log.info("Delete accounts -- End");
		}
		catch (Exception e)
		{
			log.debug("Exception:" + e);
			log.error("Failed to Execute the Delete Accounts");
			System.exit(1);
		}
	}
}


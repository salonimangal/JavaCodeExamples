/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;



import com.mckesson.smora.appl.util.ReportCriteriaAnalyzer;
import com.mckesson.smora.appl.util.ReportCriteriaSelection;//added for Issue# 437 by Infosys(Amit)-tjvobaf
import com.mckesson.smora.database.dao.AccountAccessDB;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.model.TChnNam;
import com.mckesson.smora.database.model.TChnRgn;
import com.mckesson.smora.database.model.TChnRgnDstrct;
import com.mckesson.smora.database.model.TCustDstrct;
import com.mckesson.smora.database.model.TCustRgn;
import com.mckesson.smora.database.model.TDcTerr;
import com.mckesson.smora.database.model.TNatlGrp;
import com.mckesson.smora.database.model.TNatlSubGrp;
import com.mckesson.smora.database.util.DBUtil;
import com.mckesson.smora.database.util.HibernateTemplate;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.AccountVO;
import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.UserAccountVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORADatabaseException;
import com.mckesson.smora.exception.SMORAException;



/**
 * The Class AccountUtil.
 */
public class AccountUtil
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "AccountUtil";
	/**
	 * The account access DB.
	 */
	private AccountAccessDB accountAccessDB = null;
	/**
	 * The account DB.
	 */
	private AccountDB accountDB = null;
	/**
	 * The ReportCriteriaSelection.
	 */
	private ReportCriteriaSelection criteriaSelection = null;//added for Issue# 437 by Infosys(Amit)-tjvobaf
	/**
	 * The HibernateTemplate
	 */
	private HibernateTemplate template = null;
	
	/**
	 * The user roles VO.
	 */
	private UserRolesVO userRolesVO = null;
	
	/**
	 * The Constant log.
	 */
	private static final Log log = LogFactory.getLog(AccountDB.class);
	
	/**
	 * This method gets the account access for user.
	 * 
	 * @param userID the user ID
	 * @param rowLimit the row limit
	 * 
	 * @return the account access for user
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public HashMap getAccountAccessForUser(String userID , int rowLimit, UserRolesVO userRolesVO) throws SMORAException
	{
		HashMap userAccountMap = null;
		List accountInfoList = null; 
		//QC-11258: Account selection tab does not pre-populate when logged-in user has 100 or more accounts
		int limitAccounts = 200;
		accountAccessDB = new AccountAccessDB();
		if(userRolesVO == null)
		{
			userRolesVO = accountAccessDB.getAccountAccessInfo(userID);
		}
		if (userRolesVO != null)
		{
			userAccountMap = new HashMap();	
			userAccountMap.put("ROLES", userRolesVO);
			accountDB = new AccountDB();
			int accountsCount = accountDB.getAccountsCountForUser(userID);
            criteriaSelection=new ReportCriteriaSelection(accountsCount);//added for Issue# 437 by Infosys(Amit)-tjvobaf
            //QC-11258: Account selection tab does not pre-populate when logged-in user has 100 or more accounts
            //Start
            limitAccounts = Integer.parseInt(getAccountDisLimit());
            rowLimit = limitAccounts;
            log.info("limitAccounts from property : "+limitAccounts);
			if(accountsCount <= limitAccounts)
			{
			//End
				accountInfoList = accountDB.getAccountInfo(userRolesVO , rowLimit);
				userAccountMap.put("ACCOUNTS", accountInfoList);
			}
			else
			{
				userAccountMap.put("ACCOUNTS", new ArrayList());
			}
		}
		
		return userAccountMap;
	}
	
	/**
	 * This method gets the account access for user.
	 * 
	 * @param userID the user ID
	 * @param rowLimit the row limit
	 * 
	 * @return the account access for user
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public HashMap getAccountAccessForUser(String userID , int rowLimit) throws SMORAException
	{
		HashMap userAccountMap = null;
		List accountInfoList = null; 
		
		accountAccessDB = new AccountAccessDB();
		userRolesVO = accountAccessDB.getAccountAccessInfo(userID);
		
		if (userRolesVO != null)
		{
			userAccountMap = new HashMap();	
			userAccountMap.put("ROLES", userRolesVO);
			accountDB = new AccountDB();
			accountInfoList = accountDB.getAccountInfo(userRolesVO , rowLimit);
			userAccountMap.put("ACCOUNTS", accountInfoList);
		}
		
		return userAccountMap;
	}
	
	/**
	 * This method validates the user accounts.
	 * 
	 * @param userId the user id
	 * @param userAccounts the user accounts
	 * 
	 * @return the array list
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public ArrayList validateUserAccounts(List userAccounts, String userId) throws SMORAException
	{
		ArrayList<AccountDetailsVO> returnList = null;
		AccountDetailsVO accountDetails = null;
		if (userAccounts != null && userId != null)
		{
			if (userAccounts.size() > 0)
			{
				returnList = new ArrayList<AccountDetailsVO>();
				AccountAccessDB accessDB = new AccountAccessDB();
				AccountDB accountDB = new AccountDB();
				/*UserRolesVO userRolesVO = accessDB.getAccountAccessInfo(userId);
				ArrayList<AccountDetailsVO> accountDetailsList = new AccountDB().getAccountInfo(userRolesVO);

				for (int i = 0; i < accountDetailsList.size(); i++)
				{
					AccountDetailsVO accountDetailsVO = (AccountDetailsVO) accountDetailsList.get(i);
					String accountNumber = accountDetailsVO.getAccountNum();
					if (userAccounts.contains(accountNumber))
					{
						accountDetails = new AccountDetailsVO();
						accountDetails.setAccountNum(accountNumber);
						returnList.add(accountDetails);
					}
				}*/
				List accountNumberList = (ArrayList)accountDB.validateAccountNumber(userId, userAccounts);
				for(int i = 0; i < accountNumberList.size(); i++){
					accountDetails = new AccountDetailsVO();
					Object []obj = (Object[])accountNumberList.get(i);
					accountDetails.setAccountNum(obj[0]+"");
					accountDetails.setAccountName(obj[1]+"");
					returnList.add(accountDetails);
				}
			}
		}
		return returnList;
	}
	
	public UserRolesVO validateUserAccounts(UserRolesVO xmlUserRolesVO, String userId) throws SMORAException
	{
		
		if(xmlUserRolesVO!=null){
			ArrayList<RoleVO> xmlHspList = xmlUserRolesVO.getHspRoles();
			ArrayList<RoleVO> xmlChnList = xmlUserRolesVO.getChnRoles();
			ArrayList<RoleVO> xmlSlsList = xmlUserRolesVO.getSlsRoles();
			ArrayList<RoleVO> xmlCidList = xmlUserRolesVO.getCidRoles();
			AccountAccessDB accessDB = new AccountAccessDB();
			//QC-11790: Modified to maintain customer selection in Template upload 
			RoleVO roleVO = null;
			UserRolesVO userRolesVO = accessDB.getAccountAccessInfo(userId);
			ArrayList<String> levelList = xmlUserRolesVO.getLevel();
			
			ArrayList<String> KeyList = new ArrayList<String>();
			//QC-11790: Modified to maintain customer selection in Template upload 
			if(userRolesVO.getHspRoles()!=null && xmlHspList!=null && !xmlHspList.isEmpty()){
				for(int i = 0; i < userRolesVO.getHspRoles().size(); i++){
					roleVO = userRolesVO.getHspRoles().get(i);
					if(roleVO.getChildren()==null){
						KeyList.add(roleVO.getKey());
					}
					else if(roleVO.getChildren()!=null && roleVO.getChildren().size()>0){
						getSubLevelRoleKeys(roleVO.getChildren(), KeyList);
					}
				}
			}
			//QC-11790: Modified to maintain customer selection in Template upload 
			if(userRolesVO.getChnRoles()!=null && xmlChnList!=null && !xmlChnList.isEmpty()){
				for(int i = 0; i < userRolesVO.getChnRoles().size(); i++){
					roleVO = userRolesVO.getChnRoles().get(i);
					if(roleVO.getChildren()==null){
						KeyList.add(roleVO.getKey());
					}
					else if(roleVO.getChildren()!=null && roleVO.getChildren().size()>0){
						getSubLevelRoleKeys(roleVO.getChildren(), KeyList);
					}
				}
			}
			//QC-11790: Modified to maintain customer selection in Template upload 
			if(userRolesVO.getSlsRoles()!=null && xmlSlsList!=null && !xmlSlsList.isEmpty()){
				for(int i = 0; i < userRolesVO.getSlsRoles().size(); i++){
					roleVO = userRolesVO.getSlsRoles().get(i);
					if(roleVO.getChildren()==null){
						KeyList.add(roleVO.getKey());
					}
					else if(roleVO.getChildren()!=null && roleVO.getChildren().size()>0){
						getSubLevelRoleKeys(roleVO.getChildren(), KeyList);
					}
				}
			}
			//QC-11790: Modified to maintain customer selection in Template upload 
			if(userRolesVO.getCidRoles()!=null && xmlCidList!=null && !xmlCidList.isEmpty()){
				for(int i = 0; i < userRolesVO.getCidRoles().size(); i++){
					KeyList.add(((RoleVO)userRolesVO.getCidRoles().get(i)).getKey());
				}
			}
			if(xmlHspList!=null&&xmlHspList.size()>0){
				for(int i = 0; i < xmlHspList.size(); i++){
					String key = ((RoleVO)xmlHspList.get(i)).getKey();
					//QC-11790: Modified to maintain customer selection in Template upload 
					String tmpKey = null;
					tmpKey = key;
					boolean flag = true;
					if (!KeyList.contains(key))
					{
						flag = false;
						while(!(tmpKey.equalsIgnoreCase("HSP"))){							
							tmpKey = tmpKey.substring(0, tmpKey.lastIndexOf(','));
							if(!KeyList.contains(tmpKey)){
								continue;
							}
							else{
								flag = true;
								break;
							}						
						}
					}
					if (!flag)
					{
						xmlHspList.remove(i);
						i--;
						int beginIndex = key.lastIndexOf(",");
						//QC-11790: Modified to maintain customer selection in Template upload 
						String roleCode = key.substring(beginIndex+1);
						for(int j = 0; j < levelList.size(); j++){

							String data = levelList.get(j);
							if(data.contains(roleCode)){
								levelList.remove(j);
							}
						}
					}
				}
			}
			if(xmlChnList!=null&&xmlChnList.size()>0){
				for(int i = 0; i < xmlChnList.size(); i++){
					String key = ((RoleVO)xmlChnList.get(i)).getKey();
					//QC-11790: Modified to maintain customer selection in Template upload 
					String tmpKey = null;
					tmpKey = key;
					boolean flag = true;
					if (!KeyList.contains(key))
					{
						flag = false;
						while(!(tmpKey.equalsIgnoreCase("CHN"))){							
							tmpKey = tmpKey.substring(0, tmpKey.lastIndexOf(','));
							if(!KeyList.contains(tmpKey)){
								continue;
							}
							else{
								flag = true;
								break;
							}						
						}
					}
					if (!flag)
					{						
						xmlChnList.remove(i);
						i--;
						int beginIndex = key.lastIndexOf(",");
						//QC-11790: Modified to maintain customer selection in Template upload 
						String roleCode = key.substring(beginIndex+1);
						for(int j = 0; j < levelList.size(); j++){

							String data = levelList.get(j);
							if(data.contains(roleCode)){
								levelList.remove(j);
							}
						}
					}
				}
			}
			if(xmlSlsList!=null&&xmlSlsList.size()>0){
				for(int i = 0; i < xmlSlsList.size(); i++){
					String key = ((RoleVO)xmlSlsList.get(i)).getKey();
					//QC-11790: Modified to maintain customer selection in Template upload 
					String tmpKey = null;
					tmpKey = key;
					boolean flag = true;
					if (!KeyList.contains(key))
					{
						flag = false;
						while(!(tmpKey.equalsIgnoreCase("SLS"))){							
							tmpKey = tmpKey.substring(0, tmpKey.lastIndexOf(","));
							if(!KeyList.contains(tmpKey)){
								continue;
							}
							else{
								flag = true;
								break;
							}						
						}
					}
					if (!flag)
					{
						xmlSlsList.remove(i);
						i--;
						int beginIndex = key.lastIndexOf(",");
						//QC-11790: Modified to maintain customer selection in Template upload 
						String roleCode = key.substring(beginIndex+1);
						for(int j = 0; j < levelList.size(); j++){

							String data = levelList.get(j);
							if(data.contains(roleCode)){
								levelList.remove(j);
							}
						}
					}
				}
			}
			if(xmlCidList!=null&&xmlCidList.size()>0){
				for(int i = 0; i < xmlCidList.size(); i++){
					String key = ((RoleVO)xmlCidList.get(i)).getKey();
					if (!KeyList.contains(key))
					{
						xmlCidList.remove(i);
						i--;
						int beginIndex = key.lastIndexOf(",");
						String roleCode = key.substring(beginIndex+1);
						for(int j = 0; j < levelList.size(); j++){

							String data = levelList.get(j);
							if(data.contains(roleCode)){
								levelList.remove(j);
							}
						}
					}
				}
			}
			xmlUserRolesVO.setHspRoles(xmlHspList);
			xmlUserRolesVO.setChnRoles(xmlChnList);
			xmlUserRolesVO.setSlsRoles(xmlSlsList);
			xmlUserRolesVO.setCidRoles(xmlCidList);
			xmlUserRolesVO.setLevel(levelList);
		}
		
		return xmlUserRolesVO;
	}

	/**
	 * @param groupID the group ID
	 * 
	 * @return the account details for group
	 */
	public HashMap getAccountDetails(String accountType, String key, UserRolesVO userRolesVO) throws SMORAException
	{
		final String METHODNAME = "getAccountDetails";
		ArrayList<RoleVO> nationalGroupList = null;
		ArrayList<RoleVO> nationalSubGroupList = null;
		ArrayList<RoleVO> custRegionList = null;
		ArrayList<RoleVO> hspList = null;
		ArrayList<RoleVO> chnList = null;
		ArrayList<RoleVO> slsList = null;
		ArrayList<RoleVO> cidList = null;
		UserAccountVO userAccountVO = null;
		AccountDB accountDB = new AccountDB();
		ArrayList<UserAccountVO> cidUserAccountVOList = new ArrayList<UserAccountVO>();
		ArrayList<UserAccountVO> hspUserAccountVOList = new ArrayList<UserAccountVO>();
		ArrayList<UserAccountVO> chnUserAccountVOList = new ArrayList<UserAccountVO>();
		ArrayList<UserAccountVO> slsUserAccountVOList = new ArrayList<UserAccountVO>();
		HashMap<String,ArrayList<UserAccountVO>> returnMap = new HashMap<String, ArrayList<UserAccountVO>>();
		HashMap<String, String> nationalSubGrpMap = null;
		try 
		{
				// National.
				if(accountType.equals("National Group"))
				{
					nationalGroupList = userRolesVO.getHspRoles();
					if(nationalGroupList != null)
					{
						for(int i = 0;i < nationalGroupList.size(); i++)
						{
							RoleVO nationalGroupRoleVO = nationalGroupList.get(i);
							if(nationalGroupRoleVO != null && nationalGroupRoleVO.getKey() != null && nationalGroupRoleVO.getKey().equals(key))
							{
								ArrayList<String> natlGrpCdList = new ArrayList<String>();
								natlGrpCdList.add(accountDB.getValueFromKey(key, 2));
								if(nationalGroupRoleVO.getChildren() == null)
								{
									// Has Full Access
									List nationalSubGrpList =  accountDB.getNationalSubGroup(accountDB.getValueFromKey(key, 2));
									for(int j = 0;j < nationalSubGrpList.size(); j++)
									{
										TNatlSubGrp natlSubGrp = (TNatlSubGrp)nationalSubGrpList.get(j);
										userAccountVO = new UserAccountVO();
										userAccountVO.setCode(natlSubGrp.getId().getNatlSubGrpCd());
										if(natlSubGrp.getNatlSubGrpNam() != null)
										{
											userAccountVO.setDescription(natlSubGrp.getNatlSubGrpNam());
										}
										else
										{
											userAccountVO.setDescription("Undefined");
										}
										userAccountVO.setFullAccess("Y");
										userAccountVO.setKey(key + "," + natlSubGrp.getId().getNatlSubGrpCd());
										userAccountVO.setType("Sub Group");
										hspUserAccountVOList.add(userAccountVO);
									}
									//QC-9713 : Sort by Account name
									Collections.sort(hspUserAccountVOList, new AccountNameComparator());
									returnMap.put("National Group", hspUserAccountVOList);
								}
								else
								{
									// Don't have Full Access
									ArrayList<RoleVO> nationalSubGroupRoleVO = nationalGroupRoleVO.getChildren();
									String nationalGrpCd = "";
									ArrayList<String> nationalSubGrpCode = new ArrayList<String>();
									if(nationalSubGroupRoleVO != null && nationalSubGroupRoleVO.size() > 0)
									{
										for(int k = 0;k< nationalSubGroupRoleVO.size(); k++)
										{
											RoleVO subNationalRoleVO = nationalSubGroupRoleVO.get(k);
											nationalGrpCd = accountDB.getValueFromKey(subNationalRoleVO.getKey(), 2);
											nationalSubGrpCode.add(accountDB.getValueFromKey(subNationalRoleVO.getKey(), 3));
										}
										nationalSubGrpMap =  accountDB.getNationalSubGroupDescAsMap(nationalGrpCd,nationalSubGrpCode);
										for(int k = 0;k< nationalSubGroupRoleVO.size(); k++)
										{
										RoleVO subNationalRoleVO = nationalSubGroupRoleVO.get(k);
										userAccountVO = new UserAccountVO();
										userAccountVO.setCode(accountDB.getValueFromKey(subNationalRoleVO.getKey(), 3));
										if(nationalSubGrpMap.get(accountDB.getValueFromKey(subNationalRoleVO.getKey(), 3)) != null)
										{
											userAccountVO.setDescription(nationalSubGrpMap.get(accountDB.getValueFromKey(subNationalRoleVO.getKey(), 3)));
										}
										else
										{
											userAccountVO.setDescription("Undefined");
										}
										if(subNationalRoleVO.getChildren() == null)
										{
											userAccountVO.setFullAccess("Y");
										}
										else
										{
											userAccountVO.setFullAccess("N");
										}
										userAccountVO.setKey(subNationalRoleVO.getKey());
										userAccountVO.setType("Sub Group");
										hspUserAccountVOList.add(userAccountVO);
										}
										//QC-9713 : Sort by Account name
										Collections.sort(hspUserAccountVOList, new AccountNameComparator());
										returnMap.put("National Group", hspUserAccountVOList);
									}
								}
							}
						}
					}
				}
				//Sub-National.
				else if(accountType.equals("Sub Group"))
				{
					nationalGroupList = userRolesVO.getHspRoles();
					if(nationalGroupList != null)
					{
						for(int i = 0;i < nationalGroupList.size(); i++)
						{
							RoleVO nationalGroupRoleVO = nationalGroupList.get(i);
							String natlGrpCd = key.substring(0,key.lastIndexOf(','));
							if(nationalGroupRoleVO != null && nationalGroupRoleVO.getKey() != null && nationalGroupRoleVO.getKey().equals(natlGrpCd))
							{
								nationalSubGroupList = nationalGroupRoleVO.getChildren();
								if(nationalSubGroupList != null)
								{
									for(int j = 0; j < nationalSubGroupList.size(); j++)
									{
										RoleVO nationalSubGroupVO = nationalSubGroupList.get(j);
										if(nationalSubGroupVO != null && nationalSubGroupVO.getKey() != null && nationalSubGroupVO.getKey().equals(key))
										{
											// Has full access for Sub National Level.
											if(nationalSubGroupVO.getChildren() == null)
											{
												List custRgnList = accountDB.getCustRegion(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3));
												for(int k = 0;k < custRgnList.size(); k++)
												{
													TCustRgn custRgn = (TCustRgn)custRgnList.get(k);
													userAccountVO = new UserAccountVO();
													userAccountVO.setCode(custRgn.getId().getCustRgnNum());
													userAccountVO.setDescription("Region");
													userAccountVO.setFullAccess("Y");
													userAccountVO.setKey(key + "," + custRgn.getId().getCustRgnNum());
													userAccountVO.setType("Region ID");
													hspUserAccountVOList.add(userAccountVO);
												}
												returnMap.put("National Group: Sub Group", hspUserAccountVOList);
											}
											else
											{
												// Dont have full access for Sub National Level.
												ArrayList<RoleVO> custRgnVOList = nationalSubGroupVO.getChildren();
												for(int m = 0;m < custRgnVOList.size(); m++)
												{
													RoleVO roleVO = custRgnVOList.get(m);
													userAccountVO = new UserAccountVO();
													userAccountVO.setCode(accountDB.getValueFromKey(roleVO.getKey(), 4));
													userAccountVO.setDescription("Region");
													if(roleVO.getChildren() == null)
													{
														userAccountVO.setFullAccess("Y");
													}
													else
													{
														userAccountVO.setFullAccess("N");
													}
													userAccountVO.setKey(roleVO.getKey());
													userAccountVO.setType("Region ID");
													hspUserAccountVOList.add(userAccountVO);
												}
												returnMap.put("National Group: Sub Group", hspUserAccountVOList);
											}
										}
									}
								}
								//Has Full Access.
								else
								{
									List custRgnList = accountDB.getCustRegion(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3));
									for(int k = 0;k < custRgnList.size(); k++)
									{
										TCustRgn custRgn = (TCustRgn)custRgnList.get(k);
										userAccountVO = new UserAccountVO();
										userAccountVO.setCode(custRgn.getId().getCustRgnNum());
										userAccountVO.setDescription("Region");
										userAccountVO.setFullAccess("Y");
										userAccountVO.setKey(key + "," + custRgn.getId().getCustRgnNum());
										userAccountVO.setType("Region ID");
										hspUserAccountVOList.add(userAccountVO);
									}
									returnMap.put("National Group: Sub Group", hspUserAccountVOList);
								}
							}
						}
					}
				}
				else if(accountType.equals("Region ID"))
				{
					nationalGroupList = userRolesVO.getHspRoles();
					String natlGrpCd = "HSP," + accountDB.getValueFromKey(key, 2);
					String natlSubGrpCd = natlGrpCd + "," + accountDB.getValueFromKey(key, 3);
					List custDstrctList = null;
					if(nationalGroupList != null)
					{
						for(int i = 0;i < nationalGroupList.size(); i++)
						{
							RoleVO nationalGroupRoleVO = nationalGroupList.get(i);
							if(nationalGroupRoleVO != null && nationalGroupRoleVO.getKey() != null && nationalGroupRoleVO.getKey().equals(natlGrpCd))
							{
								nationalSubGroupList = nationalGroupRoleVO.getChildren();
								if(nationalSubGroupList != null)
								{
									for(int j = 0; j < nationalSubGroupList.size(); j++)
									{
										RoleVO nationalSubGroupVO = nationalSubGroupList.get(j);
										if(nationalSubGroupVO != null && nationalSubGroupVO.getKey() != null && nationalSubGroupVO.getKey().equals(natlSubGrpCd))
										{
											custRegionList = nationalSubGroupVO.getChildren();
											if(custRegionList != null)
											{
												for(int k = 0;k < custRegionList.size(); k++)
												{
													RoleVO custRegionVO = custRegionList.get(k);
													if(custRegionVO != null && custRegionVO.getKey() != null && custRegionVO.getKey().equals(key))
													{
														// Has Full Access.
														if(custRegionVO.getChildren() == null)
														{
															custDstrctList = accountDB.getDistrict(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3),accountDB.getValueFromKey(key, 4));
															for(int m = 0; m < custDstrctList.size(); m++)
															{
																TCustDstrct custDstrct = (TCustDstrct)custDstrctList.get(m);
																userAccountVO = new UserAccountVO();
																userAccountVO.setCode(custDstrct.getId().getCustDstrctNum());
																userAccountVO.setDescription("District");
																userAccountVO.setFullAccess("Y");
																userAccountVO.setKey(key + "," + custDstrct.getId().getCustDstrctNum());
																userAccountVO.setType("District");
																hspUserAccountVOList.add(userAccountVO);
															}
															returnMap.put("National: Sub Group: Region ID: District", hspUserAccountVOList);
														}
														// Dont Have Full Access.
														else
														{
															ArrayList<RoleVO> custDstrctRoleVO = custRegionVO.getChildren();
															for(int m = 0;m < custDstrctRoleVO.size(); m++)
															{
																RoleVO roleVO = custDstrctRoleVO.get(m);
																userAccountVO = new UserAccountVO();
																userAccountVO.setCode(accountDB.getValueFromKey(roleVO.getKey(), 5));
																userAccountVO.setDescription("District");
																if(roleVO.getChildren() == null)
																{
																	userAccountVO.setFullAccess("Y");
																}
																else
																{
																	userAccountVO.setFullAccess("N");
																}
																userAccountVO.setKey(roleVO.getKey());
																userAccountVO.setType("District");
																hspUserAccountVOList.add(userAccountVO);
															}
															returnMap.put("National: Sub Group: Region ID: District", hspUserAccountVOList);
														}
													}
												}
											}
											else
											{
//												//Dont have full access.
												custDstrctList = accountDB.getDistrict(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3),accountDB.getValueFromKey(key, 4));
												for(int m = 0; m < custDstrctList.size(); m++)
												{
													TCustDstrct custDstrct = (TCustDstrct)custDstrctList.get(m);
													userAccountVO = new UserAccountVO();
													userAccountVO.setCode(custDstrct.getId().getCustDstrctNum());
													userAccountVO.setDescription("District");
													userAccountVO.setFullAccess("Y");
													userAccountVO.setKey(key + "," + custDstrct.getId().getCustDstrctNum());
													userAccountVO.setType("District");
													hspUserAccountVOList.add(userAccountVO);
												}
												returnMap.put("National: Sub Group: Region ID: District", hspUserAccountVOList);
											}
										}
									}
								}
								else
								{
									//Dont have full access.
									custDstrctList = accountDB.getDistrict(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3),accountDB.getValueFromKey(key, 4));
									for(int m = 0; m < custDstrctList.size(); m++)
									{
										TCustDstrct custDstrct = (TCustDstrct)custDstrctList.get(m);
										userAccountVO = new UserAccountVO();
										userAccountVO.setCode(custDstrct.getId().getCustDstrctNum());
										userAccountVO.setDescription("District");
										userAccountVO.setFullAccess("Y");
										userAccountVO.setKey(key + "," + custDstrct.getId().getCustDstrctNum());
										userAccountVO.setType("District");
										hspUserAccountVOList.add(userAccountVO);
									}
									returnMap.put("National: Sub Group: Region ID: District", hspUserAccountVOList);

								}
							}
						}
					}
				}
				// CHN
			else if(accountType.equals("Chain"))
			{
				List custRgnNumList = null;
				chnList = userRolesVO.getChnRoles();
				if(chnList != null)
				{
					for(int i = 0;i < chnList.size(); i++)
					{
						RoleVO chnRoleVO = chnList.get(i);
						if(chnRoleVO != null && chnRoleVO.getKey() != null && chnRoleVO.getKey().equals(key))
						{
							if(chnRoleVO.getChildren() == null)
							{
								//Has Full Access
								custRgnNumList = accountDB.getChnRegion(accountDB.getValueFromKey(chnRoleVO.getKey(), 2));
								for(int j = 0;j < custRgnNumList.size(); j++)
								{
									TChnRgn chnRgn = (TChnRgn)custRgnNumList.get(j);
									userAccountVO = new UserAccountVO();
									userAccountVO.setCode(chnRgn.getId().getCustRgnNum());
									userAccountVO.setDescription("Region");
									userAccountVO.setFullAccess("Y");
									userAccountVO.setType("Region");
									userAccountVO.setKey(chnRoleVO.getKey() + "," + chnRgn.getId().getCustRgnNum());
									chnUserAccountVOList.add(userAccountVO);
								}
								returnMap.put("Chain: Region", chnUserAccountVOList);
							}
							else
							{
								// Dont have Full Access
								ArrayList<RoleVO> custRgnList =  chnRoleVO.getChildren();
								for(int k = 0; k < custRgnList.size(); k++)
								{
									RoleVO custRgnRoleVO = custRgnList.get(k);
									userAccountVO = new UserAccountVO();
									userAccountVO.setCode(accountDB.getValueFromKey(custRgnRoleVO.getKey(), 3));
									userAccountVO.setDescription("Region");
									if(custRgnRoleVO.getChildren() == null)
									{
										userAccountVO.setFullAccess("Y");
									}
									else
									{
										userAccountVO.setFullAccess("N");
									}
									userAccountVO.setType("Region");
									userAccountVO.setKey(custRgnRoleVO.getKey());
									chnUserAccountVOList.add(userAccountVO);
								}
								returnMap.put("Chain: Region", chnUserAccountVOList);
							}
						}
					}
				}
			}
			else if(accountType.equals("Region"))
			{
				List custDstrctNumList = null;
				chnList = userRolesVO.getChnRoles();
				String custRgnNum = "CHN," + accountDB.getValueFromKey(key, 2);
				if(chnList != null)
				{
					for(int i = 0;i < chnList.size(); i++)
					{
						RoleVO chnRoleVO = chnList.get(i);
						if(chnRoleVO != null && chnRoleVO.getKey() != null && chnRoleVO.getKey().equals(custRgnNum))
						{
							if(chnRoleVO.getChildren() != null)
							{
								ArrayList<RoleVO> custRegionRoleVOList = chnRoleVO.getChildren();
								if(custRegionRoleVOList != null)
								{
									for(int j = 0;j < custRegionRoleVOList.size(); j++)
									{
										RoleVO custRegionRoleVO = custRegionRoleVOList.get(j);
										if(custRegionRoleVO != null && custRegionRoleVO.getKey() != null && custRegionRoleVO.getKey().equals(key))
										{
											if(custRegionRoleVO.getChildren() == null)
											{
												// Has full access
												custDstrctNumList = accountDB.getChnRgnDistrict(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3));
												for(int k = 0;k < custDstrctNumList.size(); k++)
												{
													TChnRgnDstrct chnRgnDstrct = (TChnRgnDstrct)custDstrctNumList.get(k);
													userAccountVO = new UserAccountVO();
													userAccountVO.setCode(chnRgnDstrct.getId().getCustDstrctNum());
													userAccountVO.setDescription("District");
													userAccountVO.setFullAccess("Y");
													userAccountVO.setKey(custRegionRoleVO.getKey() + "," + chnRgnDstrct.getId().getCustDstrctNum());
													userAccountVO.setType("District");
													chnUserAccountVOList.add(userAccountVO);
												}
												returnMap.put("Chain: Region: District", chnUserAccountVOList);
											}
											else
											{
												// Dont Have Full Access.
												ArrayList<RoleVO> custDistrictRoleVOList = custRegionRoleVO.getChildren();
												for(int m = 0;m < custDistrictRoleVOList.size(); m++)
												{
													RoleVO custDistrictRoleVO = custDistrictRoleVOList.get(m);
													userAccountVO = new UserAccountVO();
													userAccountVO.setCode(accountDB.getValueFromKey(custDistrictRoleVO.getKey(), 4));
													userAccountVO.setDescription("District");
													// Since for this level (SLS) the children node will be always null.
													if(custDistrictRoleVO.getChildren() == null)
													{
														userAccountVO.setFullAccess("Y");
													}
													else
													{
														userAccountVO.setFullAccess("N");
													}
													userAccountVO.setKey(custDistrictRoleVO.getKey());
													userAccountVO.setType("District");
													chnUserAccountVOList.add(userAccountVO);
												}
												returnMap.put("Chain: Region: District", chnUserAccountVOList);
											}
										}
										
									}
								}
								else
								{
									custDstrctNumList = accountDB.getChnRgnDistrict(accountDB.getValueFromKey(key, 2),accountDB.getValueFromKey(key, 3));
									for(int k = 0;k < custDstrctNumList.size(); k++)
									{
										TChnRgnDstrct chnRgnDstrct = (TChnRgnDstrct)custDstrctNumList.get(k);
										userAccountVO = new UserAccountVO();
										userAccountVO.setCode(chnRgnDstrct.getId().getCustDstrctNum());
										userAccountVO.setDescription("District");
										userAccountVO.setFullAccess("Y");
										userAccountVO.setKey(key + "," + chnRgnDstrct.getId().getCustDstrctNum());
										userAccountVO.setType("District");
										chnUserAccountVOList.add(userAccountVO);
									}
									returnMap.put("Chain: Region: District", chnUserAccountVOList);
								}
							}
						}
					}
				}
			}
			else if(accountType.equals("DC"))
			{
				List homeDCList = null; 
				slsList = userRolesVO.getSlsRoles();
				if(slsList != null)
				{
					for(int i = 0;i < slsList.size(); i++)
					{
						RoleVO homeDCRoleVO = slsList.get(i);
						if(homeDCRoleVO != null && homeDCRoleVO.getKey() != null && homeDCRoleVO.getKey().equals(key))
						{
							// Has Full access.
							if(homeDCRoleVO.getChildren() == null)
							{
								homeDCList = accountDB.getSlsDcTerr(accountDB.getValueFromKey(key, 2));
								for(int j = 0; j < homeDCList.size(); j++)
								{
									TDcTerr dcTerr = (TDcTerr)homeDCList.get(j);
									userAccountVO = new UserAccountVO();
									userAccountVO.setCode(dcTerr.getId().getSlsTerrId());
									userAccountVO.setDescription("Territory");
									userAccountVO.setFullAccess("Y");
									userAccountVO.setKey(key + "," + dcTerr.getId().getSlsTerrId());
									userAccountVO.setType("Territory");
									slsUserAccountVOList.add(userAccountVO);
								}
								returnMap.put("DC: Territory", slsUserAccountVOList);
							}
							// Dont Have Full Access.
							else
							{
								ArrayList<RoleVO> slsTerritoryList = homeDCRoleVO.getChildren();
								for(int k = 0; k < slsTerritoryList.size(); k++)
								{
									RoleVO slsTerritoryRoleVO = slsTerritoryList.get(k);
									userAccountVO = new UserAccountVO();
									userAccountVO.setCode(accountDB.getValueFromKey(slsTerritoryRoleVO.getKey(), 3));
									userAccountVO.setDescription("Territory");
									userAccountVO.setFullAccess("Y");
									userAccountVO.setKey(slsTerritoryRoleVO.getKey());
									userAccountVO.setType("Territory");
									slsUserAccountVOList.add(userAccountVO);
								}
								returnMap.put("DC: Territory", slsUserAccountVOList);
							}
						}
					}
				}
			}
			else if(accountType.equals("TopLevel"))
			{
				cidList = userRolesVO.getCidRoles();
				hspList = userRolesVO.getHspRoles();
				chnList = userRolesVO.getChnRoles();
				slsList = userRolesVO.getSlsRoles();
				List<String> custAcctIds = new ArrayList<String>();
				if(cidList != null && cidList.size() > 0)
				{
					for(int i = 0;i < cidList.size(); i++)
					{
						RoleVO cidRoleVO = cidList.get(i);
						custAcctIds.add(accountDB.getValueFromKey(cidRoleVO.getKey(), 2));
					}
					HashMap<String, String> custAcctNameMap = accountDB.getAccountNamesAsMap(custAcctIds);
					for(int i = 0;i < cidList.size(); i++)
					{
						RoleVO cidRoleVO = cidList.get(i);
						userAccountVO = new UserAccountVO();
						userAccountVO.setCode(accountDB.getValueFromKey(cidRoleVO.getKey(), 2));
						userAccountVO.setDescription(custAcctNameMap.get(accountDB.getValueFromKey(cidRoleVO.getKey(), 2)));
						userAccountVO.setType("Account");
						if(cidRoleVO.getChildren() == null)
						{
							userAccountVO.setFullAccess("Y");
						}
						userAccountVO.setKey(cidRoleVO.getKey());
						cidUserAccountVOList.add(userAccountVO);
					}
					//QC-9713 : Sort by Account name
					Collections.sort(cidUserAccountVOList, new AccountNameComparator());
					returnMap.put("Account", cidUserAccountVOList);
				}
				if(hspList != null && hspList.size() > 0)
				{
				ArrayList<String> natlGrpCd = new ArrayList<String>();
				for (int i = 0; i < hspList.size(); i++) 
				{
					RoleVO hspRoleVO = hspList.get(i);
					natlGrpCd.add(accountDB.getValueFromKey(hspRoleVO.getKey(), 2));
				}
				HashMap<String, String> natlGrpDescMap1 = accountDB.getNationalGrpDscAsMap(natlGrpCd);
				for (int i = 0; i < hspList.size(); i++) 
				{
					RoleVO hspRoleVO = hspList.get(i);
					userAccountVO = new UserAccountVO();
					userAccountVO.setCode(accountDB.getValueFromKey(hspRoleVO.getKey(), 2));
					if(natlGrpDescMap1.get(accountDB.getValueFromKey(hspRoleVO.getKey(), 2)) != null)
					{
						userAccountVO.setDescription(natlGrpDescMap1.get(accountDB.getValueFromKey(hspRoleVO.getKey(), 2)));
					}
					else
					{
						userAccountVO.setDescription("Undefined");
					}
					userAccountVO.setType("National Group");
					if(hspRoleVO.getChildren() == null)
					{
						userAccountVO.setFullAccess("Y");
					}
					else
					{
						userAccountVO.setFullAccess("N");
					}
					userAccountVO.setKey(hspRoleVO.getKey());
					hspUserAccountVOList.add(userAccountVO);
				}
				//QC-9713 : Sort by Account name
				Collections.sort(hspUserAccountVOList, new AccountNameComparator());
				returnMap.put("National Group", hspUserAccountVOList);
			}
			//CHN
			if(chnList != null && chnList.size() > 0)
			{
				ArrayList<String> custChnId = new ArrayList<String>();
				for(int i = 0;i < chnList.size(); i ++)
				{
					RoleVO chnRoleVO = chnList.get(i);
					custChnId.add(accountDB.getValueFromKey(chnRoleVO.getKey(), 2));
				}
				HashMap<String, String> custChnMap = accountDB.getCustChnDscAsMap(custChnId) ;
				for(int i = 0;i < chnList.size(); i ++)
				{
					RoleVO chnRoleVO = chnList.get(i);
					userAccountVO = new UserAccountVO();
					userAccountVO.setCode(accountDB.getValueFromKey(chnRoleVO.getKey(), 2));
					if(custChnMap.get(accountDB.getValueFromKey(chnRoleVO.getKey(), 2)) != null)
					{
						userAccountVO.setDescription(custChnMap.get(accountDB.getValueFromKey(chnRoleVO.getKey(), 2)));
					}
					else
					{
						userAccountVO.setDescription("Undefined");
					}
					userAccountVO.setType("Chain");
					if(chnRoleVO.getChildren() == null)
					{
						userAccountVO.setFullAccess("Y");
					}
					else
					{
						userAccountVO.setFullAccess("N");
					}
					userAccountVO.setKey(chnRoleVO.getKey());
					chnUserAccountVOList.add(userAccountVO);
				}
				//QC-9713 : Sort by Account name
				Collections.sort(chnUserAccountVOList, new AccountNameComparator());
				returnMap.put("Chain", chnUserAccountVOList);
			}
			if(slsList != null && slsList.size() > 0)
			{
				for(int i = 0;i < slsList.size(); i++)
				{
					RoleVO slsRoleVO = slsList.get(i);
					userAccountVO = new UserAccountVO();
					userAccountVO.setCode(accountDB.getValueFromKey(slsRoleVO.getKey(), 2));
					userAccountVO.setType("DC");
					userAccountVO.setDescription("");
					if(slsRoleVO.getChildren() == null)
					{
						userAccountVO.setFullAccess("Y");
					}
					else
					{
						userAccountVO.setFullAccess("N");
					}
					userAccountVO.setKey(slsRoleVO.getKey());
					slsUserAccountVOList.add(userAccountVO);
				}
				returnMap.put("DC", slsUserAccountVOList);
			}
			}
		}
		catch (Exception e) 
		{
			throw new SMORAException(e, CLASSNAME, METHODNAME);
		}
		return returnMap;
	}
	
	/**
	 * This method will return the top element count.
	 * @param userRolesVO
	 * @return
	 */
	public int getTopLevelCount(UserRolesVO userRolesVO)
	{
		int count = 0;
		ArrayList<RoleVO> cidList = userRolesVO.getCidRoles();
		ArrayList<RoleVO> hspList = userRolesVO.getHspRoles();
		ArrayList<RoleVO> chnList = userRolesVO.getChnRoles();
		ArrayList<RoleVO> slsList = userRolesVO.getSlsRoles();
		if (cidList != null) {
			count = count + cidList.size();
		}
		if (hspList != null) {
			count = count + hspList.size();
		}
		if (chnList != null) {
			count = count + chnList.size();
		}
		if (slsList != null) {
			count = count + slsList.size();
		}
		return count;
	}
	
	/**
	 * This method will return the column names for search for the 
	 * given user.
	 * @param userId
	 * @return
	 */
	public List getColumnNameForAccountSearch(String userId) throws SMORAException
	{
		final String METHODNAME = "getColumnNameForAccountSearch";
		List returnList = null;

		//Variable scope is increased for QC-11211
		Session session = null;
		try 
		{
			template = DBUtil.getTemplate(template);
			session = template.getSession();
			returnList = session.createSQLQuery("SELECT 'National Group' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' \n" +
												 " AND access_role_cd = 'HSP' \n" +
												 "  AND lvl2_valu IS NULL \n" +
												 " AND lvl3_valu IS NULL \n" +
												 " AND lvl4_valu IS NULL \n" +
												 " AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'Chain' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' \n" +
												 " AND access_role_cd = 'CHN' \n" +
												 " AND lvl2_valu IS NULL \n" +
												 " AND lvl3_valu IS NULL \n" +
												 " AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'Sub Group' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' \n" +
												 " AND access_role_cd = 'HSP' \n" +
												 " AND lvl3_valu IS NULL \n" +
												 " AND lvl4_valu IS NULL \n" +
												 " AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'Region ID' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' \n" +
												 " AND access_role_cd = 'HSP' \n" +
												 " AND lvl4_valu IS NULL \n" +
												 " AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'District' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' AND access_role_cd = 'HSP' AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'DC' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' \n" +
												 " AND access_role_cd = 'SLS' \n" +
												 " AND lvl2_valu IS NULL \n" +
												 " AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'Territory' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' AND access_role_cd = 'SLS' AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'Account Number' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' AND access_role_cd = 'CID' AND ROWNUM = 1 \n" +
												 " UNION ALL \n" +
												 " SELECT 'Account Name' \n" +
												 " FROM s_iw_user_access \n" +
												 " WHERE user_id = '" + userId + "' AND access_role_cd = 'CID' AND ROWNUM = 1 \n"
												 ).list();
		}catch(SMORADatabaseException dbe)
		{
			throw new SMORAException(dbe, CLASSNAME, METHODNAME);
		} catch (Exception e) 
		{
			throw new SMORAException(e, CLASSNAME, METHODNAME);
		}
		 //QC-11211 Finally block added
		finally
		{
			if(template!=null)
			template.finallySessionClosing(CLASSNAME, METHODNAME, session);
		}
		return returnList;
	}
	
	
	//Added for SO-61 during FY12-RU1 for getting the Department list per Account
	public static Map getAccountMapByDept(List accountVOList) {
		List tmpList = new ArrayList();
		for (int i = 0; i < accountVOList.size(); i++) {
			AccountVO accountVO = (AccountVO) accountVOList.get(i);
			if (!tmpList.contains(accountVO.getAccountNum())) {
				tmpList.add(accountVO.getAccountNum());
			}
		}
		Map accountByDeptMap = new HashMap();
		for (int j = 0; j < tmpList.size(); j++) {
			accountByDeptMap.put(tmpList.get(j), new ArrayList());
		}
		for (int i = 0; i < accountVOList.size(); i++) {
			AccountVO accountVO = (AccountVO) accountVOList.get(i);
			List deptList = (ArrayList)accountByDeptMap.get(accountVO.getAccountNum());
			deptList.add(accountVO.getDepartName());
			accountByDeptMap.put(accountVO.getAccountNum(),deptList);
		}

		/*
		String tempAccountNo="";
		List tempDeptList=null;
		for (int i = 0; i < accountVOList.size(); i++) {
			AccountVO accountVO = (AccountVO) accountVOList.get(i);
			String currentAccountNo=accountVO.getAccountNum();
			String currentDept=accountVO.getDepartName();
			if(tempAccountNo.equalsIgnoreCase("")){
				tempDeptList=new ArrayList();
				tempAccountNo=currentAccountNo;
				tempDeptList.add(currentDept);
			}
			else if((currentAccountNo!=null && currentAccountNo.equalsIgnoreCase(tempAccountNo))){
				tempDeptList.add(currentDept);
			}
			else {
				accountByDeptMap.put(tempAccountNo,tempDeptList);
				tempAccountNo="";
			}	
		}
		*/
		return accountByDeptMap;
	}

	
	/**
	 * Method to get the sub-level keys.
	 * QC-11790: Added to maintain customer selection in Template upload
	 * @param roleVOList
	 * @param KeyList
	 * @throws SMORAException
	 */
	public void getSubLevelRoleKeys(ArrayList<RoleVO> roleVOList, ArrayList<String> KeyList) throws SMORAException
	{
		RoleVO roleVO = null;
		
		for(int i = 0; i < roleVOList.size(); i++){
			roleVO = roleVOList.get(i);
			if(roleVO.getChildren()== null){
				KeyList.add(roleVO.getKey());
			}
			else if(roleVO.getChildren()!=null && roleVO.getChildren().size()>0){
				getSubLevelRoleKeys(roleVO.getChildren(), KeyList);
			}
		}
	
	
	}
	
	/**
	 * Method to get the number of accounts from smora.prop to be displayed on customer selection page .
	 * QC-11258: Account selection tab does not pre-populate when logged-in user has 100 or more accounts
	 * 
	 * @throws SMORAException
	 */
	public String getAccountDisLimit() throws SMORAException
	{		
		String accountLimit = "";
		SMOFileProps fileProps = new SMOFileProps();
		Properties properties = fileProps.getFileProperties();		
		accountLimit = properties.getProperty("ACCOUNT_LIMIT");	
		if(accountLimit == null || "".equals(accountLimit)){
			accountLimit = "200";
		}
		return accountLimit;
	}
}
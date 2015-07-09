/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.mckesson.smora.dto.RoleVO;
import com.mckesson.smora.dto.UserRolesVO;
import java.util.Iterator;

/**
 * The Class CustomerUtil.
 */
public class CustomerUtil
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "CustomerUtil";
	/**
	 * The roles.
	 */
	private ArrayList roles = null;

	/**
	 * The roles VO list.
	 */
	private ArrayList rolesVOList = null;

	/**
	 * The role names.
	 */
	private Set roleNames = null;

	/**
	 * This method gets the roles.
	 * 
	 * @param userRolesVO the user roles VO
	 * 
	 * @return the roles
	 */
	public ArrayList getRoles(UserRolesVO userRolesVO)
	{
		roles = new ArrayList();
		roleNames = new TreeSet();
		ArrayList chnVOList = new ArrayList();
		ArrayList hspVOList = new ArrayList();
		ArrayList cidVOList = new ArrayList();
		ArrayList slsVOList = new ArrayList();
		if (userRolesVO.getChnRoles() != null)
		{
			chnVOList = userRolesVO.getChnRoles();
		}
		if (userRolesVO.getHspRoles() != null)
		{
			hspVOList = userRolesVO.getHspRoles();
		}
		if (userRolesVO.getCidRoles() != null)
		{
			cidVOList = userRolesVO.getCidRoles();
		}
		if (userRolesVO.getSlsRoles() != null)
		{
			slsVOList = userRolesVO.getSlsRoles();
		}
		rolesVOList = new ArrayList();
		for (int count = 0; count < hspVOList.size(); count++)
		{
			rolesVOList.add(hspVOList.get(count));
		}
		for (int count = 0; count < chnVOList.size(); count++)
		{
			rolesVOList.add(chnVOList.get(count));
		}
		for (int count = 0; count < cidVOList.size(); count++)
		{
			rolesVOList.add(cidVOList.get(count));
		}
		for (int count = 0; count < slsVOList.size(); count++)
		{
			rolesVOList.add(slsVOList.get(count));
		}
		Iterator rolesVOIterator = rolesVOList.iterator();

		RoleVO roleVO = null;
		int i = 0;
		while (rolesVOIterator.hasNext())
		{
			roleVO = (RoleVO) rolesVOIterator.next();
			String type = roleVO.getType();
			ArrayList children = new ArrayList();
			if (roleVO.getChildren() != null)
			{
				children = roleVO.getChildren();
			}

			StringTokenizer tokens = new StringTokenizer(roleVO.getKey(), ",");
			if(tokens.hasMoreTokens()){
				tokens.nextToken();
				while (tokens.hasMoreTokens())
				{
					roleNames.add(tokens.nextToken());
				}
			}

			int level = tokens.countTokens();

			for (int j = 0; j < children.size(); j++)
			{
				RoleVO regionRoleVO = (RoleVO) children.get(j);
				String typeChild = regionRoleVO.getType();
				StringTokenizer childTokens = new StringTokenizer(regionRoleVO.getKey(), ",");
				ArrayList regionchildren = new ArrayList();
				if (regionRoleVO.getChildren() != null)
				{
					regionchildren = regionRoleVO.getChildren();
				}
				int childLevel = childTokens.countTokens();
				if(childTokens.hasMoreTokens()){
					childTokens.nextToken();
					while (childTokens.hasMoreTokens())
					{
						
						if(childTokens.hasMoreTokens()){
							roleNames.add(childTokens.nextToken());
						}
					}
				}


				for (int regionCount = 0; regionCount < regionchildren.size(); regionCount++)
				{

					RoleVO districtRoleVO = (RoleVO) regionchildren.get(regionCount);
					String districttype = districtRoleVO.getType();
					StringTokenizer districtTokens = new StringTokenizer(districtRoleVO.getKey(), ",");
					ArrayList districtchildren = new ArrayList();
					if (districtRoleVO.getChildren() != null)
					{
						districtchildren = districtRoleVO.getChildren();
					}
					// tokens.nextToken();

					int districtLevel = districtTokens.countTokens();
					if(districtTokens.hasMoreTokens()){
						districtTokens.nextToken();
						while (districtTokens.hasMoreTokens())
						{
							roleNames.add(districtTokens.nextToken());
							
						}
					}

					for (int countLevel4 = 0; countLevel4 < districtchildren.size(); countLevel4++)
					{

						RoleVO level4RoleVO = (RoleVO) districtchildren.get(j);
						String level4type = level4RoleVO.getType();
						StringTokenizer level4Tokens = new StringTokenizer(level4RoleVO.getKey(), ",");
						ArrayList level4children = new ArrayList();
						if (level4RoleVO.getChildren() != null)
						{
							level4children = level4RoleVO.getChildren();
						}
						// while(tokens.hasMoreTokens()){
						// tokens.nextToken();

						int level4Level = level4Tokens.countTokens();
						if(level4Tokens.hasMoreTokens()){
							level4Tokens.nextToken();
							while (level4Tokens.hasMoreTokens())
							{
								roleNames.add(level4Tokens.nextToken());
								
							}
						}

					}
				}
			}
		}
		Iterator roleNamesIterator = roleNames.iterator();
		while (roleNamesIterator.hasNext())
		{
			roles.add(roleNamesIterator.next());
		}
		return roles;
	}
}

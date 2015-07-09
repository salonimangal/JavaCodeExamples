/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.mckesson.smora.database.dao.ItemDB;
import com.mckesson.smora.database.model.VCtlgItemSrch;
import com.mckesson.smora.exception.SMORAException;

/**
 * The Class ItemUtil.
 */
public class ItemUtil
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ItemUtil";
	/**
	 * The csv tokenizer.
	 */
	private StringTokenizer csvTokenizer = null;

	/**
	 * The data.
	 */
	private String data = null;

	/**
	 * This method parses the and validate item file.
	 * 
	 * @param csvItemList the csv item list
	 * 
	 * @return the array list< integer>
	 */
	public ArrayList<String> parseAndValidateItemFile(String csvItemList)
	{
		try{
		ArrayList<String> csvItem = new ArrayList<String>();
		// Start of changes QC-11206 (CSV file is not getting loaded for items in vertical format)
		//csvTokenizer = new StringTokenizer(csvItemList, ",");
	
		csvTokenizer = new StringTokenizer(csvItemList, "\n,");
		// End of changes QC-11206 (CSV file is not getting loaded for items in vertical format)
		while (csvTokenizer.hasMoreElements())
		{
			data = (String) csvTokenizer.nextElement();
			csvItem.add((data.trim()));
		}
		return csvItem;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method validates the items for user.
	 * 
	 * @param userId the user id
	 * @param itemList the item list
	 * 
	 * @return the array list
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public ArrayList validateItemsForUser(String userId, ArrayList itemList) throws SMORAException
	{
		ArrayList<String> validatedItemList = new ArrayList<String>();
		if (userId != null && itemList != null)
		{
			if (itemList.size() > 0)
			{
				ItemDB itemDB = new ItemDB();
				List<VCtlgItemSrch> itemViewList = itemDB.getItemsForUser(userId);

				for (int i = 0; i < itemViewList.size(); i++)
				{
					VCtlgItemSrch vCtlgItemSrch = (VCtlgItemSrch) itemViewList.get(i);
					  if (itemList.contains(vCtlgItemSrch.getEmItemNum()))
						{
							validatedItemList.add(vCtlgItemSrch.getEmItemNum());
						}
				}
			}
		}
		return validatedItemList;
	}
}

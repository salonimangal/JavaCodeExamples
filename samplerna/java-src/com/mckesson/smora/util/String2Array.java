/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.mckesson.smora.common.SMOConstant;

/**
 * The Class String2Array.
 */
public class String2Array
{

	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "String2Array";

	/**
	 * Str arr.
	 * 
	 * @param wordString the word string
	 * 
	 * @return the array list
	 */
	public static ArrayList strArr(String wordString)
	{
		ArrayList result = new ArrayList();
		StringTokenizer st;
		st = new StringTokenizer(wordString,",");
		while (st.hasMoreElements())
		{
			String x = st.nextElement().toString();
			result.add(x);
		}
		return result;
	}
	/**
	 * This method converts the to array list.
	 * 
	 * @param stringToConvert the string to convert
	 * 
	 * @return the array list< string>
	 */
	public static ArrayList<String> convertToArrayList(String stringToConvert)
	{
		ArrayList<String> returnList = null;
		StringTokenizer tokenizer = null;
		if (stringToConvert != null)
		{
			tokenizer = new StringTokenizer(stringToConvert, ",");
			while (tokenizer.hasMoreTokens())
			{
				if (returnList == null)
				{
					returnList = new ArrayList<String>();
				}
				returnList.add(tokenizer.nextToken());
			}
		}
		return returnList;
	}

	/**
	 * Str arr.
	 * 
	 * @param delimiter the delimiter
	 * @param wordString the word string
	 * 
	 * @return the array list
	 */
	public static ArrayList strArr(String wordString, String delimiter)
	{
		ArrayList result = new ArrayList();
		StringTokenizer st;
		if(wordString != null && delimiter != null)
		{
		st = new StringTokenizer(wordString, delimiter);
		while (st.hasMoreElements())
		{
			String x = st.nextElement().toString();
			result.add(x);
			}
		}
		return result;
	}
	
	/**
	 * Str split.
	 * 
	 * @param delimiter the delimiter
	 * @param wordString the word string
	 * 
	 * @return the array list
	 */
	public static ArrayList strSplit(String wordString, String delimiter)
	{
		ArrayList result = new ArrayList();
		String[] token = wordString.split(delimiter);
		for(int i = 0; i < token.length; i++)
		{
			result.add(token[i]);
		}
		return result;
	}
}
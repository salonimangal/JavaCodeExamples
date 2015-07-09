/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * The Class ReadArrayList.
 */
public class ReadArrayList
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ReadArrayList";

	/**
	 * This method reads from file.
	 * 
	 * @param inputStream the input stream
	 * 
	 * @return the list
	 * 
	 * @throws Exception the exception
	 */
	public static List readFromFile(InputStream inputStream) throws Exception
	{
		ObjectInput ois = null;
		List list = null;
		try
		{
			ois = new ObjectInputStream(inputStream);
		}
		catch (Exception e)
		{
			throw e;			
		}
		try
		{
			list = (List) ois.readObject();
		}
		catch (Exception e)
		{			
			throw e;
		}
		return list;
	}
}

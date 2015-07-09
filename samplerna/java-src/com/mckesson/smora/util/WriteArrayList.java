/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class WriteArrayList.
 */
public class WriteArrayList
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "WriteArrayList";

	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(WriteArrayList.class);

	/**
	 * This method writes to file.
	 * 
	 * @param list the list
	 * @param outputStream the output stream
	 * 
	 * @throws Exception the exception
	 */
	public static void writeToFile(List list, OutputStream outputStream) throws Exception
	{
		ObjectOutput oos = null;
		try
		{
			oos = new ObjectOutputStream(outputStream);
		}
		catch (Exception e)
		{
			log.error("Failed to initialize the ObjectOutputStream");
		}
		try
		{
			if (list != null)
			{
				oos.writeObject(list);
			}
			else
			{
				oos.writeObject("");
			}
			outputStream.close();
		}
		catch (Exception e)
		{
			log.error("Failed to write the Query Results to the specified file");
		}
	}
}

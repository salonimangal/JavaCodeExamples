/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class SMOFileProps.
 */
public class SMOFileProps
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "SMOFileProps";
	/**
	 * The file input stream.
	 */
	private static FileInputStream fileInputStream = null;
	/**
	 * The properties.
	 */
	private static Properties properties = null;
	
	public static String domainRoot = null;
	/**
	 * The log.
	 */
	protected static Log log = LogFactory.getLog(SMOFileProps.class);

	static
	{
		loadJMSProperties();
	}

	/**
	 * The Constructor.
	 */
	public SMOFileProps()
	{
	}

	/**
	 * This method loads the JMS properties.
	 */
	private static void loadJMSProperties()
	{
		try
		{
			log.debug("Loading the SMO Properties File");
			//Changes for WL 12 migration starts
			domainRoot = System.getProperty("domain.root");
			//fileInputStream = new FileInputStream("/WEB-INF/smora.properties");
			//Added for JBoss migration
			fileInputStream = (FileInputStream) SMOFileProps.class.getClassLoader().getResourceAsStream("smora.properties");
			//Changes for WL 12 migration Ends
			//fileInputStream = new FileInputStream("/web/app2/smornaDomain/properties/smora.properties");
			properties = new Properties();
			properties.load(fileInputStream);
			properties.list(System.out);
		}
		catch (FileNotFoundException e)
		{
			log.error("FileNotFoundException , The File smora.properties was not found in the specified path");
		}
		catch (IOException ioe)
		{
			log.error("IOException, as failure of Input/Output Operations on smora.properties file");
		}
		finally
		{
			try
			{
				if(null!= fileInputStream){

					fileInputStream.close();
				}
			}
			catch (IOException e1)
			{
				log.error("IOException caught as of closing the smora.properties file");
			}
		}
	}

	/**
	 * This method gets the file properties.
	 *
	 * @return the file properties
	 */
	public Properties getFileProperties()
	{
		return properties;
	}

}

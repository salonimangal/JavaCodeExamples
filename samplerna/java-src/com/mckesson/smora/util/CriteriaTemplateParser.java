/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import org.xml.sax.InputSource;

import com.mckesson.smora.exception.SMORAException;

/**
 * The Class CriteriaTemplateParser.
 */
public class CriteriaTemplateParser
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "CriteriaTemplateParser";
	/**
	 * The validator.
	 */
	private SchemaValidator validator = null;

	/**
	 * This method parses the and validate XML file.
	 * 
	 * @param xmlTemplate the xml template
	 * 
	 * @return true, if parse and validate XML file
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public boolean parseAndValidateXMLFile(InputSource xmlTemplate) throws SMORAException
	{
		validator = new SchemaValidator();
		return validator.validateXMLDocument(xmlTemplate);
	}
}

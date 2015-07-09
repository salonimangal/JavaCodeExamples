/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.mckesson.smora.exception.SMORAException;

/**
 * The Class SchemaValidator.
 */

public class SchemaValidator
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "SchemaValidator";
	/**
	 * This method validates the XML document.
	 * 
	 * @param xmlDocument the xml document
	 * 
	 * @return true, if validate XML document
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public boolean validateXMLDocument(InputSource xmlDocument) throws SMORAException
	{
		boolean xmlValidation = false;
		SAXParser parser = new SAXParser();
		Validator validator = new Validator();
		String xmlSchema = "/files/config/web-cfg/CriteriaSchema.xsd";
		try
		{
			parser.setFeature("http://xml.org/sax/features/validation", true);
			parser.setFeature("http://apache.org/xml/features/validation/schema", true);
			parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
			parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", xmlSchema);
		}
		catch (SAXNotRecognizedException e)
		{
			throw new SMORAException("SAXNotRecognizedException - Exception caught while setting the SAX Features", e);
		}
		catch (SAXNotSupportedException e)
		{
			throw new SMORAException("SAXNotSupportedException - Exception caught while setting the SAX Features", e);
		}
		try
		{
			parser.setErrorHandler(validator);
			parser.parse(xmlDocument);
		}
		catch (SAXException e)
		{
			throw new SMORAException("SAXException - Exception caught while parsing the XML File", e);
		}
		catch (IOException e)
		{
			throw new SMORAException("SAXException - IO Exception caught while parsing the XML File", e);
		}
		xmlValidation = validator.validationError;
		return !(xmlValidation);
	}
	

	/**
	 * The Class Validator.
	 */
	private class Validator extends DefaultHandler
	{
		/**
		 * The validation error.
		 */
		public boolean validationError = false;

		/**
		 * The sax parse exception.
		 */
		public SAXParseException saxParseException = null;

		/**
		 * Error.
		 * 
		 * @param exception the exception
		 * 
		 * @throws SAXException the SAX exception
		 */
		public void error(SAXParseException exception) throws SAXException
		{
			validationError = true;
			saxParseException = exception;
		}

		/**
		 * Fatal error.
		 * 
		 * @param exception the exception
		 * 
		 * @throws SAXException the SAX exception
		 */
		public void fatalError(SAXParseException exception) throws SAXException
		{
			validationError = true;
			saxParseException = exception;
		}

		/**
		 * Warning.
		 * 
		 * @param exception the exception
		 * 
		 * @throws SAXException the SAX exception
		 */
		public void warning(SAXParseException exception) throws SAXException
		{
		}
	}	
}
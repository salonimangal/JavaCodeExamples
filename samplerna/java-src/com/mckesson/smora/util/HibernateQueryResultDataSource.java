/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * The Class HibernateQueryResultDataSource.
 */
public class HibernateQueryResultDataSource implements JRDataSource
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "HibernateQueryResultDataSource";
	/**
	 * The fields.
	 */
	private String[] fields;
	/**
	 * The iterator.
	 */
	private Iterator iterator;
	/**
	 * The current value.
	 */
	private Object currentValue;

	/**
	 * The Constructor.
	 * 
	 * @param list the list
	 * @param fields the fields
	 */
	public HibernateQueryResultDataSource(List list, String[] fields)
	{
		this.fields = fields;
		this.iterator = list.iterator();
	}

	/**
	 * This method gets the field value.
	 * 
	 * @param field the field
	 * 
	 * @return the field value
	 * 
	 * @throws JRException the JR exception
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		Object value = null;
		// Change for Issue 2484 starts
		if (fields.length > 1)
		{
			int index = getFieldIndex(field.getName());
			if (index > -1)
			{
				Object[] values = (Object[]) currentValue;
				value = values[index];
			}
			return value;
		}
		else return currentValue;
		//Change for issue 2484 ends
	}

	/**
	 * Next.
	 * 
	 * @return true, if next
	 * 
	 * @throws JRException the JR exception
	 */
	public boolean next() throws JRException
	{
		currentValue = iterator.hasNext() ? iterator.next() : null;
		return (currentValue != null);
		
	}

	/**
	 * This method gets the field index.
	 * 
	 * @param field the field
	 * 
	 * @return the field index
	 */
	private int getFieldIndex(String field)
	{
		int index = -1;
		for (int i = 0; i < fields.length; i++)
		{
			if (fields[i].equals(field))
			{
				index = i;
				break;
			}
		}
		return index;
	}

}

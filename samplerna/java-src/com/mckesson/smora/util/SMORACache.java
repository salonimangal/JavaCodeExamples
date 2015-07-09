/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.HashMap;

import com.mckesson.smora.common.JMSConstants;
import com.mckesson.smora.database.dao.CriteriaAnalysisDB;
import com.mckesson.smora.database.dao.ReportQueueDB;
import com.mckesson.smora.database.dao.ReportTholdDB;
import com.mckesson.smora.exception.SMORAException;

/**
 * @author Vamsi.KJ
 *
 */
public class SMORACache
{
	private final String CLASSNAME = "SMORACache";
	private static SMORACache instance = null;
	
	/*
	 * private Constructor suppress.
	 */
	private SMORACache()
	{
	}
	
	/**
	 * 
	 * @return Returns the instance.
	 */
	public static SMORACache getInstance()
	{
		if(instance == null)
		{
			instance = new SMORACache();
		}
		return instance;
	}
	
	/**
	 * @return
	 * @throws SMORAException 
	 */
	public int getReportThresholdValue() throws SMORAException
	{
		return ReportTholdDB.getTholdValue();
	}
	
	/**
	 * @return
	 * @throws SMORAException 
	 */
	public HashMap getQueueThreshold() throws SMORAException
	{
		return (HashMap) ReportQueueDB.getReportQueue();
	}
	
	/**
	 * @return
	 * @throws SMORAException 
	 */
	/*  Modified<<return type changed>> for Issue# 437 by Infosys(Amit)-tjvobaf */
	//public long getAccountThresholdValue() throws SMORAException 
    public double getAccountThresholdValue() throws SMORAException 
	{
		return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.ACCOUNTS_ANAL_WEIGHTS);
	}

	/**
	 * @return
	 * @throws SMORAException 
	 */
    /*  Modified<<return type changed>> for Issue# 437 by Infosys(Amit)-tjvobaf */
    //public long getItemsThresholdValue() throws SMORAException
    public double getItemsThresholdValue() throws SMORAException
	{
		return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.ITEM_ANAL_WEIGHTS);		
	}
	
	/**
	 * 
	 * @return
	 * @throws SMORAException
	 */
    /*  Modified<<return type changed>> for Issue# 437 by Infosys(Amit)-tjvobaf */
    //public long getSuppliersThresholdValue() throws SMORAException
     public double getSuppliersThresholdValue() throws SMORAException
	{
		return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.SUPPLIERS_ANAL_WEIGHTS);		
	}
	
	/**
	 * 
	 * @return
	 * @throws SMORAException
	 */
     /*  Modified<<return type changed>> for Issue# 437 by Infosys(Amit)-tjvobaf */
     //public long getFieldThresholdValue() throws SMORAException
     public double getFieldThresholdValue() throws SMORAException  
	{
		return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.FIELDS_ANAL_WEIGHTS);
	}

	/**
	 * @return
	 * @throws SMORAException 
	 */
     /*  Modified<<return type changed>> for Issue# 437 by Infosys(Amit)-tjvobaf */
    //public long getUserCalcsThresholdValue() throws SMORAException 
    	public double getUserCalcsThresholdValue() throws SMORAException 
	{
		return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.USER_CALC_ANAL_WEIGHTS);		
	}

	/**
	 * @return
	 * @throws SMORAException 
	 */
    /*  Modified<<return type changed>> for Issue# 437 by Infosys(Amit)-tjvobaf */	
    //public long getTimePeriodThresholdValue() throws SMORAException 
    public double getTimePeriodThresholdValue() throws SMORAException 
	{
		return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.TIME_ANAL_WEIGHTS);
	}

	 /* Method added for Issue# 437 by Infosys(Amit)-tjvobaf */
	    /**
		 * @return
		 * @throws SMORAException
		 */
	    public double getTableJoinThresholdValue() throws SMORAException
		{
			return CriteriaAnalysisDB.getCriteriaAnalysisWeight().get(JMSConstants.TABLE_JOIN_ANAL_WEIGHTS);
		}
    /* End of method addition for Issue# 437 by Infosys(Amit)-tjvobaf */
}

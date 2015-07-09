/**
 * 
 */
package com.mckesson.smora.util;

/**
 * @author Anil Kumar
 *
 */
public class SapPricingUtil {

	// Process ID for SAP request
	String processId;
	
	//
	String processType;
	
	// Current or Previous Price Date
	String priceDateType;
	
	// Previous Price Date
	String date;

	/**
	 * 
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the priceDateType
	 */
	public String getPriceDateType() {
		return priceDateType;
	}

	/**
	 * @param priceDateType the priceDateType to set
	 */
	public void setPriceDateType(String priceDateType) {
		this.priceDateType = priceDateType;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}
}

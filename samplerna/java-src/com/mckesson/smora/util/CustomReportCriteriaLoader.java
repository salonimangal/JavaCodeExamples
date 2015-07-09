/*
 * Copyright(c) 20012 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.database.dao.AccountGroupDB;
import com.mckesson.smora.database.dao.CriteriaTemplateDB;
import com.mckesson.smora.database.dao.CustomReportFieldsDB;
import com.mckesson.smora.database.dao.ItemGroupDB;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.UserLoginForm;

/**
 * LoadReportStatusHelperThread
 * <p>
 * LoadReportStatusHelperThread will fetch all the informations for access level and user specific objects.
 * 
 *   
 * <br>
 * </p>
 * @author Kumar.Vishal
 */
public class CustomReportCriteriaLoader extends Thread {
	
	private static  Log log = LogFactory.getLog(CustomReportCriteriaLoader.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "CustomReportCriteriaLoader";
	
	private UserLoginForm userLoginForm;
	
	HttpServletRequest request;

	@Override
	public void run() {
		try {
			this.custRptCrtLoad();
		} catch (SMORAException e) {
			log.error("CustomReportCriteriaLoader: Error custRptCrtLoad method call.."+ e.getStackTrace());
		}
		
	}
	
	/**
	 * Method is used to Load the selection criteria of user
	 * 
	 * @throws SMORAException
	 */
	public void custRptCrtLoad() throws SMORAException{
		final String METHODNAME = "custRptCrtLoad";
		log.info("Inside custRptCrtLoad method of CustomReportCriteriaLoader..");
		HttpSession session = null;
		session = this.getRequest().getSession();
		String userID = (String) session.getAttribute("userID");
		// class objects
		CustomReportFieldsDB customReportFieldsDB = new CustomReportFieldsDB();
		CriteriaTemplateDB templateDB = new CriteriaTemplateDB();
		AccountGroupDB accountGroupDB = new AccountGroupDB(); 
		ItemGroupDB itemGroupDB = new ItemGroupDB();
		FieldsListVO fieldsListVO = new FieldsListVO(); 
		List criteriaTemplatesDB = null;
		ArrayList accountDataList = null;
		Boolean isIDBUser =false;
		ArrayList itemGroupsList = null;
		
		userLoginForm = this.getUserLoginForm();
		
		try {
			fieldsListVO = customReportFieldsDB.getCustomReportFields(userID);
			criteriaTemplatesDB	= templateDB.getCustomTemplatesForUser(userID);
			accountDataList = accountGroupDB.getAccountGroups(userID);
			isIDBUser = customReportFieldsDB.isIDBUser(userID);
			itemGroupsList = itemGroupDB.getItemGroups(userID);
					
			
		} catch (SMORAException e) {
			log.error("ReportStatusLoader: Error custRptCrtLoad method call.."+ e.getStackTrace());
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
		session.setAttribute("fieldsListVO", fieldsListVO);
		session.setAttribute("criteriaTemplatesList",criteriaTemplatesDB);
		session.setAttribute("accountDataList", accountDataList);
		session.setAttribute("isIDBUser", isIDBUser);
		session.setAttribute("itemGroupsList", itemGroupsList);
	}

	/**
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return
	 */
	public UserLoginForm getUserLoginForm() {
		return userLoginForm;
	}

	/**
	 * @return
	 */
	public void setUserLoginForm(UserLoginForm userLoginForm) {
		this.userLoginForm = userLoginForm;
	}

}

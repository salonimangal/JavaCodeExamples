/*
 * Copyright(c) 20012 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.appl.mdb.ReportsProcessorBean;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.LoginDB;
import com.mckesson.smora.database.dao.ReportStatusDB;
import com.mckesson.smora.dto.ReportStatusVO;
import com.mckesson.smora.exception.SMORAException;
import com.mckesson.smora.ui.form.UserLoginForm;

/**
 * LoadReportStatusHelperThread
 * <p>
 * LoadReportStatusHelperThread will fetch all the information like report status and 
 * user access for the last calendar month and put in session
 * <br>
 * </p>
 * @author Kumar.Vishal
 */
public class ReportStatusLoader extends Thread{
	
	private static  Log log = LogFactory.getLog(ReportStatusLoader.class);
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ReportStatusLoader";
	
	private int completedReportsDisplay;
	
	/**
	 * The inprogress reports display.
	 */
	private int inprogressReportsDisplay;
	
	/**
	 * The submitted reports display.
	 */
	private int submittedReportsDisplay;	
	
	private UserLoginForm userLoginForm;
	
	HttpServletRequest request;
	
	@Override
	public void run() {
		try {
			this.loadStatusDtl();
		} catch (SMORAException e) {
			log.error("ReportStatusLoader: Error loadStatusDtl method call.."+ e.getStackTrace());
		}
		
	}
	
	
	/**
	 * load the report status details for customer
	 */
	public void loadStatusDtl() throws SMORAException{
		final String METHODNAME = "loadStatusDtl";
		
		HttpSession session = null;
		session = this.getRequest().getSession();
		String userID = (String) session.getAttribute("userID");
		ReportsProcessorBean processorBean = new ReportsProcessorBean();
		ArrayList<ReportStatusVO> submittedReports = null;
		ArrayList<ReportStatusVO> inProgressReports = null;
		ArrayList<ReportStatusVO> completedReports = null;
		HashMap<String, ArrayList<ReportStatusVO>> reportsMap = null;
		
		
		ReportStatusDB statusDB = new ReportStatusDB();
		try {
			reportsMap = statusDB.getAllReports(userID);
			submittedReports = reportsMap.get(ReportManagerConstants.SUBMITTED);
			inProgressReports = reportsMap.get(ReportManagerConstants.REPORT_PROCESSING);
			completedReports = reportsMap.get(ReportManagerConstants.REPORT_REQUEST_COMPLETED);
			userLoginForm = (UserLoginForm)session.getAttribute("userLoginForm");
			userLoginForm.setSubmittedReports(submittedReports);
			userLoginForm.setInProgressReports(inProgressReports);
			userLoginForm.setCompletedReports(completedReports);
			userLoginForm.setSubmittedReportsAsCount(submittedReports.size());
			userLoginForm.setCompletedReportsAsCount(completedReports.size());
			userLoginForm.setInProgressReportsAsCount(inProgressReports.size());
			submittedReportsDisplay = userLoginForm.getSubmittedReportsAsCount();
			boolean userSapFlag=processorBean.getUserSapFlag(userID);
			boolean globalSapFlag=processorBean.getGlobalSapFlag();
			session.setAttribute("submittedReportsDisplay", submittedReportsDisplay);
			inprogressReportsDisplay = userLoginForm.getInProgressReportsAsCount();
			session.setAttribute("inprogressReportsDisplay", inprogressReportsDisplay);
			completedReportsDisplay = userLoginForm.getCompletedReportsAsCount();
			session.setAttribute("completedReportsDisplay", completedReportsDisplay);
			session.setAttribute("userLoginForm", userLoginForm);
			session.setAttribute("reportsMap",reportsMap);
			session.setAttribute("userSapFlag", userSapFlag);
			session.setAttribute("globalSapFlag", globalSapFlag);
		} catch (SMORAException e) {
			log.error("ReportStatusLoader: Error loadStatusDtl method call.."+ e.getStackTrace());
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}

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

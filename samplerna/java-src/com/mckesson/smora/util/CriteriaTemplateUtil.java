/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.Hibernate;
import org.xml.sax.InputSource;

import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AdvancedFilterDB;
import com.mckesson.smora.database.dao.CriteriaTemplateDB;
import com.mckesson.smora.database.dao.CustomReportFieldsDB;
import com.mckesson.smora.database.dao.ItemDB;
import com.mckesson.smora.database.dao.ReportStatusDB;
import com.mckesson.smora.dto.AccountDetailsListVO;
import com.mckesson.smora.dto.AccountDetailsVO;
import com.mckesson.smora.dto.AdvancedFiltersVO;
import com.mckesson.smora.dto.CustReportCriteriaVO;
import com.mckesson.smora.dto.CustomReportVO;
import com.mckesson.smora.dto.CustomerVO;
import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;
import com.mckesson.smora.dto.ItemDetailsVO;
import com.mckesson.smora.dto.ItemNumbersListVO;
import com.mckesson.smora.dto.ItemVO;
import com.mckesson.smora.dto.LocalDeptListVO;
import com.mckesson.smora.dto.ReportBaseVO;
import com.mckesson.smora.dto.UserRolesVO;
import com.mckesson.smora.exception.SMORAException;

/**
 * The Class CriteriaTemplateUtil.
 */
public class CriteriaTemplateUtil
{
	/**
	 * The xml.
	 */
	private static String xml = null;
	/**
	 * The mapping.
	 */
	private static Mapping mapping = null;
	/**
	 * The in stream.
	 */
	private static InputStream inStream = null;
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(CriteriaTemplateUtil.class);

	/**
	 * This method converts the VO to XML.
	 * 
	 * @param reportBaseVO the report base VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String convertVOToXML(ReportBaseVO reportBaseVO) throws SMORAException
	{
		try
		{
			xml = CriteriaTemplateUtil.marshall(reportBaseVO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SMORAException("Exception Raised while Converting the VO Object to XML");
		}
		return xml;
	}

	/**
	 * This method converts the XML to VO.
	 * 
	 * @param xml the xml
	 * 
	 * @return the report base VO
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static ReportBaseVO convertXMLToVO(String xml) throws SMORAException
	{
		ReportBaseVO objectVO = null;
		try
		{
			objectVO = (ReportBaseVO) CriteriaTemplateUtil.unMarshall(xml);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SMORAException("Exception Raised while Converting the XML  to VO Object");
		}
		return objectVO;
	}

	/**
	 * This method removes the invalid accounts and items.
	 * 
	 * @param userID the user ID
	 * @param xml the xml
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String removeInvalidAccountsAndItems(String xml, String userID) throws SMORAException
	{
		AccountUtil accountUtil = new AccountUtil();
		ReportBaseVO reportBaseVO = convertXMLToVO(xml);
		if(reportBaseVO != null)
		{
			CustomReportVO customReportVO = reportBaseVO.getCustomReportVO();
			if(customReportVO != null)
			{
				CustReportCriteriaVO custReportCriteriaVO = customReportVO.getCustReportCriteriaVO();
				if(custReportCriteriaVO != null)
				{
					ItemVO itemVO = custReportCriteriaVO.getItemVO();
					if(itemVO != null)
					{
						ItemNumbersListVO itemNumberListVO = itemVO.getItemNumbersList();
						if(itemNumberListVO != null)
						{	
							ArrayList<ItemDetailsVO> itemDetailsVOList = itemNumberListVO.getItemNumberList();
							if(itemDetailsVOList!=null && itemDetailsVOList.size()>0)
							{
								itemDetailsVOList = (ArrayList) validateItemNumber(itemDetailsVOList, userID);
								itemNumberListVO.setItemNumberList(itemDetailsVOList);
								itemVO.setItemNumbersList(itemNumberListVO);
								custReportCriteriaVO.setItemVO(itemVO);
							}
						}
					}
					CustomerVO customerVO = custReportCriteriaVO.getCustomerVO();
					AccountDetailsListVO accountDetailsListVO = customerVO.getAccountDetailsListVO();
					UserRolesVO userRolesVO = customerVO.getRoles();
					if(accountDetailsListVO!=null
					&&accountDetailsListVO.getAccountDetailsVOList()!=null
					&&accountDetailsListVO.getAccountDetailsVOList().size()>0)
					{
						ArrayList accounts = accountDetailsListVO.getAccountDetailsVOList();
						if(accounts!=null)
						{
							ArrayList accountNumbers = new ArrayList();
							Iterator accountIterator = accounts.iterator();
							while (accountIterator.hasNext())
							{
								AccountDetailsVO accountDetailsVO = (AccountDetailsVO) accountIterator.next();
								accountNumbers.add(accountDetailsVO.getAccountNum());
							}
							accounts = (ArrayList) accountUtil.validateUserAccounts(accountNumbers, userID);
							accountDetailsListVO.setAccountDetailsVOList(accounts);
							customerVO.setAccountDetailsListVO(accountDetailsListVO);
							custReportCriteriaVO.setCustomerVO(customerVO);
							
						}
					}else if(userRolesVO!=null
							&&((userRolesVO.getHspRoles()!=null&&userRolesVO.getHspRoles().size()>0)
							||(userRolesVO.getChnRoles()!=null&&userRolesVO.getChnRoles().size()>0)
							||(userRolesVO.getSlsRoles()!=null&&userRolesVO.getSlsRoles().size()>0)
							||(userRolesVO.getCidRoles()!=null&&userRolesVO.getCidRoles().size()>0))
							){
						userRolesVO = accountUtil.validateUserAccounts(userRolesVO, userID);
						customerVO.setRoles(userRolesVO);
						custReportCriteriaVO.setCustomerVO(customerVO);
					}
					customReportVO.setCustReportCriteriaVO(custReportCriteriaVO);
					reportBaseVO.setCustomReportVO(customReportVO);
				}
			}
	 
			String xmlData = convertVOToXML(reportBaseVO);
			return xmlData;
	   }
	   else
		{
		   return "";
		}
	}
	
	public static String removeInvalidFieldSelection(String xml, String userID) throws SMORAException
	{
		AccountUtil accountUtil = new AccountUtil();
		ReportBaseVO reportBaseVO = convertXMLToVO(xml);
		if(reportBaseVO != null)
		{
			CustomReportVO customReportVO = reportBaseVO.getCustomReportVO();
			FieldsListVO fieldsListVO = new FieldsListVO();
			ArrayList<FieldsVO> fieldVOList = new ArrayList<FieldsVO>();
			ArrayList<String> sequenceFields = new ArrayList<String>();
			//QC-11678:MHS is not uploading
			if(customReportVO != null &&
			!xml.contains("MHS_CRITERIA")){
				CustReportCriteriaVO custReportCriteriaVO = customReportVO.getCustReportCriteriaVO();
				if(custReportCriteriaVO != null)
				{
					fieldsListVO = custReportCriteriaVO.getFieldsVOList();
					if(fieldsListVO != null&&fieldsListVO.getFieldsVOList()!=null&&fieldsListVO.getFieldsVOList().size()>0)
					{
						fieldVOList = fieldsListVO.getFieldsVOList();
						sequenceFields = custReportCriteriaVO.getSequenceFields();
						fieldVOList = (ArrayList) validateFieldList(fieldVOList, userID);
						sequenceFields = (ArrayList)validateSequenceList(sequenceFields, userID);
					}
				}
				fieldsListVO.setFieldsVOList(fieldVOList);
				custReportCriteriaVO.setFieldsVOList(fieldsListVO);
				custReportCriteriaVO.setSequenceFields(sequenceFields);
				customReportVO.setCustReportCriteriaVO(custReportCriteriaVO);
				reportBaseVO.setCustomReportVO(customReportVO);
			}
			
			String xmlData = convertVOToXML(reportBaseVO);
			return xmlData;
		}
		else
		{
		   return "";
		}
	}
	public static String removeInvalidLocalDepartment(String xml, String userID) throws SMORAException
	{
		AccountUtil accountUtil = new AccountUtil();
		ReportBaseVO reportBaseVO = convertXMLToVO(xml);
		if(reportBaseVO != null)
		{
			CustomReportVO customReportVO = reportBaseVO.getCustomReportVO();
			AdvancedFiltersVO advancedFiltersVO = new AdvancedFiltersVO();
			LocalDeptListVO localDeptListVO = null;
			ArrayList<String> localDeptList = new ArrayList<String>();
			//QC-11678:MHS is not uploading
			if(customReportVO != null &&
			!xml.contains("MHS_CRITERIA")){
				CustReportCriteriaVO custReportCriteriaVO = customReportVO.getCustReportCriteriaVO();
				if(custReportCriteriaVO != null)
				{
					advancedFiltersVO = custReportCriteriaVO.getAdvancedFiltersVO();
					if(advancedFiltersVO != null&&advancedFiltersVO.getLocalDeptList()!=null)
					{
						localDeptListVO = advancedFiltersVO.getLocalDeptList();
						if(localDeptListVO!=null&&localDeptListVO.getLocalDeptList()!=null
						&&localDeptListVO.getLocalDeptList().size()>0){
							localDeptList = localDeptListVO.getLocalDeptList();
							localDeptList = validateLocalDepartment(localDeptList, userID);
							localDeptListVO.setLocalDeptList(localDeptList);
						}
						
					}
				}
				advancedFiltersVO.setLocalDeptList(localDeptListVO);
				custReportCriteriaVO.setAdvancedFiltersVO(advancedFiltersVO);
				customReportVO.setCustReportCriteriaVO(custReportCriteriaVO);
				reportBaseVO.setCustomReportVO(customReportVO);
			}
			
			String xmlData = convertVOToXML(reportBaseVO);
			return xmlData;
		}
		else
		{
		   return "";
		}
	}
	
	public static ArrayList validateFieldList(List fieldVOList, String userId) throws SMORAException
	{
		ArrayList<FieldsVO> returnList = null;
		if (fieldVOList != null && userId != null)
		{
			if (fieldVOList.size() > 0)
			{
				ArrayList<String> fieldNameList = new ArrayList<String>();
				for(int i = 0; i < fieldVOList.size(); i++){
					fieldNameList.add(((FieldsVO)fieldVOList.get(i)).getFieldName());
				}
				returnList = new ArrayList<FieldsVO>();
				CustomReportFieldsDB customReportFieldsDB = new CustomReportFieldsDB();
				FieldsListVO fieldsListVO = customReportFieldsDB.getCustomReportFields(userId);
				
				if(fieldsListVO!=null && fieldsListVO.getFieldsVOList()!=null && fieldsListVO.getFieldsVOList().size()>0)
				{
					ArrayList<FieldsVO> fieldsVOList = fieldsListVO.getFieldsVOList();
					for (int i = 0; i < fieldsVOList.size(); i++)
					{
						FieldsVO fieldsVO = (FieldsVO) fieldsVOList.get(i);
						String fieldName = fieldsVO.getFieldName();
						int fieldID = fieldsVO.getFldId();
						//Added for SO-3806 SynerGx Rebranding - If block to support Field Name Change in old templates
						if((fieldNameList.contains(ReportManagerConstants.FLD_NAM_GNRC_RBT)	&& fieldID == ReportManagerConstants.FLD_ID_GNRC_RBT)
						|| (fieldNameList.contains(ReportManagerConstants.FLD_NAM_GNRC_IND)	&& fieldID == ReportManagerConstants.FLD_ID_GNRC_IND)
						|| (fieldNameList.contains(ReportManagerConstants.FIELD_NAM_CUST_ITEM_DEPT_ID) && fieldID == ReportManagerConstants.FIELD_CUST_ITEM_DEPT_ID) //Added for QC-1150910052 - If condition to support Field Name Change in old templates
						|| fieldNameList.contains(fieldName))
						{
							fieldsVO = new FieldsVO();
							fieldsVO.setFldId(fieldID);
							fieldsVO.setFieldName(fieldName);
							returnList.add(fieldsVO);
						}
					}
				}
			}
		}
		return returnList;
	}
	
	public static ArrayList validateSequenceList(List sequenceFields, String userId) throws SMORAException
	{
		ArrayList<String> returnList = null;
		if (sequenceFields != null && userId != null)
		{
			if (sequenceFields.size() > 0)
			{
				returnList = new ArrayList<String>();
				CustomReportFieldsDB customReportFieldsDB = new CustomReportFieldsDB();
				FieldsListVO fieldsListVO = customReportFieldsDB.getCustomReportFields(userId);
				
				if(fieldsListVO!=null && fieldsListVO.getFieldsVOList()!=null && fieldsListVO.getFieldsVOList().size()>0)
				{
					ArrayList<FieldsVO> fieldsVOList = fieldsListVO.getFieldsVOList();
					ArrayList<String> fieldsName = new ArrayList<String>();
					for (int i = 0; i < fieldsVOList.size(); i++)
					{
						FieldsVO fieldsVO = (FieldsVO) fieldsVOList.get(i);
						fieldsName.add(fieldsVO.getFieldName());
					
					}
					for (int i = 0; i < sequenceFields.size(); i++)
					{
						
						if (fieldsName.contains((String)sequenceFields.get(i)))
						{
							returnList.add((String)sequenceFields.get(i));
						}
					}
				}
			}
		}
		return returnList;
	}
	public static ArrayList validateItemNumber(List itemDetailsVOList, String userId) throws SMORAException
	{
		ArrayList<String> returnList = null;
		ArrayList<String> itemNumber = new ArrayList<String>();
		for(int i = 0; i < itemDetailsVOList.size(); i++){
			itemNumber.add(((ItemDetailsVO)itemDetailsVOList.get(i)).getItemNum());
		}
		ItemDB itemDB = new ItemDB();
		returnList = (ArrayList)itemDB.searchItemDetails(itemNumber, true, userId);
		return returnList;
	}
	public static ArrayList validateLocalDepartment(List xmlLocalDeptList, String userId) throws SMORAException
	{
		ArrayList<String> localDeptList = new ArrayList<String>();
		if(xmlLocalDeptList!=null&&xmlLocalDeptList.size()>0){
			AdvancedFilterDB advancedFilterDB = new AdvancedFilterDB();
			LocalDeptListVO localDeptListVO= advancedFilterDB.getLocalDeptList(userId, null);
			if(localDeptListVO!=null){
				localDeptList = localDeptListVO.getLocalDeptList();
			}
			String localDept;
			for(int i = 0; i < xmlLocalDeptList.size(); i++){
				
				localDept = (String)xmlLocalDeptList.get(i);
				if(!(localDeptList.contains(localDept))){
					xmlLocalDeptList.remove(i);
					i--;
				}
			
			}
		}
		return (ArrayList)xmlLocalDeptList;
	}
	public static BigDecimal getReportType(String xmlData)throws SMORAException{
		
		ReportBaseVO reportBaseVO= (ReportBaseVO)convertXMLToVO(xmlData);
		return new BigDecimal(reportBaseVO.getReportSubtype());
			
	}
	/**
	 * Marshall.
	 * 
	 * @param obj the obj
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	private static String marshall(Object obj) throws SMORAException
	{
		String xml = null;
		try
		{
			Mapping mapping = new Mapping();
			inStream = CriteriaTemplateUtil.class.getClassLoader().getResourceAsStream("CriteriaMapping.xml");
			InputSource is = new InputSource(inStream);
			mapping.loadMapping(is);
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = new Marshaller(stringWriter);
			marshaller.setMapping(mapping);
			marshaller.marshal(obj);
			xml = stringWriter.toString();
		}
		catch (FileNotFoundException e)
		{
			log.error("CriteriaMapping.xml is not found in the specified path");
			e.printStackTrace();
			throw new SMORAException("CriteriaMapping.xml is not found in the specified path", e);
		}
		catch (IOException e)
		{
			log.error("IOException caught for CriteriaMapping.xml file");
			e.printStackTrace();
			throw new SMORAException("IOException caught for CriteriaMapping.xml file", e);
		}
		catch (MappingException e)
		{
			log.error("Mapping Exception for Criteria Mapping");
			e.printStackTrace();
			throw new SMORAException("Mapping Exception for Criteria Mapping", e);
		}
		catch (MarshalException e)
		{
			log.error("Marshal Exception for Criteria Mapping");
			e.printStackTrace();
			throw new SMORAException("Marshal Exception for Criteria Mapping", e);
		}
		catch (ValidationException e)
		{
			log.error("Validation Exception for Criteria Mapping");
			e.printStackTrace();
			throw new SMORAException("Validation Exception for Criteria Mapping", e);
		}
		return xml;
	}

	/**
	 * Un marshall.
	 * 
	 * @param xml the xml
	 * 
	 * @return the object
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static Object unMarshall(String xml) throws SMORAException
	{
		ReportBaseVO reportBaseVO = null;
		try
		{
			Mapping mapping = new Mapping();
			inStream = CriteriaTemplateUtil.class.getClassLoader().getResourceAsStream("CriteriaMapping.xml");
			InputSource is = new InputSource(inStream);
			mapping.loadMapping(is);
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setMapping(mapping);
			reportBaseVO = (ReportBaseVO) unmarshaller.unmarshal(new StringReader(xml));
		}
		catch (FileNotFoundException e)
		{
			log.error("CriteriaMapping.xml is not found in the specified path");
			e.printStackTrace();
			throw new SMORAException("CriteriaMapping.xml is not found in the specified path", e);

		}
		catch (IOException e)
		{
			log.error("IOException caught for CriteriaMapping.xml file");
			e.printStackTrace();
			throw new SMORAException("IOException caught for CriteriaMapping.xml file", e);
		}
		catch (MappingException e)
		{
			log.error("Mapping Exception for Criteria Mapping");
			e.printStackTrace();
			throw new SMORAException("Mapping Exception for Criteria Mapping", e);
		}
		catch (MarshalException e)
		{
			log.error("Marshal Exception for Criteria Mapping");
			e.printStackTrace();
			throw new SMORAException("Marshal Exception for Criteria Mapping", e);
		}
		catch (ValidationException e)
		{
			log.error("Validation Exception for Criteria Mapping");
			e.printStackTrace();
			throw new SMORAException("Validation Exception for Criteria Mapping", e);
		}
		return reportBaseVO;
	}

	/**
	 * This method converts the account data TOXML.
	 * 
	 * @param userRolesVO the user roles VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String convertAccountDataTOXML(Object userRolesVO) throws SMORAException
	{
		try
		{
			xml = CriteriaTemplateUtil.marshall(userRolesVO);
		}
		catch (Exception e)
		{
			throw new SMORAException("Exception Raised while Converting the VO Object to XML");
		}
		return xml;
	}

	/**
	 * This method converts the supplier VO to XML.
	 * 
	 * @param userRolesVO the user roles VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String convertSupplierVOToXML(Object userRolesVO) throws SMORAException
	{
		try
		{
			xml = CriteriaTemplateUtil.marshall(userRolesVO);
		}
		catch (Exception e)
		{
			throw new SMORAException("Exception Raised while Converting the VO Object to XML");
		}
		return xml;
	}

	/**
	 * This method converts the generic VO to XML.
	 * 
	 * @param genericVO the generic VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */

	public static String convertGenericVOToXML(ArrayList genericVO) throws SMORAException
	{
		try
		{
			xml = CriteriaTemplateUtil.marshall(genericVO);
		}
		catch (Exception e)
		{
			throw new SMORAException("Exception Raised while Converting the VO Object to XML");
		}
		return xml;
	}

	/**
	 * This method converts the item details VO to XML.
	 * 
	 * @param itemDetailsVO the item details VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String convertItemDetailsVOToXML(Object itemDetailsVO) throws SMORAException
	{
		try
		{
			xml = CriteriaTemplateUtil.marshall(itemDetailsVO);
		}
		catch (Exception e)
		{
			throw new SMORAException("Exception Raised while Converting the VO Object to XML");
		}
		return xml;
	}

	/**
	 * This method converts the account details VO to XML.
	 * 
	 * @param accountDetailsVO the account details VO
	 * 
	 * @return the string
	 * 
	 * @throws SMORAException the SMORA exception
	 */
	public static String convertAccountDetailsVOToXML(Object accountDetailsVO) throws SMORAException
	{
		try
		{
			xml = CriteriaTemplateUtil.marshall(accountDetailsVO);
		}
		catch (Exception e)
		{
			throw new SMORAException("Exception Raised while Converting the VO Object to XML");
		}
		return xml;
	}

	public static String[] getActionMethodName(int reportType) throws SMORAException
	{
			final String METHODNAME = "getActionMethodName";
			String methodName[] = new String[2];
			ReportStatusDB reportStatusDB = new ReportStatusDB();
			try
			{

				if(reportType == ReportManagerConstants.CUSTOM_REPORT)
				{
					methodName[0]="LoadCriteriaSelectionAction";
					methodName[1]="loadCriteriaSelection";
				}
			else if(reportType == ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GROUP
					||reportType == ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_CODE
					||reportType == ReportManagerConstants.REPORT_DESCENDING_DOLLAR_GENERIC_DESCRIPTION
					||reportType == ReportManagerConstants.REPORT_DESCENDING_DOLLAR_ITEM_CODE
					||reportType == ReportManagerConstants.REPORT_DESCENDING_DOLLAR_THERAPEUTIC )
				{
					methodName[0]="DescendingDollarAction";
					methodName[1]="descendingDollarLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES || reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES_DESC_BY_CATEGORY||reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES_CODE||reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERA_TIME_SERIES_DESC)
				{
					methodName[0]="UsageDetailTheraTimeSeriesAction";
					//QC-11509
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_GENERIC_USAGE || reportType == ReportManagerConstants.REPORT_GENERIC_USAGE_DESC_BY_CATEGORY ||reportType == ReportManagerConstants.REPORT_GENERIC_USAGE_GENERIC_CODE ||reportType == ReportManagerConstants.REPORT_GENERIC_USAGE_GENERIC_DESC )
				{
					methodName[0]="UsageDetailGenericAction";
					//QC-11509
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC || reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC_DESC_BY_CATEGORY ||reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC_CODE||reportType == ReportManagerConstants.REPORT_USAGE_DETAIL_THERAPEUTIC_DESC)
				{
					methodName[0]="UsageDetailTherapeuticAction";
					//QC-11509
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.TIMESERIES_TP_COMPARISON
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_GENERIC
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_THERA
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_ITEM
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_LOCAL_DEPT
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_DOLLAR_CHG_ONLY_ORDER_DEPT
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_GENERIC
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_THERA
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_ITEM
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_LOCAL_DEPT
						||reportType == ReportManagerConstants.REPORT_TIME_SERIES_TP_CMP_FULL_DTL_ORDER_DEPT )
				{
					methodName[0]="TimeSeriesTPComparisonAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_ITEM_INFORMATION)
				{
					methodName[0]="ReportItemInformationAction";
					//QC-11509
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_TIME_SERIES_SUMMARY_GROUP
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_SUMMARY
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_DESC_SUMMARY
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_THERA_SUMMARY
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_SUPP_SUMMARY
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_LCL_DEPT_SUMMARY
					|| reportType == ReportManagerConstants.REPORT_TIME_SERIES_ORD_DEPT_SUMMARY )
				{
					methodName[0]="TimeSeriesSummaryAction";
					methodName[1]="timeSeriesSummaryLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_CUSTOMER_PRICE_CPC)
				{
					methodName[0]="ReportCustomerPriceCPCAction";
					methodName[1]="customerPriceCPCLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_CUSTOMER_PRICE_IPC)
				{
					methodName[0]="ReportCustomerPriceIPCAction";
					methodName[1]="customerPriceIPCLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_MUS_CONTRACT_OMIT)
				{
					methodName[0]="MUSSummaryAction";
					methodName[1]="musLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_PREFER_RX_SAVINGS)
				{
					methodName[0]="PreferRxSavingsAction";
					methodName[1]="preferRxSavingsLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_SKY_SAVINGS)
				{
					methodName[0]="ReportSKYSavingOpportunityAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_CONTRACT_COMPLIANCE)
				{
					methodName[0]="ReportContractComplianceAction";
					methodName[1]="contractComplianceloadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_ACC_COMPARISON_ITEM)
				{
					methodName[0]="ReportAccComparisonISAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_ACC_COMPARISON_GENERIC)
				{
					methodName[0]="ReportAccComparisonGGAction";
					methodName[1]="loadCriteriaSelection";
				}

				else if(reportType == ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GROUP
						|| reportType == ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_DESCENDING_DOLLAR
						|| reportType == ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_CODE
						||reportType == ReportManagerConstants.REPORT_8020_GENERIC_CATEGORY_GENERIC_DESCRIPTION)
				{
					methodName[0]="Report8020GenericCategoryAction";
					methodName[1]="report8020LoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_8020_CONSOLIDATED_VIEW)
				{
					methodName[0]="Report8020ConsildatedViewAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_8020_DESCENDING_WITH_UNITS)
				{
					methodName[0]="Report8020DescendingWithUnitsViewAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_ASSET_MANAGEMENT_HIGH_FREQUENCY_ITEMS)
				{
					methodName[0]="AssetManagementHighFreqItemsAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.ASSETMANAGEMENT_OBSOLETE_INVENTORY)
				{
					methodName[0]="AssetManagementObsoleteInventoryAction";
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.PURCHASE_DRILL_DOWN_SUMMARY)
				{
					methodName[0]="PDDLoadCriteriaSelectionAction";
					methodName[1]="PDDLoadCriteriaSelection";
				}
				else if(reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_GROUP
						||reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_ACTUAl
						||reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_AWP
						||reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_QTY
						||reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_ACTUAl
						||reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_AWP
						||reportType ==ReportManagerConstants.REPORT_MARKET_SHARE_BY_GENERIC_BRKBY_QTY )
				{
					methodName[0]="ReportMarketShareByGenericNameAction";
					methodName[1]="MarketShareloadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.TIME_SERIES_REPORT_DETAIL
						|| reportType == ReportManagerConstants.TIME_SERIES_REPORT_DETAIL_DOLLAR
						|| reportType == ReportManagerConstants.TIME_SERIES_REPORT_DETAIL_BOTH)
				{
					methodName[0]="TimeSeriesReportDetailAction";
					methodName[1]="timeSeriesReportDetailLoadCriteriaSelection";
				}
				else if(reportType ==ReportManagerConstants.REPORT_TIME_SERIES_SUMMARY_GROUP ||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_ACCOUNT_SUMMARY ||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_SUMMARY||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_GENERIC_CODE_DESC_SUMMARY ||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_THERA_SUMMARY||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_SUPP_SUMMARY ||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_LCL_DEPT_SUMMARY ||
						reportType == ReportManagerConstants.REPORT_TIME_SERIES_ORD_DEPT_SUMMARY)
				{
					methodName[0]="TimeSeriesSummaryAction";
					methodName[1]="timeSeriesSummaryLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE_THERA)
				{
					methodName[0]="ReportPurchaseCostVarianceTherSumAction";
					//QC-11509
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_PURCHASE_COST_VAIRANCE)
				{
					methodName[0]="ReportPurchaseCostVarianceAction";
					//QC-11509
					methodName[1]="loadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY
						|| reportType == ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_ACROSS_ACCOUNT
						||reportType == ReportManagerConstants.REPORT_QUICK_ITEM_PURCH_HISTORY_SUMMARY_BY_ACCOUNT)
				{
					methodName[0]="ReportQuickItemPurchaseHistoryAction";
					methodName[1]="quickItemPurchaseHistoryLoadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_CONTROLLED_SUBSTANCE_GRP_ID)
				{
					methodName[0]="ReportControlledSubstanceAction";
					methodName[1]="loadCriteriaSelection";
				}else if(reportType == ReportManagerConstants.REPORT_RETURNS_ANALYSIS)
				{
					methodName[0]="ReportPerformanceManagementAction";
					//QC-11509
					//methodName[1]="loadCriteriaSelection";
					methodName[1]="PerformanceMgtloadCriteriaSelection&reportName="+ReportManagerConstants.REPORT_PERFORMANCE_RETURN_NAME;
				}
				else if(reportType == ReportManagerConstants.REPORT_PERFORMANCE_TOP200)
				{
					methodName[0]="ReportPerformanceManagementTop200Action";
					//QC-11509
					//methodName[1]="loadCriteriaSelection";
					methodName[1]="PerformanceManagementloadCriteriaSelection";
				}
				else if(reportType == ReportManagerConstants.REPORT_CREDIT_LESS_THAN_100_PERCENT)
				{
					methodName[0]="ReportPerformanceManagementAction";
					//QC-11509
					//methodName[1]="loadCriteriaSelection";
					methodName[1]="PerformanceMgtloadCriteriaSelection&reportName="+ReportManagerConstants.REPORT_PERFORMANCE_CREDIT_NAME;
				}
	            else if(reportType == ReportManagerConstants.REPORT_NEWLY_PURCHASED_ITEMS)
				{
					methodName[0]="ReportNewlyPurchasedItemsAction";
					methodName[1]="NewlyPurchasedItemsloadCriteriaSelection";
				}
	            else if(reportType == ReportManagerConstants.REPORT_ITEM_MOVEMENT)
				{
					methodName[0]="ReportItemMovementAction";
					methodName[1]="loadCriteriaSelection";
				}
	            else if(reportType == ReportManagerConstants.REPORT_FISCAL_YTD)
				{
					methodName[0]="FiscalYTDAction";
					methodName[1]="loadCriteriaSelection";
				}
	            else if(reportType == ReportManagerConstants.REPORT_QTRLY_DRUG_UTILIZATION_THERA || reportType == ReportManagerConstants.REPORT_QUATERLY_DRUG_UTILIZATION)
	            {
	            	methodName[0]="ReportQuarterlyDrugUtilizationAction";
					methodName[1]="reportQuarterlyDrugUtilization";
	            }
	            else if(reportType == ReportManagerConstants.FORECASE_REPORTS_DETAIL)
	            {
	            	methodName[0]="AssetManagement12MonthInventoryTurnsForecastDetailAction";
					methodName[1]="AssetMgt12MonthDetailloadCriteriaSelection&reportName="+ReportManagerConstants.REPORT_FORECAST_DT;
	            }
	            else if(reportType == ReportManagerConstants.FORECASE_REPORTS_SUMMARY)
	            {
	            	methodName[0]="AssetManagement12MonthInventoryTurnsForecastDetailAction";
					methodName[1]="AssetMgt12MonthDetailloadCriteriaSelection&reportName="+ReportManagerConstants.REPORT_FORECAST_SM;
	            }
				//PDR Changes
	            else if(reportType == ReportManagerConstants.REPORT_PURCHASE_DETAILS|| 
	            		reportType == ReportManagerConstants.REPORT_PURCHASE_DETAILS_ITEMLEVELACROSSACCOUNTS || 
	            		reportType == ReportManagerConstants.REPORT_PURCHASE_DETAILS_ACCOUNTLEVELWITHINITEMGROUPING ||
	            		reportType == ReportManagerConstants.REPORT_PURCHASE_DETAILS_ITEMLEVELWITHINACCOUNTGROUPING || 
	            		reportType == ReportManagerConstants.REPORT_PURCHASE_DETAILS_INVOICEDETAILLEVEL ){
	            	methodName[0]="ReportPurchasingDetailsAction";
					methodName[1]="loadCriteriaSelection";
	            }
				//QC-11509
	            else if(reportType == ReportManagerConstants.REPORT_MHS_CODE){
	            	methodName[0]="MHSLoadCriteriaSelectionAction";
					methodName[1]="mhsLoadCriteriaSelection";
	            }


			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new SMORAException (e, "CriteriaTemplateUtil",METHODNAME);
			}
			return methodName;
	}
/*
This method compares the the two template xml's.
* 
* @param 
* 
* @return the boolean
* 
* @throws SMORAException the SMORA exception
 */
	public static boolean compareTemplates(HttpSession session,ReportBaseVO reportBaseVO,String template) throws SMORAException{
		boolean templateCheck=false;
		final String METHODNAME = "compareTemplates";
		try{
			Object tempIdCheck=(Object) session.getAttribute ("loadTemplateSubmitCheck");
			if(tempIdCheck.toString()!=null && tempIdCheck.toString().equals("true")){
				String tempId=(String)session.getAttribute("loadTempID");
				String clobString="";
				if(tempId!=null){
					log.info("Checking the Template "+tempId+" whether the changes has been saved or not in a template  before submit");
					CriteriaTemplateDB templateDB = new CriteriaTemplateDB();
					clobString=templateDB.getTemplate(Integer.parseInt(tempId));
				}
				else{
					clobString=(String)session.getAttribute(template);
				}
				String xmlData = CriteriaTemplateUtil.convertVOToXML(reportBaseVO);
				String templateData=xmlData;
				Clob clob=Hibernate.createClob(clobString);
				StringBuffer strOut = new StringBuffer();
				String aux;
				String clobStr=null;
				if(clob != null)
				{
					BufferedReader br = new BufferedReader(clob.getCharacterStream());
					while ((aux=br.readLine())!=null)
					{
						strOut.append(aux);
					}
				}
				clobStr = strOut.toString();
				if(xmlData!=null){
					xmlData=xmlData.replaceAll("\""," ");
					xmlData=xmlData.trim();
					xmlData=xmlData.substring(xmlData.indexOf("<CRITERIA_INFO>"),xmlData.lastIndexOf("</CRITERIA_INFO>"));
				}
				if(clobStr!=null){
					clobStr=clobStr.replaceAll("\""," ");
					clobStr=clobStr.trim();
					clobStr=clobStr.substring(clobStr.indexOf("<CRITERIA_INFO>"),clobStr.lastIndexOf("</CRITERIA_INFO>"));
				}
				if((xmlData!=null && xmlData.compareTo(clobStr)==0) || (xmlData!=null && xmlData.contentEquals(clobStr)) || (xmlData!=null && xmlData.equalsIgnoreCase(clobStr))){
					log.info("There are no changes in the template which is being submitted by the user.");
					session.setAttribute("showMessage", "hide");
				}
				else{
					log.info("The template being submitted is changed and should be saved before submission.");
					templateCheck=true;
					if(session.getAttribute(template)!=null){
						session.removeAttribute(template);
						session.setAttribute(template,templateData);
					}
					else{
						session.setAttribute(template,templateData);
					}
					session.setAttribute("showMessage", "show");
				}
			}
			else{
				templateCheck=true;
				session.setAttribute("showMessage",null);
			}
		}
		catch (Exception e) {
			throw new SMORAException(e, "CriteriaTemplatesAction", METHODNAME);
		}
		
		return templateCheck ;
		
	}
}

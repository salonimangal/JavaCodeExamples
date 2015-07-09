/*package com.mckesson.smora.util;  commented by devesh


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ibm.www.xmlns.prod.websphere.j2ca.sap.sapftitemsout.SapFtItemsOut;
//import com.mckesson.smora.appl.sap.SapJCoClient;
import com.mckesson.smora.appl.sap.SapWsTemplate;
//import com.mckesson.smora.appl.util.DateSelectionUtil;
import com.mckesson.smora.appl.wsclient.InvokeInterface;
import com.mckesson.smora.common.ReportManagerConstants;
import com.mckesson.smora.database.dao.AccountDB;
import com.mckesson.smora.database.dao.ReportStatusDB;
//import com.mckesson.smora.database.model.TAcctItemPrc;
//import com.mckesson.smora.database.model.TAcctItemPrcId;
import com.mckesson.smora.database.model.TApplRptStat;
import com.mckesson.smora.database.model.TCmsEqItemPrc;
import com.mckesson.smora.database.model.TEqItemPrcId;
import com.mckesson.smora.database.util.HibernateTemplate;
import com.mckesson.smora.exception.SMORADatabaseException;
import com.mckesson.smora.exception.SMORAException;
//import com.sap.mw.jco.JCO;

public class PriceLoadBatch {

	private final static String className= "PriceLoadBatch";
	private static final Log log=LogFactory.getLog(PriceLoadBatch.class);


	/**
	 *
	 * @return strAcctQuery
	 * @throws SMORAException
	 * This method builds the accounts query to get the account number
	 * list.
	 */
	/*private String buildAccountsQuery() throws SMORAException            commented by devesh
	{
		final String METHOD_NAME="buildAccountsQuery";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		String strAcctQuery = null;
		try
		{
			strAcctQuery.concat(" SELECT distinct CPAE.Acct_Num FROM L_CMS_PSBL_ALT_EQ_ITEM CPAE ");
		}

		catch (Exception e)
		{
			String ERROR = "Failed to generate the account query for the report request :: Exception occured in "+METHOD_NAME+": "+e;
			log.error(ERROR);
			throw new SMORAException(ERROR);
		}

	  return strAcctQuery;
	}



	/**
	 *
	 * @return strQuery
	 * @throws SMORAException
	 * This method builds the items query to get the item number
	 * list for which pricing details is required.
	 */
	/*private String buildItemsQuery() throws SMORAException          commented by devesh
	{
		final String METHOD_NAME="buildItemsQuery";
		log.info("INSIDE METHOD_NAME " + METHOD_NAME);
		String strQuery = null;
		try
		{
			strQuery.concat(" SELECT distinct CPAE.ALT_EQ_ITEM_NUM FROM L_CMS_PSBL_ALT_EQ_ITEM CPAE ");
			strQuery.concat(" WHERE CPAE.CUST_ACCT_ID = :cust_acct_id");
		}

		catch (Exception e)
		{
			String ERROR = "Failed to generate the items query for the report request :: Exception occured in "+METHOD_NAME+": "+e;
			log.error(ERROR);
			throw new SMORAException(ERROR);
		}

	  return strQuery;
	}


	/**
	 *
	 * @param reportBaseVO
	 * @param session
	 * @param itemsQuery
	 * @param properties
	 * @param userID
	 * @param custAcctList
	 * @throws SMORADatabaseException
	 * This method creates a connection with SAP and the items list is passed through the request
	 * to retrieve the pricing details for the items list.
	 */

	/*public void loadMHSPRCFieldsForAllSelectedEqItemAccountsWS(Session session, String itemsQuery, Properties properties,  List custAcctList) throws SMORADatabaseException  commented by devesh
	{
		String METHOD_NAME = "loadMHSPRCFieldsForAllSelectedEqItemAccountsWS ";
		SapWsTemplate wsPrice = new SapWsTemplate();
		InvokeInterface wsClient = null;
		SapFtItemsOut[] pricedItems = null;
		Query query_producer = null;
		Transaction tx = null;
		List itemsList = null;
		String cust_acct_id=null;
		try
		{
			query_producer = session.createSQLQuery(itemsQuery);//Gets the item Query
			//Gets the SAP userId and password from the properties file.
			String sapUserId = properties.getProperty("SAP_USER_ID");
			log.info("sapUserId: " + sapUserId);
			String sapPassword = properties.getProperty("SAP_PASSWORD");
			log.info("sapPassword: " + sapPassword);
			//Connecting to SAP
    		wsClient = wsPrice.getInterface(sapUserId, sapPassword);
    		tx = session.beginTransaction();
    		for(int custCount=0;custCount<custAcctList.size();custCount++)
    		{
    			//Account number is retrieved from the list and is set to the cust_acct_id in item query
    			cust_acct_id=(String)custAcctList.get(custCount);
    			query_producer.setString("cust_acct_id", cust_acct_id);
			itemsList = query_producer.list();//items list retrieved after execution of the items query
			log.info("itemsList size for account " + cust_acct_id + " is " + itemsList.size() );
			//Inserting the pricing data to the S_CMS_EQ_ITEM_PRC table using Hibernate.
			pricedItems = wsPrice.getPricingTable(wsClient,cust_acct_id, itemsList);
			if(pricedItems != null){
			log.info("# SAP returned items Count: " + pricedItems.length);
			for(int k = 0; k<pricedItems.length; k++){
			SapFtItemsOut temp = new SapFtItemsOut();
        		temp = pricedItems[k];
			if (temp!=null) {
			TCmsEqItemPrc obj = new TCmsEqItemPrc();
			TEqItemPrcId id = new TEqItemPrcId(cust_acct_id, temp.getItem());
			obj.setId(id);
			obj.setCntrcLeadId(null);
			obj.setEqItemPrc(temp.getYyprcGsp());
			session.save(obj);
			}
			if ( k % 50 == 0 ) {
			session.flush();
			session.clear();
			}
			}
			}
			}
			log.info("end loadPRCFieldsFor Account " + cust_acct_id);

			tx.commit();
		}

		catch (HibernateException e)
		{
			String ERROR = "Exception in retrieving List of Items from database: " + e;
    		log.error(ERROR + e);
    		throw new SMORADatabaseException (ERROR + e);
		}
		catch (SMORADatabaseException e)
	 	{
			String ERROR = "SMORADatabaseException: " + e;
    		log.error(ERROR + e);
    		throw e;
	 	}
		catch(Exception e)
		{
			String ERROR = METHOD_NAME + " : Error in web service... " + e;
			log.error(ERROR + e);
			throw new SMORADatabaseException (ERROR + e);
		}
		finally {}
	}


	/**
	 *
	 * @param reportBaseVO
	 * @param session
	 * @param itemsQuery
	 * @param custAcctList
	 * @throws SMORADatabaseException
	 */
	/*public void loadPRCFieldsForAllSelectedAccountsJCO(ReportBaseVO reportBaseVO, Session session, String itemsQuery,ArrayList custAcctList)  throws SMORADatabaseException {
		String METHOD_NAME = "loadPRCFieldsForAllSelectedAccountsJCO";
		Query query_producer = null;
		DateSelectionAndComparisonVO dateSelectionAndComparisonVO = null;
		JCO.Client jcoClient = null;
		JCO.Function jcoFunction = null;
		JCO.Table priceTable = null;
		Transaction tx = null;
		List itemsList = null;
		String startingDate = null;
		String endingDate = null;
		try
		{
			query_producer = session.createSQLQuery(itemsQuery);
			String startDate = null;
			String endDate = null;
			String fromFormat = "yyyyMM";
			String toFormat = "yyyyMM";
			SimpleDateFormat formatter = new SimpleDateFormat(fromFormat);
			SimpleDateFormat toFormatter = new SimpleDateFormat(toFormat);
			Date fromParseDate = null;
			String fromFormatDate = null;
			String toFormatDate = null;

			DateSelectionUtil dateSelectionUtil = new DateSelectionUtil();
			ArrayList allmonthList = dateSelectionUtil.getDateSelectionmonths(dateSelectionAndComparisonVO);
			ArrayList monthList = null;
			if(allmonthList != null &allmonthList.size() > 0)
			{
				startDate=(String)allmonthList.get(0);
				if(allmonthList.size() > 1)
				{
					endDate = (String)allmonthList.get(1);
				}
			}
			if(allmonthList.size() > 2)
				monthList = (ArrayList)allmonthList.get(2);


			if(startDate != "" && startDate != null)
			{
				fromParseDate= formatter.parse(startDate);
				fromFormatDate = toFormatter.format(fromParseDate);
				fromParseDate= formatter.parse(endDate);
				toFormatDate = toFormatter.format(fromParseDate);

			}
			startingDate = fromFormatDate;
			endingDate = toFormatDate;
			log.info("startingDate "+ startingDate);
			log.info("endingDate "+ endingDate);

			query_producer.setString("beginDate", startingDate);
			query_producer.setString("endDate", endingDate);

			SapJCoClient sapJCoClient = new SapJCoClient();
			jcoClient = sapJCoClient.getJCoClient();
			// jcoFunction = sapJCoClient.getFunction(jcoClient);
			//conn = session.connection();
			//For all accounts

				if(custAcctList != null && custAcctList.size() > 0)
				{
					tx = session.beginTransaction();
					for(int i = 0;i < custAcctList.size(); i++)
					{
						jcoFunction = sapJCoClient.getFunction(jcoClient);

						String cust_acct_id  =(String)custAcctList.get(i);

						log.info("start loadPRCFieldsFor Account " + cust_acct_id);
						query_producer.setString("cust_acct_id", cust_acct_id);
						itemsList = query_producer.list();
						log.info("itemsList size for account " + cust_acct_id + "is " + itemsList.size() );
						priceTable = sapJCoClient.getPricingTable(jcoClient,jcoFunction, cust_acct_id, itemsList);
						if(priceTable!=null && priceTable.getNumRows() > 0)
						{
							for (int j=0; j<priceTable.getNumRows(); j++)
							{
								TAcctItemPrc obj = new TAcctItemPrc();
								TAcctItemPrcId id = new TAcctItemPrcId(cust_acct_id, priceTable.getString("ITEM"));
								obj.setId(id);
								obj.setBegDt(null);
								obj.setEndDt(null);
								obj.setSpclPrcCd(priceTable.getChar("YYPRC_SELL_CD"));
								obj.setCntrcLeadId(null);
								obj.setProperCntrcPrc(new BigDecimal(priceTable.getString("YYPRC_GSP")));
								obj.setCntrcBidPrc(new BigDecimal(priceTable.getString("YYPRC_AWP_PRICE")));
								obj.setRtlGrsPrftPct(null);
								obj.setRtlPrc(new BigDecimal(priceTable.getString("YYPRC_RTL")));
								obj.setRtlPrcTypCd(new Character(priceTable.getChar("YYPRC_RTL_CD")));
								obj.setAvgPurchQty(null);
								obj.setCustItemDeptId(null);
								obj.setCustItemNum(null);
								obj.setDcStkInd(priceTable.getChar("YYSTOCKED"));
								obj.setCntrcMinOrdrQty(null);
								obj.setPrevProperCntrcPrc(null);
								obj.setPrevSpclPrcCd(null);
								obj.setProperCntrcPrcEffDt(null);
								obj.setPrprCntrcPrcCmpntEffDt(null);
								obj.setPrevRtlPrc(null);
								obj.setPrevRtlPrcTypCd(null);
								obj.setRtlPrcEffDt(null);
								obj.setRglrPrc(new BigDecimal(priceTable.getString("YYPRC_REG_PRICE")));
								session.save(obj);
								if ( j % 50 == 0 )
								{ //50, same as the JDBC batch size
							        //flush a batch of inserts and release memory:
							        session.flush();
							        session.clear();
							    }
								priceTable.nextRow();
							}
						}
						log.info("end loadPRCFieldsFor Account " + cust_acct_id);
					}
				}
				tx.commit();

		}
		catch(Exception e)
		{
			String ERROR = METHOD_NAME + " Exception in JCO: " + e;
			log.error(ERROR);
			throw new SMORADatabaseException (ERROR);
		}
		finally
		{
			SapJCoClient.closeJCoConnection(jcoClient);
		}

	}
	*/
	/**
	 *
	 * @param args
	 * @throws SMORADatabaseException
	 */
	/*public static void main(String args[]) throws SMORADatabaseException  commented by devesh
	{
		PriceLoadBatch priceLoadBatch =new PriceLoadBatch();
		HibernateTemplate hibernateTemplate=new HibernateTemplate();
		Session session=null;
		SMOFileProps fileProps=new SMOFileProps();
		Properties properties=fileProps.getFileProperties();
		//boolean jco=Boolean.parseBoolean(properties.getProperty("IS_JCO"));
		//log.info("JCO"+jco);
		String itemQuery=null;
		String acctQuery=null;
		String eqPrcTable=null;
		Query accountsQuery=null;
		List custAcctDetailsList = null;


		try
		{
			session=hibernateTemplate.getSession();
			itemQuery=priceLoadBatch.buildItemsQuery();
			acctQuery=priceLoadBatch.buildAccountsQuery();
			accountsQuery=session.createSQLQuery(acctQuery);
			custAcctDetailsList=accountsQuery.list();

			      /*if(jco)
				{
					priceLoadBatch.loadPRCFieldsForAllSelectedAccountsJCO(reportBaseVO,session, itemQuery,custAcctDetailsList);
					loadedGttTable = GTTTableConstants.RNAAPP_T_ACCT_ITEM_PRC;
				}
				else
				{
					priceLoadBatch.loadMHSPRCFieldsForAllSelectedEqItemAccountsWS(reportBaseVO ,session, itemQuery, properties, rptUserId,custAcctDetailsList);
					loadedGttTable = GTTTableConstants.RNAAPP_T_CMS_EQ_ITEM_PRC;
				}*/
				/*priceLoadBatch.loadMHSPRCFieldsForAllSelectedEqItemAccountsWS(session, itemQuery, properties,custAcctDetailsList);  commented by devesh

			eqPrcTable ="S_CMS_EQ_ITEM_PRC";
			List countList = session.createSQLQuery("select count(*) from "+eqPrcTable).list();
			if(countList != null && countList.size() > 0)
			{
				int count = (Integer)countList.get(0);
				log.info("*Number of rows in "+eqPrcTable+" table: "+count);
				log.info("Loaded"+eqPrcTable+"from SAP");
			}


		}
		catch(SMORAException e)
		{
			log.error("Unable to generate MHS Report");
			log.error("Exception occured in Main"+e);
		}


	}


}*/

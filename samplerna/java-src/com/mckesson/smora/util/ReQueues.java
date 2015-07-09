package com.mckesson.smora.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mckesson.smora.appl.jms.ReportQueue;
import com.mckesson.smora.database.dao.ReportStatusDB;

public class ReQueues
{
	/**
	 * The log.
	 */
	private static Log log = LogFactory.getLog(ReQueues.class);
	public static String domainRoot = null;
	
	private final static StringBuilder QUERY_FOR_REQ = new StringBuilder("WITH MAIN_QUERY  AS (SELECT RPT_ID, RPT_STAT_CD, RPT_TYPE_CD, SBMT_DTS, Q_DTS, USER_ID,(CASE  WHEN RPT_STAT_CD IN (1, 2, 3) THEN (COUNT (RPT_ID) OVER (PARTITION BY USER_ID)) END) CNT, CMPLXTY_IND,")
			.append(" DENSE_RANK () OVER (PARTITION BY RPT_STAT_CD, USER_ID ORDER BY CMPLXTY_IND, ROWID DESC) AS rnk FROM S_APPL_RPT_STAT WHERE     RPT_STAT_CD IN (1, 2, 3) AND (   ( (CASE WHEN RPT_STAT_CD = 3 ")
			.append("THEN (  24 * EXTRACT ( DAY FROM (SYSDATE - Q_DTS) DAY (9) TO SECOND)) + EXTRACT ( HOUR FROM (SYSDATE - Q_DTS) DAY (9) TO SECOND) + (  (1 / 100) * EXTRACT ( MINUTE FROM (SYSDATE - Q_DTS) DAY (9) TO ")
			.append("SECOND)) END) > 1.00) OR ( (CASE WHEN RPT_STAT_CD IN (1, 2) THEN (  24  * EXTRACT ( DAY FROM (SYSDATE - SBMT_DTS) DAY (9) TO SECOND)) + EXTRACT ( HOUR FROM (SYSDATE - SBMT_DTS) DAY (9) TO SECOND) ")
			.append("+ (  (1 / 100) * EXTRACT ( MINUTE FROM (SYSDATE - SBMT_DTS) DAY (9) TO SECOND)) END) > 0.15)) AND 7 >= 1), SUB_QUERY AS (SELECT RPT_ID, RPT_STAT_CD, SBMT_DTS, Q_DTS, USER_ID, CNT, CMPLXTY_IND, ")
			.append(" rnk, DENSE_RANK () OVER (PARTITION BY USER_ID ORDER BY RPT_STAT_CD, ROWID DESC) AS rnk_1 FROM MAIN_QUERY), SUB_QUERY_1 AS (SELECT RPT_ID, rnk, rnk_1, RPT_STAT_CD, SBMT_DTS, Q_DTS, USER_ID, CNT, ")
			.append(" CMPLXTY_IND,  RANK () OVER (PARTITION BY USER_ID ORDER BY rnk_1 DESC) RANK_2 FROM SUB_QUERY) SELECT RPT_ID, RPT_STAT_CD, SBMT_DTS, Q_DTS, USER_ID, CMPLXTY_IND FROM SUB_QUERY_1 WHERE RANK_2 < 3 ORDER BY RPT_STAT_CD DESC  ");

	public static void main(String arg[]) throws NamingException {
		log.info("ReQueues>>>>>>>>>>>");
		/* Changing the status from IP to Submitted */
		ReportStatusDB reportStatusDB = new ReportStatusDB();
		List<Integer> reportIds = null;
		reportIds = getReportToReQueues();
        
		if (reportIds != null && reportIds.size() > 0) {
			try {
				reportStatusDB.updateStatusFromIPToSubmitted(reportIds);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			try {
				/* Adding to queue */
				ReportQueue queue = new ReportQueue();
				List userIdList = reportStatusDB
						.getUserIdFromReportId(reportIds);

				if (userIdList != null) {
					for (int i = 0; i < userIdList.size(); i++) {
						queue.sendMessageAsynchronoulsy("" + userIdList.get(i));
					}
				}
				log.info("ReQueues<<<<<<<<<<<<<");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Getting DB Connection 
	 * 
	 * @return Connection
	 */
	public static Connection getConnectionForReQueues(){
		Connection con=null;
		try{
			   Properties properties = getProprties();
			   String connectionUrl = (String)properties.get("DRIVER_CONNECTION");
			   Class.forName("oracle.jdbc.OracleDriver");
			con=DriverManager.getConnection("jdbc:oracle:thin:@"+connectionUrl,"RNAAPP","RNAAPP");
		}
		catch(Exception ex){
			log.error("Exception when making connection to oracle " + ex);
			ex.printStackTrace();
		}
		return con;
	}
	
	/**
	 * Method is used to get the report Ids which needs to be ReQed
	 * 
	 * @return List<Integer>
	 */
	private static List<Integer> getReportToReQueues() {

		List<Integer> lst = new ArrayList<Integer>();
		Connection con = getConnectionForReQueues();
		Statement st = null;
		ResultSet rs = null;
		log.info("Connection Object..." + con);
		try {
			st = (Statement) con.prepareStatement(QUERY_FOR_REQ.toString());
			rs = (ResultSet) st.executeQuery(QUERY_FOR_REQ.toString());
			while (rs.next()) {
				lst.add(Integer.valueOf(rs.getString("RPT_ID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lst;
	}
	private static Properties getProprties() throws IOException{
		//Changes for WL 12 migration Ends
		 
		FileInputStream fileInputStream = new FileInputStream("/web/app2/smornaDomain/properties/smora.properties");
		Properties properties = new Properties();
		properties.load(fileInputStream);
		properties.list(System.out);
		return properties;
	}
}

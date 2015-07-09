package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.mckesson.smora.database.dao.ReportDB;
import com.mckesson.smora.dto.ReportDetailsVO;
import com.mckesson.smora.exception.SMORAException;

public class ConstructReportDetailsVO 
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "ConstructReportDetailsVO";
	/**
	 * The report details VO.
	 */
	private ReportDetailsVO reportDetailsVO = null;
	
	/**
	 * The report DB.
	 */
	private ReportDB reportDB = null;
	
	public ArrayList<ReportDetailsVO> buildReportDetailsVO(String userName) throws SMORAException
	{
		final String METHODNAME = "buildReportDetailsVO";
		try
		{
			reportDB = new ReportDB();
			Object menuDetails[] = null;
			ArrayList<ReportDetailsVO> reportDetailsVOMenuList = new ArrayList<ReportDetailsVO>();
			ArrayList<ReportDetailsVO> reportDetailsVOList = new ArrayList<ReportDetailsVO>();
			ArrayList<ReportDetailsVO> reportDetailsVOReportList = new ArrayList<ReportDetailsVO>();
			ArrayList userReportDetails = (ArrayList)reportDB.buildReportTree(userName);
			int flag = 0;
			ArrayList<Object[]> secondList = null;
			ArrayList<Object[]> reportList = null;
			ArrayList<Object[]> topList = null;
			if(userReportDetails != null && userReportDetails.size()>0)
			{
				secondList = new ArrayList<Object[]>();
				reportList = new ArrayList<Object[]>();
				topList = new ArrayList<Object[]>();
				for(int i=0;i<userReportDetails.size();i++)
				{
					menuDetails = (Object[])userReportDetails.get(i);
					if(menuDetails[1] != null)
					{

						if(menuDetails[1].toString().trim().equals("2ndMenu Level"))
						{
							secondList.add(menuDetails);
						}
						else if(menuDetails[1].toString().trim().equals("Report"))
						{
							reportList.add(menuDetails);
						}
						else if(menuDetails[1].toString().trim().equals("Top Menu"))
						{
							topList.add(menuDetails);
						}
						
					}
				}
				
			}
			if(secondList != null && secondList.size()>0)
			{
				for(int i=0;i<secondList.size();i++)
				{
					reportDetailsVO = new ReportDetailsVO();
					menuDetails = (Object[])secondList.get(i);
					if(menuDetails[0] != null)
					{
						reportDetailsVO.setDescription(menuDetails[0].toString().trim());
					}
					if(menuDetails[1] != null)
					{
						reportDetailsVO.setReportType(menuDetails[1].toString().trim());
					}
					if(menuDetails[2] != null)
					{
						reportDetailsVO.setReportIndicator(menuDetails[2].toString().trim());
					}
					if(menuDetails[3] != null)
					{
						reportDetailsVO.setReportId(menuDetails[3].toString().trim());
					}
					if(menuDetails[4] != null)
					{
						reportDetailsVO.setParentId(menuDetails[4].toString().trim());
					}
					reportDetailsVOMenuList.add(reportDetailsVO);
		       }
			}
			if(reportList != null && reportList.size()>0)
			{
				for(int i=0;i<reportList.size();i++)
				{
					menuDetails = (Object[])reportList.get(i);
					ReportDetailsVO reportDetailsVOReport = new ReportDetailsVO();
					if(menuDetails[0] != null)
					{
						reportDetailsVOReport.setDescription(menuDetails[0].toString().trim());
					}
					if(menuDetails[1] != null)
					{	
						reportDetailsVOReport.setReportType(menuDetails[1].toString().trim());
					}
					if(menuDetails[2] != null)
					{
						reportDetailsVOReport.setReportIndicator(menuDetails[2].toString().trim());
					}
					if(menuDetails[3] != null)
					{
						reportDetailsVOReport.setReportId(menuDetails[3].toString().trim());
					}
					if(menuDetails[4] != null)
					{
						reportDetailsVOReport.setParentId(menuDetails[4].toString().trim());
					}
					flag = 0;
					Iterator reportDetailsVOMenuListIterator = reportDetailsVOMenuList.iterator();
					while(reportDetailsVOMenuListIterator.hasNext())
					{
						ReportDetailsVO reportDetailsVO = (ReportDetailsVO)reportDetailsVOMenuListIterator.next();
						if(menuDetails[4] != null)
						{
							if(reportDetailsVO.getReportId().equals(menuDetails[4].toString().trim()))
							{
								reportDetailsVOReportList = reportDetailsVO.getChildren();
								if(reportDetailsVOReportList != null)
								{
									reportDetailsVOReportList.add(reportDetailsVOReport);
									reportDetailsVO.setChildren(reportDetailsVOReportList);
								}
								else
								{
									reportDetailsVOReportList = new ArrayList<ReportDetailsVO>();
									reportDetailsVOReportList.add(reportDetailsVOReport);
									reportDetailsVO.setChildren(reportDetailsVOReportList);
								}
								flag = 1;
								break;
							}
						}
					}
					if(flag == 0)
					{
						reportDetailsVOMenuList.add(reportDetailsVOReport);
					}
			    }
		     }
			if(reportDetailsVOMenuList != null && reportDetailsVOMenuList.size()>0)
			{
				Iterator reportDetailsVOMenuListIterator = reportDetailsVOMenuList.iterator();
				HashMap toSortList = new HashMap();
				while(reportDetailsVOMenuListIterator.hasNext())
				{
					ReportDetailsVO reportDetailsVO = (ReportDetailsVO)reportDetailsVOMenuListIterator.next();
					if(reportDetailsVO != null)
					{
						toSortList.put(reportDetailsVO.getDescription(), reportDetailsVO);
					}
					
				}
				Map sortedList = new TreeMap(toSortList);
				if(sortedList != null)
				{
					reportDetailsVOMenuList.clear();
					Set keySet = sortedList.keySet();
					Iterator keyIterator = keySet.iterator();
					while(keyIterator.hasNext())
					{
						String desc = (String)keyIterator.next();
						ReportDetailsVO reportDetailsVO = (ReportDetailsVO)sortedList.get(desc);
						reportDetailsVOMenuList.add(reportDetailsVO);
					}
				}
			}
			if(topList != null && topList.size()>0)
			{
				for(int i=0;i<topList.size();i++)
				{
					menuDetails = (Object[])topList.get(i);
					ReportDetailsVO reportDetailsVOTopMenu  = new ReportDetailsVO();
					if(menuDetails[0] != null)
					{
						reportDetailsVOTopMenu.setDescription(menuDetails[0].toString().trim());
					}
					if(menuDetails[1] != null)
					{
						reportDetailsVOTopMenu.setReportType(menuDetails[1].toString().trim());
					}
					if(menuDetails[2] != null)
					{
						reportDetailsVOTopMenu.setReportIndicator(menuDetails[2].toString().trim());
					}
					if(menuDetails[3] != null)
					{	
						reportDetailsVOTopMenu.setReportId(menuDetails[3].toString().trim());
					}
					ArrayList<ReportDetailsVO> reportDetailsVOSecondMenu = new ArrayList<ReportDetailsVO>();
					Iterator reportDetailsVOMenuListIterator = reportDetailsVOMenuList.iterator();
					while(reportDetailsVOMenuListIterator.hasNext())
					{
						ReportDetailsVO reportDetailsVO = (ReportDetailsVO)reportDetailsVOMenuListIterator.next();
						if(reportDetailsVO.getParentId() != null && reportDetailsVO.getParentId().equals(menuDetails[3].toString().trim()))
						{
							reportDetailsVOSecondMenu = reportDetailsVOTopMenu.getChildren();
							if(reportDetailsVOSecondMenu != null)
							{
								reportDetailsVOSecondMenu.add(reportDetailsVO);
								reportDetailsVOTopMenu.setChildren(reportDetailsVOSecondMenu);
							}
							else
							{
								reportDetailsVOSecondMenu = new ArrayList<ReportDetailsVO>();
								reportDetailsVOSecondMenu.add(reportDetailsVO);
								reportDetailsVOTopMenu.setChildren(reportDetailsVOSecondMenu);
							}
						}
					}
					reportDetailsVOList.add(reportDetailsVOTopMenu);
			  }			
			}
			return reportDetailsVOList;
		}
		catch(Exception e)
		{
			throw new SMORAException(e,CLASSNAME,METHODNAME);
		}
		
	}
	public static void main(String args[])
	{
		ConstructReportDetailsVO constructReportDetailsVO = new ConstructReportDetailsVO();
		try 
		{
			constructReportDetailsVO.buildReportDetailsVO("AESTUUN");
		} 
		catch (SMORAException e) 
		{
			e.printStackTrace();
		}
	}

}

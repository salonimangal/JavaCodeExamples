package com.mckesson.smora.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.mckesson.smora.appl.jms.ReportQueue;
import com.mckesson.smora.database.dao.ReportStatusDB;

public class AddToQueue
{
	public static void main(String arg[]){
		/*Retreiving report ids from file*/
		String filePath = "";
		for(int i = 0; i < arg.length; i++){
			if(!filePath.equals("")){
				filePath = filePath +" "+ arg[i];
			}else{
				filePath = filePath + arg[i];
			}
		}
		//String filePath = "C:\\Documents and Settings\\mohana.ramachandran\\Desktop\\reportid.txt";//arg[0];
		List reportId = null;
		List<Integer> reportIdList = new ArrayList<Integer>();
		String data = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader(filePath));
	        String str = "";
	        while ((str = in.readLine()) != null) {
	        	data += str;
	        }
	        in.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    reportId = String2Array.strArr(data,",");
	    if(reportId!=null){
	    	for(int i  = 0; i < reportId.size(); i++){
	    		if(reportId.get(i)!=null){
	    			reportIdList.add(new Integer(Integer.parseInt(""+reportId.get(i))));
	    		}
	    	}
	    }
	    /*Changing the status from IP to Submitted*/
	    ReportStatusDB reportStatusDB = new ReportStatusDB();
	    try{
	    reportStatusDB.updateStatusFromIPToSubmitted(reportIdList);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    try{
	    /*Adding to queue*/
	    ReportQueue queue = new ReportQueue();
	    List userIdList = reportStatusDB.getUserIdFromReportId(reportIdList);
	    if(userIdList!=null){
		    for(int i = 0; i < userIdList.size(); i++){
		    	queue.sendMessageAsynchronoulsy(""+userIdList.get(i));
		    }
	    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	

}

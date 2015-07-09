/*
 * Copyright(c) 2006 Supply Mgmt Online(Reports and Analysis) - McKesson Inc.
 * MCKESSON PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.mckesson.smora.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.mckesson.smora.dto.FieldsListVO;
import com.mckesson.smora.dto.FieldsVO;

/**
 * The Class FormattingUtil.
 */
public class FormattingUtil
{
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "FormattingUtil";
	/**
	 * This method sends the to view.
	 * 
	 * @param fieldsListVO the fields list VO
	 */
	public void sendToView(FieldsListVO fieldsListVO)
	{
		/**
		 *  sciptlet part :
		 */	
		
		ArrayList fieldsList= fieldsListVO.getFieldsVOList();
		
		ArrayList tableNames=new ArrayList();
		
		HashMap tableTree=getTableTree(fieldsList,tableNames);
		
		Iterator tableNamesIterator=tableNames.iterator();
		
		String tableName=null;
		
		ArrayList tableFieldsList=null;
		Iterator tableFieldsIterator=null;
		FieldsVO fieldVO=null;
		String fieldName=null;
		
		for(int i=0;tableNamesIterator.hasNext();i++)
		{
			tableName=(String)tableNamesIterator.next();
			/*
			treeDataVal['<%=i%>'] = new Array();
			treeDataVal['<%=i%>']['caption'] = '<%=tableName%>';
			treeDataVal['<%=i%>']['url']              = "";
			treeDataVal['<%=i%>']['target']           = "_blank";
			treeDataVal['<%=i%>']['isOpen']           = false;
			treeDataVal['<%=i%>']['isChecked']        = 0;
			treeDataVal['<%=i%>']['onChangeCheckbox'] = checkboxChanged;

			*/
			tableFieldsList=new ArrayList();
			tableFieldsList=(ArrayList)tableTree.get(tableName);
			tableFieldsIterator=tableFieldsList.iterator();
			for(int j=0;tableFieldsIterator.hasNext();j++)
			{
				fieldVO=(FieldsVO)tableFieldsIterator.next();
				fieldName=fieldVO.getFieldName();
				/*
				treeDataVal['<%=i%>']['children'] = new Array();
				treeDataVal['<%=i%>']['children']['<%=j%>']      = new Array;
				treeDataVal['<%=i%>']['children']['<%=j%>']['caption'] = '<%=fieldName%>';
				treeDataVal['<%=i%>']['children']['<%=j%>']['url']              = "";
				treeDataVal['<%=i%>']['children']['<%=j%>']['target']           = "_blank";
				treeDataVal['<%=i%>']['children']['<%=j%>']['isOpen']           = false;
				treeDataVal['<%=i%>']['children']['<%=j%>']['isChecked']        = 0;
				treeDataVal['<%=i%>']['children']['<%=j%>']['onChangeCheckbox'] = checkboxChanged;
				*/
			}// fields extract loop
		}
		
	}
    
	/**
	 * This method gets the table tree.
	 * 
	 * @param tableNames the table names
	 * @param fieldsList the fields list
	 * 
	 * @return the table tree
	 */
    public HashMap getTableTree(ArrayList fieldsList,ArrayList tableNames)
    {
    	
		HashMap tableTree=new HashMap(); 
    	Iterator fieldsIterator=fieldsList.iterator();
		
        FieldsVO fieldVO=null;
		String tablename=null;
		while(fieldsIterator.hasNext())
		{
			fieldVO=(FieldsVO)fieldsIterator.next();
			tablename=fieldVO.getFuncGrp();
			if(tableTree.containsKey(tablename))
			{
				ArrayList table=(ArrayList)tableTree.get(tablename);
				table.add(fieldVO);
				
				tableTree.put(tablename,table);
				
			}
			else
			{
				ArrayList table= new ArrayList();
				table.add(fieldVO);
				tableNames.add(tablename);
				tableTree.put(tablename,table);
				
			}
		}
		
		return tableTree;
		
    }
}

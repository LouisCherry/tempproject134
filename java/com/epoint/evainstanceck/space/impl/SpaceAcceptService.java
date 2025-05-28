package com.epoint.evainstanceck.space.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class SpaceAcceptService
{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;

    public SpaceAcceptService() {
        dao = CommonDao.getInstance();
    }
   
    public List<Record> getGradinfo(String grade) {
        String sql = "select itemtext text,ITEMVALUE  value from code_items where codeid='1016097' and SUBSTRING(ITEMVALUE,1,1)=? ";
        return this.dao.findList(sql, Record.class, grade);
    }
    
    public Record getHcpStati(String value) {
        String sql = "select itemtext from code_items where CODEID='1016097' and  ITEMVALUE =?";
        return this.dao.find(sql, Record.class, value);
    }
    
    public Record getstaicfiedByTaskguid(String taskguid) {
    	 String sql = "select SUM(satisfied)/count(1) as avestatis,count(1) as total from audit_online_evaluat where taskguid =?";
         return this.dao.find(sql, Record.class, taskguid);
    }
    
    public Record getevaluatStatiByTaskguid(String taskguid) {
   	 String sql = "select SUM(CASE WHEN satisfied = 1  THEN 1 ELSE 0 END) as feichangbumanyi,SUM(CASE WHEN satisfied = 2  THEN 1 ELSE 0 END) as bumanyi,";
     	sql += "SUM(CASE WHEN satisfied = 3  THEN 1 ELSE 0 END) as jibenmanyi,SUM(CASE WHEN satisfied = 4  THEN 1 ELSE 0 END) as manyi,";
     	sql += "SUM(CASE WHEN satisfied = 5  THEN 1 ELSE 0 END) as feichangmanyi from audit_online_evaluat where taskguid =?";
   	 return this.dao.find(sql, Record.class, taskguid);
   }

}

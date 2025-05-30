package com.epoint.basic.auditproject.auditproject.impl;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;

public class JNAuditProjectService
{
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public JNAuditProjectService() {
        commonDao = CommonDao.getInstance("project");

    }

    public JNAuditProjectService(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);

        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        }
        else {
            commonDao = CommonDao.getInstance();
        }
    }
    
    public Record getMaxZjNum(String name){
    	String sql = "select * from zj_num where name = ? ";
    	return commonDao.find(sql, Record.class, name);
    }
    
    public void UpdateMaxZjNum(String maxnum,String name){
    	String sql = "update zj_num set maxnum = ? where name = ? ";
    	commonDao.execute(sql, maxnum, name);
    }
    
    public AuditTask getAuditTaskByUnid(String unid){
    	String sql = "select * from audit_task where unid = ? ";
    	return commonDao.find(sql,AuditTask.class, unid);
    }
    
    
}

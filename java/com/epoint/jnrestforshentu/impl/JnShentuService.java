package com.epoint.jnrestforshentu.impl;

import java.util.List;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnrestforshentu.api.entity.AuditThreeFirst;

/**
 * 事项同步错误记录表对应的后台service
 * 
 * @author zhaoy
 * @version [版本号, 2019-01-23 16:39:23]
 */
public class JnShentuService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnShentuService() {
        baseDao = CommonDao.getInstance();
    }
    
    public Integer isExistEvaluate(String projectguid){
        String sql = "select count(rowguid) from audit_sp_tsopinion where ywguid = ?";
        return baseDao.queryInt(sql, projectguid);
    }
    
    public Integer isExistProject(String projectguid){
        String sql = "select count(ITEMCODE) from audit_rs_item_baseinfo where rowguid = ?";
        return baseDao.queryInt(sql, projectguid);
    }
    
    public List<AuditRsItemBaseinfo> getTsProjectInfo(String flowsn, String name,String creditcode){
        String sql = "select * from audit_rs_item_baseinfo where 1=1 ";
        if (StringUtil.isNotBlank(creditcode) && StringUtil.isNotBlank(flowsn)) {
            sql += "and ITEMCODE = '"+flowsn+"' and ITEMLEGALCREDITCODE = '"+creditcode+"'";
        }
        else if(StringUtil.isBlank(creditcode) && StringUtil.isNotBlank(flowsn)){
            sql += "and ITEMCODE = '"+flowsn+"'";
        }
        else if(StringUtil.isNotBlank(creditcode) && StringUtil.isBlank(flowsn)){
            sql += "and ITEMLEGALCREDITCODE = '"+creditcode+"'";
        }
        return baseDao.findList(sql, AuditRsItemBaseinfo.class);
        
    }
    
    public Record getTsProjectInfoByRowguid(String biguid) {
        String sql = "select assignee,orgname,signtime,handlecomments,bdcassignee,bdcorgname,bdcsigntime,bdchandlecomments from audit_sp_tsopinion where ywguid = (select rowguid from audit_rs_item_baseinfo where BIGUID = ? order by itemcode desc limit 1)";
        return baseDao.find(sql, Record.class,biguid);
    }
    
    public AuditRsItemBaseinfo getTsClientguidByRowguid(String biguid) {
        String sql = "select * from audit_rs_item_baseinfo where BIGUID = ?";
        return baseDao.find(sql, AuditRsItemBaseinfo.class,biguid);
    }
    
    public Integer addTsOpinion(Record opinion) {
        return baseDao.insert(opinion);
    }
    
    public List<Record> getThreeFirstByAttachguid(String type) {
        String sql = "select name,attachguid from audit_threefirst where type = ?";
        return baseDao.findList(sql, Record.class,type);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditThreeFirst record) {
        return baseDao.insert(record);
    }
}

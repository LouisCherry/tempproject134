package com.epoint.jnrestforevaluat.impl;

import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;

/**
 * 事项同步错误记录表对应的后台service
 * 
 * @author zhaoy
 * @version [版本号, 2019-01-23 16:39:23]
 */
public class JnEvaluatService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnEvaluatService() {
        baseDao = CommonDao.getInstance();
    }
    public List<FrameOu> getOuinfo(String ouguid,String areacode) {
        String sql = "select areacode,ouname,oushortname,oucode,o.ouguid,o.parentouguid from frame_ou o INNER JOIN frame_ou_extendinfo e "
                + "on o.OUGUID=e.OUGUID where 1=1";
        if(StringUtil.isNotBlank(ouguid)){
            sql += " and o.ouguid = '"+ouguid+"'";
        }
        if(StringUtil.isNotBlank(areacode)){
            sql += " and areacode = '"+areacode+"'";
        }
        return baseDao.findList(sql, FrameOu.class);
    }
    
    public PageData<AuditProject> getProjectinfo(String ouguid,String areacode,String startdate,
            String enddate,String flowsn,Integer pagesize,Integer index) {
        PageData<AuditProject> pagedata = new PageData<AuditProject>();
        String sql = "SELECT p.rowguid,p.OperateDate,FLOWSN,p.WINDOWGUID,w.WINDOWNAME,w.WINDOWNO,BANJIEDATE,BANJIEUSERNAME,p.AREACODE,ACCEPTUSERNAME,ACCEPTUSERDATE,ACCEPTUSERGUID,"
            +" p.OUGUID,o.OUCODE,APPLYDATE,APPLYERNAME,IFNULL(CONTACTPHONE,CONTACTMOBILE) as CONTACTPHONE,t.TaskName,t.ITEM_ID,p.OUNAME"
            +" from audit_project p INNER JOIN audit_task t on p.TASKGUID = t.RowGuid "
            +" INNER JOIN frame_ou o on p.OUGUID=o.OUGUID INNER JOIN audit_orga_window w on p.WINDOWGUID=w.RowGuid"
            +" where STATUS = 90 and IFNULL(ACCEPTUSERGUID,'')<>'' and IFNULL(windowguid,'')<>'' ";
        if(StringUtil.isNotBlank(ouguid)){
            sql += " and p.ouguid = '"+ouguid+"'";
        }
        if(StringUtil.isNotBlank(areacode)){
            sql += " and p.areacode = '"+areacode+"'";
        }
        if(StringUtil.isNotBlank(startdate)){
            sql += " and p.APPLYDATE >'"+startdate+"'";
        }
        if(StringUtil.isNotBlank(enddate)){
            sql += " and p.APPLYDATE <'"+enddate+"'";
        }
        if(StringUtil.isNotBlank(flowsn)){
            sql += " and p.flowsn ='"+flowsn+"'";
        }
        pagedata.setRowCount(baseDao.findList(sql, AuditProject.class).size());
        if(pagesize != null && index != null){
            sql += " limit "+pagesize*index+","+pagesize;
        }
        pagedata.setList(baseDao.findList(sql, AuditProject.class));
        
        return pagedata;
    }
    
    public PageData<FrameUser> getUserinfo(String ouguid,String areacode,String userguid,Integer pagesize,Integer index) {
        PageData<FrameUser> pagedata = new PageData<FrameUser>();
        String sql = "SELECT LOGINID,USERGUID,DISPLAYNAME,MOBILE,o.OUGUID,o.OUCODE FROM frame_user u "
                + "INNER JOIN frame_ou o on u.OUGUID = o.OUGUID "
                + "INNER JOIN frame_ou_extendinfo e on o.OUGUID = e.OUGUID where 1=1";
        if(StringUtil.isNotBlank(ouguid)){
            sql += " and o.ouguid = '"+ouguid+"'";
        }
        if(StringUtil.isNotBlank(areacode)){
            sql += " and e.areacode = '"+areacode+"'";
        }
        if(StringUtil.isNotBlank(userguid)){
            sql += " and u.userguid = '"+userguid+"'";
        }
        pagedata.setRowCount(baseDao.findList(sql, FrameUser.class).size());
        if(pagesize != null && index != null){
            sql += " limit "+pagesize*index+","+pagesize;
        }
        pagedata.setList(baseDao.findList(sql, FrameUser.class));
        return pagedata;
    }
    
    public Integer isExistProject(String projectguid){
        String sql = "select count(flowsn) from audit_project where rowguid = ?";
        return baseDao.queryInt(sql, projectguid);
    }
}

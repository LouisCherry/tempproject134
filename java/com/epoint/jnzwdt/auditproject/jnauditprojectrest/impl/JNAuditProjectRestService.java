package com.epoint.jnzwdt.auditproject.jnauditprojectrest.impl;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;


/**
 * 微信公众号办件公告接口对应的后台service接口
 * 
 * @author hlxin
 * @version [版本号, 2019-07-23 11:00:17]
 */
public class JNAuditProjectRestService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JNAuditProjectRestService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 分页查找办件表的列表清单
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @return T extends BaseEntity
     */
    public List<AuditProject> getAuditProjectRestList(int pageNumber, int pageSize, String areaCode) {
        String sql = "select rowguid,PROJECTNAME,FLOWSN,APPLYERNAME,DATE_FORMAT(APPLYDATE, '%Y-%m-%d %H:%i:%s') as APPLYDATE,BANJIERESULT,status from audit_project where AREACODE =?1 and BANJIERESULT in ('40','50') order by APPLYDATE desc limit ?2,?3";
        List<AuditProject> list = baseDao.findList(sql, AuditProject.class, areaCode,pageNumber,pageSize);
        return list;
    }
    
    /**
     * 查找办件的详细信息
     * 
     * @param rowGuid
     */
    public AuditProject getAuditProjectRestDetail(String rowGuid) {
        String sql = "select FLOWSN,PROJECTNAME,APPLYERNAME,OUName,ACCEPTUSERNAME,DATE_FORMAT(ACCEPTUSERDATE, '%Y-%m-%d %H:%i:%s') as ACCEPTUSERDATE,tasktype,BANJIERESULT,status from audit_project where rowguid = ?1";
        AuditProject auditProject = baseDao.find(sql, AuditProject.class, rowGuid);
        return auditProject;
    }
    
    /**
     * 查找办件的详细信息
     * 
     * @param rowGuid
     */
    public AuditProject getAuditProjectDetail(String rowGuid) {
        String sql = "select * from audit_project where rowguid = ?1";
        AuditProject auditProject = baseDao.find(sql, AuditProject.class, rowGuid);
        return auditProject;
    }
    
    /**
     * 统计本年、月、日办件数量
     */
    public AuditProject getAuditCount(String areaCode) {
        String sql = "SELECT count(1) as everyYearCount,sum(case when DATE_FORMAT(DATE(now()), '%Y%m') = DATE_FORMAT(applydate, '%Y%m') THEN 1 else 0 END) as everyMonthCount,sum(case when DATE_FORMAT(DATE(now()), '%Y%m%d') = DATE_FORMAT(applydate, '%Y%m%d') THEN 1 else 0 END) as everyDayCount FROM audit_project WHERE `STATUS` > 24 and year(now()) = year(applydate) and areacode = ?1";
        return baseDao.find(sql, AuditProject.class, areaCode);
    }
    
    
    /**
     * 统计办件基本满意和不满意的数量
     */
    public AuditProject getSatisfiedCount(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "select sum(case when e.satisfied in ('3','4') THEN 1 else 0 END) as basicSatisfiedCount,sum(case when e.satisfied = '1' THEN 1 else 0 END) as disSatisfiedCount from audit_online_evaluat e left join audit_project p on e.ClientIdentifier = p.rowguid where clienttype = '10' and p.areacode = ?1";
            return baseDao.find(sql, AuditProject.class, areaCode);
        }else {
            sql = "select sum(case when e.satisfied in ('3','4') THEN 1 else 0 END) as basicSatisfiedCount,sum(case when e.satisfied = '1' THEN 1 else 0 END) as disSatisfiedCount from audit_online_evaluat e left join audit_project p on e.ClientIdentifier = p.rowguid where clienttype = '10'";
            return baseDao.find(sql, AuditProject.class);
        }
    }
    
    /**
     * 统计办件总计的数量
     */
    public AuditProject getTotalSatisfiedCount(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "select count(1) as totalAudit from audit_project where `STATUS` > 24 and ACCEPTUSERGUID is NOT NULL and areacode=?1";
            return baseDao.find(sql, AuditProject.class, areaCode);
        }else {
            sql = "select count(1) as totalAudit from audit_project where `STATUS` > 24 and ACCEPTUSERGUID is NOT NULL";
            return baseDao.find(sql, AuditProject.class);
        }
    }
    
    
}

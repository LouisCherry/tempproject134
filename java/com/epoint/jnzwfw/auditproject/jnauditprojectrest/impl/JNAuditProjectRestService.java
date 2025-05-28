package com.epoint.jnzwfw.auditproject.jnauditprojectrest.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxb;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;

import java.util.Date;
import java.util.List;


/**
 * 微信公众号办件公告接口对应的后台service接口
 *
 * @author hlxin
 * @version [版本号, 2019-07-23 11:00:17]
 */
public class JNAuditProjectRestService {
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
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @return T extends BaseEntity
     */
    public List<AuditProject> getAuditProjectRestList(int pageNumber, int pageSize, String areaCode) {
        String sql = "select rowguid,PROJECTNAME,FLOWSN,APPLYERNAME,DATE_FORMAT(APPLYDATE, '%Y-%m-%d %H:%i:%s') as APPLYDATE,BANJIERESULT,status from audit_project where AREACODE =?1 and BANJIERESULT in ('40','50') order by APPLYDATE desc";
        List<AuditProject> list = baseDao.findList(sql, pageNumber, pageSize, AuditProject.class, areaCode);
        return list;
    }

    /**
     * 查找办件的详细信息
     *
     * @param rowGuid
     */
    public AuditProject getAuditProjectRestDetail(String rowGuid) {
        String sql = "select FLOWSN,PROJECTNAME,APPLYERNAME,OUName,ACCEPTUSERNAME,ACCEPTUSERDATE,tasktype,BANJIERESULT,status from audit_project where rowguid = ?1";
        AuditProject auditProject = baseDao.find(sql, AuditProject.class, rowGuid);
        return auditProject;
    }

    /**
     * 统计本年、月、日办件数量
     */
    public AuditProject getAuditCount(String areaCode) {
        String sql = "SELECT count(1) as everyYearCount,sum(case when DATE_FORMAT(DATE(now()), '%Y%m') = DATE_FORMAT(applydate, '%Y%m') THEN 1 else 0 END) as everyMonthCount,sum(case when DATE_FORMAT(DATE(now()), '%Y%m%d') = DATE_FORMAT(applydate, '%Y%m%d') THEN 1 else 0 END) as everyDayCount FROM audit_project WHERE status > 10 and status <> 20 and status <> 22 and year(now()) = year(applydate) and areacode = ?1";
        return baseDao.find(sql, AuditProject.class, areaCode);
    }


    /**
     * 统计办件基本满意和不满意的数量
     */
    public AuditProject getSatisfiedCount(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "select areacode,sum(case when satisfaction = '5' then 1 else 0 end) as verySatisfiedCount,sum(case when satisfaction = '4' then 1 else 0 end) as SatisfiedCount, ";
            sql += "sum(case when satisfaction = '3' then 1 else 0 end) as basicSatisfiedCount,sum(case when satisfaction = '2' then 1 else 0 end) as disSatisfiedCount,";
            sql += "sum(case when satisfaction = '1' then 1 else 0 end) as verydisSatisfiedCount,sum(case when satisfaction in ('5','4','3') then 1 else 0 end) as totalmanyiPercent,";
            sql += "count(1) as total from evainstance where areacode = ? and year(now()) = year(Acceptdate)";
            return baseDao.find(sql, AuditProject.class, areaCode);
        } else {
            sql = "select areacode,sum(case when satisfaction = '5' then 1 else 0 end) as verySatisfiedCount,sum(case when satisfaction = '4' then 1 else 0 end) as SatisfiedCount, ";
            sql += "sum(case when satisfaction = '3' then 1 else 0 end) as basicSatisfiedCount,sum(case when satisfaction = '2' then 1 else 0 end) as disSatisfiedCount,";
            sql += "sum(case when satisfaction = '1' then 1 else 0 end) as verydisSatisfiedCount,sum(case when satisfaction in ('5','4','3') then 1 else 0 end) as totalmanyiPercent,";
            sql += "count(1) as total from evainstance where areacode = '370800' and year(now()) = year(Acceptdate)";
            return baseDao.find(sql, AuditProject.class);
        }
    }

    public AuditProject getLsSatisfiedCount() {
        String sql = "select sum(case when evalevel = '5' then 1 else 0 end) as verySatisfiedCount,sum(case when evalevel = '4' then 1 else 0 end) as SatisfiedCount, " +
                "sum(case when evalevel = '3' then 1 else 0 end) as basicSatisfiedCount,sum(case when evalevel = '2' then 1 else 0 end) as disSatisfiedCount," +
                "sum(case when evalevel = '1' then 1 else 0 end) as verydisSatisfiedCount,count(1) as total from evainstance_ck " +
                "where areacode = '370832' and year(now()) = year(Createdate)";
        return baseDao.find(sql, AuditProject.class);
    }

    /**
     * 统计办件总计的数量
     */
    public AuditProject getTotalSatisfiedCount(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "select count(1) as totalAudit from audit_project where status > 10 and status <> 20 and status <> 22 and areacode=?1 and YEAR(applydate)=YEAR(NOW())";
            return baseDao.find(sql, AuditProject.class, areaCode);
        } else {
            sql = "select count(1) as totalAudit from audit_project where status > 10 and status <> 20 and status <> 22 and areacode='370800' and YEAR(applydate)=YEAR(NOW())";
            return baseDao.find(sql, AuditProject.class);
        }
    }

    public int updateProjectByRowguid(String projectguid, Date banjiedate, String Status, String banjieusername, String Banjieresult) {
        String sql = "update audit_project set status = " + Status + " , ";
        if (StringUtil.isNotBlank(banjiedate)) {
            sql += "banjiedate= '" + banjiedate + "',";
        }
        if (StringUtil.isNotBlank(banjieusername)) {
            sql += "banjieusername= '" + banjieusername + "',";
        }
        if (StringUtil.isNotBlank(Banjieresult)) {
            sql += "Banjieresult= '" + Banjieresult + "',";
        }
        if (sql.endsWith(",")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        sql += " where rowguid = '" + projectguid + "'";
        return baseDao.execute(sql);
    }

    /**
     * 统计办件总计的数量
     */
    public List<SpglXmspsxblxxb> getSPglXmspxxb() {
        String sql = "select * from spgl_xmspsxblxxb where sjsczt='-1' order by SPSXBM limit 20";
        return baseDao.findList(sql, SpglXmspsxblxxb.class);
    }

    /**
     * 统计办件总计的数量
     */
    public int updateSPglXmspxxb(String rowguid) {
        String sql = "update spgl_xmspsxblxxb set sjsczt='100' where rowguid = '" + rowguid + "'";
        int count = baseDao.execute(sql);
        baseDao.close();
        return count;
    }

    /**
     * 统计办件总计的数量
     */
    public List<AuditProject> getauditProjectByFlowsn(String flowsns) {
        String sql = "select * from audit_project where flowsn in (" + flowsns + ")";
        return baseDao.findList(sql, AuditProject.class);
    }
}

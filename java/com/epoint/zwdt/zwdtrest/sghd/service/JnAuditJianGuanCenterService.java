package com.epoint.zwdt.zwdtrest.sghd.service;

import java.util.ArrayList;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;

public class JnAuditJianGuanCenterService
{
    protected ICommonDao baseDao;

    public JnAuditJianGuanCenterService() {
        baseDao = CommonDao.getInstance();
    }

    /********************************************
     * 中心审管tab显示
     ****************************************************/
    /**
     * 获取中心审批办件数量
     *
     * @return
     */
    public int getSpxxCount(String sql) {
        return baseDao.queryInt(sql, AuditProject.class);
    }

    /**
     * 中心许可变更意见数目
     *
     * @return
     */
    public int getBianGengNum(String areaCode) {
        String sqlNum = "SELECT COUNT(DISTINCT p.themeguid ) from (select x.* from audit_project_permissionchange x INNER JOIN (select distinct a.OUGUID,a.OUNAME from frame_ou a inner join frame_ou_extendinfo b on a.OUGUID=b.OUGUID inner join audit_task_jn c on a.OUGUID=c.OUGUID where b.AREACODE= ? and c.is_hz='1' ) z ON x.ouguid = z.ouguid) p ";
        return baseDao.queryInt(sqlNum, areaCode);
    }

    /**
     * 中心领导获取互动协助数量
     *
     * @return
     */
    public int getHdxzCount(String areaCode) {
        String sql = "SELECT COUNT(a.themeguid) from (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE AREACODE = ? ) a ";
        return baseDao.queryInt(sql, areaCode);
    }

    /**
     * 中心获取已认领的办件数目
     *
     * @return
     */
    public int getYrl(String areaCode) {
        String sql = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where 1=1 and p.status = 90 and p.Banjieresult = '40' ";
        sql += " and p.areaCode = '" + areaCode + "' and p.handleareacode like '" + areaCode + "%'";
        /*sql += "AND p.renlingtime is not NULL ";*/
        sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= '" + sghdstarttime + "'";
        }
        return baseDao.queryInt(sql);
    }

    /**
     * 中心获取未认领的办件数目
     *
     * @return
     */
    public int getWrl(String areaCode) {
        String sql = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where 1=1 and p.status = 90 and p.Banjieresult = '40' ";
        sql += " and p.areaCode = '" + areaCode + "' and p.handleareacode like '" + areaCode + "%'";
        /*sql += "AND p.renlingtime is NULL ";*/
        sql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= '" + sghdstarttime + "'";
        }
        return baseDao.queryInt(sql);
    }

    /**
     * 获取已认领的办件数目 areacode
     *
     * @return
     */
    public int getYrlByAreaCode(String areaCode) {
        String sql = "select count(1) from audit_project WHERE areacode = ? AND STATUS = 90 AND renlingtime is NOT NULL and Banjieresult = '40' ";
        return baseDao.queryInt(sql, areaCode);
    }

    /**
     * 获取已认领的办件数目 byouguid
     *
     * @return
     */
    public int getYrlByOuguid(String ouguid) {
        String sql = "select count(1) from audit_project WHERE ouguid = ? AND STATUS = 90 AND renlingtime is NOT NULL and Banjieresult = '40' ";
        return baseDao.queryInt(sql, ouguid);
    }

    /**
     * 获取未认领的办件数目 areaCode
     *
     * @return
     */
    public int getWrlByAreaCode(String areaCode) {
        String sql = "select count(1) from audit_project WHERE AREACODE = ? AND STATUS = 90 AND renlingtime is NOT NULL and Banjieresult = '40'";
        return baseDao.queryInt(sql, areaCode);
    }

    /**
     * 获取未认领的办件数目 ouguid
     *
     * @return
     */
    public int getWrlByOuguid(String ouguid) {
        String sql = "select count(1) from audit_project WHERE ouguid = ? AND STATUS = 90 AND renlingtime is NOT NULL and Banjieresult = '40'";
        return baseDao.queryInt(sql, ouguid);
    }

    /**
     * @return List<Record> 返回类型
     * @throws
     *         @Description:
     *             获取树ou信息
     * @author male
     * @date 2019年1月23日 下午3:31:48
     */
    public List<Record> getOuInfo(String areaCode) {
        String sql = "select distinct a.OUGUID,a.OUNAME from frame_ou a inner join frame_ou_extendinfo b on a.OUGUID=b.OUGUID inner join audit_task_taian c on a.OUGUID=c.OUGUID where b.AREACODE=? and c.is_hz='1' order by a.ordernumber desc ";
        return baseDao.findList(sql, Record.class, areaCode);
    }

    /**
     * @return List<AuditProject> 返回类型
     * @throws
     *         @Description:
     *             获取TaProjectCenterAction类DateGrid
     * @author male
     * @date 2019年1月23日 下午3:45:05
     */
    public List<AuditProject> getProejctCenter(String sql, int first, int pageSize) {
        return baseDao.findList(sql, AuditProject.class, first, pageSize);
    }

    /**
     * @return String 返回类型
     * @throws
     *         @Description:
     *             根据rowguid获取audit_task ouname
     * @author male
     * @date 2019年1月23日 下午3:45:09
     */
    public String getOuNameFromAuditTask(String rowguid) {
        String sqlPromise = "SELECT ouname FROM audit_task WHERE rowguid = ? ";
        return baseDao.queryString(sqlPromise, rowguid);
    }

    /**
     * @return int 返回类型
     * @throws
     *         @Description:
     *             获取TaProjectCenterAction类DateGrid数量
     * @author male
     * @date 2019年1月23日 下午3:45:13
     */
    public int getProejctCenterNum(String sql) {
        return baseDao.queryInt(sql, AuditProject.class);
    }

    /**
     * 根据辖区获取划转事项的原部门信息
     *
     * @param areacode
     * @return
     */
    public List<Record> listOuByAreacode(String areaCode) {
        String sql = "select DISTINCT t.jg_ouguid from audit_task_jn t INNER JOIN audit_task a ON t.task_id = a.TASK_ID  WHERE AREACODE = ? AND jg_ouguid is NOT NULL AND jg_ouguid != '' ";
        List<String> list = baseDao.findList(sql, String.class, areaCode);
        if (list != null && !list.isEmpty()) {
            String sqlOu = "select OUGUID,OUSHORTNAME from frame_ou WHERE OUGUID in (" + StringUtil.joinSql(list)
                    + ") ORDER BY ORDERNUMBER DESC ";
            return baseDao.findList(sqlOu, Record.class);
        }
        else {
            return new ArrayList<Record>();
        }
    }

}

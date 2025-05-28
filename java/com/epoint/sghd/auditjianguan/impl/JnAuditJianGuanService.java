package com.epoint.sghd.auditjianguan.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jxgl.basic.auditprojectmonitorreply.domain.AuditProjectMonitorReply;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;

import java.util.List;

public class JnAuditJianGuanService {
    protected ICommonDao baseDao;

    public JnAuditJianGuanService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * @return List<AuditProjectPermissionChange> 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange （byouguid）
     * @author male
     * @date 2019年1月22日 上午11:37:06
     */
    public List<AuditProjectPermissionChange> getAuditProjectPermissionByOuguid(String areaCode, String ouguid,
                                                                                int first, int pageSize) {
        String sql = "select * from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN (select o.ouguid from frame_ou_extendinfo o INNER JOIN audit_task_jn t ON o.ouguid = t.jg_ouguid WHERE o.areacode = ? AND t.is_hz = '1') f ON p.ouguid = f.OUGUID ) a ON b.themeguid = a.themeguid where b.ouguid = ? GROUP BY b.themeguid ORDER BY b.replydate DESC limit ?,? ";
        return baseDao.findList(sql, AuditProjectPermissionChange.class, areaCode, ouguid, first, pageSize);
    }

    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange数量 （byouguid）
     * @author male
     * @date 2019年1月22日 上午11:38:52
     */
    public int getAuditProjectPermissionNumByOuguid(String areaCode, String ouguid) {
        String sqlNum = "SELECT COUNT(DISTINCT p.themeguid ) from (select x.ouguid,x.themeguid from audit_project_permissionchange x INNER JOIN (select distinct a.OUGUID,a.OUNAME from frame_ou a inner join frame_ou_extendinfo b on a.OUGUID=b.OUGUID inner join audit_task_jn c on a.OUGUID=c.jg_ouguid where b.AREACODE= ? and c.is_hz='1' ) z ON x.ouguid = z.ouguid) p WHERE p.ouguid = ? ";
        return baseDao.queryInt(sqlNum, areaCode, ouguid);

    }

    /**
     * @return List<AuditProjectPermissionChange> 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange
     * @author male
     * @date 2019年1月22日 上午11:42:59
     */
    public List<AuditProjectPermissionChange> getAuditProjectPermission(String areaCode, int first, int pageSize) {
        String sql = "select * from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN (select o.ouguid from frame_ou_extendinfo o INNER JOIN audit_task_jn t ON o.ouguid = t.jg_ouguid WHERE o.areacode = ? AND t.is_hz = '1') f ON p.ouguid = f.OUGUID ) a ON b.themeguid = a.themeguid  GROUP BY b.themeguid ORDER BY b.replydate DESC limit ?,? ";
        return baseDao.findList(sql, AuditProjectPermissionChange.class, areaCode, first, pageSize);
    }

    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange数量
     * @author male
     * @date 2019年1月22日 上午11:43:03
     */
    public int getAuditProjectPermissionNum(String areaCode) {
        String sqlNum = "SELECT COUNT(DISTINCT p.themeguid ) from (select x.ouguid,x.themeguid from audit_project_permissionchange x INNER JOIN (select distinct a.OUGUID,a.OUNAME from frame_ou a inner join frame_ou_extendinfo b on a.OUGUID=b.OUGUID inner join audit_task_jn c on a.OUGUID=c.jg_ouguid where b.AREACODE= ? and c.is_hz='1' ) z ON x.ouguid = z.ouguid) p  ";
        return baseDao.queryInt(sqlNum, areaCode);
    }

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 根据areacode获取ou信息
     * @author male
     * @date 2019年1月22日 上午11:48:11
     */
    public List<Record> getOuByareaCode(String areaCode) {
        String sql = "select distinct a.OUGUID,a.OUNAME from frame_ou a inner join frame_ou_extendinfo b on a.OUGUID=b.OUGUID inner join audit_task_jn c on a.OUGUID=c.OUGUID where b.AREACODE=? and c.is_hz='1' order by a.ordernumber desc";
        return baseDao.findList(sql, Record.class, areaCode);
    }

    /**
     * @return List<AuditProjectPermissionChange> 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange 中心省管
     * @author male
     * @date 2019年1月22日 下午1:50:12
     */
    public List<AuditProjectPermissionChange> getAuditProjectPermission2(String areaCode, int first, int pageSize) {
        String sql = "select * from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ?) a ON b.themeguid = a.themeguid GROUP BY b.themeguid ORDER BY b.replydate DESC limit ?,? ";
        return baseDao.findList(sql, AuditProjectPermissionChange.class, areaCode, first, pageSize);
    }

    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange 数量 中心省管
     * @author male
     * @date 2019年1月22日 下午1:50:52
     */
    public int getAuditProjectPermissionNum2(String areaCode) {
        String sqlNum = "SELECT COUNT(a.themeguid) from (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ? ) a ";
        return baseDao.queryInt(sqlNum, areaCode);
    }

    /**
     * @return List<AuditProjectPermissionChange> 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange （byouguid）中心省管
     * @author male
     * @date 2019年1月22日 下午1:51:10
     */
    public List<AuditProjectPermissionChange> getAuditProjectPermissionByOuguid2(String areaCode, String ouguid,
                                                                                 int first, int pageSize) {
        String sql = "select * from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ?) a ON b.themeguid = a.themeguid where b.ouguid = ? OR b.communicationouguid = ? GROUP BY b.themeguid ORDER BY b.replydate DESC limit ?,? ";
        return baseDao.findList(sql, AuditProjectPermissionChange.class, areaCode, ouguid, ouguid, first, pageSize);
    }

    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange 数量（byouguid）中心省管
     * @author male
     * @date 2019年1月22日 下午1:51:23
     */
    public int getAuditProjectPermissionNumByOuguid2(String areaCode, String ouguid) {
        // String sqlNum = "SELECT COUNT(a.themeguid) from (SELECT DISTINCT
        // themeguid from audit_project_permissionchange p INNER JOIN
        // frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ? and
        // p.ouguid = ?) a ";
        String sqlNum = "select COUNT(1) from (select b.* from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT p.themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ?) a ON b.themeguid = a.themeguid where b.ouguid = ? OR b.communicationouguid =? GROUP BY b.themeguid) AS s ";
        return baseDao.queryInt(sqlNum, areaCode, ouguid, ouguid);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 获取themeguid
     * @author male
     * @date 2019年1月22日 下午2:00:47
     */
    public String getThemeguidFromAuditPP(String monitorGuid) {
        String sql = "select themeguid from audit_project_permissionchange WHERE rowguid = ? ";
        return baseDao.queryString(sql, monitorGuid);
    }

    /**
     * @return List<AuditProjectPermissionChange> 返回类型
     * @throws
     * @Description: 根据themeguid获取AuditProjectPermissionChange
     * @author male
     * @date 2019年1月22日 下午2:03:20
     */
    public List<AuditProjectPermissionChange> getAuditPPByThemeguid(String themeguid) {
        String sql2 = "select * from audit_project_permissionchange WHERE themeguid = ? ";
        return baseDao.findList(sql2, AuditProjectPermissionChange.class, themeguid);
    }

    /**
     * @return AuditProjectPermissionChange 返回类型
     * @throws
     * @Description: 根据themeguid获取AuditProjectPermissionChange第一条
     * @author male
     * @date 2019年1月22日 下午2:46:07
     */
    public AuditProjectPermissionChange getAuditPPByThemeguidOrder(String themeGuid) {
        String sql = "select * from audit_project_permissionchange where themeguid = ? ORDER BY replydate ASC LIMIT 0,1 ";
        return baseDao.find(sql, AuditProjectPermissionChange.class, themeGuid);
    }

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 获取fileclientguid
     * @author male
     * @date 2019年1月22日 下午2:23:16
     */
    public List<Record> getFileclientguidByThemeguid(String themeGuid) {
        String sql2 = "SELECT fileclientguid from audit_project_permissionchange WHERE themeguid = ? ";
        return baseDao.findList(sql2, Record.class, themeGuid);
    }

    /**
     * @return List<FrameAttachInfo> 返回类型
     * @throws
     * @Description: 根据cliengguid获取FrameAttachInfo信息
     * @author male
     * @date 2019年1月22日 下午2:27:13
     */
    public List<FrameAttachInfo> getAllFrameAttachInfoByCliengGuid(String cliengguid) {
        String sqlAttach = "SELECT * from frame_attachinfo WHERE CLIENGGUID = ? ";
        return baseDao.findList(sqlAttach, FrameAttachInfo.class, cliengguid);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 查询 fileclientguid（from audit_project_monitor）
     * @author male
     * @date 2019年1月22日 下午3:05:09
     */
    public String getFileclientguidFromMonitor(String rowguid) {
        String sql = "select fileclientguid from audit_project_monitor WHERE RowGuid = ? ";
        return baseDao.queryString(sql, rowguid);
    }

    /**
     * @return List<AuditProjectMonitorReply> 返回类型
     * @throws
     * @Description: 根据monitorno获取MonitorReply
     * @author male
     * @date 2019年1月22日 下午3:07:26
     */
    public List<AuditProjectMonitorReply> getMonitorReplyByMonitorNo(String monitorNo) {
        String sql = "select * from audit_project_monitor_reply WHERE MONITOR_NO =? ORDER BY REPLY_DATE ASC ";
        return baseDao.findList(sql, AuditProjectMonitorReply.class, monitorNo);
    }

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 获取部门信息，ChangeOpinion类
     * @author male
     * @date 2019年1月22日 下午3:15:11
     */
    public List<Record> getChangeOpinionOuInfo(String areaCode) {
        String getOuguidSql = "select distinct a.OUGUID,a.OUNAME from frame_ou a inner join frame_ou_extendinfo b on a.OUGUID=b.OUGUID inner join audit_task_jn c on a.OUGUID=c.jg_ouguid where b.AREACODE=? and c.is_hz='1' order by a.ordernumber desc ";
        return baseDao.findList(getOuguidSql, Record.class, areaCode);
    }

    /**
     * @return List<FrameUser> 返回类型
     * @throws
     * @Description: 获取部门信息 byouguid，ChangeOpinion类
     * @author male
     * @date 2019年1月22日 下午3:19:08
     */
    public List<FrameUser> getChangeOpinionFrameOu(String ouguid) {
        String sqlUser = "select DISPLAYNAME,userguid from frame_user WHERE OUGUID = ? ORDER BY ORDERNUMBER DESC";
        return baseDao.findList(sqlUser, FrameUser.class, ouguid);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Description: 根据角色名称获取userguid
     * @author male
     * @date 2019年1月22日 下午4:03:42
     */
    public String getCenterUserGuid(String roleName) {
        String sqlUserGuid = "select u.USERGUID from frame_userrolerelation u INNER JOIN frame_role r ON u.ROLEGUID = r.ROLEGUID WHERE r.ROLENAME = ? ";
        return baseDao.queryString(sqlUserGuid, roleName);
    }

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 获取部门信息2，ChangeOpinion类
     * @author male
     * @date 2019年1月22日 下午4:03:47
     */
    public List<Record> getChangeOpinionFrameOu2(String centerUserGuid, String ouGuid) {
        String sqlOuGuid = "select DISTINCT o.OUNAME,o.OUGUID from frame_ou o INNER JOIN frame_user u ON o.OUGUID = u.OUGUID WHERE u.OUGUID = ? ORDER BY o.ORDERNUMBER DESC";
        return baseDao.findList(sqlOuGuid, Record.class, ouGuid);
    }

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 根据ouguid获取frame_user信息
     * @author male
     * @date 2019年1月22日 下午5:08:15
     */
    public List<Record> getChangeOpinionFrameOu2(String ouguid) {
        String sql = "select USERGUID,DISPLAYNAME,OUGUID from frame_user WHERE OUGUID = ? ORDER BY ORDERNUMBER DESC ";
        return baseDao.findList(sql, Record.class, ouguid);
    }

    /************************************************
     * 部门审管tab数字显示
     ****************************************************/
    /**
     * 部门获取已认领数量
     *
     * @return
     */
    public int getTaJianGuanTabYrlCount(String areaCode, String ouguid, String userguid) {
        String sql = "select count(1) from (select DISTINCT p.flowsn from audit_project p INNER JOIN " +
                "(select * from audit_task_jn where jg_ouguid = '" + ouguid + "' and jg_userguid = '" + userguid + "' ) a " +
                " ON a.TASK_ID = p.TASK_ID and a.is_hz = 1 where p.status = 90 and p.Banjieresult = '40'";
        sql += " and p.areaCode = '" + areaCode + "' ";
        sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= '" + sghdstarttime + "'";
        }
        sql += " ) c ";
        return baseDao.queryInt(sql);
    }

    /**
     * 部门获取未认领数量
     *
     * @return
     */
    public int getTaJianGuanTabWrlCount(String areaCode, String ouguid, String userguid) {
        String sql = "select count(1) from (select DISTINCT p.flowsn from audit_project p INNER JOIN " +
                "(select * from audit_task_jn where jg_ouguid = '" + ouguid + "' and jg_userguid = '" + userguid + "' ) a " +
                " ON a.TASK_ID = p.TASK_ID and a.is_hz=1 where p.status = 90 and p.Banjieresult = '40' ";
        sql += " and p.areaCode = '" + areaCode + "'";
        sql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= '" + sghdstarttime + "'";
        }
        sql += " ) c ";
        return baseDao.queryInt(sql);

    }

    /**
     * 部门获取审批信息数量
     *
     * @return
     */
    public int getTaJianGuanTabSpxxCount(String sql) {
        return baseDao.queryInt(sql, AuditProject.class);
    }

    /**
     * 部门获取tab互动协助的数量
     *
     * @return
     */
    public int getTaJianGuanTabjgcount(String ouguid, String areaCode) {
        String sql = "select COUNT(1) from (select b.* from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT p.themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ?) a ON b.themeguid = a.themeguid where b.ouguid = ? OR b.communicationouguid =? GROUP BY b.themeguid) AS s ";
        return baseDao.queryInt(sql, areaCode, ouguid, ouguid);
    }

    /**
     * @return int 返回类型
     * @throws
     * @Description: 更新fileclientguid字段
     * @author male
     * @date 2019年1月22日 下午5:41:25
     */
    public int updateFileclientguid(String fileclientguid, String rowguid) {
        String sql = "UPDATE audit_project_monitor SET fileclientguid = ? WHERE rowguid = ? ";
        return baseDao.execute(sql, fileclientguid, rowguid);
    }

    /**
     * @return List<AuditProject> 返回类型
     * @throws
     * @Description: TaProjectWrlAction类
     * @author male
     * @date 2019年1月22日 下午5:53:06
     */
    public List<AuditProject> getTaProjectWrlInfo(String sql, int first, int pageSize) {
        return baseDao.findList(sql, AuditProject.class, first, pageSize);
    }

    /**
     * @return int 返回类型
     * @throws
     * @Description: TaProjectWrlAction类
     * @author male
     * @date 2019年1月22日 下午5:53:38
     */
    public int getTaProjectWrlNum(String sql) {
        return baseDao.queryInt(sql, AuditProject.class);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditProjectPermissionChange find(Object primaryKey) {
        return baseDao.find(AuditProjectPermissionChange.class, primaryKey);
    }

    /**
     * 判断是否已办结
     *
     * @param rowguid
     * @return
     */
    public Boolean judgeSign(String rowguid) {
        String sql = "select banjiesign from audit_project_permissionchange WHERE themeguid = (select themeguid from audit_project_permissionchange WHERE rowguid = ? ) ";

        List<String> list = baseDao.findList(sql, String.class, rowguid);
        boolean flag = false;
        for (String string : list) {
            if (StringUtil.isNotBlank(string)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取最新回复的url
     *
     * @param rowguid
     * @return
     */
    public String getHandleUrlByRowguid(String rowguid) {
        String sql = "select handleUrl from audit_project_permissionchange WHERE themeguid = (select themeguid from audit_project_permissionchange WHERE rowguid = ?) ORDER BY replydate DESC LIMIT 1 ";
        return baseDao.queryString(sql, rowguid);
    }

    /**
     * 根据rowguid获取audit_task ouname
     *
     * @param rowguid
     * @return
     */
    public String getOuNameFromAuditTask(String rowguid) {
        String sql = "SELECT ouname FROM audit_task WHERE rowguid = ? ";
        return baseDao.queryString(sql, rowguid);
    }

    /**
     * 根据辖区获取文件
     *
     * @param sql
     * @param first
     * @param pageSize
     * @return
     */
    public List<AuditTaskShareFile> getShareFileInfoByAreacode(String sql, int first, int pageSize) {
        return baseDao.findList(sql, AuditTaskShareFile.class, first, pageSize);
    }

    /**
     * 根据辖区获取文件
     *
     * @param sql
     * @return
     */
    public int getShareFileInfoNumByAreacode(String sql) {
        return baseDao.queryInt(sql);
    }

    /**
     * 根据辖区获取审批局信息
     *
     * @param areaCode
     * @return
     */
    public List<Record> getCenterOunameinfo(String areaCode) {
        String sql = " select f.OUNAME,f.OUGUID from frame_ou f INNER JOIN frame_ou_extendinfo e ON f.OUGUID = e.OUGUID where AREACODE =? and OUNAME LIKE '%审批服务局%' ";
        return baseDao.findList(sql, Record.class, areaCode);
    }

    /**
     * 获取二级部门
     *
     * @param guid
     * @return
     * @authory shibin
     * @version 2019年9月19日 上午10:05:42
     */
    public List<Record> findChildOuByParentguid(String guid) {
        String sql = "select OUNAME,OUGUID from frame_ou WHERE PARENTOUGUID = ? ORDER BY ORDERNUMBER DESC ";
        return baseDao.findList(sql, Record.class, guid);
    }

    public List<String> findTaskidListByUserguidAndOuguid(String userguid, String ouguid) {
        String sql = "select task_id from audit_task_jn where jg_ouguid = ? and jg_userguid = ? and is_hz = 1 ";
        return baseDao.findList(sql, String.class, ouguid, userguid);
    }
}

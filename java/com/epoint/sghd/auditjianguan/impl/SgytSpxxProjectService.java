package com.epoint.sghd.auditjianguan.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.role.api.IRoleServiceInternal;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import com.epoint.sghd.auditjianguan.vo.ProjectVO;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJnRenling;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

/**
 * 审管一体化审批信息service
 *
 * @author 刘雨雨
 * @time 2018年12月14日上午11:18:58
 */
@Service
public class SgytSpxxProjectService {

    @Inject
    private IRoleServiceInternal roleServiceInternal;

    public PageData<AuditProject> findProjectVOPage(int first, int pageSize, AuditProject param) {
        return findSpxtProjectPage(first, pageSize, param);
    }

    public PageData<AuditProject> findProjectVOPageForCenter(int first, int pageSize, AuditProject param) {
        return findCenterProjectVOPage(first, pageSize, param);
    }

    // 审批系统
    ProjectVO createProjectVO(AuditProject auditProject) {
        ProjectVO projectVO = new ProjectVO();
        projectVO.setApplydate(auditProject.getApplydate());
        projectVO.setApplyername(auditProject.getApplyername());
        projectVO.setBanjiedate(auditProject.getBanjiedate());
        projectVO.setBanjieresult(auditProject.getBanjieresult() == null ? 0: auditProject.getBanjieresult());
        projectVO.setFlowsn(auditProject.getFlowsn());
        projectVO.setOuname(auditProject.getOuname());
        projectVO.setProjectname(auditProject.getProjectname());
        projectVO.setBusinessSource(BusinessSource.SPXT.getText());
        if (StringUtil.isNotBlank(auditProject.get("renlingtime"))) {
            projectVO.setRenlingtype("已认领");
            projectVO.setRenling_username(auditProject.getStr("renling_username"));
            projectVO.setRenlingdate(auditProject.get("renlingdate"));
        } else {
            projectVO.setRenlingtype("未认领");
        }
        projectVO.setRowguid(auditProject.getRowguid());
        if ("1".equals(projectVO.getIszijianxitong())) {
            projectVO.setProjectname("【自建系统】" + projectVO.getProjectname());
        }
        return projectVO;
    }

    PageData<AuditProject> findSpxtProjectPage(int first, int pageSize, AuditProject param) {
        String fields = "p.rowguid,p.flowsn,p.projectname,p.taskguid,p.task_id,p.banjieresult,p.banjiedate,p.applydate,p.ouname,p.applyername";
        String sql = "select DISTINCT " + fields + " from audit_project p where task_id in (";
        String sqlnum = "select count(1) from audit_project p where task_id in (";
        //查询此人配置过的taskids 集合
        IJnAuditJianGuan jnAuditJianGuanService = ContainerFactory.getContainInfo().getComponent(IJnAuditJianGuan.class);
        List<String> taskidlist = jnAuditJianGuanService.findTaskidListByUserguidAndOuguid(param.getStr("jg_userguid"), param.getOuguid());
        StringBuilder taskids = new StringBuilder();
        String task_ids = "";
        if (!taskidlist.isEmpty()) {
            for (String task_id : taskidlist) {
                taskids.append("'").append(task_id).append("'").append(",");
            }
        }
        if (taskids.length() >= 1) {
            task_ids = taskids.substring(0, taskids.length() - 1);
            task_ids += ")";
        } else {
            task_ids += "' ') ";
        }

        sql = sql + task_ids;
        sqlnum = sqlnum + task_ids;

        sql += " and p.status = 90 ";
        sqlnum += " and p.status = 90 ";

        List<Object> params = new ArrayList<>();
        String areaCode = getAreaCode();
        if(StringUtil.isBlank(areaCode) && param!=null && StringUtils.isNotBlank(param.getAreacode())){
            areaCode=param.getAreacode();
        }
        sql += " and p.handleareacode like '%" + areaCode + "%'";
        sql += " and p.areaCode = ? ";
        sqlnum += " and p.handleareacode like '%" + areaCode + "%'";
        sqlnum += " and p.areaCode = ? ";
        params.add(areaCode);
        if (StringUtil.isNotBlank(param.getProjectname())) {
            String projectname = param.getProjectname();
            if ("%".equals(projectname)) {
                projectname = "\\%";
            }
            sql = sql + " and p.projectname like ? ";
            sqlnum += " and p.projectname like ? ";
            params.add("%" + projectname + "%");
        }
        if (StringUtil.isNotBlank(param.getFlowsn())) {
            String flowsn = param.getFlowsn();
            if ("%".equals(flowsn)) {
                flowsn = "\\%";
            }
            sql = sql + " and p.flowsn like ? ";
            sqlnum += " and p.flowsn like ? ";
            params.add("%" + flowsn + "%");
        }
        if (StringUtil.isNotBlank(param.getStr("renlingtype"))) {
            if ("1".equals(param.getStr("renlingtype"))) {
                // 存在认领数据
                sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
                sqlnum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
            } else {
                sql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
                sqlnum += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
            }
        }
        if (StringUtil.isNotBlank(param.getStr("renling_username"))) {
            sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like ?)";
            sqlnum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like ?)";
            params.add("%" + param.getStr("renling_username") + "%");
        }
        if (StringUtil.isNotBlank(param.getApplyername())) {
            String applyername = param.getFlowsn();
            if ("%".equals(applyername)) {
                applyername = "\\%";
            }
            sql = sql + " and p.applyername like ? ";
            sqlnum += " and p.applyername like ? ";
            params.add("%" + applyername + "%");
        }
        if (StringUtil.isNotBlank(param.get("renlingdateStart"))) {
            sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= date(?))";
            sqlnum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= date(?))";
            params.add(param.get("renlingdateStart"));
        }
        if (StringUtil.isNotBlank(param.get("renlingdateEnd"))) {
            sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= date(?))";
            sqlnum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + param.getOuguid() + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= date(?))";
            params.add(param.get("renlingdateEnd"));
        }

        sql = sql + " and p.Banjieresult = '40' ";
        sqlnum += " and p.Banjieresult = '40' ";

        if (StringUtil.isNotBlank(param.get("banjiedateStart"))) {
            sql += " and date(p.banjiedate) >= date(?)";
            sqlnum += " and date(p.banjiedate) >= date(?)";
            params.add(param.get("banjiedateStart"));
        }
        if (StringUtil.isNotBlank(param.get("banjiedateEnd"))) {
            sql += " and date(p.banjiedate) <= date(?)";
            sqlnum += " and date(p.banjiedate) <= date(?)";
            params.add(param.get("banjiedateEnd"));
        }
        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= ? ";
            sqlnum += " and p.applyDate >= ? ";
            params.add(sghdstarttime);
        }

        Object[] paramArray = params.toArray();
        CommonDao commonDao = CommonDao.getInstance();
        int rowCount = commonDao.queryInt(sqlnum, paramArray);
        sql += " order by p.applyDate desc";

        List<AuditProject> auditProjectList = commonDao.findList(sql, first, pageSize, AuditProject.class, paramArray);
        if (auditProjectList != null) {
            String renlingSql = "select * from audit_task_jn_renling where task_id=? and projectguid=? and renling_ouguid=? AND renlingdate IS NOT NULL AND renlingdate <> '' order by renlingdate desc LIMIT 1";
            for (AuditProject auditProject : auditProjectList) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.get("iszijianxitong"))) {
                    auditProject.setProjectname("【自建系统】" + auditProject.getProjectname());
                }
                auditProject.setOuname(auditProject.getOuname());

                AuditTaskJnRenling auditTaskJnRenling = commonDao.find(renlingSql, AuditTaskJnRenling.class,
                        new Object[]{auditProject.getTask_id(), auditProject.getRowguid(), param.getOuguid()});
                if (auditTaskJnRenling != null) {
                    auditProject.put("renlingtype", "已认领");
                    auditProject.put("renling_username", auditTaskJnRenling.getRenling_username());
                    auditProject.set("renlingdate", EpointDateUtil
                            .convertDate2String(auditTaskJnRenling.getRenlingdate(), EpointDateUtil.DATE_TIME_FORMAT));
                    auditProject.put("renlingtime", auditTaskJnRenling.getRenlingdate());
                } else {
                    auditProject.put("renlingtype", "未认领");
                }
            }
        }
        return new PageData<>(auditProjectList, rowCount);
    }

    PageData<AuditProject> findCenterProjectVOPage(int first, int pageSize, AuditProject param) {
        String fields = "p.rowguid,p.flowsn,p.projectname,p.taskguid,p.task_id,p.banjieresult,p.banjiedate,p.applydate,p.ouname,p.applyername,a.jg_ouguid,a.jg_ouname ";
        String sql = "select " + fields
                + " from audit_project p inner join (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a on a.task_id = p.task_id "
                + " where p.status = 90 ";
        List<Object> params = new ArrayList<>();
        String areaCode = getAreaCode();
        if(StringUtil.isBlank(areaCode) && param!=null && StringUtils.isNotBlank(param.getAreacode())){
            areaCode=param.getAreacode();
        }
        sql += " and p.handleareacode like '" + areaCode + "%'";
        sql += " and p.areaCode = '"+ areaCode +"' ";
        if (StringUtil.isNotBlank(param.getOuguid())) {
            // 优化查询速度，将条件放在join中
            sql = sql.replace("is_hz = 1", " is_hz = 1 and jg_ouguid = ? ");
            params.add(param.getOuguid());
        }
        if (StringUtil.isNotBlank(param.getStr("jg_ouname"))) {
            sql += " and a.jg_ouname like ? ";
            params.add("%" + param.getStr("jg_ouname") + "%");
        }
        if (StringUtil.isNotBlank(param.getProjectname())) {
            sql = sql + " and p.projectname like ? ";
            params.add("%" + param.getProjectname() + "%");
        }
        if (StringUtil.isNotBlank(param.getFlowsn())) {
            sql = sql + " and p.flowsn like ? ";
            params.add("%" + param.getFlowsn() + "%");
        }
        if (StringUtil.isNotBlank(param.getStr("renlingtype"))) {
            if ("1".equals(param.getStr("renlingtype"))) {
                // 存在认领数据
                sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
            } else {
                sql += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
            }
        }
        if (StringUtil.isNotBlank(param.getStr("renling_username"))) {
            sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like ?)";
            params.add("%" + param.getStr("renling_username") + "%");
        }
        if (StringUtil.isNotBlank(param.getApplyername())) {
            sql = sql + " and p.applyername like ? ";
            params.add("%" + param.getApplyername() + "%");
        }
        if (StringUtil.isNotBlank(param.get("renlingdateStart"))) {
            sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= date(?))";
            params.add(param.get("renlingdateStart"));
        }
        if (StringUtil.isNotBlank(param.get("renlingdateEnd"))) {
            sql += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= date(?))";
            params.add(param.get("renlingdateEnd"));
        }

        sql = sql + " and p.Banjieresult = '40' ";

        if (StringUtil.isNotBlank(param.get("banjiedateStart"))) {
            sql += " and date(p.banjiedate) >= date(?)";
            params.add(param.get("banjiedateStart"));
        }
        if (StringUtil.isNotBlank(param.get("banjiedateEnd"))) {
            sql += " and date(p.banjiedate) <= date(?)";
            params.add(param.get("banjiedateEnd"));
        }
        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sql += " and p.applyDate >= ? ";
            params.add(sghdstarttime);
        }

        Object[] paramArray = params.toArray();
        CommonDao commonDao = CommonDao.getInstance();
        int rowCount = commonDao.queryInt(sql.replace(fields, "count(*)"), paramArray);
        sql += " order by p.applyDate desc";
        List<AuditProject> auditProjectList = commonDao.findList(sql, first, pageSize, AuditProject.class, paramArray);
        List<ProjectVO> projectVoList = new ArrayList<>();
        if (auditProjectList != null) {
            String renlingSql = "select * from audit_task_jn_renling where task_id=? and projectguid=? and renling_ouguid=? AND renlingdate IS NOT NULL AND renlingdate <> '' order by renlingdate desc LIMIT 1";
            for (AuditProject auditProject : auditProjectList) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.get("iszijianxitong"))) {
                    auditProject.setProjectname("【自建系统】" + auditProject.getProjectname());
                }
                auditProject.setOuname(auditProject.getOuname());

                AuditTaskJnRenling auditTaskJnRenling = commonDao.find(renlingSql, AuditTaskJnRenling.class,
                        new Object[]{auditProject.getTask_id(), auditProject.getRowguid(),
                                auditProject.getStr("jg_ouguid")});
                if (auditTaskJnRenling != null) {
                    auditProject.put("renlingtype", "已认领");
                    auditProject.put("renling_username", auditTaskJnRenling.getRenling_username());
                    auditProject.set("renlingdate", EpointDateUtil
                            .convertDate2String(auditTaskJnRenling.getRenlingdate(), EpointDateUtil.DATE_TIME_FORMAT));
                    auditProject.put("renlingtime", auditTaskJnRenling.getRenlingdate());
                } else {
                    auditProject.put("renlingtype", "未认领");
                }
                /*ProjectVO projectVO = createProjectVO(auditProject);
                projectVoList.add(projectVO);*/
            }
        }
        return new PageData<>(auditProjectList, rowCount);
    }

    private String getAreaCode() {
        // 如果是镇村接件
        ZwfwUserSession userSession = ZwfwUserSession.getInstance();
        if (userSession.getCitylevel() != null
                && (Integer.parseInt(userSession.getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            return userSession.getBaseAreaCode();
        } else {
            return userSession.getAreaCode();
        }
    }

    /**
     * 只要满足一个角色，就返回true，都不满足返回false
     *
     * @param roleNames
     * @return
     */
    public boolean isRoleByRoleName(String... roleNames) {
        String userGuid = UserSession.getInstance().getUserGuid();
        Set<String> roleNameSet = new HashSet<>();
        List<Object[]> roles = roleServiceInternal.getRoleGuidAndRoleNameByUserGuid(userGuid);
        if (roles != null) {
            for (Object[] objects : roles) {
                roleNameSet.add(objects[1].toString());
            }
        }
        for (String roleName : roleNames) {
            if (roleNameSet.contains(roleName)) {
                return true;
            }
        }
        return false;
    }

    enum BusinessSource {

        SPXT("10", "审批系统"), CITY_SELF("20", "市级自建"), PROVINCE_SELF("30", "省级自建"), OTHER("99", "其他");

        private String value;

        private String text;

        private BusinessSource(String value, String text) {
            this.value = value;
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * 部门-审管互动-未认领
     * @param first
     * @param pageSize
     * @return
     */
    public PageData<AuditProject> findWlrProjectVOPage(int first, int pageSize, AuditProject dataBean) {
        IJnAuditJianGuan jnAuditJianGuanService = ContainerFactory.getContainInfo().getComponent(IJnAuditJianGuan.class);
        String sortField = " applyDate";
        String sortOrder = "desc";

        String sqlFind = "select DISTINCT p.flowsn,p.projectname,p.rowguid,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid from audit_project p where task_id in (";
        String sqlNum = "select count(1) from audit_project p where task_id in (";
        String userguid = dataBean.get("jg_userguid");

        //查询此人配置过的taskids 集合
        List<String> taskidlist = jnAuditJianGuanService.findTaskidListByUserguidAndOuguid(userguid, dataBean.getOuguid());
        StringBuilder taskids = new StringBuilder();
        String task_ids = "";
        if (!taskidlist.isEmpty()) {
            for (String task_id : taskidlist) {
                taskids.append("'").append(task_id).append("'").append(",");
            }
        }
        if (taskids.length() >= 1) {
            task_ids = taskids.substring(0, taskids.length() - 1);
            task_ids += ")";
        } else {
            task_ids += "' ') ";
        }

        sqlFind = sqlFind + task_ids;
        sqlNum = sqlNum + task_ids;

        String handleareacode = dataBean.getAreacode();
        sqlFind = sqlFind + " and p.handleareacode like '%" + handleareacode + "%' ";
        sqlNum = sqlNum + " and p.handleareacode like '%" + handleareacode + "%' ";

        // 如果是镇村接件
        String area=dataBean.getAreacode();
        sqlFind = sqlFind + " and p.areaCode = " + area + " ";
        sqlNum = sqlNum + " and p.areaCode = " + area + " ";

        //String key = "";

        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
            //处理百分号
            if ("%".equals(dataBean.getProjectname())) {
                sqlFind = sqlFind + " and p.projectname like '%  " + "\\%" + "%' ";
                sqlNum = sqlNum + " and p.projectname like '%  " + "\\%" + "%' ";
                //key += "\\%&";
            } else {
                sqlFind = sqlFind + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                sqlNum = sqlNum + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                //key += dataBean.getProjectname() + "&";
            }
        }
        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
            //处理百分号
            if ("%".equals(dataBean.getFlowsn())) {
                sqlFind = sqlFind + " and p.flowsn like '%" + "\\%" + "%' ";
                sqlNum = sqlNum + " and p.flowsn like '%" + "\\%" + "%' ";
                //key += "\\%&";
            } else {
                sqlFind = sqlFind + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                sqlNum = sqlNum + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                //key += dataBean.getFlowsn() + "&";
            }
        }
        if (StringUtil.isNotBlank(dataBean.get("banjiedateStart"))) {
            Date banjiedateStart = EpointDateUtil.getBeginOfDate(dataBean.getDate("banjiedateStart"));
            sqlFind += " and date(p.banjiedate) >= '" + EpointDateUtil.convertDate2String(banjiedateStart)
                    + "'";
            sqlNum += " and date(p.banjiedate) >= '" + EpointDateUtil.convertDate2String(banjiedateStart)
                    + "'";
            //key += EpointDateUtil.convertDate2String(banjiedateStart) + "&";
        }
        if (StringUtil.isNotBlank(dataBean.get("banjiedateEnd"))) {
            Date banjiedateEnd = EpointDateUtil.getBeginOfDate(dataBean.getDate("banjiedateEnd"));
            sqlFind += " and date(p.banjiedate) <= '" + EpointDateUtil.convertDate2String(banjiedateEnd)
                    + "'";
            sqlNum += " and date(p.banjiedate) <= '" + EpointDateUtil.convertDate2String(banjiedateEnd)
                    + "'";
            //key += EpointDateUtil.convertDate2String(banjiedateEnd) + "&";
        }

        //加标识位，未认领模块
        //key += "wrl";

        sqlFind = sqlFind + " and p.status = 90 ";
        sqlNum = sqlNum + " and p.status = 90 ";
        sqlFind = sqlFind + " and p.Banjieresult = '40' ";
        sqlNum = sqlNum + " and p.Banjieresult = '40' ";
        // 认领时间为空
        sqlFind += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + dataBean.getOuguid()
                + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        sqlNum += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + dataBean.getOuguid()
                + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }

        sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

        List<AuditProject> list = new ArrayList<>();
                    /*List<AuditProject> cachelist = EHCacheUtil.get(key);
                    if (cachelist != null) {
                        list = cachelist;
                    } else {
                        list = jnAuditJianGuanService.getTaProjectWrlInfo(sqlFind, first, pageSize);
                        EHCacheUtil.put(key, list, 60000);
                    }*/

        list = jnAuditJianGuanService.getTaProjectWrlInfo(sqlFind, first, pageSize);

        int count = 0;
        if (list != null) {
            for (AuditProject auditProject : list) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.get("iszijianxitong"))) {
                    auditProject.setProjectname("【自建系统】" + auditProject.getProjectname());
                }

                // 新增字段“部门名称”的显示
                String taskguid = auditProject.getTaskguid();
                auditProject.put("ouname", jnAuditJianGuanService.getOuNameFromAuditTask(taskguid));
            }
            count = jnAuditJianGuanService.getTaProjectWrlNum(sqlNum);
        }
        return new PageData<>(list, count);
    }

    /**
     * 中心-审管互动-未认领
     * @param first
     * @param pageSize
     * @return
     */
    public PageData<AuditProject> findWlrProjectVOPageForCenter(int first, int pageSize, AuditProject dataBean) {
        IJnAuditJianGuanCenter jnAuditJianGuanCenter = ContainerFactory.getContainInfo().getComponent(IJnAuditJianGuanCenter.class);
        String sortField = " applyDate";
        String sortOrder = "desc";
        String sqlFind = "select p.flowsn,p.projectname,p.rowguid,p.task_id,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid,a.jg_ouguid,a.jg_ouname from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where  ";
        String sqlNum = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where  ";

        String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
        if(StringUtil.isBlank(handleareacode) && dataBean!=null && StringUtils.isNotBlank(dataBean.getAreacode())){
            handleareacode=dataBean.getAreacode();
        }
        sqlFind = sqlFind + " p.handleareacode like '" + handleareacode + "%' ";
        sqlNum = sqlNum + " p.handleareacode like '" + handleareacode + "%' ";

        // 如果是镇村接件
        String area;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        if(StringUtil.isBlank(area)&&dataBean!=null && StringUtils.isNotBlank(dataBean.getAreacode())){
            area=dataBean.getAreacode();
        }
        sqlFind = sqlFind + " and p.areaCode = " + area + " ";
        sqlNum = sqlNum + " and p.areaCode = " + area + " ";
        sqlFind = sqlFind + " and p.Banjieresult = '40' ";
        sqlNum = sqlNum + " and p.Banjieresult =  '40' ";

        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
            sqlFind = sqlFind + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
            sqlNum = sqlNum + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
            sqlFind = sqlFind + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
            sqlNum = sqlNum + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
            sqlFind = sqlFind + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
            sqlNum = sqlNum + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getStr("jg_ouname"))) {
            sqlFind = sqlFind + " and a.jg_ouname like '%" + dataBean.getStr("jg_ouname") + "%' ";
            sqlNum = sqlNum + " and a.jg_ouname like '%" + dataBean.getStr("jg_ouname") + "%' ";
        }

        sqlFind = sqlFind + " and p.status = 90 ";
        sqlNum = sqlNum + " and p.status = 90 ";

        // 认领时间为空
                    /*sqlFind = sqlFind + " and p.renlingtime is NULL  ";
                    sqlNum = sqlNum + " and p.renlingtime is NULL  ";*/
        sqlFind += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        sqlNum += " and not EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }

        sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

        List<AuditProject> list = new ArrayList<>();
        list = jnAuditJianGuanCenter.getProejctCenter(sqlFind, first, pageSize);

        int count = jnAuditJianGuanCenter.getProejctCenterNum(sqlNum);

        return new PageData<>(list, count);
    }

    /**
     * 部门-审管互动-已认领
     * @param first
     * @param pageSize
     * @return
     */
    public PageData<AuditProject> findYrlProjectVOPage(int first, int pageSize, AuditProject dataBean) {
        IJnAuditJianGuan jnAuditJianGuanService = ContainerFactory.getContainInfo().getComponent(IJnAuditJianGuan.class);
        CommonDao commonDao = CommonDao.getInstance();
        String sortField = " applyDate";
        String sortOrder = "desc";

        String sqlFind = "select DISTINCT p.flowsn,p.projectname,p.rowguid,p.task_id,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid from audit_project p where task_id in (";
        String sqlNum = "select count(1) from audit_project p where task_id in (";
        String ouGuid = dataBean.getOuguid();
        String userguid = dataBean.get("jg_userguid");

        //查询此人配置过的taskids 集合
        List<String> taskidlist = jnAuditJianGuanService.findTaskidListByUserguidAndOuguid(userguid, ouGuid);
        StringBuilder taskids = new StringBuilder();
        String task_ids = "";
        if (!taskidlist.isEmpty()) {
            for (String task_id : taskidlist) {
                taskids.append("'").append(task_id).append("'").append(",");
            }
        }
        if (taskids.length() >= 1) {
            task_ids = taskids.substring(0, taskids.length() - 1);
            task_ids += ")";
        } else {
            task_ids += "' ') ";
        }

        sqlFind = sqlFind + task_ids;
        sqlNum = sqlNum + task_ids;

        String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
        if(StringUtil.isBlank(handleareacode) && dataBean!=null && StringUtils.isNotBlank(dataBean.getAreacode())){
            handleareacode=dataBean.getAreacode();
        }
        sqlFind = sqlFind + " and p.handleareacode like '%" + handleareacode + "%' ";
        sqlNum = sqlNum + " and p.handleareacode like '%" + handleareacode + "%' ";

        // 如果是镇村接件
        String area;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        if(StringUtil.isBlank(area) && dataBean!=null && StringUtils.isNotBlank(dataBean.getAreacode())){
            area=dataBean.getAreacode();
        }
        sqlFind = sqlFind + " and p.areaCode = " + area + " ";
        sqlNum = sqlNum + " and p.areaCode = " + area + " ";

        //String key = "";

        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
            if ("%".equals(dataBean.getApplyername())) {
                sqlFind = sqlFind + " and p.applyername like '%" + "\\%" + "%' ";
                sqlNum = sqlNum + " and p.applyername like '%" + "\\%" + "%' ";
                //key += "\\%&";
            } else {
                sqlFind = sqlFind + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
                sqlNum = sqlNum + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
                //key += dataBean.getApplyername() + "&";
            }
        }

        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
            //处理百分号
            if ("%".equals(dataBean.getProjectname())) {
                sqlFind = sqlFind + " and p.projectname like '%  " + "\\%" + "%' ";
                sqlNum = sqlNum + " and p.projectname like '%  " + "\\%" + "%' ";
                //key += "\\%&";
            } else {
                sqlFind = sqlFind + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                sqlNum = sqlNum + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                //key += dataBean.getProjectname() + "&";
            }
        }
        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
            //处理百分号
            if ("%".equals(dataBean.getFlowsn())) {
                sqlFind = sqlFind + " and p.flowsn like '%" + "\\%" + "%' ";
                sqlNum = sqlNum + " and p.flowsn like '%" + "\\%" + "%' ";
                //key += "\\%&";
            } else {
                sqlFind = sqlFind + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                sqlNum = sqlNum + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                //key += dataBean.getFlowsn() + "&";
            }
        }

        if (StringUtil.isNotBlank(dataBean.getStr("renling_username"))) {
            sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                    + dataBean.getStr("renling_username") + "%')";
            sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                    + dataBean.getStr("renling_username") + "%')";
            //key += dataBean.getStr("renling_username") + "&";
        }

        if (StringUtil.isNotBlank(dataBean.get("renlingdateStart"))) {
            Date renlingdateStart = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateStart"));
            sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                    + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
            sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                    + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
            //key += EpointDateUtil.convertDate2String(renlingdateStart) + "&";
        }

        if (StringUtil.isNotBlank(dataBean.get("renlingdateEnd"))) {
            Date renlingdateEnd = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateEnd"));
            sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                    + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
            sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                    + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
            //key += EpointDateUtil.convertDate2String(renlingdateEnd) + "&";
        }

        //加标识位，已认领模块
        //key += "yrl";

        sqlFind = sqlFind + " and p.status = 90 ";
        sqlNum = sqlNum + " and p.status = 90 ";

        // 认领时间不为空
        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }

        sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

        List<AuditProject> list = new ArrayList<>();

                    /*List<AuditProject> cachelist = EHCacheUtil.get(key);
                    if (cachelist != null) {
                        list = cachelist;
                    } else {
                        list = jnAuditJianGuanService.getTaProjectWrlInfo(sqlFind, first, pageSize);
                        EHCacheUtil.put(key, list, 60000);
                    }*/

        list = jnAuditJianGuanService.getTaProjectWrlInfo(sqlFind, first, pageSize);

        int count = 0;
        if (list != null) {
            String renlingSql = "select * from audit_task_jn_renling where task_id=? and projectguid=? and renling_ouguid=? AND renlingdate IS NOT NULL AND renlingdate <> '' order by renlingdate desc LIMIT 1";
            for (AuditProject auditProject : list) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.get("iszijianxitong"))) {
                    auditProject.setProjectname("【自建系统】" + auditProject.getProjectname());
                }

                // 新增字段“部门名称”的显示
                String taskguid = auditProject.getTaskguid();
                auditProject.put("ouname", jnAuditJianGuanService.getOuNameFromAuditTask(taskguid));
                if (StringUtil.isBlank(auditProject.getStr("jianguantime"))) {
                    auditProject.put("flag", "true");
                }

                AuditTaskJnRenling auditTaskJnRenling = commonDao.find(renlingSql,AuditTaskJnRenling.class,
                        new Object[]{auditProject.getTask_id(), auditProject.getRowguid(), ouGuid});
                if (auditTaskJnRenling != null) {
                    auditProject.put("renling_username", auditTaskJnRenling.getRenling_username());
                    auditProject.set("renlingdate", EpointDateUtil.convertDate2String(
                            auditTaskJnRenling.getRenlingdate(), EpointDateUtil.DATE_TIME_FORMAT));
                }
            }
            count = jnAuditJianGuanService.getTaProjectWrlNum(sqlNum);
        }
        return new PageData<>(list, count);
    }

    /**
     * 中心-审管互动-已认领
     * @param first
     * @param pageSize
     * @return
     */
    public PageData<AuditProject> findYrlProjectVOPageForCenter(int first, int pageSize, AuditProject dataBean) {
        IJnAuditJianGuanCenter jnAuditJianGuanCenter = ContainerFactory.getContainInfo().getComponent(IJnAuditJianGuanCenter.class);
        CommonDao commonDao = CommonDao.getInstance();
        String sortField = " applyDate";
        String sortOrder = "desc";

        String sqlFind = "select p.flowsn,p.projectname,p.rowguid,p.task_id,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid,a.jg_ouguid,a.jg_ouname from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where  ";
        String sqlNum = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where ";

        String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
        if(StringUtil.isBlank(handleareacode) && dataBean!=null && StringUtils.isNotBlank(dataBean.getAreacode())){
            handleareacode=dataBean.getAreacode();
        }
        sqlFind = sqlFind + "  p.handleareacode like '" + handleareacode + "%' ";
        sqlNum = sqlNum + " p.handleareacode like '" + handleareacode + "%' ";

        // 如果是镇村接件
        String area;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        if(StringUtil.isBlank(area) && dataBean!=null && StringUtils.isNotBlank(dataBean.getAreacode())){
            area=dataBean.getAreacode();
        }
        sqlFind = sqlFind + " and p.areaCode = " + area + " ";
        sqlNum = sqlNum + " and p.areaCode = " + area + " ";

        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
            sqlFind = sqlFind + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
            sqlNum = sqlNum + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
            sqlFind = sqlFind + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
            sqlNum = sqlNum + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
            sqlFind = sqlFind + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
            sqlNum = sqlNum + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getStr("jg_ouname"))) {
            sqlFind = sqlFind + " and a.jg_ouname like '%" + dataBean.getStr("jg_ouname") + "%' ";
            sqlNum = sqlNum + " and a.jg_ouname like '%" + dataBean.getStr("jg_ouname") + "%' ";
        }
        if (StringUtil.isNotBlank(dataBean.getStr("renling_username"))) {
            sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                    + dataBean.getStr("renling_username") + "%')";
            sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                    + dataBean.getStr("renling_username") + "%')";
        }
        if (StringUtil.isNotBlank(dataBean.get("renlingdateStart"))) {
            Date renlingdateStart = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateStart"));
            sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                    + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
            sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                    + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
        }
        if (StringUtil.isNotBlank(dataBean.get("renlingdateEnd"))) {
            Date renlingdateEnd = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateEnd"));
            sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                    + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
            sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                    + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
        }

        sqlFind = sqlFind + " and p.status = 90 ";
        sqlNum = sqlNum + " and p.status = 90 ";

        // 认领时间不为空
                    /*sqlFind = sqlFind + " and p.renlingtime is not NULL ";
                    sqlNum = sqlNum + " and p.renlingtime is not NULL ";*/
        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }

        sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

        List<AuditProject> list = new ArrayList<AuditProject>();
        list = jnAuditJianGuanCenter.getProejctCenter(sqlFind, first, pageSize);

        int count = 0;
        if (list != null) {
            String renlingSql = "select * from audit_task_jn_renling where task_id=? and projectguid=? and renling_ouguid=? AND renlingdate IS NOT NULL AND renlingdate <> '' order by renlingdate desc LIMIT 1";
            for (AuditProject auditProject : list) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.get("iszijianxitong"))) {
                    auditProject.setProjectname("【自建系统】" + auditProject.getProjectname());
                }

                // 新增字段“部门名称”的显示
                String taskguid = auditProject.getTaskguid();
                auditProject.put("ouname", jnAuditJianGuanCenter.getOuNameFromAuditTask(taskguid));

                AuditTaskJnRenling auditTaskJnRenling = commonDao.find(renlingSql,AuditTaskJnRenling.class,
                        new Object[] {auditProject.getTask_id(), auditProject.getRowguid(),
                                auditProject.getStr("jg_ouguid") });
                if (auditTaskJnRenling != null) {
                    auditProject.put("renlingtype", "已认领");
                    auditProject.put("renling_username", auditTaskJnRenling.getRenling_username());
                    auditProject.set("renlingdate", EpointDateUtil.convertDate2String(
                            auditTaskJnRenling.getRenlingdate(), EpointDateUtil.DATE_TIME_FORMAT));
                    auditProject.put("renlingtime", auditTaskJnRenling.getRenlingdate());
                }
                else {
                    auditProject.put("renlingtype", "未认领");
                }
            }
            count = jnAuditJianGuanCenter.getProejctCenterNum(sqlNum);
        }
        return new PageData<>(list, count);
    }

}

package com.epoint.basic.auditproject.auditproject.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProReport;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectCK;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTJ;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTaskName;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTaskType;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectUser;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditproject.service.ProjectService;
import com.epoint.basic.auditproject.service.AuditProjectService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.database.peisistence.procedure.ProcedureService;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

@Component
@Service
public class JNAuditProjectImpl implements IJNAuditProject {
    /**
     * 产品代码版本 7.10.20200328
     * 第一次修改，办理工作台添加预审不通过筛选条件 by 赵炎 JN20200331001
     */
    private Logger log = LogUtil.getLog(JNAuditProjectImpl.class);

    @Override
    public AuditCommonResult<String> addProject(AuditProject dataBean) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditProjectService.addRecord(AuditProject.class, dataBean);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        // auditProjectService.closeConnection();
        return result;
    }

    @Override
    public AuditCommonResult<Void> deleteProjectByGuid(String rowGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("rowguid", rowGuid);
            sql.eq("areacode", areaCode);
            auditProjectService.deleteRecodByMap(AuditProject.class, sql.getMap());
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        // auditProjectService.closeConnection();
        return result;
    }

    @Override
    public AuditCommonResult<Void> deleteUselessProject(String ProjectStatus, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("status", ProjectStatus);
            sql.eq("areacode", areaCode);
            auditProjectService.deleteRecodByMap(AuditProject.class, sql.getMap());
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        // auditProjectService.closeConnection();
        return result;
    }

    @Override
    public AuditCommonResult<String> updateProject(AuditProject dataBean) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditProjectService.updateRecord(AuditProject.class, dataBean);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String fieldstr,
                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                             String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();
            projectList = auditProjectService.getRecordPageData(fieldstr, AuditProject.class, conditionMap, firstResult,
                    maxResults, sortField, sortOrder, "");
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByHandleareacode(String fieldstr,
                                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                                             String sortOrder, String handlearracode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();
            projectList = auditProjectService.getRecordPageDataByHandleareacode(fieldstr, AuditProject.class, conditionMap, firstResult,
                    maxResults, sortField, sortOrder, "", handlearracode);
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String fieldstr, String rowGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("rowguid", rowGuid);
            if (StringUtil.isNotBlank(areaCode)) {
                sql.eq("areacode", areaCode);
            }
            sql.setSelectFields(fieldstr);
            AuditProject auditProject = auditProjectService.getDetail(AuditProject.class, sql.getMap());
            result.setResult(auditProject);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        // auditProjectService.closeConnection();
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String fieldstr, String flowsn, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("flowsn", flowsn);
            if (StringUtil.isNotBlank(areaCode)) {
                sql.eq("areacode", areaCode);
            }
            sql.setSelectFields(fieldstr);
            AuditProject auditProject = auditProjectService.getDetail(AuditProject.class, sql.getMap());
            result.setResult(auditProject);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String fieldstr, String pviGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            AuditProject auditProject = auditProjectService.getDetail(fieldstr, AuditProject.class, pviGuid, "pviguid");
            result.setResult(auditProject);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getMaxOrgNumberinfo(String orgword, String rowguid, String areaCode,
                                                          String task_id) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        ProjectService projectService = new ProjectService();
        try {
            result.setResult(projectService.getMaxOrgNumberinfo(orgword, rowguid, areaCode, task_id));
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getFieldList(String fieldstr, String grouporsortstr,
                                                              String windowguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            List<AuditProject> auditProjectByfield = auditProjectService.getAuditProjectFieldList(fieldstr,
                    grouporsortstr, windowguid);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getField(String fieldstr, String grouporsortstr, String whereStr) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            AuditProject auditProjectByfield = auditProjectService.getAuditProjectField(fieldstr, grouporsortstr,
                    whereStr);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(String fieldstr, List<String> taskidList,
                                                                          String projectType, String windowguid, int first, int pageSize, String areaCode, String applyerName,
                                                                          Date datestart, Date dateend) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        String taskids = "";
        if (taskidList != null) {
            for (String taskidExp : taskidList) {
                taskids += "'" + taskidExp + "',";
            }
            if (StringUtil.isNotBlank(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }
        try {
            PageData<AuditProject> auditProjectByfield = auditProjectService.getBanJianListByPageAndTaskids(fieldstr,
                    projectType, taskids, first, pageSize, areaCode, applyerName, datestart, dateend, windowguid);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuid(String fieldstr, List<String> taskidList,
                                                                                String projectType, String windowguid, int first, int pageSize, String areaCode, String centerGuid, String applyerName,
                                                                                Date datestart, Date dateend) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        String taskids = "";
        if (taskidList != null) {
            for (String taskidExp : taskidList) {
                taskids += "'" + taskidExp + "',";
            }
            if (StringUtil.isNotBlank(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }
        try {
            PageData<AuditProject> auditProjectByfield = auditProjectService.getBanJianListByPageAndTaskidsAndCenterGuid(fieldstr,
                    projectType, taskids, first, pageSize, areaCode, centerGuid, applyerName, datestart, dateend, windowguid);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }


    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuidAndAreacode(String fieldstr, List<String> taskidList,
                                                                                           String projectType, String windowguid, int first, int pageSize, String areaCode, String centerGuid, String applyerName,
            Date datestart, Date dateend, String realAreacode, String projectname, String commitnum, String sortField,
            String sortOrder) {
        //济宁个性化————办理工作台添加预审不通过筛选条件 by 赵炎 JN20200331001
        JNAuditProjectService auditProjectService = new JNAuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        String taskids = "";
        if (taskidList != null) {
            for (String taskidExp : taskidList) {
                taskids += "'" + taskidExp + "',";
            }
            if (StringUtil.isNotBlank(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }
        try {
            PageData<AuditProject> auditProjectByfield = auditProjectService.getBanJianListByPageAndTaskidsAndCenterGuidAndAreacode(fieldstr,
                            projectType, taskids, first, pageSize, areaCode, centerGuid, applyerName, datestart,
                            dateend, windowguid, realAreacode, projectname, commitnum, sortField, sortOrder);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getBanJianList(String fieldstr, List<String> taskGuidList,
                                                                String projectType, String windowguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            List<AuditProject> auditProjectByfield = auditProjectService.getBanJianList(fieldstr, taskGuidList,
                    projectType, windowguid);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    /**
     * 不进驻中心办件
     *
     * @param fieldstr
     * @param ouGuidList
     * @param first
     * @param pageSize
     * @param areaCode
     * @param applyerName
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<PageData<AuditProject>> getJZListByPage(String fieldstr, List<String> ouGuidList,
                                                                     int first, int pageSize, String areaCode, String applyerName) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        String ouGuids = "";
        if (ouGuidList != null) {
            for (String ouguidExp : ouGuidList) {
                ouGuids += "'" + ouguidExp + "',";
            }
            if (StringUtil.isNotBlank(ouGuids)) {
                ouGuids = ouGuids.substring(0, ouGuids.length() - 1);
            }
        }
        try {
            PageData<AuditProject> auditProjectByfield = auditProjectService.getJZListByPage(fieldstr, ouGuids, first,
                    pageSize, areaCode, applyerName);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    /**
     * 不进驻中心办件
     *
     * @param fieldstr
     * @param ouGuidList
     * @param areaCode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<List<AuditProject>> getJZList(String fieldstr, List<String> ouGuidList, String areaCode) {
        JNAuditProjectService auditProjectService = new JNAuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        String ouGuids = "";
        if (ouGuidList != null) {
            for (String ouguidExp : ouGuidList) {
                ouGuids += "'" + ouguidExp + "',";
            }
            if (StringUtil.isNotBlank(ouGuids)) {
                ouGuids = ouGuids.substring(0, ouGuids.length() - 1);
            }
        }
        try {
            List<AuditProject> auditProjectByfield = auditProjectService.getJZList(fieldstr, ouGuids, areaCode);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    public int getJzListCount(List<String> ouGuidList, String areaCode) {
        String ouGuids = "";
        if (ouGuidList != null) {
            for (String ouguidExp : ouGuidList) {
                ouGuids += "'" + ouguidExp + "',";
            }
            if (StringUtil.isNotBlank(ouGuids)) {
                ouGuids = ouGuids.substring(0, ouGuids.length() - 1);
            }
        }
        return new JNAuditProjectService().getJzListCount(ouGuids, areaCode);
    }
    public int getNotJZCount(List<String> ouGuidList, String areaCode) {
        String ouGuids = "";
        if (ouGuidList != null) {
            for (String ouguidExp : ouGuidList) {
                ouGuids += "'" + ouguidExp + "',";
            }
            if (StringUtil.isNotBlank(ouGuids)) {
                ouGuids = ouGuids.substring(0, ouGuids.length() - 1);
            }
        }
        return new JNAuditProjectService().getNotJZCount(ouGuids, areaCode);
    }

    @Override
    public AuditCommonResult<Void> updateProject(Map<String, String> updateFieldMap, Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            auditProjectService.updateRecord(AuditProject.class, updateFieldMap, conditionMap);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguid(List<String> taskidList,
                                                                              String windowguid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Map<String, Integer>> result = new AuditCommonResult<Map<String, Integer>>();
        try {
            Map<String, Integer> auditProjectByfield = auditProjectService.getCountStatusByWindowguid(taskidList,
                    windowguid, areaCode);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByCenterguid(List<String> taskidList,
                                                                              String windowGuid, String centerGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Map<String, Integer>> result = new AuditCommonResult<Map<String, Integer>>();
        try {
            Map<String, Integer> auditProjectByfield = auditProjectService.getCountStatusByCenterguid(taskidList,
                    windowGuid, centerGuid, areaCode);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }


    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguid(List<String> taskidList,
                                                                                           String windowguid, String areaCode, String centerGuid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Map<String, Integer>> result = new AuditCommonResult<Map<String, Integer>>();
        try {
            Map<String, Integer> auditProjectByfield = auditProjectService.getCountStatusByWindowguidAndCenterguid(taskidList,
                    windowguid, areaCode, centerGuid);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguidAndAreacode(List<String> taskidList,
                                                                                                      String windowguid, String areaCode, String centerGuid, String baseareacode) {
        JNAuditProjectService auditProjectService = new JNAuditProjectService(AuditProject.class);
        AuditCommonResult<Map<String, Integer>> result = new AuditCommonResult<Map<String, Integer>>();
        try {
            Map<String, Integer> auditProjectByfield = auditProjectService.getCountStatusByWindowguidAndCenterguidAndAreacode(taskidList,
                    windowguid, areaCode, centerGuid, baseareacode);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String fieldstr, String projectType,
                                                                          String accountguid, int first, int pageSize, String sortField, String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(accountguid)) {
                sql.eq("applyeruserguid", accountguid);
            }
            if (StringUtil.isNotBlank(projectType)) {
                sql.eq("syatus", projectType);
            }
            @SuppressWarnings("unchecked")
            List<AuditProject> auditProjectByfield = (List<AuditProject>) auditProjectService.getRecordPageData(
                    fieldstr, AuditProject.class, sql.getMap(), first, pageSize, sortField, sortOrder, "");
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> selectHotTaskId() {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> list = auditProjectService.selectHotTaskId();
            result.setResult(list);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(String fields,
                                                                                Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            if (!SQLManageUtil.validate(conditionMap)) {
                Logger.getLogger(JNAuditProjectImpl.class).error("未传入任何条件！");
                return result;
            }
            List<AuditProject> auditProjects = new ArrayList<AuditProject>();
            auditProjects = auditProjectService.selectRecordList(AuditProject.class, conditionMap);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        // auditProjectService.closeConnection();
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String fieldstr,
                                                                                    Map<String, String> conditionMap, Integer ismaterial, Integer firstResult, Integer maxResults,
                                                                                    String sortField, String sortOrder) {
        ProjectService projectService = new ProjectService();
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> taskList = projectService.getAuditProjectPageDataMonitor(fieldstr, conditionMap,
                    ismaterial, firstResult, maxResults, sortField, sortOrder);
            result.setResult(taskList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCSCount(String windowGuid, String areaCode) {
        ProjectService projectService = new ProjectService();
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            result.setResult(projectService.getCSCount(windowGuid, areaCode));
        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
            result.setSystemFail(e.toString());
        }
        return result;
    }

    // 通过字段参数来select
    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(String fields,
                                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                                        String sortOrder, String keyword) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();
            projectList = auditProjectService.getRecordPageData(fields, AuditProject.class, conditionMap, firstResult,
                    maxResults, sortField, sortOrder, keyword);
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(Map<String, String> conditionMap,
                                                                             Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();

            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            projectList = sqlManageUtil.getDbListByPage(AuditProject.class, conditionMap, firstResult, maxResults,
                    sortField, sortOrder);
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            List<AuditProject> auditProjects = new ArrayList<AuditProject>();
            auditProjects = auditProjectService.selectRecordList(AuditProject.class, conditionMap);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String rowguid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("rowguid", rowguid);
            if (StringUtil.isNotBlank(areaCode)) {
                sql.eq("areacode", areaCode);
            }
            AuditProject auditProject = auditProjectService.getDetail(AuditProject.class, sql.getMap());
            result.setResult(auditProject);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String flowsn, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("flowsn", flowsn);
            if (StringUtil.isNotBlank(areaCode)) {
                sql.eq("areacode", areaCode);
            }
            AuditProject auditProject = auditProjectService.getDetail(AuditProject.class, sql.getMap());
            result.setResult(auditProject);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String pviGuid, String areaCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(List<String> taskGuidList, String projectType,
                                                                          String windowguid, int first, int pageSize, String areaCode, String applyerName) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        String taskGuids = "";
        if (taskGuidList != null) {
            for (String taskGuidExp : taskGuidList) {
                taskGuids += "'" + taskGuidExp + "',";
            }
            if (StringUtil.isNotBlank(taskGuids)) {
                taskGuids = taskGuids.substring(0, taskGuids.length() - 1);
            }
        }

        try {
            PageData<AuditProject> auditProjectByfield = auditProjectService.getBanJianListByPage("*", projectType,
                    taskGuids, first, pageSize, areaCode, applyerName);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getBanJianList(List<String> taskGuidList, String projectType,
                                                                String windowguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            List<AuditProject> auditProjectByfield = auditProjectService.getBanJianList("*", taskGuidList, projectType,
                    windowguid);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String projectType, String accountguid,
                                                                          int first, int pageSize, String sortField, String sortOrder) {
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(accountguid)) {
                sql.eq("applyeruserguid", accountguid);
            }
            if (StringUtil.isNotBlank(projectType)) {
                sql.eq("syatus", projectType);
            }
            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            @SuppressWarnings("unchecked")
            List<AuditProject> auditProjectByfield = (List<AuditProject>) sqlManageUtil
                    .getDbListByPage(AuditProject.class, sql.getMap(), first, pageSize, sortField, sortOrder);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(Map<String, String> conditionMap,
                                                                                    Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        ProjectService projectService = new ProjectService();
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> taskList = projectService.getAuditProjectPageDataMonitor("*", conditionMap,
                    ismaterial, firstResult, maxResults, sortField, sortOrder);
            result.setResult(taskList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(
            Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
            String sortOrder) {
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();
            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            projectList = sqlManageUtil.getDbListByPage(AuditProject.class, conditionMap, firstResult, maxResults,
                    sortField, sortOrder);
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteProject(String projectGuid, String flownsn, String pviguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            Object[] args = {projectGuid, flownsn, pviguid};
            ProcedureService procedureservice = new ProcedureService(new DataSourceConfig());
            procedureservice.beginTransaction();
            procedureservice.executeProcudure("ASP_DELETEPROJECT", args);
            procedureservice.commitTransaction();
            procedureservice.close();
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getReportCount(String ouguid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            Map<String, String> auditProjectByfield = auditProjectService.getReportCount(ouguid, areaCode);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getReportCount(String ouguid, String areaCode, String startDate,
                                                                 String endDate) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            Map<String, String> auditProjectByfield = auditProjectService.getReportCount(ouguid, areaCode, startDate,
                    endDate);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getReportOu(String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjectByfield = auditProjectService.getReportOu(areaCode);
            result.setResult(auditProjectByfield);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getSyncProjectList(Map<String, String> conditionMap) {
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        try {
            if (!SQLManageUtil.validate(conditionMap)) {
                Logger.getLogger(JNAuditProjectImpl.class).error("未传入任何条件！");
                return result;
            }
            List<AuditProject> auditProjects = new ArrayList<AuditProject>();
            auditProjects = auditProjectService.selectRecordList(AuditProject.class, conditionMap);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    /***
     *
     * @param p_beginDate
     * @param p_endDate
     * @param p_areacode
     * @return
     * @throws ParseException
     */
    @Override
    public List<AuditProjectTJ> ASP_BJ_OU(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException {
        List<AuditProjectTJ> auditProjectTJ = new ArrayList<>();
        Date endDate = new Date();
        // Date startDate=new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        if (p_endDate != null && !p_endDate.equals("")) {
            endDate = EpointDateUtil.convertString2DateAuto(p_endDate);
        }
        if (p_beginDate != null && !p_beginDate.equals("")) {
            startDate = EpointDateUtil.convertString2DateAuto(p_beginDate);
        }
        ICommonDao commonDao = CommonDao.getInstance("project");
        List<Record> records = commonDao.executeProcudure("ASP_Report_ProjectByOU", startDate, endDate, p_areacode);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                auditProjectTJ.add(record.toEntity(AuditProjectTJ.class));
            }
        }
        commonDao.close();
        return auditProjectTJ;
    }

    @Override
    public List<AuditProjectCK> ASP_BJ_WINDOW(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException {
        List<AuditProjectCK> auditProjectCK = new ArrayList<>();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        if (p_endDate != null && !p_endDate.equals("")) {
            endDate = EpointDateUtil.convertString2DateAuto(p_endDate);
        }
        if (p_beginDate != null && !p_beginDate.equals("")) {
            startDate = EpointDateUtil.convertString2DateAuto(p_beginDate);
        }
        ICommonDao commonDao = CommonDao.getInstance("project");
        List<Record> records = commonDao.executeProcudure("ASP_Report_ProjectByWindow", startDate, endDate, p_areacode);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                auditProjectCK.add(record.toEntity(AuditProjectCK.class));
            }
        }
        commonDao.close();
        return auditProjectCK;
    }

    @Override
    public List<AuditProjectUser> ASP_BJ_USER(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException {
        List<AuditProjectUser> auditProjectUser = new ArrayList<>();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        if (p_endDate != null && !p_endDate.equals("")) {
            endDate = EpointDateUtil.convertString2DateAuto(p_endDate);
        }
        if (p_beginDate != null && !p_beginDate.equals("")) {
            startDate = EpointDateUtil.convertString2DateAuto(p_beginDate);
        }
        ICommonDao commonDao = CommonDao.getInstance("project");
        List<Record> records = commonDao.executeProcudure("ASP_Report_ProjectByUser", startDate, endDate, p_areacode);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                auditProjectUser.add(record.toEntity(AuditProjectUser.class));
            }
        }
        commonDao.close();
        return auditProjectUser;
    }

    @Override
    public List<AuditProjectTaskName> ASP_BJ_TASKNAME(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException {
        List<AuditProjectTaskName> auditProjectTaskName = new ArrayList<>();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        if (p_endDate != null && !p_endDate.equals("")) {
            endDate = EpointDateUtil.convertString2DateAuto(p_endDate);
        }
        if (p_beginDate != null && !p_beginDate.equals("")) {
            startDate = EpointDateUtil.convertString2DateAuto(p_beginDate);
        }
        ICommonDao commonDao = CommonDao.getInstance("project");
        List<Record> records = commonDao.executeProcudure("ASP_Report_ProjectByTaskName", startDate, endDate,
                p_areacode);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                auditProjectTaskName.add(record.toEntity(AuditProjectTaskName.class));
            }
        }
        commonDao.close();
        return auditProjectTaskName;
    }

    @Override
    public List<AuditProjectTaskType> ASP_BJ_TASKTYPE(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException {
        List<AuditProjectTaskType> auditProjectTaskType = new ArrayList<>();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        if (p_endDate != null && !p_endDate.equals("")) {
            endDate = EpointDateUtil.convertString2DateAuto(p_endDate);
        }
        if (p_beginDate != null && !p_beginDate.equals("")) {
            startDate = EpointDateUtil.convertString2DateAuto(p_beginDate);
        }
        ICommonDao commonDao = CommonDao.getInstance("project");
        List<Record> records = commonDao.executeProcudure("ASP_Report_ProjectByShenpilb", startDate, endDate,
                p_areacode);
        if (records != null && records.size() > 0) {
            for (Record record : records) {
                auditProjectTaskType.add(record.toEntity(AuditProjectTaskType.class));
            }
        }
        commonDao.close();
        return auditProjectTaskType;
    }

    /**
     * [一句话功能简述] [功能详细描述]
     *
     * @return
     * @throws ParseException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditProReport> getTaskByOu(String p_beginDate, String p_endDate, String areaCode)
            throws ParseException {
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        if (p_endDate == null || p_endDate.equals("")) {
            p_endDate = EpointDateUtil.convertDate2String(endDate, "yyyy-MM-dd");
        }
        if (p_beginDate == null || p_beginDate.equals("")) {
            p_beginDate = EpointDateUtil.convertDate2String(startDate, "yyyy-MM-dd");
        }

        String ouname = "";
        String ouguid = "";
        // 汇总数据record
        List<AuditProReport> dataList = new ArrayList<AuditProReport>();
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        // 获取部门名称以及部门
        List<Record> oulist = new ArrayList<Record>();
        oulist = auditProjectService.getReportOu(areaCode);
        if (oulist != null && oulist.size() > 0) {
            for (int i = 0; i < oulist.size(); i++) {
                AuditProReport entity = new AuditProReport();
                ouname = oulist.get(i).get("ouname");
                ouguid = oulist.get(i).get("ouguid");
                entity.set("OUNAME", ouname);// 部门
                Map<String, String> map = auditProjectService.getReportCount(ouguid, areaCode, p_beginDate, p_endDate);
                for (String key : map.keySet()) {
                    entity.set(key, map.get(key));
                }
                dataList.add(entity);
            }
        }
        return dataList;
    }

    @Override
    public AuditCommonResult<Integer> getAuditProjectCountByCondition(Map<String, String> conditionMap) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            Integer count = sqlManageUtil.getListCount(AuditProject.class, conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getHotBanJianList(List<String> taskGuidList, String centerguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = auditProjectService.getHotBanJianList(taskGuidList, centerguid);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getHotBanJianList(List<String> taskGuidList, String centerguid, String areacode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = auditProjectService.getHotBanJianList(taskGuidList, centerguid, areacode);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWeekLineList(List<String> ouGuidList, String status, Date date) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = auditProjectService.getWeekLineList(ouGuidList, status, date);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWeekLineListByOu(List<String> ouGuidList, String status, Date date) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = auditProjectService.getWeekLineListByOu(ouGuidList, status, date);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWeekLineList(String centerguid, String areacode, String status, Date date) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = auditProjectService.getWeekLineList(centerguid, areacode, status, date);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAuditProjectCountByApplyway(Map<String, String> conditionMap, String applyway) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            Integer count = auditProjectService.getAuditProjectCountByApplyway(conditionMap, applyway);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getAuditProjectStatusCountByCondition(Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = auditProjectService.getAuditProjectCountByCondition(conditionMap);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTodayHandleProjectCount(String conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            int count = auditProjectService.getTodayHandleProjectCount(conditionMap);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTodayHandleProjectCount(String conditionMap, String field) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            int count = auditProjectService.getTodayHandleProjectCount(conditionMap, field);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOverUserCount(Map<String, String> conditionMap, int todayHandle) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            int count = auditProjectService.getOverUserCount(conditionMap, todayHandle);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOverFinishCount(Map<String, String> conditionMap, int todayFinish) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            int count = auditProjectService.getOverUserCount(conditionMap, todayFinish);
            result.setResult(count);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAvgSpendtime(Map<String, String> map) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        if (auditProjectService.getAvgSpendtime(map) == null) {
            result.setResult(0);
        } else {
            result.setResult(auditProjectService.getAvgSpendtime(map));
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getPagaBySpareTime(String fieldStr,
                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                        String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();
            projectList = auditProjectService.getPagaBySpareTime(fieldStr, conditionMap, firstResult,
                    maxResults, sortField, sortOrder, "");
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getPagaBySpareTimeCount(Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            Integer projectList = auditProjectService.getPagaBySpareTimeCount(conditionMap);
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectListByBZ(String fieldstr, Map<String, String> map,
                                                                             int first, int pageSize, String sortField, String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();
            projectList = auditProjectService.getAuditProjectListByBZ(fieldstr, map, first,
                    pageSize, sortField, sortOrder, "");
            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> selectRecordListBTS(String fieldstr, String projectname, String flowsn, int first,
                                                                         int pageSize, String sortField, String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> projectList = new PageData<AuditProject>();

            projectList = auditProjectService.selectRecordListBTS(fieldstr, projectname,
                    flowsn, first, pageSize, sortField, sortOrder);

            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> selectRecordListBTSAN(String fieldstr, String projectname,
                                                                       String flowsn, String handleProjectGuid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        try {
            List<AuditProject> projectList = new ArrayList<AuditProject>();

            projectList = auditProjectService.selectRecordListBTSAN(fieldstr, projectname,
                    flowsn, handleProjectGuid);

            result.setResult(projectList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getProjectnumGroupByFiled(String groupfield, Integer recordnum,
                                                                     Map<String, String> conditionmap) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(new AuditProjectService(AuditProject.class).getProjectnumGroupByTaskid(groupfield, recordnum, conditionmap));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getAuditProjectSatisfiedList(Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> auditProjects = new ArrayList<Record>();
            auditProjects = auditProjectService.getAuditProjectSatisfiedList(conditionMap);
            result.setResult(auditProjects);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public List<WorkflowWorkItem> getWorkItemListByPVIGuidAndStatus(String pviguid) {
        return new JNAuditProjectService().getWorkItemListByPVIGuidAndStatus(pviguid);
    }

    @Override
    public Record getMaxZjNum(String name, String year) {
        return new JNAuditProjectService().getMaxZjNum(name, year);
    }

    @Override
    public void UpdateMaxZjNum(String maxnum, String name, String year) {
        new JNAuditProjectService().UpdateMaxZjNum(maxnum, name, year);
    }

    @Override
    public Record getMaxZjNumNew(String name) {
        return new JNAuditProjectService().getMaxZjNumNew(name);
    }

    @Override
    public void UpdateMaxZjNumNew(String maxnum, String name) {
        new JNAuditProjectService().UpdateMaxZjNumNew(maxnum, name);
    }

    @Override
    public int updateProjectCertRowguid(String certrowguid, String rowguid) {
        return new JNAuditProjectService().updateProjectCertRowguid(certrowguid, rowguid);
    }

    @Override
    public AuditTask getAuditTaskByUnid(String unid) {
        return new JNAuditProjectService().getAuditTaskByUnid(unid);
    }

}

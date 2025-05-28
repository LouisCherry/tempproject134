package com.epoint.basic.auditproject.auditproject.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.*;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProjectDuban;
import com.epoint.basic.auditproject.auditproject.service.JnProjectService;
import com.epoint.basic.auditproject.auditproject.service.ProjectService;
import com.epoint.basic.auditproject.service.AuditProjectService;
import com.epoint.basic.auditproject.service.AuditProjectServiceDuban;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.database.peisistence.procedure.ProcedureService;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Service
public class AuditProjectDubanImpl implements IAuditProjectDuban {
    @Override
    public AuditCommonResult<String> addProject(AuditProject dataBean) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProjectService.addRecord(AuditProject.class, dataBean);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Void> deleteProjectByGuid(String rowGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            e.eq("rowguid", rowGuid);
            e.eq("areacode", areaCode);
            auditProjectService.deleteRecodByMap(AuditProject.class, e.getMap());
        } catch (Exception arg5) {
            result.setSystemFail(arg5.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Void> deleteUselessProject(String projectStatus, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            e.eq("status", projectStatus);
            e.eq("areacode", areaCode);
            auditProjectService.deleteRecodByMap(AuditProject.class, e.getMap());
        } catch (Exception arg5) {
            result.setSystemFail(arg5.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<String> updateProject(AuditProject dataBean) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProjectService.updateRecord(AuditProject.class, dataBean);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String fieldstr,
                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                             String sortOrder) {
        AuditProjectServiceDuban auditProjectService = new AuditProjectServiceDuban(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData e = auditProjectService.getRecordPageData(fieldstr, AuditProject.class, conditionMap, firstResult,
                    maxResults, sortField, sortOrder, "");
            result.setResult(e);
        } catch (Exception arg9) {
            result.setSystemFail(arg9.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByHandleareacode(String fieldstr,
                                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                                             String sortOrder, String handlearracode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData e = auditProjectService.getRecordPageDataByHandleareacode(fieldstr, AuditProject.class,
                    conditionMap, firstResult, maxResults, sortField, sortOrder, "", handlearracode);
            result.setResult(e);
        } catch (Exception arg10) {
            result.setSystemFail(arg10.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String fieldstr, String rowGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            e.eq("rowguid", rowGuid);
            if (StringUtil.isNotBlank(areaCode)) {
                e.eq("areacode", areaCode);
            }

            e.setSelectFields(fieldstr);
            AuditProject auditProject = (AuditProject) auditProjectService.getDetail(AuditProject.class, e.getMap());
            result.setResult(auditProject);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String fieldstr, String flowsn, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            e.eq("flowsn", flowsn);
            if (StringUtil.isNotBlank(areaCode)) {
                e.eq("areacode", areaCode);
            }

            e.setSelectFields(fieldstr);
            AuditProject auditProject = (AuditProject) auditProjectService.getDetail(AuditProject.class, e.getMap());
            result.setResult(auditProject);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String fieldstr, String pviGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            AuditProject e = (AuditProject) auditProjectService.getDetail(fieldstr, AuditProject.class, pviGuid,
                    "pviguid");
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }


    @Override
    public AuditCommonResult<List<AuditProject>> getFieldList(String fieldstr, String grouporsortstr,
                                                              String windowguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getAuditProjectFieldList(fieldstr, grouporsortstr, windowguid);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getField(String fieldstr, String grouporsortstr, String whereStr) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            AuditProject e = auditProjectService.getAuditProjectField(fieldstr, grouporsortstr, whereStr);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(String fieldstr, List<String> taskidList,
                                                                          String projectType, String windowguid, int first, int pageSize, String areaCode, String applyerName,
                                                                          Date datestart, Date dateend) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        String taskids = "";
        if (taskidList != null) {
            String taskidExp;
            for (Iterator e = taskidList.iterator(); e.hasNext(); taskids = taskids + "\'" + taskidExp + "\',") {
                taskidExp = (String) e.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        try {
            PageData e1 = auditProjectService.getBanJianListByPageAndTaskids(fieldstr, projectType, taskids, first,
                    pageSize, areaCode, applyerName, datestart, dateend, windowguid);
            result.setResult(e1);
        } catch (Exception arg15) {
            result.setSystemFail(arg15.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuid(String fieldstr,
                                                                                List<String> taskidList, String projectType, String windowguid, int first, int pageSize, String areaCode,
                                                                                String centerGuid, String applyerName, Date datestart, Date dateend) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        String taskids = "";
        if (taskidList != null) {
            String taskidExp;
            for (Iterator e = taskidList.iterator(); e.hasNext(); taskids = taskids + "\'" + taskidExp + "\',") {
                taskidExp = (String) e.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        try {
            PageData e1 = auditProjectService.getBanJianListByPageAndTaskidsAndCenterGuid(fieldstr, projectType,
                    taskids, first, pageSize, areaCode, centerGuid, applyerName, datestart, dateend, windowguid);
            result.setResult(e1);
        } catch (Exception arg16) {
            result.setSystemFail(arg16.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuidAndAreacode(String fieldstr,
                                                                                           List<String> taskidList, String projectType, String windowguid, int first, int pageSize, String areaCode,
                                                                                           String centerGuid, String applyerName, Date datestart, Date dateend, String realAreacode) {
        AuditProjectServiceDuban auditProjectService = new AuditProjectServiceDuban(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        String taskids = "";
        if (taskidList != null) {
            String taskidExp;
            for (Iterator e = taskidList.iterator(); e.hasNext(); taskids = taskids + "\'" + taskidExp + "\',") {
                taskidExp = (String) e.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        try {
            PageData e1 = auditProjectService.getBanJianListByPageAndTaskidsAndCenterGuidAndAreacode(fieldstr,
                    projectType, taskids, first, pageSize, areaCode, centerGuid, applyerName, datestart, dateend,
                    windowguid, realAreacode);
            result.setResult(e1);
        } catch (Exception arg17) {
            result.setSystemFail(arg17.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getBanJianList(String fieldstr, List<String> taskGuidList,
                                                                String projectType, String windowguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getBanJianList(fieldstr, taskGuidList, projectType, windowguid);
            result.setResult(e);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getJZListByPage(String fieldstr, List<String> ouGuidList,
                                                                     int first, int pageSize, String areaCode, String applyerName) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        String ouGuids = "";
        if (ouGuidList != null) {
            String ouguidExp;
            for (Iterator e = ouGuidList.iterator(); e.hasNext(); ouGuids = ouGuids + "\'" + ouguidExp + "\',") {
                ouguidExp = (String) e.next();
            }

            if (!"".equals(ouGuids)) {
                ouGuids = ouGuids.substring(0, ouGuids.length() - 1);
            }
        }

        try {
            PageData e1 = auditProjectService.getJZListByPage(fieldstr, ouGuids, first, pageSize, areaCode,
                    applyerName);
            result.setResult(e1);
        } catch (Exception arg11) {
            result.setSystemFail(arg11.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getJZList(String fieldstr, List<String> ouGuidList, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        String ouGuids = "";
        if (ouGuidList != null) {
            String ouguidExp;
            for (Iterator e = ouGuidList.iterator(); e.hasNext(); ouGuids = ouGuids + "\'" + ouguidExp + "\',") {
                ouguidExp = (String) e.next();
            }

            if (!"".equals(ouGuids)) {
                ouGuids = ouGuids.substring(0, ouGuids.length() - 1);
            }
        }

        try {
            List e1 = auditProjectService.getJZList(fieldstr, ouGuids, areaCode);
            result.setResult(e1);
        } catch (Exception arg8) {
            result.setSystemFail(arg8.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Void> updateProject(Map<String, String> updateFieldMap, Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProjectService.updateRecord(AuditProject.class, updateFieldMap, conditionMap);
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguid(List<String> taskidList,
                                                                              String windowguid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getCountStatusByWindowguid(taskidList, windowguid, areaCode);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByCenterguid(List<String> taskidList,
                                                                              String windowGuid, String centerGuid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getCountStatusByCenterguid(taskidList, windowGuid, centerGuid, areaCode);
            result.setResult(e);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguid(List<String> taskidList,
                                                                                           String windowguid, String areaCode, String centerGuid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getCountStatusByWindowguidAndCenterguid(taskidList, windowguid, areaCode,
                    centerGuid);
            result.setResult(e);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguidAndAreacode(
            List<String> taskidList, String windowguid, String areaCode, String centerGuid, String baseareacode) {
        AuditProjectServiceDuban auditProjectService = new AuditProjectServiceDuban(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getCountStatusByWindowguidAndCenterguidAndAreacode(taskidList, windowguid,
                    areaCode, centerGuid, baseareacode);
            result.setResult(e);
        } catch (Exception arg8) {
            result.setSystemFail(arg8.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguidAndAreacodeByWwsb(
            List<String> taskidList, String windowguid, String areaCode, String centerGuid, String baseareacode) {
        AuditProjectServiceDuban auditProjectService = new AuditProjectServiceDuban(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getCountStatusByWindowguidAndCenterguidAndAreacodeByWwsb(taskidList, windowguid,
                    areaCode, centerGuid, baseareacode);
            result.setResult(e);
        } catch (Exception arg8) {
            result.setSystemFail(arg8.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String fieldstr, String projectType,
                                                                          String accountguid, int first, int pageSize, String sortField, String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            if (StringUtil.isNotBlank(accountguid)) {
                e.eq("applyeruserguid", accountguid);
            }

            if (StringUtil.isNotBlank(projectType)) {
                e.eq("syatus", projectType);
            }

            List auditProjectByfield = (List) auditProjectService.getRecordPageData(fieldstr, AuditProject.class,
                    e.getMap(), Integer.valueOf(first), Integer.valueOf(pageSize), sortField, sortOrder, "");
            result.setResult(auditProjectByfield);
        } catch (Exception arg11) {
            result.setSystemFail(arg11.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> selectHotTaskId() {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.selectHotTaskId();
            result.setResult(e);
        } catch (Exception arg3) {
            result.setSystemFail(arg3.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(String fields,
                                                                                Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new ArrayList();
            List e = auditProjectService.selectRecordList(AuditProject.class, conditionMap);
            result.setResult(e);
        } catch (Exception arg5) {
            result.setSystemFail(arg5.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String fieldstr,
                                                                                    Map<String, String> conditionMap, Integer ismaterial, Integer firstResult, Integer maxResults,
                                                                                    String sortField, String sortOrder) {
        ProjectService projectService = new ProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            PageData e = projectService.getAuditProjectPageDataMonitor(fieldstr, conditionMap, ismaterial, firstResult,
                    maxResults, sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg10) {
            result.setSystemFail(arg10.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCSCount(String windowGuid, String areaCode) {
        ProjectService projectService = new ProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            result.setResult(projectService.getCSCount(windowGuid, areaCode));
        } catch (Exception arg5) {
            arg5.printStackTrace();
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(String fields,
                                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                                        String sortOrder, String keyword) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData e = auditProjectService.getRecordPageData(fields, AuditProject.class, conditionMap, firstResult,
                    maxResults, sortField, sortOrder, keyword);
            result.setResult(e);
        } catch (Exception arg10) {
            result.setSystemFail(arg10.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(Map<String, String> conditionMap,
                                                                             Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            PageData e = sqlManageUtil.getDbListByPage(AuditProject.class, conditionMap, firstResult, maxResults,
                    sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg8) {
            result.setSystemFail(arg8.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new ArrayList();
            List e = auditProjectService.selectRecordList(AuditProject.class, conditionMap);
            result.setResult(e);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String rowguid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            e.eq("rowguid", rowguid);
            if (StringUtil.isNotBlank(areaCode)) {
                e.eq("areacode", areaCode);
            }

            AuditProject auditProject = (AuditProject) auditProjectService.getDetail(AuditProject.class, e.getMap());
            result.setResult(auditProject);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String flowsn, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            e.eq("flowsn", flowsn);
            if (StringUtil.isNotBlank(areaCode)) {
                e.eq("areacode", areaCode);
            }

            AuditProject auditProject = (AuditProject) auditProjectService.getDetail(AuditProject.class, e.getMap());
            result.setResult(auditProject);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String pviGuid, String areaCode) {
        return null;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(List<String> taskGuidList, String projectType,
                                                                          String windowguid, int first, int pageSize, String areaCode, String applyerName) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        String taskGuids = "";
        if (taskGuidList != null) {
            String taskGuidExp;
            for (Iterator e = taskGuidList.iterator(); e
                    .hasNext(); taskGuids = taskGuids + "\'" + taskGuidExp + "\',") {
                taskGuidExp = (String) e.next();
            }

            if (taskGuids != "") {
                taskGuids = taskGuids.substring(0, taskGuids.length() - 1);
            }
        }

        try {
            PageData e1 = auditProjectService.getBanJianListByPage("*", projectType, taskGuids, first, pageSize,
                    areaCode, applyerName);
            result.setResult(e1);
        } catch (Exception arg12) {
            result.setSystemFail(arg12.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getBanJianList(List<String> taskGuidList, String projectType,
                                                                String windowguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getBanJianList("*", taskGuidList, projectType, windowguid);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String projectType, String accountguid,
                                                                          int first, int pageSize, String sortField, String sortOrder) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            SqlConditionUtil e = new SqlConditionUtil();
            if (StringUtil.isNotBlank(accountguid)) {
                e.eq("applyeruserguid", accountguid);
            }

            if (StringUtil.isNotBlank(projectType)) {
                e.eq("syatus", projectType);
            }

            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            List auditProjectByfield = (List) sqlManageUtil.getDbListByPage(AuditProject.class, e.getMap(),
                    Integer.valueOf(first), Integer.valueOf(pageSize), sortField, sortOrder);
            result.setResult(auditProjectByfield);
        } catch (Exception arg10) {
            result.setSystemFail(arg10.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(Map<String, String> conditionMap,
                                                                                    Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder, String userguid) {
        JnProjectService projectService = new JnProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            PageData e = projectService.getAuditProjectPageDataMonitor(userguid, "*", conditionMap, ismaterial, firstResult,
                    maxResults, sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg9) {
            result.setSystemFail(arg9.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(Map<String, String> conditionMap,
                                                                                    Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        ProjectService projectService = new ProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            PageData e = projectService.getAuditProjectPageDataMonitor("*", conditionMap, ismaterial, firstResult,
                    maxResults, sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg9) {
            result.setSystemFail(arg9.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(
            Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
            String sortOrder) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            PageData e = sqlManageUtil.getDbListByPage(AuditProject.class, conditionMap, firstResult, maxResults,
                    sortField, sortOrder);
            result.setResult(e);
        } catch (Exception arg8) {
            result.setSystemFail(arg8.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<String> deleteProject(String projectGuid, String flownsn, String pviguid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            Object[] e = new Object[]{projectGuid, flownsn, pviguid};
            ProcedureService procedureservice = new ProcedureService(new DataSourceConfig());
            procedureservice.beginTransaction();
            procedureservice.executeProcudure("ASP_DELETEPROJECT", e);
            procedureservice.commitTransaction();
            procedureservice.close();
        } catch (Exception arg6) {
            result.setSystemFail(arg6.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getReportCount(String ouguid, String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getReportCount(ouguid, areaCode);
            result.setResult(e);
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getReportCount(String ouguid, String areaCode, String startDate,
                                                                 String endDate) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Map e = auditProjectService.getReportCount(ouguid, areaCode, startDate, endDate);
            result.setResult(e);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getReportOu(String areaCode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getReportOu(areaCode);
            result.setResult(e);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProject>> getSyncProjectList(Map<String, String> conditionMap) {
        AuditCommonResult result = new AuditCommonResult();
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);

        try {
            new ArrayList();
            List e = auditProjectService.selectRecordList(AuditProject.class, conditionMap);
            result.setResult(e);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.getMessage());
        }

        return result;
    }

    @Override
    public List<AuditProjectTJ> ASP_BJ_OU(String pBeginDate, String pEndDate, String pAreacode)
            throws ParseException {
        ArrayList auditProjectTJ = new ArrayList();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(2, -1);
        Date startDate = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotBlank(pEndDate)) {
            endDate = format.parse(pEndDate);
        }

        if (StringUtil.isNotBlank(pBeginDate)) {
            startDate = format.parse(pBeginDate);
        }

        CommonDao commonDao = CommonDao.getInstance();
        List records = commonDao.executeProcudure("ASP_Report_ProjectByOU",
                new Object[]{startDate, endDate, pAreacode});
        if (records != null && records.size() > 0) {
            Iterator arg10 = records.iterator();

            while (arg10.hasNext()) {
                Record record = (Record) arg10.next();
                auditProjectTJ.add(record.toEntity(AuditProjectTJ.class));
            }
        }

        commonDao.close();
        return auditProjectTJ;
    }

    @Override
    public List<AuditProjectCK> ASP_BJ_WINDOW(String pBeginDate, String pEndDate, String pAreacode)
            throws ParseException {
        ArrayList auditProjectCK = new ArrayList();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(2, -1);
        Date startDate = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotBlank(pEndDate)) {
            endDate = format.parse(pEndDate);
        }

        if (StringUtil.isNotBlank(pBeginDate)) {
            startDate = format.parse(pBeginDate);
        }

        CommonDao commonDao = CommonDao.getInstance();
        List records = commonDao.executeProcudure("ASP_Report_ProjectByWindow",
                new Object[]{startDate, endDate, pAreacode});
        if (records != null && records.size() > 0) {
            Iterator arg10 = records.iterator();

            while (arg10.hasNext()) {
                Record record = (Record) arg10.next();
                auditProjectCK.add(record.toEntity(AuditProjectCK.class));
            }
        }

        commonDao.close();
        return auditProjectCK;
    }

    @Override
    public List<AuditProjectUser> ASP_BJ_USER(String pBeginDate, String pEndDate, String pAreacode)
            throws ParseException {
        ArrayList auditProjectUser = new ArrayList();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(2, -1);
        Date startDate = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotBlank(pEndDate)) {
            endDate = format.parse(pEndDate);
        }

        if (StringUtil.isNotBlank(pBeginDate)) {
            startDate = format.parse(pBeginDate);
        }

        CommonDao commonDao = CommonDao.getInstance();
        List records = commonDao.executeProcudure("ASP_Report_ProjectByUser",
                new Object[]{startDate, endDate, pAreacode});
        if (records != null && records.size() > 0) {
            Iterator arg10 = records.iterator();

            while (arg10.hasNext()) {
                Record record = (Record) arg10.next();
                auditProjectUser.add(record.toEntity(AuditProjectUser.class));
            }
        }

        commonDao.close();
        return auditProjectUser;
    }

    @Override
    public List<AuditProjectTaskName> ASP_BJ_TASKNAME(String pBeginDate, String pEndDate, String pAreacode)
            throws ParseException {
        ArrayList auditProjectTaskName = new ArrayList();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(2, -1);
        Date startDate = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotBlank(pEndDate)) {
            endDate = format.parse(pEndDate);
        }

        if (StringUtil.isNotBlank(pBeginDate)) {
            startDate = format.parse(pBeginDate);
        }

        CommonDao commonDao = CommonDao.getInstance();
        List records = commonDao.executeProcudure("ASP_Report_ProjectByTaskName",
                new Object[]{startDate, endDate, pAreacode});
        if (records != null && records.size() > 0) {
            Iterator arg10 = records.iterator();

            while (arg10.hasNext()) {
                Record record = (Record) arg10.next();
                auditProjectTaskName.add(record.toEntity(AuditProjectTaskName.class));
            }
        }

        commonDao.close();
        return auditProjectTaskName;
    }

    @Override
    public List<AuditProjectTaskType> ASP_BJ_TASKTYPE(String pBeginDate, String pEndDate, String pAreacode)
            throws ParseException {
        ArrayList auditProjectTaskType = new ArrayList();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(2, -1);
        Date startDate = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotBlank(pEndDate)) {
            endDate = format.parse(pEndDate);
        }

        if (StringUtil.isNotBlank(pBeginDate)) {
            startDate = format.parse(pBeginDate);
        }

        CommonDao commonDao = CommonDao.getInstance();
        List records = commonDao.executeProcudure("ASP_Report_ProjectByShenpilb",
                new Object[]{startDate, endDate, pAreacode});
        if (records != null && records.size() > 0) {
            Iterator arg10 = records.iterator();

            while (arg10.hasNext()) {
                Record record = (Record) arg10.next();
                auditProjectTaskType.add(record.toEntity(AuditProjectTaskType.class));
            }
        }

        commonDao.close();
        return auditProjectTaskType;
    }

    @Override
    public List<AuditProReport> getTaskByOu(String pBeginDate, String pEndDate, String areaCode)
            throws ParseException {
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(2, -1);
        Date startDate = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotBlank(pEndDate)) {
            pEndDate = format.format(endDate);
        }

        if (StringUtil.isNotBlank(startDate)) {
            pBeginDate = format.format(startDate);
        }

        String ouname = "";
        String ouguid = "";
        ArrayList dataList = new ArrayList();
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        List oulist = auditProjectService.getReportOu(areaCode);
        if (oulist != null && oulist.size() > 0) {
            for (int i = 0; i < oulist.size(); ++i) {
                AuditProReport entity = new AuditProReport();
                ouname = (String) ((Record) oulist.get(i)).get("ouname");
                ouguid = (String) ((Record) oulist.get(i)).get("ouguid");
                entity.set("OUNAME", ouname);
                Map map = auditProjectService.getReportCount(ouguid, areaCode, pBeginDate, pEndDate);
                Iterator arg15 = map.keySet().iterator();

                while (arg15.hasNext()) {
                    String key = (String) arg15.next();
                    entity.set(key, map.get(key));
                }

                dataList.add(entity);
            }
        }

        return dataList;
    }

    @Override
    public AuditCommonResult<Integer> getAuditProjectCountByCondition(Map<String, String> conditionMap) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            SQLManageUtil e = new SQLManageUtil(AuditProject.class);
            Integer count = e.getListCount(AuditProject.class, conditionMap);
            result.setResult(count);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getHotBanJianList(List<String> taskGuidList, String centerguid) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getHotBanJianList(taskGuidList, centerguid);
            result.setResult(e);
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getHotBanJianList(List<String> taskGuidList, String centerguid,
                                                             String areacode) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getHotBanJianList(taskGuidList, centerguid, areacode);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWeekLineList(List<String> ouGuidList, String status, Date date) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getWeekLineList(ouGuidList, status, date);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWeekLineListByOu(List<String> ouGuidList, String status, Date date) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getWeekLineListByOu(ouGuidList, status, date);
            result.setResult(e);
        } catch (Exception arg6) {
            result.setSystemFail(arg6.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWeekLineList(String centerguid, String areacode, String status,
                                                           Date date) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getWeekLineList(centerguid, areacode, status, date);
            result.setResult(e);
        } catch (Exception arg7) {
            result.setSystemFail(arg7.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAuditProjectCountByApplyway(Map<String, String> conditionMap,
                                                                     String applyway) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            Integer e = auditProjectService.getAuditProjectCountByApplyway(conditionMap, applyway);
            result.setResult(e);
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getAuditProjectStatusCountByCondition(Map<String, String> conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            List e = auditProjectService.getAuditProjectCountByCondition(conditionMap);
            result.setResult(e);
        } catch (Exception arg4) {
            result.setSystemFail(arg4.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTodayHandleProjectCount(String conditionMap) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            int e = auditProjectService.getTodayHandleProjectCount(conditionMap).intValue();
            result.setResult(Integer.valueOf(e));
        } catch (Exception arg4) {
            result.setSystemFail(arg4.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTodayHandleProjectCount(String conditionMap, String field) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            int e = auditProjectService.getTodayHandleProjectCount(conditionMap, field).intValue();
            result.setResult(Integer.valueOf(e));
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOverUserCount(Map<String, String> conditionMap, int todayHandle) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            int e = auditProjectService.getOverUserCount(conditionMap, todayHandle).intValue();
            result.setResult(Integer.valueOf(e));
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOverFinishCount(Map<String, String> conditionMap, int todayFinish) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            int e = auditProjectService.getOverUserCount(conditionMap, todayFinish).intValue();
            result.setResult(Integer.valueOf(e));
        } catch (Exception arg5) {
            result.setSystemFail(arg5.toString());
        }

        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAvgSpendtime(Map<String, String> map) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();
        if (auditProjectService.getAvgSpendtime(map) == null) {
            result.setResult(Integer.valueOf(0));
        } else {
            result.setResult(auditProjectService.getAvgSpendtime(map));
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getPagaBySpareTime(String fieldStr,
                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                        String sortOrder) {
        AuditProjectServiceDuban auditProjectService = new AuditProjectServiceDuban(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData e = auditProjectService.getPagaBySpareTime(fieldStr, conditionMap, firstResult, maxResults,
                    sortField, sortOrder, "");
            result.setResult(e);
        } catch (Exception arg9) {
            result.setSystemFail(arg9.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getPagaBySpareTime(String fieldStr,
                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                        String sortOrder, String userGuid) {
        AuditProjectServiceDuban auditProjectService = new AuditProjectServiceDuban(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData e = auditProjectService.getPagaBySpareTime(fieldStr, conditionMap, firstResult, maxResults,
                    sortField, sortOrder, "", userGuid);
            result.setResult(e);
        } catch (Exception arg9) {
            result.setSystemFail(arg9.getMessage());
        }

        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectListByBZ(String fieldstr, Map<String, String> map,
                                                                             int first, int pageSize, String sortField, String sortOrder) {
        AuditProjectService auditProjectService = new AuditProjectService(AuditProject.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData e = auditProjectService.getAuditProjectListByBZ(fieldstr, map, first, pageSize, sortField,
                    sortOrder, "");
            result.setResult(e);
        } catch (Exception arg9) {
            result.setSystemFail(arg9.getMessage());
        }

        return result;
    }
}
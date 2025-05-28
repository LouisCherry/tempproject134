package com.epoint.basic.auditproject.auditproject.inter;

import com.epoint.basic.auditproject.auditproject.domain.AuditProReport;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectCK;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTJ;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTaskName;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTaskType;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectUser;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAuditProjectDuban
{
    AuditCommonResult<String> addProject(AuditProject arg0);

    AuditCommonResult<Void> deleteProjectByGuid(String arg0, String arg1);

    AuditCommonResult<Void> deleteUselessProject(String arg0, String arg1);

    AuditCommonResult<List<AuditProject>> getSyncProjectList(Map<String, String> arg0);

    AuditCommonResult<String> updateProject(AuditProject arg0);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String arg0, Map<String, String> arg1,
            Integer arg2, Integer arg3, String arg4, String arg5);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByHandleareacode(String arg0,
            Map<String, String> arg1, Integer arg2, Integer arg3, String arg4, String arg5, String arg6);

    AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(String arg0, Map<String, String> arg1);

    AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String arg0, String arg1, String arg2);

    AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String arg0, String arg1, String arg2);

    AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String arg0, String arg1, String arg2);


    AuditCommonResult<List<AuditProject>> getFieldList(String arg0, String arg1, String arg2);

    AuditCommonResult<AuditProject> getField(String arg0, String arg1, String arg2);

    AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(String arg0, List<String> arg1, String arg2,
            String arg3, int arg4, int arg5, String arg6, String arg7, Date arg8, Date arg9);

    AuditCommonResult<List<AuditProject>> getBanJianList(String arg0, List<String> arg1, String arg2, String arg3);

    AuditCommonResult<PageData<AuditProject>> getJZListByPage(String arg0, List<String> arg1, int arg2, int arg3,
            String arg4, String arg5);

    AuditCommonResult<List<AuditProject>> getJZList(String arg0, List<String> arg1, String arg2);

    AuditCommonResult<Void> updateProject(Map<String, String> arg0, Map<String, String> arg1);

    AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguid(List<String> arg0, String arg1, String arg2);

    AuditCommonResult<Map<String, Integer>> getCountStatusByCenterguid(List<String> arg0, String arg1, String arg2,
            String arg3);

    AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguid(List<String> arg0, String arg1,
            String arg2, String arg3);

    AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String arg0, String arg1, String arg2, int arg3,
            int arg4, String arg5, String arg6);

    AuditCommonResult<List<Record>> selectHotTaskId();

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String arg0, Map<String, String> arg1,
            Integer arg2, Integer arg3, Integer arg4, String arg5, String arg6);

    AuditCommonResult<Integer> getCSCount(String arg0, String arg1);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(String arg0, Map<String, String> arg1,
            Integer arg2, Integer arg3, String arg4, String arg5, String arg6);

    AuditCommonResult<Map<String, String>> getReportCount(String arg0, String arg1);

    AuditCommonResult<Map<String, String>> getReportCount(String arg0, String arg1, String arg2, String arg3);

    AuditCommonResult<List<Record>> getReportOu(String arg0);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(Map<String, String> arg0, Integer arg1,
            Integer arg2, String arg3, String arg4);

    AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(Map<String, String> arg0);

    AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String arg0, String arg1);

    AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String arg0, String arg1);

    AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String arg0, String arg1);

    AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(List<String> arg0, String arg1, String arg2,
            int arg3, int arg4, String arg5, String arg6);

    AuditCommonResult<List<AuditProject>> getBanJianList(List<String> arg0, String arg1, String arg2);

    AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String arg0, String arg1, int arg2, int arg3,
            String arg4, String arg5);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(Map<String, String> arg0, Integer arg1,
            Integer arg2, Integer arg3, String arg4, String arg5,String arg6);
    
    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(Map<String, String> arg0, Integer arg1,
            Integer arg2, Integer arg3, String arg4, String arg5);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(Map<String, String> arg0, Integer arg1,
            Integer arg2, String arg3, String arg4);

    AuditCommonResult<String> deleteProject(String arg0, String arg1, String arg2);

    List<AuditProjectTaskType> ASP_BJ_TASKTYPE(String arg0, String arg1, String arg2) throws ParseException;

    List<AuditProjectTaskName> ASP_BJ_TASKNAME(String arg0, String arg1, String arg2) throws ParseException;

    List<AuditProjectUser> ASP_BJ_USER(String arg0, String arg1, String arg2) throws ParseException;

    List<AuditProjectCK> ASP_BJ_WINDOW(String arg0, String arg1, String arg2) throws ParseException;

    List<AuditProjectTJ> ASP_BJ_OU(String arg0, String arg1, String arg2) throws ParseException;

    List<AuditProReport> getTaskByOu(String arg0, String arg1, String arg2) throws ParseException;

    AuditCommonResult<Integer> getAuditProjectCountByCondition(Map<String, String> arg0);

    AuditCommonResult<List<Record>> getHotBanJianList(List<String> arg0, String arg1);

    AuditCommonResult<List<Record>> getHotBanJianList(List<String> arg0, String arg1, String arg2);

    AuditCommonResult<List<Record>> getAuditProjectStatusCountByCondition(Map<String, String> arg0);

    AuditCommonResult<Integer> getTodayHandleProjectCount(String arg0);

    AuditCommonResult<Integer> getTodayHandleProjectCount(String arg0, String arg1);

    AuditCommonResult<Integer> getOverUserCount(Map<String, String> arg0, int arg1);

    AuditCommonResult<Integer> getOverFinishCount(Map<String, String> arg0, int arg1);

    AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuid(String arg0, List<String> arg1, String arg2,
            String arg3, int arg4, int arg5, String arg6, String arg7, String arg8, Date arg9, Date arg10);

    AuditCommonResult<Integer> getAuditProjectCountByApplyway(Map<String, String> arg0, String arg1);

    AuditCommonResult<List<Record>> getWeekLineList(List<String> arg0, String arg1, Date arg2);

    AuditCommonResult<List<Record>> getWeekLineListByOu(List<String> arg0, String arg1, Date arg2);

    AuditCommonResult<List<Record>> getWeekLineList(String arg0, String arg1, String arg2, Date arg3);

    AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuidAndAreacode(String arg0, List<String> arg1,
            String arg2, String arg3, int arg4, int arg5, String arg6, String arg7, String arg8, Date arg9, Date arg10,
            String arg11);

    AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguidAndAreacode(List<String> arg0,
            String arg1, String arg2, String arg3, String arg4);

    AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguidAndAreacodeByWwsb(List<String> arg0,
            String arg1, String arg2, String arg3, String arg4);

    AuditCommonResult<Integer> getAvgSpendtime(Map<String, String> arg0);

    AuditCommonResult<PageData<AuditProject>> getPagaBySpareTime(String arg0, Map<String, String> arg1, Integer arg2,
            Integer arg3, String arg4, String arg5,String arg6);
    
    AuditCommonResult<PageData<AuditProject>> getPagaBySpareTime(String arg0, Map<String, String> arg1, Integer arg2,
            Integer arg3, String arg4, String arg5);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectListByBZ(String arg0, Map<String, String> arg1, int arg2,
            int arg3, String arg4, String arg5);
}

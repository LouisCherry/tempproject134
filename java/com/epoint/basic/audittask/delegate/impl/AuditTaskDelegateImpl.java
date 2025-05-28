package com.epoint.basic.audittask.delegate.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.delegate.service.AuditTaskDelegateService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Component
@Service
public class AuditTaskDelegateImpl implements IAuditTaskDelegate
{
    @Override
    public AuditCommonResult<String> addAuditTaskDelegate(AuditTaskDelegate auditTaskDelegate) {
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditTaskDelService.add(auditTaskDelegate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<Record>> getXZpageData(SqlConditionUtil sql, int first, int pageSize,
            String sortField, String sortOrder) {
        AuditCommonResult<PageData<Record>> result = new AuditCommonResult<PageData<Record>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        result.setResult(auditTaskDelService.getXZpageData(sql, first, pageSize, sortField, sortOrder));
        return result;
    }

    @Override
    public AuditCommonResult<PageData<Record>> getCJpageData(SqlConditionUtil sql, int first, int pageSize,
            String sortField, String sortOrder) {
        AuditCommonResult<PageData<Record>> result = new AuditCommonResult<PageData<Record>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        result.setResult(auditTaskDelService.getCJpageData(sql, first, pageSize, sortField, sortOrder));
        return result;
    }

    @Override
    public AuditCommonResult<AuditTaskDelegate> findByTaskIDAndAreacode(String taskID, String areacode) {
        AuditCommonResult<AuditTaskDelegate> result = new AuditCommonResult<AuditTaskDelegate>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (StringUtil.isNotBlank(taskID)) {
            AuditTaskDelegate bean = auditTaskDelService.findByTaskIDAndAreacode(taskID, areacode);
            result.setResult(bean);
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> updata(AuditTaskDelegate bean) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (bean != null) {
            auditTaskDelService.updata(bean);
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditTask>> getTaskListByArea(String areaCode) {
        AuditCommonResult<List<AuditTask>> result = new AuditCommonResult<List<AuditTask>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (StringUtil.isNotBlank(areaCode)) {
            List<AuditTask> list = auditTaskDelService.getTaskListByArea(areaCode);
            result.setResult(list);
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditTask>> selectAuditTaskByCondition(String conndition, String areaCode) {
        AuditCommonResult<List<AuditTask>> result = new AuditCommonResult<List<AuditTask>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (StringUtil.isNotBlank(areaCode)) {
            List<AuditTask> list = auditTaskDelService.selectAuditTaskByCondition(conndition, areaCode);
            result.setResult(list);
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditTaskDelegate>> selectDelegateByTaskID(String taskid) {
        AuditCommonResult<List<AuditTaskDelegate>> result = new AuditCommonResult<List<AuditTaskDelegate>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (StringUtil.isNotBlank(taskid)) {
            List<AuditTaskDelegate> bean = auditTaskDelService.findByTaskID(taskid);
            result.setResult(bean);
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditTaskDelegate>> selectDelegateListByTaskIDAndAreacode(String taskid,
            String areacode) {
        AuditCommonResult<List<AuditTaskDelegate>> result = new AuditCommonResult<List<AuditTaskDelegate>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (StringUtil.isNotBlank(taskid) && StringUtil.isNotBlank(areacode)) {
            List<AuditTaskDelegate> taskDelegateList = auditTaskDelService.selectDelegateListByTaskIDAndAreacode(taskid,
                    areacode);
            result.setResult(taskDelegateList);
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditTask> selectUsableTaskByTaskID(String taskid, String areacode) {
        AuditCommonResult<AuditTask> result = new AuditCommonResult<AuditTask>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        if (StringUtil.isNotBlank(taskid)) {
            AuditTask auditTask = auditTaskDelService.selectUsableTaskByTaskID(taskid, areacode);
            result.setResult(auditTask);
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditTaskDelegate>> selectDelegateByAreacode(String areacode) {
        AuditCommonResult<List<AuditTaskDelegate>> result = new AuditCommonResult<List<AuditTaskDelegate>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        List<AuditTaskDelegate> list = auditTaskDelService.getDelegateByAreacode(areacode);
        result.setResult(list);
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditTask>> getXZpageDataList(SqlConditionUtil sql, int first, int pageSize,
            String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<PageData<AuditTask>>();
        AuditTaskDelegateService auditTaskDelService = new AuditTaskDelegateService();
        result.setResult(auditTaskDelService.getXZpageDataList(sql, first, pageSize, sortField, sortOrder));
        return result;
    }

    @Override
    public void deleteByrowguid(String rowguid) {
        new SQLManageUtil().deleteByOneField(AuditTaskDelegate.class, "rowguid", rowguid);
    }

    @Override
    public AuditTaskDelegate getAuditTaskDelegatebyRowguid(String rowugid) {
        return new SQLManageUtil().getBeanByOneField(AuditTaskDelegate.class, "rowguid", rowugid);

    }

    @Override
    public List<AuditTaskDelegate> getDelegateListByCondition(Map<String, String> map) {
        return new SQLManageUtil().getListByCondition(AuditTaskDelegate.class, map);
    }

    @Override
    public List<AuditTaskDelegate> getDelegateListByAreacodeandType(String xzareacode, String taskdelegateTypeXzfd) {
        SqlConditionUtil slqc = new SqlConditionUtil();
        slqc.eq("areacode", xzareacode);
        slqc.eq("delegatetype", taskdelegateTypeXzfd);
        return new SQLManageUtil().getListByCondition(AuditTaskDelegate.class, slqc.getMap());
    }
}

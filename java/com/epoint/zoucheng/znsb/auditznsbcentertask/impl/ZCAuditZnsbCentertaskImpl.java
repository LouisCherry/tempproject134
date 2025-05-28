package com.epoint.zoucheng.znsb.auditznsbcentertask.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.service.AuditZnsbCentertaskService;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.auditznsbcentertask.inter.IZCAuditZnsbCentertask;
import com.epoint.zoucheng.znsb.auditznsbcentertask.service.ZCAuditZnsbCentertaskService;

@Component
@Service
public class ZCAuditZnsbCentertaskImpl implements IZCAuditZnsbCentertask
{

    @Override
    public AuditCommonResult<PageData<AuditZnsbCentertask>> getCenterTaskPageData(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();

        AuditCommonResult<PageData<AuditZnsbCentertask>> result = new AuditCommonResult<PageData<AuditZnsbCentertask>>();
        try {

            PageData<AuditZnsbCentertask> yuyuetimePageData = centertaskservice
                    .getRecordPageData(AuditZnsbCentertask.class, conditionMap, first, pageSize, sortField, sortOrder);

            result.setResult(yuyuetimePageData);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebyRowGuid(String RowGuid) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.deleteRecords(AuditZnsbCentertask.class, RowGuid, "rowguid");

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebyCenterGuid(String CenterGuid) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.deleteRecords(AuditZnsbCentertask.class, CenterGuid, "centerguid");

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    @Override
    public AuditCommonResult<String> insert(AuditZnsbCentertask dataBean) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.addRecord(AuditZnsbCentertask.class, dataBean);

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> update(AuditZnsbCentertask dataBean) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.updateRecord(AuditZnsbCentertask.class, dataBean);

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditZnsbCentertask> getDetail(String RowGuid) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();
        AuditCommonResult<AuditZnsbCentertask> result = new AuditCommonResult<AuditZnsbCentertask>();
        try {
            result.setResult(centertaskservice.getDetail(AuditZnsbCentertask.class, RowGuid, "rowguid"));

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditZnsbCentertask> getDetailbyTaskGuid(String TaskGuid) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();
        AuditCommonResult<AuditZnsbCentertask> result = new AuditCommonResult<AuditZnsbCentertask>();
        try {
            result.setResult(centertaskservice.getDetail(AuditZnsbCentertask.class, TaskGuid, "taskguid"));

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<Boolean> ISExistbyTaskId(String TaskId, String centerguid) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<Boolean> result = new AuditCommonResult<Boolean>();
        try {
            result.setResult(centertaskservice.ISExistbyTaskId(TaskId, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateedit(String edit, String centerguid) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.updateedit(edit, centerguid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateeditbytaskid(String edit, String taskid, String centerguid) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.updateeditbytaskid(edit, taskid, centerguid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updatehottask(String taskid, String ishottask, Integer hottaskordernum) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.updatehottask(taskid, ishottask, hottaskordernum);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updatenohottask(String areacode) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.updatenohottask(areacode);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebyedit(String edit, String centerguid) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.deletebyedit(edit, centerguid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebytaskguid(String taskguid) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.deletebytaskguid(taskguid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    @Override
    public AuditCommonResult<String> deletebyTaskId(String TaskId,String CenterGuid) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            centertaskservice.deletebyTaskId(TaskId,CenterGuid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
 
    @Override
    public AuditCommonResult<List<AuditZnsbCentertask>> getCenterTaskList(Map<String, String> conditionMap) {
        AuditQueueBasicService<AuditZnsbCentertask> centertaskservice = new AuditQueueBasicService<AuditZnsbCentertask>();

        AuditCommonResult<List<AuditZnsbCentertask>> result = new AuditCommonResult<List<AuditZnsbCentertask>>();
        try {
            result.setResult(centertaskservice.selectRecordList(AuditZnsbCentertask.class,
                    conditionMap));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<String>> getTaskOUListPageData(String centerguid, String applyertype, int first,
            int pageSize) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<PageData<String>> result = new AuditCommonResult<PageData<String>>();
        try {

            PageData<String> pageData = new PageData<String>();
            List<String> oulist = centertaskservice.getTaskOUList(centerguid, applyertype, first, pageSize);
            int dataCount = centertaskservice.getTaskOUListCount(centerguid, applyertype);
            pageData.setList(oulist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<List<String>> getTaskOUList(String centerguid, String applyertype) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {
          
            List<String> oulist = centertaskservice.getTaskOUList(centerguid, applyertype);
         
            result.setResult(oulist);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<List<String>> getTaskOUListByOuName(String centerguid, String applyertype, String ouname) {
        ZCAuditZnsbCentertaskService centertaskservice = new ZCAuditZnsbCentertaskService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {
          
            List<String> oulist = centertaskservice.getTaskOUListByOuName(centerguid, applyertype, ouname);
         
            result.setResult(oulist);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<PageData<String>> getSampleOUPageData(String centerguid, int first,
            int pageSize) {
        AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<PageData<String>> result = new AuditCommonResult<PageData<String>>();
        try {
            PageData<String> pageData = new PageData<String>();
            List<String> oulist = centertaskservice.getSampleOUList(centerguid, first, pageSize);
            int dataCount = centertaskservice.getSampleOUListCount(centerguid);
            pageData.setList(oulist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    
    @Override
    public AuditCommonResult<PageData<Record>> getSampleTaskPageData(String centerguid, String ouguid, String taskname,
            int first, int pageSize) {
    	  AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<PageData<Record>> result = new AuditCommonResult<PageData<Record>>();
        try {
            PageData<Record> pageData = new PageData<Record>();
            List<Record> tasklist = centertaskservice.getSampleTaskList(centerguid, ouguid, taskname, first,
                    pageSize);
            int dataCount = centertaskservice.getSampleTaskCount(centerguid, ouguid, taskname);
            pageData.setList(tasklist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<Record>> getCommonSampleTaskPageData(String centerguid, String taskname, int first,
            int pageSize) {
    	  AuditZnsbCentertaskService centertaskservice = new AuditZnsbCentertaskService();
        AuditCommonResult<PageData<Record>> result = new AuditCommonResult<PageData<Record>>();
        try {
            PageData<Record> pageData = new PageData<Record>();
            List<Record> tasklist = centertaskservice.getCommonSampleTaskList(centerguid, taskname, first,
                    pageSize);
            int dataCount = centertaskservice.getCommonSampleTaskCount(centerguid, taskname);
            pageData.setList(tasklist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
}

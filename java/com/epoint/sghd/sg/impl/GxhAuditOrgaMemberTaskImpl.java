package com.epoint.sghd.sg.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.service.AuditOrgaService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import com.epoint.sghd.sg.entity.AuditOrgaMemberTask;
import com.epoint.sghd.sg.service.IGxhAuditOrgaMemberTask;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 窗口基础服务实现类
 *
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 */
@Component
@Service
public class GxhAuditOrgaMemberTaskImpl implements IGxhAuditOrgaMemberTask {

    @Override
    public AuditCommonResult<List<String>> getTaskidsByWindow(String windowGuid) {
        AuditOrgaService<AuditOrgaMemberTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaMemberTask>();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("memberguid", windowGuid);
            List<AuditOrgaMemberTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaMemberTask.class,
                    sql.getMap());
            List<String> taskids = new ArrayList<>();
            for (AuditOrgaMemberTask auditWindowTask : windowTaskList) {
                taskids.add(auditWindowTask.getTaskid());
            }
            result.setResult(taskids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteWindowTaskByWindowGuid(String windowGuid) {
        new GxhAuditOrgaMemberService().deleteWindowTaskByWindowGuid(windowGuid);
    }


    @Override
    public AuditCommonResult<String> insertWindowTask(AuditOrgaMemberTask auditWindowTask) {
        AuditOrgaService<AuditOrgaMemberTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaMemberTask>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 这里判断一下是否有重复的事项
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("taskguid", auditWindowTask.getTaskguid());
            sql.eq("memberguid", auditWindowTask.getMemberguid());
            List<AuditOrgaMemberTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaMemberTask.class,
                    sql.getMap());
            if (windowTaskList.size() > 0) {
                result.setBusinessFail("窗口事项" + ZwfwConstant.BUSINESSERROR_REPEAT);
            } else {
                auditWindowTaskService.addRecord(AuditOrgaMemberTask.class, auditWindowTask, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public AuditCommonResult<List<AuditOrgaMemberTask>> getTaskByWindow(String windowGuid) {
        AuditOrgaService<AuditOrgaMemberTask> auditWindowTaskService = new AuditOrgaService<AuditOrgaMemberTask>();
        AuditCommonResult<List<AuditOrgaMemberTask>> result = new AuditCommonResult<List<AuditOrgaMemberTask>>();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("memberguid", windowGuid);
            sql.setOrderDesc("ordernum");
            List<AuditOrgaMemberTask> windowTaskList = auditWindowTaskService.getAllRecord(AuditOrgaMemberTask.class,
                    sql.getMap());
            result.setResult(windowTaskList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Record> getRenlingListByAreacode(String s) {
        return new GxhAuditOrgaMemberService().getRenlingListByAreacode(s);
    }

    @Override
    public List<Record> getMemberGuidByTaskId(String taskid) {
        return new GxhAuditOrgaMemberService().getMemberGuidByTaskId(taskid);
    }

    @Override
    public List<RenlingRecord> getWfkOption() {
        return new GxhAuditOrgaMemberService().getWfkOption();
    }


}

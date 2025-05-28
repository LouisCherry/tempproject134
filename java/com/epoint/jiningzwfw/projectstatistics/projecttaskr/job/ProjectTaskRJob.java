package com.epoint.jiningzwfw.projectstatistics.projecttaskr.job;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.IProjectTaskRService;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.entity.ProjectTaskR;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.impl.ProjectTaskRService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 每日办件事项统计
 */
@DisallowConcurrentExecution
public class ProjectTaskRJob implements Job {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IProjectTaskRService iProjectTaskRService = ContainerFactory.getContainInfo().getComponent(IProjectTaskRService.class);
    private IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            log.info("开始执行统计每日办件事项Job ,开始时间：" + EpointDateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));
            EpointFrameDsManager.begin(null);
            ProjectTaskRService projectTaskRService = new ProjectTaskRService();
            List<Record> allTaskList = new ArrayList<>();
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            //1.获取记录的最小时间
            String minDate = "";
            //1.1 查询每日办件事项记录表（PROJECT_TASK_R）SDate字段最大时间
            sqlConditionUtil.setSelectFields(" MAX(SDate) as minDate ");
            List<ProjectTaskR> list = iProjectTaskRService.findListByCondition(sqlConditionUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(list)) {
                minDate = list.get(0).get("minDate");
            }
            if (StringUtil.isBlank(minDate)) {
                //1.2查询audit_project 和 lc_project中applydate最小的时间
                minDate = projectTaskRService.getMinDateFromTables();
            }
            minDate = "2021-06-28 00:00:00";
            //2.获取到当前日期
            Date now = new Date();
            Integer intervals = EpointDateUtil.getIntervalDays(EpointDateUtil.convertString2Date(minDate), now);
            for (int i = 0; i < intervals; i++) {
                //获取每日办件办理事项的情况和部分办件情况
                String date  = EpointDateUtil.convertDate2String(EpointDateUtil.addDay(EpointDateUtil.convertString2Date(minDate), i),"yyyy-MM-dd");
                String startDate = date + " 00:00:00";
                String endDate = date + " 23:59:59";
                List<Record> taskLists = projectTaskRService.getTASKInfoByDate(startDate,endDate);
                if (ValidateUtil.isNotBlankCollection(taskLists)) {
                    allTaskList.addAll(taskLists);
                }
            }
            //3.循环记录，根据记录的TASKGUID，从事项基本信息表中获取TASK_ID和ITEM_ID
            if (ValidateUtil.isNotBlankCollection(allTaskList)) {
                AuditTask auditTask = new AuditTask();
                for (Record record : allTaskList) {
                    auditTask = iAuditTask.getAuditTaskByGuid(record.getStr("TASKGUID"), true).getResult();
                    if (ValidateUtil.isNotNull(auditTask)){
                        //4.存入PROJECT_TASK_R中
                        ProjectTaskR projectTaskR = new ProjectTaskR();
                        projectTaskR.setSdate(record.getStr("APPLYDATE"));
                        projectTaskR.setAreacode(record.getStr("AREACODE"));
                        projectTaskR.setOuguid(record.getStr("OUGUID"));
                        projectTaskR.setOuname(record.getStr("OUNAME"));
                        projectTaskR.setTask_id(auditTask.getTask_id());
                        projectTaskR.setItem_id(auditTask.getItem_id());
                        projectTaskR.setRowguid(UUID.randomUUID().toString());
                        //4.根据SDate及Task_ID判断是否存在、
                        sqlConditionUtil.clear();
                        sqlConditionUtil.eq("SDate",projectTaskR.getSdate());
                        sqlConditionUtil.eq("TASK_ID",projectTaskR.getTask_id());
                        List<ProjectTaskR> list1 = iProjectTaskRService.findListByCondition(sqlConditionUtil.getMap()).getResult();
                        if(ValidateUtil.isBlankCollection(list1)){
                            iProjectTaskRService.insert( projectTaskR);
                        }

                    }
                }
            }
            log.info("结束执行统计每日办件事项Job ,结束时间：" + EpointDateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));
            EpointFrameDsManager.commit();
        } 
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } 
        finally {
            EpointFrameDsManager.close();
        }
    }


}

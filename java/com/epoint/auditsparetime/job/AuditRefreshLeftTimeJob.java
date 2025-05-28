package com.epoint.auditsparetime.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;

@DisallowConcurrentExecution
public class AuditRefreshLeftTimeJob implements Job
{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            IAuditOrgaServiceCenter auditOrgaServiceCenter = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaServiceCenter.class);
            IAuditOrgaWorkingDay auditOrgaWorkingDay = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaWorkingDay.class);
            IAuditProjectSparetime auditProjectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            int flag = 0;
            // 先计算一下任务的时间间隔
            CronTriggerImpl cTriggerImpl=(CronTriggerImpl)context.getTrigger();
            //上一次执行服务时间，如果为null，则为第一次执行服务
            if(context.getPreviousFireTime() == null){
                flag = 1;
            }
            else{
                flag = 0;
            }

            String ex=cTriggerImpl.getCronExpression();
            int index=ex.indexOf("/");
            int minutes=Integer.parseInt(ex.substring(index+1, index+2));
/*            CronExpression cX= new CronExpression(ex);
            SimpleTriggerImpl triggerImpl = (SimpleTriggerImpl) context.getTrigger();*/
           // int minutes = (int) triggerImpl.getRepeatInterval() / 60000;
            // 获取一下当前所有的中心
            SqlConditionUtil conditionMap = new SqlConditionUtil();
            conditionMap.eq("length(belongxiaqu)", "6");
            List<AuditOrgaServiceCenter> listServiceCenter = auditOrgaServiceCenter
                    .getAuditOrgaServiceCenterByCondition(conditionMap.getMap()).getResult();
            int noCommonTime = 0;
            for (AuditOrgaServiceCenter serviceCenter : listServiceCenter) {
                //根据是否是第一次开启服务判断服务是否出现暂停，若出现则将暂停时间加入办件剩余时间
                if(flag == 1){
                    List<AuditProjectSparetime> spareTimes = auditProjectSparetime.getSparetimeByCenterGuid(serviceCenter.getRowguid()).getResult();
                    for(AuditProjectSparetime spareTime : spareTimes){
                    	 AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(spareTime.getProjectguid(), spareTime.getAreacode())
                                 .getResult();
                         if (auditProject != null && spareTime.getRefreshdate() != null) {
                                 noCommonTime = auditOrgaWorkingDay.getWorkingMinutesFromStartToEnd(spareTime.getRefreshdate() , new Date() , serviceCenter.getRowguid()).getResult();
                                 break;     
                         }else{
                            continue;
                         }   
                    }
                    if(noCommonTime > 0){
                        auditProjectSparetime.refreshSpareTime(serviceCenter.getRowguid(), noCommonTime);
                        EpointFrameDsManager.commit();
                    }
                }else{
                    // 判断今天是不是工作日
                    int count = auditOrgaWorkingDay
                            .GetWorkingDays_Between_From_To(serviceCenter.getRowguid(), new Date(), new Date()).getResult();
                    if (count > 0) {
//                        auditProjectSparetime.deleteSpareTimeByTemp();
                        auditProjectSparetime.refreshSpareTime(serviceCenter.getRowguid(), minutes);
                        EpointFrameDsManager.commit();
                    }
                }    
            }
            EpointFrameDsManager.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }
  

}

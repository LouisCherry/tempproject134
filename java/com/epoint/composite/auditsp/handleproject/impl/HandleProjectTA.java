package com.epoint.composite.auditsp.handleproject.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

@Component
@Service
public class HandleProjectTA
{
    public String initProject(String taskGuid, String projectGuid, String winuserguid, String operateUserName,
            String windowGuid, String windowName, String centerGuid, String certNum, String queueGuid,
            AuditQueueUserinfo userinfo) {
        try {
            AuditProject e = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            e.setOperateusername(operateUserName);
            e.setOperatedate(new Date());
            e.setRowguid(rowGuid);
            e.setWindowguid(windowGuid);
            e.setWindowname(windowName);
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                e.setTask_id(auditTask.getTask_id());
                e.setTaskguid(auditTask.getRowguid());
            }

            CommonDao dao = new CommonDao();

            String sql = "select operateusername from audit_task where RowGuid = ? ";
            String operateusername = dao.queryString(sql, e.getTaskguid());

            String serviceType = "同步服务";
            if (serviceType.equals(operateusername)) {

                String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());
                e.setFlowsn(flowsn);
            }
            else {
                String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());

                e.setFlowsn(flowsn);
            }

            e.setOuguid(auditTask.getOuguid());
            e.setOuname(auditTask.getOuname());
            e.setProjectname(auditTask.getTaskname());
            e.setIs_charge(auditTask.getCharge_flag());
            e.setIf_express("0");
            e.setCenterguid(centerGuid);
            String areacode = auditTask.getAreacode();
            e.setAreacode(areacode);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
            if (auditTaskExtension != null) {
                e.setCharge_when(auditTaskExtension.getCharge_when());
                e.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());
                e.setIf_express(auditTaskExtension.getIf_express());
            }

            e.setTasktype(auditTask.getType());
            e.setPromise_day(auditTask.getPromise_day());

            //默认个人
            e.setApplyertype(20);
            e.setCerttype("14");

            e.setApplydate(new Date());
            e.setApplyway(20);
            e.setIs_test(0);
            e.setIs_delay(20);
            e.setCerttype("22");
            e.setCertnum(certNum);
            if (null != userinfo) {
                e.setApplyername(userinfo.getDisplayname());
                e.setAddress(userinfo.getAddress());
                e.setContactmobile(userinfo.getMobile());
                e.setContactmobile(userinfo.getMobile());
                e.setContactphone(userinfo.getMobile());
                e.setContactperson(userinfo.getDisplayname());
            }
            e.set("queueguid", queueGuid);
            e.setHandleareacode(auditTask.getAreacode() + ",");
            e.setCurrentareacode(auditTask.getAreacode());

            //直接设为接件状态
            e.setStatus(26);
            e.setReceiveuserguid(winuserguid);
            e.setReceiveusername(operateUserName);
            e.setReceivedate(new Date());
            e.setAcceptuserguid(winuserguid);
            e.setAcceptusername(operateUserName);
            e.setAcceptuserdate(new Date());

            IAuditProject auditProjectService1 = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService1.addProject(e);
            IAuditTaskMaterial auditTaskMaterialService1 = (IAuditTaskMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService1 = (IAuditProjectMaterial) ContainerFactory
                    .getContainInfo().getComponent(IAuditProjectMaterial.class);
            List<AuditTaskMaterial> auditTaskMaterials = (List<AuditTaskMaterial>) auditTaskMaterialService1
                    .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), true).getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            Iterator<AuditTaskMaterial> arg22 = auditTaskMaterials.iterator();

            while (arg22.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) arg22.next();
                auditProjectMaterial.clear();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(e.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(Integer.valueOf(10));
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(Integer.valueOf(0));
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterialService1.addProjectMateiral(auditProjectMaterial);
            }
            return rowGuid;
        }
        catch (Exception arg24) {
            arg24.printStackTrace();
            return null;
        }
    }

    /**
     * 
     *  [办结]
     *  [功能详细描述]
     *  @param auditProject
     *  @param operateUserName
     *  @param operateUserGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void handleFinish(AuditProject auditProject, String operateUserName, String operateUserGuid) {
        auditProject.setStatus(90);
        auditProject.setBanjiedate(new Date());
        auditProject.setBanjieuserguid(operateUserGuid);
        auditProject.setBanjieusername(operateUserName);
        auditProject.setIs_guidang(1);
        auditProject.setGuidangdate(new Date());
        auditProject.setGuidanguserguid(operateUserGuid);
        auditProject.setGuidangusername(operateUserName);
        auditProject.setBanjieresult(40);
        IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                .getComponent(IAuditProject.class);
        IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
        IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        //查看有无centerguid
        if(StringUtils.isBlank(auditProject.getCenterguid())){
            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
            if(auditOrgaServiceCenter!=null){
                auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
            }
        }
        //更新承诺办结时间
        AuditTask  auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
        if(auditTask!=null) {
            List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
            Date acceptdat = auditProject.getAcceptuserdate();
            Date shouldEndDate;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOrgaWorkingDay.class);
                shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                        auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
            } else {
                shouldEndDate = null;
            }
            if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                Duration totalDuration = Duration.ZERO;  // 用于累加时间差（以秒为单位）
                LocalDateTime currentTime = null;
                for(AuditProjectUnusual auditProjectUnusual:auditProjectUnusuals) {
                    // 将Date转换为Instant
                    Instant instant = auditProjectUnusual.getOperatedate().toInstant();
                    if(10==auditProjectUnusual.getOperatetype()){
                        // 通过Instant和系统默认时区获取LocalDateTime
                        currentTime= LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    }
                    if(currentTime!=null && 11==auditProjectUnusual.getOperatetype()){
                        LocalDateTime nextTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        Duration danci = Duration.between(currentTime, nextTime);
                        totalDuration = totalDuration.plus(danci);
                        currentTime = null;
                    }
                }
                // 将累加的时间差加到初始的Date类型的shouldEndDate上
                Instant instant = shouldEndDate.toInstant();
                Instant newInstant = instant.plus(totalDuration);
                shouldEndDate = Date.from(newInstant);
            }
            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
        }
        auditProjectService.updateProject(auditProject);
    }

    private String getStrFlowSn(String numberName, String numberFlag, int snLength) {
        String flowSn = "";
        Object[] args = new Object[3];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(snLength);

        try {
            flowSn = CommonDao.getInstance("common")
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn2", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }

    private String getStdFlowSn(String numberName, String numberFlag, int theYearLength, int snLength) {
        String flowSn = "";
        Object[] args = new Object[7];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));

        args[3] = Integer.valueOf(theYearLength);

        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);

        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));

        args[6] = Integer.valueOf(snLength);

        try {
            flowSn = CommonDao.getInstance("common")
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }
}

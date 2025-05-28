
package com.epoint.audithandlecontrol.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.handlecontrol.domain.AuditTaskHandleControl;
import com.epoint.basic.audittask.handlecontrol.inter.IAuditTaskHandleControl;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.api.IHandleControlGxh;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

import java.util.HashMap;
import java.util.List;


public class AuditHandleControlGXHService implements IHandleControlGxh {

    @Override
    public HashMap<String, HashMap> initHandleControl(String arg0, String arg1, String arg2, String arg3, String arg4,
                                                      String arg5, String arg6, HashMap<String, HashMap> arg7) {

        IConfigService configservice9 = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IWFInstanceAPI9 wfinstanceApi9 = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        String isgd = configservice9.getFrameConfigValue("Is_guidang");
        IAuditProject auditservice = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTask auditTaskBasic = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditTaskHandleControl auditTaskhandleControlService = ContainerFactory.getContainInfo().getComponent(IAuditTaskHandleControl.class);
        IUserRoleRelationService roleRelationService = ContainerFactory.getContainInfo().getComponent(IUserRoleRelationService.class);
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);


        AuditTask auditTask = auditTaskBasic.getAuditTaskByGuid(arg1, false).getResult();
        String is_den = "";
        HashMap<String, HashMap> rtnMap = arg7;
        HashMap<String, String> btnMap = rtnMap.get("btnMap");
        if (StringUtil.isNotBlank(auditTask)) {
            AuditProject auditProject = auditservice.getAuditProjectByRowGuid(arg0, "").getResult(); //根据办件实例唯一标识获取办件实例
            //如果是前台工作人员 除去接件和预审通过，预审打回按钮 其他都隐藏
            boolean flag = roleService.isExistUserRoleName(UserSession.getInstance().getUserGuid(), "前台工作人员");
            if (flag) {
                // 获取当前辖区下事项模板里配置的按钮
                List<AuditTaskHandleControl> listControl = auditTaskhandleControlService
                        .selectAuditTaskHandleControlByAreaCode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                //事项模板按钮
                if(!listControl.isEmpty()){
                    for (AuditTaskHandleControl control : listControl) {
                        String controlid = control.getControlid();
                        if (!"btnYstg".equals(controlid) && !"btnYsdh".equals(controlid) && !"btnReceive".equals(controlid)) {
                            btnMap.put(control.getControlid(), "0");
                        }
                    }
                }
                //工作流按钮
                if (!rtnMap.get("lbl").isEmpty()) {
                    rtnMap.get("lbl").put("handlecontrols_btnlst", "0");
                }
            } else {
                if (StringUtil.isNotBlank(auditTask)) {
                    if (StringUtil.isNotBlank(auditTask.getStr("scxtcode")) && auditProject.getStatus().equals(ZwfwConstant.BANJIAN_STATUS_YJJ)) {
                        btnMap.put("btnRsAccept", "1");
                        btnMap.put("btnAccept", "0");
                    }
                }
                String taskname = auditTask.getTaskname();
                if ("建筑业企业资质（告知承诺方式）增项".equals(taskname)
                        || "建筑业企业资质（增项）".equals(taskname)
                        || "建筑业企业资质许可（升级）".equals(taskname)
                        || "建筑业企业资质（告知承诺方式）新申请".equals(taskname)
                        || "建筑业企业资质（首次申请）".equals(taskname)
                ) {
                    btnMap.put("btnUserSkill", "1");
                }
                btnMap.put("btnEvaluate", "0");
                AuditTaskExtension taskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                String isKy = taskExtension.getStr("is_ky");
                if ("1".equals(isKy)) {
                    if (auditProject.getStatus()>=(ZwfwConstant.BANJIAN_STATUS_YJJ)) {
                        btnMap.put("btnKt", "1");
                    }
                }
//                "城市大型户外广告设置审核".equals(taskname)||"在城市建筑物、设施上、张挂、张贴宣传品设立".equals(taskname)
//                if (auditProject.getStatus()>=(ZwfwConstant.BANJIAN_STATUS_YJJ)) {
//                    btnMap.put("btnKt", "1");
//                }
               
            }

        }


        // 办结步骤必显示办结按钮
        /*HashMap btnMap = rtnMap.get("btnMap");
        AuditProject auditproject = auditservice.getAuditProjectByRowGuid("rowguid, pviguid, taskguid, status", arg0, "").getResult();
        if (auditproject != null && StringUtil.isNotBlank(arg2)) {
            ProcessVersionInstance pvi = wfinstanceApi9.getProcessVersionInstance(auditproject.getPviguid());
            if (pvi != null) {
                WorkflowWorkItem workItem = wfinstanceApi9.getWorkItem(pvi, arg2);
                if (workItem != null && "办结".equals(workItem.getActivityName())) {
                    btnMap.put("btnFinish", "1");
                }
            }
        } else if (auditproject != null && (50 == auditproject.getStatus() || 40 == auditproject.getStatus())){
            btnMap.put("btnFinish", "1");
        }*/

        return rtnMap;

    }
}


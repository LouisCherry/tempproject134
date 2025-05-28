package com.epoint.auditproject.audithandlecontrol.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.util.TARequestUtil;
import com.epoint.workflow.service.common.runtime.WorkflowResult9;
import com.esotericsoftware.minlog.Log;

public class SgSendNotice {
	
    public static final String lclcurl= ConfigUtil.getConfigValue("epointframe", "lclcurl");
    
    public static final String lcjbxxurl= ConfigUtil.getConfigValue("epointframe", "lcjbxxurl");

    /**
     * 淇县请假流程工作流，待办处理通知
     * @param pviguid
     * @param rule
     * @throws InterruptedException 
     */
    public void handleNotice(WorkflowResult9 wfr) throws InterruptedException{
        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
        String pviguid = wfr.getCurrentWorkItem().getProcessVersionInstanceGuid();
        AuditProject auditProject = iAuditProject.getAuditProjectByPVIGuid("rowguid,areacode,taskguid", pviguid,"").getResult();
        if (auditProject != null) {
        	 
   	     	AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditProject.getTaskguid() , true).getResult();

   	     	if (auditTaskExtension != null) {
   	     	 JSONObject submit = new JSONObject();
             JSONObject params = new JSONObject();
             
             params.put("projectguid", auditProject.getRowguid());
             params.put("areacode", auditProject.getAreacode());
             params.put("acceptusername", StringUtil.isNotBlank(auditProject.getAcceptusername()) ?  auditProject.getAcceptusername():"一窗系统");
             submit.put("params", params);
             
             String resultsign = "";
             
             JSONObject submit2 = new JSONObject();
             
             submit2.put("projectGuid", auditProject.getRowguid());
             submit2.put("areaCode", auditProject.getAreacode());
             params.put("acceptusername", StringUtil.isNotBlank(auditProject.getAcceptusername()) ?  auditProject.getAcceptusername():"一窗系统");
             submit2.put("status", "01");
             
             String resultsign2 = "";
     		
             if("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
             	resultsign = TARequestUtil.sendPostInner(lcjbxxurl, submit.toJSONString(), "", "");
             	Thread.sleep(2000);
             	resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
             	Log.info("推送浪潮办件结果："+resultsign+";办件编号为："+auditProject.getFlowsn());
             	Log.info("推送浪潮受理流程结果："+resultsign2+";办件编号为："+auditProject.getFlowsn());
             }
   	     	}
        }
    }

}

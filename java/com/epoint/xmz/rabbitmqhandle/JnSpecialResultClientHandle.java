package com.epoint.xmz.rabbitmqhandle;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.util.TARequestUtil;

/**
 * 接办分离特殊操作结果消费者客户端
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnSpecialResultClientHandle extends AuditClientMessageListener{
	
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String lclcurl= ConfigUtil.getConfigValue("epointframe", "lclcurl");
	

    /**
     *  特殊操作结果逻辑
     *  @param proMessage 参数
     *  @return    
     * @exception/throws 
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
    	IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
    	IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    	
    	IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
    	
        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        String projectID = messageContent[0];
        //申请人姓名
        String areaCode = messageContent[1];
       
        JSONObject submit2 = new JSONObject();
        
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();
        
        //获取事项信息
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
       
        
        String resultsign2 = "";
        
        submit2.put("projectGuid", projectID);
        submit2.put("areaCode", areaCode);
        submit2.put("status", "03");
        
        if("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
            
            resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
            
            log.info("推送浪潮受理流程结果："+resultsign2+";办件编号为："+projectID);
        }
      
        
   
        
        
    }

}

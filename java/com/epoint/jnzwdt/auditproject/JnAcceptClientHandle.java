package com.epoint.jnzwdt.auditproject;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.container.ContainerFactory;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

/**
 * 自动接件操作消费者客户端
 */
public class JnAcceptClientHandle extends AuditClientMessageListener{
	
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
		
    /**
     *  受理操作逻辑
     *  @param proMessage 参数
     *  @return    
     * @exception/throws 
     */
		
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
    	IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IHandleProject handleProjectService = ContainerFactory.getContainInfo().getComponent(IHandleProject.class);

        try{
            //解析mq消息内容
            String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
            log.info("proMessage.getSendmessage():"+proMessage.getSendmessage());
            //办件主键
            String projectID = messageContent[0];
            //用户guid
            String userguid = messageContent[1];

            //窗口guid
            String windowguid = messageContent[2];
            //辖区
            String areaCode = messageContent[3];

            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectID, areaCode).getResult();

            handleProjectService.handleAccept(auditProject, "", "华润燃气", userguid, "综合窗口",
                    windowguid);
        }catch (Exception e){
            log.info("proMessage.getSendmessage():"+proMessage.getSendmessage());
            e.printStackTrace();
        }

    }

}

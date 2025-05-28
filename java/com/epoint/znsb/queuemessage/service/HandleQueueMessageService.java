package com.epoint.znsb.queuemessage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.znsb.queuemessage.inter.IHandleQueueMessage;

@Service
@Primary
public class HandleQueueMessageService implements IHandleQueueMessage
{
    @Autowired
    IAuditOrgaWindow windowservice;
    @Autowired
    IHandleConfig handleConfigservice;
    @Autowired
    IConfigService configservice;
    @Autowired
    IMessagesCenterService messageservice;

    @Autowired
    IAuditOrgaHall hallservice;
    @Autowired
    IAuditOrgaServiceCenter centerservice;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleRelationService roleRelationService;
    @Autowired
    private IUserService userservice;

    @Override
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowN0, String CenterGuid, String smstype,
            Record record) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        return result;
    }

    @Override
    public AuditCommonResult<String> callByVoice(String QNO, String WindowNO, String WindowGuid, String centerguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        return result;
    }

    @Override
    public void sendTaskTypeEwMessage(Integer count, String tasktypeguid, String centerguid) {

    }

    @Override
    public AuditCommonResult<String> sendLEDMQ(String QNO, String WindowNO, String CenterGuid, String WindowStatus) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        return result;
    }

    private void sendVoiceMQ(String QNO, String MacAddress, String WindowNO, String centerguid) {

    }

    private void sendLEDMQ(String content, String WindowNO, String CenterGuid) {
        try {
            JSONObject dataJson = new JSONObject();
            dataJson.put("regionName", WindowNO);
            dataJson.put("content", content);
            ProducerMQ.send("QueueLed_" + CenterGuid, "[" + dataJson.toString() + "]");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

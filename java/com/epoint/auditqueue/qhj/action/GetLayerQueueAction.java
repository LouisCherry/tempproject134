package com.epoint.auditqueue.qhj.action;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.basic.controller.BaseController;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.utils.string.StringUtil;

@RestController("getlayerqueueaction")
@Scope("request")
public class GetLayerQueueAction extends BaseController
{

    private static final long serialVersionUID = 1177302451667715305L;

    @Autowired
    private IHandleQueue handlequeueservice;

    @Autowired
    private IAuditQueueAppointment appointmentservice;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void pageLoad() {
    }

    public void getAPQNO() {
        AuditQueueAppointment auditqueueappointment = appointmentservice
                .getDetailByRowGuid(getRequestParameter("appointguid")).getResult();
        if (auditqueueappointment != null && StringUtil.isBlank(auditqueueappointment.getGetnotime())) {
            Map<String, String> map = handlequeueservice
                    .getAPQNO(getRequestParameter("appointguid"), getRequestParameter("Centerguid")).getResult();
            addCallbackParam("msg", map.get("msg"));
            addCallbackParam("qno", map.get("qno"));
        }
        else {
            addCallbackParam("msg", "未找到您的预约记录，请确定未取号！");
        }
    }

    public void getAppointment() {
        AuditQueueAppointment auditqueueappointment = appointmentservice
                .getDetailByRowGuid(getRequestParameter("appointguid")).getResult();
        if (auditqueueappointment != null && StringUtil.isBlank(auditqueueappointment.getGetnotime())
                && StringUtil.isNotBlank(auditqueueappointment.getIdentitycardnum())) {
            addCallbackParam("msg", "success");
            addCallbackParam("idcard", auditqueueappointment.getIdentitycardnum());
        }
        else {
            log.info("GetLayerQueueAction getAppointment 入参：" + getRequestParameter("appointguid"));
            log.info("GetLayerQueueAction getAppointment 查询结果：" + auditqueueappointment);
            addCallbackParam("msg", "未找到您的预约记录！");
        }

    }

}

package com.epoint.mq.spgl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.spgl.domain.*;
import com.epoint.basic.spgl.inter.*;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.sqgl.common.util.Zjconstant;

/**
 * 消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class JnSpglxmspsxblxxxxbClientHandle extends AuditClientMessageListener
{

    private static Logger log = LogUtil.getLog(JnSpglxmspsxblxxxxbClientHandle.class);

    // 设置消息类型，判断消息是监听住建系统数据
    public JnSpglxmspsxblxxxxbClientHandle() {
        super.setMassagetype(MQConstant.MESSAGETYPE_ZJ);
    }

    /**
     * 办理环节操作逻辑
     *
     * @param proMessage
     *            参数
     * @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {

        IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        ISpglXmspsxblxxxxb ispglxmspsxblxxxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxxxb.class);

        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);

        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);

        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);

        IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);

        IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);

        IAuditProjectOperation operationservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectOperation.class);

        IAuditProjectMaterial iauditprojectmaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);

        ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglDfxmsplcjdsxxxb.class);

        IAttachService iattachservice = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        ISpglXmspsxblxxb ispglxmspsxblxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxblxxb.class);

        ISpglXmqtfjxxb ispglxmqtfjxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmqtfjxxb.class);

        ISpglXmspsxpfwjxxb ispglxmspsxpfwjxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxpfwjxxb.class);

        ISpglXmspsxblqthjxxb ispglxmspsxblqthjxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblqthjxxb.class);

        ISpglXmjbxxb iSpglXmjbxxbService = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxb.class);

        IAuditProjectUnusual projectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);

        IOuService iouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);

        IUserService iuserservice = ContainerFactory.getContainInfo().getComponent(IUserService.class);

        ISpglXmspsxbltbcxxxb ispglxmspsxbltbcxxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxbltbcxxxb.class);
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

        IAuditSpITask auditSpITaskService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        if (iauditproject == null || ispglxmspsxblxxxxb == null || iauditspbusiness == null || iauditorgaarea == null
                || iAuditRsItemBaseinfo == null || iauditspinstance == null || iauditspisubapp == null
                || operationservice == null || iauditprojectmaterial == null || ispgldfxmsplcjdsxxxb == null
                || iattachservice == null || ispglxmspsxblxxb == null || ispglxmqtfjxxb == null
                || ispglxmspsxpfwjxxb == null || ispglxmspsxblqthjxxb == null || iSpglXmjbxxbService == null
                || projectUnusual == null || iouservice == null || iuserservice == null
                || ispglxmspsxbltbcxxxb == null) {
            return;
        }
        // 解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        log.info("proMessage.getSendmessage()" + proMessage.getSendmessage());

        if (messageContent == null || messageContent.length < 1) {
            return;
        }
        // 办件主键
        String projectID = messageContent[0];

        if (proMessage.getSendroutingkey().split("\\.") == null
                || proMessage.getSendroutingkey().split("\\.").length < 2) {
            return;
        }
        log.info("proMessage.getSendroutingkey()" + proMessage.getSendroutingkey());

        // 辖区编码
        String areacode = proMessage.getSendroutingkey().split("\\.")[1];
        // 获取操作
        String operate = proMessage.getSendroutingkey().split("\\.")[2];

        String msg = "";
        for (int i = 0; i < messageContent.length; i++) {
            if (StringUtil.isNotBlank(msg)) {
                msg += "." + messageContent[i];
            }
            else {
                msg += messageContent[i];
            }
        }

        sendMQMessageService.sendByExchange("exchange_handle", msg, "projectV3." + areacode + "." + operate + ".1");

        String operatetype = "";

        AuditProject project = iauditproject.getAuditProjectByRowGuid(projectID, areacode).getResult();

        if (project == null) {
            return;
        }

        if (StringUtil.isBlank(project.getBiguid())) {
            return;
        }
        AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(project.getBiguid()).getResult();
        if (auditspinstance == null) {
            return;
        }

        AuditSpITask auditSpITask = auditSpITaskService.getAuditSpITaskByProjectGuid(project.getRowguid()).getResult();
        if (auditSpITask == null) {
            return;
        }

        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false).getResult();
        if (auditTask == null) {
            return;
        }
        boolean isgyxtproject = false;
        // 山东共用系统推送事项编码
        String sdfyxtpushtaskcode = configService.getFrameConfigValue("sdfyxtpushtaskcode");
        log.info("sdfyxtpushtaskcode--->" + sdfyxtpushtaskcode);

        List<String> pushTaskItemIdList = new ArrayList<>();
        if (StringUtil.isNotBlank(sdfyxtpushtaskcode)) {
            pushTaskItemIdList = Arrays.stream(sdfyxtpushtaskcode.split(",")).collect(Collectors.toList());
        }
        if (pushTaskItemIdList.contains(auditTask.getTaskcode())) {
            isgyxtproject = true;
        }

        AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(project.getSubappguid()).getResult();
        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                .getResult();
        if (baseinfo == null) {
            return;
        }
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                .getResult();
        if (auditspbusiness == null) {
            return;
        }

        String sjsczt = "0";
        StringBuilder sbyy = new StringBuilder();// 说明

        String businessareacode = auditspbusiness.getAreacode();
        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
        if (area != null) {
            // 如果是县级，查找市级主题
            if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                // 查找市级辖区
                AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                if (sjarea == null) {
                    sjsczt = "-1";
                    sbyy.append("该主题类型未存在市级主题中！");
                }
                else {
                    sqlc.clear();
                    sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                    sqlc.eq("areacode", sjarea.getXiaqucode());
                    List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(listb)) {
                        auditspbusiness = listb.get(0);
                        areacode = sjarea.getXiaqucode();
                    }
                    else {
                        sjsczt = "-1";
                        sbyy.append("该主题类型未存在市级主题中！");
                    }
                }
            }
        }
        sub.setBusinessguid(auditspbusiness.getRowguid());

        AuditProjectUnusual unusual = null;
        // 根据mq的中的操作类型处理数据
        if (StringUtil.isNotBlank(operate)) {
            switch (operate) {
                case "accept":
                    operatetype = ZwfwConstant.OPERATE_SL;
                    break;
                case "notaccept":
                    operatetype = ZwfwConstant.OPERATE_BYSL;
                    break;
                case "sendresult":
                    operatetype = ZwfwConstant.OPERATE_BJ;
                    break;
                case "bz":
                    // 如果补正，插入特殊操作数据
                    operatetype = ZwfwConstant.OPERATE_BZ;
                    break;
                case "bzwc":
                    String opereateuser = messageContent[2];
                    // 如果补正完成需要单独插入数据
                    SpglXmspsxblxxxxb spgl = new SpglXmspsxblxxxxb();
                    spgl.setRowguid(UUID.randomUUID().toString());
                    spgl.setDfsjzj(projectID);
                    spgl.setXzqhdm("370800");
                    spgl.setGcdm(baseinfo.getItemcode());
                    spgl.setSpsxslbm(project.getFlowsn());
                    spgl.setBlcs(iouservice.getOuNameByUserGuid(opereateuser));
                    spgl.setBlr(iuserservice.getUserNameByUserGuid(opereateuser));
                    spgl.setBlsj(proMessage.getOperatedate());
                    spgl.setBlyj("补正完成");
                    spgl.setBlzt(7);
                    spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if (!iSpglXmjbxxbService.isExistGcdm(spgl.getGcdm())) {
                        spgl.set("sjsczt", "-1");
                        spgl.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxb.isExistFlowsn(spgl.getSpsxslbm())) {
                        spgl.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(spgl.getSbyy())) {
                            spgl.setSbyy(spgl.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        else {
                            spgl.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }
                    ispglxmspsxblxxxxb.insert(spgl);
                    break;
                case "specialresult":
                    // 延期申请处理数据
                    String unusualguid = messageContent[1];
                    unusual = projectUnusual.getAuditProjectUnusualByRowguid(unusualguid).getResult();
                    if (unusual == null) {
                        break;
                    }
                    if (unusual.getOperatetype() == Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_CX)) {
                        operatetype = ZwfwConstant.OPERATE_CXSQ;
                        break;
                    }

                    if (unusual.getOperatetype() == Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZZ)) {
                        operatetype = ZwfwConstant.OPERATE_YCZZ;
                        break;
                    }

                    if (unusual.getOperatetype() == Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT)) {
                        operatetype = ZwfwConstant.OPERATE_ZTJS;
                        break;
                    }
                    if (unusual.getOperatetype() == Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_HF)) {
                        operatetype = ZwfwConstant.OPERATE_HFJS;
                        break;
                    }
                    break;
                case "finishfile":
                    // 查入批复文件信息
                    SpglXmspsxpfwjxxb jxxb = new SpglXmspsxpfwjxxb();
                    jxxb.setDfsjzj(project.getRowguid());
                    jxxb.setXzqhdm("370800");
                    jxxb.setGcdm(baseinfo.getItemcode());
                    jxxb.setSpsxslbm(project.getFlowsn());
                    jxxb.setPfrq(project.getCertificatedate());
                    jxxb.setPfwh(project.getWenhao());
                    jxxb.setPfwjbt("批文");
                    jxxb.setPfwjyxqx(EpointDateUtil.convertString2DateAuto("9999-01-01"));
                    List<FrameAttachInfo> attachlist = iattachservice.getAttachInfoListByGuid(projectID);
                    if (attachlist == null) {
                        break;
                    }
                    // 多个批文文件附件处理，插入多次
                    for (FrameAttachInfo frameAttachInfo : attachlist) {
                        jxxb.setRowguid(UUID.randomUUID().toString());
                        jxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                        jxxb.setFjid(frameAttachInfo.getAttachGuid());
                        jxxb.setFjmc(frameAttachInfo.getAttachFileName());
                        jxxb.setFjlx(frameAttachInfo.getContentType());
                        jxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        jxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

                        // 数据验证,项目代码是否在工程代码
                        if (!iSpglXmjbxxbService.isExistGcdm(baseinfo.getItemcode())) {
                            jxxb.set("sjsczt", "-1");
                            jxxb.setSbyy("批文工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                        }
                        if (!ispglxmspsxblxxb.isExistFlowsn(project.getFlowsn())) {
                            jxxb.set("sjsczt", "-1");
                            if (StringUtil.isNotBlank(jxxb.getSbyy())) {
                                jxxb.setSbyy(jxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                            else {
                                jxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                        }
                        ispglxmspsxpfwjxxb.insert(jxxb);
                    }
                    break;
                case "handlenotpass":
                    opereateuser = messageContent[1];
                    // 审核通过
                    SpglXmspsxblxxxxb ztjs = new SpglXmspsxblxxxxb();
                    ztjs.setRowguid(UUID.randomUUID().toString());
                    ztjs.setDfsjzj(projectID);
                    ztjs.setXzqhdm(areacode);
                    ztjs.setGcdm(baseinfo.getItemcode());
                    ztjs.setSpsxslbm(project.getFlowsn());
                    ztjs.setBlcs(Zjconstant.getOunamebyuser(opereateuser));
                    ztjs.setBlr(iuserservice.getUserNameByUserGuid(opereateuser));
                    ztjs.setBlsj(proMessage.getOperatedate());
                    ztjs.setBlyj("特别程序（开始）");
                    ztjs.setBlzt(9);
                    ztjs.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    ztjs.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if (!iSpglXmjbxxbService.isExistGcdm(ztjs.getGcdm())) {
                        ztjs.set("sjsczt", "-1");
                        ztjs.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxb.isExistFlowsn(ztjs.getSpsxslbm())) {
                        ztjs.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(ztjs.getSbyy())) {
                            ztjs.setSbyy(ztjs.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        else {
                            ztjs.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }
                    ispglxmspsxblxxxxb.insert(ztjs);
                    break;
                case "jj":
                    operatetype = ZwfwConstant.OPERATE_JJ;
                    break;
                case "bmksbl":
                    operatetype = ZwfwConstant.OPERATE_SPTG;
                    break;
                default:
                    break;
            }
        }
        if (StringUtil.isBlank(operatetype)) {
            return;
        }
        AuditProjectOperation auditProjectOperation = operationservice.getAuditOperation(projectID, operatetype)
                .getResult();
        SpglXmspsxblxxxxb spgl = new SpglXmspsxblxxxxb();
        spgl.setRowguid(UUID.randomUUID().toString());
        if (auditProjectOperation == null) {
            spgl.setDfsjzj(UUID.randomUUID().toString());
            if (StringUtil.isBlank(project.getBanjieusername())) {
                spgl.setBlr("浪潮云平台");
            }
            else {
                spgl.setBlr(project.getBanjieusername());
            }
            spgl.setBlsj(new Date());
            spgl.setBlyj("");
            spgl.setBlcs(project.getOuname());
        }
        else {
            spgl.setDfsjzj(auditProjectOperation.getRowguid());
            spgl.setBlr(auditProjectOperation.getOperateusername());
            spgl.setBlsj(new Date());
            spgl.setBlyj(auditProjectOperation.getRemarks());
            spgl.setBlcs(iouservice.getOuNameByUserGuid(auditProjectOperation.getOperateUserGuid()));
        }

        spgl.setXzqhdm("370800");
        spgl.setGcdm(baseinfo.getItemcode());
        spgl.setSpsxslbm(project.getFlowsn());
        if (ZwfwConstant.OPERATE_JJ.equals(operatetype)) {
            log.info("开始推送接件数据");
            spgl.setBlzt(1);
        }
        else if (ZwfwConstant.OPERATE_SL.equals(operatetype)) {
            spgl.setBlzt(3);
            // 受理时推送所有办件材料
            List<AuditProjectMaterial> materil = iauditprojectmaterial.selectProjectMaterial(project.getRowguid())
                    .getResult();
            if (materil == null) {
                return;
            }
            for (AuditProjectMaterial auditProjectMaterial : materil) {
                List<FrameAttachInfo> attachlist = iattachservice
                        .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                for (FrameAttachInfo frameAttachInfo : attachlist) {
                    SpglXmqtfjxxb fxxb = new SpglXmqtfjxxb();
                    fxxb.setRowguid(UUID.randomUUID().toString());
                    fxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                    fxxb.setXzqhdm("370800");
                    fxxb.setGcdm(baseinfo.getItemcode());
                    fxxb.setSpsxslbm(project.getFlowsn());
                    fxxb.setBlspslbm(project.getSubappguid());
                    fxxb.setFjmc(frameAttachInfo.getAttachFileName());
                    fxxb.setFjfl(ZwfwConstant.CONSTANT_INT_ONE);// 默认为1，申报材料

                    fxxb.setFjlx(frameAttachInfo.getContentType());
                    fxxb.setFjid(frameAttachInfo.getAttachGuid());
                    fxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    fxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    // 数据验证,项目代码是否在工程代码
                    if (!iSpglXmjbxxbService.isExistGcdm(baseinfo.getItemcode())) {
                        fxxb.set("sjsczt", "-1");
                        fxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxb.isExistFlowsn(project.getFlowsn())) {
                        fxxb.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(fxxb.getSbyy())) {
                            fxxb.setSbyy(fxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        else {
                            fxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }
                    ispglxmqtfjxxb.insert(fxxb);
                }
            }
        }
        else if (ZwfwConstant.OPERATE_SPTG.equals(operatetype)) {
            log.info("=======部门开始办理=======");
            spgl.setBlzt(8);
        }
        else if (ZwfwConstant.OPERATE_BYSL.equals(operatetype)) {
            unusual = projectUnusual
                    .getProjectUnusualByProjectGuidAndType(project.getRowguid(), ZwfwConstant.SHENPIOPERATE_TYPE_BYSL)
                    .getResult();
            spgl.setBlyj(unusual.getNote());
            spgl.setBlzt(5);
        }
        else if (ZwfwConstant.OPERATE_BJ.equals(operatetype)) {
            AuditProjectOperation operation = operationservice.getAuditOperation(projectID, ZwfwConstant.OPERATE_SPBTG)
                    .getResult();
            // 没有审批不通过，则为审批通过
            if (operation == null) {
                spgl.setBlzt(11);
            }
            else {
                spgl.setBlzt(13);
            }
        }
        else if (ZwfwConstant.OPERATE_CXSQ.equals(operatetype)) {
            spgl.setBlzt(2);
        }
        else if (ZwfwConstant.OPERATE_YCZZ.equals(operatetype)) {
            spgl.setBlzt(13);
        }
        else if (ZwfwConstant.OPERATE_BZ.equals(operatetype)) {
            // 设置默认办理意见
            spgl.setBlyj("材料补交");
            spgl.setBlzt(6);
        }
        else if (ZwfwConstant.OPERATE_ZTJS.equals(operatetype)) {
            // 暂停计时，对应特殊程序操作，插入特殊程序表
            SpglXmspsxbltbcxxxb spglxmspsxbltbcxxxb = new SpglXmspsxbltbcxxxb();
            spglxmspsxbltbcxxxb.setRowguid(UUID.randomUUID().toString());
            spglxmspsxbltbcxxxb.setDfsjzj(project.getRowguid());
            spglxmspsxbltbcxxxb.setXzqhdm("370800");
            spglxmspsxbltbcxxxb.setGcdm(baseinfo.getItemcode());
            spglxmspsxbltbcxxxb.setSpsxslbm(project.getFlowsn());
            spglxmspsxbltbcxxxb.setTbcx(1);
            spglxmspsxbltbcxxxb.setTbcxmc(unusual.getNote());
            spglxmspsxbltbcxxxb.setTbcxkssj(new Date());
            spglxmspsxbltbcxxxb.setTbcxsxlx(ZwfwConstant.CONSTANT_INT_ONE);// 默认使用1，即工作日类型
            spglxmspsxbltbcxxxb.setTbcxsx(ZwfwConstant.CONSTANT_INT_ZERO); // 特别程序时限
            spglxmspsxbltbcxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
            spglxmspsxbltbcxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
            if (!iSpglXmjbxxbService.isExistGcdm(spglxmspsxbltbcxxxb.getGcdm())) {
                spglxmspsxbltbcxxxb.set("sjsczt", "-1");
                spglxmspsxbltbcxxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
            }
            if (!ispglxmspsxblxxb.isExistFlowsn(spglxmspsxbltbcxxxb.getSpsxslbm())) {
                spglxmspsxbltbcxxxb.set("sjsczt", "-1");
                if (StringUtil.isNotBlank(spglxmspsxbltbcxxxb.getSbyy())) {
                    spglxmspsxbltbcxxxb
                            .setSbyy(spglxmspsxbltbcxxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                }
                else {
                    spglxmspsxbltbcxxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                }
            }
            ispglxmspsxbltbcxxxb.insert(spglxmspsxbltbcxxxb);
            spgl.setBlsj(new Date());
            spgl.setBlzt(9);
        }
        else if (ZwfwConstant.OPERATE_HFJS.equals(operatetype)) {
            // 设置默认办理意见
            if (unusual != null) {
                spgl.setBlyj(unusual.getNote());
            }
            spgl.setBlzt(10);
        }
        spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        if (!iSpglXmjbxxbService.isExistGcdm(spgl.getGcdm())) {
            spgl.set("sjsczt", "-1");
            spgl.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
        }
        if (!ispglxmspsxblxxb.isExistFlowsn(spgl.getSpsxslbm())) {
            spgl.set("sjsczt", "-1");
            if (StringUtil.isNotBlank(spgl.getSbyy())) {
                spgl.setSbyy(spgl.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
            }
            else {
                spgl.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
            }
        }
        // 判断是否秒板
        if (spgl.getBlzt() == 11 || spgl.getBlzt() == 13) {
            // 承诺件
            if (ZwfwConstant.CONSTANT_INT_TWO == project.getTasktype()) {

                Date allstart = project.getReceivedate();
                Date allend = new Date();
                if (project.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ && spgl.getBlsj() != null) {
                    allend = project.getBanjiedate();
                }
                long zttime = 0;
                Date start = null;
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("spsxslbm", spgl.getSpsxslbm());
                sqlc.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                sqlc.setOrderAsc("blsj");
                List<SpglXmspsxblxxxxb> listblxxxxb = ispglxmspsxblxxxxb.getListByCondition(sqlc.getMap()).getResult();
                // 过滤草稿事项
                listblxxxxb = listblxxxxb.stream()
                        .filter(a -> !ZwfwConstant.CONSTANT_STR_NEGATIVE_ONE.equals(a.getStr("sync")))
                        .collect(Collectors.toList());
                for (SpglXmspsxblxxxxb spglXmspsxblxxxxb : listblxxxxb) {
                    if (9 == spglXmspsxblxxxxb.getBlzt()) {
                        start = spglXmspsxblxxxxb.getBlsj();
                    }
                    if (10 == spglXmspsxblxxxxb.getBlzt()) {
                        // 计算非工作日耗时
                        long diff = spglXmspsxblxxxxb.getBlsj().getTime() - start.getTime();
                        // 计算工作日天数
                        int daysnum = ZwfwUtil.weekDays(start, spglXmspsxblxxxxb.getBlsj());
                        zttime += (diff - (daysnum * 1000 * 60 * 60 * 24));
                        start = null;
                    }
                }
                // 暂停计时中，还没有回复计时
                if (start != null) {
                    // 插入结束计时，审核通过的暂停计时
                    SpglXmspsxblxxxxb hfjs = new SpglXmspsxblxxxxb();
                    hfjs.setRowguid(UUID.randomUUID().toString());
                    hfjs.setDfsjzj(projectID);
                    hfjs.setXzqhdm("370800");
                    hfjs.setGcdm(baseinfo.getItemcode());
                    hfjs.setSpsxslbm(project.getFlowsn());
                    hfjs.setBlcs(spgl.getBlcs());
                    hfjs.setBlr(spgl.getBlr());
                    hfjs.setBlsj(spgl.getBlsj());
                    hfjs.setBlyj("特别程序（结束）");
                    hfjs.setBlzt(10);
                    hfjs.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    hfjs.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if (!iSpglXmjbxxbService.isExistGcdm(hfjs.getGcdm())) {
                        hfjs.set("sjsczt", "-1");
                        hfjs.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxb.isExistFlowsn(hfjs.getSpsxslbm())) {
                        hfjs.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(hfjs.getSbyy())) {
                            hfjs.setSbyy(hfjs.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        else {
                            hfjs.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }
                    ispglxmspsxblxxxxb.insert(hfjs);

                    // 计算非工作日耗时
                    long diff = spgl.getBlsj().getTime() - start.getTime();
                    // 计算工作日天数
                    int daysnum = ZwfwUtil.weekDays(start, spgl.getBlsj());
                    zttime += (diff - (daysnum * 1000 * 60 * 60 * 24));
                    start = null;
                }

                // 判断办件信息表
                SpglXmspsxblxxb spglxmspsxblxxb = ispglxmspsxblxxb.getSpglXmspsxblxxbBySlbm(project.getFlowsn());
                if (spglxmspsxblxxb != null) {
                    int spsxblsx = spglxmspsxblxxb.getSxblsxm();
                    SpglDfxmsplcjdsxxxb spgldfxmsplcjdsxxxb = ispgldfxmsplcjdsxxxb.getSpglDfxmsplcjdsxxxb(
                            spglxmspsxblxxb.getSplcbbh(), spglxmspsxblxxb.getSplcbm(), spglxmspsxblxxb.getSpsxbbh(),
                            spglxmspsxblxxb.getSpsxbm());
                    if (spgldfxmsplcjdsxxxb != null && spgldfxmsplcjdsxxxb.getSpsxblsx() < spsxblsx) {
                        spsxblsx = spgldfxmsplcjdsxxxb.getSpsxblsx();
                    }
                }
            }
        }

        if (8 == spgl.getBlzt() && isgyxtproject) {
            spgl.setOperatedate(new Date());
            ispglxmspsxblxxxxb.insert(spgl);
        }
        else if (3 == spgl.getBlzt()) {
            spgl.setOperatedate(new Date());
            ispglxmspsxblxxxxb.insert(spgl);
            if (!isgyxtproject) {
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setBlzt(8);
                ispglxmspsxblxxxxb.insert(spgl);
            }
        }
        else {
            ispglxmspsxblxxxxb.insert(spgl);
        }
    }

}

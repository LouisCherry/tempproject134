package com.epoint.xmz.thirdreporteddata.sqgl.rabbitmqhandlev3;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
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
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.CertInfoExternalImpl;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.sqgl.common.util.Zjconstant;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.cxbus.impl.CxBusServiceImpl;
import com.epoint.xmz.thirdreporteddata.auditspitaskcorp.api.IAuditSpITaskCorpService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJzgcsgxkxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmdtxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxbltbcxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZrztxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglsqcljqtfjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfsjscxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfysxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJzgcsgxkxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSgtsjwjscxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmdtxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxbltbcxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZrztxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsqcljqtfjxxbV3;
import com.epoint.xmz.thirdreporteddata.common.GxhSpConstant;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.ISpglSpfxsbaxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.entity.SpglSpfxsbaxxb;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.ISpglSpfysxkxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity.SpglSpfysxkxxb;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;

/**
 * 消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class SpglxmspsxblxxxxbV3ClientHandle extends AuditClientMessageListener
{

    private static Logger log = LogUtil.getLog(SpglxmspsxblxxxxbV3ClientHandle.class);

    // 设置消息类型，判断消息是监听住建系统数据
    public SpglxmspsxblxxxxbV3ClientHandle() {
        super.setMassagetype(MQConstant.MESSAGETYPE_ZJ);
    }

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
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
        ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxxxbV3.class);
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
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
        IAttachService iattachservice = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ISpglsqcljqtfjxxbV3 iSpglsqcljqtfjxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglsqcljqtfjxxbV3.class);
        ISpglXmspsxpfwjxxbV3 ispglxmspsxpfwjxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxpfwjxxbV3.class);
        ISpglXmjbxxbV3 iSpglXmjbxxbV3 = ContainerFactory.getContainInfo().getComponent(ISpglXmjbxxbV3.class);
        IAuditProjectUnusual projectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IUserService iuserservice = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditTaskResult iAuditTaskResult = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        ISpglXmspsxbltbcxxxbV3 iSpglXmspsxbltbcxxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxbltbcxxxbV3.class);
        CertInfoExternalImpl certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(CertInfoExternalImpl.class);
        ICertAttachExternal iCertAttachExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertAttachExternal.class);
        IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        IAuditProjectOperation ycggoperationservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectOperation.class);
        IAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        ISpglXmspsxblxxbV3 ispglxmspsxblxxbV3 = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmspsxblxxbV3.class);
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        ISpglZrztxxbV3Service iSpglZrztxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglZrztxxbV3Service.class);
        IAuditSpITaskCorpService iAuditSpITaskCorpService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpITaskCorpService.class);
        IDantiInfoV3Service iDantiInfoV3Service = ContainerFactory.getContainInfo()
                .getComponent(IDantiInfoV3Service.class);
        ISpglXmdtxxbV3Service iSpglXmdtxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglXmdtxxbV3Service.class);
        IParticipantsInfoService iparticipantsinfoservice = ContainerFactory.getContainInfo()
                .getComponent(IParticipantsInfoService.class);
        ITaskCommonService iTaskCommonService = ContainerFactory.getContainInfo()
                .getComponent(ITaskCommonService.class);
        IDantiSubRelationService iDantiSubRelationService = ContainerFactory.getContainInfo()
                .getComponent(IDantiSubRelationService.class);
        IAuditTask iauditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        ISpglSpfysxkxxbService spglSpfysxkxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglSpfysxkxxbService.class);

        ISpglSpfxsbaxxbService spglSpfxsbaxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglSpfxsbaxxbService.class);
        ISpglSgtsjwjscxxbV3Service sgtsjwjscxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglSgtsjwjscxxbV3Service.class);
        ISpglJzgcsgxkxxbV3Service jzgcsgxkxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglJzgcsgxkxxbV3Service.class);
        ISpglJsgcjgysbaxxbV3Service jsgcjgysbaxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglJsgcjgysbaxxbV3Service.class);
        ISpglJsgcxfysxxbV3Service iSpglJsgcxfysxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglJsgcxfysxxbV3Service.class);
        ISpglJsgcxfsjscxxbV3Service iSpglJsgcxfsjscxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglJsgcxfsjscxxbV3Service.class);
        ISpglJsgcxfysbaxxbV3Service iSpglJsgcxfysbaxxbV3Service = ContainerFactory.getContainInfo()
                .getComponent(ISpglJsgcxfysbaxxbV3Service.class);

        ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);

        CxBusServiceImpl cxBusServiceImpl = new CxBusServiceImpl();

        // 解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");

        if (messageContent == null || messageContent.length < 1) {
            log.info("mq消息信息不正确！");
            return;
        }
        // 办件主键
        String projectID = messageContent[0];

        if (proMessage.getSendroutingkey().split("\\.") == null
                || proMessage.getSendroutingkey().split("\\.").length < 2) {
            log.info("mq消息信息不正确！");
            return;
        }

        // 辖区编码
        String areacode = proMessage.getSendroutingkey().split("\\.")[1];
        if (areacode.length() > 6) {
            return;
        }
        // 获取操作
        String operate = proMessage.getSendroutingkey().split("\\.")[2];

        String operatetype = "";

        AuditProject project = iauditproject.getAuditProjectByRowGuid(projectID, areacode).getResult();
        if (StringUtil.isBlank(project.getBiguid())) {
            return;
        }
        AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(project.getBiguid()).getResult();
        if (auditspinstance == null) {
            return;
        }
        AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(project.getSubappguid()).getResult();
        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                .getResult();
        if (baseinfo == null) {
            return;
        } // 国泰测试项目不上报
        if (baseinfo == null || baseinfo.getItemname().contains("国泰测试")) {
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
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                .getTaskExtensionByTaskGuid(project.getTaskguid(), false).getResult();
        FrameOuExtendInfo raExtendInfo = iOuService.getFrameOuExtendInfo(project.getOuguid());

        String xmdm = "";
        String jsdw = "";
        String jsdwdm = "";
        if (StringUtil.isNotBlank(baseinfo.getParentid())) {
            AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(baseinfo.getParentid()).getResult();
            xmdm = pauditRsItemBaseinfo.getItemcode();
            jsdw = pauditRsItemBaseinfo.getItemlegaldept();
            jsdwdm = pauditRsItemBaseinfo.getItemlegalcertnum();
        }
        else {
            xmdm = baseinfo.getItemcode();
            jsdw = baseinfo.getItemlegaldept();
            jsdwdm = baseinfo.getItemlegalcertnum();
        }
        try {
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
                        // 判断此事项需不需要传责任主体信息 是则插入责任主体信息
                        List<AuditSpBasetask> result = iTaskCommonService
                                .getAuditSpTask(sub.getBusinessguid(), project.getTask_id()).getResult();
                        if (EpointCollectionUtils.isNotEmpty(result)) {
                            if (GxhSpConstant.SX_LIST.contains(result.get(0).getStr("taskcodeV3"))) {
                                String xkbabh = project.getWenhao();
                                if ("0090001".equals(result.get(0).getStr("taskcodeV3"))
                                        || "0090002".equals(result.get(0).getStr("taskcodeV3"))
                                        || "0090003".equals(result.get(0).getStr("taskcodeV3"))
                                        || "0090004".equals(result.get(0).getStr("taskcodeV3"))) {
                                    SpglSgtsjwjscxxbV3 sgtsjwjscxxbV3 = sgtsjwjscxxbV3Service
                                            .findDominByCondition("370800", baseinfo.getItemcode());
                                    if (sgtsjwjscxxbV3 != null) {
                                        // 施工图设计文件审查信息表 的 施工图审查业务编号 字段
                                        xkbabh = sgtsjwjscxxbV3.getStywbh();
                                    }
                                }
                                else if ("0120001".equals(result.get(0).getStr("taskcodeV3"))
                                        || "0120002".equals(result.get(0).getStr("taskcodeV3"))
                                        || "0120003".equals(result.get(0).getStr("taskcodeV3"))) {
                                    SpglJzgcsgxkxxbV3 jzgcsgxkxxbV3 = jzgcsgxkxxbV3Service.findDominByCondition(
                                            "370800", baseinfo.getItemcode(), project.getFlowsn());
                                    if (jzgcsgxkxxbV3 != null) {
                                        // 建筑工程施工许可信息表 的 施工许可证编号 字段
                                        xkbabh = jzgcsgxkxxbV3.getSgxkzbh();
                                    }
                                }
                                else if ("0180000".equals(result.get(0).getStr("taskcodeV3"))) {
                                    SpglJsgcjgysbaxxbV3 jsgcjgysbaxxbV3 = jsgcjgysbaxxbV3Service.findDominByCondition(
                                            "370800", baseinfo.getItemcode(), project.getFlowsn());
                                    if (jsgcjgysbaxxbV3 != null) {
                                        // 建设工程消防验收备案信息表 的 竣工验收备案编号 字段
                                        xkbabh = jsgcjgysbaxxbV3.getJgysbabh();
                                    }
                                }
                                // else if
                                // ("0100000".equals(result.get(0).getStr("taskcodeV3")))
                                // {
                                // SpglJsgcxfsjscxxbV3 spglJsgcxfsjscxxbV3 =
                                // iSpglJsgcxfsjscxxbV3Service.findDominByCondition("370800",
                                // baseinfo.getItemcode(), project.getFlowsn());
                                // if (spglJsgcxfsjscxxbV3 != null) {
                                // //建设工程消防设计审查信息表实体 的 消防设计审查编号 字段
                                // xkbabh =
                                // spglJsgcxfsjscxxbV3.getStr("xfsjscbh");
                                // }
                                // }
                                // else if
                                // ("0140000".equals(result.get(0).getStr("taskcodeV3")))
                                // {
                                // SpglJsgcxfysxxbV3 spglJsgcxfysxxbV3 =
                                // iSpglJsgcxfysxxbV3Service.findDominByCondition("370800",
                                // baseinfo.getItemcode(), project.getFlowsn());
                                // if (spglJsgcxfysxxbV3 != null) {
                                // //建设工程消防验收信息表实体 的 消防验收合格书编号 字段
                                // xkbabh =
                                // spglJsgcxfysxxbV3.getStr("xfyshgsbh");
                                // }
                                // }
                                // else if
                                // ("0150000".equals(result.get(0).getStr("taskcodeV3")))
                                // {
                                // SpglJsgcxfysbaxxbV3 spglJsgcxfysbaxxbV3 =
                                // iSpglJsgcxfysbaxxbV3Service.findDominByCondition("370800",
                                // baseinfo.getItemcode(), project.getFlowsn());
                                // if (spglJsgcxfysbaxxbV3 != null) {
                                // //建设工程消防验收备案信息表实体 的 消防验收备案编号 字段
                                // xkbabh =
                                // spglJsgcxfysbaxxbV3.getStr("xfysbabh");
                                // }
                                // }

                                // 直接推送五方责任主体
                                List<ParticipantsInfo> participantsinfolist = iSpglZrztxxbV3Service
                                        .getParticipantsInfoListBySubappguid(project.getSubappguid());
                                if (!participantsinfolist.isEmpty()) {
                                    for (ParticipantsInfo participantsInfo : participantsinfolist) {
                                        Integer dwlx = getDwlxByCorptype(participantsInfo.getCorptype());
                                        SpglZrztxxbV3 spglZrztxxbV3 = new SpglZrztxxbV3();
                                        spglZrztxxbV3.setDfsjzj(participantsInfo.getRowguid());
                                        spglZrztxxbV3.setRowguid(UUID.randomUUID().toString());
                                        spglZrztxxbV3.setXzqhdm("370800");
                                        spglZrztxxbV3.setXmdm(xmdm);
                                        spglZrztxxbV3.setGcdm(baseinfo.getItemcode());
                                        spglZrztxxbV3.setSpsxslbm(project.getFlowsn());
                                        spglZrztxxbV3.setXkbabh(xkbabh);
                                        spglZrztxxbV3.setDwtyshxydm(participantsInfo.getCorpcode());
                                        spglZrztxxbV3.setDwmc(participantsInfo.getCorpname());
                                        checkBack("单位名称", participantsInfo.getCorpname());
                                        checkBack("单位统一社会信用代码", participantsInfo.getCorpcode());
                                        spglZrztxxbV3.setDwlx(dwlx);
                                        spglZrztxxbV3.setZzdj(participantsInfo.getStr("CERT"));
                                        spglZrztxxbV3.setFddbr(participantsInfo.getStr("legal"));
                                        if (StringUtil.isNotBlank(participantsInfo.get("legalcardtype"))) {
                                            spglZrztxxbV3
                                                    .setFrzjlx(Integer.parseInt(participantsInfo.get("legalcardtype")));
                                        }
                                        spglZrztxxbV3.setFrzjhm(participantsInfo.getStr("legalpersonicardnum"));
                                        spglZrztxxbV3.setFzrxm(participantsInfo.getStr("XMFZR"));
                                        if (StringUtil.isNotBlank(participantsInfo.get("fzrzjlx"))) {
                                            spglZrztxxbV3.setFzrzjlx(Integer.parseInt(participantsInfo.get("fzrzjlx")));
                                        }
                                        spglZrztxxbV3.setFzrzjhm(participantsInfo.getStr("xmfzr_idcard"));
                                        spglZrztxxbV3.setFzrlxdh(participantsInfo.getStr("xmfzr_phone"));
                                        spglZrztxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                        spglZrztxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                        spglZrztxxbV3.setSync("0");

                                        // 数据验证,项目代码是否在工程代码
                                        if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                            spglZrztxxbV3.set("sjsczt", "-1");
                                            spglZrztxxbV3.setSbyy("单位工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                        }
                                        iSpglZrztxxbV3Service.insert(spglZrztxxbV3);
                                    }
                                }

                                // 生成单体信息
                                List<DantiSubRelation> findListBySubappGuid = iDantiSubRelationService
                                        .findListBySubappGuid(project.getSubappguid());
                                for (DantiSubRelation dantiSubRelation : findListBySubappGuid) {
                                    DantiInfoV3 dantiInfoV3 = iDantiInfoV3Service.find(dantiSubRelation.getDantiguid());
                                    if (dantiInfoV3 != null) {
                                        SpglXmdtxxbV3 spglXmdtxxbV3 = new SpglXmdtxxbV3();
                                        spglXmdtxxbV3.setRowguid(UUID.randomUUID().toString());
                                        spglXmdtxxbV3.setDfsjzj(dantiInfoV3.getRowguid());
                                        spglXmdtxxbV3.setXzqhdm("370800");
                                        spglXmdtxxbV3.setXmdm(xmdm);
                                        spglXmdtxxbV3.setGcdm(baseinfo.getItemcode());
                                        spglXmdtxxbV3.setSpsxslbm(project.getFlowsn());
                                        spglXmdtxxbV3.setXkbabh(xkbabh);
                                        spglXmdtxxbV3.setDtmc(dantiInfoV3.getDtmc());
                                        spglXmdtxxbV3.setDtbm(dantiInfoV3.getDtbm());
                                        spglXmdtxxbV3.setGcyt(dantiInfoV3.getGcyt());
                                        spglXmdtxxbV3.setGmzb(dantiInfoV3.getGmzb());
                                        spglXmdtxxbV3.setJgtx(dantiInfoV3.getJgtx());
                                        spglXmdtxxbV3.setNhdj(dantiInfoV3.getNhdj());
                                        spglXmdtxxbV3.setJzfs(dantiInfoV3.getJzfs());
                                        spglXmdtxxbV3.setDtjwdzb(dantiInfoV3.getDtjwdzb());
                                        spglXmdtxxbV3.setDtgczzj(dantiInfoV3.getDtgczzj());
                                        spglXmdtxxbV3.setJzmj(dantiInfoV3.getJzmj());
                                        spglXmdtxxbV3.setDsjzmj(dantiInfoV3.getDsjzmj());
                                        spglXmdtxxbV3.setDxjzmj(dantiInfoV3.getDxjzmj());
                                        spglXmdtxxbV3.setZdmj(dantiInfoV3.getZdmj());
                                        spglXmdtxxbV3.setJzgcgd(dantiInfoV3.getJzgcgd());
                                        spglXmdtxxbV3.setDscs(dantiInfoV3.getDscs());
                                        spglXmdtxxbV3.setDxcs(dantiInfoV3.getDxcs());
                                        spglXmdtxxbV3.setCd(dantiInfoV3.getCd());
                                        spglXmdtxxbV3.setKd(dantiInfoV3.getKd());
                                        spglXmdtxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                        spglXmdtxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                        spglXmdtxxbV3.setSync("0");
                                        iSpglXmdtxxbV3Service.insert(spglXmdtxxbV3);
                                    }

                                }

                            }
                        }

                        // 办结的时候判断是否 是商品房事项，如果是直接推送前置库
                        AuditTask auditTask = iauditTask.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
                        if (auditTask != null) {
                            if ("商品房预售许可（县级权限）核发".equals(auditTask.getTaskname())
                                    || "商品房预售许可（设区的市级权限）核发".equals(auditTask.getTaskname())
                                    || "商品房预售许可（县级权限）变更".equals(auditTask.getTaskname())
                                    || "商品房预售许可（设区的市级权限）变更".equals(auditTask.getTaskname())) {
                                // 那应该取，iCxBusService.getDzbdDetail("formtable20240611181440",
                                // auditProject.getSubappguid());
                                Record formDetail = null;
                                if (StringUtil.isNotBlank(project.getBiguid())
                                        && StringUtil.isNotBlank(auditspbusiness.getStr("yjsformid"))) {

                                    formDetail = iCxBusService.getDzbdDetail("formtable20230206094334",
                                            project.getSubappguid());
                                    if (formDetail == null) {
                                        formDetail = iCxBusService.getDzbdDetail("formtable20230206094334",
                                                project.getRowguid());
                                    }

                                }
                                else {
                                    formDetail = iCxBusService.getDzbdDetail("formtable20230206094334",
                                            project.getRowguid());
                                    if (formDetail == null) {
                                        formDetail = iCxBusService.getDzbdDetail("formtable20230206094334",
                                                project.getSubappguid());
                                    }
                                }
                                // 查询证照信息
                                String certinfoguid = project.getCertrowguid();
                                Map<String, Object> filter = new HashMap<>();
                                // 设置基本信息guid
                                filter.put("certinfoguid", certinfoguid);
                                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class,
                                        filter, false);
                                SpglSpfysxkxxb spglSpfysxkxxb = new SpglSpfysxkxxb();
                                spglSpfysxkxxb.setRowguid(UUID.randomUUID().toString());
                                spglSpfysxkxxb.setDfsjzj(UUID.randomUUID().toString());
                                spglSpfysxkxxb.setXzqhdm("370800");

                                // 县级区划名称
                                spglSpfysxkxxb.set("XJQHMC", area.getXiaquname());
                                // 县级区划代码
                                spglSpfysxkxxb.set("XJQHDM", auditTask.getAreacode());

                                spglSpfysxkxxb.setGcdm(baseinfo.getItemcode());

                                if (formDetail != null) {
                                    // 工程进度节点名称
                                    spglSpfysxkxxb.set("GCJDJDCM", formDetail.get("gcjdjdmc"));
                                    // 工程进度节点编码
                                    spglSpfysxkxxb.set("GCJDJDBM", formDetail.get("gcjdjdbm"));
                                    // 所属集团
                                    spglSpfysxkxxb.set("SSJT", formDetail.get("ssjt"));
                                    // 预销售房屋项日地址
                                    spglSpfysxkxxb.setXmzl(formDetail.get("yxsfwxrdz"));
                                    // 商品房预售资金监管银行
                                    spglSpfysxkxxb.setZjjgyh(formDetail.get("yxsfwjgyx"));
                                    // 商品房预售资金监管账号
                                    spglSpfysxkxxb.setZjjgzh(formDetail.get("yxsfwjgzh"));
                                    // 完成交付所需后续资金
                                    spglSpfysxkxxb.set("WCJFSXHXZJ", formDetail.get("wcjfsxhxzj"));
                                    // 批准预售面积
                                    spglSpfysxkxxb.setYszmj(formDetail.getDouble("pzysmj"));
                                    // 预售总栋数
                                    spglSpfysxkxxb.setYszds(formDetail.getInt("yszds"));
                                    // 预售总套数
                                    spglSpfysxkxxb.setYszts(formDetail.getInt("yszts"));
                                    /**
                                     * 预售住宅套数
                                     * 预售住宅面积
                                     * 预售商业面积
                                     * 预售其他面积
                                     * 
                                     */
                                    spglSpfysxkxxb.set("YSZZTS", formDetail.get("yszzts"));
                                    spglSpfysxkxxb.set("YSZZMJ", formDetail.get("yszzmj"));
                                    spglSpfysxkxxb.set("YSSYMJ", formDetail.get("yssymj"));
                                    spglSpfysxkxxb.set("YSQTMJ", formDetail.get("ysqtmj"));
                                    // 其中
                                    spglSpfysxkxxb.setQz(formDetail.getStr("qiz"));
                                    // 不动产单元代码
                                    spglSpfysxkxxb.setBdcdydm(formDetail.getStr("bdcdydm"));
                                }

                                spglSpfysxkxxb.setSpsxslbm(project.getFlowsn());

                                spglSpfysxkxxb.setKfdwmc(baseinfo.getItemlegaldept());
                                spglSpfysxkxxb.setKfdwdm(baseinfo.getItemlegalcreditcode());

                                spglSpfysxkxxb.setXmmc(baseinfo.getItemname());

                                if (certinfoExtension != null) {
                                    spglSpfysxkxxb.setSpfysxkzbh(certinfoExtension.getStr("bh"));

                                    spglSpfysxkxxb.setBzlh(certinfoExtension.getStr("pzlh"));

                                    if (StringUtil.isNotBlank(certinfoExtension.getStr("fzrq"))) {
                                        spglSpfysxkxxb.setFzrq(EpointDateUtil
                                                .convertString2Date(certinfoExtension.getStr("fzrq"), "yyyy-MM-dd"));
                                    }
                                    // if
                                    // (StringUtil.isNotBlank(certinfoExtension.getStr("YXQQSSJ")))
                                    // {
                                    // spglSpfysxkxxb.setYxqqssj(EpointDateUtil
                                    // .convertString2Date(certinfoExtension.getStr("YXQQSSJ"),
                                    // "yyyy-MM-dd"));
                                    // }
                                    if (StringUtil.isNotBlank(certinfoExtension.getStr("fzrq"))) {
                                        spglSpfysxkxxb.setYxqqssj(EpointDateUtil
                                                .convertString2Date(certinfoExtension.getStr("fzrq"), "yyyy-MM-dd"));
                                    }
                                    if (StringUtil.isNotBlank(certinfoExtension.getStr("yxzzrq"))) {
                                        spglSpfysxkxxb.setYxqjzsj(EpointDateUtil
                                                .convertString2Date(certinfoExtension.getStr("yxzzrq"), "yyyy-MM-dd"));
                                    }

                                }
                                else {
                                    spglSpfysxkxxb.setFzrq(new Date());
                                    spglSpfysxkxxb.setYxqqssj(EpointDateUtil.convertString2Date("9999-12-31"));
                                }
                                spglSpfysxkxxb.setFzjg(auditTask.getOuname());
                                if (raExtendInfo != null) {
                                    spglSpfysxkxxb.setFzjgtyshxydm(raExtendInfo.getStr("ORGCODE"));
                                }

                                spglSpfysxkxxb.setSjsczt(0);
                                spglSpfysxkxxb.setSjyxbs(1);
                                spglSpfysxkxxbService.insert(spglSpfysxkxxb);
                            }

                            if ("商品房项目现售备案".equals(auditTask.getTaskname())) {
                                // 那应该取，iCxBusService.getDzbdDetail("formtable20240611181440",
                                // auditProject.getSubappguid());
                                Record formDetail = null;
                                if (StringUtil.isNotBlank(project.getBiguid())
                                        && StringUtil.isNotBlank(auditspbusiness.getStr("yjsformid"))) {

                                    formDetail = iCxBusService.getDzbdDetail("formtable20240717141634",
                                            project.getSubappguid());
                                    if (formDetail == null) {
                                        formDetail = iCxBusService.getDzbdDetail("formtable20240717141634",
                                                project.getRowguid());
                                    }

                                }
                                else {
                                    formDetail = iCxBusService.getDzbdDetail("formtable20240717141634",
                                            project.getRowguid());
                                    if (formDetail == null) {
                                        formDetail = iCxBusService.getDzbdDetail("formtable20240717141634",
                                                project.getSubappguid());
                                    }
                                }
                                // 查询证照信息
                                String certinfoguid = project.getCertrowguid();
                                Map<String, Object> filter = new HashMap<>();
                                // 设置基本信息guid
                                filter.put("certinfoguid", certinfoguid);
                                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class,
                                        filter, false);
                                SpglSpfxsbaxxb spglSpfxsbaxxb = new SpglSpfxsbaxxb();
                                spglSpfxsbaxxb.setRowguid(UUID.randomUUID().toString());
                                spglSpfxsbaxxb.setDfsjzj(UUID.randomUUID().toString());
                                spglSpfxsbaxxb.setXzqhdm("370800");

                                // 工程代码
                                spglSpfxsbaxxb.setGcdm(baseinfo.getItemcode());
                                // 审批事项实例编码
                                spglSpfxsbaxxb.setSpsxslbm(project.getFlowsn());
                                // 发证机关
                                spglSpfxsbaxxb.setFzjg(auditTask.getOuname());
                                // 发证机关统一社会信用代码
                                if (raExtendInfo != null) {
                                    spglSpfxsbaxxb.setFzjgtyshxydm(raExtendInfo.getStr("ORGCODE"));
                                }
                                if (formDetail != null) {

                                    // 开发单位名称
                                    spglSpfxsbaxxb.setKfdwmc(formDetail.get("kfdwmc"));
                                    // 开发单位代码
                                    spglSpfxsbaxxb.setKfdwdm(formDetail.get("kfdwdm"));

                                    // 商品房现售备案证编号
                                    spglSpfxsbaxxb.set("SPFYSXKZBH", formDetail.get("spfxsbazbh"));
                                    // 项目名称
                                    spglSpfxsbaxxb.setXmmc(formDetail.get("xmmc"));
                                    // 项目坐落
                                    spglSpfxsbaxxb.setXmzl(formDetail.get("xmdz"));

                                    // 批准现售面积
                                    spglSpfxsbaxxb.setYszmj(formDetail.getDouble("pzxsmj"));

                                    // 其中
                                    spglSpfxsbaxxb.setQz(formDetail.getStr("qz"));
                                    // 不动产单元代码
                                    spglSpfxsbaxxb.setBdcdydm(formDetail.getStr("bdcdydm"));
                                }

                                if (certinfoExtension != null) {

                                    if (StringUtil.isNotBlank(certinfoExtension.getStr("fzrq"))) {
                                        spglSpfxsbaxxb.setFzrq(EpointDateUtil
                                                .convertString2Date(certinfoExtension.getStr("fzrq"), "yyyy-MM-dd"));
                                    }
                                    // if
                                    // (StringUtil.isNotBlank(certinfoExtension.getStr("YXQQSSJ")))
                                    // {
                                    // spglSpfxsbaxxb.setYxqqssj(EpointDateUtil
                                    // .convertString2Date(certinfoExtension.getStr("YXQQSSJ"),
                                    // "yyyy-MM-dd"));
                                    // }
                                    if (StringUtil.isNotBlank(certinfoExtension.getStr("fzrq"))) {
                                        spglSpfxsbaxxb.setYxqqssj(EpointDateUtil
                                                .convertString2Date(certinfoExtension.getStr("fzrq"), "yyyy-MM-dd"));
                                    }
                                    if (StringUtil.isNotBlank(certinfoExtension.getStr("yxzzrq"))) {
                                        spglSpfxsbaxxb.setYxqjzsj(EpointDateUtil
                                                .convertString2Date(certinfoExtension.getStr("yxzzrq"), "yyyy-MM-dd"));
                                    }

                                }
                                else {
                                    spglSpfxsbaxxb.setFzrq(new Date());
                                    spglSpfxsbaxxb.setYxqqssj(EpointDateUtil.convertString2Date("9999-12-31"));
                                }
                                spglSpfxsbaxxb.setSjsczt(0);
                                spglSpfxsbaxxb.setSjyxbs(1);
                                spglSpfxsbaxxbService.insert(spglSpfxsbaxxb);
                            }
                        }

                        // 查入批复文件信息
                        SpglXmspsxpfwjxxbV3 pfwjv3 = new SpglXmspsxpfwjxxbV3();
                        pfwjv3.setDfsjzj(project.getRowguid());
                        pfwjv3.setXzqhdm("370800");
                        pfwjv3.setGcdm(baseinfo.getItemcode());
                        pfwjv3.setSpsxslbm(project.getFlowsn());
                        pfwjv3.setPfrq(project.getCertificatedate());
                        pfwjv3.setPfwh(project.getWenhao());
                        pfwjv3.setPfwjbt("批文");
                        pfwjv3.setPfwjyxqx(EpointDateUtil.convertString2DateAuto("9999-01-01"));
                        // 3.0上报新增字段
                        CertInfo certinfo = certInfoExternalImpl.getCertInfoByRowguid(project.getCertrowguid());
                        String picurl = "";
                        if (certinfo != null) {
                            pfwjv3.setSpjglx("10");
                            pfwjv3.setZzbh(certinfo.getCertno());
                            pfwjv3.setZzbs(project.getCertrowguid());
                            List<JSONObject> certAttachList = iCertAttachExternal
                                    .getAttachList(certinfo.getCertcliengguid(), project.getAreacode());
                            if (EpointCollectionUtils.isNotEmpty(certAttachList)) {
                                JSONObject info = certAttachList.get(0);
                                picurl = iCertAttachExternal.getAttachScan(info.get("attachguid").toString(), areacode);

                            }
                            pfwjv3.setDzzzwjlj(picurl);

                            // 3.0上报新增字段结束
                            List<FrameAttachInfo> attachlist = iattachservice
                                    .getAttachInfoListByGuid(certinfo.getCertcliengguid());
                            if (attachlist == null) {
                                break;
                            }
                            // 多个批文文件附件处理，插入多次
                            for (FrameAttachInfo frameAttachInfo : attachlist) {
                                pfwjv3.setRowguid(UUID.randomUUID().toString());
                                pfwjv3.setDfsjzj(frameAttachInfo.getAttachGuid());
                                pfwjv3.setFjid(frameAttachInfo.getAttachGuid());
                                pfwjv3.setFjmc(frameAttachInfo.getAttachFileName());
                                pfwjv3.setFjlx(frameAttachInfo.getContentType());
                                pfwjv3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                pfwjv3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                // 数据验证,项目代码是否在工程代码
                                if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                    pfwjv3.set("sjsczt", "-1");
                                    pfwjv3.setSbyy("批文工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                }
                                if (!ispglxmspsxblxxbV3.isExistFlowsn(project.getFlowsn())) {
                                    pfwjv3.set("sjsczt", "-1");
                                    if (StringUtil.isNotBlank(pfwjv3.getSbyy())) {
                                        pfwjv3.setSbyy(pfwjv3.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                    }
                                    else {
                                        pfwjv3.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                    }
                                }
                                ispglxmspsxpfwjxxb.insert(pfwjv3);
                            }
                        }
                        else {
                            pfwjv3.setSpjglx("30");
                            // 推送结果文件
                            String projectattachguid = project.getRowguid();
                            if (StringUtil.isNotBlank(projectattachguid)) {
                                List<FrameAttachInfo> docattachs = iattachservice
                                        .getAttachInfoListByGuid(projectattachguid);
                                if (!docattachs.isEmpty()) {
                                    for (FrameAttachInfo attach : docattachs) {
                                        pfwjv3.setRowguid(UUID.randomUUID().toString());
                                        pfwjv3.setDfsjzj(attach.getAttachGuid());
                                        pfwjv3.setFjid(attach.getAttachGuid());
                                        pfwjv3.setFjmc(attach.getAttachFileName());
                                        pfwjv3.setFjlx(attach.getContentType());
                                        pfwjv3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                        pfwjv3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                        // 数据验证,项目代码是否在工程代码
                                        if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                            pfwjv3.set("sjsczt", "-1");
                                            pfwjv3.setSbyy("批文工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                                        }
                                        if (!ispglxmspsxblxxbV3.isExistFlowsn(project.getFlowsn())) {
                                            pfwjv3.set("sjsczt", "-1");
                                            if (StringUtil.isNotBlank(pfwjv3.getSbyy())) {
                                                pfwjv3.setSbyy(
                                                        pfwjv3.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                            }
                                            else {
                                                pfwjv3.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                                            }
                                        }
                                        ispglxmspsxpfwjxxb.insert(pfwjv3);
                                    }
                                }
                            }
                        }

                        break;
                    case "bz":
                        // 如果补正，插入特殊操作数据
                        operatetype = ZwfwConstant.OPERATE_BZ;
                        break;
                    case "bzwc":
                        String opereateuser = messageContent[2];
                        // 如果补正完成需要单独插入数据
                        SpglXmspsxblxxxxbV3 spgl = new SpglXmspsxblxxxxbV3();
                        spgl.setRowguid(UUID.randomUUID().toString());
                        spgl.setDfsjzj(projectID);
                        spgl.setXzqhdm("370800");
                        spgl.setGcdm(baseinfo.getItemcode());
                        spgl.setSpsxslbm(project.getFlowsn());
                        spgl.setBlcs(Zjconstant.getOunamebyuser(opereateuser));
                        spgl.setBlr(iuserservice.getUserNameByUserGuid(opereateuser));
                        spgl.setBlsj(proMessage.getOperatedate());
                        spgl.setBlyj("补正完成");
                        spgl.setBlzt(7);
                        spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        spgl.setOperatedate(new Date());
                        // 盐南对接，新增JSDDXZQH 字段
                        spgl.set("JSDDXZQH", project.getAreacode());
                        if (!iSpglXmjbxxbV3.isExistGcdm(spgl.getGcdm())) {
                            spgl.set("sjsczt", "-1");
                            spgl.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                        }
                        if (!ispglxmspsxblxxbV3.isExistFlowsn(spgl.getSpsxslbm())) {
                            spgl.set("sjsczt", "-1");
                            if (StringUtil.isNotBlank(spgl.getSbyy())) {
                                spgl.setSbyy(spgl.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                            else {
                                spgl.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                        }
                        spgl.setOperatedate(new Date());
                        // 3.0新增上报字段
                        if (StringUtil.isNotBlank(project.getOuname())) {
                            spgl.setDwmc(project.getOuname());
                        }
                        else {
                            spgl.setDwmc("错误数据");
                            spgl.set("sjsczt", "-1");
                            spgl.setSbyy("单位名称校验有误！");
                        }

                        if (raExtendInfo != null) {
                            spgl.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                        }
                        else {
                            spgl.set("sjsczt", "-1");
                            spgl.setSbyy("单位统一社会信用代码校验有误！");
                        }

                        if (auditTaskExtension != null) {
                            String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                    auditTaskExtension.getStr("handle_system"));
                            if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                                spgl.setSjly(SYSTEM_NAME);
                            }
                            else {
                                // 塞入自己系统默认值
                                spgl.setSjly("济宁市工程建设项目网上申报系统");
                            }
                        }
                        iSpglXmspsxblxxxxbV3.insert(spgl);
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
                        SpglXmspsxpfwjxxbV3 jxxb = new SpglXmspsxpfwjxxbV3();
                        jxxb.setDfsjzj(project.getRowguid());
                        jxxb.setXzqhdm("370800");
                        jxxb.setGcdm(baseinfo.getItemcode());
                        jxxb.setSpsxslbm(project.getFlowsn());
                        jxxb.setPfrq(project.getCertificatedate());
                        jxxb.setPfwh(project.getWenhao());
                        jxxb.setPfwjbt("批文");
                        jxxb.setPfwjyxqx(EpointDateUtil.convertString2DateAuto("9999-01-01"));
                        // 3.0上报新增字段
                        AuditTaskResult auditTaskResult = iAuditTaskResult
                                .getAuditResultByTaskGuid(project.getTaskguid(), false).getResult();
                        if (auditTaskResult != null) {
                            jxxb.setSpjglx(("40").equals(auditTaskResult.getResulttype().toString()) ? "30"
                                    : auditTaskResult.getResulttype().toString());
                        }
                        CertInfo certInfo = certInfoExternalImpl.getCertInfoByRowguid(project.getCenterguid());
                        String picUrl = "";
                        if (certInfo != null) {
                            jxxb.setZzbh(certInfo.getCertno());
                            jxxb.setZzbs(project.getCenterguid());
                            List<JSONObject> certAttachList = iCertAttachExternal
                                    .getAttachList(certInfo.getCertcliengguid(), project.getAreacode());
                            if (EpointCollectionUtils.isNotEmpty(certAttachList)) {
                                JSONObject info = certAttachList.get(0);
                                picUrl = iCertAttachExternal.getAttachScan(info.get("attachguid").toString(), areacode);

                            }
                            jxxb.setDzzzwjlj(picUrl);
                        }
                        // 3.0上报新增字段结束
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
                            if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                                jxxb.set("sjsczt", "-1");
                                jxxb.setSbyy("批文工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                            }
                            if (!ispglxmspsxblxxbV3.isExistFlowsn(project.getFlowsn())) {
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
                        SpglXmspsxblxxxxbV3 ztjs = new SpglXmspsxblxxxxbV3();
                        ztjs.setRowguid(UUID.randomUUID().toString());
                        ztjs.setDfsjzj(projectID);
                        ztjs.setXzqhdm("370800");
                        ztjs.setGcdm(baseinfo.getItemcode());
                        ztjs.setSpsxslbm(project.getFlowsn());
                        ztjs.setBlcs(Zjconstant.getOunamebyuser(opereateuser));
                        ztjs.setBlr(iuserservice.getUserNameByUserGuid(opereateuser));
                        ztjs.setBlsj(proMessage.getOperatedate());
                        ztjs.setBlyj("特别程序（开始）");
                        ztjs.setBlzt(9);
                        ztjs.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        ztjs.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        if (!iSpglXmjbxxbV3.isExistGcdm(ztjs.getGcdm())) {
                            ztjs.set("sjsczt", "-1");
                            ztjs.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                        }
                        if (!ispglxmspsxblxxbV3.isExistFlowsn(ztjs.getSpsxslbm())) {
                            ztjs.set("sjsczt", "-1");
                            if (StringUtil.isNotBlank(ztjs.getSbyy())) {
                                ztjs.setSbyy(ztjs.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                            else {
                                ztjs.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                        }
                        ztjs.setOperatedate(new Date());
                        // 3.0新增上报字段
                        if (StringUtil.isNotBlank(project.getOuname())) {
                            ztjs.setDwmc(project.getOuname());
                        }
                        else {
                            ztjs.setDwmc("错误数据");
                            ztjs.set("sjsczt", "-1");
                            ztjs.setSbyy("单位名称校验有误！");
                        }
                        if (raExtendInfo != null) {
                            ztjs.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                        }
                        else {
                            ztjs.set("sjsczt", "-1");
                            ztjs.setSbyy("单位统一社会信用代码校验有误！");
                        }

                        if (auditTaskExtension != null) {
                            String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                    auditTaskExtension.getStr("handle_system"));
                            if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                                ztjs.setSjly(SYSTEM_NAME);
                            }
                            else {
                                // 默认塞自己系统
                                ztjs.setSjly("济宁市工程建设项目网上申报系统");
                            }
                        }
                        iSpglXmspsxblxxxxbV3.insert(ztjs);
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
            if (auditProjectOperation == null) {
                return;
            }
            SpglXmspsxblxxxxbV3 spgl = new SpglXmspsxblxxxxbV3();
            spgl.setRowguid(UUID.randomUUID().toString());
            spgl.setOperatedate(new Date());
            spgl.setDfsjzj(project.getRowguid());
            spgl.setGcdm(baseinfo.getItemcode());
            spgl.setSpsxslbm(project.getFlowsn());
            spgl.setBlcs(Zjconstant.getOunamebyuser(auditProjectOperation.getOperateUserGuid()));
            spgl.setBlr(auditProjectOperation.getOperateusername());
            spgl.setBlsj(auditProjectOperation.getOperatedate());
            spgl.setBlyj(auditProjectOperation.getRemarks());
            // 3.0新增上报字段
            if (StringUtil.isNotBlank(project.getOuname())) {
                spgl.setDwmc(project.getOuname());
            }
            else {
                spgl.setDwmc("错误数据");
                spgl.set("sjsczt", "-1");
                spgl.setSbyy("单位名称校验有误！");
            }
            if (raExtendInfo != null) {
                spgl.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
            }
            else {
                spgl.set("sjsczt", "-1");
                spgl.setSbyy("单位统一社会信用代码校验有误！");
            }

            if (auditTaskExtension != null) {
                String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                        auditTaskExtension.getStr("handle_system"));
                if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                    spgl.setSjly(SYSTEM_NAME);
                }
                else {
                    // 默认塞自己系统
                    spgl.setSjly("济宁市工程建设项目网上申报系统");
                }
            }

            if (ZwfwConstant.OPERATE_SL.equals(operatetype)) {
                spgl.setBlzt(3);
                // 受理查询工作流意见
                if (StringUtil.isNotBlank(project.getPviguid())) {
                    // 工作项标识
                    String workitemGuid = messageContent[2];
                    ProcessVersionInstance pvi = wfInstance.getProcessVersionInstance(project.getPviguid());
                    WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pvi, workitemGuid);
                    spgl.setBlyj(workflowWorkItem.getOpinion());
                }
                else {
                    spgl.setBlyj(Zjconstant.SPGL_SL);
                }

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
                        SpglsqcljqtfjxxbV3 fxxb = new SpglsqcljqtfjxxbV3();
                        fxxb.setRowguid(UUID.randomUUID().toString());
                        fxxb.setDfsjzj(frameAttachInfo.getAttachGuid());
                        fxxb.setXzqhdm("370800");
                        fxxb.setGcdm(baseinfo.getItemcode());
                        fxxb.setSpsxslbm(project.getFlowsn());
                        fxxb.setBlspslbm(project.getSubappguid());
                        // 查询materialid
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            fxxb.setClmlbh(StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH"))
                                    ? auditTaskMaterial.get("CLMLBH")
                                    : auditTaskMaterial.getMaterialid());
                        }
                        else {
                            fxxb.setClmlbh("");
                            fxxb.set("sjsczt", "-1");
                            fxxb.setSbyy("材料目录编号校验失败!");
                        }
                        fxxb.setClmc(frameAttachInfo.getAttachFileName());
                        fxxb.setClfl(ZwfwConstant.CONSTANT_INT_ONE);// 默认为1，申报材料

                        fxxb.setCllx(frameAttachInfo.getContentType());
                        fxxb.setClid(frameAttachInfo.getAttachGuid());
                        fxxb.setSqfs(2);
                        fxxb.setSqsl(0);
                        fxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                        fxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                        // 数据验证,项目代码是否在工程代码
                        if (!iSpglXmjbxxbV3.isExistGcdm(baseinfo.getItemcode())) {
                            fxxb.set("sjsczt", "-1");
                            fxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                        }
                        if (!ispglxmspsxblxxbV3.isExistFlowsn(project.getFlowsn())) {
                            fxxb.set("sjsczt", "-1");
                            if (StringUtil.isNotBlank(fxxb.getSbyy())) {
                                fxxb.setSbyy(fxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                            else {
                                fxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                        }
                        iSpglsqcljqtfjxxbV3.insert(fxxb);
                    }
                }

            }
            else if (ZwfwConstant.OPERATE_BYSL.equals(operatetype)) {
                unusual = projectUnusual.getProjectUnusualByProjectGuidAndType(project.getRowguid(),
                        ZwfwConstant.SHENPIOPERATE_TYPE_BYSL).getResult();
                spgl.setBlyj(unusual.getNote());
                spgl.setBlzt(5);
            }
            else if (ZwfwConstant.OPERATE_BJ.equals(operatetype)) {
                AuditProjectOperation operation = ycggoperationservice
                        .getAuditOperation(projectID, ZwfwConstant.OPERATE_SPBTG).getResult();
                spgl.setBlyj(Zjconstant.SPGL_BJ);
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
                if (unusual != null) {
                    SpglXmspsxbltbcxxxbV3 spglxmspsxbltbcxxxb = new SpglXmspsxbltbcxxxbV3();
                    spglxmspsxbltbcxxxb.setRowguid(UUID.randomUUID().toString());
                    spglxmspsxbltbcxxxb.setDfsjzj(project.getRowguid());
                    spglxmspsxbltbcxxxb.setXzqhdm("370800");
                    spglxmspsxbltbcxxxb.setGcdm(baseinfo.getItemcode());
                    spglxmspsxbltbcxxxb.setSpsxslbm(project.getFlowsn());
                    spglxmspsxbltbcxxxb.setTbcx(Integer.valueOf(unusual.getPauseReason()));// TODO
                    // 特殊理由转为代码值
                    // 当时8是set特别程序名称
                    spglxmspsxbltbcxxxb.setTbcxmc(unusual.getPauseReason());
                    spglxmspsxbltbcxxxb.setTbcxkssj(auditProjectOperation.getOperatedate());
                    spglxmspsxbltbcxxxb.setTbcxsxlx(ZwfwConstant.CONSTANT_INT_ONE);// 默认使用1，即工作日类型
                    spglxmspsxbltbcxxxb.setTbcxsx(ZwfwConstant.CONSTANT_INT_ZERO); // 特别程序时限
                    spglxmspsxbltbcxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spglxmspsxbltbcxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    if (!iSpglXmjbxxbV3.isExistGcdm(spglxmspsxbltbcxxxb.getGcdm())) {
                        spglxmspsxbltbcxxxb.set("sjsczt", "-1");
                        spglxmspsxbltbcxxxb.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                    }
                    if (!ispglxmspsxblxxbV3.isExistFlowsn(spglxmspsxbltbcxxxb.getSpsxslbm())) {
                        spglxmspsxbltbcxxxb.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(spglxmspsxbltbcxxxb.getSbyy())) {
                            spglxmspsxbltbcxxxb.setSbyy(
                                    spglxmspsxbltbcxxxb.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                        else {
                            spglxmspsxbltbcxxxb.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                        }
                    }
                    iSpglXmspsxbltbcxxxbV3.insert(spglxmspsxbltbcxxxb);
                }
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
            if (!iSpglXmjbxxbV3.isExistGcdm(spgl.getGcdm())) {
                spgl.set("sjsczt", "-1");
                spgl.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
            }
            if (!ispglxmspsxblxxbV3.isExistFlowsn(spgl.getSpsxslbm())) {
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
                    List<SpglXmspsxblxxxxbV3> listblxxxxb = iSpglXmspsxblxxxxbV3.getListByCondition(sqlc.getMap())
                            .getResult();
                    // 过滤草稿事项
                    listblxxxxb = listblxxxxb.stream()
                            .filter(a -> !ZwfwConstant.CONSTANT_STR_NEGATIVE_ONE.equals(a.getStr("sync")))
                            .collect(Collectors.toList());
                    for (SpglXmspsxblxxxxbV3 spglXmspsxblxxxxb : listblxxxxb) {
                        if (9 == spglXmspsxblxxxxb.getBlzt()) {
                            start = spglXmspsxblxxxxb.getBlsj();
                        }
                        if (10 == spglXmspsxblxxxxb.getBlzt() && start != null) {
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
                        SpglXmspsxblxxxxbV3 hfjs = new SpglXmspsxblxxxxbV3();
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
                        // 3.0新增上报字段
                        hfjs.setDwmc(project.getOuname());
                        if (raExtendInfo != null) {
                            hfjs.setDwtyshxydm(raExtendInfo.getStr("ORGCODE"));
                        }
                        else {
                            hfjs.set("sjsczt", "-1");
                            hfjs.setSbyy("单位统一社会信用代码校验有误！");
                        }

                        if (auditTaskExtension != null) {
                            String SYSTEM_NAME = codeItemsService.getItemTextByCodeName("办理系统",
                                    auditTaskExtension.getStr("handle_system"));
                            if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                                hfjs.setSjly(SYSTEM_NAME);
                            }
                            else {
                                // 默认塞自己系统
                                hfjs.setSjly("济宁市工程建设项目网上申报系统");
                            }
                        }
                        if (!iSpglXmjbxxbV3.isExistGcdm(hfjs.getGcdm())) {
                            hfjs.set("sjsczt", "-1");
                            hfjs.setSbyy("办件工程代码在项目基本信息表无对应项目或对应的项目校验失败！");
                        }
                        if (!ispglxmspsxblxxbV3.isExistFlowsn(hfjs.getSpsxslbm())) {
                            hfjs.set("sjsczt", "-1");
                            if (StringUtil.isNotBlank(hfjs.getSbyy())) {
                                hfjs.setSbyy(hfjs.getSbyy() + ";审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                            else {
                                hfjs.setSbyy("审批事项实例编码在项目审批事项办理信息表中无对应或对应的事项办理信息校验失败！");
                            }
                        }
                        iSpglXmspsxblxxxxbV3.insert(hfjs);

                        // 计算非工作日耗时
                        long diff = spgl.getBlsj().getTime() - start.getTime();
                        // 计算工作日天数
                        int daysnum = ZwfwUtil.weekDays(start, spgl.getBlsj());
                        zttime += (diff - (daysnum * 1000 * 60 * 60 * 24));
                        start = null;
                    }
                    int alldaysnum = ZwfwUtil.weekDays(allstart, allend);
                    long alltime = allend.getTime() - allstart.getTime() - (alldaysnum * 1000 * 60 * 60 * 24);
                    long usetime = (alltime - zttime) / (1000 * 60);
                    long hour = usetime / 60;
                    long days = hour / 24;
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(Zjconstant.validata_mb) && hour < Zjconstant.MB_HOUR) {
                        spgl.set("sjsczt", "-1");
                        if (StringUtil.isNotBlank(spgl.getSbyy())) {
                            spgl.setSbyy(spgl.getSbyy() + ";受理和办结时间相差小于" + Zjconstant.MB_HOUR + "小时，时间较短判断为秒办！");
                        }
                        else {
                            spgl.setSbyy("受理和办结时间相差小于" + Zjconstant.MB_HOUR + "小时，时间较短判断为秒办！");
                        }
                    }
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(Zjconstant.validata_yq)) {
                        // 判断办件信息表
                        SpglXmspsxblxxbV3 spglxmspsxblxxb = ispglxmspsxblxxbV3
                                .getSpglXmspsxblxxbBySlbm(project.getFlowsn());
                        if (spglxmspsxblxxb != null) {
                            if (days >= spglxmspsxblxxb.getSxblsxm()) {
                                spgl.set("sjsczt", "-1");
                                if (StringUtil.isNotBlank(spgl.getSbyy())) {
                                    spgl.setSbyy(spgl.getSbyy() + "; 办件以超期！");
                                }
                                else {
                                    spgl.setSbyy("办件以超期！");
                                }
                            }
                        }
                    }
                }
            }
            spgl.setXzqhdm("370800");
            spgl.setSpsxslbm(project.getFlowsn());
            iSpglXmspsxblxxxxbV3.insert(spgl);
            if (3 == spgl.getBlzt()) {
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setBlzt(8);
                iSpglXmspsxblxxxxbV3.insert(spgl);
            }
        }
        catch (Exception e) {
            log.info("########报错：SpglxmspsxblxxxxbV3ClientHandle监听类报错");
            e.printStackTrace();
        }

    }

    public void checkBack(String field, Object o) {
        if (o == null) {
            throw new RuntimeException(field + "的值不能为空！");
        }
    }

    private Integer getDwlxByCorptype(String corptype) {
        Integer dwlx = 99;
        if ("1".equals(corptype)) {
            dwlx = 1;
        }
        else if ("2".equals(corptype)) {
            dwlx = 2;
        }
        else if ("3".equals(corptype)) {
            dwlx = 3;
        }
        else if ("4".equals(corptype)) {
            dwlx = 4;
        }
        return dwlx;
    }

}

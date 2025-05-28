package com.epoint.gjdw.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.gjdw.api.IGjdwHandleSPInstance;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Service
@Component
public class GjdwHandleSPInstanceImpl implements IGjdwHandleSPInstance {
    private Logger log = LogUtil.getLog(GjdwHandleSPInstanceImpl.class);

    @Override
    public void initInstance(String projectguid, String paramXml) throws Exception {
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial iAuditProjectMaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditSpIMaterial iAuditSpIMaterial = ContainerFactory.getContainInfo().getComponent(IAuditSpIMaterial.class);
        IAuditSpTask iAuditSpTask = ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
        IAuditSpBasetaskR iAuditSpBasetaskR = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetaskR.class);
        IAuditSpPhase iAuditSpPhase = ContainerFactory.getContainInfo().getComponent(IAuditSpPhase.class);
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

        // log.info("获取到的入参：" + projectguid);
        // log.info("获取到的入参：" + paramXml);
        try {
            // 1、解析param
            Document ducument = DocumentHelper.parseText(paramXml);
            Element root = ducument.getRootElement();
            List<Element> elements = root.elements();
            JSONObject jsonxml = xmlToJSON(elements);
            JSONObject dataListObject = JSONObject.parseObject(jsonxml.getString("DATALIST"));
            JSONObject itemObject = JSONObject.parseObject(dataListObject.getString("item"));
            JSONObject textObject = JSONObject.parseObject(itemObject.getString("TEXT"));
            // 项目信息
            JSONObject itemmObject = JSONObject.parseObject(textObject.getString("item"));
            String itemcode = itemmObject.getString("XMBH");
            if (StringUtil.isNotBlank(itemcode)) {
                AuditRsItemBaseinfo itemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(itemcode)
                        .getResult();
                AuditRsItemBaseinfo zitemBaseinfo = new AuditRsItemBaseinfo();
                if (itemBaseinfo != null) {
                    //查询是否有子申报
                    List<AuditSpISubapp> subapps = iAuditSpISubapp.getSubappByBIGuid(itemBaseinfo.getBiguid()).getResult();
                    String flag = "0";
                    if (subapps == null) {
                        flag = "1";
                    }
                    // 初始化subapp信息
                    AuditSpISubapp subapp = new AuditSpISubapp();
                    String subappguid = UUID.randomUUID().toString();
                    subapp.setRowguid(subappguid);
                    subapp.setOperatedate(new Date());
                    subapp.setBiguid(itemBaseinfo.getBiguid());
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(itemBaseinfo.getBiguid())
                            .getResult();
                    if (auditSpInstance != null && StringUtil.isNotBlank(auditSpInstance.getBusinessguid())) {
                        subapp.setBusinessguid(auditSpInstance.getBusinessguid());
                        AuditProject auditProject = iAuditProject
                                .getAuditProjectByRowGuid("*", projectguid, auditSpInstance.getAreacode()).getResult();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.setInnerJoinTable("audit_sp_basetask_r b", "a.basetaskguid", "b.basetaskguid");
                        sql.eq("b.taskid", auditProject.getTask_id());
                        sql.eq("a.businessguid", auditSpInstance.getBusinessguid());
                        List<AuditSpTask> spTasks = iAuditSpTask.getAllAuditSpTask(sql.getMap()).getResult();
                        if (EpointCollectionUtils.isNotEmpty(spTasks)) {
                            subapp.setPhaseguid(spTasks.get(0).getPhaseguid());
                            AuditSpPhase phase = iAuditSpPhase.getAuditSpPhaseByRowguid(spTasks.get(0).getPhaseguid())
                                    .getResult();
                            subapp.setSubappname(itemBaseinfo.getItemname() + phase.getPhasename());
                            subapp.setCreatedate(new Date());
                            subapp.setApplyername(itemmObject.getString("SQDWMC"));
                            subapp.setApplyerway(ZwfwConstant.WEBAPPLYER_STATUS_ZC);
                            subapp.setStatus(ZwfwConstant.LHSP_Status_YSJ);
                            subapp.setInitmaterial(ZwfwConstant.CONSTANT_STR_ONE);
                            SqlConditionUtil sqlc = new SqlConditionUtil();
                            sqlc.eq("parentid", itemBaseinfo.getRowguid());
                            List<AuditRsItemBaseinfo> items = iAuditRsItemBaseinfo
                                    .selectAuditRsItemBaseinfoByCondition(sqlc.getMap()).getResult();
                            if (EpointCollectionUtils.isNotEmpty(items)) {
                                zitemBaseinfo = items.get(0);
                                subapp.setYewuguid(items.get(0).getRowguid());
                            } else {
                                subapp.setYewuguid(itemBaseinfo.getRowguid());
                            }
                            iAuditSpISubapp.addSubapp(subapp);
                            //如果不存在子申报，需要先上报项目
                            if ("1".equals(flag)) {
                                String mqmsg = "";
                                if (!zitemBaseinfo.isEmpty()) {
                                    //如果是新增项目，则需要把项目推送到住建系统
                                    mqmsg = zitemBaseinfo.getRowguid() + "." + auditSpInstance.getAreacode() + "." + subappguid;
                                } else {
                                    //如果是新增项目，则需要把项目推送到住建系统
                                    mqmsg = itemBaseinfo.getRowguid() + "." + auditSpInstance.getAreacode() + "." + subappguid;
                                }

                                sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blsp.rsitem." + auditSpInstance.getBusinessguid());
                            }

                            auditProject.setSubappguid(subappguid);
                            auditProject.setBiguid(itemBaseinfo.getBiguid());
                            iAuditProject.updateProject(auditProject);
                            iAuditSpITask.addTaskInstance(auditSpInstance.getBusinessguid(), itemBaseinfo.getBiguid(),
                                    spTasks.get(0).getPhaseguid(), auditProject.getTaskguid(),
                                    auditProject.getProjectname(), subappguid, ZwfwConstant.CONSTANT_INT_ONE,
                                    auditSpInstance.getAreacode());
                            EpointFrameDsManager.commit();
                            List<AuditSpITask> spITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subappguid)
                                    .getResult();
                            if (EpointCollectionUtils.isNotEmpty(spITasks)) {
                                AuditSpITask spITask = spITasks.get(0);
                                spITask.setProjectguid(projectguid);
                                iAuditSpITask.updateAuditSpITask(spITask);
                            }

                            List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                    .selectProjectMaterial(projectguid).getResult();
                            if (EpointCollectionUtils.isNotEmpty(auditProjectMaterials)) {
                                for (int i = 0; i < auditProjectMaterials.size(); i++) {
                                    AuditProjectMaterial auditProjectMaterial = auditProjectMaterials.get(i);
                                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                                    auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                                    auditSpIMaterial.setOperatedate(new Date());
                                    auditSpIMaterial.setBusinessguid(auditSpInstance.getBusinessguid());
                                    auditSpIMaterial.setBiguid(itemBaseinfo.getBiguid());
                                    auditSpIMaterial.setPhaseguid(spTasks.get(0).getPhaseguid());
                                    auditSpIMaterial.setSubappguid(subappguid);
                                    auditSpIMaterial.setNecessity(auditProjectMaterial.getStr("necessary"));
                                    auditSpIMaterial
                                            .setAllowrongque(String.valueOf(auditProjectMaterial.getIs_rongque()));
                                    auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ZERO);
                                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                            .getResult();
                                    auditSpIMaterial.setMaterialguid(auditTaskMaterial.getMaterialid());
                                    auditSpIMaterial.setSubmittype(auditTaskMaterial.getSubmittype());
                                    auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                                    auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_DSH);
                                    auditSpIMaterial.setCliengguid(auditProjectMaterial.getCliengguid());
                                    //auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                                    auditSpIMaterial.setMaterialname(auditProjectMaterial.getTaskmaterial());
                                    auditSpIMaterial.setOrdernum(1);
                                    iAuditSpIMaterial.addSpIMaterial(auditSpIMaterial);
                                }
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("'");
                            sb.append(projectguid).append("','");
                            sb.append("'");
                            // 办件初始化完成推送办件到住建系统
                            if (!zitemBaseinfo.isEmpty()) {
                                projectSendzj("false", sb.toString(), subappguid, zitemBaseinfo.getItemcode(),
                                        auditSpInstance.getBusinessguid(), auditSpInstance.getAreacode(),
                                        auditProject.getAcceptuserguid());
                            } else {
                                projectSendzj("false", sb.toString(), subappguid, itemBaseinfo.getItemcode(),
                                        auditSpInstance.getBusinessguid(), auditSpInstance.getAreacode(),
                                        auditProject.getAcceptuserguid());
                            }

                        } else {
                            // 抛出异常
                            EpointFrameDsManager.rollback();
                            throw new Exception("未找到主题阶段");
                        }
                    } else {
                        // 抛出异常
                        EpointFrameDsManager.rollback();
                        throw new Exception("未查询到申报实例auditspinstance");
                    }
                }
            }
        } catch (DocumentException e) {
            // 抛出异常
            EpointFrameDsManager.rollback();
            log.info(e.getMessage(), e);
        }

    }

    private JSONObject xmlToJSON(List<Element> elements) {
        JSONObject jsonxml = new JSONObject();
        for (Element element : elements) {
            String name = element.getName();
            String value = element.getText();
            List<Element> subelemetns = element.elements();
            if (subelemetns != null && !subelemetns.isEmpty()) {
                JSONObject subjsonxml = xmlToJSON(subelemetns);
                jsonxml.put(name, subjsonxml);
            } else {
                jsonxml.put(name, value);
            }
        }
        return jsonxml;
    }

    public void projectSendzj(String isck, String projectguids, String subappGuid, String itemcode, String businessGuid,
                              String areacode, String userguid) {
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        // 发送mq消息推送数据
        String msg = subappGuid + "." + areacode + "." + itemcode;
        if ("false".equals(isck)) {
            msg += ".nck";
        } else {
            msg += ".isck";
        }
        // 拼接办理人用户标识
        msg += "." + userguid + "." + projectguids;
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.subapp." + businessGuid);
    }

}

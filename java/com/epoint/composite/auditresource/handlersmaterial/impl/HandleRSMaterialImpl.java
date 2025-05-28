package com.epoint.composite.auditresource.handlersmaterial.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.authentication.UserSession;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditocrid.domain.AuditOcrId;
import com.epoint.basic.auditorga.auditocrid.inter.IAuditOcrId;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;

@Component
@Service
public class HandleRSMaterialImpl implements IHandleRSMaterial
{
    /**
     * 添加材料关联 如果材料原先有附件在，则先删除附件。 如果没有，则先将证照复制到库里，同时改变流程状态，更新工作流材料实例
     * 
     * @param processVersionInstanceGuid
     *            流程实例guid
     * @param certInfo
     *            证照实例对象
     */
    public void addCertGuanlian(AuditProjectMaterial auditProjectMaterial, CertInfo certInfo) {
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
        // 复制证照实例对应的附件
        attachService.copyAttachByClientGuid(certInfo.getCertcliengguid(), "从共享材料库引用", null,
                auditProjectMaterial.getCliengguid());
        // 更新流程材料状态
        auditProjectMaterial.setStatus(20);
        // 更新流程实例材料字段
        auditProjectMaterial.setSharematerialiguid(certInfo.getCertcatalogid());
        auditProjectMaterialBasic.updateProjectMaterial(auditProjectMaterial);
    }

    /**
     * 删除无效的关联
     * 
     * @param processVersionInstanceGuid
     *            流程实例guid
     */
    public void cleanGuanLian(AuditProjectMaterial auditProjectMaterial) {
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        // 如果材料原先有对应的附件,把原先的附件删除
        attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
        int count = attachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
        Integer statu = auditProjectMaterial.getStatus();
        if (count > 0) {
            if (statu != null && !(statu % 10 == 0)) {
                auditProjectMaterial.setStatus(25);
            }
            else {
                auditProjectMaterial.setStatus(20);
            }
        }
        else {
            if (statu != null && !(statu % 10 == 0)) {
                auditProjectMaterial.setStatus(15);
            }
            else {
                auditProjectMaterial.setStatus(10);
            }
        }
        // 更新流程材料状态
        auditProjectMaterial.setSharematerialiguid(null);
        auditProjectMaterialBasic.updateProjectMaterial(auditProjectMaterial);
    }

    /**
     * 办件材料进入共享材料库
     * 
     * @param taskGuid
     *            事项唯一标识
     * @param projectGuid
     *            办件唯一标识
     * @param ownerGuid
     *            申请人唯一标识
     */
    @Override
    public AuditCommonResult<Void> insertShareMaterial(String taskGuid, String projectGuid, String ownerGuid,
            String operateUserGuid, String operateDisplayName, String applyerName, String certNum, int applyertype) {
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectShareMaterialByTaskGuid(taskGuid).getResult();
            List<AuditProjectMaterial> auditProjectMaterials = auditProjectMaterialService
                    .selectProjectMaterial(projectGuid).getResult();
            if (auditTaskMaterials != null && !auditTaskMaterials.isEmpty() && auditProjectMaterials != null
                    && !auditProjectMaterials.isEmpty()) {
                Iterator<AuditProjectMaterial> it = auditProjectMaterials.iterator();
                while (it.hasNext()) {
                    AuditProjectMaterial auditProjectMaterial = it.next();
                    // 提交了电子材料的
                    if (auditProjectMaterial.getStatus() >= 20) {
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            if (auditTaskMaterial.getRowguid().equals(auditProjectMaterial.getTaskmaterialguid())) {
                                auditProjectMaterial.set("sharematerialguid", auditTaskMaterial.getSharematerialguid());
                            }
                        }
                    }
                    // 办件共享材料
                    if (StringUtil.isNotBlank(auditProjectMaterial.get("sharematerialguid"))) {
                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditProjectMaterial.get("sharematerialguid"));
                        String belongtype = "";
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_FR;
                        }
                        if (certCatalog != null) {
                            if (ZwfwConstant.Material_PW.equals(certCatalog.getMaterialtype())
                                    || certCatalog.getBelongtype().contains(belongtype)) {
                                CertInfo certInfo = null;
                                if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                                    certInfo = certInfoExternalImpl
                                            .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                                }
                                if (certInfo != null) {
                                    boolean flag = false;
                                    if ("C".equalsIgnoreCase(certInfo.getCertlevel())) {
                                        certInfo.setCertlevel("B");
                                        flag = true;
                                    }
                                    //持有人证件编号使用页面上传过来的证照编号，以防止页面先获取证照实例之后修改页面证照编号，导致一人会变更另一个人的证照实例的情况
                                    certInfo.setCertownerno(certNum);
                                    int countRs = attachService
                                            .getAttachCountByClientGuid(certInfo.getCertcliengguid());
                                    int countProject = attachService
                                            .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
                                    // 数量不一致
                                    if (countProject > 0 && countProject != countRs) {
                                        if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                            // 证照
                                            certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    auditProjectMaterial.getCliengguid(), ZwfwConstant.Material_ZZ);
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }
                                        else {
                                            // 插入共享材料库
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    auditProjectMaterial.getCliengguid(),
                                                    certCatalog.getMaterialtype());
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }

                                    }
                                    else if (countProject == countRs) {
                                        // 数量一致继续比较
                                        List<FrameAttachInfo> rsInfos = attachService
                                                .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                                        List<FrameAttachInfo> projectInfos = attachService
                                                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                        rsInfos.sort(
                                                (a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                                        projectInfos.sort(
                                                (a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                                        boolean isModify = false;
                                        for (int i = 0; i < rsInfos.size(); i++) {
                                            // AttachStorageGuid不同说明附件改动过
                                            if (!rsInfos.get(i).getAttachStorageGuid()
                                                    .equals(projectInfos.get(i).getAttachStorageGuid())) {
                                                isModify = true;
                                                break;
                                            }
                                        }

                                        if (isModify) {
                                            // 证照
                                            if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                                // 证照
                                                certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        auditProjectMaterial.getCliengguid(), ZwfwConstant.Material_ZZ);
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                            else {
                                                // 共享材料
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        auditProjectMaterial.getCliengguid(),
                                                        certCatalog.getMaterialtype());
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                        }
                                        else {
                                            if (flag) {
                                                certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        auditProjectMaterial.getCliengguid(),
                                                        certCatalog.getMaterialtype());
                                            }
                                        }
                                    }
                                }
                                else {
                                    if (certCatalog != null
                                            && ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                        // 添加证照基本信息
                                        certInfo = new CertInfo();
                                        String certinfoinstance = UUID.randomUUID().toString();
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertlevel("B");
                                        certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                        certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                auditProjectMaterial.getCliengguid(), ZwfwConstant.Material_ZZ);
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                    }
                                    else {
                                        // 不存在共享材料直接插入
                                        certInfo = new CertInfo();
                                        String certinfoinstance = UUID.randomUUID().toString();
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                auditProjectMaterial.getCliengguid(), certCatalog.getMaterialtype());
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> initMaterialAttachFromCert(String projectGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        IAuditTaskMaterial auditTaskMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        List<AuditProjectMaterial> auditProjectMaterialList = auditProjectMaterialBasic
                .selectProjectMaterial(projectGuid).getResult();
        // 遍历工作流配置材料
        try {
            if (auditProjectMaterialList != null && !auditProjectMaterialList.isEmpty()) {
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {

                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialBasic
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    CertInfo certInfo = null;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                        if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                            certInfo = certInfoExternalImpl
                                    .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                        }
                        if (certInfo != null) {
                            int count = attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid());
                            if (count > 0) {
                                // 设置关联
                                this.addCertGuanlian(auditProjectMaterial, certInfo);
                            }
                            else {
                                // 不存在附件
                                this.cleanGuanLian(auditProjectMaterial);
                            }
                        }
                        else {
                            // 不存在共享材料
                            this.cleanGuanLian(auditProjectMaterial);
                        }

                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<Void> initMaterialAttachFromCert(String projectGuid, String areacode, String userGuid,
            String displayName) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        IAuditTaskMaterial auditTaskMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        ICertAttachExternal iCertAttachExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertAttachExternal.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        List<AuditProjectMaterial> auditProjectMaterialList = auditProjectMaterialBasic
                .selectProjectMaterial(projectGuid).getResult();
        // 遍历工作流配置材料
        try {
            if (auditProjectMaterialList != null && !auditProjectMaterialList.isEmpty()) {
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {
                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialBasic
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    CertInfo certInfo = null;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                        if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                            certInfo = certInfoExternalImpl
                                    .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                        }
                        if (certInfo != null) {
                            List<JSONObject> attachList = iCertAttachExternal
                                    .getAttachList(certInfo.getCertcliengguid(), areacode);
                            attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                            if (attachList != null && !attachList.isEmpty()) {
                                if (attachList != null && !attachList.isEmpty()) {
                                    for (JSONObject json : attachList) {
                                        //独立证照库情况下，将附件存储到本地附件库中。本地模式下，实现附件复制
                                        AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                                auditProjectMaterial.getCliengguid(), json.getString("attachname"),
                                                json.getString("contentype"), json.getString("attachstorageguid"),
                                                json.getLong("size"),
                                                iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                                userGuid, displayName);
                                    }
                                }
                                // 更新流程材料状态
                                Integer statu = auditProjectMaterial.getStatus();
                                if (statu != null && !(statu % 10 == 0)) {
                                    auditProjectMaterial.setStatus(25);
                                }
                                else {
                                    auditProjectMaterial.setStatus(20);
                                }
                                // 更新流程实例材料字段
                                auditProjectMaterial.setSharematerialiguid(certInfo.getCertcatalogid());
                                auditProjectMaterialBasic.updateProjectMaterial(auditProjectMaterial);
                            }
                            else {
                                // 不存在附件
                                this.cleanGuanLian(auditProjectMaterial);
                            }
                        }
                        else {
                            // 不存在共享材料
                            this.cleanGuanLian(auditProjectMaterial);
                        }

                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<Void> initMaterialAttachFromCertByMaterialguid(String projectGuid, String materialguid,
            String areacode, String userGuid, String displayName) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        IAuditTaskMaterial auditTaskMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        ICertAttachExternal iCertAttachExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertAttachExternal.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        AuditProjectMaterial auditProjectMaterial = auditProjectMaterialBasic
                .getProjectMaterialDetail(materialguid, projectGuid).getResult();
        // 遍历工作流配置材料
        try {
            if (auditProjectMaterial != null) {
                AuditTaskMaterial auditTaskMaterial = auditTaskMaterialBasic
                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                CertInfo certInfo = null;
                // 事项材料存在共享材料
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                        certInfo = certInfoExternalImpl
                                .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                    }
                    if (certInfo != null) {
                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(certInfo.getCertcliengguid(),
                                areacode);
                        attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                        if (attachList.size() > 0) {
                            if (attachList != null && attachList.size() > 0) {
                                for (JSONObject json : attachList) {
                                    //独立证照库情况下，将附件存储到本地附件库中。本地模式下，实现附件复制
                                    AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                            auditProjectMaterial.getCliengguid(), json.getString("attachname"),
                                            json.getString("contentype"), json.getString("attachstorageguid"),
                                            json.getLong("size"),
                                            iCertAttachExternal.getAttach(json.getString("attachguid"), areacode),
                                            userGuid, displayName);
                                }
                            }
                            // 更新流程材料状态
                            Integer statu = auditProjectMaterial.getStatus();
                            if (statu != null && !(statu % 10 == 0)) {
                                auditProjectMaterial.setStatus(25);
                            }
                            else {
                                auditProjectMaterial.setStatus(20);
                            }
                            // 更新流程实例材料字段
                            auditProjectMaterial.setSharematerialiguid(certInfo.getCertcatalogid());
                            auditProjectMaterialBasic.updateProjectMaterial(auditProjectMaterial);
                        }
                        else {
                            // 不存在附件
                            this.cleanGuanLian(auditProjectMaterial);
                        }
                    }
                    else {
                        // 不存在共享材料
                        this.cleanGuanLian(auditProjectMaterial);
                    }

                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<String> is_ExistCert(String projectGuid, String applyerType, String certNum) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditTaskMaterial auditTaskMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        List<AuditProjectMaterial> auditProjectMaterialList = auditProjectMaterialBasic
                .selectProjectMaterial(projectGuid).getResult();
        // 默认不存在共享材料
        result.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
        // 遍历工作流配置材料
        try {
            if (auditProjectMaterialList != null && !auditProjectMaterialList.isEmpty()) {
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {

                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialBasic
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    Boolean flag = false;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())
                            && !"20".equals(auditTaskMaterial.getSubmittype())) {

                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditTaskMaterial.getSharematerialguid());

                        if (certCatalog != null) {
                            String certownertype = "";
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyerType))) {
                                certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                            }
                            else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyerType))) {
                                certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
                            }

                            flag = certInfoExternalImpl.isExistCertByOwner(auditTaskMaterial.getSharematerialguid(),
                                    certownertype, "", certNum, null);
                            if (flag) {
                                result.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                                return result;
                            }
                            else {
                                result.setResult("2");
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> createShareMaterialI(String shareMaterialGuid, String certnum, String oldCliengGuid,
            String newCliengGuid, String detailRowGuid, int addWay, String addUserGuid, String addUserName,
            String applyername, String certownertype) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        try {
            // 1、获取共享材料配置
            CertCatalog certCatalog = certConfigExternalImpl.getCatalogByCatalogid(shareMaterialGuid);
            if (certCatalog != null && StringUtil.isNotBlank(certnum)) {
                // 2、创建共享实例
                CertInfo certInfo = new CertInfo();
                certInfo.setRowguid(UUID.randomUUID().toString());
                certInfo.setAdduserguid(addUserGuid);
                certInfo.setAddusername(addUserName);
                certInfo.setCertname(certCatalog.getCertname());
                certInfo.setMaterialtype(certCatalog.getMaterialtype());
                certInfo.setOperatedate(new Date());
                certInfo.setCertownertype(certownertype);
                certInfo.setOperateusername(addUserName);
                certInfo.setCertownerno(certnum);
                certInfo.setCertownername(applyername);
                certInfo.setCertcatalogid(shareMaterialGuid);
                certInfo.setVersiondate(new Date());
                certInfoExternalImpl.submitCertInfo(certInfo, null, oldCliengGuid, certCatalog.getMaterialtype());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> createShareMaterialI(String shareMaterialGuid, String certnum, String oldCliengGuid,
            String newCliengGuid, String detailRowGuid, int addWay, String addUserGuid, String addUserName,
            String applyername, String certownertype, String certownercerttype) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        try {
            // 1、获取共享材料配置
            CertCatalog certCatalog = certConfigExternalImpl.getCatalogByCatalogid(shareMaterialGuid);
            if (certCatalog != null && StringUtil.isNotBlank(certnum)) {
                // 2、创建共享实例
                CertInfo certInfo = new CertInfo();
                certInfo.setRowguid(UUID.randomUUID().toString());
                certInfo.setAdduserguid(addUserGuid);
                certInfo.setAddusername(addUserName);
                certInfo.setCertname(certCatalog.getCertname());
                certInfo.setMaterialtype(certCatalog.getMaterialtype());
                certInfo.setCertownercerttype(certownercerttype);
                certInfo.setOperatedate(new Date());
                certInfo.setCertownertype(certownertype);
                certInfo.setOperateusername(addUserName);
                certInfo.setCertownerno(certnum);
                certInfo.setCertownername(applyername);
                certInfo.setCertcatalogid(shareMaterialGuid);
                certInfo.setVersiondate(new Date());
                certInfoExternalImpl.submitCertInfo(certInfo, null, oldCliengGuid, certCatalog.getMaterialtype());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> is_ExistCert(String projectGuid, String applyerType, String certNum,
            String certownercerttype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditTaskMaterial auditTaskMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        List<AuditProjectMaterial> auditProjectMaterialList = auditProjectMaterialBasic
                .selectProjectMaterial(projectGuid).getResult();
        // 默认不存在共享材料
        result.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
        // 遍历工作流配置材料
        try {
            if (auditProjectMaterialList != null && !auditProjectMaterialList.isEmpty()) {
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {

                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialBasic
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    Boolean flag = false;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())
                            && !"20".equals(auditTaskMaterial.getSubmittype())) {

                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditTaskMaterial.getSharematerialguid());

                        if (certCatalog != null) {
                            String certownertype = "";
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyerType))) {
                                certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                            }
                            else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyerType))) {
                                certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
                            }

                            flag = certInfoExternalImpl.isExistCertByOwner(auditTaskMaterial.getSharematerialguid(),
                                    certownertype, certownercerttype, certNum, null);
                            if (flag) {
                                result.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                                return result;
                            }
                            else {
                                result.setResult("2");
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> is_ExistCert(String projectGuid, String applyerType, String certNum,
            String certownercerttype, String areacode) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditTaskMaterial auditTaskMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialBasic = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);

        IAuditTaskMaterialCase iaudittaskmaterialcase = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterialCase.class);
        IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

        List<AuditProjectMaterial> auditProjectMaterialList = auditProjectMaterialBasic
                .selectProjectMaterial(projectGuid).getResult();
        // 默认不存在共享材料
        result.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
        // 遍历工作流配置材料
        try {
            AuditProject auditProject = iauditproject.getAuditProjectByRowGuid(projectGuid, areacode).getResult();
            Map<String, Integer> caseMap = null;
            if (auditProject != null && StringUtil.isNotBlank(auditProject.getTaskcaseguid())
                    && !"none".equals(auditProject.getTaskcaseguid())) {
                List<AuditTaskMaterialCase> auditTaskMaterialCases = iaudittaskmaterialcase
                        .selectTaskMaterialCaseByCaseGuid(auditProject.getTaskcaseguid()).getResult();
                caseMap = new HashMap<>(16);
                // 转成map方便查找
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }

            if (auditProjectMaterialList != null && !auditProjectMaterialList.isEmpty()) {
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {
                    if (caseMap != null && !caseMap.containsKey(auditProjectMaterial.get("taskmaterialguid"))) {
                        continue;
                    }
                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialBasic
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    Boolean flag = false;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())
                            && !"20".equals(auditTaskMaterial.getSubmittype())) {

                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditTaskMaterial.getSharematerialguid(), areacode);

                        if (certCatalog != null) {
                            String certownertype = "";
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyerType))) {
                                certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                            }
                            else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyerType))) {
                                certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
                            }

                            flag = certInfoExternalImpl.isExistCertByOwner(auditTaskMaterial.getSharematerialguid(),
                                    certownertype, certownercerttype, certNum, null);
                            if (flag) {
                                result.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                                return result;
                            }
                            else {
                                result.setResult("2");
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> insertShareMaterial(String taskGuid, String projectGuid, String ownerGuid,
            String operateUserGuid, String operateDisplayName, String applyerName, String certNum, int applyertype,
            String certownercerttype) {
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectShareMaterialByTaskGuid(taskGuid).getResult();
            List<AuditProjectMaterial> auditProjectMaterials = auditProjectMaterialService
                    .selectProjectMaterial(projectGuid).getResult();
            if (auditTaskMaterials != null && auditTaskMaterials.size() > 0 && auditProjectMaterials != null
                    && auditProjectMaterials.size() > 0) {
                Iterator<AuditProjectMaterial> it = auditProjectMaterials.iterator();
                while (it.hasNext()) {
                    AuditProjectMaterial auditProjectMaterial = it.next();
                    // 提交了电子材料的
                    if (auditProjectMaterial.getStatus() >= 20) {
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            if (auditTaskMaterial.getRowguid().equals(auditProjectMaterial.getTaskmaterialguid())) {
                                auditProjectMaterial.set("sharematerialguid", auditTaskMaterial.getSharematerialguid());
                            }
                        }
                    }
                    // 办件共享材料
                    if (StringUtil.isNotBlank(auditProjectMaterial.get("sharematerialguid"))) {
                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditProjectMaterial.get("sharematerialguid"));
                        String belongtype = "";
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_FR;
                        }
                        if (certCatalog != null) {
                            if (ZwfwConstant.Material_PW.equals(certCatalog.getMaterialtype())
                                    || certCatalog.getBelongtype().contains(belongtype)) {
                                CertInfo certInfo = null;
                                if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                                    certInfo = certInfoExternalImpl
                                            .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                                }
                                if (certInfo != null) {
                                    boolean flag = false;
                                    if ("C".equalsIgnoreCase(certInfo.getCertlevel())) {
                                        certInfo.setCertlevel("B");
                                        flag = true;
                                    }
                                    //持有人证件编号使用页面上传过来的证照编号，以防止页面先获取证照实例之后修改页面证照编号，导致一人会变更另一个人的证照实例的情况
                                    certInfo.setCertownerno(certNum);
                                    certInfo.setCertownercerttype(certownercerttype);
                                    int countRs = attachService
                                            .getAttachCountByClientGuid(certInfo.getCertcliengguid());
                                    int countProject = attachService
                                            .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
                                    // 数量不一致
                                    if (countProject > 0 && countProject != countRs) {
                                        if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                            // 证照
                                            certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    auditProjectMaterial.getCliengguid(), ZwfwConstant.Material_ZZ);
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }
                                        else {
                                            // 插入共享材料库
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    auditProjectMaterial.getCliengguid(),
                                                    certCatalog.getMaterialtype());
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }

                                    }
                                    else if (countProject == countRs) {
                                        // 数量一致继续比较
                                        List<FrameAttachInfo> rsInfos = attachService
                                                .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                                        List<FrameAttachInfo> projectInfos = attachService
                                                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                        rsInfos.sort(
                                                (a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                                        projectInfos.sort(
                                                (a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                                        boolean isModify = false;
                                        for (int i = 0; i < rsInfos.size(); i++) {
                                            // AttachStorageGuid不同说明附件改动过
                                            if (!rsInfos.get(i).getAttachStorageGuid()
                                                    .equals(projectInfos.get(i).getAttachStorageGuid())) {
                                                isModify = true;
                                                break;
                                            }
                                        }

                                        if (isModify) {
                                            // 证照
                                            if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                                // 证照
                                                certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        auditProjectMaterial.getCliengguid(), ZwfwConstant.Material_ZZ);
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                            else {
                                                // 共享材料
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        auditProjectMaterial.getCliengguid(),
                                                        certCatalog.getMaterialtype());
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                        }
                                        else {
                                            if (flag) {
                                                certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        auditProjectMaterial.getCliengguid(),
                                                        certCatalog.getMaterialtype());
                                            }
                                        }
                                    }
                                }
                                else {
                                    if (certCatalog != null
                                            && ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                        // 添加证照基本信息
                                        certInfo = new CertInfo();
                                        String certinfoinstance = UUID.randomUUID().toString();
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertownercerttype(certownercerttype);
                                        certInfo.setCertlevel("B");
                                        certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                        certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                auditProjectMaterial.getCliengguid(), ZwfwConstant.Material_ZZ);
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                    }
                                    else {
                                        // 不存在共享材料直接插入
                                        certInfo = new CertInfo();
                                        String certinfoinstance = UUID.randomUUID().toString();
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertownercerttype(certownercerttype);
                                        certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                auditProjectMaterial.getCliengguid(), certCatalog.getMaterialtype());
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> createShareMaterialI(String shareMaterialGuid, String certnum, String oldCliengGuid,
            String newCliengGuid, String detailRowGuid, int addWay, String addUserGuid, String addUserName,
            String applyername, String certownertype, String certownercerttype, String certrowguid, String areacode) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        try {
            AuditProject auditproject = auditProjectService.getAuditProjectByRowGuid(oldCliengGuid, areacode)
                    .getResult();
            FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditproject.getOuguid());
            // 1、获取共享材料配置
            CertCatalog certCatalog = certConfigExternalImpl.getCatalogByCatalogid(shareMaterialGuid, areacode);
            if (certCatalog != null && StringUtil.isNotBlank(certnum)) {
                // 2、创建共享实例
                CertInfo certInfo = new CertInfo();
                certInfo.setRowguid(certrowguid);
                certInfo.setAdduserguid(addUserGuid);
                certInfo.setAddusername(addUserName);
                certInfo.setCertname(certCatalog.getCertname());
                certInfo.setMaterialtype(certCatalog.getMaterialtype());
                certInfo.setOperatedate(new Date());
                certInfo.setCertownertype(certownertype);
                certInfo.setCertownercerttype(certownercerttype);
                certInfo.setOperateusername(addUserName);
                certInfo.setCertownerno(certnum);
                certInfo.setCertownername(applyername);
                certInfo.setCertcatalogid(shareMaterialGuid);
                certInfo.setVersiondate(new Date());
                certInfo.setCertcliengguid(oldCliengGuid);
                certInfo.setOuguid(UserSession.getInstance().getOuGuid());
                certInfoExternalImpl.submitCertInfo(certInfo, null, certCatalog.getMaterialtype(), areacode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> insertShareMaterial(String taskGuid, String projectGuid, String ownerGuid,
            String operateUserGuid, String operateDisplayName, String applyerName, String certNum, int applyertype,
            String certownercerttype, String areacode) {
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        ICertAttachExternal certAttachExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertAttachExternal.class);
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectShareMaterialByTaskGuid(taskGuid).getResult();
            List<AuditProjectMaterial> auditProjectMaterials = auditProjectMaterialService
                    .selectProjectMaterial(projectGuid).getResult();
            if (auditTaskMaterials != null && auditTaskMaterials.size() > 0 && auditProjectMaterials != null
                    && auditProjectMaterials.size() > 0) {
                Iterator<AuditProjectMaterial> it = auditProjectMaterials.iterator();
                while (it.hasNext()) {
                    AuditProjectMaterial auditProjectMaterial = it.next();
                    // 提交了电子材料的
                    if (auditProjectMaterial.getStatus() >= 20) {
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            if (auditTaskMaterial.getRowguid().equals(auditProjectMaterial.getTaskmaterialguid())) {
                                auditProjectMaterial.set("sharematerialguid", auditTaskMaterial.getSharematerialguid());
                            }
                        }
                    }
                    // 办件共享材料
                    if (StringUtil.isNotBlank(auditProjectMaterial.get("sharematerialguid"))) {
                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditProjectMaterial.get("sharematerialguid"), areacode);
                        String belongtype = "";
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_FR;
                        }
                        if (certCatalog != null) {
                            if (ZwfwConstant.Material_SQBGZ.equals(certCatalog.getMaterialtype())
                                    || ZwfwConstant.Material_PW.equals(certCatalog.getMaterialtype())
                                    || certCatalog.getBelongtype().contains(belongtype)) {
                                CertInfo certInfo = null;
                                if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                                    certInfo = certInfoExternalImpl
                                            .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                                }
                                if (certInfo != null) {
                                    boolean flag = false;
                                    if ("C".equalsIgnoreCase(certInfo.getCertlevel())) {
                                        certInfo.setCertlevel("B");
                                        flag = true;
                                    }
                                    //持有人证件编号使用页面上传过来的证照编号，以防止页面先获取证照实例之后修改页面证照编号，导致一人会变更另一个人的证照实例的情况
                                    certInfo.setCertownerno(certNum);
                                    certInfo.setCertownercerttype(certownercerttype);
                                    List<JSONObject> rsInfos = certAttachExternalImpl
                                            .getAttachList(certInfo.getCertcliengguid(), areacode);
                                    int countRs = 0;
                                    if (rsInfos != null) {
                                        countRs = rsInfos.size();
                                    }
                                    int countProject = attachService
                                            .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
                                    // 数量不一致
                                    if (countProject > 0 && countProject != countRs) {
                                        if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                            // 证照
                                            certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                            certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    ZwfwConstant.Material_ZZ, areacode);
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }
                                        else {
                                            certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                            // 插入共享材料库
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    certCatalog.getMaterialtype(), areacode);
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }

                                    }
                                    else if (countProject == countRs) {
                                        // 数量一致继续比较
                                        List<FrameAttachInfo> projectInfos = attachService
                                                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                        /*rsInfos.sort((a, b) -> a.getString("attachstorageguid").compareTo(b.getString("attachstorageguid")));
                                        projectInfos
                                                .sort((a, b) -> a.getCliengTag().compareTo(b.getCliengTag()));
                                        boolean isModify = false;
                                        if(rsInfos.size() > 0 && rsInfos != null){
                                            for (int i = 0; i < rsInfos.size(); i++) {
                                                // AttachStorageGuid不同说明附件改动过
                                                if (!rsInfos.get(i).getString("attachstorageguid")
                                                        .equals(projectInfos.get(i).getCliengTag())) {
                                                    isModify = true;
                                                    break;
                                                }
                                            }
                                        }*/
                                        if (rsInfos != null) {
                                            rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                        }
                                        projectInfos.sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                        boolean isModify = false;
                                        if (rsInfos != null && rsInfos.size() > 0) {
                                            for (int i = 0; i < rsInfos.size(); i++) {
                                                //如果是核对过的cliengtag不为空，若为空且大小不同表示已修改
                                                if (StringUtil.isBlank(projectInfos.get(i).getCliengTag())
                                                        && rsInfos.get(i).getLong("size").longValue() != projectInfos
                                                                .get(i).getAttachLength().longValue()) {
                                                    isModify = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (isModify) {
                                            // 证照
                                            if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                                // 证照
                                                certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                                certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        ZwfwConstant.Material_ZZ, areacode);
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                            else {
                                                certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                                // 共享材料
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        certCatalog.getMaterialtype(), areacode);
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                        }
                                        else {
                                            if (flag) {
                                                certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                                certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        certCatalog.getMaterialtype(), areacode);
                                            }
                                        }
                                    }
                                    // 证照更新执行
                                    doMateInfo(auditProjectMaterial.getRowguid(), certInfo,
                                            ZwfwConstant.CONSTANT_STR_ZERO);
                                }
                                else {
                                    if (certCatalog != null
                                            && ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                        // 添加证照基本信息
                                        certInfo = new CertInfo();
                                        String certinfoinstance = UUID.randomUUID().toString();
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertownercerttype(certownercerttype);
                                        certInfo.setCertlevel("B");
                                        certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                        certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                        certInfoExternalImpl.submitCertInfo(certInfo, null, ZwfwConstant.Material_ZZ,
                                                areacode);
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        // 确认是否有元数据信息，有就插入该信息
                                        doMateInfo(auditProjectMaterial.getRowguid(), certInfo,
                                                ZwfwConstant.CONSTANT_STR_ONE);
                                    }
                                    else {
                                        // 不存在共享材料直接插入
                                        certInfo = new CertInfo();
                                        String certinfoinstance = UUID.randomUUID().toString();
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertownercerttype(certownercerttype);
                                        certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                        certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                certCatalog.getMaterialtype(), areacode);
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> insertShareMaterial(String taskGuid, String projectGuid, String ownerGuid,
            String operateUserGuid, String operateDisplayName, String applyerName, String certNum, int applyertype,
            String certownercerttype, String areacode, String orgcode) {
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        ICertAttachExternal certAttachExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertAttachExternal.class);
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectShareMaterialByTaskGuid(taskGuid).getResult();
            List<AuditProjectMaterial> auditProjectMaterials = auditProjectMaterialService
                    .selectProjectMaterial(projectGuid).getResult();
            if (auditTaskMaterials != null && auditTaskMaterials.size() > 0 && auditProjectMaterials != null
                    && auditProjectMaterials.size() > 0) {
                Iterator<AuditProjectMaterial> it = auditProjectMaterials.iterator();
                while (it.hasNext()) {
                    AuditProjectMaterial auditProjectMaterial = it.next();
                    // 提交了电子材料的
                    if (auditProjectMaterial.getStatus() >= 20) {
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            if (auditTaskMaterial.getRowguid().equals(auditProjectMaterial.getTaskmaterialguid())) {
                                auditProjectMaterial.set("sharematerialguid", auditTaskMaterial.getSharematerialguid());
                            }
                        }
                    }
                    // 办件共享材料
                    if (StringUtil.isNotBlank(auditProjectMaterial.get("sharematerialguid"))) {
                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditProjectMaterial.get("sharematerialguid"), areacode);
                        String belongtype = "";
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                            belongtype = ZwfwConstant.CERTOWNERTYPE_FR;
                        }
                        if (certCatalog != null) {
                            if (ZwfwConstant.Material_QT.equals(certCatalog.getMaterialtype())
                                    || ZwfwConstant.Material_ZZCL.equals(certCatalog.getMaterialtype())
                                    || ZwfwConstant.Material_QWJGWJ.equals(certCatalog.getMaterialtype())
                                    || ZwfwConstant.Material_SQBGZ.equals(certCatalog.getMaterialtype())
                                    || ZwfwConstant.Material_PW.equals(certCatalog.getMaterialtype())
                                    || (certCatalog.getBelongtype().contains(belongtype)
                                            || CertConstant.CERTOWNERTYPE_HH.equals(certCatalog.getBelongtype())
                                            || CertConstant.CERTOWNERTYPE_QT.equals(certCatalog.getBelongtype()))) {
                                CertInfo certInfo = null;
                                if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                                    certInfo = certInfoExternalImpl
                                            .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                                }
                                if (certInfo != null) {
                                    boolean flag = false;
                                    if ("C".equalsIgnoreCase(certInfo.getCertlevel())) {
                                        flag = true;
                                    }
                                    //持有人证件编号使用页面上传过来的证照编号，以防止页面先获取证照实例之后修改页面证照编号，导致一人会变更另一个人的证照实例的情况
                                    certInfo.setCertownerno(certNum);
                                    certInfo.setCertownercerttype(certownercerttype);
                                    certInfo.setCertlevel("B");
                                    List<JSONObject> rsInfos = certAttachExternalImpl
                                            .getAttachList(certInfo.getCertcliengguid(), areacode);
                                    int countRs = 0;
                                    if (rsInfos != null) {
                                        countRs = rsInfos.size();
                                    }
                                    int countProject = attachService
                                            .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
                                    // 数量不一致
                                    if (countProject > 0 && countProject != countRs) {
                                        if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                            // 证照
                                            certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                            certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    ZwfwConstant.Material_ZZ, areacode);
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }
                                        else {
                                            certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                            // 插入共享材料库
                                            String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                    certCatalog.getMaterialtype(), areacode);
                                            //关联到办件材料
                                            auditProjectMaterial.remove("sharematerialguid");
                                            auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                            auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        }

                                    }
                                    else if (countProject == countRs) {
                                        // 数量一致继续比较
                                        List<FrameAttachInfo> projectInfos = attachService
                                                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                        /*rsInfos.sort((a, b) -> a.getString("attachstorageguid").compareTo(b.getString("attachstorageguid")));
                                        projectInfos
                                                .sort((a, b) -> a.getCliengTag().compareTo(b.getCliengTag()));
                                        boolean isModify = false;
                                        if(rsInfos.size() > 0 && rsInfos != null){
                                            for (int i = 0; i < rsInfos.size(); i++) {
                                                // AttachStorageGuid不同说明附件改动过
                                                if (!rsInfos.get(i).getString("attachstorageguid")
                                                        .equals(projectInfos.get(i).getCliengTag())) {
                                                    isModify = true;
                                                    break;
                                                }
                                            }
                                        }*/
                                        if (rsInfos != null) {
                                            rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                        }
                                        projectInfos.sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                        boolean isModify = false;
                                        if (rsInfos != null && rsInfos.size() > 0) {
                                            for (int i = 0; i < rsInfos.size(); i++) {
                                                //如果是核对过的cliengtag不为空，若为空且大小不同表示已修改
                                                if (StringUtil.isBlank(projectInfos.get(i).getCliengTag())
                                                        && rsInfos.get(i).getLong("size").longValue() != projectInfos
                                                                .get(i).getAttachLength().longValue()) {
                                                    isModify = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (isModify) {
                                            // 证照
                                            if (ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                                // 证照
                                                certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                                certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        ZwfwConstant.Material_ZZ, areacode);
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                            else {
                                                certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                                // 共享材料
                                                String certguid = certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        certCatalog.getMaterialtype(), areacode);
                                                //关联到办件材料
                                                auditProjectMaterial.remove("sharematerialguid");
                                                auditProjectMaterial.setCertinfoinstanceguid(certguid);
                                                auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                            }
                                        }
                                        else {
                                            if (flag) {
                                                certInfo.setOuguid(UserSession.getInstance().getOuGuid());
                                                certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                                certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                        certCatalog.getMaterialtype(), areacode);
                                            }
                                        }
                                    }
                                    // 证照更新执行
                                    doMateInfo(auditProjectMaterial.getRowguid(), certInfo,
                                            ZwfwConstant.CONSTANT_STR_ZERO);
                                }
                                else {
                                    if (certCatalog != null
                                            && ZwfwConstant.Material_ZZ.equals(certCatalog.getMaterialtype())) {
                                        // 添加证照基本信息
                                        certInfo = new CertInfo();
                                        String certinfoinstance;
                                        if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                                            certinfoinstance = auditProjectMaterial.getCertinfoinstanceguid();
                                        }
                                        else {
                                            certinfoinstance = UUID.randomUUID().toString();
                                        }
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertownercerttype(certownercerttype);
                                        certInfo.setOuguid(UserSession.getInstance().getOuGuid());
                                        certInfo.setCertlevel("B");
                                        certInfo.setMaterialtype(ZwfwConstant.Material_ZZ);
                                        certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                        certInfoExternalImpl.submitCertInfo(certInfo, null, ZwfwConstant.Material_ZZ,
                                                areacode);
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                        // 确认是否有元数据信息，有就插入该信息
                                        doMateInfo(auditProjectMaterial.getRowguid(), certInfo,
                                                ZwfwConstant.CONSTANT_STR_ONE);
                                    }
                                    else {
                                        // 不存在共享材料直接插入
                                        certInfo = new CertInfo();
                                        String certinfoinstance;
                                        if (StringUtil.isNotBlank(auditProjectMaterial.getCertinfoinstanceguid())) {
                                            certinfoinstance = auditProjectMaterial.getCertinfoinstanceguid();
                                        }
                                        else {
                                            certinfoinstance = UUID.randomUUID().toString();
                                        }
                                        certInfo.setRowguid(certinfoinstance);
                                        certInfo.setCertcatalogid(auditProjectMaterial.get("sharematerialguid"));
                                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_ZRR);
                                        }
                                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(applyertype))) {
                                            certInfo.setCertownertype(ZwfwConstant.CERTOWNERTYPE_FR);
                                        }
                                        certInfo.setOuguid(UserSession.getInstance().getOuGuid());
                                        certInfo.setCertownername(applyerName);
                                        certInfo.setCertname(certCatalog.getCertname());
                                        certInfo.setCertownerno(certNum);
                                        certInfo.setCertownercerttype(certownercerttype);
                                        certInfo.setCertcliengguid(auditProjectMaterial.getCliengguid());
                                        certInfoExternalImpl.submitCertInfo(certInfo, null,
                                                certCatalog.getMaterialtype(), areacode);
                                        //关联到办件材料
                                        auditProjectMaterial.remove("sharematerialguid");
                                        auditProjectMaterial.setCertinfoinstanceguid(certinfoinstance);
                                        auditProjectMaterialService.updateProjectMaterial(auditProjectMaterial);
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void doMateInfo(String materialInstanceGuid, CertInfo certInfo, String isNew) {
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        // 判断是否启用OCR身份证扫描功能
        String AS_ID_CERTCATCODE = iConfigService.getFrameConfigValue("AS_ID_CERTCATCODE");
        if (StringUtil.isNotBlank(AS_ID_CERTCATCODE)) {
            DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                    ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
            MongodbUtil mongodbUtil = new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(),
                    dsc.getPassword());
            IAuditOcrId iAuditOcrId = ContainerFactory.getContainInfo().getComponent(IAuditOcrId.class);
            ICertMetaData iCertMetaData = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);
            // 如果是新增证照实例的
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isNew)) {
                // 把当前库里在用的临时证照设置为在用证照
                AuditOcrId auditOcrIdNew = iAuditOcrId.getAuditOcrIdByMaterialInstanceGuid(materialInstanceGuid)
                        .getResult();
                if (auditOcrIdNew != null) {
                    CertInfoExtension dataBean;
                    // 获得原有元数据信息
                    Map<String, Object> filter = new HashMap<>(16);
                    // 设置基本信息guid
                    filter.put("certinfoguid", certInfo.getRowguid());
                    dataBean = mongodbUtil.find(CertInfoExtension.class, filter, false);
                    if (dataBean == null) {
                        dataBean = new CertInfoExtension();
                    }
                    // 获得元数据配置表所有顶层节点
                    List<CertMetadata> metaDataList = iCertMetaData
                            .selectTopDispinListByCertguid(certInfo.getCertareaguid());
                    //渲染基本信息
                    for (CertMetadata certMetadata : metaDataList) {
                        // 目前默认以写死对应值的方式
                        dataBean.put(certMetadata.getFieldname(), auditOcrIdNew.get(certMetadata.getFieldname()));
                    }
                    dataBean.setRowguid(UUID.randomUUID().toString());
                    dataBean.setCertinfoguid(certInfo.getRowguid());
                    mongodbUtil.update(dataBean);
                    iAuditOcrId.deleteAuditOcrIdByGuid(auditOcrIdNew.getRowguid());
                }
            }
        }
    }
}

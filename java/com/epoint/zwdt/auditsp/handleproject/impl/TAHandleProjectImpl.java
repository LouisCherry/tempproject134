package com.epoint.zwdt.auditsp.handleproject.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.common.util.JsonUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.ConfigServiceImpl;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.zwdt.auditsp.handleproject.api.ITAHandleProject;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;

@Component
@Service
public class TAHandleProjectImpl implements ITAHandleProject
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    // 其他
    private static final String QfWavePreFixUrl = ConfigUtil.getConfigValue("datasyncjdbc", "qfwaveprefixurl");
    // 公共服务
    private static final String QfWavePreFixUrlgg = ConfigUtil.getConfigValue("datasyncjdbc", "qfwaveprefixurlgg");
    // 行政许可
    private static final String QfWavePreFixUrlXK = ConfigUtil.getConfigValue("datasyncjdbc", "qfwaveprefixurlxk");

    @SuppressWarnings({"rawtypes", "unchecked", "unused" })
    @Override
    public AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String taskGuid,
            String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
            String projectguid, String taskCaseguid, String applyerType) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditTaskMaterialCase auditTaskMaterialCaseService = (IAuditTaskMaterialCase) ContainerFactory
                    .getContainInfo().getComponent(IAuditTaskMaterialCase.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
            IAuditOrgaServiceCenter auditOrgaServiceCenterService = (IAuditOrgaServiceCenter) ContainerFactory
                    .getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
            IAuditOrgaArea auditOrgaAreaService = (IAuditOrgaArea) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaArea.class);
            if (auditTask == null) {
                auditTask = new AuditTask();
            }

            if (auditTaskExtension == null) {
                auditTaskExtension = new AuditTaskExtension();
            }

            AuditProject auditproject = new AuditProject();
            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
            auditproject.setOnlineapplyerguid(applyerGuid);
            auditproject.setTaskguid(taskGuid);
            auditproject.setStatus(8);
            auditproject.setPromise_day(auditTask.getPromise_day());
            auditproject.setApplydate(new Date());
            auditproject.setIs_delay(20);
            auditproject.setProjectname(auditTask.getTaskname());
            auditproject.setInsertdate(new Date());
            auditproject.setIs_charge(auditTask.getCharge_flag());
            auditproject.setCharge_when(auditTaskExtension.getCharge_when());
            auditproject.setTasktype(auditTask.getType());
            Integer applyWay = auditTaskExtension.getWebapplytype();
            if (Integer.parseInt("2") == applyWay || Integer.parseInt("4") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("11"));
            }
            else if (Integer.parseInt("1") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("10"));
            }

            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            log.info("centerguid:"+centerGuid);
            if("undefined".equals(centerGuid)){
                log.info("centerguid:"+centerGuid);
            }else{
                auditproject.setCenterguid(centerGuid);
            }

            if ("20".equals(applyerType)) {
                auditproject.setCerttype("22");
            }
            else {
                auditproject.setCerttype("16");
            }

            auditproject.setApplyertype(Integer.parseInt(applyerType));
            auditproject.setApplyeruserguid(applyerGuid);
            auditproject.setApplyername(applyerUserName);
            auditproject.setCertnum(certNum);
            auditproject.setTaskid(auditTask.getTask_id());
            auditproject.setTask_id(auditTask.getTask_id());
            // 设置办件多情形
            Map<String, Integer> caseMap = new HashMap<>(16);
            if (StringUtil.isNotBlank(auditproject) && StringUtil.isNotBlank(taskCaseguid)) {
                auditproject.setTaskcaseguid(taskCaseguid);
                // 查找出多情形配置的材料
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseguid).getResult();
                // 转成map方便查找
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }

            auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());
            String xzAreaCode = "";
            if (StringUtil.isNotBlank(centerGuid)) {
                AuditOrgaServiceCenter auditOrgaServiceCenter = (AuditOrgaServiceCenter) auditOrgaServiceCenterService
                        .findAuditServiceCenterByGuid(centerGuid).getResult();
                if (auditOrgaServiceCenter != null) {
                    String centerAraecode = auditOrgaServiceCenter.getBelongxiaqu();
                    AuditOrgaArea auditOrgaArea = (AuditOrgaArea) auditOrgaAreaService.getAreaByAreacode(centerAraecode)
                            .getResult();
                    if ("3".equals(auditOrgaArea.getCitylevel()) || "4".equals(auditOrgaArea.getCitylevel())) {
                        xzAreaCode = auditOrgaArea.getXiaqucode();
                    }
                }
            }

            if (StringUtil.isNotBlank(xzAreaCode)) {
                auditproject.setCurrentareacode(xzAreaCode);
                auditproject.setAcceptareacode(xzAreaCode);
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);
            AuditOnlineProject auditOnlineProject = new AuditOnlineProject();
            auditOnlineProject.setRowguid(UUID.randomUUID().toString());
            auditOnlineProject.setAreacode(areaCode);
            auditOnlineProject.setSourceguid(projectguid);
            auditOnlineProject.setCenterguid(centerGuid);
            auditOnlineProject.setCreatedate(new Date());
            auditOnlineProject.setApplyerguid(auditproject.getApplyeruserguid());
            auditOnlineProject.setApplyername(auditproject.getApplyername());
            auditOnlineProject.setIs_charge(auditproject.getIs_charge());
            auditOnlineProject.setIs_check(auditproject.getIs_check());
            auditOnlineProject.setIs_evaluat(auditproject.getIs_evaluat());
            auditOnlineProject.setIs_fee(auditproject.getIs_fee());
            auditOnlineProject.setOperatedate(new Date());
            auditOnlineProject.setOuguid(auditproject.getOuguid());
            auditOnlineProject.setOuname(auditproject.getOuname());
            auditOnlineProject.setProjectname(auditproject.getProjectname());
            auditOnlineProject.setTaskguid(auditproject.getTaskguid());
            auditOnlineProject.setIs_fee(0);
            auditOnlineProject.setIs_evaluat(0);
            auditOnlineProject.setType("0");
            auditOnlineProject.setApplyertype(applyerType);
            auditOnlineProject.setStatus(String.valueOf(auditproject.getStatus()));
            auditOnlineProject.setTaskcaseguid(taskCaseguid);
            auditOnlineProject.setTasktype(String.valueOf(auditTask.getType()));
            if (StringUtil.isNotBlank(xzAreaCode)) {
                auditOnlineProject.setCurrentareacode(xzAreaCode);
            }

            IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);
            IAuditTaskMaterial auditTaskMaterialService = (IAuditTaskMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = (IAuditProjectMaterial) ContainerFactory
                    .getContainInfo().getComponent(IAuditProjectMaterial.class);
            ICertConfigExternal iCertConfigExternal = (ICertConfigExternal) ContainerFactory.getContainInfo()
                    .getComponent(ICertConfigExternal.class);
            ICertInfoExternal iCertInfoExternal = (ICertInfoExternal) ContainerFactory.getContainInfo()
                    .getComponent(ICertInfoExternal.class);
            ICertAttachExternal iCertAttachExternal = (ICertAttachExternal) ContainerFactory.getContainInfo()
                    .getComponent(ICertAttachExternal.class);
            IAttachService attachService = (IAttachService) ContainerFactory.getContainInfo()
                    .getComponent(IAttachService.class);
            IJnProjectService iJnProjectService = (IJnProjectService) ContainerFactory.getContainInfo()
                    .getComponent(IJnProjectService.class);
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = (List) auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            List<AuditProjectMaterial> returnmaterials = new ArrayList();
            Iterator var34 = auditTaskMaterials.iterator();

            while (var34.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) var34.next();
                // 当前该条材料是否返回页面上标志
                Boolean flag = false;
                // 先判断材料是否在多情形列表中
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    if (caseMap.containsKey(auditTaskMaterial.getRowguid())
                            || ZwfwConstant.NECESSITY_SET_YES.equals(auditTaskMaterial.getNecessity().toString())) {
                        flag = true;
                    }
                }
                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                String materialname = auditTaskMaterial.getMaterialname();
                String isbigshow = auditTask.getStr("isbigshow");
                String bigshowtype = auditTaskMaterial.getStr("bigshowtype");
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(materialname);
                auditProjectMaterial.setOperatedate(new Date());
                String type = "";
                String materialLevel = "";
                String materialType = "";
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    if ("20".equals(applyerType)) {
                        type = "001";
                    }
                    else if ("10".equals(applyerType)) {
                        type = "002";
                    }

                    List<CertInfo> certInfos = iCertInfoExternal.selectCertByOwner(
                            auditTaskMaterial.getSharematerialguid(), type, "", certNum, false,
                            auditproject.getAreacode(), (Map) null);
                    if (certInfos != null && certInfos.size() > 0) {
                        CertInfo certInfo = (CertInfo) certInfos.get(0);
                        if (certInfo != null) {
                            materialType = certInfo.getMaterialtype();
                            if ("1".equals(materialType)) {
                                materialLevel = certInfo.getCertlevel();
                            }

                            List<JSONObject> attachList = iCertAttachExternal
                                    .getAttachList(certInfo.getCertcliengguid(), auditproject.getAreacode());
                            if (attachList != null && attachList.size() > 0) {
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                if (attachList != null && attachList.size() > 0) {
                                    Iterator var44 = attachList.iterator();

                                    while (var44.hasNext()) {
                                        JSONObject json = (JSONObject) var44.next();
                                        AttachUtil
                                                .saveFileInputStream(UUID.randomUUID().toString(),
                                                        auditProjectMaterial.getCliengguid(),
                                                        json.getString("attachname"), json.getString("contentype"),
                                                        json.getString("cliengtag"), json.getLong("size"),
                                                        iCertAttachExternal.getAttach(json.getString("attachguid"),
                                                                auditproject.getAreacode()),
                                                        (String) null, (String) null);
                                    }
                                }

                                auditProjectMaterial.setStatus(20);
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            }
                            else {
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            }
                        }
                    }
                }

                /*
                 * if ("1".equals(isbigshow) &&
                 * StringUtil.isNotBlank(bigshowtype)) {
                 * List<FrameAttachInfo> attachinfos =
                 * iJnProjectService.getFrameAttachByIdenumberBigType(certNum,
                 * bigshowtype);
                 * if (attachinfos != null && attachinfos.size() > 0) {
                 * auditProjectMaterial.setCliengguid(attachinfos.get(0).
                 * getCliengGuid());
                 * }
                 * }
                 */

                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                auditProjectMaterial.set("materialLevel", materialLevel);
                auditProjectMaterial.set("materialType", materialType);
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    if (flag) {
                        returnmaterials.add(auditProjectMaterial);
                    }
                }
                else {
                    returnmaterials.add(auditProjectMaterial);
                }
            }

            result.setResult(returnmaterials);
        }
        catch (Exception var46) {
            var46.printStackTrace();
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var46));
        }

        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "unused" })
    public AuditCommonResult<List<AuditProjectMaterial>> InitOnlineCompanyProjectReturnMaterials(String taskGuid,
            String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
            String projectguid, String taskCaseguid, String applyerType, String declarerName, String declarerGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditTaskMaterialCase auditTaskMaterialCaseService = (IAuditTaskMaterialCase) ContainerFactory
                    .getContainInfo().getComponent(IAuditTaskMaterialCase.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
            IAuditOrgaServiceCenter auditOrgaServiceCenterService = (IAuditOrgaServiceCenter) ContainerFactory
                    .getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
            IAuditOrgaArea auditOrgaAreaService = (IAuditOrgaArea) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaArea.class);
            if (auditTask == null) {
                auditTask = new AuditTask();
            }

            if (auditTaskExtension == null) {
                auditTaskExtension = new AuditTaskExtension();
            }

            AuditProject auditproject = new AuditProject();
            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
            auditproject.setOnlineapplyerguid(applyerGuid);
            auditproject.setTaskguid(taskGuid);
            auditproject.setStatus(8);
            auditproject.setPromise_day(auditTask.getPromise_day());
            auditproject.setApplydate(new Date());
            auditproject.setIs_delay(20);
            auditproject.setProjectname(auditTask.getTaskname());
            auditproject.setInsertdate(new Date());
            auditproject.setIs_charge(auditTask.getCharge_flag());
            auditproject.setCharge_when(auditTaskExtension.getCharge_when());
            auditproject.setTasktype(auditTask.getType());
            Integer applyWay = auditTaskExtension.getWebapplytype();
            if (Integer.parseInt("2") == applyWay || Integer.parseInt("4") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("11"));
            }
            else {
                auditproject.setApplyway(Integer.parseInt("10"));
            }

            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            log.info("centerguid:"+centerGuid);
            if("undefined".equals(centerGuid)){
                log.info("centerguid:"+centerGuid);
            }else{
                auditproject.setCenterguid(centerGuid);
            }
            if ("20".equals(applyerType)) {
                auditproject.setCerttype("22");
            }
            else {
                auditproject.setCerttype("16");
            }

            auditproject.setApplyertype(Integer.parseInt(applyerType));
            auditproject.setApplyeruserguid(applyerGuid);
            auditproject.setApplyername(applyerUserName);
            auditproject.setCertnum(certNum);
            auditproject.setTaskid(auditTask.getTask_id());
            auditproject.setTask_id(auditTask.getTask_id());
            // 设置办件多情形
            Map<String, Integer> caseMap = new HashMap<>(16);
            if (StringUtil.isNotBlank(auditproject) && StringUtil.isNotBlank(taskCaseguid)) {
                auditproject.setTaskcaseguid(taskCaseguid);
                // 查找出多情形配置的材料
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseguid).getResult();
                // 转成map方便查找
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }

            auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());
            String xzAreaCode = "";
            if (StringUtil.isNotBlank(centerGuid)) {
                AuditOrgaServiceCenter auditOrgaServiceCenter = (AuditOrgaServiceCenter) auditOrgaServiceCenterService
                        .findAuditServiceCenterByGuid(centerGuid).getResult();
                if (auditOrgaServiceCenter != null) {
                    String centerAraecode = auditOrgaServiceCenter.getBelongxiaqu();
                    AuditOrgaArea auditOrgaArea = (AuditOrgaArea) auditOrgaAreaService.getAreaByAreacode(centerAraecode)
                            .getResult();
                    if ("3".equals(auditOrgaArea.getCitylevel()) || "4".equals(auditOrgaArea.getCitylevel())) {
                        xzAreaCode = auditOrgaArea.getXiaqucode();
                    }
                }
            }

            if (StringUtil.isNotBlank(xzAreaCode)) {
                auditproject.setCurrentareacode(xzAreaCode);
                auditproject.setAcceptareacode(xzAreaCode);
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);
            IAuditTaskMaterial auditTaskMaterialService = (IAuditTaskMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = (IAuditProjectMaterial) ContainerFactory
                    .getContainInfo().getComponent(IAuditProjectMaterial.class);
            ICertConfigExternal iCertConfigExternal = (ICertConfigExternal) ContainerFactory.getContainInfo()
                    .getComponent(ICertConfigExternal.class);
            ICertInfoExternal iCertInfoExternal = (ICertInfoExternal) ContainerFactory.getContainInfo()
                    .getComponent(ICertInfoExternal.class);
            IAttachService attachService = (IAttachService) ContainerFactory.getContainInfo()
                    .getComponent(IAttachService.class);
            IJnProjectService iJnProjectService = (IJnProjectService) ContainerFactory.getContainInfo()
                    .getComponent(IJnProjectService.class);
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = (List) auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            List<AuditProjectMaterial> returnmaterials = new ArrayList();
            Iterator var33 = auditTaskMaterials.iterator();

            while (var33.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) var33.next();
                Boolean flag = false;
                // 先判断材料是否在多情形列表中
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    if (caseMap.containsKey(auditTaskMaterial.getRowguid())
                            || ZwfwConstant.NECESSITY_SET_YES.equals(auditTaskMaterial.getNecessity().toString())) {
                        flag = true;
                    }
                }

                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                String materialname = auditTaskMaterial.getMaterialname();
                String isbigshow = auditTask.getStr("isbigshow");
                String bigshowtype = auditTaskMaterial.getStr("bigshowtype");
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(materialname);
                auditProjectMaterial.setOperatedate(new Date());
                String type = "";
                String materialLevel = "";
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    if ("20".equals(applyerType)) {
                        type = "001";
                    }
                    else if ("10".equals(applyerType)) {
                        type = "002";
                    }

                    List<CertInfo> certInfos = iCertInfoExternal.selectCertByOwner(
                            auditTaskMaterial.getSharematerialguid(), type, "", certNum, false,
                            auditproject.getAreacode(), (Map) null);
                    if (certInfos != null && certInfos.size() > 0) {
                        CertInfo certInfo = (CertInfo) certInfos.get(0);
                        if (certInfo != null) {
                            String materialType = certInfo.getMaterialtype();
                            if ("1".equals(materialType)) {
                                materialLevel = certInfo.getCertlevel();
                            }

                            int count = attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid());
                            if (count > 0) {
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                attachService.copyAttachByClientGuid(certInfo.getCertcliengguid(), "从共享材料库引用",
                                        (String) null, auditProjectMaterial.getCliengguid());
                                auditProjectMaterial.setStatus(20);
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            }
                            else {
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            }
                        }
                    }
                }

                /*
                 * if ("1".equals(isbigshow) &&
                 * StringUtil.isNotBlank(bigshowtype)) {
                 * List<FrameAttachInfo> attachinfos =
                 * iJnProjectService.getFrameAttachByIdenumberBigType(certNum,
                 * bigshowtype);
                 * if (attachinfos != null && attachinfos.size() > 0) {
                 * auditProjectMaterial.setCliengguid(attachinfos.get(0).
                 * getCliengGuid());
                 * }
                 * }
                 * 
                 * 
                 * if ("1".equals(isbigshow) &&
                 * StringUtil.isNotBlank(bigshowtype)) {
                 * auditProjectMaterial.set("companyshow", bigshowtype);
                 * }
                 */

                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                auditProjectMaterial.set("materialLevel", materialLevel);
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    if (flag) {
                        returnmaterials.add(auditProjectMaterial);
                    }
                }
                else {
                    returnmaterials.add(auditProjectMaterial);
                }
            }

            result.setResult(returnmaterials);
        }
        catch (Exception var43) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var43));
        }

        return result;
    }

    // 网厅办件提交的时候调取
    // 获取浪潮网厅的办件编码
    public Record getFlowsn(AuditTask audittask, Record record) {
        // 获取事项ID
        String unid = getunidbyTaskid(audittask.getTask_id());
        // 请求接口获取受理编码
        if (StringUtil.isNotBlank(unid)) {
            String zwdturl = new ConfigServiceImpl().getFrameConfigValue("zwdtflowsnurl");
            JSONObject submit = new JSONObject();
            JSONObject json = new JSONObject();
            json.put("unid", unid);
            submit.put("params", json);
            submit.put("token", "Epoint_WebSerivce_**##0601");
            String resultsign = TARequestUtil.sendPostInner(zwdturl, submit.toJSONString(), "", "");

            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject jsonobject = JSONObject.parseObject(resultsign);
                JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
                if ("200".equals(jsonstatus.get("code").toString())) {
                    JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
                    if ("1".equals(jsoncustom.get("code").toString())) {
                        String flowsn = jsoncustom.getString("flowsn");
                        record.set("receiveNum", flowsn);
                    }
                    else
                        System.out.println("========================>获取受理编码失败！");
                }
                else {
                    System.out.println("========================>获取受理编码失败！");
                }
            }
            else {
                System.out.println("========================>获取受理编码失败！");
            }
        }
        return record;
    }

    public static JSONObject createReceiveNum(String paramsGet, String shenpilb) throws IOException {
        String url = "";
        if ("11".equals(shenpilb)) {
            url = QfWavePreFixUrlgg;
        }
        else if ("01".equals(shenpilb)) {
            url = QfWavePreFixUrlXK;
        }
        else {
            url = QfWavePreFixUrl;
        }
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = url + "web/approval/createReceiveNum";
        // 开始请求接口
        try {
            responseJsonObj = getHttp(interFaceUrl, paramsGet);
        }
        catch (JSONException e) {
        }
        return responseJsonObj;
    }

    public static JSONObject getHttp(String url, String params2Get) throws IOException {
        JSONObject responseJsonObj = new JSONObject();
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("UTF-8");
        GetMethod getMethod = new GetMethod(url + params2Get);
        httpClient.executeMethod(getMethod);
        responseJsonObj = (JSONObject) JSONObject.parse(getMethod.getResponseBodyAsString());
        return responseJsonObj;
    }

    public String getunidbyTaskid(String taskid) {
        String sql = "select unid from audit_task where TASK_ID=? AND IFNULL(IS_HISTORY,0)=0"
                + " AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0";
        return CommonDao.getInstance().queryString(sql, taskid);
    }
}

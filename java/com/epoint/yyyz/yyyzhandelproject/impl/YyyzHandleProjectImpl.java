package com.epoint.yyyz.yyyzhandelproject.impl;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditonlineproject.auditapplyjslog.inter.IAuditApplyJsLog;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectcharge.inter.IAuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectchargedetail.inter.IAuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyRegister;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.auditresource.individual.inter.IAuditIndividual;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StarProjectInteractUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handledoc.inter.IHandleDoc;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handleproject.service.HandleProjectService;
import com.epoint.composite.auditsp.handleproject.service.TAHandleProjectService;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.yyyz.yyyzhandelproject.api.IYyyzHandleProject;

@Component
@Service
public class YyyzHandleProjectImpl implements IYyyzHandleProject
{
	
	private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public AuditCommonResult<String> InitProject(String taskGuid, String projectGuid, String operateUserName,
            String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum,
            String qno) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(20);
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setOuname(auditTask.getOuname());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setIf_express("0");
                auditproject.setCenterguid(centerGuid);
                String areacode = auditTask.getAreacode();
                auditproject.setAreacode(areacode);
                IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                    auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());
                    auditproject.setIf_express(auditTaskExtension.getIf_express());
                }

                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
                if ("10".equals(auditTask.getApplyertype())) {
                    auditproject.setApplyertype(Integer.parseInt("10"));
                    auditproject.setCerttype("14");
                }
                else {
                    auditproject.setApplyertype(Integer.parseInt("20"));
                    auditproject.setCerttype("22");
                }
            }

            auditproject.setApplydate(new Date());
            auditproject.setApplyway(Integer.parseInt("20"));
            auditproject.setIs_test(Integer.parseInt("0"));
            auditproject.setIs_delay(20);
            if (StringUtil.isNotBlank(qno)) {
                auditproject.setCerttype("22");
                auditproject.setCertnum(certNum);
                IAuditQueueUserinfo userinfoservice = (IAuditQueueUserinfo) ContainerFactory.getContainInfo()
                        .getComponent(IAuditQueueUserinfo.class);
                Record userinfo = (Record) userinfoservice.getUserByIdentityCardNum(certNum).getResult();
                if (userinfo != null) {
                    auditproject.setApplyername((String) userinfo.get("DisplayName"));
                    auditproject.setAddress((String) userinfo.get("address"));
                    auditproject.setContactmobile((String) userinfo.get("Mobile"));
                }

                IAuditQueue queueservice = (IAuditQueue) ContainerFactory.getContainInfo()
                        .getComponent(IAuditQueue.class);
                queueservice.updateQNOProject(rowGuid, qno, centerGuid);
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);
            IAuditTaskMaterial auditTaskMaterialService = (IAuditTaskMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = (IAuditProjectMaterial) ContainerFactory
                    .getContainInfo().getComponent(IAuditProjectMaterial.class);
            List<AuditTaskMaterial> auditTaskMaterials = (List) auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(auditTask == null ? "" : auditTask.getRowguid(), true)
                    .getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            Iterator var20 = auditTaskMaterials.iterator();

            while (var20.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) var20.next();
                auditProjectMaterial.clear();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
            }

            result.setResult(rowGuid);
        }
        catch (Exception var22) {
            var22.printStackTrace();
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var22));
        }

        return result;
    }

    public AuditCommonResult<AuditProject> InitOnlineProject(String taskGuid, String centerGuid, String areaCode,
            String applyerGuid, String applyerUserName, String certNum) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
            AuditProject auditproject = new AuditProject();
            String projectguid = UUID.randomUUID().toString();
            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
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
            auditproject.setApplyway(Integer.parseInt("10"));
            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
            auditproject.setCerttype("22");
            auditproject.setApplyertype(20);
            auditproject.setApplyeruserguid(applyerGuid);
            auditproject.setApplyername(applyerUserName);
            auditproject.setCertnum(certNum);
            auditproject.setTask_id(auditTask.getTask_id());
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
            auditOnlineProject.setStatus(String.valueOf(auditproject.getStatus()));
            IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);
            IAuditTaskMaterial auditTaskMaterialService = (IAuditTaskMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = (IAuditProjectMaterial) ContainerFactory
                    .getContainInfo().getComponent(IAuditProjectMaterial.class);
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = (List) auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            Iterator var21 = auditTaskMaterials.iterator();

            while (var21.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) var21.next();
                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterial.setOperatedate(new Date());
                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
            }

            result.setResult(auditproject);
        }
        catch (Exception var24) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var24));
        }

        return result;
    }

    public AuditCommonResult<String> saveProject(AuditProject auditProject) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            if (auditProject.getStatus() <= 24 && auditProject.getStatus() >= 20) {
                auditProject.setStatus(24);
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
            String rowGuid_GR = UUID.randomUUID().toString();
            String rowGuid_QY = UUID.randomUUID().toString();
            if ("20".equals(auditProject.getApplyertype().toString())) {
                IAuditIndividual individualService = (IAuditIndividual) ContainerFactory.getContainInfo()
                        .getComponent(IAuditIndividual.class);
                AuditRsIndividualBaseinfo auditindividual = (AuditRsIndividualBaseinfo) individualService
                        .getAuditRsIndividualBaseinfoByIDNumber(auditProject.getCertnum()).getResult();
                AuditRsIndividualBaseinfo auditprojectindividual = (AuditRsIndividualBaseinfo) individualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                if (auditindividual != null) {
                    auditindividual.setApplyerinfosource(Integer.parseInt("20"));
                    auditindividual.setIdnumber(auditProject.getCertnum());
                    auditindividual.setClientname(auditProject.getApplyername());
                    auditindividual.setContactperson(auditProject.getContactperson());
                    auditindividual.setContactphone(auditProject.getContactphone());
                    auditindividual.setContactmobile(auditProject.getContactmobile());
                    auditindividual.setContactpostcode(auditProject.getContactpostcode());
                    auditindividual.setContactemail(auditProject.getContactemail());
                    auditindividual.setContactfax(auditProject.getContactfax());
                    auditindividual.setDeptaddress(auditProject.getAddress());
                    auditindividual.setContactcertnum(auditProject.getContactcertnum());
                    individualService.updateAuditRsIndividualBaseinfo(auditindividual);
                    rowGuid_GR = auditindividual.getRowguid();
                }
                else if (auditprojectindividual != null) {
                    auditprojectindividual.setIs_history("0");
                }
                else {
                    new AuditRsIndividualBaseinfo();
                    String personid = UUID.randomUUID().toString();
                    auditindividual = new AuditRsIndividualBaseinfo();
                    if (StringUtil.isNotBlank(auditProject.getApplyeruserguid())) {
                        auditindividual.setRowguid(auditProject.getApplyeruserguid());
                    }
                    else {
                        auditindividual.setRowguid(rowGuid_GR);
                    }

                    auditindividual.setPersonid(personid);
                    auditindividual.setApplyerinfosource(Integer.parseInt("20"));
                    auditindividual.setIdnumber(auditProject.getCertnum());
                    auditindividual.setClientname(auditProject.getApplyername());
                    auditindividual.setContactperson(auditProject.getContactperson());
                    auditindividual.setContactphone(auditProject.getContactphone());
                    auditindividual.setContactmobile(auditProject.getContactmobile());
                    auditindividual.setContactpostcode(auditProject.getContactpostcode());
                    auditindividual.setContactemail(auditProject.getContactemail());
                    auditindividual.setContactfax(auditProject.getContactfax());
                    auditindividual.setDeptaddress(auditProject.getAddress());
                    auditindividual.setContactcertnum(auditProject.getContactcertnum());
                    auditindividual.setIs_history("0");
                    auditindividual.setVersion(1);
                    auditindividual.setVersiontime(new Date());
                    individualService.addAuditRsIndividualBaseinfo(auditindividual);
                    rowGuid_GR = auditindividual.getRowguid();
                }

                auditProject.setApplyeruserguid(rowGuid_GR);
            }
            else {
                IAuditRsCompanyLegal rsCompanyLegalService = (IAuditRsCompanyLegal) ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyLegal.class);
                IAuditRsCompanyRegister rsCompanyRegisterService = (IAuditRsCompanyRegister) ContainerFactory
                        .getContainInfo().getComponent(IAuditRsCompanyRegister.class);
                String certtype = auditProject.getCerttype();
                IAuditRsCompanyBaseinfo companyService = (IAuditRsCompanyBaseinfo) ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyBaseinfo.class);
                AuditRsCompanyBaseinfo auditcompany;
                AuditRsCompanyLegal auditRsCompanyLegal;
                AuditRsCompanyRegister auditRsCompanyRegister;
                String companyid;
                if ("14".equals(certtype)) {
                    auditcompany = (AuditRsCompanyBaseinfo) companyService
                            .getCompanyByOneField("organcode", auditProject.getCertnum()).getResult();
                    if (auditcompany != null) {
                        auditcompany.setOrgancode(auditProject.getCertnum());
                        auditcompany.setOrganname(auditProject.getApplyername());
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());
                        companyService.updateAuditRsCompanyBaseinfo(auditcompany);
                        auditRsCompanyLegal = (AuditRsCompanyLegal) rsCompanyLegalService
                                .getAuditRsCompanyLegalByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyLegal != null) {
                            auditRsCompanyLegal.setContactperson(auditProject.getContactperson());
                            auditRsCompanyLegal.setContactfax(auditProject.getContactfax());
                            auditRsCompanyLegal.setContactemail(auditProject.getContactemail());
                            auditRsCompanyLegal.setContactmobile(auditProject.getContactmobile());
                            auditRsCompanyLegal.setContactpostcode(auditProject.getContactpostcode());
                            auditRsCompanyLegal.setLegalname(auditProject.getLegal());
                            rsCompanyLegalService.updateAuditRsCompanyLegal(auditRsCompanyLegal);
                        }

                        auditRsCompanyRegister = (AuditRsCompanyRegister) rsCompanyRegisterService
                                .getAuditRsCompanyRegisterByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyRegister != null) {
                            auditRsCompanyRegister.setContactphone(auditProject.getContactphone());
                            auditRsCompanyRegister.setBusinessaddress(auditProject.getAddress());
                            rsCompanyRegisterService.updateAuditRsCompanyRegister(auditRsCompanyRegister);
                        }

                        rowGuid_QY = auditcompany.getRowguid();
                    }
                    else {
                        companyid = UUID.randomUUID().toString();
                        auditcompany = new AuditRsCompanyBaseinfo();
                        auditcompany.setRowguid(UUID.randomUUID().toString());
                        auditcompany.setOrgancode(auditProject.getCertnum());
                        auditcompany.setOrganname(auditProject.getApplyername());
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());
                        auditcompany.setCompanyid(companyid);
                        auditcompany.setVersion(1);
                        auditcompany.setVersiontime(new Date());
                        auditcompany.setIs_history("0");
                        companyService.addAuditRsCompanyBaseinfo(auditcompany);
                        AuditRsCompanyLegal auditRsCompanyLegal1 = new AuditRsCompanyLegal();
                        auditRsCompanyLegal1.setLegalname(auditProject.getLegal());
                        auditRsCompanyLegal1.setOrgancode(auditProject.getCertnum());
                        auditRsCompanyLegal1.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyLegal1.setContactperson(auditProject.getContactperson());
                        auditRsCompanyLegal1.setContactfax(auditProject.getContactfax());
                        auditRsCompanyLegal1.setContactemail(auditProject.getContactemail());
                        auditRsCompanyLegal1.setContactmobile(auditProject.getContactmobile());
                        auditRsCompanyLegal1.setContactpostcode(auditProject.getContactpostcode());
                        auditRsCompanyLegal1.setIs_history("0");
                        auditRsCompanyLegal1.setCompanyid(companyid);
                        auditRsCompanyLegal1.setVersion(1);
                        auditRsCompanyLegal1.setVersiontime(new Date());
                        rsCompanyLegalService.addAuditRsCompanyLegal(auditRsCompanyLegal1);
                        AuditRsCompanyRegister auditRsCompanyRegister1 = new AuditRsCompanyRegister();
                        auditRsCompanyRegister1.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister1.setContactphone(auditProject.getContactphone());
                        auditRsCompanyRegister1.setBusinessaddress(auditProject.getAddress());
                        auditRsCompanyRegister1.setOrgancode(auditProject.getCertnum());
                        auditRsCompanyRegister1.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister1.setIs_history("0");
                        auditRsCompanyRegister1.setCompanyid(companyid);
                        auditRsCompanyRegister1.setVersion(1);
                        auditRsCompanyRegister1.setVersiontime(new Date());
                        rsCompanyRegisterService.addAuditRsCompanyRegister(auditRsCompanyRegister1);
                        rowGuid_QY = auditcompany.getRowguid();
                    }
                }
                else {
                    auditcompany = null;
                    if ("12".equals(certtype)) {
                        auditcompany = (AuditRsCompanyBaseinfo) companyService
                                .getCompanyByOneField("businesslicense", auditProject.getCertnum()).getResult();
                    }
                    else {
                        auditcompany = (AuditRsCompanyBaseinfo) companyService
                                .getCompanyByOneField("creditcode", auditProject.getCertnum()).getResult();
                    }

                    if (auditcompany != null) {
                        auditcompany.setOrganname(auditProject.getApplyername());
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());
                        companyService.updateAuditRsCompanyBaseinfo(auditcompany);
                        auditRsCompanyLegal = (AuditRsCompanyLegal) rsCompanyLegalService
                                .getAuditRsCompanyLegalByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyLegal != null) {
                            auditRsCompanyLegal.setContactperson(auditProject.getContactperson());
                            auditRsCompanyLegal.setContactfax(auditProject.getContactfax());
                            auditRsCompanyLegal.setContactemail(auditProject.getContactemail());
                            auditRsCompanyLegal.setContactmobile(auditProject.getContactmobile());
                            auditRsCompanyLegal.setContactpostcode(auditProject.getContactpostcode());
                            auditRsCompanyLegal.setLegalname(auditProject.getLegal());
                            rsCompanyLegalService.updateAuditRsCompanyLegal(auditRsCompanyLegal);
                        }

                        auditRsCompanyRegister = (AuditRsCompanyRegister) rsCompanyRegisterService
                                .getAuditRsCompanyRegisterByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyRegister != null) {
                            auditRsCompanyRegister.setContactphone(auditProject.getContactphone());
                            auditRsCompanyRegister.setBusinessaddress(auditProject.getAddress());
                            rsCompanyRegisterService.updateAuditRsCompanyRegister(auditRsCompanyRegister);
                        }

                        rowGuid_QY = auditcompany.getRowguid();
                    }
                    else {
                        companyid = UUID.randomUUID().toString();
                        auditcompany = new AuditRsCompanyBaseinfo();
                        auditcompany.setRowguid(rowGuid_QY);
                        auditcompany.setCreditcode(auditProject.getCertnum());
                        auditcompany.setOrganname(auditProject.getApplyername());
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());
                        auditcompany.setCompanyid(companyid);
                        auditcompany.setVersion(1);
                        auditcompany.setVersiontime(new Date());
                        auditcompany.setIs_history("0");
                        companyService.addAuditRsCompanyBaseinfo(auditcompany);
                        String is_yangzhou_sys = ConfigUtil.getConfigValue("is_yangzhou_sys");
                        if (StringUtil.isNotBlank(is_yangzhou_sys) && is_yangzhou_sys.equals("1")) {
                            auditcompany.set("totalinvestment", auditProject.get("totalinvestment"));
                            auditcompany.set("companytype", auditProject.get("companytype"));
                            auditcompany.set("compycontactaddress", auditProject.get("compycontactaddress"));
                            auditcompany.set("registeredcapital", auditProject.get("registeredcapital"));
                            auditcompany.set("managementtype", auditProject.get("managementtype"));
                            auditcompany.set("registeraddress", auditProject.get("compyjyaddress"));
                            auditcompany.set("admindivision", auditProject.get("admindivision"));
                            auditcompany.set("compycontactphone", auditProject.get("compycontactphone"));
                        }

                        companyService.updateAuditRsCompanyBaseinfo(auditcompany);
                        AuditRsCompanyLegal auditRsCompanyLegal1 = new AuditRsCompanyLegal();
                        auditRsCompanyLegal1.setLegalname(auditProject.getLegal());
                        auditRsCompanyLegal1.setCreditcode(auditProject.getCertnum());
                        auditRsCompanyLegal1.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyLegal1.setContactperson(auditProject.getContactperson());
                        auditRsCompanyLegal1.setContactfax(auditProject.getContactfax());
                        auditRsCompanyLegal1.setContactemail(auditProject.getContactemail());
                        auditRsCompanyLegal1.setContactmobile(auditProject.getContactmobile());
                        auditRsCompanyLegal1.setContactpostcode(auditProject.getContactpostcode());
                        auditRsCompanyLegal1.setIs_history("0");
                        auditRsCompanyLegal1.setCompanyid(companyid);
                        auditRsCompanyLegal1.setVersion(1);
                        auditcompany.setVersiontime(new Date());
                        rsCompanyLegalService.addAuditRsCompanyLegal(auditRsCompanyLegal1);
                        AuditRsCompanyRegister auditRsCompanyRegister1 = new AuditRsCompanyRegister();
                        auditRsCompanyRegister1.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister1.setContactphone(auditProject.getContactphone());
                        auditRsCompanyRegister1.setBusinessaddress(auditProject.getAddress());
                        auditRsCompanyRegister1.setCreditcode(auditProject.getCertnum());
                        auditRsCompanyRegister1.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister1.setIs_history("0");
                        auditRsCompanyRegister1.setCompanyid(companyid);
                        auditRsCompanyRegister1.setVersion(1);
                        auditRsCompanyRegister1.setVersiontime(new Date());
                        rsCompanyRegisterService.addAuditRsCompanyRegister(auditRsCompanyRegister1);
                        rowGuid_QY = auditcompany.getRowguid();
                    }
                }

                auditProject.setApplyeruserguid(rowGuid_QY);
            }

            HandleProjectService projectService = new HandleProjectService();
            if (auditProject.getStatus() >= 26) {
                if (StringUtil.isBlank(auditProject.getFlowsn())) {
//                    projectService.checkFlowSN(auditProject);
                    String unid = auditTask.getStr("unid");
                    String resultflowsn = FlowsnUtil.createReceiveNum(unid,auditTask.getRowguid());
                    auditProject.setFlowsn(resultflowsn);

                }
                auditProjectService.updateProject(auditProject);
                if ("10".equals(auditProject.getApplyway().toString())
                        || "11".equals(auditProject.getApplyway().toString())) {
                    IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                            .getComponent(IAuditOnlineProject.class);
                    AuditOnlineProject auditOnlineProject = (AuditOnlineProject) iAuditOnlineProject
                            .getOnlineProjectByTaskGuid(auditProject.getRowguid(), auditProject.getTaskguid())
                            .getResult();
                    if (StringUtil.isBlank(auditOnlineProject.getFlowsn())) {
                        auditOnlineProject.setFlowsn(auditProject.getFlowsn());
                        iAuditOnlineProject.updateProject(auditOnlineProject);
                    }
                }
            }
            else {
                auditProjectService.updateProject(auditProject);
            }
        }
        catch (Exception var15) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var15));
        }

        return result;
    }

    public AuditCommonResult<String> handleAccept(AuditProject auditProject, String workItemGuid,
            String operateUserName, String operateUserGuid, String windowName, String windowGuid) {
        return this.handleAccept(auditProject, workItemGuid, operateUserName, operateUserGuid, windowName, windowGuid,
                "0");
    }

    public AuditCommonResult<String> handleAccept(AuditProject auditProject, String workItemGuid,
            String operateUserName, String operateUserGuid, String windowName, String windowGuid, String pizhun) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService
                    .getAuditTaskByGuid(auditProject.getTaskguid(), auditProject.getAcceptareacode(), true).getResult();
            IAuditApplyJsLog iAuditApplyJsLog = (IAuditApplyJsLog) ContainerFactory.getContainInfo()
                    .getComponent(IAuditApplyJsLog.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IOuService ouService = (IOuService) ContainerFactory.getContainInfo().getComponent(IOuService.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            String operatetype = "30";
            if ("11".equals(String.valueOf(auditProject.getApplyway()))) {
                operatetype = "29";
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "23", operateUserGuid, operateUserName, workItemGuid);
            if ("1".equals(String.valueOf(auditProject.getTasktype())) && !"1".equals(auditTask.getJbjmode())) {
                auditProject.setStatus(35);
            }
            else {
                auditProject.setStatus(30);
            }

            auditProject.setAcceptuserdate(new Date());
            auditProject.setAcceptuserguid(operateUserGuid);
            auditProject.setAcceptusername(operateUserName);
            auditProject.setIs_pause(0);
            if (StringUtil.isBlank(auditProject.getWindowname())) {
                auditProject.setWindowname(windowName);
            }

            if (StringUtil.isBlank(auditProject.getWindowguid())) {
                auditProject.setWindowguid(windowGuid);
            }

            String ouname = "";
            if (StringUtil.isBlank(auditProject.getOuname())) {
                ouname = ouService.getOuNameByUserGuid(operateUserGuid);
                auditProject.setOuname(ouname);
            }

            IAuditOnlineProject auditOnlineProjectService;
            String isjS;
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("ouguid=", auditProject.getOuguid());
                updateFieldMap.put("ouname=", auditProject.getOuname());
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                isjS = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        isjS = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            auditOnlineProjectService = null;
            Date shouldEndDate;
            int ts;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                IAuditOrgaWorkingDay workingDayService = (IAuditOrgaWorkingDay) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOrgaWorkingDay.class);
                shouldEndDate = (Date) workingDayService.getWorkingDayWithOfficeSet(auditProject.getCenterguid(),
                        new Date(), auditTask.getPromise_day()).getResult();
                ts = auditTask.getPromise_day() * 24 * 60;
            }
            else {
                shouldEndDate = null;
                ts = 0;
            }

            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
            IAuditProjectSparetime sparetimeService = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            if (!"1".equals(auditProject.getTasktype().toString())) {
                sparetimeService.addSpareTimeByProjectGuid(auditProject.getRowguid(), ts, auditProject.getAreacode(),
                        auditProject.getCenterguid());
            }

            IAuditProjectNumber projectNumberService = (IAuditProjectNumber) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectNumber.class);
            projectNumberService.addProjectNumber(auditProject, true, operateUserGuid, operateUserName);
            if (!"1".equals(String.valueOf(auditProject.getTasktype())) && 1 == auditTaskExtension.getIs_simulation()) {
                isjS = "";
                IAuditTaskResult auditResultService = (IAuditTaskResult) ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskResult.class);
                AuditTaskResult auditresult = (AuditTaskResult) auditResultService
                        .getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if ((1 != auditTask.getCharge_flag()
                        || !"2".equals(String.valueOf(auditTaskExtension.getCharge_when())))
                        && (auditresult == null || auditresult.getResulttype() == 99)) {
                    IHandleConfig handleConfigServic = (IHandleConfig) ContainerFactory.getContainInfo()
                            .getComponent(IHandleConfig.class);
                    isjS = (String) handleConfigServic
                            .getFrameConfig("AS_SIMULATION_TO_STATUS", auditProject.getCenterguid()).getResult();
                    isjS = StringUtil.isBlank(isjS) ? String.valueOf(90) : isjS;
                }
                else {
                    isjS = String.valueOf(50);
                }

                auditProject.setStatus(Integer.parseInt(isjS));
            }

            if ("1".equals(pizhun)) {
                if (!"1".equals(String.valueOf(auditTask.getType()))) {
                    ;
                }

                auditProject.setStatus(50);
                if ("1".equals(String.valueOf(auditTask.getType()))
                        || "2".equals(String.valueOf(auditTask.getType()))) {
                    auditProject.setBanjieresult(40);
                    auditProject.setBanwandate(new Date());
                }
            }

            this.saveProject(auditProject);
            isjS = ConfigUtil.getConfigValue("epointframe", "isJSTY");
            if ("1".equals(isjS)) {
                String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                if (StringUtil.isNotBlank(url)) {
                    url = url + "/epointzwdt/pages/onlinedeclaration_js/declarationinfodetail?declareid="
                            + auditTask.getTask_id() + "&centerguid=" + auditProject.getCenterguid() + "&taskguid="
                            + auditTask.getRowguid() + "&flowsn=" + auditProject.getFlowsn();
                    Record record = StarProjectInteractUtil.updateOrInsertStar(auditProject.getFlowsn(), "10",
                            StringUtil.isBlank(auditTask.getDept_yw_reg_no()) ? auditTask.getItem_id()
                                    : auditTask.getDept_yw_reg_no(),
                            auditTask.getTaskname(), auditProject.getApplyername(), auditProject.getCertnum(), "查看办件",
                            url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                }
            }

            IAuditProjectMaterial projectMaterialService = (IAuditProjectMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            projectMaterialService.updateAllAuditStatus(auditProject.getRowguid());
            String ownGuid = "";
            IAuditIndividual auditIndividualService = (IAuditIndividual) ContainerFactory.getContainInfo()
                    .getComponent(IAuditIndividual.class);
            IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService = (IAuditRsCompanyBaseinfo) ContainerFactory
                    .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
            if ("10".equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = (AuditRsCompanyBaseinfo) auditRsCompanyBaseinfoService
                        .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsCompanyBaseinfo.getCompanyid();
            }
            else if ("20".equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = (AuditRsIndividualBaseinfo) auditIndividualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsIndividualBaseinfo.getPersonid();
            }

            if (StringUtil.isBlank(ownGuid)) {
                ownGuid = auditProject.getApplyeruserguid();
            }

            FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
            IHandleRSMaterial handleRSMaterialService = (IHandleRSMaterial) ContainerFactory.getContainInfo()
                    .getComponent(IHandleRSMaterial.class);
            handleRSMaterialService.insertShareMaterial(auditProject.getTaskguid(), auditProject.getRowguid(), ownGuid,
                    operateUserGuid, operateUserName, auditProject.getApplyername(), auditProject.getCertnum(),
                    auditProject.getApplyertype(), auditProject.getCerttype(), auditProject.getAreacode(),
                    (String) ou.get("orgcode"));
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_sl())) {
                    content = auditTaskExtension.getNotify_sl();
                    content = content.replace("[#=OuName#]", auditTask.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var33) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var33));
        }

        return result;
    }

    public AuditCommonResult<String> handleYstgAddreason(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String reason) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProject.setStatus(16);
            auditProject.setBubandate(new Date());
            auditProject.setYushendate(new Date());
            this.saveProject(auditProject);
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap();
                Map<String, String> updateDateFieldMap = new HashMap();
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "10", operateUserGuid, operateUserName, "", reason);
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_ys())) {
                    content = auditTaskExtension.getNotify_ys();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var13) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var13));
        }

        return result;
    }

    public AuditCommonResult<String> handleYstg(AuditProject auditProject, String operateUserName,
            String operateUserGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProject.setStatus(16);
            auditProject.setBubandate(new Date());
            auditProject.setYushendate(new Date());
            this.saveProject(auditProject);
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
                if (auditProject.getApplyertype().toString().equals("10")) {
                    Map<String, String> updateFieldMap2 = new HashMap(16);
                    Map<String, String> updateDateFieldMap2 = new HashMap(16);
                    updateFieldMap2.put("status=", auditProject.getStatus().toString());
                    sql.clear();
                    sql.eq("sourceguid", auditProject.getRowguid());
                    if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                        sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                    }

                    auditOnlineProjectService.updateOnlineProject(updateFieldMap2, updateDateFieldMap2, sql.getMap());
                }
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "10", operateUserGuid, operateUserName);
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_ys())) {
                    content = auditTaskExtension.getNotify_ys();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var12) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var12));
        }

        return result;
    }

    public AuditCommonResult<String> handleYsdh(AuditProject auditProject, String operateUserName,
            String operateUserGuid) {
        AuditCommonResult<String> result = new AuditCommonResult();
        IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

        try {
            auditProject.setStatus(14);
            auditProject.setBubandate(new Date());
            project.updateProject(auditProject);
            IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("status=", String.valueOf(14));
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "11", operateUserGuid, operateUserName);
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_nys())) {
                    content = auditTaskExtension.getNotify_nys();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var14) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var14));
        }

        return result;
    }

    public AuditCommonResult<String> handleYsdhAddreason(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult();
        IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

        try {
            auditProject.setStatus(14);
            auditProject.setBubandate(new Date());
            project.updateProject(auditProject);
            String applyerguid = null;
            IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            if (auditProject.getApplyertype().toString().equals("10")) {
                IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                        .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                        .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                if (auditrscompanybaseinfo != null) {
                    applyerguid = auditrscompanybaseinfo.getCompanyid();
                }
            }

            Map<String, String> updateFieldMap = new HashMap();
            updateFieldMap.put("status=", String.valueOf(14));
            updateFieldMap.put("backreason=", reason);
            Map<String, String> conditionMap = new HashMap();
            conditionMap.put("sourceguid=", auditProject.getRowguid());
            if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                conditionMap.put("applyerguid=", auditProject.getOnlineapplyerguid());
            }

            auditOnlineProjectService.updateOnlineProjectJS(updateFieldMap, conditionMap);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "11", operateUserGuid, operateUserName, "", reason);
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_nys())) {
                    content = auditTaskExtension.getNotify_nys();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                    content = content.replace("[#=Reason#]", reason);
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var18) {
            var18.printStackTrace();
            result.setSystemFail(var18.toString());
        }

        return result;
    }

    public AuditCommonResult<String> handlePatch(AuditProject auditProject, String operateUserName,
            String operateUserGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            if (auditProject.getStatus() >= 30) {
                auditProject.setStatus(37);
            }
            else {
                auditProject.setStatus(28);
            }

            auditProject.setBubandate(new Date());
            this.saveProject(auditProject);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "22", operateUserGuid, operateUserName);
            IHandleConfig handleConfig = (IHandleConfig) ContainerFactory.getContainInfo()
                    .getComponent(IHandleConfig.class);
            String asdocword = (String) handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid())
                    .getResult();
            boolean isword = !"0".equals(asdocword);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            IHandleDoc docService = (IHandleDoc) ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            String bzgzsaddress = "";
            if (!"1".equals(auditProject.getTasktype().toString())) {
                bzgzsaddress = (String) docService
                        .getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                                auditTaskExtension.getIs_notopendoc(), String.valueOf(2), false, isword)
                        .getResult();
                if (StringUtil.isNotBlank(bzgzsaddress)) {
                    if (bzgzsaddress.contains("?")) {
                        bzgzsaddress = bzgzsaddress + "&ProjectGuid=" + auditProject.getRowguid()
                                + "&ProcessVersionInstanceGuid=" + auditProject.getPviguid() + "&taskguid="
                                + auditProject.getTaskcaseguid();
                    }
                    else {
                        bzgzsaddress = bzgzsaddress + "?ProjectGuid=" + auditProject.getRowguid()
                                + "&ProcessVersionInstanceGuid=" + auditProject.getPviguid() + "&taskguid="
                                + auditProject.getTaskcaseguid();
                    }
                }
            }

            result.setResult(bzgzsaddress);
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("status=", String.valueOf(28));
                SqlConditionUtil sql = new SqlConditionUtil();
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    sql.eq("sourceguid", auditProject.getBiguid());
                }
                else {
                    sql.eq("sourceguid", auditProject.getRowguid());
                }

                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory
                        .getContainInfo().getComponent(IMessagesCenterService.class);
                IWFInstanceAPI9 wfinstance = (IWFInstanceAPI9) ContainerFactory.getContainInfo()
                        .getComponent(IWFInstanceAPI9.class);
                ProcessVersionInstance pVersionInstance = wfinstance
                        .getProcessVersionInstance(auditProject.getPviguid());
                List<Integer> status = new ArrayList();
                status.add(20);
                status.add(10);
                List<WorkflowWorkItem> workflowWorkItems = wfinstance
                        .getWorkItemListByPVIGuidAndStatus(pVersionInstance, status);
                if (workflowWorkItems != null && workflowWorkItems.size() > 0) {
                    Iterator var18 = workflowWorkItems.iterator();

                    while (var18.hasNext()) {
                        WorkflowWorkItem workflowWorkItem = (WorkflowWorkItem) var18.next();
                        if (20 == workflowWorkItem.getStatus()) {
                            messagesCenterService.updateMessageTitleAndShow(workflowWorkItem.getWaitHandleGuid(),
                                    "【待补正】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")",
                                    workflowWorkItem.getTransactor());
                        }
                    }
                }
            }
        }
        catch (Exception var20) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var20));
        }

        return result;
    }

    public AuditCommonResult<String> handleReceipt(AuditProject auditProject, String chargeGuid, String operateUserName,
            String operateUserGuid, String messageItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProject.setFeedate(new Date());
            auditProject.setIs_fee(1);
            auditProject.setIs_pause(0);
            auditProjectService.updateProject(auditProject);
            int applyWay = auditProject.getApplyway();
            int temp = Integer.parseInt("20");
            if (applyWay < temp) {
                IAuditOnlineProject auditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                SqlConditionUtil sql = new SqlConditionUtil();
                updateFieldMap.put("is_fee =", "1");
                sql.eq("SOURCEGUID", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "31", operateUserGuid, operateUserName);
            IAuditProjectCharge projectChargeService = (IAuditProjectCharge) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = (AuditProjectCharge) projectChargeService
                    .getProjectChargeByRowGuid(chargeGuid).getResult();
            auditprojectcharge.setFeedate(new Date());
            auditprojectcharge.setFeeuserguid(operateUserGuid);
            auditprojectcharge.setFeeusername(operateUserName);
            auditprojectcharge.setIscharged(1);
            projectChargeService.updateProjectCharge(auditprojectcharge);
            IAuditProjectChargeDetail detailService = (IAuditProjectChargeDetail) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectChargeDetail.class);
            detailService.updateStatusByChargeGuid(chargeGuid);
        }
        catch (Exception var16) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var16));
        }

        return result;
    }

    public AuditCommonResult<String> handleReceipt(AuditProject auditProject, String chargeGuid, String operateUserName,
            String operateUserGuid, String messageItemGuid, String bankCode, String chargeFlowNo, Integer jiaofeitype) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProject.setFeedate(new Date());
            auditProject.setIs_fee(1);
            auditProject.setIs_pause(0);
            auditProjectService.updateProject(auditProject);
            int applyWay = auditProject.getApplyway();
            int temp = Integer.parseInt("20");
            if (applyWay < temp) {
                IAuditOnlineProject auditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                SqlConditionUtil sql = new SqlConditionUtil();
                updateFieldMap.put("is_fee =", "1");
                sql.eq("SOURCEGUID", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "31", operateUserGuid, operateUserName);
            IAuditProjectCharge projectChargeService = (IAuditProjectCharge) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = (AuditProjectCharge) projectChargeService
                    .getProjectChargeByRowGuid(chargeGuid).getResult();
            auditprojectcharge.setFeedate(new Date());
            auditprojectcharge.setFeeuserguid(operateUserGuid);
            auditprojectcharge.setFeeusername(operateUserName);
            auditprojectcharge.setIscharged(1);
            auditprojectcharge.setBankcode(bankCode);
            auditprojectcharge.setChargeflowno(chargeFlowNo);
            auditprojectcharge.setJiaofeitype(jiaofeitype);
            projectChargeService.updateProjectCharge(auditprojectcharge);
            IAuditProjectChargeDetail detailService = (IAuditProjectChargeDetail) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectChargeDetail.class);
            detailService.updateStatusByChargeGuid(chargeGuid);
        }
        catch (Exception var19) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var19));
        }

        return result;
    }

    public AuditCommonResult<String> handleReject(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String windowName, String windowGuid) {
        AuditCommonResult result = new AuditCommonResult();
        IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
        IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        try {
            String message = "";
            String bysljdsaddress = "";
            auditProject.setBanjieresult(Integer.parseInt("31"));
            auditProject.setBanjiedate(new Date());
            auditProject.setStatus(97);
            auditProject.setAcceptuserguid(operateUserGuid);
            auditProject.setAcceptusername(operateUserName);
            if (StringUtil.isBlank(auditProject.getWindowname())) {
                auditProject.setWindowname(windowName);
            }

            if (StringUtil.isBlank(auditProject.getWindowguid())) {
                auditProject.setWindowguid(windowGuid);
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            //查看有无centerguid
            if(StringUtils.isBlank(auditProject.getCenterguid())){
                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
                if(auditOrgaServiceCenter!=null){
                    auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
                }
            }
            IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaWorkingDay.class);
            //更新承诺办结时间
            AuditTask  auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
            if(auditTask!=null) {
                List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
                Date acceptdat = auditProject.getAcceptuserdate();
                Date shouldEndDate;
                if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {

                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                    log.info("shouldEndDate:"+shouldEndDate);
                } else {
                    shouldEndDate = null;
                }
                if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                                    AuditProjectUnusualUtils auditProjectUnusualUtils= new AuditProjectUnusualUtils();
                int totalWorkingDaysPaused  = auditProjectUnusualUtils.calculateTotalWorkingDaysPaused(auditProjectUnusuals,auditProject.getCenterguid());
                if (totalWorkingDaysPaused > 0 && shouldEndDate != null) {
                    // 重新计算包含暂停时间的预计结束日期
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), shouldEndDate, (int) totalWorkingDaysPaused).getResult();
                    log.info("考虑暂停时间后的预计结束日期 shouldEndDate: " + shouldEndDate);
                }
                log.info("shouldEndDate:"+shouldEndDate);
                }
                if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
            }
            
            auditProjectService.updateProject(auditProject);
            int applyWay = auditProject.getApplyway();
            int temp = Integer.parseInt("20");
            if (applyWay < temp) {
                IAuditOnlineProject auditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                SqlConditionUtil sql = new SqlConditionUtil();
                updateFieldMap.put("STATUS =", "97");
                sql.eq("SOURCEGUID", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "24", operateUserGuid, operateUserName);
            IHandleConfig handleConfig = (IHandleConfig) ContainerFactory.getContainInfo()
                    .getComponent(IHandleConfig.class);
            String asdocword = (String) handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid())
                    .getResult();
            boolean isword = !"0".equals(asdocword);
            IHandleDoc docService = (IHandleDoc) ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            bysljdsaddress = (String) docService
                    .getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                            auditTaskExtension.getIs_notopendoc(), String.valueOf(3), false, isword)
                    .getResult();
            if (StringUtil.isNotBlank(bysljdsaddress)) {
                bysljdsaddress = bysljdsaddress + "&ProjectGuid=" + auditProject.getRowguid()
                        + "&ProcessVersionInstanceGuid=" + auditProject.getPviguid() + "&taskguid="
                        + auditProject.getTaskcaseguid();
            }

            message = message + ";" + bysljdsaddress;
            result.setResult(message);
        }
        catch (Exception var19) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var19));
        }

        return result;
    }

    public AuditCommonResult<String> handleApprove(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String messageItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            String message = "";
            String unusualGuid = "";
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "50", operateUserGuid, operateUserName, workItemGuid);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            String ownGuid;
            HandleProjectService handleProjectService;
            if (!"1".equals(auditProject.getTasktype().toString())) {
                auditProject.setIs_pause(1);
                AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                if (auditProjectSparetime != null) {
                    auditProjectSparetime.setPause("1");
                    projectSparetime.updateSpareTime(auditProjectSparetime);
                }

                if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                    ownGuid = "【已暂停】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                    handleProjectService = new HandleProjectService();
                    handleProjectService.updateProjectTitle(ownGuid, auditProject.getPviguid(), messageItemGuid,
                            operateUserGuid);
                }

                IAuditProjectUnusual auditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectUnusual.class);
                unusualGuid = (String) auditProjectUnusual.insertProjectUnusual("系统自动暂停计时", "系统自动暂停计时", auditProject,
                        Integer.parseInt("10"), workItemGuid, "非即办件批准后自动暂停计时").getResult();
            }

            auditProject.setStatus(50);
            auditProject.setBanjieresult(40);
            auditProject.setBanwandate(new Date());
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo;
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                handleProjectService = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    auditRsCompanyBaseinfoService = (IAuditRsCompanyBaseinfo) ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    auditRsCompanyBaseinfo = (AuditRsCompanyBaseinfo) auditRsCompanyBaseinfoService
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditRsCompanyBaseinfo != null) {
                        String var30 = auditRsCompanyBaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                ;
            }

            ownGuid = "";
            IAuditIndividual auditIndividualService = (IAuditIndividual) ContainerFactory.getContainInfo()
                    .getComponent(IAuditIndividual.class);
            auditRsCompanyBaseinfoService = (IAuditRsCompanyBaseinfo) ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyBaseinfo.class);
            if ("10".equals(String.valueOf(auditProject.getApplyertype()))) {
                auditRsCompanyBaseinfo = (AuditRsCompanyBaseinfo) auditRsCompanyBaseinfoService
                        .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsCompanyBaseinfo.getCompanyid();
            }
            else if ("20".equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = (AuditRsIndividualBaseinfo) auditIndividualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsIndividualBaseinfo.getPersonid();
            }

            if (StringUtil.isBlank(ownGuid)) {
                ownGuid = auditProject.getApplyeruserguid();
            }

            String zyxkjdsaddress = "";
            IHandleConfig handleConfig = (IHandleConfig) ContainerFactory.getContainInfo()
                    .getComponent(IHandleConfig.class);
            String asdocword = (String) handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid())
                    .getResult();
            boolean isword = !"0".equals(asdocword);
            IHandleDoc docService = (IHandleDoc) ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            zyxkjdsaddress = (String) docService
                    .getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                            auditTaskExtension.getIs_notopendoc(), String.valueOf(8), false, isword)
                    .getResult();
            if (StringUtil.isNotBlank(zyxkjdsaddress)) {
                zyxkjdsaddress = zyxkjdsaddress + "&ProjectGuid=" + auditProject.getRowguid()
                        + "&ProcessVersionInstanceGuid=" + auditProject.getPviguid() + "&taskguid="
                        + auditProject.getTaskcaseguid();
            }

            message = message + ";" + zyxkjdsaddress + "@" + unusualGuid;
            result.setResult(message);
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_pz())) {
                    content = auditTaskExtension.getNotify_pz();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var26) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var26));
        }

        return result;
    }

    public AuditCommonResult<String> handleNotApprove(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String messageItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            String message = "";
            String unusualGuid = "";
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "51", operateUserGuid, operateUserName, workItemGuid);
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                    .getResult();
            HandleProjectService handleProjectService;
            if (!"1".equals(auditProject.getTasktype().toString()) || !"1".equals(auditTask.getJbjmode())) {
                auditProject.setIs_pause(1);
                AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                if (auditProjectSparetime != null) {
                    auditProjectSparetime.setPause("1");
                    projectSparetime.updateSpareTime(auditProjectSparetime);
                }

                if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                    String title = "【已暂停】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                    handleProjectService = new HandleProjectService();
                    handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid,
                            operateUserGuid);
                }

                IAuditProjectUnusual auditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectUnusual.class);
                unusualGuid = (String) auditProjectUnusual.insertProjectUnusual("系统自动暂停计时", "系统自动暂停计时", auditProject,
                        Integer.parseInt("10"), workItemGuid, "非即办件不予批准后自动暂停计时").getResult();
            }

            auditProject.setStatus(40);
            auditProject.setBanjieresult(50);
            auditProject.setBanwandate(new Date());
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            String asdocword;
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                handleProjectService = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        asdocword = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            IHandleConfig handleConfig = (IHandleConfig) ContainerFactory.getContainInfo()
                    .getComponent(IHandleConfig.class);
            asdocword = (String) handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
            boolean isword = !"0".equals(asdocword);
            IHandleDoc docService = (IHandleDoc) ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            String byxkjdsaddress = "";
            byxkjdsaddress = (String) docService
                    .getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                            auditTaskExtension.getIs_notopendoc(), String.valueOf(4), false, isword)
                    .getResult();
            if (StringUtil.isNotBlank(byxkjdsaddress)) {
                byxkjdsaddress = byxkjdsaddress + "&ProjectGuid=" + auditProject.getRowguid()
                        + "&ProcessVersionInstanceGuid=" + auditProject.getPviguid() + "&taskguid="
                        + auditProject.getTaskcaseguid();
            }

            message = message + ";" + byxkjdsaddress + "@" + unusualGuid;
            result.setResult(message);
            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_npz())) {
                    content = auditTaskExtension.getNotify_npz();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid, operateUserName,
                        "", "", "", false, "短信");
            }
        }
        catch (Exception var25) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var25));
        }

        return result;
    }

    public AuditCommonResult<String> handleFinish(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid) {
        AuditCommonResult result = new AuditCommonResult();
        IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
        IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        AuditProjectSparetime auditprojectspare;
        try {
            if (auditProject.getIs_pause() == 1) {
                this.handleRecover(auditProject, operateUserName, operateUserGuid, (String) null, (String) null);
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "60", operateUserGuid, operateUserName, workItemGuid);
            IAuditProjectSparetime sparetimeService = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            auditprojectspare = (AuditProjectSparetime) sparetimeService
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            if (StringUtil.isBlank(auditProject.getSparetime())) {
                if (auditprojectspare != null && !auditprojectspare.isEmpty()) {
                    int spendtime = auditprojectspare.getSpendminutes();
                    int sparetime = auditprojectspare.getSpareminutes();
                    auditProject.setSparetime(sparetime);
                    auditProject.setSpendtime(spendtime);
                }

                if (auditprojectspare != null) {
                    sparetimeService.deleteSpareTimeByRowGuid(auditprojectspare.getRowguid());
                }
            }

            auditProject.setStatus(90);
            auditProject.setBanjiedate(new Date());
            auditProject.setBanjieuserguid(operateUserGuid);
            auditProject.setBanjieusername(operateUserName);
            auditProject.setIs_guidang(1);
            auditProject.setGuidangdate(new Date());
            auditProject.setGuidanguserguid(operateUserGuid);
            auditProject.setGuidangusername(operateUserName);
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if ("1".equals(auditTaskExtension.getIszijianxitong().toString())
                    && (auditProject.getBanjieresult() == null || auditProject.getBanjieresult() == 0)) {
                auditProject.setBanjieresult(40);
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            //查看有无centerguid
            if(StringUtils.isBlank(auditProject.getCenterguid())){
                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
                if(auditOrgaServiceCenter!=null){
                    auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
                }
            }
            IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaWorkingDay.class);
            //更新承诺办结时间
            AuditTask  auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
            if(auditTask!=null) {
                List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
                Date acceptdat = auditProject.getAcceptuserdate();
                Date shouldEndDate;
                if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {

                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                    log.info("shouldEndDate:"+shouldEndDate);
                } else {
                    shouldEndDate = null;
                }
                if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                                    AuditProjectUnusualUtils auditProjectUnusualUtils= new AuditProjectUnusualUtils();
                int totalWorkingDaysPaused  = auditProjectUnusualUtils.calculateTotalWorkingDaysPaused(auditProjectUnusuals,auditProject.getCenterguid());
                if (totalWorkingDaysPaused > 0 && shouldEndDate != null) {
                    // 重新计算包含暂停时间的预计结束日期
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), shouldEndDate, (int) totalWorkingDaysPaused).getResult();
                    log.info("考虑暂停时间后的预计结束日期 shouldEndDate: " + shouldEndDate);
                }
                log.info("shouldEndDate:"+shouldEndDate);
                }
                if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
            }
            
            auditProjectService.updateProject(auditProject);
            IAuditProjectNumber projectNumberService = (IAuditProjectNumber) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectNumber.class);
            projectNumberService.addProjectNumber(auditProject, false, operateUserGuid, operateUserName);
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                ;
            }

            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("status=", String.valueOf(90));
                updateDateFieldMap.put("banjiedate=",
                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }
        }
        catch (Exception var18) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var18));
        }

        IAuditTaskMaterialCase auditTaskMaterialCaseService = (IAuditTaskMaterialCase) ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterialCase.class);
        IAuditProjectMaterial auditProjectMaterialService = (IAuditProjectMaterial) ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        auditprojectspare = null;
        String taskcaseguid = auditProject.getTaskcaseguid();
        if (StringUtil.isNotBlank(taskcaseguid)) {
            List<AuditTaskMaterialCase> auditTaskMaterialCases = (List) auditTaskMaterialCaseService
                    .selectTaskMaterialCaseByCaseGuid(taskcaseguid).getResult();
            Map<String, String> caseMap = new HashMap(16);
            if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                Iterator var26 = auditTaskMaterialCases.iterator();

                while (var26.hasNext()) {
                    AuditTaskMaterialCase auditTaskMaterialCase = (AuditTaskMaterialCase) var26.next();
                    caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getMaterialguid());
                }

                auditProjectMaterialService.deleteNoUsedMaterialByMap(auditProject.getRowguid(), caseMap);
            }
        }

        return result;
    }

    public AuditCommonResult<String> handlePricing(AuditProject auditProject, String operateUserName,
            String operateUserGuid, List<Record> listItem, Double realSum, Double cutSum, String messageItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditProjectCharge auditProjectChargeService = (IAuditProjectCharge) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            String chargeGuid = UUID.randomUUID().toString();
            AuditProjectCharge auditprojectcharge = new AuditProjectCharge();
            auditprojectcharge.setRowguid(chargeGuid);
            auditprojectcharge.setProjectguid(auditProject.getRowguid());
            auditprojectcharge.setCheckdate(new Date());
            auditprojectcharge.setCheckuserguid(operateUserGuid);
            auditprojectcharge.setCheckusername(operateUserName);
            auditprojectcharge.setJiaokuanperson(auditProject.getApplyername());
            auditprojectcharge.setCharge_when(auditProject.getCharge_when());
            auditprojectcharge.setNote(auditprojectcharge.getCutreason());
            auditprojectcharge.setIs_cancel(0);
            auditprojectcharge.setIscharged(0);
            auditprojectcharge.setRealsum(realSum);
            auditprojectcharge.setCutsum(cutSum);
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                    .getResult();
            int charNo = (Integer) auditProjectChargeService.getMaxChargeNo(auditTask.getAreacode()).getResult();
            auditprojectcharge.setChargeno(charNo);
            auditProjectChargeService.addProjectCharge(auditprojectcharge);
            auditProject.setIs_check(1);
            auditProject.setIs_fee(0);
            if (!"1".equals(auditProject.getTasktype().toString())) {
                ;
            }

            auditProject.setCheckuserguid(operateUserGuid);
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            IAuditProjectChargeDetail chargeDetailService = (IAuditProjectChargeDetail) ContainerFactory
                    .getContainInfo().getComponent(IAuditProjectChargeDetail.class);
            Iterator var17 = listItem.iterator();

            Record applyerguid;
            while (var17.hasNext()) {
                applyerguid = (Record) var17.next();
                AuditProjectChargeDetail auditprojectchargedetail = new AuditProjectChargeDetail();
                auditprojectchargedetail.setRowguid(UUID.randomUUID().toString());
                auditprojectchargedetail.setOperatedate(new Date());
                auditprojectchargedetail.setOperateusername(operateUserName);
                auditprojectchargedetail.setChargeguid(chargeGuid);
                auditprojectchargedetail.setChargeitemguid(applyerguid.get("itemguid").toString());
                auditprojectchargedetail.setIs_directinput(0);
                auditprojectchargedetail
                        .setCutfeeproportion(Double.parseDouble(applyerguid.get("Cutfeeproportion").toString()));
                auditprojectchargedetail.setCutreason(
                        applyerguid.get("cutreason") == null ? "" : applyerguid.get("cutreason").toString());
                auditprojectchargedetail.setItemsum(Double.parseDouble(applyerguid.get("itemsum").toString()));
                auditprojectchargedetail.setPrice(Double.parseDouble(applyerguid.get("price").toString()));
                auditprojectchargedetail.setQuantity(Double.parseDouble(applyerguid.get("quantity").toString()));
                auditprojectchargedetail.setStatus(0);
                chargeDetailService.addProjectChargeDetail(auditprojectchargedetail);
            }

            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        String var25 = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("is_check=", "1");
                updateFieldMap.put("is_fee=", "0");
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "30", operateUserGuid, operateUserName);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause("1");
            projectSparetime.updateSpareTime(auditProjectSparetime);
        }
        catch (Exception var22) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var22));
        }

        return result;
    }

    public AuditCommonResult<String> handlePriceCancel(AuditProject auditProject, String chargeGuid,
            String operateUserName, String operateUserGuid, String cancelReason) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProject.setBillno((String) null);
            auditProject.setIs_check(0);
            auditProject.setCheckuserguid(operateUserGuid);
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            IAuditProjectCharge projectChargeService = (IAuditProjectCharge) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = (AuditProjectCharge) projectChargeService
                    .getProjectChargeByRowGuid(chargeGuid).getResult();
            auditprojectcharge.setIs_cancel(1);
            auditprojectcharge.setCancelreason(cancelReason);
            projectChargeService.updateProjectCharge(auditprojectcharge);
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("is_check=", "0");
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "32", operateUserGuid, operateUserName, "", cancelReason);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause("0");
            projectSparetime.updateSpareTime(auditProjectSparetime);
        }
        catch (Exception var15) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var15));
        }

        return result;
    }

    public AuditCommonResult<String> handlePriceCancel(AuditProject auditProject, String chargeGuid,
            String operateUserName, String operateUserGuid, String cancelReason, Integer cancelType) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProject.setBillno((String) null);
            auditProject.setIs_check(0);
            auditProject.setCheckuserguid(operateUserGuid);
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            IAuditProjectCharge projectChargeService = (IAuditProjectCharge) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = (AuditProjectCharge) projectChargeService
                    .getProjectChargeByRowGuid(chargeGuid).getResult();
            auditprojectcharge.setIs_cancel(1);
            auditprojectcharge.setCancelreason(cancelReason);
            auditprojectcharge.setCanceltype(cancelType);
            projectChargeService.updateProjectCharge(auditprojectcharge);
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMap = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMap.put("is_check=", "0");
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "32", operateUserGuid, operateUserName, "", cancelReason);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause("0");
            projectSparetime.updateSpareTime(auditProjectSparetime);
        }
        catch (Exception var16) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var16));
        }

        return result;
    }

    public AuditCommonResult<String> handlePause(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String messageItemGuid, String note) {
        AuditCommonResult<String> result = new AuditCommonResult();
        HandleProjectService handleProjectService = new HandleProjectService();

        try {
            IWorkflowActivityService activityService = (IWorkflowActivityService) ContainerFactory.getContainInfo()
                    .getComponent(IWorkflowActivityService.class);
            IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IWFInstanceAPI9 wfInstance = (IWFInstanceAPI9) ContainerFactory.getContainInfo()
                    .getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pVersionInstance = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            IAuditProjectUnusual auditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            auditProject.setIs_pause(1);
            project.updateProject(auditProject);
            AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause("1");
            projectSparetime.updateSpareTime(auditProjectSparetime);
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pVersionInstance, workItemGuid);
            activityService.getByActivityGuid(workflowWorkItem.getActivityGuid());
            String title = "【暂停计时】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid, operateUserGuid);
            String unusualGuid = (String) auditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt("10"), workItemGuid, "暂停计时").getResult();
            result.setResult(unusualGuid);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "40", operateUserGuid, operateUserName, workItemGuid, note);
        }
        catch (Exception var21) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var21));
        }

        return result;
    }

    public AuditCommonResult<String> handlePause(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String messageItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult();
        HandleProjectService handleProjectService = new HandleProjectService();

        try {
            IWorkflowActivityService activityService = (IWorkflowActivityService) ContainerFactory.getContainInfo()
                    .getComponent(IWorkflowActivityService.class);
            IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IWFInstanceAPI9 wfInstance = (IWFInstanceAPI9) ContainerFactory.getContainInfo()
                    .getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pVersionInstance = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            IAuditProjectUnusual auditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            auditProject.setIs_pause(1);
            project.updateProject(auditProject);
            AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause("1");
            projectSparetime.updateSpareTime(auditProjectSparetime);
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pVersionInstance, workItemGuid);
            activityService.getByActivityGuid(workflowWorkItem.getActivityGuid());
            String title = "【暂停计时】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid, operateUserGuid);
            String unusualGuid = (String) auditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt("10"), workItemGuid, "暂停计时").getResult();
            result.setResult(unusualGuid);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "40", operateUserGuid, operateUserName, workItemGuid);
        }
        catch (Exception var20) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var20));
        }

        return result;
    }

    public AuditCommonResult<String> handleRecover(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String messageItemGuid) {
        IWorkflowActivityService activityService = (IWorkflowActivityService) ContainerFactory.getContainInfo()
                .getComponent(IWorkflowActivityService.class);
        IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        AuditCommonResult result = new AuditCommonResult();

        try {
            auditProject.setIs_pause(0);
            IAuditProject iAuditProject = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            iAuditProject.updateProject(auditProject);
            AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause("0");
            projectSparetime.updateSpareTime(auditProjectSparetime);
            IWFInstanceAPI9 wfinstance = (IWFInstanceAPI9) ContainerFactory.getContainInfo()
                    .getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pvi = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
            WorkflowWorkItem wfitem = wfinstance.getWorkItem(pvi, workItemGuid);
            WorkflowActivity wa = activityService.getByActivityGuid(wfitem.getActivityGuid());
            String title = "【" + wa.getActivityName() + "】" + auditProject.getProjectname() + "("
                    + auditProject.getApplyername() + ")";
            (new HandleProjectService()).updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid,
                    operateUserGuid);
            IAuditProjectUnusual iauditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            String unusualGuid = (String) iauditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt("11"), workItemGuid, "恢复计时").getResult();
            result.setResult(unusualGuid);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "41", operateUserGuid, operateUserName, workItemGuid);
        }
        catch (Exception var19) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var19));
        }

        return result;
    }

    public AuditCommonResult<String> handleDelayPass(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String messageItemGuid, String AuditProjectUnusualGuid, String sqrUserGuid,
            String oldmessageitemguid) {
        IAuditProjectUnusual iauditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        AuditCommonResult<AuditProjectUnusual> auditProjectUnusualResult = iauditProjectUnusual
                .getAuditProjectUnusualByRowguid(AuditProjectUnusualGuid);
        AuditProjectUnusual auditProjectUnusual = (AuditProjectUnusual) auditProjectUnusualResult.getResult();
        auditProjectUnusual.setOperatetype(Integer.parseInt("20"));
        auditProjectUnusual.setAuditresult(1);
        auditProjectUnusual.setAudituserguid(operateUserGuid);
        auditProjectUnusual.setAuditusername(operateUserName);
        iauditProjectUnusual.updateProjectUnusual(auditProjectUnusual);
        auditProject.setIs_delay(1);
        IAuditOrgaWorkingDay workingDayService = (IAuditOrgaWorkingDay) ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaWorkingDay.class);
        Date shouldEndDay = (Date) workingDayService.getWorkingDayWithOfficeSet(auditProject.getCenterguid(),
                auditProject.getPromiseenddate(), auditProjectUnusual.getDelayworkday()).getResult();
        auditProject.setPromiseenddate(shouldEndDay);
        project.updateProject(auditProject);
        AuditCommonResult<String> result = new AuditCommonResult();
        IAuditProjectSparetime iauditProjectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        AuditCommonResult<AuditProjectSparetime> AuditProjectSparetimeResult = iauditProjectSparetime
                .getSparetimeByProjectGuid(auditProject.getRowguid());
        AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) AuditProjectSparetimeResult.getResult();
        if (auditProjectSparetime != null) {
            int sparemunites = auditProjectSparetime.getSpareminutes() + 1440 * auditProjectUnusual.getDelayworkday();
            auditProjectSparetime.setSpareminutes(sparemunites);
            iauditProjectSparetime.updateSpareTime(auditProjectSparetime);
        }

        IMessagesCenterService messagecenterservice = (IMessagesCenterService) ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        messagecenterservice.deleteMessage(messageItemGuid, operateUserGuid);
        IRoleService roleService = (IRoleService) ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IAuditOrgaArea auditOrgaAreaService = (IAuditOrgaArea) ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaArea.class);
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "办件特殊操作审核");
        IUserService userService = (IUserService) ContainerFactory.getContainInfo().getComponent(IUserService.class);
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            String ouGuid = ((AuditOrgaArea) auditOrgaAreaService.getAreaByAreacode(auditProject.getAreacode())
                    .getResult()).getOuguid();
            List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false, 3);
            if (listUser != null && listUser.size() > 0) {
                Iterator var26 = listUser.iterator();

                while (var26.hasNext()) {
                    FrameUser frameUser = (FrameUser) var26.next();
                    messagecenterservice.deleteMessageByIdentifier(auditProject.getRowguid(), frameUser.getUserGuid());
                }
            }
        }

        MessagesCenter messagescenter = messagecenterservice.getDetail(oldmessageitemguid, sqrUserGuid);
        if (messagescenter != null) {
            messagecenterservice
                    .updateMessageTitleAndShow(
                            messagescenter.getMessageItemGuid(), "【" + messagescenter.getBeiZhu() + "】"
                                    + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")",
                            sqrUserGuid);
        }

        HandleProjectService projectService = new HandleProjectService();
        projectService.HandProjectLog(auditProject, "72", operateUserGuid, operateUserName, "", "延期申请审核通过！");
        IOnlineMessageInfoService onlinemessage = (IOnlineMessageInfoService) ContainerFactory.getContainInfo()
                .getComponent(IOnlineMessageInfoService.class);
        onlinemessage.insertMessage(UUID.randomUUID().toString(), operateUserGuid, operateUserName, sqrUserGuid,
                userService.getUserNameByUserGuid(sqrUserGuid), sqrUserGuid,
                auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")" + "延期申请审核通过！", new Date());
        return result;
    }

    public AuditCommonResult<String> handleDelay(AuditProject auditProject, String operateUserName,
            String operateUserGuid, int yanqiday, String reason, String workItemGuid, String messageItemGuid) {
        String message = "";
        AuditCommonResult<String> result = new AuditCommonResult();
        IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditProjectUnusual projectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);

        try {
            if (auditProject.getPromise_day() != null) {
                if (yanqiday > 0) {
                    String unusualguid = UUID.randomUUID().toString();
                    message = (new HandleProjectService()).sendMGSToYQSH(unusualguid, auditProject, operateUserGuid,
                            messageItemGuid);
                    if ("ok".equals(message)) {
                        AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();
                        auditProjectUnusual.setRowguid(unusualguid);
                        auditProjectUnusual.setOperateuserguid(operateUserGuid);
                        auditProjectUnusual.setOperateusername(operateUserName);
                        auditProjectUnusual.setOperatedate(new Date());
                        auditProjectUnusual.setAuditresult(0);
                        auditProjectUnusual.setOperatetype(Integer.parseInt("20"));
                        auditProjectUnusual.setDelayworkday(yanqiday);
                        auditProjectUnusual.setNote(reason);
                        auditProjectUnusual.setProjectguid(auditProject.getRowguid());
                        auditProjectUnusual.setPviguid(auditProject.getPviguid());
                        auditProjectUnusual.setWorkitemguid(workItemGuid);
                        projectUnusual.addProjectUnusual(auditProjectUnusual);
                        String title = "【延期申请】" + auditProject.getProjectname() + "(" + auditProject.getApplyername()
                                + ")";
                        (new HandleProjectService()).updateProjectTitle(title, auditProject.getPviguid(),
                                messageItemGuid, operateUserGuid);
                        auditProject.setIs_delay(10);
                        project.updateProject(auditProject);
                        message = message + ";" + unusualguid;
                        HandleProjectService projectService = new HandleProjectService();
                        projectService.HandProjectLog(auditProject, "70", operateUserGuid, operateUserName,
                                workItemGuid, reason);
                    }
                }
                else {
                    message = "延期天数不能小于0！";
                }
            }
            else {
                message = "此办件承诺期限为0，不能延期！";
            }

            result.setResult(message);
        }
        catch (Exception var16) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var16));
        }

        return result;
    }

    public AuditCommonResult<String> handleDelayNotPass(AuditProject auditProject, String auditProjectUnusualGuid,
            String operateUserName, String operateUserGuid, String messageItemGuid, String sqrUserGuid,
            String oldmessageitemguid) {
        AuditCommonResult<String> result = new AuditCommonResult();
        IAuditProjectUnusual projectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IAuditProject project = (IAuditProject) ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);

        try {
            AuditProjectUnusual auditprojectunusual = (AuditProjectUnusual) projectUnusual
                    .getAuditProjectUnusualByRowguid(auditProjectUnusualGuid).getResult();
            auditprojectunusual.setOperatetype(Integer.parseInt("21"));
            auditprojectunusual.setAuditresult(1);
            auditprojectunusual.setAudituserguid(operateUserGuid);
            auditprojectunusual.setAuditusername(operateUserName);
            projectUnusual.updateProjectUnusual(auditprojectunusual);
            auditProject.setIs_delay(0);
            project.updateProject(auditProject);
            messagesCenterService.deleteMessage(messageItemGuid, operateUserGuid);
            IRoleService roleService = (IRoleService) ContainerFactory.getContainInfo()
                    .getComponent(IRoleService.class);
            IAuditOrgaArea auditOrgaAreaService = (IAuditOrgaArea) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaArea.class);
            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "办件特殊操作审核");
            IUserService userService = (IUserService) ContainerFactory.getContainInfo()
                    .getComponent(IUserService.class);
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                String ouGuid = ((AuditOrgaArea) auditOrgaAreaService.getAreaByAreacode(auditProject.getAreacode())
                        .getResult()).getOuguid();
                List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false,
                        3);
                if (listUser != null && listUser.size() > 0) {
                    Iterator var20 = listUser.iterator();

                    while (var20.hasNext()) {
                        FrameUser frameUser = (FrameUser) var20.next();
                        messagesCenterService.deleteMessageByIdentifier(auditProject.getRowguid(),
                                frameUser.getUserGuid());
                    }
                }
            }

            MessagesCenter messagescenter = messagesCenterService.getDetail(oldmessageitemguid, sqrUserGuid);
            if (messagescenter != null) {
                messagesCenterService.updateMessageTitleAndShow(
                        messagescenter.getMessageItemGuid(), "【" + messagescenter.getBeiZhu() + "】"
                                + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")",
                        sqrUserGuid);
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "71", operateUserGuid, operateUserName);
            IOnlineMessageInfoService onlinemessage = (IOnlineMessageInfoService) ContainerFactory.getContainInfo()
                    .getComponent(IOnlineMessageInfoService.class);
            onlinemessage.insertMessage(UUID.randomUUID().toString(), operateUserGuid, operateUserName, sqrUserGuid,
                    userService.getUserNameByUserGuid(sqrUserGuid), sqrUserGuid,
                    auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")" + "延期申请审核已退回！",
                    new Date());
        }
        catch (Exception var22) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var22));
        }

        return result;
    }

    public AuditCommonResult<String> handleRevoke(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult();
        HandleProjectService handleProjectService = new HandleProjectService();

        try {
            String unusualGuid = handleProjectService.handleRevokeOrSuspension(auditProject, operateUserName,
                    operateUserGuid, "98", workItemGuid, reason);
            result.setResult(unusualGuid);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "61", operateUserGuid, operateUserName, workItemGuid, reason);
        }
        catch (Exception var10) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var10));
        }

        return result;
    }

    public AuditCommonResult<String> handleSuspension(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult();
        HandleProjectService handleProjectService = new HandleProjectService();

        try {
            String unusualGuid = handleProjectService.handleRevokeOrSuspension(auditProject, operateUserName,
                    operateUserGuid, "99", workItemGuid, reason);
            result.setResult(unusualGuid);
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "62", operateUserGuid, operateUserName, workItemGuid, reason);
        }
        catch (Exception var10) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var10));
        }

        return result;
    }

    public AuditCommonResult<String> handleEvaluate(AuditProject auditProject, String operateUserName,
            String operateUserGuid) {
        HandleProjectService projectService = new HandleProjectService();
        projectService.HandProjectLog(auditProject, "81", operateUserGuid, operateUserName);
        return null;
    }

    public AuditCommonResult<String> handleResult(AuditProject auditProject, String operateUserName,
            String operateUserGuid) {
        HandleProjectService projectService = new HandleProjectService();
        projectService.HandProjectLog(auditProject, "80", operateUserGuid, operateUserName);
        return null;
    }

    public AuditCommonResult<List<SelectItem>> initDocList(String projectGuid, String tasktype) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            List<SelectItem> jpdDoc = null;
            new ArrayList();
            List<SelectItem> jpddoclist = DataUtil
                    .convertMap2ComboBox((List) CodeModalFactory.factory("下拉列表", "文书类型", (String) null, false));
            List<SelectItem> Doclist = new ArrayList();
            int i;
            String docvalue;
            if ("1".equals(tasktype)) {
                for (i = 0; i < jpddoclist.size(); ++i) {
                    docvalue = ((SelectItem) jpddoclist.get(i)).getValue().toString();
                    if (!String.valueOf(5).equals(docvalue) && !String.valueOf(6).equals(docvalue)
                            && !String.valueOf(2).equals(docvalue)) {
                        Doclist.add(jpddoclist.get(i));
                    }
                }

                jpdDoc = Doclist;
            }
            else {
                for (i = 0; i < jpddoclist.size(); ++i) {
                    docvalue = ((SelectItem) jpddoclist.get(i)).getValue().toString();
                    if (!String.valueOf(5).equals(docvalue) && !String.valueOf(6).equals(docvalue)) {
                        Doclist.add(jpddoclist.get(i));
                    }
                }

                jpdDoc = Doclist;
            }

            if (StringUtil.isNotBlank(projectGuid)) {
                String s = "";
                IAuditProjectDocSnap docSnapService = (IAuditProjectDocSnap) ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectDocSnap.class);
                List<AuditProjectDocSnap> auditprojectDocsnaplist = (List) docSnapService
                        .selectDocSnapByProjectGuid(projectGuid).getResult();

                for (int k = 0; k < jpdDoc.size(); ++k) {
                    if (auditprojectDocsnaplist != null) {
                        for (int j = 0; j < auditprojectDocsnaplist.size(); ++j) {
                            if (((SelectItem) jpdDoc.get(k)).getValue().toString().equals(String
                                    .valueOf(((AuditProjectDocSnap) auditprojectDocsnaplist.get(j)).getDoctype()))) {
                                s = "(已打印)";
                                break;
                            }

                            s = "";
                        }
                    }

                    String label = ((SelectItem) jpdDoc.get(k)).getText();
                    ((SelectItem) jpdDoc.get(k)).setText(label + s);
                }
            }

            jpdDoc.add(0, new SelectItem("", "--文书列表--"));
            result.setResult(jpdDoc);
        }
        catch (Exception var12) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var12));
        }

        return result;
    }

    public AuditCommonResult<List<Map<String, String>>> initProjectDocList(String projectGuid, String taskGuid,
            String centerGuid, String tasktype, String notOpenDocs, boolean isdoc) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            List<Map<String, String>> listDoc = new ArrayList();
            ICodeItemsService codeItemsService = (ICodeItemsService) ContainerFactory.getContainInfo()
                    .getComponent(ICodeItemsService.class);
            List<CodeItems> listDocs = codeItemsService.listCodeItemsByCodeName("文书类型");
            notOpenDocs = notOpenDocs + ",";
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, false).getResult();
            String temptaskguid = "";
            if (auditTask != null) {
                temptaskguid = (String) auditTaskService.getTemplateTaskGuid(auditTask.getAreacode()).getResult();
            }

            IAuditProjectDocSnap docSnapService = (IAuditProjectDocSnap) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectDocSnap.class);
            IHandleDoc handleDocService = (IHandleDoc) ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            List<AuditProjectDocSnap> auditprojectDocsnaplist = (List) docSnapService
                    .selectDocSnapByProjectGuid(projectGuid).getResult();
            if (auditprojectDocsnaplist != null && auditprojectDocsnaplist.size() > 0) {
                for (int i = 0; i < auditprojectDocsnaplist.size(); ++i) {
                    for (int j = auditprojectDocsnaplist.size() - 1; j > i; --j) {
                        if (((AuditProjectDocSnap) auditprojectDocsnaplist.get(i)).getDoctype()
                                .equals(((AuditProjectDocSnap) auditprojectDocsnaplist.get(j)).getDoctype())) {
                            if (((AuditProjectDocSnap) auditprojectDocsnaplist.get(i)).getOperatedate()
                                    .getTime() > ((AuditProjectDocSnap) auditprojectDocsnaplist.get(j)).getOperatedate()
                                            .getTime()) {
                                auditprojectDocsnaplist.remove(j);
                            }
                            else {
                                auditprojectDocsnaplist.remove(i);
                            }
                        }
                    }
                }
            }

            Iterator var24 = listDocs.iterator();

            while (true) {
                CodeItems docItem;
                do {
                    if (!var24.hasNext()) {
                        result.setResult(listDoc);
                        return result;
                    }

                    docItem = (CodeItems) var24.next();
                }
                while (notOpenDocs.indexOf(docItem.getItemValue() + ",") > -1);

                boolean addFlag = true;
                Iterator iterator = auditprojectDocsnaplist.iterator();

                HashMap doc;
                while (iterator.hasNext()) {
                    AuditProjectDocSnap docSnap = (AuditProjectDocSnap) iterator.next();
                    doc = new HashMap(16);
                    if (docItem.getItemValue().equals(String.valueOf(docSnap.getDoctype()))) {
                        doc.put("docName", docItem.getItemText());
                        if (StringUtil.isNotBlank(docSnap.getDocattachguid())) {
                            doc.put("docUrl", docSnap.getDocattachguid());
                            doc.put("isWord", "1");
                        }
                        else {
                            doc.put("docUrl", handleDocService.getDocEditPageUrl(taskGuid, centerGuid,
                                    docItem.getItemValue(), false, temptaskguid).getResult());
                            doc.put("isWord", "0");
                        }

                        doc.put("isPrint", "1");
                        iterator.remove();
                        addFlag = false;
                        listDoc.add(doc);
                    }
                }

                if (addFlag) {
                    doc = new HashMap(16);
                    doc.put("docName", docItem.getItemText());
                    doc.put("docUrl", handleDocService
                            .getDocEditPageUrl(taskGuid, centerGuid, docItem.getItemValue(), isdoc, temptaskguid)
                            .getResult());
                    doc.put("isWord", isdoc ? "1" : "0");
                    doc.put("isPrint", "0");
                    listDoc.add(doc);
                }
            }
        }
        catch (Exception var23) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var23));
            return result;
        }
    }

    public AuditCommonResult<String> handleMaterialNotPass(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String workItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditProject audtiProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            String note = "因以下原因计时暂停：缺失必要办件";
            IAuditProjectUnusual auditProjectUnusual = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            String unusualGuid = (String) auditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt("10"), workItemGuid, note).getResult();
            result.setResult(unusualGuid);
        }
        catch (Exception var11) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var11));
        }

        return result;
    }

    public AuditCommonResult<Void> handleProjectPass(AuditProject auditProject, String messageItemGuid,
            String operateUserGuid, String workItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            if (37 == auditProject.getStatus()) {
                IAuditProjectMaterial projectMaterialService = (IAuditProjectMaterial) ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectMaterial.class);
                projectMaterialService.updateAllAuditStatus(auditProject.getRowguid());
            }

            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService
                    .getAuditTaskByGuid(auditProject.getTaskguid(), auditProject.getAcceptareacode(), true).getResult();
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            IOuService ouService = (IOuService) ContainerFactory.getContainInfo().getComponent(IOuService.class);
            String taskType = auditProject.getTasktype().toString();
            String pviGuid = auditProject.getPviguid();
            String projectGuid = auditProject.getRowguid();
            if (!"1".equals(taskType)) {
                AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                if (auditProjectSparetime != null && !auditProjectSparetime.isEmpty()) {
                    int spendtime = auditProjectSparetime.getSpendminutes();
                    int sparetime = auditProjectSparetime.getSpareminutes();
                    auditProject.setSparetime(sparetime);
                    auditProject.setSpendtime(spendtime);
                }

                if (auditProjectSparetime != null) {
                    projectSparetime.deleteSpareTimeByRowGuid(auditProjectSparetime.getRowguid());
                }
            }

            auditProject.setStatus(50);
            if ("1".equals(taskType) || "2".equals(taskType)) {
                auditProject.setBanjieresult(40);
                auditProject.setBanwandate(new Date());
            }

            auditProjectService.updateProject(auditProject);
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo;
            IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    auditRsCompanyBaseinfoService = (IAuditRsCompanyBaseinfo) ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    auditRsCompanyBaseinfo = (AuditRsCompanyBaseinfo) auditRsCompanyBaseinfoService
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditRsCompanyBaseinfo != null) {
                        applyerguid = auditRsCompanyBaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMapOnline = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMapOnline.put("status=", auditProject.getStatus().toString());
                updateFieldMapOnline.put("banjieresult=", String.valueOf(50));
                updateFieldMapOnline.put("spendtime=", String.valueOf(auditProject.getSpendtime()));
                SqlConditionUtil sqlnew = new SqlConditionUtil();
                sqlnew.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sqlnew.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMapOnline, updateDateFieldMap,
                        sqlnew.getMap());
            }

            String targetUser;
            if ("3".equals(taskType)) {
                IWFInstanceAPI9 wfinstance = (IWFInstanceAPI9) ContainerFactory.getContainInfo()
                        .getComponent(IWFInstanceAPI9.class);
                IAuditProjectUnusual unusualService = (IAuditProjectUnusual) ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectUnusual.class);
                IMessagesCenterService mcservice = (IMessagesCenterService) ContainerFactory.getContainInfo()
                        .getComponent(IMessagesCenterService.class);
                ProcessVersionInstance pVersionInstance = wfinstance.getProcessVersionInstance(pviGuid);
                List<Integer> status = Arrays.asList(20);
                List<WorkflowWorkItem> list = wfinstance.getWorkItemListByPVIGuidAndStatus(pVersionInstance, status);
                if (list != null && list.size() > 0) {
                    AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();
                    auditProjectUnusual.setRowguid(UUID.randomUUID().toString());
                    auditProjectUnusual.setOperateusername("系统自动暂停计时");
                    auditProjectUnusual.setOperatedate(new Date());
                    auditProjectUnusual.setOperateuserguid("系统自动暂停计时");
                    auditProjectUnusual.setOperatetype(10);
                    auditProjectUnusual.setNote("上报件本级审批通过后自动暂停计时");
                    auditProjectUnusual.setProjectguid(projectGuid);
                    auditProjectUnusual.setPviguid(pviGuid);
                    auditProjectUnusual.setWorkitemguid(((WorkflowWorkItem) list.get(0)).getWorkItemGuid());
                    unusualService.addProjectUnusual(auditProjectUnusual);
                }

                targetUser = "";
                MessagesCenter messagesCenter = mcservice.getDetail(messageItemGuid, operateUserGuid);
                if (messagesCenter != null) {
                    targetUser = "【已暂停】" + messagesCenter.getTitle();
                    mcservice.updateMessageTitleAndShow(messagesCenter.getMessageItemGuid(), targetUser,
                            operateUserGuid);
                }
            }

            if ("1".equals(taskType) || "2".equals(taskType)) {
                String ownGuid = "";
                IAuditIndividual auditIndividualService = (IAuditIndividual) ContainerFactory.getContainInfo()
                        .getComponent(IAuditIndividual.class);
                auditRsCompanyBaseinfoService = (IAuditRsCompanyBaseinfo) ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyBaseinfo.class);
                if ("10".equals(String.valueOf(auditProject.getApplyertype()))) {
                    auditRsCompanyBaseinfo = (AuditRsCompanyBaseinfo) auditRsCompanyBaseinfoService
                            .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                    ownGuid = auditRsCompanyBaseinfo.getCompanyid();
                }
                else if ("20".equals(String.valueOf(auditProject.getApplyertype()))) {
                    AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = (AuditRsIndividualBaseinfo) auditIndividualService
                            .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    ownGuid = auditRsIndividualBaseinfo.getPersonid();
                }

                if (StringUtil.isBlank(ownGuid)) {
                    ownGuid = auditProject.getApplyeruserguid();
                }

                FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
                IHandleRSMaterial handleRSMaterialService = (IHandleRSMaterial) ContainerFactory.getContainInfo()
                        .getComponent(IHandleRSMaterial.class);
                handleRSMaterialService.insertShareMaterial(auditProject.getTaskguid(), auditProject.getRowguid(),
                        ownGuid, operateUserGuid, auditProject.getOperateusername(), auditProject.getApplyername(),
                        auditProject.getCertnum(), auditProject.getApplyertype(), auditProject.getCerttype(),
                        auditProject.getAreacode(), (String) ou.get("orgcode"));
                IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory
                        .getContainInfo().getComponent(IMessagesCenterService.class);
                if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                    targetUser = "";
                    String targetDispName = "";
                    if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                        targetUser = auditProject.getAcceptuserguid();
                        targetDispName = auditProject.getAcceptusername();
                    }
                    else {
                        targetUser = operateUserGuid;
                        targetDispName = auditProject.getOperateusername();
                    }

                    String content = "";
                    if (StringUtil.isNotBlank(auditTaskExtension.getNotify_pz())) {
                        content = auditTaskExtension.getNotify_pz();
                        content = content.replace("[#=OuName#]", auditProject.getOuname());
                        content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                        content = content.replace("[#=ApplyDate#]",
                                EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                        content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                    }

                    messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                            new Date(), auditProject.getContactmobile(), targetUser, targetDispName, operateUserGuid,
                            auditProject.getOperateusername(), "", "", "", false, "短信");
                }
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "35", operateUserGuid, "", workItemGuid);
        }
        catch (Exception var25) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var25));
        }

        return result;
    }

    public AuditCommonResult<Void> handleProjectNotPass(AuditProject auditProject, String OperateUserGuid,
            String workItemGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                    .getResult();
            IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            String taskType = auditProject.getTasktype().toString();
            if (!"1".equals(auditProject.getTasktype().toString()) || !"1".equals(auditTask.getJbjmode())) {
                AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                if (auditProjectSparetime != null && !auditProjectSparetime.isEmpty()) {
                    int spendtime = auditProjectSparetime.getSpendminutes();
                    int sparetime = auditProjectSparetime.getSpareminutes();
                    auditProject.setSparetime(sparetime);
                    auditProject.setSpendtime(spendtime);
                }

                if (auditProjectSparetime != null) {
                    projectSparetime.deleteSpareTimeByRowGuid(auditProjectSparetime.getRowguid());
                }
            }

            auditProject.setStatus(40);
            if ("1".equals(taskType) || "2".equals(taskType)) {
                auditProject.setBanjieresult(50);
                auditProject.setBanwandate(new Date());
            }

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            String targetUser;
            if ("10".equals(auditProject.getApplyway().toString())
                    || "11".equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                targetUser = null;
                if (auditProject.getApplyertype().toString().equals("10")) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = (IAuditRsCompanyBaseinfo) ContainerFactory
                            .getContainInfo().getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = (AuditRsCompanyBaseinfo) iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        targetUser = auditrscompanybaseinfo.getCompanyid();
                    }
                }

                Map<String, String> updateFieldMapOnline = new HashMap(16);
                Map<String, String> updateDateFieldMap = new HashMap(16);
                updateFieldMapOnline.put("status=", auditProject.getStatus().toString());
                updateFieldMapOnline.put("banjieresult=", String.valueOf(40));
                updateFieldMapOnline.put("spendtime=", String.valueOf(auditProject.getSpendtime()));
                SqlConditionUtil sqlnew = new SqlConditionUtil();
                sqlnew.eq("sourceguid", auditProject.getRowguid());
                if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                    sqlnew.eq("applyerguid", auditProject.getOnlineapplyerguid());
                }

                auditOnlineProjectService.updateOnlineProject(updateFieldMapOnline, updateDateFieldMap,
                        sqlnew.getMap());
            }

            IMessagesCenterService messagesCenterService = (IMessagesCenterService) ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                targetUser = "";
                String targetDispName = "";
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                }
                else {
                    targetUser = OperateUserGuid;
                    targetDispName = auditProject.getOperateusername();
                }

                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getNotify_npz())) {
                    content = auditTaskExtension.getNotify_npz();
                    content = content.replace("[#=OuName#]", auditProject.getOuname());
                    content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                    content = content.replace("[#=ApplyDate#]",
                            EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                    content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName, OperateUserGuid,
                        auditProject.getOperateusername(), "", "", "", false, "短信");
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, "36", OperateUserGuid, "", workItemGuid);
        }
        catch (Exception var17) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var17));
        }

        return result;
    }

    public AuditCommonResult<String> InitBLSPProject(String taskGuid, String projectGuid, String operateUserName,
            String operateUserGuid, String certtype, String certnum, String windowGuid, String windowName,
            String centerGuid, String biGuid, String subappGuid, String businessGuid, String certNum,
            String applyerName) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IHandleFlowSn flowSn = (IHandleFlowSn) ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
            IHandleConfig handleConfig = (IHandleConfig) ContainerFactory.getContainInfo()
                    .getComponent(IHandleConfig.class);
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(26);
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setCenterguid(centerGuid);
                auditproject.setAreacode(auditTask.getAreacode());
                IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                }

                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
            }

            auditproject.setApplyertype(Integer.parseInt("10"));
            auditproject.setApplydate(new Date());
            auditproject.setApplyway(Integer.parseInt("20"));
            if (StringUtil.isNotBlank(certtype)) {
                auditproject.setCerttype(certtype);
            }
            else {
                auditproject.setCerttype("16");
            }

            if (StringUtil.isNotBlank(certnum)) {
                auditproject.setCertnum(certnum);
            }
            else {
                auditproject.setCertnum(certNum);
            }

            IAuditRsItemBaseinfo auditRsItemBaseinfoImpl = (IAuditRsItemBaseinfo) ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsItemBaseinfo.class);
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("BIGUID", biGuid);
            AuditRsItemBaseinfo auditRsItemBaseinfo = (AuditRsItemBaseinfo) ((List) auditRsItemBaseinfoImpl
                    .selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult()).get(0);
            auditproject.setXiangmubh(auditRsItemBaseinfo.getRowguid());
            auditproject.setApplyername(applyerName);
            auditproject.setBiguid(biGuid);
            auditproject.set("subappguid", subappGuid);
            auditproject.setBusinessguid(businessGuid);
            auditproject.setReceivedate(new Date());
            auditproject.setReceiveuserguid(operateUserGuid);
            auditproject.setReceiveusername(operateUserName);
            auditproject.setIs_test(Integer.parseInt("0"));
            auditproject.setIs_delay(20);
            /*String numberFlag = (String) handleConfig.getFrameConfig("AS_FLOWSN_PRE", centerGuid).getResult();
            if (StringUtil.isBlank(numberFlag)) {
                numberFlag = "STD";
            }*/

            String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());

            auditproject.setFlowsn(flowsn);

            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);
            result.setResult(rowGuid);
        }
        catch (Exception var27) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var27));
        }

        return result;
    }

    /**
     * 初始化办件，个性化流水号
     */
    public AuditCommonResult<String> InitIntegratedProject(String taskGuid, String projectGuid, String operateUserName,
            String operateUserGuid, String windowGuid, String windowName, String centerGuid, String biGuid,
            String subappGuid, String businessGuid, String applyway) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IHandleFlowSn flowSn = (IHandleFlowSn) ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
            IHandleConfig handleConfig = (IHandleConfig) ContainerFactory.getContainInfo()
                    .getComponent(IHandleConfig.class);
            IAuditSpInstance iAuditSpInstance = (IAuditSpInstance) ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpInstance.class);
            AuditSpInstance auditSpInstance = (AuditSpInstance) iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
            IAuditSpIntegratedCompany iAuditSpIntegratedCompany = (IAuditSpIntegratedCompany) ContainerFactory
                    .getContainInfo().getComponent(IAuditSpIntegratedCompany.class);
            AuditSpIntegratedCompany auditSpIntegratedCompany = (AuditSpIntegratedCompany) iAuditSpIntegratedCompany
                    .getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid()).getResult();
            IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            AuditOnlineProject auditOnlineProject = (AuditOnlineProject) iAuditOnlineProject
                    .getOnlineProjectByTaskGuid(biGuid, businessGuid).getResult();
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = (IAuditTask) ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = (AuditTask) auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(26);
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setCenterguid(centerGuid);
                auditproject.setAreacode(auditTask.getAreacode());
                IAuditTaskExtension auditTaskExtensionService = (IAuditTaskExtension) ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                }

                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
            }

            auditproject.setApplyertype(Integer.parseInt("10"));
            auditproject.setApplydate(new Date());
            auditproject.setApplyway(Integer.parseInt(applyway));
            auditproject.setCerttype("16");
            auditproject.setBiguid(biGuid);
            auditproject.setSubappguid(subappGuid);
            auditproject.setReceivedate(new Date());
            auditproject.setReceiveuserguid(operateUserGuid);
            auditproject.setReceiveusername(operateUserName);
            auditproject.setIs_test(Integer.parseInt("0"));
            auditproject.setIs_delay(20);
            auditproject.setApplyername(auditSpIntegratedCompany.getCompanyname());
            if (!"10".equals(applyway) && !"11".equals(applyway)) {
                auditproject.setApplyeruserguid(auditSpIntegratedCompany.getRowguid());
            }
            else {
                auditproject.setApplyeruserguid(auditOnlineProject.getApplyerguid());
            }

            auditproject.setCertnum(auditSpIntegratedCompany.getZzjgdmz());
            auditproject.setAddress(auditSpIntegratedCompany.getAddress());
            auditproject.setLegal(auditSpIntegratedCompany.getLegalpersonname());
            auditproject.setContactperson(auditSpIntegratedCompany.getContactperson());
            auditproject.setContactcertnum(auditSpIntegratedCompany.getContactcertnum());
            auditproject.setContactphone(auditSpIntegratedCompany.getContactphone());
            auditproject.setContactpostcode(auditSpIntegratedCompany.getContactpostcode());
            auditproject.setContactemail(auditSpIntegratedCompany.getLegalpersonemail());
             /*String numberFlag = (String) handleConfig.getFrameConfig("AS_FLOWSN_PRE", centerGuid).getResult();
            if (StringUtil.isBlank(numberFlag)) {
                numberFlag = "STD";
            }*/
            String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());

            auditproject.setFlowsn(flowsn);
            

            auditproject.setBusinessguid(businessGuid);
            IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditProject.class);
            log.info("产生的auditTask："+auditTask);
            if (auditTask != null && "1".equals(auditTask.getIs_enable().toString())) {
                auditProjectService.addProject(auditproject);
                log.info("新增办件成功，flowsn："+auditproject);
            }

            result.setResult(rowGuid);
        }
        catch (Exception var27) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var27));
        }

        return result;
    }

    public AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String taskGuid,
            String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
            String projectguid, String taskCaseguid) {
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
            auditproject.setApplyway(Integer.parseInt("10"));
            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
            auditproject.setCerttype("22");
            auditproject.setApplyertype(20);
            auditproject.setApplyeruserguid(applyerGuid);
            auditproject.setApplyername(applyerUserName);
            auditproject.setCertnum(certNum);
            auditproject.setTaskid(auditTask.getTask_id());
            auditproject.setTask_id(auditTask.getTask_id());
            Map<String, Integer> caseMap = new HashMap(16);
            if (StringUtil.isNotBlank(auditproject) && StringUtil.isNotBlank(taskCaseguid)) {
                auditproject.setTaskcaseguid(taskCaseguid);
                List<AuditTaskMaterialCase> auditTaskMaterialCases = (List) auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseguid).getResult();
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    Iterator var19 = auditTaskMaterialCases.iterator();

                    while (var19.hasNext()) {
                        AuditTaskMaterialCase auditTaskMaterialCase = (AuditTaskMaterialCase) var19.next();
                        caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                    }
                }
            }

            auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());
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
            auditOnlineProject.setStatus(String.valueOf(auditproject.getStatus()));
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
            IAttachService attachService = (IAttachService) ContainerFactory.getContainInfo()
                    .getComponent(IAttachService.class);
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = (List) auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            List<AuditProjectMaterial> returnmaterials = new ArrayList();
            Iterator var29 = auditTaskMaterials.iterator();

            while (var29.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) var29.next();
                Boolean flag = false;
                if (StringUtil.isNotBlank(taskCaseguid) && caseMap.containsKey(auditTaskMaterial.getRowguid())) {
                    flag = true;
                }

                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterial.setOperatedate(new Date());
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    List<CertInfo> certInfos = iCertInfoExternal.selectCertByOwner(
                            auditTaskMaterial.getSharematerialguid(), "001", "", certNum, false,
                            auditproject.getAreacode(), (Map) null);
                    if (certInfos != null && certInfos.size() > 0) {
                        CertInfo certInfo = (CertInfo) certInfos.get(0);
                        if (certInfo != null) {
                            int count = attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid());
                            if (count > 0) {
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                attachService.copyAttachByClientGuid(certInfo.getCertcliengguid(), "从共享材料库引用",
                                        (String) null, auditProjectMaterial.getCliengguid());
                                auditProjectMaterial.setStatus(20);
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                            }
                            else {
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                            }
                        }
                    }
                }

                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
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
        catch (Exception var36) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var36));
        }

        return result;
    }

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
            if (Integer.parseInt("2") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("11"));
            }
            else if (Integer.parseInt("1") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("10"));
            }

            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
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
            Map<String, Integer> caseMap = new HashMap(16);
            if (StringUtil.isNotBlank(auditproject) && StringUtil.isNotBlank(taskCaseguid)) {
                auditproject.setTaskcaseguid(taskCaseguid);
                List<AuditTaskMaterialCase> auditTaskMaterialCases = (List) auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseguid).getResult();
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    Iterator var22 = auditTaskMaterialCases.iterator();

                    while (var22.hasNext()) {
                        AuditTaskMaterialCase auditTaskMaterialCase = (AuditTaskMaterialCase) var22.next();
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
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = (List) auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            List<AuditProjectMaterial> returnmaterials = new ArrayList();
            Iterator var34 = auditTaskMaterials.iterator();

            while (var34.hasNext()) {
                AuditTaskMaterial auditTaskMaterial = (AuditTaskMaterial) var34.next();
                Boolean flag = false;
                if (StringUtil.isNotBlank(taskCaseguid) && caseMap.containsKey(auditTaskMaterial.getRowguid())) {
                    flag = true;
                }

                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
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
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var46));
        }

        return result;
    }

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
            if (Integer.parseInt("2") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("11"));
            }
            else if (Integer.parseInt("1") == applyWay) {
                auditproject.setApplyway(Integer.parseInt("10"));
            }

            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
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
            Map<String, Integer> caseMap = new HashMap(16);
            if (StringUtil.isNotBlank(auditproject) && StringUtil.isNotBlank(taskCaseguid)) {
                auditproject.setTaskcaseguid(taskCaseguid);
                List<AuditTaskMaterialCase> auditTaskMaterialCases = (List) auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseguid).getResult();
                if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                    Iterator var24 = auditTaskMaterialCases.iterator();

                    while (var24.hasNext()) {
                        AuditTaskMaterialCase auditTaskMaterialCase = (AuditTaskMaterialCase) var24.next();
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
                if (StringUtil.isNotBlank(taskCaseguid) && caseMap.containsKey(auditTaskMaterial.getRowguid())) {
                    flag = true;
                }

                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus("10");
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
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

    public AuditCommonResult<Void> InitOnlineProjectForBusiness(String businessguid, String applyerGuid,
            String areaCode, String applyerUserName, String biguid, String subappGuid, String yewuguid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditSpInstance iAuditSpInstance = (IAuditSpInstance) ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpInstance.class);
            IAuditSpBusiness iAuditSpBusiness = (IAuditSpBusiness) ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpBusiness.class);
            AuditSpInstance auditSpInstance = (AuditSpInstance) iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
            AuditSpBusiness auditSpBusiness = (AuditSpBusiness) iAuditSpBusiness
                    .getAuditSpBusinessByRowguid(businessguid).getResult();
            AuditOnlineProject auditOnlineProject = new AuditOnlineProject();
            auditOnlineProject.setRowguid(UUID.randomUUID().toString());
            auditOnlineProject.setSourceguid(biguid);
            auditOnlineProject.setCreatedate(new Date());
            auditOnlineProject.setAreacode(areaCode);
            auditOnlineProject.setProjectname(auditSpBusiness.getBusinessname());
            auditOnlineProject.setApplyerguid(applyerGuid);
            auditOnlineProject.setApplyername(auditSpInstance.getApplyername());
            auditOnlineProject.setApplydate(new Date());
            auditOnlineProject.setOperatedate(new Date());
            auditOnlineProject.setTaskguid(businessguid);
            auditOnlineProject.setIs_fee(0);
            auditOnlineProject.setIs_evaluat(0);
            auditOnlineProject.setType("1");
            auditOnlineProject.setStatus(String.valueOf(10));
            IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);
        }
        catch (Exception var15) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var15));
        }

        return result;
    }

    public AuditCommonResult<Void> saveOperateLog(AuditProject auditProject, String OpeateUserGuid,
            String OpeateUserName, String OperateType, String remark) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, OperateType, OpeateUserGuid, OpeateUserName, "", remark);
        }
        catch (Exception var8) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var8));
        }

        return result;
    }

    public AuditCommonResult<Void> handleLogistics(AuditProject auditProject,
            AuditLogisticsBasicinfo auditLogisticsBasicinfo) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditLogisticsBasicInfo auditLogisticsBasicInfoService = (IAuditLogisticsBasicInfo) ContainerFactory
                    .getContainInfo().getComponent(IAuditLogisticsBasicInfo.class);
            auditLogisticsBasicinfo.setRowguid(UUID.randomUUID().toString());
            auditLogisticsBasicinfo.setProjectguid(auditProject.getRowguid());
            auditLogisticsBasicinfo.setChk_code(String.valueOf((int) ((Math.random() * 9.0D + 1.0D) * 100000.0D)));
            auditLogisticsBasicInfoService.addLogisticsBasicinfo(auditLogisticsBasicinfo);
        }
        catch (Exception var5) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var5));
        }

        return result;
    }

    public AuditCommonResult<Void> updateLogistics(AuditProject auditProject,
            AuditLogisticsBasicinfo auditLogisticsBasicinfo) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            IAuditLogisticsBasicInfo auditLogisticsBasicInfoService = (IAuditLogisticsBasicInfo) ContainerFactory
                    .getContainInfo().getComponent(IAuditLogisticsBasicInfo.class);
            auditLogisticsBasicInfoService.updateLogisticsBasicinfo(auditLogisticsBasicinfo);
        }
        catch (Exception var5) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var5));
        }

        return result;
    }

    public AuditCommonResult<Void> addCompanyOnlineProject(AuditProject auditProject, String declarerguid,
            String declarername, String applyerGuid, String applyerName, String companyrowguid, String sqGuid) {
        AuditCommonResult result = new AuditCommonResult();

        try {
            AuditOnlineProject auditOnlineProject = new AuditOnlineProject();
            auditOnlineProject.setRowguid(UUID.randomUUID().toString());
            auditOnlineProject.setAreacode(auditProject.getAreacode());
            auditOnlineProject.setSourceguid(auditProject.getRowguid());
            auditOnlineProject.setCenterguid(auditProject.getCenterguid());
            auditOnlineProject.setCreatedate(new Date());
            auditOnlineProject.setApplyerguid(applyerGuid);
            auditOnlineProject.setApplyername(applyerName);
            auditOnlineProject.setIs_charge(auditProject.getIs_charge());
            auditOnlineProject.setIs_check(auditProject.getIs_check());
            auditOnlineProject.setIs_evaluat(auditProject.getIs_evaluat());
            auditOnlineProject.setIs_fee(auditProject.getIs_fee());
            auditOnlineProject.setOperatedate(new Date());
            auditOnlineProject.setOuguid(auditProject.getOuguid());
            auditOnlineProject.setOuname(auditProject.getOuname());
            auditOnlineProject.setProjectname(auditProject.getProjectname());
            auditOnlineProject.setTaskguid(auditProject.getTaskguid());
            auditOnlineProject.setIs_fee(0);
            auditOnlineProject.setIs_evaluat(0);
            auditOnlineProject.setType("0");
            auditOnlineProject.setDeclarerguid(declarerguid);
            auditOnlineProject.setDeclarername(declarername);
            auditOnlineProject.setApplyertype(auditProject.getApplyertype().toString());
            auditOnlineProject.setStatus(String.valueOf(auditProject.getStatus()));
            auditOnlineProject.setTaskcaseguid(auditProject.getTaskcaseguid());
            auditOnlineProject.setTasktype(String.valueOf(auditProject.getTasktype()));
            auditOnlineProject.setStatus(String.valueOf(10));
            auditOnlineProject.setCompanyrowguid(companyrowguid);
            auditOnlineProject.setSqGuid(sqGuid);
            if (StringUtil.isNotBlank(auditProject.getCurrentareacode())) {
                auditOnlineProject.setCurrentareacode(auditProject.getCurrentareacode());
            }

            IAuditOnlineProject iAuditOnlineProject = (IAuditOnlineProject) ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);
        }
        catch (Exception var11) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(var11));
        }

        return result;
    }

    public String getStrFlowSn(String numberName, String numberFlag, int snLength) {

        String flowSn = "";

        Object[] args = new Object[3];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(snLength);

        try {

            flowSn = new TAHandleProjectService()
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn2", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }

    private String getStdFlowSn(String numberName, String numberFlag, int theYearLength, int snLength) {
        String flowSn = "";
        Object[] args = new Object[7];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));

        args[3] = Integer.valueOf(theYearLength);

        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);

        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));

        args[6] = Integer.valueOf(snLength);

        try {

            flowSn = new TAHandleProjectService()
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }

    @Override
    public AuditCommonResult<String> handleReceive(AuditProject var1, String var2, String var3, String var4,
            String var5) {
        return null;
    }

}

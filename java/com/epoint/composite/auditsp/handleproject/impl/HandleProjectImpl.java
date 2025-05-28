package com.epoint.composite.auditsp.handleproject.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditonlineproject.auditapplyjslog.inter.IAuditApplyJsLog;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectcharge.inter.IAuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectchargedetail.inter.IAuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
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
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.audityjscsb.domain.AuditYjsCsb;
import com.epoint.basic.auditsp.audityjscsb.inter.IAuditYjsCsbService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibrary;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibraryR;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibraryR;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.*;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handledoc.inter.IHandleDoc;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handleproject.service.HandleProjectService;
import com.epoint.composite.auditsp.handleproject.service.JnHandleProjectService;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.workingday.api.IWorkingdayService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.util.TARequestUtil;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
@SuppressWarnings("unused")
public class HandleProjectImpl implements IHandleProject {
    private Logger log = LogUtil.getLog(HandleProjectImpl.class);

    public static final String lcjbxxurl = ConfigUtil.getConfigValue("epointframe", "lcjbxxurl");

    public static final String lclcurl = ConfigUtil.getConfigValue("epointframe", "lclcurl");

    @Override
    public AuditCommonResult<String> InitProject(String taskGuid, String projectGuid, String operateUserName,
                                                 String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum, String qno,
                                                 String acceptareacode, String cityLevel) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String rowGuid = this.InitProject(taskGuid, projectGuid, operateUserName, operateUserGuid, windowGuid,
                    windowName, centerGuid, certNum, qno, acceptareacode, cityLevel, "").getResult();
            result.setResult(rowGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> InitProject(String taskGuid, String projectGuid, String operateUserName,
                                                 String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum,
                                                 String qno) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_INIT);// 办件状态：初始化
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setOuname(auditTask.getOuname());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setIf_express(ZwfwConstant.CONSTANT_STR_ZERO);
                auditproject.setCenterguid(centerGuid);
                String areacode = auditTask.getAreacode();
                auditproject.setAreacode(areacode);
                IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                    auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());// 进驻大厅
                    auditproject.setIf_express(auditTaskExtension.getIf_express());
                }
                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
                // 企业类型
                if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditTask.getApplyertype())) {
                    auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));
                    auditproject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);// 申请人证照类型：组织机构代码
                } else {
                    // 个人类型
                    auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));
                    auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
                }
            }
            auditproject.setApplydate(new Date());// 办件申请时间
            auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 办件申请方式：窗口申请
            // auditproject.setIssendsms(StringUtil.isBlank(asissendmessage)
            // ? ZwfwConstant.CONSTANT_STR_ZERO
            // : ZwfwConstant.CONSTANT_STR_ONE);// 是否发送短信
            auditproject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.setIs_delay(20);// 是否延期
            // 排队叫号系统传递过来的参数处理
            if (StringUtil.isNotBlank(qno)) {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                auditproject.setCertnum(certNum);
                // TODO 这个地方需要调整

                IAuditQueueUserinfo userinfoservice = ContainerFactory.getContainInfo()
                        .getComponent(IAuditQueueUserinfo.class);
                Record userinfo = userinfoservice.getUserByIdentityCardNum(certNum).getResult();

                if (userinfo != null) {
                    auditproject.setApplyername(userinfo.get("DisplayName"));
                    auditproject.setAddress(userinfo.get("address"));
                    auditproject.setContactmobile(userinfo.get("Mobile"));
                }
                // 更新排队叫号与办件关联关系
                IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
                queueservice.updateQNOProject(rowGuid, qno, centerGuid);

            }
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);

            // 直接赋值给相关数据ProjectGuid
            // wfcontextvalueservice.createOrUpdateContext(processVersionInstanceGuid,
            // "ProjectGuid",
            // auditproject.getRowguid(),
            // WorkflowKeyNames9.ParameterType_T_string, 2);

            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);

            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(auditTask == null ? "" : auditTask.getRowguid(), true)
                    .getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                auditProjectMaterial.clear();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask == null ? "" : auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());

                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
            }
            result.setResult(rowGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditProject> InitOnlineProject(String taskGuid, String centerGuid, String areaCode,
                                                             String applyerGuid, String applyerUserName, String certNum) {
        AuditCommonResult<AuditProject> result = new AuditCommonResult<AuditProject>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskGuid, true)
                    .getResult();
            // 创建办件
            AuditProject auditproject = new AuditProject();
            String projectguid = UUID.randomUUID().toString();

            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
            auditproject.setTaskguid(taskGuid);
            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);// 办件状态：外网初始化
            auditproject.setPromise_day(auditTask.getPromise_day());
            auditproject.setApplydate(new Date());
            auditproject.setIs_delay(20);
            auditproject.setProjectname(auditTask.getTaskname());
            auditproject.setInsertdate(new Date());
            auditproject.setIs_charge(auditTask.getCharge_flag());
            auditproject.setCharge_when(auditTaskExtension.getCharge_when());// 收费的时间，受理前还是办结前
            auditproject.setTasktype(auditTask.getType());
            auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS));
            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
            auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 身份证
            auditproject.setApplyertype(20);
            auditproject.setApplyeruserguid(applyerGuid);
            auditproject.setApplyername(applyerUserName);
            auditproject.setCertnum(certNum);
            auditproject.setTask_id(auditTask.getTask_id());
            auditproject.setIs_test(ZwfwConstant.CONSTANT_INT_ZERO);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
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
            auditOnlineProject.setIs_fee(0); // 办件未收讫
            auditOnlineProject.setIs_evaluat(0);// 办件未评价
            auditOnlineProject.setType("0");// 0，套餐式：1。默认为0
            auditOnlineProject.setStatus(String.valueOf(auditproject.getStatus()));
            IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);

            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            // 初始化材料
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterial.setOperatedate(new Date());
                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);

            }
            result.setResult(auditproject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> saveProject(AuditProject auditProject) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 1、保存信息
            if (auditProject.getStatus() <= ZwfwConstant.BANJIAN_STATUS_DJJ
                    && auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_INIT) {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_DJJ);
            }
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
            // 2、保存申请人信息
            String rowGuid_GR = UUID.randomUUID().toString();// 个人信息标识
            String rowGuid_QY = UUID.randomUUID().toString();// 企业信息标识
            // 申请人类型：个人
            if (ZwfwConstant.APPLAYERTYPE_GR.equals(auditProject.getApplyertype().toString())) {
                IAuditIndividual individualService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditIndividual.class);
                // 根据身份证号取出个人信息,如果已经在人员库中存在则更新信息,不存在则在人员库中新增
                AuditRsIndividualBaseinfo auditindividual = individualService
                        .getAuditRsIndividualBaseinfoByIDNumber(auditProject.getCertnum()).getResult();
                // 当前办件关联的个人信息
                AuditRsIndividualBaseinfo auditprojectindividual = individualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                if (auditindividual != null) {
                    auditindividual.setApplyerinfosource(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 申报人信息来源
                    auditindividual.setIdnumber(auditProject.getCertnum());// 身份证号
                    auditindividual.setClientname(auditProject.getApplyername());// 申请人名称
                    auditindividual.setContactperson(auditProject.getContactperson());// 联系人名称
                    auditindividual.setContactphone(auditProject.getContactphone());// 联系电话
                    auditindividual.setContactmobile(auditProject.getContactmobile());// 手机号码
                    auditindividual.setContactpostcode(auditProject.getContactpostcode());// 邮编
                    auditindividual.setContactemail(auditProject.getContactemail());// 邮箱
                    auditindividual.setContactfax(auditProject.getContactfax());// 传真
                    auditindividual.setDeptaddress(auditProject.getAddress());// 地址
                    auditindividual.setContactcertnum(auditProject.getContactcertnum());// 联系人身份证号

                    individualService.updateAuditRsIndividualBaseinfo(auditindividual);
                    rowGuid_GR = auditindividual.getRowguid();
                } // 如果逻辑删除人，但是办件在
                else if (auditprojectindividual != null) {
                    auditprojectindividual.setIs_history(ZwfwConstant.CONSTANT_STR_ZERO);
                }
                else {
                    auditindividual = new AuditRsIndividualBaseinfo();
                    // 如果不存在外网用户，则新增外网用户
                    // personid 为个人用户唯一标识，当个人存在多条数据的时候，personid不变
                    String personid = UUID.randomUUID().toString();// 个人信息标识
                    auditindividual = new AuditRsIndividualBaseinfo();
                    if (StringUtil.isNotBlank(auditProject.getApplyeruserguid())) {
                        auditindividual.setRowguid(auditProject.getApplyeruserguid());
                    } else {
                        auditindividual.setRowguid(rowGuid_GR);
                    }
                    auditindividual.setPersonid(personid);
                    auditindividual.setApplyerinfosource(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 申报人信息来源
                    auditindividual.setIdnumber(auditProject.getCertnum());// 身份证号
                    auditindividual.setClientname(auditProject.getApplyername());// 申请人名称
                    auditindividual.setContactperson(auditProject.getContactperson());// 联系人名称
                    auditindividual.setContactphone(auditProject.getContactphone());// 联系电话
                    auditindividual.setContactmobile(auditProject.getContactmobile());// 手机号码
                    auditindividual.setContactpostcode(auditProject.getContactpostcode());// 邮编
                    auditindividual.setContactemail(auditProject.getContactemail());// 邮箱
                    auditindividual.setContactfax(auditProject.getContactfax());// 传真
                    auditindividual.setDeptaddress(auditProject.getAddress());// 地址
                    auditindividual.setContactcertnum(auditProject.getContactcertnum());// 联系人身份证号
                    auditindividual.setIs_history("0");
                    auditindividual.setVersion(1);
                    auditindividual.setVersiontime(new Date());
                    individualService.addAuditRsIndividualBaseinfo(auditindividual);
                    rowGuid_GR = auditindividual.getRowguid();
                }
                // 设置申请人标识
                auditProject.setApplyeruserguid(rowGuid_GR);
            }
            // 申请人类型：企业
            else {
                IAuditRsCompanyLegal rsCompanyLegalService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyLegal.class);
                IAuditRsCompanyRegister rsCompanyRegisterService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyRegister.class);
                String certtype = auditProject.getCerttype();// 申请人证照类型
                IAuditRsCompanyBaseinfo companyService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyBaseinfo.class);
                // 证照类型：组织机构代码证
                if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certtype)) {
                    // 根据组织机构代码证取出企业信息,如果已经在企业库中存在则更新信息,不存在则在企业库中新增
                    AuditRsCompanyBaseinfo auditcompany = companyService
                            .getCompanyByOneField("organcode", auditProject.getCertnum()).getResult();
                    if (auditcompany != null) {
                        // auditcompany.setApplyerinfosource(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));//
                        // 申报人信息来源
                        auditcompany.setOrgancode(auditProject.getCertnum());// 身份证号
                        auditcompany.setOrganname(auditProject.getApplyername());// 申请人名称
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());
                        /*
                         * // 如果
                         * if (auditcompany.getOrgalegal_idnumber().equals(
                         * auditProject.getContactcertnum())) {
                         * auditcompany.setOrgalegal_idnumber(auditProject.
                         * getContactcertnum());// 联系人身份号
                         * }
                         */
                        /*
                         * auditcompany.setTotalinvestment(auditProject.
                         * getTotalinvestment());
                         * auditcompany.setCompanytype(auditProject.
                         * getCompanytype());
                         * auditcompany.setCompycontactaddress(auditProject.
                         * getCompycontactaddress());
                         * auditcompany.setRegisteredcapital(auditProject.
                         * getRegisteredcapital());
                         * auditcompany.setManagementtype(auditProject.
                         * getManagementtype());
                         * auditcompany.setRegisteraddress(auditProject.
                         * getCompyjyaddress());
                         * auditcompany.setAdmindivision(auditProject.
                         * getAdmindivision());
                         * auditcompany.setCompycontactphone(auditProject.
                         * getCompycontactphone());
                         */

                        companyService.updateAuditRsCompanyBaseinfo(auditcompany);
                        AuditRsCompanyLegal auditRsCompanyLegal = rsCompanyLegalService
                                .getAuditRsCompanyLegalByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyLegal != null) {
                            // 新增企业额外信息放入法人表
                            auditRsCompanyLegal.setContactperson(auditProject.getContactperson());
                            auditRsCompanyLegal.setContactfax(auditProject.getContactfax());
                            auditRsCompanyLegal.setContactemail(auditProject.getContactemail());
                            auditRsCompanyLegal.setContactmobile(auditProject.getContactmobile());
                            auditRsCompanyLegal.setContactpostcode(auditProject.getContactpostcode());

                            auditRsCompanyLegal.setLegalname(auditProject.getLegal());
                            rsCompanyLegalService.updateAuditRsCompanyLegal(auditRsCompanyLegal);
                        }

                        AuditRsCompanyRegister auditRsCompanyRegister = rsCompanyRegisterService
                                .getAuditRsCompanyRegisterByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyRegister != null) {
                            auditRsCompanyRegister.setContactphone(auditProject.getContactphone());
                            auditRsCompanyRegister.setBusinessaddress(auditProject.getAddress());
                            rsCompanyRegisterService.updateAuditRsCompanyRegister(auditRsCompanyRegister);
                        }

                        /*
                         * auditcompany.setContactperson(auditProject.
                         * getContactperson());// 联系人名称
                         * auditcompany.setContactphone(auditProject.
                         * getContactphone());// 联系电话
                         * auditcompany.setContactmobile(auditProject.
                         * getContactmobile());// 手机号码
                         * auditcompany.setContactpostcode(auditProject.
                         * getContactpostcode());// 邮编
                         * auditcompany.setContactemail(auditProject.
                         * getContactemail());// 邮箱
                         * auditcompany.setContactfax(auditProject.getContactfax
                         * ());// 传真
                         * auditcompany.setDeptaddress(auditProject.getAddress()
                         * );// 地址
                         * auditcompany.setLegal(auditProject.getLegal());//
                         * 法定代表人
                         * companyService.updateAuditCompany(auditcompany);
                         */
                        rowGuid_QY = auditcompany.getRowguid();
                    }
                    // 证照类型：组织机构代码证
                    else {
                        String companyid = UUID.randomUUID().toString();
                        auditcompany = new AuditRsCompanyBaseinfo();
                        auditcompany.setRowguid(UUID.randomUUID().toString());
                        auditcompany.setOrgancode(auditProject.getCertnum());// 身份证号
                        auditcompany.setOrganname(auditProject.getApplyername());// 申请人名称
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());// 联系人身份号
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());
                        auditcompany.setCompanyid(companyid);
                        auditcompany.setVersion(1);
                        auditcompany.setVersiontime(new Date());
                        auditcompany.setIs_history("0");
                        companyService.addAuditRsCompanyBaseinfo(auditcompany);

                        AuditRsCompanyLegal auditRsCompanyLegal = new AuditRsCompanyLegal();
                        auditRsCompanyLegal.setLegalname(auditProject.getLegal());
                        auditRsCompanyLegal.setOrgancode(auditProject.getCertnum());
                        auditRsCompanyLegal.setRowguid(UUID.randomUUID().toString());
                        // 新增企业额外信息放入法人表
                        auditRsCompanyLegal.setContactperson(auditProject.getContactperson());
                        auditRsCompanyLegal.setContactfax(auditProject.getContactfax());
                        auditRsCompanyLegal.setContactemail(auditProject.getContactemail());
                        auditRsCompanyLegal.setContactmobile(auditProject.getContactmobile());
                        auditRsCompanyLegal.setContactpostcode(auditProject.getContactpostcode());

                        auditRsCompanyLegal.setIs_history("0");
                        auditRsCompanyLegal.setCompanyid(companyid);
                        auditRsCompanyLegal.setVersion(1);
                        auditRsCompanyLegal.setVersiontime(new Date());
                        rsCompanyLegalService.addAuditRsCompanyLegal(auditRsCompanyLegal);

                        AuditRsCompanyRegister auditRsCompanyRegister = new AuditRsCompanyRegister();
                        auditRsCompanyRegister.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister.setContactphone(auditProject.getContactphone());
                        auditRsCompanyRegister.setBusinessaddress(auditProject.getAddress());
                        auditRsCompanyRegister.setOrgancode(auditProject.getCertnum());
                        auditRsCompanyRegister.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister.setIs_history("0");
                        auditRsCompanyRegister.setCompanyid(companyid);
                        auditRsCompanyRegister.setVersion(1);
                        auditRsCompanyRegister.setVersiontime(new Date());
                        rsCompanyRegisterService.addAuditRsCompanyRegister(auditRsCompanyRegister);
                        rowGuid_QY = auditcompany.getRowguid();

                        /*
                         * auditcompany.setContactperson(auditProject.
                         * getContactperson());// 联系人名称
                         * auditcompany.setContactphone(auditProject.
                         * getContactphone());// 联系电话
                         * auditcompany.setContactmobile(auditProject.
                         * getContactmobile());// 手机号码
                         * auditcompany.setContactpostcode(auditProject.
                         * getContactpostcode());// 邮编
                         * auditcompany.setContactemail(auditProject.
                         * getContactemail());// 邮箱
                         * auditcompany.setContactfax(auditProject.getContactfax
                         * ());// 传真
                         * auditcompany.setDeptaddress(auditProject.getAddress()
                         * );// 地址
                         * auditcompany.setLegal(auditProject.getLegal());//
                         * 法定代表人 companyService.addAuditCompany(auditcompany);
                         */
                    }
                }
                // 证照类型：工商营业执照
                else {
                    // 根据工商营业执照证取出企业信息,如果已经在企业库中存在则更新信息,不存在则在企业库中新增
                    AuditRsCompanyBaseinfo auditcompany = null;
                    if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(certtype)) {
                        // 工商营业执照
                        auditcompany = companyService.getCompanyByOneField("businesslicense", auditProject.getCertnum())
                                .getResult();
                    } else {
                        // 统一社会信用代码
                        auditcompany = companyService.getCompanyByOneField("creditcode", auditProject.getCertnum())
                                .getResult();
                    }

                    if (auditcompany != null) {
                        auditcompany.setOrganname(auditProject.getApplyername());// 申请人名称
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());// 法人
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());// 联系人

                        companyService.updateAuditRsCompanyBaseinfo(auditcompany);

                        AuditRsCompanyLegal auditRsCompanyLegal = rsCompanyLegalService
                                .getAuditRsCompanyLegalByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyLegal != null) {
                            // 新增企业额外信息放入法人表
                            auditRsCompanyLegal.setContactperson(auditProject.getContactperson());
                            auditRsCompanyLegal.setContactfax(auditProject.getContactfax());
                            auditRsCompanyLegal.setContactemail(auditProject.getContactemail());
                            auditRsCompanyLegal.setContactmobile(auditProject.getContactmobile());
                            auditRsCompanyLegal.setContactpostcode(auditProject.getContactpostcode());
                            auditRsCompanyLegal.setLegalname(auditProject.getLegal());
                            rsCompanyLegalService.updateAuditRsCompanyLegal(auditRsCompanyLegal);
                        }

                        AuditRsCompanyRegister auditRsCompanyRegister = rsCompanyRegisterService
                                .getAuditRsCompanyRegisterByCompanyid(auditcompany.getCompanyid()).getResult();
                        if (auditRsCompanyRegister != null) {
                            auditRsCompanyRegister.setContactphone(auditProject.getContactphone());
                            auditRsCompanyRegister.setBusinessaddress(auditProject.getAddress());
                            rsCompanyRegisterService.updateAuditRsCompanyRegister(auditRsCompanyRegister);
                        }
                        rowGuid_QY = auditcompany.getRowguid();
                    }
                    // 证照类型：工商营业执照
                    else {
                        String companyid = UUID.randomUUID().toString();
                        auditcompany = new AuditRsCompanyBaseinfo();
                        auditcompany.setRowguid(rowGuid_QY);
                        auditcompany.setCreditcode(auditProject.getCertnum());// 身份证号
                        auditcompany.setOrganname(auditProject.getApplyername());// 申请人名称
                        auditcompany.setOrganlegal(auditProject.getLegal());
                        auditcompany.setOrgalegal_idnumber(auditProject.getLegalid());// 联系人身份号
                        auditcompany.setContactcertnum(auditProject.getContactcertnum());// 联系人身份号
                        auditcompany.setCompanyid(companyid);
                        auditcompany.setVersion(1);
                        auditcompany.setVersiontime(new Date());
                        auditcompany.setIs_history("0");
                        companyService.addAuditRsCompanyBaseinfo(auditcompany);

                        // 扬州个性化功能
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

                        AuditRsCompanyLegal auditRsCompanyLegal = new AuditRsCompanyLegal();
                        auditRsCompanyLegal.setLegalname(auditProject.getLegal());
                        auditRsCompanyLegal.setCreditcode(auditProject.getCertnum());
                        auditRsCompanyLegal.setRowguid(UUID.randomUUID().toString());
                        // 新增企业额外信息放入法人表
                        auditRsCompanyLegal.setContactperson(auditProject.getContactperson());
                        auditRsCompanyLegal.setContactfax(auditProject.getContactfax());
                        auditRsCompanyLegal.setContactemail(auditProject.getContactemail());
                        auditRsCompanyLegal.setContactmobile(auditProject.getContactmobile());
                        auditRsCompanyLegal.setContactpostcode(auditProject.getContactpostcode());

                        auditRsCompanyLegal.setIs_history("0");
                        auditRsCompanyLegal.setCompanyid(companyid);
                        auditRsCompanyLegal.setVersion(1);
                        auditRsCompanyLegal.setVersiontime(new Date());
                        rsCompanyLegalService.addAuditRsCompanyLegal(auditRsCompanyLegal);

                        AuditRsCompanyRegister auditRsCompanyRegister = new AuditRsCompanyRegister();
                        auditRsCompanyRegister.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister.setContactphone(auditProject.getContactphone());
                        auditRsCompanyRegister.setBusinessaddress(auditProject.getAddress());
                        auditRsCompanyRegister.setCreditcode(auditProject.getCertnum());
                        auditRsCompanyRegister.setRowguid(UUID.randomUUID().toString());
                        auditRsCompanyRegister.setIs_history("0");
                        auditRsCompanyRegister.setCompanyid(companyid);
                        auditRsCompanyRegister.setVersion(1);
                        auditRsCompanyRegister.setVersiontime(new Date());
                        rsCompanyRegisterService.addAuditRsCompanyRegister(auditRsCompanyRegister);
                        rowGuid_QY = auditcompany.getRowguid();
                    }
                }
                auditProject.setApplyeruserguid(rowGuid_QY);
            }
            HandleProjectService projectService = new HandleProjectService();
            // 3、产生办件流水号
            if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YJJ) {
                if (StringUtil.isBlank(auditProject.getFlowsn())) {
                    // projectService.checkFlowSN(auditProject);
                    // 获取事项ID
                    String unid = auditTask.getStr("unid");
                    String resultflowsn = FlowsnUtil.createReceiveNum(unid, auditTask.getRowguid());
                    auditProject.setFlowsn(resultflowsn);
                }
                auditProjectService.updateProject(auditProject);
                if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                        || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                    IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                            .getComponent(IAuditOnlineProject.class);
                    AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                            .getOnlineProjectByTaskGuid(auditProject.getRowguid(), auditProject.getTaskguid())
                            .getResult();
                    if (auditOnlineProject != null && StringUtil.isBlank(auditOnlineProject.getFlowsn())) {
                        auditOnlineProject.setFlowsn(auditProject.getFlowsn());
                        iAuditOnlineProject.updateProject(auditOnlineProject);
                    }
                }
            } else {
                auditProjectService.updateProject(auditProject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleReceive(AuditProject auditProject, String operateUserName,
                                                   String operateUserGuid, String windowGuid, String windowName) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            // 1、更新办件接件信息
            if (auditProject.getSpendtime() == null) {
                auditProject.setSpendtime(0);
            }
            auditProject.setReceiveuserguid(operateUserGuid);
            auditProject.setReceiveusername(operateUserName);
            auditProject.setReceivedate(new Date());
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);// 办件状态：接件
            auditProject.setWindowguid(windowGuid);
            auditProject.setWindowname(windowName);
            saveProject(auditProject);
            // 2、更新办件操作信息

            // 3、自建系统只接件模式：自建系统只接件模式由于不经过受理步骤，固从接件开始计时
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();

            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())
                    && ZwfwConstant.ZIJIANMODE_JJMODE.equals(auditTaskExtension.getZijian_mode().toString())) {
                Date acceptdate = new Date();
                Date shouldEndDate;
                int ts;
                // 事项承诺时间大于0
                if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                    IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditOrgaWorkingDay.class);
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), acceptdate, auditTask.getPromise_day()).getResult();
                    ts = auditTask.getPromise_day() * 24 * 60;
                } else {
                    shouldEndDate = null;
                    ts = 0;
                }
                // 3.1 更新办件受理信息
                auditProject.setAcceptuserguid(operateUserGuid);
                auditProject.setAcceptusername(operateUserName);
                auditProject.setAcceptuserdate(new Date());
                if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
                auditProject.setIs_pause((ZwfwConstant.CONSTANT_INT_ZERO));
                IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
                auditProjectService.updateProject(auditProject);
                if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                        || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                    IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditOnlineProject.class);
                    String applyerguid = null;
                    if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                        IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                                .getComponent(IAuditRsCompanyBaseinfo.class);
                        AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                                .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                        if (auditrscompanybaseinfo != null) {
                            applyerguid = auditrscompanybaseinfo.getCompanyid();
                        }
                    }
                    Map<String, String> updateFieldMap = new HashMap<>();
                    Map<String, String> updateDateFieldMap = new HashMap<>();// 更新日期类型的字段
                    updateFieldMap.put("status=", auditProject.getStatus().toString());
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("sourceguid", auditProject.getRowguid());
                    // if
                    // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                    // {
                    // sql.eq("applyerguid",
                    // auditProject.getOnlineapplyerguid());
                    // }
                    // if (applyerguid != null) {
                    // sql.eq("applyerguid", applyerguid);
                    // }
                    // else {
                    // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                    // }
                    auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
                }

                // 3.2 更新办件剩余时间
                IAuditProjectSparetime auditProjectSparetimeService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectSparetime.class);
                auditProjectSparetimeService.addSpareTimeByProjectGuid(auditProject.getRowguid(), ts,
                        auditProject.getAreacode(), auditProject.getCenterguid());
                // 如果是并联审批办件，判断并联审批是否需要计时
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    handleBlspSparetime(auditProject.getSubappguid());
                }
                // 3.3 插入操作记录(操作纪录在下方插入
                // 3.4 TODO EXFRONT_ACCEPT
                // XML.CreateXML(Convert.ToString(oRow["RowGuid"]));插入受理信息到受理信息表中
            }
            // 5、判断申请通知书是否弹出
            String sqtzaddress = "";
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String isneedshenqingshu = handleConfig
                    .getFrameConfig("AS_NEED_DOC_SHENQINGSHU", auditProject.getCenterguid()).getResult();
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isneedshenqingshu) || StringUtil.isBlank(isneedshenqingshu)) {
                // 系统参数如果没有配置默认为word形式
                String asdocword = handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
                boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
                IHandleDoc docService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
                sqtzaddress = docService.getDocEditPage(auditTask.getRowguid(), auditProject.getCenterguid(),
                        auditTaskExtension.getIs_notopendoc(), String.valueOf(ZwfwConstant.DOC_TYPE_SQTZ), false,
                        isword).getResult();
                if (StringUtil.isNotBlank(sqtzaddress)) {
                    sqtzaddress += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                            + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskcaseguid();
                }
            }

            // 如果为外网申报办件更新状态
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_JJ, operateUserGuid, operateUserName);

            result.setResult(sqtzaddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleAccept(AuditProject auditProject, String workItemGuid,
                                                  String operateUserName, String operateUserGuid, String windowName, String windowGuid) {
        // 默认不直接批准
        return this.handleAccept(auditProject, workItemGuid, operateUserName, operateUserGuid, windowName, windowGuid,
                ZwfwConstant.CONSTANT_STR_ZERO);
    }

    @Override
    public AuditCommonResult<String> handleAccept(AuditProject auditProject, String workItemGuid,
                                                  String operateUserName, String operateUserGuid, String windowName, String windowGuid, String pizhun) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService
                    .getAuditTaskByGuid(auditProject.getTaskguid(), auditProject.getAcceptareacode(), true).getResult();
            IAuditApplyJsLog iAuditApplyJsLog = ContainerFactory.getContainInfo().getComponent(IAuditApplyJsLog.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            // 1、添加受理操作记录
            // 默认操作类型：窗口受理
            String operatetype = ZwfwConstant.SHENPIOPERATE_TYPE_CKSL;
            if (ZwfwConstant.APPLY_WAY_NETZJSB.equals(String.valueOf(auditProject.getApplyway()))) {
                // 网上直接申报：网上受理
                operatetype = ZwfwConstant.SHENPIOPERATE_TYPE_NETSL;
            }

            // TODO mongodb
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_SL, operateUserGuid, operateUserName,
                    workItemGuid);

            // TODO 2、绿色通道办件处理
            // 3、更新办件受理状态
            // 即办件更新为已受理待办结状态
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditProject.getTasktype()))
                    && !ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())) {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSLDBJ);
            } else {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSL);
            }
            auditProject.setAcceptuserdate(new Date());
            auditProject.setAcceptuserguid(operateUserGuid);
            auditProject.setAcceptusername(operateUserName);
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);
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
            IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            IAuditOnlineIndividual auditOnlineIndividualService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineIndividual.class);
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {

                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                // 受理后要添加窗口部门
                updateFieldMap.put("ouguid=", auditProject.getOuguid());
                updateFieldMap.put("ouname=", auditProject.getOuname());
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // } // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }
            Date shouldEndDate = null;// 承诺办结日期
            int ts;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                IAuditOrgaWorkingDay workingDayService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOrgaWorkingDay.class);
                shouldEndDate = workingDayService.getWorkingDayWithOfficeSet(auditProject.getCenterguid(), new Date(),
                        auditTask.getPromise_day()).getResult();
                ts = auditTask.getPromise_day() * 24 * 60;
            } else {
                shouldEndDate = null;
                ts = 0;
            }
            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}

            // 4、添加办件剩余时间
            IAuditProjectSparetime sparetimeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())) {
                sparetimeService.addSpareTimeByProjectGuid(auditProject.getRowguid(), ts, auditProject.getAreacode(),
                        auditProject.getCenterguid());
                // 如果是并联审批办件，判断并联审批是否需要计时
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    handleBlspSparetime(auditProject.getSubappguid());
                }
            }

            // 5、如果合并办理填写了数量，需要插入批量办件数字
            IAuditProjectNumber projectNumberService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectNumber.class);
            projectNumberService.addProjectNumber(auditProject, true, operateUserGuid, operateUserName);
            // TODO 6、如果是自建系统，那么生成对接数据
            // TODO 7、如果是并联审批办件，发消息提醒给其他引用本办件审批结果审批事项的窗口人员已受理
            // 8、非即办件走模拟流程
            // 如果事项配置了需要走模拟办件，如是收费事项，需办件流转到审核通过，其他情况根据系统参数配置流转到的步骤。
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditProject.getTasktype()))
                    && Integer.valueOf(ZwfwConstant.CONSTANT_INT_ONE).equals(auditTaskExtension.getIs_simulation())) {
                String strflowstatus = "";
                IAuditTaskResult auditResultService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskResult.class);
                AuditTaskResult auditresult = auditResultService
                        .getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if ((Integer.valueOf(ZwfwConstant.CONSTANT_INT_ONE).equals(auditTask.getCharge_flag())
                        && ZwfwConstant.CHARGE_WHEN_BJQ.equals(String.valueOf(auditTaskExtension.getCharge_when())))
                        || (auditresult != null
                        && (auditresult.getResulttype() != ZwfwConstant.LHSP_RESULTTYPE_NULL))) {
                    // 收费事项、打证办件状态设置为 审核通过
                    strflowstatus = String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPTG);
                } else {
                    // 非收费事项办件状态设置为正常办结
                    IHandleConfig handleConfigServic = ContainerFactory.getContainInfo()
                            .getComponent(IHandleConfig.class);
                    strflowstatus = handleConfigServic
                            .getFrameConfig("AS_SIMULATION_TO_STATUS", auditProject.getCenterguid()).getResult();
                    strflowstatus = StringUtil.isBlank(strflowstatus) ? String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ)
                            : strflowstatus;
                }
                auditProject.setStatus(Integer.parseInt(strflowstatus));
                // TODO 自动流转流程暂缓

            }
            // 如果是直接批准的，那么就更新办件状态数据,同时直接将办件时间计时暂停
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(pizhun)) {
                if (!ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                    // 办件暂停计时
                    // auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
                    // // 更新时间表状态 恢复计时0 暂停计时1
                    // AuditProjectSparetime auditProjectSparetime =
                    // sparetimeService
                    // .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                    // auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                    // sparetimeService.updateSpareTime(auditProjectSparetime);

                }
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPTG);
                if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))
                        || ZwfwConstant.ITEMTYPE_CNJ.equals(String.valueOf(auditTask.getType()))) {
                    auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_ZYXK);
                    auditProject.setBanwandate(new Date());
                }
            }

            // 保存办件信息
            saveProject(auditProject);

            // 判断是否是即办件简易模式
            if (!(auditTask.getType().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ))
                    || auditTask.getType().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_SBJ))
                    || (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))
                    && ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())))) {
                // 推送即办件简易模式办件基本信息
                JSONObject submit = new JSONObject();
                JSONObject params = new JSONObject();
                params.put("projectguid", auditProject.getRowguid());
                params.put("areacode", auditProject.getAreacode());
                submit.put("params", params);
                if ("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
                    String resultsign = TARequestUtil.sendPostInner(lcjbxxurl, submit.toJSONString(), "", "");
                    log.info("推送浪潮办件结果：" + resultsign + ";办件编号为：" + auditProject.getFlowsn());

                    Thread.sleep(1000);
                    JSONObject submit2 = new JSONObject();
                    submit2.put("projectGuid", auditProject.getRowguid());
                    submit2.put("areaCode", auditProject.getAreacode());
                    submit2.put("status", "01");
                    String resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
                    log.info("推送浪潮办件结果：" + resultsign2 + ";办件编号为：" + auditProject.getFlowsn());
                }
            }

            String isjS = ConfigUtil.getConfigValue("epointframe", "isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isjS)) {
                // 如果是通用版，则需要调用新网接口，更改办件状态
                String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                // 只有在配置的情况下才能进行请求
                if (StringUtil.isNotBlank(url)) {
                    url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfodetail?declareid="
                            + auditTask.getTask_id() + "&centerguid=" + auditProject.getCenterguid() + "&taskguid="
                            + auditTask.getRowguid() + "&flowsn=" + auditProject.getFlowsn();
                    Record record = StarProjectInteractUtil.updateOrInsertStar(auditProject.getFlowsn(),
                            ZwdtConstant.STAR_PROJECT_SLZ,
                            StringUtil.isBlank(auditTask.getDept_yw_reg_no()) ? auditTask.getItem_id()
                                    : auditTask.getDept_yw_reg_no(),
                            auditTask.getTaskname(), auditProject.getApplyername(), auditProject.getCertnum(), "查看办件",
                            url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                }
            }
            // 更新材料审核状态
            IAuditProjectMaterial projectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);

            // 查找是否存在补正材料
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("projectguid", auditProject.getRowguid());
            sqlc.in("auditstatus",
                    "'" + ZwfwConstant.Material_AuditStatus_DBZ + "','" + ZwfwConstant.Material_AuditStatus_YBZ + "'");
            Integer materialnum = projectMaterialService.getProjectMaterialCountByCondition(sqlc.getMap()).getResult();
            if (materialnum != null && materialnum > 0) {
                // 发送补正完成代办
                String msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "." + operateUserGuid;
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "project." + auditProject.getAreacode() + ".bzwc." + auditProject.getTask_id());
            }
            // projectMaterialService.se
            projectMaterialService.updateAllAuditStatus(auditProject.getRowguid());
            // 处理复用材料（增加内置关联关系）
            IAuditSpBusiness auditSpBusinessService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpBusiness.class);
            IAuditSpInstance auditSpiService = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            String onlineapplyercode = "";
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                AuditSpInstance auditSpi = auditSpiService.getDetailByBIGuid(auditProject.getBiguid()).getResult();
                AuditSpBusiness auditBusiness = auditSpBusinessService
                        .getAuditSpBusinessByRowguid(auditSpi.getBusinessguid()).getResult();
                if (ZwfwConstant.CONSTANT_STR_TWO.equals(auditBusiness.getBusinesstype())) {
                    // AuditOnlineProject onlineProject =
                    // auditOnlineProjectService.getOnlineProjectByApplyerGuid(auditProject.getRowguid(),
                    // auditProject.getOnlineapplyerguid()).getResult();
                    // if(onlineProject!=null){
                    // AuditOnlineIndividual auditOnlineIndividual =
                    // auditOnlineIndividualService.getIndividualByApplyerGuid(onlineProject.getApplyerguid()).getResult();
                    // onlineapplyercode = auditOnlineIndividual.getIdnumber();
                    // }
                    if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                        AuditOnlineIndividual auditonlineindividual = auditOnlineIndividualService
                                .getIndividualByApplyerGuid(auditProject.getOnlineapplyerguid()).getResult();
                        if (auditonlineindividual != null) {
                            onlineapplyercode = auditonlineindividual.getIdnumber();
                        }
                    }
                }
            }
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid", auditProject.getRowguid());
            IAuditMaterialLibrary auditMaterialLibrary = ContainerFactory.getContainInfo()
                    .getComponent(IAuditMaterialLibrary.class);
            IAuditTaskMaterial taskMaterialservice = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditMaterialLibraryR auditMaterialLibraryRService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditMaterialLibraryR.class);
            IAttachService frameAttachInfoService = ContainerFactory.getContainInfo()
                    .getComponent(IAttachService.class);
            List<AuditMaterialLibrary> recordlist = auditMaterialLibrary
                    .getAuditMaterialLibraryListByPage(sql.getMap(), 0, 0, "operatedate", "desc").getResult().getList();
            // 复用过来的历史材料
            List<String> copymaterials = new ArrayList<String>();

            if (recordlist.size() > 0) {
                for (AuditMaterialLibrary materialLibrary : recordlist) {
                    copymaterials.add(materialLibrary.getAttachguid());
                }
            }
            SqlConditionUtil sqlm = new SqlConditionUtil();
            sqlm.eq("projectguid", auditProject.getRowguid());
            List<AuditProjectMaterial> materials = projectMaterialService
                    .getProjectMaterialPageData(sqlm.getMap(), 0, 0, "", "").getResult().getList();
            if (materials != null && materials.size() > 0) {
                // 附件信息
                Map<String, FrameAttachInfo> attachmap = new HashMap<>();
                for (AuditProjectMaterial material : materials) {
                    // 新增的材料
                    List<String> newmaterials = new ArrayList<String>();
                    String cliengguid = material.getCliengguid();
                    List<FrameAttachInfo> attachs = frameAttachInfoService.getAttachInfoListByGuid(cliengguid);
                    if (attachs != null && attachs.size() > 0) {
                        for (FrameAttachInfo attach : attachs) {
                            newmaterials.add(attach.getAttachGuid());
                            attachmap.put(attach.getAttachGuid(), attach);
                        }
                    }
                    // 所有attachguid去除复用附件的attachguid，就是新增的附件的attachguid
                    newmaterials.removeAll(copymaterials);
                    if (newmaterials.size() > 0) {
                        for (String attachguid : newmaterials) {
                            AuditMaterialLibrary auditMaterialLibrary1 = new AuditMaterialLibrary();
                            auditMaterialLibrary1.setRowguid(UUID.randomUUID().toString());
                            auditMaterialLibrary1.setFlowsn(auditProject.getFlowsn());
                            auditMaterialLibrary1.setProjectname(auditProject.getProjectname());
                            auditMaterialLibrary1.setOperatedate(new Date());
                            auditMaterialLibrary1.setOperateusername(auditProject.getAcceptusername());
                            auditMaterialLibrary1.setProjectguid(auditProject.getRowguid());
                            auditMaterialLibrary1.setTasktype(String.valueOf(auditProject.getTasktype()));
                            auditMaterialLibrary1.setOuname(auditProject.getOuname());
                            auditMaterialLibrary1.setItemcode(auditTask.getItem_id());
                            if (StringUtil.isBlank(onlineapplyercode)) {
                                auditMaterialLibrary1.setOwnerno(auditProject.getCertnum());
                            } else {
                                auditMaterialLibrary1.setOwnerno(onlineapplyercode);
                            }
                            String materialid = taskMaterialservice
                                    .getAuditTaskMaterialByRowguid(material.getTaskmaterialguid()).getResult()
                                    .getMaterialid();
                            auditMaterialLibrary1.setMaterialid(materialid);
                            auditMaterialLibrary1.setMaterialname(material.getTaskmaterial());
                            auditMaterialLibrary1.setAttachguid(attachguid);
                            auditMaterialLibrary1.setCount(ZwfwConstant.CONSTANT_INT_ZERO);
                            auditMaterialLibrary1.setFirstflag(ZwfwConstant.CONSTANT_STR_ONE);
                            FrameAttachInfo attachinfo = attachmap.get(attachguid);
                            if (attachinfo != null) {
                                auditMaterialLibrary1.setFilename(attachinfo.getAttachFileName());
                                auditMaterialLibrary1.setFiletype(attachinfo.getContentType());
                                auditMaterialLibrary1.setFileSize(attachinfo.getAttachLength());
                                auditMaterialLibrary1.setUploaddate(attachinfo.getUploadDateTime());
                            }

                            auditMaterialLibrary.insert(auditMaterialLibrary1);
                        }
                    }
                }
            }
            EpointFrameDsManager.commit();
            if (recordlist.size() > 0) {
                for (AuditMaterialLibrary materialLibrary : recordlist) {
                    if (ZwfwConstant.CONSTANT_STR_ZERO.equals(materialLibrary.getFirstflag())
                            && StringUtil.isNotBlank(materialLibrary.getCopyfrom())) {
                        String newattachguid = materialLibrary.getAttachguid();
                        String oldguid = materialLibrary.getCopyfrom();
                        sql.clear();
                        sql.eq("ATTACHGUID", newattachguid);
                        AuditMaterialLibrary newMaterial = auditMaterialLibrary
                                .getAuditMaterialLibraryListByPage(sql.getMap(), 0, 0, "operatedate", "desc")
                                .getResult().getList().get(0);
                        AuditMaterialLibrary oldMaterial = auditMaterialLibrary.find(oldguid);
                        if (oldMaterial != null && !oldMaterial.getMaterialid().equals(newMaterial.getMaterialid())) {
                            String materialid = oldMaterial.getMaterialid();
                            AuditMaterialLibraryR auditMaterialLibraryR = auditMaterialLibraryRService
                                    .selectRelationByMaterialid(materialid).getResult();
                            AuditMaterialLibraryR newauditMaterialLibraryR = auditMaterialLibraryRService
                                    .selectRelationByMaterialid(newMaterial.getMaterialid()).getResult();

                            if (auditMaterialLibraryR != null && newauditMaterialLibraryR == null) {
                                AuditMaterialLibraryR newRelation = new AuditMaterialLibraryR();
                                newRelation.setSharematerialguid(auditMaterialLibraryR.getSharematerialguid());
                                newRelation.setRowguid(UUID.randomUUID().toString());
                                newRelation.setMaterialid(newMaterial.getMaterialid());
                                auditMaterialLibraryRService.insert(newRelation);
                            } else if (auditMaterialLibraryR == null && newauditMaterialLibraryR != null) {
                                AuditMaterialLibraryR newRelation = new AuditMaterialLibraryR();
                                newRelation.setSharematerialguid(newauditMaterialLibraryR.getSharematerialguid());
                                newRelation.setRowguid(UUID.randomUUID().toString());
                                newRelation.setMaterialid(oldMaterial.getMaterialid());
                                auditMaterialLibraryRService.insert(newRelation);
                            } else if (auditMaterialLibraryR != null && newauditMaterialLibraryR != null) {
                                List<AuditMaterialLibraryR> list = auditMaterialLibraryRService
                                        .selectRelationByShareMaterialGuid(
                                                newauditMaterialLibraryR.getSharematerialguid())
                                        .getResult();
                                for (AuditMaterialLibraryR materialr : list) {
                                    materialr.setSharematerialguid(auditMaterialLibraryR.getSharematerialguid());
                                    auditMaterialLibraryRService.update(materialr);
                                }
                            } else {
                                String shareMaterialGuid = UUID.randomUUID().toString();
                                AuditMaterialLibraryR newRelation = new AuditMaterialLibraryR();
                                newRelation.setSharematerialguid(shareMaterialGuid);
                                newRelation.setRowguid(UUID.randomUUID().toString());
                                newRelation.setMaterialid(newMaterial.getMaterialid());
                                auditMaterialLibraryRService.insert(newRelation);
                                newRelation.setSharematerialguid(shareMaterialGuid);
                                newRelation.setRowguid(UUID.randomUUID().toString());
                                newRelation.setMaterialid(oldMaterial.getMaterialid());
                                auditMaterialLibraryRService.insert(newRelation);
                            }

                        }

                    }
                }
            }

            // 处理共享材料
            String ownGuid = "";
            IAuditIndividual auditIndividualService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditIndividual.class);
            IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyBaseinfo.class);
            // 企业类型申请人
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfoService
                        .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsCompanyBaseinfo.getCompanyid();
            }
            // 个人
            else if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = auditIndividualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                if (auditRsIndividualBaseinfo != null) {
                    ownGuid = auditRsIndividualBaseinfo.getPersonid();
                }
            }
            if (StringUtil.isBlank(ownGuid)) {
                ownGuid = auditProject.getApplyeruserguid();
            }
            FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
            IHandleRSMaterial handleRSMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IHandleRSMaterial.class);
            handleRSMaterialService.insertShareMaterial(auditProject.getTaskguid(), auditProject.getRowguid(), ownGuid,
                    operateUserGuid, operateUserName, auditProject.getApplyername(), auditProject.getCertnum(),

                    auditProject.getApplyertype(), auditProject.getCerttype(), auditProject.getAreacode(),
                    ou.get("orgcode"));

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }

            //@author fryu 2024年1月15日 个性化调用三方接口
            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + auditProject.getAreacode() + ".jnaccept." + auditProject.getTask_id());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleYstgAddreason(AuditProject auditProject, String operateUserName,
                                                         String operateUserGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYSTG);
            auditProject.setBubandate(new Date());
            auditProject.setYushendate(new Date());
            saveProject(auditProject);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap<>();
                Map<String, String> updateDateFieldMap = new HashMap<>();// 更新日期类型的字段
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YSTG, operateUserGuid, operateUserName, "",
                    reason);

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleYstg(AuditProject auditProject, String operateUserName,
                                                String operateUserGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYSTG);
            auditProject.setBubandate(new Date());
            auditProject.setYushendate(new Date());
            saveProject(auditProject);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());

                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    Map<String, String> updateFieldMap2 = new HashMap<>(16);
                    Map<String, String> updateDateFieldMap2 = new HashMap<>(16);// 更新日期类型的字段
                    updateFieldMap2.put("status=", auditProject.getStatus().toString());
                    sql.clear();
                    sql.eq("sourceguid", auditProject.getRowguid());
                    // if
                    // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                    // {
                    // sql.eq("applyerguid",
                    // auditProject.getOnlineapplyerguid());
                    // }
                    auditOnlineProjectService.updateOnlineProject(updateFieldMap2, updateDateFieldMap2, sql.getMap());
                }

            }

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YSTG, operateUserGuid, operateUserName);

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleYsdh(AuditProject auditProject, String operateUserName,
                                                String operateUserGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        try {
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYSTU);
            auditProject.setBubandate(new Date());
            project.updateProject(auditProject);
            // 跟新onlineproject表
            IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTU));// 修改状态到外网预审退回
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }
            /*
             * Map<String, String> updateFieldMap = new HashMap<>();
             * Map<String, String> updateDateFieldMap = new HashMap<>();//
             * 更新日期类型的字段
             * updateFieldMap.put("status=",
             * String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTU));//
             * 修改状态到外网预审退回
             * SqlConditionUtil sql = new SqlConditionUtil();
             * sql.eq("sourceguid", auditProject.getRowguid());
             */
            // auditOnlineProjectService.updateOnlineProject(updateFieldMap,
            // updateDateFieldMap, sql.getMap());

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YSDH, operateUserGuid, operateUserName);

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    /**
     * 预审打回操作，短信增加退回原因
     */
    public AuditCommonResult<String> handleYsdhAddreason(AuditProject auditProject, String operateUserName,
                                                         String operateUserGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        try {
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYSTU);
            auditProject.setBubandate(new Date());
            project.updateProject(auditProject);

            String applyerguid = null;
            IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyBaseinfo.class);
                AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                        .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                if (auditrscompanybaseinfo != null) {
                    applyerguid = auditrscompanybaseinfo.getCompanyid();
                }
            }
            Map<String, String> updateFieldMap = new HashMap<>();
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTU));// 修改状态到外网预审退回
            updateFieldMap.put("backreason=", reason);
            Map<String, String> conditionMap = new HashMap<>();
            conditionMap.put("sourceguid=", auditProject.getRowguid());
            if (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid())) {
                conditionMap.put("applyerguid=", auditProject.getOnlineapplyerguid());
            }
            /*
             * if (applyerguid != null) {
             * conditionMap.put("applyerguid=", applyerguid);
             * }
             * else {
             * conditionMap.put("applyerguid=",
             * auditProject.getApplyeruserguid());
             * }
             */
            auditOnlineProjectService.updateOnlineProjectJS(updateFieldMap, conditionMap);
            // 跟新onlineproject表
            /*
             * IAuditOnlineProject auditOnlineProjectService =
             * ContainerFactory.getContainInfo()
             * .getComponent(IAuditOnlineProject.class);
             * Map<String, String> updateFieldMap = new HashMap<>();
             * updateFieldMap.put("status=",
             * String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTU));
             * updateFieldMap.put("backreason=", reason);
             * Map<String, String> conditionMap = new HashMap<>();
             * conditionMap.put("applyerguid=",
             * auditProject.getApplyeruserguid());
             * conditionMap.put("sourceguid=", auditProject.getRowguid());
             * auditOnlineProjectService.updateOnlineProjectJS(updateFieldMap,
             * conditionMap);
             */

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YSDH, operateUserGuid, operateUserName, "",
                    reason);

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            IAuditProjectUnusual projectUnusualService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            IWorkingdayService iWorkingdayService  = ContainerFactory.getContainInfo()
                    .getComponent(IWorkingdayService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
                    targetUser = operateUserGuid;
                    targetDispName = operateUserName;
                }
                String content = "";
                if (StringUtil.isNotBlank(auditTaskExtension.getStr("is_sendbz"))){
                    if("1".equals(auditTaskExtension.getStr("is_sendbz"))){
                        Date date = new Date();
                        Date officeSet = iWorkingdayService.getWorkingDayWithOfficeSet(date, 5, false);
                        int monthOfDate = EpointDateUtil.getMonthOfDate(officeSet)+1;
                        int day = EpointDateUtil.getDayOfDate(officeSet);
                        content="请按照反馈意见："+ reason +"。于"+  monthOfDate+  "月" + day+ "日前（5个工作日内）完成补正，逾期办件将自动结束。如继续申请需重新提报。";
                    }
                    else {
                        if (StringUtil.isNotBlank(auditTaskExtension.getNotify_nys())) {
                            content = auditTaskExtension.getNotify_nys();
                            content = content.replace("[#=OuName#]", auditProject.getOuname());
                            content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                            content = content.replace("[#=ApplyDate#]",
                                    EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                            content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                            content = content.replace("[#=Reason#]", reason);
                        }
                    }
                }
                else {
                    if (StringUtil.isNotBlank(auditTaskExtension.getNotify_nys())) {
                        content = auditTaskExtension.getNotify_nys();
                        content = content.replace("[#=OuName#]", auditProject.getOuname());
                        content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                        content = content.replace("[#=ApplyDate#]",
                                EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                        content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                        content = content.replace("[#=Reason#]", reason);
                    }
                }

                messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                        content, new Date(), 0, new Date(),
                        auditProject.getContactmobile(), targetUser, targetDispName,
                        operateUserGuid, operateUserName,
                        "", "", "", false, auditProject.getAreacode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handlePatch(AuditProject auditProject, String operateUserName,
                                                 String operateUserGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

            // 1、更新办件状态
            if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL) {
                // 受理后更新为 受理补办状态
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSLDBB);
            } else {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_DBB);
            }
            auditProject.setBubandate(new Date());
            auditProject.set("bubanfinishdate",null);
            saveProject(auditProject);
            // TODO mongodb
            // 添加操作纪录
            JnHandleProjectService projectService = new JnHandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BZ, operateUserGuid, operateUserName);
            // 2、更新办件审批操作表
            // projectoperationservice.insertProjectOperation(userSession,
            // auditProject,
            // Integer.parseInt(ZwfwConstant.SHENPIOPERATE_TYPE_BB));
            // 3、判断补正告知书是否弹出
            // 系统参数如果没有配置默认为word形式
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String asdocword = handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
            boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IJNAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IJNAuditProject.class);

            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();

            IHandleDoc docService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            String bzgzsaddress = "";
            // 非即办件弹补正告知书
            // if
            // (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString()))
            // {
            // 判断当前事项audit_task.businesstype等于1代表依申请事项，弹补正告知书
            if ("1".equals(auditTask.getStr("businesstype"))) {
                bzgzsaddress = docService.getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                        auditTaskExtension.getIs_notopendoc(), String.valueOf(ZwfwConstant.DOC_TYPE_CLBZGZS), false,
                        isword).getResult();
                if (StringUtil.isNotBlank(bzgzsaddress)) {
                    if (bzgzsaddress.contains("?")) {
                        bzgzsaddress += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                                + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskcaseguid();
                    } else {
                        bzgzsaddress += "?ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                                + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskcaseguid();
                    }
                }
            }
            result.setResult(bzgzsaddress);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_DBB));
                SqlConditionUtil sql = new SqlConditionUtil();
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    sql.eq("sourceguid", auditProject.getBiguid());
                } else {
                    sql.eq("sourceguid", auditProject.getRowguid());
                }
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // 修改待办标题
            if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                        .getComponent(IMessagesCenterService.class);
                IWFInstanceAPI9 wfinstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
                ProcessVersionInstance pVersionInstance = wfinstance
                        .getProcessVersionInstance(auditProject.getPviguid());
                List<Integer> status = new ArrayList<Integer>();
                status.add(WorkflowKeyNames9.WorkItemStatus_Active);
                status.add(WorkflowKeyNames9.WorkItemStatus_Inactive);
                // List<WorkflowWorkItem> workflowWorkItems = wfinstance
                // .getWorkItemListByPVIGuidAndStatus(pVersionInstance, status);

                List<WorkflowWorkItem> workflowWorkItems = iAuditProject
                        .getWorkItemListByPVIGuidAndStatus(auditProject.getPviguid());

                if (workflowWorkItems != null && workflowWorkItems.size() > 0) {
                    for (WorkflowWorkItem workflowWorkItem : workflowWorkItems) {
                        if (WorkflowKeyNames9.WorkItemStatus_Active == workflowWorkItem.getStatus()) {
                            messagesCenterService.updateMessageTitle(workflowWorkItem.getWaitHandleGuid(),
                                    "【待补正】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")",
                                    workflowWorkItem.getTransactor());
                        }
                    }
                }
            }

            //@author fryu 2024年1月15日 个性化调用三方接口
            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + auditProject.getAreacode() + ".jnpatch." + auditProject.getTask_id());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleReceipt(AuditProject auditProject, String chargeGuid, String operateUserName,
                                                   String operateUserGuid, String messageItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            // 1、更新办件信息
            auditProject.setFeedate(new Date());
            auditProject.setIs_fee(ZwfwConstant.CONSTANT_INT_ONE);
            // 更新办件状态 恢复计时0 暂停计时1
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);
            auditProjectService.updateProject(auditProject);

            // 判断是否是外网政务大厅申报办件，如果是，则需要同步更新 audit_online_project 表状态
            int applyWay = auditProject.getApplyway();
            int temp = Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ);
            if (applyWay < temp) {
                IAuditOnlineProject auditOnlineProject = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                updateFieldMap.put("is_fee =", ZwfwConstant.CONSTANT_INT_ONE + "");
                sql.eq("SOURCEGUID", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                /*
                 * if (applyerguid != null) {
                 * sql.eq("applyerguid", applyerguid);
                 * }
                 * else {
                 * sql.eq("applyerguid", auditProject.getApplyeruserguid());
                 * }
                 */
                auditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // TODO mongodb
            // 2、添加办件操作记录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_SQ, operateUserGuid, operateUserName);

            // 3、更新办件收费记录信息
            IAuditProjectCharge projectChargeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = projectChargeService.getProjectChargeByRowGuid(chargeGuid)
                    .getResult();
            auditprojectcharge.setFeedate(new Date());
            auditprojectcharge.setFeeuserguid(operateUserGuid);
            auditprojectcharge.setFeeusername(operateUserName);
            auditprojectcharge.setIscharged(ZwfwConstant.CONSTANT_INT_ONE);
            projectChargeService.updateProjectCharge(auditprojectcharge);
            // 4、更新办件收费明细信息
            IAuditProjectChargeDetail detailService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectChargeDetail.class);
            detailService.updateStatusByChargeGuid(chargeGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleReceipt(AuditProject auditProject, String chargeGuid, String operateUserName,
                                                   String operateUserGuid, String messageItemGuid, String bankCode, String chargeFlowNo, Integer jiaofeitype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            // 1、更新办件信息
            auditProject.setFeedate(new Date());
            auditProject.setIs_fee(ZwfwConstant.CONSTANT_INT_ONE);
            // 更新办件状态 恢复计时0 暂停计时1
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);
            auditProjectService.updateProject(auditProject);

            // 判断是否是外网政务大厅申报办件，如果是，则需要同步更新 audit_online_project 表状态
            int applyWay = auditProject.getApplyway();
            int temp = Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ);
            if (applyWay < temp) {
                IAuditOnlineProject auditOnlineProject = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                updateFieldMap.put("is_fee =", ZwfwConstant.CONSTANT_INT_ONE + "");
                sql.eq("SOURCEGUID", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                /*
                 * if (applyerguid != null) {
                 * sql.eq("applyerguid", applyerguid);
                 * }
                 * else {
                 * sql.eq("applyerguid", auditProject.getApplyeruserguid());
                 * }
                 */
                auditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // TODO mongodb
            // 2、添加办件操作记录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_SQ, operateUserGuid, operateUserName);

            // 3、更新办件收费记录信息
            IAuditProjectCharge projectChargeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = projectChargeService.getProjectChargeByRowGuid(chargeGuid)
                    .getResult();
            auditprojectcharge.setFeedate(new Date());
            auditprojectcharge.setFeeuserguid(operateUserGuid);
            auditprojectcharge.setFeeusername(operateUserName);
            auditprojectcharge.setIscharged(ZwfwConstant.CONSTANT_INT_ONE);
            auditprojectcharge.setBankcode(bankCode);
            auditprojectcharge.setChargeflowno(chargeFlowNo);
            auditprojectcharge.setJiaofeitype(jiaofeitype);
            projectChargeService.updateProjectCharge(auditprojectcharge);
            // 4、更新办件收费明细信息
            IAuditProjectChargeDetail detailService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectChargeDetail.class);
            detailService.updateStatusByChargeGuid(chargeGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleReject(AuditProject auditProject, String operateUserName,
            String operateUserGuid, String windowName, String windowGuid) {
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
        IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String message = "";
            String bysljdsaddress = "";
            // 1、更新办件状态
            auditProject.setBanjieresult(Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_BYSL));
            auditProject.setBanjiedate(new Date());
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_BYSL);
            auditProject.setAcceptuserguid(operateUserGuid);
            auditProject.setAcceptusername(operateUserName);
            if (StringUtil.isBlank(auditProject.getWindowname())) {
                auditProject.setWindowname(windowName);
            }
            if (StringUtil.isBlank(auditProject.getWindowguid())) {
                auditProject.setWindowguid(windowGuid);
            }
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

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

            // 判断是否是外网政务大厅申报办件，如果是，则需要同步更新 audit_online_project 表状态
            int applyWay = auditProject.getApplyway();
            int temp = Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ);
            if (applyWay < temp) {
                IAuditOnlineProject auditOnlineProject = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                updateFieldMap.put("STATUS =", ZwfwConstant.BANJIAN_STATUS_BYSL + "");
                sql.eq("SOURCEGUID", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                /*
                 * if (applyerguid != null) {
                 * sql.eq("applyerguid", applyerguid);
                 * }
                 * else {
                 * sql.eq("applyerguid", auditProject.getApplyeruserguid());
                 * }
                 */
                auditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // 2、更新办件审批操作表
            // TODO mongodb
            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BYSL, operateUserGuid, operateUserName);

            // 3、判断打开发送短信或者是直接打开不予受理决定书
            // if
            // (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.getIssendsms()))
            // {
            // message = "ok";
            // }
            // 非即办件需要打印不予受理通知书

            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String asdocword = handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
            boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
            IHandleDoc docService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            bysljdsaddress = docService.getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                    auditTaskExtension.getIs_notopendoc(), String.valueOf(ZwfwConstant.DOC_TYPE_BYSLJDS), false, isword)
                    .getResult();
            if (StringUtil.isNotBlank(bysljdsaddress)) {
                bysljdsaddress += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                        + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskcaseguid();
            }

            message = message + ";" + bysljdsaddress;
            result.setResult(message);

            //@author fryu 2024年1月15日 个性化调用三方接口
            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + auditProject.getAreacode() + ".jnnoaccept." + auditProject.getTask_id());

            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".notaccept." + auditProject.getTask_id());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleApprove(AuditProject auditProject, String operateUserName,
                                                   String operateUserGuid, String workItemGuid, String messageItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String message = "";
            String unusualGuid = "";
            log.info("批准短信发送");
            // 1、添加批准操作记录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_PZ, operateUserGuid, operateUserName,
                    workItemGuid);
            IWFInstanceAPI9 instanceapi = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            // 2、非即办件暂停计时
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())) {
                // 2.1办件暂停计时
                auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
                // 更新时间表状态 恢复计时0 暂停计时1
                AuditProjectSparetime auditProjectSparetime = projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                if (auditProjectSparetime != null) {
                    auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                    projectSparetime.updateSpareTime(auditProjectSparetime);
                    // 如果是并联审批办件，判断并联审批是否需要计时
                    if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                        handleBlspSparetime(auditProject.getSubappguid());
                    }
                }
                // 2.2更新待办 (自建系统办件不更新)
                if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                    String title = "【已暂停】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                    HandleProjectService handleProjectService = new HandleProjectService();
                    handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid,
                            operateUserGuid);
                }
                // 2.3添加办件特殊操作
                IAuditProjectUnusual auditProjectUnusual = ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectUnusual.class);
                unusualGuid = auditProjectUnusual
                        .insertProjectUnusual("系统自动暂停计时", "系统自动暂停计时", auditProject,
                                Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT), workItemGuid, "非即办件批准后自动暂停计时")
                        .getResult();
            }
            // 3、修改办件状态为审批通过，审批结果为准予许可
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPTG);
            auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_ZYXK);
            auditProject.setBanwandate(new Date());
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // TODO 4、如果是并联审批办件，发消息提醒给其他引用本办件审批结果审批事项的窗口人员已批准
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                // sendOnlineMessageToRelatedPeople
            }
            // 5、批准时再判断材料是否需提交到证照库
            // auditprojectmaterialservice.synchroMaterialToCertLibrary(auditProject);
            // 6、判断打开发送短信或者是直接打开准予许可决定书
            // if
            // (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.getIssendsms()))
            // {
            // message = "ok";
            // }

            // 处理共享材料
            String ownGuid = "";
            IAuditIndividual auditIndividualService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditIndividual.class);
            IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyBaseinfo.class);
            // 企业类型申请人
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfoService
                        .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsCompanyBaseinfo.getCompanyid();
            }
            // 个人
            else if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = auditIndividualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsIndividualBaseinfo.getPersonid();
            }
            if (StringUtil.isBlank(ownGuid)) {
                ownGuid = auditProject.getApplyeruserguid();
            }

            // IHandleRSMaterial handleRSMaterialService =
            // ContainerFactory.getContainInfo()
            // .getComponent(IHandleRSMaterial.class);
            // handleRSMaterialService.insertShareMaterial(auditProject.getTaskguid(),
            // auditProject.getRowguid(), ownGuid,
            // operateUserGuid, operateUserName, auditProject.getApplyername(),
            // auditProject.getCertnum(),
            // auditProject.getApplyertype(),
            // auditProject.getCerttype(),auditProject.getAreacode());
            // 非即办件
            String zyxkjdsaddress = "";
            // if
            // (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString()))
            // {
            // 打印准予许可决定书
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String asdocword = handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
            boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
            IHandleDoc docService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            zyxkjdsaddress = docService.getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                    auditTaskExtension.getIs_notopendoc(), String.valueOf(ZwfwConstant.DOC_TYPE_ZYXKJD), false, isword)
                    .getResult();
            if (StringUtil.isNotBlank(zyxkjdsaddress)) {
                zyxkjdsaddress += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                        + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskcaseguid();
            }
            // }
            message = message + ";" + zyxkjdsaddress + "@" + unusualGuid;
            result.setResult(message);
            // 批准短信
            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleNotApprove(AuditProject auditProject, String operateUserName,
                                                      String operateUserGuid, String workItemGuid, String messageItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String message = "";
            String unusualGuid = "";
            log.info("不予批准短信发送");
            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BYPZ, operateUserGuid, operateUserName,
                    workItemGuid);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IWFInstanceAPI9 instanceapi = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
            // 2、非即办件或非正常模式即办件且非自建系统时暂停计时
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())
                    || !ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())) {
                // 2.1办件暂停计时
                auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
                // 更新时间表状态 恢复计时0 暂停计时1
                AuditProjectSparetime auditProjectSparetime = projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                if (auditProjectSparetime != null) {
                    auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                    projectSparetime.updateSpareTime(auditProjectSparetime);
                    // 如果是并联审批办件，判断并联审批是否需要计时
                    if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                        handleBlspSparetime(auditProject.getSubappguid());
                    }
                }
                // 2.2更新待办 (自建系统办件不更新)
                if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                    String title = "【已暂停】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                    HandleProjectService handleProjectService = new HandleProjectService();
                    handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid,
                            operateUserGuid);
                }
                // 2.3添加办件特殊操作
                IAuditProjectUnusual auditProjectUnusual = ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectUnusual.class);
                unusualGuid = auditProjectUnusual
                        .insertProjectUnusual("系统自动暂停计时", "系统自动暂停计时", auditProject,
                                Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT), workItemGuid, "非即办件不予批准后自动暂停计时")
                        .getResult();
            }
            // 3、修改办件状态为审批通过，审批结果为不予许可
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPBTG);
            auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_BYXK);
            auditProject.setBanwandate(new Date());
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }

                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }
            // 4、判断打开发送短信或者是直接打开准予许可决定书
            // if
            // (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.getIssendsms()))
            // {
            // message = "ok";
            // }
            // 打印不予许可决定书
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String asdocword = handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
            boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
            IHandleDoc docService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            String byxkjdsaddress = "";
            // if
            // (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString()))
            // {
            byxkjdsaddress = docService.getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                    auditTaskExtension.getIs_notopendoc(), String.valueOf(ZwfwConstant.DOC_TYPE_BYXKJDS), false, isword)
                    .getResult();
            if (StringUtil.isNotBlank(byxkjdsaddress)) {
                byxkjdsaddress += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                        + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskcaseguid();
            }
            // }
            message = message + ";" + byxkjdsaddress + "@" + unusualGuid;
            result.setResult(message);
            // 不予批准短信
            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                    ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(auditProject.getPviguid());
                    if (pvi != null) {
                        WorkflowWorkItem workflowWorkItem = instanceapi.getWorkItem(pvi, workItemGuid);
                        if (workflowWorkItem != null) {
                            content = content.replace("[#=Reason#]", workflowWorkItem.getOpinion());
                        }
                    }
                }
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }

            //@author fryu 2024年1月16日 调用三方接口mq
            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + auditProject.getAreacode() + ".jnnopass." + auditProject.getTask_id());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleFinish(AuditProject auditProject, String operateUserName,
                                                  String operateUserGuid, String workItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
        IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        try {
            // 自建系统办件不予批准会自动暂停
            if (auditProject.getIs_pause() == 1) {
                handleRecover(auditProject, operateUserName, operateUserGuid, null, null);
            }
            // 1、添加办结操作记录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BJ, operateUserGuid, operateUserName,
                    workItemGuid);

            // 3、更新办件用时、状态、归档
            IAuditProjectSparetime sparetimeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditprojectspare = sparetimeService
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            // 审核通过就不计时，承诺件两步流程无审核通过情况
            if (StringUtil.isBlank(auditProject.getSparetime())) {
                // 这里判断一下是不是存在没有剩余时间的情况，如果没有剩余时间，就不在针对这两个字段进行操作
                if (auditprojectspare != null && !auditprojectspare.isEmpty()) {
                    int spendtime = auditprojectspare.getSpendminutes();// 已用时间
                    int sparetime = auditprojectspare.getSpareminutes();// 剩余的时间
                    auditProject.setSparetime(sparetime);
                    auditProject.setSpendtime(spendtime);
                }
                // 4、剩余时间临时表记录
                if (auditprojectspare != null) {
                    sparetimeService.deleteSpareTimeByRowGuid(auditprojectspare.getRowguid());
                    // 如果是并联审批办件，判断并联审批是否需要计时
                    if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                        handleBlspSparetime(auditProject.getSubappguid());
                    }
                }
            }
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);// 办件状态：正常办结
            auditProject.setBanjiedate(new Date());
            auditProject.setBanjieuserguid(operateUserGuid);
            auditProject.setBanjieusername(operateUserName);

            auditProject.setIs_guidang(ZwfwConstant.CONSTANT_INT_ONE);// 办件归档
            auditProject.setGuidangdate(new Date());
            auditProject.setGuidanguserguid(operateUserGuid);
            auditProject.setGuidangusername(operateUserName);

            // TODO 5、自建系统办结信息插入EXFRONT_BANJIE
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())) {
                if (auditProject.getBanjieresult() == null || auditProject.getBanjieresult() == 0) {
                    auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_ZYXK);
                }
                // EXFRONT_BANJIE
            }
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);


            //查看有无centerguid
            if(StringUtils.isBlank(auditProject.getCenterguid())){
                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
                if(auditOrgaServiceCenter!=null){
                    auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
                }
            }
            //更新承诺办结时间
            AuditTask  auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
            IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaWorkingDay.class);
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

            // 2、 批量登记办件办结更新
            IAuditProjectNumber projectNumberService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectNumber.class);
            projectNumberService.addProjectNumber(auditProject, false, operateUserGuid, operateUserName);

            // TODO 6、如果是并联审批办件并且本办件是本次申报最后一个办结的办件，提醒统一受理窗口本次申报完成
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {

            }
            // 暂缓

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                updateDateFieldMap.put("banjiedate=", EpointDateUtil.convertDate2String(auditProject.getBanjiedate(),
                        EpointDateUtil.DATE_TIME_FORMAT));
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 删除办件材料表中当前办件情形之外的办件材料
        IAuditTaskMaterialCase auditTaskMaterialCaseService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterialCase.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAuditTaskMaterial iaudittaskmaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        Map<String, String> caseMap = null;
        String taskcaseguid = auditProject.getTaskcaseguid();
        if (StringUtil.isNotBlank(taskcaseguid)) {
            List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                    .selectTaskMaterialCaseByCaseGuid(taskcaseguid).getResult();
            caseMap = new HashMap<>(16);
            if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getMaterialguid());
                }
                //
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("taskguid", auditProject.getTaskguid());
                sqlConditionUtil.eq("necessity", ZwfwConstant.NECESSITY_SET_YES);

                List<AuditTaskMaterial> listm = iaudittaskmaterial
                        .selectMaterialListByCondition(sqlConditionUtil.getMap()).getResult();
                for (AuditTaskMaterial auditTaskMaterial : listm) {
                    caseMap.put(auditTaskMaterial.getRowguid(), auditTaskMaterial.getRowguid());
                }
                auditProjectMaterialService.deleteNoUsedMaterialByMap(auditProject.getRowguid(), caseMap);
            }
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handlePricing(AuditProject auditProject, String operateUserName,
                                                   String operateUserGuid, List<Record> listItem, Double realSum, Double cutSum, String messageItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IAuditProjectCharge auditProjectChargeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            // 1、添加办件收费记录信息
            String chargeGuid = UUID.randomUUID().toString();
            AuditProjectCharge auditprojectcharge = new AuditProjectCharge();
            auditprojectcharge.setRowguid(chargeGuid);
            auditprojectcharge.setProjectguid(auditProject.getRowguid());
            auditprojectcharge.setCheckdate(new Date());// 核价日期
            auditprojectcharge.setCheckuserguid(operateUserGuid);// 核价人员标识
            auditprojectcharge.setCheckusername(operateUserName);// 核价人姓名
            auditprojectcharge.setJiaokuanperson(auditProject.getApplyername());// 缴款人姓名
            auditprojectcharge.setCharge_when(auditProject.getCharge_when());// 何时收费
            auditprojectcharge.setNote(auditprojectcharge.getCutreason());
            auditprojectcharge.setIs_cancel(ZwfwConstant.CONSTANT_INT_ZERO);// 没有取消
            auditprojectcharge.setIscharged(ZwfwConstant.CONSTANT_INT_ZERO);// 尚未收费
            auditprojectcharge.setRealsum(realSum);
            auditprojectcharge.setCutsum(cutSum);
            // 设置收费编号
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();

            int charNo = auditProjectChargeService.getMaxChargeNo(auditTask.getAreacode()).getResult();
            auditprojectcharge.setChargeno(charNo);
            auditProjectChargeService.addProjectCharge(auditprojectcharge);
            // 2、核价完成更新办件信息
            auditProject.setIs_check(ZwfwConstant.CONSTANT_INT_ONE);
            auditProject.setIs_fee(ZwfwConstant.CONSTANT_INT_ZERO);
            // 更新办件状态 恢复计时0 暂停计时1
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())) {
                // auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
            }
            auditProject.setCheckuserguid(operateUserGuid);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            IAuditProjectChargeDetail chargeDetailService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectChargeDetail.class);
            for (Record record : listItem) {
                AuditProjectChargeDetail auditprojectchargedetail = new AuditProjectChargeDetail();
                auditprojectchargedetail.setRowguid(UUID.randomUUID().toString());
                auditprojectchargedetail.setOperatedate(new Date());
                auditprojectchargedetail.setOperateusername(operateUserName);
                auditprojectchargedetail.setChargeguid(chargeGuid);// 收费记录标识
                auditprojectchargedetail.setChargeitemguid(record.get("itemguid").toString());// 收费条目标识
                auditprojectchargedetail.setIs_directinput(ZwfwConstant.CONSTANT_INT_ZERO);// 是否直接输入:否
                auditprojectchargedetail
                        .setCutfeeproportion(Double.parseDouble(record.get("Cutfeeproportion").toString()));// 收取比例
                auditprojectchargedetail
                        .setCutreason(record.get("cutreason") == null ? "" : record.get("cutreason").toString());// 减免理由
                auditprojectchargedetail.setItemsum(Double.parseDouble(record.get("itemsum").toString()));// 收费条目金额
                auditprojectchargedetail.setPrice(Double.parseDouble(record.get("price").toString()));// 单价
                auditprojectchargedetail.setQuantity(Double.parseDouble(record.get("quantity").toString()));// 数量
                auditprojectchargedetail.setStatus(ZwfwConstant.CONSTANT_INT_ZERO);// ?
                chargeDetailService.addProjectChargeDetail(auditprojectchargedetail);
            }
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("is_check=", ZwfwConstant.CONSTANT_STR_ONE);
                updateFieldMap.put("is_fee=", ZwfwConstant.CONSTANT_STR_ZERO);
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            HandleProjectService projectService = new HandleProjectService();
            // 添加操作纪录
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BJSF, operateUserGuid, operateUserName);
            // 更新时间表状态 恢复计时0 暂停计时1
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditProjectSparetime = projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            projectSparetime.updateSpareTime(auditProjectSparetime);
            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handlePriceCancel(AuditProject auditProject, String chargeGuid,
                                                       String operateUserName, String operateUserGuid, String cancelReason) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 1、删除办件收费明细记录 (现在保留核价的详细信息)
            // IAuditProjectChargeDetail chargeDetailService =
            // ContainerFactory.getContainInfo().getComponent(IAuditProjectChargeDetail.class);
            // chargeDetailService.deleteChargeDetailByChargeGuid(chargeGuid);
            // 2、更新办件信息
            auditProject.setBillno(null);
            auditProject.setIs_check(ZwfwConstant.CONSTANT_INT_ZERO);
            auditProject.setCheckuserguid(operateUserGuid);
            // 更新办件状态 恢复计时0 暂停计时1
            // auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            // 3、更新办件收费记录
            IAuditProjectCharge projectChargeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = projectChargeService.getProjectChargeByRowGuid(chargeGuid)
                    .getResult();
            auditprojectcharge.setIs_cancel(ZwfwConstant.CONSTANT_INT_ONE);
            auditprojectcharge.setCancelreason(cancelReason);
            projectChargeService.updateProjectCharge(auditprojectcharge);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("is_check=", ZwfwConstant.CONSTANT_STR_ZERO);
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BJSFQX, operateUserGuid, operateUserName,
                    "", cancelReason);
            // 更新时间表状态 恢复计时0 暂停计时1
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditProjectSparetime = projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
            projectSparetime.updateSpareTime(auditProjectSparetime);
            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<String> handlePriceCancel(AuditProject auditProject, String chargeGuid,
                                                       String operateUserName, String operateUserGuid, String cancelReason, Integer cancelType) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 1、删除办件收费明细记录 (现在保留核价的详细信息)
            // IAuditProjectChargeDetail chargeDetailService =
            // ContainerFactory.getContainInfo().getComponent(IAuditProjectChargeDetail.class);
            // chargeDetailService.deleteChargeDetailByChargeGuid(chargeGuid);
            // 2、更新办件信息
            auditProject.setBillno(null);
            auditProject.setIs_check(ZwfwConstant.CONSTANT_INT_ZERO);
            auditProject.setCheckuserguid(operateUserGuid);
            // 更新办件状态 恢复计时0 暂停计时1
            // auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
            // 3、更新办件收费记录
            IAuditProjectCharge projectChargeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectCharge.class);
            AuditProjectCharge auditprojectcharge = projectChargeService.getProjectChargeByRowGuid(chargeGuid)
                    .getResult();
            auditprojectcharge.setIs_cancel(ZwfwConstant.CONSTANT_INT_ONE);
            auditprojectcharge.setCancelreason(cancelReason);
            auditprojectcharge.setCanceltype(cancelType);
            projectChargeService.updateProjectCharge(auditprojectcharge);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMap.put("is_check=", ZwfwConstant.CONSTANT_STR_ZERO);
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // }
                // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BJSFQX, operateUserGuid, operateUserName,
                    "", cancelReason);
            // 更新时间表状态 恢复计时0 暂停计时1
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditProjectSparetime auditProjectSparetime = projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
            projectSparetime.updateSpareTime(auditProjectSparetime);
            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public AuditCommonResult<String> handlePause(AuditProject auditProject, String operateUserName,
                                                 String operateUserGuid, String workItemGuid, String messageItemGuid, String note) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        HandleProjectService handleProjectService = new HandleProjectService();
        try {
            IWorkflowActivityService activityService = ContainerFactory.getContainInfo()
                    .getComponent(IWorkflowActivityService.class);
            IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);

            ProcessVersionInstance pVersionInstance = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            IAuditProjectUnusual auditProjectUnusual = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            // 1、更新办件状态 恢复计时0 暂停计时1
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
            project.updateProject(auditProject);
            // 更新时间表状态 恢复计时0 暂停计时1
            AuditProjectSparetime auditProjectSparetime = projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            projectSparetime.updateSpareTime(auditProjectSparetime);

            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
            // 2、更新待办标题
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pVersionInstance, workItemGuid);
            WorkflowActivity activity = activityService.getByActivityGuid(workflowWorkItem.getActivityGuid());
            // new
            // WFAPI9().getDefinitionAPI().getWFActivityAPI().getActivity(workflowWorkItem);
            String title = "【暂停计时】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid, operateUserGuid);
            List<MessagesCenter> messagesCenter = messageCenterService
                    .getWaitHandleListByPviguid(auditProject.getPviguid());
            messagesCenter.removeIf(x -> messageItemGuid.equals(x.getMessageItemGuid()));
            if (!messagesCenter.isEmpty()) {
                for (MessagesCenter messages : messagesCenter) {
                    messageCenterService.updateMessageTitle(messages.getMessageItemGuid(), title,
                            messages.getTargetUser());
                }
            }
            // 3、添加办件特殊操作
            String unusualGuid = auditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT), workItemGuid, "暂停计时")
                    .getResult();
            result.setResult(unusualGuid);

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_ZTJS, operateUserGuid, operateUserName,
                    workItemGuid, note);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handlePause(AuditProject auditProject, String operateUserName,
                                                 String operateUserGuid, String workItemGuid, String messageItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        HandleProjectService handleProjectService = new HandleProjectService();
        try {
            IWorkflowActivityService activityService = ContainerFactory.getContainInfo()
                    .getComponent(IWorkflowActivityService.class);
            IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);

            ProcessVersionInstance pVersionInstance = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            IAuditProjectUnusual auditProjectUnusual = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            // 1、更新办件状态 恢复计时0 暂停计时1
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
            project.updateProject(auditProject);
            // 更新时间表状态 恢复计时0 暂停计时1
            AuditProjectSparetime auditProjectSparetime = projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            projectSparetime.updateSpareTime(auditProjectSparetime);
            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
            // 2、更新待办标题
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pVersionInstance, workItemGuid);
            WorkflowActivity activity = activityService.getByActivityGuid(workflowWorkItem.getActivityGuid());
            // new
            // WFAPI9().getDefinitionAPI().getWFActivityAPI().getActivity(workflowWorkItem);
            String title = "【暂停计时】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            handleProjectService.updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid, operateUserGuid);
            // 3、添加办件特殊操作
            String unusualGuid = auditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT), workItemGuid, "暂停计时")
                    .getResult();
            result.setResult(unusualGuid);

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_ZTJS, operateUserGuid, operateUserName,
                    workItemGuid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleRecover(AuditProject auditProject, String operateUserName,
                                                   String operateUserGuid, String workItemGuid, String messageItemGuid) {
        IWorkflowActivityService activityService = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowActivityService.class);
        IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 更新办件状态
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);// 0恢复 1暂停
            IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            iAuditProject.updateProject(auditProject);
            // 更新时间表状态 恢复计时0 暂停计时1
            AuditProjectSparetime auditProjectSparetime = projectSparetime
                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
            projectSparetime.updateSpareTime(auditProjectSparetime);
            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
            // 更新代办标题
            IWFInstanceAPI9 wfinstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pvi = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
            WorkflowWorkItem wfitem = wfinstance.getWorkItem(pvi, workItemGuid);
            WorkflowActivity wa = activityService.getByActivityGuid(wfitem.getActivityGuid());
            // 如果是到了下一步是核价步骤，则需要改成核价
            String title = "【" + wa.getActivityName() + "】" + auditProject.getProjectname() + "("
                    + auditProject.getApplyername() + ")";
            new HandleProjectService().updateProjectTitle(title, auditProject.getPviguid(), wfitem.getWaitHandleGuid(),
                    operateUserGuid);

            List<MessagesCenter> messagesCenter = messageCenterService
                    .getWaitHandleListByPviguid(auditProject.getPviguid());
            messagesCenter.removeIf(x -> messageItemGuid.equals(x.getMessageItemGuid()));
            if (!messagesCenter.isEmpty()) {
                for (MessagesCenter messages : messagesCenter) {
                    messageCenterService.updateMessageTitle(messages.getMessageItemGuid(), title,
                            messages.getTargetUser());
                }
            }

            // 添加办件特殊操作
            IAuditProjectUnusual iauditProjectUnusual = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            String unusualGuid = iauditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_HF), workItemGuid, "恢复计时")
                    .getResult();
            result.setResult(unusualGuid);

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_HFJS, operateUserGuid, operateUserName,
                    workItemGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleDelayPass(AuditProject auditProject, String operateUserName,
                                                     String operateUserGuid, String messageItemGuid, String AuditProjectUnusualGuid, String sqrUserGuid,
                                                     String oldmessageitemguid) {
        // 1、特殊操作类型：延期
        IAuditProjectUnusual iauditProjectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        // IAuditProject iAuditProject =
        // ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        AuditCommonResult<AuditProjectUnusual> auditProjectUnusualResult = iauditProjectUnusual
                .getAuditProjectUnusualByRowguid(AuditProjectUnusualGuid);
        AuditProjectUnusual auditProjectUnusual = auditProjectUnusualResult.getResult();
        auditProjectUnusual.setOperatetype(Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_YQ));
        auditProjectUnusual.setAuditresult(1);
        auditProjectUnusual.setAudituserguid(operateUserGuid);
        auditProjectUnusual.setAuditusername(operateUserName);
        iauditProjectUnusual.updateProjectUnusual(auditProjectUnusual);
        // 2、办件信息更新
        auditProject.setIs_delay(1);
        IAuditOrgaWorkingDay workingDayService = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaWorkingDay.class);
        Date shouldEndDay = workingDayService.getWorkingDayWithOfficeSet(auditProject.getCenterguid(),
                auditProject.getPromiseenddate(), auditProjectUnusual.getDelayworkday()).getResult();
        auditProject.setPromiseenddate(shouldEndDay);
        project.updateProject(auditProject);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        // 3、计时更新
        IAuditProjectSparetime iauditProjectSparetime = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        AuditCommonResult<AuditProjectSparetime> AuditProjectSparetimeResult = iauditProjectSparetime
                .getSparetimeByProjectGuid(auditProject.getRowguid());
        AuditProjectSparetime auditProjectSparetime = AuditProjectSparetimeResult.getResult();
        if (auditProjectSparetime != null) {
            int sparemunites = auditProjectSparetime.getSpareminutes()
                    + (24 * 60 * (auditProjectUnusual.getDelayworkday()));
            auditProjectSparetime.setSpareminutes(sparemunites);
            iauditProjectSparetime.updateSpareTime(auditProjectSparetime);
        }
        // 5、更新原办件待办
        IMessagesCenterService messagecenterservice = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        // 删除待办
        messagecenterservice.deleteMessage(messageItemGuid, operateUserGuid);
        // 删除其他延期处理待办(先根据特殊操作处理人的角色找到收件人，等框架增加新方法，再做处理)
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "办件特殊操作审核");
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            String ouGuid = auditOrgaAreaService.getAreaByAreacode(auditProject.getAreacode()).getResult().getOuguid();
            // 2、获取该角色的对应的人员
            List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false, 3);
            if (listUser != null && listUser.size() > 0) {
                // 3、发送待办给审核人员
                for (FrameUser frameUser : listUser) {
                    messagecenterservice.deleteMessageByIdentifier(auditProject.getRowguid(), frameUser.getUserGuid());
                }
            }
        }
        MessagesCenter messagescenter = messagecenterservice.getDetail(oldmessageitemguid, sqrUserGuid);
        if (messagescenter != null) {
            messagecenterservice
                    .updateMessageTitle(
                            messagescenter.getMessageItemGuid(), "【" + messagescenter.getBeiZhu() + "】"
                                    + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")",
                            sqrUserGuid);
        }
        // 添加操作纪录
        HandleProjectService projectService = new HandleProjectService();
        projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YQSHTG, operateUserGuid, operateUserName, "",
                "延期申请审核通过！");

        // 系统提醒
        IOnlineMessageInfoService onlinemessage = ContainerFactory.getContainInfo()
                .getComponent(IOnlineMessageInfoService.class);
        onlinemessage.insertMessage(UUID.randomUUID().toString(), operateUserGuid, operateUserName, sqrUserGuid,
                userService.getUserNameByUserGuid(sqrUserGuid), sqrUserGuid,
                auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")" + "延期申请审核通过！", new Date());
        return result;
    }

    @Override
    public AuditCommonResult<String> handleDelay(AuditProject auditProject, String operateUserName,
                                                 String operateUserGuid, int yanqiday, String reason, String workItemGuid, String messageItemGuid) {
        String message = "";
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditProjectUnusual projectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        try {
            // 判断承诺截止时间是否为空且延期时间是否为空
            if (auditProject.getPromise_day() != null) {
                if (yanqiday > 0) {
                    String unusualguid = UUID.randomUUID().toString();
                    // 1、给办件特殊操作审核角色人员发送待办
                    message = new HandleProjectService().sendMGSToYQSH(unusualguid, auditProject, operateUserGuid,
                            messageItemGuid);
                    if ("ok".equals(message)) {
                        // 2、插入异常记录
                        AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();

                        auditProjectUnusual.setRowguid(unusualguid);
                        auditProjectUnusual.setOperateuserguid(operateUserGuid);
                        auditProjectUnusual.setOperateusername(operateUserName);
                        auditProjectUnusual.setOperatedate(new Date());
                        auditProjectUnusual.setAuditresult(0);
                        auditProjectUnusual.setOperatetype(Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_YQ));
                        auditProjectUnusual.setDelayworkday(yanqiday);
                        auditProjectUnusual.setNote(reason);
                        auditProjectUnusual.setProjectguid(auditProject.getRowguid());
                        auditProjectUnusual.setPviguid(auditProject.getPviguid());
                        auditProjectUnusual.setWorkitemguid(workItemGuid);

                        projectUnusual.addProjectUnusual(auditProjectUnusual);
                        // 4、更新原有的办件的待办
                        String title = "【延期申请】" + auditProject.getProjectname() + "(" + auditProject.getApplyername()
                                + ")";
                        new HandleProjectService().updateProjectTitle(title, auditProject.getPviguid(), messageItemGuid,
                                operateUserGuid);
                        // 5、更新办件 此处只是申请，所以并未真正延期
                        auditProject.setIs_delay(10);// 延期申请中
                        project.updateProject(auditProject);
                        message = message + ";" + unusualguid;

                        // 添加操作纪录
                        HandleProjectService projectService = new HandleProjectService();
                        projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_BJYQ, operateUserGuid,
                                operateUserName, workItemGuid, reason);
                    }
                } else {
                    message = "延期天数不能小于0！";
                }
            } else {
                message = "此办件承诺期限为0，不能延期！";
            }
            result.setResult(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleDelayNotPass(AuditProject auditProject, String auditProjectUnusualGuid,
                                                        String operateUserName, String operateUserGuid, String messageItemGuid, String sqrUserGuid,
                                                        String oldmessageitemguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditProjectUnusual projectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        try {
            AuditProjectUnusual auditprojectunusual = projectUnusual
                    .getAuditProjectUnusualByRowguid(auditProjectUnusualGuid).getResult();
            auditprojectunusual.setOperatetype(Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_YQTH));
            auditprojectunusual.setAuditresult(1);
            auditprojectunusual.setAudituserguid(operateUserGuid);
            auditprojectunusual.setAuditusername(operateUserName);
            projectUnusual.updateProjectUnusual(auditprojectunusual);
            auditProject.setIs_delay(0);
            project.updateProject(auditProject);
            // 删除待办
            messagesCenterService.deleteMessage(messageItemGuid, operateUserGuid);
            // 删除其他延期处理待办(先根据特殊操作处理人的角色找到收件人，等框架增加新方法，再做处理)
            IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
            IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "办件特殊操作审核");
            IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                String ouGuid = auditOrgaAreaService.getAreaByAreacode(auditProject.getAreacode()).getResult()
                        .getOuguid();
                // 2、获取该角色的对应的人员
                List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false,
                        3);
                if (listUser != null && listUser.size() > 0) {
                    // 3、发送待办给审核人员
                    for (FrameUser frameUser : listUser) {
                        messagesCenterService.deleteMessageByIdentifier(auditProject.getRowguid(),
                                frameUser.getUserGuid());
                    }
                }
            }
            // 2、更新原办件待办
            MessagesCenter messagescenter = messagesCenterService.getDetail(oldmessageitemguid, sqrUserGuid);
            if (messagescenter != null) {
                messagesCenterService.updateMessageTitle(
                        messagescenter.getMessageItemGuid(), "【" + messagescenter.getBeiZhu() + "】"
                                + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")",
                        sqrUserGuid);
            }

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YQSHBTG, operateUserGuid, operateUserName);
            // 系统提醒
            IOnlineMessageInfoService onlinemessage = ContainerFactory.getContainInfo()
                    .getComponent(IOnlineMessageInfoService.class);
            onlinemessage.insertMessage(UUID.randomUUID().toString(), operateUserGuid, operateUserName, sqrUserGuid,
                    userService.getUserNameByUserGuid(sqrUserGuid), sqrUserGuid,
                    auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")" + "延期申请审核已退回！",
                    new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleRevoke(AuditProject auditProject, String operateUserName,
                                                  String operateUserGuid, String workItemGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        HandleProjectService handleProjectService = new HandleProjectService();
        try {
            String unusualGuid = handleProjectService.handleRevokeOrSuspension(auditProject, operateUserName,
                    operateUserGuid, ZwfwConstant.SHENPIOPERATE_TYPE_CX, workItemGuid, reason);
            result.setResult(unusualGuid);

            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_CXSQ, operateUserGuid, operateUserName,
                    workItemGuid, reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleSuspension(AuditProject auditProject, String operateUserName,
                                                      String operateUserGuid, String workItemGuid, String reason) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        HandleProjectService handleProjectService = new HandleProjectService();
        try {
            String unusualGuid = handleProjectService.handleRevokeOrSuspension(auditProject, operateUserName,
                    operateUserGuid, ZwfwConstant.SHENPIOPERATE_TYPE_ZZ, workItemGuid, reason);
            result.setResult(unusualGuid);
            // 添加操作纪录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_YCZZ, operateUserGuid, operateUserName,
                    workItemGuid, reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleEvaluate(AuditProject auditProject, String operateUserName,
                                                    String operateUserGuid) {

        // 添加操作纪录
        HandleProjectService projectService = new HandleProjectService();
        projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_PJ, operateUserGuid, operateUserName);

        return null;
    }

    @Override
    public AuditCommonResult<String> handleResult(AuditProject auditProject, String operateUserName,
                                                  String operateUserGuid) {

        // 添加操作纪录
        HandleProjectService projectService = new HandleProjectService();
        projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_JGFF, operateUserGuid, operateUserName);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AuditCommonResult<List<SelectItem>> initDocList(String projectGuid, String tasktype) {
        AuditCommonResult<List<SelectItem>> result = new AuditCommonResult<List<SelectItem>>();
        try {
            List<SelectItem> jpdDoc = null;
            jpdDoc = new ArrayList<SelectItem>();
            // 获取代码项配置的所有的文书列表
            List<SelectItem> jpddoclist = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "文书类型", null, false));
            // 不同类型事项返回的文书列表
            List<SelectItem> Doclist = new ArrayList<SelectItem>();
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(tasktype)) {
                // 即办件又弹文书啦
                for (int i = 0; i < jpddoclist.size(); i++) {
                    // 即办件过滤材料接收凭证、决定延期通知书、材料补正告知书
                    String docvalue = jpddoclist.get(i).getValue().toString();
                    if (!(String.valueOf(ZwfwConstant.DOC_TYPE_CLJSPZ).equals(docvalue)
                            || String.valueOf(ZwfwConstant.DOC_TYPE_JDYQTZS).equals(docvalue)
                            || String.valueOf(ZwfwConstant.DOC_TYPE_CLBZGZS).equals(docvalue))) {
                        Doclist.add(jpddoclist.get(i));
                    }
                }
                jpdDoc = Doclist;
            } else {
                // 非即办件过滤材料接收凭证、决定延期通知书
                for (int i = 0; i < jpddoclist.size(); i++) {
                    String docvalue = jpddoclist.get(i).getValue().toString();
                    if (!(String.valueOf(ZwfwConstant.DOC_TYPE_CLJSPZ).equals(docvalue)
                            || String.valueOf(ZwfwConstant.DOC_TYPE_JDYQTZS).equals(docvalue))) {
                        Doclist.add(jpddoclist.get(i));
                    }
                }
                jpdDoc = Doclist;
            }
            // 办件已经创建
            if (StringUtil.isNotBlank(projectGuid)) {
                String s = "";
                IAuditProjectDocSnap docSnapService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectDocSnap.class);
                List<AuditProjectDocSnap> auditprojectDocsnaplist = docSnapService
                        .selectDocSnapByProjectGuid(projectGuid).getResult();
                for (int i = 0; i < jpdDoc.size(); i++) {
                    if (auditprojectDocsnaplist != null) {
                        for (int j = 0; j < auditprojectDocsnaplist.size(); j++) {
                            if (jpdDoc.get(i).getValue().toString()
                                    .equals(String.valueOf(auditprojectDocsnaplist.get(j).getDoctype()))) {
                                s = "(已打印)";
                                break;
                            } else {
                                s = "";
                            }
                        }
                    }
                    String label = jpdDoc.get(i).getText();
                    jpdDoc.get(i).setText(label + s);
                }
            }
            jpdDoc.add(0, new SelectItem("", "--文书列表--"));
            result.setResult(jpdDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Map<String, String>>> initProjectDocList(String projectGuid, String taskGuid,
                                                                           String centerGuid, String tasktype, String notOpenDocs, boolean isdoc) {
        AuditCommonResult<List<Map<String, String>>> result = new AuditCommonResult<List<Map<String, String>>>();
        try {
            List<Map<String, String>> listDoc = new ArrayList<Map<String, String>>();
            // 获取代码项配置的所有的文书列表
            ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                    .getComponent(ICodeItemsService.class);
            List<CodeItems> listDocs = codeItemsService.listCodeItemsByCodeName("文书类型");
            notOpenDocs = notOpenDocs + ",";

            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, false).getResult();

            String temptaskguid = "";
            if (auditTask != null) {
                temptaskguid = auditTaskService.getTemplateTaskGuid(auditTask.getAreacode()).getResult();
            }

            IAuditProjectDocSnap docSnapService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectDocSnap.class);
            IHandleDoc handleDocService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
            List<AuditProjectDocSnap> auditprojectDocsnaplist = docSnapService.selectDocSnapByProjectGuid(projectGuid)
                    .getResult();
            // 相同的文书取最新的
            if (auditprojectDocsnaplist != null && auditprojectDocsnaplist.size() > 0) {
                for (int i = 0; i < auditprojectDocsnaplist.size(); i++) {
                    for (int j = auditprojectDocsnaplist.size() - 1; j > i; j--) {
                        if (auditprojectDocsnaplist.get(i).getDoctype()
                                .equals(auditprojectDocsnaplist.get(j).getDoctype())) {
                            if (auditprojectDocsnaplist.get(i).getOperatedate().getTime() > auditprojectDocsnaplist
                                    .get(j).getOperatedate().getTime()) {
                                auditprojectDocsnaplist.remove(j);
                            } else {
                                auditprojectDocsnaplist.remove(i);
                            }
                        }
                    }
                }
            }

            for (CodeItems docItem : listDocs) {
                Map<String, String> doc;
                // 如果不包含在不弹出的文书中，那么进行加载
                if (notOpenDocs.indexOf(docItem.getItemValue() + ",") <= -1) {
                    boolean addFlag = true;
                    Iterator<AuditProjectDocSnap> iterator = auditprojectDocsnaplist.iterator();
                    while (iterator.hasNext()) {
                        AuditProjectDocSnap docSnap = iterator.next();
                        doc = new HashMap<String, String>(16);
                        if (docItem.getItemValue().equals(String.valueOf(docSnap.getDoctype()))) {
                            doc.put("docName", docItem.getItemText());
                            // 如果是有word标识的，那就返回标识，否则返回地址
                            if (StringUtil.isNotBlank(docSnap.getDocattachguid())) {
                                doc.put("docUrl", docSnap.getDocattachguid());
                                doc.put("isWord", "1");
                            } else {
                                // 如果已经生成了，并且确定不是通过word的，那就直接返回html的地址
                                doc.put("docUrl", handleDocService

                                        .getDocEditPageUrl(taskGuid, centerGuid, docItem.getItemValue(), false,
                                                temptaskguid)
                                        .getResult());
                                doc.put("isWord", "0");
                            }
                            doc.put("isPrint", "1");
                            iterator.remove();
                            addFlag = false;
                            listDoc.add(doc);
                        }
                    }
                    if (addFlag) {
                        doc = new HashMap<String, String>(16);
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

            result.setResult(listDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleMaterialNotPass(AuditProject auditProject, String operateUserName,
                                                           String operateUserGuid, String workItemGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            IAuditProject audtiProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            // 1、更新办件状态
            // auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
            // audtiProjectService.updateProject(auditProject);
            // // 更新时间表状态 恢复计时0 暂停计时1
            // AuditProjectSparetime auditProjectSparetime = projectSparetime
            // .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            // auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            // projectSparetime.updateSpareTime(auditProjectSparetime);
            // 2、插入异常操作
            String note = "因以下原因计时暂停：缺失必要办件";
            IAuditProjectUnusual auditProjectUnusual = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectUnusual.class);
            String unusualGuid = auditProjectUnusual.insertProjectUnusual(operateUserGuid, operateUserName,
                    auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_ZT), workItemGuid, note)
                    .getResult();
            result.setResult(unusualGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> handleProjectPass(AuditProject auditProject, String messageItemGuid,
                                                     String operateUserGuid, String workItemGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            log.info("批准短信发送");

            if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == auditProject.getStatus()) {
                // 补正状态更新材料审核状态
                IAuditProjectMaterial projectMaterialService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectMaterial.class);
                ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo()
                        .getComponent(ISendMQMessage.class);
                ;

                // 查找是否存在补正材料
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("projectguid", auditProject.getRowguid());
                sqlc.in("auditstatus", "'" + ZwfwConstant.Material_AuditStatus_DBZ + "','"
                        + ZwfwConstant.Material_AuditStatus_YBZ + "'");
                Integer materialnum = projectMaterialService.getProjectMaterialCountByCondition(sqlc.getMap())
                        .getResult();
                if (materialnum != null && materialnum > 0) {
                    // 发送补正完成代办
                    String msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "." + operateUserGuid;
                    sendMQMessageService.sendByExchange("exchange_handle", msg,
                            "project." + auditProject.getAreacode() + ".bzwc." + auditProject.getTask_id());
                }

                projectMaterialService.updateAllAuditStatus(auditProject.getRowguid());
            }
            IWFInstanceAPI9 instanceapi = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);

            IJNAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IJNAuditProject.class);

            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService
                    .getAuditTaskByGuid(auditProject.getTaskguid(), auditProject.getAcceptareacode(), true).getResult();
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            String taskType = auditProject.getTasktype().toString();
            String pviGuid = auditProject.getPviguid();
            String projectGuid = auditProject.getRowguid();

            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                // 审核通过，计时结束
                AuditProjectSparetime auditProjectSparetime = projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                // 这里判断一下是不是存在没有剩余时间的情况，如果没有剩余时间，就不在针对这两个字段进行操作
                if (auditProjectSparetime != null && !auditProjectSparetime.isEmpty()) {
                    int spendtime = auditProjectSparetime.getSpendminutes();// 已用时间
                    int sparetime = auditProjectSparetime.getSpareminutes();// 剩余的时间
                    // 如果已用时为0，可能没有刷新计时，就一办结
                    if (spendtime == 0 && auditProject.getAcceptuserdate() != null) {
                        long start = auditProject.getAcceptuserdate().getTime();
                        long end = System.currentTimeMillis();
                        spendtime = (int) ((end - start) / (1000 * 60));
                        sparetime = sparetime - spendtime;
                    }
                    auditProject.setSparetime(sparetime);
                    auditProject.setSpendtime(spendtime);
                }
                // 剩余时间临时表记录
                if (auditProjectSparetime != null) {
                    projectSparetime.deleteSpareTimeByRowGuid(auditProjectSparetime.getRowguid());
                    // 如果是并联审批办件，判断并联审批是否需要计时
                    if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                        handleBlspSparetime(auditProject.getSubappguid());
                    }
                }

            }
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPTG);
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType) || ZwfwConstant.ITEMTYPE_CNJ.equals(taskType)) {
                auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_ZYXK);
                auditProject.setBanwandate(new Date());
            }

            auditProjectService.updateProject(auditProject);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMapOnline = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMapOnline.put("status=", auditProject.getStatus().toString());
                updateFieldMapOnline.put("banjieresult=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPTG));
                updateFieldMapOnline.put("spendtime=", String.valueOf(auditProject.getSpendtime()));
                SqlConditionUtil sqlnew = new SqlConditionUtil();
                sqlnew.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sqlnew.eq("applyerguid",
                // auditProject.getOnlineapplyerguid());
                // }
                /*
                 * if (applyerguid != null) {
                 * sqlnew.eq("applyerguid", applyerguid);
                 * }
                 * else {
                 * sqlnew.eq("applyerguid", auditProject.getApplyeruserguid());
                 * }
                 */
                auditOnlineProjectService.updateOnlineProject(updateFieldMapOnline, updateDateFieldMap,
                        sqlnew.getMap());
            }

            // 自动暂停计时
            if (ZwfwConstant.ITEMTYPE_SBJ.equals(taskType)) {
                IWFInstanceAPI9 wfinstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
                IAuditProjectUnusual unusualService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditProjectUnusual.class);
                IMessagesCenterService mcservice = ContainerFactory.getContainInfo()
                        .getComponent(IMessagesCenterService.class);

                ProcessVersionInstance pVersionInstance = wfinstance.getProcessVersionInstance(pviGuid);
                List<Integer> status = Arrays.asList(WorkflowKeyNames9.WorkItemStatus_Active);

                List<WorkflowWorkItem> list = iAuditProject.getWorkItemListByPVIGuidAndStatus(pviGuid);

                // List<WorkflowWorkItem> list =
                // wfinstance.getWorkItemListByPVIGuidAndStatus(pVersionInstance,
                // status);
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
                    auditProjectUnusual.setWorkitemguid(list.get(0).getWorkItemGuid());
                    unusualService.addProjectUnusual(auditProjectUnusual);
                }

                String title = "";
                MessagesCenter messagesCenter = mcservice.getDetail(messageItemGuid, operateUserGuid);
                if (messagesCenter != null) {
                    title = "【已暂停】" + messagesCenter.getTitle();
                    mcservice.updateMessageTitle(messagesCenter.getMessageItemGuid(), title, operateUserGuid);
                }
            }
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType) || ZwfwConstant.ITEMTYPE_CNJ.equals(taskType)) {

                // 处理共享材料
                String ownGuid = "";
                IAuditIndividual auditIndividualService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditIndividual.class);
                IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyBaseinfo.class);
                // 企业类型申请人
                if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfoService
                            .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                    ownGuid = auditRsCompanyBaseinfo.getCompanyid();
                }
                // 个人
                else if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                    AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = auditIndividualService
                            .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    ownGuid = auditRsIndividualBaseinfo.getPersonid();
                }
                if (StringUtil.isBlank(ownGuid)) {
                    ownGuid = auditProject.getApplyeruserguid();
                }
                FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
                IHandleRSMaterial handleRSMaterialService = ContainerFactory.getContainInfo()
                        .getComponent(IHandleRSMaterial.class);
                handleRSMaterialService.insertShareMaterial(auditProject.getTaskguid(), auditProject.getRowguid(),
                        ownGuid, operateUserGuid, auditProject.getOperateusername(), auditProject.getApplyername(),
                        auditProject.getCertnum(), auditProject.getApplyertype(), auditProject.getCerttype(),
                        auditProject.getAreacode(), ou.get("orgcode"));

                // 批准短信
                IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                        .getComponent(IMessagesCenterService.class);
                if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                    String targetUser = "";
                    String targetDispName = "";
                    // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                    if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                        targetUser = auditProject.getAcceptuserguid();
                        targetDispName = auditProject.getAcceptusername();
                    } else {
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
                    // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                    // content, new Date(), 0,
                    // new Date(), auditProject.getContactmobile(), targetUser,
                    // targetDispName, operateUserGuid,
                    // auditProject.getOperateusername(), "", "", "", false,
                    // "短信");
                }
            }
            // 增加审批通过记录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_SPTG, operateUserGuid, "", workItemGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> handleProjectNotPass(AuditProject auditProject, String OperateUserGuid,
                                                        String workItemGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {
            log.info("不予批准短信发送");
            // Map<String, String> updateFieldMap = new HashMap<>();
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IWFInstanceAPI9 instanceapi = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditProjectSparetime projectSparetime = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            String taskType = auditProject.getTasktype().toString();
            // 非即办件或非正常模式即办件且非自建系统时，审核通过则计时结束
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())
                    || !ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())) {

                AuditProjectSparetime auditProjectSparetime = projectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                // 这里判断一下是不是存在没有剩余时间的情况，如果没有剩余时间，就不在针对这两个字段进行操作
                if (auditProjectSparetime != null && !auditProjectSparetime.isEmpty()) {
                    int spendtime = auditProjectSparetime.getSpendminutes();// 已用时间
                    int sparetime = auditProjectSparetime.getSpareminutes();// 剩余的时间
                    // 如果已用时为0，可能没有刷新计时，就一办结
                    if (spendtime == 0 && auditProject.getAcceptuserdate() != null) {
                        long start = auditProject.getAcceptuserdate().getTime();
                        long end = System.currentTimeMillis();
                        spendtime = (int) ((end - start) / (1000 * 60));
                        sparetime = sparetime - spendtime;
                    }
                    auditProject.setSparetime(sparetime);
                    auditProject.setSpendtime(spendtime);
                }
                // 剩余时间临时表记录
                if (auditProjectSparetime != null) {
                    projectSparetime.deleteSpareTimeByRowGuid(auditProjectSparetime.getRowguid());
                    // 如果是并联审批办件，判断并联审批是否需要计时
                    if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                        handleBlspSparetime(auditProject.getSubappguid());
                    }
                }
            }
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPBTG);
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType) || ZwfwConstant.ITEMTYPE_CNJ.equals(taskType)) {
                auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_BYXK);
                auditProject.setBanwandate(new Date());
            }

            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);

            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMapOnline = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                updateFieldMapOnline.put("status=", auditProject.getStatus().toString());
                updateFieldMapOnline.put("banjieresult=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG));
                updateFieldMapOnline.put("spendtime=", String.valueOf(auditProject.getSpendtime()));
                SqlConditionUtil sqlnew = new SqlConditionUtil();
                sqlnew.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sqlnew.eq("applyerguid",
                // auditProject.getOnlineapplyerguid());
                // }
                /*
                 * if (applyerguid != null) {
                 * sqlnew.eq("applyerguid", applyerguid);
                 * }
                 * else {
                 * sqlnew.eq("applyerguid", auditProject.getApplyeruserguid());
                 * }
                 */
                auditOnlineProjectService.updateOnlineProject(updateFieldMapOnline, updateDateFieldMap,
                        sqlnew.getMap());
            }

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                    ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(auditProject.getPviguid());
                    if (pvi != null) {
                        WorkflowWorkItem workflowWorkItem = instanceapi.getWorkItem(pvi, workItemGuid);
                        if (workflowWorkItem != null) {
                            content = content.replace("[#=Reason#]", workflowWorkItem.getOpinion());
                        }
                    }
                }
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // OperateUserGuid,
                // auditProject.getOperateusername(), "", "", "", false, "短信");
            }

            // 增加审批不通过记录
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_SPBTG, OperateUserGuid, "", workItemGuid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> InitBLSPProject(String taskGuid, String projectGuid, String operateUserName,
                                                     String operateUserGuid, String certtype, String certnum, String windowGuid, String windowName,
                                                     String centerGuid, String biGuid, String subappGuid, String businessGuid, String certNum,
                                                     String applyerName) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IHandleFlowSn flowSn = ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);// 办件状态：初始化
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setAreacode(auditTask.getAreacode());
                IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                }
                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
            }
            auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));// 申请人类型：个人
            auditproject.setApplydate(new Date());// 办件申请时间
            auditproject.setReceivedate(new Date());// 办件接件时间
            auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 办件申请方式：窗口申请
            if (StringUtil.isNotBlank(certtype)) {
                auditproject.setCerttype(certtype);// 并联审批传过来的证照类型
            } else {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM); // 申请人证照类型：身份证
            }
            // 对应法人证照号
            if (StringUtil.isNotBlank(certnum)) {
                auditproject.setCertnum(certnum);
            } else {
                auditproject.setCertnum(certNum);
            }
            IAuditRsItemBaseinfo auditRsItemBaseinfoImpl = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsItemBaseinfo.class);
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("BIGUID", biGuid);
            AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfoImpl
                    .selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult().get(0);
            auditproject.setXiangmubh(auditRsItemBaseinfo.getRowguid());
            auditproject.setApplyername(applyerName);
            auditproject.setBiguid(biGuid);
            auditproject.set("subappguid", subappGuid);
            auditproject.setBusinessguid(businessGuid);
            auditproject.setReceivedate(new Date());
            auditproject.setReceiveuserguid(operateUserGuid);
            auditproject.setReceiveusername(operateUserName);
            // auditproject.setIssendsms(StringUtil.isBlank(asissendmessage)
            // ? ZwfwConstant.CONSTANT_STR_ZERO
            // : ZwfwConstant.CONSTANT_STR_ONE);// 是否发送短信
            auditproject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.setIs_delay(20);// 是否延期
            // 来源（外网还是其他系统）
            String resource = "1";
            // 获取事项ID
            String unid = auditTask.getStr("unid");
            // 请求接口获取受理编码
            if (StringUtil.isNotBlank(unid)) {
                // 请求接口获取受理编码
                if (StringUtil.isNotBlank(unid)) {
                    String resultflowsn = FlowsnUtil.createReceiveNum(unid, auditTask.getRowguid());
                    if (!"error".equals(resultflowsn)) {
                        auditproject.setFlowsn(resultflowsn);
                        log.info("========================>获取受理编码成功！" + resultflowsn);
                    } else {
                        log.info("========================>获取受理编码失败！");
                    }
                }

                /*
                 * // 构造获取受理编码的请求入参
                 * String params2Get = "?itemId=" + unid + "&applyFrom=" +
                 * resource;
                 * try {
                 * JSONObject jsonObj =
                 * WavePushInterfaceUtils.createReceiveNum(params2Get,
                 * auditTask.getShenpilb());
                 * if (jsonObj != null &&
                 * "200".equals(jsonObj.getString("state"))) {
                 * log.info("========================>获取受理编码成功！" +
                 * jsonObj.getString("receiveNum") + "#####"
                 * + jsonObj.getString("password"));
                 * String pwd = jsonObj.getString("password");
                 * String receiveNum = jsonObj.getString("receiveNum");
                 * auditproject.setFlowsn(receiveNum);
                 * auditproject.set("pwd", pwd);
                 * }
                 * else {
                 * log.info("========================>获取受理编码失败！");
                 * }
                 * }
                 * catch (IOException e) {
                 * log.info("接口请求报错！========================>" +
                 * e.getMessage());
                 * e.printStackTrace();
                 * }
                 */
            }

            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);

            if (StringUtil.isBlank(unid) || StringUtil.isBlank(auditproject.getFlowsn())) {
                // 3、产生办件流水号
                // HandleProjectService projectService = new
                // HandleProjectService();
                // projectService.checkFlowSN(auditproject);
                String unid1 = auditTask.getStr("unid");
                String resultflowsn = FlowsnUtil.createReceiveNum(unid1, auditTask.getRowguid());
                auditproject.setFlowsn(resultflowsn);
                auditProjectService.updateProject(auditproject);
            }

            // 直接赋值给相关数据ProjectGuid
            // wfcontextvalueservice.createOrUpdateContext(processVersionInstanceGuid,
            // "ProjectGuid",
            // auditproject.getRowguid(),
            // WorkflowKeyNames9.ParameterType_T_string, 2);

            /*
             * IAuditTaskMaterial auditTaskMaterialService =
             * ContainerFactory.getContainInfo()
             * .getComponent(IAuditTaskMaterial.class);
             */
            result.setResult(rowGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> InitIntegratedProject(String taskGuid, String projectGuid, String operateUserName,
                                                           String operateUserGuid, String windowGuid, String windowName, String centerGuid, String biGuid,
                                                           String subappGuid, String businessGuid, String applyway) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IHandleFlowSn flowSn = ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
            IAuditSpIntegratedCompany iAuditSpIntegratedCompany = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpIntegratedCompany.class);
            AuditSpIntegratedCompany auditSpIntegratedCompany = iAuditSpIntegratedCompany
                    .getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid()).getResult();
            IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            AuditOnlineProject auditOnlineProject = iAuditOnlineProject.getOnlineProjectByTaskGuid(biGuid, businessGuid)
                    .getResult();
            IAuditOnlineIndividual auditOnlineIndividualService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineIndividual.class);
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);// 办件状态：初始化
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setAreacode(auditTask.getAreacode());
                IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                }
                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
            }

            // 根据主题处理地址来判断申请人类型
            IAuditSpBusiness iAuditSpBusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
            AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
            String handleUrl = auditSpBusiness.getHandleURL();

            if ("epointzwfw/auditsp/auditindividual".equals(handleUrl)) {
                auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));// 申请人类型：个人
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ); // 申请人证照类型：身份证
            } else {
                auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));// 申请人类型：企业
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM); // 申请人证照类型：统一社会信用代码
            }

            auditproject.setApplydate(new Date());// 办件申请时间
            auditproject.setApplyway(Integer.parseInt(applyway));// 办件申请方式：窗口申请
            auditproject.setBiguid(biGuid);
            auditproject.setSubappguid(subappGuid);
            auditproject.setReceivedate(new Date());
            auditproject.setReceiveuserguid(operateUserGuid);
            auditproject.setReceiveusername(operateUserName);
            auditproject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.setIs_delay(20);// 是否延期
            auditproject.setApplyername(auditSpIntegratedCompany.getCompanyname());
            if (auditOnlineProject != null) {
                auditproject.setOnlineapplyerguid(auditOnlineProject.getApplyerguid());
                auditproject.setApplyeruserguid(auditOnlineProject.getApplyerguid());
            }
            auditproject.setCertnum(auditSpIntegratedCompany.getZzjgdmz());// 组织机构代码证
            auditproject.setAddress(auditSpIntegratedCompany.getAddress());
            auditproject.setLegal(auditSpIntegratedCompany.getLegalpersonname());
            auditproject.setContactperson(auditSpIntegratedCompany.getContactperson());
            auditproject.setContactcertnum(auditSpIntegratedCompany.getContactcertnum());
            auditproject.setLegal(auditSpIntegratedCompany.getContactperson());
            auditproject.setLegalid(auditSpIntegratedCompany.getContactcertnum());
            auditproject.setContactphone(auditSpIntegratedCompany.getContactphone());
            auditproject.setContactmobile(auditSpIntegratedCompany.getContactphone());
            auditproject.setContactpostcode(auditSpIntegratedCompany.getContactpostcode());
            auditproject.setContactemail(auditSpIntegratedCompany.getLegalpersonemail());
            AuditOnlineIndividual auditonlineindividual = auditOnlineIndividualService
                    .getIndividualByApplyerGuid(auditproject.getApplyeruserguid()).getResult();
            if (auditonlineindividual != null) {
                auditproject.setLegal(auditonlineindividual.getClientname());
                auditproject.setLegalid(auditonlineindividual.getIdnumber());
            }
            /*
             * String numberFlag =
             * handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE,
             * centerGuid).getResult();
             * if (StringUtil.isBlank(numberFlag)) {
             * numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
             * }
             * auditproject.setFlowsn(flowSn.getFlowsn("办件编号",
             * numberFlag).getResult());
             */

            HandleProjectService projectService = new HandleProjectService();
            // 3、产生办件流水号
            // IAuditTask auditTaskService =
            // ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            // String numberFlag =
            // handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE,
            // auditproject.getCenterguid())
            // .getResult();
            // if (StringUtil.isBlank(numberFlag)) {
            // numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
            // }
            // String flowsn = flowSn.getFlowsn("办件编号", numberFlag).getResult();

            String flowsn = "";
            // 获取事项ID
            String unid = auditTask.getStr("unid");
            // 请求接口获取受理编码
            if (StringUtil.isNotBlank(unid)) {
                String resultflowsn = FlowsnUtil.createReceiveNum(unid, auditTask.getRowguid());
                if (!"error".equals(resultflowsn)) {
                    log.info("========================>获取受理编码成功！" + resultflowsn);
                    flowsn = resultflowsn;
                } else {
                    log.info("========================>获取受理编码失败！");
                }
            }

            auditproject.setFlowsn(flowsn);// 设置办件流水号
            auditproject.setBusinessguid(businessGuid);

            if (auditTask != null && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTask.getIs_enable().toString())) {
                auditProjectService.addProject(auditproject);
            } else {
                // 如果禁用或者事项为空，设置proejctguid为空
                rowGuid = "";
            }
            result.setResult(rowGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> InitBornIntegratedProject(String taskGuid, String projectGuid,
                                                               String operateUserName, String operateUserGuid, String windowGuid, String windowName, String centerGuid,
                                                               String biGuid, String subappGuid, String businessGuid, String applyway) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            IHandleFlowSn flowSn = ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
            IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditTaskExtension iaudittaskextension = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditYjsCsbService iAuditYjsCsbService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditYjsCsbService.class);
            IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            AuditSpInstance auditSpInstance = iauditspinstance.getDetailByBIGuid(biGuid).getResult();
            AuditYjsCsb dataBean = iAuditYjsCsbService.find(auditSpInstance.getYewuguid());
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.set("operateusername", operateUserName);
            auditproject.set("operatedate", new Date());
            auditproject.set("rowguid", rowGuid);
            auditproject.set("windowguid", windowGuid);
            auditproject.set("windowname", windowName);
            AuditTask auditTask = iaudittask.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                // auditproject.set("centerguid",centerGuid);
                auditproject.set("task_id", auditTask.get("task_id"));
                auditproject.set("taskguid", auditTask.get("rowguid"));
                auditproject.set("status", ZwfwConstant.BANJIAN_STATUS_YJJ);// 办件状态：初始化
                auditproject.set("ouguid", auditTask.get("ouguid"));
                auditproject.set("projectname", auditTask.get("taskname"));
                auditproject.set("is_charge", auditTask.get("charge_flag"));
                auditproject.set("areacode", auditTask.get("areacode"));
                AuditTaskExtension auditTaskExtension = iaudittaskextension.getTaskExtensionByTaskGuid(taskGuid, false)
                        .getResult();
                if (auditTaskExtension != null) {
                    auditproject.set("charge_when", auditTaskExtension.get("charge_when"));
                }
                auditproject.set("tasktype", auditTask.get("type"));
                auditproject.set("promise_day", auditTask.get("promise_day"));
            }

            auditproject.set("applyertype", Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));// 申请人类型：个人
            auditproject.set("applydate", new Date());// 办件申请时间
            auditproject.set("applyway", Integer.parseInt(applyway));// 办件申请方式：窗口申请
            auditproject.set("certtype", ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
            auditproject.set("biguid", biGuid);
            auditproject.set("subappguid", subappGuid);
            auditproject.set("receivedate", new Date());
            auditproject.set("receiveuserguid", operateUserGuid);
            auditproject.set("receiveusername", operateUserName);
            auditproject.set("is_test", Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.set("is_delay", 20);// 是否延期

            auditproject.set("applyername", dataBean.getFathername());
            auditproject.set("applyeruserguid", dataBean.getRowguid());
            /*
             * // 这边通过外网的办件表的applyguid 来存放，这样在套餐服务的时候就能获取到
             * if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(applyway) ||
             * ZwfwConstant.APPLY_WAY_NETZJSB.equals(applyway)) {
             * auditproject.setApplyeruserguid(dataBean.getApplyerguid());
             * }
             * else {
             * auditproject.setApplyeruserguid(dataBean.getRowguid());
             * }
             */

            auditproject.set("certnum", dataBean.getFatheridnumber());// 组织机构代码证
            auditproject.set("address", dataBean.getFathernowaddress());
            auditproject.set("legal", "");
            auditproject.set("contactperson", dataBean.getFathername());
            auditproject.set("contactcertnum", dataBean.getFatheridnumber());
            auditproject.set("contactphone", dataBean.getReceivephonenumber());
            auditproject.set("contactpostcode", "");
            auditproject.set("contactemail", "");
            /*
             * String numberFlag =
             * handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE,
             * centerGuid).getResult();
             * if (StringUtil.isBlank(numberFlag)) {
             * numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
             * }
             * auditproject.set("flowsn",flowSn.getFlowsn("办件编号",
             * numberFlag).getResult());
             */
            HandleProjectService projectService = new HandleProjectService();
            // 3、产生办件流水号
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            String numberFlag = handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE, auditproject.getCenterguid())
                    .getResult();
            if (StringUtil.isBlank(numberFlag)) {
                numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
            }
//            String flowsn = flowSn.getFlowsn("办件编号", numberFlag).getResult();
            String flowsn =FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());
            String useJSInternalNO = handleConfig.getFrameConfig("AS_USEJSINTERNALNO", auditproject.getCenterguid())
                    .getResult();
            auditproject.setFlowsn(flowsn);// 设置办件流水号
            auditproject.set("businessguid", businessGuid);
            if (auditTask != null && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTask.get("is_enable").toString())) {
                iauditproject.addProject(auditproject);
            }
            result.setResult(rowGuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String taskGuid,
                                                                                          String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
                                                                                          String projectguid, String taskCaseguid) {
        AuditCommonResult<List<AuditProjectMaterial>> result = new AuditCommonResult<List<AuditProjectMaterial>>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditTaskMaterialCase auditTaskMaterialCaseService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterialCase.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskGuid, true)
                    .getResult();
            IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
            if (auditTask == null) {
                auditTask = new AuditTask();
            }
            if (auditTaskExtension == null) {
                auditTaskExtension = new AuditTaskExtension();
            }
            // 创建办件
            AuditProject auditproject = new AuditProject();
            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
            auditproject.setTaskguid(taskGuid);
            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);// 办件状态：外网初始化
            auditproject.setPromise_day(auditTask.getPromise_day());
            auditproject.setApplydate(new Date());
            auditproject.setIs_delay(20);
            auditproject.setProjectname(auditTask.getTaskname());
            auditproject.setInsertdate(new Date());
            auditproject.setIs_charge(auditTask.getCharge_flag());
            auditproject.setCharge_when(auditTaskExtension.getCharge_when());// 收费的时间，受理前还是办结前
            auditproject.setTasktype(auditTask.getType());
            auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS));
            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
            auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 身份证
            auditproject.setApplyertype(20);
            auditproject.setApplyeruserguid(applyerGuid);
            auditproject.setApplyername(applyerUserName);
            auditproject.setCertnum(certNum);
            auditproject.setTaskid(auditTask.getTask_id());
            auditproject.setTask_id(auditTask.getTask_id());
            auditproject.setIs_test(ZwfwConstant.CONSTANT_INT_ZERO);
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
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
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
            auditOnlineProject.setIs_fee(0); // 办件未收讫
            auditOnlineProject.setIs_evaluat(0);// 办件未评价
            auditOnlineProject.setType("0");// 0，套餐式：1。默认为0
            auditOnlineProject.setStatus(String.valueOf(auditproject.getStatus()));
            IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);
            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertConfigExternal.class);
            ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertInfoExternal.class);
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            // 初始化材料（如果存在多情形，需要考虑多情形功能）
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            // 返回的材料
            List<AuditProjectMaterial> returnmaterials = new ArrayList<AuditProjectMaterial>();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                // 当前该条材料是否返回页面上标志
                Boolean flag = false;
                // 先判断材料是否在多情形列表中
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    if (caseMap.containsKey(auditTaskMaterial.getRowguid())) {
                        flag = true;
                    }
                }
                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT);
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterial.setOperatedate(new Date());
                // 事项材料存在共享材料
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    List<CertInfo> certInfos = iCertInfoExternal.selectCertByOwner(
                            auditTaskMaterial.getSharematerialguid(), ZwdtConstant.CERTOWNERTYPE_PERSON, "", certNum,
                            false, auditproject.getAreacode(), null);
                    if (certInfos != null && certInfos.size() > 0) {
                        CertInfo certInfo = certInfos.get(0);
                        if (certInfo != null) {
                            int count = attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid());
                            if (count > 0) {
                                // 设置关联
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                // 复制证照实例对应的附件
                                attachService.copyAttachByClientGuid(certInfo.getCertcliengguid(), "从共享材料库引用", null,
                                        auditProjectMaterial.getCliengguid());
                                // 更新流程材料状态
                                auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                                // 更新流程实例材料字段
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                            } else {
                                // 不存在附件
                                // 如果材料原先有对应的附件,把原先的附件删除
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                // 更新流程材料状态
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                            }
                        }
                    } else {
                        // 不存在共享材料
                        // this.cleanGuanLian(auditProjectMaterial);
                    }
                }
                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                // 返回的材料
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    // 有情形情况
                    if (flag) {
                        returnmaterials.add(auditProjectMaterial);
                    }
                } else {
                    returnmaterials.add(auditProjectMaterial);
                }
            }
            result.setResult(returnmaterials);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String taskGuid,
                                                                                          String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
                                                                                          String projectguid, String taskCaseguid, String applyerType) {
        AuditCommonResult<List<AuditProjectMaterial>> result = new AuditCommonResult<List<AuditProjectMaterial>>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditTaskMaterialCase auditTaskMaterialCaseService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterialCase.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskGuid, true)
                    .getResult();
            IAuditOrgaServiceCenter auditOrgaServiceCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaServiceCenter.class);
            IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
            if (auditTask == null) {
                auditTask = new AuditTask();
            }
            if (auditTaskExtension == null) {
                auditTaskExtension = new AuditTaskExtension();
            }
            // 创建办件
            AuditProject auditproject = new AuditProject();
            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
            auditproject.setOnlineapplyerguid(applyerGuid);
            auditproject.setTaskguid(taskGuid);
            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);// 办件状态：外网初始化
            auditproject.setPromise_day(auditTask.getPromise_day());
            auditproject.setApplydate(new Date());
            auditproject.setIs_delay(20);
            auditproject.setProjectname(auditTask.getTaskname());
            auditproject.setInsertdate(new Date());
            auditproject.setIs_charge(auditTask.getCharge_flag());
            auditproject.setCharge_when(auditTaskExtension.getCharge_when());// 收费的时间，受理前还是办结前
            auditproject.setTasktype(auditTask.getType());
            // auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS));
            Integer applyWay = auditTaskExtension.getWebapplytype();
            if (Integer.parseInt(ZwdtConstant.WEBAPPLYWAY_ZJBL) == applyWay) {
                auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB));
            } else if (Integer.parseInt(ZwdtConstant.WEBAPPLYWAY_SBYS) == applyWay) {
                auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS));
            }
            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
            if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 个人默认为身份证
            } else {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM);// 企业默认为统一社会信用代码
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
            // 如果是乡镇中心办理，则需要设置办件表内的区域代码
            String xzAreaCode = "";
            if (StringUtil.isNotBlank(centerGuid)) {
                AuditOrgaServiceCenter auditOrgaServiceCenter = auditOrgaServiceCenterService
                        .findAuditServiceCenterByGuid(centerGuid).getResult();
                if (auditOrgaServiceCenter != null) {
                    String centerAraecode = auditOrgaServiceCenter.getBelongxiaqu();
                    AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(centerAraecode).getResult();
                    if (ZwfwConstant.AREA_TYPE_XZJ.equals(auditOrgaArea.getCitylevel())
                            || ZwfwConstant.AREA_TYPE_CJ.equals(auditOrgaArea.getCitylevel())) {
                        xzAreaCode = auditOrgaArea.getXiaqucode();
                    }
                }
            }
            if (StringUtil.isNotBlank(xzAreaCode)) {
                auditproject.setCurrentareacode(xzAreaCode);
                auditproject.setAcceptareacode(xzAreaCode);
            }
            auditproject.setIs_test(ZwfwConstant.CONSTANT_INT_ZERO);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
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
            auditOnlineProject.setIs_fee(0); // 办件未收讫
            auditOnlineProject.setIs_evaluat(0);// 办件未评价
            auditOnlineProject.setType("0");// 0，普通办件：1。默认为0
            auditOnlineProject.setApplyertype(applyerType);
            auditOnlineProject.setStatus(String.valueOf(auditproject.getStatus()));
            auditOnlineProject.setTaskcaseguid(taskCaseguid);
            auditOnlineProject.setTasktype(String.valueOf(auditTask.getType()));
            if (StringUtil.isNotBlank(xzAreaCode)) {
                auditOnlineProject.setCurrentareacode(xzAreaCode);
            }
            IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);

            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertConfigExternal.class);
            ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertInfoExternal.class);
            ICertAttachExternal iCertAttachExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertAttachExternal.class);
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            // 初始化材料（如果存在多情形，需要考虑多情形功能）
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            // 返回的材料
            List<AuditProjectMaterial> returnmaterials = new ArrayList<AuditProjectMaterial>();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
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
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT);
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterial.setOperatedate(new Date());
                // 事项材料存在共享材料
                String type = "";
                String materialLevel = "";
                String materialType = "";
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    // 证件所有人类别转换
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        type = ZwdtConstant.CERTOWNERTYPE_PERSON;
                    } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        type = ZwdtConstant.CERTOWNERTYPE_COMPANY;
                    }
                    List<CertInfo> certInfos = iCertInfoExternal.selectCertByOwner(
                            auditTaskMaterial.getSharematerialguid(), type, "", certNum, false,
                            auditproject.getAreacode(), null);
                    if (certInfos != null && certInfos.size() > 0) {
                        CertInfo certInfo = certInfos.get(0);
                        if (certInfo != null) {
                            // 如果材料是证照，则返回证照级别
                            materialType = certInfo.getMaterialtype();
                            if (ZwdtConstant.MATERIALTYPE_CERT.equals(materialType)) {
                                materialLevel = certInfo.getCertlevel();
                            }
                            List<JSONObject> attachList = iCertAttachExternal
                                    .getAttachList(certInfo.getCertcliengguid(), auditproject.getAreacode());
                            if (attachList != null && attachList.size() > 0) {
                                // 设置关联
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                if (attachList != null && attachList.size() > 0) {
                                    for (JSONObject json : attachList) {
                                        // 独立证照库情况下，将附件存储到本地附件库中。本地模式下，实现附件复制
                                        AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                                auditProjectMaterial.getCliengguid(), json.getString("attachname"),
                                                json.getString("contentype"), json.getString("cliengtag"),
                                                json.getLong("size"),
                                                iCertAttachExternal.getAttach(json.getString("attachguid"),
                                                        auditproject.getAreacode()),
                                                null, null);
                                    }
                                }
                                // 更新流程材料状态
                                auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                                // 更新流程实例材料字段
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            } else {
                                // 不存在附件
                                // 如果材料原先有对应的附件,把原先的附件删除
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                // 更新流程材料状态
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            }
                        } else {
                            // 不存在共享材料
                            // this.cleanGuanLian(auditProjectMaterial);
                        }
                    }
                }
                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                auditProjectMaterial.set("materialLevel", materialLevel);
                auditProjectMaterial.set("materialType", materialType);
                // 返回的材料
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    // 有情形情况
                    if (flag) {
                        returnmaterials.add(auditProjectMaterial);
                    }
                } else {
                    returnmaterials.add(auditProjectMaterial);
                }
            }
            result.setResult(returnmaterials);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditProjectMaterial>> InitOnlineCompanyProjectReturnMaterials(String taskGuid,
                                                                                                 String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
                                                                                                 String projectguid, String taskCaseguid, String applyerType, String declarerName, String declarerGuid) {
        AuditCommonResult<List<AuditProjectMaterial>> result = new AuditCommonResult<List<AuditProjectMaterial>>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IAuditTaskMaterialCase auditTaskMaterialCaseService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterialCase.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskGuid, true)
                    .getResult();
            IAuditOrgaServiceCenter auditOrgaServiceCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaServiceCenter.class);
            IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
            if (auditTask == null) {
                auditTask = new AuditTask();
            }
            if (auditTaskExtension == null) {
                auditTaskExtension = new AuditTaskExtension();
            }
            // 创建办件
            AuditProject auditproject = new AuditProject();
            auditproject.setRowguid(projectguid);
            auditproject.setOperatedate(new Date());
            auditproject.setOnlineapplyerguid(applyerGuid);
            auditproject.setTaskguid(taskGuid);
            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);// 办件状态：外网初始化
            auditproject.setPromise_day(auditTask.getPromise_day());
            auditproject.setApplydate(new Date());
            auditproject.setIs_delay(20);
            auditproject.setProjectname(auditTask.getTaskname());
            auditproject.setInsertdate(new Date());
            auditproject.setIs_charge(auditTask.getCharge_flag());
            auditproject.setCharge_when(auditTaskExtension.getCharge_when());// 收费的时间，受理前还是办结前
            auditproject.setTasktype(auditTask.getType());
            Integer applyWay = auditTaskExtension.getWebapplytype();
            if (Integer.parseInt(ZwdtConstant.WEBAPPLYWAY_ZJBL) == applyWay) {
                auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB));
            } else if (Integer.parseInt(ZwdtConstant.WEBAPPLYWAY_SBYS) == applyWay) {
                auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS));
            }
            auditproject.setOuguid(auditTask.getOuguid());
            auditproject.setOuname(auditTask.getOuname());
            auditproject.setAreacode(areaCode);
            auditproject.setCenterguid(centerGuid);
            if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 个人默认为身份证
            } else {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM);// 企业默认为统一社会信用代码
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
            // 如果是乡镇中心办理，则需要设置办件表内的区域代码
            String xzAreaCode = "";
            if (StringUtil.isNotBlank(centerGuid)) {
                AuditOrgaServiceCenter auditOrgaServiceCenter = auditOrgaServiceCenterService
                        .findAuditServiceCenterByGuid(centerGuid).getResult();
                if (auditOrgaServiceCenter != null) {
                    String centerAraecode = auditOrgaServiceCenter.getBelongxiaqu();
                    AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(centerAraecode).getResult();
                    if (ZwfwConstant.AREA_TYPE_XZJ.equals(auditOrgaArea.getCitylevel())
                            || ZwfwConstant.AREA_TYPE_CJ.equals(auditOrgaArea.getCitylevel())) {
                        xzAreaCode = auditOrgaArea.getXiaqucode();
                    }
                }
            }
            if (StringUtil.isNotBlank(xzAreaCode)) {
                auditproject.setCurrentareacode(xzAreaCode);
                auditproject.setAcceptareacode(xzAreaCode);
            }
            auditproject.setIs_test(ZwfwConstant.CONSTANT_INT_ZERO);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);
            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertConfigExternal.class);
            ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertInfoExternal.class);
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            // 初始化材料（如果存在多情形，需要考虑多情形功能）
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("taskguid", auditTask.getRowguid());
            conditionsql.setOrder("ORDERNUM", "desc");
            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectMaterialListByCondition(conditionsql.getMap()).getResult();
            // 返回的材料
            List<AuditProjectMaterial> returnmaterials = new ArrayList<AuditProjectMaterial>();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
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
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT);
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                auditProjectMaterial.setOperatedate(new Date());
                // 事项材料存在共享材料
                String type = "";
                String materialLevel = "";
                if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                    // 证件所有人类别转换
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        type = ZwdtConstant.CERTOWNERTYPE_PERSON;
                    } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        type = ZwdtConstant.CERTOWNERTYPE_COMPANY;
                    }
                    List<CertInfo> certInfos = iCertInfoExternal.selectCertByOwner(
                            auditTaskMaterial.getSharematerialguid(), type, "", certNum, false,
                            auditproject.getAreacode(), null);
                    if (certInfos != null && certInfos.size() > 0) {
                        CertInfo certInfo = certInfos.get(0);
                        if (certInfo != null) {
                            // 如果材料是证照，则返回证照级别
                            String materialType = certInfo.getMaterialtype();
                            if (ZwdtConstant.MATERIALTYPE_CERT.equals(materialType)) {
                                materialLevel = certInfo.getCertlevel();
                            }
                            int count = attachService.getAttachCountByClientGuid(certInfo.getCertcliengguid());
                            if (count > 0) {
                                // 设置关联
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                // 复制证照实例对应的附件
                                attachService.copyAttachByClientGuid(certInfo.getCertcliengguid(), "从共享材料库引用", null,
                                        auditProjectMaterial.getCliengguid());
                                // 更新流程材料状态
                                auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                                // 更新流程实例材料字段
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            } else {
                                // 不存在附件
                                // 如果材料原先有对应的附件,把原先的附件删除
                                attachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                // 更新流程材料状态
                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                            }
                        } else {
                            // 不存在共享材料
                            // this.cleanGuanLian(auditProjectMaterial);
                        }
                    }
                }
                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                auditProjectMaterial.set("materialLevel", materialLevel);
                // 返回的材料
                if (StringUtil.isNotBlank(taskCaseguid)) {
                    // 有情形情况
                    if (flag) {
                        returnmaterials.add(auditProjectMaterial);
                    }
                } else {
                    returnmaterials.add(auditProjectMaterial);
                }
            }
            result.setResult(returnmaterials);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> InitOnlineProjectForBusiness(String businessguid, String applyerGuid,
                                                                String areaCode, String applyerUserName, String biguid, String subappGuid, String yewuguid) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            IAuditSpBusiness iAuditSpBusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
            AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();

            // IAuditSpITask iAuditSpITask =
            // ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);

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
            auditOnlineProject.setIs_fee(0); // 办件未收讫
            auditOnlineProject.setIs_evaluat(0);// 办件未评价
            auditOnlineProject.setType("1");// 套餐式：1
            auditOnlineProject.setStatus(String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));
            IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> saveOperateLog(AuditProject auditProject, String OpeateUserGuid,
                                                  String OpeateUserName, String OperateType, String remark) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, OperateType, OpeateUserGuid, OpeateUserName, "", remark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> handleLogistics(AuditProject auditProject,
                                                   AuditLogisticsBasicinfo auditLogisticsBasicinfo) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            IAuditLogisticsBasicInfo auditLogisticsBasicInfoService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditLogisticsBasicInfo.class);
            auditLogisticsBasicinfo.setRowguid(UUID.randomUUID().toString());
            auditLogisticsBasicinfo.setProjectguid(auditProject.getRowguid());
            auditLogisticsBasicinfo.setChk_code(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));
            auditLogisticsBasicInfoService.addLogisticsBasicinfo(auditLogisticsBasicinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> updateLogistics(AuditProject auditProject,
                                                   AuditLogisticsBasicinfo auditLogisticsBasicinfo) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        try {
            IAuditLogisticsBasicInfo auditLogisticsBasicInfoService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditLogisticsBasicInfo.class);
            auditLogisticsBasicInfoService.updateLogisticsBasicinfo(auditLogisticsBasicinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> InitConsultProject(String taskGuid, String projectGuid, String operateUserName,
                                                        String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum, String qno,
                                                        String acceptareacode, String cityLevel, String delegatetype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IAuditOnlineConsult iAuditOnlineConsult = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineConsult.class);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                AuditOnlineConsult auditOnlineConsult = new AuditOnlineConsult();
                auditOnlineConsult.setRowguid(UUID.randomUUID().toString());
                auditOnlineConsult.setTaskguid(taskGuid);
                auditOnlineConsult.put("taskname", auditTask.getTaskname());
                auditOnlineConsult.put("taskid", auditTask.getTask_id());
                auditOnlineConsult.put("windowguid", windowGuid);
                auditOnlineConsult.put("windowname", windowName);
                auditOnlineConsult.setPublishonweb(ZwfwConstant.CONSTANT_STR_ZERO);// 是否对外发布
                auditOnlineConsult.setIsAnonymous(ZwfwConstant.CONSTANT_STR_ZERO);// 是否匿名
                auditOnlineConsult.setCenterguid(centerGuid);// 中心标识
                auditOnlineConsult.setAreaCode(auditTask.getAreacode());// 区域标识
                auditOnlineConsult.setAskdate(new Date());// 提问时间
                auditOnlineConsult.setConsulttype(ZwfwConstant.CONSTANT_STR_ONE);// 咨询投诉类型
                auditOnlineConsult.setReadstatus(ZwdtConstant.CONSULT_READSTATUS_NO);// 咨询记录阅读状态：未读
                auditOnlineConsult.setStatus(ZwfwConstant.ZIXUN_TYPE_DDF);// 状态：待答复
                iAuditOnlineConsult.addConsult(auditOnlineConsult);
                result.setResult(auditOnlineConsult.getRowguid());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> InitProject(String taskGuid, String projectGuid, String operateUserName,
                                                 String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum, String qno,
                                                 String acceptareacode, String cityLevel, String delegatetype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_INIT);// 办件状态：初始化
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setOuname(auditTask.getOuname());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setIf_express(ZwfwConstant.CONSTANT_STR_ZERO);
                auditproject.setCenterguid(centerGuid);
                auditproject.setAreacode(auditTask.getAreacode());
                IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                    auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());// 进驻大厅
                    auditproject.setIf_express(auditTaskExtension.getIf_express());
                }
                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
                // 企业类型
                if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditTask.getApplyertype())) {
                    auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));
                    auditproject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);// 申请人证照类型：组织机构代码
                } else {
                    // 个人类型
                    auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));
                    auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
                }
            }
            auditproject.setApplydate(new Date());// 办件申请时间
            auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 办件申请方式：窗口申请
            // auditproject.setIssendsms(StringUtil.isBlank(asissendmessage)
            // ? ZwfwConstant.CONSTANT_STR_ZERO
            // : ZwfwConstant.CONSTANT_STR_ONE);// 是否发送短信
            auditproject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.setIs_delay(20);// 是否延期

            // TODO这个地方需要调整

            // 排队叫号系统传递过来的参数处理
            if (StringUtil.isNotBlank(qno)) {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                auditproject.setCertnum(certNum);
                IAuditQueueUserinfo userinfoservice = ContainerFactory.getContainInfo()
                        .getComponent(IAuditQueueUserinfo.class);
                Record userinfo = userinfoservice.getUserByIdentityCardNum(certNum).getResult();
                if (userinfo != null) {
                    auditproject.setApplyername(userinfo.get("DisplayName"));
                    auditproject.setAddress(userinfo.get("address"));
                    auditproject.setContactmobile(userinfo.get("Mobile"));
                }
                // 更新排队叫号与办件关联关系
                IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
                queueservice.updateQNOProject(rowGuid, qno, centerGuid);
            }

            // 如果是镇村接件或是部门进驻事项，ACCEPTAREACODE字段设置为当前登记办件窗口所在的辖区编码
            if (Integer.parseInt(cityLevel) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)
                    || ZwfwConstant.TASKDELEGATE_TYPE_ZCBS.equals(delegatetype)) {
                auditproject.setAcceptareacode(acceptareacode);
            }
            auditproject.setCurrentareacode(acceptareacode);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);

            // 直接赋值给相关数据ProjectGuid
            // wfcontextvalueservice.createOrUpdateContext(processVersionInstanceGuid,
            // "ProjectGuid",
            // auditproject.getRowguid(),
            // WorkflowKeyNames9.ParameterType_T_string, 2);

            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);

            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(auditTask == null ? "" : auditTask.getRowguid(), true)
                    .getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                auditProjectMaterial.clear();
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setStatus(10);
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(UUID.randomUUID().toString());
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());

                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
            }
            result.setResult(rowGuid);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> addCompanyOnlineProject(AuditProject auditProject, String declarerguid,
                                                           String declarername, String applyerGuid, String applyerName, String companyrowguid, String sqGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
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
            auditOnlineProject.setIs_fee(0); // 办件未收讫
            auditOnlineProject.setIs_evaluat(0);// 办件未评价
            auditOnlineProject.setType("0");// 0，普通办件：1。默认为0
            auditOnlineProject.setDeclarerguid(declarerguid);
            auditOnlineProject.setDeclarername(declarername);
            auditOnlineProject.setApplyertype(auditProject.getApplyertype().toString());
            auditOnlineProject.setStatus(String.valueOf(auditProject.getStatus()));
            auditOnlineProject.setTaskcaseguid(auditProject.getTaskcaseguid());
            auditOnlineProject.setTasktype(String.valueOf(auditProject.getTasktype()));
            auditOnlineProject.setStatus(String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));
            auditOnlineProject.setCompanyrowguid(companyrowguid);
            auditOnlineProject.setSqGuid(sqGuid);
            // 设置外网乡镇延伸区域编码
            if (StringUtil.isNotBlank(auditProject.getCurrentareacode())) {
                auditOnlineProject.setCurrentareacode(auditProject.getCurrentareacode());
            }
            IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            iAuditOnlineProject.addProject(auditOnlineProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> handleAcceptArea(AuditProject auditProject, String workItemGuid,
                                                      String displayName, String userGuid, String windowName, String windowGuid, String areaCode) {
        // 默认不直接批准
        return this.handleAcceptArea(auditProject, workItemGuid, displayName, userGuid, windowName, windowGuid,
                areaCode, ZwfwConstant.CONSTANT_STR_ZERO);
    }

    @Override
    public AuditCommonResult<String> handleAcceptArea(AuditProject auditProject, String workItemGuid,
                                                      String operateUserName, String operateUserGuid, String windowName, String windowGuid, String areaCode,
                                                      String pizhun) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService
                    .getAuditTaskByGuid(auditProject.getTaskguid(), auditProject.getAcceptareacode(), true).getResult();
            IAuditApplyJsLog iAuditApplyJsLog = ContainerFactory.getContainInfo().getComponent(IAuditApplyJsLog.class);
            IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskExtension.class);
            IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

            // 1、添加受理操作记录
            // 默认操作类型：窗口受理
            String operatetype = ZwfwConstant.SHENPIOPERATE_TYPE_CKSL;
            if (ZwfwConstant.APPLY_WAY_NETZJSB.equals(String.valueOf(auditProject.getApplyway()))) {
                // 网上直接申报：网上受理
                operatetype = ZwfwConstant.SHENPIOPERATE_TYPE_NETSL;
            }

            // TODO mongodb
            HandleProjectService projectService = new HandleProjectService();
            projectService.HandProjectLog(auditProject, ZwfwConstant.OPERATE_SL, operateUserGuid, operateUserName,
                    workItemGuid);

            // TODO 2、绿色通道办件处理
            // 3、更新办件受理状态
            // 即办件更新为已受理待办结状态
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditProject.getTasktype()))
                    && !ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())) {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSLDBJ);
            } else {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSL);
            }
            // 保存处理过办件的区域编码(不重复保存)
            String areaStr = "";
            if (StringUtil.isBlank(auditProject.getHandleareacode())) {
                areaStr = areaCode + ",";
            } else if ((auditProject.getHandleareacode().indexOf(areaCode + ",") < 0)) {
                areaStr = auditProject.getHandleareacode() + areaCode + ",";
            }
            if (StringUtil.isNotBlank(areaStr)) {
                auditProject.setHandleareacode(areaStr);
            }
            auditProject.setCurrentareacode(areaCode);
            auditProject.setAcceptuserdate(new Date());
            auditProject.setAcceptuserguid(operateUserGuid);
            auditProject.setAcceptusername(operateUserName);
            auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);
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
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                Map<String, String> updateFieldMap = new HashMap<>(16);
                Map<String, String> updateDateFieldMap = new HashMap<>(16);// 更新日期类型的字段
                // 受理后要添加窗口部门
                updateFieldMap.put("ouguid=", auditProject.getOuguid());
                updateFieldMap.put("ouname=", auditProject.getOuname());
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                sql.eq("sourceguid", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // sql.eq("applyerguid", auditProject.getOnlineapplyerguid());
                // } // if (applyerguid != null) {
                // sql.eq("applyerguid", applyerguid);
                // }
                // else {
                // sql.eq("applyerguid", auditProject.getApplyeruserguid());
                // }
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }
            Date shouldEndDate = null;// 承诺办结日期
            int ts;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                IAuditOrgaWorkingDay workingDayService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOrgaWorkingDay.class);
                shouldEndDate = workingDayService.getWorkingDayWithOfficeSet(auditProject.getCenterguid(), new Date(),
                        auditTask.getPromise_day()).getResult();
                ts = auditTask.getPromise_day() * 24 * 60;
            } else {
                shouldEndDate = null;
                ts = 0;
            }
            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}

            // 4、添加办件剩余时间
            IAuditProjectSparetime sparetimeService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectSparetime.class);
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())) {
                sparetimeService.addSpareTimeByProjectGuid(auditProject.getRowguid(), ts, auditProject.getAreacode(),
                        auditProject.getCenterguid());
                // 如果是并联审批办件，判断并联审批是否需要计时
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    handleBlspSparetime(auditProject.getSubappguid());
                }
            }

            // 5、如果合并办理填写了数量，需要插入批量办件数字
            IAuditProjectNumber projectNumberService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectNumber.class);
            projectNumberService.addProjectNumber(auditProject, true, operateUserGuid, operateUserName);
            // TODO 6、如果是自建系统，那么生成对接数据
            // TODO 7、如果是并联审批办件，发消息提醒给其他引用本办件审批结果审批事项的窗口人员已受理
            // 8、非即办件走模拟流程
            // 如果事项配置了需要走模拟办件，如是收费事项，需办件流转到审核通过，其他情况根据系统参数配置流转到的步骤。
            if (!ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditProject.getTasktype()))
                    && ZwfwConstant.CONSTANT_INT_ONE == (auditTaskExtension.getIs_simulation())) {
                String strflowstatus = "";
                IAuditTaskResult auditResultService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskResult.class);
                AuditTaskResult auditresult = auditResultService
                        .getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if ((ZwfwConstant.CONSTANT_INT_ONE == auditTask.getCharge_flag()
                        && ZwfwConstant.CHARGE_WHEN_BJQ.equals(String.valueOf(auditTaskExtension.getCharge_when())))
                        || (auditresult != null
                        && (auditresult.getResulttype() != ZwfwConstant.LHSP_RESULTTYPE_NULL))) {
                    // 收费事项、打证办件状态设置为 审核通过
                    strflowstatus = String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPTG);
                } else {
                    // 非收费事项办件状态设置为正常办结
                    IHandleConfig handleConfigServic = ContainerFactory.getContainInfo()
                            .getComponent(IHandleConfig.class);
                    strflowstatus = handleConfigServic
                            .getFrameConfig("AS_SIMULATION_TO_STATUS", auditProject.getCenterguid()).getResult();
                    strflowstatus = StringUtil.isBlank(strflowstatus) ? String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ)
                            : strflowstatus;
                }
                auditProject.setStatus(Integer.parseInt(strflowstatus));
                // TODO 自动流转流程暂缓

            }
            // 如果是直接批准的，那么就更新办件状态数据,同时直接将办件时间计时暂停
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(pizhun)) {
                if (!ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                    // 办件暂停计时
                    // auditProject.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
                    // // 更新时间表状态 恢复计时0 暂停计时1
                    // AuditProjectSparetime auditProjectSparetime =
                    // sparetimeService
                    // .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                    // auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                    // sparetimeService.updateSpareTime(auditProjectSparetime);

                }
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPTG);
                if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))
                        || ZwfwConstant.ITEMTYPE_CNJ.equals(String.valueOf(auditTask.getType()))) {
                    auditProject.setBanjieresult(ZwfwConstant.BANJIE_TYPE_ZYXK);
                    auditProject.setBanwandate(new Date());
                }
            }

            // 保存办件信息
            saveProject(auditProject);
            String isjS = ConfigUtil.getConfigValue("epointframe", "isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isjS)) {
                // 如果是通用版，则需要调用新网接口，更改办件状态
                String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                // 只有在配置的情况下才能进行请求
                if (StringUtil.isNotBlank(url)) {
                    url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfodetail?declareid="
                            + auditTask.getTask_id() + "&centerguid=" + auditProject.getCenterguid() + "&taskguid="
                            + auditTask.getRowguid() + "&flowsn=" + auditProject.getFlowsn();
                    Record record = StarProjectInteractUtil.updateOrInsertStar(auditProject.getFlowsn(),
                            ZwdtConstant.STAR_PROJECT_SLZ,
                            StringUtil.isBlank(auditTask.getDept_yw_reg_no()) ? auditTask.getItem_id()
                                    : auditTask.getDept_yw_reg_no(),
                            auditTask.getTaskname(), auditProject.getApplyername(), auditProject.getCertnum(), "查看办件",
                            url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                }
            }
            // 更新材料审核状态
            IAuditProjectMaterial projectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            projectMaterialService.updateAllAuditStatus(auditProject.getRowguid());
            // 处理共享材料
            String ownGuid = "";
            IAuditIndividual auditIndividualService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditIndividual.class);
            IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyBaseinfo.class);
            // 企业类型申请人
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfoService
                        .getCompanyByOneField("rowguid", auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsCompanyBaseinfo.getCompanyid();
            }
            // 个人
            else if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                AuditRsIndividualBaseinfo auditRsIndividualBaseinfo = auditIndividualService
                        .getAuditRsIndividualBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                ownGuid = auditRsIndividualBaseinfo.getPersonid();
            }
            if (StringUtil.isBlank(ownGuid)) {
                ownGuid = auditProject.getApplyeruserguid();
            }
            FrameOuExtendInfo ou = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
            IHandleRSMaterial handleRSMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IHandleRSMaterial.class);
            handleRSMaterialService.insertShareMaterial(auditProject.getTaskguid(), auditProject.getRowguid(), ownGuid,
                    operateUserGuid, operateUserName, auditProject.getApplyername(), auditProject.getCertnum(),

                    auditProject.getApplyertype(), auditProject.getCerttype(), auditProject.getAreacode(),
                    ou.get("orgcode"));

            IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                    .getComponent(IMessagesCenterService.class);
            if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                String targetUser = "";
                String targetDispName = "";
                // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                    targetUser = auditProject.getAcceptuserguid();
                    targetDispName = auditProject.getAcceptusername();
                } else {
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
                // messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(),
                // content, new Date(), 0, new Date(),
                // auditProject.getContactmobile(), targetUser, targetDispName,
                // operateUserGuid, operateUserName,
                // "", "", "", false, "短信");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void handleblspsub(AuditProject auditProject, String userguid, String displayname) {
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditProjectSparetime iauditprojectsparetime = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditSpITask iauditspitask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IOnlineMessageInfoService iOnlineMessageInfoService = ContainerFactory.getContainInfo()
                .getComponent(IOnlineMessageInfoService.class);
        AuditSpBusiness business = iauditspbusiness.getAuditSpBusinessByRowguid(auditProject.getBusinessguid())
                .getResult();
        AuditSpISubapp auditspisubapp = iauditspisubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
        if (auditspisubapp == null) {
            return;
        }
        //建设审批主题
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(business.getBusinesstype())) {
            // 如果是里程碑事项，并且状态是 不予受理，异常终止，撤销申请，审核不通过办结 则需要进重新申报这个事项.
            Integer status = auditProject.getStatus();
            if (!(status == ZwfwConstant.BANJIAN_STATUS_ZCBJ && auditProject.getBanjieresult() != null
                    && auditProject.getBanjieresult() == ZwfwConstant.BANJIE_TYPE_ZYXK)) {
                // 判断该事项是否里程碑事项
                AuditSpITask spi = iauditspitask.getAuditSpITaskByProjectGuid(auditProject.getRowguid()).getResult();
                if (spi != null) {
                    // 重新添加一条数据，修改状态
                    iauditspitask.addTaskInstance(spi.getBusinessguid(), spi.getBiguid(), spi.getPhaseguid(),
                            spi.getTaskguid(), spi.getTaskname(), spi.getSubappguid(), 999, spi.getAreacode(),
                            spi.getReviewguid(), spi.getSflcbsx());
                    auditspisubapp.setStatus("23");
                    iauditspisubapp.updateAuditSpISubapp(auditspisubapp);
                    return;

                }
            }

            // 查找当前阶段办理的里程碑事项
            List<AuditSpITask> listtask = iauditspitask.getTaskInstanceBySubappGuid(auditProject.getSubappguid())
                    .getResult();
            List<AuditSpITask> listtasklcb = new ArrayList<>();
            for (AuditSpITask auditSpITask : listtask) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getSflcbsx())) {
                    listtasklcb.add(auditSpITask);
                }
            }
            // 存在里程碑事项
            if (listtasklcb.size() > 0) {
                boolean check = true;
                for (AuditSpITask auditSpITask : listtasklcb) {
                    // 判断是否有里程碑事项没有初始化
                    if (StringUtil.isBlank(auditSpITask.getProjectguid())) {
                        check = false;
                    }
                }
                int minitues=0;
                if (check) {
                    // 判断所有里程碑事项是否都已办结
                    for (AuditSpITask auditSpITask : listtasklcb) {
                        // 判断是否有里程碑事项没有初始化
                        AuditProject auditproject = iauditproject.getAuditProjectByRowGuid("status",
                                auditSpITask.getProjectguid(), auditSpITask.getAreacode()).getResult();
                        if(auditProject!=null){
                            AuditProjectSparetime sparetime = iauditprojectsparetime
                                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                            if(sparetime!=null){
                                if(sparetime.getSpendminutes()!=null && sparetime.getSpendminutes()>minitues){
                                    minitues=sparetime.getSpendminutes();
                                }
                            }
                            if (auditproject.getStatus() < 90) {
                                check = false;
                            }
                        }

                    }
                    if (check) {
                        // 办结取出耗时，更新状态
                        AuditProjectSparetime sparetime = iauditprojectsparetime
                                .getSparetimeByProjectGuid(auditProject.getSubappguid()).getResult();
                        if (sparetime != null) {
                            log.info("sparetime:"+sparetime.getRowguid());
                            auditspisubapp.setSpendminutes(sparetime.getSpendminutes());
                            // 删除实体
                            iauditprojectsparetime.deleteSpareTimeByRowGuid(sparetime.getRowguid());
                        }else{
                            auditspisubapp.setSpendminutes(minitues);
                        }
                        auditspisubapp.setStatus(ZwfwConstant.LHSP_Status_YBJ);
                        auditspisubapp.setFinishdate(new Date());
                        iauditspisubapp.updateAuditSpISubapp(auditspisubapp);
                        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(business.getAreacode()).getResult();
                        AuditSpISubapp subapp = iauditspisubapp.getSubappByGuid(auditProject.getSubappguid())
                                .getResult();
                        AuditRsItemBaseinfo baseinfo = iauditrsitembaseinfo
                                .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                        String roleGuid = roleService.listRole("统一收件人员", "").get(0).getRoleGuid();
                        List<FrameUser> listUser = userService.listUserByOuGuid(area.getOuguid(), roleGuid, "", "",
                                false, true, true, 3);
                        for (FrameUser user : listUser) {
                            iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                                    UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                    user.getUserGuid(), "", user.getUserGuid(),
                                    "您发起的项目申报【" + baseinfo.getItemname() + "】事项已全部办理完毕！", new Date());
                        }
                    }

                }
            } else {
                // 如果没有里程碑事项。判断是否所有事项是否都办结
                boolean flag = true;
                int minitues = 0;
                for (AuditSpITask auditSpITask : listtask) {
                    if (StringUtil.isBlank(auditSpITask.getProjectguid())) {
                        flag = false;
                        break;
                    } else {
                        AuditProject auditproject = iauditproject
                                .getAuditProjectByRowGuid(auditSpITask.getProjectguid(), auditSpITask.getAreacode())
                                .getResult();
                        if(auditProject!=null){
                            AuditProjectSparetime sparetime = iauditprojectsparetime
                                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                            if(sparetime!=null){
                                if(sparetime.getSpendminutes()!=null && sparetime.getSpendminutes()>minitues){
                                    minitues=sparetime.getSpendminutes();
                                }
                            }
                            if (auditproject.getStatus() < 90) {
                                flag = false;
                                break;
                            }
                        }

                    }
                }
                // 所有事项办结
                if (flag) {
                    // 办结取出耗时，更新状态
                    AuditProjectSparetime sparetime = iauditprojectsparetime
                            .getSparetimeByProjectGuid(auditProject.getSubappguid()).getResult();
                    if (sparetime != null) {
                        log.info("sparetime:"+sparetime.getRowguid());
                        auditspisubapp.setSpendminutes(sparetime.getSpendminutes());
                        // 删除实体
                        iauditprojectsparetime.deleteSpareTimeByRowGuid(sparetime.getRowguid());
                    }else{
                        auditspisubapp.setSpendminutes(minitues);
                    }
                    auditspisubapp.setStatus(ZwfwConstant.LHSP_Status_YBJ);
                    auditspisubapp.setFinishdate(new Date());
                    iauditspisubapp.updateAuditSpISubapp(auditspisubapp);
                    AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(business.getAreacode()).getResult();
                    AuditSpISubapp subapp = iauditspisubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
                    AuditRsItemBaseinfo baseinfo = iauditrsitembaseinfo
                            .getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
                    String roleGuid = roleService.listRole("统一收件人员", "").get(0).getRoleGuid();
                    List<FrameUser> listUser = userService.listUserByOuGuid(area.getOuguid(), roleGuid, "", "", false,
                            true, true, 3);
                    for (FrameUser user : listUser) {
                        iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                user.getUserGuid(), "", user.getUserGuid(),
                                "您发起的项目申报【" + baseinfo.getItemname() + "】事项已全部办理完毕！", new Date());
                    }
                }
            }

            // 判断是否需要停止计时
        }
    }

    public void handleBlspSparetime(String subappguid) {
        IAuditSpITask iauditspitask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditProjectSparetime iauditprojectsparetime = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        AuditProjectSparetime auditProjectSparetime = iauditprojectsparetime.getSparetimeByProjectGuid(subappguid)
                .getResult();
        // 如果没有查到计时数据，停止
        if (auditProjectSparetime == null) {
            return;
        }
        List<String> projectguidlist = iauditspitask.getProjectguidsBySubappGuid(subappguid).getResult();
        // 去除null
        projectguidlist = projectguidlist.stream().filter(a -> StringUtil.isNotBlank(a)).collect(Collectors.toList());
        Integer count = iauditprojectsparetime.getSpareTimeCountByProjectguids(projectguidlist).getResult();

        // 存在办件计时
        if (count > 0) {
            if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(auditProjectSparetime.getPause())) {
                auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
                iauditprojectsparetime.updateSpareTime(auditProjectSparetime);
            }
        } else {
            if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditProjectSparetime.getPause())) {
                auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                iauditprojectsparetime.updateSpareTime(auditProjectSparetime);
            }
        }
    }

    /**
     * add by yrchan,2022-04-20,勘验事项的办件办结保存是已经存在的，操作日志保存
     *
     * @param auditProject
     * @param opeateType
     * @param operateUserGuid
     * @param operateUserName
     * @param workItemGuid
     */
    public void ZcBjHandProjectLog(AuditProject auditProject, String opeateType, String operateUserGuid,
                                   String operateUserName, String workItemGuid) {
        IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectOperation.class);
        // 办件操作类型:60:办结
        String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '" + opeateType + "'";
        // 添加办结备注
        AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                .getOperationFileldByProjectGuid(strWhere, " * ", "").getResult();
        // 判断是否存在
        boolean isExist = false;
        if (auditProjectOperation != null) {
            isExist = true;
        } else {
            auditProjectOperation = new AuditProjectOperation();
        }

        // 如果存在启动工作流，则需要把工作流流转意见存入日志表
        if (StringUtil.isNotBlank(workItemGuid)) {
            IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pVersionInstance = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pVersionInstance, workItemGuid);
            if (StringUtil.isNotBlank(workflowWorkItem.getOpinion())) {
                auditProjectOperation.setRemarks(workflowWorkItem.getOpinion());
            }
        }
        // 有些方法没有获取到operateuser,可以手动获取一下
        if (operateUserName.equals("") && !operateUserGuid.equals("")) {
            IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
            operateUserName = userService.getUserNameByUserGuid(operateUserGuid);
        }

        // 判断是否存在
        if (isExist) {
            // 存在
            iAuditProjectOperation.updateAuditProjectOperation(auditProjectOperation);
        } else {
            // 目前采用插入数据库操作
            auditProjectOperation.setRowguid(UUID.randomUUID().toString());
            auditProjectOperation.setAreaCode(auditProject.getAreacode());
            auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
            auditProjectOperation.setApplyerName(auditProject.getApplyername());
            auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
            auditProjectOperation.setPVIGuid(auditProject.getPviguid());
            auditProjectOperation.setOperatedate(new Date());
            auditProjectOperation.setOperateusername(operateUserName);
            auditProjectOperation.setOperateUserGuid(operateUserGuid);
            auditProjectOperation.setOperateType(opeateType);
            auditProjectOperation.setProjectGuid(auditProject.getRowguid());
            iAuditProjectOperation.addProjectOperation(auditProjectOperation);
        }

    }

    @Override
    public void delZwfwMessage(String ouguid, String areacode, String rolename, String identifier) {
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        if (StringUtil.isNotBlank(areacode)) {
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(areacode).getResult();
            if (area != null) {
                ouguid = area.getOuguid();
            }
        }
        String roleGuid = roleService.listRole(rolename, "").get(0).getRoleGuid();
        List<FrameUser> listUser = userService.listUserByOuGuid(ouguid, roleGuid, "", "", false, true, true, 3);
        for (FrameUser frameUser : listUser) {
            messageCenterService.deleteMessageByIdentifier(identifier, frameUser.getUserGuid());
        }
    }

    @Override
    public void delProjectMessage(String taskid, String identifier) {
        IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        IAuditOrgaWindow iauditorgawindow = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
        List<FrameUser> listUser = iauditorgawindow.getFrameUserByTaskID(taskid).getResult();
        for (FrameUser frameUser : listUser) {
            messageCenterService.deleteMessageByIdentifier(identifier, frameUser.getUserGuid());
        }
    }
}

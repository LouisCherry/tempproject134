package com.epoint.auditsp.auditsphandle.action;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspireview.domain.AuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.blsp.material.inter.IAuditBlspMaterial;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.ProjectConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespbusiness.inter.IHandleSpBusiness;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jn.util.JnBeanUtil;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.powersupply.api.IAuditSpProjectinfoGdbzService;
import com.epoint.powersupply.entity.AuditSpProjectinfoGdbz;
import com.epoint.web.blsp.material.bizlogic.AuditBlspMaterialService;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.thirdreporteddata.auditspitaskcorp.api.IAuditSpITaskCorpService;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import com.epoint.zhenggai.api.ZhenggaiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目基本信息表新增页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */

@RestController("jnhandlesubmitmaterialaction")
@Scope("request")
public class JNHandleSubmitMaterialAction extends BaseController
{
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *
     */
    private static final long serialVersionUID = 1313301881620639339L;
    /**
     * 项目库API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private IHandleSpBusiness handleSpBusinessService;

    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditSpISubapp auditSpISubappService;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IAuditTaskCase iAuditTaskCase;
    @Autowired
    private IAuditSpTask iauditsptask;
    /**
     * 单体API
     */
    @Autowired
    private IDantiInfoService iDantiInfoService;

    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;
    /**
     * 参建单位API
     */
    @Autowired
    private IParticipantsInfoService iParticipantsInfo;

    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;
    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;
    // 组合服务
    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditSpPhase auditSpPhaseService;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    private String yewuguid;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditRsCompanyBaseinfo companyBaseinfoService;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IConfigService configservice;

    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private IAuditOrgaServiceCenter iauditorgaservicecenter;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IAuditTaskMaterial iaudittaskmaterial;

    @Autowired
    IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditProjectSparetime sparetimeService;

    @Autowired
    private IAuditSpShareMaterialRelation iauditspsharematerialrelation;

    @Autowired
    private IAuditBlspMaterial iauditblspmaterial;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IHandleFlowSn flowSn;

    @Autowired
    private IHandleConfig handleConfig;

    @Autowired
    private ZhenggaiService zhenggaiImpl;

    @Autowired
    private IAuditSpSpLxydghxkService iauditspsplxydghxkservice;

    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IAuditSpSpJgysService iauditspspjgysservice;

    @Autowired
    private IAuditTaskExtension iaudittaskextension;

    @Autowired
    private IAuditSpProjectinfoGdbzService auditSpProjectinfoGdbzService;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private ICxBusService cxBusService;

    @Autowired
    private IAuditSpITaskCorpService iAuditSpITaskCorpService;

    @Autowired
    private ITaskCommonService taskCommonService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;
    private AuditSpIntegratedCompany integratedCompany = null;
    private DataGridModel<AuditSpITask> modelTask = null;
    private DataGridModel<AuditSpITask> fmodelTask = null;
    private AuditSpIReview dataReview = null;

    /**
     * 项目类型下拉列表model
     */
    private List<SelectItem> itemTypeModel = null;
    /**
     * 建设性质下拉列表model
     */
    private List<SelectItem> constructionPropertyModel = null;
    /**
     * 证照类型下拉列表model
     */
    private List<SelectItem> itemLegalCertTypeModel = null;
    /**
     * 所属行业下拉列表model
     */
    private List<SelectItem> belongtIndustryModel = null;
    /**
     * 法人性质下拉列表model
     */
    private List<SelectItem> legalPropertyModel = null;
    /**
     * 资金来源下拉列表model
     */
    private List<SelectItem> fundSourcesModel = null;
    /**
     * 财政资金来源下拉列表model
     */
    private List<SelectItem> financialResourcesModel = null;
    /**
     * 量化建设规模类别下拉列表model
     */
    private List<SelectItem> quantifyConstructTypeModel = null;
    /**
     * 是否技改项目单选按钮组model
     */
    private List<SelectItem> isImprovementModel = null;

    private List<SelectItem> OuListModel = null;

    private String phaseGuid = "";
    private String businessGuid = "";
    private String businessType = "";
    private String subappGuid = "";
    private String biGuid = "";
    private String initMaterial = ZwfwConstant.CONSTANT_STR_ZERO;
    private String ownerGuid = "";
    /**
     * 图审和联审都用下面的状态标记位。因为图审和联审材料不会出现在通一个阶段，所以可以合用相同的状态位
     * 通过判断材料类型，来确定界面上显示送交图审还是送交联审操作
     */
    public static final String LHSP_Status_TSZ = "21";
    public static final String LHSP_Status_TSWC = "22";
    public static final String LHSP_Status_SJZ = "23";

    AuditSpPhase AuditSpPhase;

    private String status;

    @Override
    public void pageLoad() {
        subappGuid = getRequestParameter("subappGuid");
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
        if (auditSpISubapp != null) {
            status = auditSpISubapp.getStatus();
            addCallbackParam("applyway", auditSpISubapp.getApplyerway());
        }

        String status = "";
        if (LHSP_Status_TSZ.equals(auditSpISubapp.getStatus())) {
            status = "审核中，请耐心等待。。。";
        }
        else if (LHSP_Status_TSWC.equals(auditSpISubapp.getStatus())) {
            status = "审核完成";
        }
        addCallbackParam("status", status);
        initMaterial = auditSpISubapp.getInitmaterial();
        biGuid = auditSpISubapp.getBiguid();
        AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        businessGuid = auditSpInstance.getBusinessguid();
        businessType = auditSpInstance.getBusinesstype();
        yewuguid = auditSpISubapp.getYewuguid();
        dataBean = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
        integratedCompany = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid())
                .getResult();
        phaseGuid = auditSpISubapp.getPhaseguid();
        AuditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
        String itemGuid = auditSpISubapp.getYewuguid();
        if (StringUtil.isNotBlank(itemGuid)) {
            this.addCallbackParam("itemGuid", itemGuid);
            this.addCallbackParam("subappGuid", subappGuid);
            this.addCallbackParam("eformurl", AuditSpPhase.getEformurl());
        }
        if (StringUtil.isNotBlank(AuditSpPhase.getPhasename())) {
            this.addCallbackParam("phasename", AuditSpPhase.getPhasename());
        }
        if (StringUtil.isNotBlank(AuditSpPhase.getStr("formid"))) {
            AuditRsItemBaseinfo pdatabean = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
            addCallbackParam("itemcode", pdatabean.getItemcode());
            addCallbackParam("formid", AuditSpPhase.getStr("formid"));
            addCallbackParam("eformCommonPage", "jiningzwfw/epointsform/eformpage");
            addCallbackParam("epointUrl", configService.getFrameConfigValue("epointsformurl"));
        }
    }

    /**
     * 保存并关闭
     */
    public void save(String isck) {
        List<AuditSpITask> listTask = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();

        // 如果状态为已接件，则说明处理完成了
        if (ZwfwConstant.LHSP_Status_YSJ.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "该流程已处理完成！");
            return;
        }

        // 判断是否第一次发件
        boolean firstsj = true;
        for (AuditSpITask auditspitask : listTask) {
            if (StringUtil.isNotBlank(auditspitask.getProjectguid())) {
                firstsj = false;
            }
        }
        List<String> taskguids = new ArrayList<>();
        // 统一收件
        if (!ZwfwConstant.LHSP_Status_DYS.equals(status)) {
            if (getDataGridTask().getSelectKeys() != null) {
                taskguids.addAll(getDataGridTask().getSelectKeys());
            }
            if (fgetDataGridTask().getSelectKeys() != null) {
                taskguids.addAll(fgetDataGridTask().getSelectKeys());
            }
            listTask = listTask.stream().filter(spitask -> (taskguids.contains(spitask.getRowguid()))
                    && StringUtil.isBlank(spitask.getProjectguid())).collect(Collectors.toList());
        }
        else {
            // 预审逻辑
            listTask = listTask.stream().filter(
                    a -> ZwfwConstant.CONSTANT_STR_ONE.equals(a.getStatus()) && StringUtil.isBlank(a.getProjectguid()))
                    .collect(Collectors.toList());
        }

        /* 3.0新增逻辑验证 */
        // 检查事项是否有推荐关联单位类型,如果有是不是关联了相关类型的单位
        boolean point = false;
        for (AuditSpITask task : listTask) {
            // 查询此事项有没有配置单位类型
            String auditbasetaskguid = taskCommonService
                    .getCropInfo(task.getTaskguid(), subappGuid, task.getPhaseguid()).getResult();
            if (StringUtil.isNotBlank(auditbasetaskguid)) {
                // 查询此基础事项偶没有配置单位类型
                String cropType = taskCommonService.getCropName(auditbasetaskguid).getResult();
                if (StringUtil.isNotBlank(cropType)) {
                    // 查询此单位类型有没有关联单位
                    Integer count = iAuditSpITaskCorpService.countAuditSpITaskCorps(task.getTaskguid(), subappGuid);
                    if (count == 0) {
                        point = true;
                        break;
                    }
                }
            }
        }
        if (point) {
            addCallbackParam("msg", "该流程存在未关联推荐单位类型的单位！");
            addCallbackParam("noClose", "1");
            return;
        }
        /* 3.0新增逻辑结束 */

        String csaeguid = auditSpISubapp.getCaseguid();

        AuditTaskCase auditTaskCase = iAuditTaskCase.getAuditTaskCaseByRowguid(csaeguid).getResult();
        String selectoptions = "";
        if (auditTaskCase != null) {
            selectoptions = auditTaskCase.getSelectedoptions();
        }
        List<String> materiallist = new ArrayList<>();
        if (StringUtil.isNotBlank(selectoptions)) {
            JSONObject jsons = JSONObject.parseObject(selectoptions);
            List<String> optionlist = (List<String>) jsons.get("optionlist");
            Map<String, AuditSpOption> optionmap = new HashMap<>();
            if (optionlist != null && optionlist.size() > 0) {
                for (String guid : optionlist) {
                    AuditSpOption auditSpOption = iAuditSpOptionService.find(guid).getResult();
                    optionmap.put(guid, auditSpOption);
                }
                optionmap = iAuditSpOptionService.getAllOptionByOptionMap(optionmap).getResult();
                for (AuditSpOption auditSpOption : optionmap.values()) {
                    if (StringUtil.isBlank(auditSpOption.getMaterialids())) {
                        continue;
                    }
                    String materials = auditSpOption.getMaterialids();
                    if (StringUtil.isNotBlank(auditSpOption.getMaterialids())) {
                        String[] mater = materials.split(";");
                        for (int i = 0; i < mater.length; i++) {
                            if (StringUtil.isNotBlank(mater[i])) {
                                materiallist.add(mater[i]);
                            }
                        }
                    }
                }
            }
        }
        AuditRsItemBaseinfo result = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(yewuguid).getResult();
        String centerguid1 = ZwfwUserSession.getInstance().getCenterGuid();
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
        List<DantiInfo> alldantiInfo = iDantiInfoService.findListBySubAppguid(subappGuid);
        for (AuditSpITask task : listTask) {
            // 先生成办件，再初始化材料
            String projectGuid = handleProjectService
                    .InitBLSPProject(task.getTaskguid(), "", userSession.getDisplayName(), userSession.getUserGuid(),
                            dataBean.getItemlegalcerttype(), dataBean.getItemlegalcertnum(), "", "", "", biGuid,
                            subappGuid, businessGuid, dataBean.getItemlegalcreditcode(), dataBean.getItemlegaldept())
                    .getResult();
            auditSpITaskService.updateProjectGuid(task.getRowguid(), projectGuid);
            if (auditSpSpSgxk != null && "建筑工程施工许可证核发".equals(task.getTaskname())) {
                AuditProjectFormSgxkz sgxkz = new AuditProjectFormSgxkz();
                sgxkz.setRowguid(UUID.randomUUID().toString());
                sgxkz.setProjectguid(projectGuid);
                sgxkz.setXiangmudaima(auditSpSpSgxk.getItemcode());
                sgxkz.setXiangmubianhao(auditSpSpSgxk.getXmbm());
                sgxkz.setXiangmufenlei(auditSpSpSgxk.getStr("xmfl"));
                sgxkz.setXiangmumingchen(auditSpSpSgxk.getItemname());
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("gctzxz"))) {
                    sgxkz.setInvPropertyNum(auditSpSpSgxk.getStr("gctzxz"));
                }
                sgxkz.setBargainDays(auditSpSpSgxk.getStr("htgq"));
                sgxkz.setXiangmusuozaishengfen(auditSpSpSgxk.getStr("xmszsf"));
                sgxkz.setXiangmusuozaichengshi(auditSpSpSgxk.getStr("xmszcs"));
                sgxkz.setXiangmusuozaiquxian(auditSpSpSgxk.getStr("xmszqx"));
                sgxkz.setXiangmudidian(auditSpSpSgxk.getItemaddress());
                sgxkz.setLiXiangPiFuJiGuan(auditSpSpSgxk.getStr("lxpfjg"));
                sgxkz.setLixiangpifushijian(auditSpSpSgxk.getDate("lxpfsj"));
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("zbtzsbh"))) {
                    sgxkz.setZhongBiaoTongZhiShuBianHao(auditSpSpSgxk.getStr("zbtzsbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("sgtsxmbh"))) {
                    sgxkz.setShiGongTuShenChaXiangMuBianHao(auditSpSpSgxk.getStr("sgtsxmbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("jsydghxkzbh"))) {
                    sgxkz.set("JianSheYongDiGuiHuaXuKeZhen", auditSpSpSgxk.getStr("jsydghxkzbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("jsgcghxkzbh"))) {
                    sgxkz.setJianSheGongChengGuiHuaXuKeZhen(auditSpSpSgxk.getStr("jsgcghxkzbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getMoneysources())) {
                    sgxkz.setZijinlaiyuan(auditSpSpSgxk.getMoneysources());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getAllmoney())) {
                    sgxkz.setXiangmuzongtouziwanyuan(auditSpSpSgxk.getAllmoney() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getAllbuildarea())) {
                    sgxkz.setXiangmuzongjianzhumianjipingfa(auditSpSpSgxk.getAllbuildarea() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getOverloadarea())) {
                    sgxkz.setXiangmudishangjianzhumianjipin(auditSpSpSgxk.getOverloadarea() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getDownloadarea())) {
                    sgxkz.setXiangmudixiajianzhumianjipingf(auditSpSpSgxk.getDownloadarea() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("zcd"))) {
                    sgxkz.setZongchangdumi1(auditSpSpSgxk.getStr("zcd"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("dscd"))) {
                    sgxkz.setXiangmudishangchangdumi(auditSpSpSgxk.getStr("dscd"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("dxcd"))) {
                    sgxkz.setXiangmudixiachangdumi(auditSpSpSgxk.getStr("dxcd"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("kuadu"))) {
                    sgxkz.setXiangmukuadumi(auditSpSpSgxk.getStr("kuadu"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getGcyt())) {
                    sgxkz.setGongChengYongTu(auditSpSpSgxk.getGcyt());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("jsgm"))) {
                    sgxkz.setJiansheguimo(auditSpSpSgxk.getStr("jsgm"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getBuildproperties())) {
                    sgxkz.setJianshexingzhi(auditSpSpSgxk.getBuildproperties());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getProjectlevel())) {
                    sgxkz.setZhongdianxiangmu(auditSpSpSgxk.getProjectlevel());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("htjg"))) {
                    sgxkz.setHetongjiagewanyuan(auditSpSpSgxk.getStr("htjg"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("htkgrq"))) {
                    sgxkz.setHetongkaigongriqi(auditSpSpSgxk.getStr("htkgrq"));
                }

                sgxkz.setHetongjungongriqi(auditSpSpSgxk.getStr("htjgrq"));
                sgxkz.setShentudanwei(auditSpSpSgxk.getStr("stdw"));
                sgxkz.setShenTuDanWeiCode(auditSpSpSgxk.getStr("stdwtyshxydm"));
                sgxkz.setShentudanweixiangmufuzeren(auditSpSpSgxk.getStr("stxmfzr"));
                String itemguid = "";
                if (dataBean != null) {
                    // 如果是子项目则获取主项目guid
                    if (StringUtil.isNotBlank(dataBean.getParentid())) {
                        itemguid = dataBean.getParentid();
                    }
                }

                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("corptype", "31");
                sql.eq("itemguid", itemguid);
                List<ParticipantsInfo> jsinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                ParticipantsInfo jsinfo = null;
                if (jsinfos != null && jsinfos.size() > 0) {
                    jsinfo = jsinfos.get(0);
                    sgxkz.setJianshedanweimingchen(jsinfo.getCorpname());
                    sgxkz.setJianSheDanWeiTongYiSheHuiXinYo(jsinfo.getCorpcode());
                    sgxkz.setJianshedanweidizhi(jsinfo.getAddress());
                    sgxkz.setJianshedanweidianhua(jsinfo.getPhone());
                    sgxkz.setJianshedanweifadingdaibiaoren(jsinfo.getLegal());
                    sgxkz.setJianshedanweixiangmufuzeren(jsinfo.getXmfzr());
                }

                SqlConditionUtil sql1 = new SqlConditionUtil();
                sql1.eq("corptype", "2");
                sql1.eq("subappguid", subappGuid);
                List<ParticipantsInfo> sjinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql1.getMap());
                ParticipantsInfo sjinfo = null;
                if (sjinfos != null && sjinfos.size() > 0) {
                    sjinfo = sjinfos.get(0);
                    sgxkz.setShejidanwei(sjinfo.getCorpname());
                    sgxkz.setShejidanweixiangmufuzeren(sjinfo.getXmfzr());
                    sgxkz.setSheJiDanWeiCode(sjinfo.getCorpcode());
                }

                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("corptype", "1");
                sql2.eq("subappguid", subappGuid);
                List<ParticipantsInfo> kcinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql2.getMap());
                ParticipantsInfo kcinfo = null;
                if (kcinfos != null && kcinfos.size() > 0) {
                    kcinfo = kcinfos.get(0);
                    sgxkz.setKanchadanwei(kcinfo.getCorpname());
                    sgxkz.setKanchadanweixiangmufuzeren(kcinfo.getXmfzr());
                    sgxkz.setKanChaDanWeiCode(kcinfo.getCorpcode());
                }

                SqlConditionUtil sql3 = new SqlConditionUtil();
                sql3.eq("corptype", "3");
                sql3.eq("subappguid", subappGuid);
                List<ParticipantsInfo> sginfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql3.getMap());
                ParticipantsInfo sginfo = null;
                if (sginfos != null && sginfos.size() > 0) {
                    sginfo = sginfos.get(0);
                    sgxkz.setShigongzongchengbaoqiye(sginfo.getCorpname());
                    sgxkz.setShiGongZongChengBaoQiYeTongYiS(sginfo.getCorpcode());
                    sgxkz.setShigongzongchengbaoqiyexiangmu(sginfo.getXmfzperson());
                }

                SqlConditionUtil sql4 = new SqlConditionUtil();
                sql4.eq("corptype", "4");
                sql4.eq("subappguid", subappGuid);
                List<ParticipantsInfo> jlinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql4.getMap());
                ParticipantsInfo jlinfo = null;
                if (jlinfos != null && jlinfos.size() > 0) {
                    jlinfo = jlinfos.get(0);
                    sgxkz.setJianlidanwei(jlinfo.getCorpname());
                    sgxkz.setJianLiDanWeiCode(jlinfo.getCorpcode());
                    sgxkz.setZongjianligongchengshi(jlinfo.getXmfzr());
                }

                SqlConditionUtil sql5 = new SqlConditionUtil();
                sql5.eq("corptype", "11");
                sql5.eq("subappguid", subappGuid);
                List<ParticipantsInfo> jcinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql5.getMap());
                ParticipantsInfo jcinfo = null;
                if (jcinfos != null && jcinfos.size() > 0) {
                    jcinfo = jcinfos.get(0);
                    sgxkz.setJiancedanwei(jcinfo.getCorpname());
                    sgxkz.setJianCeDanWeiCode(jcinfo.getCorpcode());
                    sgxkz.setJianCeDanWeiFuZeRen(jcinfo.getXmfzr());
                }

                iAuditProjectFormSgxkzService.insert(sgxkz);
                log.info("插入施工许可证信息成功：" + sgxkz);

                if (alldantiInfo != null && alldantiInfo.size() > 0) {
                    for (DantiInfo dantiInfo : alldantiInfo) {
                        AuditProjectFormSgxkzDanti danti = new AuditProjectFormSgxkzDanti();
                        danti.setRowguid(UUID.randomUUID().toString());
                        danti.setProjectguid(projectGuid);
                        danti.setDantijiangouzhuwumingchen(dantiInfo.getDantiname());
                        danti.setGongchengzaojiawanyuan(dantiInfo.getPrice() + "");
                        danti.setJianzhumianjipingfangmi(dantiInfo.getZjzmj() + "");
                        danti.setDixiacengshu(dantiInfo.getDxcs() + "");
                        danti.setDishangcengshu(dantiInfo.getDscs() + "");
                        danti.setSeismicIntensityScale(dantiInfo.getKzlevel() + "");
                        danti.setISSuperHightBuilding(dantiInfo.getIslowenergy());
                        iAuditProjectFormSgxkzService.insert(danti);
                        log.info("插入施工许可证单体信息成功：" + danti);
                    }
                }
            }
            // 保存办件表单
            if (StringUtil.isNotBlank(yewuguid)) {
                AuditProject project = new AuditProject();
                project.setRowguid(projectGuid);
                project.setProjectname(task.getTaskname() + "(" + result.getItemname() + ")");
                project.setCertnum(result.getItemlegalcertnum());
                project.setApplyername(result.getItemlegaldept());
                project.setAddress(result.getConstructionsite());
                project.setContactperson(result.getContractperson());
                project.setContactcertnum(result.getContractidcart());
                project.setContactphone(result.getContractphone());
                project.setContactemail(result.getFremail());
                project.setXiangmubh(result.getRowguid());
                project.setTaskcaseguid(csaeguid);
                project.setRemark(result.getConstructionscaleanddesc());
                /*
                 * String flowsn=getFlowsn(task.getTaskguid()); Random random = new Random();
                 * if(StringUtil.isBlank(flowsn)){ flowsn= "080101"+ random.nextInt(1000 - 100)
                 * + 100 + 1; } project.setFlowsn(flowsn);
                 */

                auditProjectService.updateProject(project);
            }
            sb.append(projectGuid).append("','");
            // 再进行材料初始化
            handleSPIMaterialService.initProjctMaterial(materiallist, businessGuid, subappGuid, task.getTaskguid(),
                    projectGuid);

            // 判断申报事项是否为供水（370800ZSSW000000000000000000002、3708000000000000000000000000102）
            // 供电（913708001659213936337209900400201、913708001659213936337209900400102）
            // taskGuid查询事项表，根据item_id判断
            log.info("判断当前事项是否是供水、供电或燃气的事项");
            // String pushTaskItem = configservice.getFrameConfigValue("pushTaskItem");
            // log.info("获取系统参数配置的水电气暖事项itemid--->" + pushTaskItem);
            // 供电推送
            String gdPushTaskItem = configService.getFrameConfigValue("gdPushTaskItem");
            // 供水推送
            String gsPushTaskItem = configService.getFrameConfigValue("gsPushTaskItem");
            // 燃气推送
            String rqPushTaskItem = configService.getFrameConfigValue("rqPushTaskItem");
            log.info("gdPushTaskItem--->" + gdPushTaskItem);
            log.info("gsPushTaskItem--->" + gsPushTaskItem);
            log.info("rqPushTaskItem--->" + rqPushTaskItem);
            List<String> pushTaskItemIdList = new ArrayList<>();
            List<String> gdList = new ArrayList<>();
            List<String> gsList = new ArrayList<>();
            List<String> rqList = new ArrayList<>();
            if (StringUtil.isNotBlank(gdPushTaskItem)) {
                gdList = Arrays.stream(gdPushTaskItem.split(",")).collect(Collectors.toList());
                pushTaskItemIdList.addAll(gdList);
            }
            if (StringUtil.isNotBlank(gsPushTaskItem)) {
                gsList = Arrays.stream(gsPushTaskItem.split(",")).collect(Collectors.toList());
                pushTaskItemIdList.addAll(gsList);
            }
            if (StringUtil.isNotBlank(rqPushTaskItem)) {
                rqList = Arrays.stream(rqPushTaskItem.split(",")).collect(Collectors.toList());
                pushTaskItemIdList.addAll(rqList);
            }
            log.info("水电气暖全部推送itemId--->" + pushTaskItemIdList);
            // if (StringUtil.isNotBlank(pushTaskItem)) {
            if (EpointCollectionUtils.isNotEmpty(pushTaskItemIdList)) {
                // List<String> pushTaskItemIdList =
                // Arrays.stream(pushTaskItem.split(",")).collect(Collectors.toList());
                AuditTask auditTask = iaudittask.getAuditTaskByGuid(task.getTaskguid(), false).getResult();
                if (EpointCollectionUtils.isNotEmptyMap(auditTask)) {
                    String itemId = auditTask.getItem_id();
                    log.info("获取事项itemId--->" + itemId);
                    if (pushTaskItemIdList.contains(itemId)) {
                        log.info("该事项属于推送事项");
                        // 属于推送事项，需要调用第三方流程提交检查接口和发起流程接口
                        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, null)
                                .getResult();
                        log.info("推送事项的办件主键--->" + auditProject.getRowguid());
                        if (EpointCollectionUtils.isNotEmptyMap(auditProject)) {
                            // if ("913708001659213936337209900400201".equals(itemId) ||
                            // "913708001659213936337209900400102".equals(itemId)) {
                            if (gdList.contains(itemId)) {
                                // 先获取电子表单数据
                                jsbdysq(subappGuid, auditProject);
                                // 如果是供电事项，获取申报时传递的预申请编码
                                log.info("供电事项，获取申报时传递的预申请编码");
                                log.info("更新了subApp的preAppNo字段，重新查询一下subApp实体");
                                AuditSpISubapp tempSubApp = auditSpISubappService.getSubappByGuid(subappGuid)
                                        .getResult();
                                log.info("查询出的subApp实体--->" + tempSubApp);
                                String preAppNo = tempSubApp.getStr("preAppNo");
                                AuditSpProjectinfoGdbz bySubAppGuid = auditSpProjectinfoGdbzService
                                        .getBySubAppGuid(subappGuid);
                                if (StringUtil.isBlank(preAppNo)) {
                                    log.info("从subApp中没有查询出preAppNo，改为从供电报装办件中查询preAppNo");
                                    // bySubAppGuid = auditSpProjectinfoGdbzService.getBySubAppGuid(subappGuid);
                                    log.info("供电报装办件实体--->" + bySubAppGuid);
                                    preAppNo = bySubAppGuid.getStr("preAppNo");
                                }
                                log.info("preAppNo--->" + preAppNo);
                                // 根据该编码查找对应的供电报装办件
                                // SqlConditionUtil sql = new SqlConditionUtil();
                                // sql.eq("preAppNo", preAppNo);
                                // log.info("根据preAppNo查询供电报装办件");
                                // AuditSpProjectinfoGdbz auditspprojectinfogdbz = new SQLManageUtil()
                                // .getBeanByCondition(AuditSpProjectinfoGdbz.class, sql.getMap());
                                if (EpointCollectionUtils.isNotEmptyMap(bySubAppGuid)) {
                                    log.info("查询到供电报装办件实体，将对应的projectGuid设置到供电报装办件的projectGuid字段");
                                    bySubAppGuid.setProjectGuid(projectGuid);
                                    auditSpProjectinfoGdbzService.update(bySubAppGuid);
                                    log.info("更新完成，对应的projectGuid--->" + projectGuid);
                                }
                            }
                            // else if("91370800798683639M3372099rq1rq3".equals(itemId) ||
                            // "91370800798683639M3372099rq1rq2".equals(itemId)) {
                            else if (rqList.contains(itemId)) {
                                // 燃气事项
                                log.info("燃气事项");
                                log.info("注册身份用户信息");
                                String rqsfzyhzc = rqsfzyhzc(auditProject);
                                log.info("调用注册身份用户信息结果--->" + rqsfzyhzc);
                                if (StringUtil.isNotBlank(rqsfzyhzc)) {
                                    log.info("调用注册身份用户信成功，调用燃气报装提交接口");
                                    String rqbztj = rqbztj(subappGuid, projectGuid);
                                    if (StringUtil.isNotBlank(rqbztj)) {
                                        log.info("报装成功");
                                        log.info("生成接件的办件操作数据");
                                        AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                        auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                        auditProjectOperation.setProjectGuid(projectGuid);
                                        auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                        auditProjectOperation
                                                .setOperateUserGuid(UserSession.getInstance().getUserGuid());
                                        auditProjectOperation.setOperatedate(new Date());
                                        auditProjectOperation
                                                .setOperateusername(UserSession.getInstance().getDisplayName());
                                        auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                        auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                        auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                        auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                        auditProjectOperationService.addProjectOperation(auditProjectOperation);
                                        log.info("生成完成");
                                        // 生成完之后，直接发送mq生成受理步骤的数据
                                        log.info("生成受理数据");
                                        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
                                        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");
                                        handleProjectService.handleAccept(auditProject, "", "华润燃气", userguid, "综合窗口",
                                                windowguid);
                                        String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                                        log.info("发送mq");
                                        if (auditProject.getIs_test() != Integer
                                                .parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                                            log.info("非测试件");
                                            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                                    + "370800" + "." + "accept" + "." + auditProject.getTask_id());
                                        }
                                    }
                                    else {
                                        log.info("报装失败，不做任何操作");
                                    }
                                }
                                else {
                                    log.info("调用注册身份用户信失败，不做任何操作");
                                }
                            }
                            else {
                                // 供水事项
                                String projectFlowSn = auditProject.getFlowsn();
                                log.info("获取办件流水号--->" + projectFlowSn);
                                JSONObject checkResult;
                                int i = 0;
                                while (true) {
                                    checkResult = checkSubmitFlow(projectFlowSn);
                                    if (checkResult.getBoolean("isSuccess")) {
                                        // 如果请求成功，跳出循环
                                        break;
                                    }
                                    else if (!checkResult.getBoolean("isSuccess")
                                            && "0".equals(checkResult.getString("code"))) {
                                        // 请求失败同时code为0表示流水号有问题，重新生成流水号
                                        /*IHandleFlowSn handleFlowSn = ContainerFactory.getContainInfo()
                                                .getComponent(IHandleFlowSn.class);
                                        String numberFlag = handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE,
                                                auditProject.getCenterguid()).getResult();
                                        if (StringUtil.isBlank(numberFlag)) {
                                            numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
                                        }
                                        projectFlowSn = handleFlowSn.getFlowsn("办件编号", numberFlag).getResult();
                                        log.info("重新生成的流水号--->" + projectFlowSn);*/
                                        String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());

                                        auditProject.set("flowsn", flowsn);
                                        auditProjectService.updateProject(auditProject);
                                        log.info("更新办件流水号--->" + auditProject.getFlowsn());
                                    }
                                    else {
                                        // 请求失败
                                        log.info("请求失败，" + checkResult.getString("msg"));
                                        break;
                                    }
                                    i++;
                                    if (i == 100) {
                                        log.info("请求达到100次，跳出循环");
                                        break;
                                    }
                                }
                                // 走到这里，说明处理完毕，跳出循环的情况只需判断isSuccess是true还是false即可
                                if (!checkResult.getBoolean("isSuccess")) {
                                    // 请求失败
                                    log.info("跳出循环，请求失败，" + checkResult.getString("msg"));
                                }
                                else {
                                    // 继续请求发起流程接口
                                    String billNo = startProcess(auditProject);
                                    if (StringUtil.isNotBlank(billNo)) {
                                        log.info("已请求发起流程接口，单据编号为：" + billNo);
                                    }
                                    else {
                                        log.info("请求发起流程接口异常，未获取到单据编号");
                                    }
                                }
                            }
                        }
                        else {
                            log.info("未查询到办件，projectGuid：" + projectGuid);
                        }
                    }
                }
                else {
                    log.info("未查询到事项");
                }
            }
        }
        sb.append("'");
        // 办件初始化完成推送办件到住建系统,20240909最新调整不再在处理完成步骤推送办件，改为在窗口待受理步骤点击受理按钮同步
        projectSendzj(isck, sb.toString());
        listTask = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
        boolean flag = false;
        for (AuditSpITask auditspitask : listTask) {
            if (StringUtil.isBlank(auditspitask.getProjectguid())) {
                flag = true;
            }
        }
        // 第二次分发办件不进行下面操作，内网状态
        if (LHSP_Status_SJZ.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "收件完成！");
            // 判断是否全部办理
            if (!flag) {
                auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
                afterYsj(isck);
            }
            return;
        }

        // 第一次申报，如果全部办理，修改为已收件
        if (flag) {
            auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_SJZ, null);
            addCallbackParam("msg", "收件完成！");
        }
        else {
            auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
            afterYsj(isck);
        }

        // 如果过第一次申报，插入计时数据记录
        if (firstsj) {
            String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
            if (StringUtil.isBlank(centerguid)) {
                // 如果session里面没有centerguid，则获取辖区下第一个中心标识
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("belongxiaqu", ZwfwUserSession.getInstance().getAreaCode());
                List<AuditOrgaServiceCenter> listcenter = iauditorgaservicecenter
                        .getAuditOrgaServiceCenterByCondition(sqlc.getMap()).getResult();
                if (listcenter.size() > 0) {
                    centerguid = listcenter.get(0).getRowguid();
                }
            }
            sparetimeService.addSpareTimeByProjectGuid(subappGuid, ZwfwConstant.CONSTANT_INT_ZERO,
                    ZwfwUserSession.getInstance().getAreaCode(), centerguid);
            // 插入后暂停计时
            AuditProjectSparetime auditProjectSparetime = sparetimeService.getSparetimeByProjectGuid(subappGuid)
                    .getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            sparetimeService.updateSpareTime(auditProjectSparetime);
        }
        else {
            return;
        }

        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        String roleGuid = roleService.listRole("统一收件人员", "").get(0).getRoleGuid();
        List<FrameUser> listUser = userService.listUserByOuGuid(area.getOuguid(), roleGuid, "", "", false, true, true,
                3);
        for (FrameUser user : listUser) {
            messageCenterService.deleteMessageByIdentifier(subappGuid, user.getUserGuid());
        }

    }

    public void afterYsj(String isck) {
        // 如果是第四阶段，需要和联合验收系统对接
        String tkurl = configservice.getFrameConfigValue("AS_BLSP_TKURL");
        if ("4".equals(AuditSpPhase.getPhaseId()) && StringUtil.isNotBlank(tkurl)) {
            String msg = subappGuid + "_SPLIT_" + ZwfwUserSession.getInstance().getAreaCode() + "_SPLIT_"
                    + dataBean.getItemcode() + "_SPLIT_" + WebUtil.getRequestCompleteUrl(request);
            sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.tk." + businessGuid);
        }
    }

    public void projectSendzj(String isck, String projectguids) {
        // 发送mq消息推送数据
        String msg = subappGuid + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + dataBean.getItemcode();
        if ("false".equals(isck)) {
            msg += ".nck";
        }
        else {
            msg += ".isck";
        }
        // 拼接办理人用户标识
        msg += "." + UserSession.getInstance().getUserGuid() + "." + projectguids;
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.subapp." + businessGuid);
        log.info("消防发送mq=====");
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.xf.subapp." + businessGuid);
        addCallbackParam("msg", "收件完成！");
    }

    /**
     * 保存并关闭
     */
    public void saveBicheck() {

        List<AuditSpIMaterial> lists = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid).getResult();
        String selectids = "";
        if (ZwfwConstant.LHSP_Status_DYS.equals(status)) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("subappguid", subappGuid);
            sqlc.eq("status", ZwfwConstant.CONSTANT_STR_ONE);
            sqlc.isBlank("projectguid");
            List<String> listguid = auditSpITaskService.getStringListByCondition("taskguid", sqlc.getMap()).getResult();
            selectids = StringUtil.join(listguid, ";");
        }
        String[] taskguid = null;
        if (selectids != null) {
            taskguid = selectids.split(";");
        }
        if (taskguid != null && taskguid.length > 0) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("taskguid", StringUtil.joinSql(taskguid));
            List<AuditTaskMaterial> listm = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap()).getResult();
            List<String> materilids = new ArrayList<>();
            for (AuditTaskMaterial auditTaskMaterial : listm) {
                materilids.add(auditTaskMaterial.getMaterialid());
            }
            List<String> spimaterialids = auditSpIMaterialService
                    .getSpIMaterialsByIDS(businessGuid, subappGuid, materilids).getResult();
            lists = lists.stream().filter(auditspimaterial -> spimaterialids.contains(auditspimaterial.getRowguid()))
                    .collect(Collectors.toList());
        }
        else {
            lists = new ArrayList<>();
        }

        // 去除审核通过的材料
        if (lists != null && lists.size() > 0) {
            lists.removeIf(material -> {
                if (ZwfwConstant.CONSTANT_STR_THREE.equals(material.getIsbuzheng())) {
                    return true;
                }
                else {
                    return false;
                }
            });
        }
        // 有审核不通过的材料,修改状态为预审材料待补正
        if (lists.size() > 0) {
            // 查找所有审核不通过的数据，更新auditsp材料
            List<String> materialids = new ArrayList<>();
            // 获取布阵的材料的id
            for (AuditSpIMaterial auditSpIMaterial : lists) {
                // 是否共享材料
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getShared())) {
                    List<AuditSpShareMaterialRelation> listr = iauditspsharematerialrelation
                            .selectByShareMaterialGuid(businessGuid, auditSpIMaterial.getMaterialguid()).getResult();
                    for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listr) {
                        materialids.add(auditSpShareMaterialRelation.getMaterialid());
                    }
                }
                else {
                    materialids.add(auditSpIMaterial.getMaterialguid());
                }
            }
            List<String> taskguids = iaudittaskmaterial.getTaskguidListByMaterialids(materialids).getResult();
            if (taskguids.size() > 0) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("subappguid", subappGuid);
                sqlc.eq("status", ZwfwConstant.CONSTANT_STR_ONE);
                sqlc.in("taskguid", StringUtil.joinSql(taskguids));
                List<AuditSpITask> spitasks = auditSpITaskService.getAuditSpITaskListByCondition(sqlc.getMap())
                        .getResult();
                for (AuditSpITask auditSpITask : spitasks) {
                    auditSpITask.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    auditSpITaskService.updateAuditSpITask(auditSpITask);
                }
            }
            auditSpISubappService.updateSubapp(subappGuid, "34", null);
            // 大厅发送的代办消息
            AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
            String username = userService.getUserNameByUserGuid(auditSpISubapp.getApplyerguid());
            if (auditSpISubapp != null && StringUtil.isBlank(username)) {
                String openUrl = configService.getFrameConfigValue("zwdtMsgurl")
                        + "/epointzwmhwz/pages/approve/perioddetail?biguid=" + auditSpISubapp.getBiguid()
                        + "&phaseguid=" + auditSpISubapp.getPhaseguid() + "&businessguid="
                        + auditSpISubapp.getBusinessguid() + "&subappguid=" + auditSpISubapp.getRowguid() + "&itemguid="
                        + dataBean.getRowguid() + "&type=watch";
                String title = "【材料审核完成】"
                        + rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(yewuguid).getResult().getItemname();
                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, auditSpISubapp.getApplyerguid(), "",
                        UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), "",
                        openUrl, UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1,
                        null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
            }
        }
        else {
            save("false");
        }
        addCallbackParam("msg", "材料审核完成！");
        // 处理完成删除所有待办，查询改辖区的所有统一收件人
        handleProjectService.delZwfwMessage("", ZwfwUserSession.getInstance().getAreaCode(), "统一收件人员", subappGuid);
        // 工程建设阶段，复制规划设计方案审查材料附件给11370500004311394A3370310004812
        try {
            AuditSpPhase phase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
            if (phase != null && phase.getPhasename().contains("工程建设")) {
                // 获取事项
                List<AuditSpIMaterial> oriMaterials = getSpIMaterialList();
                List<AuditProjectMaterial> desMaterials = getProjectMaterialList();
                log.info("oriMaterials:" + oriMaterials);
                log.info("desMaterials:" + desMaterials);
                oriMaterials.forEach(oriMaterial -> {
                    desMaterials.forEach(desMaterial -> {
                        if (oriMaterial.getMaterialname().equals(desMaterial.getTaskmaterial())) {
                            desMaterial.setStatus(20);
                            iAttachService.copyAttachByClientGuid(oriMaterial.getCliengguid(), null, null,
                                    desMaterial.getCliengguid());
                            projectMaterialService.updateProjectMaterial(desMaterial);
                        }
                    });
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AuditSpIMaterial> getSpIMaterialList() {
        List<AuditSpIMaterial> spIMaterials = new ArrayList<>();
        // 查询spi_task 中的规划审查事项,获取规划审查事项的guid
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        log.info("处理人区划：" + areaCode);
        switch (StringUtil.isBlank(areaCode) ? "370800" : areaCode) {
            case "370828":
            case "370830":
            case "370832":
            case "370881":
            case "370890":
            case "370882":
                sqlUtil.eq("taskName", "建设工程规划许可证");
                break;
            default:
                sqlUtil.eq("taskName", "建设工程规划许可证核发（建筑类）");
                break;
        }
        sqlUtil.eq("subAppGuid", subappGuid);
        List<AuditSpITask> auditSpITasks = auditSpITaskService.getAuditSpITaskListByCondition(sqlUtil.getMap())
                .getResult();
        if (CollectionUtils.isNotEmpty(auditSpITasks)) {
            log.info("查询出的规划审查事项的guid:" + auditSpITasks.get(0).getTaskguid());
            String taskGuid = auditSpITasks.get(0).getTaskguid();
            List<AuditTaskMaterial> auditTaskMaterialList = iaudittaskmaterial
                    .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
            List<String> materialIds = auditTaskMaterialList.stream().map(AuditTaskMaterial::getMaterialid)
                    .collect(Collectors.toList());
            List<String> spiMaterialGuids = auditSpIMaterialService
                    .getSpIMaterialsByIDS(businessGuid, subappGuid, materialIds).getResult();
            log.info("spiMaterialGuids:" + spiMaterialGuids);
            if (CollectionUtils.isNotEmpty(spiMaterialGuids)) {
                for (String spiMaterialGuid : spiMaterialGuids) {
                    AuditSpIMaterial spIMaterial = auditSpIMaterialService.getSpIMaterialByrowguid(spiMaterialGuid)
                            .getResult();
                    if (spIMaterial != null) {
                        spIMaterials.add(spIMaterial);
                    }
                }
            }
        }
        return spIMaterials;
    }

    private List<AuditProjectMaterial> getProjectMaterialList() {
        List<AuditProjectMaterial> projectMaterials = new ArrayList<>();
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.eq("IS_EDITAFTERIMPORT", "1");
        sqlUtil.isBlankOrValue("IS_HISTORY", "0");
        sqlUtil.eq("IS_ENABLE", "1");
        sqlUtil.eq("item_id", "11370500004311394A3370310004812");
        List<AuditTask> auditTasks = iaudittask.getAuditTaskList(sqlUtil.getMap()).getResult();
        if (CollectionUtils.isNotEmpty(auditTasks)) {
            sqlUtil.clear();
            sqlUtil.eq("subAppGuid", subappGuid);
            sqlUtil.eq("taskGuid", auditTasks.get(0).getRowguid());
            // 获取办件
            AuditProject auditProject = auditProjectService.getAuditProjectByCondition(sqlUtil.getMap());
            if (auditProject != null) {
                projectMaterials = projectMaterialService.selectProjectMaterial(auditProject.getRowguid()).getResult();
            }
        }
        return projectMaterials;
    }

    public String modelAuditMaterial(String selectids) {
        if (StringUtil.isNotBlank(selectids)) {
            String[] rowguids = selectids.split(";");
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("rowguid", StringUtil.joinSql(rowguids));
            List<String> listguid = auditSpITaskService.getStringListByCondition("taskguid", sqlc.getMap()).getResult();
            selectids = StringUtil.join(listguid, ";");
        }
        // 如果是外网申报，只展示已申报的材料
        if (ZwfwConstant.LHSP_Status_DYS.equals(status)) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("subappguid", subappGuid);
            sqlc.eq("status", ZwfwConstant.CONSTANT_STR_ONE);
            sqlc.isBlank("projectguid");
            List<String> listguid = auditSpITaskService.getStringListByCondition("taskguid", sqlc.getMap()).getResult();
            selectids = StringUtil.join(listguid, ";");
        }

        String[] taskguid = null;
        if (selectids != null) {
            taskguid = selectids.split(";");
        }
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        // 如果材料尚未初始化，那就要先初始化一下材料
        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(initMaterial)) {

            // 组织机构代码证
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(dataBean.getItemlegalcerttype())) {
                AuditRsCompanyBaseinfo auditCompany = companyBaseinfoService
                        .getCompanyByOneField("organcode", dataBean.getItemlegalcertnum()).getResult();
                if (auditCompany != null) {
                    ownerGuid = auditCompany.getCompanyid();
                }
            }
            // 统一社会信用代码
            else {
                AuditRsCompanyBaseinfo auditCompany = companyBaseinfoService
                        .getCompanyByOneField("creditcode", dataBean.getItemlegalcertnum()).getResult();
                if (auditCompany != null) {
                    ownerGuid = auditCompany.getCompanyid();
                }
            }
            listMaterial = handleSPIMaterialService.initSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid,
                    ownerGuid, dataBean.getItemlegalcertnum()).getResult();
        }
        else {
            listMaterial = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid).getResult();
        }
        // 更具事项过滤材料
        if (taskguid != null && taskguid.length > 0) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("taskguid", StringUtil.joinSql(taskguid));
            List<AuditTaskMaterial> listm = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap()).getResult();
            List<String> materilids = new ArrayList<>();
            for (AuditTaskMaterial auditTaskMaterial : listm) {
                materilids.add(auditTaskMaterial.getMaterialid());
            }
            List<String> spimaterialids = auditSpIMaterialService
                    .getSpIMaterialsByIDS(businessGuid, subappGuid, materilids).getResult();
            listMaterial = listMaterial.stream()
                    .filter(auditspimaterial -> spimaterialids.contains(auditspimaterial.getRowguid()))
                    .collect(Collectors.toList());
        }
        else {
            listMaterial = new ArrayList<>();
        }

        // // 判断是否存在需要图审的材料
        // String tsmaterial =
        // configservice.getFrameConfigValue("AS_BLSP_TSMATERIAL");
        // String[] tsmaterials = tsmaterial.split(";");
        // //图审材料ids
        // List<String> listtsmaterials =
        // StringUtil.change2ArrayList(tsmaterials);
        // AuditSpShareMaterialRelation r = null;
        // 查找图审材料是是否存在共享材料
        // if (listtsmaterials.size() > 0) {
        // r = relationservice.getRelationByIDS(businessGuid, "'" +
        // StringUtil.join(tsmaterials, "','") + "'")
        // .getResult();
        // }

        // List<Record> projectMaterial =
        // handleMaterialService.getProjectMaterial(projectGuid).getResult();
        JSONObject jsonMaterials = new JSONObject();
        try {
            // 这里需要定义几个变量
            int submitedMaterialCount = 0;
            int shouldPaperCount = 0;
            int submitedPaperCount = 0;
            int submitedAttachCount = 0;
            int shouldAttachCount = 0;
            int rongqueCount = 0;
            int buRongqueCount = 0;
            jsonMaterials.put("total", listMaterial != null ? listMaterial.size() : 0);
            if (listMaterial != null) {
                for (int i = 0; i < listMaterial.size(); i++) {
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(listMaterial.get(i).getResult())) {
                        JSONObject jsonMaterial = new JSONObject();
                        int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                        String submittype = listMaterial.get(i).get("SUBMITTYPE");
                        jsonMaterial.put("status", listMaterial.get(i).getStatus());
                        jsonMaterial.put("rowguid", listMaterial.get(i).getRowguid());
                        jsonMaterial.put("necessity", listMaterial.get(i).get("NECESSITY"));
                        jsonMaterial.put("isbuzheng", listMaterial.get(i).getIsbuzheng());
                        // 判断此材料是不是测绘材料
                        // 材料前面的标识
                        String materialtype = "";
                        if (StringUtil.isNotBlank(listMaterial.get(i).getMaterialtype())) {

                            // 图审材料、测绘材料、多评材料。写死
                            // 材料类型，4位为1层。第一层
                            String materialTypeMain = listMaterial.get(i).getMaterialtype().substring(0, 4);

                            if (ProjectConstant.MATERIAL_TYPE_TSCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("ists", ZwfwConstant.CONSTANT_STR_ONE);
                                    jsonMaterial.put("isbuzheng", ZwfwConstant.CONSTANT_INT_TWO);
                                }
                                materialtype = "<span style=\"color:red\">【图审】</span>";
                            }
                            else if (ProjectConstant.MATERIAL_TYPE_CHCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("isch", ZwfwConstant.CONSTANT_STR_ONE);
                                }
                                jsonMaterials.put("ischmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                                materialtype = "<span style=\"color:red\">【测绘】</span>";
                            }
                            else if (ProjectConstant.MATERIAL_TYPE_DPCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("isdp", ZwfwConstant.CONSTANT_STR_ONE);
                                }
                                jsonMaterials.put("isdpmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                                materialtype = "<span style=\"color:red\">【多评】</span>";
                            }
                            else if (ProjectConstant.MATERIAL_TYPE_LSCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("isls", ZwfwConstant.CONSTANT_STR_ONE);
                                    jsonMaterial.put("isbuzheng", ZwfwConstant.CONSTANT_INT_TWO);
                                }
                                materialtype = "<span style=\"color:red\">【联审】</span>";
                            }

                            // //图审材料不存在存在共享材料,比较materialid
                            // if
                            // (listtsmaterials.contains(listMaterial.get(i).getMaterialguid()))
                            // {
                            // }
                            // //图审材料存在共享材料,比较sharematerialguid
                            // if (r != null) {
                            // if
                            // (r.getSharematerialguid().equals(listMaterial.get(i).getMaterialguid()))
                            // {
                            // jsonMaterial.put("istsmaterial",
                            // ZwfwConstant.CONSTANT_STR_ONE);
                            // jsonMaterials.put("ists",
                            // ZwfwConstant.CONSTANT_STR_ONE);
                            // }
                            // }
                            jsonMaterial.put("materialtype", materialtype);
                        }
                        jsonMaterial.put("materialname", listMaterial.get(i).get("MATERIALNAME"));
                        String is_rongque = "";
                        if ("1".equals(listMaterial.get(i).get("NECESSITY").toString())
                                || "10".equals(listMaterial.get(i).get("NECESSITY").toString())) {
                            jsonMaterial.put("isnecessity", "必需");
                            if (ZwfwConstant.CONSTANT_STR_ONE
                                    .equals(listMaterial.get(i).get("ALLOWRONGQUE").toString())) {
                                is_rongque = "1";
                                rongqueCount++;
                            }
                            else {
                                is_rongque = "0";
                                buRongqueCount++;
                            }
                        }
                        else {
                            jsonMaterial.put("isnecessity", "非必需");
                        }
                        jsonMaterial.put("submittype", submittype);
                        jsonMaterial.put("cliengguid", listMaterial.get(i).get("CLIENGGUID"));
                        jsonMaterial.put("shared", listMaterial.get(i).getShared());
                        jsonMaterial.put("is_rongque", is_rongque);
                        materialList.add(jsonMaterial);
                        // 这里对材料信息进行判断
                        // 材料的提交情况
                        if (materialstatus > 10) {
                            submitedMaterialCount++;
                            if (materialstatus == 20) {
                                submitedAttachCount++;
                            }
                            else if (materialstatus == 15) {
                                submitedPaperCount++;
                            }
                            else {
                                submitedAttachCount++;
                                submitedPaperCount++;
                            }
                        }
                        // 材料的应提交情况
                        if (!"20".equals(submittype)) {
                            shouldAttachCount++;
                        }
                        if (!"10".equals(submittype)) {
                            shouldPaperCount++;
                        }
                    }
                }
                // if(hasch&&isPostback()){
                // materialinit();
                // }
            }
            jsonMaterials.put("type", "submit");
            jsonMaterials.put("isAllPaper", shouldPaperCount > submitedPaperCount ? 0 : 1);
            // 提交纸质为0应提交0时不显示勾选
            if (shouldPaperCount == 0 && submitedPaperCount == 0) {
                jsonMaterials.put("isAllPaper", 0);
            }
            jsonMaterials.put("data", materialList);
            jsonMaterials.put("materialSummary", "已标记" + submitedMaterialCount + "件材料，其中纸质" + submitedPaperCount
                    + "件，电子文档" + submitedAttachCount + "件；容缺" + rongqueCount + "件；不容缺" + buRongqueCount + "件。");

        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
        return jsonMaterials.toString();
    }

    public void materialinit() throws IOException {
        String msg = iauditblspmaterial.chMaterialinit(dataBean.getRowguid(), subappGuid);
        try {
            addCallbackParam("attachlist", JSONArray.parseArray(msg));
        }
        catch (Exception e) {
            addCallbackParam("msg", msg);
        }
    }

    public void materialinit_dpcl() {
        String msg = iauditblspmaterial.dpMaterialinit(dataBean.getRowguid(), subappGuid);
        try {
            addCallbackParam("attachlist", JSONArray.parseArray(msg));
        }
        catch (Exception e) {
            addCallbackParam("msg", msg);
        }
    }

    /**
     * [测绘下载功能] [功能详细描述]
     *
     * @param params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void uploadAttach(String params) {
        JSONArray array = JSONArray.parseArray(params);
        array.getJSONObject(0).getString("uploaduserdisplayname");
        String attachfilename = array.getJSONObject(0).getString("attachfilename");
        String attachurl = array.getJSONObject(0).getString("attachurl");
        String materialguid = array.getJSONObject(0).getString("materialguid");

        AuditSpIMaterial auditSpIMaterial = auditSpIMaterialService.getSpIMaterialByrowguid(materialguid).getResult();
        InputStream in = null;
        try {
            if (StringUtil.isNotBlank(attachurl)) {
                JSONObject file = ProjectConstant.filePathToInputStream(attachurl);
                // 获取附件流
                // URL url1 = new
                // URL("http://192.168.203.124:8080/szjslhchqy/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=0116224a-4a20-45a7-8641-4f8a0323fc0d");
                URL url1 = new URL(attachurl);
                URLConnection conn = url1.openConnection();
                Long fileLength = conn.getContentLengthLong();
                in = conn.getInputStream();
                byte[] fileByte = FileManagerUtil.getContentFromInputStream(in);
                InputStream inReadOver = new ByteArrayInputStream(fileByte);

                InputStream is = (InputStream) file.get("inputStream");
                String attachguid = UUID.randomUUID().toString();
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setAttachGuid(attachguid);
                frameAttachInfo.setCliengGuid(auditSpIMaterial.getCliengguid());
                frameAttachInfo.setAttachFileName(attachfilename);
                frameAttachInfo.setContentType("application/octet-stream");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setCliengTag(auditSpIMaterial.getCliengguid().substring(0, 1));
                // 长度必填
                frameAttachInfo.setAttachLength(fileLength);
                iAttachService.addAttach(frameAttachInfo, inReadOver);
                // 修改材料状态
                String materialStatus = auditSpIMaterial.getStatus();
                if (StringUtil.isBlank(materialStatus)
                        || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == Integer.parseInt(materialStatus)) {
                    materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                }
                else if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer.parseInt(materialStatus)) {
                    materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                }
                auditSpIMaterial.setStatus(materialStatus);
                auditSpIMaterialService.updateSpIMaterial(auditSpIMaterial);
                log.info(
                        "=============================测绘下载==============================================================");
                array.remove(0);
            }
            addCallbackParam("attachlist", array);
        }
        catch (Exception e) {
            addCallbackParam("msg", "文件下载失败，" + e.getMessage());
            log.error("====================测绘下载异常=================", e);
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * [多评下载功能] [功能详细描述]
     *
     * @param params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void uploadAttach_dpcl(String params) {
        try {
            JSONArray array = JSONArray.parseArray(params);
            String cliengGuid = array.getJSONObject(0).getString("cliengguid");
            String attachurl = array.getJSONObject(0).getString("attachurl");
            String attachname = array.getJSONObject(0).getString("attachname");
            String subappguid = array.getJSONObject(0).getString("subappguid");

            if (StringUtil.isNotBlank(attachurl)) {
                JSONObject file = ProjectConstant.filePathToInputStream(attachurl);
                InputStream is = (InputStream) file.get("inputStream");
                String attachguid = UUID.randomUUID().toString();
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setAttachGuid(attachguid);
                frameAttachInfo.setCliengGuid(cliengGuid);
                frameAttachInfo.setAttachFileName(attachname);
                frameAttachInfo.setContentType("application/octet-stream");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setCliengTag(cliengGuid.substring(0, 1));
                // 长度必填
                frameAttachInfo.setAttachLength(file.getLong("length"));
                iAttachService.addAttach(frameAttachInfo, is);

                // 修改材料状态
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("subappguid", subappguid);
                sqlConditionUtil.eq("cliengguid", cliengGuid);
                List<AuditSpIMaterial> materialList = auditSpIMaterialService
                        .getSpIMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                if (materialList != null && materialList.size() > 0) {
                    for (AuditSpIMaterial auditSpIMaterial : materialList) {
                        String materialStatus = auditSpIMaterial.getStatus();
                        if (StringUtil.isBlank(materialStatus)
                                || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == Integer.parseInt(materialStatus)) {
                            materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                        }
                        else if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer.parseInt(materialStatus)) {
                            materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                        }
                        auditSpIMaterial.setStatus(materialStatus);
                        auditSpIMaterialService.updateSpIMaterial(auditSpIMaterial);
                    }
                }
                array.remove(0);
            }
            addCallbackParam("attachlist", array);
        }
        catch (Exception e) {
            addCallbackParam("msg", "文件下载失败，" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void totalMaterialinit(Boolean dp, Boolean ch, Boolean qt) {
        List<AuditSpIMaterial> listMaterial = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid)
                .getResult();
        List<String> chclList = new ArrayList<String>();
        List<String> dpclList = new ArrayList<String>();
        List<String> qtclList = new ArrayList<String>();
        for (AuditSpIMaterial auditSpIMaterial : listMaterial) {
            String materialType = auditSpIMaterial.getMaterialtype();
            if (StringUtil.isNotBlank(materialType) && materialType.length() > 4
                    && (String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER).equals(auditSpIMaterial.getStatus())
                            || String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT)
                                    .equals(auditSpIMaterial.getStatus()))) {
                // 给所有材料分类。确认各种材料是否都存在
                if (ProjectConstant.MATERIAL_TYPE_DPCL.equals(materialType.substring(0, 4))) {
                    if (!dpclList.contains(materialType.substring(4))) {
                        dpclList.add(materialType.substring(4));
                    }
                }
                else if (ProjectConstant.MATERIAL_TYPE_CHCL.equals(materialType.substring(0, 4))) {
                    if (!chclList.contains(materialType.substring(4))) {
                        chclList.add(materialType.substring(4));
                    }
                }
                else if (ProjectConstant.MATERIAL_TYPE_QTCL.equals(materialType.substring(0, 4))) {
                    if (!qtclList.contains(materialType.substring(4))) {
                        qtclList.add(materialType.substring(4));
                    }
                }
            }
        }

        if (dpclList != null && dpclList.size() > 0 && dp) {
            String dpurl = configservice.getFrameConfigValue("AS_BLSP_DPURL");
            if (StringUtil.isBlank(dpurl)) {
                addCallbackParam("dpmsg", "未配置多评地址！");
            }
            String chclass = ConfigUtil.getConfigValue("dpclass");
            AuditBlspMaterialService auditblspmaterialservice = new AuditBlspMaterialService(chclass);
            String msg = auditblspmaterialservice.totalMaterialinit(dataBean.getRowguid(), subappGuid, dpurl, dpclList,
                    ProjectConstant.MATERIAL_TYPE_DPCL);
            try {
                addCallbackParam("dpattachlist", JSONArray.parseArray(msg));
            }
            catch (Exception e) {
                addCallbackParam("dpmsg", msg);
            }
        }

        if (chclList != null && chclList.size() > 0 && ch) {
            String churl = configservice.getFrameConfigValue("AS_BLSP_CHURL");
            if (StringUtil.isBlank(churl)) {
                addCallbackParam("chmsg", "未配置测绘地址！");
            }
            String chclass = ConfigUtil.getConfigValue("chclass");
            AuditBlspMaterialService auditblspmaterialservice = new AuditBlspMaterialService(chclass);
            String msg = auditblspmaterialservice.totalMaterialinit(dataBean.getRowguid(), subappGuid, churl, chclList,
                    ProjectConstant.MATERIAL_TYPE_CHCL);
            try {
                addCallbackParam("chattachlist", JSONArray.parseArray(msg));
            }
            catch (Exception e) {
                addCallbackParam("chmsg", msg);
            }
        }

        if (qtclList != null && qtclList.size() > 0 && qt) {
            String qturl = configservice.getFrameConfigValue("AS_BLSP_QTURL");
            if (StringUtil.isBlank(qturl)) {
                addCallbackParam("qtmsg", "未配置多评地址！");
                return;
            }
            String chclass = ConfigUtil.getConfigValue("qtclass");
            AuditBlspMaterialService auditblspmaterialservice = new AuditBlspMaterialService(chclass);
            String msg = auditblspmaterialservice.totalMaterialinit(dataBean.getRowguid(), subappGuid, qturl, qtclList,
                    ProjectConstant.MATERIAL_TYPE_QTCL);
            try {
                addCallbackParam("qtattachlist", JSONArray.parseArray(msg));
            }
            catch (Exception e) {
                addCallbackParam("qtmsg", msg);
            }
        }
    }

    public static void toFile(String base64Code, String targetPath) throws Exception {
        Files.write(Paths.get(targetPath), Base64.getDecoder().decode(base64Code), StandardOpenOption.CREATE);
    }

    public static String encryptToBase64(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void paperOperate(Map<String, String> dataMaterials) {

        Map<String, String> params = getRequestParametersMap();
        String param = params.get("params");
        JSONArray jsonParam = JSONObject.parseArray(param);
        jsonParam.forEach(jsonPram -> {
            Map<String, Object> map = JsonUtil.jsonToMap(jsonPram.toString());
            String operateType = String.valueOf(map.get("operateType"));
            String materialInstanceGuid = String.valueOf(map.get("materialInstanceGuid"));
            if ("submit".equals(operateType)) {
                handleSPIMaterialService.updateMaterialStatus(materialInstanceGuid, 5);
            }
            else {
                handleSPIMaterialService.updateMaterialStatus(materialInstanceGuid, -5);
            }
        });

        // 处理返回数据
        Record rtnInfo = new Record();
        rtnInfo.put("customer", "success");
        sendRespose(JsonUtil.objectToJson(rtnInfo));
    }

    public void searchCert(String isbz, String selectids) {
        String[] taskguid = null;
        if (selectids != null) {
            taskguid = selectids.split(";");
        }

        String areacode = "";
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isbz)) {
            listMaterial = auditSpIMaterialService.getSpIPatchMaterialBySubappGuid(subappGuid).getResult();
        }
        else {
            listMaterial = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid).getResult();
            // 更具事项过滤材料
            if (taskguid != null && taskguid.length > 0) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.in("taskguid", StringUtil.joinSql(taskguid));
                List<AuditTaskMaterial> listm = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap())
                        .getResult();
                List<String> materilids = new ArrayList<>();
                for (AuditTaskMaterial auditTaskMaterial : listm) {
                    materilids.add(auditTaskMaterial.getMaterialid());
                }
                List<String> spimaterialids = auditSpIMaterialService
                        .getSpIMaterialsByIDS(businessGuid, subappGuid, materilids).getResult();
                listMaterial = listMaterial.stream()
                        .filter(auditspimaterial -> spimaterialids.contains(auditspimaterial.getRowguid()))
                        .collect(Collectors.toList());
            }
            else {
                listMaterial = new ArrayList<>();
            }
        }

        // 关联共享材料
        String flag;
        if (ZwfwConstant.CONSTANT_STR_TWO.equals(businessType)) {
            flag = handleSPIMaterialService
                    .is_ExistSPCert(listMaterial, ownerGuid, integratedCompany.getZzjgdmz(), areacode).getResult();
        }
        else {
            flag = handleSPIMaterialService
                    .is_ExistSPCert(listMaterial, ownerGuid, dataBean.getItemlegalcertnum(), areacode).getResult();
        }
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", ZwfwConstant.CERTOWNERTYPE_FR);
        if (ZwfwConstant.CONSTANT_STR_TWO.equals(businessType)) {
            addCallbackParam("certnum", integratedCompany.getZzjgdmz());
        }
        else {
            addCallbackParam("certnum", dataBean.getItemlegalcertnum());
        }
        addCallbackParam("subappGuid", subappGuid);
    }

    public AuditRsItemBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public AuditSpIReview getDataReview() {
        if (dataReview == null) {
            dataReview = new AuditSpIReview();
        }
        return dataReview;
    }

    public void setDataReview(AuditSpIReview dataReview) {
        this.dataReview = dataReview;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemTypeModel() {
        if (itemTypeModel == null) {
            itemTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目类型", null, false));
        }
        return itemTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getConstructionPropertyModel() {
        if (constructionPropertyModel == null) {
            constructionPropertyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "建设性质", null, false));
        }
        return constructionPropertyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemLegalCertTypeModel() {
        if (itemLegalCertTypeModel == null) {
            itemLegalCertTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
            // 去除身份证
            itemLegalCertTypeModel.removeIf(a -> ZwfwConstant.CERT_TYPE_SFZ.equals(a.getValue()));
        }
        return itemLegalCertTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBelongtIndustryModel() {
        if (belongtIndustryModel == null) {
            belongtIndustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属行业", null, false));
        }
        return belongtIndustryModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getLegalPropertyModel() {
        if (legalPropertyModel == null) {
            legalPropertyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "法人性质", null, false));
        }
        return legalPropertyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFundSourcesModel() {
        if (fundSourcesModel == null) {
            fundSourcesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "资金来源", null, false));
        }
        return fundSourcesModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFinancialResourcesModel() {
        if (financialResourcesModel == null) {
            financialResourcesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "财政资金来源", null, false));
        }
        return financialResourcesModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getQuantifyConstructTypeModel() {
        if (quantifyConstructTypeModel == null) {
            quantifyConstructTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "量化建设规模的类别", null, false));
        }
        return quantifyConstructTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsImprovementModel() {
        if (isImprovementModel == null) {
            isImprovementModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return isImprovementModel;
    }

    public List<SelectItem> getOuListModel() {
        if (OuListModel == null) {
            OuListModel = new ArrayList<SelectItem>();

            // 这里进行绑定部门列表
            List<String> listOu = handleSpBusinessService.getPhaseRelationOus(businessGuid, phaseGuid).getResult();
            for (String ou : listOu) {
                String ouName = ouService.getOuByOuGuid(ou).getOuname();
                OuListModel.add(new SelectItem(ou, ouName));
            }
        }
        return OuListModel;
    }

    /**
     * 更新办件，把事项征求的表单信息录入到办件中
     *
     * @param
     *//*
        * public void updateProject(String projectGuid){ AuditProject project = new
        * AuditProject(); AuditRsItemBaseinfo result =
        * iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(yewuguid). getResult();
        * project.setRowguid(projectGuid);
        * project.setCertnum(result.getItemlegalcertnum());
        * project.setApplyername(result.getItemlegaldept());
        * project.setAddress(result.getConstructionsite());
        * project.setContactperson(result.getContractperson());
        * project.setContactmobile(result.getContractphone());
        * project.setContactemail(result.getContractemail());
        * project.setRemark(result.getConstructionscaleanddesc());
        * auditProjectService.updateProject(project); }
        */
    public void saveTs() {
        // 发送mq处理调用图审接口，处理数据
        String msg = subappGuid + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + dataBean.getItemcode();
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.ts." + businessGuid);
        // 更新阶段标识
        auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_TSZ, null);
        addCallbackParam("msg", "送交图审完成，等待处理中！");
    }

    public void saveLs() {
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
        if (LHSP_Status_TSZ.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "已送交联审，请不要重复点击");
            return;
        }
        else if (LHSP_Status_TSWC.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "联审已完成，请不要重复点击");
            return;
        }

        // 发送mq处理调用联审接口，处理数据
        String msg = subappGuid + "_SPLIT_" + ZwfwUserSession.getInstance().getAreaCode() + "_SPLIT_"
                + dataBean.getItemcode() + "_SPLIT_" + WebUtil.getRequestCompleteUrl(request);
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.ls." + businessGuid);
        // 更新阶段标识
        auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_TSZ, null);
        addCallbackParam("msg", "送交联审完成，等待处理中！");
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid)
                            .getResult();
                    /*
                     * //如果是子申报为材料预审中 if(ZwfwConstant.LHSP_Status_DYS.equals(status)){ auditSpITasks
                     * = auditSpITasks.stream().filter(a->ZwfwConstant.
                     * CONSTANT_STR_ONE.equals(a.getStatus())).collect( Collectors.toList()); }
                     */
                    List<String> taskids = iauditspbasetaskr.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
                    List<AuditSpITask> auditSpITasksdel = new ArrayList<>();
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        // 如果该事项在并行阶段，则事项为副线事项
                        AuditTask audittask = iaudittask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false)
                                .getResult();
                        if (audittask != null) {
                            String task_id = audittask.getTask_id();
                            if (taskids.contains(task_id)) {
                                auditSpITasksdel.add(auditSpITask);
                            }
                            else {
                                auditSpITask.put("ztsxlx", "1");
                            }
                        }
                        dealSpitask(auditSpITask);
                        /* 3.0新增逻辑 */
                        // 查询此事项有没有配置单位类型,有的话查出来进行关联
                        String auditbasetaskguid = taskCommonService
                                .getCropInfo(auditSpITask.getTaskguid(), subappGuid, auditSpITask.getPhaseguid())
                                .getResult();
                        if (StringUtil.isNotBlank(auditbasetaskguid)) {
                            // 查询此基础事项偶没有配置单位类型
                            String cropType = taskCommonService.getCropName(auditbasetaskguid).getResult();
                            if (StringUtil.isNotBlank(cropType)) {
                                String corpName = codeItemsService.getItemTextByCodeName("关联单位类型", cropType);
                                auditSpITask.set("cropname", corpName);
                            }
                        }
                        String affiliatedUnit = iAuditSpITaskCorpService.getCorpnamesBySubappguidAndTaskguid(subappGuid,
                                auditSpITask.getTaskguid());
                        auditSpITask.set("affiliatedunit", affiliatedUnit);
                        /* 3.0新增逻辑结束 */
                    }
                    // 已办理的事项，前台不能办理
                    auditSpITasks.removeAll(auditSpITasksdel);
                    this.setRowCount(auditSpITasks.size());
                    return auditSpITasks;
                }
            };
        }
        return modelTask;
    }

    public DataGridModel<AuditSpITask> fgetDataGridTask() {
        // 获得表格对象
        if (fmodelTask == null) {
            fmodelTask = new DataGridModel<AuditSpITask>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid)
                            .getResult();
                    /*
                     * //如果是子申报为材料预审中 if(ZwfwConstant.LHSP_Status_DYS.equals(status)){ auditSpITasks
                     * = auditSpITasks.stream().filter(a->ZwfwConstant.
                     * CONSTANT_STR_ONE.equals(a.getStatus())).collect( Collectors.toList()); }
                     */
                    List<String> taskids = iauditspbasetaskr.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
                    List<AuditSpITask> auditSpITasksdel = new ArrayList<>();
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        // 如果该事项在并行阶段，则事项为副线事项
                        AuditTask audittask = iaudittask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false)
                                .getResult();
                        if (audittask != null) {
                            String task_id = audittask.getTask_id();
                            if (taskids.contains(task_id)) {
                                auditSpITask.put("ztsxlx", "0");
                            }
                            else {
                                auditSpITasksdel.add(auditSpITask);
                            }
                        }
                        dealSpitask(auditSpITask);
                        /* 3.0新增逻辑 */
                        // 查询此事项有没有配置单位类型,有的话查出来进行关联
                        String auditbasetaskguid = taskCommonService
                                .getCropInfo(auditSpITask.getTaskguid(), subappGuid, auditSpITask.getPhaseguid())
                                .getResult();
                        if (StringUtil.isNotBlank(auditbasetaskguid)) {
                            // 查询此基础事项偶没有配置单位类型
                            String cropType = taskCommonService.getCropName(auditbasetaskguid).getResult();
                            if (StringUtil.isNotBlank(cropType)) {
                                String corpName = codeItemsService.getItemTextByCodeName("关联单位类型", cropType);
                                auditSpITask.set("cropname", corpName);
                            }
                        }
                        /* 3.0新增逻辑结束 */
                    }
                    // 已办理的事项，前台不能办理
                    auditSpITasks.removeAll(auditSpITasksdel);
                    this.setRowCount(auditSpITasks.size());
                    return auditSpITasks;
                }
            };
        }
        return fmodelTask;
    }

    public void dealSpitask(AuditSpITask auditSpITask) {
        if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
            AuditProject auditproject = auditProjectService
                    .getAuditProjectByRowGuid(auditSpITask.getProjectguid(), null).getResult();
            if (auditproject != null) {
                auditSpITask.set("status",
                        codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(auditproject.getStatus())));
            }
            else {
                auditSpITask.set("status", "未初始化");
            }
        }
        else {
            auditSpITask.set("status", "未初始化");
        }

        // 如果有有表单设置表单
        AuditTaskExtension audittaskextension = iaudittaskextension
                .getTaskExtensionByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
        if (audittaskextension != null && StringUtil.isNotBlank(audittaskextension.getStr("formid"))) {
            auditSpITask.put("formid", audittaskextension.getStr("formid"));
        }
    }

    public String getFlowsn(String taskguid) {
        // 来源（外网还是其他系统）
        String resource = "1";
        // 获取事项ID
        AuditTask task = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
        String unid = zhenggaiImpl.getunidbyTaskid(task.getTask_id());
        String item_Id = unid;
        String receiveNum = "";

        // 请求接口获取受理编码
        if (StringUtil.isNotBlank(unid)) {
            String result = FlowsnUtil.createReceiveNum(unid, task.getRowguid());
            if (!"error".equals(result)) {
                log.info("========================>获取受理编码成功！" + result);
                receiveNum = result;

            }
            else {
                log.info("========================>获取受理编码失败！");
            }
        }

        /*
         * // 构造获取受理编码的请求入参 String receiveNum=""; String params2Get = "?itemId=" +
         * item_Id + "&applyFrom=" + resource; // 请求接口获取受理编码 try { JSONObject jsonObj =
         * WavePushInterfaceUtils.createReceiveNum(params2Get,task.getShenpilb() );
         *
         * if (jsonObj != null && "200".equals(jsonObj.getString("state"))) {
         * //system.out.println("========================>获取受理编码成功！" +
         * jsonObj.getString("receiveNum") + "#####" + jsonObj.getString("password"));
         * receiveNum=jsonObj.getString("receiveNum");
         *
         * }else{ //system.out.println("========================>获取受理编码失败！");
         *
         * } } catch (IOException e) {
         * //system.out.println("接口请求报错！========================>" + e.getMessage());
         * e.printStackTrace(); }
         */
        return receiveNum;
    }

    public String showblue() {
        String xmzb = dataBean.getStr("xmzb");
        String str = "";
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        params.put("content", xmzb);
        String response = HttpUtil.doPost("http://59.206.96.142:8080/jnmapservice/api/ThirdParty/InsertContent", params,
                headers);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String ReturnCode = jsonObject.getString("ReturnCode");
        if ("0".equals(ReturnCode)) {
            str = "success";
        }
        else {
            str = "error";
        }
        return str;
    }

    public void checkAddtask() {
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
        // 获取副线事项阶段
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("businedssguid", auditSpISubapp.getBusinessguid());
        sqlc.eq("phaseid", "5");
        List<AuditSpPhase> phaselist = auditSpPhaseService.getAuditSpPhase(sqlc.getMap()).getResult();
        if (phaselist != null && !phaselist.isEmpty()) {
            List<AuditSpTask> listtask = iauditsptask
                    .getIntersectionByPhaseguid(auditSpISubapp.getPhaseguid(), phaselist.get(0).getRowguid())
                    .getResult();
            // 存在辅线事项
            if (listtask != null && !listtask.isEmpty()) {
                return;
            }
        }
        // 不存在辅线事项，判断是不是所有事项都已办理
        List<AuditSpBasetask> listsptask = iauditsptask.selectAuditSpBaseTasksByPhaseguid(auditSpISubapp.getPhaseguid())
                .getResult();
        List<String> list = new ArrayList<>();
        for (AuditSpBasetask auditSpBasetask : listsptask) {
            list.add(auditSpBasetask.getRowguid());
        }
        sqlc.clear();
        if (StringUtil.isNotBlank(StringUtil.joinSql(list))) {
            sqlc.in("basetaskguid", StringUtil.joinSql(list));
        }
        else {
            sqlc.in("basetaskguid", "''");
        }
        // 查询已经求的事项
        List<AuditSpITask> spitasklist = auditSpITaskService.getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid())
                .getResult();
        List<String> taskids = new ArrayList<>();
        // 过滤之前选中的事项，一个事项只能选择一次
        for (AuditSpITask auditspitask : spitasklist) {
            AuditTask audittask = iaudittask.selectTaskByRowGuid(auditspitask.getTaskguid()).getResult();
            // 辅线事项不添加
            if (audittask != null) {
                taskids.add(audittask.getTask_id());
            }
        }
        if (!taskids.isEmpty()) {
            sqlc.notin("taskid", StringUtil.joinSql(taskids));
        }
        List<AuditSpBasetaskR> listsptaskr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap())
                .getResult();
        List<String> sjcode = iauditorgaarea.getAllAreacodeByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                .getResult();
        listsptaskr = listsptaskr.stream().filter(a -> sjcode.contains(a.getAreacode())).collect(Collectors.toList());
        // 如果没有可办理的则不能办理
        if (listsptaskr == null || listsptaskr.isEmpty()) {
            addCallbackParam("msg", "阶段所有事项已征求，不用在添加事项！");
        }
    }

    public void getXfCommonData() throws FileNotFoundException, IOException {
        AuditSpInstance auditspinstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (auditspinstance == null) {
            throw new RuntimeException("未查询到申报数据!");
        }
        AuditRsItemBaseinfo auditrsitembaseinfo = iAuditRsItemBaseinfo
                .getAuditRsItemBaseinfoByRowguid(auditspinstance.getYewuguid()).getResult();
        Record phaseform = null;
        switch (AuditSpPhase.getPhaseId()) {
            case ZwfwConstant.CONSTANT_STR_ONE:
                phaseform = iauditspsplxydghxkservice.findAuditSpSpLxydghxkBysubappguid(subappGuid);
                break;
            case ZwfwConstant.CONSTANT_STR_TWO:
                phaseform = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappGuid);
                break;
            case ZwfwConstant.CONSTANT_STR_THREE:
                phaseform = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
                break;
            case "4":
                phaseform = iauditspspjgysservice.findAuditSpSpJgysBySubappGuid(subappGuid);
                break;
            default:
                break;
        }
        if (phaseform != null) {
            auditrsitembaseinfo.putAll(phaseform);
        }
        // 查询字表，重新设置下
        JnBeanUtil jnbeanutil = new JnBeanUtil("xf.properties");
        jnbeanutil.dataPut(auditrsitembaseinfo);

        // 查询建设单位
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("corptype", "31");
        sql.eq("itemguid", auditspinstance.getYewuguid());
        List<ParticipantsInfo> jsinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
        if (!EpointCollectionUtils.isEmpty(jsinfos)) {
            jnbeanutil.dataPut(jsinfos.get(0));
        }
        Record commondata = jnbeanutil.coverBean(new Record(), true);
        addCallbackParam("commondata", commondata);
    }

    /**
     * 供水-流程提交检查接口
     *
     * @param flow
     *            办件流水号
     */
    public JSONObject checkSubmitFlow(String flow) {
        try {
            log.info("========== 开始调用流程提交检查接口 ==========");
            JSONObject result = new JSONObject();
            result.put("isSuccess", false);
            String apiUrl = configservice.getFrameConfigValue("checkSubmitFlowUrl");
            if (StringUtil.isBlank(apiUrl)) {
                result.put("msg", "未获取到提交流程检查接口");
                return result;
            }
            log.info("流程提交检查接口地址--->" + apiUrl);
            log.info("请求参数--->" + "?flowcode=" + flow);
            String resultStr = HttpUtil.doPost(apiUrl, null, null);
            log.info("接口返回值--->" + resultStr);
            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                result.put("msg", "未获取到接口返回值");
                result.put("code", "-1");
                return result;
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("200".equals(resultJson.getString("code"))) {
                log.info("请求成功");
                result.put("isSuccess", true);
            }
            else {
                log.info("请求失败");
                result.put("code", "0");
                result.put("msg", resultJson.getString("msg"));
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("调用流程检查接口出现异常");
            JSONObject result = new JSONObject();
            result.put("isSuccess", false);
            result.put("code", "-1");
            result.put("msg", "调用流程检查接口出现异常");
            return result;
        }
    }

    /**
     * 供水-发起流程接口
     *
     * @param auditProject
     *            办件信息
     */
    public String startProcess(AuditProject auditProject) {
        try {
            log.info("========== 开始调用发起流程接口 ==========");
            String apiUrl = configservice.getFrameConfigValue("submitUrl");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到发起流程接口地址");
                return "";
            }

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("clientid", "JNGG");
            param.put("key", "JNGG");
            param.put("appuserno", "10001");
            param.put("flowcode", auditProject.getFlowsn());
            param.put("bustype", "1");
            param.put("cardname", auditProject.getApplyername());
            param.put("address", auditProject.getAddress());
            param.put("linkman", auditProject.getContactperson());
            param.put("tel", auditProject.getContactphone());
            log.info("发起流程接口地址--->" + apiUrl);
            log.info("请求头--->" + header);
            log.info("请求参数--->" + param);
            String resultStr = HttpUtil.doPost(apiUrl, param, header);
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return "";
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if (resultJson.getBoolean("success")) {
                // 如果成功，获取单据编号进行保存
                String billNo = resultJson.getString("msg").split(",")[0];
                log.info("办件" + auditProject.getFlowsn() + "对应的单据编号为：" + billNo);
                auditProject.set("billno", billNo);
                auditProjectService.updateProject(auditProject); // 将单据编号保存到办件表中
                log.info("单据编号已保存");
                return billNo;
            }
            else {
                log.info("请求失败，" + resultJson.getString("msg"));
                return "";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("调用发起流程接口出现异常");
            return "";
        }
    }

    /**
     * [接收办电预申请]
     */
    public String jsbdysq(String subAppGuid, AuditProject auditProject) {
        try {
            log.info("=======开始请求jsbdysq");
            log.info("subAppGuid--->" + subAppGuid);

            // 设置供电报装办件信息subappGuid、preApp
            // 在审批中判断是否是供电报装的办件，是的话设置办件subappGuid，并把办件guid设置到供电报装办件的projectguid

            log.info("查询供电报装的电子表单数据，参数subAppGuid--->" + subAppGuid);
            // 根据参数获取电子表单业务数据，供电报装表名：formtable20231108162746
            Record formDetail = cxBusService.getDzbdDetail("formtable20231108162746", subAppGuid);
            log.info("查询结果--->" + formDetail);
            // 将表单数据放入params
            JSONObject parms = new JSONObject();
            // 预申请编号（接口文档中37408002已被使用，修改为37408003）
            String preAppNo = "DL37408003"
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT)
                    + (int) (Math.random() * 90 + 10);
            parms.put("preAppNo", preAppNo);
            // 服务渠道，默认64
            parms.put("channelNo", "64");
            // 省proCode，默认370000
            parms.put("proCode", "370000");
            // 市编码，默认370800
            parms.put("cityCode", "370800");
            // 区县编码
            parms.put("countyCode", formDetail.getStr("qx"));
            // 街道
            parms.put("streetCode", formDetail.getStr("jd"));
            // 用电地址（详细地址）
            parms.put("elecAddr", formDetail.getStr("xxdz"));
            // 报装意向基本事项标识
            List<AuditSpITask> result = auditSpITaskService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            String taskGuid = "";
            for (AuditSpITask auditSpITask : result) {
                if (auditSpITask.getTaskname().contains("电")) {
                    taskGuid = auditSpITask.getTaskguid();
                    break;
                }
            }
            parms.put("meaningtaskguid", taskGuid);
            // 业务类型
            // parms.put("busiTypeCode", "0102");
            parms.put("busiTypeCode", formDetail.getStr("yewlx"));
            // 业务子类
            // parms.put("reduceCapMode", "010202");
            parms.put("reduceCapMode", formDetail.getStr("yewzl"));
            // 客户类型
            parms.put("custType", formDetail.getStr("kehlx"));
            // 用户编号
            parms.put("consNo", formDetail.getStr("yhbh"));
            // 供电单位
            parms.put("orgNo", formDetail.getStr("gddw"));
            // 用户名称
            parms.put("consName", formDetail.getStr("yhmc"));
            // 备注
            parms.put("remark", formDetail.getStr("bz"));
            // 是否个人充电桩*
            parms.put("ischcEqp", formDetail.getStr("sfgrcdz"));
            // 是否装变压器*
            parms.put("isInstTrans", formDetail.getStr("sfzbyq"));
            // 申请运行容量 capRunCap
            parms.put("capRunCap", formDetail.getStr("sqyxrl"));
            // 申请合同容量 contractCap
            parms.put("contractCap", formDetail.getStr("sqhtrl"));

            // 联系人
            JSONArray custContactLists = new JSONArray();
            JSONObject contact = new JSONObject();
            // 联系信息标识，数字类型主键，不超过16为即可
            contact.put("contactId", new Random().nextInt(900000000) + 100000000);
            // 联系人类型 写死为17
            contact.put("contactMode", "17");
            // 联系人姓名
            contact.put("contactName", formDetail.getStr("lxrxm"));
            // 移动电话
            contact.put("custPhone", formDetail.getStr("yddh"));
            custContactLists.add(contact);
            parms.put("custContactLists", custContactLists);

            // 附件下载地址，传第三方提供的默认值
            String powerEnv = configservice.getFrameConfigValue("powerEnv");
            String fileUrl = "";
            if ("0".equals(powerEnv)) {
                // 测试环境
                fileUrl = "http://27.40.64.214/inner/c000/f01";
            }
            else {
                // 生产环境
                fileUrl = "http://25.219.177.202/inner/c000/f01";
            }
            // 材料
            JSONArray appDataLists = new JSONArray();
            // 根据projectGuid从audit_project_material中查询对应数据（多条），根据每一条数据的cliengguid从frame_attachinfo中查出附件地址存入。
            log.info("根据projectGuid查询AuditProjectMaterial");
            List<AuditProjectMaterial> materialList = projectMaterialService
                    .selectProjectMaterial(auditProject.getRowguid()).getResult();
            log.info("查询出的materialList--->" + materialList);
            if (EpointCollectionUtils.isNotEmpty(materialList)) {
                log.info("材料list不为空");
                for (AuditProjectMaterial material : materialList) {
                    List<FrameAttachInfo> attachList = iAttachService.getAttachInfoListByGuid(material.getCliengguid());
                    log.info("查询出cliengguid为" + material.getCliengguid() + "对应的附件list--->" + attachList);
                    if (EpointCollectionUtils.isNotEmpty(attachList)) {
                        log.info("附件list不为空");
                        for (FrameAttachInfo frameAttachInfo : attachList) {
                            JSONObject attachInfo = new JSONObject();
                            // 附件序号采用附件上传的时间戳
                            attachInfo.put("sequenceId", String.valueOf(frameAttachInfo.getUploadDateTime().getTime()));
                            // 附件类型有两个，分别为营业执照（120110）和不动产权证（120111）
                            attachInfo.put("fileType", "营业执照".equals(material.getTaskmaterial()) ? "120110" : "120111");
                            // 联系人类型写死为17
                            attachInfo.put("contactMode", "17");
                            // 证件号码
                            attachInfo.put("certNo", auditProject.getCertnum());
                            // 文件名称
                            attachInfo.put("fileName", frameAttachInfo.getAttachFileName());
                            // 文件地址
                            attachInfo.put("fileUrl", fileUrl);
                            // 文件类型
                            attachInfo.put("fvNo", frameAttachInfo.getContentType().split("\\.")[1]);
                            // 附件接口上传的id
                            String attachDtlId = gdFileUpload(frameAttachInfo.getAttachGuid(), frameAttachInfo);
                            if (StringUtil.isNotBlank(attachDtlId)) {
                                log.info("获取到了attachDtlId，添加附件信息");
                                attachInfo.put("fileId", attachDtlId);
                                appDataLists.add(attachInfo);
                            }
                            else {
                                log.info("不添加附件信息");
                            }
                        }
                    }
                }
            }
            parms.put("appDataLists", appDataLists);

            JSONObject paramsJson = new JSONObject();
            paramsJson.put("serviceCode", "20001780");
            paramsJson.put("source", "010262");
            paramsJson.put("target", "37101");
            paramsJson.put("data", parms);
            log.info("接收办电预申请接口参数--->" + paramsJson);
            String returnParam = com.epoint.core.utils.httpclient.HttpUtil
                    .doPostJson(configService.getFrameConfigValue("bdysq_url"), paramsJson.toJSONString());
            log.info("接受办电预申请接口返回值--->" + returnParam);

            AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subAppGuid).getResult();
            log.info("获取当前auditSpISubapp实体--->" + auditSpISubapp);
            if (!ObjectUtils.isEmpty(auditSpISubapp)) {
                log.info("更新auditSpISubapp的preAppNo字段");
                auditSpISubapp.set("preAppNo", preAppNo);
                auditSpISubappService.updateAuditSpISubapp(auditSpISubapp);
                log.info("更新成功");
            }
            log.info("新增供电报装办件");
            AuditSpProjectinfoGdbz projectInfoGdbz = new AuditSpProjectinfoGdbz();
            projectInfoGdbz.setRowguid(UUID.randomUUID().toString());
            log.info("设置供电报装办件的subAppGuid--->" + subAppGuid);
            projectInfoGdbz.setSubAppGuid(subAppGuid);
            log.info("设置供电报装办件的preAppNo--->" + preAppNo);
            projectInfoGdbz.set("preAppNo", preAppNo);
            projectInfoGdbz.set("operateDate", new Date());
            auditSpProjectinfoGdbzService.insert(projectInfoGdbz);
            // log.info("更新办件状态为已受理(30)");
            // auditProject.setStatus(30);
            // auditProjectService.updateProject(auditProject);
            log.info("更新成功");
            log.info("=======结束请求jsbdysq");
            return "请求成功";
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======jsbdysq出现异常");
            return "请求失败";
        }
    }

    /**
     * 供电附件上传接口
     */
    public String gdFileUpload(String attachGuid, FrameAttachInfo attachInfo) {
        try {
            log.info("========== 开始调用供电附件上传接口 ==========");
            String apiUrl = configService.getFrameConfigValue("bdysq_url");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到供电附件上传接口");
                return null;
            }
            log.info("attachGuid--->" + attachGuid);
            // 根据附件attachGuid获取附件流
            InputStream content = iAttachService.getAttach(attachGuid).getContent();
            JSONObject params = new JSONObject();
            params.put("serviceCode", "20001002");
            params.put("source", "117005004");
            params.put("target", "37101");
            JSONObject data = new JSONObject();
            data.put("attachDtlCateg", "01");
            data.put("centerSrc", "flc");
            data.put("dispOpenFlag", "2");
            log.info("文件流转base64");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = content.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] bytes = outputStream.toByteArray();
            String fileBase64 = Base64.getEncoder().encodeToString(bytes);
            log.info("文件base64码串--->" + fileBase64);
            data.put("fileContent", fileBase64);
            data.put("fileName", attachInfo.getAttachFileName());
            data.put("uploadStf", StringUtil.isBlank(attachInfo.getUploadUserDisplayName()) ? "系统管理员"
                    : attachInfo.getUploadUserDisplayName());
            params.put("data", data);

            log.info("供电上传附件接口地址--->" + apiUrl);
            log.info("供电上传附件接口请求参数--->" + params);
            String resultStr = com.epoint.core.utils.httpclient.HttpUtil
                    .doPostJson(configService.getFrameConfigValue("bdysq_url"), params.toJSONString());
            log.info("接口返回值--->" + resultStr);

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("1".equals(resultJson.getString("code"))) {
                // 请求成功
                String attachDtlId = resultJson.getJSONObject("data").getJSONObject("data").getString("attachDtlId");
                log.info("附件id--->" + attachDtlId);
                return attachDtlId;
            }
            else {
                // 请求失败
                log.info("请求失败，原因：" + resultJson.getString("message"));
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用供电附件上传接口出现异常");
            return null;
        }
    }

    /**
     * 燃气身份证用户注册
     */
    public String rqsfzyhzc(AuditProject auditProject) {
        try {
            log.info("========== 开始调用燃气身份证用户注册接口 ==========");
            // String rqEnv = configservice.getFrameConfigValue("rqEnv");
            // if(StringUtil.isBlank(rqEnv)) {
            // log.info("未获取到燃气接口环境系统参数");
            // return "";
            // }
            // String rqUrlPrefixDev = configservice.getFrameConfigValue("rqUrlPrefixDev");
            // String rqUrlPrefixProd =
            // configservice.getFrameConfigValue("rqUrlPrefixProd");
            // if(StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd))
            // {
            // log.info("未获取到燃气接口地址前缀");
            // return "";
            // }
            // String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
            // String apiUrl = rqUrlPrefix + "/com/common/accountRegister?authVersion=v2";
            // log.info("燃气身份证用户注册接口地址--->" + apiUrl);
            // String idCard = auditProject.getCertnum();
            // JSONObject params = new JSONObject();
            // params.put("idCardNo", idCard);
            // String encryptParams =
            // Base64.getEncoder().encodeToString(params.toJSONString().getBytes(StandardCharsets.UTF_8));
            // JSONObject finalParams = new JSONObject();
            // finalParams.put("request", encryptParams);
            // log.info("燃气身份证用户注册接口请求参数明文--->" + params);
            // log.info("燃气身份证用户注册接口请求参数密文--->" + finalParams);
            // Map<String, String> headers = new HashMap<>();
            // // 获取请求头签名
            // String sign = generateRqHeader();
            // headers.put("param", sign);
            // log.info("燃气身份证用户注册接口请求头--->" + headers);
            // String resultStr = HttpUtil.doPostJson(apiUrl, finalParams.toJSONString(),
            // headers);
            // log.info("燃气身份证用户注册接口返回值--->" + resultStr);
            //
            // if(StringUtil.isBlank(resultStr)) {
            // log.info("未获取到接口返回值");
            // return "";
            // }
            // // 获取到返回值
            // JSONObject resultJson = JSON.parseObject(resultStr);
            // if("0".equals(resultJson.getString("errcode"))) {
            // // 请求成功
            // String accountId = resultJson.getJSONObject("data").getString("accountId");
            // log.info("获取到的accountId--->" + accountId);
            // log.info("将accountId存入audit_project的billNo字段");
            // auditProject.set("billno", accountId);
            // log.info("更新auditProject");
            // auditProjectService.updateProject(auditProject);
            // log.info("更新成功");
            // log.info("========== 结束调用燃气身份证用户注册接口 ==========");
            // return "请求成功";
            // }
            // else {
            // // 请求失败，打印错误信息
            // log.info("请求失败，错误信息：" + resultJson.getString("errormsg"));
            // return "";
            // }
            JSONObject params = new JSONObject();
            params.put("idCard", auditProject.getCertnum());
            params.put("projectGuid", auditProject.getRowguid());
            // 注册接口地址
            String apiUrl = configservice.getFrameConfigValue("rqzwdtzcUrl");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到网厅燃气注册接口地址");
                return "";
            }
            log.info("接口地址--->" + apiUrl);
            log.info("接口参数--->" + params.toJSONString());
            String resultStr = HttpUtil.doPostJson(apiUrl, params.toJSONString(), new HashMap<>());
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                return "";
            }
            else {
                return resultStr;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用网厅燃气注册接口出现异常");
            return "";
        }
    }

    /**
     * 燃气报装提交接口
     */
    public String rqbztj(String subAppGuid, String projectGuid) {
        try {
            log.info("========== 开始调用燃气报装提交接口 ==========");
            // String rqEnv = configservice.getFrameConfigValue("rqEnv");
            // if(StringUtil.isBlank(rqEnv)) {
            // log.info("未获取到燃气接口环境系统参数");
            // return "";
            // }
            // String rqUrlPrefixDev = configservice.getFrameConfigValue("rqUrlPrefixDev");
            // String rqUrlPrefixProd =
            // configservice.getFrameConfigValue("rqUrlPrefixProd");
            // if(StringUtil.isBlank(rqUrlPrefixDev) || StringUtil.isBlank(rqUrlPrefixProd))
            // {
            // log.info("未获取到燃气接口地址前缀");
            // return "";
            // }
            // String rqUrlPrefix = "0".equals(rqEnv) ? rqUrlPrefixDev : rqUrlPrefixProd;
            // String apiUrl = rqUrlPrefix + "/com/common/saveInstall?authVersion=v2";
            //
            // log.info("subAppGuid--->" + subAppGuid);
            // log.info("查询燃气报装的电子表单数据，参数subAppGuid--->" + subAppGuid);
            // // 根据参数获取电子表单业务数据，燃气报装表名：formtable20231123163828
            // Record formDetail = cxBusService.getDzbdDetail("formtable20231123163828",
            // subAppGuid);
            // log.info("查询结果--->" + formDetail);
            //
            // AuditProject auditProject =
            // auditProjectService.getAuditProjectByRowGuid(projectGuid, null).getResult();
            // log.info("查询到的办件--->" + auditProject);
            //
            // JSONObject params = new JSONObject();
            // // 城市行政区域编码
            // params.put("cityNo", "370800");
            // // 对接第三方名称
            // params.put("outsideSystemName", "济宁市一窗受理综合服务平台");
            // // 报装类型
            // params.put("installType", formDetail.getStr("bzlx"));
            // // 外部工单编号
            // params.put("serialId", projectGuid);
            // // 用户编号
            // params.put("consNo", "");
            // // 账户id
            // if(EpointCollectionUtils.isEmptyMap(auditProject)) {
            // log.info("未查询到办件，accountId设置为0");
            // params.put("accountId", "0");
            // }
            // else {
            // log.info("查询到办件，获取办件的accountId");
            // params.put("accountId", auditProject.getStr("billno"));
            // }
            // // 联系电话
            // params.put("phone", formDetail.getStr("lxdh"));
            // // 报装地址
            // params.put("address", formDetail.getStr("bzdz"));
            // // 报装说明
            // params.put("remark", formDetail.getStr("bzsm"));
            // // 附件 需要调用接口
            //
            // // 用气设备列表
            // JSONArray deviceList = new JSONArray();
            // // 目前电子表单就写死为一个用气设备
            // JSONObject device = new JSONObject();
            // // 设备名称
            // device.put("deviceName", formDetail.getStr("sbmc"));
            // // 型号
            // device.put("model", formDetail.getStr("xh"));
            // // 外形尺寸
            // device.put("size", formDetail.getStr("wxcc"));
            // // 单台额定用气量
            // device.put("normalGasRate", formDetail.getStr("dtedyql"));
            // // 气管接口管径
            // device.put("pipeDiameter", formDetail.getStr("qgjkgj"));
            // // 台数
            // device.put("quantity", formDetail.getStr("ts"));
            // // 额定热负荷
            // device.put("heatLoad", formDetail.getStr("edrfh"));
            // deviceList.add(device);
            // params.put("deviceList", deviceList);
            //
            // // 附件
            // List<String> fileIdList = new ArrayList<>();
            // List<String> fileTypeList = Arrays.asList(".pdf", ".png", ".jpg", ".jpeg",
            // ".bmp");
            // // 获取办件对应的材料附件
            // log.info("根据projectGuid查询AuditProjectMaterial");
            // List<AuditProjectMaterial> materialList =
            // projectMaterialService.selectProjectMaterial(auditProject.getRowguid()).getResult();
            // log.info("查询出的materialList--->" + materialList);
            // if(EpointCollectionUtils.isNotEmpty(materialList)) {
            // log.info("材料list不为空");
            // for (AuditProjectMaterial material : materialList) {
            // List<FrameAttachInfo> attachList =
            // iAttachService.getAttachInfoListByGuid(material.getCliengguid());
            // log.info("查询出cliengguid为" + material.getCliengguid() + "对应的附件list--->" +
            // attachList);
            // if(EpointCollectionUtils.isNotEmpty(attachList)) {
            // log.info("附件list不为空");
            // for (FrameAttachInfo frameAttachInfo : attachList) {
            // // 燃气上传附件接口只识别图片和pdf
            // if(fileTypeList.contains(frameAttachInfo.getContentType())) {
            // // 当符合类型才进行上传
            // log.info("附件：" + frameAttachInfo.getAttachFileName() + "符合类型，转base64");
            // content =
            // iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
            // outputStream = new ByteArrayOutputStream();
            // byte[] buffer = new byte[4096];
            // int bytesRead;
            // while ((bytesRead = content.read(buffer)) != -1) {
            // outputStream.write(buffer, 0, bytesRead);
            // }
            // byte[] bytes = outputStream.toByteArray();
            // String fileBase64 = Base64.getEncoder().encodeToString(bytes);
            // String fileName = frameAttachInfo.getAttachFileName();
            // String fileId = rqFileUpload(fileBase64, fileName);
            // if(StringUtil.isNotBlank(fileId)) {
            // log.info("上传成功，将fileId添加");
            // fileIdList.add(fileId);
            // }
            // }
            // else {
            // log.info("附件："+ frameAttachInfo.getAttachFileName() + "不符合类型，不进行上传");
            // }
            // }
            // }
            // }
            // }
            // params.put("fileIdList", fileIdList);
            //
            // String encryptParams =
            // Base64.getEncoder().encodeToString(params.toJSONString().getBytes(StandardCharsets.UTF_8));
            // JSONObject finalParams = new JSONObject();
            // finalParams.put("request", encryptParams);
            //
            // // 由于请求头签名有效期1分钟，所以在调用接口之前设置
            // Map<String, String> headers = new HashMap<>();
            // // 获取请求头签名
            // String sign = generateRqHeader();
            // headers.put("param", sign);
            // log.info("燃气报装提交接口地址--->" + apiUrl);
            // log.info("燃气报装提交接口请求头--->" + headers);
            // log.info("燃气报装提交接口请求参数明文--->" + params);
            // log.info("燃气报装提交接口请求参数密文--->" + finalParams);
            // String resultStr = HttpUtil.doPostJson(apiUrl, finalParams.toJSONString(),
            // headers);
            // log.info("接口返回值--->" + resultStr);
            //
            // JSONObject resultJson = JSON.parseObject(resultStr);
            // if("0".equals(resultJson.getString("errcode"))) {
            // log.info("请求成功");
            // return resultJson.getString("errmsg");
            // }
            // else {
            // log.info("请求失败，原因：" + resultJson.getString("errmsg"));
            // return "";
            // }
            JSONObject params = new JSONObject();
            params.put("subAppGuid", subAppGuid);
            params.put("projectGuid", projectGuid);
            // 注册接口地址
            String apiUrl = configservice.getFrameConfigValue("rqzwdttjUrl");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到网厅燃气提交接口地址");
                return "";
            }
            log.info("接口地址--->" + apiUrl);
            log.info("接口参数--->" + params.toJSONString());
            String resultStr = HttpUtil.doPostJson(apiUrl, params.toJSONString(), new HashMap<>());
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return "";
            }
            else {
                return resultStr;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用网厅燃气提交接口出现异常", e);
            return "";
        }
    }
}

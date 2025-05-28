package com.epoint.auditsp.auditsphandle.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
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
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.blsp.material.inter.IAuditBlspMaterial;
import com.epoint.common.util.ProjectConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespbusiness.inter.IHandleSpBusiness;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
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
import com.epoint.web.blsp.material.bizlogic.AuditBlspMaterialService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

@RestController("handlesubmitmaterialaction")
@Scope("request")
public class HandleSubmitMaterialAction extends BaseController {
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

    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

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
        } else if (LHSP_Status_TSWC.equals(auditSpISubapp.getStatus())) {
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

        if (dataBean != null) {
            addCallbackParam("xmdm", dataBean.getItemcode());
            addCallbackParam("itemguid", dataBean.getRowguid());
        }

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
    }

    /**
     * 保存并关闭
     */
    public void save(String isck) {
        List<AuditSpITask> listTask = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();

        //如果状态为已接件，则说明处理完成了
        if (ZwfwConstant.LHSP_Status_YSJ.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "该流程已处理完成！");
            return;
        }

        //判断是否第一次发件
        boolean firstsj = true;
        for (AuditSpITask auditspitask : listTask) {
            if (StringUtil.isNotBlank(auditspitask.getProjectguid())) {
                firstsj = false;
            }
        }
        List<String> taskguids = new ArrayList<>();
        //统一收件
        if (!ZwfwConstant.LHSP_Status_DYS.equals(status)) {
            if (getDataGridTask().getSelectKeys() != null) {
                taskguids.addAll(getDataGridTask().getSelectKeys());
            }
            if (fgetDataGridTask().getSelectKeys() != null) {
                taskguids.addAll(fgetDataGridTask().getSelectKeys());
            }
            listTask = listTask.stream().filter(spitask -> (taskguids.contains(spitask.getRowguid())) && StringUtil.isBlank(spitask.getProjectguid())).collect(Collectors.toList());
        } else {
            //预审逻辑
            listTask = listTask.stream().filter(a -> ZwfwConstant.CONSTANT_STR_ONE.equals(a.getStatus()) && StringUtil.isBlank(a.getProjectguid())).collect(Collectors.toList());
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        for (AuditSpITask task : listTask) {
            // 先生成办件，再初始化材料
            String projectGuid = handleProjectService.InitBLSPProject(task.getTaskguid(), "",
                    userSession.getDisplayName(), userSession.getUserGuid(), dataBean.getItemlegalcerttype(),
                    dataBean.getItemlegalcertnum(), "", "", ZwfwUserSession.getInstance().getCenterGuid(), biGuid,
                    subappGuid, businessGuid, dataBean.getItemlegalcreditcode(), dataBean.getItemlegaldept())
                    .getResult();
            auditSpITaskService.updateProjectGuid(task.getRowguid(), projectGuid);
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
                auditProjectService.updateProject(project);
            }
            sb.append(projectGuid).append("','");
            // 再进行材料初始化
            handleSPIMaterialService.initProjctMaterial(materiallist, businessGuid, subappGuid, task.getTaskguid(),
                    projectGuid);
        }
        sb.append("'");
        //办件初始化完成推送办件到住建系统
        projectSendzj(isck, sb.toString());
        listTask = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
        boolean flag = false;
        for (AuditSpITask auditspitask : listTask) {
            if (StringUtil.isBlank(auditspitask.getProjectguid())) {
                flag = true;
            }
        }
        //第二次分发办件不进行下面操作，内网状态
        if (LHSP_Status_SJZ.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "收件完成！");
            // 判断是否全部办理
            if (!flag) {
                auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
                afterYsj(isck);
            }
            return;
        }

        //第一次申报，如果全部办理，修改为已收件
        if (flag) {
            auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_SJZ, null);
            addCallbackParam("msg", "收件完成！");
        } else {
            auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
            afterYsj(isck);
        }

        //如果过第一次申报，插入计时数据记录
        if (firstsj) {
            String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
            if (StringUtil.isBlank(centerguid)) {
                //如果session里面没有centerguid，则获取辖区下第一个中心标识
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("belongxiaqu", ZwfwUserSession.getInstance().getAreaCode());
                List<AuditOrgaServiceCenter> listcenter = iauditorgaservicecenter.getAuditOrgaServiceCenterByCondition(sqlc.getMap()).getResult();
                if (listcenter.size() > 0) {
                    centerguid = listcenter.get(0).getRowguid();
                }
            }
            sparetimeService.addSpareTimeByProjectGuid(subappGuid, ZwfwConstant.CONSTANT_INT_ZERO, ZwfwUserSession.getInstance().getAreaCode(),
                    centerguid);
            //插入后暂停计时
            AuditProjectSparetime auditProjectSparetime = sparetimeService.getSparetimeByProjectGuid(subappGuid).getResult();
            auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
            sparetimeService.updateSpareTime(auditProjectSparetime);
        } else {
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
        //如果是第四阶段，需要和联合验收系统对接
        String tkurl = configservice.getFrameConfigValue("AS_BLSP_TKURL");
        if ("4".equals(AuditSpPhase.getPhaseId()) && StringUtil.isNotBlank(tkurl)) {
            String msg = subappGuid + "_SPLIT_" + ZwfwUserSession.getInstance().getAreaCode() + "_SPLIT_" + dataBean.getItemcode()
                    + "_SPLIT_" + WebUtil.getRequestCompleteUrl(request);
            sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.tk." + businessGuid);
        }
    }

    public void projectSendzj(String isck, String projectguids) {
        //发送mq消息推送数据
        String msg = subappGuid + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + dataBean.getItemcode();
        if ("false".equals(isck)) {
            msg += ".nck";
        } else {
            msg += ".isck";
        }
        //拼接办理人用户标识
        msg += "." + UserSession.getInstance().getUserGuid() + "." + projectguids;
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.subapp." + businessGuid);
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
            List<String> spimaterialids = auditSpIMaterialService.getSpIMaterialsByIDS(businessGuid, subappGuid, materilids).getResult();
            lists = lists.stream().filter(auditspimaterial -> spimaterialids.contains(auditspimaterial.getRowguid())).collect(Collectors.toList());
        } else {
            lists = new ArrayList<>();
        }


        //去除审核通过的材料
        if (lists != null && lists.size() > 0) {
            lists.removeIf(material -> {
                if (ZwfwConstant.CONSTANT_STR_THREE.equals(material.getIsbuzheng())) {
                    return true;
                } else {
                    return false;
                }
            });
        }
        //有审核不通过的材料,修改状态为预审材料待补正
        if (lists.size() > 0) {
            //查找所有审核不通过的数据，更新auditsp材料
            List<String> materialids = new ArrayList<>();
            //获取布阵的材料的id
            for (AuditSpIMaterial auditSpIMaterial : lists) {
                //是否共享材料
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getShared())) {
                    List<AuditSpShareMaterialRelation> listr = iauditspsharematerialrelation.selectByShareMaterialGuid(businessGuid, auditSpIMaterial.getMaterialguid()).getResult();
                    for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listr) {
                        materialids.add(auditSpShareMaterialRelation.getMaterialid());
                    }
                } else {
                    materialids.add(auditSpIMaterial.getMaterialguid());
                }
            }
            List<String> taskguids = iaudittaskmaterial.getTaskguidListByMaterialids(materialids).getResult();
            if (taskguids.size() > 0) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("subappguid", subappGuid);
                sqlc.eq("status", ZwfwConstant.CONSTANT_STR_ONE);
                sqlc.in("taskguid", StringUtil.joinSql(taskguids));
                List<AuditSpITask> spitasks = auditSpITaskService.getAuditSpITaskListByCondition(sqlc.getMap()).getResult();
                for (AuditSpITask auditSpITask : spitasks) {
                    auditSpITask.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    auditSpITaskService.updateAuditSpITask(auditSpITask);
                }
            }
            auditSpISubappService.updateSubapp(subappGuid, "34", null);
            //大厅发送的代办消息
            AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
            String username = userService.getUserNameByUserGuid(auditSpISubapp.getApplyerguid());
            if (auditSpISubapp != null && StringUtil.isBlank(username)) {
                String openUrl = configService.getFrameConfigValue("zwdtMsgurl") + "/epointzwmhwz/pages/approve/perioddetail?biguid=" + auditSpISubapp.getBiguid()
                        + "&phaseguid=" + auditSpISubapp.getPhaseguid() + "&businessguid=" + auditSpISubapp.getBusinessguid() + "&subappguid=" + auditSpISubapp.getRowguid()
                        + "&itemguid=" + dataBean.getRowguid() + "&type=watch";
                String title = "【材料审核完成】" + rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(yewuguid).getResult().getItemname();
                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title, IMessagesCenterService.MESSAGETYPE_WAIT, auditSpISubapp.getApplyerguid(), "", UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                        "", openUrl, UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1, null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
            }
        } else {
            save("false");
        }
        addCallbackParam("msg", "材料审核完成！");
        //处理完成删除所有待办，查询改辖区的所有统一收件人
        handleProjectService.delZwfwMessage("", ZwfwUserSession.getInstance().getAreaCode(), "统一收件人员", subappGuid);
    }

    public String modelAuditMaterial(String selectids) {
        if (StringUtil.isNotBlank(selectids)) {
            String[] rowguids = selectids.split(";");
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("rowguid", StringUtil.joinSql(rowguids));
            List<String> listguid = auditSpITaskService.getStringListByCondition("taskguid", sqlc.getMap()).getResult();
            selectids = StringUtil.join(listguid, ";");
        }
        //如果是外网申报，只展示已申报的材料
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
        } else {
            listMaterial = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid).getResult();
        }
        //更具事项过滤材料
        if (taskguid != null && taskguid.length > 0) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("taskguid", StringUtil.joinSql(taskguid));
            List<AuditTaskMaterial> listm = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap()).getResult();
            List<String> materilids = new ArrayList<>();
            for (AuditTaskMaterial auditTaskMaterial : listm) {
                materilids.add(auditTaskMaterial.getMaterialid());
            }
            List<String> spimaterialids = auditSpIMaterialService.getSpIMaterialsByIDS(businessGuid, subappGuid, materilids).getResult();

            List<AuditSpIMaterial> materials = new ArrayList<>();

//            log.info("listMaterial:" + listMaterial);
            if (listMaterial != null) {
                for (AuditSpIMaterial material : listMaterial) {
                    if (material != null) {
                        if (spimaterialids.contains(material.getRowguid())) {
                            materials.add(material);
                        }
                    }
                }
            }
            listMaterial = materials;
//            listMaterial= listMaterial.stream().filter(auditspimaterial -> auditspimaterial != null && spimaterialids.contains(auditspimaterial.getRowguid())).collect(Collectors.toList());


        } else {
            listMaterial = new ArrayList<>();
        }


        //        // 判断是否存在需要图审的材料
        //        String tsmaterial = configservice.getFrameConfigValue("AS_BLSP_TSMATERIAL");
        //        String[] tsmaterials = tsmaterial.split(";");
        //        //图审材料ids
        //        List<String> listtsmaterials = StringUtil.change2ArrayList(tsmaterials);
        //        AuditSpShareMaterialRelation r = null;
        //查找图审材料是是否存在共享材料
        //        if (listtsmaterials.size() > 0) {
        //            r = relationservice.getRelationByIDS(businessGuid, "'" + StringUtil.join(tsmaterials, "','") + "'")
        //                    .getResult();
        //        }

        // List<Record> projectMaterial =
        // handleMaterialService.getProjectMaterial(projectGuid).getResult();
        JSONObject jsonMaterials = new JSONObject();
        try {
            // 这里需要定义几个变量
            int submitedMaterialCount = 0;
            int shouldPaperCount = 0;
            int submitedPaperCount = 0;
            int submitedAttachCount = 0;
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
                        //判断此材料是不是测绘材料
                        //材料前面的标识
                        String materialtype = "";
                        if (StringUtil.isNotBlank(listMaterial.get(i).getMaterialtype())) {

                            //图审材料、测绘材料、多评材料。写死
                            //材料类型，4位为1层。第一层
                            String materialTypeMain = listMaterial.get(i).getMaterialtype().substring(0, 4);

                            if (ProjectConstant.MATERIAL_TYPE_TSCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("ists", ZwfwConstant.CONSTANT_STR_ONE);
                                    jsonMaterial.put("isbuzheng", ZwfwConstant.CONSTANT_INT_TWO);
                                }
                                materialtype = "<span style=\"color:red\">【图审】</span>";
                            } else if (ProjectConstant.MATERIAL_TYPE_CHCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("isch", ZwfwConstant.CONSTANT_STR_ONE);
                                }
                                jsonMaterials.put("ischmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                                materialtype = "<span style=\"color:red\">【测绘】</span>";
                            } else if (ProjectConstant.MATERIAL_TYPE_DPCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("isdp", ZwfwConstant.CONSTANT_STR_ONE);
                                }
                                jsonMaterials.put("isdpmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                                materialtype = "<span style=\"color:red\">【多评】</span>";
                            } else if (ProjectConstant.MATERIAL_TYPE_LSCL.equals(materialTypeMain)) {
                                if (materialstatus == 10) {
                                    jsonMaterials.put("isls", ZwfwConstant.CONSTANT_STR_ONE);
                                    jsonMaterial.put("isbuzheng", ZwfwConstant.CONSTANT_INT_TWO);
                                }
                                materialtype = "<span style=\"color:red\">【联审】</span>";
                            }

                            //                            //图审材料不存在存在共享材料,比较materialid
                            //                            if (listtsmaterials.contains(listMaterial.get(i).getMaterialguid())) {
                            //                            }
                            //                            //图审材料存在共享材料,比较sharematerialguid
                            //                            if (r != null) {
                            //                                if (r.getSharematerialguid().equals(listMaterial.get(i).getMaterialguid())) {
                            //                                    jsonMaterial.put("istsmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                            //                                    jsonMaterials.put("ists", ZwfwConstant.CONSTANT_STR_ONE);
                            //                                }
                            //                            }
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
                            } else {
                                is_rongque = "0";
                                buRongqueCount++;
                            }
                        } else {
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
                            } else if (materialstatus == 15) {
                                submitedPaperCount++;
                            } else {
                                submitedAttachCount++;
                                submitedPaperCount++;
                            }
                        }
                        // 材料的应提交情况
                        if (!"20".equals(submittype)) {
                        }
                        if (!"10".equals(submittype)) {
                            shouldPaperCount++;
                        }
                    }
                }
                //                if(hasch&&isPostback()){
                //                    materialinit();
                //                }
            }
            jsonMaterials.put("type", "submit");
            jsonMaterials.put("isAllPaper", shouldPaperCount > submitedPaperCount ? 0 : 1);
            //提交纸质为0应提交0时不显示勾选
            if (shouldPaperCount == 0 && submitedPaperCount == 0) {
                jsonMaterials.put("isAllPaper", 0);
            }
            jsonMaterials.put("data", materialList);
            jsonMaterials.put("materialSummary", "已标记" + submitedMaterialCount + "件材料，其中纸质" + submitedPaperCount
                    + "件，电子文档" + submitedAttachCount + "件；容缺" + rongqueCount + "件；不容缺" + buRongqueCount + "件。");

        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
        return jsonMaterials.toString();
    }

    public void materialinit() throws IOException {
        String msg = iauditblspmaterial.chMaterialinit(dataBean.getRowguid(), subappGuid);
        try {
            addCallbackParam("attachlist", JSONArray.parseArray(msg));
        } catch (Exception e) {
            addCallbackParam("msg", msg);
        }
    }

    public void materialinit_dpcl() {
        String msg = iauditblspmaterial.dpMaterialinit(dataBean.getRowguid(), subappGuid);
        try {
            addCallbackParam("attachlist", JSONArray.parseArray(msg));
        } catch (Exception e) {
            addCallbackParam("msg", msg);
        }
    }

    /**
     * [测绘下载功能]
     * [功能详细描述]
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
                //获取附件流
//                URL url1 = new URL("http://192.168.203.124:8080/szjslhchqy/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=0116224a-4a20-45a7-8641-4f8a0323fc0d");
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
                //长度必填
                frameAttachInfo.setAttachLength(fileLength);
                iAttachService.addAttach(frameAttachInfo, inReadOver);
                //修改材料状态
                String materialStatus = auditSpIMaterial.getStatus();
                if (StringUtil.isBlank(materialStatus)
                        || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == Integer.parseInt(materialStatus)) {
                    materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                } else if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer.parseInt(materialStatus)) {
                    materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                }
                auditSpIMaterial.setStatus(materialStatus);
                auditSpIMaterialService.updateSpIMaterial(auditSpIMaterial);
                log.info("=============================测绘下载==============================================================");
                array.remove(0);
            }
            addCallbackParam("attachlist", array);
        } catch (Exception e) {
            addCallbackParam("msg", "文件下载失败，" + e.getMessage());
            log.error("====================测绘下载异常=================", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * [多评下载功能]
     * [功能详细描述]
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
                //长度必填
                frameAttachInfo.setAttachLength(file.getLong("length"));
                iAttachService.addAttach(frameAttachInfo, is);

                //修改材料状态
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
                        } else if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer.parseInt(materialStatus)) {
                            materialStatus = String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                        }
                        auditSpIMaterial.setStatus(materialStatus);
                        auditSpIMaterialService.updateSpIMaterial(auditSpIMaterial);
                    }
                }
                array.remove(0);
            }
            addCallbackParam("attachlist", array);
        } catch (Exception e) {
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
                    && (String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER)
                    .equals(auditSpIMaterial.getStatus())
                    || String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT)
                    .equals(auditSpIMaterial.getStatus()))) {
                //给所有材料分类。确认各种材料是否都存在
                if (ProjectConstant.MATERIAL_TYPE_DPCL.equals(materialType.substring(0, 4))) {
                    if (!dpclList.contains(materialType.substring(4))) {
                        dpclList.add(materialType.substring(4));
                    }
                } else if (ProjectConstant.MATERIAL_TYPE_CHCL.equals(materialType.substring(0, 4))) {
                    if (!chclList.contains(materialType.substring(4))) {
                        chclList.add(materialType.substring(4));
                    }
                } else if (ProjectConstant.MATERIAL_TYPE_QTCL.equals(materialType.substring(0, 4))) {
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
            String msg = auditblspmaterialservice.totalMaterialinit(dataBean.getRowguid(), subappGuid, dpurl, dpclList, ProjectConstant.MATERIAL_TYPE_DPCL);
            try {
                addCallbackParam("dpattachlist", JSONArray.parseArray(msg));
            } catch (Exception e) {
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
            String msg = auditblspmaterialservice.totalMaterialinit(dataBean.getRowguid(), subappGuid, churl, chclList, ProjectConstant.MATERIAL_TYPE_CHCL);
            try {
                addCallbackParam("chattachlist", JSONArray.parseArray(msg));
            } catch (Exception e) {
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
            String msg = auditblspmaterialservice.totalMaterialinit(dataBean.getRowguid(), subappGuid, qturl, qtclList, ProjectConstant.MATERIAL_TYPE_QTCL);
            try {
                addCallbackParam("qtattachlist", JSONArray.parseArray(msg));
            } catch (Exception e) {
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
        } catch (IOException e) {
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
            } else {
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
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isbz)) {
            listMaterial = auditSpIMaterialService.getSpIPatchMaterialBySubappGuid(subappGuid).getResult();
        } else {
            listMaterial = auditSpIMaterialService.getSpIMaterialBySubappGuid(subappGuid).getResult();
            //更具事项过滤材料
            if (taskguid != null && taskguid.length > 0) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.in("taskguid", StringUtil.joinSql(taskguid));
                List<AuditTaskMaterial> listm = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap()).getResult();
                List<String> materilids = new ArrayList<>();
                for (AuditTaskMaterial auditTaskMaterial : listm) {
                    materilids.add(auditTaskMaterial.getMaterialid());
                }
                List<String> spimaterialids = auditSpIMaterialService.getSpIMaterialsByIDS(businessGuid, subappGuid, materilids).getResult();
                listMaterial = listMaterial.stream().filter(auditspimaterial -> spimaterialids.contains(auditspimaterial.getRowguid())).collect(Collectors.toList());
            } else {
                listMaterial = new ArrayList<>();
            }
        }


        // 关联共享材料
        String flag;
        if (ZwfwConstant.CONSTANT_STR_TWO.equals(businessType)) {
            flag = handleSPIMaterialService
                    .is_ExistSPCert(listMaterial, ownerGuid, integratedCompany.getZzjgdmz(), areacode).getResult();
        } else {
            flag = handleSPIMaterialService
                    .is_ExistSPCert(listMaterial, ownerGuid, dataBean.getItemlegalcertnum(), areacode).getResult();
        }
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", ZwfwConstant.CERTOWNERTYPE_FR);
        if (ZwfwConstant.CONSTANT_STR_TWO.equals(businessType)) {
            addCallbackParam("certnum", integratedCompany.getZzjgdmz());
        } else {
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
            //去除身份证
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
     * @param projectGuid
     *//*
     * public void updateProject(String projectGuid){ AuditProject project =
     * new AuditProject(); AuditRsItemBaseinfo result =
     * iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(yewuguid).
     * getResult(); project.setRowguid(projectGuid);
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
        //发送mq处理调用图审接口，处理数据
        String msg = subappGuid + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + dataBean.getItemcode();
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.ts." + businessGuid);
        //更新阶段标识
        auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_TSZ, null);
        addCallbackParam("msg", "送交图审完成，等待处理中！");
    }

    public void saveLs() {
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
        if (LHSP_Status_TSZ.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "已送交联审，请不要重复点击");
            return;
        } else if (LHSP_Status_TSWC.equals(auditSpISubapp.getStatus())) {
            addCallbackParam("msg", "联审已完成，请不要重复点击");
            return;
        }

        //发送mq处理调用联审接口，处理数据
        String msg = subappGuid + "_SPLIT_" + ZwfwUserSession.getInstance().getAreaCode() + "_SPLIT_" + dataBean.getItemcode() + "_SPLIT_"
                + WebUtil.getRequestCompleteUrl(request);
        sendMQMessageService.sendByExchange("exchange_handle", msg, "blsp.ls." + businessGuid);
        //更新阶段标识
        auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_TSZ, null);
        addCallbackParam("msg", "送交联审完成，等待处理中！");
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
                    /*//如果是子申报为材料预审中
                    if(ZwfwConstant.LHSP_Status_DYS.equals(status)){
                        auditSpITasks = auditSpITasks.stream().filter(a->ZwfwConstant.CONSTANT_STR_ONE.equals(a.getStatus())).collect(Collectors.toList());
                    }*/
                    List<String> taskids = iauditspbasetaskr.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
                    List<AuditSpITask> auditSpITasksdel = new ArrayList<>();
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        //如果该事项在并行阶段，则事项为副线事项
                        AuditTask audittask = iaudittask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false).getResult();
                        if (audittask != null) {
                            String task_id = audittask.getTask_id();
                            if (taskids.contains(task_id)) {
                                auditSpITasksdel.add(auditSpITask);
                            } else {
                                auditSpITask.put("ztsxlx", "1");
                            }
                        }

                        if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                            AuditProject auditproject = auditProjectService.getAuditProjectByRowGuid(auditSpITask.getProjectguid(), auditSpITask.getAreacode()).getResult();
                            auditSpITask.set("status", codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(auditproject.getStatus())));

                        } else {
                            auditSpITask.set("status", "未初始化");
                        }
                    }
                    //已办理的事项，前台不能办理
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
            fmodelTask = new DataGridModel<AuditSpITask>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
                    /*//如果是子申报为材料预审中
                    if(ZwfwConstant.LHSP_Status_DYS.equals(status)){
                        auditSpITasks = auditSpITasks.stream().filter(a->ZwfwConstant.CONSTANT_STR_ONE.equals(a.getStatus())).collect(Collectors.toList());
                    }*/
                    List<String> taskids = iauditspbasetaskr.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
                    List<AuditSpITask> auditSpITasksdel = new ArrayList<>();
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        //如果该事项在并行阶段，则事项为副线事项
                        AuditTask audittask = iaudittask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false).getResult();
                        if (audittask != null) {
                            String task_id = audittask.getTask_id();
                            if (taskids.contains(task_id)) {
                                auditSpITask.put("ztsxlx", "0");
                            } else {
                                auditSpITasksdel.add(auditSpITask);
                            }
                        }
                        if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                            AuditProject auditproject = auditProjectService.getAuditProjectByRowGuid(auditSpITask.getProjectguid(), auditSpITask.getAreacode()).getResult();
                            auditSpITask.set("status", codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(auditproject.getStatus())));

                        } else {
                            auditSpITask.set("status", "未初始化");
                        }
                    }
                    //已办理的事项，前台不能办理
                    auditSpITasks.removeAll(auditSpITasksdel);
                    this.setRowCount(auditSpITasks.size());
                    return auditSpITasks;
                }
            };
        }
        return fmodelTask;
    }

    public void checkAddtask() {
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
        //获取副线事项阶段
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("businedssguid", auditSpISubapp.getBusinessguid());
        sqlc.eq("phaseid", "5");
        List<AuditSpPhase> phaselist = auditSpPhaseService.getAuditSpPhase(sqlc.getMap()).getResult();
        if (phaselist != null && !phaselist.isEmpty()) {
            List<AuditSpTask> listtask = iauditsptask
                    .getIntersectionByPhaseguid(auditSpISubapp.getPhaseguid(), phaselist.get(0).getRowguid())
                    .getResult();
            //存在辅线事项
            if (listtask != null && !listtask.isEmpty()) {
                return;
            }
        }
        //不存在辅线事项，判断是不是所有事项都已办理
        List<AuditSpBasetask> listsptask = iauditsptask.selectAuditSpBaseTasksByPhaseguid(auditSpISubapp.getPhaseguid())
                .getResult();
        List<String> list = new ArrayList<>();
        for (AuditSpBasetask auditSpBasetask : listsptask) {
            list.add(auditSpBasetask.getRowguid());
        }
        sqlc.clear();
        if (StringUtil.isNotBlank(StringUtil.joinSql(list))) {
            sqlc.in("basetaskguid", StringUtil.joinSql(list));
        } else {
            sqlc.in("basetaskguid", "''");
        }
        //查询已经求的事项
        List<AuditSpITask> spitasklist = auditSpITaskService.getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid())
                .getResult();
        List<String> taskids = new ArrayList<>();
        //过滤之前选中的事项，一个事项只能选择一次
        for (AuditSpITask auditspitask : spitasklist) {
            AuditTask audittask = iaudittask.selectTaskByRowGuid(auditspitask.getTaskguid()).getResult();
            //辅线事项不添加
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
        //如果没有可办理的则不能办理
        if (listsptaskr == null || listsptaskr.isEmpty()) {
            addCallbackParam("msg", "阶段所有事项已征求，不用在添加事项！");
        }
    }

}

package com.epoint.auditsp.auditsphandle.action;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditsp.HandleSpCommonService;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.api.IJnCertInfo;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespbusiness.inter.IHandleSpBusiness;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目基本信息表新增页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnhandlebipreliminarydetailaction")
@Scope("request")
public class JNHandleBiPreliminarydetailAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1313301881620639339L;
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    /**
     * 办件组合服务
     */

    @Autowired
    private IAuditSpInstance auditSpInstanceService;

    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    @Autowired
    private IHandleSpBusiness handleSpBusinessService;

    @Autowired
    private IAuditProject projectService;

    @Autowired
    private IAuditTaskMaterial auditTaskMaterialService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;
    @Autowired
    private IAuditOrgaArea auditAreaImpl;
    @Autowired
    private IAuditSpPhase auditSpPhaseService;
    @Autowired
    private IAuditSpIReview auditSpIReviewService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IAuditSpTask iauditsptask;
    @Autowired
    private IAuditTask iaudittask;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IConfigService configservice;
    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;
    @Autowired
    private IAuditSpBasetask iauditspbasetask;
    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IAuditSpSpLxydghxkService iauditspsplxydghxkservice;

    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IAuditSpSpJgysService iauditspspjgysservice;

    @Autowired
    IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;
    @Autowired
    private IConfigService configService;

    @Autowired
    IUserService iuserservice;

    @Autowired
    private IParticipantsInfoService participantsInfoService;
    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private HandleSpCommonService handlespcommonservice;

    //3.0新增
    /**
     * 表格控件model
     */
    private DataGridModel<SpglQypgxxb> qypgModel;

    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;
    @Autowired
    private IDantiSubRelationService iDantiSubRelationService;


    /**
     * 工程建设许可信息表实体对象
     */
    private AuditSpSpGcjsxk phase2 = null;
    /**
     * 施工许可信息表实体对象
     */
    private AuditSpSpSgxk phase3 = null;

    public AuditSpSpLxydghxk phase1 = null;

    public AuditSpSpJgys phase4 = null;

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;
    /**
     * 项目申报日期
     */
    private Date createdate;
    private List<SelectItem> OuListModel = null;

    private DataGridModel<Record> taskListModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 主题实例标识
     */
    private String biGuid = "";
    /**
     * 主题标识
     */
    private String businessGuid = "";
    /**
     * 主题实例
     */
    private AuditSpInstance spInstance;
    /**
     * 子申报实例标识
     */
    private String subappGuid;

    private String ous;

    private String tasks;

    private String phaseguid;

    public String zitemname;

    public String zitemcode;

    public String isadditem;

    public AuditRsItemBaseinfo zitem;

    public String citylevel;

    public List<String> sjcode;

    public AuditSpISubapp auditSpISubapp;

    AuditSpPhase auditSpPhase;

    @Autowired
    private IJnCertInfo iJnCertInfo;

    @Override
    public void pageLoad() {
        try {
            // 设置辖区
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult();
            if (area != null) {
                citylevel = area.getCitylevel();
            }

            // 查询所有市级的areacode
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
            List<AuditOrgaArea> listarea = iauditorgaarea.selectAuditAreaList(sqlc.getMap()).getResult();
            if (listarea != null && listarea.size() > 0) {
                sjcode = listarea.stream().map(AuditOrgaArea::getXiaqucode).collect(Collectors.toList());
                sjcode.add(ZwfwUserSession.getInstance().getAreaCode());
            } else {
                sjcode = new ArrayList<>();
            }

            biGuid = getRequestParameter("biGuid");
            spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
            if (spInstance != null) {
                subappGuid = getRequestParameter("subappGuid");
                auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
                phase1 = iauditspsplxydghxkservice.findAuditSpSpLxydghxkBysubappguid(subappGuid);
                phase2 = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappGuid);
                phase3 = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
                phase4 = iauditspspjgysservice.findAuditSpSpJgysBySubappGuid(subappGuid);

                businessGuid = spInstance.getBusinessguid();
                dataBean = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid()).getResult();

                if (dataBean != null) {
                    addCallbackParam("xmdm", dataBean.getItemcode());
                    addCallbackParam("itemguid", dataBean.getRowguid());

                    zitem = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid())
                            .getResult();
                    if (dataBean.getRowguid().equals(zitem.getRowguid())) {
                        // 没有进行拆分神申报
                        isadditem = ZwfwConstant.CONSTANT_STR_ZERO;
                    } else {
                        isadditem = ZwfwConstant.CONSTANT_STR_ONE;
                        zitemcode = zitem.getItemcode();
                        zitemname = zitem.getItemname();
                    }
                }

                createdate = spInstance.getCreatedate();
                phaseguid = auditSpISubapp.getPhaseguid();
                AuditSpPhase AuditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseguid).getResult();
                if (StringUtil.isNotBlank(AuditSpPhase.getPhaseId())) {
                    addCallbackParam("phaseid", AuditSpPhase.getPhaseId());
                    if (StringUtil.isNotBlank(AuditSpPhase.getPhaseId())
                            && (AuditSpPhase.getPhaseId().equals("1") || AuditSpPhase.getPhaseId().equals("2"))) {
                        addCallbackParam("jsdwshow", true);
                    } else {
                        addCallbackParam("jsdwshow", false);
                    }
                }
                String itemGuid = auditSpISubapp.getYewuguid();
                if (StringUtil.isNotBlank(itemGuid)) {
                    this.addCallbackParam("itemGuid", spInstance.getYewuguid());
                    this.addCallbackParam("subappGuid", subappGuid);
                    this.addCallbackParam("eformurl", AuditSpPhase.getEformurl());
                }
                if (StringUtil.isNotBlank(AuditSpPhase.getPhasename())) {
                    this.addCallbackParam("phasename", AuditSpPhase.getPhasename());
                }
                auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid()).getResult();
                if (auditSpPhase != null) {
                    String leadOuGuid = auditSpPhase.getLeadouguid();
                    if (StringUtil.isNotBlank(leadOuGuid)) {
                        addCallbackParam("isOwnQt", true);
                    } else {
                        addCallbackParam("isOwnQt", false);
                    }
                    addCallbackParam("phaseid", auditSpPhase.getPhaseId());
                    addCallbackParam("added", auditSpPhase.get("ishb"));
                }
            }

            // 判断是否可选择事项
            String choosetask = configservice.getFrameConfigValue("AS_BLSP_CHOOSETASK");
            if (StringUtil.isNotBlank(choosetask)) {
                addCallbackParam("choosetask", choosetask);
            } else {
                addCallbackParam("choosetask", "0");
            }
            if (phase1 == null) {
                phase1 = new AuditSpSpLxydghxk();
            }
            if (phase2 == null) {
                phase2 = new AuditSpSpGcjsxk();
            }
            if (phase3 == null) {
                phase3 = new AuditSpSpSgxk();
            }
            if (phase4 == null) {
                phase4 = new AuditSpSpJgys();
            }

            if (StringUtil.isNotBlank(phase4.getRowguid())) {
                String subappname = phase4.getSubappname();
                if (!"装饰装修".equals(subappname)) {
                    addCallbackParam("newformid", "576"); //第四阶段 竣工验收表单
                    addCallbackParam("eformCommonPage", configservice.getFrameConfigValue("eformCommonPage"));
                    addCallbackParam("epointUrl", configservice.getFrameConfigValue("epointsformurl"));
                }
            } else if (StringUtil.isNotBlank(phase3.getRowguid())) {
                addCallbackParam("newformid", "601"); //
                addCallbackParam("eformCommonPage", configservice.getFrameConfigValue("eformCommonPage"));
                addCallbackParam("epointUrl", configservice.getFrameConfigValue("epointsformurl"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* 3.0新增逻辑 */
        // 判断单体是否的新的;
        String subappguid = getRequestParameter("subappguid");
        if (StringUtil.isNotBlank(subappguid)) {
            List<DantiSubRelation> listBydanti = iDantiSubRelationService.findListBySubappGuid(subappguid);
            if (!EpointCollectionUtils.isEmpty(listBydanti)) {
                DantiSubRelation dantiSubRelation = listBydanti.get(0);
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(dantiSubRelation.getStr("is_v3"))) {
                    addCallbackParam("dantitype", "1");
                }
            }
        }
        /* 3.0结束逻辑 */

    }

    /**
     * 预审通过
     */
    public void passyes() {
        String messageguid = getRequestParameter("MessageItemGuid");
        if (StringUtil.isNotBlank(messageguid)) {
            // 判断消息是否处理过
            MessagesCenter message = messageCenterService.getDetail(messageguid,
                    UserSession.getInstance().getUserGuid());
            if (message == null) {
                addCallbackParam("msg", "该待办已经处理完成！");
                return;
            }
        }
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_DPS, null);

        // 通过阶段主键获取实体类
        AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseguid).getResult();
        // 存放牵头部门的guid
        String leadOuGuid = null;
        if (auditSpPhase != null) {
            // 通信办件生成
            SqlConditionUtil sqlUtil = new SqlConditionUtil();
            sqlUtil.eq("IS_EDITAFTERIMPORT", "1");
            sqlUtil.isBlankOrValue("IS_HISTORY", "0");
            sqlUtil.eq("IS_ENABLE", "1");
            //是否需要生成通信办件
            boolean ifcreatebanjian = false;
            log.info("Phasename:"+auditSpPhase.getPhasename());
            if (auditSpPhase.getPhasename().contains("工程建设")) {
                sqlUtil.eq("item_id", "11370500004311394A3370310004812");
                ifcreatebanjian=true;
            } else if (auditSpPhase.getPhasename().contains("竣工验收")) {
                sqlUtil.eq("item_id", "11370800MB285591846370117000003");
                ifcreatebanjian=true;
            }
            List<AuditTask> taskList = null;
            if(ifcreatebanjian){
                taskList = iaudittask.getAuditTaskList(sqlUtil.getMap()).getResult();
            }

            if (CollectionUtils.isNotEmpty(taskList) && StringUtil.isBlank(ConfigUtil.getFrameConfigValue("generateTxProject"))) {
                log.info("taskList:"+taskList.size());
                AuditTask auditTask = taskList.get(0);
                // 生成 通信基础设规划设计方案联审 事项的办件
                String projectGuid = handleProjectService
                        .InitBLSPProject(auditTask.getRowguid(), "", userSession.getDisplayName(), userSession.getUserGuid(),
                                dataBean.getItemlegalcerttype(), dataBean.getItemlegalcertnum(), "", "", "", biGuid,
                                subappGuid, businessGuid, dataBean.getItemlegalcreditcode(), dataBean.getItemlegaldept())
                        .getResult();
                // 设置联系人
                AuditProject project = projectService.getAuditProjectByRowGuid("rowguid", projectGuid, null).getResult();
                if (project != null) {
                    // 查询建设单位
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("corptype", "31");
                    sqlConditionUtil.eq("itemguid", spInstance.getYewuguid());
                    List<ParticipantsInfo> sjList = participantsInfoService.getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
                    if (CollectionUtils.isNotEmpty(sjList)) {
                        ParticipantsInfo participantsInfo = sjList.get(0);
                        project.setContactphone(participantsInfo.getPhone());
                        project.setContactmobile(participantsInfo.getPhone());
                        project.setContactperson(participantsInfo.getLegal());
                        projectService.updateProject(project);
                    }
                }
                List<String> materialList = new ArrayList<>();
                List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                        .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), true).getResult();
                if (CollectionUtils.isNotEmpty(auditTaskMaterials)) {
                    materialList = auditTaskMaterials.stream()
                            .map(AuditTaskMaterial::getMaterialid).collect(Collectors.toList());
                }
                // 再进行材料初始化
                handleSPIMaterialService.initProjctMaterial(materialList, businessGuid, subappGuid, auditTask.getRowguid(),
                        projectGuid);
            }
            leadOuGuid = auditSpPhase.getLeadouguid();
        }
        // 给窗口负责人发送通知提醒
        String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
        log.info("roleGuid:"+roleGuid);
        if (StringUtil.isNotBlank(leadOuGuid)) {
            log.info("leadOuGuid:"+leadOuGuid);
            // 给牵头部门插入一条记录
            String reviewGuid = auditSpIReviewService.addReview(biGuid, phaseguid, leadOuGuid, subappGuid, 1)
                    .getResult();
            // 给牵头部门发送通知提醒
            List<FrameUser> listUser = iJnCertInfo.getuserbyouguid(leadOuGuid,roleGuid);
//            List<FrameUser> listUser = userService.listUserByOuGuid(leadOuGuid, roleGuid, "", "", false, true, false, 3);
            String targetUserGuid = userSession.getUserGuid();
            String url = "epointzwfw/auditsp/auditsphandle/handleitemregisterforleadou?reviewGuid=" + reviewGuid
                    + "&leadOuGuid=" + leadOuGuid + "&targetuserguid=" + targetUserGuid;
            log.info("listUser.size():"+listUser.size());
            if (listUser != null && listUser.size() > 0) {
                for (FrameUser user : listUser) {
                    messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                            "【事项征求】" + auditSpISubapp.getSubappname(), IMessagesCenterService.MESSAGETYPE_WAIT,
                            user.getUserGuid(), user.getDisplayName(), userSession.getUserGuid(),
                            userSession.getDisplayName(), "事项征求", url, leadOuGuid, "", 1, "", "", reviewGuid,
                            reviewGuid.substring(0, 1), new Date(), "", userSession.getUserGuid(), "", "");
                }
            }
        } else {
            // 发起事项征求，没有牵头部门的话，走正常的通知所有窗口部门的流程
            String getous = getOus();
            log.info("getous:"+getous);
            String[] ous = getous.split(",");
            // 数组去重
            Map<String, Object> map = new HashMap<String, Object>();
            for (String str : ous) {
                map.put(str, str);
            }
            // 返回一个包含所有对象的指定类型的数组
            ous = map.keySet().toArray(new String[1]);
            String gettasks = getTasks();
            log.info("gettasks:"+gettasks);
            String[] lists = gettasks.split(",");
            if (ous != null && ous.length > 0 && StringUtil.isNotBlank(ous[0])) {
                for (String ou : ous) {
                    String reviewGuid = auditSpIReviewService.addReview(biGuid, phaseguid, ou, subappGuid).getResult();
                    List<FrameUser> listUser = iJnCertInfo.getuserbyouguid(ou,roleGuid);
//                    List<FrameUser> listUser = userService.listUserByOuGuid(ou, roleGuid, "", "", false, true, false, 3);
                    String targetUserGuid = userSession.getUserGuid();
                    String url = "epointzwfw/auditsp/auditsphandle/handlereview?reviewGuid=" + reviewGuid
                            + "&targetuserguid=" + targetUserGuid;
                    log.info("listUser.size():"+listUser.size());
                    if(listUser == null || listUser.size() == 0) {
                        log.info("ou:"+ou);
                    }
                    for (FrameUser user : listUser) {
                        messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                                "【事项征求】" + auditSpISubapp.getSubappname(), IMessagesCenterService.MESSAGETYPE_WAIT,
                                user.getUserGuid(), user.getDisplayName(), userSession.getUserGuid(),
                                userSession.getDisplayName(), "事项征求", url, ou, "", 1, "", "", reviewGuid,
                                reviewGuid.substring(0, 1), new Date(), "", userSession.getUserGuid(), "", "");
                    }
                }
            } else if (lists != null && lists.length > 0) {
                // 修改subapp的状态为已评审
                log.info("subappGuid:"+subappGuid);
                iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YPS, null);
                // 插入事项实例
                for (String taskid : lists) {
                    AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
                    AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(taskid).getResult();
                    auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseguid, auditTask.getRowguid(),
                            auditTask.getTaskname(), subappGuid,
                            auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(),
                            basetask.getSflcbsx());
                }
                // addCallbackParam("url","epointzwfw/auditsp/auditsphandle/handlesubmitmaterial?subappGuid="+subappGuid);
            }
        }
        // 声明变量记录主项标识
        String prowguid = zitem.getRowguid();
        String issendzj = zitem.getIssendzj();
        // 如果是子项查询主项
        if (StringUtil.isNotBlank(zitem.getParentid())) {
            AuditRsItemBaseinfo dataBean1 = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(zitem.getParentid())
                    .getResult();
            if (dataBean1 != null) {
                prowguid = dataBean1.getRowguid();
                issendzj = dataBean1.getIssendzj();
            }
        }
        String mqmsg;
        if (!zitem.getRowguid().equals(prowguid) && !ZwfwConstant.CONSTANT_STR_ONE.equals(issendzj)) {
            // 如果是新增项目，则需要把项目推送到住建系统
            mqmsg = prowguid + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + getViewData("subappGuid");
            sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blsp.rsitem." + businessGuid);
        }
        // 添加推送数据
        mqmsg = zitem.getRowguid() + "." + ZwfwUserSession.getInstance().getAreaCode() + "." + subappGuid;
        sendMQMessageService.sendByExchange("exchange_handle", mqmsg, "blsp.rsitem." + businessGuid);

        // 向大厅发送代办消息
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
        String title = "【预审通过】" + dataBean.getItemname();
        String username = iuserservice.getUserNameByUserGuid(auditSpISubapp.getApplyerguid());
        if (auditSpISubapp != null && StringUtil.isBlank(username)) {
            String openUrl = configService.getFrameConfigValue("zwdtMsgurl")
                    + "/epointzwmhwz/pages/approve/perioddetailW?biguid=" + auditSpISubapp.getBiguid() + "&phaseguid="
                    + auditSpISubapp.getPhaseguid() + "&businessguid=" + auditSpISubapp.getBusinessguid()
                    + "&subappguid=" + auditSpISubapp.getRowguid() + "&itemguid=" + dataBean.getRowguid();
            messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                    IMessagesCenterService.MESSAGETYPE_WAIT, auditSpISubapp.getApplyerguid(), "",
                    UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), "", openUrl,
                    UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1, null, "", null,
                    null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
        }
        // 删除大厅的代办
        String msgguid = this.getRequestParameter("messageItemGuid");
        handleProjectService.delZwfwMessage("", ZwfwUserSession.getInstance().getAreaCode(), "统一收件人员", subappGuid);
        addCallbackParam("msg", "预审通过！");
    }

    public List<SelectItem> getOuListModel() {
        if (OuListModel == null) {
            OuListModel = new ArrayList<SelectItem>();

            // 这里进行绑定部门列表
            List<String> listOu = handleSpBusinessService.getPhaseRelationOus(businessGuid, phaseguid).getResult();
            for (String ou : listOu) {
                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou);
                AuditOrgaArea area = auditAreaImpl.getAreaByAreacode(ouExt.get("areacode")).getResult();
                if (StringUtil.isBlank(ouExt.get("areacode")) || StringUtil.isBlank(area.getCitylevel())
                        || Integer.parseInt(area.getCitylevel()) < Integer.parseInt(ZwfwConstant.AREA_TYPE_XZJ)) {
                    String ouName = ouService.getOuByOuGuid(ou).getOuname();
                    OuListModel.add(new SelectItem(ou, ouName));
                }
            }
        }
        return OuListModel;
    }

    public DataGridModel<Record> getTaskListModel() {
        if (taskListModel == null) {
            taskListModel = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int arg0, int arg1, String arg2, String arg3) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("businessguid", businessGuid);
                    sqlc.eq("phaseguid", phaseguid);
                    List<AuditSpTask> listsptask = iauditsptask.getAllAuditSpTask(sqlc.getMap()).getResult();
                    List<String> list = new ArrayList<>();
                    for (AuditSpTask sptask : listsptask) {
                        if (sptask.getBasetaskguid() != null) {
                            list.add(sptask.getBasetaskguid());
                        }
                    }
                    sqlc.clear();
                    String baseTaskGuids = "";
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        baseTaskGuids = "'" + StringUtil.join(list, "','") + "'";
                        sqlc.in("basetaskguid", baseTaskGuids);
                    } else {
                        return new ArrayList<>();
                    }
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(citylevel)) {
                        sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    } else if (ZwfwConstant.CONSTANT_STR_TWO.equals(citylevel)) {
                        // 如果是区县的，过滤其他区县
                        sqlc.in("areacode", StringUtil.joinSql(sjcode));
                    }
                    sqlc.setOrderAsc("areacode");
                    List<AuditSpBasetaskR> auditSpBasetaskRList = iauditspbasetaskr
                            .getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                    // 获取辖区的部门部门

                    Map<String, Record> maprecord = new HashMap<>();
                    if (ValidateUtil.isNotBlankCollection(auditSpBasetaskRList)) {
                        for (AuditSpBasetaskR auditSpBasetaskR : auditSpBasetaskRList) {
                            // 获取辖区记录，如果没有获取新的辖区记录
                            Record r = maprecord.get(auditSpBasetaskR.getAreacode());
                            if (r == null) {
                                r = new Record();
                                r.set("xiaquname", auditSpBasetaskR.getXiaquname());
                                r.set("areacode", auditSpBasetaskR.getAreacode());
                                maprecord.put(auditSpBasetaskR.getAreacode(), r);
                            }
                            List<Record> tasklist = r.get("taskdata");
                            if (tasklist == null) {
                                tasklist = new ArrayList<>();
                                r.set("taskdata", tasklist);
                            }
                            Record taskdata = new Record();
                            taskdata.set("id", auditSpBasetaskR.getTaskid());
                            taskdata.set("text", auditSpBasetaskR.getTaskname());
                            tasklist.add(taskdata);
                        }
                    }
                    // 转换数据
                    List<Record> returnlist = new ArrayList<>();
                    for (Record r : maprecord.values()) {
                        List<Record> tasklist = r.get("taskdata");
                        tasklist.sort((a, b) -> {
                            return a.getStr("text").compareTo(b.getStr("text"));
                        });
                        r.set("taskdata", JsonUtil.listToJson(tasklist));
                        returnlist.add(r);
                    }
                    return returnlist;

                }
            };
        }
        return taskListModel;
    }

    public DataGridModel<Record> getouchoosemodel() {
        if (model == null) {
            model = new DataGridModel<Record>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int arg0, int arg1, String arg2, String arg3) {
                    // 获取当期阶段需要办理的审批事项的集合，供根据部门名称和主题标识，获取征求部门的集合
                    List<AuditSpTask> listsptask = iauditsptask.getAllAuditSpTaskByPhaseguid(phaseguid).getResult();
                    List<String> basetaskguids = new ArrayList<String>();
                    for (AuditSpTask auditsptask : listsptask) {
                        if (!basetaskguids.contains(auditsptask.getBasetaskguid())) {
                            if (auditsptask.getBasetaskguid() != null) {
                                basetaskguids.add(auditsptask.getBasetaskguid());
                            }
                        }
                    }
                    // 根据basetaskguids 获取审批事项部门
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.in("rowguid", "'" + StringUtil.join(basetaskguids, "','") + "'");
                    List<Record> listou = iauditspbasetask.getDistinctOuByCondition(sqlc.getMap()).getResult();

                    // 判断是否存在设置默认值的部门
                    List<String> chooseouguid = handlespcommonservice.getChooseOuguid(auditSpPhase);

                    // 获取一件事主题辖区下所有事项的部门guid列表
                    List<String> yjsBusinessTaskOuGuidList = new ArrayList<>();
                    String yjsBusinessGuid = auditSpISubapp.getStr("yjsbusinessguid");
                    if (StringUtil.isNotBlank(yjsBusinessGuid)) {
                        SqlConditionUtil sUtil = new SqlConditionUtil();
                        sUtil.setSelectFields("b.taskid, c.ouguid");
                        sUtil.setInnerJoinTable("audit_sp_basetask_r b", "a.basetaskguid",
                                "b.basetaskguid inner join audit_task c on b.taskid = c.task_id");
                        sUtil.eq("a.businessguid", yjsBusinessGuid);
                        sUtil.eq("b.areacode", spInstance.getAreacode());
                        sUtil.isBlankOrValue("c.is_history", ZwfwConstant.CONSTANT_STR_ZERO);
                        sUtil.eq("c.is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
                        sUtil.eq("c.is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                        List<AuditSpTask> yjsBusinessTaskList = iauditsptask.getAllAuditSpTask(sUtil.getMap())
                                .getResult();
                        if (EpointCollectionUtils.isNotEmpty(yjsBusinessTaskList)) {
                            List<String> curTaskOuGuidList = yjsBusinessTaskList.stream().map(AuditSpTask::getOuguid)
                                    .distinct().collect(Collectors.toList());
                            if (EpointCollectionUtils.isNotEmpty(curTaskOuGuidList)) {
                                yjsBusinessTaskOuGuidList.addAll(curTaskOuGuidList);
                            }
                        }
                    }

                    // 根据审批事项部门生成记录
                    List<Record> list = new ArrayList<>();
                    for (Record ou : listou) {
                        Record r = new Record();
                        r.set("ouname", ou.getStr("ouname"));
                        // 处理征求部门的逻辑
                        List<Record> listrou = iauditspbasetaskr
                                .getTaskidlistbyBasetaskOuname(ou.getStr("ouname"), basetaskguids).getResult();

                        // 如果市级过滤掉所有他辖区的征求
                        if (ZwfwConstant.CONSTANT_STR_TWO.equals(citylevel)) {
                            // 如果是区县的，过滤其他区县
                            listrou = listrou.stream().filter(a -> sjcode.contains(a.getStr("areacode")))
                                    .collect(Collectors.toList());
                        }
                        // 转换部门多选的下拉数据
                        List<String> oustem = new ArrayList<>();
                        for (Record record : listrou) {
                            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(record.getStr("taskid"))
                                    .getResult();
                            if (audittask == null || oustem.contains(audittask.getOuguid())) {
                                continue;
                            }
                            oustem.add(audittask.getOuguid());
                            record.set("id", audittask.getOuguid());
                            record.set("areacode", audittask.getAreacode());
                            record.set("text", record.getStr("xiaquname") + "-" + audittask.getOuname());

                            // 判断是否选中
                            record.set("checked", false);
                            if (EpointCollectionUtils.isNotEmpty(chooseouguid)) {
                                if (chooseouguid.contains(audittask.getOuguid())) {
                                    record.set("checked", true);
                                }
                            }
                            
                            // 如果未选中，在此处添加额外选中逻辑：一件事主题辖区下事项对应的部门列表中包含此部门则选中
                            if (!record.getBoolean("checked")
                                    && yjsBusinessTaskOuGuidList.contains(audittask.getOuguid())) {
                                record.set("checked", true);
                            }
                        }
                        listrou.removeIf(a -> {
                            if (StringUtil.isBlank(a.getStr("id"))) {
                                return true;
                            } else {
                                return false;
                            }
                        });

                        Map<String, List<Record>> oulistmap = listrou.stream()
                                .collect(Collectors.groupingBy(x -> x.getStr("areacode")));
                        if (oulistmap.get(ZwfwUserSession.getInstance().getAreaCode()) != null
                                && oulistmap.get(ZwfwUserSession.getInstance().getAreaCode()).size() > 0) {
                            r.set("ouchoosedata",
                                    JsonUtil.listToJson(oulistmap.get(ZwfwUserSession.getInstance().getAreaCode())));
                        } else {
                            r.set("ouchoosedata", "");
                        }
                        oulistmap.remove(ZwfwUserSession.getInstance().getAreaCode());
                        List<Record> otherarea = new ArrayList<>();
                        for (List<Record> string : oulistmap.values()) {
                            otherarea.addAll(string);
                        }
                        if (otherarea.size() > 0) {
                            r.set("otherarea", JsonUtil.listToJson(otherarea));
                        } else {
                            r.set("otherarea", "");
                        }
                        list.add(r);
                    }
                    list.removeIf(a -> {
                        String b = a.getStr("ouchoosedata");
                        String c = a.getStr("otherarea");
                        if (StringUtil.isBlank(b) && StringUtil.isBlank(c)) {
                            return true;
                        }
                        List<Record> r = JsonUtil.jsonToList(b, Record.class);
                        List<Record> r1 = JsonUtil.jsonToList(c, Record.class);
                        boolean rresult;
                        boolean r1result;
                        if (r != null && r.size() > 0) {
                            rresult = false;
                        } else {
                            rresult = true;
                        }
                        if (r1 != null && r1.size() > 0) {
                            r1result = false;
                        } else {
                            r1result = true;
                        }
                        if (rresult && r1result) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return model;
    }

    public String showblue() {
        String xmzb = zitem.getStr("xmzb");
        String str = "";
        // system.out.println("xmzb:"+xmzb);
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        params.put("content", xmzb);
        String response = HttpUtil.doPost("http://59.206.96.142:8080/jnmapservice/api/ThirdParty/InsertContent", params,
                headers);
        // system.out.println("response:"+response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String ReturnCode = jsonObject.getString("ReturnCode");
        // system.out.println("ReturnCode:"+ReturnCode);
        if ("0".equals(ReturnCode)) {
            str = "success";
        } else {
            str = "error";
        }
        return str;
    }

    public DataGridModel<SpglQypgxxb> getQypgDatagrid() {
        // 获得表格对象
        if (qypgModel == null) {
            qypgModel = new DataGridModel<SpglQypgxxb>() {
                @Override
                public List<SpglQypgxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.setLeftJoinTable("spgl_qypgxxb_edit_r b", "a.rowguid", "b.qypgguid");
                    sql.eq("b.pre_itemguid", getDataBean().getRowguid());
                    sql.setSelectFields("a.*,b.rowguid rguid,b.subappguid");
                    PageData<SpglQypgxxb> pageData = iSpglQypgxxbService.getAuditSpDanitemByPage(sql.getMap(), first,
                            pageSize, "b.createdate", sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglQypgxxb> list = pageData.getList();
                    for (SpglQypgxxb spglQypgxxb : list) {
                        if (getRequestParameter("subappGuid").equals(spglQypgxxb.get("subappguid"))) {
                            spglQypgxxb.set("isNew", true);
                        }
                    }
                    return list;
                }
            };
        }
        return qypgModel;
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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getOus() {
        return ous;
    }

    public void setOus(String ous) {
        this.ous = ous;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public AuditSpSpGcjsxk getPhase2() {
        return phase2;
    }

    public void setPhase2(AuditSpSpGcjsxk phase2) {
        this.phase2 = phase2;
    }

    public AuditSpSpSgxk getPhase3() {
        return phase3;
    }

    public void setPhase3(AuditSpSpSgxk phase3) {
        this.phase3 = phase3;
    }

    public AuditSpSpLxydghxk getPhase1() {
        return phase1;
    }

    public void setPhase1(AuditSpSpLxydghxk phase1) {
        this.phase1 = phase1;
    }

    public AuditSpSpJgys getPhase4() {
        return phase4;
    }

    public void setPhase4(AuditSpSpJgys phase4) {
        this.phase4 = phase4;
    }

    public String getZitemname() {
        return zitemname;
    }

    public void setZitemname(String zitemname) {
        this.zitemname = zitemname;
    }

    public String getZitemcode() {
        return zitemcode;
    }

    public void setZitemcode(String zitemcode) {
        this.zitemcode = zitemcode;
    }

    public String getIsadditem() {
        return isadditem;
    }

    public void setIsadditem(String isadditem) {
        this.isadditem = isadditem;
    }


}

package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.epoint.cert.api.IJnCertInfo;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditsp.HandleSpCommonService;
import com.epoint.auditsp.auditsphandle.api.IAuditSpITaskfwxm;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspireview.domain.AuditSpIReview;
import com.epoint.basic.auditsp.auditspireview.inter.IAuditSpIReview;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
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
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespbusiness.inter.IHandleSpBusiness;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.cs.clchoose.api.IClChooseService;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.zjcs.fwxminfo.bizlogic.FwxmInfoService;
import com.epoint.zjcs.fwxminfo.bizlogic.domain.FwxmInfo;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnhandlereviewaction")
@Scope("request")
public class JNHandleReviewAction extends BaseController
{
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
    private IAuditSpIReview auditSpIReviewService;

    @Autowired
    private IAuditSpITaskfwxm auditSpITaskfwxmService;
    @Autowired
    private IAuditSpTask auditSpTaskService;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IAuditTask auditTaskService;
    @Autowired
    private IClChooseService service;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IOnlineMessageInfoService iOnlineMessageInfoService;

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IAuditSpPhase auditSpPhaseService;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    private IAuditOrgaArea areaImpl;

    @Autowired
    private IAuditProject iauditproject;

    @Autowired
    private IAuditSpSpLxydghxkService iauditspsplxydghxkservice;

    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IAuditSpSpJgysService iauditspspjgysservice;

    @Autowired
    private IAuditProjectSparetime sparetimeService;

    @Autowired
    private HandleSpCommonService handlespcommonservice;
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

    private DataGridModel<FwxmInfo> model1;
    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;

    private AuditSpIReview dataReview = null;

    private FileUploadModel9 fileUploadModel;
    private FwxmInfoService fwxminfoService = new FwxmInfoService();

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

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> projectmodel;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> spitaskmodel;

    private List<SelectItem> OuListModel = null;

    private String phaseGuid = "";
    private String businessGuid = "";
    private String reviewGuid = "";
    private String subappGuid = "";
    private String ouGuid = "";
    private String biGuid = "";
    private String targetuserguid = "";
    private String yijiaoopinion = "";
    private String ouguids = "";

    private AuditSpISubapp subapp;
    private AuditSpInstance spInstance = null;

    @Autowired
    private IJnCertInfo iJnCertInfo;

    @Override
    public void pageLoad() {

        reviewGuid = getRequestParameter("reviewGuid");
        targetuserguid = getRequestParameter("targetuserguid");

        AuditSpIReview auditSpReview = auditSpIReviewService.getReviewByGuid(reviewGuid).getResult();
        // 赋值
        biGuid = auditSpReview.getBiguid();
        ouGuid = auditSpReview.getOuguid();
        phaseGuid = auditSpReview.getPhaseguid();
        subappGuid = auditSpReview.getSubappguid();
        spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        subapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();
        businessGuid = spInstance.getBusinessguid();
        if (subapp != null && StringUtil.isNotBlank(subapp.getYewuguid())) {
            dataBean = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
            dataBean.set("subappname", subapp.getSubappname());
            AuditSpPhase phase = auditSpPhaseService.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
            phase1 = iauditspsplxydghxkservice.findAuditSpSpLxydghxkBysubappguid(subappGuid);
            phase2 = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappGuid);
            phase3 = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
            phase4 = iauditspspjgysservice.findAuditSpSpJgysBySubappGuid(subappGuid);
            if (phase != null) {
                addCallbackParam("phaseid", phase.getPhaseId());
                List<String> taskids = handlespcommonservice.getChooseTaskguid(phase);
                addCallbackParam("musttask", taskids);
            }
        }
        else {
            dataBean = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid()).getResult();
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
        getDataGridData();
    }

    /**
     * 保存并关闭
     * 
     */
    public void save(String params) {
        int ordernum = 0;
        JSONObject jsonObject = JSON.parseObject(params);
        if (jsonObject != null) {
            List<String> select2 = getDataGridData().getSelectKeys();
            Iterator<String> keys = jsonObject.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                for (String string : select2) {
                    if (string.equals(key)) {
                        JSONArray ids = jsonObject.getJSONArray(key);
                        if (ids != null) {
                            for (int i = 0; i < ids.size(); i++) {
                                // 提取出ids中的所有
                                String id = (String) ids.get(i);
                                AuditTaskMaterial auditMaterial = service.findMaterial(id);
                                AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                                auditSpIMaterial.setEffictiverange("");
                                auditSpIMaterial.setShared("0");
                                auditSpIMaterial.setMaterialguid(auditMaterial.getMaterialid());
                                auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                                auditSpIMaterial.setAllowrongque(auditMaterial.getIs_rongque().toString());
                                auditSpIMaterial.setBiguid("");
                                auditSpIMaterial.setBusinessguid(businessGuid);
                                auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                                auditSpIMaterial.setOperatedate(new Date());
                                auditSpIMaterial.setPhaseguid(phaseGuid);
                                auditSpIMaterial.setResult("0");
                                auditSpIMaterial.setMaterialname(auditMaterial.getMaterialname());
                                auditSpIMaterial.setStatus("10");
                                auditSpIMaterial.setSubmittype(auditMaterial.getSubmittype());
                                auditSpIMaterial.setNecessity(String.valueOf(auditMaterial.getNecessity()));
                                auditSpIMaterial.setSubappguid(subappGuid + "1");
                                auditSpIMaterial.setOrdernum(ordernum);
                                auditSpIMaterial.set("flag", "true");
                                ordernum += 10;
                                service.insert(auditSpIMaterial);
                            }
                        }
                    }
                }
            }
        }

        String a = auditSpIReviewService.IfOrNo(reviewGuid);
        if (a.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
            addCallbackParam("msg", "已经评审通过，不能再次评审！");
        }
        else {
            // 更新当前的评审记录为以评审
            auditSpIReviewService.updateReview(reviewGuid, UserSession.getInstance().getUserGuid(), new Date(),
                    dataReview.getPingshenopinion(), ZwfwConstant.LHSP_Status_YPS);
            // 发送部门征求
            passon(ouguids);
            // 获取spitask中存在的事项id
            List<String> spitaskIdlist = auditSpITaskService.getTaskIDByReviewguid(reviewGuid).getResult();
            // 插入事项实例
            List<String> select = getDataGridData().getSelectKeys();
            List<String> select1 = getDataGridData1().getSelectKeys();

            if (select != null && select.size() > 0) {
                // 遍历spitask，去除taskids中已经评审的事项
                for (String taskid : spitaskIdlist) {
                    if (select.contains(taskid)) {
                        select.remove(taskid);
                    }
                }

                labe: for (String taskid : select) {
                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(taskid).getResult();
                    if (select1 != null && select1.size() > 0) {
                        for (String rowguid : select1) {
                            FwxmInfo fwxmInfo = fwxminfoService.find(FwxmInfo.class, rowguid);
                            if (fwxmInfo.getSjdspsxGuid().equals(auditTask.getRowguid())) {
                                auditSpITaskfwxmService.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                        auditTask.getRowguid(), auditTask.getTaskname(), subappGuid,
                                        auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), rowguid);
                                continue labe;
                            }
                        }
                    }
                    AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(taskid).getResult();
                    auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.getRowguid(),
                            auditTask.getTaskname(), subappGuid,
                            auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(),
                            reviewGuid, basetask.getSflcbsx());
                }

            }
            // TODO 这里后面需要改成用队列来处理，否则存在数据查询冲突的情况
            // 判断一下是不是要完成评审
            List<AuditSpIReview> listReview = auditSpIReviewService.getReviewBySubappGuid(subappGuid).getResult();
            listReview.removeIf(review -> {
                return ZwfwConstant.LHSP_Status_YPS.equals(review.getStatus());
            });
            // 如果排除掉了所有的评审，那就说明都已经完成了，那么就更新子申报的状态 为已评审完成
            if (listReview.size() == 0) {
                List<AuditSpITask> spITasks = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
                // 存在事项才需要收件
                if (spITasks != null && spITasks.size() > 0) {
                    auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YPS, null);
                    /*
                     * // 全部评审完毕后需要通知征求发起人
                     * AuditSpInstance auditSpInstance =
                     * auditSpInstanceService.getDetailByBIGuid(biGuid).
                     * getResult();
                     * String itemName = auditSpInstance.getItemname();
                     * iOnlineMessageInfoService.insertMessage(UUID.randomUUID()
                     * .toString(), userSession.getUserGuid(),
                     * userSession.getDisplayName(), targetuserguid, "",
                     * targetuserguid,
                     * "您发起的项目 " + itemName + "评审已完成，请及时上传材料！", new Date());
                     */
                    // 全部评审完毕后,通知牵头部门，让牵头部门发送消息给统一收件人
                    AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
                    String itemName = "";
                    if (auditSpInstance != null) {
                        itemName = auditSpInstance.getItemname();
                    }
                    if (StringUtil.isNotBlank(itemName) && StringUtil.isNotBlank(targetuserguid)) {
                        // 如果有牵头部门，发给牵头部门
                        // 通过阶段主键获取实体类
                        AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
                        // 存放牵头部门的guid
                        String leadOuGuid = "";
                        if (auditSpPhase != null) {
                            leadOuGuid = auditSpPhase.getLeadouguid();
                        }
                        if (StringUtil.isNotBlank(leadOuGuid)) {
                            iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                                    userSession.getUserGuid(), userSession.getDisplayName(), targetuserguid, "",
                                    targetuserguid, "牵头部门发起的项目 " + itemName + " 征求评审已完成，请及时结束评审!", new Date());
                        }
                        else {
                            iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                                    userSession.getUserGuid(), userSession.getDisplayName(), targetuserguid, "",
                                    targetuserguid, "您发起的项目 " + itemName + " 评审已完成，请及时上传材料!", new Date());
                        }
                    }
                }
                else {
                    // 办结
                    auditSpISubappService.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YBJ, null);
                }
            }
            // TODO 这个地方需要改造一下，应该是一个循环判断的过程 ,
            // TODO 删除报错，需要咨询一下
            String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
            List<FrameUser> listUser = iJnCertInfo.getuserbyouguid(userSession.getOuGuid(),roleGuid);
//            List<FrameUser> listUser = userService.listUserByOuGuid(userSession.getOuGuid(), roleGuid, "", "", false,
//                    true, true, 3);
            for (FrameUser frameUser : listUser) {
                messageCenterService.deleteMessageByIdentifier(reviewGuid, frameUser.getUserGuid());
            }
            dataReview = null;
            addCallbackParam("msg", "评审完成！");
        }

    }

    public DataGridModel<AuditTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 7974759929168741717L;

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 获取已征求的的事项的记录
                    /*
                     * List<String> selectedtaskids =
                     * auditSpITaskService.getTaskIDBySubappGuid(subappGuid).
                     * getResult();
                     * List<String> basetaskguid = new ArrayList<>();
                     * if(selectedtaskids.size()>0){
                     * basetaskguid =
                     * iauditspbasetaskr.getBasetaskguidsBytaskids(
                     * selectedtaskids).getResult();
                     * }
                     */
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("phaseguid", phaseGuid);
                    List<AuditSpTask> auditSpListTemp = auditSpTaskService.getAllAuditSpTask(sql.getMap()).getResult();
                    // 根据spbasetaskguid
                    StringBuilder basetaskguids = new StringBuilder();
                    basetaskguids.append("'");
                    for (AuditSpTask auditSpTask : auditSpListTemp) {
                        // if(!basetaskguid.contains(auditSpTask.getBasetaskguid())){
                        basetaskguids.append(auditSpTask.getBasetaskguid()).append("','");
                        // }
                    }
                    basetaskguids.append("'");
                    // 获取所有事项数据
                    sql.clear();
                    sql.in("basetaskguid", basetaskguids.toString());
                    List<AuditSpBasetaskR> listr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sql.getMap())
                            .getResult();
                    // 获取所有事项的taskid
                    List<String> taskids = new ArrayList<>();
                    for (AuditSpBasetaskR auditspbasetaskr : listr) {
                        if (!taskids.contains(auditspbasetaskr.getTaskid())) {
                            taskids.add(auditspbasetaskr.getTaskid());
                        }
                    }

                    List<AuditTask> auditTasks = new ArrayList<>();
                    // 获取spitask中存在的事项id
                    List<String> spitaskIdlist = auditSpITaskService.getTaskIDByReviewguid(reviewGuid).getResult();
                    // 去除删除或禁用的事项
                    if (taskids != null && taskids.size() > 0) {
                        // 遍历spitask，去除taskids中已经评审的事项
                        for (String taskid : spitaskIdlist) {
                            if (taskids.contains(taskid)) {
                                taskids.remove(taskid);
                            }
                        }
                        sql = new SqlConditionUtil();
                        sql.eq("IS_EDITAFTERIMPORT", "1");
                        sql.eq("IS_ENABLE", "1");
                        sql.isBlankOrValue("IS_HISTORY", "0");
                        sql.in("task_id", "'" + StringUtil.join(taskids, "','") + "'");
                        sql.eq("ouguid", ouGuid);
                        sql.setSelectFields("task_id,taskname,rowguid");
                        auditTasks = auditTaskService.getAllTask(sql.getMap()).getResult();
                    }
                    // 设置里程碑标识 //设置是否主线事项
                    List<String> fxtaskids = iauditspbasetaskr.gettaskidsByBusinedssguid(businessGuid, "5").getResult();

                    // 获取一件事主题事项id列表
                    List<String> yjsBusinessTaskIdList = new ArrayList<>();
                    if (subapp != null && StringUtil.isNotBlank(subapp.getStr("yjsbusinessguid"))) {
                        String yjsBusinessGuid = subapp.getStr("yjsbusinessguid");
                        SqlConditionUtil sUtil = new SqlConditionUtil();
                        sUtil.setSelectFields("b.taskid");
                        sUtil.setInnerJoinTable("audit_sp_basetask_r b", "a.basetaskguid",
                                "b.basetaskguid inner join audit_task c on b.taskid = c.task_id");
                        sUtil.eq("a.businessguid", yjsBusinessGuid);
                        sUtil.eq("b.areacode", spInstance.getAreacode());
                        sUtil.eq("c.ouguid", ouGuid);
                        sUtil.isBlankOrValue("c.is_history", ZwfwConstant.CONSTANT_STR_ZERO);
                        sUtil.eq("c.is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
                        sUtil.eq("c.is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                        List<AuditSpTask> yjsBusinessTaskList = auditSpTaskService.getAllAuditSpTask(sUtil.getMap())
                                .getResult();
                        if (EpointCollectionUtils.isNotEmpty(yjsBusinessTaskList)) {
                            List<String> curTaskIdList = yjsBusinessTaskList.stream().map(AuditSpTask::getTaskid)
                                    .distinct().collect(Collectors.toList());
                            if (EpointCollectionUtils.isNotEmpty(curTaskIdList)) {
                                yjsBusinessTaskIdList.addAll(curTaskIdList);
                            }
                        }
                    }

                    for (AuditTask audittask : auditTasks) {
                        AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(audittask.getTask_id())
                                .getResult();
                        if (basetask != null) {
                            audittask.set("sflcbsx", basetask.getSflcbsx());
                        }
                        if (fxtaskids.contains(audittask.getTask_id())) {
                            audittask.put("ztsxlx", "0");
                        }
                        else {
                            audittask.put("ztsxlx", "1");
                        }

                        // 如果当前事项属于一件事主题事项列表，则设置为已勾选
                        if (yjsBusinessTaskIdList.contains(audittask.getTask_id())) {
                            audittask.put("yjsztsx", ZwfwConstant.CONSTANT_STR_ONE);
                        }
                        else {
                            audittask.put("yjsztsx", ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                    }

                    this.setRowCount(auditTasks.size());
                    return auditTasks;
                }
            };
        }
        return model;
    }

    public DataGridModel<FwxmInfo> getDataGridData1() {
        // 获得表格对象
        if (model1 == null) {
            model1 = new DataGridModel<FwxmInfo>()
            {
                @Override
                public List<FwxmInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("phaseguid", phaseGuid);
                    sql.eq("ouguid", ouGuid);
                    List<AuditSpTask> auditSpListTemp = auditSpTaskService.getAllAuditSpTask(sql.getMap()).getResult();
                    List<AuditSpTask> listSpTask = new ArrayList<>();
                    List<FwxmInfo> listFwxmInfo = new ArrayList<>();

                    // 去除删除或禁用的事项
                    if (auditSpListTemp != null && auditSpListTemp.size() > 0) {
                        String taskids = "";
                        List<String> taskidList = new ArrayList<>();
                        for (AuditSpTask auditSpTask : auditSpListTemp) {
                            taskids += "'" + auditSpTask.getTaskid() + "',";
                            taskidList.add(auditSpTask.getTaskid());
                        }
                        taskids = taskids.substring(0, taskids.length() - 1);
                        sql = new SqlConditionUtil();
                        sql.eq("IS_EDITAFTERIMPORT", "1");
                        sql.eq("IS_ENABLE", "1");
                        sql.isBlankOrValue("IS_HISTORY", "0");
                        sql.in("task_id", taskids);
                        sql.setSelectFields("task_id");
                        List<AuditTask> auditTasks = auditTaskService.getAllTask(sql.getMap()).getResult();
                        if (auditTasks != null && auditTasks.size() > 0) {
                            for (AuditTask auditTask : auditTasks) {
                                listSpTask.add(auditSpListTemp.get(taskidList.indexOf(auditTask.getTask_id())));
                            }
                        }
                    }
                    for (AuditSpTask sptask : listSpTask) {
                        // sptask.put("taskguid",
                        // auditTaskService.getUseTaskAndExtByTaskid(sptask.getTaskid()).getResult().getRowguid());
                        FwxmInfo fwxmInfo = fwxminfoService.getfwxmbytaskguid(sptask.getTaskid());
                        if (fwxmInfo != null) {
                            listFwxmInfo.add(fwxmInfo);
                        }
                    }
                    this.setRowCount(listFwxmInfo == null ? 0 : listFwxmInfo.size());
                    return listFwxmInfo == null ? new ArrayList<>() : listFwxmInfo;
                }
            };
        }
        return model1;
    }

    public DataGridModel<Record> getspitaskmodel() {
        // 获得表格对象
        if (spitaskmodel == null) {
            spitaskmodel = new DataGridModel<Record>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 7974759929168741717L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> spitasklist = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid)
                            .getResult();
                    List<AuditSpIReview> reviewlist = auditSpIReviewService.getReviewBySubappGuid(subappGuid)
                            .getResult();
                    List<Record> rtnlist = new ArrayList<>();
                    // 去除未评审的征求
                    reviewlist.removeIf(a -> {
                        if (ZwfwConstant.LHSP_Status_YPS.equals(a.getStatus())) {
                            return false;
                        }
                        else {
                            return true;
                        }
                    });
                    Map<String, List<AuditSpITask>> reviewtasklist = spitasklist.stream()
                            .collect(Collectors.groupingBy(x -> x.getReviewguid()));
                    // 将reviewlist转换成map
                    StringBuilder taskname = new StringBuilder();
                    for (AuditSpIReview auditSpIReview : reviewlist) {
                        Record record = new Record();
                        FrameOu frameOu = ouService.getOuByOuGuid(auditSpIReview.getOuguid());
                        FrameOuExtendInfo frameOuExtendInfo = ouService
                                .getFrameOuExtendInfo(auditSpIReview.getOuguid());
                        AuditOrgaArea auditOrgaArea = areaImpl.getAreaByAreacode(frameOuExtendInfo.get("areacode"))
                                .getResult();
                        record.put("xiaquname", auditOrgaArea.getXiaquname());
                        record.put("ouname", frameOu.getOuname());
                        record.put("pingshenguser",
                                userService.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                        record.put("pingshengdate", auditSpIReview.getPingshengdate());
                        record.put("pingshenopinion", auditSpIReview.getPingshenopinion());
                        List<AuditSpITask> reviewtask = reviewtasklist.get(auditSpIReview.getRowguid());
                        taskname.setLength(0);
                        if (reviewtask != null && reviewtask.size() > 0) {
                            for (AuditSpITask auditSpITask : reviewtask) {
                                if (StringUtil.isNotBlank(auditSpITask.getTaskname())) {
                                    taskname.append(auditSpITask.getTaskname()).append("<br/>");
                                }
                            }
                        }
                        record.put("taskname", taskname.toString());
                        rtnlist.add(record);
                    }
                    // 按照评审时间排序
                    rtnlist.sort((a, b) -> {
                        if (a.getDate("pingshengdate").getTime() - b.getDate("pingshengdate").getTime() > 0) {
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    });

                    return rtnlist;
                }
            };
        }
        return spitaskmodel;
    }

    public DataGridModel<AuditProject> getprojectmodel() {
        if (projectmodel == null) {
            projectmodel = new DataGridModel<AuditProject>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditProject> fetchData(int arg0, int arg1, String arg2, String arg3) {
                    List<String> fxtaskids = iauditspbasetaskr.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
                    if (fxtaskids.size() == 0) {
                        setRowCount(0);
                        return new ArrayList<>();
                    }
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.in("task_id", StringUtil.joinSql(fxtaskids));
                    sqlc.eq("biguid", biGuid);
                    List<AuditProject> lists = iauditproject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
                    for (AuditProject auditProject : lists) {
                        // 设置阶段名称
                        String subappguid = auditProject.getSubappguid();
                        Record r = auditSpISubappService.getPhaseAndSubdetail("phasename", subappguid).getResult();
                        if (r != null) {
                            auditProject.put("phasename", r.getStr("phasename"));
                        }
                        Integer status = auditProject.getStatus();
                        String sparetime = "--";
                        if (status >= ZwfwConstant.BANJIAN_STATUS_YSL && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ
                                && !ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())) {
                            if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_pause()) {
                                sparetime = "暂停计时";// 办件处于暂停计时状态
                            }
                            else {
                                AuditProjectSparetime auditprojectsparetime = sparetimeService
                                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                                if (auditprojectsparetime != null) {
                                    sparetime = CommonUtil.getSpareTimes(auditprojectsparetime.getSpareminutes());
                                    if (auditprojectsparetime.getSpareminutes() < 0) {
                                        sparetime = "超时" + sparetime;
                                    }
                                }
                            }
                        }
                        auditProject.put("sparetime", sparetime);
                    }
                    return lists;
                }

            };
        }
        return projectmodel;
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照类型", null, false));
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

    public List<Map<String, Object>> getoulist() {
        List<AuditSpTask> listsptask = auditSpTaskService.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
        List<String> basetaskguids = new ArrayList<>();
        for (AuditSpTask auditsptask : listsptask) {
            if (!basetaskguids.contains(auditsptask.getBasetaskguid())) {
                basetaskguids.add(auditsptask.getBasetaskguid());
            }
        }
        // 根据basetaskguids 获取审批事项部门
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.in("rowguid", "'" + StringUtil.join(basetaskguids, "','") + "'");
        List<Record> listou = iauditspbasetask.getDistinctOuByCondition(sqlc.getMap()).getResult();
        // 根据审批事项部门生成记录
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        for (Record ou : listou) {
            // 处理征求部门的逻辑
            List<Record> listrou = iauditspbasetaskr.getTaskidlistbyBasetaskOuname(ou.getStr("ouname"), basetaskguids)
                    .getResult();
            // 转换部门多选的下拉数据
            List<String> oustem = new ArrayList<>();
            List<Map<String, Object>> list = new ArrayList<>();
            for (Record record : listrou) {
                AuditTask audittask = auditTaskService.selectUsableTaskByTaskID(record.getStr("taskid")).getResult();
                if (audittask == null || oustem.contains(audittask.getOuguid())) {
                    continue;
                }
                oustem.add(audittask.getOuguid());
                Map<String, Object> map = new HashMap<>();
                if (StringUtil.isNotBlank(audittask.getOuguid()) && !(audittask.getOuguid().equals(ouGuid))) {
                    map.put("id", audittask.getOuguid());
                    map.put("text", record.getStr("xiaquname") + "-" + audittask.getOuname());
                    list.add(map);
                }
            }
            if (!oustem.contains(ouGuid)) {

            }
            else {
                rtnlist.addAll(list);
                continue;
            }
        }
        List<AuditSpIReview> list = auditSpIReviewService.getReviewBySubappGuid(subappGuid).getResult();
        // 去除已经存在评审征求的部门转移
        rtnlist.removeIf(a -> {
            for (AuditSpIReview auditSpIReview : list) {
                if (a.get("id") != null && a.get("id").toString().equals(auditSpIReview.getOuguid())) {
                    if (ZwfwConstant.LHSP_Status_DPS.equals(auditSpIReview.getStatus())) {
                        return true;
                    }
                }
            }
            return false;
        });

        rtnlist = rtnlist.stream().filter(distinctByKey(b -> b.get("id"))).collect(Collectors.toList());

        return rtnlist;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void passon(String ouguidlist) {

        AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(phaseGuid).getResult();
        // 发起事项征求，没有牵头部门的话，走正常的通知所有窗口部门的流程
        String[] ouguid = ouguidlist.split(",");
        // 数组去重
        Map<String, Object> map = new HashMap<String, Object>();
        for (String str : ouguid) {
            map.put(str, str);
        }
        // 返回一个包含所有对象的指定类型的数组
        ouguid = map.keySet().toArray(new String[1]);
        if (ouguid != null && ouguid.length > 0 && StringUtil.isNotBlank(ouguid[0])) {
            for (String ou : ouguid) {
                String reviewGuid = auditSpIReviewService.addReview(dataBean.getBiguid(), phaseGuid, ou, subappGuid)
                        .getResult();
                // 给窗口负责人发送通知提醒
                String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
                List<FrameUser> listUser = userService.listUserByOuGuid(ou, roleGuid, "", "", false, true, true, 3);
                String targetUserGuid = userSession.getUserGuid();
                String url = "epointzwfw/auditsp/auditsphandle/handlereview?reviewGuid=" + reviewGuid
                        + "&targetuserguid=" + targetUserGuid;
                for (FrameUser user : listUser) {
                    messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                            "【事项征求】" + subapp.getSubappname(), IMessagesCenterService.MESSAGETYPE_WAIT,
                            user.getUserGuid(), user.getDisplayName(), userSession.getUserGuid(),
                            userSession.getDisplayName(), "事项征求", url, ou, "", 1, "", "", reviewGuid,
                            reviewGuid.substring(0, 1), new Date(), "", userSession.getUserGuid(), "", "");
                }
            }
            // TODO 删除报错，需要咨询一下
            String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
            List<FrameUser> listUser = userService.listUserByOuGuid(userSession.getOuGuid(), roleGuid, "", "", false,
                    true, true, 3);
            for (FrameUser frameUser : listUser) {
                messageCenterService.deleteMessageByIdentifier(reviewGuid, frameUser.getUserGuid());
            }
        }
        else {
            addCallbackParam("msg", "转移部门失败！");
        }

    }

    public String getYijiaoopinion() {
        return yijiaoopinion;
    }

    public void setYijiaoopinion(String yijiaoopinion) {
        this.yijiaoopinion = yijiaoopinion;
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

    public String getOuguids() {
        return ouguids;
    }

    public void setOuguids(String ouguids) {
        this.ouguids = ouguids;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName

            // clientGuid一般是地址中获取到的，此处只做参考使用
            String itemguid = null;
            if (dataBean != null) {
                itemguid = dataBean.get("clientguid");
                if (itemguid == null) {
                    itemguid = dataBean.getRowguid();
                }
            }
            if (itemguid == null) {
                itemguid = "1111";
            }
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(itemguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }

    public void setFileUploadModel(FileUploadModel9 fileUploadModel) {
        this.fileUploadModel = fileUploadModel;
    }

    public String showblue() {
        String xmzb = dataBean.getStr("xmzb");
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
        }
        else {
            str = "error";
        }
        return str;
    }
}

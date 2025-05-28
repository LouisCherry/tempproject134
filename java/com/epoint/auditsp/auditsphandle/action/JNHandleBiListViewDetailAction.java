package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditresource.auditspisubappopinion.api.IAuditSpISubappOpinionService;
import com.epoint.auditresource.auditspisubappopinion.api.entity.AuditSpISubappOpinion;
import com.epoint.auditsp.auditsphandle.api.IJnAuditSpInstance;
import com.epoint.auditsp.auditsphandle.api.ISyncXfService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
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
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.jnrestforshentu.api.IJnShentuService;
import com.epoint.jnzwfw.jndtanddw.api.IJnDtAndDwService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;

/**
 * 项目基本信息表新增页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnhandlebilistviewdetailaction")
@Scope("request")
public class JNHandleBiListViewDetailAction extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1313301881620639339L;
    /**
     * 项目库API
     */

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;

    /**
     * 项目申报日期
     */
    private Date createdate;

    /**
     * 项目初审日期
     */
    private Date pingshengdate;

    /**
     * 事项与材料情况
     */
    private String materialdesc = "";

    /**
     * 材料提交情况
     */
    private String materialsubmitdesc = "";

    /**
     * biguid
     */
    private String biGuid = "";

    private String yewuguid = "";

    private List<AuditSpITask> spITasks = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpIReview> modelReview = null;

    private DataGridModel<AuditSpITask> modelTask = null;

    private DataGridModel<Record> modelXf = null;

    private DataGridModel<AuditSpISubappOpinion> modelOpinion = null;

    private DataGridModel<AuditSpISubappOpinion> modelFaq = null;

    private FileUploadModel9 sltfileUploadModel;

    private FileUploadModel9 imgfileUploadModel;

    private FileUploadModel9 videofileUploadModel;

    private FileUploadModel9 tushenfileUploadModel;

    private FileUploadModel9 budongchanfileUploadModel;
    private FileUploadModel9 lxpfOnefileUploadModel;
    private FileUploadModel9 lxpfTwofileUploadModel;
    private FileUploadModel9 lxpfThreefileUploadModel;
    private FileUploadModel9 lxpfFourfileUploadModel;
    private String sltcliengguid = "";
    private String bdcliengguid = "";
    private String lxpfliengguid1 = "";
    private String lxpfliengguid2 = "";
    private String lxpfliengguid3 = "";
    private String lxpfliengguid4 = "";
    private String imgcliengguid = "";
    private String videocliengguid = "";
    private String tushencliengguid = "";

    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    @Autowired
    private IAuditSpInstance auditSpInstanceService;

    @Autowired
    private IAuditSpIReview auditSpIReviewService;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditSpPhase auditSpPhaseService;

    @Autowired
    private IAuditSpISubapp auditSpISubappService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuditProject projectService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditProjectSparetime sparetimeService;

    @Autowired
    private IAuditSpISubappOpinionService auditSpISubappOpinionService;

    @Autowired
    private IJnAuditSpInstance jnAuditSpInstance;

    @Autowired
    private IJnShentuService iJnShentuService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IJnDtAndDwService iJnDtAndDwService;

    /**
     * 项目库API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private ISyncXfService syncXfService;

    /**
     * 3.0新增
     */
    /**
     * 表格控件model
     */
    private DataGridModel<SpglQypgxxb> qypgModel;
    private DataGridModel<Record> dtinfoModel;
    private DataGridModel<ParticipantsInfo> jsdwModel;
    private DataGridModel<ParticipantsInfo> sjdwModel;
    private DataGridModel<ParticipantsInfo> kcdwModel;
    private DataGridModel<ParticipantsInfo> sgdwModel;
    private DataGridModel<ParticipantsInfo> jldwModel;
    private DataGridModel<ParticipantsInfo> zljcdwModel;

    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;
    @Autowired
    public IDantiSubRelationService iDantiSubRelationService;
    @Autowired
    private IDwgcInfoService dwgcInfoService;
    @Autowired
    private IParticipantsInfoService iParticipantsInfoService;

    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("guid");
        Record record = iJnShentuService.getTsProjectInfoByRowguid(biGuid);
        if (record != null) {
            addCallbackParam("assignee", record.getStr("assignee"));
            addCallbackParam("orgname", record.getStr("orgname"));
            addCallbackParam("signtime", record.getDate("signtime"));
            addCallbackParam("handlecomments", record.getStr("handlecomments"));
            addCallbackParam("bdcassignee", record.getStr("bdcassignee"));
            addCallbackParam("bdcorgname", record.getStr("bdcorgname"));
            addCallbackParam("bdchandlecomments", record.getStr("bdchandlecomments"));
            addCallbackParam("bdcsigntime", record.getDate("bdcsigntime"));
        }
        AuditRsItemBaseinfo auditRsItemBaseinfo = iJnShentuService.getTsClientguidByRowguid(biGuid);
        if (auditRsItemBaseinfo != null) {
            tushencliengguid = auditRsItemBaseinfo.getRowguid();

        }
        AuditSpInstance spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
            dataBean = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid()).getResult();
            createdate = spInstance.getCreatedate();
            imgcliengguid = spInstance.getStr("imgcliengguid");
            sltcliengguid = spInstance.getStr("sltcliengguid");
            bdcliengguid = spInstance.getStr("bdcliengguid");
            lxpfliengguid1 = spInstance.getStr("lxpfliengguid1");
            lxpfliengguid2 = spInstance.getStr("lxpfliengguid2");
            lxpfliengguid3 = spInstance.getStr("lxpfliengguid3");
            lxpfliengguid4 = spInstance.getStr("lxpfliengguid4");
            videocliengguid = spInstance.getStr("videocliengguid");
            if (StringUtil.isBlank(imgcliengguid)) {
                imgcliengguid = UUID.randomUUID().toString();
                spInstance.set("imgcliengguid", imgcliengguid);
            }
            if (StringUtil.isBlank(sltcliengguid)) {
                sltcliengguid = UUID.randomUUID().toString();
                spInstance.set("sltcliengguid", sltcliengguid);
            }
            if (StringUtil.isBlank(bdcliengguid)) {
                bdcliengguid = UUID.randomUUID().toString();
                spInstance.set("bdcliengguid", bdcliengguid);
                jnAuditSpInstance.updateSpInstance(spInstance);
            }
            if (StringUtil.isBlank(lxpfliengguid1)) {
                lxpfliengguid1 = UUID.randomUUID().toString();
                spInstance.set("lxpfliengguid1", lxpfliengguid1);
                jnAuditSpInstance.updateSpInstance(spInstance);
            }
            if (StringUtil.isBlank(lxpfliengguid2)) {
                lxpfliengguid2 = UUID.randomUUID().toString();
                spInstance.set("lxpfliengguid2", lxpfliengguid2);
                jnAuditSpInstance.updateSpInstance(spInstance);
            }
            if (StringUtil.isBlank(lxpfliengguid3)) {
                lxpfliengguid3 = UUID.randomUUID().toString();
                spInstance.set("lxpfliengguid3", lxpfliengguid3);
                jnAuditSpInstance.updateSpInstance(spInstance);
            }
            if (StringUtil.isBlank(lxpfliengguid4)) {
                lxpfliengguid4 = UUID.randomUUID().toString();
                spInstance.set("lxpfliengguid4", lxpfliengguid4);
                jnAuditSpInstance.updateSpInstance(spInstance);
            }
            if (StringUtil.isBlank(videocliengguid)) {
                videocliengguid = UUID.randomUUID().toString();
                spInstance.set("videocliengguid", videocliengguid);
                jnAuditSpInstance.updateSpInstance(spInstance);
            }

            yewuguid = spInstance.getYewuguid();
            AuditRsItemBaseinfo item = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid())
                    .getResult();
            if (item != null) {
                addCallbackParam("xmdm", item.getItemcode());
                if ("1".equals(item.getStr("is_qianqi"))) {
                    addCallbackParam("hasqianqi", "1");
                }

                String lxpfcliengguid = item.getStr("lxpfcliengguid");
                String jsgcfliengguid = item.getStr("jsgcfliengguid");
                boolean fileaccth = false;
                if (StringUtil.isBlank(lxpfcliengguid)) {
                    lxpfcliengguid = UUID.randomUUID().toString();
                    item.set("lxpfcliengguid", lxpfcliengguid);
                    fileaccth = true;
                }

                if (StringUtil.isBlank(jsgcfliengguid)) {
                    jsgcfliengguid = UUID.randomUUID().toString();
                    item.set("jsgcfliengguid", jsgcfliengguid);
                    fileaccth = true;

                }

                if (fileaccth) {
                    rsItemBaseinfoService.updateAuditRsItemBaseinfo(item);
                }

                addCallbackParam("lxpfcliengguid", lxpfcliengguid);
                addCallbackParam("jsgcfliengguid", jsgcfliengguid);
                // 判断该项目是否办过消防办件
                if (StringUtil.isNotBlank(item.getBiguid())) {
                    String flag = "0";
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("biguid", item.getBiguid());
                    List<AuditProject> list = projectService.getAuditProjectListByCondition(conditionUtil.getMap())
                            .getResult();
                    if (list != null && !list.isEmpty()) {
                        for (AuditProject auditProject : list) {
                            AuditTask task = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                                    .getResult();
                            if (task != null && ("特殊建设工程消防验收".equals(task.getTaskname())
                                    || "其他建设工程消防验收备案抽查".equals(task.getTaskname())
                                    || "建设工程消防设计审查".equals(task.getTaskname())
                                    || "建设工程消防设计审查（设区的市级权限）（新设）".equals(task.getTaskname())
                                    || "建设工程消防验收（设区的市级权限）（新设）".equals(task.getTaskname())
                                    || "建设工程消防验收备案".equals(task.getTaskname())
                                    || "建设工程消防设计审查（县级权限）（新设）".equals(task.getTaskname())
                                    || "建设工程消防验收（县级权限）（新设）".equals(task.getTaskname()))) {
                                flag = "1";
                            }
                        }
                        if ("1".equals(flag)) {
                            addCallbackParam("isXF", "1");
                        }
                        else {
                            addCallbackParam("isXF", "0");
                        }
                    }
                    else {
                        addCallbackParam("isXF", "0");
                    }
                }
                else {
                    addCallbackParam("isXF", "0");
                }
            }
        }
        List<AuditSpIReview> auditSpIReviews = auditSpIReviewService.getReviewByBIGuid(biGuid).getResult();
        if (auditSpIReviews != null && auditSpIReviews.size() > 0) {
            pingshengdate = auditSpIReviews.get(0).getPingshengdate();
        }
        spITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid).getResult();
        String taskCount = "0";
        String materialCount = "0";
        // 电子材料
        int dzMaterial = 0;
        // 纸质材料
        int zzMaterial = 0;
        // 容缺材料
        int rqMaterial = 0;
        // 补正材料
        int bzMaterial = 0;
        if (spITasks != null) {
            taskCount = String.valueOf(spITasks.size());
        }
        List<AuditSpIMaterial> auditSpIMaterials = auditSpIMaterialService.getSpIMaterialByBIGuid(biGuid).getResult();
        if (auditSpIMaterials != null) {
            materialCount = String.valueOf(auditSpIMaterials.size());
            for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                if ("15".equals(auditSpIMaterial.getStatus())) {
                    zzMaterial++;
                }
                else if ("20".equals(auditSpIMaterial.getStatus())) {
                    dzMaterial++;
                }
                else if ("25".equals(auditSpIMaterial.getStatus())) {
                    zzMaterial++;
                    dzMaterial++;
                }
                if ("10".equals(auditSpIMaterial.getNecessity()) && "1".equals(auditSpIMaterial.getAllowrongque())) {
                    rqMaterial++;
                }
                if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                    bzMaterial++;
                }
            }
        }
        materialdesc = "共" + taskCount + "个事项，" + materialCount + "份材料";
        materialsubmitdesc = "提交电子材料" + String.valueOf(dzMaterial) + "份 提交纸质材料" + String.valueOf(zzMaterial)
                + "份 容缺（暂无）" + String.valueOf(rqMaterial) + "份 需整改" + String.valueOf(bzMaterial) + "份";

        String subappguid = getRequestParameter("subappguid");
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp != null) {
            String itemGuid = auditSpISubapp.getYewuguid();
            if (StringUtil.isNotBlank(itemGuid)) {
                this.addCallbackParam("itemGuid", spInstance.getYewuguid());
                this.addCallbackParam("subappGuid", subappguid);
            }
        }

        /* 3.0新增逻辑 */
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

    public DataGridModel<AuditSpIReview> getDataGridReview() {
        // 获得表格对象
        if (modelReview == null) {
            modelReview = new DataGridModel<AuditSpIReview>()
            {
                @Override
                public List<AuditSpIReview> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("biguid", biGuid);
                    sortField = "pingshengdate";
                    sortOrder = "desc";
                    PageData<AuditSpIReview> pageData = auditSpIReviewService
                            .getReviewBByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();

                    for (AuditSpIReview auditSpIReview : pageData.getList()) {
                        FrameOu ou = ouService.getOuByOuGuid(auditSpIReview.getOuguid());
                        if (ou != null) {
                            auditSpIReview.put("ouname", ou.getOuname());
                        }
                        else {
                            auditSpIReview.put("ouname", "--");
                        }

                        AuditSpPhase auditSpPhase = auditSpPhaseService
                                .getAuditSpPhaseByRowguid(auditSpIReview.getPhaseguid()).getResult();
                        if (auditSpPhase != null) {
                            auditSpIReview.put("phasename", auditSpPhase.getPhasename());
                        }
                        AuditSpISubapp auditSpISubapp = auditSpISubappService
                                .getSubappByGuid(auditSpIReview.getSubappguid()).getResult();
                        if (auditSpISubapp != null) {
                            auditSpIReview.put("subappname", auditSpISubapp.getSubappname());
                        }
                        auditSpIReview.put("username",
                                userService.getUserNameByUserGuid(auditSpIReview.getPingshenguserguid()));
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return modelReview;
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = new ArrayList<>();
                    if (spITasks != null && spITasks.size() > 0) {
                        spITasks.sort(
                                (a, b) -> EpointDateUtil.compareDateOnDay(a.getOperatedate(), b.getOperatedate()));
                        for (AuditSpITask auditSpITask : spITasks) {
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                AuditSpPhase auditSpPhase = auditSpPhaseService
                                        .getAuditSpPhaseByRowguid(auditSpITask.getPhaseguid()).getResult();
                                if (auditSpPhase != null) {
                                    auditSpITask.put("phasename", auditSpPhase.getPhasename());
                                }
                                AuditSpISubapp auditSpISubapp = auditSpISubappService
                                        .getSubappByGuid(auditSpITask.getSubappguid()).getResult();
                                if (auditSpISubapp != null) {
                                    auditSpITask.put("subappname", auditSpISubapp.getSubappname());
                                }
                                String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,is_pause ";
                                AuditProject auditProject = projectService.getAuditProjectByRowGuid(fields,
                                        auditSpITask.getProjectguid(), null)
                                        .getResult();
                                if (auditProject != null) {
                                    int status = auditProject.getStatus();
                                    auditSpITask.put("status",
                                            codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));

                                    String sparetime = "--";
                                    if (status >= ZwfwConstant.BANJIAN_STATUS_YSL
                                            && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ && !ZwfwConstant.ITEMTYPE_JBJ
                                                    .equals(auditProject.getTasktype().toString())) {
                                        if (StringUtil.isNotBlank(auditProject.getIs_pause())
                                                && ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_pause()) {
                                            sparetime = "暂停计时";// 办件处于暂停计时状态
                                        }
                                        else {
                                            AuditProjectSparetime auditprojectsparetime = sparetimeService
                                                    .getSparetimeByProjectGuid(auditSpITask.getProjectguid())
                                                    .getResult();
                                            if (auditprojectsparetime != null) {
                                                sparetime = CommonUtil
                                                        .getSpareTimes(auditprojectsparetime.getSpareminutes());
                                            }
                                        }
                                    }
                                    auditSpITask.put("sparetime", sparetime);
                                }
                                auditSpITasks.add(auditSpITask);
                            }
                        }
                    }
                    this.setRowCount(auditSpITasks.size());
                    return auditSpITasks;
                }
            };
        }
        return modelTask;
    }

    // 新增消防推送模块
    public DataGridModel<Record> getXfDataGrid() {
        // 获得表格对象
        if (modelXf == null) {
            modelXf = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    AuditSpInstance spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
                    AuditRsItemBaseinfo item = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid()).getResult();
                    String xmdm = item.getItemcode();
                    PageData<Record> pageData = syncXfService.geXflistByXmdm(first, pageSize, sortField, sortOrder,
                            xmdm);
                    List<Record> list = pageData.getList();
                    for (Record Xfinfo : list) {
                        // 把失败原因中的引号去除
                        if (StringUtil.isNotBlank(Xfinfo.getStr("SBYY"))) {
                            Xfinfo.set("SBYY", Xfinfo.getStr("SBYY").replace("\'", ""));
                        }
                        if ("4".equals(Xfinfo.getStr("SJSCZT"))) {
                            Xfinfo.set("SJSCZT", "成功");
                        }
                        else {
                            Xfinfo.set("SJSCZT", "失败");
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return list;
                }
            };
        }
        return modelXf;
    }

    public DataGridModel<AuditSpISubappOpinion> getDataGridOpinion() {
        // 获得表格对象
        if (modelOpinion == null) {
            modelOpinion = new DataGridModel<AuditSpISubappOpinion>()
            {
                @Override
                public List<AuditSpISubappOpinion> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<AuditSpISubappOpinion> auditSpIOpinion = new ArrayList<>();
                    String sql = "select * from audit_sp_i_subapp_opinion where biguid=? and type='1' order by createdate desc";
                    auditSpIOpinion = auditSpISubappOpinionService.findList(sql, first * pageSize, pageSize,
                            new Object[] {biGuid });
                    this.setRowCount(auditSpIOpinion.size());
                    return auditSpIOpinion;
                }
            };
        }
        return modelOpinion;
    }

    public DataGridModel<AuditSpISubappOpinion> getDataGridFaq() {
        // 获得表格对象
        if (modelFaq == null) {
            modelFaq = new DataGridModel<AuditSpISubappOpinion>()
            {
                @Override
                public List<AuditSpISubappOpinion> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<AuditSpISubappOpinion> auditSpIOpinion = new ArrayList<>();
                    String sql = "select * from audit_sp_i_subapp_opinion where biguid=? and type<>'1' order by createdate desc";
                    auditSpIOpinion = auditSpISubappOpinionService.findList(sql, first * pageSize, pageSize,
                            new Object[] {biGuid });
                    this.setRowCount(auditSpIOpinion.size());
                    return auditSpIOpinion;
                }
            };
        }
        return modelFaq;
    }

    // 项目缩略图
    public FileUploadModel9 getSltfileUploadModel() {
        if (sltfileUploadModel == null) {
            sltfileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(sltcliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return sltfileUploadModel;
    }

    public void setSltfileUploadModel(FileUploadModel9 sltfileUploadModel) {
        this.sltfileUploadModel = sltfileUploadModel;
    }

    // 项目图片
    public FileUploadModel9 getImgfileUploadModel() {
        if (imgfileUploadModel == null) {
            imgfileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(imgcliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return imgfileUploadModel;
    }

    public void setImgfileUploadModel(FileUploadModel9 imgfileUploadModel) {
        this.imgfileUploadModel = imgfileUploadModel;
    }

    // 项目视频
    public FileUploadModel9 getVideofileUploadModel() {
        if (videofileUploadModel == null) {
            videofileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(videocliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return videofileUploadModel;
    }

    // 审图附件
    public FileUploadModel9 getTuShenfileUploadModel() {
        if (tushenfileUploadModel == null) {
            tushenfileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(tushencliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return tushenfileUploadModel;
    }

    // 不动产
    public FileUploadModel9 getDuDongChanfileUploadModel() {
        if (budongchanfileUploadModel == null) {
            budongchanfileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(bdcliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return budongchanfileUploadModel;
    }

    public FileUploadModel9 getLxpfOnefileUploadModel() {
        if (lxpfOnefileUploadModel == null) {
            lxpfOnefileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(lxpfliengguid1, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return lxpfOnefileUploadModel;
    }

    public FileUploadModel9 getLxpfTwofileUploadModel() {
        if (lxpfTwofileUploadModel == null) {
            lxpfTwofileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(lxpfliengguid2, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return lxpfTwofileUploadModel;
    }

    public FileUploadModel9 getLxpfThreefileUploadModel() {
        if (lxpfThreefileUploadModel == null) {
            lxpfThreefileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(lxpfliengguid3, null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return lxpfThreefileUploadModel;
    }

    public FileUploadModel9 getLxpfFourfileUploadModel() {
        if (lxpfFourfileUploadModel == null) {
            lxpfFourfileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(lxpfliengguid4, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return lxpfFourfileUploadModel;
    }

    public FileUploadModel9 getTushenfileUploadModel() {
        return tushenfileUploadModel;
    }

    public void setTushenfileUploadModel(FileUploadModel9 tushenfileUploadModel) {
        this.tushenfileUploadModel = tushenfileUploadModel;
    }

    public void setVideofileUploadModel(FileUploadModel9 videofileUploadModel) {
        this.videofileUploadModel = videofileUploadModel;
    }

    public void deleteOpinion(String rowguid) {
        auditSpISubappOpinionService.deleteByGuid(rowguid);
        addCallbackParam("ok", "已删除");
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

    public Date getPingshengdate() {
        return pingshengdate;
    }

    public void setPingshengdate(Date pingshengdate) {
        this.pingshengdate = pingshengdate;
    }

    public String getMaterialdesc() {
        return materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        this.materialdesc = materialdesc;
    }

    public String getMaterialsubmitdesc() {
        return materialsubmitdesc;
    }

    public void setMaterialsubmitdesc(String materialsubmitdesc) {
        this.materialsubmitdesc = materialsubmitdesc;
    }

    public String getSltcliengguid() {
        return sltcliengguid;
    }

    public void setSltcliengguid(String sltcliengguid) {
        this.sltcliengguid = sltcliengguid;
    }

    public String getImgcliengguid() {
        return imgcliengguid;
    }

    public void setImgcliengguid(String imgcliengguid) {
        this.imgcliengguid = imgcliengguid;
    }

    public String getVideocliengguid() {
        return videocliengguid;
    }

    public void setVideocliengguid(String videocliengguid) {
        this.videocliengguid = videocliengguid;
    }

    public String showblue() {
        String dlxx = dataBean.getStr("xmzb");
        String projectName = dataBean.getItemname();
        String projectCode = dataBean.getItemcode();

        String str = "";
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        params.put("dlxx", dlxx);
        params.put("projectName", projectName);
        params.put("projectCode", projectCode);
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

    public DataGridModel<SpglQypgxxb> getQypgDatagrid() {
        // 获得表格对象
        if (qypgModel == null) {
            qypgModel = new DataGridModel<SpglQypgxxb>()
            {
                @Override
                public List<SpglQypgxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String itemGuid = (String) getRequestContext().getResult().customData().get("itemGuid");
                    if (StringUtil.isBlank(itemGuid)) {
                        itemGuid = getDataBean().getRowguid();
                    }
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.setLeftJoinTable("spgl_qypgxxb_edit_r b", "a.rowguid", "b.qypgguid");
                    sql.eq("b.pre_itemguid", itemGuid);
                    sql.setSelectFields("a.*,b.rowguid rguid,b.subappguid");
                    PageData<SpglQypgxxb> pageData = iSpglQypgxxbService
                            .getAuditSpDanitemByPage(sql.getMap(), first, pageSize, "b.createdate", sortOrder)
                            .getResult();
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

    public DataGridModel<Record> getDtInfoDatagrid() {
        // 获得表格对象
        if (dtinfoModel == null) {
            dtinfoModel = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String itemGuid = (String) getRequestContext().getResult().customData().get("itemGuid");
                    if (StringUtil.isBlank(itemGuid)) {
                        itemGuid = getDataBean().getRowguid();
                    }

                    List<Record> dtInfoList = iJnDtAndDwService.getDtInfoByItemGuid(itemGuid);
                    for (Record dantiInfo : dtInfoList) {
                        if (StringUtil.isNotBlank(dantiInfo.getStr("GCLB"))) {
                            String name = getNameByCode(dantiInfo.getStr("GCLB"));
                            dantiInfo.put("gclb", name);
                        }
                        if (StringUtil.isNotBlank(dantiInfo.getStr("GCXZ"))) {
                            if (dantiInfo.getStr("GCXZ").contains(",")) {
                                String[] list = dantiInfo.getStr("GCXZ").split(",");
                                StringBuilder sb = new StringBuilder();
                                for (String code : list) {
                                    String name = getItemText(code, "工程性质");
                                    sb.append(name);
                                    sb.append(";");
                                }
                                dantiInfo.put("gcxz", sb.substring(0, sb.toString().length() - 1));
                            }
                            else {
                                dantiInfo.put("gcxz", getItemText(dantiInfo.getStr("GCXZ"), "工程性质"));
                            }

                        }

                        if (StringUtil.isNotBlank(dantiInfo.getStr("GONGCHENGGUID"))) {
                            DwgcInfo dwgcInfo = dwgcInfoService.find(dantiInfo.getStr("GONGCHENGGUID"));
                            dantiInfo.put("gongchengname", dwgcInfo.getGongchengname());
                        }
                    }
                    this.setRowCount(dtInfoList.size());
                    return dtInfoList;
                }
            };
        }
        return dtinfoModel;
    }

    public DataGridModel<ParticipantsInfo> getJsdw() {
        // 获得表格对象
        if (jsdwModel == null) {
            jsdwModel = new DataGridModel<ParticipantsInfo>()
            {
                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<ParticipantsInfo> dwList = getParticipantsInfos("31");
                    this.setRowCount(dwList.size());
                    return dwList;
                }
            };
        }
        return jsdwModel;
    }

    public DataGridModel<ParticipantsInfo> getSjdw() {
        // 获得表格对象
        if (sjdwModel == null) {
            sjdwModel = new DataGridModel<ParticipantsInfo>()
            {
                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<ParticipantsInfo> dwList = getParticipantsInfos("2");
                    this.setRowCount(dwList.size());
                    return dwList;
                }
            };
        }
        return sjdwModel;
    }

    public DataGridModel<ParticipantsInfo> getKcdw() {
        // 获得表格对象
        if (kcdwModel == null) {
            kcdwModel = new DataGridModel<ParticipantsInfo>()
            {
                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<ParticipantsInfo> dwList = getParticipantsInfos("1");
                    this.setRowCount(dwList.size());
                    return dwList;
                }
            };
        }
        return kcdwModel;
    }

    public DataGridModel<ParticipantsInfo> getSgdw() {
        // 获得表格对象
        if (sgdwModel == null) {
            sgdwModel = new DataGridModel<ParticipantsInfo>()
            {
                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<ParticipantsInfo> dwList = getParticipantsInfos("3");
                    this.setRowCount(dwList.size());
                    return dwList;
                }
            };
        }
        return sgdwModel;
    }

    public DataGridModel<ParticipantsInfo> getJldw() {
        // 获得表格对象
        if (jldwModel == null) {
            jldwModel = new DataGridModel<ParticipantsInfo>()
            {
                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<ParticipantsInfo> dwList = getParticipantsInfos("4");
                    this.setRowCount(dwList.size());
                    return dwList;
                }
            };
        }
        return jldwModel;
    }

    public DataGridModel<ParticipantsInfo> getZljcdw() {
        // 获得表格对象
        if (zljcdwModel == null) {
            zljcdwModel = new DataGridModel<ParticipantsInfo>()
            {
                @Override
                public List<ParticipantsInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<ParticipantsInfo> dwList = getParticipantsInfos("10");
                    this.setRowCount(dwList.size());
                    return dwList;
                }
            };
        }
        return zljcdwModel;
    }

    private List<ParticipantsInfo> getParticipantsInfos(String corptype) {
        String itemGuid = (String) getRequestContext().getResult().customData().get("itemGuid");
        if (StringUtil.isBlank(itemGuid)) {
            itemGuid = getDataBean().getRowguid();
        }
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("CORPTYPE", corptype);
        sqlConditionUtil.eq("ITEMGUID", itemGuid);
        sqlConditionUtil.setSelectFields("distinct corpname,xmfzr,xmfzr_phone,legal,phone,itemlegaldept");
        return iParticipantsInfoService.getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
    }

    public String getNameByCode(String code) {
        // 显示父节点及子节点的内容
        String grandson = "";
        String son = "";
        String father = "";
        StringBuilder sb = new StringBuilder();
        String allRood = "";
        if (code.contains(",")) {
            String[] codes = code.split(",");
            for (String codevalue : codes) {
                if (StringUtil.isNotBlank(codevalue)) {
                    grandson = codeItemsService.getItemTextByCodeName("项目分类", codevalue);

                    if (codevalue.length() > 4) {
                        father = codeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 4));
                    }
                    if (codevalue.length() > 6) {
                        son = codeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 6));
                    }
                    if (codevalue.length() == 8) {
                        allRood = father + "·" + son + "·" + grandson;
                    }
                    if (codevalue.length() == 6) {
                        allRood = father + "·" + grandson;
                    }
                    if (codevalue.length() == 4) {
                        allRood = grandson;
                    }
                }

                sb.append(allRood);
                sb.append(";");
            }
        }
        else {
            if (StringUtil.isNotBlank(code)) {
                grandson = codeItemsService.getItemTextByCodeName("项目分类", code);

                if (code.length() > 4) {
                    father = codeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 4));
                }
                if (code.length() > 6) {
                    son = codeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 6));
                }
                if (code.length() == 8) {
                    return father + "·" + son + "·" + grandson;
                }
                if (code.length() == 6) {
                    return father + "·" + grandson;
                }
                if (code.length() == 4) {
                    return grandson;
                }
                else {
                    return grandson;
                }
            }
        }

        return sb.substring(0, sb.toString().length() - 1);
    }

    public String getItemText(String itemvalue, String codename) {
        return codeItemsService.getItemTextByCodeName(codename, itemvalue);
    }

    public void setLxpfOnefileUploadModel(FileUploadModel9 lxpfOnefileUploadModel) {
        this.lxpfOnefileUploadModel = lxpfOnefileUploadModel;
    }

    public void setLxpfTwofileUploadModel(FileUploadModel9 lxpfTwofileUploadModel) {
        this.lxpfTwofileUploadModel = lxpfTwofileUploadModel;
    }

    public void setLxpfThreefileUploadModel(FileUploadModel9 lxpfThreefileUploadModel) {
        this.lxpfThreefileUploadModel = lxpfThreefileUploadModel;
    }

    public void setLxpfFourfileUploadModel(FileUploadModel9 lxpfFourfileUploadModel) {
        this.lxpfFourfileUploadModel = lxpfFourfileUploadModel;
    }

    public FileUploadModel9 getBudongchanfileUploadModel() {
        return budongchanfileUploadModel;
    }

    public void setBudongchanfileUploadModel(FileUploadModel9 budongchanfileUploadModel) {
        this.budongchanfileUploadModel = budongchanfileUploadModel;
    }

    public String getBdcliengguid() {
        return bdcliengguid;
    }

    public void setBdcliengguid(String bdcliengguid) {
        this.bdcliengguid = bdcliengguid;
    }

}

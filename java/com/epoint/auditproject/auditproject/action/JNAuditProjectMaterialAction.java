package com.epoint.auditproject.auditproject.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.auditproject.monitorsupervise.api.IJNAuditProjectMonitorService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 办件材料对应的后台
 * 
 * @author Dong
 * @version [版本号, 2016-09-26 11:08:08]
 */
@RestController("jnauditprojectmaterialaction")
@Scope("request")
public class JNAuditProjectMaterialAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 2576171858091481945L;

    private String projectGuid;

    /**
     * 情形Model
     */
    private List<SelectItem> taskcaseModel = null;

    private AuditProject auditProject;

    private String taskcaseguid;
    private String certnum;

    @Autowired
    private IHandleMaterial handleMaterialService;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IJNAuditProjectMonitorService iJNAuditProjectMonitorService;

    @Autowired
    private IAuditTaskCase auditTaskCaseService;

    @Autowired
    private IAuditTaskElementService auditTaskElementService;

    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    private IHandleRSMaterial rsMateralService;

    @Autowired
    private IAuditMaterialLibrary auditMaterialLibraryService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    @Autowired
    private IJnProjectService iJnProjectService;

    @Override
    public void pageLoad() {
        projectGuid = getRequestParameter("projectguid");
        certnum = getRequestParameter("certnum");
        String processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        if (StringUtil.isBlank(projectGuid) && StringUtil.isNotBlank(processVersionInstanceGuid)) {
            auditProject = auditProjectService
                    .getAuditProjectByPVIGuid(" rowguid,biguid,subappguid,taskguid,taskcaseguid,status,areacode ",
                            processVersionInstanceGuid, ZwfwUserSession.getInstance().getAreaCode())
                    .getResult();
            if (auditProject != null) {
                projectGuid = auditProject.getRowguid();
            }
        }
    }

    @SuppressWarnings("unused")
    public String modelAuditMaterial() {
        certnum = getRequestParameter("certnum");
        String fields = " rowguid,biguid,subappguid,taskguid,taskcaseguid,status,areacode,task_id,applyway,is_samecity,certnum";
        projectGuid = StringUtil.isBlank(projectGuid) ? getRequestParameter("projectguid") : projectGuid;
        String processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        String isPostback = getRequestParameter("isPostback");
        if (StringUtil.isBlank(projectGuid) && StringUtil.isNotBlank(processVersionInstanceGuid)) {
            auditProject = auditProjectService.getAuditProjectByPVIGuid(fields, processVersionInstanceGuid,
                    ZwfwUserSession.getInstance().getAreaCode()).getResult();
            if (auditProject != null) {
                projectGuid = auditProject.getRowguid();
            }
        }
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        JSONObject jsonMaterials = new JSONObject();
        String area = "";

        if (auditProject == null) {
            auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, area).getResult();
        }

        if (auditProject == null) {
            auditProject = iJNAuditProjectMonitorService.getOldProjectByRowGuid(projectGuid);
        }

        if (auditProject == null) {
            return "";
        }

        // ***************开始***************
        // add by yrchan,2022-04-24,是否勘验
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
        if (auditTaskExtension != null
                && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
            return jsonMaterials.toString();

        }
        // **************结束***************

        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProject.getStr("IS_SAMECITY"))) {
            area = auditProject.getAreacode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        String biguid = auditProject.getBiguid();
        String suappguid = auditProject.getSubappguid();

        if (StringUtil.isNotBlank(isPostback) && "0".equals(isPostback)) {

            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                // 材料检查
                List<String> changeMaterial = handleMaterialService
                        .syncShareMaterial(projectGuid, auditProject.getSubappguid()).getResult();
                jsonMaterials.put("changeMaterialGuids", changeMaterial);
            }
            else {
                // if (taskcaseModel == null) {
                // taskcaseModel = new ArrayList<>();
                // 第一次加载获取情形
                // List<AuditTaskCase> auditTaskCases = auditTaskCaseService
                // .selectTaskCaseByTaskGuid(auditProject.getTaskguid(),ZwfwConstant.CONSTANT_STR_ONE).getResult();
                // if (auditTaskCases != null && auditTaskCases.size() > 0) {
                // for (AuditTaskCase auditTaskCase : auditTaskCases) {
                // taskcaseModel.add(new SelectItem(auditTaskCase.getRowguid(),
                // auditTaskCase.getCasename()));
                // }
                // jsonMaterials.put("taskcase", taskcaseModel);
                // 首次打开默认选中第一个保存

                // if (StringUtil.isBlank(auditProject.getTaskcaseguid())) {
                // taskcaseguid = auditTaskCases.get(0).getRowguid();
                //// auditProject.setTaskcaseguid(taskcaseguid);
                //// auditProjectService.updateProject(auditProject);
                // }
                // else {
                // taskcaseguid = auditProject.getTaskcaseguid();
                // }
                // if(StringUtil.isNotBlank(taskcaseguid)){
                // jsonMaterials.put("taskcaseguid", taskcaseguid);
                // }
                jsonMaterials.put("isenabled", auditProject.getStatus() < 30);
                // }
                // }
            }
        }

        Map<String, Integer> caseMap = null;
        if (StringUtil.isNotBlank(getRequestParameter("taskcaseguid"))) {
            // 情形变更后保存
            taskcaseguid = getRequestParameter("taskcaseguid");
            AuditProject a = new AuditProject();
            a.setRowguid(auditProject.getRowguid());
            a.setTaskcaseguid(taskcaseguid);
            auditProject.setTaskcaseguid(taskcaseguid);
            a.setAreacode(auditProject.getAreacode());
            a.setTask_id(auditProject.getTask_id());
            a.setApplyway(auditProject.getApplyway());
            auditProjectService.updateProject(a);
        }
        else {
            taskcaseguid = auditProject.getTaskcaseguid();
        }
        // 用于详情页面显示情形名称
        if (StringUtil.isNotBlank(taskcaseguid)) {
            AuditTaskCase auditTaskCase = auditTaskCaseService.getAuditTaskCaseByRowguid(taskcaseguid).getResult();
            if (auditTaskCase != null) {
                jsonMaterials.put("taskcaseguid", taskcaseguid);
                if (StringUtil.isNotBlank(auditTaskCase.getCasename())) {
                    jsonMaterials.put("casename", auditTaskCase.getCasename());
                }
            }
            else {
                jsonMaterials.put("delcase", ZwfwConstant.CONSTANT_INT_ONE);
            }
        }

        AuditTaskCase taskCase = auditTaskCaseService.getAuditTaskCaseByRowguid(taskcaseguid).getResult();
        if (StringUtil.isNotBlank(taskcaseguid) && taskCase != null && StringUtils.isNotBlank(taskCase.getCasename())) {
            List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                    .selectTaskMaterialCaseByCaseGuid(taskcaseguid).getResult();
            caseMap = new HashMap<>(16);
            // 转成map方便查找
            if (auditTaskMaterialCases != null && auditTaskMaterialCases.size() > 0) {
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    caseMap.put(auditTaskMaterialCase.getMaterialguid(), auditTaskMaterialCase.getNecessity());
                }
            }
        }
        // 判断事项是否配置了要素与选项数量
        if (StringUtil.isNotBlank(auditProject.getTask_id()) && StringUtil.isNotBlank(auditProject.getTaskguid())) {
            List<AuditTaskElement> auditTaskElementlist = auditTaskElementService
                    .findAllElementByTaskId(auditProject.getTask_id()).getResult();
            List<AuditTaskCase> auditTaskcaselist = auditTaskCaseService
                    .selectTaskCaseByTaskGuid(auditProject.getTaskguid(), ZwfwConstant.CONSTANT_STR_ONE).getResult();
            if (auditTaskElementlist.size() > 0) {
                // 存在情形选项
                jsonMaterials.put("elementnum", 1);
            }
            else if (auditTaskElementlist.size() == 0 && auditTaskcaselist.size() > 0) {
                // 不存在情形类别但存在常用情形 即旧情形
                jsonMaterials.put("elementnum", 2);
            }
            else {
                // 两者都不存在
                jsonMaterials.put("elementnum", 0);
            }
            jsonMaterials.put("applyway", auditProject.getApplyway());
        }

        if (StringUtil.isBlank(auditProject.getBiguid())) {
            if (taskcaseModel == null) {
                taskcaseModel = new ArrayList<>();
                List<AuditTaskCase> auditTaskCases = auditTaskCaseService
                        .selectTaskCaseByTaskGuid(auditProject.getTaskguid(), ZwfwConstant.CONSTANT_STR_ONE)
                        .getResult();
                if (auditTaskCases != null && auditTaskCases.size() > 0) {
                    for (AuditTaskCase auditTaskCase : auditTaskCases) {
                        taskcaseModel.add(new SelectItem(auditTaskCase.getRowguid(), auditTaskCase.getCasename()));
                    }
                    jsonMaterials.put("taskcase", taskcaseModel);
                }
            }
        }

        List<Record> projectMaterial = handleMaterialService.getProjectMaterialALL(projectGuid, biguid, suappguid)
                .getResult();
        // 这里需要定义几个变量
        int totalcount = 0;
        int submitedMaterialCount = 0;
        int shouldPaperCount = 0;
        int submitedPaperCount = 0;
        int shouldAttachCount = 0;
        int submitedAttachCount = 0;
        int rongqueCount = 0;
        int buRongqueCount = 0;
        try {
            if (projectMaterial.size() > 0) {
                totalcount = projectMaterial.size();
                projectMaterial.sort((a,
                        b) -> (StringUtil.isNotBlank(b.get("Ordernum")) ? Integer.valueOf(b.get("Ordernum").toString())
                                : Integer.valueOf(0))
                                        .compareTo(StringUtil.isNotBlank(a.get("Ordernum"))
                                                ? Integer.valueOf(a.get("Ordernum").toString())
                                                : 0));
                for (int i = 0; i < totalcount; i++) {
                    String bigshowtype = projectMaterial.get(i).getStr("bigshowtype");
                    // 匹配情形
                    if (StringUtil.isNotBlank(caseMap)) {
                        if (!ZwfwConstant.NECESSITY_SET_YES.equals(projectMaterial.get(i).get("NECESSITY").toString())
                                && !caseMap.containsKey(projectMaterial.get(i).get("taskmaterialguid"))) {
                            continue;
                        }
                    }
                    AuditTaskMaterial audittaskmaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(projectMaterial.get(i).getStr("TASKMATERIALGUID"))
                            .getResult();
                    if (audittaskmaterial != null) {
                        if (StringUtil.isNotBlank(audittaskmaterial.getStr("bigshowtype"))) {
                            bigshowtype = audittaskmaterial.getStr("bigshowtype");
                        }
                    }

                    if (StringUtil.isBlank(certnum)) {
                        certnum = auditProject.getCertnum();
                    }
                    JSONObject jsonMaterial = new JSONObject();
                    int materialstatus = Integer.parseInt(projectMaterial.get(i).get("STATUS").toString());
                    String submittype = "";
                    String materialnametemp = "";

                    if (StringUtil.isNotBlank(bigshowtype) && StringUtil.isNotBlank(certnum)) {
                        List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(certnum,
                                bigshowtype);
                        if (attachinfos != null && attachinfos.size() > 0) {
                            FrameAttachInfo attach = attachinfos.get(0);
                            jsonMaterial.put("materialstatus", "20");
                            jsonMaterial.put("cliengguid", attach.getCliengGuid());
                        }
                        else {
                            jsonMaterial.put("materialstatus", materialstatus);
                            jsonMaterial.put("cliengguid", projectMaterial.get(i).get("CLIENGGUID"));
                        }
                    }
                    else {
                        jsonMaterial.put("materialstatus", materialstatus);
                        jsonMaterial.put("cliengguid", projectMaterial.get(i).get("CLIENGGUID"));
                    }
                    jsonMaterial.put("materialname", projectMaterial.get(i).get("MATERIALNAME") + materialnametemp);
                    if (projectMaterial.get(i).get("EXAMPLEATTACHGUID") == null) {
                        jsonMaterial.put("exampleattachguid", "0");
                    }
                    else {
                        jsonMaterial.put("exampleattachguid", projectMaterial.get(i).get("EXAMPLEATTACHGUID"));
                    }
                    jsonMaterial.put("materialinstanceguid", projectMaterial.get(i).get("MATERIALINSTANCEGUID"));
                    jsonMaterial.put("necessity", projectMaterial.get(i).get("NECESSITY"));
                    jsonMaterial.put("sharematerialguid", projectMaterial.get(i).get("sharematerialguid"));
                    jsonMaterial.put("taskmaterialguid", projectMaterial.get(i).get("taskmaterialguid"));
                    if (StringUtil.isNotBlank(biguid)) {
                        jsonMaterial.put("isblsp", "true");
                    }
                    else {
                        jsonMaterial.put("isblsp", "false");
                    }
                    if (StringUtil.isNotBlank(projectMaterial.get(i).get("SUBMITTYPE").toString())) {
                        submittype = projectMaterial.get(i).get("SUBMITTYPE").toString();
                    }

                    if ("50".equals(submittype)) {
                        submittype = "10";
                    }

                    // 判断该办件是否为线下窗口登记的办件，applyway =
                    // 20，且该事项配置了【是否可纸质】(is_tssgs)为是
                    Integer applyway = auditProject.getApplyway();
                    String is_tssgs = auditTaskExtension.getStr("is_tssgs");
                    if (applyway!=null && applyway == 20 && StringUtil.isNotBlank(is_tssgs) && "true".equals(is_tssgs)) {
                        // 是线下窗口登记的办件，且可纸质
                        submittype = "35";
                    }
                    jsonMaterial.put("submittype", submittype);

                    jsonMaterial.put("auditstatus", projectMaterial.get(i).get("AUDITSTATUS"));
                    String is_rongque = "";
                    // 如果存在情形非必要也计算容缺
                    if (taskCase != null) {
                        if (ZwfwConstant.CONSTANT_STR_ONE
                                .equals(projectMaterial.get(i).get("IS_ALLOWRONGQUE").toString())) {
                            is_rongque = "1";
                            rongqueCount++;
                        }
                        else {
                            is_rongque = "0";
                            buRongqueCount++;
                        }
                    }
                    else {
                        if ("10".equals(projectMaterial.get(i).get("NECESSITY").toString())) {
                            if (ZwfwConstant.CONSTANT_STR_ONE
                                    .equals(projectMaterial.get(i).get("IS_ALLOWRONGQUE").toString())) {
                                is_rongque = "1";
                                rongqueCount++;
                            }
                            else {
                                is_rongque = "0";
                                buRongqueCount++;
                            }
                        }
                    }
                    jsonMaterial.put("is_rongque", is_rongque);
                    // 判断未页面首次加载
                    if (((StringUtil.isNotBlank(jsonMaterials.get("taskcaseguid"))
                            || ZwfwConstant.CONSTANT_STR_ZERO.equals(isPostback))
                            || (StringUtil.isBlank(isPostback)
                                    && StringUtil.isBlank(getRequestParameter("taskcaseguid"))))
                            && StringUtil.isBlank(processVersionInstanceGuid)) {
                        // 判断是想存在情形选项，则只显示必要材料，以及在办件状态为20时
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(jsonMaterials.get("elementnum").toString())
                                && (auditProject.getStatus() == 20 || auditProject.getStatus() == 26)
                                && (StringUtil.isBlank(auditProject.getTaskcaseguid()) || taskCase == null)) {
                            if ("10".equals(String.valueOf(jsonMaterial.get("necessity")))) {
                                materialList.add(jsonMaterial);
                            }
                        }
                        else if (StringUtil.isNotBlank(jsonMaterials.get("delcase"))) {
                            if ("10".equals(String.valueOf(jsonMaterial.get("necessity")))) {
                                materialList.add(jsonMaterial);
                            }
                        }
                        else {
                            materialList.add(jsonMaterial);
                        }
                    }
                    else {
                        materialList.add(jsonMaterial);
                    }
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
            jsonMaterials.put("total", totalcount);
            AuditTask audittask = new AuditTask();
            if (auditProject != null && StringUtil.isNotBlank(auditProject.getTaskguid())) {
                audittask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                if (audittask != null && ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())
                        && !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())) {
                    jsonMaterials.put("taskType", "jbj");
                }
                jsonMaterials.put("status", auditProject.getStatus());
            }

            String certnum = getRequestParameter("certnum");
            if (StringUtil.isNotBlank(certnum)) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("ownerno", certnum);
                sqlc.eq("firstflag", ZwfwConstant.CONSTANT_STR_ONE);
                int count = auditMaterialLibraryService.getAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "", "")
                        .getResult().getRowCount();
                if (count > 0) {
                    jsonMaterials.put("hasm", count);
                }
            }
            if (StringUtil.isNotBlank(biguid)) {
                jsonMaterials.put("isblsp", "true");
            }
            else {
                jsonMaterials.put("isblsp", "false");
            }
            jsonMaterials.put("isAllPaper", materialList.size() > submitedPaperCount ? 0 : 1);
            // 纸质全选是否隐藏
            jsonMaterials.put("isPaperHide", shouldPaperCount > 0 ? 0 : 1);
            jsonMaterials.put("data", materialList);
            // 如果选定情形，不容却数量为总数减去容缺数量
            if (taskCase != null) {
                buRongqueCount = materialList.size() - rongqueCount;
            }
            jsonMaterials.put("materialSummary", "已标记" + submitedMaterialCount + "件材料，其中纸质" + submitedPaperCount
                    + "件，电子文档" + submitedAttachCount + "件；容缺" + rongqueCount + "件；不容缺" + buRongqueCount + "件。");
            jsonMaterials.put("projectguid", projectGuid);
            jsonMaterials.put("areacode", auditProject.getAreacode());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonMaterials.toString();
    }

    public void getClsp(String taskmaterialguid) {
        AuditTaskMaterial audittaskmaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialguid)
                .getResult();
        String str = "<span style=\"font-weight:bold\">材料名称</span>：" + audittaskmaterial.getMaterialname();
        str += "<br/><span style=\"font-weight:bold\">审查要点</span>："
                + (StringUtil.isNotBlank(audittaskmaterial.getXsreviewkp()) ? audittaskmaterial.getXsreviewkp() : "无");
        addCallbackParam("str", str + "<p></p>");
    }

    public void paperOperate(Map<String, String> dataMaterials) {
        try {
            EpointFrameDsManager.begin(null);
            Map<String, String> params = getRequestParametersMap();
            String param = params.get("params");
            JSONArray jsonParam = JSONObject.parseArray(param);
            jsonParam.forEach(jsonPram -> {
                Map<String, Object> map = JsonUtil.jsonToMap(jsonPram.toString());
                String operateType = String.valueOf(map.get("operateType"));
                String materialInstanceGuid = String.valueOf(map.get("materialInstanceGuid"));
                String auditstatus = String.valueOf(map.get("auditstatus"));
                String projectguid = String.valueOf(map.get("projectguid"));
                if ("submit".equals(operateType)) {
                    projectMaterialService.updateProjectMaterialStatus(materialInstanceGuid, projectguid, 5);
                    // 未提交更新为审核通过
                    if (auditstatus.equals(ZwfwConstant.Material_AuditStatus_WTJ)) {
                        projectMaterialService.updateProjectMaterialAuditStatus(materialInstanceGuid,
                                Integer.parseInt(ZwfwConstant.Material_AuditStatus_SHTG), projectguid);
                    }
                }
                else {
                    projectMaterialService.updateProjectMaterialStatus(materialInstanceGuid, projectguid, -5);
                }
                // 待补正更新为待审核
                if (auditstatus.equals(ZwfwConstant.Material_AuditStatus_DBZ)) {
                    projectMaterialService.updateProjectMaterialAuditStatus(materialInstanceGuid,
                            Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ), projectguid);
                }
            });

            // 处理返回数据
            Record rtnInfo = new Record();
            rtnInfo.put("customer", "success");
            EpointFrameDsManager.commit();
            sendRespose(JsonUtil.objectToJson(rtnInfo));
        }
        catch (Exception ex) {
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    public List<SelectItem> gettaskcaseModel() {
        if (taskcaseModel == null) {
            taskcaseModel = new ArrayList<>();
            if (StringUtil.isBlank(auditProject.getBiguid())) {
                List<AuditTaskCase> auditTaskCases = auditTaskCaseService
                        .selectTaskCaseByTaskGuid(auditProject.getTaskguid(), ZwfwConstant.CONSTANT_STR_ONE)
                        .getResult();
                for (AuditTaskCase auditTaskCase : auditTaskCases) {
                    taskcaseModel.add(new SelectItem(auditTaskCase.getRowguid(), auditTaskCase.getCasename()));
                }
            }
        }
        return taskcaseModel;
    }

    // public void updateRemark(){
    // projectGuid = getRequestParameter("projectguid");
    // remark = getRequestParameter("remark");
    // List<AuditProjectMaterial> material =
    // projectMaterialService.selectProjectMaterial(projectGuid).getResult();
    // }
    public void searchCert() {
        projectGuid = getRequestParameter("projectguid");
        String area = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, area).getResult();
        certnum = getRequestParameter("certnum");
        String certownercerttype = getRequestParameter("certownercerttype");
        // 关联共享材料
        String applyerType = getRequestParameter("applyerType");
        String flag = rsMateralService
                .is_ExistCert(auditProject.getRowguid(), applyerType, certnum, certownercerttype, area).getResult();
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", applyerType);
        addCallbackParam("certnum", certnum);
    }

    public void searchBisghowCert() {
        projectGuid = getRequestParameter("projectguid");
        String area = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, area).getResult();
        certnum = getRequestParameter("certnum");
        String certownercerttype = getRequestParameter("certownercerttype");
        // 关联共享材料
        String applyerType = getRequestParameter("applyerType");
        String flag = rsMateralService
                .is_ExistCert(auditProject.getRowguid(), applyerType, certnum, certownercerttype, area).getResult();
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", applyerType);
        addCallbackParam("certnum", certnum);
    }

    public void checkMaterialLibrary() {
        String certnum = getRequestParameter("certnum");
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("ownerno", certnum);
        sqlc.eq("firstflag", ZwfwConstant.CONSTANT_STR_ONE);
        int count = auditMaterialLibraryService.getAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "", "")
                .getResult().getRowCount();
        if (count > 0) {
            addCallbackParam("hasm", count);
        }
    }

    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }
}

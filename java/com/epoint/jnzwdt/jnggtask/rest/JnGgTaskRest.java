package com.epoint.jnzwdt.jnggtask.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.jnzwdt.jnggtask.api.IJnAuditSpBaseTaskService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.jgtaskmaterialrelation.api.IJgTaskmaterialrelationService;
import com.epoint.xmz.jgtaskmaterialrelation.api.entity.JgTaskmaterialrelation;

@RestController
@RequestMapping("/jnggtaskrest")
public class JnGgTaskRest
{
    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IJnAuditSpBaseTaskService iJnAuditSpBaseTaskService;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    @Autowired
    private IAuditSpITask iAuditSpITask;

    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;

    @Autowired
    private IAuditSpShareMaterialRelation iAuditSpShareMaterialRelation;

    @Autowired
    private IAuditSpShareMaterial iAuditSpShareMaterial;

    @Autowired
    private IJgTaskmaterialrelationService relationService;

    /**
     * 获取高频事项taskid
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getGpTaskIds", method = RequestMethod.POST)
    public String getGpTaskIds(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getListByCertType接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                // 工改阶段标识
                String phaseguid = paramsObj.getString("phaseGuid");

                // 一件事主题标识
                String businessGuid = paramsObj.getString("businessGuid");

                List<String> taskids;
                // 如果一件事主题guid不为空，从一件事逻辑中获取taskids，反之则从工改标准事项配置中获取taskids
                if (StringUtil.isNotBlank(businessGuid)) {
                    taskids = iJnAuditSpBaseTaskService.listTaskIdsByBusinessGuid(businessGuid);
                }
                else {
                    taskids = iJnAuditSpBaseTaskService.listTaskIdsByPhaseGuid(phaseguid);
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("taskids", taskids);
                log.info("=======结束调用getListByCertType接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取高频事项taskid列表成功！", dataJson.toString());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.info("接口调用失败！错误信息", e);
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }

    /**
     * 获取网厅选中事项列表的关联材料
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getSpMaterial", method = RequestMethod.POST)
    public String getSpMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getListByCertType接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject paramsObj = jsonObject.getJSONObject("params");
                String subAppGuid = paramsObj.getString("subappguid");
                JSONArray spitaskguids = paramsObj.getJSONArray("itaskguids");

                // 2、查询子申报实例,并判断该申报阶段的状态是否为已评审
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();

                // 查询需要办理的事项的数量
                List<AuditSpITask> auditSpITasks = iAuditSpITask
                        .getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                int allNum = auditSpITasks.size();
                int inNum = 0;
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 如果project不为空证明该事项已经分发，或者status不为空和0证明外网已提交过
                    if (StringUtil.isNotBlank(auditSpITask.getProjectguid())
                            || (StringUtil.isNotBlank(auditSpITask.getStatus())
                                    && !"0".equals(auditSpITask.getStatus()))) {
                        inNum++;
                    }
                }
                if (ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus()) || (allNum - inNum) != 0) {
                    String biGuid = auditSpISubapp.getBiguid();
                    // 2.1、查询申报实例信息
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    String businessGuid = auditSpInstance.getBusinessguid();
                    String companyId = auditSpISubapp.getApplyerguid();
                    String phaseGuid = auditSpISubapp.getPhaseguid();
                    // 2.2、查询申报项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                    // 2.3 如果材料实例没有初始化，则初始化材料
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpISubapp.getInitmaterial())) {
                        iHandleSPIMaterial.initSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, companyId,
                                auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                    }
                    List<JSONObject> spiMaterialJsonList = new ArrayList<>();

                    StringBuilder materialnames = new StringBuilder();
                    List<String> clientguidlist = new ArrayList<>();

                    for (Object itaskguid : spitaskguids) {
                        String spitaskGuid = (String) itaskguid;
                        AuditSpITask auditSpITask = iAuditSpITask.getAuditSpITaskDetail(spitaskGuid).getResult();

                        // 新增四阶段三个事项自动带入监管事项材料 ----hqt
                        String materialid = "";
                        String beizhu = "";
                        String cliengguid = "";
                        AuditTask task = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), true).getResult();
                        if (task != null) {
                            String task_id = task.getTask_id();
                            CodeItems codeItems = iCodeItemsService.getCodeItemByCodeName("监管事项材料对应关系", task_id);
                            if (codeItems != null) {
                                // 事项材料id
                                materialid = codeItems.getItemText();
                                beizhu = codeItems.getDmAbr1();
                            }

                            // 先查询是否满足自动带入条件
                            if (StringUtil.isNotBlank(materialid) && StringUtil.isNotBlank(beizhu)) {
                                JgTaskmaterialrelation jgTaskmaterialrelation = relationService
                                        .getRelationByItemguid(auditRsItemBaseinfo.getRowguid());
                                if (jgTaskmaterialrelation != null) {
                                    switch (beizhu) {
                                        case "工程规划批后监管":
                                            cliengguid = jgTaskmaterialrelation.getGccliengguid();
                                            break;
                                        case "建筑工程质量安全监督":
                                            cliengguid = jgTaskmaterialrelation.getJzcliengguid();
                                            break;
                                        case "人防工程质量安全监督":
                                            cliengguid = jgTaskmaterialrelation.getRfcliengguid();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }

                        }

                        // 5.1、获取事项下的材料列表
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();

                        // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                        // 定义一个标志位用于区分该事项下是否有多评多测材料
                        int typeFlag = 0; // 0代表没有多评和多测 1代表只有多评 2代表有多测 3代表全都有
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            if (materialnames.toString().contains(auditTaskMaterial.getMaterialname())) {
                                continue;
                            }
                            JSONObject spiMaterialJson = new JSONObject();
                            // 5.3、获取事项实例中的材料实例信息
                            AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                    .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                    .getResult();
                            if (auditSpIMaterial != null
                                    && ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                continue;
                            }
                            // 4.11、 判断材料来源
                            String materialsource = auditTaskMaterial.getFile_source();
                            // 5.6、如果没有找到对应的材料，则为共享/结果材料，找到对应材料输出
                            if (auditSpIMaterial == null) {
                                AuditSpShareMaterialRelation auditSpShareMaterialRelation = iAuditSpShareMaterialRelation
                                        .getRelationByID(auditSpITask.getBusinessguid(),
                                                auditTaskMaterial.getMaterialid())
                                        .getResult();
                                // 4.7、如果存在共享材料/结果材料
                                if (auditSpShareMaterialRelation != null) {
                                    AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                            .getAuditSpShareMaterialByShareMaterialGuid(
                                                    auditSpShareMaterialRelation.getSharematerialguid(), businessGuid)
                                            .getResult();
                                    if (auditSpShareMaterial != null) {
                                        materialsource = auditSpShareMaterial.getFile_source();
                                    }
                                    // 4.9、获取存在的材料共享/结果关系
                                    auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByMaterialGuid(subAppGuid,
                                            auditSpShareMaterialRelation.getSharematerialguid()).getResult();
                                    // 4.10、判断是否是结果材料，如果是就不显示该材料
                                    if (auditSpIMaterial == null
                                            || ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                        continue;
                                    }
                                }
                                else {
                                    // 没有找到共享材料数据，结束该循环
                                    continue;
                                }
                            }

                            // 把综管上传的材料放入该事项对应的材料中去
                            if (StringUtil.isNotBlank(cliengguid) && StringUtil.isNotBlank(materialid)
                                    && materialid.equals(auditTaskMaterial.getMaterialid())) {
                                // 先把原先关联删除在复制
                                iAttachService.deleteAttachByGuid(auditSpIMaterial.getCliengguid());
                                iAttachService.copyAttachByClientGuid(cliengguid, "监管事项材料复制", null,
                                        auditSpIMaterial.getCliengguid());
                                // 同时把状态改为已上传
                                auditSpIMaterial.setStatus("20");
                                iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                            }

                            if (StringUtil.isBlank(materialsource)) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", materialsource);
                            }
                            spiMaterialJson.put("pagenum", StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0"
                                    : auditTaskMaterial.getPage_num());
                            spiMaterialJson.put("cliengguid", auditSpIMaterial.getCliengguid());
                            spiMaterialJson.put("stantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());
                            spiMaterialJson.put("materialsource", materialsource);
                            spiMaterialJson.put("type", auditTaskMaterial.getType());
                            spiMaterialJson.put("materialguid", auditSpIMaterial.getRowguid());
                            spiMaterialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());
                            String materialtype = "";
                            if (StringUtil.isNotBlank(auditTaskMaterial.getMaterialtype())) {
                                String materialTypeMain = auditTaskMaterial.getMaterialtype().substring(0, 4);
                                if (StringUtil.isNotBlank(materialTypeMain)) {
                                    switch (materialTypeMain) {
                                        case "tscl":
                                            materialtype = "【图审】";
                                            break;
                                        case "chcl":
                                            materialtype = "【测绘】";
                                            if (typeFlag == 0) {
                                                typeFlag = 2;
                                            }
                                            else if (typeFlag == 1) {
                                                typeFlag = 3;
                                            }
                                            break;
                                        case "dpcl":
                                            materialtype = "【多评】";
                                            if (typeFlag == 0) {
                                                typeFlag = 1;
                                            }
                                            else if (typeFlag == 2) {
                                                typeFlag = 3;
                                            }
                                            break;
                                        case "lscl":
                                            materialtype = "【联审】";
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            spiMaterialJson.put("materialtype", materialtype);
                            spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                            spiMaterialJson.put("materialinstanceguid", auditTaskMaterial.getRowguid());
                            spiMaterialJson.put("status", auditSpIMaterial.getStatus());
                            spiMaterialJson.put("rowguid", auditTaskMaterial.getRowguid());
                            // 是否共享材料
                            spiMaterialJson.put("shared", auditSpIMaterial.getShared());
                            // 共享材料返回共享材料的必要性、容缺、提交方式
                            // 是否必须 10必须 20非必须
                            spiMaterialJson.put("necessity", auditSpIMaterial.getNecessity());
                            // 是否容缺
                            String rongque = "0";// 默认不容缺
                            if (StringUtil.isNotBlank(auditSpIMaterial.getNecessity())
                                    && "10".equals(auditSpIMaterial.getNecessity())) {
                                rongque = auditSpIMaterial.getAllowrongque();// 只有必要的材料才需要显示容缺
                            }
                            spiMaterialJson.put("allowrongque", rongque);
                            spiMaterialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditSpIMaterial.getSubmittype())));
                            // 示例表格
                            String exampleClientGuid = auditTaskMaterial.getExampleattachguid();
                            if (StringUtil.isNotBlank(exampleClientGuid)) {
                                int exampleAttachCount = iAttachService.getAttachCountByClientGuid(exampleClientGuid);
                                if (exampleAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(exampleClientGuid);
                                    spiMaterialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 空白表格
                            String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                            if (StringUtil.isNotBlank(templateClientGuid)) {
                                int templateAttachCount = iAttachService.getAttachCountByClientGuid(templateClientGuid);
                                if (templateAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(templateClientGuid);
                                    spiMaterialJson.put("templateattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 3.12、 判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                            int showButton; // 按钮显示方式
                            String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel"))
                                    ? "1"
                                    : "0"; // 是否需要上传到证照库 0:不需要 、1:需要
                            if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                                // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() != null
                                        && auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else {
                                    // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.6.2、如果关联了证照库
                                if (count > 0) {
                                    // 4.6.2.1、获取材料类别
                                    String materialType = auditTaskMaterial.getMaterialtype();
                                    if (StringUtil.isBlank(materialType)) {
                                        materialType = "0";
                                    }
                                    if (Integer.parseInt(materialType) == 1) {
                                        // 4.6.2.1.1、已引用证照库
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                    }
                                    else if (Integer.parseInt(materialType) == 2) {
                                        // 4.6.2.1.2、已引用批文
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                    }
                                    else {
                                        // 4.6.2.1.3、已引用材料
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                    }
                                }
                                else {
                                    // 4.6.2.2、如果没有附件，则标识为未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);

                                }
                            }
                            spiMaterialJson.put("showbutton", showButton);
                            spiMaterialJson.put("needload", needLoad);
                            spiMaterialJson.put("taskguid", auditSpITask.getRowguid());
                            spiMaterialJsonList.add(spiMaterialJson);
                            materialnames.append(auditSpIMaterial.getMaterialname()).append(",");

                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("spimateriallist", spiMaterialJsonList);
                    log.info("=======结束调用getListByCertType接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取材料列表成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "当前阶段不可提交材料！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.info("接口调用失败！错误信息", e);
            return JsonUtils.zwdtRestReturn("0", "接口异常", "");
        }
    }
}

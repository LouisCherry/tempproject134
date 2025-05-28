package com.epoint.zczwfw.audittask.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.feeaccount.domain.AuditOrgaFeeaccount;
import com.epoint.basic.auditorga.feeaccount.inter.IAuditOrgaFeeaccount;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.basic.audittask.dict.inter.IAuditTaskDict;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.map.domain.AuditTaskMap;
import com.epoint.basic.audittask.map.inter.IAuditTaskMap;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.CompareUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.service.DeptSelfBuildSystemService;
import com.epoint.zczwfw.audittaskinquestsituation.api.IAuditTaskInquestsituationService;
import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;

@RestController("zcaudittaskextenddetailaction")
@Scope("request")
public class ZCAuditTaskExtendDetailAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -5811009959141283016L;

    @Autowired
    private IAuditTaskDict auditTaskDictImpl;

    @Autowired
    private IAuditTaskMap auditTaskMapImpl;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Autowired
    private IAttachService attachServiceImpl;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    /**
     * 勘验事项情形API
     */
    @Autowired
    private IAuditTaskInquestsituationService iAuditTaskInquestsituationService;

    private CompareUtil compareUtil = new CompareUtil();
    /**
     * 事项基本信息
     */
    private AuditTask auditTask;
    /**
     * 当前版本guid
     */
    private String currentTaskGuid;
    /**
     * 对比版本guid
     */
    private String compareTaskGuid;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 外部流程图名称
     */
    private String outflowimgattachname = "";

    /**
     * 外部流程图标识
     */
    private String outflowimgattachguid = "";

    /**
     * 代码项service
     */
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 附件service
     */
    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditOrgaFeeaccount feeaaccountService;
    private DeptSelfBuildSystem deptSelfBuildSystem;
    @Autowired
    private DeptSelfBuildSystemService deptSelfBuildSystemService;

    /**
     * add by yrchan,2022-04-24,是否勘验
     */
    private String is_inquest;

    @Override
    public void pageLoad() {
        currentTaskGuid = this.getViewData("currentTaskGuid");
        if (StringUtil.isBlank(currentTaskGuid)) {
            currentTaskGuid = this.getRequestParameter("currentGuid");
        }
        compareTaskGuid = this.getViewData("compareTaskGuid");
        if (StringUtil.isBlank(compareTaskGuid)) {
            compareTaskGuid = this.getRequestParameter("compTaskGuid");
        }
        String compJson = "";
        auditTask = auditTaskBasicImpl.getAuditTaskByGuid(currentTaskGuid, false).getResult();
        auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(currentTaskGuid, false).getResult();
        AuditTask compareAuditTask = auditTaskBasicImpl.getAuditTaskByGuid(compareTaskGuid, false).getResult();
        AuditTaskExtension compareAuditTaskExtension = auditTaskExtensionImpl
                .getTaskExtensionByTaskGuid(compareTaskGuid, false).getResult();
        String rowguid = auditTask.get("selfBuildSystemGuid");
        if (rowguid != null) {
            deptSelfBuildSystem = deptSelfBuildSystemService.findSelfBuildSystemByRowGuid(rowguid);
        }
        Map<String, Object> mapAuditTask = compareUtil.compare2Bean(auditTask, compareAuditTask);
        Map<String, Object> mapExtension = compareUtil.compare2Bean(auditTaskExtension, compareAuditTaskExtension);
        Map<String, Object> mapAll = new HashMap<String, Object>(16);
        for (Map.Entry<String, Object> map : mapAuditTask.entrySet()) {
            if ("is_enable".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("charge_flag".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", map.getValue().toString()));
            }
            else if ("charge_lc".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", map.getValue().toString()));
            }
            else if ("taskoutimgguid".equals(map.getKey())) {
                mapAll.put(map.getKey(), getTempUrl(String.valueOf(map.getValue())));
            }

            else if ("feeaccountguid".equals(map.getKey())) {
                AuditOrgaFeeaccount auditOrgaFeeaccount = feeaaccountService
                        .getFeeaccountByRowguid(compareAuditTask.getFeeaccountguid()).getResult();
                if (auditOrgaFeeaccount != null) {
                    mapAll.put(map.getKey(), auditOrgaFeeaccount.getAccountname());
                }
                else {
                    mapAll.put(map.getKey(), "无");
                }
            }
            else {
                mapAll.put(map.getKey(), map.getValue());
            }
        }
        for (Map.Entry<String, Object> map : mapExtension.entrySet()) {
            if ("webapplytype".equals(map.getKey())) {
                mapAll.put(map.getKey(),
                        codeItemsService.getItemTextByCodeName("网上申报类型", String.valueOf(map.getValue())));
            }
            else if ("is_simulation".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("is_feishui".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("iszijianxitong".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", (String) map.getValue()));
            }
            else if ("reservationmanagement".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("if_express".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("onlinepayment".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("isriskpoint".equals(map.getKey())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("是否", String.valueOf(map.getValue())));
            }
            else if ("zijian_mode".equals(map.getKey())) {
                String ZIJIAN = String.valueOf(mapExtension.get("iszijianxitong"));
                if ("0".equals(ZIJIAN) || "否".equals(ZIJIAN)) {
                    mapAll.put(map.getKey(), "无");
                }
                else {
                    mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("自建系统模式", (String) map.getValue()));
                }
            }
            else if ("express_content".equals(map.getKey()) && !"无".equals(map.getValue().toString())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("物流快递内容", map.getValue().toString()));
            }
            else if (map.getKey().equals("express_send_type") && !"无".equals(map.getValue().toString())) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("快递送达方式", map.getValue().toString()));
            }
            else if (map.getKey().equals("charge_when")) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("何时收费", (String) map.getValue()));
            }
            else if (map.getKey().equals("subjectnature")) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("实施主体性质", (String) map.getValue()));
            }
            else if (map.getKey().equals("use_level")) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("行使层级", (String) map.getValue()));
            }
            else if (map.getKey().equals("is_notopendoc")) {
                mapAll.put(map.getKey(), codeItemsService.getItemTextByCodeName("文书类型", (String) map.getValue()));
            }
            else {
                mapAll.put(map.getKey(), map.getValue());
            }
        }
        compJson = JsonUtil.objectToJson(mapAll);

        if (auditTask == null) {
            auditTask = new AuditTask();
        }
        if (auditTaskExtension == null) {
            auditTaskExtension = new AuditTaskExtension();
        }
        this.addCallbackParam("compJson", compJson);

        if (StringUtil.isNotBlank(auditTask.getTaskoutimgguid())) {// 外部流程图标识不为空
            /**
             * 根据业务guid获取附件名字和guid的数组,中间用;拼接 参数: clientGuid - 业务guid，type -
             * 大小附件标识 1:大附件;2:小附件 返回: String[]name和guid
             **/
            List<FrameAttachInfo> frameAttachInfoList = attachService
                    .getAttachInfoListByGuid(auditTask.getTaskoutimgguid());
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {
                for (int i = 0; i < frameAttachInfoList.size(); i++) {
                    if (i == frameAttachInfoList.size() - 1) {
                        outflowimgattachname += frameAttachInfoList.get(i).getAttachFileName();
                        outflowimgattachguid += frameAttachInfoList.get(i).getAttachGuid();
                    }
                    else {
                        outflowimgattachname += frameAttachInfoList.get(i).getAttachFileName() + ";";
                        outflowimgattachguid += frameAttachInfoList.get(i).getAttachGuid() + ";";
                    }
                }
            }
        }

        // 对比版本的是否收费
        this.addCallbackParam("chargeflag", auditTask.getCharge_flag());
        this.addCallbackParam("comparechargeflag", compareAuditTask.getCharge_flag());
        // 对比版本的是物流
        this.addCallbackParam("expressflag", auditTaskExtension.getIf_express());
        this.addCallbackParam("compareexpressflag", auditTaskExtension.getIf_express());
        // 自建系统判断
        this.addCallbackParam("iszijianxitong", auditTaskExtension.getIszijianxitong());
        this.addCallbackParam("iszijianxitongJR", auditTaskExtension.getIszijianxitong());

        this.addCallbackParam("comiszijianxitong", "");
        this.addCallbackParam("taskid", auditTask.getTask_id());
        if (StringUtils.isNoneBlank(auditTask.getTransact_addr())) {
            this.addCallbackParam("applyaddress", auditTask.getTransact_addr());
        }
        else {
            this.addCallbackParam("applyaddress", "");
        }
        if (StringUtil.isNotBlank(auditTask.getFeeaccountguid())) {
            AuditOrgaFeeaccount auditOrgaFeeaccount = feeaaccountService
                    .getFeeaccountByRowguid(auditTask.getFeeaccountguid()).getResult();
            if (auditOrgaFeeaccount != null) {
                this.addCallbackParam("feeaccount", auditOrgaFeeaccount.getAccountname());
            }
        }

        // ************开始**************
        // add by yrchan,2022-04-18,勘验事项情形， 查询勘验事项情形数据
        is_inquest = ZwfwConstant.CONSTANT_STR_ZERO;

        if (StringUtil.isNotBlank(compareTaskGuid)) {
            if (compareAuditTaskExtension != null
                    && ZwfwConstant.CONSTANT_STR_ONE.equals(compareAuditTaskExtension.getStr("is_inquest"))) {
                is_inquest = ZwfwConstant.CONSTANT_STR_ONE;
                addCallbackParam("is_inquest", ZwfwConstant.CONSTANT_STR_ONE);
                List<AuditTaskInquestsituation> situationnameList = iAuditTaskInquestsituationService
                        .listAuditTaskInquestsituationByTaskGuid("situationname", compareTaskGuid);
                StringBuilder situationNames = new StringBuilder();
                if (!situationnameList.isEmpty()) {
                    for (AuditTaskInquestsituation auditTaskInquestsituation : situationnameList) {
                        situationNames.append(auditTaskInquestsituation.getSituationname()).append(";");
                    }
                }
                addCallbackParam("taskinquestname", situationNames.toString());
            }
        }
        else {
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
                is_inquest = ZwfwConstant.CONSTANT_STR_ONE;

                addCallbackParam("is_inquest", ZwfwConstant.CONSTANT_STR_ONE);
                List<AuditTaskInquestsituation> situationnameList = iAuditTaskInquestsituationService
                        .listAuditTaskInquestsituationByTaskGuid("situationname", auditTask.getRowguid());
                StringBuilder situationNames = new StringBuilder();
                if (!situationnameList.isEmpty()) {
                    for (AuditTaskInquestsituation auditTaskInquestsituation : situationnameList) {
                        situationNames.append(auditTaskInquestsituation.getSituationname()).append(";");
                    }
                }
                addCallbackParam("taskinquestname", situationNames.toString());
            }
        }
        // ************结束**************
    }

    /**
     * 
     * 把当前的guid和比较的guid存入view
     * 
     * @param currentTaskGuid
     * @param compareTaskGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addTaskGuidToView(String currentTaskGuid, String compareTaskGuid) {
        this.addViewData("currentTaskGuid", currentTaskGuid);
        this.addViewData("compareTaskGuid", compareTaskGuid);
    }

    public AuditTask getAuditTask() {
        return auditTask;
    }

    public void setAuditTask(AuditTask auditTask) {
        this.auditTask = auditTask;
    }

    public AuditTaskExtension getAuditTaskExtension() {
        return auditTaskExtension;
    }

    public void setAuditTaskExtension(AuditTaskExtension auditTaskExtension) {
        this.auditTaskExtension = auditTaskExtension;
    }

    /**
     * 
     * 把事项和类型信息保存到表中
     * 
     * @param guidList
     *            前台获取的事项guid 用“;”分割
     * @param windowGuid
     *            窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void listTaskMap(String taskid) {
        // String result = auditTaskDictBizlogic.listTaskMap(taskid);

        String msg = "";
        if (StringUtil.isNotBlank(taskid)) {
            Map<String, String> map = new HashMap<String, String>(16);
            map.put("task_id=", taskid);
            List<AuditTaskMap> resultList = auditTaskMapImpl.getAuditTaskMapList(map).getResult();
            for (AuditTaskMap auditTaskMap : resultList) {
                String finalname = "";
                AuditTaskDict dict = auditTaskDictImpl.getAuditTaskDictByRowguid(auditTaskMap.getDictid()).getResult();
                if (dict == null) {
                    continue;
                }
                if (StringUtil.isBlank(dict.getParentdictid())) {
                    finalname = dict.getClassname();
                }
                else {
                    Map<String, String> map2 = new HashMap<String, String>(16);
                    map2.put("rowguid=", dict.getParentdictid());
                    AuditTaskDict pDict = auditTaskDictImpl.getAuditTaskDictList(map2).getResult().get(0);
                    if (StringUtil.isBlank(pDict.getParentdictid())) {
                        finalname = pDict.getClassname() + "-" + dict.getClassname();
                    }
                    else {
                        map2.put("rowguid=", pDict.getParentdictid());
                        AuditTaskDict gDict = auditTaskDictImpl.getAuditTaskDictList(map2).getResult().get(0);
                        finalname = gDict.getClassname() + "-" + pDict.getClassname() + "-" + dict.getClassname();
                    }
                }
                msg += finalname + ";";
            }
        }
        this.addCallbackParam("msg", msg);
    }

    /**
     * 
     * 对比两个版本事项
     * 
     * @param currentTaskGuid
     * @param compareTaskGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void compareTwoVersion(String currentTaskGuid, String compareTaskGuid) {
        this.addViewData("currentTaskGuid", currentTaskGuid);
        this.addViewData("compareTaskGuid", compareTaskGuid);
    }

    /**
     * 附件下载地址
     * 
     * @param cliengguid
     *            业务guid
     *
     */
    public String getTempUrl(String attachguid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(attachguid)) {
            // FrameAttachInfo frameAttachInfo =
            // attachServiceImpl.getAttachInfoDetail(cliengguid);
            FrameAttachInfo frameAttachInfo = attachServiceImpl.getAttachInfoDetail(attachguid);
            if (frameAttachInfo != null && StringUtils.isNoneBlank(frameAttachInfo.getAttachFileName())) {
                String strURL = "onclick=\"goToAttach('" + frameAttachInfo.getAttachGuid() + "')\"";
                wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" " + strURL
                        + ">" + frameAttachInfo.getAttachFileName() + "</a>&nbsp;&nbsp;";
            }
            else {
                wsmbName = "无附件！";
            }
        }
        else {
            wsmbName = "无附件！";
        }
        return wsmbName;
    }

    public String getOutflowimgattachname() {
        return outflowimgattachname;
    }

    public String getOutflowimgattachguid() {
        return outflowimgattachguid;
    }

    public void setOutflowimgattachname(String outflowimgattachname) {
        this.outflowimgattachname = outflowimgattachname;
    }

    public void setOutflowimgattachguid(String outflowimgattachguid) {
        this.outflowimgattachguid = outflowimgattachguid;
    }

    public DeptSelfBuildSystem getDeptSelfBuildSystem() {
        return deptSelfBuildSystem;
    }

    public void setDeptSelfBuildSystem(DeptSelfBuildSystem deptSelfBuildSystem) {
        this.deptSelfBuildSystem = deptSelfBuildSystem;
    }

    /**
     * add by yrchan,2022-04-24,是否勘验
     * 
     * @return
     */
    public String getIs_inquest() {
        return is_inquest;
    }

    public void setIs_inquest(String is_inquest) {
        this.is_inquest = is_inquest;
    }

}

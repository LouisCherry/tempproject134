package com.epoint.zczwfw.audittask.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.service.DeptSelfBuildSystemService;
import com.epoint.zczwfw.audittaskinquestsituation.api.IAuditTaskInquestsituationService;
import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;

@RestController("zcaudittaskextendcomparedetailaction")
@Scope("request")
public class ZCAuditTaskExtendCompareDetailAction extends BaseController
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
    private IAuditTaskExtension auditTaskExtensionImpl;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService attachService;

    /**
     * 勘验事项情形API
     */
    @Autowired
    private IAuditTaskInquestsituationService iAuditTaskInquestsituationService;

    /**
     * 事项标识
     */
    private String taskGuid;
    /**
     * 事项基本信息
     */
    private AuditTask auditTask;

    private DeptSelfBuildSystem deptSelfBuildSystem;
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

    @Autowired
    private IAuditOrgaFeeaccount feeaaccountService;
    @Autowired
    private DeptSelfBuildSystemService deptSelfBuildSystemService;

    @Override
    public void pageLoad() {
        taskGuid = this.getRequestParameter("taskGuid");
        auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();

        if (auditTask == null) {
            auditTask = new AuditTask();
        }
        String rowguid = auditTask.get("selfBuildSystemGuid");
        if (rowguid != null) {
            deptSelfBuildSystem = deptSelfBuildSystemService.findSelfBuildSystemByRowGuid(rowguid);
        }
        auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false).getResult();
        if (auditTaskExtension == null) {
            auditTaskExtension = new AuditTaskExtension();
        }
        if (StringUtil.isBlank(auditTaskExtension.getIs_notopendoc())) {
            addCallbackParam("notopendoc", "无");
        }

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
        // 对比版本的是否物流快递
        this.addCallbackParam("expressflag", auditTaskExtension.getIf_express());
        // 自建系统判断
        this.addCallbackParam("iszijianxitong", auditTaskExtension.getIszijianxitong());
        this.addCallbackParam("taskid", auditTask.getTask_id());
        if (StringUtil.isNotBlank(auditTask.getFeeaccountguid())) {
            AuditOrgaFeeaccount auditOrgaFeeaccount = feeaaccountService
                    .getFeeaccountByRowguid(auditTask.getFeeaccountguid()).getResult();
            if (auditOrgaFeeaccount != null) {
                this.addCallbackParam("feeaccount", auditOrgaFeeaccount.getAccountname());
            }
        }

        // ************开始**************
        // add by yrchan,2022-04-18,勘验事项情形， 查询勘验事项情形数据
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
            addCallbackParam("is_inquest", ZwfwConstant.CONSTANT_STR_ONE);
            List<AuditTaskInquestsituation> situationnameList = iAuditTaskInquestsituationService
                    .listAuditTaskInquestsituationByTaskGuid("situationname", taskGuid);
            StringBuilder situationNames = new StringBuilder();
            if (!situationnameList.isEmpty()) {
                for (AuditTaskInquestsituation auditTaskInquestsituation : situationnameList) {
                    situationNames.append(auditTaskInquestsituation.getSituationname()).append(";");
                }
            }
            addCallbackParam("taskinquestname", situationNames.toString());
        }
        // ************结束**************
    }

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

}

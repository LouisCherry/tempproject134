package com.epoint.zczwfw.workflow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.workflow.controller.execute.WorkflowHistoryAction;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 工作流历史意见
 * 
 * @作者 jxw
 * @version [版本号, 2016年3月24日]
 */
@RestController("zczwfwworkflowhistoryaction")
@Scope("request")
public class ZCZWFWWorkflowHistoryAction extends WorkflowHistoryAction
{

    /**
     * 
     */
    private static final long serialVersionUID = -7754134313559315819L;

    /**
     * 获取流程版本实例操作API（注意：TCC主业务服务中不允许调用）
     */
    @Autowired
    private IWFInstanceAPI9 instanceapi;

    /**
     * 获取流程流转页面信息API（注意：TCC主业务服中不允许调用）
     */
    @Autowired
    private IWFInitPageAPI9 initapi;

    /**
     * 获取工作流流程定义信息API
     */
    @Autowired
    private IWFDefineAPI9 defineapi;

    @Autowired
    private IAttachService attachServiceImpl;

    @Autowired
    private IAuditProject iauditproject;

    @Override
    public void pageLoad() {
    }

    /**
     * 返回历史记录
     * 
     * @param dataOptions
     */
    public String historyCtrl(Map<String, String> dataOptions) {
        JSONObject jsonobject = new JSONObject();
        String workitemGuid = getRequestParameter("WorkItemGuid");
        String activityGuid = getRequestParameter("activityGuid");
        String processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        if (StringUtil.isBlank(processVersionInstanceGuid)) {
            AuditProject auditproject = iauditproject.getAuditProjectByRowGuid("pviguid",
                    getRequestParameter("projectguid"), ZwfwUserSession.getInstance().getAreaCode()).getResult();
            // 办件删除时提示
            if (auditproject == null) {
                // 办件可能是在其他辖区，需要查看所有的事项
                auditproject = iauditproject
                        .getAuditProjectByRowGuid("pviguid", getRequestParameter("projectguid"), null).getResult();
            }
            if (auditproject != null) {
                processVersionInstanceGuid = auditproject.getPviguid();
            }
        }
        ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(processVersionInstanceGuid);
        if (StringUtil.isBlank(workitemGuid) && StringUtil.isNotBlank(activityGuid)) {
            WorkflowActivity wa = defineapi.getActivity(pvi, activityGuid);
            // 如果是浏览活动
            if (wa.getActivityType() == WorkflowKeyNames9.ActivityType_Browser) {
                List<WorkflowWorkItem> lswi = instanceapi.getWorkItemListByPVIGuidAndStatus(pvi, null);
                if (lswi.size() > 0) {
                    WorkflowWorkItem item = lswi.get(lswi.size() - 1);
                    workitemGuid = item.getWorkItemGuid();
                    jsonobject.put("workitemguid", workitemGuid);

                }
            }
        }
        else
            jsonobject.put("workitemguid", workitemGuid);
        jsonobject.put("pviguid", processVersionInstanceGuid);
        String str = initapi.init_getWorkItemList(jsonobject.toString());
        JSONObject rtnjson = JSONObject.parseObject(str);
        List<Map<String, Object>> datalist = (List<Map<String, Object>>) rtnjson.get("data");
        List<Map<String, Object>> rtnlist = new ArrayList<Map<String, Object>>();
        Map<Object, Object> rtnmap = new HashMap<>();
        if (datalist != null) {
            for (int i = 0; i < datalist.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) datalist.get(i);
                String rtnsee = checkFiles(map.get("activityinstanceguid").toString());
                map.put("opinion", rtnsee + map.get("opinion"));

                // *************开始*************
                // add by yrchan,2022-04-18,流程信息，新增列“附件”
                List<FrameAttachInfo> infoList = attachServiceImpl
                        .getAttachInfoListByGuid(map.get("activityinstanceguid").toString());
                if (!infoList.isEmpty()) {
                    map.put("attachlist", infoList);
                }
                // *************结束*************

                rtnlist.add(map);
            }
            rtnmap.put("total", datalist.size());
            rtnmap.put("data", rtnlist);
        }
        return rtnjson.toString();
    }

    public String checkFiles(String id) {
        List<FrameAttachInfo> frameattachlist = attachServiceImpl.getAttachInfoListByGuid(id);
        if (frameattachlist != null && frameattachlist.size() > 0) {
            return "<span style=\"color:#F00\">【有附件】</span> ";
        }
        else {
            return "";
        }
    }

}

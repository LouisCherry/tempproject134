package com.epoint.common.zwfw.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditonlineuser.auditonlinemessages.domain.AuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdefaultuser.domain.AuditProjectDefaultuser;
import com.epoint.basic.auditproject.auditprojectdefaultuser.inter.IAuditProjectDefaultuser;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.ProjectConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handledoc.inter.IHandleDoc;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.remind.entity.MessagesMessage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.custom.WorkFlowVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivityOperation;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.entity.organization.WorkflowUser;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.common.runtime.WorkflowResult9;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.common.util.WorkflowUtil9;
import com.epoint.workflow.service.config.api.IWorkflowActOperationService;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFEngineAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.workflow.service.core.api.IWFManageAPI9;
import com.epoint.workflow.service.organ.api.IWFOrganAPI9;
import com.epoint.zczwfw.evaluateproject.api.IEvaluateProjectService;
import com.epoint.zczwfw.evaluateproject.api.entity.EvaluateProject;
import com.epoint.zczwfw.zccommon.api.IZcCommonService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class AuditWorkflowBizlogic
{
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 事项接口初始化
     */
    IWFOrganAPI9 orgaService = ContainerFactory.getContainInfo().getComponent(IWFOrganAPI9.class);
    IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
            .getComponent(IAuditTaskExtension.class);
    IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
    IWorkflowActOperationService workflowActOperationService = ContainerFactory.getContainInfo()
            .getComponent(IWorkflowActOperationService.class);
    IAuditSpISubapp auditSpISubappService = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
    IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo()
            .getComponent(IAuditRsItemBaseinfo.class);

    IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);

    IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo().getComponent(IAuditOnlineProject.class);

    // 新增证照联办推送
    IAuditSpBusiness auditSpBusinessService = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
    private IWorkflowActivityService workflowActivityService9 = ContainerFactory.getContainInfo()
            .getComponent(IWorkflowActivityService.class);

    /**
     * 空构造函数 所有的seam组件必须有空构造函数(使用容器的em)
     */
    public AuditWorkflowBizlogic() {
        super();
    }

    /**
     * 工作流引擎初始化
     */
    public HashMap<String, String> startProjectWorkflow(String taskGuid, String projectGuid, String ApplyerName,
            String userGuid, String userName) {
        String fields = " rowguid,taskguid,projectname,pviguid,status,handleareacode,currentareacode,xiangmubh ";
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, false).getResult();
        String areaCode = "";
        if (auditTask != null) {
            areaCode = auditTask.getAreacode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                .getResult();
        WorkflowWorkItem nextWorkflowItem;
        WorkflowActivityOperation operation;
        Boolean flag = false;
        Boolean workflowError = false;
        WorkflowWorkItem newWorkflowItem = new WorkflowWorkItem();
        // 如果已经初始化过就不再启动流程
        if (StringUtil.isBlank(auditProject.getPviguid())) {
            IWFEngineAPI9 workflowApi = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);

            String processGuid = auditTask.getProcessguid();
            // 启动流程操作
            WorkflowParameter9 param = new WorkflowParameter9();
            param.setProcessGuid(processGuid);
            param.setSendGuid(userGuid);
            param.setOperateType(WorkflowKeyNames9.OperationType_Start);

            JSONObject outValueMap = new JSONObject();
            outValueMap.put("ProjectGuid", auditProject.getRowguid() + "&TaskGuid=" + auditProject.getTaskguid());
            param.setOutValueMap(outValueMap);
            try {
                String paramJson = param.ConvertToJson().toString();
                WorkflowResult9 workFolwResult = workflowApi.operate(paramJson);
                // workflowApi.saveResultTCC(null, workFolwResult);
                WorkFlowVersionInstance WAI = workFolwResult.getCurrentInstance();
                // 这里设置相关参数
                IWFInstanceAPI9 worflowInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
                IWFManageAPI9 worflowManage = ContainerFactory.getContainInfo().getComponent(IWFManageAPI9.class);
                worflowInstance.createOrUpdateContext(WAI.getProcessVersionInstance(), "ProjectGuid",
                        auditProject.getRowguid() + "&TaskGuid=" + auditProject.getTaskguid(), 2);
                if (StringUtil.isNotBlank(auditProject.getXiangmubh())) {
                    AuditRsItemBaseinfo itembaseinfo = iauditrsitembaseinfo
                            .getAuditRsItemBaseinfoByRowguid(auditProject.getXiangmubh()).getResult();
                    if (itembaseinfo != null) {
                        ApplyerName = itembaseinfo.getItemname();
                    }
                }
                worflowInstance.createOrUpdateContext(WAI.getProcessVersionInstance(), "ApplyerName", ApplyerName, 2);

                // 获取第一个工作项,并取得该工作项对应的操作按钮
                nextWorkflowItem = WAI.getActInstanceList().get(1).getWorkItemList().get(0);
                List<WorkflowWorkItem> workflowItemList = WAI.getActInstanceList().get(1).getWorkItemList();
                List<String> tranguidList = new ArrayList<String>();
                for (WorkflowWorkItem workflowItem : workflowItemList) {
                    tranguidList.add(workflowItem.getTransactor());
                    if (UserSession.getInstance().getUserGuid().equals(workflowItem.getTransactor())) {
                        nextWorkflowItem = workflowItem;
                    }
                }
                if (!tranguidList.contains(UserSession.getInstance().getUserGuid())) {
                    flag = true;
                    newWorkflowItem = nextWorkflowItem.clone();
                    newWorkflowItem.setTransactor(UserSession.getInstance().getUserGuid());
                    newWorkflowItem.setTransactorName(UserSession.getInstance().getDisplayName());
                    String workItemGuid = UUID.randomUUID().toString();
                    newWorkflowItem.setWorkItemGuid(workItemGuid);
                    newWorkflowItem.setWaitHandleGuid(UUID.randomUUID().toString());
                    String[] url = nextWorkflowItem.getHandleUrl().split("&");
                    newWorkflowItem.setHandleUrl(url[0] + "&WorkItemGuid=" + workItemGuid);
                    worflowManage.addWorkItem(newWorkflowItem, false);
                    for (WorkflowWorkItem workflowItem : workflowItemList) {
                        worflowManage.deleteWorkItem(WAI.getProcessVersionInstance(), workflowItem.getWorkItemGuid());
                    }
                }
                WorkflowActivity workflowActivity = WAI.getActInstanceList().get(1).getActivity();
                operation = workflowActOperationService.selectByActivityGuid(workflowActivity.getActivityGuid()).get(0);

                Map<String, String> updateFieldMap = new HashMap<>(16);
                updateFieldMap.put("pviguid=", nextWorkflowItem.getProcessVersionInstanceGuid());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("rowguid", projectGuid);
                sql.eq("areacode", areaCode);
                auditProjectService.updateProject(updateFieldMap, sql.getMap());
            }
            catch (Exception ex) {
                operation = null;
                nextWorkflowItem = null;
                workflowError = true;
                ex.printStackTrace();
            }
        }
        else {
            IWFInstanceAPI9 wfinstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pVersionInstance = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
            // 会报错
            // List<Integer> status =
            // Arrays.asList(WorkflowKeyNames9.WorkItemStatus_Inactive);
            // List<WorkflowWorkItem> workflowWorkItems =
            // wfinstance.getWorkItemListByPVIGuidAndStatus(pVersionInstance,
            // status);
            IWFManageAPI9 worflowManage = ContainerFactory.getContainInfo().getComponent(IWFManageAPI9.class);
            List<WorkflowWorkItem> workflowWorkItems = wfinstance.getWorkItemListByPVIGuidAndStatus(pVersionInstance,
                    null);
            nextWorkflowItem = workflowWorkItems.get(0);
            List<String> tranguidList = new ArrayList<String>();
            for (WorkflowWorkItem workflowItem : workflowWorkItems) {
                tranguidList.add(workflowItem.getTransactor());
                if (UserSession.getInstance().getUserGuid().equals(workflowItem.getTransactor())) {
                    nextWorkflowItem = workflowItem;
                }
            }
            if (!tranguidList.contains(UserSession.getInstance().getUserGuid())) {
                flag = true;
                newWorkflowItem = nextWorkflowItem.clone();
                newWorkflowItem.setTransactor(UserSession.getInstance().getUserGuid());
                newWorkflowItem.setTransactorName(UserSession.getInstance().getDisplayName());
                String workItemGuid = UUID.randomUUID().toString();
                newWorkflowItem.setWorkItemGuid(workItemGuid);
                newWorkflowItem.setWaitHandleGuid(UUID.randomUUID().toString());
                String[] url = nextWorkflowItem.getHandleUrl().split("&");
                newWorkflowItem.setHandleUrl(url[0] + "&WorkItemGuid=" + workItemGuid);
                worflowManage.addWorkItem(newWorkflowItem, false);
                for (WorkflowWorkItem workflowItem : workflowWorkItems) {
                    worflowManage.deleteWorkItem(pVersionInstance, workflowItem.getWorkItemGuid());
                }
            }
            operation = workflowActOperationService.selectByActivityGuid(nextWorkflowItem.getActivityGuid()).get(0);
        }
        // 返回需要的几个值，如果需要额外的，可以再进行添加
        HashMap<String, String> rtnValue = new HashMap<String, String>(16);
        if (workflowError) {
            rtnValue.put("message", "该事项内部流程配置异常，请重新配置！");
        }
        else {
            String operationGuid = operation.getOperationGuid();
            rtnValue.put("operationGuid", operationGuid);
            String transitionGuid = operation.getTransitionGuid();
            if (transitionGuid == null) {
                transitionGuid = "";
            }
            rtnValue.put("transitionGuid", transitionGuid);
            String workItemGuid = nextWorkflowItem.getWorkItemGuid();
            if (flag) {
                rtnValue.put("workItemGuid", newWorkflowItem.getWorkItemGuid());
            }
            else {
                rtnValue.put("workItemGuid", workItemGuid);
            }
            String pviGuid = nextWorkflowItem.getProcessVersionInstanceGuid();
            rtnValue.put("pviGuid", pviGuid);
        }
        return rtnValue;
    }

    private JSONObject convertToEngineData(JSONObject jsonobject) {
        if (jsonobject == null || !jsonobject.containsKey("nextsteplist")) {
            return jsonobject;
        }
        JSONArray array = WorkflowUtil9.getJSONArray(jsonobject.get("nextsteplist"));
        if (array == null || array.size() == 0) {
            return jsonobject;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONArray list = new JSONArray();
            JSONObject object1 = WorkflowUtil9.getJSONObject(array.get(i));
            if (!object1.containsKey("handlerlist")) {
                return jsonobject;
            }
            Object handleobject = object1.get("handlerlist");
            if (handleobject instanceof JSONObject) {
                JSONObject object2 = (JSONObject) handleobject;
                if (!object2.containsKey("values")) {
                    return jsonobject;
                }
                String values = object2.get("values").toString();
                String[] itemlist = values.trim().split(",");
                String texts = object2.get("texts").toString();
                String[] itemtextlist = texts.trim().split(",");
                for (int index = 0; index < itemlist.length; index++) {
                    String item = itemlist[index];
                    if (StringUtil.isNotBlank(item)) {
                        if (item.indexOf(EpointKeyNames9.SPECIAL_SPLITCHAR_1) >= 0) {
                            String ouguid = item.split(EpointKeyNames9.SPECIAL_SPLITCHAR_1)[1];
                            String userguid = item.split(EpointKeyNames9.SPECIAL_SPLITCHAR_1)[0];
                            Map<String, Object> useritem = new HashMap<String, Object>();
                            useritem.put("handlerguid", userguid);
                            if (StringUtil.isBlank(ouguid)) {
                                WorkflowUser user = orgaService.getUserByUserField("userguid", item);
                                if (user != null) {
                                    useritem.put("handlerguid", user.getUserGuid());
                                    useritem.put("handlername", user.getDisplayName());
                                    useritem.put("ouguid", user.getOuGuid());
                                    list.add(useritem);
                                }
                            }
                            else {
                                String itemtext = (itemtextlist != null && itemtextlist.length > index)
                                        ? itemtextlist[index]
                                        : "";
                                useritem.put("handlername", itemtext);
                                useritem.put("ouguid", ouguid);
                            }
                            list.add(useritem);
                        }
                        else {
                            WorkflowUser user = orgaService.getUserByUserField("userguid", item);
                            if (user != null) {
                                Map<String, Object> useritem = new HashMap<String, Object>();
                                useritem.put("handlerguid", user.getUserGuid());
                                useritem.put("handlername", user.getDisplayName());
                                useritem.put("ouguid", user.getOuGuid());
                                list.add(useritem);
                            }
                        }
                    }
                }
                object1.remove("handlerlist");
                object1.put("handlerlist", list);
            }
        }
        jsonobject.put("nextsteplist", array);
        return jsonobject;
    }

    /**
     * 统一的送下一步操作，在该操作中需要根据操作按钮的备注信息来判断实际进行的是什么操作
     * 业务操作包括：受理\handleaccept、审批通过\handlepass、审批不通过\handlenopass、办结\handlefinish
     * 
     * @param data
     * @param userGuid
     * @return
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    public String operate(String data, String userGuid, AuditProject auditProject) {
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);

        data = EncodeUtil.decode(data);
        JSONObject jsonobject = JSON.parseObject(data);
        jsonobject.put("userguid", userGuid);
        // String operationGuid = jsonobject.get("operationguid").toString();
        // JSONObject contexts = new JSONObject();
        // WorkflowParameter9 param = new WorkflowParameter9();

        // contexts.put("TaskGuid", auditProject.getTaskguid());
        /*
         * contexts.put("ProjectGuid", auditProject.getRowguid() + "&TaskGuid="
         * + auditProject.getTaskguid()); jsonobject.put("contexts", contexts);
         */
        String json = jsonobject.toString();

        IWFEngineAPI9 wfengine = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
        IWFInstanceAPI9 wf9instance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        JSONObject returnjsonobject = new JSONObject();
        ProcessVersionInstance pvi = wf9instance.getProcessVersionInstance(String.valueOf(jsonobject.get("pviguid")));
        if (auditProject == null) {
            return null;
        }
        if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_BYSL) {
            returnjsonobject.put("message", "该办件已经不予受理，不能再次处理！|close");
            finish(jsonobject.get("workitemguid").toString(), "", pvi, jsonobject.get("operationguid").toString());
            return returnjsonobject.toString();
        }
        // 将流程的申请人名称重新加载进去
        String ApplyerName = auditProject.getApplyername();
        if (StringUtil.isNotBlank(auditProject.getXiangmubh())) {
            AuditRsItemBaseinfo itembaseinfo = iauditrsitembaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditProject.getXiangmubh()).getResult();
            if (itembaseinfo != null) {
                ApplyerName = itembaseinfo.getItemname();
            }
        }
        wf9instance.createOrUpdateContext(pvi, "ApplyerName", ApplyerName, 2);

        String msg = "";
        WorkflowResult9 result;
        try {
            result = wfengine.operate(json);
        }
        catch (Exception e) {
            e.printStackTrace();
            pvi = wf9instance.getProcessVersionInstance(String.valueOf(jsonobject.get("pviguid")));
            wf9instance.unlockPvi(pvi);
            if (e.getMessage().contains("EWF-013")) {
                returnjsonobject.put("message", "当前步骤已经处理完成，无需再次处理！|close");
            }
            else {
                returnjsonobject.put("message", "流程处理出错！");
            }
            return returnjsonobject.toString();
        }

        List<MessagesCenter> listMessage = result.getMessageList();
        boolean redirect = false;
        String handleUrl = "";
        for (int i = 0; i < listMessage.size(); i++) {
            if ("办理".equals(listMessage.get(i).getMessageType())
                    && userGuid.equals(listMessage.get(i).getTargetUser())) {
                redirect = true;
                handleUrl = listMessage.get(i).getHandleUrl();
                break;
            }
        }

        // 保存处理过办件的区域编码(不重复保存)
        String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
        String areaStr = "";
        if (StringUtil.isBlank(auditProject.getHandleareacode())) {
            areaStr = handleAreaCode + ",";
        }
        else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
            areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
        }
        if (StringUtil.isNotBlank(areaStr)) {
            auditProject.setHandleareacode(areaStr);
        }
        IUserServiceInternal frameUserService = ContainerFactory.getContainInfo()
                .getComponent(IUserServiceInternal.class);
        // 转换json串
        jsonobject = convertToEngineData(jsonobject);
        if (jsonobject.containsKey("nextsteplist")) {
            String nextsteplist = jsonobject.get("nextsteplist").toString();
            JSONArray jsonnextsteplist = JSON.parseArray(nextsteplist);
            List<Map<String, Object>> mapListJson = (List) jsonnextsteplist;
            // 取出数组第一个元素
            Map<String, Object> o = mapListJson.get(0);
            Object o1 = o.get("handlerlist");
            List<Map<String, String>> list = (List<Map<String, String>>) o1;
            IOuServiceInternal frameOuService = ContainerFactory.getContainInfo()
                    .getComponent(IOuServiceInternal.class);
            auditProject.setCurrentareacode("");
            if (list != null && !list.isEmpty()) {
                for (Map<String, String> map : list) {
                    FrameUser user = frameUserService.getUserByUserField("userguid", map.get("handlerguid"));
                    if(user != null){
                        String ouguid = user.getOuGuid();
                        FrameOuExtendInfo frameOuExtendInfo = frameOuService.getFrameOuExtendInfo(ouguid);
                        if (StringUtil.isNotBlank(frameOuExtendInfo.get("areacode"))) {
                            auditProject.setCurrentareacode(frameOuExtendInfo.get("areacode"));
                            break;
                        }
                    }
                }
            }
        }
        IHandleProject handleProjectService = ContainerFactory.getContainInfo().getComponent(IHandleProject.class);
        /* handleProjectService.saveProject(auditProject); */

        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            IAuditProjectDefaultuser defaultuserService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectDefaultuser.class);
            IWFDefineAPI9 iWFDefineAPI9 = ContainerFactory.getContainInfo().getComponent(IWFDefineAPI9.class);
            IAuditTaskRiskpoint iAuditTaskRiskpoint = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskRiskpoint.class);
            // 有下一步
            if (jsonobject.containsKey("nextsteplist")) {
                String nextsteplist = jsonobject.get("nextsteplist").toString();
                JSONArray jsonArray = JSON.parseArray(nextsteplist);
                /*
                 * // 取出数组第一个元素 JSONObject jUser =
                 * jsonArray.getJSONObject(0).getJSONObject("handlerlist"); //
                 * 取出第一个元素的信息，并且转化为JSONObject String nextguid =
                 * jUser.getString("values"); String[] nextguids =
                 * nextguid.split(",");
                 */
                JSONArray jsonnextsteplist = JSON.parseArray(nextsteplist);
                List<Map<String, Object>> mapListJson = (List) jsonnextsteplist;
                // 取出数组第一个元素
                Map<String, Object> o = mapListJson.get(0);
                Object o1 = o.get("handlerlist");
                List<Map<String, String>> list = (List<Map<String, String>>) o1;
                List<String> nextguids = new ArrayList<String>();
                for (Map<String, String> map : list) {
                    nextguids.add(map.get("handlerguid"));
                }
                String stepguid = jsonArray.getJSONObject(0).get("stepguid").toString();
                ProcessVersionInstance pvinstance = wf9instance
                        .getProcessVersionInstance(String.valueOf(jsonobject.get("pviguid")));
                WorkflowTransition transition = iWFDefineAPI9.getTransition(pvinstance, stepguid);
                AuditTaskRiskpoint riskpoint = iAuditTaskRiskpoint
                        .getAuditTaskRiskpointByActivityguid(transition.getToActivityGuid(), true).getResult();
                String currentareacode = auditProject.getCurrentareacode();
                IAuditOrgaArea iAuditOrgaArea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
                AuditOrgaArea currentarea = iAuditOrgaArea.getAreaByAreacode(currentareacode).getResult();
                AuditOrgaArea handlearea = iAuditOrgaArea.getAreaByAreacode(handleAreaCode).getResult();
                if (currentarea != null && !ZwfwConstant.AREA_TYPE_SJ.equals(handlearea.getCitylevel())
                        && !(ZwfwConstant.AREA_TYPE_XQJ.equals(currentarea.getCitylevel())
                                || ZwfwConstant.AREA_TYPE_SJ.equals(currentarea.getCitylevel()))) {
                    String usertype = "";
                    if (currentarea != null) {
                        if (ZwfwConstant.AREA_TYPE_XZJ.equals(currentarea.getCitylevel())) {
                            usertype = ZwfwConstant.ZWFW_DEFAULTUSERTYPE_ZJ;
                        }
                        else if (ZwfwConstant.AREA_TYPE_XQJ.equals(currentarea.getCitylevel())
                                || ZwfwConstant.AREA_TYPE_SJ.equals(currentarea.getCitylevel())) {
                            usertype = ZwfwConstant.ZWFW_DEFAULTUSERTYPE_SJ;
                        }
                        else if (ZwfwConstant.AREA_TYPE_CJ.equals(currentarea.getCitylevel())) {
                            usertype = ZwfwConstant.ZWFW_DEFAULTUSERTYPE_CJ;
                        }
                    }
                    if (riskpoint != null) {
                        // 先删除办件预设，再增加当前选择人员
                        defaultuserService.deleteDefaultuserByRpid(riskpoint.getRiskpointid(), usertype);
                    }
                    for (String nextuserguid : nextguids) {
                        AuditProjectDefaultuser auditProjectDefaultuser = new AuditProjectDefaultuser();
                        auditProjectDefaultuser.setRowguid(UUID.randomUUID().toString());
                        auditProjectDefaultuser.setDefaultuserguid(nextuserguid);
                        String userName = frameUserService.getUserNameByUserGuid(nextuserguid);
                        auditProjectDefaultuser.setDefaultusername(userName);
                        auditProjectDefaultuser.setTaskid(auditProject.getTaskid());
                        if (riskpoint != null) {
                            auditProjectDefaultuser.setRiskpointid(riskpoint.getRiskpointid());
                        }
                        auditProjectDefaultuser.setAddouguid(UserSession.getInstance().getOuGuid());
                        auditProjectDefaultuser.setAdduserguid(userGuid);
                        auditProjectDefaultuser.setDefaultusertype(usertype);
                        defaultuserService.insertDefaultuser(auditProjectDefaultuser);
                    }
                }
            }
        }

        String message = "";
        String isJSTY = ConfigUtil.getConfigValue("isJSTY");
        // 这个地方作为操作后事件执行的方法，放在这里可以通过TCC事务来统一操作，方便回滚
        switch (String.valueOf(result.getOperation().getNote())) {
            case "handleaccept":
                // TODO

                auditProject.setPviguid(String.valueOf(jsonobject.get("pviguid")));
                // 这里再判断一下是不是直接到办结步骤了
                WorkflowActivity activity = result.getNextActIstanceList().get(0).getActivity();
                boolean pizhun = workflowActivityService9.isTheEndActivity(activity.getProcessVersionGuid(),
                        activity.getActivityGuid());

                EpointFrameDsManager.begin(null);
                handleProjectService.handleAccept(auditProject, String.valueOf(jsonobject.get("workitemguid")),
                        UserSession.getInstance().getDisplayName(), userGuid,
                        ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getWindowGuid(),
                        pizhun ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO);
                EpointFrameDsManager.commit();

                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    if (auditProject.getApplyway() != Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS)
                            && auditProject.getApplyway() != Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {
                        // MQ 受理操作
                        // // MQ 受理操作
                        msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                        // 接办分离 受理
                        msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "."
                                + String.valueOf(jsonobject.get("workitemguid"));
                        sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                + ZwfwUserSession.getInstance().getAreaCode() + ".accept." + auditProject.getTask_id());

                        IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo()
                                .getComponent(IAuditOrgaArea.class);
                        AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                        String areaName = "";
                        if (auditOrgaArea != null) {
                            areaName = auditOrgaArea.getXiaquname();
                        }
                        // MQ 江苏省标
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                                    + areaName + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                        }
                    }
                }

                boolean docflag = true;
                // 事项配置中配置是否不弹出文书
                String is_notOpenDoc = auditTaskExtension.getIs_notopendoc();
                if (StringUtil.isNotBlank(is_notOpenDoc)) {
                    String[] str = is_notOpenDoc.split(",");
                    for (String doctype : str) {
                        if ("1".equals(doctype)) {
                            docflag = false;
                            break;
                        }
                    }
                }
                if (docflag) {
                    // 弹出文书
                    if (auditProject != null) {
                        IHandleConfig handleConfigService = ContainerFactory.getContainInfo()
                                .getComponent(IHandleConfig.class);
                        IHandleDoc handleDocService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
                        String asdocword = handleConfigService
                                .getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
                        boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
                        String address = handleDocService.getDocEditPage(auditProject.getTaskguid(),
                                auditProject.getCenterguid(), auditTaskExtension.getIs_notopendoc(),
                                String.valueOf(ZwfwConstant.DOC_TYPE_SLTZS), false, isword).getResult();
                        if (StringUtil.isNotBlank(address)) {
                            address += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                                    + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskguid();
                        }
                        message = address;
                    }
                }
                // message="accept";
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    // MQ 环节信息
                    msg = "handleProcess:" + auditProject.getRowguid() + ";" + String.valueOf(jsonobject.get("pviguid"))
                            + ";" + String.valueOf(jsonobject.get("workitemguid")) + ";"
                            + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode();
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 环节信息
                    msg = auditProject.getRowguid() + "." + String.valueOf(jsonobject.get("pviguid")) + "."
                            + String.valueOf(jsonobject.get("workitemguid")) + "."
                            + String.valueOf(jsonobject.get("operationguid"));
                    //@author fryu 2024年1月16日 个性化调用三方接口
                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                            + ZwfwUserSession.getInstance().getAreaCode() + ".jnprocess." + auditProject.getTask_id());

                    // MQ 江苏省标
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                        msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                + String.valueOf(jsonobject.get("pviguid")) + ";"
                                + String.valueOf(jsonobject.get("workitemguid")) + ";"
                                + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode()
                                + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                    }
                }

                // *************开始***********
                // add by yrchan,2022-04-21,勘验事项会从小程序调用接口，发送代办给配置的所有窗口人员；
                if (auditTaskExtension != null
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
                    IAuditOrgaWindowYjs iAuditOrgaWindow = ContainerFactory.getContainInfo()
                            .getComponent(IAuditOrgaWindowYjs.class);
                    IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                            .getComponent(IMessagesCenterService.class);

                    // 根据中心标识 centerguid，确定所选事项所在窗口，获取该窗口的关联业务人员，发送【待受理】待办
                    // 根据指定条件获取窗口人员
                    AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                            .getResult();

                    SqlConditionUtil windowUserSql = new SqlConditionUtil();
                    windowUserSql.setSelectFields("distinct userguid,username");
                    windowUserSql.in("windowguid",
                            "(select a.rowguid from audit_orga_window a,audit_orga_windowtask b where a.RowGuid=b.WINDOWGUID AND b.TASKID = '"
                                    + auditTask.getTask_id() + "' and a.centerguid = '" + auditProject.getCenterguid()
                                    + "')");
                    List<AuditOrgaWindowUser> windowUserList = iAuditOrgaWindow.getWindowUser(windowUserSql.getMap())
                            .getResult();
                    if (!windowUserList.isEmpty()) {
                        for (AuditOrgaWindowUser windowUser : windowUserList) {
                            List<MessagesCenter> messagesList = iMessagesCenterService.queryForList(
                                    windowUser.getUserguid(), null, null, "", IMessagesCenterService.MESSAGETYPE_WAIT,
                                    auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
                            if (!messagesList.isEmpty()) {
                                for (MessagesCenter messagescenter : messagesList) {
                                    iMessagesCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                                            messagescenter.getTargetUser());
                                    EpointFrameDsManager.commit();
                                }
                            }
                        }
                    }
                }
                // *************结束***********
                break;
            case "handlepass":
                EpointFrameDsManager.begin(null);
                handleProjectService.handleProjectPass(auditProject, "", userGuid,
                        String.valueOf(jsonobject.get("workitemguid")));
                EpointFrameDsManager.commit();
                // 测试办件不生成
                if (StringUtil.isNotBlank(auditProject.getIs_test())) {
                    if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                        // MQ 环节信息
                        msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                + String.valueOf(jsonobject.get("pviguid")) + ";"
                                + String.valueOf(jsonobject.get("workitemguid")) + ";"
                                + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode();
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                        // 接办分离 环节信息
                        msg = auditProject.getRowguid() + "." + String.valueOf(jsonobject.get("pviguid")) + "."
                                + String.valueOf(jsonobject.get("workitemguid")) + "."
                                + String.valueOf(jsonobject.get("operationguid"));
                        //@author fryu 2024年1月16日 个性化调用三方接口
                        sendMQMessageService.sendByExchange("exchange_handle", msg,
                                "project." + ZwfwUserSession.getInstance().getAreaCode() + ".jnprocess."
                                        + auditProject.getTask_id());
                        // 住建系统对接使用
                        msg = auditProject.getRowguid() + "." + userGuid;
                        sendMQMessageService.sendByExchange("exchange_handle", msg,
                                "project." + ZwfwUserSession.getInstance().getAreaCode() + ".handlepass."
                                        + auditProject.getTask_id());
                        // MQ 江苏省标
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                    + String.valueOf(jsonobject.get("pviguid")) + ";"
                                    + String.valueOf(jsonobject.get("workitemguid")) + ";"
                                    + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode()
                                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                        }
                    }
                }

                // *************开始*************
                // add by yrchan,2022-04-19,审核通过，是勘验事项
                AuditTaskExtension taskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if (taskExtension != null && ZwfwConstant.CONSTANT_STR_ONE.equals(taskExtension.getStr("is_inquest"))) {
                    // 是勘验事项
                    IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                            .getComponent(IMessagesCenterService.class);
                    IZcCommonService iZcCommonService = ContainerFactory.getContainInfo()
                            .getComponent(IZcCommonService.class);

                    // 向短信表（messages_center）插入一条短信记录，短信内容如下：
                    // “[邹城市为民服务中心]xx 您好，您于 2022-03-23 11:32:45
                    // 申请的“农药经营许可”勘验事项，经审核勘验通过，请登录微信小程序“邹城 xxx”进行查看”
                    String dateStr = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                            "yyyy-MM-dd HH:mm:ss");
                    String content = auditProject.getApplyername() + " 您好，您于" + dateStr + "申请的“"
                            + auditProject.getProjectname() + "”勘验事项，经审核勘验通过，请登录“爱山东”应用云勘验模块进行查看”。";

                    iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                            new Date(), auditProject.getContactmobile(), auditProject.getApplyeruserguid(),
                            auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                            UserSession.getInstance().getDisplayName(), "", "", "", false, auditProject.getAreacode());

                    // 小程序消息
                    MessagesMessage messagesMessage = new MessagesMessage();
                    messagesMessage.setMessageGuid(UUID.randomUUID().toString());
                    messagesMessage.setMessageContent(content);
                    messagesMessage.setFromUserID(UserSession.getInstance().getUserGuid());
                    messagesMessage.setFromUserName(UserSession.getInstance().getDisplayName());
                    messagesMessage.setTargetUserID(auditProject.getApplyeruserguid());
                    messagesMessage.setTargetUserName(auditProject.getApplyername());
                    messagesMessage.setClientIdentifier(auditProject.getRowguid());
                    messagesMessage.setSendTime(new Date());
                    messagesMessage.setTitle("审核通过");
                    CommonDao.getInstance().insert(messagesMessage);
                    // 向微信消息记录表（audit_online_messages）插入一条微信消息
                    AuditOnlineMessages auditOnlineMessages = new AuditOnlineMessages();
                    auditOnlineMessages.setRowguid(UUID.randomUUID().toString());
                    auditOnlineMessages.setOperatedate(new Date());
                    auditOnlineMessages.setOperateusername(UserSession.getInstance().getDisplayName());
                    auditOnlineMessages.setYwguid(auditProject.getRowguid());
                    auditOnlineMessages.setInserttime(new Date());
                    auditOnlineMessages.setClientid(auditProject.getStr("openid"));
                    auditOnlineMessages.setTcnote(content);
                    auditOnlineMessages.setType("4");
                    auditOnlineMessages.setSendtime(new Date());
                    iZcCommonService.insert(auditOnlineMessages);
                }
                // *************结束*************

                break;
            case "handlenopass":
                handleProjectService.handleProjectNotPass(auditProject, userGuid,
                        String.valueOf(jsonobject.get("workitemguid")));
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    // MQ 环节信息
                    msg = "handleProcess:" + auditProject.getRowguid() + ";" + String.valueOf(jsonobject.get("pviguid"))
                            + ";" + String.valueOf(jsonobject.get("workitemguid")) + ";"
                            + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode();
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 环节信息
                    msg = auditProject.getRowguid() + "." + String.valueOf(jsonobject.get("pviguid")) + "."
                            + String.valueOf(jsonobject.get("workitemguid")) + "."
                            + String.valueOf(jsonobject.get("operationguid"));
                    //@author fryu 2024年1月16日 个性化调用三方接口
                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                            + ZwfwUserSession.getInstance().getAreaCode() + ".jnprocess." + auditProject.getTask_id());
                    // MQ 江苏省标
                    msg = auditProject.getRowguid() + "." + userGuid;
                    sendMQMessageService.sendByExchange("exchange_handle", msg,
                            "project." + ZwfwUserSession.getInstance().getAreaCode() + ".handlenotpass."
                                    + auditProject.getTask_id());
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                        msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                + String.valueOf(jsonobject.get("pviguid")) + ";"
                                + String.valueOf(jsonobject.get("workitemguid")) + ";"
                                + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode()
                                + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                    }
                }

                // *************开始*************
                // add by yrchan,2022-04-19,审核不通过，是勘验事项
                AuditTaskExtension nopassTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if (nopassTaskExtension != null
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(nopassTaskExtension.getStr("is_inquest"))) {
                    // 是勘验事项
                    IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                            .getComponent(IMessagesCenterService.class);
                    IZcCommonService iZcCommonService = ContainerFactory.getContainInfo()
                            .getComponent(IZcCommonService.class);

                    // 向短信表（messages_center）插入一条短信记录，短信内容如下：
                    // “[邹城市为民服务中心]xx 您好，您于 2022-03-23 11:32:45
                    // 申请的“农药经营许可”勘验事项，经审核勘验通过，请登录微信小程序“邹城 xxx”进行查看”
                    String dateStr = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                            "yyyy-MM-dd HH:mm:ss");
                    String content = auditProject.getApplyername() + " 您好，您于" + dateStr + "申请的“"
                            + auditProject.getProjectname() + "”勘验事项，经审核不符合现场要求，请登录“爱山东”应用云勘验模块进行重新修改”。";

                    iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                            new Date(), auditProject.getContactmobile(), auditProject.getApplyeruserguid(),
                            auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                            UserSession.getInstance().getDisplayName(), "", "", "", false, auditProject.getAreacode());
                    // 小程序消息
                    MessagesMessage messagesMessage = new MessagesMessage();
                    messagesMessage.setMessageGuid(UUID.randomUUID().toString());
                    messagesMessage.setMessageContent(content);
                    messagesMessage.setFromUserID(UserSession.getInstance().getUserGuid());
                    messagesMessage.setFromUserName(UserSession.getInstance().getDisplayName());
                    messagesMessage.setTargetUserID(auditProject.getApplyeruserguid());
                    messagesMessage.setTargetUserName(auditProject.getApplyername());
                    messagesMessage.setClientIdentifier(auditProject.getRowguid());
                    messagesMessage.setSendTime(new Date());
                    messagesMessage.setTitle("审核不通过");
                    CommonDao.getInstance().insert(messagesMessage);
                    // 向微信消息记录表（audit_online_messages）插入一条微信消息
                    AuditOnlineMessages auditOnlineMessages = new AuditOnlineMessages();
                    auditOnlineMessages.setRowguid(UUID.randomUUID().toString());
                    auditOnlineMessages.setOperatedate(new Date());
                    auditOnlineMessages.setOperateusername(UserSession.getInstance().getDisplayName());
                    auditOnlineMessages.setYwguid(auditProject.getRowguid());
                    auditOnlineMessages.setInserttime(new Date());
                    auditOnlineMessages.setClientid(auditProject.getStr("openid"));
                    auditOnlineMessages.setTcnote(content);
                    auditOnlineMessages.setType("4");
                    auditOnlineMessages.setSendtime(new Date());
                    iZcCommonService.insert(auditOnlineMessages);
                }
                // *************结束*************

                // 一件事不予受理按钮添加退回功能
                // 判断是否为一件的单办件
                if (StringUtil.isNotBlank(auditProject.getSubappguid())) {
                    auditProject.set("backreason", jsonobject.get("opinion"));// 退回原因
                    // 更新子申报状态
                    AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(auditProject.getSubappguid())
                            .getResult();
                    if (auditSpISubapp != null) {
                        auditSpISubapp.setStatus("26");
                        auditSpISubappService.updateAuditSpISubapp(auditSpISubapp);
                    }
                    // 更新一件事办件iAuditOnlineProject
                    if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditProject.getBiguid())
                                .getResult();
                        if (auditSpInstance != null) {
                            AuditOnlineProject auditOnlineProject = iAuditOnlineProject.getOnlineProjectByApplyerGuid(
                                    auditProject.getBiguid(), auditSpInstance.getApplyerguid()).getResult();
                            if (auditOnlineProject != null) {
                                auditOnlineProject.setStatus("101");
                                iAuditOnlineProject.updateProject(auditOnlineProject);
                            }
                        }
                    }
                    auditProjectService.updateProject(auditProject);

                }

                break;
            case "handlefinish":
                EpointFrameDsManager.begin(null);
                handleProjectService.handleFinish(auditProject, UserSession.getInstance().getDisplayName(), userGuid,
                        String.valueOf(jsonobject.get("workitemguid")));

                EpointFrameDsManager.commit();
                if (auditProject.getSubappguid() != null) {
                    IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo()
                            .getComponent(IAuditSpBusiness.class);
                    AuditSpBusiness business = iauditspbusiness
                            .getAuditSpBusinessByRowguid(auditProject.getBusinessguid()).getResult();
                    if (business != null && ZwfwConstant.CONSTANT_STR_ONE.equals(business.getBusinesstype())) {
                        handleProjectService.handleblspsub(auditProject, UserSession.getInstance().getUserGuid(),
                                UserSession.getInstance().getDisplayName());
                    }

                    // 若是证照联办产生的办件，则需要回传办结信息给省平台
                    if (StringUtil.isNotBlank(auditProject.getBusinessguid())
                            && StringUtil.isNotBlank(auditProject.getBiguid())) {
                        AuditSpBusiness auditSpBusiness = auditSpBusinessService
                                .getAuditSpBusinessByRowguid(auditProject.getBusinessguid()).getResult();
                        if (StringUtil.isNotBlank(auditSpBusiness) && "6".equals(auditSpBusiness.getBusinesstype())) {
                            String mqMsg = auditProject.getRowguid();
                            sendMQMessageService.sendByExchange("exchange_handle", mqMsg,
                                    "pushprojectinfo." + ProjectConstant.SJZCLX_DSX + "." + auditProject.getRowguid());
                        }
                    }

                }
                message = "finish";
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    // MQ 环节信息
                    msg = "handleProcess:" + auditProject.getRowguid() + ";" + String.valueOf(jsonobject.get("pviguid"))
                            + ";" + String.valueOf(jsonobject.get("workitemguid")) + ";"
                            + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode();
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 环节信息
                    msg = auditProject.getRowguid() + "." + String.valueOf(jsonobject.get("pviguid")) + "."
                            + String.valueOf(jsonobject.get("workitemguid")) + "."
                            + String.valueOf(jsonobject.get("operationguid"));
                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                            + ZwfwUserSession.getInstance().getAreaCode() + ".jnprocess." + auditProject.getTask_id());
                    // MQ 江苏省标
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                        msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                + String.valueOf(jsonobject.get("pviguid")) + ";"
                                + String.valueOf(jsonobject.get("workitemguid")) + ";"
                                + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode()
                                + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                    }

                    // MQ 办结操作
                    msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                            + UserSession.getInstance().getDisplayName();
                    ProducerMQ.sendByExchange("receiveproject", msg, "");

                    log.info("办件推送对应的工改办件编号：" + auditProject.getFlowsn());

                    // 接办分离 办结操作
                    msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "."
                            + UserSession.getInstance().getDisplayName();
                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                            + ZwfwUserSession.getInstance().getAreaCode() + ".sendresult." + auditProject.getTask_id());

                    // MQ 江苏省标
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                        msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                                + UserSession.getInstance().getDisplayName()
                                + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                    }
                }
                // *************开始**************
                // 审批办件仅需保留邹城市级办件，无需算入镇街 ，这项筛选条件改为通过办件ouguid字段关联部门拓展表查询areacode
                // edit by yrchan,2022-04-15,判断该办件辖区是否为370883，若是，则插表评价办件信息表
                if (StringUtil.isNotBlank(auditProject.getOuguid())) {
                    IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
                    FrameOuExtendInfo frameOuExtendInfo = iOuService.getFrameOuExtendInfo(auditProject.getOuguid());

                    if (frameOuExtendInfo != null && "370883".equals(frameOuExtendInfo.getStr("areacode"))) {
                        IEvaluateProjectService iEvaluateProjectService = ContainerFactory.getContainInfo()
                                .getComponent(IEvaluateProjectService.class);
                        IConfigService iConfigService = ContainerFactory.getContainInfo()
                                .getComponent(IConfigService.class);
                        IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                                .getComponent(IMessagesCenterService.class);

                        Date nowDate = new Date();
                        // 5。存放在评价办件信息表（evaluate_project）
                        EvaluateProject evaluateProject = new EvaluateProject();
                        evaluateProject.setRowguid(UUID.randomUUID().toString());
                        evaluateProject.setOperatedate(nowDate);
                        evaluateProject.setOperateusername(UserSession.getInstance().getDisplayName());
                        evaluateProject.setCreat_date(nowDate);
                        evaluateProject.setAccept_user(auditProject.getAcceptusername());
                        evaluateProject
                                .setAccept_department(iOuService.getOuNameByUserGuid(auditProject.getAcceptuserguid()));
                        evaluateProject.setTask_name(auditProject.getProjectname());
                        evaluateProject.setApply_object(auditProject.getApplyername());
                        evaluateProject.setApply_id(auditProject.getCertnum());
                        evaluateProject.setAccept_date(auditProject.getAcceptuserdate());
                        evaluateProject.setHandle_date(auditProject.getBanjiedate());
                        evaluateProject.setLink_user(auditProject.getContactperson());
                        evaluateProject.setLink_phone(auditProject.getContactmobile());
                        // 是否发送短信：1
                        evaluateProject.setIs_send(ZwfwConstant.CONSTANT_INT_ONE);
                        // 是否评价：0：未评价
                        evaluateProject.setIs_evaluate(ZwfwConstant.CONSTANT_INT_ZERO);
                        // 评价来源：1：导入办件，2：审批办件
                        evaluateProject.setProject_source(ZwfwConstant.CONSTANT_INT_TWO);

                        // 编号
                        evaluateProject.setProject_no(auditProject.getFlowsn());
                        evaluateProject.setProject_guid(auditProject.getRowguid());

                        // 5.判断办结时间为同一天且同一个手机号的评价办件信息是否存在
                        boolean isExistBj = iEvaluateProjectService.isExistPhoneAndHandleDate(
                                evaluateProject.getLink_phone(), EpointDateUtil.convertDate2String(
                                        evaluateProject.getHandle_date(), EpointDateUtil.DATE_FORMAT));
                        // 若存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,不发短信
                        if (isExistBj) {
                            iEvaluateProjectService.insert(evaluateProject);
                        }
                        else {
                            // 若不存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,并发短信
                            iEvaluateProjectService.insert(evaluateProject);
                            // 获取系统参数：评价短信发送模板
                            String content = iConfigService.getFrameConfigValue("EAVL_MSG_CONTENT");

                            // 发短信
                            String contentText = content.replaceAll("#=TASK_NAME=#", evaluateProject.getTask_name());
                            iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), contentText, nowDate,
                                    0, nowDate, evaluateProject.getLink_phone(), "-", evaluateProject.getLink_user(),
                                    UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                    evaluateProject.getLink_phone(), UserSession.getInstance().getOuGuid(), "", true,
                                    "370883");
                        }
                        EpointFrameDsManager.commit();
                    }
                }
                // *************结束**************

                message = "finish";
                break;
            default:
                handleProjectService.saveProject(auditProject);
                // 添加工作流操作日志
                EpointFrameDsManager.begin(null);
                // String pivguid = String.valueOf(jsonobject.get("pviguid"));
                // IWFInstanceAPI9 wf9instance =
                // ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
                // ProcessVersionInstance pvi =
                // wf9instance.getProcessVersionInstance(pivguid);
                WorkflowWorkItem workflowWorkItem = wf9instance.getWorkItem(pvi,
                        String.valueOf(jsonobject.get("workitemguid")));
                handleProjectService.saveOperateLog(auditProject, userGuid, UserSession.getInstance().getDisplayName(),
                        workflowWorkItem.getActivityName() + " ", workflowWorkItem.getOpinion());

                EpointFrameDsManager.commit();

                // wfa.getActivityInstanceName()
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    // MQ 环节信息
                    msg = "handleProcess:" + auditProject.getRowguid() + ";" + String.valueOf(jsonobject.get("pviguid"))
                            + ";" + String.valueOf(jsonobject.get("workitemguid")) + ";"
                            + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode();
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 环节信息
                    msg = auditProject.getRowguid() + "." + String.valueOf(jsonobject.get("pviguid")) + "."
                            + String.valueOf(jsonobject.get("workitemguid")) + "."
                            + String.valueOf(jsonobject.get("operationguid"));
                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                            + ZwfwUserSession.getInstance().getAreaCode() + ".jnprocess." + auditProject.getTask_id());
                    // MQ 江苏省标
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                        msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                + String.valueOf(jsonobject.get("pviguid")) + ";"
                                + String.valueOf(jsonobject.get("workitemguid")) + ";"
                                + String.valueOf(jsonobject.get("operationguid")) + ";" + auditProject.getAreacode()
                                + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                    }
                }
                break;
        }

        IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        // 如果存在待办事宜，则删除待办
        List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userGuid, null, null, "",
                IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
        if (messagesCenterList != null && messagesCenterList.size() > 0) {
            for (MessagesCenter messagescenter : messagesCenterList) {
                messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
            }
        }

        returnjsonobject.put("operationtype", result.getOperation().getOperationType());
        // 这个是给自动跳转自己的待办用的

        if (redirect && StringUtil.isBlank(message)) {
            message = "redirect";
        }
        returnjsonobject.put("handleUrl", handleUrl);

        returnjsonobject.put("message", message);

        return returnjsonobject.toString();

    }

    public String operate(String data, String userGuid, String rowGuid) {
        data = EncodeUtil.decode(data);
        JSONObject jsonobject = JSON.parseObject(data);
        jsonobject.put("userguid", userGuid);
        JSONObject returnjsonobject = new JSONObject();
        IWFEngineAPI9 wfengine = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
        IWFInstanceAPI9 wf9instance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        JSONObject contexts = new JSONObject();
        returnjsonobject.put("message", "");
        // WorkflowParameter9 param = new WorkflowParameter9();
        // contexts.put("TaskGuid", auditProject.getTaskguid());
        contexts.put("guid", rowGuid);
        WorkflowResult9 result;
        try {
            result = wfengine.operate(jsonobject.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            ProcessVersionInstance pvi = wf9instance
                    .getProcessVersionInstance(String.valueOf(jsonobject.get("pviguid")));
            wf9instance.unlockPvi(pvi);
            if (e.getMessage().contains("EWF-013")) {
                returnjsonobject.put("message", "当前步骤已经处理完成，无需再次处理！");
            }
            else {
                returnjsonobject.put("message", "流程处理出错！");
            }
            return returnjsonobject.toString();
        }
        returnjsonobject.put("operationtype", result.getOperation().getOperationType());
        return returnjsonobject.toString();
    }

    /**
     * 办结
     * 
     * @param workItemGuid
     * @param opinion
     */
    public void finish(String workItemGuid, String opinion, ProcessVersionInstance pvi, String operationGuid) {
        try {
            IWFEngineAPI9 wfengine = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
            WorkflowParameter9 param = new WorkflowParameter9();
            param.setProcessVersionInstanceGuid(pvi.getPvi().getProcessVersionInstanceGuid());
            param.setOperateType(WorkflowKeyNames9.OperationType_TerminatePVI);
            param.setWorkItemGuid(workItemGuid);
            param.setOpinion(opinion);
            param.setOperationGuid(operationGuid);
            wfengine.operate(param.ConvertToJson());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void terminate(String workItemGuid, String opinion, ProcessVersionInstance pvi) {
        IWFEngineAPI9 wfengine = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
        WorkflowParameter9 param = new WorkflowParameter9();
        param.setWorkItemGuid(workItemGuid);
        param.setOperateType(WorkflowKeyNames9.OperationType_TerminatePVI);
        param.setOpinion(opinion);
        wfengine.operate(param.ConvertToJson());
    }

    /**
     * 操作按钮初始化
     * 
     * @param handleControl
     * @param operation
     * @return
     */
    public Map<String, Object> initHandleControl(Map<String, Object> handleControl, String operation) {
        // 针对不同的操作类型，需要初始化的按钮也会有所不同
        switch (operation) {
            case "add":// 中心新增
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                break;
            case "edit":// 中心变更
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                handleControl.put("showSubmit", true);
                break;
            case "inaudit":// 中心审核
                handleControl.put("showSave", true);
                handleControl.put("showPass", true);
                handleControl.put("showNotPass", true);
                break;
            case "windowAdd":// 窗口新增
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                handleControl.put("showReport", true);
                break;
            case "windowCopy":// 窗口复制
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                handleControl.put("showReport", true);
                break;
            case "windowChange":// 窗口变更
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                handleControl.put("showReport", true);
                break;
            case "windowReportEdit":// 窗口上报
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                handleControl.put("showReport", true);
                break;
            case "windowEditNotPassChange":// 未通过修改
                handleControl.put("showNext", true);
                handleControl.put("showPrev", true);
                handleControl.put("showReport", true);
                break;
            case "windowEditNotPass":
                handleControl.put("shownext", true);
                handleControl.put("showprev", true);
                handleControl.put("showReport", true);
                break;
            default:
                break;
        }
        return handleControl;

    }
}

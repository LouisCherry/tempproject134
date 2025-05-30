package com.epoint.composite.auditresource.handletask.impl;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditsp.auditspshareoption.api.IAuditSpShareoption;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.docs.domain.AuditTaskDoc;
import com.epoint.basic.audittask.docs.inter.IAuditTaskDoc;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.faq.inter.IAuditTaskFaq;
import com.epoint.basic.audittask.fee.domain.AuditTaskChargeItem;
import com.epoint.basic.audittask.fee.inter.IAuditTaskChargeItem;
import com.epoint.basic.audittask.map.inter.IAuditTaskMap;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.search.inteligentsearch.restful.sdk.InteligentSearchRestNewSdk;
import com.epoint.search.inteligentsearch.restful.sdk.InteligentSearchRestSdk;
import com.epoint.search.inteligentsearch.sdk.domain.*;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowProcess;
import com.epoint.workflow.service.common.entity.config.WorkflowProcessVersion;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HandleTaskImpl implements IHandleTask {
    private Logger log = LogUtil.getLog(HandleTaskImpl.class);
    private static int accuracy = Integer.parseInt(ConfigUtil.getConfigValue("inteligentSearch", "accuracy"));
    @Override
    public AuditCommonResult<PageData<AuditTask>> getAuditTaskPageDataByWindow(String windwoGuid,
                                                                               Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<>();
        try {
            List<String> rowGuids = this.getUsableTaskGuidsByWindow(windwoGuid).getResult();
            if (conditionMap == null) {
                conditionMap = new HashMap<String, String>(16);
            }
            SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
            if (rowGuids != null) {
                sqlCondition.in("a.rowGuid", "'" + StringUtil.join(rowGuids, "','") + "'");
            }
            sqlCondition.eq("a.IS_Enable", "1");
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            result.setResult(auditTaskService
                    .getAuditTaskPageData(sqlCondition.getMap(), first, pageSize, sortField, sortOrder).getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditTask>> getAuditTaskPageDataXzByWindow(String windwoGuid, String areaCode,
                                                                                 Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<>();
        try {
            List<String> rowGuids = this.getUsableTaskGuidsXzByWindow(windwoGuid, areaCode).getResult();
            if (conditionMap == null) {
                conditionMap = new HashMap<String, String>(16);
            }
            SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
            if (rowGuids != null) {
                sqlCondition.in("a.rowGuid", "'" + StringUtil.join(rowGuids, "','") + "'");
            }
            sqlCondition.eq("a.IS_Enable", "1");
            sqlCondition.eq("a.delegateareacode", areaCode);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            result.setResult(auditTaskService
                    .getXzAuditTaskPageData(sqlCondition.getMap(), first, pageSize, sortField, sortOrder).getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getUsableTaskGuidsByWindow(String windowGuid) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        IAuditOrgaWindow orgawindowService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
        IAuditTask audittaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        try {
            List<AuditOrgaWindowTask> windowTaskList = orgawindowService.getTaskByWindow(windowGuid).getResult();
            List<String> taskguids = new ArrayList<>();
            for (AuditOrgaWindowTask auditWindowTask : windowTaskList) {
                AuditTask auditTask = audittaskService.selectUsableTaskByTaskID(auditWindowTask.getTaskid())
                        .getResult();
                if (auditTask != null) {
                    taskguids.add(auditTask.getRowguid());
                }
            }
            result.setResult(taskguids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getUsableTaskGuidsXzByWindow(String windowGuid, String areaCode) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        IAuditOrgaWindow orgawindowService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
        IAuditTaskDelegate delegateService = ContainerFactory.getContainInfo().getComponent(IAuditTaskDelegate.class);
        try {
            List<AuditOrgaWindowTask> windowTaskList = orgawindowService.getTaskByWindow(windowGuid).getResult();
            List<String> taskguids = new ArrayList<>();
            for (AuditOrgaWindowTask auditWindowTask : windowTaskList) {
                AuditTask auditTask = delegateService.selectUsableTaskByTaskID(auditWindowTask.getTaskid(), areaCode)
                        .getResult();
                if (auditTask != null) {
                    taskguids.add(auditTask.getRowguid());
                }
            }
            result.setResult(taskguids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditTask>> getAuditTaskListByCondition(boolean flag, String itemid,
                                                                              Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<PageData<AuditTask>>();
        IHandleConfig iHandleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        IAuditTask auditTaskBasicService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        SqlConditionUtil conditionsql = new SqlConditionUtil(conditionMap);
        try {
            // 国标新事项分类规则 / 默认事项编码默认都为31位（但不会强制限制），主项结尾为 000, 子项为XXX结尾
            String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
            if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)) {
                // flag为true，取大项
                if (flag) {
                    // 大项
                    // 倒数结尾3位为000
                    conditionsql.rightLike("ITEM_ID", "000");
                } else {
                    // 子项规则为，前几位编码都一致，后三位编码随机
                    conditionsql.nq("ITEM_ID", itemid);
                    String tempid = itemid.substring(0, itemid.length() - 3);
                    conditionsql.leftLike("ITEM_ID", tempid);
                }
            }
            // 旧事项分类规则/ 18/21位事项编码规则
            else {
                // 事项大小项分类
                String taskcategory = iHandleConfig.getFrameConfig("AS_Task_Category", "").getResult();
                switch (taskcategory) {
                    case "0":
                        String strlength = iHandleConfig.getFrameConfig("AS_ITEM_ID_LENGTH", "").getResult();
                        String[] arrlen = strlength.split(";");
                        int sublength = StringUtil.isBlank(arrlen[1]) ? 0 : Integer.parseInt(arrlen[1]);// 子项长度
                        int parentlength = StringUtil.isBlank(arrlen[0]) ? 0 : Integer.parseInt(arrlen[0]);// 父项长度
                        // flag为true，取大项
                        if (flag) {
                            // 大项
                            conditionsql.eq("LENGTH(ITEM_ID)", Integer.toString(parentlength));
                        } else {
                            // 小项
                            conditionsql.eq("LENGTH(ITEM_ID)", Integer.toString(sublength));
                            String temp = itemid.substring(0, parentlength);
                            conditionsql.eq("SUBSTRING(ITEM_ID, 1, " + parentlength + ")", temp);
                        }
                        break;
                    case "1":
                        // 代表通过right_category表区分大小项
                    case "2":
                        // 代表不区分大小项
                    default:
                        // 未设置参数
                        if (!flag) {
                            result.setResult(null);
                            return result;
                        }
                        break;
                }
            }
            if (conditionMap.containsKey("dict_id" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                    + ZwfwConstant.ZWFW_SPLIT + "S")) {
                // 有分类条件，去除外网标志位
                conditionsql.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
                result.setResult(auditTaskBasicService
                        .getAuditTaskPageDataWithDict(conditionsql.getMap(), first, pageSize, sortField, sortOrder)
                        .getResult());
            } else {
                // 没有分类条件
                result.setResult(auditTaskBasicService
                        .getAuditTaskPageData(conditionsql.getMap(), first, pageSize, sortField, sortOrder)
                        .getResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("serial")
    @Override
    public AuditCommonResult<String> addOrUpdateIndex(String categoryNum, String taskID, boolean isDel) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
        if (StringUtil.isNotBlank(restIpConfig)) {
            List<String> listcategorynums = new ArrayList<String>();
            String[] categorynumsarray = null;
            if (StringUtil.isNotBlank(categoryNum)) {
                categorynumsarray = categoryNum.split(";");
            }
            if (StringUtil.isNotBlank(categorynumsarray)) {
                for (String s : categorynumsarray) {
                    listcategorynums.add(s);
                }
            }

            try {
                if (isDel) {
                    InteligentSearchRestNewSdk fulltextSdk = new InteligentSearchRestNewSdk();
                    fulltextSdk.deleteIndexByIds(restIpConfig, listcategorynums, new ArrayList<String>() {
                        {
                            add(taskID);
                        }
                    }, "1", null);
                } else {
                    List<IndexFieldFormat> list = new ArrayList<>();
                    IndexFieldFormat f1 = new IndexFieldFormat();
                    f1.setField("id");
                    f1.setPluginBak("");
                    f1.setPluginName("");
                    list.add(f1);

                    IndexFieldFormat f2 = new IndexFieldFormat();
                    f2.setField("taskname");
                    f2.setPluginBak("");
                    f2.setPluginName("");
                    list.add(f2);

                    IndexFieldFormat f3 = new IndexFieldFormat();
                    f3.setField("content");
                    f3.setPluginBak("");
                    f3.setPluginName("ITextFilterOperator");
                    list.add(f3);

                    IndexFieldFormat f4 = new IndexFieldFormat();
                    f4.setField("infodate");
                    f4.setPluginBak("");
                    f4.setPluginName("");
                    list.add(f4);

                    IndexFieldFormat f5 = new IndexFieldFormat();
                    f5.setField("linkurl");
                    f5.setPluginBak("");
                    f5.setPluginName("");
                    list.add(f5);

                    IndexFieldFormat f6 = new IndexFieldFormat();
                    f5.setField("taskdic");
                    f5.setPluginBak("");
                    f5.setPluginName("");
                    list.add(f6);

                    IndexFieldFormat f7 = new IndexFieldFormat();
                    f7.setField("ouguid");
                    f7.setPluginBak("");
                    f7.setPluginName("");
                    list.add(f7);

                    IndexFieldFormat f8 = new IndexFieldFormat();
                    f8.setField("taskfaq");
                    f8.setPluginBak("");
                    f8.setPluginName("ITextFilterOperator");
                    list.add(f8);

                    IndexFieldFormat f9 = new IndexFieldFormat();
                    f9.setField("ouname");
                    f9.setPluginBak("");
                    f9.setPluginName("");
                    list.add(f9);

                    IndexFieldFormat f10 = new IndexFieldFormat();
                    f10.setField("taskmaterial");
                    f10.setPluginBak("");
                    f10.setPluginName("");
                    list.add(f10);

                    IndexFieldFormat f11 = new IndexFieldFormat();
                    f11.setField("applytype");
                    f11.setPluginBak("");
                    f11.setPluginName("");
                    list.add(f11);

                    IndexFieldFormat f12 = new IndexFieldFormat();
                    f12.setField("areacode");
                    f12.setPluginBak("");
                    f12.setPluginName("");
                    list.add(f12);

                    // 是否在线办理
                    IndexFieldFormat f13 = new IndexFieldFormat();
                    f13.setField("webapplytype");
                    f13.setPluginBak("");
                    f13.setPluginName("");
                    list.add(f13);

                    // 事项主键
                    IndexFieldFormat f14 = new IndexFieldFormat();
                    f14.setField("taskguid");
                    f14.setPluginBak("");
                    f14.setPluginName("");
                    list.add(f14);

                    // 事项编码
                    IndexFieldFormat f15 = new IndexFieldFormat();
                    f15.setField("itemid");
                    f15.setPluginBak("");
                    f15.setPluginName("");
                    list.add(f15);

                    // 权力类型
                    IndexFieldFormat f16 = new IndexFieldFormat();
                    f16.setField("qlkind");
                    f16.setPluginBak("");
                    f16.setPluginName("");
                    list.add(f16);

                    // 行使层级
                    IndexFieldFormat f17 = new IndexFieldFormat();
                    f17.setField("exerciseclass");
                    f17.setPluginBak("");
                    f17.setPluginName("");
                    list.add(f17);

                    // 办理类型
                    IndexFieldFormat f18 = new IndexFieldFormat();
                    f18.setField("type");
                    f18.setPluginBak("");
                    f18.setPluginName("");
                    list.add(f18);

                    // 法定期限
                    IndexFieldFormat f19 = new IndexFieldFormat();
                    f19.setField("anticipateday");
                    f19.setPluginBak("");
                    f19.setPluginName("");
                    list.add(f19);

                    // 承诺期限
                    IndexFieldFormat f20 = new IndexFieldFormat();
                    f20.setField("promiseday");
                    f20.setPluginBak("");
                    f20.setPluginName("");
                    list.add(f20);

                    // 咨询电话
                    IndexFieldFormat f21 = new IndexFieldFormat();
                    f21.setField("linktel");
                    f21.setPluginBak("");
                    f21.setPluginName("");
                    list.add(f21);

                    // 监督投诉电话
                    IndexFieldFormat f22 = new IndexFieldFormat();
                    f22.setField("supervisetel");
                    f22.setPluginBak("");
                    f22.setPluginName("");
                    list.add(f22);

                    // 办理地点、时间
                    IndexFieldFormat f23 = new IndexFieldFormat();
                    f23.setField("addressandtime");
                    f23.setPluginBak("");
                    f23.setPluginName("");
                    list.add(f23);

                    // 大小项标识
                    IndexFieldFormat f24 = new IndexFieldFormat();
                    f24.setField("isfathertask");
                    f24.setPluginBak("");
                    f24.setPluginName("");
                    list.add(f24);

                    // 如果是小项，他的大项编码
                    IndexFieldFormat f25 = new IndexFieldFormat();
                    f25.setField("fatheritem");
                    f25.setPluginBak("");
                    f25.setPluginName("");
                    list.add(f25);

                    // 办理对象
                    IndexFieldFormat f26 = new IndexFieldFormat();
                    f26.setField("managementobj");
                    f26.setPluginBak("");
                    f26.setPluginName("");
                    list.add(f26);

                    IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
                    IAuditTaskExtension audittaskextensionservice = ContainerFactory.getContainInfo()
                            .getComponent(IAuditTaskExtension.class);
                    ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo()
                            .getComponent(ICodeItemsService.class);
                    IAuditTaskMap auditTaskMapService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditTaskMap.class);
                    IAuditTaskFaq auditTaskFaqService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditTaskFaq.class);
                    IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditTaskMaterial.class);
                    IHandleConfig iHandleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
                    AuditTask auditTask = auditTaskService.getUseTaskAndExtByTaskid(taskID).getResult();
                    if (auditTask != null) {
                        AuditTaskExtension audittaskextension = audittaskextensionservice
                                .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
                        Map<String, Object> data = new HashMap<String, Object>(16);
                        data.put("id", auditTask.getTask_id());
                        data.put("categorynum", categoryNum);// 一定要传递
                        data.put("userguid", "");// 一定要传递
                        data.put("attachname", "");// 一定要传递
                        data.put("taskguid", auditTask.getRowguid());
                        data.put("itemid", auditTask.getItem_id());
                        data.put("taskname", auditTask.getTaskname());
                        data.put("content", auditTask.getBy_law() == null ? "" : auditTask.getBy_law());
                        data.put("infodate", new Date());
                        String onlinehandle = "0";// 默认不能在线办理
                        if (Integer.parseInt(ZwfwConstant.WEB_APPLY_TYPE_YS) == audittaskextension.getWebapplytype()
                                || Integer.parseInt(ZwfwConstant.WEB_APPLY_TYPE_SL) == audittaskextension
                                .getWebapplytype()) {
                            onlinehandle = "1";
                        }
                        data.put("webapplytype", onlinehandle);// 在线申报
                        data.put("qlkind", iCodeItemsService.getItemTextByCodeName("审批类别",
                                String.valueOf(auditTask.getShenpilb())));// 权力类型
                        String uselevel = audittaskextension.getUse_level();
                        String exerciseclass = "";
                        if (StringUtil.isNotBlank(uselevel)) {
                            String[] userlevelarr = uselevel.split(";");
                            for (int i = 0; i < userlevelarr.length; i++) {
                                exerciseclass = iCodeItemsService.getItemTextByCodeName("行使层级", userlevelarr[i]) + ";";
                            }
                            exerciseclass = exerciseclass.substring(0, exerciseclass.length() - 1);
                        }
                        data.put("exerciseclass", exerciseclass);// 行使层级

                        data.put("type",
                                iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办理类型
                        data.put("anticipateday", auditTask.getAnticipate_day());// 法定期限
                        data.put("promiseday", auditTask.getPromise_day());// 承诺期限
                        data.put("linktel", auditTask.getLink_tel());// 咨询电话
                        data.put("supervisetel", auditTask.getSupervise_tel());// 投诉电话
                        data.put("addressandtime", auditTask.getTransact_addr() + auditTask.getTransact_time());// 办公时间+地点
                        String applyertype = auditTask.getApplyertype();
                        String managementobj = "";
                        if (StringUtil.isNotBlank(applyertype)) {
                            String[] applyertypearr = applyertype.split(";");
                            for (int i = 0; i < applyertypearr.length; i++) {
                                managementobj = iCodeItemsService.getItemTextByCodeName("申请人类型", applyertypearr[i])
                                        + ";";
                            }
                            managementobj = managementobj.substring(0, managementobj.length() - 1);
                        }
                        data.put("managementobj", managementobj); // 办理对象
                        String strlength = iHandleConfig.getFrameConfig("AS_ITEM_ID_LENGTH", "").getResult();
                        String[] arrlen = strlength.split(";");
                        int parentlength = StringUtil.isBlank(arrlen[0]) ? 0 : Integer.parseInt(arrlen[0]);// 父项长度
                        if (parentlength == auditTask.getItem_id().length()) {
                            // 父项
                            data.put("isfathertask", "1");
                            data.put("fatheritem", "");
                        } else {
                            // 子项
                            data.put("isfathertask", "0");
                            data.put("fatheritem", auditTask.getItem_id());
                        }
                        String[] dics;
                        List<Record> maps = auditTaskMapService.getDictConnectMap(auditTask.getTask_id()).getResult();
                        if (maps != null && maps.size() > 0) {
                            dics = new String[maps.size()];
                            for (int i = 0; i < maps.size(); i++) {
                                dics[i] = maps.get(i).get("dict_id");
                            }
                        } else {
                            dics = new String[0];
                        }
                        data.put("taskdic", dics);
                        if (!"".equals(auditTask.getApplyertype())) {
                            String[] applytypes = auditTask.getApplyertype().split(",");
                            data.put("applytype", applytypes);
                        } else {
                            data.put("applytype", "");
                        }
                        data.put("ouguid", auditTask.getOuguid());
                        data.put("ouname", auditTask.getOuname());
                        data.put("linkurl", "");

                        List<String[]> listfaq = new ArrayList<String[]>();
                        List<AuditTaskFaq> auditTaskFaqs = auditTaskFaqService.selectAuditTaskFaqByTaskId(taskID)
                                .getResult();
                        if (auditTaskFaqs != null && auditTaskFaqs.size() > 0) {
                            for (int i = 0; i < auditTaskFaqs.size(); i++) {
                                String[] faqs = new String[2];
                                faqs[0] = auditTaskFaqs.get(i).getQuestion();
                                faqs[1] = auditTaskFaqs.get(i).getAnswer();
                                listfaq.add(faqs);
                            }
                        }
                        data.put("taskfaq", listfaq);
                        data.put("areacode", auditTask.getAreacode());

                        List<String> listmaterial = new ArrayList<>();
                        List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                                .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), true).getResult();
                        if (auditTaskMaterials != null && auditTaskMaterials.size() > 0) {
                            for (int i = 0; i < auditTaskMaterials.size(); i++) {
                                listmaterial.add(auditTaskMaterials.get(i).getMaterialname());
                            }
                        }
                        data.put("taskmaterial", listmaterial);

                        listData.add(data);
                        InteligentSearchRestNewSdk fulltextSdk = new InteligentSearchRestNewSdk();
                        String msg = fulltextSdk.addOrUpdateIndex(restIpConfig, categoryNum, listData, list, "1", null);
                        result.setResult(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> AIgetData(String restIpConfig, String categoryNum, String searchWords,
                                               List<SearchCondition> cs, String searchWordsrangs) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestNewSdk fulltextSdk = new InteligentSearchRestNewSdk();
            String msg = fulltextSdk.getFullTextDataNew(restIpConfig, "", "0", "100", "", "", searchWords, searchWords,
                    "", searchWordsrangs, "", categoryNum, null, null, 0, "0", cs, null, null, null, null, null, 0, "",
                    true, false, "", null, null, null, null, "1", "");
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String escapeQueryChars(String keyWord) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyWord.length(); i++) {
            char c = keyWord.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '!' || c == ':' || c == '^' || c == '\"' || c == '~' || c == '*' || c == '"'
                    || c == '?' || c == '|' || c == '&' || c == ';' || Character.isWhitespace(c) || c == '/'|| c == '['||
                    c == ']'|| c == '（'|| c == '）'|| c == '('|| c == ')'|| c == '【'|| c == '】') {
                continue;
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    @Override
    public AuditCommonResult<String> getData(String restIpConfig, String categoryNum, String searchWords,
                                             List<SearchCondition> cs, List<SearchUnionCondition> ucs, String searchWordsrangs, String currentPage,
                                             String pageSize) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestNewSdk fulltextSdk = new InteligentSearchRestNewSdk();

            System.out.println("restIpConfig===" + restIpConfig);
            System.out.println("currentPage===" + currentPage);
            System.out.println("pageSize===" + pageSize);
            System.out.println("searchWords===" + searchWords);
            System.out.println("searchWordsrangs===" + searchWordsrangs);
            System.out.println("categoryNum===" + categoryNum);
            searchWords = escapeQueryChars(searchWords);
            System.out.println("newsearchWords===" + searchWords);
            String msg = fulltextSdk.getFullTextDataNew(restIpConfig, "", currentPage, pageSize, "", "", searchWords,
                    searchWords, "", searchWordsrangs, "", categoryNum, null, null, 0, "0", cs, null, null, null, null,
                    ucs, accuracy, "", true, false, "", null, null, null, null, "1", "");
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getData(String categoryNum, String searchWords, List<SearchCondition> cs,
                                             String searchWordsrangs, String currentPage, String pageSize) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestSdk fulltextSdk = new InteligentSearchRestSdk();
            String msg = fulltextSdk.getFullTextData("", currentPage, pageSize, null, null, searchWords, "", "",
                    searchWordsrangs, categoryNum, null, "", 10000, "0", cs, null, searchWordsrangs, "", null, 0, "0");
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getDataByExtraCondition(String restIpConfig, String categoryNum,
                                                             String searchWords, List<SearchCondition> cs, String searchWordsrangs, String currentPage,
                                                             String pageSize) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestNewSdk fulltextSdk = new InteligentSearchRestNewSdk();
            String msg = fulltextSdk.getFullTextDataNew(restIpConfig, "", currentPage, pageSize, "", "", searchWords,
                    searchWords, "", searchWordsrangs, "", categoryNum, null, null, 0, "0", cs, null, null, null, null,
                    null, 0, "", true, false, "", null, null, null, null, "1", "");
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditOrgaArea>> getTemplateCityAreaList(String rowGuid) {
        AuditCommonResult<List<AuditOrgaArea>> result = new AuditCommonResult<>();
        try {
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            Map<String, String> conditionMap = new HashMap<>(16);
            conditionMap.put("istemplate=", "1");
            conditionMap.put("is_editafterimport=", "1");
            if (StringUtil.isNotBlank(rowGuid)) {
                conditionMap.put("rowGuid!=", rowGuid);
            }
            List<AuditTask> auditTasks = auditTaskService.getAllTask(conditionMap).getResult();
            String tasks = "";
            if (auditTasks != null && auditTasks.size() > 0) {
                for (AuditTask auditTask : auditTasks) {
                    tasks += "'" + auditTask.getAreacode() + "'" + ",";
                }
                tasks = tasks.substring(0, tasks.length() - 1);
            }
            IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
            conditionMap.clear();
            if (StringUtil.isNotBlank(tasks)) {
                conditionMap.put("xiaqucode not in", tasks);
            }
            result.setResult(auditOrgaAreaService.selectAuditAreaList(conditionMap).getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> createCategory(InteligentSearchCategory category, String paraentCategoryNum,
                                                    List<InteligentSearchColumnConfig> config) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchCategory searchcategory = new InteligentSearchCategory();
            searchcategory.setCategoryName("政务大厅事项索引");// 分类名称
            searchcategory.setIsOpen(1);// 是否公开 公开 1
            searchcategory.setCategoryDescribe("政务大厅展示事项全文索引");

            List<InteligentSearchColumnConfig> list = new ArrayList<InteligentSearchColumnConfig>();
            InteligentSearchColumnConfig c1 = new InteligentSearchColumnConfig();
            c1.setField("id");// 字段名称
            c1.setName("主键");// 字段中文名称
            c1.setType(""); // 字段类型 Id 不需要设置字段类别，其余字段必须设置字段类型
            list.add(c1);

            InteligentSearchColumnConfig c2 = new InteligentSearchColumnConfig();
            c2.setField("taskname");
            c2.setName("事项名称");
            c2.setType("_ik");
            list.add(c2);

            InteligentSearchColumnConfig c3 = new InteligentSearchColumnConfig();
            c3.setField("content");
            c3.setName("法律依据");
            c3.setType("_ikh");
            list.add(c3);

            InteligentSearchColumnConfig c4 = new InteligentSearchColumnConfig();
            c4.setField("infodate");
            c4.setName("创建日期");
            c4.setType("_tdt");
            list.add(c4);

            InteligentSearchColumnConfig c5 = new InteligentSearchColumnConfig();
            c5.setField("linkurl");
            c5.setName("链接地址");
            c5.setType("_sh");
            list.add(c5);

            InteligentSearchColumnConfig c6 = new InteligentSearchColumnConfig();
            c6.setField("taskdic");
            c6.setName("主题分类标识");
            c6.setType("_ssh");
            list.add(c6);

            InteligentSearchColumnConfig c7 = new InteligentSearchColumnConfig();
            c7.setField("ouguid");
            c7.setName("部门guid");
            c7.setType("_sh");
            list.add(c7);

            InteligentSearchColumnConfig c8 = new InteligentSearchColumnConfig();
            c8.setField("taskfaq");
            c8.setName("常见问题");
            c8.setType("_iksh");
            list.add(c8);

            InteligentSearchColumnConfig c9 = new InteligentSearchColumnConfig();
            c9.setField("ouname");
            c9.setName("部门名称");
            c9.setType("_ik");
            list.add(c9);

            InteligentSearchColumnConfig c10 = new InteligentSearchColumnConfig();
            c10.setField("taskmaterial");
            c10.setName("事项材料");
            c10.setType("_iksh");
            list.add(c10);

            InteligentSearchColumnConfig c11 = new InteligentSearchColumnConfig();
            c11.setField("applytype");
            c11.setName("事项类型");
            c11.setType("_ssh");
            list.add(c11);

            InteligentSearchColumnConfig c12 = new InteligentSearchColumnConfig();
            c12.setField("areacode");
            c12.setName("区域标识");
            c12.setType("_sh");
            list.add(c12);

            InteligentSearchColumnConfig c13 = new InteligentSearchColumnConfig();
            c13.setField("webapplytype");
            c13.setName("是否在线办理");
            c13.setType("_sh");
            list.add(c13);

            InteligentSearchColumnConfig c14 = new InteligentSearchColumnConfig();
            c14.setField("taskguid");
            c14.setName("事项主键");
            c14.setType("_s");
            list.add(c14);

            InteligentSearchColumnConfig c15 = new InteligentSearchColumnConfig();
            c15.setField("itemid");
            c15.setName("事项编码");
            c15.setType("_s");
            list.add(c15);

            InteligentSearchRestSdk fulltextSdk = new InteligentSearchRestSdk();
            String msg = fulltextSdk.createCategory(searchcategory, "", list);
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteAllQwsy() {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        String CategoryNums = ConfigUtil.getConfigValue("inteligentSearchCategoryNums");
        List<String> listcategorynums = new ArrayList<String>();
        String[] categorynumsarray = null;
        if (StringUtil.isNotBlank(CategoryNums)) {
            categorynumsarray = CategoryNums.split(";");
        }
        if (StringUtil.isNotBlank(categorynumsarray)) {
            for (String s : categorynumsarray) {
                listcategorynums.add(s);
            }
        }
        try {
            InteligentSearchRestSdk sdk = new InteligentSearchRestSdk();
            result.setResult(sdk.deleteAll(listcategorynums));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteQwsyById(String ids) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestSdk sdk = new InteligentSearchRestSdk();
            List<String> list = new ArrayList<>();
            list.add(ids);

            String CategoryNums = ConfigUtil.getConfigValue("inteligentSearchCategoryNums");
            List<String> listcategorynums = new ArrayList<String>();
            String[] categorynumsarray = null;
            if (StringUtil.isNotBlank(CategoryNums)) {
                categorynumsarray = CategoryNums.split(";");
            }
            if (StringUtil.isNotBlank(categorynumsarray)) {
                for (String s : categorynumsarray) {
                    listcategorynums.add(s);
                }
            }
            result.setResult(sdk.deleteIndexByIds(listcategorynums, list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteQwsyByCondition(String categoryNum, Map<String, Object> condition) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestSdk sdk = new InteligentSearchRestSdk();
            result.setResult(sdk.deleteByCondition(categoryNum, condition));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getQwsyHotWordsBycategory(String restIpConfig, String categoryNum, String count) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            InteligentSearchRestNewSdk fulltextSdk = new InteligentSearchRestNewSdk();
            result.setResult(
                    fulltextSdk.getHotWords(restIpConfig, "", Integer.parseInt(count), categoryNum, "1", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> initNewTask(String newtaskguid, String taskId, String addusername,
                                               String adduserguid, boolean shouldnewtaskid, AuditTask audittask) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        IWorkflowProcessService workflowProcessService9 = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowProcessService.class);
        IAuditTaskRiskpoint auditTaskRiskpointService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskRiskpoint.class);
        IWorkflowActivityService workflowActivityService9 = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowActivityService.class);
        IWorkflowProcessVersionService workflowProcessVersionService9 = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowProcessVersionService.class);
        HandleTaskService taskService = new HandleTaskService();
        try {
            String newprocessguid;// 流程标识
            String newconvertguid = UUID.randomUUID().toString();// 用于转换
            if (audittask != null) {
                String nameexpression = audittask.getTaskname() + "([#=ApplyerName#])";
                WorkflowProcess wp = workflowProcessService9.getByProcessGuid(audittask.getProcessguid());
                // 流程不存在则生成
                if (wp == null || wp.isEmpty()) {
                    wp = new WorkflowProcess();
                    wp.setProcessName(audittask.getTaskname());
                    newprocessguid = UUID.randomUUID().toString();
                    wp.setProcessGuid(newprocessguid);
                    workflowProcessService9.addWorkflowProcess(wp, adduserguid, "");
                    List<AuditTaskRiskpoint> datalist = auditTaskRiskpointService
                            .getRiskPointListByTaksGuid(audittask.getRowguid(), false).getResult();
                    if (datalist != null && datalist.size() > 0) {
                        try {
                            for (AuditTaskRiskpoint auditTaskRiskpoint : datalist) {
                                workflowActivityService9.deleteActivityCompletely(auditTaskRiskpoint.getActivityguid());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    newprocessguid = workflowProcessService9.copyOneWorkFlow(wp);
                    WorkflowProcess wfp = workflowProcessService9.getByProcessGuid(newprocessguid);
                    wfp.setProcessName(audittask.getTaskname());
                    workflowProcessService9.updateWorkflowProcess(wfp);
                }

                // 1、流程信息复制
                // WorkflowProcessVersion wpv =
                // workflowProcessVersionService9.getPv(newprocessguid);
                WorkflowProcessVersion wpv = workflowProcessVersionService9.selectByProcessGuid(newprocessguid).get(0);

                // 2、事项复制，复制的事项状态为-1，历史及版本信息为空。
                taskService.copyNewVersionTask(taskId, newtaskguid, newprocessguid, wpv.getProcessVersionGuid(),
                        addusername, adduserguid, newconvertguid, shouldnewtaskid, audittask.getTaskoutimgguid());
                // 3、刷一遍岗位的activityguid
                List<WorkflowActivity> workflowActivities = workflowActivityService9
                        .selectAllByProcessVersionGuid(wpv.getProcessVersionGuid(), " and ACTIVITYTYPE='30'");
                for (int i = 0; i < workflowActivities.size(); i++) {
                    auditTaskRiskpointService.updateAuditTaskRiskpointActivityGuid(taskService
                                    .convertGuid(workflowActivities.get(i).getCopyFromActivityGuid(), newconvertguid),
                            workflowActivities.get(i).getActivityGuid(), newtaskguid);
                }
                // 4、设置流程为可用版本
                wpv.setStatus(10);
                wpv.setIsLogMisTableValues(20);
                wpv.setProcessVersionName(audittask.getTaskname());
                wpv.setInstanceNameExpression(nameexpression);
                wpv.setWorkItemNameExpression(nameexpression);
                workflowProcessVersionService9.updateWorkflowProcessVersion(wpv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> submitTask(Boolean flag, String taskGuid, AuditTask auditTask,
                                                AuditTaskExtension auditTaskExtension, String addusername, String adduserguid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditTaskElementService iaudittaskelementservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskElementService.class);
        IAuditTaskOptionService iaudittaskoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskOptionService.class);
        IAuditTaskExtension auditTaskExtensionImpl = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditTaskResult auditResultService = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        IAuditTaskDoc auditTaskDocService = ContainerFactory.getContainInfo().getComponent(IAuditTaskDoc.class);
        IAuditTaskRiskpoint auditTaskRiskpointService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskRiskpoint.class);
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditTaskChargeItem auditTaskChargeItemService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskChargeItem.class);
        IAuditTaskCase iaudittaskcase = ContainerFactory.getContainInfo().getComponent(IAuditTaskCase.class);
        HandleTaskService taskService = new HandleTaskService();
        try {
            String msg = "";
            String copyTaskGuid = auditTask.getRowguid();
            EpointFrameDsManager.begin(null);
            auditTaskExtension.setTaskadduserguid(adduserguid);
            auditTaskExtension.setTaskadduserdisplayname(addusername);
            // 保存情形数据
            String taskid = auditTask.getTask_id();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            if (!ZwfwConstant.TASKAUDIT_STATUS_DQR.equals(auditTask.getIs_editafterimport().toString())) {
                sqlc.eq("taskid", taskid);
                sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                List<AuditTaskElement> liste = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();
                for (AuditTaskElement auditTaskElement : liste) {
                    // 删除要素
                    iaudittaskelementservice.deleteByGuid(auditTaskElement.getRowguid());
                    // 删除情形
                    iaudittaskoptionservice.deleteByOneField("elementguid", auditTaskElement.getRowguid());
                }
                // 刪除选项名称为空的选项
                sqlc.clear();
                sqlc.eq("taskid", taskid);
                sqlc.isBlank("optionname");
                List<AuditTaskOption> listoption = iaudittaskoptionservice.findListByCondition(sqlc.getMap())
                        .getResult();
                for (AuditTaskOption auditTaskOption : listoption) {
                    iaudittaskoptionservice.deleteByGuid(auditTaskOption.getRowguid());
                }
                // 修改草稿为正式事项
                sqlc.clear();
                sqlc.eq("taskid", taskid);
                sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
                liste = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();
                for (AuditTaskElement auditTaskElement : liste) {
                    // 删除没有名称的数据
                    if (StringUtil.isBlank(auditTaskElement.getElementname())) {
                        // 删除要素
                        iaudittaskelementservice.deleteByGuid(auditTaskElement.getRowguid());
                        // 删除情形
                        iaudittaskoptionservice.deleteByOneField("elementguid", auditTaskElement.getRowguid());

                    } else {
                        iaudittaskelementservice.updateByField("draft", ZwfwConstant.CONSTANT_STR_ZERO,
                                auditTaskElement.getRowguid());
                    }
                }
            }
            updateShareOption(taskid);
            // 删除历史常用情形

            if (flag == true) {
                sqlc.clear();
                sqlc.eq("taskguid", taskGuid);
                List<AuditTaskCase> result2 = iaudittaskcase.selectCaseListByCondition(sqlc.getMap()).getResult();
                for (AuditTaskCase auditTaskCase : result2) {
                    iaudittaskcase.deleteAuditTaskCase(auditTaskCase);
                }
                // 事项名称可能更改
                // 通过processguid更新workflow_processversion表中的instancenameexpression,workitemnameexpressio
                String taskname = auditTask.getTaskname();
                String processguid = auditTask.getProcessguid();
                String instancenameexpression = taskname + "([#=ApplyerName#])";
                String workitemnameexpression = taskname + "([#=ApplyerName#])";
                // 更新工作流版本个性化名字
                taskService.updateWPVByProcessGuid(taskname, instancenameexpression, workitemnameexpression,
                        processguid);
                // 先保存数据
                auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
                EpointFrameDsManager.commit();
                auditTaskBasicImpl.passAuditTask(flag, copyTaskGuid, auditTask.getTask_id(), auditTask,
                        auditTaskExtension);
                auditTaskBasicImpl.updateTaskCache(taskGuid, auditTask.getRowguid());
                msg = "生成新版本成功";
            } else {
                sqlc.clear();
                sqlc.eq("taskguid", copyTaskGuid);
                List<AuditTaskCase> result2 = iaudittaskcase.selectCaseListByCondition(sqlc.getMap()).getResult();
                for (AuditTaskCase auditTaskCase : result2) {
                    iaudittaskcase.deleteAuditTaskCase(auditTaskCase);
                }
                Map<String, String> conditionMap = new HashMap<String, String>(16);
                if (StringUtil.isNotBlank(copyTaskGuid)) {
                    conditionMap.put("taskguid=", copyTaskGuid);
                }
                // 申报材料的tab
                List<AuditTaskMaterial> listMaterial = auditTaskMaterialService
                        .selectTaskMaterialListByTaskGuid(copyTaskGuid, false).getResult();

                // 情形
                taskService.notBuildVersionCase(taskGuid, copyTaskGuid, listMaterial, addusername);

                taskService.notBuildVersion(AuditTaskMaterial.class, taskGuid, copyTaskGuid, listMaterial, addusername);
                // 岗位的tab
                List<AuditTaskRiskpoint> listRiskpoint = auditTaskRiskpointService
                        .getRiskPointListByTaksGuid(copyTaskGuid, false).getResult();
                taskService.notBuildVersion(AuditTaskRiskpoint.class, taskGuid, copyTaskGuid, listRiskpoint,
                        addusername);
                // 文书的tab
                List<AuditTaskDoc> listDoc = auditTaskDocService.selectAuditTaskDocByTaskGuid(copyTaskGuid, false)
                        .getResult();
                taskService.notBuildVersion(AuditTaskDoc.class, taskGuid, copyTaskGuid, listDoc, addusername);
                // 收费项目的tab
                List<AuditTaskChargeItem> listChargeitem = auditTaskChargeItemService
                        .selectAuditTaskChargeItemByTaskGuid(copyTaskGuid, false).getResult();
                taskService.notBuildVersion(AuditTaskChargeItem.class, taskGuid, copyTaskGuid, listChargeitem,
                        addusername);

                // 如果key为审批结果tab
                AuditTaskResult auditResult = auditResultService.getAuditResultByTaskGuid(copyTaskGuid, false)
                        .getResult();
                // 更新结果数据
                taskService.notBuildVersionByResult(taskGuid, auditResult, addusername);
                auditTask.setOperateusername(addusername);
                taskService.notBuildNewVersionByBasicExt(taskGuid, auditTask, auditTaskExtension, addusername);
                // 删除复制出来的事项
                taskService.deleteAllTaskByTaskGuidWithOutWorkflow(copyTaskGuid);
                msg = "修改成功";
            }
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> submitNewTask(AuditTask auditTask, AuditTaskExtension auditTaskExtension,
                                                   String operation, String centerGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        IAuditTaskExtension auditTaskExtensionImpl = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditTaskElementService iaudittaskelementservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskElementService.class);
        IAuditTaskOptionService iaudittaskoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskOptionService.class);

        HandleTaskService taskService = new HandleTaskService();
        try {
            String msg = "";
            // 事项库的新增，生成新版本
            auditTask.setVersion(ZwfwConstant.CONSTANT_STR_ONE);
            auditTask.setVersiondate(new Date());
            auditTask.setIs_history(ZwfwConstant.CONSTANT_INT_ZERO);
            auditTask.setVersionnote("");

            // 保存情形数据
            String taskid = auditTask.getTask_id();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskid", taskid);
            sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            List<AuditTaskElement> liste = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                // 删除要素
                iaudittaskelementservice.deleteByGuid(auditTaskElement.getRowguid());
                // 删除情形
                iaudittaskoptionservice.deleteByOneField("elementguid", auditTaskElement.getRowguid());
            }
            // 刪除选项名称为空的选项
            sqlc.clear();
            sqlc.eq("taskid", taskid);
            sqlc.isBlank("optionname");
            List<AuditTaskOption> listoption = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskOption auditTaskOption : listoption) {
                iaudittaskoptionservice.deleteByGuid(auditTaskOption.getRowguid());
            }
            // 修改草稿为正式事项
            sqlc.clear();
            sqlc.eq("taskid", taskid);
            sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
            liste = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                // 删除没有名称的数据
                if (StringUtil.isBlank(auditTaskElement.getElementname())) {
                    // 删除要素
                    iaudittaskelementservice.deleteByGuid(auditTaskElement.getRowguid());
                    // 删除情形
                    iaudittaskoptionservice.deleteByOneField("elementguid", auditTaskElement.getRowguid());

                } else {
                    iaudittaskelementservice.updateByField("draft", ZwfwConstant.CONSTANT_STR_ZERO,
                            auditTaskElement.getRowguid());
                }
            }
            updateShareOption(taskid);
            // 事项库新增
            if ((operation.equals("add") || operation.equals("copy") || operation.equals("confirmEdit"))) {
                EpointFrameDsManager.begin(null);
                if (!Integer.valueOf(ZwfwConstant.TASKAUDIT_STATUS_DQR).equals(auditTask.getIs_editafterimport())) {
                    // 判断事项编码是否正确
                    Map<Object, Object> map = judgeItemId(auditTask.getItem_id(), auditTask.getRowguid(), centerGuid)
                            .getResult();
                    msg = (String) map.get("msg");
                    if (StringUtil.isNotBlank(msg)) {
                        result.setResult(msg);
                        return result;
                    }
                }

                // 更新当前页面内容
                auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_SHTG));
                // 把生成的数据库在库里更新
                auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
                // 把生成的数据存入缓存，变成可用事项
                auditTaskBasicImpl.updateTaskCache("", auditTask.getRowguid());
                EpointFrameDsManager.commit();
                // 事项名称可能更改
                String taskname = auditTask.getTaskname();
                String processguid = auditTask.getProcessguid();
                String instancenameexpression = taskname + "([#=ApplyerName#])";
                String workitemnameexpression = taskname + "([#=ApplyerName#])";
                // 更新工作流版本个性化名字
                taskService.updateWPVByProcessGuid(taskname, instancenameexpression, workitemnameexpression,
                        processguid);
                // 发送MQ消息，新增全文检索索引
                try {
                    String RabbitMQMsg = "handleSerachIndex:" + "insert" + ";" + auditTask.getRowguid();
                    ProducerMQ.TopicPublish("auditTask", "insert", RabbitMQMsg, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg = "事项生成成功";
            } else if (operation.equals("windowAdd")) {
                // 设置事项状态为待上报
                auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_DSB));
                // 更新该事项数据库的信息
                auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
                msg = "待上报事项生成";
            }
            result.setResult(msg);
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<Object, Object>> judgeItemId(String itemId, String TaskGuid, String centerGuid) {
        AuditCommonResult<Map<Object, Object>> result = new AuditCommonResult<>();
        IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IHandleConfig iHandleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        try {
            Map<Object, Object> map = new HashMap<Object, Object>(16);
            boolean issubtask = true;// 是否子项
            String parentitemid = "";// 父项编码
            String msg = "";// 错误信息
            boolean iscorrect = false;
            Boolean existsItemId = true;// 是否存在相同的事项编码
            // 判断事项编码是否已经存在
            if (TaskGuid == "") {
                existsItemId = auditTaskBasicImpl.isItemIdExist(itemId).getResult();
            } else {
                existsItemId = auditTaskBasicImpl.isItemIdExist(itemId, TaskGuid).getResult();
            }
            if (existsItemId) {
                msg = "已存在相同事项编码的事项";
            } else {
                // 判断是否对事项编码进行检查
                String limit = iHandleConfig.getFrameConfig("AS_ITEM_ID_NO_LIMIT", centerGuid).getResult();
                if (!"1".equals(limit)) {
                    // 事项大小项分类
                    String taskcategory = iHandleConfig.getFrameConfig("AS_Task_Category", centerGuid).getResult();
                    if (StringUtil.isNotBlank(taskcategory)) {
                        switch (Integer.parseInt(taskcategory)) {
                            case 0:
                                String strlength = iHandleConfig.getFrameConfig("AS_ITEM_ID_LENGTH", centerGuid)
                                        .getResult();
                                if (StringUtil.isBlank(strlength)) {
                                    msg = "请验证系统参数AS_ITEM_ID_LENGTH是否配置正确！";
                                } else {
                                    String[] arrlen = strlength.split(";");
                                    if (arrlen.length != 2) {
                                        msg = "请验证系统参数AS_ITEM_ID_LENGTH是否配置正确！";
                                    } else {
                                        int sublength = StringUtil.isBlank(arrlen[1]) ? 0 : Integer.parseInt(arrlen[1]);// 子项长度
                                        int parentlength = StringUtil.isBlank(arrlen[0]) ? 0
                                                : Integer.parseInt(arrlen[0]);// 父项长度
                                        if (itemId.length() == sublength || itemId.length() == parentlength) {
                                            // 通过长度判断是主事项还是子事项
                                            if (itemId.length() == sublength) {
                                                String temp = itemId.substring(0, parentlength);
                                                if (!auditTaskBasicImpl.isItemIdExist(temp).getResult()) {
                                                    msg = "前" + parentlength + "位对应的可用事项编码不存在!";
                                                } else {
                                                    iscorrect = true;
                                                    issubtask = true;
                                                    parentitemid = temp;
                                                }
                                            } else if (itemId.length() == parentlength) {
                                                iscorrect = true;
                                                issubtask = false;
                                            } else {
                                                msg = "事项编码长度不符合规范！";
                                            }
                                        } else {
                                            msg = "事项编码长度不符合规范！";
                                        }
                                    }
                                }
                                break;
                            case 1:
                                // 代表通过right_category表区分大小项
                                iscorrect = true;
                                break;
                            case 2:
                                // 代表不区分大小项
                                iscorrect = true;
                                break;
                            default:
                                // 未设置参数
                                msg = "请验证系统参数 AS_Task_Category是否配置正确！";
                                break;
                        }
                    } else {
                        msg = "请验证系统参数 AS_Task_Category是否配置正确！";
                    }
                }
            }
            map.put("iscorrect", iscorrect);
            map.put("msg", msg);
            map.put("issubtask", issubtask);
            map.put("parentitemid", parentitemid);
            result.setResult(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> passTask(Boolean flag, String taskGuid, String taskId, AuditTask audittask,
                                              AuditTaskExtension auditTaskExtension, String addusername, String adduserguid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        IAuditTaskExtension auditTaskExtensionImpl = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditTaskResult auditResultService = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        IAuditTaskElementService iaudittaskelementservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskElementService.class);
        IAuditTaskOptionService iaudittaskoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskOptionService.class);
        IAuditTaskDoc auditTaskDocService = ContainerFactory.getContainInfo().getComponent(IAuditTaskDoc.class);
        IAuditTaskRiskpoint auditTaskRiskpointService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskRiskpoint.class);
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditTaskChargeItem auditTaskChargeItemService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskChargeItem.class);
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditOrgaServiceCenter serviceCenter = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaServiceCenter.class);
        IMessagesCenterService messageService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        HandleTaskService taskService = new HandleTaskService();
        try {
            String msg = "";
            // audittask.setIs_editafterimport(ZwfwConstant.CONSTANT_INT_ONE);
            // 获取版本号
            String version = audittask.getVersion();
            String newRowGuid = audittask.getRowguid();
            EpointFrameDsManager.begin(null);

            // 保存情形数据
            String taskid = audittask.getTask_id();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskid", taskid);
            sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            List<AuditTaskElement> liste = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                // 删除要素
                iaudittaskelementservice.deleteByGuid(auditTaskElement.getRowguid());
                // 删除情形
                iaudittaskoptionservice.deleteByOneField("elementguid", auditTaskElement.getRowguid());
            }
            // 刪除选项名称为空的选项
            sqlc.clear();
            sqlc.eq("taskid", taskid);
            sqlc.isBlank("optionname");
            List<AuditTaskOption> listoption = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskOption auditTaskOption : listoption) {
                iaudittaskoptionservice.deleteByGuid(auditTaskOption.getRowguid());
            }
            // 修改草稿为正式事项
            sqlc.clear();
            sqlc.eq("taskid", taskid);
            sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
            liste = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                // 删除没有名称的数据
                if (StringUtil.isBlank(auditTaskElement.getElementname())) {
                    // 删除要素
                    iaudittaskelementservice.deleteByGuid(auditTaskElement.getRowguid());
                    // 删除情形
                    iaudittaskoptionservice.deleteByOneField("elementguid", auditTaskElement.getRowguid());

                } else {
                    iaudittaskelementservice.updateByField("draft", ZwfwConstant.CONSTANT_STR_ZERO,
                            auditTaskElement.getRowguid());
                }
            }
            updateShareOption(taskid);
            // 审核通过默认启用事项
            audittask.setIs_enable(ZwfwConstant.CONSTANT_INT_ONE);
            // 如果版本号为空则是窗口事项新增和窗口事项复制过来的，设置为1
            if (StringUtil.isBlank(version)) {
                // 先把生成的数据在库里更新
                auditTaskExtensionImpl.updateAuditTaskAndExt(audittask, auditTaskExtension, false);
                // 把生成的数据存入缓存，变成可用事项
                // auditTaskBizlogic.updateTaskCache("",
                // audittask.getRowguid());
                // 事项名称可能更改
                // 通过processguid更新workflow_processversion表中的instancenameexpression,workitemnameexpressio
                String taskname = audittask.getTaskname();
                String processguid = audittask.getProcessguid();
                String instancenameexpression = taskname + "([#=ApplyerName#])";
                String workitemnameexpression = taskname + "([#=ApplyerName#])";
                // 更新工作流版本个性化名字
                taskService.updateWPVByProcessGuid(taskname, instancenameexpression, workitemnameexpression,
                        processguid);
                // 完成事项的状态变更
                audittask.setVersionnote("事项新增");
                EpointFrameDsManager.commit();
                auditTaskBasicImpl.passAuditTask(flag, newRowGuid, taskId, audittask, auditTaskExtension);
                // 插入缓存
                auditTaskBasicImpl.updateTaskCache("", newRowGuid);
                msg = "事项审核通过，生成事项！";
            }
            // 窗口变更上报的
            else {
                // 根据taskid获取可用事项
                AuditTask usableAuditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(taskId).getResult();
                String usableTaskGuid = "";
                if (usableAuditTask != null) {
                    usableTaskGuid = usableAuditTask.getRowguid();
                } else {
                    usableTaskGuid = newRowGuid;
                }
                if (flag == true) {
                    // 先保存数据
                    auditTaskExtensionImpl.updateAuditTaskAndExt(audittask, auditTaskExtension, false);
                    EpointFrameDsManager.commit();
                    auditTaskBasicImpl.passAuditTask(flag, newRowGuid, taskId, audittask, auditTaskExtension);
                    // 暂时去掉，进一步验证，等到确认没问题直接去除
                    // auditTaskBizlogic.buildNewVersionByBasicExt(usableTaskGuid,
                    // audittask,
                    // auditTaskExtension);
                    // 更新缓存
                    auditTaskBasicImpl.updateTaskCache(taskGuid, audittask.getRowguid());
                    msg = "事项审核通过，同时生成新版本！";
                } else {

                    Map<String, String> conditionMap = new HashMap<String, String>(16);
                    if (StringUtil.isNotBlank(taskGuid)) {
                        conditionMap.put("taskguid=", taskGuid);
                    }
                    // 申报材料的tab
                    List<AuditTaskMaterial> listMaterial = auditTaskMaterialService
                            .selectTaskMaterialListByTaskGuid(newRowGuid, false).getResult();

                    // 情形
                    taskService.notBuildVersionCase(usableTaskGuid, newRowGuid, listMaterial, addusername);

                    taskService.notBuildVersion(AuditTaskMaterial.class, usableTaskGuid, newRowGuid, listMaterial,
                            addusername);
                    // 岗位的tab
                    List<AuditTaskRiskpoint> listRiskpoint = auditTaskRiskpointService
                            .getRiskPointListByTaksGuid(newRowGuid, false).getResult();
                    taskService.notBuildVersion(AuditTaskRiskpoint.class, usableTaskGuid, newRowGuid, listRiskpoint,
                            addusername);
                    // 文书的tab
                    List<AuditTaskDoc> listDoc = auditTaskDocService.selectAuditTaskDocByTaskGuid(newRowGuid, false)
                            .getResult();
                    taskService.notBuildVersion(AuditTaskDoc.class, usableTaskGuid, newRowGuid, listDoc, addusername);
                    // 收费项目的tab
                    List<AuditTaskChargeItem> listChargeitem = auditTaskChargeItemService
                            .selectAuditTaskChargeItemByTaskGuid(newRowGuid, false).getResult();
                    taskService.notBuildVersion(AuditTaskChargeItem.class, usableTaskGuid, newRowGuid, listChargeitem,
                            addusername);

                    // 如果key为审批结果tab
                    AuditTaskResult auditResult = auditResultService.getAuditResultByTaskGuid(newRowGuid, false)
                            .getResult();
                    // 更新结果数据
                    taskService.notBuildVersionByResult(usableTaskGuid, auditResult, addusername);
                    taskService.notBuildNewVersionByBasicExt(usableTaskGuid, audittask, auditTaskExtension,
                            addusername);
                    // 删除待审核事项
                    if (!newRowGuid.equals(usableTaskGuid)) {
                        auditTaskBasicImpl.deleteAuditTask(newRowGuid);
                    }
                    msg = "审核通过。同时修改了事项！";

                    EpointFrameDsManager.commit();
                }
            }
            // 删除待办
            FrameRole role = roleService.getRoleByRoleField("rolename", "中心管理员");
            if (role != null) {
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("BELONGXIAQU", audittask.getAreacode());
                List<AuditOrgaServiceCenter> certerList = serviceCenter
                        .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();
                if (certerList != null && !certerList.isEmpty()) {
                    AuditOrgaServiceCenter orgaServiceCenter = certerList.get(0);
                    String ouGuid = orgaServiceCenter.getOuguid();
                    List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, role.getRoleGuid(), "", "", false,
                            true, true, 3);
                    if (listUser != null && !listUser.isEmpty()) {
                        for (FrameUser user : listUser) {
                            messageService.deleteMessageByIdentifier(audittask.getTask_id(), user.getUserGuid());
                        }
                    }
                }
            }
            // 消息提醒
            if (StringUtil.isNotBlank(audittask.getReport_userguid())) {
                IOnlineMessageInfoService onlinemessage = ContainerFactory.getContainInfo()
                        .getComponent(IOnlineMessageInfoService.class);
                onlinemessage.insertMessage(UUID.randomUUID().toString(), adduserguid, addusername,
                        audittask.getReport_userguid(), audittask.getReport_displayname(),
                        audittask.getReport_userguid(), "您上报的“" + audittask.getTaskname() + "”事项已经审核通过！", new Date());
            }
            result.setResult(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> notpassAuditTask(String taskGuid) {
        IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditOrgaServiceCenter serviceCenter = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaServiceCenter.class);
        IMessagesCenterService messageService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        AuditCommonResult<String> result = new AuditCommonResult<>();
        String msg = "";
        AuditTask auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
        auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_SHWTG));
        // 只更新数据库操作
        auditTask.setOperatedate(new Date());
        auditTaskBasicImpl.updateAuditTask(auditTask);
        // 消息提醒
        if (StringUtil.isNotBlank(auditTask.getReport_userguid())) {
            IOnlineMessageInfoService onlinemessage = ContainerFactory.getContainInfo()
                    .getComponent(IOnlineMessageInfoService.class);
            onlinemessage.insertMessage(UUID.randomUUID().toString(), UserSession.getInstance().getUserGuid(),
                    UserSession.getInstance().getDisplayName(), auditTask.getReport_userguid(),
                    auditTask.getReport_displayname(), auditTask.getReport_userguid(),
                    "您上报的“" + auditTask.getTaskname() + "”事项未能审核通过，请在“审核不通过”中查看。", new Date());
        }
        msg = "处理成功！";
        // 删除待办
        FrameRole role = roleService.getRoleByRoleField("rolename", "中心管理员");
        if (role != null) {
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("BELONGXIAQU", auditTask.getAreacode());
            List<AuditOrgaServiceCenter> certerList = serviceCenter
                    .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();
            if (certerList != null && !certerList.isEmpty()) {
                AuditOrgaServiceCenter orgaServiceCenter = certerList.get(0);
                String ouGuid = orgaServiceCenter.getOuguid();
                List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, role.getRoleGuid(), "", "", false, true,
                        true, 3);
                if (listUser != null && !listUser.isEmpty()) {
                    for (FrameUser user : listUser) {
                        messageService.deleteMessageByIdentifier(auditTask.getTask_id(), user.getUserGuid());
                    }
                }
            }
        }
        result.setResult(msg);
        return result;
    }

    @Override
    public AuditCommonResult<String> report(AuditTask auditTask, AuditTaskExtension auditTaskExtension) {
        IAuditTaskExtension auditTaskExtensionImpl = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IAuditOrgaServiceCenter serviceCenter = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaServiceCenter.class);
        IMessagesCenterService messageService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        AuditCommonResult<String> result = new AuditCommonResult<>();
        String msg = "";
        // 设置事项状态为待审核
        auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_DSH));
        auditTask.setAuditopinion("");
        auditTask.setReport_displayname(UserSession.getInstance().getDisplayName());
        auditTask.setReport_userguid(UserSession.getInstance().getUserGuid());
        // 更新该事项数据库的信息
        auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
        msg = "上报成功，待审核";
        // 发送待办
        // 获取中心管理员guid
        FrameRole role = roleService.getRoleByRoleField("rolename", "中心管理员");
        if (role != null) {
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("BELONGXIAQU", auditTask.getAreacode());
            List<AuditOrgaServiceCenter> certerList = serviceCenter
                    .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();
            if (certerList != null && !certerList.isEmpty()) {
                AuditOrgaServiceCenter orgaServiceCenter = certerList.get(0);
                String ouGuid = orgaServiceCenter.getOuguid();
                String url = "epointzwfw/audittask/audittasktopwizardsh?opr=inaudit&taskGuid=" + auditTask.getRowguid()
                        + "&copyTaskGuid=" + auditTask.getRowguid() + "&is_editafterimport=2";
                List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, role.getRoleGuid(), "", "", false, true,
                        true, 3);
                if (listUser != null && !listUser.isEmpty()) {
                    for (FrameUser user : listUser) {
                        messageService.insertWaitHandleMessage(UUID.randomUUID().toString(), auditTask.getTaskname(),
                                IMessagesCenterService.MESSAGETYPE_WAIT, user.getUserGuid(), user.getDisplayName(),
                                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                "事项审核", url, ouGuid, "", 1, "", "", auditTask.getTask_id(), "", new Date(), "",
                                UserSession.getInstance().getUserGuid(), "", "");
                    }
                }
            }
        }
        result.setResult(msg);
        return result;
    }

    // 同步修改套餐中关联的字段
    public void updateShareOption(String taskid) {
        IAuditSpShareoption iauditspshareoption = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareoption.class);
        IAuditTaskOptionService iaudittaskoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskOptionService.class);
        List<AuditTaskOption> audittaskoptionlist = iaudittaskoptionservice.findListByTaskid(taskid).getResult();
        for (AuditTaskOption auditTaskOption : audittaskoptionlist) {
            if (StringUtil.isNotBlank(auditTaskOption.getCopyfrom())) {
                iauditspshareoption.updateOptionguid(auditTaskOption.getRowguid(), auditTaskOption.getCopyfrom());
            }
        }
    }

    @Override
    public AuditCommonResult<String> confirmPass(Boolean flag, String taskGuid, String taskId, AuditTask audittask,
                                                 AuditTaskExtension auditTaskExtension) {
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        String msg = "生成新版本成功";
        try {
            auditTaskService.passAuditTask(flag, taskGuid, taskId, audittask, null);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        AuditCommonResult<String> result = new AuditCommonResult<>();
        result.setResult(msg);
        return result;
    }
}


package com.epoint.audittask.workflow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.audittask.workflow.bizlogic.AuditTaskRiskpointCommonBizlogic;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.workflow.service.common.custom.Activity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowProcess;
import com.epoint.workflow.service.common.entity.config.WorkflowProcessVersion;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;

/**
 * 事项岗位风险点信息表list页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-10-09 09:12:27]
 */
@RestController("jnaudittaskriskpointlistaction")
@Scope("request")
public class JNAuditTaskRiskpointListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -3654364071195527677L;

    /**
     * 事项岗位风险点信息表实体对象
     */
    private AuditTaskRiskpoint dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskRiskpoint> model1;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskRiskpoint> model2;
    /**
     * 下个岗位model
     */
    private List<SelectItem> nextActivityModel = null;
    /**
     * 是否排序生成下个岗位
     */
    private String isSort = ZwfwConstant.CONSTANT_STR_ZERO;
    /**
     * 事项标识
     */
    private String taskGuid;
    /**
     * 流程版本guid
     */
    private String processversionguid = "";

    /**
     * 工作流活动service
     */
    @Autowired
    private IWorkflowActivityService wfaservice;
    /**
     * 工作流变迁service
     */
    @Autowired
    private IWorkflowTransitionService wftservice;
    /**
     * 变迁
     */
    WorkflowTransition workflowTran;

    @Autowired
    private IWorkflowProcessVersionService wfvservice;

    @Autowired
    private IWorkflowProcessService wpService;
    
    @Autowired
    private ISendMQMessage sendMQMessageService;
    /**
     * 事项
     */
    private AuditTask dataBeanTask;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension dataBeanTaskExtension;
    /**
     * 编辑时候拷贝过来的taskGuid
     */
    private String copyTaskGuid;
    /**
     * 事项id
     */
    private String taskId;
    /**
     * 操作
     */
    private String operation;
    /**
     * 事项数据库操作实现
     */

    private AuditTaskRiskpointCommonBizlogic auditTaskRiskpointBizlogic = new AuditTaskRiskpointCommonBizlogic();

    @Autowired
    private IAuditTask auditTaskBasicService;

    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointimpl;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    @Autowired
    private IWorkflowProcessVersionService workflowProcessVersionService9;

    @Autowired
    private IHandleTask handleTaskService;

    @Autowired
    private IAuditTask auditTaskServie;
    
    @Autowired
    private IHandleConfig handleConfigService;

    @Override
    public void pageLoad() {
        // 获取taskguid
        taskGuid = this.getRequestParameter("taskGuid");
        taskId = this.getRequestParameter("taskId");
        copyTaskGuid = "undefined".equals(this.getRequestParameter("copyTaskGuid")) ? ""
                : this.getRequestParameter("copyTaskGuid");
        // 获取操作
        operation = this.getRequestParameter("operation");
        if (StringUtil.isNotBlank(taskGuid)) {
            // 时间戳为空，则说明是用新增页面跳转过来的
            if (StringUtil.isNotBlank(copyTaskGuid)) {
                if (operation.equals("windowCopy") || operation.equals("copy")) {
                    // 事项新增从数据库获取
                    dataBeanTask = auditTaskBasicService.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                    dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid, false)
                            .getResult();
                }
                else {
                    dataBeanTask = auditTaskBasicService.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                    dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid, false)
                            .getResult();
                }
            }
            else {
                // 事项新增从数据库获取
                dataBeanTask = auditTaskBasicService.getAuditTaskByGuid(taskGuid, false).getResult();
                dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false).getResult();
            }
            processversionguid = dataBeanTask == null ? "" : dataBeanTask.getPvguid();
        }

        if (isPostback()) {
            // 非首次加载从viewdata中获取是否排序
            isSort = this.getViewData("isSort");
        }
        AuditTask Task = auditTaskBasicService.getAuditTaskByGuid(taskGuid, false).getResult();
    	if(StringUtil.isNotBlank(Task)){
    		String unid=Task.get("unid");
    		//system.out.println(unid);
    		dataBeanTask.set("unid", Task.get("unid"));
    	}
        this.addCallbackParam("processversionguid", processversionguid);
    }

    /**
     * 
     * 按排序生成下个岗位
     * 
     */
    public void sortFlow() {
        // 这里判断一下是不是有开始结束活动节点，如果不存在，则说明流程复制有问题，直接初始化开始和结束
        List<WorkflowActivity> listAct = wfaservice.selectAllByProcessVersionGuid(processversionguid, "");
        boolean hasStart = false;
        boolean hasEnd = false;
        for (int i = 0; i < listAct.size(); i++) {
            if (10 == listAct.get(i).getActivityType()){
                hasStart = true;
            }
            else if (20 == listAct.get(i).getActivityType()){
                hasEnd = true;
            }
        }
        if (!hasStart) {
            WorkflowActivity actStart = new WorkflowActivity();
            actStart.setActivityDispName("开始");
            actStart.setActivityGuid(UUID.randomUUID().toString());
            actStart.setActivityName("开始");
            actStart.setActivityType(10);
            actStart.setIconX("3000");
            actStart.setIconY("2000");
            actStart.setSplitType(30);
            actStart.setVmlId(1);
            actStart.setProcessVersionGuid(processversionguid);
            wfaservice.addWorkflowActivity(actStart);
        }
        if (!hasEnd) {
            WorkflowActivity actEnd = new WorkflowActivity();
            actEnd.setActivityDispName("结束");
            actEnd.setActivityGuid(UUID.randomUUID().toString());
            actEnd.setActivityName("结束");
            actEnd.setActivityType(20);
            actEnd.setIconX("12000");
            actEnd.setIconY("2000");
            actEnd.setJoinType(30);
            actEnd.setVmlId(-1);
            actEnd.setProcessVersionGuid(processversionguid);
            wfaservice.addWorkflowActivity(actEnd);
        }
        isSort = ZwfwConstant.CONSTANT_STR_ONE;
        this.addViewData("isSort", isSort);
    }

    /**
     * 新增岗位时不生成下个岗位
     */
    public void sortDefault() {
        isSort = ZwfwConstant.CONSTANT_STR_ZERO;
        this.addViewData("isSort", isSort);
    }

    /**
     * 
     * 生成内部流程图
     * 
     */
    public void autoGenWorkFlow() {
        String processGuid = "";
        WorkflowProcess workflowProcess = wpService.getByProcessGuid(dataBeanTask.getProcessguid());
        // 不存在流程
        if (workflowProcess == null || workflowProcess.isEmpty()) {
            workflowProcess = new WorkflowProcess();
            workflowProcess.setProcessName(dataBeanTask.getTaskname());
            processGuid = UUID.randomUUID().toString();
            workflowProcess.setProcessGuid(processGuid);
            dataBeanTask.setProcessguid(processGuid);
            WorkflowProcessVersion version = wpService.addWorkflowProcess(workflowProcess, userSession.getUserGuid(),
                    "");
            processversionguid = version.getProcessVersionGuid();
            dataBeanTask.setPvguid(processversionguid);
            updateAuditTask(dataBeanTask);
            List<AuditTaskRiskpoint> datalist = getDataGridData2().getWrappedData();
            if (datalist != null && datalist.size() > 0) {
                try {
                    for (AuditTaskRiskpoint auditTaskRiskpoint : datalist) {
                        wfaservice.deleteActivityCompletely(auditTaskRiskpoint.getActivityguid());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.addCallbackParam("pvguid", processversionguid);
        }
        else {
            WorkflowProcessVersion workflowProcessVersion = wfvservice.getDetail(processversionguid);
            // 不存在流程版本
            if (workflowProcessVersion == null || workflowProcessVersion.isEmpty()) {
                workflowProcess = new WorkflowProcess();
                workflowProcess.setProcessName(dataBeanTask.getTaskname());
                processGuid = UUID.randomUUID().toString();
                workflowProcess.setProcessGuid(processGuid);
                dataBeanTask.setProcessguid(processGuid);
                WorkflowProcessVersion version = wpService.addWorkflowProcess(workflowProcess,
                        userSession.getUserGuid(), "");
                processversionguid = version.getProcessVersionGuid();
                dataBeanTask.setPvguid(processversionguid);
                updateAuditTask(dataBeanTask);
                List<AuditTaskRiskpoint> datalist = getDataGridData2().getWrappedData();
                if (datalist != null && datalist.size() > 0) {
                    try {
                        for (AuditTaskRiskpoint auditTaskRiskpoint : datalist) {
                            wfaservice.deleteActivityCompletely(auditTaskRiskpoint.getActivityguid());
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.addCallbackParam("pvguid", processversionguid);
            }
        }

        String message = "";
        boolean isCorrect = true;
        List<AuditTaskRiskpoint> datalist = getDataGridData2().getWrappedData();
        if (datalist != null && datalist.size() > 0) {
            // 1、检查岗位是不是设置正确异常情况包括：
            // ---a、某个岗位没有设置下一个岗位；
            // ---b、某个岗位的下一步以及分支指向同一个岗位；
            // ---c、存在多个岗位同时指向结束节点
            // ---d、存在多个岗位作为起始岗位
            int toEndActivityCount = 0;
            // 先扫描一次，作为临时对比用
            Set<String> activitySet = new HashSet<String>();
            for (AuditTaskRiskpoint auditTaskRiskpoint : datalist) {

                activitySet.add(auditTaskRiskpoint.getActivityguid() + "&" + auditTaskRiskpoint.getActivityname());
            }
            // 定义一个set，用来存放所有目标岗位的标识

            // 再进行计算
            for (AuditTaskRiskpoint auditTaskRiskpoint : datalist) {
                // //system.out.println(auditTaskRiskpoint.get("nextactivityname"));
                String activityname = auditTaskRiskpoint.getActivityname();// 岗位名称
                String nextactivityguid = auditTaskRiskpoint.get("nextactivityguid") == null ? ""
                        : auditTaskRiskpoint.get("nextactivityguid");// 下个岗位标识
                String secondactivityguid = auditTaskRiskpoint.get("secondactivityguid") == null ? ""
                        : auditTaskRiskpoint.get("secondactivityguid");// 第二分支
                String thirdactivityguid = auditTaskRiskpoint.get("thirdactivityguid") == null ? ""
                        : auditTaskRiskpoint.get("thirdactivityguid");// 第三分支
                // 去除已经有上一步骤的活动
                activitySet.removeIf(record -> {
                    String activityGuid = record.split("&")[0];

                    boolean result = activityGuid.equals(auditTaskRiskpoint.get("nextactivityguid"))
                            || activityGuid.equals(auditTaskRiskpoint.get("secondactivityguid"));
                    return result;
                });
                if (StringUtil.isBlank(nextactivityguid) && StringUtil.isBlank(secondactivityguid)) {
                    message = "“" + activityname + "”没有设置下一个岗位！";
                    isCorrect = false;
                    break;
                }
                else if (nextactivityguid.equals(secondactivityguid)
                        || (StringUtil.isNotBlank(nextactivityguid) && nextactivityguid.equals(thirdactivityguid))
                        || (StringUtil.isNotBlank(secondactivityguid)
                                && secondactivityguid.equals(thirdactivityguid))) {
                    message = "“" + activityname + "”不能重复指向同一个岗位！";
                    isCorrect = false;
                    break;
                }
                if ("结束".equals(auditTaskRiskpoint.get("nextactivityname"))
                        || "结束".equals(auditTaskRiskpoint.get("secondactivityname"))) {
                    if (StringUtil.isNotBlank(nextactivityguid) && StringUtil.isNotBlank(secondactivityguid)) {
                        message = "岗位" + activityname + "不能既指向结束岗位，又指向其他非结束岗位";
                        isCorrect = false;
                        break;
                    }
                    if (toEndActivityCount > 0) {
                        message = "已经有活动指向结束岗位，“" + activityname + "”不能重复指向结束岗位！";
                        isCorrect = false;
                        break;
                    }
                    else {
                        toEndActivityCount++;
                    }

                }
                if("结束".equals(auditTaskRiskpoint.get("nextactivityname"))&&StringUtil.isNotBlank(auditTaskRiskpoint.get("secondactivityguid"))){
                    message = "结束岗位不能设置分支流程";
                    isCorrect = false;
                    break;
                }
            }

            // 这里加上对最后一个岗位的判断，如果存在多个岗位，就说明有多个岗位同时作为开始岗位
            if (isCorrect && activitySet.size() >= 2) {
                Iterator<String> acList = activitySet.iterator();
                String acNames = "";
                while (acList.hasNext()) {
                    acNames += acList.next().split("&")[1] + "、";
                }
                acNames = acNames.substring(0, acNames.length() - 1);
                message = "按照当前配置，岗位" + acNames + "都可作为开始岗位，请进行调整！";
                isCorrect = false;
            }
            //
            else if (isCorrect && activitySet.size() == 0) {
                message = "开始岗位不能作为下一步配置，请进行调整！";
                isCorrect = false;
            }
            if(toEndActivityCount==0){
                message = "结束岗位配置有误，请进行调整！";
                isCorrect = false;
            }
            // 2、根据设置的流转顺序生成流程
            if (isCorrect) {
                List<Activity> activitiList = this.sortActivity(datalist);
                WorkflowParameter9 workFlowParameter9 = new WorkflowParameter9();
                workFlowParameter9.setProcessVersionGuid(processversionguid);

                // TODO 需要等到更新之后再处理
                // 2.1、生成流程
                wfvservice.designProcess(processversionguid, activitiList, true);
                // 2.2、流程配置修改
                handleWorkFlow(processversionguid, ZwfwUserSession.getInstance().getAreaCode());
                message = "生成内部流程成功！";
            }
        }
        else {
            List<WorkflowActivity> listActivity = wfaservice.selectByProcessVersionGuid(processversionguid);
            for (int i = 0; i < listActivity.size(); i++) {
                wfaservice.deleteActivityCompletely(listActivity.get(i).getActivityGuid());
            }

        }
        this.addCallbackParam("message", message);
        // 一旦点击了生成流程标志位变成1，缓存map里面工作流模块的标志位为true
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            if (dataBeanTask.getIsneednewversion() == null || dataBeanTask.getIsneednewversion() == 0) {
                // 事项标志位为true那么事项isneednewversion=1
                dataBeanTask.setIsneednewversion(ZwfwConstant.CONSTANT_INT_ONE);
                // 标志位存入数据库
                updateAuditTask(dataBeanTask);
            }
        }
    }

    /**
     * 
     * 设置活动的流转顺序
     * 
     */
    private List<Activity> sortActivity(List<AuditTaskRiskpoint> datalist) {
        // 1、组织流程节点,先创建一个容器，用来放置所有的节点对象
        List<Activity> activitiList = new ArrayList<Activity>();
        AuditTask audittask = auditTaskBasicService.getAuditTaskByGuid(taskGuid, false).getResult();
        // 构建活动对象
        int ordernum = 1000;
        for (int i = 0; i < datalist.size(); i++) {
            AuditTaskRiskpoint auditTaskRiskpoint = datalist.get(i);
            // 构建1个活动对象
            Activity ac = new Activity();
            // 构建1个工作流活动
            WorkflowActivity workflowActivity = new WorkflowActivity();
            ac.setActivity(workflowActivity);
            // 设置活动guid,这里可以关联上业务系统记录的主键
            workflowActivity.setActivityGuid(auditTaskRiskpoint.getActivityguid());
            // 设置活动名称
            workflowActivity.setActivityName(auditTaskRiskpoint.getActivityname());

            if (audittask != null) {
                AuditTaskExtension auditTaskExt = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false)
                        .getResult();
                String formUrl = ZwfwConstant.CONSTANT_FORM_URL;
                if (auditTaskExt != null && !auditTaskExt.isEmpty()
                        && StringUtil.isNotBlank(auditTaskExt.getCustomformurl())){
                    formUrl = auditTaskExt.getCustomformurl();
                }
                // 即办件
                if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(audittask.getType()))) {
                    workflowActivity.setHandleUrl(formUrl + (i == 0 ? "" : "?projectguid=[#=ProjectGuid#]"));
                }
                // 非即办件
                else {
                    workflowActivity.setHandleUrl(formUrl + (i == 0 ? "" : "?projectguid=[#=ProjectGuid#]"));
                }
            }
            workflowActivity.setIsLockWhenMultiTransactor(1);
            workflowActivity.setSplitType(WorkflowKeyNames9.SplitType_XOR);
            workflowActivity.setActivityType(WorkflowKeyNames9.ActivityType_Manual);
            // 其他关于工作流活动的属性都可以设置，具体可以参考工作流人工活动基本信息页面
            // 设置活动处理人,多个以;分割
            ac.setPaticipator(
                    StringUtil.isBlank(auditTaskRiskpoint.getAcceptguid()) ? "" : auditTaskRiskpoint.getAcceptguid());
            // 加入容器
            activitiList.add(ac);
        }
        // 2、 对岗位进行排序
        for (int j = 0; j < datalist.size(); j++) {
            AuditTaskRiskpoint auditTaskRiskpoint = datalist.get(j);
            String activityguid = auditTaskRiskpoint.getActivityguid();// 当前活动标识
            String nextactivityguid = auditTaskRiskpoint.get("nextactivityguid"); // 下个岗位
            String secondactivityguid = auditTaskRiskpoint.get("secondactivityguid") == null ? ""
                    : auditTaskRiskpoint.get("secondactivityguid");// 第二分支
            String thirdactivityguid = auditTaskRiskpoint.get("thirdactivityguid") == null ? ""
                    : auditTaskRiskpoint.get("thirdactivityguid");// 第三分支
            Activity ac1 = new Activity();
            Activity ac2 = new Activity();
            Activity ac3 = new Activity();
            Activity ac4 = new Activity();
            for (int k = 0; k < activitiList.size(); k++) {
                if (activityguid.equals(activitiList.get(k).getActivity().getActivityGuid())) {
                    ac1 = activitiList.get(k);
                }
                if (nextactivityguid.equals(activitiList.get(k).getActivity().getActivityGuid())) {
                    ac2 = activitiList.get(k);
                }
                if (StringUtil.isNotBlank(secondactivityguid)) {
                    if (secondactivityguid.equals(activitiList.get(k).getActivity().getActivityGuid())) {
                        ac3 = activitiList.get(k);
                    }
                }
                if (StringUtil.isNotBlank(thirdactivityguid)) {
                    if (thirdactivityguid.equals(activitiList.get(k).getActivity().getActivityGuid())) {
                        ac4 = activitiList.get(k);
                    }
                }
            }
            // 组织活动间的关系,第一个活动指向下个岗位
            if (ac2.getActivity() != null) {
                ac1.getNext().add(ac2);
            }
            // 组织活动间的关系,第一个活动指向第二分支
            if (ac3.getActivity() != null) {
                ac1.getNext().add(ac3);
            }
            // 组织活动间的关系,第一个活动指向第三分支
            if (ac4.getActivity() != null) {
                ac1.getNext().add(ac4);
            }
        }
        List<Activity> aclist = new ArrayList<Activity>();
        for (Activity activity1 : activitiList) {
            List<Activity> list = activity1.getNext();
            for (Activity activity2 : list) {
                if (activitiList.contains(activity2)) {
                    aclist.add(activity2);
                }
            }
        }
        activitiList.removeAll(aclist);
        setOrder(activitiList, ordernum);
        return activitiList;
    }

    public void setOrder(List<Activity> activitiList, int orderNum) {
        if (activitiList.size() > 0 && activitiList.get(0).getActivity() != null) {
            WorkflowActivity workflowActivity = activitiList.get(0).getActivity();
            workflowActivity.setOrderNum(orderNum);
            orderNum -= 10;
            setOrder(activitiList.get(0).getNext(), orderNum);
        }
    }

    /**
     * 
     * 保存信息
     * 
     */
    public void saveInfo() {
        List<AuditTaskRiskpoint> auditTaskRiskpointList = getDataGridData1().getWrappedData();
        for (AuditTaskRiskpoint auditTaskRiskpoint : auditTaskRiskpointList) {
            String isRiskPoint = StringUtils.isBlank(auditTaskRiskpoint.get("isriskpoint")) ? ""
                    : auditTaskRiskpoint.get("isriskpoint");
            if ("否".equals(isRiskPoint)) {
                auditTaskRiskpoint.setIsriskpoint(0);
            }
            else {
                auditTaskRiskpoint.setIsriskpoint(1);
            }
            update(auditTaskRiskpoint);

        }
        this.addCallbackParam("msg", "保存成功！");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData1().getSelectKeys();
        for (String sel : select) {
            auditTaskRiskpointimpl.deleteAuditTaskRiskpoint(
                    auditTaskRiskpointimpl.getAuditTaskRiskpointByRowguid(sel, false).getResult());
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 
     * 审核通过操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void passAudit() {
        String msg = "";
        // 调用存储过程审核通过
        AuditCommonResult<String> result;
        String syncTaskGuid = dataBeanTask.getRowguid();
        int isNeedNewVersion = dataBeanTask.getIsneednewversion() == null ? 0 : dataBeanTask.getIsneednewversion();
        boolean flag = isNeedNewVersion == 1
                || auditTaskServie.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
                        taskGuid, dataBeanTask.getRowguid(), dataBeanTask, dataBeanTaskExtension).getResult();
        if (!flag) {
            syncTaskGuid = taskGuid;
        }
        if (StringUtil.isNotBlank(dataBeanTask.getVersion())) {
            // 从缓存里获取存储标志的map
            result = handleTaskService.passTask(flag, copyTaskGuid, taskId, dataBeanTask, dataBeanTaskExtension,
                    userSession.getDisplayName(), userSession.getUserGuid());
        }
        else {
            result = handleTaskService.passTask(flag, taskGuid, taskId, dataBeanTask, dataBeanTaskExtension,
                    userSession.getDisplayName(), userSession.getUserGuid());
        }
        if (!result.isSystemCode()) {
            msg = "处理失败！";
        }
        else {
            msg = result.getResult();
            syncWindowTask("modify", syncTaskGuid);
        }
        this.addCallbackParam("msg", msg);
    }

    /**
     * 
     * 审核不通过操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void notPassAudit() {
        String msg = "";
        // 调用存储过程审核通过
        msg = handleTaskService.notpassAuditTask(dataBeanTask.getRowguid()).getResult();
        this.addCallbackParam("msg", msg);
    }

    /**
     * 
     * 初始化表格1数据
     * 
     * @return
     */
    public DataGridModel<AuditTaskRiskpoint> getDataGridData1() {
        // 获得表格对象
        if (model1 == null) {
            model1 = new DataGridModel<AuditTaskRiskpoint>()
            {

                @Override
                public List<AuditTaskRiskpoint> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(copyTaskGuid)) {
                        sql.eq("TaskGuid", copyTaskGuid);
                    }
                    else {
                        sql.eq("TaskGuid", taskGuid);
                    }
                    sql.setOrderDesc("ordernum");
                    sql.setOrderAsc("operatedate");
                    // sortField = "ordernum desc,operatedate asc";
                    sortOrder = "";
                    PageData<AuditTaskRiskpoint> pageData = auditTaskRiskpointimpl
                            .getAuditTaskRiskpointPageData(sql.getMap(), first, 0, sortField, sortOrder)
                            .getResult();
                    for (AuditTaskRiskpoint auditTaskRiskpoint : pageData.getList()) {
                        if ("1".equals(auditTaskRiskpoint.getIsriskpoint())) {
                            auditTaskRiskpoint.put("isriskpoint", "是");
                        }
                        else if ("0".equals(auditTaskRiskpoint.getIsriskpoint())) {
                            auditTaskRiskpoint.put("isriskpoint", "否");
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model1;
    }

    /**
     * 
     * 初始化表格2数据
     * 
     * @return
     */
    public DataGridModel<AuditTaskRiskpoint> getDataGridData2() {
        // 获得表格对象
        if (model2 == null) {
            model2 = new DataGridModel<AuditTaskRiskpoint>()
            {

                @Override
                public List<AuditTaskRiskpoint> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    List<AuditTaskRiskpoint> list = new ArrayList<>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(copyTaskGuid)) {
                        sql.eq("TaskGuid", copyTaskGuid);
                    }
                    else {
                        sql.eq("TaskGuid", taskGuid);
                    }
                    sql.setOrderDesc("ordernum");
                    sql.setOrderAsc("operatedate");
                    // sortField = "ordernum desc,operatedate asc";
                    sortOrder = "";
                    PageData<AuditTaskRiskpoint> pageData = auditTaskRiskpointimpl
                            .getAuditTaskRiskpointPageData(sql.getMap(), first, 0, sortField, sortOrder)
                            .getResult();
                    list = pageData.getList();
                    // 排序号等于1的时候执行
                    for (int i = 0; i < list.size(); i++) {
                        AuditTaskRiskpoint riskpoint = list.get(i);
                        // 按排序生成下个岗位
                        if (isSort.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                            if (i < list.size() - 1) {
                                // 设置非最后一个岗位的下个岗位
                                riskpoint.put("nextactivityguid", list.get(i + 1).getActivityguid());
                                riskpoint.put("nextactivityname", list.get(i + 1).getActivityname());
                            }
                            else if (i == list.size() - 1) {
                                // 最后一步下个岗位为结束
                                List<WorkflowActivity> activitylist = wfaservice
                                        .selectAllByProcessVersionGuid(processversionguid, " and activityName = '结束'");
                                if (activitylist != null && activitylist.size() > 0) {
                                    // 设置最后一个岗位到结束
                                    riskpoint.put("nextactivityguid", activitylist.get(0).getActivityGuid());
                                    riskpoint.put("nextactivityname", "结束");
                                }
                                else {
                                    riskpoint.put("nextactivityguid", "");
                                }
                            }
                        }
                        else {
                            // 获取下个岗位
                            List<WorkflowTransition> wftlist = wftservice
                                    .selectByFromActivityGuid(riskpoint.getActivityguid());
                            if (wftlist != null && wftlist.size() > 0) {
                                SqlConditionUtil sqlmap = new SqlConditionUtil();
                                String toActivityGuids = "";
                                for (int j = 0; j < wftlist.size(); j++) {
                                    if (j == wftlist.size() - 1) {
                                        toActivityGuids += "'" + wftlist.get(j).getToActivityGuid() + "'";
                                    }
                                    else {
                                        toActivityGuids += "'" + wftlist.get(j).getToActivityGuid() + "',";
                                    }
                                }
                                // 环节标识
                                sqlmap.in("activityguid", toActivityGuids);
                                // 倒叙
                                sqlmap.setOrderDesc("ordernum");
                                List<AuditTaskRiskpoint> toAuditTaskRiskpointList = auditTaskRiskpointimpl
                                        .getResultRiskpointByCondition(sqlmap.getMap()).getResult();
                                if (toAuditTaskRiskpointList != null && toAuditTaskRiskpointList.size() > 0) {
                                    for (int k = 0; k < toAuditTaskRiskpointList.size(); k++) {
                                        if (k == 0) {
                                            riskpoint.put("nextactivityguid",
                                                    toAuditTaskRiskpointList.get(k).getActivityguid());
                                            riskpoint.put("nextactivityname",
                                                    toAuditTaskRiskpointList.get(k).getActivityname());
                                        }
                                        else if (k == 1) {
                                            riskpoint.put("secondactivityguid",
                                                    toAuditTaskRiskpointList.get(k).getActivityguid());
                                            riskpoint.put("secondactivityname",
                                                    toAuditTaskRiskpointList.get(k).getActivityname());
                                        }
                                    }
                                }
                                // 如果未在办理环节表中查到数据，则说明下一步环节为结束，直接赋值
                                else {
                                    riskpoint.put("nextactivityguid", wftlist.get(0).getToActivityGuid());
                                    riskpoint.put("nextactivityname", "结束");
                                }
                            }
                        }
                    }

                    int count = pageData.getRowCount();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model2;

    }

    /**
     * 
     * 初始化下个岗位的model
     * 
     * @param activityGuid
     * @return
     */
    public List<SelectItem> getNextActivityModel() {
        String activityGuid = this.getRequestParameter("activityGuid");
        if (nextActivityModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            List<AuditTaskRiskpoint> resultList = auditTaskRiskpointimpl
                    .getResultRiskpoint(copyTaskGuid, activityGuid, taskGuid).getResult();
            if (resultList != null && resultList.size() > 0) {
                for (AuditTaskRiskpoint databean : resultList) {
                    // 去除本行的岗位
                    if (!activityGuid.equals(databean.getActivityguid())) {
                        SelectItem si = new SelectItem();
                        si.setValue(databean.getActivityguid());
                        si.setText(databean.getActivityname());
                        silist.add(si);
                    }
                }
                List<WorkflowActivity> activitylist = wfaservice.selectAllByProcessVersionGuid(processversionguid,
                        " and activityName = '结束'");
                if (activitylist != null && activitylist.size() > 0) {
                    silist.add(new SelectItem(activitylist.get(0).getActivityGuid(), "结束"));
                }
                silist.add(0, new SelectItem("", "---"));
                nextActivityModel = silist;
            }
        }
        return nextActivityModel;
    }

    /**
     * 
     * 点击提交按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void submit() {
        String msg = "";
        String syncTaskGuid = dataBeanTask.getRowguid();
        AuditCommonResult<String> result;
        if (StringUtil.isNotBlank(copyTaskGuid) && !operation.equals("copy") || operation.equals("confirmEdit")) {
            if (operation.equals("confirmEdit")) {
                result = handleTaskService.submitTask(true, taskGuid, dataBeanTask, dataBeanTaskExtension,
                        userSession.getDisplayName(), userSession.getUserGuid());
            }
            else {
            	boolean taskversion =true;
				String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
				if(ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)){
					taskversion=false;
				}
                int isNeedNewVersion = dataBeanTask.getIsneednewversion() == null ? 0
                        : dataBeanTask.getIsneednewversion();
                boolean flag = taskversion &&(isNeedNewVersion == 1
                        || auditTaskServie
                                .judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
                                        taskGuid, dataBeanTask.getRowguid(), dataBeanTask, dataBeanTaskExtension)
                                .getResult());
               
                result = handleTaskService.submitTask(flag, taskGuid, dataBeanTask, dataBeanTaskExtension,
                        userSession.getDisplayName(), userSession.getUserGuid());
                if (!flag) {
                    syncTaskGuid = taskGuid;
                }
            }
        }
        else {
            result = handleTaskService.submitNewTask(dataBeanTask, dataBeanTaskExtension, operation,
                    ZwfwUserSession.getInstance().getCenterGuid());
        }
        if (!result.isSystemCode()) {
            msg = "处理失败！";
        }
        else {
            msg = result.getResult();
            syncWindowTask("modify", syncTaskGuid);
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 点击上报按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void report() {
        String msg = "";
        // 判断事项编码是否正确
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            Map<Object, Object> map = handleTaskService
                    .judgeItemId(dataBeanTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
                            ZwfwUserSession.getInstance().getCenterGuid())
                    .getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                this.addCallbackParam("msg", msg);
                return;
            }
        }
        // 上报事项
        msg = handleTaskService.report(dataBeanTask, dataBeanTaskExtension).getResult();
        this.addCallbackParam("msg", msg);
    }

    public void setNextActivityModel(List<SelectItem> nextActivityModel) {
        this.nextActivityModel = nextActivityModel;
    }

    /**
     * 
     * 同步事项到窗口
     * 
     * @param SendType
     *            事项消息发送类型 消息类型有enable、insert、modify、delete
     * @param RabbitMQMsg
     *            事项id
     */
    public void syncWindowTask(String SendType, String taskGuid) {
        // TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
        // 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
        try {
/*            String RabbitMQMsg = "handleSerachIndex:" + SendType + ";" + taskGuid + "#" + "handleCenterTask:" + SendType
                    + ";" + taskGuid + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
            ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);*/
            String RabbitMQMsg = SendType + ";" + taskGuid ;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg, "task."+ZwfwUserSession.getInstance().getAreaCode()+"."+SendType);

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AuditTaskRiskpoint getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskRiskpoint();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskRiskpoint dataBean) {
        this.dataBean = dataBean;
    }

    public String update(AuditTaskRiskpoint auditTaskRiskpoint) {
        String msg = "";
        AuditCommonResult<String> addResult = auditTaskRiskpointimpl.updateAuditTaskRiskpoint(auditTaskRiskpoint);
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        return msg;
    }

    /**
     * 
     * 流程配置修改
     * 
     */
    public void handleWorkFlow(String processversionguid, String areaCode) {
        AuditTask tempAuditTask = new AuditTask();
        // 获取事项模板
        String temptaskguid = auditTaskBasicService.getTemplateTaskGuid(ZwfwUserSession.getInstance().getAreaCode())
                .getResult();
        if (StringUtil.isNotBlank(temptaskguid)) {
            tempAuditTask = auditTaskBasicService.getAuditTaskByGuid(temptaskguid, false).getResult();
        }
        // 模板pvguid
        String temprocessVersionGuid = tempAuditTask.getPvguid();
        // 定义相关数据
        // 将标准的审批工作流的数据插入到每个审批的工作流流程中
        auditTaskRiskpointBizlogic.generateContextList(temprocessVersionGuid, processversionguid);
        // 活动环节个数，按1个环节2个环节，和3个即以上的环节进行处理
        auditTaskRiskpointBizlogic.generateActivityList(processversionguid);
        // 再启用权力对应的processversion
        workflowProcessVersionService9.updateStatus(10, processversionguid);
    }

    /**
     * 
     * 修改
     * 
     * @param dataBean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String updateAuditTask(AuditTask dataBean) {
        String msg = "";
        dataBean.setOperatedate(new Date());
        AuditCommonResult<String> editResult = auditTaskBasicService.updateAuditTask(dataBean);
        // String m=editResult.getResult();
        if (!editResult.isSystemCode()) {
            msg = editResult.getSystemDescription();
        }

        else if (!editResult.isBusinessCode()) {
            msg = editResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        return msg;
    }
}

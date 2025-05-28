package com.epoint.zczwfw.audittask.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.EpointKeyNames9;
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
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.service.DeptSelfBuildSystemService;
import com.epoint.zczwfw.audittaskinquestsituation.api.IAuditTaskInquestsituationService;
import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;
import com.epoint.zhenggai.api.ZhenggaiService;

/**
 * 
 * 事项系统设置action
 * 
 * @author Administrator
 * @version 2022-04-18
 * 
 * 
 */
@RestController("zcaudittaskextendaddaction")
@Scope("request")
@SuppressWarnings("unchecked")
public class ZCAuditTaskExtendAddAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -2215014088842095485L;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    @Autowired
    private IAuditTaskMap auditTaskMapImpl;

    @Autowired
    private IAuditTaskDict auditTaskDictImpl;
    @Autowired
    private ZhenggaiService zhenggaiImpl;

    /**
     * 勘验事项情形API
     */
    @Autowired
    private IAuditTaskInquestsituationService iAuditTaskInquestsituationService;

    /**
     * 是否代码项
     */
    private List<SelectItem> yesornoModal;
    /**
     * 自建系统模式代码项
     */
    private List<SelectItem> ziJianModeModal;
    /**
     * 何时收费代码项
     */
    private List<SelectItem> chargeWhenModal;
    /**
     * 个人申请人分类下拉框
     */
    private List<SelectItem> applyerclassForPersonModal;
    /**
     * 针对个人的主题分类下拉框
     */
    private List<SelectItem> taskClassForPersonModal;
    /**
     * 针对企业的行业分类下拉框
     */
    private List<SelectItem> applyerclassForCompanyModal;
    /**
     * 针对企业的主题分类下拉框
     */
    private List<SelectItem> taskClassForCompanyModal;
    /**
     * 针对个人生命周期分类
     */
    private List<SelectItem> lifecycleForPersonModal;
    /**
     * 针对个人生命周期分类
     */
    private List<SelectItem> lifecycleForCompanyModal;

    /**
     * 是否显示在样单机
     */
    private List<SelectItem> is_show_sampleModel;

    /**
     * 收费账号下拉框
     */
    private List<SelectItem> feeAccountModel;

    /**
     * 事项
     */
    private AuditTask auditTask;
    /**
     * 事项guid
     */
    private String taskGuid;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 是否启用
     */
    private List<SelectItem> is_enableModel;
    /**
     * 是否走模拟流程
     */
    private List<SelectItem> is_simulationModel;
    /**
     * 是否走模拟流程
     */
    private List<SelectItem> lcModal;
    /**
     * 网上申报类型
     */
    private List<SelectItem> webapplytypeModel;
    /**
     * 是否支持预约办理
     */
    private List<SelectItem> reservationModel;
    /**
     * 是否支持网上支付
     */
    private List<SelectItem> onlinepaymentModel;
    /**
     * 是否支持物流快递
     */
    private List<SelectItem> logisticsexpressModel;
    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;
    private FileUploadModel9 pcFileUploadModel = null;
    private FileUploadModel9 mobileFileUploadModel = null;
    /**
     * 事项ID
     * 
     */
    private String taskId;
    /**
     * 操作
     */
    private String operation;
    /**
     * 拷贝taskGuid
     */
    private String copyTaskGuid;
    /**
     * 快递送达方式
     */
    private List<SelectItem> express_send_typeModel;

    /**
     * 实施主体性质
     */
    private List<SelectItem> subjectnatureModel;

    /**
     * 科室名称
     */
    private List<SelectItem> keshkinumModel;

    /**
     * 行使层级
     */
    private List<SelectItem> use_levelModel;

    /**
     * 物流快递内容
     */
    private List<SelectItem> express_contentModel;

    /**
     * 材料归属
     */
    private List<SelectItem> clgsModel;

    /**
     * 外部流程图标识
     */
    private String taskoutimgguid;
    private String pcspydguid;
    private String mobilespydguid;

    @Autowired
    private IAuditOrgaFeeaccount feeaccountService;

    @Autowired
    private IAttachService attachSerivce;

    @Autowired
    private ISendMQMessage sendMQMessageService;
    /**
     * 是否不弹出文书
     */
    private List<SelectItem> is_notopendocModel;

    @Autowired
    private IHandleTask handleTaskService;

    @Autowired
    private IAuditTask auditTaskServie;

    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    public DeptSelfBuildSystemService selfBuildSystemService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    public List<SelectItem> selfBuildSystemList;

    @Override
    public void pageLoad() {
        // 获取taskguid
        taskGuid = this.getRequestParameter("taskGuid");
        taskId = this.getRequestParameter("taskId");
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");
        // 获取操作
        operation = this.getRequestParameter("operation");
        if (StringUtil.isNotBlank(taskGuid)) {
            if (StringUtil.isBlank(copyTaskGuid)) {
                // 事项增加事项操作，从数据库获取
                auditTask = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
                auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false).getResult();
            }
            else {
                // 事项库变更的时候，需要从缓存里获取数据
                if ("windowCopy".equals(operation) || "copy".equals(operation)) {
                    auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                    auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid, false)
                            .getResult();
                }
                // 窗口事项复制和窗口事项变更的时候初始化从数据库读取
                else {
                    auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                    auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid, false)
                            .getResult();
                    // auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(copyTaskGuid,
                    // false);
                }
            }

            // 背景核验
            if (StringUtil.isBlank(auditTaskExtension.getStr("is_bgv"))) {
                auditTaskExtension.set("is_bgv", "0");
            }

            // 如果外部流程图已经上传且保存
            if (StringUtil.isNotBlank(auditTask.getTaskoutimgguid())) {
                taskoutimgguid = auditTask.getTaskoutimgguid();
                addViewData("taskoutimgguid", taskoutimgguid);
            }
            // 还未上传
            if (StringUtil.isBlank(getViewData("taskoutimgguid"))) {
                taskoutimgguid = UUID.randomUUID().toString();
                addViewData("taskoutimgguid", taskoutimgguid);
            }
            //pc端视频已上传
            if (StringUtil.isNotBlank(auditTaskExtension.get("pcspydguid"))) {
            	pcspydguid = auditTaskExtension.getStr("pcspydguid");
                addViewData("pcspydguid", pcspydguid);
            }
            // 还未上传
            if (StringUtil.isBlank(getViewData("pcspydguid"))) {
            	pcspydguid = UUID.randomUUID().toString();
                addViewData("pcspydguid", pcspydguid);
            }
            //移动端视频已上传
            if (StringUtil.isNotBlank(auditTaskExtension.get("mobilespydguid"))) {
            	mobilespydguid = auditTaskExtension.getStr("mobilespydguid");
                addViewData("mobilespydguid", mobilespydguid);
            }
            // 还未上传
            if (StringUtil.isBlank(getViewData("mobilespydguid"))) {
            	mobilespydguid = UUID.randomUUID().toString();
                addViewData("mobilespydguid", mobilespydguid);
            }

            this.addCallbackParam("ouGuid", auditTask.getOuguid());
            this.addCallbackParam("taskid", auditTask.getTask_id());

            // 判断收费账号是否已删除，删除则不再显示
            if (StringUtil.isNotBlank(auditTask.getFeeaccountguid())) {
                AuditOrgaFeeaccount auditFeeaccount = feeaccountService
                        .getFeeaccountByRowguid(auditTask.getFeeaccountguid()).getResult();
                if (auditFeeaccount == null) {
                    auditTask.setFeeaccountguid("");
                }
            }
            // 判断部门是否改变，若改变，则收费账号随之改变
            List<Object> list = new ArrayList<Object>();
            getFeeAccountModal();
            for (SelectItem fee : feeAccountModel) {
                list.add(fee.getValue());
            }
            if (!list.contains(auditTask.getFeeaccountguid())) {
                auditTask.setFeeaccountguid("");
            }

        }
        init();

        // ************开始**************
        // add by yrchan,2022-04-18,勘验事项情形， 查询勘验事项情形数据
        if (StringUtil.isBlank(auditTaskExtension.getStr("is_inquest"))) {
            auditTaskExtension.set("is_inquest", ZwfwConstant.CONSTANT_STR_ZERO);
        }
        List<AuditTaskInquestsituation> situationnameList = null;
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            situationnameList = iAuditTaskInquestsituationService
                    .listAuditTaskInquestsituationByTaskGuid("situationname", copyTaskGuid);
        }
        else {
            situationnameList = iAuditTaskInquestsituationService
                    .listAuditTaskInquestsituationByTaskGuid("situationname", taskGuid);
        }
        StringBuilder situationNames = new StringBuilder();
        if (situationnameList != null && !situationnameList.isEmpty()) {
            for (AuditTaskInquestsituation auditTaskInquestsituation : situationnameList) {
                situationNames.append(auditTaskInquestsituation.getSituationname()).append(";");
            }
        }
        addCallbackParam("taskinquestname", situationNames.toString());
        // ************结束**************

    }

    private void init() {
        AuditTask Task = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
        if (StringUtil.isNotBlank(Task)) {
            auditTask.set("charge_lc", Task.get("charge_lc"));
            auditTask.set("unid", Task.get("unid"));
        }

        String key = "selfBuildSystemGuid";
        String val = "";
        if (StringUtil.isBlank(val = getViewData(key))) {
            DeptSelfBuildSystem selfBuildSystem = selfBuildSystemService
                    .findDeptSelfBuildSystemByTaskId(auditTask.getTask_id());
            if (selfBuildSystem != null) {
                addViewData(key, val = selfBuildSystem.getRowguid());
                auditTask.set("selfBuildSystemGuid", val);
            }
        }
        else {
            auditTask.set("selfBuildSystemGuid", val);
        }

        try {
            String key2 = "qssxjhm";
            String val2 = "";
            if (StringUtil.isBlank(val2 = getViewData(key2))) {
                AuditTaskExtension taskExtension = getTaskExtensionByTaskGuid(taskGuid);
                addViewData(key2, val2 = taskExtension.getStr("qssxjhm"));
                this.auditTaskExtension.set("qssxjhm", val2);
            }
            else {
                this.auditTaskExtension.set("qssxjhm", val2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击提交按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void submit() {
        String msg = "";
        String syncTaskGuid = auditTask.getRowguid();
        AuditCommonResult<String> result;
        // 外部流程图
        if (StringUtil.isNotBlank(getViewData("taskoutimgguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("taskoutimgguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
                auditTask.setTaskoutimgguid(getViewData("taskoutimgguid"));
            }
            else {
                auditTask.setTaskoutimgguid(null);
            }
        }
        // PC端视频
        if (StringUtil.isNotBlank(getViewData("pcspydguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("pcspydguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
            	auditTaskExtension.set("pcspydguid",getViewData("pcspydguid"));
            }
            else {
            	auditTaskExtension.set("pcspydguid",null);
            }
        }
        // 移动端视频
        if (StringUtil.isNotBlank(getViewData("mobilespydguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("mobilespydguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
            	auditTaskExtension.set("mobilespydguid",getViewData("mobilespydguid"));
            }
            else {
            	auditTaskExtension.set("mobilespydguid",null);
            }
        }

        if (StringUtil.isNotBlank(auditTaskExtension.getStr("keshinum"))) {
            auditTaskExtension.set("keshiname",
                    iCodeItemsService.getItemTextByCodeName("科室名称", auditTaskExtension.getStr("keshinum")));
        }
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            if ("confirmEdit".equals(operation)) {
                result = handleTaskService.submitTask(true, taskGuid, auditTask, auditTaskExtension,
                        userSession.getDisplayName(), userSession.getUserGuid());
            }
            else {
                boolean taskversion = true;
                String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)) {
                    taskversion = false;
                }
                int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
                // *************开始***************
                // add by yrchan,2022-04-22,是否勘验事项，isInquest，是否生成新版本
                boolean isInquest = false;
                if (StringUtil.isNotBlank(copyTaskGuid)) {
                    AuditTaskExtension oldExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false)
                            .getResult();
                    if (oldExtension != null && auditTaskExtension != null) {
                        String oldIsinquest = StringUtil.isBlank(oldExtension.getStr("is_inquest")) ? "0"
                                : oldExtension.getStr("is_inquest");
                        String newIsinquest = StringUtil.isBlank(auditTaskExtension.getStr("is_inquest")) ? "0"
                                : auditTaskExtension.getStr("is_inquest");
                        if (!oldIsinquest.equals(newIsinquest)) {
                            isInquest = true;
                        }
                    }
                }
                // *************结束***************

                /*boolean flag = taskversion
                        && (isInquest || isNeedNewVersion == 1
                                || auditTaskServie.judgeNewVersionByBean(
                                        EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG), taskGuid,
                                        auditTask.getRowguid(), auditTask, auditTaskExtension).getResult());*/

                boolean flag = true;
                result = handleTaskService.submitTask(true, taskGuid, auditTask, auditTaskExtension,
                        userSession.getDisplayName(), userSession.getUserGuid());

                if (!flag) {
                    syncTaskGuid = taskGuid;
                }
            }
        }
        else {
            // 新增页面提交操作
            result = handleTaskService.submitNewTask(auditTask, auditTaskExtension, operation,
                    ZwfwUserSession.getInstance().getCenterGuid());
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
     * 下一步操作按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addNew() {
        String msg = "";
        // 设置事项扩展信息
        auditTask.setOperatedate(new Date());
        auditTaskExtension.setOperatedate(new Date());
        setChargeAndZjMode(auditTask, auditTaskExtension);
        // 外部流程图
        if (StringUtil.isNotBlank(getViewData("taskoutimgguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("taskoutimgguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
                auditTask.setTaskoutimgguid(getViewData("taskoutimgguid"));
            }
            else {
                auditTask.setTaskoutimgguid(null);
            }
        }
        // PC端视频
        if (StringUtil.isNotBlank(getViewData("pcspydguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("pcspydguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
            	auditTaskExtension.set("pcspydguid",getViewData("pcspydguid"));
            }
            else {
            	auditTaskExtension.set("pcspydguid",null);
            }
        }
        // 移动端视频
        if (StringUtil.isNotBlank(getViewData("mobilespydguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("mobilespydguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
            	auditTaskExtension.set("mobilespydguid",getViewData("mobilespydguid"));
            }
            else {
            	auditTaskExtension.set("mobilespydguid",null);
            }
        }
        // 保存事项和事项扩展信息
        if (StringUtil.isBlank(copyTaskGuid)) {
            // 窗口新增事项，点击系统设置自动把状态设置为待上报
            if ("windowAdd".equals(operation) || "windowEdit".equals(operation)
                    || "windowReportEdit".equals(operation)) {
                // 设置事项状态为待上报
                auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_DSB));
            }
            auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
            msg = "保存成功";
        }
        else {
            if ("windowAdd".equals(operation) || "windowEdit".equals(operation)
                    || "windowReportEdit".equals(operation)) {
                // 设置事项状态为待上报
                auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_DSB));
                auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
            }
            else if ("windowCopy".equals(operation) || operation.contains("copy")) {
                // 把数据更新到数据库
                auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
            }
            else {
                // 把数据更新到数据库
                auditTaskExtensionImpl.updateAuditTaskAndExt(auditTask, auditTaskExtension, false);
            }
            msg = "保存成功";
        }
        this.addCallbackParam("msg", msg);
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
        AuditCommonResult<String> result;
        String syncTaskGuid = auditTask.getRowguid();
        int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
        boolean flag = isNeedNewVersion == 1
                || auditTaskServie.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
                        taskGuid, auditTask.getRowguid(), auditTask, auditTaskExtension).getResult();
        if (!flag) {
            syncTaskGuid = taskGuid;
        }
        if (StringUtil.isNotBlank(auditTask.getVersion())) {
            result = handleTaskService.passTask(flag, taskGuid, taskId, auditTask, auditTaskExtension,
                    userSession.getDisplayName(), userSession.getUserGuid());
        }
        else {
            result = handleTaskService.passTask(flag, taskGuid, taskId, auditTask, auditTaskExtension,
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
        msg = handleTaskService.notpassAuditTask(auditTask.getRowguid()).getResult();
        this.addCallbackParam("msg", msg);
    }

    /**
     * 点击上报按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void report() {
        String msg = "";
        if (StringUtil.isNotBlank(getViewData("taskoutimgguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("taskoutimgguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
                auditTask.setTaskoutimgguid(getViewData("taskoutimgguid"));
            }
            else {
                auditTask.setTaskoutimgguid(null);
            }
        }
        // PC端视频
        if (StringUtil.isNotBlank(getViewData("pcspydguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("pcspydguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
            	auditTaskExtension.set("pcspydguid",getViewData("pcspydguid"));
            }
            else {
            	auditTaskExtension.set("pcspydguid",null);
            }
        }
        // 移动端视频
        if (StringUtil.isNotBlank(getViewData("mobilespydguid"))) {
            List<FrameAttachInfo> frameAttachInfoList = attachSerivce
                    .getAttachInfoListByGuid(getViewData("mobilespydguid"));
            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {// 这边必须加上判断，有些情况是上传了之后又被删除了，但业务guid还存在缓存中
            	auditTaskExtension.set("mobilespydguid",getViewData("mobilespydguid"));
            }
            else {
            	auditTaskExtension.set("mobilespydguid",null);
            }
        }
        // 判断事项编码是否正确
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            Map<Object, Object> map = handleTaskService
                    .judgeItemId(auditTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
                            ZwfwUserSession.getInstance().getCenterGuid())
                    .getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                this.addCallbackParam("msg", msg);
                return;
            }
        }
        // 上报事项
        msg = handleTaskService.report(auditTask, auditTaskExtension).getResult();
        this.addCallbackParam("msg", msg);
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
            /*
             * String RabbitMQMsg = "handleSerachIndex:" + SendType + ";" +
             * taskGuid + "#" + "handleCenterTask:" + SendType + ";" + taskGuid
             * + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
             * ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg,
             * true);
             */
            String RabbitMQMsg = SendType + ";" + taskGuid;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                    "task." + ZwfwUserSession.getInstance().getAreaCode() + "." + SendType);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * add by yrchan,2022-04-18,勘验事项情形， 删除勘验事项情形数据
     */
    public void deleteInquest() {
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            iAuditTaskInquestsituationService.deleteByTaskGuid(copyTaskGuid);
        }
        else {
            iAuditTaskInquestsituationService.deleteByTaskGuid(taskGuid);
        }
    }

    /**
     * add by yrchan,2022-04-18,勘验事项情形， 查询勘验事项情形数据
     */
    public void inquestChangedInquest() {
        // ************开始**************
        // add by yrchan,2022-04-18,勘验事项情形， 查询勘验事项情形数据
        List<AuditTaskInquestsituation> situationnameList = null;
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            situationnameList = iAuditTaskInquestsituationService
                    .listAuditTaskInquestsituationByTaskGuid("situationname", copyTaskGuid);
        }
        else {
            situationnameList = iAuditTaskInquestsituationService
                    .listAuditTaskInquestsituationByTaskGuid("situationname", taskGuid);
        }
        StringBuilder situationNames = new StringBuilder();
        if (situationnameList != null && !situationnameList.isEmpty()) {
            for (AuditTaskInquestsituation auditTaskInquestsituation : situationnameList) {
                situationNames.append(auditTaskInquestsituation.getSituationname()).append(";");
            }
        }
    }

    public AuditTaskExtension getAuditTaskExtension() {
        if (auditTaskExtension == null) {
            auditTaskExtension = new AuditTaskExtension();
        }
        return auditTaskExtension;
    }

    public void setAuditTaskExtension(AuditTaskExtension auditTaskExtension) {
        this.auditTaskExtension = auditTaskExtension;
    }

    public AuditTask getAuditTask() {
        if (auditTask == null) {
            auditTask = new AuditTask();
        }
        return auditTask;
    }

    public void setAuditTask(AuditTask auditTask) {
        this.auditTask = auditTask;
    }

    public List<SelectItem> getIs_enableModel() {
        if (is_enableModel == null) {
            is_enableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_enableModel;
    }

    public List<SelectItem> getIs_simulationModel() {
        if (is_simulationModel == null) {
            is_simulationModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_simulationModel;
    }

    public List<SelectItem> getWebapplytypeModel() {
        if (webapplytypeModel == null) {
            webapplytypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "网上申报类型", null, false));
        }
        return this.webapplytypeModel;
    }

    public List<SelectItem> getYesornoModal() {
        if (yesornoModal == null) {
            yesornoModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.yesornoModal;
    }

    public List<SelectItem> getlcModal() {
        if (lcModal == null) {
            lcModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.lcModal;
    }

    public List<SelectItem> getZiJianModeModal() {
        if (ziJianModeModal == null) {
            ziJianModeModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "自建系统模式", null, false));
        }
        return this.ziJianModeModal;
    }

    public List<SelectItem> getChargeWhenModal() {
        if (chargeWhenModal == null) {
            chargeWhenModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "何时收费", null, false));
        }
        return this.chargeWhenModal;
    }

    public List<SelectItem> getApplyerclassForPersonModal() {
        if (applyerclassForPersonModal == null) {
            applyerclassForPersonModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对个人的申请人分类", null, false));
        }
        return this.applyerclassForPersonModal;
    }

    public List<SelectItem> getTaskClassForPersonModal() {
        if (taskClassForPersonModal == null) {
            taskClassForPersonModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对个人的事项主题分类", null, false));
        }
        return this.taskClassForPersonModal;
    }

    public List<SelectItem> getApplyerclassForCompanyModal() {
        if (applyerclassForCompanyModal == null) {
            applyerclassForCompanyModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对企业的行业分类", null, false));
        }
        return this.applyerclassForCompanyModal;
    }

    public List<SelectItem> getTaskClassForCompanyModal() {
        if (taskClassForCompanyModal == null) {
            taskClassForCompanyModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对企业的事项主题分类", null, false));
        }
        return this.taskClassForCompanyModal;
    }

    public List<SelectItem> getLifecycleForPersonModal() {
        if (lifecycleForPersonModal == null) {
            lifecycleForPersonModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对个人生命周期分类", null, false));
        }
        return this.lifecycleForPersonModal;
    }

    public List<SelectItem> getLifecycleForCompanyModal() {
        if (lifecycleForCompanyModal == null) {
            lifecycleForCompanyModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对企业生命周期分类", null, false));
        }
        return this.lifecycleForCompanyModal;
    }

    public List<SelectItem> getReservationModel() {
        if (reservationModel == null) {
            reservationModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "是否", null, false));
        }
        return reservationModel;
    }

    public List<SelectItem> getIs_show_sampleModel() {
        if (is_show_sampleModel == null) {
            is_show_sampleModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "是否", null, false));
        }
        return is_show_sampleModel;
    }

    public List<SelectItem> getOnlinepaymentModel() {
        if (onlinepaymentModel == null) {
            onlinepaymentModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "是否", null, false));
        }
        return onlinepaymentModel;
    }

    public List<SelectItem> getLogisticsexpressModel() {
        if (logisticsexpressModel == null) {
            logisticsexpressModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "是否", null, false));
        }
        return logisticsexpressModel;
    }

    /*
     * public List<SelectItem> getGetFeeAccountModal() { getFeeAccountModal =
     * new ArrayList<>(); if (feeAccountModal == null) { List<AuditFeeaccount>
     * auditFeeaccounts = auditFeeaccountService.getAllFeeaccount(ouguid); if
     * (auditFeeaccounts != null && auditFeeaccounts.size() > 0) { for
     * (AuditFeeaccount auditFeeaccount : auditFeeaccounts) {
     * getFeeAccountModalf .add(new SelectItem(auditFeeaccount.getRowguid(),
     * auditFeeaccount.getAccountnumber())); } } } return getFeeAccountModal; }
     */

    public List<SelectItem> getExpress_send_typeModel() {
        if (express_send_typeModel == null) {
            express_send_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "快递送达方式", null, false));
        }
        return this.express_send_typeModel;
    }

    public List<SelectItem> getSubjectnatureModel() {
        if (subjectnatureModel == null) {
            subjectnatureModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "实施主体性质", null, false));
        }
        return this.subjectnatureModel;
    }

    public List<SelectItem> getKeshinumModel() {
        if (keshkinumModel == null) {
            keshkinumModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "科室名称", null, false));
        }
        return this.keshkinumModel;
    }

    public List<SelectItem> getUse_levelModel() {
        if (use_levelModel == null) {
            use_levelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行使层级", null, false));
        }
        return this.use_levelModel;
    }

    public List<SelectItem> getExpress_contentModel() {
        if (express_contentModel == null) {
            express_contentModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "物流快递内容", null, false));
        }
        return this.express_contentModel;
    }

    /**
     * 类别
     * 
     * @param taskid
     */
    public void listTaskMap(String taskid) {

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

    public List<SelectItem> getFeeAccountModal() {
        if (feeAccountModel == null) {
            feeAccountModel = new ArrayList<>();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("ouguid", auditTask.getOuguid());
            List<AuditOrgaFeeaccount> auditOrgaFeeaccounts = feeaccountService.getFeeaccountList(sql.getMap())
                    .getResult();
            if (auditOrgaFeeaccounts != null) {
                for (AuditOrgaFeeaccount auditOrgaFeeaccount : auditOrgaFeeaccounts) {
                    SelectItem sel = new SelectItem(auditOrgaFeeaccount.getRowguid(),
                            auditOrgaFeeaccount.getAccountname());
                    feeAccountModel.add(sel);
                }
            }
        }
        return this.feeAccountModel;
    }

    public List<SelectItem> getIs_notopendocModel() {
        if (is_notopendocModel == null) {
            is_notopendocModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "文书类型", null, false));
        }
        return this.is_notopendocModel;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("taskoutimgguid"),
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }
    public FileUploadModel9 getPcFileUploadModel() {
        if (pcFileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                	pcFileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            pcFileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("pcspydguid"),
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return pcFileUploadModel;
    }
    public FileUploadModel9 getMobileFileUploadModel() {
        if (mobileFileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                	mobileFileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            mobileFileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("mobilespydguid"),
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return mobileFileUploadModel;
    }

    /**
     * 如果收费为否，那么何时收费等就为空，如果是否自建为否，那么自建模式为空
     * 
     * @param auditTask
     *            事项
     * @param auditTaskExtension
     *            事项扩展信息
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void setChargeAndZjMode(AuditTask auditTask, AuditTaskExtension auditTaskExtension) {
        // 是否收费：否
        if (auditTask.getCharge_flag() == null || ZwfwConstant.CONSTANT_INT_ZERO == auditTask.getCharge_flag()) {
            auditTaskExtension.setIs_feishui(null); // 是否非税
            auditTaskExtension.setCharge_when(null);// 何时收费：受理前
            auditTask.setCharge_standard(null);// 收费标准
            auditTask.setCharge_basis(null); // 收费依据
            auditTask.setFeeaccountguid(null);// 收费账号
        }
        // 是否自建模式
        if (auditTaskExtension.getIszijianxitong() == null
                || ZwfwConstant.CONSTANT_INT_ZERO == auditTaskExtension.getIszijianxitong()) {
            auditTaskExtension.setZijian_mode(null);
        }
    }

    public List<SelectItem> getSelfBuildSystemList() {
        if (selfBuildSystemList == null) {
            selfBuildSystemList = selfBuildSystemService.getSelfBuildSystemSIList();
        }
        return selfBuildSystemList;
    }

    private AuditTaskExtension getTaskExtensionByTaskGuid(String taskGuid) {
        AuditTaskExtension ext = zhenggaiImpl.getAuditExtbytaskguid(taskGuid);
        return ext;
    }

    public List<SelectItem> getClgsModel() {
        if (is_notopendocModel == null) {
            is_notopendocModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "材料归属", null, true));
        }
        return this.is_notopendocModel;
    }
}

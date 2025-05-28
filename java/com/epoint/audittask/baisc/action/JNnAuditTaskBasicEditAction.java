package com.epoint.audittask.baisc.action;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.hottask.inter.IAuditTaskHottask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.jnzwfw.jntask.api.IJnTaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 事项基本信息action
 *
 * @author Administrator
 * @version [版本号, 2016年10月8日]
 */
@RestController("jnnaudittaskbasiceditaction")
@Scope("request")
public class JNnAuditTaskBasicEditAction extends BaseController {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *
     */
    private static final long serialVersionUID = -2215014088842095485L;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    private FileUploadModel9 finishproductsamplesmodel;

    private FileUploadModel9 turnattachguidmodel;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Autowired
    private IAuditTaskHottask iAuditTaskHottask;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IJnTaskService service;

    @Autowired
    private ICodeItemsService codeitems;
    /**
     * 事项
     */
    private AuditTask dataBeanTask;
    private String turnattachguidtemp;
    /**
     * 事项guid
     */
    private String taskGuid;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension dataBeanTaskExtension;
    /**
     * 事项类型代码项
     */
    private List<SelectItem> auditTaskTypeModal;
    /**
     * 申请人类型代码项
     */
    private List<SelectItem> applyertypeModal;
    /**
     * 网厅电子表单
     */
    private List<SelectItem> projectFormModel;
    /**
     * 网厅电子表单
     */
    private List<SelectItem> iswtShowFormModel;
    /**
     * 是否代码项
     */
    private List<SelectItem> yesornoModal;
    /**
     * 审批类别代码项
     */
    private List<SelectItem> shenpilbModel;

    private List<SelectItem> trunTypeModel;

    /**
     * 服务方式
     */
    private List<SelectItem> fwfsModel;

    /**
     * 服务类别
     */
    private List<SelectItem> fwlyModel;

    /**
     * 服务类别
     */
    private List<SelectItem> persontypeModel;
    /**
     * 服务类别
     */
    private List<SelectItem> lisencttypeModel;

    /**
     * 即办件流转模式
     */
    private List<SelectItem> jbjModel;
    /**
     * 是否允许上报
     */
    private List<SelectItem> sbjModel;
    /**
     * 网厅主题
     */
    private TreeModel getWtztModel = null;
    /**
     * 部门service
     */
    @Autowired
    private IOuService frameOuService9;
    /**
     * 事项id
     */
    private String taskId;

    private String turnattachguid;
    /**
     * 上传附件标识
     */
    private String finishproductsamplestemp;
    /**
     * 新上传附件标识
     */
    private String finishproductsamples;

    /**
     * 操作
     */
    private String operation;

    private String itemidtemp;

    private String itemid;

    /**
     * 复制事项guid
     */
    private String copyTaskGuid;

    @Autowired
    private IHandleTask handleTaskService;

    @Autowired
    private IHandleConfig handleConfigService;

    /**
     * 国标事项类型
     */
    private List<SelectItem> sxlxModel;
    /**
     * 国标权力来源
     */
    private List<SelectItem> qllyModel;
    /**
     * 国标服务对象
     */
    private List<SelectItem> fwdxModel;
    /**
     * 国标通办范围
     */
    private List<SelectItem> tbfwModel;
    /**
     * 国标办理形式
     */
    private List<SelectItem> blxsModel;
    /**
     * 国标时限单位
     */
    private List<SelectItem> sxdwModel;
    /**
     * 国标期限单位
     */
    private List<SelectItem> qxdwModel;

    /**
     * 国标业务情形
     */
    private List<SelectItem> ywqxModel;

    /**
     * 办理流程附件标识
     */
    private String templateguid;
    /**
     * 原办理流程附件标识
     */
    private String templateattachguid;

    /**
     * 办理流程附件model
     */
    private FileUploadModel9 templateAttachUploadModel;


    @Override
    public void pageLoad() {
        // 获取taskguid
        taskGuid = getRequestParameter("taskGuid");
        taskId = getRequestParameter("taskId");
        operation = getRequestParameter("operation");
        copyTaskGuid = getRequestParameter("copyTaskGuid");
        String taskoutInfo = "";
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            dataBeanTask = auditTaskBasicImpl.getAuditTaskByGuid(copyTaskGuid, false).getResult();
            dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid, false).getResult();
            if(dataBeanTask!=null){
                itemid = dataBeanTask.getItem_id();
            }
            
            // auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(copyTaskGuid,
            // false);
        } else if (StringUtil.isNotBlank(taskGuid)) {
            dataBeanTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
            if(dataBeanTask!=null){
                itemidtemp = dataBeanTask.getItem_id();
                itemid = dataBeanTask.getItem_id();
            }

            dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false).getResult();
            
        }
        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())
                || ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
            addCallbackParam("xz", "true");
        }

        if (StringUtil.isNotBlank(taskGuid)) {
            // 编辑页面部门显示
            if(dataBeanTask!=null){
                addCallbackParam("ouname", frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
                addCallbackParam("ouguid", dataBeanTask.getOuguid());
                addCallbackParam("taskoutInfo", taskoutInfo);
                addCallbackParam("is_editafterimport", dataBeanTask.getIs_editafterimport());
                addCallbackParam("tasksource", dataBeanTask.getTasksource());
                addCallbackParam("projectform", dataBeanTask.getStr("projectform"));
                addCallbackParam("iswtshow", dataBeanTask.getStr("iswtshow"));
            }
        }
        if(dataBeanTaskExtension!=null){
            finishproductsamplestemp = dataBeanTaskExtension.getFinishproductsamples();
            turnattachguidtemp = dataBeanTaskExtension.getStr("turnattachguid");
        }


        if (StringUtils.isNoneBlank(turnattachguidtemp)) {
            turnattachguid = turnattachguidtemp;
        } else {
            turnattachguid = UUID.randomUUID().toString();
        }
        // 原先是否存空白模板
        if (StringUtils.isNoneBlank(finishproductsamplestemp)) {
            finishproductsamples = finishproductsamplestemp;
        } else {
            finishproductsamples = UUID.randomUUID().toString();
        }
        if (!isPostback()) {
            addViewData("turnattachguid", turnattachguid);
            addViewData("finishproductsamples", finishproductsamples);
        }
        if (dataBeanTaskExtension!=null && StringUtil.isNotBlank(dataBeanTaskExtension.getStr("is_tssgs"))) {
            addCallbackParam("is_tssgs", dataBeanTaskExtension.getStr("is_tssgs"));
        }

        /*3.0新增逻辑*/
        if(dataBeanTask!=null){
            if (("1").equals(dataBeanTask.get("is_ggtask"))) {
                addCallbackParam("is_ggtask", "true");
            }
            templateattachguid = dataBeanTask.get("handle_flow");
        }

        //原先是否存空白模板
        if (StringUtils.isNoneBlank(templateattachguid)) {
            templateguid = templateattachguid;
        } else {
            templateguid = UUID.randomUUID().toString();
        }
        /*3.0新增逻辑结束*/

    }

    /**
     * 编辑页面点击下一步的操作
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void update() {
        String msg = "";
        if ("edit".equals(operation) && StringUtil.isNotBlank(itemid) && !itemid.equals(dataBeanTask.getItem_id())) {
            throw new ReadOnlyException("事项编码");
        }
        if (StringUtil.isNotBlank(dataBeanTask.getType()) && dataBeanTask.getType() == ZwfwConstant.CONSTANT_INT_ONE) {
log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
            dataBeanTask.setPromise_day(ZwfwConstant.CONSTANT_INT_ONE);
                            log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
        }
        if (StringUtil.isNotBlank(dataBeanTask.getItem_id()) && StringUtil.isNotBlank(itemidtemp)) {
            Map<Object, Object> map = handleTaskService
                    .judgeItemId(dataBeanTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
                            ZwfwUserSession.getInstance().getCenterGuid())
                    .getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("msg", msg);
                return;
            }
        }
        // 判断事项编码是否正确
        if ((StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid))
                && ZwfwConstant.TASKAUDIT_STATUS_DQR.equals(String.valueOf(dataBeanTask.getIs_editafterimport()))) {
            Map<Object, Object> map = handleTaskService
                    .judgeItemId(dataBeanTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
                            ZwfwUserSession.getInstance().getCenterGuid())
                    .getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("msg", msg);
                return;
            }
        }
        msg = "保存成功！";
        dataBeanTask.setOperatedate(new Date());
        dataBeanTask.setOperateusername(userSession.getDisplayName());
        dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
        setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
        dataBeanTaskExtension.setOperatedate(new Date());
        dataBeanTaskExtension.setOperateusername(userSession.getDisplayName());
        // 新增
        dataBeanTaskExtension.setFinishproductsamples(getViewData("finishproductsamples"));
        dataBeanTaskExtension.set("turnattachguid", getViewData("turnattachguid"));

        // 窗口点击下一步状态变成待上报
        if (operation.contains("window")
                && !ZwfwConstant.TASKAUDIT_STATUS_SHWTG.equals(String.valueOf(dataBeanTask.getIs_editafterimport()))) {
            dataBeanTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_DSB));
        }
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            // 窗口事项复制和窗口事项变更点击下一步直接对数据库操作
            if (operation.equals("windowCopy") || operation.contains("copy")) {
                dataBeanTaskExtension.setTaskadduserdisplayname(userSession.getDisplayName());
                dataBeanTaskExtension.setTaskadduserguid(userSession.getUserGuid());
                auditTaskExtensionImpl.updateAuditTaskAndExt(dataBeanTask, dataBeanTaskExtension, false);
            }
            // 需要版本对比的，就把新版本数据存入数据库
            else {
                // 点击下一步先只对数据库进行操作，更新复制过来的事项
                auditTaskExtensionImpl.updateAuditTaskAndExt(dataBeanTask, dataBeanTaskExtension, false);
            }
        } else {
            // 事项直接更新到数据库
            auditTaskExtensionImpl.updateAuditTaskAndExt(dataBeanTask, dataBeanTaskExtension, false);
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 审核通过操作
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void passAudit() {
        try {
            if ("edit".equals(operation) && StringUtil.isNotBlank(itemid)
                    && !itemid.equals(dataBeanTask.getItem_id())) {
                throw new ReadOnlyException("事项编码");
            }
            if (StringUtil.isNotBlank(dataBeanTask.getType())
                    && dataBeanTask.getType() == ZwfwConstant.CONSTANT_INT_ONE) {
log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
                dataBeanTask.setPromise_day(ZwfwConstant.CONSTANT_INT_ONE);
                            log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
            }
            EpointFrameDsManager.begin(null);
            dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
            String msg = "";
            setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
            AuditCommonResult<String> result;
            String syncTaskGuid = dataBeanTask.getRowguid();
            int isNeedNewVersion = dataBeanTask.getIsneednewversion() == null ? 0 : dataBeanTask.getIsneednewversion();
            boolean flag = isNeedNewVersion == 1
                    || auditTaskBasicImpl
                    .judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
                            taskGuid, dataBeanTask.getRowguid(), dataBeanTask, dataBeanTaskExtension)
                    .getResult();
            if (!flag) {
                syncTaskGuid = taskGuid;
            }
            if (StringUtil.isNotBlank(dataBeanTask.getVersion())) {
                result = handleTaskService.passTask(flag, taskGuid, taskId, dataBeanTask, dataBeanTaskExtension,
                        userSession.getDisplayName(), userSession.getUserGuid());
            } else {
                result = handleTaskService.passTask(flag, taskGuid, taskId, dataBeanTask, dataBeanTaskExtension,
                        userSession.getDisplayName(), userSession.getUserGuid());
            }
            EpointFrameDsManager.commit();
            if (!result.isSystemCode()) {
                msg = "处理失败！";
            } else {
                msg = result.getResult();
                syncWindowTask("modify", syncTaskGuid);
            }
            addCallbackParam("msg", msg);
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            log.info("========Exception信息========" + e.getMessage());
        }
    }

    /**
     * 审核不通过操作
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void notPassAudit() {
        dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
        String msg = "";
        setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
        // 调用存储过程审核通过
        msg = handleTaskService.notpassAuditTask(dataBeanTask.getRowguid()).getResult();
        addCallbackParam("msg", msg);
    }

    /**
     * 点击上报按钮触发，使事项的状态变成待审核
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void report() {
        String msg = "";
        if ("edit".equals(operation) && StringUtil.isNotBlank(itemid) && !itemid.equals(dataBeanTask.getItem_id())) {
            throw new ReadOnlyException("事项编码");
        }
        if (StringUtil.isNotBlank(dataBeanTask.getType()) && dataBeanTask.getType() == ZwfwConstant.CONSTANT_INT_ONE) {
log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
            dataBeanTask.setPromise_day(ZwfwConstant.CONSTANT_INT_ONE);
                            log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
        }
        // 判断事项编码是否正确
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            Map<Object, Object> map = handleTaskService
                    .judgeItemId(dataBeanTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
                            ZwfwUserSession.getInstance().getCenterGuid())
                    .getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("msg", msg);
                return;
            }
        }
        // 更新事项状态
        dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
        setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
        msg = handleTaskService.report(dataBeanTask, dataBeanTaskExtension).getResult();
        addCallbackParam("msg", msg);
    }

    /**
     * 点击提交按钮触发
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void submit() {
        try {
            if ("edit".equals(operation) && StringUtil.isNotBlank(itemid)
                    && !itemid.equals(dataBeanTask.getItem_id())) {
                throw new ReadOnlyException("事项编码");
            }
            if (StringUtil.isNotBlank(dataBeanTask.getType())
                    && dataBeanTask.getType() == ZwfwConstant.CONSTANT_INT_ONE) {
log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
                dataBeanTask.setPromise_day(ZwfwConstant.CONSTANT_INT_ONE);
                            log.info("update promise_day-task:"+dataBeanTask.getItem_id()+","+dataBeanTask.getPromise_day()+",user:"+userSession.getUserGuid());
            }
            EpointFrameDsManager.begin(null);
            dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
            dataBeanTaskExtension.setFinishproductsamples(getViewData("finishproductsamples"));
            dataBeanTaskExtension.set("turnattachguid", getViewData("turnattachguid"));
            String msg = "";
            String syncTaskGuid = dataBeanTask.getRowguid();
            AuditCommonResult<String> result;
            if (StringUtil.isNotBlank(copyTaskGuid) && !operation.equals("copy") || operation.equals("confirmEdit")) {
                if (operation.equals("confirmEdit")) {
                    result = handleTaskService.submitTask(true, taskGuid, dataBeanTask, dataBeanTaskExtension,
                            userSession.getDisplayName(), userSession.getUserGuid());
                } else {
                    boolean taskversion = true;
                    String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)) {
                        taskversion = false;
                    }
                    int isNeedNewVersion = dataBeanTask.getIsneednewversion() == null ? 0
                            : dataBeanTask.getIsneednewversion();
                    boolean flag = taskversion && (isNeedNewVersion == 1 || auditTaskBasicImpl
                            .judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
                                    taskGuid, dataBeanTask.getRowguid(), dataBeanTask, dataBeanTaskExtension)
                            .getResult());

                    result = handleTaskService.submitTask(flag, taskGuid, dataBeanTask, dataBeanTaskExtension,
                            userSession.getDisplayName(), userSession.getUserGuid());
                    updateHotTask(dataBeanTask);
                    if (!flag) {
                        syncTaskGuid = taskGuid;
                    }
                }
            }
            // 新增操作的时候第一次生成事项
            else {
                result = handleTaskService.submitNewTask(dataBeanTask, dataBeanTaskExtension, operation,
                        ZwfwUserSession.getInstance().getCenterGuid());
            }
            EpointFrameDsManager.commit();
            if (!result.isSystemCode()) {
                msg = "处理失败！";
            } else {
                msg = result.getResult();
                syncWindowTask("modify", syncTaskGuid);
            }
            addCallbackParam("msg", msg);
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            log.info("========Exception信息========" + e.getMessage());
        }
    }

    /**
     * 同步事项到窗口
     *
     * @param SendType    事项消息发送类型 消息类型有enable、insert、modify、delete
     * @param RabbitMQMsg 事项id
     */
    public void syncWindowTask(String SendType, String taskGuid) {
        // TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
        // 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
        try {
            String RabbitMQMsg = SendType + ";" + taskGuid;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                    "task." + ZwfwUserSession.getInstance().getAreaCode() + "." + SendType);
            String isStaticTask = handleConfigService.getFrameConfig("AS_isStaticTask", null).getResult();
            if (StringUtil.isNotBlank(isStaticTask) && ZwfwConstant.CONSTANT_STR_ONE.equals(isStaticTask)) {
                sendMQMessageService.sendByExchange("zwdt_exchange_handle", RabbitMQMsg,
                        "task." + ZwfwUserSession.getInstance().getAreaCode() + "." + SendType);
            }
        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
    }

    /**
     * 如果该事项已经加入到热门事项，则将对应的热门事项更新
     *
     * @param auditTask
     */
    public void updateHotTask(AuditTask auditTask) {
        AuditTaskHottask auditTaskHottask = iAuditTaskHottask.getDetailbyTaskID(auditTask.getTask_id()).getResult();
        if (auditTaskHottask != null) {
            auditTaskHottask.setTaskname(auditTask.getTaskname());
            auditTaskHottask.setOuname(auditTask.getOuname());
            auditTaskHottask.setEnable(auditTask.getIs_enable());
            auditTaskHottask.setApplyertype(auditTask.getApplyertype());
            auditTaskHottask.setTaskguid(auditTask.getRowguid());
            iAuditTaskHottask.updateHottak(auditTaskHottask, auditTaskHottask.getRowguid());
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getAuditTaskTypeModal() {
        if (auditTaskTypeModal == null) {
            auditTaskTypeModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "事项类型", null, false));
        }
        return this.auditTaskTypeModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getYesornoModal() {
        if (yesornoModal == null) {
            yesornoModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.yesornoModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getApplyertypeModal() {
        if (applyertypeModal == null) {
            applyertypeModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "申请人类型", null, false));
        }
        return this.applyertypeModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getShenpilbModel() {
        if (shenpilbModel == null) {
            shenpilbModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批类别", null, false));
        }
        return this.shenpilbModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getpersontypeModel() {
        if (persontypeModel == null) {
            persontypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对个人的事项主题分类", null, false));
        }
        return this.persontypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getlisencttypeModel() {
        if (lisencttypeModel == null) {
            lisencttypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "针对企业的事项主题分类", null, false));
        }
        return this.lisencttypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getProjectFormModel() {
        if (projectFormModel == null) {
            projectFormModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "网厅电子表单", null, false));
        }
        return this.projectFormModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIswtShowFormModel() {
        if (iswtShowFormModel == null) {
            iswtShowFormModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "是否", null, false));
        }
        return this.iswtShowFormModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getfwfsModel() {
        if (fwfsModel == null) {
            fwfsModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "服务方式", null, false));
        }
        return this.fwfsModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getfwlyModel() {
        if (fwlyModel == null) {
            fwlyModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "服务领域", null, false));
        }
        return this.fwlyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTrunTypeModel() {
        if (trunTypeModel == null) {
            trunTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("复选框组", "网厅跳转类型", null, false));
        }
        return trunTypeModel;
    }

    public List<SelectItem> getjbjmodeModal() {
        if (jbjModel == null) {
            jbjModel = new ArrayList<>();
            jbjModel.add(new SelectItem(ZwfwConstant.JBJMODE_SIMPLE, "简易模式（不启动流程）"));
            jbjModel.add(new SelectItem(ZwfwConstant.JBJMODE_STANDARD, "正常模式"));
        }
        return this.jbjModel;
    }

    public List<SelectItem> getsbjmodeModal() {
        if (sbjModel == null) {
            sbjModel = new ArrayList<>();
            sbjModel.add(new SelectItem(ZwfwConstant.CONSTANT_INT_ZERO, "否"));
            sbjModel.add(new SelectItem(ZwfwConstant.CONSTANT_INT_ONE, "是"));
        }
        return this.sbjModel;
    }

    /**
     * @return 网厅主题
     */
    public TreeModel getWtztModel() {

        if (getWtztModel == null) {
            getWtztModel = new TreeModel() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    String codeName = "网厅主题";
                    return twoLayerTreeByCodeName(treeNode, codeName);
                }

            };
        }
        return this.getWtztModel;
    }

    /**
     * 通过代码主项名字获取两层结构的树
     *
     * @param treeNode     树节点
     * @param codeItemName 代码主项名称
     * @return
     */
    public List<TreeNode> twoLayerTreeByCodeName(TreeNode treeNode, String codeName) {
        List<TreeNode> list = new ArrayList<>();

        TreeNode root = new TreeNode();

        root.setText("所有分类");
        root.setId(TreeFunction9.F9ROOT);
        root.setPid("-1");
        root.getColumns().put("cantChecked", true);
        root.setChecked(false);
        list.add(root);
        // 展开下一层节点
        root.setExpanded(true);
        root.setCkr(false);
        root.getColumns().put("cantChecked", true);

        List<CodeItems> themeList = codeitems.listCodeItemsByCodeName(codeName);
        if (themeList != null) {
            for (CodeItems theme : themeList) {
                if (theme.getItemValue().length() == 2) {
                    TreeNode themeNode = new TreeNode();
                    themeNode.setPid(root.getId());
                    themeNode.setText(theme.getItemText());
                    themeNode.setId(theme.getItemValue());
//                    themeNode.setLeaf(true);
                    themeNode.setChecked(false);
                    themeNode.setCkr(false);
                    themeNode.getColumns().put("cantChecked", true);
                    list.add(themeNode);
                }
                if (theme.getItemValue().length() == 4) {
                    TreeNode themeNode = new TreeNode();
                    themeNode.setPid(theme.getItemValue().substring(0, 2));
                    themeNode.setText(theme.getItemText());
                    themeNode.setId(theme.getItemValue());
                    themeNode.setLeaf(true);
                    list.add(themeNode);
                }
            }
        }

        return list;
    }

    /**
     * 如果收费为否，那么何时收费等就为空，如果是否自建为否，那么自建模式为空
     *
     * @param auditTask          事项
     * @param auditTaskExtension 事项扩展信息
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

    public FileUploadModel9 getFinishproductsamplesmodel() {
        if (finishproductsamplesmodel == null) {
            finishproductsamplesmodel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(getViewData("finishproductsamples"), null, null, null,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return finishproductsamplesmodel;
    }

    public FileUploadModel9 getTurnattachguidmodel() {
        if (turnattachguidmodel == null) {
            turnattachguidmodel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("turnattachguid"),
                    null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return turnattachguidmodel;
    }

    public void setAuditTaskTypeModal(List<SelectItem> auditTaskTypeModal) {
        this.auditTaskTypeModal = auditTaskTypeModal;
    }

    public void setApplyertypeModal(List<SelectItem> applyertypeModal) {
        this.applyertypeModal = applyertypeModal;
    }

    public AuditTask getDataBeanTask() {
        return dataBeanTask;
    }

    public String getTurnattachguid() {
        return turnattachguid;
    }

    public void setTurnattachguid(String turnattachguid) {
        this.turnattachguid = turnattachguid;
    }

    public void setDataBeanTask(AuditTask dataBeanTask) {
        this.dataBeanTask = dataBeanTask;
    }

    public AuditTaskExtension getDataBeanTaskExtension() {
        return dataBeanTaskExtension;
    }

    public void setDataBeanTaskExtension(AuditTaskExtension dataBeanTaskExtension) {
        this.dataBeanTaskExtension = dataBeanTaskExtension;
    }

    public String getFinishproductsamples() {
        return finishproductsamples;
    }

    public void setFinishproductsamples(String finishproductsamples) {
        this.finishproductsamples = finishproductsamples;
    }

    public FileUploadModel9 getTemplateAttachUploadModel() {
        if (templateAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    templateAttachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            templateAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(templateguid, null, null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return templateAttachUploadModel;
    }


    @SuppressWarnings("unchecked")
    public List<SelectItem> getGbsxlxModel() {
        if (sxlxModel == null) {
            sxlxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_事项类型", null, false));
        }
        return this.sxlxModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> QLLYModal() {
        if (qllyModel == null) {
            qllyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_权力来源", null, false));
        }
        return this.qllyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> FWDXModal() {
        if (fwdxModel == null) {
            fwdxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_服务对象", null, false));
            List<SelectItem> fwdxModelNew = new ArrayList<SelectItem>();
            if(dataBeanTask!=null){
                if (("20").equals(dataBeanTask.getApplyertype())) {
                    for (SelectItem selectItem : fwdxModel) {
                        if (("1").equals(selectItem.getValue())) {
                            fwdxModelNew.add(selectItem);
                        }

                    }
                    this.fwdxModel = fwdxModelNew;
                } else if (("10").equals(dataBeanTask.getApplyertype())) {
                    for (SelectItem selectItem : fwdxModel) {
                        if (!("1").equals(selectItem.getValue())) {
                            fwdxModelNew.add(selectItem);
                        }

                    }
                    this.fwdxModel = fwdxModelNew;
                }
            }
            
        }
        return this.fwdxModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> handleareaModal() {
        if (tbfwModel == null) {
            tbfwModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_通办范围", null, false));
        }
        return this.tbfwModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> handletypeModal() {
        if (blxsModel == null) {
            blxsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_办理形式", null, false));
        }
        return this.blxsModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> anticipatetypeModal() {
        if (sxdwModel == null) {
            sxdwModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_时限单位", null, false));
        }
        return this.sxdwModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getYwqxModel() {
        if (ywqxModel == null) {
            ywqxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_业务情形", null, false));
        }
        return this.ywqxModel;
    }


}

package com.epoint.audittask.baisc.action;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.workflow.service.common.entity.config.WorkflowProcess;
import com.epoint.workflow.service.common.entity.config.WorkflowProcessVersion;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;
import com.epoint.xmz.thirdreporteddata.auditggconfig.api.IAuditGgConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 事项基本信息action
 *
 * @author Administrator
 * @version [版本号, 2016年10月8日]
 */
@RestController("jnaudittaskbasicaddaction")
@Scope("request")
public class JnAuditTaskBasicAddAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = -2215014088842095485L;

    /**
     * 部门service
     */
    @Autowired
    private IOuService frameOuService9;

    @Autowired
    private IWorkflowProcessService wfapi;
    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    /**
     * 上传附件标识
     */
    private String finishproductsamples;

    public String getFinishproductsamples() {
        return finishproductsamples;
    }

    public void setFinishproductsamples(String finishproductsamples) {
        this.finishproductsamples = finishproductsamples;
    }

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
     * 事项类型代码项
     */
    private List<SelectItem> auditTaskTypeModal;
    /**
     * 申请人类型代码项
     */
    private List<SelectItem> applyertypeModal;
    /**
     * 是否代码项
     */
    private List<SelectItem> yesornoModal;
    /**
     * 审批类别代码项
     */
    private List<SelectItem> shenpilbModel;

    /**
     * 服务方式
     */
    private List<SelectItem> fwfsModel;

    /**
     * 服务类别
     */
    private List<SelectItem> fwlyModel;

    /**
     * 即办件流转模式
     */
    private List<SelectItem> jbjModel;
    /**
     * 是否允许上报
     */
    private List<SelectItem> sbjModel;

    /**
     * 事项id
     */
    private String taskId;
    /**
     * 操作
     */
    private String operation;
    /**
     * 部门guid
     */
    private String ouguid;

    private Integer ordernum;

    private FileUploadModel9 finishproductsamplesmodel;

    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;

    @Autowired
    private IHandleTask handleTaskService;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateImpl;

    //3.0新增
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

    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditGgConfigService iAuditGgConfigService;

    /**
     * 办理流程附件model
     */
    private FileUploadModel9 templateAttachUploadModel;

    /**
     * 办理流程附件标识
     */
    private String templateattachguid;
    //3.0新增结束

    @Override
    public void pageLoad() {
        // 获取taskguid
        taskGuid = this.getRequestParameter("taskGuid");
        taskId = this.getRequestParameter("taskId");
        operation = this.getRequestParameter("operation");
        ouguid = this.getRequestParameter("ouguid");
        if (!isPostback()) {
            addViewData("finishproductsamples", UUID.randomUUID().toString());
        }
        String areacode = "";
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel()) || ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
            addCallbackParam("xz", "true");
        }
        // 获取事项模板
        String temptaskguid = auditTaskBasicImpl.getTemplateTaskGuid(areacode).getResult();
        if (StringUtil.isBlank(temptaskguid)) {
            addCallbackParam("msg", "请先设置对应的模板事项！");
        } else {
            //事项里面新增和窗口人员新增执行
            if ("add".equals(operation) || "windowAdd".equals(operation)) {
                // 新增的情况下就直接给对象赋值
                if (auditTask == null) {
                    auditTask = new AuditTask();
                    auditTaskExtension = new AuditTaskExtension();
                    // 左侧选择部门名称，则为该部门的名称
                    if (StringUtil.isNotBlank(ouguid)) {
                        auditTask.setOuguid(ouguid);
                        auditTask.setOuname(frameOuService9.getOuByOuGuid(ouguid).getOuname());
                        this.addCallbackParam("ouname", frameOuService9.getOuByOuGuid(ouguid).getOuname());
                    }

                    // 左侧没有选部门
                    else {
                        // 获取操作人员所在窗口信息
                        AuditOrgaWindow auditwindow = auditOrgaWindowService
                                .getWindowByWindowGuid(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
                        if (auditwindow != null) {
                            // 默认事项所属部门设置为该操作人员所在窗口对应的部门的（BASEOUGUID）
                            String baseOUGuid = frameOuService9.getOuByOuGuid(auditwindow.getOuguid()).getBaseOuguid();
                            auditTask.setOuguid(baseOUGuid);
                            String ouname = frameOuService9.getOuByOuGuid(baseOUGuid).getOuname();
                            auditTask.setOuname(ouname);
                            this.addCallbackParam("ouname", ouname);
                            // 如果该人员没有窗口，则获取该人员所在部门
                        } else {
                            FrameOu ou = frameOuService9.getOuByOuGuid(userSession.getBaseOUGuid());
                            String ouname = "";
                            if ("windowAdd".equals(operation)) {
                                if (ou != null) {
                                    String baseOUGuid = frameOuService9.getOuByOuGuid(userSession.getBaseOUGuid())
                                            .getBaseOuguid();
                                    auditTask.setOuguid(baseOUGuid);
                                    ouname = frameOuService9.getOuByOuGuid(baseOUGuid).getOuname();
                                }
                            }
                            auditTask.setOuname(ouname);
                            this.addCallbackParam("ouname", ouname);
                        }
                    }
                    AuditTaskExtension tempExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(temptaskguid, false).getResult();
                    if (tempExtension != null) {
                        // 网上预审通过短信
                        auditTaskExtension.setNotify_ys(tempExtension.getNotify_ys());
                        // 网上预审不通过短信
                        auditTaskExtension.setNotify_nys(tempExtension.getNotify_nys());
                        // 审批通过短信
                        auditTaskExtension.setNotify_pz(tempExtension.getNotify_pz());
                        // 审批不通过短信
                        auditTaskExtension.setNotify_npz(tempExtension.getNotify_npz());
                        // 受理短信
                        auditTaskExtension.setNotify_sl(tempExtension.getNotify_sl());
                        // 不予受理短信
                        auditTaskExtension.setNotify_nsl(tempExtension.getNotify_nsl());
                        // 是否走模拟流程
                        auditTaskExtension.setIs_simulation(tempExtension.getIs_simulation());
                        // 是否允许网上申报
                        auditTaskExtension.setWebapplytype(tempExtension.getWebapplytype());
                    }
                    // 新增事项来源字段
                    if ("windowAdd".equals(operation)) {
                        auditTask.setTasksource(ZwfwConstant.TASKSOURCE_WINDOW);
                    } else if ("add".equals(operation)) {
                        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                            auditTask.setTasksource(ZwfwConstant.TASKSOURCE_WINDOW);
                        } else {
                            auditTask.setTasksource(ZwfwConstant.TASKSOURCE_CENTER);
                        }
                    }
                }

                if (StringUtil.isBlank(auditTaskExtension.get("handle_system"))) {
                    auditTaskExtension.set("handle_system", "1");
                }
                auditTask.set("anticipate_type", "1");// 法定时限单位
                auditTask.set("promise_type", "1");// 法定承诺期限单位
                if(!isPostback()){
                    addViewData("templateattachguid", UUID.randomUUID().toString());
                }
                String SXBSZNDZ = iAuditGgConfigService.getConfigValueByName("sxbszndz");
                if (StringUtil.isNotBlank(SXBSZNDZ)) {
                    auditTask.set("SXBSZNDZ", SXBSZNDZ + "?taskguid=" + taskGuid);
                }
                String SXZXSBDZ = iAuditGgConfigService.getConfigValueByName("sxzxsbdz");
                if (StringUtil.isNotBlank(SXZXSBDZ)) {
                    auditTask.set("SXZXSBDZ", SXZXSBDZ + "?taskguid=" + taskGuid);
                }
            }
        }

    }

    /**
     * 点击一下操作，一般来说是数据直接入库，同时状态改为草稿-1
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addNew() {
        String message = "";
        // 判断事项编码是否正确
        Map<Object, Object> map = handleTaskService.judgeItemId(auditTask.getItem_id(), "", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        message = (String) map.get("msg");
        if (StringUtil.isNotBlank(message)) {
            this.addCallbackParam("msg", message);
        } else {
            if (StringUtil.isBlank(auditTask.getRowguid())) {
                /*3.0新增逻辑*/
                // 查询orgcode自动赋值
                FrameOuExtendInfo raExtendInfo = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
                if (raExtendInfo != null) {
                    auditTask.setDeptcode(raExtendInfo.getStr("ORGCODE"));
                }

                templateattachguid = getViewData("templateattachguid");
                auditTask.set("handle_flow",templateattachguid);
                /*3.0新增逻辑*/
                auditTask.setRowguid(taskGuid);
                auditTask.setOperatedate(new Date());
                auditTask.setOperateusername(userSession.getDisplayName());
                auditTask.setOrdernum(ZwfwConstant.CONSTANT_INT_ZERO);
                // 初始化事项审核状态：草稿
                auditTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_CG));
                auditTask.setIs_enable(ZwfwConstant.CONSTANT_INT_ONE);
                auditTask.setIs_history(ZwfwConstant.CONSTANT_INT_ZERO);
                auditTask.setIstemplate(ZwfwConstant.CONSTANT_INT_ZERO);//非模板事项
                String areacode = "";
                if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                } else {
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                auditTask.setAreacode(areacode);//辖区编码
                auditTask.setCharge_flag(0);
                if (StringUtil.isNotBlank(auditTask.getOuguid())) {
                    String ouname = frameOuService9.getOuByOuGuid(auditTask.getOuguid()).getOuname();
                    auditTask.setOuname(ouname);
                }
                auditTaskExtension.setTaskadduserdisplayname(userSession.getDisplayName());
                auditTaskExtension.setTaskadduserguid(userSession.getUserGuid());
                auditTaskExtension.setOnlinepayment(ZwfwConstant.CONSTANT_STR_ZERO);
                auditTaskExtension.setReservationmanagement(ZwfwConstant.CONSTANT_STR_ZERO);
                auditTaskExtension.setIf_express(ZwfwConstant.CONSTANT_STR_ZERO);
                auditTaskExtension.setOnlinepayment(ZwfwConstant.CONSTANT_STR_ZERO);
                auditTaskExtension.setIf_jz_hall(ZwfwConstant.CONSTANT_STR_ZERO);
                finishproductsamples = getViewData("finishproductsamples");
                auditTaskExtension.setFinishproductsamples(finishproductsamples);
                // 生成taskid，相同版本的taskid一样
                auditTask.setTask_id(taskId);
                // 构建工作流服务
                // 创建一个流程
                WorkflowProcess workflowProcess = new WorkflowProcess();
                workflowProcess.setProcessName(auditTask.getTaskname());
                workflowProcess.setProcessGuid(UUID.randomUUID().toString());
                WorkflowProcessVersion version = wfapi.addWorkflowProcess(workflowProcess, userSession.getUserGuid(),
                        "");
                auditTask.setPvguid(version.getProcessVersionGuid());
                auditTask.setProcessguid(version.getProcessGuid());
                auditTask.setOrdernum(ordernum);
                // 初始化事项审核状态：草稿
                auditTask.setIs_editafterimport(Integer.valueOf(ZwfwConstant.TASKAUDIT_STATUS_CG));
                // 设置事项扩展信息
                auditTaskExtension.setRowguid(UUID.randomUUID().toString());
                auditTaskExtension.setTaskguid(taskGuid);
                auditTaskExtension.setOperateusername(userSession.getDisplayName());
                auditTaskExtension.setOperatedate(new Date());
                auditTaskExtension.setTaskadduserdisplayname(userSession.getDisplayName());
                auditTaskExtension.setTaskadduserguid(userSession.getUserGuid());
                auditTaskExtension.setIs_simulation(ZwfwConstant.CONSTANT_INT_ZERO);
                auditTaskExtension.setIsriskpoint(0);
                //保存事项和事项扩展信息  直接插入数据库不存入缓存
                //防止系统卡住，多点击了一次，导致报错，先判断库中是否已存在改标识的事项
                String msg = "";
                AuditTask audittask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
                if (audittask == null) {
                    msg = addAuditTaskAndExt(auditTask, auditTaskExtension);
                    addCallbackParam("msg", msg);
                } else {
                    msg = addAuditTaskAndExt(auditTask, auditTaskExtension);
                }
                addCallbackParam("msg", msg);
                auditTask = null;
                auditTaskExtension = null;
            }
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

    public void setAuditTaskTypeModal(List<SelectItem> auditTaskTypeModal) {
        this.auditTaskTypeModal = auditTaskTypeModal;
    }

    public void setApplyertypeModal(List<SelectItem> applyertypeModal) {
        this.applyertypeModal = applyertypeModal;
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

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public String addAuditTaskAndExt(AuditTask auditTask, AuditTaskExtension auditTaskExtension) {
        String msg = "";
        AuditCommonResult<String> addResult = auditTaskExtensionImpl.addAuditTaskAndExt(auditTask, auditTaskExtension);
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        } else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        } else {
            if (ZwfwUserSession.getInstance().getCitylevel() != null && (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel()))) {
                AuditTaskDelegate audittaskdelegate = new AuditTaskDelegate();
                audittaskdelegate.setRowguid(UUID.randomUUID().toString());
                audittaskdelegate.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
                audittaskdelegate.setTaskguid(UUID.randomUUID().toString());
                audittaskdelegate.setOuguid(auditTask.getOuguid());
                audittaskdelegate.setOuname(auditTask.getOuname());
                audittaskdelegate.setDelegatetype(ZwfwConstant.TASKDELEGATE_TYPE_XZFD);
                audittaskdelegate.setStatus(String.valueOf(ZwfwConstant.CONSTANT_INT_ONE));
                audittaskdelegate.setXzorder(0);
                audittaskdelegate.setTaskid(auditTask.getTask_id());
                audittaskdelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ONE);
                audittaskdelegate.setApplytime(auditTask.getTransact_time());
                auditTaskDelegateImpl.addAuditTaskDelegate(audittaskdelegate);
            } else if (ZwfwUserSession.getInstance().getCitylevel() != null && (ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel()))) {
                AuditTaskDelegate audittaskdelegate = new AuditTaskDelegate();
                audittaskdelegate.setRowguid(UUID.randomUUID().toString());
                audittaskdelegate.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
                audittaskdelegate.setTaskguid(UUID.randomUUID().toString());
                audittaskdelegate.setOuguid(auditTask.getOuguid());
                audittaskdelegate.setOuname(auditTask.getOuname());
                audittaskdelegate.setDelegatetype(ZwfwConstant.TASKDELEGATE_TYPE_CJFD);
                audittaskdelegate.setStatus(String.valueOf(ZwfwConstant.CONSTANT_INT_ONE));
                audittaskdelegate.setXzorder(0);
                audittaskdelegate.setTaskid(auditTask.getTask_id());
                audittaskdelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ONE);
                audittaskdelegate.setApplytime(auditTask.getTransact_time());
                auditTaskDelegateImpl.addAuditTaskDelegate(audittaskdelegate);
            }
            msg = "保存成功！";
        }
        return msg;
    }

    public FileUploadModel9 getFinishproductsamplesmodel() {
        if (finishproductsamplesmodel == null) {
            finishproductsamplesmodel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("finishproductsamples"), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return finishproductsamplesmodel;
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
            if (("20").equals(auditTask.getApplyertype())) {
                for (SelectItem selectItem : fwdxModel) {
                    if (("1").equals(selectItem.getValue())) {
                        fwdxModelNew.add(selectItem);
                    }

                }
                this.fwdxModel = fwdxModelNew;
            } else if (("10").equals(auditTask.getApplyertype())) {
                for (SelectItem selectItem : fwdxModel) {
                    if (!("1").equals(selectItem.getValue())) {
                        fwdxModelNew.add(selectItem);
                    }

                }
                this.fwdxModel = fwdxModelNew;
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

    public FileUploadModel9 getTemplateAttachUploadModel() {
        if (templateAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {
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
            templateAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("templateattachguid"), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return templateAttachUploadModel;
    }



}

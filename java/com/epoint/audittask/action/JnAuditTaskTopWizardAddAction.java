package com.epoint.audittask.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.map.inter.IAuditTaskMap;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;

/**
 * 
 * 主要是生成json字符串传入到leftwizard控件中。 必须传入的参数有name（左边流程名字），code（左边流程的代码），
 * url（右边加载的url地址），status（导航栏的状态，包括4种， active 编辑状态；done 完成状态 （已处理）；default
 * 默认状态（未处理） disable 锁定状态（失效）） showPrev(是否显示上一步按钮，传入boolean类型)
 * showNext(是否显示下一步按钮，传入boolean类型) showSubmit(是否显示提交按钮，传入boolean类型)
 * showArrow(是否显示箭头，传入boolean类型) 如果按钮不够，请自行在leftWizard_snippet.html模板页面添加按钮。
 * 
 * @author Administrator
 * @version [版本号, 2016年10月11日]
 * 
 * 
 */
@RestController("jnaudittasktopwizardaddaction")
@Scope("request")
public class JnAuditTaskTopWizardAddAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    private AuditWorkflowBizlogic auditWorkflowBizlogic = new AuditWorkflowBizlogic();

    /**
     * 事项guid
     */
    private String taskGuid = "";
    /**
     * 操纵
     */
    private String operation = "";
    /**
     * 部门guid，新增时候需要
     */
    private String ouguid = "";

    /**
     * 事项复制过来的rowguid
     */
    private String copyTaskGuid = "";
    /**
     * 事项id
     */
    private String taskId = "";

    /**
     * 初始化按钮区域
     */
    private Map<String, Object> handleControl = new HashMap<String, Object>();

    /**
     * url参数
     */
    private String urlParam = "";

    /**
     * 事项基本信息
     */
    AuditTask auditTask = new AuditTask();

    @Autowired
    private IAuditTaskMap auditTaskDictService;

    @Autowired
    private IAuditTaskCase auditTaskCaseService;

    @Autowired
    private IAuditTaskMaterial materialService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IHandleTask handleTaskService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateImpl;

    @Autowired
    private IAuditTaskElementService iaudittaskelementservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;

    @Override
    public void pageLoad() {
        // 当前操作类型
        operation = this.getRequestParameter("operation");
        // 对于不同的操作类型，按钮的显示应该有所不同；
        handleControl = auditWorkflowBizlogic.initHandleControl(handleControl, operation);
        // 从页面上获取ouguid
        ouguid = this.getRequestParameter("ouguid");
        // 事项库新增操作,不需要版本复制的 ：1、中心人员的新增add 2、窗口人员的新增windowAdd
        if ("add".equals(operation) || "windowAdd".equals(operation)) {
            // 生成taskId
            taskId = UUID.randomUUID().toString();
            // 新增操作生成taskGuid
            taskGuid = UUID.randomUUID().toString();
        }
        else if ("windowCopy".equals(operation) || "copy".equals(operation)) {
            // 这里是直接获取的数据库数据
            taskGuid = getRequestParameter("taskguid");
            auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
            AuditTaskExtension oldAuditTaskExt = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false)
                    .getResult();
            String is_tssgs = oldAuditTaskExt.getStr("is_tssgs");
            copyTaskGuid = UUID.randomUUID().toString();
            // 复制事项
            boolean shouldnewtaskid = true;
            handleTaskService.initNewTask(copyTaskGuid, auditTask.getTask_id(), userSession.getDisplayName(),
                    userSession.getUserGuid(), shouldnewtaskid, auditTask);
            // 从数据库获取复制的事项，存入临时缓存
            AuditTask auditTaskCopy = auditTaskBasicImpl.getAuditTaskByGuid(copyTaskGuid, false).getResult();
            if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                AuditTaskDelegate audittaskdelegate = new AuditTaskDelegate();
                audittaskdelegate.setRowguid(UUID.randomUUID().toString());
                audittaskdelegate.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
                audittaskdelegate.setTaskguid(UUID.randomUUID().toString());
                audittaskdelegate.setOuguid(auditTaskCopy.getOuguid());
                audittaskdelegate.setOuname(auditTaskCopy.getOuname());
                audittaskdelegate.setDelegatetype(ZwfwConstant.TASKDELEGATE_TYPE_XZFD);
                audittaskdelegate.setStatus(String.valueOf(ZwfwConstant.CONSTANT_INT_ONE));
                audittaskdelegate.setXzorder(0);
                audittaskdelegate.setTaskid(auditTaskCopy.getTask_id());
                audittaskdelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ONE);
                audittaskdelegate.setApplytime(auditTask.getTransact_time());
                auditTaskDelegateImpl.addAuditTaskDelegate(audittaskdelegate);
            }
            else if (ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                AuditTaskDelegate audittaskdelegate = new AuditTaskDelegate();
                audittaskdelegate.setRowguid(UUID.randomUUID().toString());
                audittaskdelegate.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
                audittaskdelegate.setTaskguid(UUID.randomUUID().toString());
                audittaskdelegate.setOuguid(auditTaskCopy.getOuguid());
                audittaskdelegate.setOuname(auditTaskCopy.getOuname());
                audittaskdelegate.setDelegatetype(ZwfwConstant.TASKDELEGATE_TYPE_CJFD);
                audittaskdelegate.setStatus(String.valueOf(ZwfwConstant.CONSTANT_INT_ONE));
                audittaskdelegate.setXzorder(0);
                audittaskdelegate.setTaskid(auditTaskCopy.getTask_id());
                audittaskdelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ONE);
                audittaskdelegate.setApplytime(auditTask.getTransact_time());
                auditTaskDelegateImpl.addAuditTaskDelegate(audittaskdelegate);
            }
            AuditTaskExtension auditTaskExtensionCopy = auditTaskExtensionImpl
                    .getTaskExtensionByTaskGuid(copyTaskGuid, false).getResult();
            auditTaskExtensionCopy.set("is_tssgs", is_tssgs);
            taskGuid = copyTaskGuid;
            taskId = auditTaskCopy.getTask_id();
            // 事项复制和窗口事项变更加载页面的时候就需要把状态改为待上报 delete
            auditTaskExtensionCopy.setTaskadduserdisplayname(userSession.getDisplayName());
            auditTaskExtensionCopy.setTaskadduserguid(userSession.getUserGuid());

            // 复制情形
            auditTaskCaseService.copytaskCase(auditTask.getRowguid(), copyTaskGuid);

            // 复制情形类别
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("taskid", auditTask.getTask_id());
            sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            String ELEMENTGUIDS = "'";
            List<AuditTaskElement> liste = iaudittaskelementservice.findListByCondition(sql.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                ELEMENTGUIDS += auditTaskElement.getRowguid() + "','";
                auditTaskElement.setOperateusername(UserSession.getInstance().getDisplayName());
                auditTaskElement.setDraft(ZwfwConstant.CONSTANT_STR_ZERO);
                auditTaskElement.setTaskid(taskId);
                auditTaskElement.setRowguid(MDUtils.md5Hex(auditTaskElement.getRowguid() + copyTaskGuid));
                // if (StringUtil.isNotBlank(auditTaskElement.getPreoptionguid())) {
                // auditTaskElement
                // .setPreoptionguid(MDUtils.md5Hex(auditTaskElement.getPreoptionguid() + copyTaskGuid));
                // }
                if (StringUtil.isNotBlank(auditTaskElement.getPreoptionguid())
                        && auditTaskElement.getPreoptionguid().indexOf("start") == -1) {
                    auditTaskElement
                            .setPreoptionguid(MDUtils.md5Hex(auditTaskElement.getPreoptionguid() + copyTaskGuid));
                }
                else {
                    auditTaskElement.setPreoptionguid("start");
                }
                iaudittaskelementservice.insert(auditTaskElement);
            }
            ELEMENTGUIDS += "'";
            // 复制正在使用的情形选项
            SqlConditionUtil sqlOption = new SqlConditionUtil();
            sqlOption.eq("taskid", auditTask.getTask_id());
            sqlOption.in("ELEMENTGUID", ELEMENTGUIDS);
            List<AuditTaskOption> listo = iaudittaskoptionservice.findListByCondition(sqlOption.getMap()).getResult();
            // 查询之前的材料和之后的材料
            List<AuditTaskMaterial> auditTaskMaterials = materialService
                    .selectTaskMaterialListByTaskGuid(copyTaskGuid, false).getResult();
            List<AuditTaskMaterial> oldTaskMaterials = materialService
                    .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), false).getResult();
            // 新建map存储老的id和新增id的关系
            Map<String, String> mapid = new HashMap<>();
            Map<String, String> mapname = new HashMap<>();
            for (AuditTaskMaterial oldTaskMaterial : oldTaskMaterials) {
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (oldTaskMaterial.getRowguid().equals(auditTaskMaterial.getCopyfrom())) {
                        mapid.put(oldTaskMaterial.getMaterialid(), auditTaskMaterial.getMaterialid());
                        mapname.put(oldTaskMaterial.getMaterialname(), auditTaskMaterial.getMaterialname());
                    }
                }
            }

            for (AuditTaskOption auditTaskOption : listo) {
                auditTaskOption.setOperateusername(UserSession.getInstance().getDisplayName());
                auditTaskOption.setTaskid(taskId);
                auditTaskOption.setRowguid(MDUtils.md5Hex(auditTaskOption.getRowguid() + copyTaskGuid));
                auditTaskOption.setElementguid(MDUtils.md5Hex(auditTaskOption.getElementguid() + copyTaskGuid));
                // 处理材料和情形的关系
                String ids = auditTaskOption.getMaterialids();
                String names = auditTaskOption.getMaterialnames();
                if (StringUtil.isNotBlank(ids)) {
                    String[] idsarr = ids.split(";");
                    String[] namesarr = ids.split(";");
                    for (String string : idsarr) {
                        if (StringUtil.isBlank(string)) {
                            continue;
                        }
                        if (StringUtil.isNotBlank(mapid.get(string))) {
                            ids = ids.replaceAll(string, mapid.get(string));
                        }
                    }
                    for (String string : namesarr) {
                        if (StringUtil.isBlank(string)) {
                            continue;
                        }
                        if (StringUtil.isNotBlank(mapname.get(string))) {
                            names = ids.replaceAll(string, mapname.get(string));
                        }
                    }
                    auditTaskOption.setMaterialids(ids);
                    auditTaskOption.setMaterialnames(names);
                }
                else {
                    auditTaskOption.setMaterialids("");
                    auditTaskOption.setMaterialnames("");
                }
                iaudittaskoptionservice.insert(auditTaskOption);
            }
            // 复制一遍草稿情形
            copyelement(taskId);

            // 复制材料附件
            if (auditTaskMaterials != null && !auditTaskMaterials.isEmpty()) {
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                        String newCliengguid = UUID.randomUUID().toString();
                        attachService.copyAttachByClientGuid(auditTaskMaterial.getTemplateattachguid(), null, null,
                                newCliengguid);
                        auditTaskMaterial.setTemplateattachguid(newCliengguid);
                    }

                    if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                        String newCliengguid = UUID.randomUUID().toString();
                        attachService.copyAttachByClientGuid(auditTaskMaterial.getExampleattachguid(), null, null,
                                newCliengguid);
                        auditTaskMaterial.setExampleattachguid(newCliengguid);
                    }

                    if (StringUtil.isNotBlank(auditTaskMaterial.getFormattachguid())) {
                        String newCliengguid = UUID.randomUUID().toString();
                        attachService.copyAttachByClientGuid(auditTaskMaterial.getFormattachguid(), null, null,
                                newCliengguid);
                        auditTaskMaterial.setFormattachguid(newCliengguid);
                    }
                    if (StringUtil.isNotBlank(auditTaskMaterial.getPid())) {
                        auditTaskMaterial.setPid(mapid.get(auditTaskMaterial.getPid()));
                    }
                    materialService.updateAuditTaskMaterial(auditTaskMaterial);
                }
            }

            // 复制外部流程图
            if (StringUtil.isNotBlank(auditTask.getTaskoutimgguid())) {
                String newCliengguid = UUID.randomUUID().toString();
                attachService.copyAttachByClientGuid(auditTask.getTaskoutimgguid(), null, null, newCliengguid);
                auditTaskCopy.setTaskoutimgguid(newCliengguid);
                updateAuditTask(auditTaskCopy);
            }

            // 事项复制需要生成新的taskId
            if ("windowCopy".equals(operation)) {
                auditTaskCopy.setTasksource(ZwfwConstant.TASKSOURCE_WINDOW);// 中心复制的事项来源：窗口
                auditTaskDictService.copyTaskDictMap(auditTask.getTask_id(), taskId);
            }
            else if ("copy".equals(operation)) {
                if (ZwfwUserSession.getInstance().getCitylevel() != null
                        && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                    auditTask.setTasksource(ZwfwConstant.TASKSOURCE_WINDOW);
                }
                else {
                    auditTask.setTasksource(ZwfwConstant.TASKSOURCE_CENTER);
                }
                auditTaskDictService.copyTaskDictMap(auditTask.getTask_id(), taskId);
            }
            updateAuditTask(auditTaskCopy);
            updateAuditTaskExt(auditTaskExtensionCopy);
        }
        // 通用的url传参，用到的都写上，如果没有值也没关系
        urlParam = "?taskGuid=" + taskGuid + "&ouguid=" + ouguid + "&taskId=" + taskId + "&operation=" + operation;

    }

    public void copyelement(String taskid) {
        // 查询是否有草稿情形
        if (StringUtil.isBlank(taskid)) {
            addCallbackParam("msg", "未获取到事项版本唯一标识！");
            return;
        }
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("taskid", taskid);
        sql.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        Integer count = iaudittaskelementservice.findCountByCondition(sql.getMap()).getResult();
        if (count == null || count == 0) {
            // 没有草稿，重新复制草稿
            sql.clear();
            sql.eq("taskid", taskid);
            sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            List<AuditTaskElement> liste = iaudittaskelementservice.findListByCondition(sql.getMap()).getResult();
            for (AuditTaskElement auditTaskElement : liste) {
                auditTaskElement.setOperateusername("复制");
                auditTaskElement.setDraft(ZwfwConstant.CONSTANT_STR_ONE);
                auditTaskElement.setRowguid(MDUtils.md5Hex(auditTaskElement.getRowguid()));

                if (StringUtil.isNotBlank(auditTaskElement.getPreoptionguid())
                        && auditTaskElement.getPreoptionguid().indexOf("start") == -1) {
                    auditTaskElement.setPreoptionguid(MDUtils.md5Hex(auditTaskElement.getPreoptionguid()));
                }
                iaudittaskelementservice.insert(auditTaskElement);
            }
            List<AuditTaskOption> listo = iaudittaskoptionservice.findListByTaskid(taskid).getResult();
            for (AuditTaskOption auditTaskOption : listo) {
                auditTaskOption.setOperateusername("复制");
                auditTaskOption.setCopyfrom(auditTaskOption.getRowguid());
                auditTaskOption.setRowguid(MDUtils.md5Hex(auditTaskOption.getRowguid()));
                auditTaskOption.setElementguid(MDUtils.md5Hex(auditTaskOption.getElementguid()));
                iaudittaskoptionservice.insert(auditTaskOption);
            }
        }
    }

    /**
     * 
     * 加载各个模块的信息
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initWizardData() {
        List<Record> recordList = new ArrayList<Record>();

        // 基本信息
        Record basic = new Record();
        basic.put("name", "基本信息");
        basic.put("code", "basic");
        // 新增页面的操作
        if ("add".equals(operation) || "windowAdd".equals(operation)) {
            basic.put("url", "epointzwfw/audittask/basic/audittaskbasicadd" + urlParam);
        }
        // 复制页面的操作
        else {
            basic.put("url", "epointzwfw/audittask/basic/audittaskbasicedit" + urlParam);
        }
        basic.put("status", "active");
        basic.put("showArrow", this.isShowArrow(operation));
        basic.put("showPrev", this.isShowPrev(operation));
        basic.put("showNext", this.isShowNext(operation));
        basic.put("showSubmit", this.isShowSubmit(operation));
        basic.put("showSave", this.isShowSave(operation));
        basic.put("showPass", this.isShowPass(operation));
        basic.put("showNotPass", this.isShowNotPass(operation));
        Map<String, Object> basicHandleControl = new HashMap<String, Object>(16);
        basicHandleControl.putAll(handleControl);
        if (basicHandleControl.containsKey("showReport")) {
            basicHandleControl.replace("showReport", false);
        }
        basic.put("handleControl", basicHandleControl);
        recordList.add(basic);

        // 办理流程
        Record bllc = new Record();
        bllc.put("name", "办理流程");
        bllc.put("code", "bllc");

        bllc.put("url", "epointzwfw/audittask/workflow/audittaskriskpointlist" + urlParam);

        bllc.put("handleControl", handleControl);
        bllc.put("status", this.getStatusByOpr(operation));
        bllc.put("showPrev", this.isShowPrev(operation));
        bllc.put("showNext", this.isShowNext(operation));
        bllc.put("showSubmit", this.isShowSubmit(operation));
        bllc.put("showArrow", this.isShowArrow(operation));
        bllc.put("showPass", this.isShowPass(operation));
        bllc.put("showNotPass", this.isShowNotPass(operation));
        if ("windowCopy".equals(operation)) {
            bllc.put("showReport", true);
        }
        recordList.add(bllc);

        // 系统设置
        Record xtsz = new Record();
        xtsz.put("name", "系统设置");
        xtsz.put("code", "xtsz");
        xtsz.put("url", "epointzwfw/audittask/extend/audittaskconfig" + urlParam);
        xtsz.put("status", this.getStatusByOpr(operation));
        xtsz.put("showArrow", this.isShowArrow(operation));
        Map<String, Object> extendHandleControl = new HashMap<String, Object>(16);
        extendHandleControl.putAll(handleControl);
        xtsz.put("status", this.getStatusByOpr(operation));
        xtsz.put("showPrev", this.isShowPrev(operation));
        xtsz.put("showNext", this.isShowNext(operation));
        xtsz.put("showSubmit", "add".equals(operation) || "copy".equals(operation));
        if ("windowAdd".equals(operation) || "windowCopy".equals(operation)) {
            xtsz.put("showReport", true);
        }
        xtsz.put("handleControl", extendHandleControl);
        xtsz.put("showArrow", this.isShowArrow(operation));
        xtsz.put("showSave", this.isShowSave(operation));
        xtsz.put("showPass", this.isShowPass(operation));
        xtsz.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(xtsz);

        // 申报材料
        Record sbcl = new Record();
        sbcl.put("name", "申报材料");
        sbcl.put("code", "sbcl");
        // 新增操作
        sbcl.put("url", "epointzwfw/audittask/material/matterlist" + urlParam);
        sbcl.put("status", this.getStatusByOpr(operation));
        sbcl.put("showArrow", this.isShowArrow(operation));
        sbcl.put("handleControl", handleControl);
        sbcl.put("showPrev", this.isShowPrev(operation));
        sbcl.put("showNext", this.isShowNext(operation));
        sbcl.put("showSubmit", this.isShowSubmit(operation));
        sbcl.put("showPass", this.isShowPass(operation));
        sbcl.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(sbcl);

        // 要素信息
        Record ysxx = new Record();
        ysxx.put("name", "情形类别");
        ysxx.put("code", "ysxx");
        // 新增操作
        ysxx.put("url", "epointzwfw/audittask/element/audittaskelementandoption" + urlParam);
        ysxx.put("status", this.getStatusByOpr(operation));
        ysxx.put("showArrow", this.isShowArrow(operation));
        ysxx.put("handleControl", handleControl);
        ysxx.put("showPrev", this.isShowPrev(operation));
        ysxx.put("showNext", this.isShowNext(operation));
        ysxx.put("showSubmit", this.isShowSubmit(operation));
        ysxx.put("showPass", this.isShowPass(operation));
        ysxx.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(ysxx);

        // 收费项目
        Record sfxm = new Record();
        sfxm.put("name", "收费项目");
        sfxm.put("code", "sfxm");
        // 新增操作
        sfxm.put("url", "epointzwfw/audittask/fee/audittaskchargeitemlist" + urlParam);
        sfxm.put("status", this.getStatusByOpr(operation));
        sfxm.put("showArrow", this.isShowArrow(operation));
        sfxm.put("handleControl", handleControl);
        sfxm.put("showPrev", this.isShowPrev(operation));
        sfxm.put("showNext", this.isShowNext(operation));
        sfxm.put("showSubmit", this.isShowSubmit(operation));
        sfxm.put("showPass", this.isShowPass(operation));
        sfxm.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(sfxm);

        // 文书配置
        Record wspz = new Record();
        wspz.put("name", "文书配置");
        wspz.put("code", "wspz");
        wspz.put("url", "epointzwfw/audittask/docs/audittaskdoclist" + urlParam);
        wspz.put("status", this.getStatusByOpr(operation));
        wspz.put("showArrow", this.isShowArrow(operation));
        wspz.put("handleControl", handleControl);
        wspz.put("showPrev", this.isShowPrev(operation));
        wspz.put("showNext", this.isShowNext(operation));
        wspz.put("showSubmit", this.isShowSubmit(operation));
        wspz.put("showPass", this.isShowPass(operation));
        wspz.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(wspz);

        // 审批结果
        Record spjg = new Record();
        spjg.put("name", "审批结果");
        spjg.put("code", "spjg");
        spjg.put("url", "epointzwfw/audittask/result/auditresultadd" + urlParam);

        spjg.put("status", this.getStatusByOpr(operation));
        spjg.put("showArrow", this.isShowArrow(operation));
        spjg.put("handleControl", handleControl);
        spjg.put("showPrev", this.isShowPrev(operation));
        spjg.put("showNext", this.isShowNext(operation));
        spjg.put("showSubmit", this.isShowSubmit(operation));
        spjg.put("showSave", this.isShowSave(operation));
        spjg.put("showPass", this.isShowPass(operation));
        spjg.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(spjg);

        // 常见问题
        Record cjwt = new Record();
        cjwt.put("name", "常见问题");
        cjwt.put("code", "cjwt");
        cjwt.put("url", "epointzwfw/audittask/faq/audittaskfaqlist" + urlParam);
        cjwt.put("status", this.getStatusByOpr(operation));
        cjwt.put("showArrow", this.isShowArrow(operation));
        cjwt.put("handleControl", handleControl);
        cjwt.put("showPrev", this.isShowPrev(operation));
        cjwt.put("showNext", this.isShowNext(operation));
        cjwt.put("showSubmit", this.isShowSubmit(operation));
        cjwt.put("showPass", this.isShowPass(operation));
        cjwt.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(cjwt);
        sendRespose(JsonUtil.listToJson(recordList));
    }

    public String getTaskGuid() {
        return taskGuid;
    }

    public void setTaskGuid(String taskGuid) {
        this.taskGuid = taskGuid;
    }

    /**
     * 
     * 根据操作判断流程的状态，如果不为add，则状态为active，如果为add，则状态为default
     * 
     * @param operator
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String getStatusByOpr(String operator) {
        return "default";
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowArrow(String operation) {
        return !"detail".equals(operation);
    }

    /**
     * 
     * 是否显示上一步的按钮，当操作类型为detail不显示上一步
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowPrev(String operation) {
        return !("detail".equals(operation) || "inaudit".equals(operation));
    }

    /**
     * 
     * 是否显示下一步的按钮，当操作类型为detail不显示下一步
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowNext(String operation) {
        return !("detail".equals(operation) || "inaudit".equals(operation));
    }

    /**
     * 
     * 是否显示提交的按钮，当操作类型为edit显示提交
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowSubmit(String operation) {
        return "edit".equals(operation) || "confirmEdit".equals(operation);
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowSave(String operation) {
        return "inaudit".equals(operation);
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowPass(String operation) {
        return "inaudit".equals(operation);
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowNotPass(String operation) {
        return "inaudit".equals(operation);
    }

    public String updateAuditTaskExt(AuditTaskExtension auditTaskExtension) {
        String msg = "";
        AuditCommonResult<String> updateResult = auditTaskExtensionImpl.updateAuditTaskExt(auditTaskExtension);
        if (!updateResult.isSystemCode()) {
            msg = updateResult.getSystemDescription();
        }
        else if (!updateResult.isBusinessCode()) {
            msg = updateResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        return msg;
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
        AuditCommonResult<String> editResult = auditTaskBasicImpl.updateAuditTask(dataBean);
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

package com.epoint.knowledge.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 
 * @author Administrator
 * @version [版本号, 2016年11月1日]
 * 
 * 
 */
@RestController("zskaudittasktoptabnavaction")
@Scope("request")
public class ZskAuditTaskTopTabNavAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 事项guid
     */
    private String taskGuid = "";
    /**
     * 版本modal
     */
    private List<SelectItem> currentVersionModal;
    /**
     * 事项service
     */
    // private AuditTaskBasicService auditTaskService = new
    // AuditTaskBasicService();
    /**
     * 事项对象
     */
    private AuditTask auditTask;
    /**
     * 事项扩展对象
     */
    // private AuditTaskExtension auditTaskExtension;
    /**
     * 当前版本
     */
    private String currentVersion;
    /**
     * 当前版本
     */
    private Integer currentIseditafterimport;
    /**
     * 事项id
     */
    private String taskId;
    /**
     * 代码项service
     */
    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditTask auditTaskService;
    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;
    @Override
    public void pageLoad() {

        taskGuid = this.getRequestParameter("taskGuid");
        taskId = this.getRequestParameter("taskId");
        if (StringUtil.isNotBlank(taskGuid)) {
            auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, false).getResult();
            // auditTaskExtension =
            // auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(taskGuid,
            // false);

            String title = "";
            if (auditTask != null
                    && !ZwfwConstant.TASKAUDIT_STATUS_YZF.equals(String.valueOf(auditTask.getIs_editafterimport()))) {
                taskId = auditTask.getTask_id();
                if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
                    AuditTaskDelegate bean = auditTaskDelegateService.findByTaskIDAndAreacode(taskId,ZwfwUserSession.getInstance().getAreaCode()).getResult();
                    this.addCallbackParam("delegatetype", bean.getDelegatetype());
                }
               
                if (ZwfwConstant.TASKAUDIT_STATUS_SHTG.equals(String.valueOf(auditTask.getIs_editafterimport()))) {
                    title = "此版本由" + auditTask.getOperateusername() + "创建，并审核通过！";
                }
                else {
                    title = "此版本由" + auditTask.getOperateusername() + "创建，暂未审核通过！";
                }
            }
            else {
                // auditTask = auditTaskBizlogic.getCopyTaskByTaskId(taskGuid,
                // "1");
                auditTask = auditTaskService.getUseTaskAndExtByTaskid(taskId).getResult();
                // auditTaskExtension =
                // auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(auditTask.getRowguid(),false);
                if (ZwfwConstant.TASKAUDIT_STATUS_SHTG.equals(String.valueOf(auditTask.getIs_editafterimport()))) {
                    title = "此版本由" + auditTask.getOperateusername() + "创建，并审核通过！";
                }
                else {
                    title = "此版本由" + auditTask.getOperateusername() + "创建，暂未审核通过！";
                }
            }
            // taskId = auditTask.getTask_id();
            currentVersion = auditTask.getRowguid();
            currentIseditafterimport = auditTask.getIs_editafterimport();
            this.addCallbackParam("title", title);
        }
    }

    /**
     * 
     * 加载各个模块的信息
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public void initTabData() {
        List<Record> recordList = new ArrayList<Record>();
        // 基本信息
        Record basic = new Record();
        basic.put("name", "基本信息");
        basic.put("code", "basic");
        basic.put("icon", "modicon-6");
        //basic.put("url", "epointzwfw/audittask/basic/audittaskbasicdetail");
        basic.put("url", "lhzwfw/individuation/overall/knowledge/task/audittaskbasicdetail");
        recordList.add(basic);
        // 申报材料
        Record sbcl = new Record();
        sbcl.put("name", "申报材料");
        sbcl.put("code", "sbcl");
        sbcl.put("icon", "modicon-6");
        //sbcl.put("url", "epointzwfw/audittask/material/audittaskmaterialdetail");
        sbcl.put("url", "lhzwfw/individuation/overall/knowledge/task/audittaskmaterialdetail");
        recordList.add(sbcl);
        // 常见问题
        Record cjwt = new Record();
        cjwt.put("name", "常见问题");
        cjwt.put("code", "cjwt");
        cjwt.put("icon", "modicon-6");
        cjwt.put("url", "epointzwfw/audittask/faq/audittaskfaqlist?taskId=" + taskId + "&operation=detail");
        recordList.add(cjwt);
        sendRespose(JsonUtil.listToJson(recordList));
    }

    public String getTaskGuid() {
        return taskGuid;
    }

    public void setTaskGuid(String taskGuid) {
        this.taskGuid = taskGuid;
    }

    public List<SelectItem> getVersionModal() {
        if (currentVersionModal == null) {
            currentVersionModal = new ArrayList<SelectItem>();
            String filter = " and (IS_EDITAFTERIMPORT = " + ZwfwConstant.TASKAUDIT_STATUS_SHTG + ") ";
            // 1、获取审核通过及作废事项
            List<AuditTask> list = auditTaskService.selectTaskByTaskId(taskId, filter).getResult();
            for (AuditTask databean : list) {
                SelectItem si = new SelectItem(databean.getRowguid(),
                        StringUtil.isBlank(databean.getVersion()) ? codeItemsService.getItemTextByCodeName("事项审核状态",
                                String.valueOf(databean.getIs_editafterimport())) : databean.getVersion());
                currentVersionModal.add(si);
            }
            // 2、补充非正式事项
            List<AuditTask> auditTaskList = auditTaskService.selectlatestByTaskId(taskId).getResult();
            if (auditTaskList != null && auditTaskList.size() > 0) {
                for (AuditTask auditTask : auditTaskList) {
                    String status = String.valueOf(auditTask.getIs_editafterimport());
                    if (ZwfwConstant.TASKAUDIT_STATUS_CG.equals(String.valueOf(currentIseditafterimport))) {
                        if (!ZwfwConstant.TASKAUDIT_STATUS_SHTG.equals(status)
                                && !ZwfwConstant.TASKAUDIT_STATUS_YZF.equals(status)) {
                            SelectItem si = new SelectItem(auditTask.getRowguid(),
                                    codeItemsService.getItemTextByCodeName("事项审核状态",
                                            String.valueOf(auditTask.getIs_editafterimport())));
                            currentVersionModal.add(si);
                        }
                    }
                    else {
                        if (!ZwfwConstant.TASKAUDIT_STATUS_SHTG.equals(status)
                                && !ZwfwConstant.TASKAUDIT_STATUS_YZF.equals(status)
                                && !ZwfwConstant.TASKAUDIT_STATUS_CG.equals(status)) {
                            SelectItem si = new SelectItem(auditTask.getRowguid(),
                                    codeItemsService.getItemTextByCodeName("事项审核状态",
                                            String.valueOf(auditTask.getIs_editafterimport())));
                            currentVersionModal.add(si);
                        }
                    }
                }
            }
        }
        return currentVersionModal;
    }

    /**
     * 根据taskguid改变标题
     * 
     * @param taskGuid
     *            事项guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeTitleByTaskGuid(String taskGuid) {
        if (StringUtil.isNotBlank(taskGuid)) {
            auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, false).getResult();
            // auditTaskExtension =
            // auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(taskGuid,
            // false);
            String title = "";
            if (auditTask != null) {
                if (ZwfwConstant.TASKAUDIT_STATUS_SHTG.equals(String.valueOf(auditTask.getIs_editafterimport()))) {
                    title = "此版本由" + auditTask.getOperateusername() + "创建，并审核通过！";
                }
                else {
                    title = "此版本由" + auditTask.getOperateusername() + "创建，暂未审核通过！";
                }
            }
            this.addCallbackParam("title", title);
        }
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Integer getCurrentIseditafterimport() {
        return currentIseditafterimport;
    }

    public void setCurrentIseditafterimport(Integer currentIseditafterimport) {
        this.currentIseditafterimport = currentIseditafterimport;
    }

}

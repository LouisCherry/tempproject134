package com.epoint.auditqueue.auditqueuetasktype.action;

import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.domain.AuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.inter.IAuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.inter.IAuditQueueYuyueTime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 事项分类list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2016-11-09 09:27:31]
 */
@RestController("jnauditqueuetasktypelistaction")
@Scope("request")
public class JNAuditQueueTasktypeListAction extends BaseController {
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    @Autowired
    private IAuditQueueTasktypeTask tasktypetaskservice;
    @Autowired
    private IAuditOrgaWindowYjs windowservice;
    @Autowired
    private IAuditQueueXianhaotime xianhaotimeservice;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    @Autowired
    private IAuditQueueYuyueTime yuyuetimeservice;
    @Autowired
    private IAuditQueueWindowTasktype windowtasktypeservice;
    @Autowired
    private IOuService frameOuService9;
    @Autowired
    private ITasktype tasktyservice;
    /**
     * 事项分类实体对象
     */
    private AuditQueueTasktype dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditQueueTasktype> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;
    /**
     * 左边部门树列表
     */
    private String leftTreeNodeText;
    /**
     * 按事项类别名字搜索
     */
    private String tasktypename;

    // 中心guid
    private String centerGuid;

    private String areacode;

    @Override
    public void pageLoad() {
        // 首先获取guid参数赋值给centerGuid
        // 为空时，说明是从窗口配置模块进来的，赋值session中的guid
        centerGuid = getRequestParameter("guid");
        areacode = getRequestParameter("areacode");
        if (StringUtil.isBlank(centerGuid)) {
            centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }

        addCallbackParam("centerguid", centerGuid);
        addCallbackParam("areacode", areacode);

    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();

        for (String sel : select) {
            // 删除预约时间配置
            yuyuetimeservice.deletebyTaskGuid(sel);
            // 删除事项对应关系
            tasktypetaskservice.deletebyTaskTypeGuid(sel);
            // 删除对应窗口
            windowtasktypeservice.deletebyTaskTypeGuid(sel);
            // 删除限号时段
            xianhaotimeservice.deletebyTasktypeguid(sel);

            tasktypeservice.delete(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 保存修改
     */
    public void saveInfo() {
        List<AuditQueueTasktype> tasktypeList = getDataGridData().getWrappedData();
        for (AuditQueueTasktype tasktype : tasktypeList) {
            if (tasktype.getOrdernum() == null) {// null会跑到最前
                tasktype.setOrdernum(0);
            }

            String msg = "";

            tasktype.setOperatedate(new Date());
            AuditCommonResult<String> addResult = null;

            addResult = tasktypeservice.updateAuditQueueTasktype(tasktype);

            if (!addResult.isSystemCode()) {
                msg = addResult.getSystemDescription();
            } else if (!addResult.isBusinessCode()) {
                msg = addResult.getBusinessDescription();
            } else {
                msg = "修改成功！";
            }

            if (msg != null && StringUtil.isNotBlank(msg) && !"修改成功！".equals(msg)) {
                addCallbackParam("msg", msg);
                break;
            }
        }
        addCallbackParam("msg", "保存成功！");
    }

    public void importTask() {
        String type = "1";
        String params = areacode;
        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
            type = "2";
            params = leftTreeNodeGuid;
        }
        List<AuditTask> list = tasktyservice.gettasklist(type, params);
        List<AuditOrgaWindowTask> windowlist;
        String tasktypeGuid = "";

        for (AuditTask audittask : list) {
            AuditQueueTasktype dataObj = new AuditQueueTasktype();

            // 通过TaskName,OUGuid判断是否存在相同的，相同的不插入
            if (tasktypeservice.getAuditQueueTasktypeCount(audittask.getTaskname(), audittask.getOuguid(), centerGuid)
                    .getResult() == 0) {
                tasktypeGuid = UUID.randomUUID().toString();
                dataObj.setRowguid(tasktypeGuid);
                dataObj.setOperatedate(new Date());
                dataObj.setOperateusername(UserSession.getInstance().getDisplayName());
                dataObj.setOuguid(audittask.getOuguid());
                dataObj.setTasktypename(audittask.getTaskname());
                dataObj.setIs_yuyue(QueueConstant.Common_yes_String);
                dataObj.setIsmixwindow(QueueConstant.Common_no_String);
                dataObj.setQueueweight(QueueConstant.Common_no_String);
                dataObj.setCenterguid(centerGuid);
                dataObj.setCentername(
                        centerservice.findAuditServiceCenterByGuid(centerGuid).getResult().getCentername());
                dataObj.setOrdernum(audittask.getOrdernum());
                tasktypeservice.insertTasktype(dataObj);

                // 添加事项分类和事项之间的关系
                AuditQueueTasktypeTask tasktypeTask = new AuditQueueTasktypeTask();
                tasktypeTask.setRowguid(UUID.randomUUID().toString());
                tasktypeTask.setOperatedate(new Date());
                tasktypeTask.setOperateusername(UserSession.getInstance().getDisplayName());
                tasktypeTask.setTask_id(audittask.getTask_id());
                tasktypeTask.setTasktypeguid(tasktypeGuid);
                tasktypetaskservice.insertTasktypeTask(tasktypeTask);

                // 插入窗口关联表,将相关的事项分类同audit_window_task复制到audit_queue_window_tasktype
                windowlist = windowservice.getTaskByTaskguid(audittask.getRowguid()).getResult();
                for (AuditOrgaWindowTask auditwindow : windowlist) {
                    AuditQueueWindowTasktype windowtasktype = new AuditQueueWindowTasktype();
                    windowtasktype.setRowguid(UUID.randomUUID().toString());
                    windowtasktype.setOperatedate(new Date());
                    windowtasktype.setOperateusername(UserSession.getInstance().getDisplayName());
                    windowtasktype.setTasktypeguid(tasktypeGuid);
                    windowtasktype.setWindowguid(auditwindow.getWindowguid());
                    windowtasktypeservice.insertWindowtasktype(windowtasktype);
                }
            }
        }
        addCallbackParam("msg", "导入成功！");
    }

    public DataGridModel<AuditQueueTasktype> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditQueueTasktype>() {

                @Override
                public List<AuditQueueTasktype> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.eq("OUGuid", leftTreeNodeGuid);
                    }
                    if (StringUtil.isNotBlank(tasktypename)) {
                        sql.like("tasktypename", tasktypename);
                    }

                    sql.eq("centerguid", centerGuid);
                    PageData<AuditQueueTasktype> pageData = tasktypeservice.getAuditQueueTasktypeByPage(" * ",
                            sql.getMap(), first, pageSize, "ouguid desc, ordernum", "desc").getResult();

                    // 查询部门名称
                    for (AuditQueueTasktype auditqueuetaskype : pageData.getList()) {
                        FrameOu frameOu = frameOuService9.getOuByOuGuid(auditqueuetaskype.getOuguid());
                        auditqueuetaskype.put("ouname", frameOu != null ? frameOu.getOuname() : "");
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public AuditQueueTasktype getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQueueTasktype();
        }
        return dataBean;
    }

    public void setDataBean(AuditQueueTasktype dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getLeftTreeNodeText() {
        return leftTreeNodeText;
    }

    public void setLeftTreeNodeText(String leftTreeNodeText) {
        this.leftTreeNodeText = leftTreeNodeText;
    }

    public String getTasktypename() {
        return tasktypename;
    }

    public void setTasktypename(String tasktypename) {
        this.tasktypename = tasktypename;
    }
}

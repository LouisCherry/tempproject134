package com.epoint.audittask.windowtask.action;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 事项登记list页面对应的后台
 *
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@SuppressWarnings("unused")
@RestController("jnwindowtasklistaction")
@Scope("request")
public class JNWindowTaskListAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 常量：事项编码区分
     */
    private static final String CONSTANT_ZERO = "0";
    /**
     * 常量：大小项配置表区分
     */
    private static final String CONSTANT_ONE = "1";
    /**
     * 常量：不区分大小项
     */
    private static final String CONSTANT_TWO = "2";
    /**
     * AS_ITEM_ID_LENGTH是否配置
     */
    private String isexititemid = CONSTANT_ONE;
    /**
     * AS_ITEM_ID_LENGTH是否配置正确
     */
    private String isitemidright = CONSTANT_ONE;

    /**
     * 审批事项基本信息实体对象
     */
    private AuditTask dataBean = new AuditTask();
    /**
     * 事项大小项分类配置
     */
    private String astaskcategory;
    /**
     * 审批事项基本信息实体对象
     */
    @Autowired
    private IAuditOrgaWindowYjs taskservice;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;
    /**
     * 获取系统参数
     */
    @Autowired
    private IHandleConfig auditCenterConfigService;
    /**
     * 从ZwfwUserSession中获取的windowguid
     */
    private String windowguid;
    /**
     * 父项长度
     */
    private String parentlen;
    /**
     * 子项长度
     */
    private String sublen;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IWorkflowProcessVersionService workflowProcessVersionService;

    @Autowired
    private IWorkflowTransitionService workflowTransitionService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    @Autowired
    private IAuditOrgaWindowYjs auditorgawindowservice;

    private String asItemCategory;

    @Autowired
    private ICodeItemsService codeItemService;

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        // 事项大小项分类配置
        String str = auditCenterConfigService.getFrameConfig("AS_Task_Category", "").getResult();
        astaskcategory = StringUtil.isBlank(str) ? CONSTANT_TWO : str;
        // 事项编码区分大小项
        if (CONSTANT_ZERO.equals(astaskcategory)) {
            String itemlen = auditCenterConfigService.getFrameConfig("AS_ITEM_ID_LENGTH", "").getResult();
            if (StringUtil.isNotBlank(itemlen)) {
                String[] arrlen = itemlen.split(";");
                if (arrlen.length == 2) {
                    parentlen = arrlen[0];// 父项长度
                    sublen = arrlen[1];// 子项长度
                } else {
                    isitemidright = CONSTANT_ZERO;// AS_ITEM_ID_LENGTH配置错误
                }
            } else {
                isexititemid = CONSTANT_ZERO;// AS_ITEM_ID_LENGTH没有配置
            }
        }
        asItemCategory = auditCenterConfigService.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        this.addCallbackParam("asItemCategory", asItemCategory);
        if (dataBean == null) {
            dataBean = new AuditTask();
        }
    }

    /**
     * 保存排序
     */
    public void saveAllOrderNumber() {
        List<Map<String, Object>> select = this.getDataGridData().getFeedbackdata();

        for (Map<String, Object> auditTask : select) {
            AuditTask task = auditTaskService.getAuditTaskByGuid(auditTask.get("rowguid").toString(), false)
                    .getResult();
            // 市级、区县事项登记页面
            AuditOrgaWindowTask windowtask = auditorgawindowservice
                    .findWindowTask(auditTask.get("windowtaskguid").toString()).getResult();
            if (windowtask != null) {
                windowtask.setOrdernum(Integer.valueOf(auditTask.get("ordernum").toString()));
                auditorgawindowservice.updateWindowTask(windowtask);
            }
        }
        addCallbackParam("msg", "排序成功！");
    }

    public DataGridModel<AuditTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 677864526968407035L;

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    boolean flag = true;
                    if (StringUtil.isNotBlank(dataBean.getTaskname())) {
                        sql.like("taskname", dataBean.getTaskname());
                        flag = false;
                    }
                    if (StringUtil.isNotBlank(dataBean.getItem_id())) {
                        sql.like("item_id", dataBean.getItem_id());
                        flag = false;
                    }
                    sql.eq("IS_ENABLE", "1");
                    sortField = "ordernum";
                    sortOrder = "desc";
                    PageData<AuditTask> pageData = null;

                    pageData = auditTaskService
                            .getAuditWindowTaskPageData(windowguid, sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();

                    for (AuditTask auditTask : pageData.getList()) {
                        auditTask.setOrdernum(auditTask.getInt("ordernumber"));
                        String opurl = getOperateURL(auditTask.getRowguid(), auditTask.getProcessguid(),
                                auditTask.getTaskname(), auditTask.getTask_id(), String.valueOf(auditTask.getType()),
                                auditTask.getJbjmode());

                        auditTask.put("taskname", opurl);
                        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                            AuditTaskDelegate delegate = auditTaskDelegateService.findByTaskIDAndAreacode(
                                    auditTask.getTask_id(), ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            if (delegate != null) {
                                if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                                        && ZwfwConstant.CONSTANT_STR_ONE.equals(delegate.getUsecurrentinfo())) {
                                    if (StringUtil.isNotBlank(delegate.getPromise_day())) {
                                        auditTask.put("promise_day", delegate.getPromise_day());
                                    }
                                }
                                // 事项下放类型
                                if (StringUtil.isNotBlank(delegate.getDelegatetype())) {
                                    auditTask.put("delegatetype", delegate.getDelegatetype());
                                }
                            }
                        }
                        String ywztcode = auditTask.getStr("ywztcode");

                        if (StringUtil.isNotBlank(ywztcode)) {
                            String[] yeztcode = ywztcode.split(";");
                            if (yeztcode != null && yeztcode.length > 1) {
                                auditTask.put("ywztcode", yeztcode[0]);
                                auditTask.put("itemcode", yeztcode[1]);
                            } else {
                                auditTask.put("ywztcode", "");
                            }
                        }
                    }
                    this.setRowCount(pageData.getRowCount());

                    return pageData.getList();
                }

            };
        }
        return model;
    }

    /**
     *
     */

    protected PageData<AuditTask> windowtaskToAudittask(PageData<AuditOrgaWindowTask> windowtaskpage) {
        PageData<AuditTask> pagedata = new PageData<AuditTask>();
        AuditTask audittask = null;
        List<AuditTask> list = new ArrayList<AuditTask>();
        for (AuditOrgaWindowTask auditorgawindowtask : windowtaskpage.getList()) {
            audittask = auditTaskService.selectUsableTaskByTaskID(auditorgawindowtask.getTaskid()).getResult();
            audittask.set("ordernumber", auditorgawindowtask.getOrdernum());
            audittask.set("windowtaskguid", auditorgawindowtask.getRowguid());
            list.add(audittask);
        }
        pagedata.setList(list);
        pagedata.setRowCount(windowtaskpage.getRowCount());
        return pagedata;
    }

    /**
     * 获取窗口事项名称链接地址 如果流程没有出现在过程版本信息表里面，返回 【<span
     * style=\"color:red\">流程配置错误</span>】 如果流程没有变迁条件，返回【<span
     * style=\"color:red\">尚未新增岗位</span>】 其余则返回以事项名称为内容的a标签
     *
     * @param taskguid    事项guid
     * @param processguid 过程guid
     * @param taskname    事项名称
     * @param taskid      事项id
     * @return String 返回的HTMl
     */
    public String getOperateURL(String taskguid, String processguid, String taskname, String taskid, String taskType,
                                String jbjMode) {
        String returl = "";
        // 事项大项
        if (StringUtil.isBlank(taskid)) {
            returl = taskname;
        } else {
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType) && !ZwfwConstant.JBJMODE_STANDARD.equals(jbjMode)) {
                returl = taskname;
            } else {
                String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(processguid);
                if (processversionguid != null) {
                    List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                    if (list != null && list.size() > 0) {
                        returl = taskname;
                    } else {
                        returl = taskname + "【<span style=\"color:red\">尚未新增岗位</span>】";
                    }
                } else {
                    returl = taskname + "【<span style=\"color:red\">流程配置错误</span>】";
                }
            }
        }
        return returl;
    }

    /**
     * 判断是否存在岗位配置 [一句话功能简述] [功能详细描述]
     *
     * @param taskguid
     * @param processguid
     * @param taskname
     * @param taskid
     * @param taskType
     * @param jbjMode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean getHasFlow(String taskguid, String processguid, String taskname, String taskid, String taskType,
                              String jbjMode) {
        boolean returl = false;
        // 事项大项
        if (StringUtil.isBlank(taskid)) {
            returl = false;
        } else {

            String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(processguid);
            if (processversionguid != null) {
                List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                if (list != null && list.size() > 0) {
                    returl = true;
                } else {
                    returl = false;
                }
            } else {
                returl = true;
            }

        }
        return returl;

    }

    public AuditTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }

}

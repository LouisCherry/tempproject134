package com.epoint.sghd;

import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProjectDuban;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.bizlogic.orga.ou.service.FrameOuService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.commonutil.api.IJnSghdCommonutil;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 办件登记页面action
 *
 * @version [版本号, 2018年10月15日]
 * @作者 shibin
 */
@RestController("jncomprehensionwindowaction")
@Scope("request")
public class JnComprehensionWindowAction extends BaseController {

    private static final long serialVersionUID = -1490905998750897938L;

    @Autowired
    private IAuditQueue auditqueueService;

    @Autowired
    private IJnSghdCommonutil sghdService;

    @Autowired
    private IWorkflowProcessVersionService workflowProcessVersionService;

    @Autowired
    private IWorkflowTransitionService workflowTransitionService;

    @Autowired
    private IAuditOrgaWindow iAuditOrgaWindow;

    @Autowired
    private IAuditProjectDuban iAuditProject;

    /**
     * 办件事项信息实体对象
     */
    private AuditTask dataBean = new AuditTask();

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;
    /**
     * 事项名称，搜索用
     */
    private String taskName;
    /**
     * 部门名称
     */
    private String ouname;

    private FrameOuService9 frameOuService = new FrameOuService9();

    private String bmtjnumber;

    /**
     * 从ZwfwUserSession中获取的windowguid
     */
    private String windowguid;

    private Integer count = 0;

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
    }

    /**
     * tabs数据
     */
    public void refreshTabNav() {

        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }

        // 查询已办结事项数目
        int zsknum = getTaskNum();

        // 获取审批中以及已暂停办件数量
        List<String> taskidList = iAuditOrgaWindow.getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid())
                .getResult();

        Map<String, Integer> map = iAuditProject.getCountStatusByWindowguidAndCenterguidAndAreacode(taskidList,
                ZwfwUserSession.getInstance().getWindowGuid(), ZwfwUserSession.getInstance().getAreaCode(),
                ZwfwUserSession.getInstance().getCenterGuid(), area).getResult();

        addCallbackParam("djjnumber", map == null ? "0" : map.get("DJJ"));
        addCallbackParam("dslnumber", map == null ? "0" : map.get("DSL"));
        addCallbackParam("dbbnumber", map == null ? "0" : map.get("DBB"));
        addCallbackParam("dbjnumber", map == null ? "0" : map.get("DBJ"));
        addCallbackParam("spznumber", map == null ? "0" : map.get("SPZ"));
        addCallbackParam("yztnumber", map == null ? "0" : map.get("YZT"));
        addCallbackParam("zsknum", zsknum);
    }

    /**
     * 获取对应的办件量数目
     *
     * @return
     */

    public Integer getTaskNum() {
        String windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        String ouGuid = userSession.getOuGuid();

        // 如果是镇村接件
        String area;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }

        String sql = "select count(1) from audit_project where 1=1 and status >= 90 and areacode = '" + area + "' ";

        String userGuid = userSession.getUserGuid();
        // 获取事项的task_id
        if (StringUtil.isNotBlank(windowguid)) {
            // 窗口人员登录，查看本窗口事项
            sql = sql + " and windowguid = '" + windowguid + "' ";
            sql = sql + "and ((TASKTYPE = 1 AND ACCEPTUSERGUID = '" + userGuid
                    + "') OR (TASKTYPE = 2 AND RECEIVEUSERGUID = '" + userGuid + "' ))";
        } else {
            // 其他人员登录，查看本部门的事项
            sql = sql + " and ouguid = '" + ouGuid + "' ";
        }

        int count = sghdService.queryInt(sql);
        return count;
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    //SqlConditionUtil sql = new SqlConditionUtil();
                    String taskName = dataBean.getTaskname();
                    Map<String, String> conditionMap = new HashMap<String, String>();
                    if (StringUtil.isNotBlank(taskName)) {
                        conditionMap.put("b.taskname like", taskName);
                    }
                    if (StringUtil.isNotBlank(ouname) && !"f9root".equals(ouname.toLowerCase())) {
                        conditionMap.put("b.ouguid=", ouname);
                    }

                    // 如果是镇村接件
                    String area = "";
                    if (ZwfwUserSession.getInstance().getCitylevel() != null
                            && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    } else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    conditionMap.put("b.areacode=", area);
                    conditionMap.put("a.windowguid=", windowguid);
                    conditionMap.put("b.IS_ENABLE=", "1");
                    conditionMap.put("b.IS_EDITAFTERIMPORT=", "1");
                    //过滤掉大项
                    conditionMap.put("ifnull(b.IS_PARENTTASK, '0')=", ZwfwConstant.CONSTANT_STR_ZERO);
                    conditionMap.put("ifnull(b.is_history, '0')=", ZwfwConstant.CONSTANT_STR_ZERO);
                    PageData<Record> pageData = sghdService.getTaskByWindowguid(conditionMap,
                            Integer.valueOf(first), Integer.valueOf(pageSize), sortField, sortOrder);
                    List<Record> list = pageData.getList();
                    if (list != null && list.size() > 0) {
                        String ouguid = list.get(0).get("ouguid");
                        FrameOu ouByOuGuid = frameOuService.getOuByOuGuid(ouguid);
                        for (Record record : list) {
                            record.put("ouname", ouByOuGuid.getOushortName());
                            record.set("Taskname",
                                    getOperateURL(record.get("rowguid").toString(),
                                            record.get("processguid").toString(), record.get("taskname").toString(),
                                            record.get("task_id").toString(), record.get("type").toString()));
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return list;
                }
            };
        }
        return model;
    }

    public void queueAdd(String qno) {
        AuditQueue auditQueue = new AuditQueue();
        auditQueue.setRowguid(UUID.randomUUID().toString());
        auditQueue.setQno(qno);
        auditQueue.setCalltime(new Date());
        auditQueue.setHandlewindowguid(ZwfwUserSession.getInstance().getWindowGuid());
        auditqueueService.addQueue(auditQueue);
    }

    public void queueUpdate(String qno) {
        String sql = "select * from AUDIT_QUEUE where 1=1 and handlewindowguid=?1 and qno=?2 and date(calltime) = curdate()";
        AuditQueue record = sghdService.find(sql, AuditQueue.class, ZwfwUserSession.getInstance().getWindowGuid(), qno);
        if (record != null && record.size() > 0) {
            record.setCalltime(new Date());
            sghdService.update(record);
        } else {
            addCallbackParam("msg", "不存在此号码，请重新呼叫！");
        }

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
    public String getOperateURL(String taskguid, String processguid, String taskname, String taskid, String taskType) {
        String returl = "";
        // 事项大项
        if (StringUtil.isBlank(taskid)) {
            returl = taskname;
        } else {
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                returl = "<a  href='javascript:void(0)' onclick='creatProject(\"" + taskid + "\",\"\")'>" + taskname
                        + "</a>";
            } else {
                String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(processguid);
                if (processversionguid != null) {
                    List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                    if (list != null && list.size() > 0) {
                        returl = "<a  href='javascript:void(0)' onclick='creatProject(\"" + taskid + "\",\""
                                + processguid + "\")'>" + taskname + "</a>";
                    } else {
                        returl = taskname + "【<span style=\"color:red;\">尚未新增岗位</span>】";
                    }
                } else {
                    returl = taskname + "【<span style=\"color:red\">流程配置错误</span>】";
                }
            }
        }
        return returl;
    }

    public LazyTreeModal9 getTreeModal() {
        return new SelectFoShanOuAction().getTreeModel();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getBmtjnumber() {
        return bmtjnumber;
    }

    public void setBmtjnumber(String bmtjnumber) {
        this.bmtjnumber = bmtjnumber;
    }

    public AuditTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }

    public String getOuname() {
        return ouname;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
    }

    public String getWindowguid() {
        return windowguid;
    }

    public void setWindowguid(String windowguid) {
        this.windowguid = windowguid;
    }

}

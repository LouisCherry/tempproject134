package com.epoint.audittask.baisc.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.audittask.basic.api.IJnAuditTaskService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.basic.service.AuditTaskBasicService;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 事项基本信息list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-03 14:38:39]
 */
@RestController("hbaudittasklist4xzysaction")
@Scope("request")
public class HbAuditTaskList4XzysAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -1490905998750897938L;

    /**
     * 事项基本信息实体对象
     */
    private AuditTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;
    /**
     * 事项名称，搜索用
     */
    private String taskName;
    /**
     * 事项编码，搜索用
     */
    private String item_id;
    /**
     * 是否只显示收费
     */
    private String isfee;
    /**
     * 是否只显示自建系统
     */
    private String isZiJianXT;
    /**
     *审核状态
     */
    private String is_editafterimport;

    /**
     *审批类别
     */
    private String shenpilb;

    /**
     * 事项缓存service
     */
    //private IAuditTaskService auditTaskServiceMemImpl=new AuditTaskServiceMemImpl();
    /**
     * 是否自建系统
     */
    private String iszijianxitong;

    private Date dtFrom;

    /**
     * 审批类别代码项
     */
    private List<SelectItem> shenpilbModel;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    
    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Autowired
    private IAuditOrgaWindowYjs auditWindowImpl;

    @Autowired
    private IAuditTaskDelegate delegateService;

    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IHandleConfig handleConfigService;
    
    @Autowired
    private IConfigService configservice;

    @Override
    public void pageLoad() {
        is_editafterimport = this.getRequestParameter("is_editafterimport");
        String shenpi = this.getRequestParameter("shenpi");
        shenpilb = getShenpilbcode(shenpi);
        if (StringUtil.isNotBlank(shenpilb)) {
            GregorianCalendar gcNew = new GregorianCalendar();
            gcNew.set(Calendar.MONTH, gcNew.get(Calendar.MONTH) - 1);
            dtFrom = gcNew.getTime();
        }
        String ouguid = this.getRequestParameter("ouguid");
        if (!"undefined".equals(ouguid)) {
            leftTreeNodeGuid = ouguid;
        }
        ;
    }

    public String getShenpilbcode(String shenpi) {
        List<Map<String, String>> list = CodeModalFactory.factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批类别", null,
                false);
        for (Map<String, String> map : list) {
            if (map.get(shenpi) != null) {
                return map.get(shenpi);
            }
        }
        return null;
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        String msg = "";
        for (String taskGuid : select) {
            String task_id = getDataGridData().getWrappedData().stream()
                    .filter(audittask -> taskGuid.equals(audittask.getRowguid())).findFirst().get().getTask_id();
            AuditTask auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
            AuditTask shauditTask = getCopyTaskByTaskId(auditTask.getTask_id(), ZwfwConstant.TASKAUDIT_STATUS_DSH);
            if (shauditTask != null && !shauditTask.isEmpty()) {
                msg = "待审核的事项不能被删除！";
                break;
            }
            log.info("删除事项："+taskGuid+"，操作人："+userSession.getDisplayName());
            deleteTaskByStatus(taskGuid);
            updataDelegate(auditTask.getTask_id(), ZwfwConstant.CONSTANT_STR_ZERO);
            auditTaskBasicImpl.deleteTaskByTaskIdAndStatus(task_id, ZwfwConstant.TASKAUDIT_STATUS_CG);
            auditTaskBasicImpl.deleteTaskByTaskIdAndStatus(task_id, ZwfwConstant.TASKAUDIT_STATUS_DSB);
            auditTaskBasicImpl.deleteTaskByTaskIdAndStatus(task_id, ZwfwConstant.TASKAUDIT_STATUS_SHWTG);
        }
        if (StringUtil.isBlank(msg)) {
            addCallbackParam("msg", "成功删除！");
        }
        else {
            addCallbackParam("msg", msg);
        }

    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getShenpilbModel() {
        if (shenpilbModel == null) {
            shenpilbModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批类别", null, false));
        }
        return this.shenpilbModel;
    }

    /**
     * 更改事项开启状态
     * @throws InterruptedException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeAuditTaskStatus(String taskGuid) throws InterruptedException {
        try {
            EpointFrameDsManager.begin(null);
            AuditTask databean = this.auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
            if (databean.getIs_enable() == ZwfwConstant.CONSTANT_INT_ZERO) {
                databean.setIs_enable(ZwfwConstant.CONSTANT_INT_ONE);
            }
            else {
                databean.setIs_enable(ZwfwConstant.CONSTANT_INT_ZERO);
            }
            auditTaskBasicImpl.updateAuditTask(databean);
            updataDelegate(databean.getTask_id(), String.valueOf(databean.getIs_enable()));
            EpointFrameDsManager.commit();
            // 发送RabbitMQ  Enable通知
            syncWindowTask("enable", taskGuid);
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
    }

    /**
     * 开启事项状态
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void startAuditTask() {
        try {
            EpointFrameDsManager.begin(null);
            List<String> taskGuidList = this.getDataGridData().getSelectKeys();
            for (String taskGuid : taskGuidList) {
                AuditTask auditTask = this.auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
                auditTask.setIs_enable(ZwfwConstant.CONSTANT_INT_ONE);
                auditTaskBasicImpl.updateAuditTask(auditTask);
                updataDelegate(auditTask.getTask_id(), ZwfwConstant.CONSTANT_STR_ONE);
            }
            EpointFrameDsManager.commit();
            for (String taskGuid : taskGuidList) {
                // 发送RabbitMQ Enable通知
                syncWindowTask("enable", taskGuid);
            }
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
    }

    /**
     * 禁用事项状态
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void stopAuditTask() {
        try {
            EpointFrameDsManager.begin(null);
            List<String> taskGuidList = this.getDataGridData().getSelectKeys();
            for (String taskGuid : taskGuidList) {
                AuditTask auditTask = this.auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
                auditTask.setIs_enable(ZwfwConstant.CONSTANT_INT_ZERO);
                auditTaskBasicImpl.updateAuditTask(auditTask);
                updataDelegate(auditTask.getTask_id(), ZwfwConstant.CONSTANT_STR_ZERO);
            }
            EpointFrameDsManager.commit();
            for (String taskGuid : taskGuidList) {
                // 发送RabbitMQ通知
                syncWindowTask("modify", taskGuid);
            }
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
    }

    /**
     * 
     *  判断该事项是否进行了拷贝
     *  @param taskGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void judgeIfCopy(String taskGuid, String operation) {
        String msg = "";
        String copyTaskGuid = "";
        String msgDSH = "";
        String msgcg = "";
        //从缓存里面获取数据
        AuditTask auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
        AuditTask auditTaskDSH = getCopyTaskByTaskId(auditTask.getTask_id(), ZwfwConstant.TASKAUDIT_STATUS_DSH);
        AuditTask auditTaskDQR = getCopyTaskByTaskId(auditTask.getTask_id(), ZwfwConstant.TASKAUDIT_STATUS_DQR);
        if (auditTaskDSH != null) {
            msgDSH = "该事项正在审核，无法进行变更!";
        }
        else if (auditTaskDQR != null) {
            msgDSH = "该事项存在待确认事项，无法进行变更！";
        }
        else {
            AuditTask copyAuditTask = auditTaskBasicImpl
                    .getCopyTaskByTaskId(auditTask.getTask_id(), ZwfwConstant.TASKAUDIT_STATUS_CG,
                            ZwfwConstant.TASKAUDIT_STATUS_DSB, ZwfwConstant.TASKAUDIT_STATUS_SHWTG)
                    .getResult();
            if (copyAuditTask != null) {
                if (StringUtil.isNotBlank(copyAuditTask.get("count"))
                        && Integer.parseInt(copyAuditTask.get("count").toString()) <= 1) {
                    AuditTask usableAuditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(auditTask.getTask_id())
                            .getResult();
                    if (usableAuditTask != null) {
                        String displayname = copyAuditTask.getOperateusername();
                        if (StringUtil.isNotBlank(displayname) && !displayname.equals(userSession.getDisplayName())) {
                            displayname = "【" + displayname + "】";
                            msg = "该事项" + displayname + "正在编辑，是否直接加载该数据？";
                        }
                    }
                    else {
                        msgcg = "cg";
                    }
                    copyTaskGuid = copyAuditTask.getRowguid();
                }
                else {
                    auditTaskBasicImpl.deleteTaskByTaskIdAndStatus(auditTask.getTask_id(),
                            ZwfwConstant.TASKAUDIT_STATUS_CG);
                    auditTaskBasicImpl.deleteTaskByTaskIdAndStatus(auditTask.getTask_id(),
                            ZwfwConstant.TASKAUDIT_STATUS_DSB);
                    auditTaskBasicImpl.deleteTaskByTaskIdAndStatus(auditTask.getTask_id(),
                            ZwfwConstant.TASKAUDIT_STATUS_SHWTG);
                }

            }
        }
        this.addCallbackParam("msgcg", msgcg);
        this.addCallbackParam("msg", msg);
        this.addCallbackParam("copyTaskGuid", copyTaskGuid);
        this.addCallbackParam("msgDSH", msgDSH);
    }

    /**
     * 保存事项信息。主要是排序
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveAuditTask() {
        List<AuditTask> auditTaskList = this.getDataGridData().getWrappedData();
        for (AuditTask auditTask : auditTaskList) {
            auditTask.keep("rowguid", "ordernum");
            auditTaskBasicImpl.updateAuditTask(auditTask);
        }
        addCallbackParam("msg", "保存成功！");
    }

    /**
     * 
     *  从缓存里面获取数据，并且分页
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<AuditTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 3533647924418797189L;

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(is_editafterimport)) {
                        sql.eq("is_editafterimport", is_editafterimport);
                    }
                    else {
                        sql.eq("is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.eq("ouguid", leftTreeNodeGuid);
                    }
                    else {
                        AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                        if (auditOrgaArea != null) {
                            //取辖区下窗口部门
                            List<FrameOu> frameOus = frameOu.getWindowOUList(auditOrgaArea.getXiaqucode(), true)
                                    .getResult();
                            String ouGuids = "'";
                            StringBuilder bld = new StringBuilder(ouGuids);
                            for (FrameOu frameou : frameOus) {
                                bld.append(frameou.getOuguid());
                                bld.append("','");
                            }
                            bld.append("'");
                            ouGuids = bld.toString();
                            sql.in("ouguid", ouGuids);
                        }
                    }

                    if (StringUtil.isNotBlank(taskName)) {
                        sql.like("taskname", taskName);
                    }
                    if (StringUtil.isNotBlank(item_id)) {
                        sql.like("item_id", item_id);
                    }
                    if (StringUtil.isNotBlank(shenpilb)) {
                        sql.eq("shenpilb", shenpilb);
                    }
                    if (StringUtil.isNotBlank(dtFrom)) {
                        sql.ge("b.operatedate", dtFrom);
                    }
                    if (StringUtil.isNotBlank(isfee) && "true".equals(isfee)) {
                        sql.eq("charge_flag",
                                "true".equals(isfee) ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO);
                    }
                    if (StringUtil.isNotBlank(iszijianxitong) && "true".equals(iszijianxitong)) {
                        sql.eq("iszijianxitong", "true".equals(iszijianxitong) ? ZwfwConstant.CONSTANT_STR_ONE
                                : ZwfwConstant.CONSTANT_STR_ZERO);
                    }
                    //辖区编码作为过滤条件
                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    sql.eq("istemplate", ZwfwConstant.CONSTANT_STR_ZERO);//排除模板事项
                    // 启用事项
                    sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                    sortField = "ordernum";
                    AuditTaskBasicService AuditTaskBasicService = new AuditTaskBasicService();
                    PageData<AuditTask> pageData = AuditTaskBasicService
                            .getAuditTaskPageData(sql.getMap(), first, pageSize, sortField, sortOrder);
                    List<AuditTask> list = new ArrayList<>();
                    list = pageData.getList();
                    if (list != null && !list.isEmpty()) {
                        for (AuditTask auditTask : list) {
                            auditTask.put("operationType", getOperateURL(auditTask.getRowguid()));
                            if (StringUtil.isNotBlank(auditTask.getYw_catalog_id()) && "1".equals(configservice.getFrameConfigValue("IS_CHECKNEWTASK"))) {                             
                                    auditTask.put("taskname", "【新】" + auditTask.getTaskname());
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

    public String getOperateURL(String taskguid) {
        String retstr = "";
        retstr = "<a href=javascript:openEdit('" + taskguid + "')><font color='red'>配置</font></a>";
        return retstr;
    }

    /**
     * 
     * 根据taskId获取事项
     * 
     * @param taskId
     * @param is_editafterimports
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditTask getCopyTaskByTaskId(String taskId, String is_editafterimports) {
        AuditCommonResult<AuditTask> result = auditTaskBasicImpl.getCopyTaskByTaskId(taskId, is_editafterimports);
        return result.getResult();
    }

    /**
     * 
     * 逻辑删除事项
     * 
     * @param taskGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteTaskByStatus(String taskGuid) {
        try {
            EpointFrameDsManager.begin(null);
            auditTaskBasicImpl.deleteAuditTask(taskGuid);
            // 将窗口事项表中配置的老的事项删除
            List<AuditOrgaWindowTask> windowtaskList = auditWindowImpl.getTaskByTaskguid(taskGuid).getResult();
            if (!windowtaskList.isEmpty()) {
                log.info("删除事项："+taskGuid+"，操作人："+userSession.getDisplayName());
//                auditWindowImpl.deleteWindowTaskByTaskGuid(taskGuid);
            }
            EpointFrameDsManager.commit();
            // 2017_4_8 CH 事项删除以后发送消息至RabbitMQ队列
            syncWindowTask("delete", taskGuid);
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
    }

    public void updataDelegate(String taskid, String status) {
        List<AuditTaskDelegate> list = delegateService.selectDelegateByTaskID(taskid).getResult();
        for (AuditTaskDelegate auditTaskDelegate : list) {
            // 如果该乡镇事项未授权，则不处理
            if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTaskDelegate.getDelegatetype())) {
                auditTaskDelegate.setStatus(status);
                delegateService.updata(auditTaskDelegate);
            }
        }
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
            String RabbitMQMsg = SendType + ";" + taskGuid;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                    "task." + ZwfwUserSession.getInstance().getAreaCode() + "." + SendType);
            String isStaticTask = handleConfigService.getFrameConfig("AS_isStaticTask", null).getResult();
            if (StringUtil.isNotBlank(isStaticTask) && ZwfwConstant.CONSTANT_STR_ONE.equals(isStaticTask)) {
                sendMQMessageService.sendByExchange("zwdt_exchange_handle", RabbitMQMsg,
                        "task." + ZwfwUserSession.getInstance().getAreaCode() + "." + SendType);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getIsfee() {
        return isfee;
    }

    public void setIsfee(String isfee) {
        this.isfee = isfee;
    }

    public String getIsZiJianXT() {
        return isZiJianXT;
    }

    public void setIsZiJianXT(String isZiJianXT) {
        this.isZiJianXT = isZiJianXT;
    }

    public String getIs_editafterimport() {
        return is_editafterimport;
    }

    public void setIs_editafterimport(String is_editafterimport) {
        this.is_editafterimport = is_editafterimport;
    }

    public String getIszijianxitong() {
        return iszijianxitong;
    }

    public void setIszijianxitong(String iszijianxitong) {
        this.iszijianxitong = iszijianxitong;
    }

    public String getShenpilb() {
        return shenpilb;
    }

    public void setShenpilb(String shenpilb) {
        this.shenpilb = shenpilb;
    }

}

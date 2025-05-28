package com.epoint.auditorga.auditwindow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditorga.auditwindow.api.IJnGxhTaskConfigService;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 窗口事项配置页面对应的后台
 *
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("jnauditwindowtaskconfigaction")
@Scope("request")
public class JNAuditWindowTaskConfigAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -4632640761998959244L;
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    @Autowired
    private IHandleFrameOU frameOu;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    @Autowired
    private ISendMQMessage sendMQMessageService;

    /**
     * 窗口与事项的关联实体对象
     */
    private AuditOrgaWindowTask dataBean = null;
    /**
     * 窗口人员model
     */
    private LazyTreeModal9 treeModel = null;
    /**
     * 窗口标识
     */
    private String windowGuid = null;

    @Override
    public void pageLoad() {
        windowGuid = getRequestParameter("guid");
        log.info("登录人员："+userSession.getDisplayName());
    }

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditTaskDelegate iAuditTaskDelegate;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IJnGxhTaskConfigService taskConfigService;


    /***
     *
     * 用treebean构造事项树
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public LazyTreeModal9 getTreeModel() {

        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllTask());
            if (!isPostback()) {
                treeModel.setSelectNode(this.getSelectTask(windowGuid));
            }
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            // treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("所有辖区");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    /**
     * 使用SimpleFetchHandler9构造树
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private SimpleFetchHandler9 loadAllTask() {

        // 我已评估无sonar问题
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9() {


            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public List<AuditTask> handleTaskList(List<AuditTask> auditTasks) {
                if (auditTasks != null && !auditTasks.isEmpty()) {
                    auditTasks = auditTasks.stream().filter(
                            i -> ("1".equals(i.getStr("businesstype")) || StringUtil.isBlank(i.getStr("businesstype"))))
                            .collect(Collectors.toList());
                }
                else {
                    auditTasks = new ArrayList<AuditTask>();
                }
                return auditTasks;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public <T> List<T> search(String conndition) {
                List list = new ArrayList();
                if (StringUtil.isNotBlank(conndition)) {
                    List<AuditTask> auditTasks = auditTaskBasicImpl
                            .selectAuditTaskByCondition(conndition, ZwfwUserSession.getInstance().getAreaCode())
                            .getResult();
                    auditTasks = handleTaskList(auditTasks);
                    list = auditTasks;
                }
                return list;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        list = frameOu.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(), true).getResult();
                        // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有事项
                    } else {
                        // list =
                        // auditWindowTaskBizlogic.selectAuditTaskOuByObjectGuid(treeData.getObjectGuid());
                        // 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
                        if ("frameou".equals(treeData.getObjectcode())) {
                            List<AuditTask> auditTasks = auditTaskBasicImpl
                                    .selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(),
                                    ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            auditTasks = handleTaskList(auditTasks);

                            list = auditTasks;
                        }
                        // 如果点击的是事项的checkbox，则返回该是事项的list
                        else if ("audittask".equals(treeData.getObjectcode())) {
                            List<AuditTask> auditTasks = auditTaskBasicImpl
                                    .selectAuditTaskByObjectGuid(treeData.getObjectGuid()).getResult();
                            auditTasks = handleTaskList(auditTasks);

                            list = auditTasks;
                            String[] t = treeData.getObjectGuid().split("&");
                            list.add(iAuditTask.getAuditTaskByGuid(t[0], false).getResult());
                        }
                    }

                    //添加市本级事项下放的事项
                    List<AuditTaskDelegate> delegates = iAuditTaskDelegate.selectDelegateByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                    if (delegates != null && delegates.size() > 0) {
                        for (AuditTaskDelegate delegate : delegates) {
                            AuditTask auditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(delegate.getTaskid()).getResult();
                            if (auditTask != null && "1".equals(delegate.getStatus())
                                    && ("1".equals(auditTask.getStr("businesstype"))
                                            || StringUtil.isBlank(auditTask.getStr("businesstype")))) {
                                list.add(auditTask);
                            }
                        }
                    }
                }
                // 点击checkbox的时候触发
                else {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
                    if ("frameou".equals(treeData.getObjectcode())) {
                        List<AuditTask> auditTasks = auditTaskBasicImpl
                                .selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(),
                                ZwfwUserSession.getInstance().getAreaCode()).getResult();
                        auditTasks = handleTaskList(auditTasks);
                        list = auditTasks;
                    }
                    // 如果点击的是事项的checkbox，则返回该是事项的list
                    else if ("audittask".equals(treeData.getObjectcode())) {
                        List<AuditTask> auditTasks = auditTaskBasicImpl
                                .selectAuditTaskByObjectGuid(treeData.getObjectGuid()).getResult();
                        auditTasks = handleTaskList(auditTasks);
                        list = auditTasks;
                        String[] t = treeData.getObjectGuid().split("&");
                        list.add(iAuditTask.getAuditTaskByGuid(t[0], false).getResult());
                    } else {
                        List<AuditTask> auditTasks = iAuditTask
                                .selectAuditTaskByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                                .getResult();
                        auditTasks = handleTaskList(auditTasks);
                        list = auditTasks;
                    }
                }
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeDate) {
                List<AuditTask> auditTasks = auditTaskBasicImpl.selectAuditTaskOuByObjectGuid(treeDate.getObjectGuid(),
                        ZwfwUserSession.getInstance().getAreaCode()).getResult();
                auditTasks = handleTaskList(auditTasks);
                return auditTasks.size();
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        // 将dataList转化为frameou的list
                        if (obj instanceof FrameOu) {
                            FrameOu frameOu = (FrameOu) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameOu.getOuguid());
                            treeData.setTitle(frameOu.getOuname());
                            // 没有子节点的不允许点击
                            List<AuditTask> auditTasks = auditTaskBasicImpl.selectAuditTaskOuByObjectGuid(
                                    frameOu.getOuguid(), ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            auditTasks = handleTaskList(auditTasks);
                            int childCount = auditTasks.size();
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            } else {
                                // objectcode的作用是来区分是点击了部门还是事项
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }
                        }
                        // 将dataList转化为AuditTask的list
                        if (obj instanceof AuditTask) {
                            AuditTask auditTask = (AuditTask) obj;
                            TreeData treeData = new TreeData();
                            String guid = auditTask.getRowguid() + "&" + auditTask.getTask_id();
                            treeData.setObjectGuid(guid);
                            treeData.setTitle(auditTask.getTaskname().replace(",", "，"));
                            // objectcode的作用是来区分是点击了部门还是事项
                            treeData.setObjectcode("audittask");
                            treeList.add(treeData);
                        }
                    }
                }
                return treeList;
            }
        };
        return fetchHandler9;

    }

    /**
     * 根据windowguid获取事项SelectItem，初始化事项信息
     *
     * @param windowGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private List<SelectItem> getSelectTask(String windowGuid) {
        List<SelectItem> auditTaskList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(windowGuid)) {
            List<AuditTask> taskList = getTaskByWindow(windowGuid);
            if (taskList != null && taskList.size() > 0) {
                for (AuditTask auditTask : taskList) {
                    auditTaskList.add(new SelectItem(auditTask.getRowguid() + "&" + auditTask.getTask_id(),
                            auditTask.getTaskname().replace(",", "，")));
                }
            }
        }

        return auditTaskList;
    }

    /**
     * 把事项和窗口信息保存到关系表里面
     * 1. 对比选中的事项（taskid）和当前窗口已经关联的事项，判断他是新增了关联事项还是删除了关联事项
     * 2. 如果是新增关联的事项，则跳过不用管，正常新增，如果还存在删除关联的事项，则需要判断，
     * 如果对应删除关联的事项是在用状态（is_enable=1），则也正常删除，相反如果他是禁用状态（is_enable=0），
     * 则需要校验，如果对应的事项存在尚在办理中的办件（办件表中TASK_ID字段为此id且状态大于等于待预审（12）且小于办结（90），
     * 且不等于初始化（20）且不等于预审退回（14）且不等于审批不通过（40））如果不存在，则正常删除关联，
     * 如果存在此类办件，则遍历获取到其办件编号，其他不满足此条件的事项正常新增、删除关联，满足了此提条件的情况，
     * 则返回给前台提示“保存成功，事项“XXX（事项名称）”存在未办结的下列办件：办件STD20212XXX（办件编号）、
     * STD20212XXX（办件编号），无法删除关联”。
     *
     * @param guidList   前台获取的事项guid 用“;”分割
     * @param windowGuid 窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveTaskToWindow(String guidList, String windowGuid) {
        try {
            if (StringUtil.isNotBlank(windowGuid)) {
                EpointFrameDsManager.begin(null);
                // 获取原窗口事项list
                List<String> oldtaskidlist = new ArrayList<>();
                // 获取新窗口事项list
                List<String> newtaskidlist = new ArrayList<>();

                oldtaskidlist = auditWindowImpl.getTaskidsByWindow(windowGuid).getResult();
                AuditOrgaWindow auditOrgaWindow = auditWindowImpl.getWindowByWindowGuid(windowGuid)
                        .getResult();
                // 添加新的事项关联关系
                if (StringUtil.isNotBlank(guidList)) {
                    String[] taskGuids = guidList.split(";");
                    if (StringUtil.isNotBlank(windowGuid)) {
                        for (int i = 0; i < taskGuids.length; i++) {
                            String[] guids = taskGuids[i].split("&");
                            if (guids.length == 2) {
                                Boolean flag = false;
                                for (String taskid : oldtaskidlist) {
                                    if (taskid.equals(guids[1])) {//有相同的
                                        flag = true;
                                    }
                                }
                                if (!flag) {//如果没有相同的则为增加的
                                    AuditOrgaWindowTask auditWindowTask = new AuditOrgaWindowTask();
                                    auditWindowTask.setRowguid(UUID.randomUUID().toString());
                                    auditWindowTask.setWindowguid(windowGuid);
                                    auditWindowTask.setTaskguid(guids[0]);
                                    auditWindowTask.setOperatedate(new Date());
                                    auditWindowTask.setOrdernum(taskGuids.length - i);
                                    auditWindowTask.setTaskid(guids[1]);
                                    auditWindowTask.setEnabled("1");// 插入的数据默认为有效
                                    auditWindowImpl.insertWindowTask(auditWindowTask);
                                }
                                newtaskidlist.add(guids[1]);
                            }
                        }
                    }
                }
                StringBuilder allBuffer = new StringBuilder();
                allBuffer.append("保存成功，");
                for (String oldtaskid : oldtaskidlist) {
                    boolean flag = false;
                    for (String newtaskid : newtaskidlist) {
                        if (oldtaskid.equals(newtaskid)) {//有相同的则是没有删除的
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {//删除的
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("task_id", oldtaskid);
                        sql.isBlankOrValue("is_history", "0");
                        sql.eq("is_editafterimport", "1");
                        List<AuditTask> result1 = iAuditTask.getAuditTaskList(sql.getMap()).getResult();
                        if (result1 == null || result1.isEmpty()) {
                            log.info("删除窗口："+auditOrgaWindow.getWindowname()+",删除事项：，操作人："+userSession.getDisplayName()+",删除id："+oldtaskid);
                            auditWindowImpl.deleteWindowTaskByWindowGuidAndTaskId(windowGuid, oldtaskid);
                        } else {
                            AuditTask auditTaskByCondition = result1.get(0);
                            if ("1".equals(auditTaskByCondition.getIs_enable().toString())) {//正常删除
                                log.info("删除窗口："+auditOrgaWindow.getWindowname()+",删除事项："+auditTaskByCondition.getTaskname()+"，操作人："+userSession.getDisplayName()+",删除id："+auditTaskByCondition.getTask_id());
                                auditWindowImpl.deleteWindowTaskByWindowGuidAndTaskId(windowGuid, auditTaskByCondition.getTask_id());
                            } else {
                                //禁用状态（is_enable=0），则需要校验
                                List<AuditProject> result = taskConfigService.getAuditProjectListByTaskid(auditTaskByCondition.getTask_id());
                                if (result == null || result.isEmpty()) {//如果不存在，则正常删除关联
                                    log.info("删除窗口："+auditOrgaWindow.getWindowname()+",删除事项："+auditTaskByCondition.getTaskname()+"，操作人："+userSession.getDisplayName()+",删除id："+auditTaskByCondition.getTask_id());
                                    auditWindowImpl.deleteWindowTaskByWindowGuidAndTaskId(windowGuid, auditTaskByCondition.getTask_id());
                                } else {
                                    StringBuilder stringBuffer = new StringBuilder();
                                    stringBuffer.append("事项\"").append(auditTaskByCondition.getTaskname()).append("\"存在未办结的下列办件：办件 ");
                                    int count = 0;
                                    for (AuditProject auditProject : result) {
                                        if (count != result.size() - 1) {
                                            stringBuffer.append(auditProject.getFlowsn()).append("、");
                                            count++;
                                        } else {
                                            stringBuffer.append(auditProject.getFlowsn()).append("，无法删除关联。");
                                        }
                                    }
                                    allBuffer.append(stringBuffer);
                                }
                            }
                        }
                    }
                }
                addCallbackParam("msg", allBuffer.toString());
                EpointFrameDsManager.commit();
                // 并集去除重复项
                List<String> oldtaskidlisttemp = new ArrayList<>(oldtaskidlist);
                List<String> newtaskidlisttemp = new ArrayList<>(newtaskidlist);
                oldtaskidlisttemp.retainAll(newtaskidlisttemp);

                oldtaskidlist.addAll(newtaskidlist);
                oldtaskidlist.removeAll(oldtaskidlisttemp);

                if (!oldtaskidlist.isEmpty()) {
                    syncWindowTask("modify", windowGuid, ZwfwUserSession.getInstance().getCenterGuid(),
                            oldtaskidlist.toString());
                }
            }
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
    }

    // 推送事项窗口关系修改信息
    public void syncWindowTask(String SendType, String windowguid, String centerguid, String taskids) {
        try {
            String RabbitMQMsg = SendType + ";" + windowguid + ";" + centerguid + ";" + taskids;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg, "windowtask." + ZwfwUserSession.getInstance().getAreaCode() + "." + SendType);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getWindowGuid() {
        return windowGuid;
    }

    public void setWindowGuid(String windowGuid) {
        this.windowGuid = windowGuid;
    }

    public AuditOrgaWindowTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOrgaWindowTask dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * 根据窗口guid获取事项列表
     *
     * @param windowGuid 事项guid
     * @return List<AuditTask>
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getTaskByWindow(String windowGuid) {
        List<AuditTask> taskList = new ArrayList<AuditTask>();
        if (StringUtil.isNotBlank(windowGuid)) {
            List<AuditOrgaWindowTask> auditWindowList = auditWindowImpl.getTaskByWindow(windowGuid).getResult();
            if (auditWindowList != null && auditWindowList.size() > 0) {
                for (AuditOrgaWindowTask auditWindowTask : auditWindowList) {
                    AuditTask auditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(auditWindowTask.getTaskid())
                            .getResult();
                    if (auditTask != null) {
                        taskList.add(auditTask);
                    }
                }
            }
        }
        return taskList;
    }

}

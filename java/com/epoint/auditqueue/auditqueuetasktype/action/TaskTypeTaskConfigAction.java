package com.epoint.auditqueue.auditqueuetasktype.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.core.EpointFrameDsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditqueuetasktypetask.domain.AuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("tasktypetaskconfigaction")
@Scope("request")
public class TaskTypeTaskConfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;
    @Autowired
    private IAuditQueueTasktypeTask tasktypetaskservice;
    @Autowired
    private IHandleFrameOU handleFrameOUservice;
    @Autowired
    private IAuditTask taskservice;
    @Autowired
    private IHandleConfig configservice;
    /**
     * 窗口人员model
     */
    private LazyTreeModal9 treeModel = null;
    /**
     * 事项分类标识
     */
    private String tasktypeguid = null;

    private String itemcategory = "";
    // 中心guid
    private String centerGuid;

    private String areacode;

    @Override
    public void pageLoad() {
        centerGuid = getRequestParameter("centerguid");
        areacode = getRequestParameter("areacode");
        if (StringUtil.isBlank(centerGuid)) {
            centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        tasktypeguid = getRequestParameter("guid");
        itemcategory = configservice.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
            addCallbackParam("itemcategory", "true");
        }
    }

    /***
     * 
     * 用treebean构造事项树
     * 
     * @return
     * 
     * 
     */
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllTask(tasktypeguid));
            treeModel.setSelectNode(this.getSelectTask(tasktypeguid));
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            // treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("所有部门");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    /**
     * 
     * 使用SimpleFetchHandler9构造树
     * 
     * @param tasktypeguid
     * 
     * @return
     * 
     * 
     */
    public SimpleFetchHandler9 loadAllTask(String tasktypeguid) {
        return new SimpleFetchHandler9()
        {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings({"unchecked" })
            @Override
            public <T> List<T> search(String conndition) {
                List<AuditTask> list = new ArrayList<AuditTask>();
                if (StringUtil.isNotBlank(conndition)) {

                    list = taskservice.selectAuditTaskByCondition(conndition, areacode).getResult();
                }
                return (List<T>) list;
            }

            @SuppressWarnings({"unchecked", "rawtypes" })
            @Override
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        list = handleFrameOUservice.getWindowOUList(areacode, true).getResult();
                    }
                    // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有事项
                    else {
                        /*
                         * list =
                         * taskservice.selectAuditTaskOuByObjectGuid(treeData.
                         * getObjectGuid(),
                         * ZwfwUserSession.getInstance().getAreaCode()).
                         * getResult();
                         */
                        if ("frameou".equals(treeData.getObjectcode())) {
                            if (StringUtil.isNotBlank(itemcategory)
                                    && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("OUGUID", treeData.getObjectGuid());
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("areacode", areacode);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.rightLike("item_id", "000");
                                list = taskservice.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                        .getResult().getList();

                            }
                            else {
                                list = taskservice.selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(), areacode)
                                        .getResult();

                            }

                        }
                        // 如果点击的是事项的checkbox，则返回该是事项的list
                        else if ("audittask".equals(treeData.getObjectcode())) {
                            if (StringUtil.isNotBlank(itemcategory)
                                    && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                String itemid = treeData.getObjectGuid().split("&")[2];
                                String itemend = itemid.substring(itemid.length() - 3);
                                AuditTask parentTask = taskservice
                                        .getAuditTaskByGuid(treeData.getObjectGuid().split("&")[0], areacode, false)
                                        .getResult();
                                if ("000".equals(itemend) && itemid.length() >= 3) {
                                    list = taskservice.selectUsableTaskItemListByItemId(
                                            itemid.substring(0, itemid.length() - 3), itemid).getResult();
                                    List removetask = new ArrayList<>();
                                    for (Object auditTask : list) {
                                        if (auditTask instanceof AuditTask) {
                                            if (((AuditTask) auditTask).getItem_id().length() != itemid.length()
                                                    || !((AuditTask) auditTask).getOuguid()
                                                            .equals(parentTask.getOuguid())) {
                                                removetask.add(auditTask);
                                            }
                                        }
                                    }
                                    list.removeAll(removetask);
                                }
                            }
                            else {
                                list = taskservice.selectAuditTaskByObjectGuid(treeData.getObjectGuid()).getResult();
                                String[] t = treeData.getObjectGuid().split("&");
                                list.add(taskservice.getAuditTaskByGuid(t[0], false).getResult());
                            }

                        }
                    }
                }
                // 点击checkbox的时候触发
                else {
                    /*
                     * // 如果点击的是部门的checkbox，则返回该部门下所有的事项的list if
                     * ("frameou".equals(treeData.getObjectcode())) { list =
                     * taskservice.selectAuditTaskOuByObjectGuid(treeData.
                     * getObjectGuid(),
                     * ZwfwUserSession.getInstance().getAreaCode()).getResult();
                     * } // 如果点击的是事项的checkbox，则返回该是事项的list else if
                     * ("audittask".equals(treeData.getObjectcode())) { list =
                     * taskservice.selectAuditTaskByObjectGuid(treeData.
                     * getObjectGuid()).getResult(); }
                     */
                    if ("audittask".equals(treeData.getObjectcode())) {
                        list = taskservice.selectAuditTaskByObjectGuid(treeData.getObjectGuid()).getResult();
                        String[] t = treeData.getObjectGuid().split("&");
                        list.add(taskservice.getAuditTaskByGuid(t[0], false).getResult());
                    }
                }

                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeDate) {
                int childCount = 0;
                if ("frameou".equals(treeDate.getObjectcode())) {
                    if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("OUGUID", treeDate.getObjectGuid());
                        sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                        sql.eq("areacode", areacode);
                        sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                        sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                        sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                        sql.rightLike("item_id", "000");
                        childCount = taskservice.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                .getResult().getRowCount();

                    }
                    else {
                        childCount = taskservice.selectAuditTaskOuByObjectGuid(treeDate.getObjectGuid(), areacode)
                                .getResult().size();
                    }
                }
                else if ("audittask".equals(treeDate.getObjectcode())) {
                    if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                        String itemid = treeDate.getObjectGuid().split("&")[2];
                        String itemend = itemid.substring(itemid.length() - 3);
                        AuditTask parentTask = taskservice
                                .getAuditTaskByGuid(treeDate.getObjectGuid().split("&")[0], areacode, false)
                                .getResult();
                        if ("000".equals(itemend) && itemid.length() >= 3) {
                            List<AuditTask> childtask = new ArrayList<>();
                            childtask = taskservice
                                    .selectUsableTaskItemListByItemId(itemid.substring(0, itemid.length() - 3), itemid)
                                    .getResult();
                            List<AuditTask> removetask = new ArrayList<>();
                            for (AuditTask auditTask : childtask) {
                                if ((auditTask).getItem_id().length() != itemid.length()
                                        || !parentTask.getOuguid().equals(auditTask.getOuguid())) {
                                    removetask.add(auditTask);
                                }
                            }
                            childtask.removeAll(removetask);
                            childCount = childtask.size();
                        }
                    }
                }

                return childCount;
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                String itemend = "";
                if (dataList != null) {
                    for (Object obj : dataList) {
                        // 将dataList转化为frameou的list
                        if (obj instanceof FrameOu) {
                            FrameOu frameOu = (FrameOu) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameOu.getOuguid());
                            treeData.setTitle(frameOu.getOuname());
                            treeData.setNoClick(true);
                            // 没有子节点的不允许点击
                            int childCount = 0;
                            if (StringUtil.isNotBlank(itemcategory)
                                    && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("OUGUID", treeData.getObjectGuid());
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("areacode", areacode);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.rightLike("item_id", "000");
                                childCount = taskservice.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                        .getResult().getRowCount();

                            }
                            else {
                                childCount = taskservice.selectAuditTaskOuByObjectGuid(frameOu.getOuguid(), areacode)
                                        .getResult().size();
                            }
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            }
                            else {
                                // objectcode的作用是来区分是点击了部门还是事项
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }
                        }
                        // 将dataList转化为AuditTask的list
                        if (obj instanceof AuditTask) {
                            AuditTask auditTask = (AuditTask) obj;
                            TreeData treeData = new TreeData();
                            String guid = auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&"
                                    + auditTask.getItem_id();
                            treeData.setObjectGuid(guid);
                            itemend = auditTask.getItem_id().substring(auditTask.getItem_id().length() - 3);
                            if (StringUtil.isNotBlank(itemcategory)
                                    && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                treeData.setTitle("(" + itemend + ")" + auditTask.getTaskname().replace(",", "，"));
                            }
                            else {
                                treeData.setTitle(auditTask.getTaskname().replace(",", "，"));
                            }
                            // objectcode的作用是来区分是点击了部门还是事项
                            treeData.setObjectcode("audittask");
                            treeList.add(treeData);
                        }
                    }
                }
                return treeList;
            }
        };

        
    }

    public List<SelectItem> getSelectTask(String tasktypeguid) {
        List<SelectItem> auditTaskList = new ArrayList<SelectItem>();
        String item = "";
        if (StringUtil.isNotBlank(tasktypeguid)) {
            List<AuditTask> taskList = new ArrayList<AuditTask>();
            if (StringUtil.isNotBlank(tasktypeguid)) {
                List<String> taskids = tasktypetaskservice.getTaskIDbyTaskTypeGuid(tasktypeguid);
                if (taskids != null && !taskids.isEmpty()) {
                    for (String taskid : taskids) {
                        AuditTask auditTask = taskservice.selectUsableTaskByTaskID(taskid).getResult();
                        if (auditTask != null && ZwfwConstant.CONSTANT_INT_ONE == auditTask.getIs_enable()) {
                            taskList.add(auditTask);
                        }
                    }
                }
            }
            if (taskList != null && !taskList.isEmpty()) {
                for (AuditTask auditTask : taskList) {
                    item = auditTask.getItem_id();
                    if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                        auditTaskList
                                .add(new SelectItem(auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + item,
                                        "(" + item.substring(item.length() - 3) + ")"
                                                + auditTask.getTaskname().replace(",", "，")));
                    }
                    else {
                        auditTaskList
                                .add(new SelectItem(auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + item,
                                        auditTask.getTaskname().replace(",", "，")));
                    }
                }
            }
        }
        return auditTaskList;
    }

    public void saveTaskType(String guidList, String tasktypeguid) {
        if (StringUtil.isNotBlank(tasktypeguid)) {
            EpointFrameDsManager.begin(null);
            tasktypetaskservice.deletebyTaskTypeGuid(tasktypeguid);
            EpointFrameDsManager.commit();
            // 添加新的事项关联关系
            if (StringUtil.isNotBlank(guidList)) {
                String[] taskids = guidList.split(";");
                if (StringUtil.isNotBlank(tasktypeguid)) {
                    for (int i = 0; i < taskids.length; i++) {
                        String[] guids = taskids[i].split("&");
                        if (guids.length == 3) {
                            // 一个中心一个事项只能配置到一个分类，删除该事项在其他类别的对应关系。
                            String rowguid = tasktypetaskservice.getRowguidbyTaskIDandCenterGuid(guids[1], centerGuid)
                                    .getResult();
                            if (rowguid != null) {
                                EpointFrameDsManager.begin(null);
                                tasktypetaskservice.deletebyGuid(rowguid);
                                EpointFrameDsManager.commit();
                            }
                            // 插入事项
                            AuditQueueTasktypeTask tasktypeTask = new AuditQueueTasktypeTask();
                            tasktypeTask.setRowguid(UUID.randomUUID().toString());
                            tasktypeTask.setOperatedate(new Date());
                            tasktypeTask.setOperateusername(UserSession.getInstance().getDisplayName());
                            tasktypeTask.setTasktypeguid(tasktypeguid);
                            tasktypeTask.setTask_id(guids[1]);
                            EpointFrameDsManager.begin(null);
                            tasktypetaskservice.insertTasktypeTask(tasktypeTask);
                            EpointFrameDsManager.commit();
                        }
                    }
                }
            }
        }

    }

}

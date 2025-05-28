package com.epoint.sghd.sg.action;

import com.epoint.auditorga.auditwindow.action.AuditWindowTaskConfigAction;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.commonutil.api.IJnSghdCommonutil;
import com.epoint.sghd.sg.entity.AuditOrgaMemberTask;
import com.epoint.sghd.sg.service.IGxhAuditOrgaMemberTask;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 窗口事项配置页面对应的后台
 *
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("jnmembertaskconfigaction")
@Scope("request")
public class JnMemberTaskConfigAction extends BaseController {
    transient Logger log = LogUtil.getLog(AuditWindowTaskConfigAction.class);
    /**
     *
     */
    private static final long serialVersionUID = -4632640761998959244L;
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    @Autowired
    private IHandleFrameOU frameOu;
    @Autowired
    private IGxhAuditOrgaMemberTask auditWindowImpl;
    @Autowired
    private IHandleConfig config;

    @Autowired
    private IJnSghdCommonutil sghdService;
    /**
     * 窗口与事项的关联实体对象
     */
    private AuditOrgaMemberTask dataBean = null;

    private TreeModel treeModel1 = null;
    /**
     * 窗口标识
     */
    private String windowGuid = null;

    private String itemcategory = "";

    private String fields = "rowguid,task_id,item_id,type,taskname,ouguid";

    private String areacode = "";

    private String centerguid = "";

    @Override
    public void pageLoad() {
        windowGuid = getRequestParameter("guid");
        itemcategory = config.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
            addCallbackParam("itemcategory", "true");
        }

        areacode = this.getRequestParameter("areacode");

        if (StringUtil.isBlank(areacode)) {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }

        centerguid = this.getRequestParameter("centerguid");

        if (StringUtil.isBlank(centerguid)) {
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        }

    }

    @SuppressWarnings("finally")
    public TreeModel getTaskTreeModel() {
        try {
            EpointFrameDsManager.begin(null);
            if (treeModel1 == null) {
                treeModel1 = new TreeModel() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 7262093133578183235L;

                    /***
                     * 加载树，懒加载
                     */
                    @Override
                    public List<TreeNode> fetch(TreeNode treeNode) {
                        TreeData treeData = TreeFunction9.getData(treeNode);
                        List<TreeNode> list = new ArrayList<>();
                        List<AuditTask> listTask = new ArrayList<>();
                        List<FrameOu> listOu = new ArrayList<>();
                        if (treeData == null) {
                            TreeNode root = new TreeNode();
                            root.setText("所有部门");
                            root.setId("root");
                            root.setPid("-1");
                            root.getColumns().put("isOU", "true");//标记：不是部门节点
                            list.add(root);
                            root.setCkr(true);
                            root.setExpanded(true);//展开下一层节点
                            list.addAll(fetch(root));//自动加载下一层树结构
                        } else {
                            if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                                //以下是框架封装的方法，具体参数请参见api
                                List<AuditTask> listsearchTask = new ArrayList<AuditTask>();
                                if (StringUtil.isNotBlank(itemcategory)
                                        && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                    sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.like("taskname", treeNode.getSearchCondition());
                                    sql.eq("areacode", areacode);
                                    sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                    listsearchTask = auditTaskBasicImpl
                                            .getAuditEnableTaskPageData(fields, sql.getMap(), 0, 0, "ordernum", "desc")
                                            .getResult().getList();

                                } else {
                                    //                                listsearchTask = auditTaskBasicImpl
                                    //                                        .selectAuditTaskByCondition(treeNode.getSearchCondition(), areacode)
                                    //                                        .getResult();
                                    listsearchTask = sghdService
                                            .selectAuditTaskByCondition(treeNode.getSearchCondition(), areacode);
                                }
                                //搜索完成后，进行树的绑定
                                for (int j = 0; j < listsearchTask.size(); j++) {
                                    TreeNode node = new TreeNode();
                                    node.setId(listsearchTask.get(j).getRowguid() + "&" + listsearchTask.get(j).getTask_id()
                                            + "&" + listsearchTask.get(j).getItem_id());
                                    node.setText(listsearchTask.get(j).getTaskname() + "<b>("
                                            + listsearchTask.get(j).getOuname() + ")</b>");
                                    node.setPid("-1");
                                    node.setLeaf(true);
                                    list.add(node);
                                }
                            } else {
                                String objectGuid = treeData.getObjectGuid();
                                SqlConditionUtil sql = new SqlConditionUtil();
                                if ("root".equals(objectGuid)) {
                                    listOu = frameOu.getWindowOUList(areacode, true).getResult();
                                } else {
                                    if ("true".equals(treeNode.getColumns().get("isOU"))) {
                                        if (StringUtil.isNotBlank(itemcategory)
                                                && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                            sql.clear();
                                            sql.eq("OUGUID", treeData.getObjectGuid());
                                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.eq("areacode", areacode);
                                            sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.rightLike("item_id", "000");
                                            listTask = auditTaskBasicImpl
                                                    .getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                                    .getResult().getList();
                                        } else {
                                            //                                        listTask = auditTaskBasicImpl
                                            //                                                .selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(), areacode)
                                            //                                                .getResult();

                                            listTask = sghdService
                                                    .selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(), areacode);
                                        }
                                    } else {
                                        sql.clear();
                                        String itemleft;
                                        String item_id = (String) treeNode.getColumns().get("item_id");
                                        if (item_id != null && item_id.length() > 3) {
                                            itemleft = item_id.substring(0, item_id.length() - 3);
                                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.eq("areacode", areacode);
                                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.leftLike("item_id", itemleft);
                                            sql.nq("item_id", item_id);
                                            listTask = auditTaskBasicImpl
                                                    .getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                                    .getResult().getList();
                                        }
                                    }

                                }

                                // 部门绑定
                                for (int i = 0; i < listOu.size(); i++) {
                                    if ("root".equals(objectGuid) || (listOu.get(i).getParentOuguid().equals(objectGuid))) {
                                        TreeNode node = new TreeNode();
                                        node.setId(listOu.get(i).getOuguid());
                                        node.setText(listOu.get(i).getOuname());
                                        if ("root".equals(objectGuid)) {
                                            node.setPid("root");
                                        } else {
                                            node.setPid(listOu.get(i).getParentOuguid());
                                        }
                                        node.setCkr(true);
                                        node.getColumns().put("isOU", "true");//标记：是部门节点
                                        node.setLeaf(false);
                                        int taskCount = 0;
                                        if (StringUtil.isNotBlank(itemcategory)
                                                && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                            sql.clear();
                                            sql.eq("OUGUID", listOu.get(i).getOuguid());
                                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.eq("areacode", areacode);
                                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.rightLike("item_id", "000");
                                            taskCount = auditTaskBasicImpl
                                                    .getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                                    .getResult().getRowCount();

                                        } else {
                                            //                                        taskCount = auditTaskBasicImpl
                                            //                                                .selectAuditTaskOuByObjectGuid(listOu.get(i).getOuguid(), areacode)
                                            //                                                .getResult().size();
                                            taskCount = sghdService
                                                    .selectAuditTaskOuByObjectGuid(listOu.get(i).getOuguid(), areacode)
                                                    .size();

                                        }
                                        if (taskCount > 0) {
                                            list.add(node);
                                        }
                                    }

                                }
                                //事项的绑定
                                String itemleft;
                                int taskCount = 0;
                                for (int j = 0; j < listTask.size(); j++) {

                                    //判断事项类型  sghdService
                                    //String tasktype = sghdService.getTaskTypeByRowguid(listTask.get(j).getRowguid());
                                    //if (!"02".equals(tasktype)) {
                                    TreeNode node2 = new TreeNode();
                                    node2.setId(listTask.get(j).getRowguid() + "&" + listTask.get(j).getTask_id() + "&"
                                            + listTask.get(j).getItem_id());
                                    node2.setText(listTask.get(j).getTaskname().replace(",", "，"));
                                    node2.setPid(listTask.get(j).getOuguid());
                                    node2.getColumns().put("isOU", "false");//标记：不是部门节点
                                    node2.setLeaf(true);
                                    if (StringUtil.isNotBlank(itemcategory)
                                            && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                        sql.clear();
                                        sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                        sql.eq("areacode", areacode);
                                        sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                        sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                        sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                        sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                        itemleft = listTask.get(j).getItem_id();
                                        itemleft = itemleft.substring(0, itemleft.length() - 3);
                                        sql.nq("item_id", listTask.get(j).getItem_id());
                                        sql.leftLike("item_id", itemleft);
                                        if (listTask.get(j).getItem_id().endsWith("000")) {
                                            taskCount = auditTaskBasicImpl
                                                    .getAuditTaskPageData(sql.getMap(), 0, 1, "ordernum", "desc")
                                                    .getResult().getRowCount();
                                            node2.setText("("
                                                    + listTask.get(j).getItem_id()
                                                    .substring(listTask.get(j).getItem_id().length() - 3)
                                                    + ")" + listTask.get(j).getTaskname());
                                        } else {
                                            node2.setPid(objectGuid);
                                            node2.setText("("
                                                    + listTask.get(j).getItem_id()
                                                    .substring(listTask.get(j).getItem_id().length() - 3)
                                                    + ")" + listTask.get(j).getTaskname());
                                        }
                                        if (taskCount > 0) {
                                            node2.setLeaf(false);
                                            node2.getColumns().put("item_id", listTask.get(j).getItem_id());
                                        }
                                    }

                                    list.add(node2);
                                }
                                //}
                            }
                        }
                        return list;
                    }

                    /***
                     * 懒加载进行获取数据，把左边树中选择的内容加载到右边
                     */
                    @Override
                    public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                        //获取到tree原有的select
                        List<SelectItem> selectedItems = treeModel1.getSelectNode();
                        //复选框选中
                        if (treeNode.isChecked() == true) {
                            //利用标记的isOU做判断
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {
                                List<AuditTask> listTask = null;
                                if ("root".equals(treeNode.getId())) {
                                    listTask = sghdService.findAuditTaskByareacode(areacode);
                                } else {
                                    listTask = sghdService.selectAuditTaskOuByObjectGuid(treeNode.getId(), areacode);
                                }
                                if (StringUtil.isNotBlank(itemcategory)
                                        && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                    for (int i = 0; i < listTask.size(); i++) {
                                        for (int j = 0; j < selectedItems.size(); j++) {
                                            if ((listTask.get(i).getRowguid() + "&" + listTask.get(i).getTask_id() + "&"
                                                    + listTask.get(i).getItem_id())
                                                    .equals(selectedItems.get(j).getValue())) {
                                                selectedItems.remove(j);
                                            }
                                        }
                                        selectedItems.add(new SelectItem(listTask.get(i).getRowguid() + "&"
                                                + listTask.get(i).getTask_id() + "&" + listTask.get(i).getItem_id(),
                                                "("
                                                        + listTask.get(i).getItem_id()
                                                        .substring(listTask.get(i).getItem_id().length() - 3)
                                                        + ")" + listTask.get(i).getTaskname().replace(",", "，")));
                                    }

                                } else {
                                    for (int i = 0; i < listTask.size(); i++) {
                                        for (int j = 0; j < selectedItems.size(); j++) {
                                            if (listTask.get(i).getTask_id()
                                                    .equals(((String) selectedItems.get(j).getValue()).split("&")[1])) {
                                                selectedItems.remove(j);
                                            }
                                        }
                                        selectedItems.add(new SelectItem(
                                                listTask.get(i).getRowguid() + "&" + listTask.get(i).getTask_id() + "&"
                                                        + listTask.get(i).getItem_id(),
                                                listTask.get(i).getTaskname().replace(",", "，")));
                                    }

                                }
                            } else {
                                SqlConditionUtil sql = new SqlConditionUtil();
                                String item_id = (String) treeNode.getColumns().get("item_id");
                                if (StringUtil.isBlank(item_id)) {
                                    return treeModel1.getSelectNode();
                                }
                                String itemleft = item_id.substring(0, item_id.length() - 3);
                                List<AuditTask> tasklist = new ArrayList<AuditTask>();
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("areacode", areacode);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.leftLike("item_id", itemleft);
                                tasklist = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                        .getResult().getList();
                                for (AuditTask auditTask : tasklist) {
                                    if (auditTask.getItem_id().length() < 31) {
                                        continue;
                                    }
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (auditTask.getTask_id().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                    selectedItems.add(new SelectItem(
                                            auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&"
                                                    + auditTask.getItem_id(),
                                            "(" + auditTask.getItem_id().substring(auditTask.getItem_id().length() - 3)
                                                    + ")" + auditTask.getTaskname()));
                                }
                            }
                        }
                        //复选框取消选中
                        else {
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {

                                if ("".equals(treeNode.getId()) || "root".equals(treeNode.getId())) {
                                    selectedItems.clear();
                                } else {
                                    List<AuditTask> listTask = sghdService
                                            .selectAuditTaskOuByObjectGuid(treeNode.getId(), areacode);

                                    for (int i = 0; i < listTask.size(); i++) {
                                        for (int j = 0; j < selectedItems.size(); j++) {
                                            if (((String) selectedItems.get(j).getValue()).split("&")[1]
                                                    .equals(listTask.get(i).getTask_id())) {
                                                selectedItems.remove(j);
                                            }
                                        }
                                    }
                                }
                            } else {

                                SqlConditionUtil sql = new SqlConditionUtil();
                                String item_id = (String) treeNode.getColumns().get("item_id");
                                if (StringUtil.isBlank(item_id)) {
                                    return treeModel1.getSelectNode();
                                }
                                String itemleft = item_id.substring(0, item_id.length() - 3);
                                List<AuditTask> tasklist = new ArrayList<AuditTask>();
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("areacode", areacode);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.leftLike("item_id", itemleft);
                                tasklist = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                        .getResult().getList();
                                for (AuditTask auditTask : tasklist) {
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (auditTask.getTask_id()
                                                .equals(((String) selectedItems.get(j).getValue()).split("&")[1])) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                }
                            }
                        }
                        return selectedItems;
                    }
                };
                if (!isPostback()) {
                    setSelectNode();
                }
            }
            EpointFrameDsManager.commit();

        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
            return treeModel1;
        }

    }

    public void setSelectNode() {
        List<SelectItem> selectedItems = new ArrayList<SelectItem>();
        List<String> taskids = auditWindowImpl.getTaskidsByWindow(windowGuid).getResult();
        AuditTask auditTask;
        List<AuditTask> listTask;
        String itemid;
        //大项不可用的item_id
        List<String> unableitemid = new ArrayList<String>();
        for (String taskid : taskids) {
            auditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(taskid).getResult();
            if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                if (auditTask == null) {
                    continue;
                }
                if (auditTask.getItem_id().length() <= 3) {
                    continue;
                }
                //如果是小项，并且大项被禁用或者删除，则不添加进选中列表

                itemid = auditTask.getItem_id();
                itemid = itemid.substring(0, itemid.length() - 3) + "000";
                if (unableitemid.contains(itemid)) {
                    continue;
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                sql.eq("areacode", areacode);
                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                sql.notin("SHENPILB", ZwfwConstant.TASK_SHENPILB_CF);
                sql.eq("item_id", itemid);
                listTask = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc").getResult()
                        .getList();
                if (listTask == null || listTask.size() == 0) {
                    if (!unableitemid.contains(itemid)) {
                        unableitemid.add(itemid);
                    }
                    continue;
                }

                selectedItems.add(new SelectItem(
                        auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + auditTask.getItem_id(),
                        "(" + auditTask.getItem_id().substring(auditTask.getItem_id().length() - 3) + ")"
                                + auditTask.getTaskname()));
            } else {
                if (auditTask != null) {
                    selectedItems.add(new SelectItem(
                            auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + auditTask.getItem_id(),
                            auditTask.getTaskname()));
                }
            }
        }
        treeModel1.setSelectNode(selectedItems);
    }

    /**
     * 把事项和窗口信息保存到关系表里面
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
                // 获取新窗口事项list
                List<String> newtaskidlist = new ArrayList<>();
                log.info("删除窗口："+windowGuid+",操作人："+userSession.getDisplayName());
                auditWindowImpl.deleteWindowTaskByWindowGuid(windowGuid);
                // 添加新的事项关联关系
                if (StringUtil.isNotBlank(guidList)) {
                    String[] taskGuids = guidList.split(";");
                    if (StringUtil.isNotBlank(windowGuid)) {
                        for (int i = 0; i < taskGuids.length; i++) {
                            String[] guids = taskGuids[i].split("&");
                            if (guids.length == 3) {
                                AuditOrgaMemberTask auditWindowTask = new AuditOrgaMemberTask();
                                auditWindowTask.setRowguid(UUID.randomUUID().toString());
                                auditWindowTask.setMemberguid(windowGuid);
                                auditWindowTask.setTaskguid(guids[0]);
                                auditWindowTask.setOperatedate(new Date());
                                auditWindowTask.setOrdernum(taskGuids.length - i);
                                auditWindowTask.setTaskid(guids[1]);
                                auditWindowTask.setEnabled("1");// 插入的数据默认为有效
                                auditWindowImpl.insertWindowTask(auditWindowTask);
                                newtaskidlist.add(guids[1]);
                            }
                        }
                    }
                }
                EpointFrameDsManager.commit();
            }
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            log.info("========Exception信息========" + e.getMessage());
        }
    }


    public String getWindowGuid() {
        return windowGuid;
    }

    public void setWindowGuid(String windowGuid) {
        this.windowGuid = windowGuid;
    }

    public AuditOrgaMemberTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOrgaMemberTask dataBean) {
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
            List<AuditOrgaMemberTask> auditWindowList = auditWindowImpl.getTaskByWindow(windowGuid).getResult();
            if (auditWindowList != null && auditWindowList.size() > 0) {
                for (AuditOrgaMemberTask auditWindowTask : auditWindowList) {
                    AuditTask auditTask = auditTaskBasicImpl.selectUsableTaskByTaskID(auditWindowTask.getTaskid())
                            .getResult();
                    if (auditTask != null && ZwfwConstant.CONSTANT_INT_ONE == auditTask.getIs_enable()) {
                        taskList.add(auditTask);
                    }
                }
            }
        }
        if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
            List<String> ids = new ArrayList<String>();
            if (taskList != null && taskList.size() > 0) {
                for (AuditTask task : taskList) {
                    if (task.getItem_id().length() > 3 && "000".equals(
                            task.getItem_id().substring(task.getItem_id().length() - 3, task.getItem_id().length()))) {
                        ids.add(task.getItem_id().substring(0, task.getItem_id().length() - 3));
                    }
                }
                List<AuditTask> removetask = new ArrayList<>();
                for (AuditTask task : taskList) {
                    if (task.getItem_id().length() > 3
                            && !ids.contains(task.getItem_id().substring(0, task.getItem_id().length() - 3))) {
                        removetask.add(task);
                    }
                }
                taskList.removeAll(removetask);
            }
        }
        return taskList;
    }

}

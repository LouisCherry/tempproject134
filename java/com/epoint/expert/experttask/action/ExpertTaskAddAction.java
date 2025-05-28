package com.epoint.expert.experttask.action;

import java.util.*;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
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
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.experttask.api.IExpertTaskService;
import com.epoint.expert.experttask.api.entity.ExpertTask;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 专家关联事项表新增页面对应的后台
 *
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:57]
 */
@RightRelation(ExpertTaskListAction.class)
@RestController("experttaskaddaction")
@Scope("request")
public class ExpertTaskAddAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 3026856721986759168L;
    @Autowired
    private IExpertTaskService service;

    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Autowired
    private IHandleConfig configService;

    /**
     * 专家关联事项表实体对象
     */
    private ExpertTask dataBean = null;

    private TreeModel treeModel = null;

    private String fields = "rowguid,task_id,item_id,type,taskname,ouguid";

    private String itemcategory = "";


    private String areacode = "";

    private String centerguid = "";


    public void pageLoad() {
        dataBean = new ExpertTask();
        itemcategory = configService.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
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

    public TreeModel getTaskTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
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
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    } else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            //以下是框架封装的方法，具体参数请参见api
                            List<AuditTask> listsearchTask = new ArrayList<AuditTask>();
                            if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.like("taskname", treeNode.getSearchCondition());
                                sql.eq("areacode", areacode);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                listsearchTask = auditTaskBasicImpl.getAuditEnableTaskPageData(fields, sql.getMap(), 0, 0, "ordernum", "desc").getResult().getList();

                            } else {
                                listsearchTask = auditTaskBasicImpl
                                        .selectAuditTaskByCondition(treeNode.getSearchCondition(),
                                                areacode)
                                        .getResult();
                            }
                            //搜索完成后，进行树的绑定
                            for (int j = 0; j < listsearchTask.size(); j++) {
                                TreeNode node = new TreeNode();
                                node.setId(listsearchTask.get(j).getRowguid() + "&" + listsearchTask.get(j).getTask_id() + "&" + listsearchTask.get(j).getItem_id());
                                node.setText(listsearchTask.get(j).getTaskname());
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
                                    if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                        sql.clear();
                                        sql.eq("OUGUID", treeData.getObjectGuid());
                                        sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                        sql.eq("areacode", areacode);
                                        sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                        sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                        sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                        sql.rightLike("item_id", "000");
                                        listTask = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                                .getResult().getList();
                                    } else {
                                        listTask = auditTaskBasicImpl.selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(),
                                                areacode).getResult();
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
                                        sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                        sql.leftLike("item_id", itemleft);
                                        sql.nq("item_id", item_id);
                                        listTask = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                                .getResult().getList();
                                    }
                                }

                            }

                            // 部门绑定
                            for (int i = 0; i < listOu.size(); i++) {
                                if ("root".equals(objectGuid)
                                        || (listOu.get(i).getParentOuguid().equals(objectGuid))) {
                                    TreeNode node = new TreeNode();
                                    node.setId(listOu.get(i).getOuguid());
                                    node.setText(listOu.get(i).getOuname());
                                    if ("root".equals(objectGuid)) {
                                        node.setPid("root");
                                    } else {
                                        node.setPid(listOu.get(i).getParentOuguid());
                                    }
                                    node.setCkr(false);
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
                                        sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                        sql.rightLike("item_id", "000");
                                        taskCount = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                                .getResult().getRowCount();

                                    } else {
                                        taskCount = auditTaskBasicImpl
                                                .selectAuditTaskOuByObjectGuid(listOu.get(i).getOuguid(),
                                                        areacode)
                                                .getResult().size();
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
                                TreeNode node2 = new TreeNode();
                                node2.setId(listTask.get(j).getRowguid() + "&" + listTask.get(j).getTask_id() + "&" + listTask.get(j).getItem_id());
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
                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                    itemleft = listTask.get(j).getItem_id();
                                    itemleft = itemleft.substring(0, itemleft.length() - 3);
                                    sql.nq("item_id", listTask.get(j).getItem_id());
                                    sql.leftLike("item_id", itemleft);
                                    if (listTask.get(j).getItem_id().endsWith("000")) {
                                        taskCount = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 1, "ordernum", "desc")
                                                .getResult().getRowCount();
                                        node2.setText("(" + listTask.get(j).getItem_id().substring(listTask.get(j).getItem_id().length() - 3) + ")" + listTask.get(j).getTaskname());
                                    } else {
                                        node2.setPid(objectGuid);
                                        node2.setText("(" + listTask.get(j).getItem_id().substring(listTask.get(j).getItem_id().length() - 3) + ")" + listTask.get(j).getTaskname());
                                    }
                                    if (taskCount > 0) {
                                        node2.setLeaf(false);
                                        node2.getColumns().put("item_id", listTask.get(j).getItem_id());
                                    }
                                }

                                list.add(node2);

                            }
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
                    List<SelectItem> selectedItems = treeModel.getSelectNode();
                    //复选框选中
                    if (treeNode.isChecked() == true) {
                        //利用标记的isOU做判断
                        if ("true".equals(treeNode.getColumns().get("isOU"))) {
                            List<AuditTask> listTask = auditTaskBasicImpl.selectAuditTaskOuByObjectGuid(treeNode.getId(),
                                    areacode).getResult();
                            if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                for (int i = 0; i < listTask.size(); i++) {

                                    Iterator<SelectItem> it = selectedItems.iterator();
                                    while (it.hasNext()) {
                                        SelectItem temp = it.next();
                                        if ((listTask.get(i).getRowguid() + "&" + listTask.get(i).getTask_id() + "&" + listTask.get(i).getItem_id()).equals(temp.getValue())) {
                                            it.remove();
                                        }
                                    }
                                    selectedItems.add(
                                            new SelectItem(listTask.get(i).getRowguid() + "&" + listTask.get(i).getTask_id() + "&" + listTask.get(i).getItem_id(), "(" + listTask.get(i).getItem_id().substring(listTask.get(i).getItem_id().length() - 3) + ")" + listTask.get(i).getTaskname().replace(",", "，")));
                                }

                            } else {
                                for (int i = 0; i < listTask.size(); i++) {

                                    Iterator<SelectItem> it = selectedItems.iterator();
                                    while (it.hasNext()) {
                                        SelectItem temp = it.next();
                                        if (listTask.get(i).getTask_id().equals(((String) temp.getValue()).split("&")[1])) {
                                            it.remove();
                                        }
                                    }
                                    selectedItems.add(
                                            new SelectItem(listTask.get(i).getRowguid() + "&" + listTask.get(i).getTask_id() + "&" + listTask.get(i).getItem_id(), listTask.get(i).getTaskname().replace(",", "，")));
                                }

                            }
                        } else {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            String item_id = (String) treeNode.getColumns().get("item_id");
                            if (StringUtil.isBlank(item_id)) {
                                return treeModel.getSelectNode();
                            }
                            String itemleft = item_id.substring(0, item_id.length() - 3);
                            List<AuditTask> tasklist = new ArrayList<AuditTask>();
                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.eq("areacode", areacode);
                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.leftLike("item_id", itemleft);
                            tasklist = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                    .getResult().getList();
                            for (AuditTask auditTask : tasklist) {
                                if (auditTask.getItem_id().length() < 31) {
                                    continue;
                                }
                                Iterator<SelectItem> it = selectedItems.iterator();
                                while (it.hasNext()) {
                                    SelectItem temp = it.next();
                                    if (auditTask.getTask_id().equals(temp.getValue())) {
                                        it.remove();
                                    }
                                }
                                selectedItems.add(new SelectItem(auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + auditTask.getItem_id(), "(" + auditTask.getItem_id().substring(auditTask.getItem_id().length() - 3) + ")" + auditTask.getTaskname()));
                            }
                        }
                    }
                    //复选框取消选中
                    else {
                        if ("true".equals(treeNode.getColumns().get("isOU"))) {

                            if ("".equals(treeNode.getId())) {
                                selectedItems.clear();
                            } else {
                                List<AuditTask> listTask = auditTaskBasicImpl.selectAuditTaskOuByObjectGuid(treeNode.getId(),
                                        areacode).getResult();
                                for (int i = 0; i < listTask.size(); i++) {
                                    Iterator<SelectItem> it = selectedItems.iterator();
                                    while (it.hasNext()) {
                                        SelectItem temp = it.next();
                                        if (((String) temp.getValue()).split("&")[1].equals(listTask.get(i).getTask_id())) {
                                            {
                                                it.remove();
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            String item_id = (String) treeNode.getColumns().get("item_id");
                            if (StringUtil.isBlank(item_id)) {
                                return treeModel.getSelectNode();
                            }
                            String itemleft = item_id.substring(0, item_id.length() - 3);
                            List<AuditTask> tasklist = new ArrayList<AuditTask>();
                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.eq("areacode", areacode);
                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.leftLike("item_id", itemleft);
                            tasklist = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                    .getResult().getList();
                            for (AuditTask auditTask : tasklist) {
                                Iterator<SelectItem> it = selectedItems.iterator();
                                while (it.hasNext()) {
                                    SelectItem temp = it.next();
                                    if (auditTask.getTask_id().equals(((String) temp.getValue()).split("&")[1])) {
                                        {
                                            it.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return selectedItems;
                }
            }

            ;
            if (!

                    isPostback()) {
                setSelectNode();
            }
        }
        return treeModel;
    }

    public void setSelectNode() {
        List<SelectItem> selectedItems = new ArrayList<SelectItem>();
        List<String> taskids = service.getTaskId(getRequestParameter("expertinfo"));
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
                itemid = itemid.substring(0, itemid.length() - 2);
                if (unableitemid.contains(itemid)) {
                    continue;
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                sql.eq("areacode", areacode);
                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
//                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                sql.eq("item_id", itemid);
                listTask = auditTaskBasicImpl.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                        .getResult().getList();
                if (CollectionUtils.isNotEmpty(listTask) && listTask.get(0).getIs_enable() != 1) {
                    unableitemid.add(itemid);
                    continue;
                }

                selectedItems.add(new SelectItem(auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + auditTask.getItem_id(), "(" + auditTask.getItem_id().substring(auditTask.getItem_id().length() - 3) + ")" + auditTask.getTaskname()));
            } else {
                if (auditTask != null) {
                    selectedItems.add(new SelectItem(auditTask.getRowguid() + "&" + auditTask.getTask_id() + "&" + auditTask.getItem_id(), auditTask.getTaskname()));
                }
            }
        }
        treeModel.setSelectNode(selectedItems);
    }

    /**
     * 把事项保存到关系表里面
     *
     * @param guidList   前台获取的事项guid 用“;”分割
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveTask(String guidList, String expertinfoguid) {
        try {
            if (StringUtil.isNotBlank(expertinfoguid)) {
                EpointFrameDsManager.begin(null);
                // 获取原窗口事项list
                List<String> oldtaskidlist = new ArrayList<>();
                // 获取新窗口事项list
                List<String> newtaskidlist = new ArrayList<>();

                oldtaskidlist = service.getTaskId(expertinfoguid);

                service.deleteByExpertguid(expertinfoguid);
                // 添加新的事项关联关系
                if (StringUtil.isNotBlank(guidList)) {
                    String[] taskGuids = guidList.split(";");
                    if (StringUtil.isNotBlank(expertinfoguid)) {
                        for (int i = 0; i < taskGuids.length; i++) {
                            String[] guids = taskGuids[i].split("&");
                            if (guids.length == 3) {
                                ExpertTask expertTask = new ExpertTask();
                                expertTask.setRowguid(UUID.randomUUID().toString());
                                expertTask.setOperatedate(new Date());
                                expertTask.setExpertguid(expertinfoguid);
                                expertTask.setTask_id(guids[1]);
                                expertTask.setOperateusername(userSession.getDisplayName());
                                service.insert(expertTask);
                                newtaskidlist.add(guids[1]);
                            }
                        }
                    }
                }
                EpointFrameDsManager.commit();
                // 并集去除重复项
                List<String> oldtaskidlisttemp = new ArrayList<>(oldtaskidlist);
                List<String> newtaskidlisttemp = new ArrayList<>(newtaskidlist);
                oldtaskidlisttemp.retainAll(newtaskidlisttemp);
                oldtaskidlist.addAll(newtaskidlist);
                oldtaskidlist.removeAll(oldtaskidlisttemp);
                addCallbackParam("msg", "关联成功！");
            }
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
    }

    public ExpertTask getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertTask();
        }
        return dataBean;
    }

    public void setDataBean(ExpertTask dataBean) {
        this.dataBean = dataBean;
    }

}

package com.epoint.expert.expertinstance.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 专家抽取实例表新增页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:41:50]
 */

@RestController("taskselecttreeaction")
@Scope("request")
public class TaskSelectTreeAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    @Autowired
    private IHandleFrameOU frameOu;
    private TreeModel treeModel = null;
    private String fields = "rowguid,task_id,item_id,type,taskname,ouguid";

    public void pageLoad() {

    }

    public TreeModel getTaskTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
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
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        if ("root".equals(objectGuid)) {
                            listOu = frameOu.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(), true)
                                    .getResult();
                        }
                        else {
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {
                                sql.clear();
                                sql.eq("OUGUID", treeData.getObjectGuid());
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                listTask = auditTaskBasicImpl
                                        .getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc").getResult()
                                        .getList();
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
                                }
                                else {
                                    node.setPid(listOu.get(i).getParentOuguid());
                                }
                                node.setCkr(false);
                                node.getColumns().put("isOU", "true");//标记：是部门节点
                                node.setLeaf(false);
                                int taskCount = 0;
                                sql.clear();
                                sql.eq("OUGUID", listOu.get(i).getOuguid());
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                taskCount = auditTaskBasicImpl.getTasknumByCondition(sql.getMap()).getResult();
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
                            node2.setId(listTask.get(j).getRowguid() + "&" + listTask.get(j).getTask_id() + "&"
                                    + listTask.get(j).getItem_id());
                            node2.setText(listTask.get(j).getTaskname().replace(",", "，"));
                            node2.setPid(listTask.get(j).getOuguid());
                            node2.getColumns().put("isOU", "false");//标记：不是部门节点
                            node2.setLeaf(true);
                            sql.clear();
                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                            itemleft = listTask.get(j).getItem_id();
                            itemleft = itemleft.substring(0, itemleft.length() - 3);
                            sql.nq("item_id", listTask.get(j).getItem_id());
                            sql.leftLike("item_id", itemleft);
                                node2.setPid(objectGuid);
                                node2.setText(listTask.get(j).getTaskname());
                            if (taskCount > 0) {
                                node2.setLeaf(false);
                                node2.getColumns().put("item_id", listTask.get(j).getItem_id());
                            }
                            list.add(node2);

                        }
                    }

                    return list;
                }
            };
            /*      if (!isPostback()) {
                setSelectNode();
            }*/
        }
        return treeModel;
    }

}

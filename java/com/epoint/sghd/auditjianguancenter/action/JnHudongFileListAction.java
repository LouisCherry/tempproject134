package com.epoint.sghd.auditjianguancenter.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IAuditTaskShareFile;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 中心政策文件action
 *
 * @version [版本号, 2018年10月11日]
 * @作者 shibin
 */
@RestController("jnhudongfilelistaction")
@Scope("request")
public class JnHudongFileListAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private DataGridModel<AuditTaskShareFile> model;

    private AuditTaskShareFile dateBean;

    private String taskname;

    private String areaCode;

    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;

    /**
     * 树控件model
     */
    private TreeModel treeModel;

    @Autowired
    private IAuditTaskShareFile shareService;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    @Override
    public void pageLoad() {
        areaCode = ZwfwUserSession.getInstance().getAreaCode();
        String ouguid = this.getRequestParameter("ouguid");
        if (!"undefined".equals(ouguid)) {
            leftTreeNodeGuid = ouguid;
        }
    }

    public DataGridModel<AuditTaskShareFile> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskShareFile>() {

                @Override
                public List<AuditTaskShareFile> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    String sql = "select * from audit_task_sharefile s INNER JOIN frame_ou_extendinfo f ON s.ouguid = f.OUGUID WHERE 1=1 ";
                    String sqlnum = "select count(1) from audit_task_sharefile s INNER JOIN frame_ou_extendinfo f ON s.ouguid = f.OUGUID WHERE 1=1 ";
                    // 搜索条件
                    if (StringUtil.isNotBlank(taskname)) {
                        sql += " and taskname like '%" + taskname + "%' ";
                        sqlnum += " and taskname like '%" + taskname + "%' ";
                    }
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql += " and s.ouguid = '" + leftTreeNodeGuid + "' ";
                        sqlnum += " and s.ouguid = '" + leftTreeNodeGuid + "' ";
                    }

                    // 适配区县
                    sql += " and AREACODE = '" + areaCode + "' ";
                    sqlnum += " and AREACODE = '" + areaCode + "' ";

                    sql = sql + " order by s.createtime " + sortOrder + " limit ?,? ";

                    List<AuditTaskShareFile> list = new ArrayList<>();
                    list = shareService.getShareFileInfoByAreacode(sql, first, pageSize);
                    int listnum = shareService.getShareFileInfoNumByAreacode(sqlnum);

                    this.setRowCount(listnum);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 构建树结构
     *
     * @return
     */
    public TreeModel getOUTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                private static final long serialVersionUID = 1L;

                /**
                 * 树加载事件
                 *
                 * @param treeNode
                 *            当前展开的节点
                 * @return List<TreeNode>
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();

                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    } else {

                        List<Record> listOu = new ArrayList<>();
                        listOu = jnAuditJianGuanCenter.listOuByAreacode(areaCode);
                        for (int i = 0; i < listOu.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(listOu.get(i).getStr("OUGUID"));
                            node.setText(listOu.get(i).getStr("OUSHORTNAME"));
                            node.setPid("f9root");
                            node.setLeaf(true);

                            list.add(node);
                        }
                    }
                    return list;
                }
            };
        }
        return treeModel;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public AuditTaskShareFile getDateBean() {
        return dateBean;
    }

    public void setDateBean(AuditTaskShareFile dateBean) {
        this.dateBean = dateBean;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

}

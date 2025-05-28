package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 中心已发起的许可变更意见action
 *
 * @version [版本号, 2018年10月8日]
 * @作者 shibin
 */
@RestController("auditprojectyifaqicenterlistaction")
@Scope("request")
public class AuditProjectYiFaQiCenterListAction extends BaseController {

    private static final long serialVersionUID = -4046499456177644472L;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectPermissionChange> model = null;

    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;

    /**
     * 树控件model
     */
    private TreeModel treeModel;

    private String ouGuid;
    private String areaCode;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    @Override
    public void pageLoad() {

        areaCode = ZwfwUserSession.getInstance().getAreaCode();
        ouGuid = this.getRequestParameter("ouguid");
        if (!"undefined".equals(ouGuid)) {
            leftTreeNodeGuid = ouGuid;
        }

    }

    public DataGridModel<AuditProjectPermissionChange> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectPermissionChange>() {

                @Override
                public List<AuditProjectPermissionChange> fetchData(int first, int pageSize, String sortField,
                                                                    String sortOrder) {

                    List<AuditProjectPermissionChange> list = new ArrayList<>();
                    int num = 0;
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        ouGuid = leftTreeNodeGuid;
                        list = jnAuditJianGuanService.getAuditProjectPermissionByOuguid(areaCode, ouGuid, first,
                                pageSize);
                        num = jnAuditJianGuanService.getAuditProjectPermissionNumByOuguid(areaCode, ouGuid);
                    } else {
                        list = jnAuditJianGuanService.getAuditProjectPermission(areaCode, first, pageSize);
                        num = jnAuditJianGuanService.getAuditProjectPermissionNum(areaCode);
                    }

                    this.setRowCount(num);
                    return list;
                }

            };
        }
        return model;

    }

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

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

}

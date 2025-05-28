package com.epoint.jnzwfw.sdwaithandle.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 事项登记list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("audittaskycsx")
@Scope("request")
public class AuditTaskYcsx extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 审批事项基本信息实体对象
     */
    private AuditTask dataBean = new AuditTask();

    private String selectGuid;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;

    /**
     * 辖区树
     */
    private TreeModel treeModel;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditOrgaArea auditOrgaArea;

    @Override
    public void pageLoad() {
        if(!isPostback()){
            selectGuid =  ZwfwUserSession.getInstance().getAreaCode();
        }else{
            selectGuid = getSelectGuid();
        }
    }

    public DataGridModel<AuditTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>()
            {

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("IS_SAMECITY", ZwfwConstant.CONSTANT_STR_ONE);
                    System.err.println(selectGuid);
                    if (StringUtil.isNotBlank(selectGuid)) {
                        sql.like("areacode", selectGuid);
                    }
                    if (StringUtil.isNotBlank(dataBean.getTaskname())) {
                        sql.like("taskname", dataBean.getTaskname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getOuname())) {
                        sql.like("ouname", dataBean.getOuname());
                    }
                    sql.eq("is_history", ZwfwConstant.CONSTANT_STR_ZERO);
                    sql.eq("is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
                    sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                  //  String fields = "*";
                    PageData<AuditTask> pageData = auditTaskService
                            .getAuditTaskPageData( sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    List<AuditTask> auditTasks = pageData.getList();
                    AuditOrgaArea area = null;
                    if (auditTasks != null) {
                        for (AuditTask auditTask : auditTasks) {
                            area = auditOrgaArea.getAreaByAreacode(auditTask.getAreacode()).getResult();
                            if (area != null) {
                                auditTask.set("areaname", area.getXiaquname());
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

    public TreeModel getTreeData() {

        if (treeModel == null) {
            treeModel = new TreeModel()
            {
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
                        root.setText("所有辖区");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();

                        // 框架方法获取Module，具体方法参见api
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.setOrder("xiaqucode", "ASC");
                        sql.isBlank("BaseAreaCode");
                        List<AuditOrgaArea> areaList = auditOrgaArea.selectAuditAreaList(sql.getMap()).getResult();

                        for (int i = 0; i < areaList.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(areaList.get(i).getXiaqucode());
                            node.setText(areaList.get(i).getXiaquname());
                            node.setPid(objectGuid);
                            node.setLeaf(true);
                            list.add(node);
                        }
                    }
                    return list;
                }

                /**
                 * 树节点点击事件
                 *
                 * @param item
                 *            当前点击的树节点
                 * @return List<com.epoint.core.dto.model.SelectItem>
                 */
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode item) {
                    // TODO Auto-generated method stub
                    return super.onLazyNodeSelect(item);
                }
            };
        }
        return treeModel;
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

    public String getSelectGuid() {
        return selectGuid;
    }

    public void setSelectGuid(String selectGuid) {
        this.selectGuid = selectGuid;
    }

}

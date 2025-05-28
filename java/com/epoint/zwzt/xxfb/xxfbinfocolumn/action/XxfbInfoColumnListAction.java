package com.epoint.zwzt.xxfb.xxfbinfocolumn.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.zwzt.xxfb.constant.XxfbConstant;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.IXxfbInfoColumnService;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 信息栏目表list页面对应的后台
 *
 * @author D0Be
 * @version [版本号, 2022-04-25 13:57:31]
 */
@RestController("xxfbinfocolumnlistaction")
@Scope("request")
public class XxfbInfoColumnListAction extends BaseController
{
    /**
     * 信息栏目表service
     */
    @Autowired
    private IXxfbInfoColumnService service;
    /**
     * 角色权限service
     */
    @Autowired
    private IRoleService roleService;
    /**
     * 信息栏目表实体对象
     */
    private XxfbInfoColumn dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<XxfbInfoColumn> model;

    /**
     * 栏目编号类型下拉列表model
     */
    private List<SelectItem> info_column_typeModel = null;
    /**
     * 启用单选按钮组model
     */
    private List<SelectItem> is_enableModel = null;
    private TreeModel treeModel;

    /**
     * 选中的栏目rowguid
     */
    private String leftTreeNodeGuid;
    boolean isAdminRole = false;
    boolean isFBRole = false;
    boolean isSHRole = false;

    public void pageLoad() {
        isAdminRole = roleService.isExistUserRoleName(userSession.getUserGuid(), "信息发布管理员");
        addCallbackParam("isAdminRole", isAdminRole);
        isFBRole = roleService.isExistUserRoleName(userSession.getUserGuid(), "信息发布人-组件");
        addCallbackParam("isFBRole", isFBRole);
        isSHRole = roleService.isExistUserRoleName(userSession.getUserGuid(), "信息审核人-组件");
        addCallbackParam("isSHRole", isSHRole);
        addCallbackParam("hasType", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_COLUMN_TYPE_IS));
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {

                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有栏目");
                        root.setId("f9root");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    // 每次点击树节点前的加号，进行加载
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        SqlConditionUtil sqlUtil = new SqlConditionUtil();
                        sqlUtil.eq("parent_column_rowguid", StringUtil.isNotBlank(objectGuid) ? objectGuid : "f9root");
                        sqlUtil.setOrderAsc("create_time");
                        List<XxfbInfoColumn> columnList = service.findList(sqlUtil.getMap());

                        // 部门的绑定
                        for (int i = 0; i < columnList.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(columnList.get(i).getRowguid());
                            node.setText(columnList.get(i).getInfo_column_name());
                            node.setPid(columnList.get(i).getParent_column_rowguid());
                            node.setCkr(true);
                            node.setLeaf(true);
                            sqlUtil.clear();
                            sqlUtil.eq("parent_column_rowguid", columnList.get(i).getRowguid());
                            List<XxfbInfoColumn> subList = service.findList(sqlUtil.getMap());
                            if (!subList.isEmpty()) {
                                node.setLeaf(false);
                            }
                            list.add(node);
                        }
                    }
                    return list;
                }

            };
        }
        return treeModel;
    }

    public void deleteColumn(String rowguid) {
        if (StringUtil.isNotBlank(rowguid)) {
            service.deleteByGuid(rowguid);
        }
        addCallbackParam("msg", "删除成功！");
    }

    public void toggleColumn(String rowguid, String field) {
        if (StringUtil.isNotBlank(rowguid)) {
            XxfbInfoColumn column = service.find(rowguid);
            if (column != null && !column.isEmpty()) {
                column.setIs_enable("1".equals(column.getIs_enable()) ? "0" : "1");
                service.update(column);
                addCallbackParam("msg", field + "成功");
            }
        }
    }

    public void checkEnableCheck() {
        boolean flag = true;
        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
            XxfbInfoColumn column = service.find(leftTreeNodeGuid);
            if (column != null && !column.isEmpty() && StringUtil.isNotBlank(column.getColumn_number())
                    && column.getColumn_number().length() / 3 >= Integer.parseInt(XxfbConstant.ZWZT_XXFB_INFO_COLUMN_LEVEL)) {
                flag = false;
            }
        }
        addCallbackParam("flag", flag);
        addCallbackParam("maxLevel", XxfbConstant.ZWZT_XXFB_INFO_COLUMN_LEVEL);
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        // 栏目下包含子栏目下有关联信息不可删除
        List<String> notDeletelist = new ArrayList<>();
        List<String> Deletelist = new ArrayList<>();
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        for (String sel : select) {
            String name = service.find(sel).getInfo_column_name();
            sqlUtil.eq("parent_column_rowguid", sel);
            List<XxfbInfoColumn> subList = service.findList(sqlUtil.getMap());

            if (subList != null && !subList.isEmpty()) {
                notDeletelist.add("【" + name + "】");
            }
            else {
                service.deleteByGuid(sel);
                Deletelist.add("【" + name + "】");
            }
        }
        String msg = "";
        if (Deletelist != null && !Deletelist.isEmpty()) {
            msg += StringUtil.join(Deletelist, ",") + "，删除成功<br/>";
        }
        if (notDeletelist != null && !notDeletelist.isEmpty()) {
            msg += StringUtil.join(notDeletelist, ",") + "下包含子栏目，不可删除<br/>";
        }

        addCallbackParam("msg", msg);
    }

    public boolean hasSubColumn(String rowguid) {
        boolean flag = false;
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.eq("parent_column_rowguid", rowguid);
        List<XxfbInfoColumn> subList = service.findList(sqlUtil.getMap());
        if (subList != null && !subList.isEmpty()) {
            flag = true;
        }
        addCallbackParam("flag", flag);
        return flag;
    }

    /**
     * 启用选定
     */
    public void enableSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            XxfbInfoColumn column = service.find(sel);
            if (column != null && !column.isEmpty() && "0".equals(column.getIs_enable())) {
                column.setIs_enable("1");
                service.update(column);
            }
        }
        addCallbackParam("msg", "成功启用！");
    }

    /**
     * 禁用选定
     */
    public void disableSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            XxfbInfoColumn column = service.find(sel);
            if (column != null && !column.isEmpty() && "1".equals(column.getIs_enable())) {
                column.setIs_enable("0");
                service.update(column);
            }
        }
        addCallbackParam("msg", "成功禁用！");
    }

    public DataGridModel<XxfbInfoColumn> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XxfbInfoColumn>()
            {
                @Override
                public List<XxfbInfoColumn> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getInfo_column_name())) {
                        sqlUtil.like("info_column_name", dataBean.getInfo_column_name());
                    }
                    if (StringUtil.isNotBlank(dataBean.getIs_enable())) {
                        sqlUtil.eq("is_enable", dataBean.getIs_enable());
                    }
                    if (StringUtil.isNotBlank(leftTreeNodeGuid) && !"f9root".equals(leftTreeNodeGuid)) {
                        sqlUtil.eq("parent_column_rowguid", leftTreeNodeGuid);
                    }
                    if (isFBRole) {
                        sqlUtil.eq("create_userguid", userSession.getUserGuid());
                    }

                    sortField = StringUtil.isBlank(sortField) ? "ordernum" : sortField;
                    sortOrder = StringUtil.isBlank(sortOrder) ? "desc" : sortOrder;
                    sqlUtil.setOrder(sortField, sortOrder);

                    PageData<XxfbInfoColumn> pageData = service.paginatorList(sqlUtil.getMap(), first, pageSize);
                    List<XxfbInfoColumn> list = pageData.getList();
                    for (XxfbInfoColumn column : list) {
                        String parent_name = "";
                        if (StringUtil.isNotBlank(column.getParent_column_rowguid())) {
                            if (!"f9root".equals(column.getParent_column_rowguid())) {
                                sqlUtil.clear();
                                sqlUtil.setSelectFields("info_column_name");
                                sqlUtil.setSelectCounts(1);
                                sqlUtil.eq("rowguid", column.getParent_column_rowguid());
                                parent_name = service.find(sqlUtil.getMap()).getStr("info_column_name");
                            }
                        }
                        column.setParent_column_rowguid(parent_name);
                    }
                    this.setRowCount(pageData.getRowCount());
                    return list;
                }

            };
        }
        return model;
    }

    public XxfbInfoColumn getDataBean() {
        if (dataBean == null) {
            dataBean = new XxfbInfoColumn();
        }
        return dataBean;
    }

    public void setDataBean(XxfbInfoColumn dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getInfo_column_typeModel() {
        if (info_column_typeModel == null) {
            info_column_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "栏目类型", null, true));
        }
        return this.info_column_typeModel;
    }

    public List<SelectItem> getIs_enableModel() {
        if (is_enableModel == null) {
            is_enableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.is_enableModel;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }
}

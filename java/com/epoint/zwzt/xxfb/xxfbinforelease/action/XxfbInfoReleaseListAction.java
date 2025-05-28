package com.epoint.zwzt.xxfb.xxfbinforelease.action;

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
import com.epoint.zwzt.xxfb.xxfbinforelease.api.IXxfbInfoReleaseService;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.entity.XxfbInfoRelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 信息发布表list页面对应的后台
 *
 * @author D0Be
 * @version [版本号, 2022-04-27 15:34:21]
 */
@RestController("xxfbinforeleaselistaction")
@Scope("request")
public class XxfbInfoReleaseListAction extends BaseController
{
    /**
     * 信息发布表service
     */
    @Autowired
    private IXxfbInfoReleaseService service;
    /**
     * 信息栏目表service
     */
    @Autowired
    private IXxfbInfoColumnService columnService;
    /**
     * 角色权限service
     */
    @Autowired
    private IRoleService roleService;

    /**
     * 信息发布表实体对象
     */
    private XxfbInfoRelease dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<XxfbInfoRelease> model;

    /**
     * 发布人名义下拉列表model
     */
    private List<SelectItem> publisherModel = null;
    /**
     * 发布人类型下拉列表model
     */
    private List<SelectItem> publisher_typeModel = null;
    /**
     * 是否置顶单选按钮组model
     */
    private List<SelectItem> is_topModel = null;
    /**
     * 信息类型下拉列表model
     */
    private List<SelectItem> info_typeModel = null;
    /**
     * 信息状态下拉列表model
     */
    private List<SelectItem> info_statusModel = null;

    /**
     * 所属栏目树
     */
    private TreeModel treeModel;

    /**
     * 所选栏目标识
     */
    private String leftTreeNodeGuid;
    /**
     * 是否为信息管理员权限
     */
    boolean isAdminRole = false;
    /**
     * 是否为信息发布管理员权限
     */
    boolean isFBRole = false;
    /**
     * 是否为信息审核管理员权限
     */
    boolean isSHRole = false;

    public void pageLoad() {
        isAdminRole = roleService.isExistUserRoleName(userSession.getUserGuid(), "信息发布管理员");
        addCallbackParam("isAdminRole", isAdminRole);
        isFBRole = roleService.isExistUserRoleName(userSession.getUserGuid(), "信息发布人-组件");
        addCallbackParam("isFBRole", isFBRole);
        isSHRole = roleService.isExistUserRoleName(userSession.getUserGuid(), "信息审核人-组件");
        addCallbackParam("isSHRole", isSHRole);

        addCallbackParam("isEnableIsHot", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_ISHOT__IS));
    }

    /**
     * 删除选中
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 修改信息状态为已发布
     *
     * @param guid
     */
    public void publishInfo(String guid) {
        if (StringUtil.isNotBlank(guid)) {
            XxfbInfoRelease release = service.find(guid);
            if (release != null && !release.isEmpty()) {
                release.setInfo_status("10");
                service.update(release);
            }
        }
        addCallbackParam("msg", "成功发布！");
    }

    /**
     * 修改信息状态为已下架
     */
    public void downInfo(String guid) {
        if (StringUtil.isNotBlank(guid)) {
            XxfbInfoRelease release = service.find(guid);
            if (release != null && !release.isEmpty()) {
                release.setInfo_status("11");
                service.update(release);
            }
        }
        addCallbackParam("msg", "已下架！");
    }

    /**
     * 置顶选中
     */
    public void enableSelectTop() {
        List<String> select = getDataGridData().getSelectKeys();
        Date nowTime =  new Date();
        for (String sel : select) {
            XxfbInfoRelease release = service.find(sel);
            if (release != null && !release.isEmpty()) {
                release.setIs_top("1");
                release.setOperatedate(nowTime);
                service.update(release);
            }
        }
        addCallbackParam("msg", "成功置顶！");
    }

    /**
     * 发布选中
     */
    public void enablePublishSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            XxfbInfoRelease release = service.find(sel);
            if (release != null && !release.isEmpty() && !"10".equals(release.getInfo_status())) {
                release.setInfo_status("10");
                service.update(release);
            }
        }
        addCallbackParam("msg", "成功发布！");
    }

    public DataGridModel<XxfbInfoRelease> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XxfbInfoRelease>()
            {

                @Override
                public List<XxfbInfoRelease> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getInfo_title())) {
                        sqlUtil.like("info_title", dataBean.getInfo_title());
                    }
                    if (StringUtil.isNotBlank(dataBean.getInfo_status())) {
                        sqlUtil.eq("info_status", dataBean.getInfo_status());
                    }
                    if (StringUtil.isNotBlank(leftTreeNodeGuid) && !"f9root".equals(leftTreeNodeGuid)) {
                        sqlUtil.eq("column_guid", leftTreeNodeGuid);
                    }
                    if (isFBRole) {
                        List<String> guidList = new ArrayList<String>();
                        SqlConditionUtil condition = new SqlConditionUtil();
                        condition.eq("create_userguid", userSession.getUserGuid());
                        condition.setSelectFields("rowguid");
                        List<XxfbInfoColumn> columnList = columnService.findList(condition.getMap());
                        if (columnList != null && !columnList.isEmpty()) {
                            guidList = columnList.stream().map(XxfbInfoColumn::getRowguid).collect(Collectors.toList());
                        }
                        sqlUtil.in("column_guid", StringUtil.joinSql(guidList));
                    }
                    sortField = "is_top desc  , operatedate desc , " + (StringUtil.isBlank(sortField) ?
                            "ordernum" :
                            sortField);
                    sortOrder = StringUtil.isBlank(sortOrder) ? "desc" : sortOrder;
                    sqlUtil.setOrder(sortField, sortOrder);
                    PageData<XxfbInfoRelease> pageData = service.paginatorList(sqlUtil.getMap(), first, pageSize);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    /**
     * 生成栏目树
     *
     * @return 栏目树
     */
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
                        List<XxfbInfoColumn> columnList = columnService.findList(sqlUtil.getMap());

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
                            List<XxfbInfoColumn> subList = columnService.findList(sqlUtil.getMap());
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

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public XxfbInfoRelease getDataBean() {
        if (dataBean == null) {
            dataBean = new XxfbInfoRelease();
        }
        return dataBean;
    }

    public void setDataBean(XxfbInfoRelease dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getPublisherModel() {
        if (publisherModel == null) {
            publisherModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "发布人类型", null, false));
        }
        return this.publisherModel;
    }

    public List<SelectItem> getPublisher_typeModel() {
        if (publisher_typeModel == null) {
            publisher_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "发布人类型", null, false));
        }
        return this.publisher_typeModel;
    }

    public List<SelectItem> getIs_topModel() {
        if (is_topModel == null) {
            is_topModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_topModel;
    }

    public List<SelectItem> getInfo_typeModel() {
        if (info_typeModel == null) {
            info_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "信息类型", null, false));
        }
        return this.info_typeModel;
    }

    public List<SelectItem> getInfo_statusModel() {
        if (info_statusModel == null) {
            info_statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "信息状态", null, false));
        }
        return this.info_statusModel;
    }

}

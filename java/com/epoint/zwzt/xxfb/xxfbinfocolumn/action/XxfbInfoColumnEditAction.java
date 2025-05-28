package com.epoint.zwzt.xxfb.xxfbinfocolumn.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zwzt.xxfb.constant.XxfbConstant;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.IXxfbInfoColumnService;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 信息栏目表修改页面对应的后台
 *
 * @author D0Be
 * @version [版本号, 2022-04-25 13:57:31]
 */
@RightRelation(XxfbInfoColumnListAction.class)
@RestController("xxfbinfocolumneditaction")
@Scope("request")
public class XxfbInfoColumnEditAction extends BaseController
{
    /**
     * 信息栏目表service
     */
    @Autowired
    private IXxfbInfoColumnService service;

    /**
     * 信息栏目表实体对象
     */
    private XxfbInfoColumn dataBean = null;

    /**
     * 栏目编号类型下拉列表model
     */
    private List<SelectItem> info_column_typeModel = null;
    /**
     * 启用单选按钮组model
     */
    private List<SelectItem> is_enableModel = null;
    /**
     * 信息栏目父栏目树
     */
    private TreeModel treeModel;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean != null) {
            if (!isPostback()) {
                String parent_column_rowguid = dataBean.getParent_column_rowguid();
                if (StringUtil.isNotBlank(parent_column_rowguid)) {
                    dataBean.setParent_column_rowguid(parent_column_rowguid);
                    XxfbInfoColumn parent = service.find(parent_column_rowguid);
                    if (parent != null && !parent.isEmpty()) {
                        dataBean.setParent_column_number(parent.getColumn_number());
                        addCallbackParam("parentName", parent.getInfo_column_name());
                    }
                }
                else {
                    dataBean.setParent_column_rowguid("f9root");
                    dataBean.setParent_column_number("f9root");
                }
                String number = generateColumnNumber(parent_column_rowguid);
                dataBean.setColumn_number(number);
            }
        }
        else {
            dataBean = new XxfbInfoColumn();
        }
        addCallbackParam("maxLevel", XxfbConstant.ZWZT_XXFB_INFO_COLUMN_LEVEL);
        boolean hasType = "1".equals(XxfbConstant.ZWZT_XXFB_INFO_COLUMN_TYPE_IS);
        addCallbackParam("hasType", hasType);
    }

    /**
     * 根据父栏目编号生成栏目编号
     *
     * @param parent_column_rowguid 父栏目编号
     * @return 栏目编号
     */
    private String generateColumnNumber(String parent_column_rowguid) {
        // 自动生成生成规则 示例栏目编号为上级栏目编号+上级栏目下子栏目最大编号【没有则为0】加1 每级编号长度为3长度不够前段补全0
        String preStr = "";
        int version = 0;
        int length = 3;
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        // 查询上级栏目编号是否存在
        if (StringUtil.isNotBlank(parent_column_rowguid)) {
            // 存在则将上级栏目编号作为前缀，将上级编号作为查询条件，将上级栏目编号的长度+3作为栏目编号长度
            XxfbInfoColumn parent = service.find(parent_column_rowguid);
            if (parent != null && StringUtil.isNotBlank(parent.getColumn_number())) {
                sqlUtil.leftLike("column_number", parent.getColumn_number());
                length = parent.getColumn_number().length() + 3;
                preStr = parent.getColumn_number();
            }
        }

        // 查询当前级父栏目下符合条件最大编号
        sqlUtil.eq("length(column_number)", length);
        sqlUtil.setSelectFields("column_number");
        sqlUtil.setOrderDesc("column_number");
        sqlUtil.setSelectCounts(1);

        XxfbInfoColumn column = service.find(sqlUtil.getMap());
        // 如果存在则将最大编号加1
        if (column != null && StringUtil.isNotBlank(column.getColumn_number())) {
            version = Integer.parseInt(column.getColumn_number().substring(column.getColumn_number().length() - 3));
        }
        // 如果不存在则将最大编号设置为0
        version++;
        // 将最大编号加1后补全前缀
        return preStr + StringUtils.leftPad(String.valueOf(version), 3, "0");
    }

    /**
     * 获取栏目编号
     *
     * @param parent_column_rowguid 父栏目编号
     */
    public void getColumnNumber(String parent_column_rowguid) {
        String columnNumber = generateColumnNumber(parent_column_rowguid);
        addCallbackParam("columnNumber", columnNumber);
    }

    /**
     * 生成父栏目树
     *
     * @return 父栏目树
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

    /**
     * 保存修改
     */
    public void save() {
        XxfbInfoColumn parent = service.find(dataBean.getParent_column_rowguid());
        if (parent != null) {
            dataBean.setParent_column_number(parent.getColumn_number());
        }
        else if ("f9root".equals(dataBean.getParent_column_rowguid())) {
            dataBean.setParent_column_number("f9root");
        }
        dataBean.setOperatedate(new Date());
        dataBean.setOperateuserguid(userSession.getUserGuid());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public XxfbInfoColumn getDataBean() {
        return dataBean;
    }

    public void setDataBean(XxfbInfoColumn dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getInfo_column_typeModel() {
        if (info_column_typeModel == null) {
            info_column_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "栏目类型", null, false));
        }
        return this.info_column_typeModel;
    }

    public List<SelectItem> getIs_enableModel() {
        if (is_enableModel == null) {
            is_enableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_enableModel;
    }

}

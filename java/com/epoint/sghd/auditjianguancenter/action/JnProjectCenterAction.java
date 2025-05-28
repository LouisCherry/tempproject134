package com.epoint.sghd.auditjianguancenter.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.auditjianguan.impl.SgytSpxxProjectService;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;

/**
 * 中心监管list
 *
 * @author swy
 * @version [版本号, 2018年9月20日]
 */
@RestController("jnprojectcenteraction")
@Scope("request")
public class JnProjectCenterAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();

    /**
     * 事项基本信息实体对象
     */
    private AuditTask audittask = new AuditTask();

    /**
     * 树控件model
     */
    private TreeModel treeModel;

    /**
     * 业务来源
     */
    private List<SelectItem> resourceModel = null;

    /**
     * 办结类型
     */
    private List<SelectItem> banjieTypeModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    @Autowired
    private IAuditProject auditProjectService;

    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;

    private String treeNode;

    String ouGuid = "";

    String areaCode = "";

    private int yrlNum;

    private int wrlNum;

    @Autowired
    private SgytSpxxProjectService sgytSpxxProjectService;

    @Autowired
    private IOuServiceInternal ouService;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    private ExportModel exportModel;

    @Override
    public void pageLoad() {
        areaCode = ZwfwUserSession.getInstance().getAreaCode();
        ouGuid = this.getRequestParameter("ouguid");
        treeNode = this.getRequestParameter("leftTreeNodeGuid");
        if (!"undefined".equals(ouGuid)) {
            leftTreeNodeGuid = ouGuid;
        }
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    dataBean.set("ouguid", leftTreeNodeGuid);
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        FrameOu frameOu = ouService.getOuByOuGuid(leftTreeNodeGuid);
                        dataBean.set("oucode", frameOu != null ? frameOu.getOucode() : "");
                    }
                    PageData<AuditProject> pageData = sgytSpxxProjectService.findProjectVOPageForCenter(first, pageSize,
                            dataBean);
                    setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public void hasProGuid(String rowguid) {
        String areacode;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    /**
     * 构建树结构
     *
     * @return
     */
    public TreeModel getOUTreeModel() {
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
                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "flowsn,projectname,ouname,applyername,banjiedate,renlingtype,jg_ouname,renlingdate,renling_username",
                    "办件编号,事项名称,部门,申请人,办结时间,认领状态,监管部门,认领时间,认领人");
        }
        return exportModel;
    }

    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public List<SelectItem> getResourceModel() {
        if (resourceModel == null) {
            resourceModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "业务来源", null, true));
        }
        return this.resourceModel;
    }

    /**
     * 办结类型
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<SelectItem> getBanjieTypeModel() {
        if (banjieTypeModel == null) {
            banjieTypeModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "办结类型", null, true));
        }
        return this.banjieTypeModel;
    }
}

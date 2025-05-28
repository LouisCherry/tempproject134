package com.epoint.evainstanceck.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;

/**
 * 好差评信息表list页面对应的后台
 * 
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
@RestController("evainstancecklistactionnew")
@Scope("request")
public class EvainstanceCkListActionNew extends BaseController
{
    @Autowired
    private IEvainstanceCkService service;

    /**
     * 好差评信息表实体对象
     */
    private EvainstanceCk dataBean;
    private String areacode = "";
    private TreeModel treeModel = null;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    /**
     * 是否整改下拉列表model
     */
    private List<SelectItem> iszgModel = null;
    /**
     * Evalevel下拉列表model
     */
    private List<SelectItem> evalevelModel = null;
    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid;
    private String RECEIVEUSERNAME;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    List<Record> list = service.findListByouguidTemp(leftTreeNodeGuid, dataBean.getCreatedate(),
                            dataBean.getEvalevel(), dataBean.getIszg(), RECEIVEUSERNAME, first, pageSize);
                    int count = service.countListByouguidTemp(leftTreeNodeGuid, dataBean.getCreatedate(),
                            dataBean.getEvalevel(), dataBean.getIszg(), RECEIVEUSERNAME);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public EvainstanceCk getDataBean() {
        if (dataBean == null) {
            dataBean = new EvainstanceCk();
        }
        return dataBean;
    }

    public void setDataBean(EvainstanceCk dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public List<SelectItem> getIszgModel() {
        if (iszgModel == null) {
            iszgModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, true));
        }
        return this.iszgModel;
    }

    public List<SelectItem> getEvalevelModel() {
        if (evalevelModel == null) {
            evalevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, true));
            evalevelModel.remove(1);// 非常满意
            evalevelModel.remove(1);// 满意

        }
        return this.evalevelModel;
    }

    public TreeModel getTreeModel() {
        areacode = ZwfwUserSession.getInstance().getAreaCode();
        System.out.println(areacode);
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = -7089566877270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");// 标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        List<FrameOu> listRootOu;
                        // 当前登陆人员所在部门区域信息
                        AuditOrgaArea areanow = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                        String areanowOuguid = areanow.getOuguid();
                        // 管理员
                        if ("370800".equals(areacode)) {
                            listRootOu = ouService.listOUByGuid("", 4);
                        }
                        else {
                            listRootOu = ouService.listOUByGuid(areanowOuguid, 4);
                        }

                        // 部门的绑定
                        for (FrameOu frameOu : listRootOu) {
                            TreeNode node = new TreeNode();
                            if (StringUtil.isBlank(frameOu.getParentOuguid())) {
                                FrameOuExtendInfo extendInfo = ouService.getFrameOuExtendInfo(frameOu.getOuguid());
                                node.setId(extendInfo.get("areacode"));
                                node.setText(frameOu.getOuname());
                                if (areanowOuguid.equals(frameOu.getOuguid())) {
                                    node.setPid("f9root");
                                }
                                else {
                                    node.setPid(StringUtil.isBlank(frameOu.getParentOuguid()) ? "f9root"
                                            : frameOu.getParentOuguid());
                                }
                                node.setLeaf(true);
                                list.add(node);
                            }

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

    public String getRECEIVEUSERNAME() {
        return RECEIVEUSERNAME;
    }

    public void setRECEIVEUSERNAME(String rECEIVEUSERNAME) {
        RECEIVEUSERNAME = rECEIVEUSERNAME;
    }

}

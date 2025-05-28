package com.epoint.auditperformance.auditperformanceruledetail.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;

/**
 * 考评细则表list页面对应的后台
 * 
 * @author Willy
 * @version [版本号, 2018-01-09 15:34:36]
 */
@RestController("auditperformancerulemainaction")
@Scope("request")
public class AuditPerformanceRuleMainAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    @Autowired
    private IAuditPerformanceRule ruleservice;
    /**
     * 考评细则表实体对象
     */
    private AuditPerformanceRuleDetail dataBean;
    private TreeModel treeModel = null;
    
    private String leftTreeNodeGuid;

    @Override
    public void pageLoad() {
        if(StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())){
            addCallbackParam("msg", "人员没有分配到中心!");
        }
    }

    public TreeModel getRuleTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 8323462298954520965L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("考评规则");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);
                        list.addAll(fetch(root));
                    }
                    else {
                        List<AuditPerformanceRule> rules = ruleservice
                                .getAllRuleByCenterGuid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                        String objectGuid = treeData.getObjectGuid();
                        TreeNode node = new TreeNode();
                        if (rules != null && rules.size() > 0) {
                            for (AuditPerformanceRule rule : rules) {
                                node = new TreeNode();
                                node.setId(rule.getRowguid());
                                node.setText(rule.getRulename());
                                node.setPid(objectGuid);
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

    public AuditPerformanceRuleDetail getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceRuleDetail();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRuleDetail dataBean) {
        this.dataBean = dataBean;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

}

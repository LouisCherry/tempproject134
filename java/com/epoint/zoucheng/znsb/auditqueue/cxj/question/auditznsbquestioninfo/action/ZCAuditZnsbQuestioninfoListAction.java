package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestioninfo.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;

/**
 * 问卷调查-问题list页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:17]
 */
@RestController("zcauditznsbquestioninfolistaction")
@Scope("request")
public class ZCAuditZnsbQuestioninfoListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IZCAuditZnsbQuestioninfoService service;
    @Autowired
    private IZCAuditZnsbQuestiondetailService detailService;
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 问卷调查-问题实体对象
     */
    private AuditZnsbQuestioninfo dataBean;
    private String questionnaireguid;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbQuestioninfo> model;
    private TreeModel treeModel = null;

    public void pageLoad() {
        questionnaireguid = getRequestParameter("questionnaireguid");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
            detailService.deleteByQuestionGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditZnsbQuestioninfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestioninfo>()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditZnsbQuestioninfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getQuestiontype())) {
                        sql.eq("questiontype", dataBean.getQuestiontype());
                    }
                    if (StringUtil.isNotBlank(dataBean.getQuestion())) {
                        sql.like("question", dataBean.getQuestion());
                    }
                    PageData<AuditZnsbQuestioninfo> pageData = service
                            .findListByQuestionnairerowguid(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
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
                        root.setText("所有类型");
                        root.setId("f9root");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        List<CodeItems> codeItemslist = codeItemsService.listCodeItemsByCodeName("题目类型");
                        if (codeItemslist != null && !codeItemslist.isEmpty()) {
                            for (CodeItems codeItems : codeItemslist) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItems.getItemValue());
                                node.setText(codeItems.getItemText());
                                node.setPid("f9root");
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

    public AuditZnsbQuestioninfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestioninfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestioninfo dataBean) {
        this.dataBean = dataBean;
    }

}

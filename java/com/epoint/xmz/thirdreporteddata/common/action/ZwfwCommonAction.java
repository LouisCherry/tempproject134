package com.epoint.xmz.thirdreporteddata.common.action;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("zwfwcommonaction")
@Scope("request")
public class ZwfwCommonAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = -8891122366031205337L;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    private LazyTreeModal9 treeModal9;
    private TreeModel treeModel;

    private TreeModel ryzytreeModel;

    @Override
    public void pageLoad() {

    }

    public List<SelectItem> getCommonModel(String codename) {
        return DataUtil.convertMap2ComboBox(
                CodeModalFactory.factory(EpointKeyNames9.CHECK_SELECT_GROUP, codename, null, false));
    }

    public LazyTreeModal9 getCommonRadioTree(String codename) {
        if (treeModal9 == null) {
            treeModal9 = CodeModalFactory.factory(EpointKeyNames9.DROP_DOWN_RADIO_TREE, codename, null, false);
        }
        return treeModal9;
    }

    public TreeModel getZzdjTree() {
        if (treeModel == null) {
            treeModel = new TreeModel() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载
                    if (treeNode == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有资质等级");
                        root.setId("al");
                        root.setPid("-1");
                        root.setCkr(false);
                        root.setExpanded(true);// 展开下一层节点
                        list.add(root);
                        list.addAll(fetch(root));// 自动加载下一层树结构
                        List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("国标_资质等级");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() == 1) {
                                TreeNode pnode = new TreeNode();
                                pnode.setId(codeItem.getItemValue());
                                pnode.setText(codeItem.getItemText());
                                pnode.setPid("al");
                                pnode.setLeaf(false);
                                pnode.setCkr(false);
                                list.add(pnode);
                                list.addAll(fetch(pnode));
                            }
                            if (codeItem.getItemValue().length() == 3) {
                                TreeNode snode = new TreeNode();
                                snode.setId(codeItem.getItemValue());
                                snode.setText(codeItem.getItemText());
                                snode.setPid(codeItem.getItemValue().substring(0, 1));
                                snode.setLeaf(true);
                                snode.setCkr(true);
                                list.add(snode);
                            }
                        }
                    }
                    return list;
                }
            };

        }
        return treeModel;
    }


}

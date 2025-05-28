package com.epoint.xmz.audittaskjn.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政务服务选择窗口部门 注意点：在编辑页面跳转到该action的时候，需要传入两个参数，分别是guid和tablename,
 * guid表示XXX表的guid，tablename表示与ouguid关联的表名。主要的作用是用来回显部门。
 *
 * @version [版本号, 2016年8月25日]
 */
@RestController("jnzwfwselectouaction")
@Scope("request")
public class ZwfwSelectOuAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private TreeModel treeModel = null;

    /**
     * 新增的时候显示的部门
     */
    private String ouguid;
    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditTaskJnService jnService;

    @Override
    public void pageLoad() {
        ouguid = this.getRequestParameter("ouguid");
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                private static final long serialVersionUID = -7089566877270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    List<FrameOu> parentlistou = new ArrayList<FrameOu>();
                    List<FrameOu> listou = new ArrayList<>();

                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    } else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            List<FrameOu> frameOus = jnService.getAllOu();
                            parentlistou = frameOus.stream()
                                    .filter(o -> o.getOuname().contains(treeNode.getSearchCondition()))
                                    .filter(o -> ouService.getFrameOuExtendInfo(o.getOuguid()) != null)
                                    .sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
                                    .collect(Collectors.toList());
                            if(!parentlistou.isEmpty()){
                                for(FrameOu ou : parentlistou){
                                    List<FrameOu> oulist = ouService.listOUByGuid(ou.getOuguid(),5);
                                    listou.addAll(oulist);
                                }
                            }
                        } else {
                            parentlistou = jnService.getAllOu();
                            if(!parentlistou.isEmpty()){
                                for(FrameOu ou : parentlistou){
                                    List<FrameOu> oulist = ouService.listOUByGuid(ou.getOuguid(),5);
                                    listou.addAll(oulist);
                                }
                            }
                        }
                    }

                    //父部门绑定
                    for (FrameOu frameOu : parentlistou) {
                        TreeNode node = new TreeNode();
                        node.setId(frameOu.getOuguid());
                        node.setText(frameOu.getOuname());
                        node.setPid("f9root");
                        node.setLeaf(true);
                        list.add(node);
                    }

                    //子部门绑定
                    for (FrameOu frameOu : listou) {
                        TreeNode node = new TreeNode();
                        node.setId(frameOu.getOuguid());
                        node.setText(frameOu.getOuname());
                        node.setPid(frameOu.getParentOuguid());
                        node.setLeaf(true);
                        list.add(node);
                    }

                    return list;
                }
            };
            if (StringUtil.isNotBlank(ouguid)) {
                FrameOu ou = ouService.getOuByOuGuid(ouguid);
                if (ou != null) {
                    treeModel.setSelectNode(new ArrayList<SelectItem>() {
                        {
                            add(new SelectItem(ouguid, ouService.getOuByOuGuid(ouguid).getOuname()));
                        }
                    });
                }
            }
        }

        return treeModel;
    }

}

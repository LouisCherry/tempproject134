package com.epoint.sghd.auditjianguan.rlconfig.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.auditjianguan.rlconfig.api.IRlconfigSendmsg;
import com.epoint.sghd.auditjianguan.rlconfig.api.RlconfigSendmsg;
import com.epoint.sghd.commonutil.api.IJnSghdCommonutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 前四阶段信息配置表新增页面对应的后台
 *
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@RestController("rlconfigsendmsgaddaction")
@Scope("request")
public class RlconfigSendmsgAddAction extends BaseController {
    @Autowired
    private IRlconfigSendmsg service;
    /**
     * 前四阶段信息配置表实体对象
     */
    private RlconfigSendmsg dataBean = null;
    @Autowired
    private IJnSghdCommonutil sghdService;
    /**
     * 部门树
     */
    private TreeModel treeModel;
    String rlouguid;
    @Autowired
    private IOuService ouservice;

    public void pageLoad() {
        dataBean = new RlconfigSendmsg();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.set("rlouguid", rlouguid);
        FrameOu ou = ouservice.getOuByOuGuid(rlouguid);
        if (ou != null) {
            dataBean.set("rlouname", ou.getOuname());
        }
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        dataBean.set("areacode", areacode);
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new RlconfigSendmsg();
    }

    /**
     * 部门树
     *
     * @return
     */
    public TreeModel getRadModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        if ("370800".equals(areacode)) {
                            root.setText("济宁市直部门");
                        } else {
                            root.setText("所有部门");
                        }
                        root.setId("root");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    // 每次点击树节点前的加号，进行加载
                    else {
                        // 获取一级部门
                        List<FrameOu> listRootOu = new ArrayList<>();
                        listRootOu = sghdService.listOuinfo(areacode);

                        // 部门的绑定
                        for (int i = 0; i < listRootOu.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(listRootOu.get(i).getOuguid());
                            node.setText(listRootOu.get(i).getOuname());
                            node.setPid("root");
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

    public RlconfigSendmsg getDataBean() {
        return dataBean;
    }

    public void setDataBean(RlconfigSendmsg dataBean) {
        this.dataBean = dataBean;
    }

    public String getRlouguid() {
        return rlouguid;
    }

    public void setRlouguid(String rlouguid) {
        this.rlouguid = rlouguid;
    }
}

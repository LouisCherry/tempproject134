package com.epoint.common.usermanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 
 * 政务服务选择窗口部门 注意点：在编辑页面跳转到该action的时候，需要传入两个参数，分别是guid和tablename,
 * guid表示XXX表的guid，tablename表示与ouguid关联的表名。主要的作用是用来回显部门。
 * 
 * @version [版本号, 2016年8月25日]
 */
@RestController("zwfwselectareaaction")
@Scope("request")
public class ZwfwSelectAreaAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    
    private TreeModel treeModel = null;

    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    private String areacode= "";
    private String centerguid= "";

    @Autowired
    private IRoleService iRoleService;
    
    @Override
    public void pageLoad() {
        areacode = this.getRequestParameter("areacode");
        centerguid = this.getRequestParameter("guid");
        if(StringUtil.isBlank(areacode)){
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        if(StringUtil.isBlank(centerguid)){
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        }
    }

    public TreeModel getTreeModel() {
        areacode = this.getRequestParameter("areacode");
        centerguid = this.getRequestParameter("guid");
        boolean isadmin = userSession.isAdmin();
        boolean isquanliangview = false;
        List<String> roles = userSession.getUserRoleList();
        if (roles != null && !roles.isEmpty()) {
            for (String roleid : roles) {
                FrameRole role = iRoleService.getRoleByRoleField("roleguid", roleid);
                if (role != null) {
                    if ("全量查看".equals(role.getRoleName())) {
                        isquanliangview=true;
                        break;
                    }
                }
            }
        }
        if(StringUtil.isBlank(areacode)){
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        if(StringUtil.isBlank(centerguid)){
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        }
        if (treeModel == null) {
            boolean finalIsquanliangview = isquanliangview;
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = -7089566877270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    List<AuditOrgaArea> listarea = new ArrayList<AuditOrgaArea>();
                    SqlConditionUtil condition = new SqlConditionUtil();
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
                    }
                    else {
                        if (isadmin || finalIsquanliangview) {
                        	condition.le("CityLevel", "2");
                        	listarea = auditOrgaAreaService.selectAuditAreaList(condition.getMap()).getResult();
                        }else{
                        	condition.eq("XiaQuCode", areacode);
                        	listarea = auditOrgaAreaService.selectAuditAreaList(condition.getMap()).getResult();
                        }
                        if(listarea != null) {
                        	// 筛选并排序
                        	if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                        		listarea = listarea.stream()
                        				.filter(x -> x.getXiaquname().contains(treeNode.getSearchCondition()))
                        				.sorted((a, b) -> b.getOrdernum().compareTo(a.getOrdernum()))
                        				.collect(Collectors.toList());
                        	}else {
                        		listarea = listarea.stream()
                        				.sorted((a, b) -> b.getOrdernum().compareTo(a.getOrdernum()))
                        				.collect(Collectors.toList());
                        	}
                        }
                    }
                    
                    
                    //部门绑定
                    for (AuditOrgaArea area : listarea) {
                        TreeNode node = new TreeNode();
                        node.setId(area.getXiaqucode());
                        node.setText(area.getXiaquname());
                        node.setPid("f9root");
                        node.setLeaf(true);
                        list.add(node);
                    }
                    return list;
                }
            };
            if (StringUtil.isNotBlank(areacode)) {
            	AuditOrgaArea area = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                if (area != null) {
                    treeModel.setSelectNode(new ArrayList<SelectItem>()
                    {
						private static final long serialVersionUID = 1L;
						{
                            add(new SelectItem(areacode, area.getXiaquname()));
                        }
                    });
                }
            }
        }

        return treeModel;
    }

}

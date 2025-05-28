    package com.epoint.auditqueue.auditznsbremotehelpuser.action;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 一体机绑定好视通账户list页面对应的后台
 * 
 * @author JackLove
 * @version [版本号, 2018-04-12 15:24:50]
 */
@RestController("auditznsbremotehelpuseraction")
@Scope("request")
public class AuditZnsbRemoteHelpUserAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditZnsbRemoteHelpUserService auditZnsbAssessConfigService;

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleRelationService roleRelationService;
    @Autowired
    private IUserService userService;
    
    private AuditZnsbRemoteHelpUser  dataBean = null;
    private TreeModel treeModel;
    private LazyTreeModal9 modal;
    private String guid = null;
  


    public void pageLoad() {
        dataBean = new AuditZnsbRemoteHelpUser();
        guid = getRequestParameter("guid");
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            // 加载所有人员
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = -5605654757820052996L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {

                    return getModal().fetch(treeNode);

                }

                //懒加载进行获取数据，把左边树中选择的内容加载到右边
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {

                    //获取tree原有的select
                    List<SelectItem> selectedItems = treeModel.getSelectNode();

                    //复选框选中
                    if (treeNode.isChecked() == true) {
                        if (("f9root").equals(treeNode.getId())) {
                            AuditZnsbRemoteHelpUser auditZnsbAssessconfig = auditZnsbAssessConfigService.getDetail(guid).getResult();
                            FrameUser frameUser = userService.getUserByUserField("userguid", auditZnsbAssessconfig.getUserguid());
                            if (frameUser.getUserGuid().equals(selectedItems.get(0).getValue())) {
                                        selectedItems.remove(0);
                            }                               
                            selectedItems.add(new SelectItem(frameUser.getUserGuid(),
                                    frameUser.getDisplayName()));
                        }
                        else {
                            selectedItems.add(new SelectItem(treeNode.getId(), treeNode.getText()));
                        }

                    }
                    else {
                        if (("f9root").equals(treeNode.getId())) {
                            selectedItems.clear();
                        }
                        else {
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i).getValue().equals(treeNode.getId())) {
                                    selectedItems.remove(i);

                                }
                            }
                        }
                    }
                    return selectedItems;
                };

            };

            if (!isPostback()) {
                setSelectedTreeNode();
            }
        }

        return treeModel;
    }

    private void setSelectedTreeNode() {
        List<SelectItem> sItem = new ArrayList<SelectItem>();
        FrameUser frameUser = userService.getUserByUserField("userguid", auditZnsbAssessConfigService.getDetail(guid).getResult().getUserguid());
        if(frameUser != null){
            sItem.add(new SelectItem(frameUser.getUserGuid(), frameUser.getDisplayName()));
        }        
        treeModel.setSelectNode(sItem);
    }

    public LazyTreeModal9 getModal() {
        if (modal == null) {
            modal = new LazyTreeModal9(new FetchHandler9()
            {
                private static final long serialVersionUID = -5605654757820052996L;

                @Override
                public <T> List<T> fetchTotalData(String arg0, boolean arg1) {
                    return null;
                }

                @Override
                public <T> List<T> fetchData(int arg0, int arg1, TreeData arg2) {
                    List frameUserList = new ArrayList<>();
                    if (StringUtil.isBlank(arg2.getObjectcode())) {
                     // 1、获取角色标识
                        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "远程协助");
                        if (frameRole != null) {
                            String roleguid = frameRole.getRoleGuid();
                            // 2、获取该角色的对应的人员
                            List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                                    .getRelationListByField("roleGuid", roleguid, null, null);
                            if (frameuserrolerelationlist != null && frameuserrolerelationlist.size() > 0) {
                                for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                                    String targetuserguid = frameUserRoleRelation.getUserGuid();
                                    FrameUser frameUser = userService.getUserByUserField("userguid",targetuserguid);
                                    frameUserList.add(frameUser);
                                }
                            }
                        }
                    }
                    return frameUserList;
                }

                @Override
                public int fetchChildCount(int arg0, TreeData arg1) {
                    return 0;
                }

                @Override
                public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                    List<TreeData> treelist = new ArrayList<TreeData>();
                    if (dataList != null) {
                        for (Object ob : dataList) {
                            FrameUser frameUser = (FrameUser) ob;
                            TreeData treeData = new TreeData();
                            treeData.setObjectcode(frameUser.getUserGuid());
                            treeData.setTitle(frameUser.getDisplayName());
                            treelist.add(treeData);
                        }
                    }
                    return treelist;
                }
            });
            modal.setRootName("所有人员");
        }
        return modal;
    }

    public List<String> getUserGuid() {
        List<String> list = new ArrayList<String>();
        List<SelectItem> nodeList = getTreeModel().getSelectNode();
        for (SelectItem item : nodeList) {
            list.add((String) item.getValue());
        }
        return list;
    }

    /**
     * 保存并关闭
     * 
     */
   public void add() {
        List<String> list = getUserGuid();
        if (list != null && list.size() == 1) {
            for (int i = 0; i < list.size(); i++) {
                AuditZnsbRemoteHelpUser auditZnsbAssessconfig = auditZnsbAssessConfigService.getDetail(guid).getResult();
                int count = auditZnsbAssessConfigService.getAuditZnsbRemoteHelpUserByUserguid(list.get(i));
                if(count>0){
                    addCallbackParam("msg", "人员已被绑定！");
                }else{
                    auditZnsbAssessconfig.setUserguid(list.get(i));
                    auditZnsbAssessConfigService.update(auditZnsbAssessconfig);
                    addCallbackParam("msg", "保存成功！"); 
                }
            }
        }else {
            addCallbackParam("msg", "只能选择一个人员！");
        }
        
        dataBean = null;
    }

    public AuditZnsbRemoteHelpUser getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbRemoteHelpUser();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbRemoteHelpUser dataBean) {
        this.dataBean = dataBean;
    }

}

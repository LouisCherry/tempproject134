package com.epoint.knowledge.category.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.orga.ou.service.FrameOuService9;
import com.epoint.basic.bizlogic.orga.role.service.FrameRoleService9;
import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.convert.ChineseToPinyin;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;

import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserSession;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;



/**
 * 知识库类别新增页面对应的后台
 * 
 * @author zhoukj
 * @version [版本号, 2016-11-02 19:17:17]
 */
@RestController("categoryaddaction")
@Scope("request")
public class CategoryAddAction extends BaseController
{
    /**
     * 
     */
    
    private static final long serialVersionUID = 1L;
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    //private BasicModel basicModel = new BasicModel();
    private FrameRoleService9 frameRoleService = new FrameRoleService9();
    private FrameUserService9 frameUserService = new FrameUserService9();
    private FrameOuService9 frameOuService = new FrameOuService9();
    private FrameOuService9 getFrameOUService = new FrameOuService9();
    private FrameUserService9 getFrameUserService = new FrameUserService9();
    private String parentName = null;

    private String ouname = "";

    private String ouguid = "";

    private String isdepttop = "";
    /**
     * 知识库类别实体对象
     */
    private CnsKinfoCategory cnsKinfoCategory = null;

    private String parentGuid = "";

    private TreeModel cagouGuidModel = null;

    @Override
    public void pageLoad() {
        if (cnsKinfoCategory == null) {
            cnsKinfoCategory = new CnsKinfoCategory();
        }
        parentGuid = this.getRequestParameter("categoryguid");
        //如果父节点不为空
        if (StringUtil.isNotBlank(parentGuid)) {
            CnsKinfoCategory parentCat = kinfoCategoryService.getBeanByGuid(parentGuid);
            parentName = parentCat.getCategoryname();
            CnsKinfoCategory parentKinfoCategory = kinfoCategoryService.getOuByRowguid(parentGuid);
            ouname = parentKinfoCategory.getOuname();
            ouguid = parentKinfoCategory.getOuguid();
            isdepttop=parentKinfoCategory.getIsdepttop();
        }else{
            if(CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)){
                ouguid=userSession.getOuGuid();
                ouname=userSession.getOuName();
            }
        }
        this.addCallbackParam("parentName", parentName);
        this.addCallbackParam("parentGuid", parentGuid);
        this.addCallbackParam("ouname", ouname);
        this.addCallbackParam("ouguid", ouguid);
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        if (StringUtil.isNotBlank(cnsKinfoCategory.getOuguid())) {
            if (StringUtil.isNotBlank(isdepttop)) {
                cnsKinfoCategory.setIsdepttop(CnsConstValue.CNS_ZERO_STR);
            }
            else {
                cnsKinfoCategory.setIsdepttop(CnsConstValue.CNS_ONT_STR);
            }
        }
        cnsKinfoCategory.setRowguid(UUID.randomUUID().toString());
        cnsKinfoCategory.setOperatedate(new Date());
        cnsKinfoCategory.setOperateusername(userSession.getDisplayName());
        cnsKinfoCategory.setCategorycode(kinfoCategoryService.getMaxCategoryCode(cnsKinfoCategory.getParentguid()));
        //如果类别缩写为空，那么就设置为类型名称
        if (StringUtil.isBlank(cnsKinfoCategory.getCategoryshortname())) {
            cnsKinfoCategory.setCategoryshortname(cnsKinfoCategory.getCategoryname());
        }
        if (cnsKinfoCategory.getOrdernumber() == null) {
            cnsKinfoCategory.setOrdernumber(0);
        }
        String name = cnsKinfoCategory.getCategoryname();
        if (StringUtil.isNotBlank(name)) {
            String sql = "select count(1) from cns_kinfo_Category where Categoryname='"+name+"'";
            int count = CommonDao.getInstance().find(sql, Integer.class);
            if(count>0){
                this.addCallbackParam("msg", "类别名称重复！");
                this.addCallbackParam("status", "1");
                return ;
            }
            cnsKinfoCategory.setFirstpy(ChineseToPinyin.getPinYinHeadChar(name));
            cnsKinfoCategory.setFulltpy(ChineseToPinyin.getPingYin(name));
        }
        //新添加的节点肯定是叶子节点。
        cnsKinfoCategory.setIsleaf("1");
        //同时需要把父节点更为不为非叶子节点。
        kinfoCategoryService.updateParentLeaf(cnsKinfoCategory.getParentguid());
        //先添加关联关系，再添加类别
        kinfoCategoryService.addRecord(cnsKinfoCategory);
        this.addCallbackParam("msg", "保存成功！");
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        cnsKinfoCategory = new CnsKinfoCategory();
    }

    public CnsKinfoCategory getCnsKinfoCategory() {
        return cnsKinfoCategory;
    }

    public void setCnsKinfoCategory(CnsKinfoCategory cnsKinfoCategory) {
        this.cnsKinfoCategory = cnsKinfoCategory;
    }

    public String getParentName() {
        return parentName;
    }
    public TreeModel getCagouGuidModel() {
        if (cagouGuidModel == null) {
            //cagouGuidModel = basicModel.getHandleOuGuidModel("");
            cagouGuidModel = new TreeModel(){

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                       TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：是否是部门节点
                        //list.add(root);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                        
                    }else{
                        String objectGuid = treeData.getObjectGuid();
                        List<FrameOu> listRootOu = getFrameOUService.listOUByGuid(objectGuid, 2);
                        //List<FrameOu> listRootOu = getFrameOUService.listAllOu();
                        for (int i = 0; i < listRootOu.size(); i++) {
                            if(listRootOu.get(i).getParentOuguid()==null){
                                listRootOu.get(i).setParentOuguid("");
                            }
                            
                            if(((StringUtil.isBlank(listRootOu.get(i).getParentOuguid()))&&objectGuid.equals(""))||(listRootOu.get(i).getParentOuguid().equals(objectGuid))) {
                                
                                TreeNode node = new TreeNode();
                                node.setId(listRootOu.get(i).getOuguid());
                                node.setText(listRootOu.get(i).getOuname());
                                node.setPid(listRootOu.get(i).getParentOuguid());
                                node.setLeaf(true);

                                for (int j = 0; j < listRootOu.size(); j++) {
                                    if (listRootOu.get(i).getOuguid().equals(listRootOu.get(j).getParentOuguid())) {
                                        node.setLeaf(false);
                                        break;
                                    }
                                }
                                list.add(node);
                            }
                       }    
                    }
                    return list;
                
                } 
            };
        }
        return cagouGuidModel;
    }
            

    
    private List getUserByRoleName(String roleName) {
        List frameUserList = null;
        if (StringUtil.isNotBlank(roleName)) {
            String roleGuid = frameRoleService.getRoleGuidByRoleName(roleName);
            if (StringUtil.isNotBlank(roleGuid)) {
                frameUserList = frameUserService.getUserListByOuGuidWithNotEnabled("", roleGuid, "", (String) null, false, false,
                        false, 3);
            }
        }

        return frameUserList;
    }

    public void queryOu(String categoryguid){
        CnsKinfoCategory kinfoCategory=kinfoCategoryService.getBeanByGuid(categoryguid);
        if(kinfoCategory!=null)
        {
            this.addCallbackParam("treeouname", kinfoCategory.getOuname());
            this.addCallbackParam("treeouguid", kinfoCategory.getOuguid());
        }
    }

}

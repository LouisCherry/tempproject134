package com.epoint.analysis.yjstjbb.action;

import java.util.ArrayList;
import java.util.List;

import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.analysis.yjstjbb.api.IAnaYjsTjbb;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;

/**
 * 一件事统计报表页面action
 * @Author 28101
 * @Description
 * @Date 2022/10/25 
 **/
@RestController("anayjstjbbaction")
@Scope("request")
public class AnaYjsTjbbAction extends BaseController {
    /**
     * 
     */
    private static final long serialVersionUID =2L;

    @Autowired
    private IAnaYjsTjbb IAnaYjsTjbb;
    
    @Autowired
    private ICodeMainService iCodeMainService;
    
    @Autowired
    private ICodeItemsService iCodeItemsService;
    
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();

    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;


    private List<SelectItem> applywayModel = null;

    /**
     * 申请时间
     */
    private String applydateStart;
    private String applydateEnd;
    /**
     * 办结时间
     */
    private String finishdateStart;
    private String finishdateEnd;


    private DataGridModel<Record> model;

    private TreeModel treeModel = null;

    private ExportModel exportModel;
    
    private String startDate;
    
    private String endDate;
    
    private String applyerway;

    /**
     * 左边树列表
     */
    private String leftTreeNodeGuid ="";

    @Autowired
    private IRoleService iRoleService;

    @Override
    public void pageLoad() {
        //设置默认申请方式为全部
        applyerway = "00";
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {
                
                /**
                 * 
                 */
                private static final long serialVersionUID = 3L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    List<Record> list =new ArrayList<>();
                    int count = 0 ;
                    //判断当前人员是否是市本级
                    if ("370800".equals(areacode)) {
                      //判断是否点击树节点，没有则将当前人员所属区域赋值给areacode
                        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                            areacode = leftTreeNodeGuid;
                        }
                        else {
                            areacode = "3708";
                        }
                        
                    }
                    else {
                      //判断是否点击树节点，没有则将当前人员所属区域赋值给areacode
                        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                            if (leftTreeNodeGuid.equals(areacode)) {
                                areacode = leftTreeNodeGuid;
                            }
                            else {
                                areacode = "";
                            }
                            
                        }
                    }
                    if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
                        list = IAnaYjsTjbb.getList(first, pageSize, startDate, endDate, areacode, applyerway);
                        count = IAnaYjsTjbb.countList(first, pageSize,startDate, endDate, areacode, applyerway);
                    }
                    else {
                       startDate = "";
                       endDate = "";
                       list = IAnaYjsTjbb.getList(first, pageSize, startDate, endDate, areacode, applyerway);
                       count = IAnaYjsTjbb.countList(first, pageSize,startDate, endDate, areacode, applyerway);
                    }  
                    
                    if (list != null) {
                        
                        this.setRowCount(count);
                        return list;
                    }
                    else {
                        return new ArrayList<>();
                    }
                 
                }
            };
        }
        return model;
    }

//    public TreeModel getTreeModel() {
//        if (treeModel == null) {
//            treeModel = new TreeModel() {
//
//                private static final long serialVersionUID = -708955557270145158L;
//
//                @Override
//                public List<TreeNode> fetch(TreeNode treeNode) {
//                    TreeData treeData = TreeFunction9.getData(treeNode);
//                    List<TreeNode> list = new ArrayList<>();
//                    boolean isadmin = userSession.isAdmin();
//                    boolean isquanliangview = false;
//                    List<String> roles = userSession.getUserRoleList();
//                    if (roles != null && !roles.isEmpty()) {
//                        for (String roleid : roles) {
//                            FrameRole role = iRoleService.getRoleByRoleField("roleguid", roleid);
//                            if (role != null) {
//                                if ("全量查看".equals(role.getRoleName())) {
//                                    isquanliangview=true;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    String  areacode = ZwfwUserSession.getInstance().getAreaCode();
//
//                    if (treeData == null) {
//                        TreeNode root = new TreeNode();
//                        if (isadmin || isquanliangview) {
//                            root.setText("济宁市");
//                            root.setId("370800");
//                        }else {
//                            AuditOrgaArea orga = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
//                            if (orga != null) {
//                                root.setText(orga.getXiaquname());
//                                root.setId(areacode);
//                            }
//                        }
//                        root.setPid("-1");
//                        list.add(root);
//                        root.setCkr(true);
//                        root.setExpanded(true);//展开下一层节点
//                        list.addAll(fetch(root));//自动加载下一层树结构
//                    }
//                    else {
//
//                        if (isadmin || isquanliangview) {
//                            List<Record> areaList = IAnaYjsTjbb.getAreaList();
//                            for (Record record : areaList) {
//                                if (!"370800".equals(record.getStr("itemvlaue"))) {
//                                    TreeNode node = new TreeNode();
//                                    node.setId(record.getStr("itemvalue"));
//                                    node.setText(record.getStr("itemtext"));
//                                    node.setPid("370800");
//                                    node.setLeaf(true);
//                                    list.add(node);
//                                }
//                            }
//                        }
//
//
//
//                    }
//
//                    return list;
//                }
//            };
//        }
//
//        return treeModel;
//    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                
                private static final long serialVersionUID = -708955557270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
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
                    String  areacode = ZwfwUserSession.getInstance().getAreaCode();
                    
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        if (isadmin || isquanliangview) {
                        	 root.setText("济宁市");
                             root.setId("370800");
                        }else {
                        	AuditOrgaArea orga = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
                        	if (orga != null) {
                        		root.setText(orga.getXiaquname());
                                root.setId(areacode);
                        	}
                        }
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                    	
                    	if (isadmin||isquanliangview) {
                    		List<Record> areaList = IAnaYjsTjbb.getAreaList();
                            for (Record record : areaList) {
//                                if (!"370800".equals(record.getStr("itemtext"))) {
//                                    TreeNode node = new TreeNode();
//                                    node.setId(record.getStr("itemtext"));
//                                    node.setText(record.getStr("itemvalue"));
//                                    node.setPid("370800");
//                                    node.setLeaf(true);
//                                    list.add(node);
//                                }
                                if (!"370800".equals(record.getStr("itemvalue"))) {
                                    TreeNode node = new TreeNode();
                                    node.setId(record.getStr("itemvalue"));
                                    node.setText(record.getStr("itemtext"));
                                    node.setPid("370800");
                                    node.setLeaf(true);
                                    list.add(node);
                                }
                            }
                    	}
                        
                        

                    }

                    return list;
                }
            };
        }

        return treeModel;
    }

    public List<SelectItem> getApplywayModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("00", "全部"));
        result.add(new SelectItem("10", "网上申报"));
        result.add(new SelectItem("20", "窗口申报"));
        return result;
    }


    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("XiaQuName,sb,bj",
                    "辖区,申报,办结");
        }
        return exportModel;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getApplyerway() {
        return applyerway;
    }

    public void setApplyerway(String applyerway) {
        this.applyerway = applyerway;
    }

  
    


}

package com.epoint.xmz.kyperson.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditdepartment.inter.IAuditOrgaDepartment;
import com.epoint.basic.auditorga.auditmember.domain.AuditOrgaMember;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.xmz.kyperson.api.IKyPersonService;
import com.epoint.xmz.kyperson.api.entity.KyPerson;

/**
 * 勘验人员表新增页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:34:44]
 */
@RightRelation(KyPersonListAction.class)
@RestController("kypersonaddaction")
@Scope("request")
public class KyPersonAddAction extends BaseController
{
    @Autowired
    private IKyPersonService service;
    /**
     * 勘验人员表实体对象
     */
    private KyPerson dataBean = null;

    /**
     * 勘验结果单选按钮组model
     */
    private List<SelectItem> resultModel = null;

    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;
    
    /**
     * 窗口人员model
     */
    private TreeModel treeModel = null;
    private String kyguid;
    @Autowired
    private IAuditOrgaMember iAuditOrgaMember;
    @Autowired
    private IAuditOrgaDepartment iAuditOrgaDepartment;
    @Autowired
    private IHandleFrameOU frameOu ;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    @Autowired
    private IOuServiceInternal ouService;
    @Autowired
    private IUserServiceInternal userService;
    /**
     * 窗口标识
     */
    private String windowGuid = null;
    

    private String areacode= "";
    private String centerguid= "";
    
    private String userName1;
    private FrameUser user = null;
    private TreeModel userModel = null; 
    public void pageLoad() {
        kyguid = getRequestParameter("projectguid");
        dataBean = new KyPerson();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setKyguid(kyguid);
        FrameUser userinfo = userService.getUserByUserField("userguid", userName1);
        String displayName = userinfo.getDisplayName();
        dataBean.setName(displayName);
        dataBean.set("userguid", userName1);
        dataBean.setClientguid(UUID.randomUUID().toString());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new KyPerson();
    }

    public KyPerson getDataBean() {
        if (dataBean == null) {
            dataBean = new KyPerson();
        }
        return dataBean;
    }

    public void setDataBean(KyPerson dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getResultModel() {
        if (resultModel == null) {
            resultModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "勘验结果", null, false));
        }
        return this.resultModel;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            String clientGuid = getViewData("clientguid");
            if (StringUtil.isBlank(clientGuid)) {
                addViewData("clientguid", UUID.randomUUID().toString());
            }
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("clientguid"), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        // 该属性设置他为只读，不能被删除
        // fileUploadModel1.setReadOnly("true");
        return fileUploadModel;
    }
    
    /***
     * 
     * 用treebean构造人员树
     * 
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = loadAllUser(windowGuid);
            if (!isPostback()) {
                treeModel.setSelectNode(getSelectUser(windowGuid));
            }
        }
        return treeModel;
    }
    
    public TreeModel loadAllUser(String windowGuid){
        if (treeModel == null) {
            treeModel = new TreeModel(){
                
                private static final long serialVersionUID = -7417424000390574490L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    List<FrameOu> listou = new ArrayList<>();
                    List<FrameUser> listuser = new ArrayList<>();
                    List<ViewFrameUser> viewlistuser = new ArrayList<>();
                    //根节点
                    if(treeNode==null){
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }else{
                        if(StringUtil.isNotBlank(treeNode.getSearchCondition())){
                            TreeNode root = new TreeNode();
                            root.setText("所有部门");
                            root.setId("root");
                            root.setPid("-1");
                            list.add(root);
                            List<FrameOu> oulist = frameOu.getWindowOUList(areacode,true).getResult();
                            for (FrameOu frameou : oulist) {
                                List<ViewFrameUser> userList = userService.paginatorUserViewByOuGuid(frameou.getOuguid(), treeNode.getSearchCondition(), "",
                                        "", "", "", 0, 100, "", "", true, false).getList();
                                 viewlistuser.addAll(userList);
                            }   
                            
                            //去重操作
                            viewlistuser = viewlistuser.stream().distinct().collect(Collectors.toList());
                            AuditOrgaArea orgaarea=auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                            List<FrameUser> listareauser = new ArrayList<FrameUser>();
                            String ouguid="";
                            if(orgaarea!=null){
                                ouguid=orgaarea.getOuguid();
                            }
                            listareauser = userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1);
                            for (FrameUser frameuser : listareauser) {
                                if(frameuser.getDisplayName().indexOf(treeNode.getSearchCondition())!=-1  && listuser.lastIndexOf(frameuser) == -1){
                                    listuser.add(frameuser);
                                }
                            }
                        }else{
                           if("root".equals(treeNode.getId())){
                               AuditOrgaArea orgaarea=auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                               String ouguid="";
                               if(orgaarea!=null){
                                   ouguid=orgaarea.getOuguid();
                               }
                               listuser = userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1);
                               List<FrameOu> ouList = ouService.listOUByGuid(ouguid, 5);
                               if(ouList!=null){
                                   ouList.removeIf(ou -> {
                                       FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                       if(ouExt != null) {
                                           if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                                   && areacode.equals(ouExt.get("areacode").toString())) {
                                               return false;
                                           } else {
                                               return true;
                                           } 
                                       }else {
                                           return true;
                                       }
                                       
                                   });                            
                               }
                               if (ouList != null && ouList.size() > 0) {
                                   for (FrameOu ou : ouList) {
                                       listou.add(ou);
                                   }
                               }  
                           }else{
                               listuser = userService.listUserByOuGuid(treeNode.getId(), "", "", "", false, true, true, 1);
                               FrameOuExtendInfo treeouExt = ouService.getFrameOuExtendInfo(treeNode.getId());
                               String treearea=treeouExt.get("areacode");
                               List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                               if(ouList!=null){
                                   ouList.removeIf(ou -> {
                                       FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                       if (ouExt != null) {
                                           if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                                   && treearea.equals(ouExt.get("areacode").toString())) {
                                               return false;
                                           }
                                           else {
                                               return true;
                                           }
                                       }else {
                                           return true;
                                       }
                                       
                                   });
                               }
                               if (ouList != null && ouList.size() > 0) {
                                   for (FrameOu ou : ouList) {
                                       listou.add(ou);
                                   }
                               }
                           }
                        }
                    }
                    
                    //list转换
                    for (ViewFrameUser viewFrameUser : viewlistuser) {
                        TreeNode node = new TreeNode();
                        node.setId(viewFrameUser.getUserGuid());
                        node.setText(viewFrameUser.getDisplayName() + "(" + viewFrameUser.getOuname() + ")");
                        node.getColumns().put("isOU", "0");
                        node.setPid("root");
                        node.setLeaf(true);
                        list.add(node);
                    }
                    for (FrameUser frameUser : listuser) {
                        TreeNode node = new TreeNode();
                        node.setId(frameUser.getUserGuid());
                        node.setText(frameUser.getDisplayName());
                        node.getColumns().put("isOU", "0");
                        node.setLeaf(true);
                        node.setPid(treeNode.getId()==null?"root":treeNode.getId());
                        list.add(node);
                    }
                    
                    for (FrameOu frameou : listou) {
                        TreeNode node = new TreeNode();
                        node.setId(frameou.getOuguid());
                        node.setText(frameou.getOuname());
                        node.getColumns().put("isOU", "1");
                        node.setPid(StringUtil.isBlank(treeNode.getId())?"root":treeNode.getId());
                        node.setLeaf(false);
                        List<FrameUser> frameUsers = userService.listUserByOuGuid(frameou.getOuguid(), "", "", "",
                                false, true, true, 3);
                        int childCount = 0;
                        if (frameUsers != null) {
                            childCount = frameUsers.size();
                        }
                        if(childCount > 0){
                            list.add(node);                                
                        }
                    }
                    return list;
                }
                
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    List<SelectItem> selectedItems = treeModel.getSelectNode();
                    if(treeNode.isChecked() == true){
                        List<FrameUser> list = new ArrayList<>();
                        list = userService.listUserByOuGuid(treeNode.getId(), "", "", "", false, true, true, 1);
                        List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                        for (FrameOu topou : ouList) {
                            list.addAll(userService.listUserByOuGuid(topou.getOuguid(), "", "", "", false, true, true, 3));
                        }
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < selectedItems.size(); j++) {
                                if (list.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                    selectedItems.remove(j);
                                }
                            }
                            selectedItems.add(  new SelectItem(list.get(i).getUserGuid(),list.get(i).getDisplayName()));
                        }    
                    }else{
                        List<FrameUser> list = new ArrayList<>();
                        list = userService.listUserByOuGuid(treeNode.getId(), "", "", "", false, true, true, 1);
                        List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                        for (FrameOu topou : ouList) {
                            list.addAll(userService.listUserByOuGuid(topou.getOuguid(), "", "", "", false, true, true, 3));
                        }
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < selectedItems.size(); j++) {
                                if (list.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                    selectedItems.remove(j);
                                }
                            }
                        }    
                    }
                    return selectedItems;
                }
            };
        }
        return treeModel;
    }

    
    /**
     * 根据windowguid获取用户SelectItem，初始化的信息
     * 
     * @param windowGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<SelectItem> getSelectUser(String windowGuid) {
        List<SelectItem> userItemList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(windowGuid)) {
            List<AuditOrgaWindowUser> userList = auditWindowImpl.getUserByWindow(windowGuid).getResult();
            if (userList != null && userList.size() > 0) {
                for (AuditOrgaWindowUser user : userList) {
                    if(user.getUserguid()!=null && user.getUsername()!=null){
                        userItemList.add(new SelectItem(user.getUserguid(), user.getUsername()));                        
                    }
                }
            }
        }
        
        return userItemList;
    }
    
    
    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    /**
     * 获取批量新增用户树数据
     * 
     * @return String
     */
//    public TreeModel getUserModel(){
//        if (userModel == null) {
//            userModel = new TreeModel()
//            {
//                /***
//                 * 加载树，懒加载
//                 */
//                @Override
//                public List<TreeNode> fetch(TreeNode treeNode) {
//                    TreeData treeData = TreeFunction9.getData(treeNode);
//                    List<TreeNode> list = new ArrayList<>();
////                    String groupguid = iAuditOrgaDepartment.getDepartmentByRowguid(getRequestParameter("departmentGuid")).getResult().getGroupguid();
//                    String groupguid="";
//                    SqlConditionUtil sql=new SqlConditionUtil();
//                    sql.eq("groupguid", groupguid);
//                    List<AuditOrgaMember> memList = iAuditOrgaMember.getAuditMemberList(sql.getMap()).getResult();
//                    //已存在于部门的userguid
//                    Set<String> userguids = new HashSet<String>();
//                    for (AuditOrgaMember a : memList) {
//                        userguids.add(a.getUserguid());
//                    }
//                    SqlConditionUtil sqldept=new SqlConditionUtil();
////                    sqldept.eq("rowGuid", getRequestParameter("departmentGuid"));
//                    
//                    String ouGuid = iAuditOrgaDepartment.getAuditDepartment(sqldept.getMap()).getResult().getOuguid();
//                    ouGuid="3731e57d-269f-45b4-96cb-e1b6410e07ae";
//                    if (treeData == null) {
//                        TreeNode root = new TreeNode();
//                        root.setText(ouService.getOuByOuGuid(ouGuid).getOuname());
//                        root.setId("root");
//                        root.setPid("-1");
//                        root.getColumns().put("isOU", "true");//标记：不是部门节点
//                        list.add(root);
//                        root.setCkr(false);
//                        root.setExpanded(false);//展开下一层节点
//                    }
//                    else {
//                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
//                            
//                        }else{
//                            List addlist = new ArrayList();
//                            String objectGuid = treeData.getObjectGuid();
//                            if ("root".equals(objectGuid)) {
//                                List<FrameOu> ouList = ouService.listOUByGuid(ouGuid, 5);
//                                addlist.addAll(ouList);
//                                List<FrameUser> userList = userService.getUserListByOuGuidWithNotEnabled(ouGuid, "", "", "",
//                                        false, false, false, 1);
//                                addlist.addAll(userList);
//                            }else{
//                                addlist.addAll(ouService.listOUByGuid(objectGuid, 5));
//                                List<FrameUser> userList = userService.getUserListByOuGuidWithNotEnabled(
//                                        objectGuid, "", "", "", false, false, false, 1);
//                                addlist.addAll(userList);   
//                            }
//                            for (Object obj : addlist) {
//                                // 将dataList转化为frameou的list
//                                if (obj instanceof FrameOu) {
//                                    FrameOu frameOu = (FrameOu) obj;
//                                    TreeNode node = new TreeNode();
//                                    node.setId(frameOu.getOuguid());
//                                    node.setText(frameOu.getOuname());
//                                    node.setPid(objectGuid);
//                                    node.setCkr(false);
//                                    node.getColumns().put("isOU", "true");//标记：是部门节点
//                                    node.setLeaf(false);
//                                    // 没有子节点的不允许点击
//                                    int a = 0;
//                                    int b = 0;
//                                    // 判断当前节点是否含有子节点 ( >0有子节点，<0无子节点)
//                                    if (StringUtil.isNotBlank(treeData.getObjectcode())) {
//                                        String pid = treeData.getObjectcode();
//                                        List<FrameOu> oulist = ouService.listOUByGuid(pid, 5);
//                                        a = oulist.size();
//                                    }
//                                    if (StringUtil.isNotBlank(treeData.getObjectcode())) {
//                                        String ouguid = treeData.getObjectcode();
//                                        b = userService.getUserCountByOuGuid(ouguid, "", "", "", false, false, false, 1);
//                                    }
//                                    int childCount = a > b ? a : b;
//                                    if (childCount > 0) {
//                                        // objectcode的作用是来区分是点击了部门还是事项
//                                        node.getColumns().put("isOU", "true");
//                                        list.add(node);
//                                    }
//                                }
//                                // 将dataList转化为AuditTask的list
//                                if (obj instanceof FrameUser) {
//                                    FrameUser user = (FrameUser) obj;
//                                    TreeNode node = new TreeNode();
//                                    node.setId(user.getUserGuid());
//                                    node.setText(user.getDisplayName());
//                                    node.setLeaf(true);
//                                    // objectcode的作用是来区分是点击了部门还是事项
//                                    node.getColumns().put("isOU", "false");
//                                    if (userguids.contains(user.getUserGuid())) {
//                                        node.setCkr(false);
//                                    }
//                                    list.add(node);
//                                }
//                            }
//                            
//                        }
//                    }
//                    return list;
//                }
//            };
//        }
//        return userModel;
//    }

    
    /**
     * 通过userguid查询username
     * 
     * @param userguid
     * @return
     */
    public String getOuUserNameByUserguid(String userguid) {
        return userService.getUserNameByUserGuid(userguid);
    }
    
    /**
     * 通过userguid获取user
     * 
     * @param userguid
     * @return
     */
    public FrameUser getUserByUserguid(String userguid) {
        List<String> list = new ArrayList<String>();
        list.add(userguid);
        List<FrameUser> userList = userService.getUserListByUserguids(list);
        FrameUser user = userList.get(0);
        return user;
    }
    
    
    /**
     * 获取批量新增用户树数据
     * 
     * @return String
     */
    public TreeModel getUserModel(){
//        areacode="370800";
        areacode="";
        String roleguid="832c67a8-1468-45a2-92cd-6c683fc6cafb";
//        String roleguid="d7656f4b-95af-416a-a351-240a507e0171";
//        String roleguid="";
        if (userModel == null) {
            userModel = new TreeModel(){
                
                private static final long serialVersionUID = -7417424000390574490L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    List<FrameOu> listou = new ArrayList<>();
                    List<FrameUser> listuser = new ArrayList<>();
                    List<ViewFrameUser> viewlistuser = new ArrayList<>();
                    //根节点
                    if(treeNode==null){
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }else{
                        if(StringUtil.isNotBlank(treeNode.getSearchCondition())){
                            TreeNode root = new TreeNode();
                            root.setText("所有部门");
                            root.setId("root");
                            root.setPid("-1");
                            list.add(root);
                            List<FrameOu> oulist = frameOu.getWindowOUList(areacode,true).getResult();
//                            oulist = ouService.listAllOu();
                            for (FrameOu frameou : oulist) {
                                List<ViewFrameUser> userList = userService.paginatorUserViewByOuGuid(frameou.getOuguid(), treeNode.getSearchCondition(), "",
                                        "", "", "", 0, 100, "", "", true, false).getList();
                                 viewlistuser.addAll(userList);
                            }   
                            
                            //去重操作
                            viewlistuser = viewlistuser.stream().distinct().collect(Collectors.toList());
                            AuditOrgaArea orgaarea=auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                            List<FrameUser> listareauser = new ArrayList<FrameUser>();
                            String ouguid="";
                            if(orgaarea!=null){
                                ouguid=orgaarea.getOuguid();
                            }
                            listareauser = userService.listUserByOuGuid(ouguid, roleguid, "", "", false, true, true, 1);
                            for (FrameUser frameuser : listareauser) {
                                if(frameuser.getDisplayName().indexOf(treeNode.getSearchCondition())!=-1  && listuser.lastIndexOf(frameuser) == -1){
                                    listuser.add(frameuser);
                                }
                            }
                        }else{
                           if("root".equals(treeNode.getId())){
                               AuditOrgaArea orgaarea=auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                               String ouguid="";
//                               if(orgaarea!=null){
//                                   ouguid=orgaarea.getOuguid();
//                               }
                               listuser = userService.listUserByOuGuid(ouguid, roleguid, "", "", false, true, true, 1);
                               List<FrameOu> ouList = ouService.listOUByGuid(ouguid, 5);
//                               if(ouList!=null){
//                                   ouList.removeIf(ou -> {
//                                       FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
//                                       if(ouExt != null) {
//                                           if (StringUtil.isNotBlank(ouExt.get("areacode"))
//                                                   && areacode.equals(ouExt.get("areacode").toString())) {
//                                               return false;
//                                           } else {
//                                               return true;
//                                           } 
//                                       }else {
//                                           return true;
//                                       }
//                                       
//                                   });                            
//                               }
                               if (ouList != null && ouList.size() > 0) {
                                   for (FrameOu ou : ouList) {
                                       listou.add(ou);
                                   }
                               }  
                           }else{
                               listuser = userService.listUserByOuGuid(treeNode.getId(),roleguid, "", "", false, true, true, 1);
                               FrameOuExtendInfo treeouExt = ouService.getFrameOuExtendInfo(treeNode.getId());
                               String treearea=treeouExt.get("areacode");
                               List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                               if(ouList!=null){
                                   ouList.removeIf(ou -> {
                                       FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                       if (ouExt != null) {
                                           if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                                   && treearea.equals(ouExt.get("areacode").toString())) {
                                               return false;
                                           }
                                           else {
                                               return true;
                                           }
                                       }else {
                                           return true;
                                       }
                                       
                                   });
                               }
                               if (ouList != null && ouList.size() > 0) {
                                   for (FrameOu ou : ouList) {
                                       listou.add(ou);
                                   }
                               }
                           }
                        }
                    }
                    
                    //list转换
                    for (ViewFrameUser viewFrameUser : viewlistuser) {
                        TreeNode node = new TreeNode();
                        node.setId(viewFrameUser.getUserGuid());
                        node.setText(viewFrameUser.getDisplayName() + "(" + viewFrameUser.getOuname() + ")");
                        node.getColumns().put("isOU", "0");
                        node.setImg(viewFrameUser.getMobile());
                        node.setPid("root");
                        node.setLeaf(true);
                        list.add(node);
                    }
                    for (FrameUser frameUser : listuser) {
                        TreeNode node = new TreeNode();
                        node.setId(frameUser.getUserGuid());
                        node.setText(frameUser.getDisplayName());
                        node.getColumns().put("isOU", "0");
                        node.setImg(frameUser.getMobile());
                        node.setLeaf(true);
                        node.setPid(treeNode.getId()==null?"root":treeNode.getId());
                        list.add(node);
                    }
                    
                    for (FrameOu frameou : listou) {
                        TreeNode node = new TreeNode();
                        node.setId(frameou.getOuguid());
                        node.setText(frameou.getOuname());
                        node.getColumns().put("isOU", "1");
                        node.setPid(StringUtil.isBlank(treeNode.getId())?"root":treeNode.getId());
                        node.setLeaf(false);
                        List<FrameUser> frameUsers = userService.listUserByOuGuid(frameou.getOuguid(), roleguid, "", "",
                                false, true, true, 3);
                        int childCount = 0;
                        if (frameUsers != null) {
                            childCount = frameUsers.size();
                        }
                        if(childCount > 0){
                            list.add(node);                                
                        }
                    }
                    return list;
                }
                
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    List<SelectItem> selectedItems = userModel.getSelectNode();
                    if(treeNode.isChecked() == true){
                        List<FrameUser> list = new ArrayList<>();
                        list = userService.listUserByOuGuid(treeNode.getId(), roleguid, "", "", false, true, true, 1);
                        List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                        for (FrameOu topou : ouList) {
                            list.addAll(userService.listUserByOuGuid(topou.getOuguid(), roleguid, "", "", false, true, true, 3));
                        }
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < selectedItems.size(); j++) {
                                if (list.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                    selectedItems.remove(j);
                                }
                            }
                            selectedItems.add(  new SelectItem(list.get(i).getUserGuid(),list.get(i).getDisplayName()));
                        }    
                    }else{
                        List<FrameUser> list = new ArrayList<>();
                        list = userService.listUserByOuGuid(treeNode.getId(), roleguid, "", "", false, true, true, 1);
                        List<FrameOu> ouList = ouService.listOUByGuid(treeNode.getId(), 5);
                        for (FrameOu topou : ouList) {
                            list.addAll(userService.listUserByOuGuid(topou.getOuguid(), roleguid, "", "", false, true, true, 3));
                        }
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < selectedItems.size(); j++) {
                                if (list.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                    selectedItems.remove(j);
                                }
                            }
                        }    
                    }
                    return selectedItems;
                }
            };
        }
        return userModel;
    }
}

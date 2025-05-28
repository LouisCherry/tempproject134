package com.epoint.auditorga.auditwindow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;

/**
 * 窗口人员配置页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("auditwindowuserconfigaction")
@Scope("request")
public class AuditWindowUserConfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;
    /**
     * 窗口与人员的关联实体对象
     */
    private AuditOrgaWindowUser dataBean = null;
    /**
     * 窗口人员model
     */
    private TreeModel treeModel = null;
    /**
     * 窗口标识
     */
    private String windowGuid = null;
    
    @Autowired
    private IHandleFrameOU frameOu ;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    private String areacode= "";
    private String centerguid= "";
    
    @Override
    public void pageLoad() {
        windowGuid = getRequestParameter("guid");
        
        areacode = this.getRequestParameter("areacode");
        centerguid = this.getRequestParameter("centerguid");
        if(StringUtil.isBlank(areacode)){
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        
        if(StringUtil.isBlank(centerguid)){
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        }
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

    /**
     * 
     * 保存用户到窗口下
     * 
     * @param guidList
     *            前台传来的参数， 用‘_SPLIT_’分割
     * @param windowGuid
     *            窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveUserToWindow(String guidList, String windowGuid) {

        // 先删除该窗口下的所有人员关系
        if (StringUtil.isNotBlank(windowGuid)) {
            log.info("删除窗口："+windowGuid+",操作人："+userSession.getDisplayName());

            auditWindowImpl.deleteWindowUserByWindowGuid(windowGuid);
            // 添加新的人员关联关系
            if (StringUtil.isNotBlank(guidList)) {
                String[] array = guidList.split("_SPLIT_");
                if (array.length > 0) {
                    String[] userGuids = array[0].split(";");
                    if (StringUtil.isNotBlank(windowGuid)) {
                        for (int i = 0; i < userGuids.length; i++) {
                            AuditOrgaWindowUser auditWindowUser = new AuditOrgaWindowUser();
                            auditWindowUser.setRowguid(UUID.randomUUID().toString());
                            auditWindowUser.setWindowguid(windowGuid);
                            auditWindowUser.setUserguid(userGuids[i]);
                            auditWindowUser.setUsername(userService.getUserNameByUserGuid(userGuids[i]));
                            auditWindowUser.setOperatedate(new Date());
                            auditWindowUser.setOperateusername(UserSession.getInstance().getDisplayName());
                            auditWindowImpl.insertWindowUser(auditWindowUser);
                        }
                    }
                }
            }
        }
    }

    public AuditOrgaWindowUser getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaWindowUser();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaWindowUser dataBean) {
        this.dataBean = dataBean;
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
     * 
     * 获取窗口人员树
     * 
     * @param windowGuid
     *            窗口guid
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

  /*  public SimpleFetchHandler9 loadAllUser1(String windowGuid) {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9()
        {

            *//**
             * 
             *//*
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List<FrameOu> list = new ArrayList();
                List listuser = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    list = frameOu.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(),true).getResult();
                    if (list != null && list.size() > 0) {
                        for (FrameOu frameou : list) {
                            List<ViewFrameUser> userList = userService.paginatorUserViewByOuGuid(frameou.getOuguid(), conndition, "",
                                    "", "", "", 0, 100, "", "", true, false).getList();
                            if(!listuser.containsAll(userList)){
                                listuser.addAll(userList);
                            }
                        }
                    }
                }
                AuditOrgaArea orgaarea=auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                List<FrameUser> listareauser = new ArrayList<FrameUser>();
                String ouguid="";
                if(orgaarea!=null){
                    ouguid=orgaarea.getOuguid();
                }
                listareauser = userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1);
                for (FrameUser frameuser : listareauser) {
					if(frameuser.getDisplayName().indexOf(conndition)!=-1  && listuser.lastIndexOf(frameuser) == -1){
						listuser.add(frameuser);
					}
				}
                return listuser;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        
                    }
                    else {
                       
                    }
                }
                // 点击checkbox的时候触发
                else {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的人员的list
                    if ("frameou".equals(treeData.getObjectcode())) {
                        list = userService.listUserByOuGuid(treeData.getObjectGuid(), "", "", "", false, true, true, 1);
                        List<FrameOu> ouList = ouService.listOUByGuid(treeData.getObjectGuid(), 5);                       
                        if (ouList != null && ouList.size() > 0) {
                            for (FrameOu ou : ouList) {
                                list.addAll(userService.listUserByOuGuid(ou.getOuguid(), "", "", "", false, true, true, 1));
                            }
                        }
                    }
                    else
                    {
                        List<FrameOu> listOU = frameOu.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(),true).getResult();
                        for(FrameOu topou:listOU)
                        {
                            list.addAll(userService.listUserByOuGuid(topou.getOuguid(), "", "", "", false, true, true, 3));                                                  
                        }
                    }
                }
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                int a = 0;
                int b = 0;
                a = ouService.listOUByGuid(treeData.getObjectGuid(), 5).size();
                b = userService.getUserCountByOuGuid(treeData.getObjectGuid(), "", "", "", false, false, false, 1);
                if (a > 0 || b > 0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        // 将dataList转化为frameou的list
                        if (obj instanceof FrameOu) {
                            FrameOu frameOu = (FrameOu) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameOu.getOuguid());
                            treeData.setTitle(frameOu.getOuname());
                            // 没有子节点的不允许点击
                            List<FrameUser> frameUsers = userService.listUserByOuGuid(frameOu.getOuguid(), "", "", "",
                                    false, true, true, 3);
                            int childCount = 0;
                            if (frameUsers != null) {
                                childCount = frameUsers.size();
                            }
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            }
                            else {
                                // objectcode的作用是来区分是点击了部门还是事项
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }
                        }
                        // 将dataList转化为AuditTask的list
                        if (obj instanceof FrameUser) {
                            FrameUser frameuser = (FrameUser) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameuser.getUserGuid());
                            treeData.setTitle(frameuser.getDisplayName());
                            // objectcode的作用是来区分是点击了部门还是事项
                            treeData.setObjectcode("frameuser");
                            treeList.add(treeData);
                        }
                        if (obj instanceof ViewFrameUser) {
                            ViewFrameUser frameuser = (ViewFrameUser) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameuser.getUserGuid());
                            treeData.setTitle(frameuser.getDisplayName() + "(" + frameuser.getOuname() + ")");
                            // objectcode的作用是来区分是点击了部门还是事项
                            treeData.setObjectcode("frameuser");
                            treeList.add(treeData);
                        }
                    }
                }

                return treeList;
            }
        };
        return fetchHandler9;
    }
    */
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

}

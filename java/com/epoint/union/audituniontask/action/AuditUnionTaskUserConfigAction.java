package com.epoint.union.audituniontask.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.union.audituniontaskuser.api.IAuditUnionTaskUserService;
import com.epoint.union.audituniontaskuser.api.entity.AuditUnionTaskUser;

/**
 * 窗口人员配置页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("audituniontaskuserconfigaction")
@Scope("request")
public class AuditUnionTaskUserConfigAction extends BaseController
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
     * 事项标识
     */
    private String taskid = null;
    
    @Autowired
    private IHandleFrameOU frameOu ;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOuService ouService;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    
    @Autowired
    private IAuditUnionTaskUserService auditUnionTaskUserService;

    private String areacode= "";
    private String centerguid= "";
    
    @Override
    public void pageLoad() {
    	taskid = getRequestParameter("guid");
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
            treeModel = loadAllUser();
            if (!isPostback()) {
                treeModel.setSelectNode(getSelectUser(taskid));
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
    public void saveUserToWindow(String guidList, String task_id) {
        // 先删除该窗口下的所有人员关系
        if (StringUtil.isNotBlank(task_id)) {
        	auditUnionTaskUserService.deleteByTaskid(task_id);
            // 添加新的人员关联关系
            if (StringUtil.isNotBlank(guidList)) {
                String[] array = guidList.split("_SPLIT_");
                if (array.length > 0) {
                    String[] userGuids = array[0].split(";");
                    if (StringUtil.isNotBlank(taskid)) {
                        for (int i = 0; i < userGuids.length; i++) {
                        	AuditUnionTaskUser auditUniontaskUser = new AuditUnionTaskUser();
                        	auditUniontaskUser.setRowguid(UUID.randomUUID().toString());
                        	auditUniontaskUser.setUserguid(userGuids[i]);
                        	auditUniontaskUser.setUsername(userService.getUserNameByUserGuid(userGuids[i]));
                        	auditUniontaskUser.setOperatedate(new Date());
                        	auditUniontaskUser.setOperateusername(UserSession.getInstance().getDisplayName());
                        	auditUniontaskUser.setTask_id(task_id);
                            auditUnionTaskUserService.insert(auditUniontaskUser);
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
    
    
    public TreeModel loadAllUser(){
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
                                if(!listuser.containsAll(userList)){
                                    viewlistuser.addAll(userList);
                                }
                            }   
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
                                       if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                               && areacode.equals(ouExt.get("areacode").toString())) {
                                           return false;
                                       } else {
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
                                       if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                               && treearea.equals(ouExt.get("areacode").toString())) {
                                           return false;
                                       }
                                       else {
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
    public List<SelectItem> getSelectUser(String taskid) {
        List<SelectItem> userItemList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(taskid)) {
            List<Record> userList = auditUnionTaskUserService.getUserBytaskid(taskid);
            if (userList != null && userList.size() > 0) {
                for (Record user : userList) {
                    if(user.getStr("userguid")!=null && user.getStr("username")!=null){
                        userItemList.add(new SelectItem(user.getStr("userguid"), user.getStr("username")));                        
                    }
                }
            }
        }
        
        return userItemList;
    }

}

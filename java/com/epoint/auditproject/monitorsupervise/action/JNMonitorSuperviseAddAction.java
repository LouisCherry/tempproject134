package com.epoint.auditproject.monitorsupervise.action;

import com.epoint.auditproject.monitorsupervise.api.IJNAuditProjectMonitorService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmonitor.domain.AuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectmonitor.inter.IAuditProjectMonitor;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TabType;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 督查督办基本信息表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2016-11-11 13:55:02]
 */
@RestController("jnmonitorsuperviseaddaction")
@Scope("request")
public class JNMonitorSuperviseAddAction extends BaseController {
    private static final long serialVersionUID = 181286499037869484L;
    /**
     * 办件实体对象
     */
    private AuditProject auditProject = null;
    /**
     * 页面绑定字段：督办对象标识
     */
    private String monitorGuids;
    /**
     * 页面绑定字段：督办对象名称
     */
    private String monitorNames;
    /**
     * 页面绑定字段：督办意见
     */
    private String monitorOption;

    private int deadTime;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditProjectMonitor superviseService;

    @Autowired
    private IMessagesCenterService messageCenterService;

    private String ouguid;

    private String usertype;

    @Autowired
    private IJNAuditProjectMonitorService ijnAuditProjectMonitorService;


    private TreeModel treeModel_Ou;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IUserService userservice;

    @Override
    public void pageLoad() {
        String rowGuid = getRequestParameter("guid");
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouguid,centerguid";
        auditProject = auditProjectService
                .getAuditProjectByRowGuid(fields, rowGuid, ZwfwUserSession.getInstance().getAreaCode()).getResult();
        if (auditProject != null) {
            ouguid = auditProject.getOuguid();
        }
    }

    /**
     * 发起督办
     */
    public void add() {
        if (StringUtil.isNotBlank(monitorGuids)) {
            String[] guids = monitorGuids.split(",");
            String supname = "";
            for (int i = 0; i < guids.length; i++) {
                FrameUser user = userService.getUserByUserField("UserGuid", guids[i]);
                FrameOu ou = null;
                if (user != null) {
                    ou = ouService.getOuByOuGuid(user.getOuGuid());
                    String supervise2 = ijnAuditProjectMonitorService
                            .findsupervise(user.getUserGuid(), auditProject.getRowguid());
                    if (supervise2 != null) {
                        supname += user.getDisplayName() + ",";
                        continue;
                    } else {
                        // 1.新增督办记录
                        AuditProjectMonitor supervise = new AuditProjectMonitor();
                        supervise.setMonitor_id(user.getUserGuid());
                        supervise.setUpdate_date(new Date());
                        supervise.setMonitor_person(user.getDisplayName());
                        supervise.setSupervise_date(new Date());
                        supervise.setMonitor_name(ou.getOuname());
                        supervise.setMonitor_org_id(ou.getOuguid());
                        supervise.setSupervise_id(userSession.getUserGuid());
                        supervise.setSupervise_person(userSession.getDisplayName());
                        supervise.setBj_no(auditProject.getFlowsn());
                        supervise.setNo(UUID.randomUUID().toString());
                        supervise.setDubantype(10);// 。net写死的
                        supervise.setSupervise_opinion(this.monitorOption);
                        supervise.setSupervise_org_id(userSession.getOuGuid());
                        supervise.setBj_type("非处罚类办件");// 。net写死的
                        supervise.setRowguid(UUID.randomUUID().toString());
                        supervise.setOperatedate(new Date());
                        supervise.setOperateusername(userSession.getDisplayName());
                        supervise.setAreacode(auditProject.getAreacode());
                        supervise.setProjectname(auditProject.getProjectname());
                        supervise.setSupervise_reply_date(EpointDateUtil.addDay(new Date(), deadTime));
                        supervise.setIsreply(ZwfwConstant.CONSTANT_INT_ONE);
                        supervise.setSpvtype(ZwfwConstant.CONSTANT_INT_ZERO);
                        supervise.setCenterguid(auditProject.getCenterguid());
                        supervise.setAlertguid(auditProject.getRowguid());
                        supervise.set("usertype", usertype);
                        superviseService.addMonitorSupervise(supervise);
                        // 2.发送待办事宜
                        String title = "【督办】关于【" + auditProject.getProjectname() + "】办件的监察督办("
                                + supervise.getSupervise_person() + ")";
                        String handleUrl = "epointzwfw/auditmonitor/auditprojectmonitor/monitorsupervisehandle?monitorGuid="
                                + supervise.getRowguid() + "&isreply=1";
                        String messageItemGuid = UUID.randomUUID().toString();
                        if (StringUtil.isNotBlank(handleUrl) && (handleUrl.indexOf("?") > 0)) {
                            handleUrl += "&messageItemGuid=" + messageItemGuid;
                        } else {
                            handleUrl += "?messageItemGuid=" + messageItemGuid;
                        }
                        handleUrl += "&clientIdentifier=" + supervise.getRowguid();
                        messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                                IMessagesCenterService.MESSAGETYPE_WAIT, user.getUserGuid(), user.getDisplayName(),
                                userSession.getUserGuid(), userSession.getDisplayName(), "待回复督办", handleUrl,
                                user.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "", supervise.getRowguid(),
                                supervise.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                                user.getUserGuid(), "", "");
                    }
                }
            }
            if (supname == "") {
                addCallbackParam("message", "督办发起成功！");
            } else {
                if (supname.endsWith(",")) {
                    supname = supname.substring(0, supname.length() - 1);
                }
                addCallbackParam("message", "发送失败，给" + supname + "的待办已发送");
            }
        }
    }

    public TreeModel getTreeOuModel() {
        if (treeModel_Ou == null) {
            // 获取到tab到底属于哪个选项
            String tabKey = requestContext.getReq().getParameter("tabKey");


            if (StringUtil.isBlank(tabKey) || tabKey.equals(TabType.OWNOU)) {
                // 加载本部门
                treeModel_Ou = new TreeModel() {

                    /***
                     * 加载树，懒加载
                     */
                    @Override
                    public List<TreeNode> fetch(TreeNode treeNode) {
                        TreeData treeData = TreeFunction9.getData(treeNode);
                        List<TreeNode> list = new ArrayList<>();
                        // 初次加载根节点
                        if (treeData == null) {
                            TreeNode root = new TreeNode();

                            root.setText("本部门");
                            //因为配置了免登录，所以此处无法取UserSession，正确的应该用注释的代码
                            root.setId(UserSession.getInstance().getOuGuid());
                            root.setPid("-1");
                            root.getColumns().put("isOU", "true");// 标记：是不是部门节点
                            list.add(root);
                            root.setExpanded(true);// 展开下一层节点

                            list.addAll(fetch(root));// 自动加载下一层树结构
                        }
                        // 每点一次+号，进入加载树
                        else {
                            // 首先判断是搜索还是继续加载树节点
                            // 搜索功能
                            if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {

                                // 以下是框架封装的方法，具体参数请参见api
                                List<FrameUser> listsearchUser = userservice.listUserByOuGuid(null, null, null,
                                        "name:" + treeNode.getSearchCondition(), false, true, false, 3);

                                // 搜索完成后，进行树的绑定
                                for (int j = 0; j < listsearchUser.size(); j++) {
                                    TreeNode node = new TreeNode();
                                    node.setId(listsearchUser.get(j).getUserGuid());
                                    node.setText(listsearchUser.get(j).getDisplayName());
                                    node.getColumns().put("isOU", "false");// 标记：是不是部门节点
                                    node.setPid("-1");
                                    node.setLeaf(true);
                                    list.add(node);
                                }
                            } else {
                                // 获取到需要加载节点的Guid
                                String objectGuid = treeData.getObjectGuid();

                                // 查询出所有人员的绑定信息，以下是框架封装的方法，具体参数请参见api
                                List<FrameUser> listUser = userservice.listUserByOuGuid(objectGuid, null, null, null,
                                        false, true, false, 1);
                                List<FrameOu> listOu = ouservice.listOUByGuid(objectGuid, 2);

                                // 部门的绑定
                                for (int i = 0; i < listOu.size(); i++) {
                                    if ((listOu.get(i).getParentOuguid() == null && "".equals(objectGuid))
                                            || (listOu.get(i).getParentOuguid().equals(objectGuid))) {
                                        TreeNode node = new TreeNode();
                                        node.setId(listOu.get(i).getOuguid());
                                        node.setText(listOu.get(i).getOuname());
                                        node.setPid(listOu.get(i).getParentOuguid());
                                        node.getColumns().put("isOU", "true");// 标记：是部门节点
                                        node.setLeaf(true);

                                        int Usercount = userservice.getUserCountByOuGuid(listOu.get(i).getOuguid(),
                                                null, null, null, false, true, false, 3);

                                        for (int j = 0; j < listOu.size(); j++) {
                                            if (listOu.get(i).getOuguid().equals(listOu.get(j).getParentOuguid())
                                                    || Usercount != 0) {
                                                node.setLeaf(false);
                                                break;
                                            }
                                        }

                                        list.add(node);
                                    }
                                }

                                // 人员的绑定
                                for (int j = 0; j < listUser.size(); j++) {
                                    TreeNode node2 = new TreeNode();
                                    node2.setId(listUser.get(j).getUserGuid());
                                    node2.setText(listUser.get(j).getDisplayName());
                                    node2.setPid(listUser.get(j).getOuGuid());
                                    node2.getColumns().put("isOU", "false");// 标记：不是部门节点
                                    node2.setLeaf(true);
                                    list.add(node2);
                                }
                            }
                        }
                        return list;
                    }

                    /***
                     * 懒加载进行获取数据，把左边树中选择的内容加载到右边
                     */
                    @Override
                    public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                        // 获取到tree原有的select

                        List<SelectItem> selectedItems = treeModel_Ou.getSelectNode();

                        // 复选框选中
                        if (treeNode.isChecked() == true) {
                            // 利用标记的isOU做判断
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {

                                // 以下是框架封装的方法，具体参数请参见api
                                List<FrameUser> listUser = userservice.listUserByOuGuid(treeNode.getId(), null, null,
                                        null, false, true, false, 3);

                                for (int i = 0; i < listUser.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {

                                        if (listUser.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }

                                    }
                                    selectedItems.add(new SelectItem(listUser.get(i).getUserGuid(),
                                            listUser.get(i).getDisplayName()));
                                }
                            }
                            // 如果是个人直接绑定
                            else {
                                selectedItems.add(new SelectItem(treeNode.getId(), treeNode.getText()));
                            }
                        }
                        // 复选框取消选中
                        else {
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {

                                // 以下是框架封装的方法，具体参数请参见api
                                List<FrameUser> listUser = userservice.listUserByOuGuid(treeNode.getId(), null, null,
                                        null, false, true, false, 3);

                                for (int i = 0; i < listUser.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (selectedItems.get(j).getValue().equals(listUser.get(i).getUserGuid())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    if (selectedItems.get(i).getValue().equals(treeNode.getId())) {
                                        selectedItems.remove(i);
                                    }
                                }
                            }
                        }
                        return selectedItems;
                    }

                    ;
                };

            } else if (StringUtil.isNotBlank(tabKey) && tabKey.equals(TabType.OU)) {
                // 加载所有部门
                treeModel_Ou = new TreeModel() {
                    /***
                     * 加载树，懒加载
                     */
                    @Override
                    public List<TreeNode> fetch(TreeNode treeNode) {

                        TreeData treeData = TreeFunction9.getData(treeNode);
                        List<TreeNode> list = new ArrayList<>();

                        if (treeData == null) {
                            TreeNode root = new TreeNode();

                            root.setText("所有用戶");
                            root.setId("");
                            root.setPid("-1");
                            root.getColumns().put("isOU", "true");// 标记：是不是部门节点
                            list.add(root);
                            root.setExpanded(true);// 展开下一层节点
                            list.addAll(fetch(root));// 自动加载下一层树结构
                        } else {
                            if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {

                                // 根据选择的搜索内容进行查询，以下是框架封装的方法，具体参数请参见api
                                List<FrameUser> listsearchUser = userservice.listUserByOuGuid(null, null, null,
                                        "name:" + treeNode.getSearchCondition(), false, true, false, 3);

                                // 搜索完成后，进行树的绑定
                                for (int j = 0; j < listsearchUser.size(); j++) {
                                    TreeNode node = new TreeNode();
                                    node.setId(listsearchUser.get(j).getUserGuid());
                                    node.setText(listsearchUser.get(j).getDisplayName());
                                    node.getColumns().put("isOU", "false");// 标记：不是部门节点
                                    node.setPid("-1");
                                    node.setLeaf(true);
                                    list.add(node);
                                }
                            } else {
                                String objectGuid = treeData.getObjectGuid();

                                // 查询出所有人员的绑定信息，以下是框架封装的方法，具体参数请参见api
                                List<FrameUser> listUser = userservice.listUserByOuGuid(objectGuid, null, null, null,
                                        false, true, false, 1);
                                List<FrameOu> listOu = ouservice.listOUByGuid(objectGuid, 2);

                                // 部门的绑定
                                for (int i = 0; i < listOu.size(); i++) {
                                    if ((listOu.get(i).getParentOuguid() == null && "".equals(objectGuid))
                                            || (listOu.get(i).getParentOuguid().equals(objectGuid))) {
                                        TreeNode node = new TreeNode();
                                        node.setId(listOu.get(i).getOuguid());
                                        node.setText(listOu.get(i).getOuname());
                                        node.setPid(listOu.get(i).getParentOuguid());
                                        node.getColumns().put("isOU", "true");// 标记：是部门节点
                                        node.setLeaf(true);

                                        int Usercount = userservice.getUserCountByOuGuid(listOu.get(i).getOuguid(),
                                                null, null, null, false, true, false, 3);

                                        for (int j = 0; j < listOu.size(); j++) {
                                            if (listOu.get(i).getOuguid().equals(listOu.get(j).getParentOuguid())
                                                    || Usercount != 0) {
                                                node.setLeaf(false);
                                                break;
                                            }
                                        }

                                        list.add(node);
                                    }
                                }

                                // 人员的绑定
                                for (int j = 0; j < listUser.size(); j++) {
                                    TreeNode node2 = new TreeNode();
                                    node2.setId(listUser.get(j).getUserGuid());
                                    node2.setText(listUser.get(j).getDisplayName());
                                    node2.setPid(listUser.get(j).getOuGuid());
                                    node2.getColumns().put("isOU", "false");// 标记：不是部门节点
                                    node2.setLeaf(true);
                                    list.add(node2);
                                }
                            }
                        }
                        return list;
                    }

                    /***
                     * 懒加载进行获取数据，把左边树中选择的内容加载到右边
                     */
                    @Override
                    public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                        // 获取到tree原有的select
                        List<SelectItem> selectedItems = treeModel_Ou.getSelectNode();
                        // 复选框选中
                        if (treeNode.isChecked() == true) {
                            // 利用标记的isOU做判断，如果是部门需要到webserviceOU进行递归查询
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {

                                List<FrameUser> listUser = userservice.listUserByOuGuid(treeNode.getId(), null, null,
                                        null, false, true, false, 3);

                                for (int i = 0; i < listUser.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {

                                        if (listUser.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }

                                    }
                                    selectedItems.add(new SelectItem(listUser.get(i).getUserGuid(),
                                            listUser.get(i).getDisplayName()));
                                }
                            }
                            // 如果是个人直接绑定
                            else {
                                selectedItems.add(new SelectItem(treeNode.getId(), treeNode.getText()));
                            }
                        }
                        // 复选框取消选中
                        else {
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {

                                if ("".equals(treeNode.getId())) {
                                    selectedItems.clear();
                                } else {
                                    List<FrameUser> listUser = userservice.listUserByOuGuid(treeNode.getId(), null,
                                            null, null, false, true, false, 3);

                                    for (int i = 0; i < listUser.size(); i++) {
                                        for (int j = 0; j < selectedItems.size(); j++) {
                                            if (selectedItems.get(j).getValue().equals(listUser.get(i).getUserGuid())) {
                                                selectedItems.remove(j);
                                            }
                                        }
                                    }
                                }
                            } else {

                                for (int i = 0; i < selectedItems.size(); i++) {
                                    if (selectedItems.get(i).getValue().equals(treeNode.getId())) {
                                        selectedItems.remove(i);
                                    }
                                }
                            }
                        }
                        return selectedItems;

                    }

                };
            }

            if (!isPostback()) {
                setSelectedTreeNode();
            }
        }
        return treeModel_Ou;
    }

    public void setSelectedTreeNode() {
        List<SelectItem> sItem = new ArrayList<SelectItem>();
        //因为配置了免登录，所以此处无法取UserSession，正确的应该用注释的代码
        sItem.add(new SelectItem(UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName()));
        treeModel_Ou.setSelectNode(sItem);
    }

    public String getOuguid() {
        return ouguid;
    }

    public void setOuguid(String ouguid) {
        this.ouguid = ouguid;
    }

    public AuditProject getAuditProject() {
        return auditProject;
    }

    public void setAuditProject(AuditProject auditProject) {
        this.auditProject = auditProject;
    }

    public String getMonitorGuids() {
        return monitorGuids;
    }

    public void setMonitorGuids(String monitorGuids) {
        this.monitorGuids = monitorGuids;
    }

    public String getMonitorNames() {
        return monitorNames;
    }

    public void setMonitorNames(String monitorNames) {
        this.monitorNames = monitorNames;
    }

    public String getMonitorOption() {
        return monitorOption;
    }

    public void setMonitorOption(String monitorOption) {
        this.monitorOption = monitorOption;
    }

    public int getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(int deadTime) {
        this.deadTime = deadTime;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getUsertypeList() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("0", "申请人/单位"));
        result.add(new SelectItem("1", "中心人员"));
        return result;

    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUsertype() {
        return usertype;
    }

}

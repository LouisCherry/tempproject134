package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import com.epoint.workflow.service.organ.api.IWFOrganAPI9;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 许可变更意见模块对应的action
 *
 * 2018年10月7日
 * @author shibin
 *
 */
@RestController("jnchangeopinionaction")
@Scope("request")
public class JnChangeOpinionAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    private AuditTaskShareFile dataBean;

    private String fileclientguid;

    //页面显示的字段
    private String monitorGuids;

    private String monitorOption;

    private String monitorTheme;

    private String deadTime;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMessagesCenterService messageCenterService;

    private String ouGuid;
    private String areaCode;
    private String userGuid;

    /**
     * 树数据模型
     */
    private TreeModel treeModel;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IWFOrganAPI9 organAPI;

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    @Autowired
    private IJnAuditJianGuan taAuditJianGuanService;

    @Override
    public void pageLoad() {

        ouGuid = userSession.getOuGuid();
        userGuid = userSession.getUserGuid();
        areaCode = ZwfwUserSession.getInstance().getAreaCode();

        this.dataBean = new AuditTaskShareFile();
        if (StringUtil.isNotBlank(this.dataBean.getFileclientguid())) {
            this.fileclientguid = this.dataBean.getFileclientguid();
            this.addViewData("fileclientguid", this.fileclientguid);
        }

        if (StringUtil.isBlank(this.getViewData("fileclientguid"))) {
            this.fileclientguid = UUID.randomUUID().toString();
            this.addViewData("fileclientguid", this.fileclientguid);
        }
    }

    /**
     *  确认发送信息
     */
    public void send() {
        if (StringUtil.isNotBlank(monitorGuids)) {
            String[] guids = monitorGuids.split(";");
            for (int i = 0; i < guids.length; i++) {
                FrameUser user = userService.getUserByUserField("UserGuid", guids[i]);
                FrameOu ou = ouService.getOuByOuGuid(user.getOuGuid());
                if (user != null) {
                    // 1.新增记录
                    AuditProjectPermissionChange permissionchange = new AuditProjectPermissionChange();

                    permissionchange.setRowguid(UUID.randomUUID().toString());
                    permissionchange.setOuguid(userSession.getOuGuid());
                    permissionchange.setOuname(userSession.getOuName());
                    permissionchange.setSendUserGuid(userSession.getUserGuid());
                    permissionchange.setSendperson(userSession.getDisplayName());
                    permissionchange.setCommunicationOuGuid(ou.getOuguid());
                    permissionchange.setCommunicationOuName(ou.getOuname());
                    permissionchange.setCommunicationUserGuid(user.getUserGuid());
                    permissionchange.setCommunicationperson(user.getDisplayName());
                    permissionchange.setCommunicationtheme(monitorTheme);
                    permissionchange.setThemeguid(UUID.randomUUID().toString());
                    permissionchange.setChangeopinion(monitorOption);
                    permissionchange.setDeadtime(deadTime);
                    permissionchange.setFileclientguid(this.getViewData("fileclientguid"));
                    permissionchange.setReplydate(new Date());

                    //taAuditJianGuanService.insert(permissionchange);

                    // 2.发送待办事宜
                    String title = "【发起许可变更意见】关于【" + monitorTheme + "】主题的变更意见("
                            + permissionchange.getCommunicationperson() + ")";
                    String handleUrl = "jiningzwfw/sghd/projectjianguan/auditprojectbiangeng?monitorGuid="
                            + permissionchange.getRowguid() + "&isreply=1&selectuserguid=" + guids[i] + "&senduserguid="
                            + userSession.getUserGuid();
                    String messageItemGuid = UUID.randomUUID().toString();
                    if (StringUtil.isNotBlank(handleUrl) && (handleUrl.indexOf("?") > 0)) {
                        handleUrl += "&messageItemGuid=" + messageItemGuid;
                    }
                    else {
                        handleUrl += "?messageItemGuid=" + messageItemGuid;
                    }
                    handleUrl += "&clientIdentifier=" + permissionchange.getRowguid();

                    permissionchange.setHandleUrl(handleUrl);
                    taAuditJianGuanService.insert(permissionchange);

                    messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                            IMessagesCenterService.MESSAGETYPE_WAIT, user.getUserGuid(), user.getDisplayName(),
                            userSession.getUserGuid(), userSession.getDisplayName(), "待回复意见", handleUrl,
                            user.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "", permissionchange.getRowguid(),
                            permissionchange.getRowguid().substring(0, 1), new Date(), "", user.getUserGuid(), "", "");

                }
            }
        }
        addCallbackParam("message", "发起成功！");

    }

    public AuditTaskShareFile getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTaskShareFile dataBean) {
        this.dataBean = dataBean;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("fileclientguid"),
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    /**
     *  许可变更意见交流对象人员选择
     */
    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    //首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有人员");
                        root.setId("root");
                        root.getColumns().put("isOU", "true");
                        list.add(root);
                        //展开下一层节点
                        root.setExpanded(true);
                        //自动加载下一层树结构
                        list.addAll(fetch(root));
                    }
                    //每次点击树节点前的加号，进行加载
                    else {

                        // 中心审管角色
                        if (iRoleService.isExistUserRoleName(userSession.getUserGuid(), "中心审管")) {
                            List<Record> ouguidList = new ArrayList<>();

                            ouguidList = taAuditJianGuanService.getChangeOpinionOuInfo(areaCode);

                            //设置部门节点
                            for (int i = 0; i < ouguidList.size(); i++) {
                                TreeNode node = new TreeNode();
                                node.setId(ouguidList.get(i).getStr("OUGUID"));
                                node.setText(ouguidList.get(i).getStr("OUNAME"));
                                node.setPid("root");
                                node.getColumns().put("isOU", "true");
                                node.setCkr(false);
                                node.setChecked(false);

                                List<FrameUser> listRenyuan = new ArrayList<>();
                                listRenyuan = taAuditJianGuanService
                                        .getChangeOpinionFrameOu(ouguidList.get(i).getStr("OUGUID"));
                                list.add(node);

                                //  展示部门审管人员,设置人员节点
                                for (int j = 0; j < listRenyuan.size(); j++) {
                                    if (iRoleService.isExistUserRoleName(listRenyuan.get(j).getUserGuid(), "部门审管")) {
                                        // 有部门审管角色
                                        node.setLeaf(false);
                                        TreeNode node1 = new TreeNode();
                                        node1.setId(listRenyuan.get(j).getUserGuid());
                                        node1.setText(listRenyuan.get(j).getDisplayName());
                                        node1.setPid(ouguidList.get(i).getStr("OUGUID"));
                                        node1.setCkr(true);
                                        node1.setLeaf(true);
                                        node1.getColumns().put("isOU", "false");
                                        node1.setChecked(true);
                                        list.add(node1);
                                    }
                                    else {
                                        // 没有部门审管角色
                                        node.setLeaf(true);
                                    }
                                }
                            }

                        }
                        else {
                            // 部门审管角色

                            List<Record> listOu = new ArrayList<>();
                            listOu = taAuditJianGuanService.getCenterOunameinfo(areaCode);

                            //设置部门节点
                            Boolean flag = true;
                            for (int i = 0; i < listOu.size(); i++) {
                                TreeNode node = new TreeNode();
                                node.setId(listOu.get(i).getStr("OUGUID"));
                                node.setText(listOu.get(i).getStr("OUNAME"));
                                node.setPid("root");
                                node.getColumns().put("isOU", "true");
                                node.setCkr(false);
                                node.setChecked(false);
                                list.add(node);

                                List<Record> listChildOu = new ArrayList<>();
                                listChildOu = taAuditJianGuanService
                                        .findChildOuByParentguid(listOu.get(i).getStr("OUGUID"));

                                List<Record> listRenyuan = new ArrayList<>();
                                listRenyuan = taAuditJianGuanService
                                        .getChangeOpinionFrameOu2(listOu.get(i).getStr("OUGUID"));
                                // 设置人员节点
                                for (int j = 0; j < listRenyuan.size(); j++) {
                                    if (listRenyuan.get(j).getStr("OUGUID").equals(ouGuid)) {
                                        // 去除登录人员
                                        if (listRenyuan.get(j).getStr("USERGUID").equals(userSession.getUserGuid())) {
                                            continue;
                                        }
                                        TreeNode node1 = new TreeNode();
                                        node1.setId(listRenyuan.get(j).getStr("USERGUID"));
                                        node1.setText(listRenyuan.get(j).getStr("DISPLAYNAME"));
                                        node1.setPid(listRenyuan.get(j).getStr("OUGUID"));
                                        node1.setCkr(true);
                                        node1.getColumns().put("isOU", "false");
                                        node1.setLeaf(true);
                                        list.add(node1);
                                    }
                                    else {
                                        if (iRoleService.isExistUserRoleName(listRenyuan.get(j).getStr("USERGUID"),
                                                "中心审管")) {
                                            // 有中心审管角色
                                            node.setLeaf(false);
                                            TreeNode node11 = new TreeNode();
                                            node11.setId(listRenyuan.get(j).getStr("USERGUID"));
                                            node11.setText(listRenyuan.get(j).getStr("DISPLAYNAME"));
                                            node11.setPid(listRenyuan.get(j).getStr("OUGUID"));
                                            node11.setCkr(true);
                                            node11.setLeaf(true);
                                            node11.getColumns().put("isOU", "false");
                                            node11.setChecked(true);
                                            list.add(node11);
                                        }
                                        else {
                                            // 没有中心审管角色
                                            node.setLeaf(true);
                                        }

                                    }
                                }

                                // 设置二级部门节点以及人员信息
                                for (Record record : listChildOu) {
                                    TreeNode node2 = new TreeNode();
                                    node2.setId(record.getStr("OUGUID"));
                                    node2.setText(record.getStr("OUNAME"));
                                    node2.setPid(listOu.get(i).getStr("OUGUID"));
                                    node2.getColumns().put("isOU", "true");
                                    node2.setCkr(false);
                                    node2.setChecked(false);
                                    list.add(node2);
                                    List<Record> listRenyuan3 = new ArrayList<>();
                                    listRenyuan3 = taAuditJianGuanService
                                            .getChangeOpinionFrameOu2(record.getStr("OUGUID"));

                                    if (listRenyuan3 != null && listRenyuan3.size() > 0) {
                                        // 设置人员节点
                                        for (int j = 0; j < listRenyuan3.size(); j++) {
                                            if (listRenyuan3.get(j).getStr("OUGUID").equals(ouGuid)) {
                                                // 去除登录人员
                                                if (listRenyuan3.get(j).getStr("USERGUID")
                                                        .equals(userSession.getUserGuid())) {
                                                    continue;
                                                }
                                                TreeNode node3 = new TreeNode();
                                                node3.setId(listRenyuan3.get(j).getStr("USERGUID"));
                                                node3.setText(listRenyuan3.get(j).getStr("DISPLAYNAME"));
                                                node3.setPid(listRenyuan3.get(j).getStr("OUGUID"));
                                                node3.setCkr(true);
                                                node3.getColumns().put("isOU", "false");
                                                node3.setLeaf(true);
                                                list.add(node3);
                                            }
                                            else {
                                                if (iRoleService.isExistUserRoleName(
                                                        listRenyuan3.get(j).getStr("USERGUID"), "中心审管")) {
                                                    // 有中心审管角色
                                                    node2.setLeaf(false);
                                                    TreeNode node4 = new TreeNode();
                                                    node4.setId(listRenyuan3.get(j).getStr("USERGUID"));
                                                    node4.setText(listRenyuan3.get(j).getStr("DISPLAYNAME"));
                                                    node4.setPid(listRenyuan3.get(j).getStr("OUGUID"));
                                                    node4.setCkr(true);
                                                    node4.setLeaf(true);
                                                    node4.getColumns().put("isOU", "false");
                                                    node4.setChecked(true);
                                                    list.add(node4);
                                                }
                                                else {
                                                    // 没有中心审管角色
                                                    node2.setLeaf(true);
                                                }

                                            }
                                        }
                                    }
                                    else {
                                        node2.setLeaf(true);
                                    }

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

    public String getMonitorGuids() {
        return monitorGuids;
    }

    public void setMonitorGuids(String monitorGuids) {
        this.monitorGuids = monitorGuids;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    public String getMonitorOption() {
        return monitorOption;
    }

    public void setMonitorOption(String monitorOption) {
        this.monitorOption = monitorOption;
    }

    public String getMonitorTheme() {
        return monitorTheme;
    }

    public void setMonitorTheme(String monitorTheme) {
        this.monitorTheme = monitorTheme;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

}

package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmonitor.domain.AuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectmonitor.inter.IAuditProjectMonitor;
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
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import com.epoint.workflow.service.common.entity.organization.WorkflowUser;
import com.epoint.workflow.service.organ.api.IWFOrganAPI9;
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
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnprojectstartjgaction")
@Scope("request")
public class JnProjectStartJgAction extends BaseController {
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

    private AuditTaskShareFile dataBean;

    private String fileclientguid;
    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

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

    private TreeModel treeModel;

    @Autowired
    private IWFOrganAPI9 organAPI;

    @Autowired
    private IAuditOrgaServiceCenter centerService;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    private RenlingService jgservice = new RenlingService();

    @Override
    public void pageLoad() {
        String rowGuid = getRequestParameter("guid");
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouguid,centerguid";
        auditProject = auditProjectService
                .getAuditProjectByRowGuid(fields, rowGuid, ZwfwUserSession.getInstance().getAreaCode()).getResult();
        if (auditProject != null) {
            ouguid = auditProject.getOuguid();
        }

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
     * 发起督办
     */
    public void add() {
        if (StringUtil.isNotBlank(monitorGuids)) {
            String[] guids = monitorGuids.split(";");
            for (int i = 0; i < guids.length; i++) {
                FrameUser user = userService.getUserByUserField("UserGuid", guids[i]);
                FrameOu ou = ouService.getOuByOuGuid(user.getOuGuid());
                if (user != null) {
                    // 1.新增监察记录
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
                    // 。net写死的
                    supervise.setDubantype(10);
                    supervise.setSupervise_opinion(this.monitorOption);
                    supervise.setSupervise_org_id(userSession.getOuGuid());
                    // 。net写死的
                    supervise.setBj_type("非处罚类办件");
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
                    superviseService.addMonitorSupervise(supervise);

                    //                    permissionchange.setFileclientguid(this.getViewData("fileclientguid"));
                    //String sql = "UPDATE audit_project_monitor SET fileclientguid = ? WHERE rowguid = ? ";
                    //dao.execute(sql, this.getViewData("fileclientguid"), supervise.getRowguid());
                    jnAuditJianGuanService.updateFileclientguid(this.getViewData("fileclientguid"),
                            supervise.getRowguid());
                    // 2.发送待办事宜
                    String title = "【监管】关于【" + auditProject.getProjectname() + "】办件的监管("
                            + supervise.getSupervise_person() + ")";
                    String handleUrl = "epointjxgl/runmonitor/supervision/monitorsupervisehandle?monitorGuid="
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
                            userSession.getUserGuid(), userSession.getDisplayName(), "待回复监管", handleUrl,
                            user.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "", supervise.getRowguid(),
                            supervise.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                            user.getUserGuid(), "", "");
                    //更新监管时间
                    jgservice.jianguan(auditProject.getRowguid());
                }
            }
        }
        addCallbackParam("message", "监管发起成功！");
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel() {
                /**
                 *
                 */
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
                        list.add(root);
                        //展开下一层节点
                        root.setExpanded(true);
                        //自动加载下一层树结构
                        list.addAll(fetch(root));
                    }
                    //每次点击树节点前的加号，进行加载
                    else {
                        //                        String objectGuid = treeData.getObjectGuid();
                        if (ouService.getOuByOuGuid(ouguid) == null) {
                            AuditOrgaServiceCenter serviceCenter = centerService
                                    .findAuditServiceCenterByGuid(ZwfwUserSession.getInstance().getCenterGuid())
                                    .getResult();
                            ouguid = serviceCenter.getOuguid();
                        }
                        List<WorkflowUser> listUser = organAPI.getUserListByOuGuid(ouguid, null, "", null, false, false,
                                false, 3);
                        //部门的绑定
                        for (int i = 0; i < listUser.size(); i++) {
                            if (userSession.getUserGuid().equals(listUser.get(i).getUserGuid())) {
                                continue;
                            }
                            TreeNode node = new TreeNode();
                            node.setId(listUser.get(i).getUserGuid());
                            node.setText(listUser.get(i).getDisplayName());
                            node.setPid("root");
                            node.setCkr(true);
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

    /**
     * 附件上传功能
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9() {
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

}

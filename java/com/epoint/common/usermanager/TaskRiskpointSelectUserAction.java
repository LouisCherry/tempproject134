package com.epoint.common.usermanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowParticipator;
import com.epoint.workflow.service.common.entity.organization.WorkflowUser;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowParticipatorService;
import com.epoint.workflow.service.organ.api.IWFOrganAPI9;

/**
 * 
 * 政务服务选择用户
 * 
 * @version [版本号, 2016年8月25日]
 */
@RestController("taskriskpointselectuseraction")
@Scope("request")
public class TaskRiskpointSelectUserAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -6720503910593188651L;
    /**
     * 用户树model
     */
    private TreeModel treeModel = null;
    /**
     * 已选人员标识
     */
    private String userGuids;
    /**
     * 已选人员姓名
     */
    private String userNames;

    // 岗位ROWGUID
    private String rowguid;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IWFOrganAPI9 organAPI;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuditTaskRiskpoint iAuditTaskRiskpoint;

    @Autowired
    private IOuService ouService;

    /**
     * 工作流活动service
     */
    @Autowired
    private IWorkflowActivityService wfaservice;

    /**
     * 工作流处理人service
     */
    @Autowired
    private IWorkflowParticipatorService participatorService;

    /**
     * 工作流处理人service
     */
    @Autowired
    private IAuditTask audittaskservice;
    
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;

    @Override
    public void pageLoad() {
        userGuids = getRequestParameter("userGuids");
        userNames = getRequestParameter("userNames");
        rowguid = getRequestParameter("rowguid");
    }

    /**
     * 
     * 确定选择人员
     * 
     */
    public void confirmSelectUser() {
        String userGuids = "";// 所选用户标识
        String userNames = "";// 所选用户姓名
        List<SelectItem> selectItems = getTreeModel().getSelectNode();
        for (SelectItem selectItem : selectItems) {
            userGuids += selectItem.getValue() + ";";
            userNames += selectItem.getText() + ";";
        }
        if (StringUtil.isNotBlank(userGuids) && StringUtil.isNotBlank(userNames)) {
            // 去除末尾的分号
            userGuids = userGuids.substring(0, userGuids.length() - 1);
            userNames = userNames.substring(0, userNames.length() - 1);
        }
        if (StringUtil.isNotBlank(rowguid)) {
            // 如果为岗位页面过来的新增，则需要即时保存数据
            AuditTaskRiskpoint auditTaskRiskpoint = iAuditTaskRiskpoint.getAuditTaskRiskpointByRowguid(rowguid, false).getResult();
            auditTaskRiskpoint.setAcceptguid(userGuids);
            auditTaskRiskpoint.setAcceptname(userNames);
            iAuditTaskRiskpoint.updateAuditTaskRiskpoint(auditTaskRiskpoint);
            // 判断如果已生成内部工作流，则添加流程处理人
            WorkflowActivity activity = wfaservice.getByActivityGuid(auditTaskRiskpoint.getActivityguid());
            AuditTask task = audittaskservice.getAuditTaskByGuid(auditTaskRiskpoint.getTaskguid(), false).getResult();
            
            if (activity != null) {
                participatorService.deleteBySourceGuid(auditTaskRiskpoint.getActivityguid());
                String[] acceptguidStrArray = auditTaskRiskpoint.getAcceptguid().split(";");
                for (int i = 0; i < acceptguidStrArray.length; i++) {
                    WorkflowParticipator wp = new WorkflowParticipator();
                    if (StringUtil.isNotBlank(acceptguidStrArray[i])) {
                        wp.setBelongDept(ouService.getOuByUserGuid(acceptguidStrArray[i]).getOuguid());
                    }
                    else {
                        wp.setBelongDept("");
                    }
                    wp.setBelongTo(10);
                    wp.setDataId(acceptguidStrArray[i]);
                    wp.setParticipatorGuid(UUID.randomUUID().toString());
                    wp.setParticipatorName("人员");
                    wp.setMethodGuid("");
                    wp.setProcessVersionGuid(task.getPvguid());
                    wp.setSourceGuid(auditTaskRiskpoint.getActivityguid());
                    wp.setModeType(10);
                    wp.setType(30);
                    participatorService.addWorkflowParticipator(wp);
                    // participatorService.addOne(task.getPvguid(),
                    // auditTaskRiskpoint.getActivityguid(), 10,
                    // acceptguidStrArray[i], acceptnameStrArray[i], 30);
                }
            }
            
            // 市级：将配置人员关联的窗口增加该事项
            if ("370800".equals(task.getAreacode()) && StringUtil.isNotBlank(auditTaskRiskpoint.getAcceptguid())) {
                List<AuditOrgaWindow> allWindowList = new ArrayList<AuditOrgaWindow>();
                String[] userGuidArray = auditTaskRiskpoint.getAcceptguid().split(";");
                for (String userGuid : userGuidArray) {
                    List<AuditOrgaWindow> userWindowList = auditWindowImpl.getWindowListByUserGuid(userGuid).getResult();
                    if (userWindowList != null && !userWindowList.isEmpty()) {
                        allWindowList.addAll(userWindowList);
                    }
                    else {
                        String userName = userService.getUserNameByUserGuid(userGuid);
                        addCallbackParam("error", "【" + userName + "】没有配置任何窗口！");
                        break;
                    }
                }

                for (AuditOrgaWindow auditOrgaWindow : allWindowList) {
                    // 判断该窗口是否配置该事项，没配置的话，将事项加入窗口
                    List<AuditOrgaWindowTask> existList = auditWindowImpl.getTaskByWindowAndTaskId(auditOrgaWindow.getRowguid(), task.getTask_id()).getResult();
                    if (existList == null || existList.isEmpty()) {
                        AuditOrgaWindowTask auditWindowTask = new AuditOrgaWindowTask();
                        auditWindowTask.setRowguid(UUID.randomUUID().toString());
                        auditWindowTask.setWindowguid(auditOrgaWindow.getRowguid());
                        auditWindowTask.setTaskguid(task.getRowguid());
                        auditWindowTask.setOperatedate(new Date());
                        auditWindowTask.setOrdernum(999);
                        auditWindowTask.setTaskid(task.getTask_id());
                        auditWindowTask.setEnabled("1");// 插入的数据默认为有效
                        auditWindowImpl.insertWindowTask(auditWindowTask);
                    }
                }
            }
            
        }
        this.addCallbackParam("namesAndGuids", userGuids + "_SPLIT_" + userNames);
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            String tabKey = getRequestParameter("tabKey");
            if ("allUser".equals(tabKey)) {
                treeModel = loadAllUser();
            }
            else if (StringUtil.isBlank(tabKey) || "ouUser".equals(tabKey)) {
                treeModel = loadCurrentOu();
            }
            if (!isPostback()) {
                if (StringUtil.isNotBlank(rowguid)) {
                    AuditTaskRiskpoint auditTaskRiskpoint = iAuditTaskRiskpoint.getAuditTaskRiskpointByRowguid(rowguid, false).getResult();
                    if (StringUtil.isNotBlank(auditTaskRiskpoint.getAcceptguid())) {
                        String[] arruserGuids = auditTaskRiskpoint.getAcceptguid().split(";");
                        // String[] arruserNames = userNames.split(";");
                        List<SelectItem> selectItems = new ArrayList<SelectItem>();
                        for (int i = 0; i < arruserGuids.length; i++) {
                            SelectItem item = new SelectItem();
                            // IE下乱码
                            // item.setText(arruserNames[i]);
                            item.setValue(arruserGuids[i]);
                            item.setText(userService.getUserNameByUserGuid(item.getValue().toString()));
                            selectItems.add(item);
                        }
                        treeModel.setSelectNode(selectItems);
                    }
                }
                else if (StringUtil.isNotBlank(userGuids)) {
                    String[] arruserGuids = userGuids.split(";");
                    List<SelectItem> selectItems = new ArrayList<SelectItem>();
                    for (int i = 0; i < arruserGuids.length; i++) {
                        SelectItem item = new SelectItem();
                        // IE下乱码
                        // item.setText(arruserNames[i]);
                        item.setValue(arruserGuids[i]);
                        item.setText(userService.getUserNameByUserGuid(item.getValue().toString()));
                        selectItems.add(item);
                    }
                    treeModel.setSelectNode(selectItems);
                }
                else {
                    String[] arruserGuids = userGuids.split(";");
                    String[] arruserNames = userNames.split(";");
                    List<SelectItem> selectItems = new ArrayList<SelectItem>();
                    for (int i = 0; i < arruserGuids.length; i++) {
                        SelectItem item = new SelectItem();
                        item.setValue(arruserGuids[i]);
                        item.setText(arruserNames[i]);
                        selectItems.add(item);
                    }
                    treeModel.setSelectNode(selectItems);
                }
            }
        }
        return treeModel;
    }

    /**
     * 加载所有部门
     * 
     * @return
     */
    public FetchHandler9 loadAllOu() {
        AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        String ouGuid = auditOrgaArea.getOuguid();
        FetchHandler9 handler = new EpointTreeHandler9(ConstValue9.SPECIFIED_FRAMEOU, ouGuid);
        return handler;
    }

    public TreeModel loadAllUser() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    String condition = "";
                    String id = "";
                    if (treeNode != null) {
                        condition = treeNode.getSearchCondition();
                        id = treeNode.getId();
                    }
                    List<TreeNode> list = new ArrayList<>();
                    List<FrameOu> listou = new ArrayList<>();
                    List<FrameUser> listuser = new ArrayList<>();
                    List<ViewFrameUser> viewlistuser = new ArrayList<>();

                    if (treeNode == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有用户");
                        root.setId("root");
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(true);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        if (StringUtil.isNotBlank(condition)) {
                            AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            if (auditOrgaArea != null) {
                                viewlistuser = userService.paginatorUserViewByOuGuid(auditOrgaArea.getOuguid(), condition, null, null, null, null, 0, 100, null, null, true, false).getList();
                            }
                        }
                        else {
                            String ouGuid = treeNode.getId();
                            if ("root".equals(ouGuid)) {
                                AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                                if (auditOrgaArea != null) {
                                    ouGuid = auditOrgaArea.getOuguid();
                                }
                            }
                            List<FrameOu> frameOus = ouService.listOUByGuid(ouGuid, 5);
                            listou.addAll(frameOus);

                            List<FrameUser> frameUsers = userService.listUserByOuGuid(ouGuid, null, null, null, false, false, false, 1);
                            listuser.addAll(frameUsers);
                        }
                        // list转换
                        for (ViewFrameUser viewFrameUser : viewlistuser) {
                            TreeNode node = new TreeNode();
                            node.setId(viewFrameUser.getUserGuid());
                            node.setText(dealCondition(viewFrameUser.getDisplayName(), condition));
                            node.getColumns().put("isOU", "0");
                            node.setPid("root");
                            node.setLeaf(true);
                            list.add(node);
                        }
                        for (FrameUser frameUser : listuser) {
                            TreeNode node = new TreeNode();
                            node.setId(frameUser.getUserGuid());
                            node.setText(dealCondition(frameUser.getDisplayName(), condition));
                            node.getColumns().put("isOU", "0");
                            node.setLeaf(true);
                            node.setPid(id == null ? "root" : id);
                            list.add(node);
                        }

                        for (FrameOu frameou : listou) {
                            TreeNode node = new TreeNode();
                            node.setId(frameou.getOuguid());
                            node.setText(frameou.getOuname());
                            node.getColumns().put("isOU", "1");
                            node.setPid(StringUtil.isBlank(id) ? "root" : id);
                            node.setLeaf(false);
                            int userCount = userService.getUserCountByOuGuid(frameou.getOuguid(), null, null, null, false, false, false, 3);
                            if (userCount > 0) {
                                list.add(node);
                            }
                        }
                    }
                    return list;
                }

                public String dealCondition(String content, String conddition) {
                    return content;
                }

                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    List<SelectItem> selectedItems = treeModel.getSelectNode();
                    List<FrameUser> list = new ArrayList<>();
                    if ("root".equals(treeNode.getId())) {
                        AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                        if (auditOrgaArea != null) {
                            list = userService.listUserByOuGuid(auditOrgaArea.getOuguid(), null, null, null, false, false, false, 3);
                        }
                    }
                    else {
                        list = userService.listUserByOuGuid(treeNode.getId(), null, null, null, false, false, false, 3);
                    }
                    if (treeNode.isChecked() == true) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < selectedItems.size(); j++) {
                                if (list.get(i).getUserGuid().equals(selectedItems.get(j).getValue())) {
                                    selectedItems.remove(j);
                                }
                            }
                            selectedItems.add(new SelectItem(list.get(i).getUserGuid(), list.get(i).getDisplayName()));
                        }
                    }
                    else {
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

    public TreeModel loadCurrentOu() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    String condition = "";
                    String id = "";
                    if (treeNode != null) {
                        condition = treeNode.getSearchCondition();
                        id = treeNode.getId();
                    }
                    List<TreeNode> list = new ArrayList<>();
                    List<WorkflowUser> workflowuserlist = new ArrayList<>();

                    if (treeNode == null) {
                        TreeNode root = new TreeNode();
                        root.setText("本部门");
                        root.setId("root");
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(true);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        if (StringUtil.isNotBlank(condition)) {
                            workflowuserlist = organAPI.getUserListByOuGuid(userSession.getOuGuid(), null, userSession.getBaseOUGuid(), "name:" + condition, false, true, false, 3);
                        }
                        else {
                            workflowuserlist = organAPI.getUserListByOuGuid(userSession.getOuGuid(), null, "", null, false, false, false, 3);
                        }

                        for (WorkflowUser workflowuser : workflowuserlist) {
                            TreeNode node = new TreeNode();
                            node.setId(workflowuser.getUserGuid());
                            node.setText(dealCondition(workflowuser.getDisplayName(), condition));
                            node.setPid(id);
                            node.setLeaf(true);
                            list.add(node);
                        }
                    }
                    return list;
                }

                public String dealCondition(String content, String conddition) {
                    return content;
                }

            };
        }
        return treeModel;
    }

}

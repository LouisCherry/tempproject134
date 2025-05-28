package com.epoint.common.workflow;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.delegate.domain.AuditTaskDelegateAuth;
import com.epoint.basic.auditorga.delegate.inter.IAuditTaskDelegateAuth;
import com.epoint.basic.auditproject.auditprojectdefaultuser.domain.AuditProjectDefaultuser;
import com.epoint.basic.auditproject.auditprojectdefaultuser.inter.IAuditProjectDefaultuser;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.*;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.convert.ChineseToPinyin;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.organization.WorkflowUser;
import com.epoint.workflow.service.common.runtime.WorkflowParticatorMessage;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.workflow.service.organ.api.IWFOrganAPI9;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树控件action
 *
 * @author shengjia 2016-4-27
 */
@RestController("jnauditworkflowtreeaction")
@Scope("request")
public class JnAuditWorkflowTreeAction extends BaseController implements Serializable {
    private WorkflowParticatorMessage transactors = new WorkflowParticatorMessage();

    Map<String, String> selectOu = new HashMap<>();

    Map<String, String> selectUser = new HashMap<>();

    String ouStr = "";

    private ProcessVersionInstance pvi = null;

    @Autowired
    private IWFInstanceAPI9 oWAPI;

    @Autowired
    private IWFOrganAPI9 organAPI;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IAuditOrgaServiceCenter centerservice;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IWFDefineAPI9 iWFDefineAPI9;

    @Autowired
    private IAuditTaskRiskpoint iAuditTaskRiskpoint;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditProjectDefaultuser iAuditProjectDefaultuser;

    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private IUserServiceInternal frameUserService;

    @Autowired
    private IAuditTaskDelegateAuth iAuditTaskDelegateAuth;

    @Autowired
    private IAuditTaskDelegate iAuditTaskDelegate;

    @Autowired
    private IAuditOrgaArea areaImpl;

    @Autowired
    private IRoleService roleService;
    private LazyTreeModal9 categoryModel;

    private LazyTreeModal9 treeOuModel;

    /**
     * 树的选中值
     */
    private String treeValue = "";

    /**
     * 树选中文本
     */
    private String treeText = "";

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1281922122814877622L;

    private UserSession userSession = UserSession.getInstance();

    /**
     * 判断当前窗口是否是乡镇窗口
     */
    public boolean isWindowFalg = false;

    /**
     * 当前部门所在辖区guid
     */
    public String ouguid;

    String workItemGuid = "";

    String transitionGuid = "";

    String pviguid = "";

    @Override
    public void pageLoad() {
        workItemGuid = getRequestParameter("workItemGuid");
        transitionGuid = getRequestParameter("transitionGuid");
        pviguid = getRequestParameter("pviGuid");
        try {
            if (StringUtil.isBlank(transitionGuid)) {
                transitionGuid = getRequestParameter("stepguid");
            }
            pvi = oWAPI.getProcessVersionInstance(pviguid);
            transactors = oWAPI.getParticatorMessage(pvi, workItemGuid, transitionGuid, userSession.getUserGuid());
            if (transactors.getGuids() != null && transactors.getGuids().length > 0)
                for (String guid : transactors.getGuids()) {
                    if (StringUtil.isNotBlank(guid)) {
                        if (guid.contains("_gtigepoint_")) {
                            guid = guid.split("_gtigepoint_")[0];
                        }

                        WorkflowUser user = organAPI.getUserByUserGuid(guid);
                        if (user != null) {
                            selectOu.put(user.getOuGuid(), user.getOuGuid());
                            selectUser.put(user.getUserGuid(), user.getDisplayName());
                            ouStr += user.getOuGuid() + "','";
                        }
                    }
                }

            // 获取当前用户所在部门信息
            FrameOuExtendInfo info = ouService.getFrameOuExtendInfo(userSession.getOuGuid());
            if (info != null) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("xiaqucode", info.get("areacode"));
                AuditOrgaArea area = areaImpl.getAuditArea(sql.getMap()).getResult();
                if (area != null) {
                    ouguid = area.getOuguid();
                    if (ZwfwConstant.AREA_TYPE_XZJ.equals(area.getCitylevel())) {
                        isWindowFalg = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LazyTreeModal9 getCategoryModel() {
        if (categoryModel == null) {
            categoryModel = new LazyTreeModal9(new EpointTreeHandler9(ConstValue9.TABLECATEGORY));
            // categoryModel.setInitType(ConstValue9.CODE_INIT);
            categoryModel.setRootName("所有子系统");
        }
        return categoryModel;
    }

    public LazyTreeModal9 getTreeOuModel() {
        try {
            if (treeOuModel == null) {
                String tabKey = getRequestParameter("tabKey");
                String area = getRequestParameter("area");
                String taskid = getRequestParameter("taskid");
                String areacode = "";
                AuditTask task = iAuditTask.selectUsableTaskByTaskID(taskid).getResult();
                Boolean jntask = false;
                List<AuditTaskDelegate> delegate = iAuditTaskDelegate
                        .selectDelegateListByTaskIDAndAreacode(taskid, ZwfwUserSession.getInstance().getAreaCode())
                        .getResult();
                if (delegate != null && delegate.size() > 0) {
                    jntask = true;
                }
                if (task != null) {
                    areacode = task.getAreacode();
                }
                if (StringUtils.isBlank(tabKey) || TabType.OWNOU.equals(tabKey)) {
                    // 本部门
                    if (transactors != null) {
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            SimpleFetchHandler9 fetchHandler = loadSelectUser(tabKey, area, areacode, jntask);
                            treeOuModel = new LazyTreeModal9(fetchHandler);
                        } else {
                            FetchHandler9 fetchHandler = loadCurrentOu();
                            treeOuModel = new LazyTreeModal9(fetchHandler);
                        }
                    } else {
                        FetchHandler9 fetchHandler = loadCurrentOu();
                        treeOuModel = new LazyTreeModal9(fetchHandler);
                    }

                    treeOuModel.setTreeType(ConstValue9.CHECK_MULTI);
                    treeOuModel.setInitType(ConstValue9.GUID_INIT);
                    treeOuModel.setRootSelect(false);
                    treeOuModel.setRootName("本部门");
                } else if (StringUtils.isNotBlank(tabKey) && TabType.OU.equals(tabKey)) {
                    // 市级部门
                    if (transactors != null) {
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            treeOuModel = new LazyTreeModal9(loadSelectUser("xq", area, areacode, jntask));
                        } else {
                            treeOuModel = new LazyTreeModal9(loadAllUser("xq", area, areacode, jntask));
                        }
                    } else {
                        treeOuModel = new LazyTreeModal9(loadAllUser("xq", area, areacode, jntask));
                    }

                    treeOuModel.setTreeType(ConstValue9.CHECK_SINGLE);
                    treeOuModel.setInitType(ConstValue9.GUID_INIT);
                    treeOuModel.setRootSelect(false);
                    treeOuModel.setRootName("市级部门");
                } else if (StringUtils.isNotBlank(tabKey) && TabType.WORKFLOWOU.equals(tabKey)) {
                    // 可选用户
                    FetchHandler9 fetchHandler = loadEpointCurrentOu();
                    treeOuModel = new LazyTreeModal9(fetchHandler);
                    treeOuModel.setTreeType(ConstValue9.CHECK_SINGLE);
                    treeOuModel.setInitType(ConstValue9.GUID_INIT);
                    treeOuModel.setRootSelect(false);
                    treeOuModel.setRootName("可选用户");
                } else if (StringUtils.isNotBlank(tabKey) && "cunou".equals(tabKey)) {
                    // 本镇
                    if (transactors != null) {
                        // 市级部门
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            treeOuModel = new LazyTreeModal9(loadSelectUser("cun", area, areacode, jntask));
                        } else {
                            treeOuModel = new LazyTreeModal9(loadAllUser("cun", area, areacode, jntask));
                        }
                    } else {
                        treeOuModel = new LazyTreeModal9(loadAllUser("cun", area, areacode, jntask));
                    }

                    treeOuModel.setTreeType(ConstValue9.CHECK_SINGLE);
                    treeOuModel.setInitType(ConstValue9.GUID_INIT);
                    treeOuModel.setRootSelect(false);
                    treeOuModel.setRootName("村（社区）");
                } else if (StringUtils.isNotBlank(tabKey) && "townou".equals(tabKey)) {
                    // 本镇
                    if (transactors != null) {
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            treeOuModel = new LazyTreeModal9(loadSelectUser("xz", area, areacode, jntask));
                        } else {
                            treeOuModel = new LazyTreeModal9(loadAllUser("xz", area, areacode, jntask));
                        }
                    } else {
                        treeOuModel = new LazyTreeModal9(loadAllUser("xz", area, areacode, jntask));
                    }

                    treeOuModel.setTreeType(ConstValue9.CHECK_SINGLE);
                    treeOuModel.setInitType(ConstValue9.GUID_INIT);
                    treeOuModel.setRootSelect(false);
                    treeOuModel.setRootName("乡镇");
                } else if (StringUtils.isNotBlank(tabKey) && "cityou".equals(tabKey)) {
                    // 市级部门
                    if (transactors != null) {
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            treeOuModel = new LazyTreeModal9(loadSelectUser("xq", area, areacode, jntask));
                        } else {
                            treeOuModel = new LazyTreeModal9(loadAllUser("xq", area, areacode, jntask));
                        }
                    } else {
                        treeOuModel = new LazyTreeModal9(loadAllUser("xq", area, areacode, jntask));
                    }

                    treeOuModel.setTreeType(ConstValue9.CHECK_SINGLE);
                    treeOuModel.setInitType(ConstValue9.GUID_INIT);
                    treeOuModel.setRootSelect(false);
                    treeOuModel.setRootName("市级部门");
                } else if (StringUtils.isNotBlank(tabKey) && "all".equals(tabKey)) {
                    // 所有部门
                    if (transactors != null) {
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            SimpleFetchHandler9 fetchHandler = loadSelectUser(tabKey, area, areacode, jntask);
                            treeOuModel = new LazyTreeModal9(fetchHandler);
                        } else {
                            FetchHandler9 fetchHandler = loadXqAllOu();
                            treeOuModel = new LazyTreeModal9(fetchHandler);
                        }
                    } else {
                        FetchHandler9 fetchHandler = loadXqAllOu();
                        treeOuModel = new LazyTreeModal9(fetchHandler);
                    }

                    treeOuModel.setTreeType(ConstValue9.CHECK_MULTI);
                    treeOuModel.setRootName("所有用户");

                }
                if (!isPostback()) {
                    setSelectedTreeNode();
                }
            } else {
                try {
                    if (transactors != null) {
                        if (transactors.getGuids() == null || transactors.getGuids().length == 0) {
                            List<TreeNode> nodeList = treeOuModel.getWrappedData();
                            nodeList.removeIf(treeNode -> {
                                if (treeNode.getIsLeaf()
                                        && StringUtil.isBlank(treeNode.getColumns().get("objectcode"))) {
                                    boolean flag = roleService.isExistUserRoleName(treeNode.getId(), "前台工作人员");
                                    return flag;
                                }
                                return false;
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果默认选择的人为空 则过滤掉所有的
        return treeOuModel;
    }

    /**
     * 加载所有部门
     *
     * @return
     */
    public FetchHandler9 loadAllOu() {
        AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        String ouGuid = auditOrgaArea.getOuguid();
        FetchHandler9 handler = new EpointTreeHandler9(ConstValue9.SPECIFIED_FRAMEOU, ouGuid);
        return handler;
    }

    /**
     * 加载所有部门
     *
     * @return
     */
    public FetchHandler9 loadXqAllOu() {
        EpointTreeHandler9 handler = new EpointTreeHandler9(ConstValue9.FRAMEOU);
        handler.setForbidOuCheckLevel("0");
        return handler;
    }

    public LazyTreeModal9 loadCenterOu() {
        AuditOrgaServiceCenter centerguid = centerservice
                .findAuditServiceCenterByGuid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        String ouGuid = centerguid.getOuguid();
        FetchHandler9 handler = new EpointTreeHandler9(ConstValue9.SPECIFIED_FRAMEOU, ouGuid);
        LazyTreeModal9 treeOuModel = new LazyTreeModal9(handler);
        treeOuModel.setTreeType(ConstValue9.CHECK_MULTI);
        treeOuModel.setInitType(ConstValue9.GUID_INIT);
        treeOuModel.setRootName("所有部门");
        return treeOuModel;
    }

    /**
     * 加载本部门
     *
     * @return
     */
    public FetchHandler9 loadCurrentOu() {
        EpointTreeHandler9 fetchHandler = new EpointTreeHandler9(ConstValue9.OWN_DEPARTMENT);
        fetchHandler.setOwnDepartmentOwnTopOu(true);
        return fetchHandler;
    }

    /**
     * 框架加载本部门
     *
     * @return
     */
    @SuppressWarnings("serial")
    public FetchHandler9 loadEpointCurrentOu() {
        FetchHandler9 fetchHandler = new FetchHandler9() {

            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            public <T> List<T> fetchData(int level, int ID, TreeData obj) {
                List users = new ArrayList();
                if (obj != null) {
                    if (StringUtils.isBlank(obj.getObjectGuid()) || (StringUtils.isNotBlank(obj.getObjectGuid())
                            && obj.getObjectGuid().equals(TreeFunction9.F9ROOT))) {
                        if (transactors != null) {
                            if (transactors.getFilterGuids() != null && transactors.getFilterGuids().length > 0) {
                                for (String guid : transactors.getFilterGuids()) {
                                    if (StringUtil.isNotBlank(guid)) {
                                        if (guid.contains("_gtigepoint_")) {
                                            guid = guid.split("_gtigepoint_")[0];
                                        }
                                        WorkflowUser user = organAPI.getUserByUserGuid(guid);
                                        users.add(user);
                                    }
                                }
                            } else {
                                users = organAPI.getUserListByOuGuid(userSession.getOuGuid(), null,
                                        userSession.getBaseOUGuid(), null, false, true, false, 3);
                            }
                        } else {
                            users = organAPI.getUserListByOuGuid(userSession.getOuGuid(), null,
                                    userSession.getBaseOUGuid(), null, false, true, false, 3);
                        }

                    } else {
                        String objectGuid = obj.getObjectGuid();
                        objectGuid = objectGuid.split(EpointKeyNames9.SPECIAL_SPLITCHAR_1)[0];
                        WorkflowUser user = organAPI.getUserByUserGuid(objectGuid);
                        users.add(user);
                    }
                }

                return users;
            }

            @Override
            public int fetchChildCount(int ID, TreeData obj) {
                return 0;
            }

            @SuppressWarnings({"unchecked", "rawtypes", "unused"})
            @Override
            public <T> List<T> fetchTotalData(String condition, boolean hasUser) {
                String ouguid = null;
                List list = null;
                if (transactors != null) {
                    if (transactors.getFilterGuids() != null && transactors.getFilterGuids().length > 0) {
                        List users = new ArrayList();
                        for (String guid : transactors.getFilterGuids()) {
                            if (guid.contains("_gtigepoint_")) {
                                guid = guid.split("_gtigepoint_")[0];
                            }
                            WorkflowUser user = organAPI.getUserByUserGuid(guid);
                            if (user.getDisplayName().contains(condition)) {
                                users.add(user);
                            }
                        }
                        return users;
                    } else {
                        list = organAPI.getUserListByOuGuid(userSession.getOuGuid(), null, userSession.getBaseOUGuid(),
                                "name:" + condition, false, true, false, 3);

                    }
                } else {
                    list = organAPI.getUserListByOuGuid(userSession.getOuGuid(), null, userSession.getBaseOUGuid(),
                            "name:" + condition, false, true, false, 3);
                }
                return list;
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> objlist) {
                List<TreeData> list = new ArrayList<TreeData>();
                if (objlist != null && objlist.size() > 0) {
                    List<WorkflowUser> frameUsers = (List<WorkflowUser>) objlist;
                    for (WorkflowUser user : frameUsers) {
                        TreeData treeData = new TreeData();
                        treeData.setObjectGuid(user.getUserGuid());
                        treeData.setTitle(user.getDisplayName());
                        treeData.setLeaf(true);
                        list.add(treeData);
                    }
                }
                return list;
            }
        };
        return fetchHandler;
    }

    /**
     * 框架加载本部门
     *
     * @return
     */
    @SuppressWarnings("serial")
    public SimpleFetchHandler9 loadSelectUser(String tabKey, String area, String areacode1, boolean jntask) {
        SimpleFetchHandler9 fetchHandler = new SimpleFetchHandler9() {
            @SuppressWarnings("unchecked")
            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> objlist) {
                List<TreeData> list = new ArrayList<TreeData>();
                if (objlist != null && objlist.size() > 0) {
                    List<WorkflowUser> frameUsers = (List<WorkflowUser>) objlist;
                    for (WorkflowUser user : frameUsers) {
                        TreeData treeData = new TreeData();
                        treeData.setObjectGuid(user.getUserGuid());
                        treeData.setTitle(user.getDisplayName());
                        treeData.setLeaf(true);
                        list.add(treeData);
                    }
                }
                return list;
            }

            @Override
            public <T> List<T> search(String conndition) {
                {
                    List<FrameOu> list = new ArrayList();
                    List listuser = new ArrayList<>();
                    if (StringUtil.isNotBlank(conndition)) {
                        list = getOulist(tabKey, area, areacode1, jntask);
                        if (list != null && list.size() > 0) {
                            for (FrameOu frameou : list) {
                                for (String guid : transactors.getGuids()) {
                                    if (StringUtil.isNotBlank(guid)) {
                                        if (guid.contains("_gtigepoint_")) {
                                            guid = guid.split("_gtigepoint_")[0];
                                        }

                                        WorkflowUser user = organAPI.getUserByUserGuid(guid);
                                        if (user.getOuGuid().equals(frameou.getOuguid())
                                                && user.getDisplayName().contains(conndition)) {
                                            listuser.add(user);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return listuser;
                }
            }

            @Override
            public <T> List<T> fetchData(int i, TreeData obj) {
                {
                    List users = new ArrayList();
                    if (obj != null) {
                        if (StringUtils.isBlank(obj.getObjectGuid()) || (StringUtils.isNotBlank(obj.getObjectGuid())
                                && obj.getObjectGuid().equals(TreeFunction9.F9ROOT))) {
                            if (transactors != null) {
                                if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                                    List<FrameOu> list = new ArrayList();
                                    list = getOulist(tabKey, area, areacode1, jntask);
                                    if (list != null && list.size() > 0) {
                                        for (FrameOu frameou : list) {
                                            for (String guid : transactors.getGuids()) {
                                                if (StringUtil.isNotBlank(guid)) {
                                                    if (guid.contains("_gtigepoint_")) {
                                                        guid = guid.split("_gtigepoint_")[0];
                                                    }
                                                    WorkflowUser user = organAPI.getUserByUserGuid(guid);
                                                    if (user.getOuGuid().equals(frameou.getOuguid())) {
                                                        users.add(user);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            String objectGuid = obj.getObjectGuid();
                            objectGuid = objectGuid.split(EpointKeyNames9.SPECIAL_SPLITCHAR_1)[0];
                            WorkflowUser user = organAPI.getUserByUserGuid(objectGuid);
                            users.add(user);
                        }
                    }
                    return users;
                }
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                return 0;
            }
        };
        return fetchHandler;
    }

    public List<FrameOu> getOulist(String tabKey, String area, String areacode1, boolean jntask) {
        String areacode = "";
        List<FrameOu> list = new ArrayList<>();
        if (transactors != null) {
            if (!(transactors.getGuids() != null && transactors.getGuids().length > 0)) {
                return list;
            }
        }

        if ("xz".equals(tabKey)) {
            if (StringUtil.isNotBlank(tabKey) && !("undefined".equals(tabKey)) && area != null
                    && !"undefined".equals(area)) {
                areacode = area;
            } else {
                areacode = ZwfwUserSession.getInstance().getAreaCode().substring(0, 9);
            }

            list = frameOu.getOUListByAreacode(areacode).getResult();
            if (StringUtil.isNotBlank(areacode) && areacode.length() > 6) {
                String finalAreacode = areacode;
                list.removeIf(ou -> {
                    FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                    if (!selectOu.containsKey(ou.getOuguid())) {
                        return true;
                    } else {
                        if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                && finalAreacode.equals(ouExt.get("areacode").toString())) {
                            return false;
                        }
                        return true;
                    }
                });
            }
        } else if ("cun".equals(tabKey)) {
            if (StringUtil.isNotBlank(area) && !("undefined".equals(area)) && area != null
                    && !"undefined".equals(area)) {
                areacode = area;
            } else {
                areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            list = frameOu.getOUListByAreacode(areacode).getResult();
            list.removeIf(ou -> {
                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                if (!selectOu.containsKey(ou.getOuguid())) {
                    return true;
                }
                return false;
            });
        } else if ("ownou".equals(tabKey)) {
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(ouStr) && ouStr.contains(",")) {
                ouStr = ouStr.substring(0, ouStr.lastIndexOf("','"));
            }
            sql.in("ouguid", "'" + ouStr + "'");
            sql.eq("ouguid", userSession.getOuGuid());
            list = frameOu.getOuListByCondition(sql.getMap());
        } else if ("all".equals(tabKey)) {
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(ouStr) && ouStr.contains(",")) {
                ouStr = ouStr.substring(0, ouStr.lastIndexOf("','"));
            }
            sql.in("ouguid", "'" + ouStr + "'");
            list = frameOu.getOuListByCondition(sql.getMap());
        } else {
            // 如果是镇村
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                if ("370800".equals(areacode1)) {
                    areacode = areacode1;
                } else {
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                }
            } else {
                if (jntask) {
                    areacode = "370800";
                } else {
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
            }
            list = frameOu.getOUListByAreacode(areacode).getResult();
            String finalAreacode1 = areacode;
            list.removeIf(ou -> {
                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                if (!selectOu.containsKey(ou.getOuguid())) {
                    return true;
                } else {
                    if (StringUtil.isNotBlank(ouExt.get("areacode"))
                            && finalAreacode1.equals(ouExt.get("areacode").toString())) {
                        return false;
                    }
                    return true;
                }
            });
        }
        return list;
    }

    /**
     * 获取窗口人员树
     *
     * @param windowGuid 窗口guid
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    /**
     * 获取窗口人员树
     *
     * @param windowGuid 窗口guid
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public SimpleFetchHandler9 loadAllUser(String code, String area, String areacode1, Boolean jntask) {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public <T> List<T> search(String conndition) {
                List<FrameOu> list = new ArrayList();
                List listuser = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    String areacode;
                    if ("xz".equals(code)) {
                        if (StringUtil.isNotBlank(area) && !("undefined".equals(area))) {
                            areacode = area;
                        } else {
                            areacode = ZwfwUserSession.getInstance().getAreaCode().substring(0, 9);
                        }
                        list = frameOu.getOUListByAreacode(areacode).getResult();
                        if (areacode.length() > 6) {
                            list.removeIf(ou -> {
                                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                        && areacode.equals(ouExt.get("areacode").toString())) {
                                    return false;
                                } else {
                                    return true;
                                }
                            });
                        }
                    } else if ("cun".equals(code)) {
                        if (StringUtil.isNotBlank(area) && !("undefined".equals(area))) {
                            areacode = area;
                        } else {
                            areacode = ZwfwUserSession.getInstance().getAreaCode();
                        }
                        list = frameOu.getOUListByAreacode(areacode).getResult();
                    } else if ("all".equals(code)) {
                        if (StringUtil.isNotBlank(area) && !("undefined".equals(area))) {
                            areacode = area;
                        } else {
                            areacode = ZwfwUserSession.getInstance().getAreaCode();
                        }
                        SqlConditionUtil sql = new SqlConditionUtil();
                        list = frameOu.getOuListByCondition(sql.getMap());
                    } else {
                        // 如果是镇村
                        if (ZwfwUserSession.getInstance().getCitylevel() != null
                                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                            if ("370800".equals(areacode1)) {
                                areacode = areacode1;
                            } else {
                                areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                            }
                        } else {
                            if (jntask) {
                                areacode = "370800";
                            } else {
                                areacode = ZwfwUserSession.getInstance().getAreaCode();
                            }
                        }
                        list = frameOu.getOUListByAreacode(areacode).getResult();
                        list.removeIf(ou -> {
                            FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                            if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                    && areacode.equals(ouExt.get("areacode").toString())) {
                                return false;
                            } else {
                                return true;
                            }
                        });
                    }
                    if (list != null && list.size() > 0) {
                        for (FrameOu frameou : list) {
                            listuser.addAll(userService.paginatorUserViewByOuGuid(frameou.getOuguid(), conndition, "",
                                    "", "", "", 0, 100, "", "", true, true).getList());
                            List<ViewFrameUser> users = userService.paginatorUserViewByOuGuid(frameou.getOuguid(), "",
                                    "", "", "", "", 0, 100, "", "", true, true).getList();
                            for (ViewFrameUser user : users) {
                                // 拼音搜索
                                if (ChineseToPinyin.getPingYin(user.getDisplayName()).contains(conndition)) {
                                    if (!listuser.contains(user)) {
                                        listuser.add(user);
                                    }
                                }
                                // 拼音缩写搜索
                                if (ChineseToPinyin.getPinYinHeadChar(user.getDisplayName()).contains(conndition)) {
                                    if (!listuser.contains(user)) {
                                        listuser.add(user);
                                    }
                                }
                            }
                        }
                    }
                }
                return listuser;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        String areacode;
                        if (code.equals("xz")) {
                            if (StringUtil.isNotBlank(area) && !("undefined".equals(area))) {
                                areacode = area;
                            } else {
                                areacode = ZwfwUserSession.getInstance().getAreaCode().substring(0, 9);
                            }
                            // *list =
                            // frameOu.getOUListByAreacode(areacode).getResult();
                            AuditOrgaArea orgaarea = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                            String ouguid = "";
                            if (orgaarea != null) {
                                ouguid = orgaarea.getOuguid();
                            }
                            list = userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1);
                            List<FrameOu> ouList = ouService.listOUByGuid(ouguid, 5);
                            if (ouList != null && ouList.size() > 0) {
                                for (FrameOu ou : ouList) {
                                    list.add(ou);
                                }
                            }
                            if (areacode.length() > 6) {
                                list.removeIf(ou -> {
                                    if (ou instanceof FrameUser) {
                                        return false;
                                    }
                                    FrameOuExtendInfo ouExt = ouService
                                            .getFrameOuExtendInfo(((FrameOu) ou).getOuguid());
                                    if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                            && areacode.equals(ouExt.get("areacode").toString())) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                });
                            }
                        } else if (code.equals("cun")) {
                            if (StringUtil.isNotBlank(area) && !("undefined".equals(area))) {
                                areacode = area;
                            } else {
                                areacode = ZwfwUserSession.getInstance().getAreaCode();
                            }
                            // list =
                            // frameOu.getOUListByAreacode(areacode).getResult();
                            AuditOrgaArea orgaarea = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                            String ouguid = "";
                            if (orgaarea != null) {
                                ouguid = orgaarea.getOuguid();
                            }
                            list = userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1);
                            List<FrameOu> ouList = ouService.listOUByGuid(ouguid, 5);
                            if (ouList != null && ouList.size() > 0) {
                                for (FrameOu ou : ouList) {
                                    list.add(ou);
                                }
                            }
                        } else if ("all".equals(code)) {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            list = frameOu.getOuListByCondition(sql.getMap());
                        } else {
                            // 如果是镇村
                            if (ZwfwUserSession.getInstance().getCitylevel() != null
                                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                                if ("370800".equals(areacode1)) {
                                    areacode = areacode1;
                                } else {
                                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                                }
                            } else {
                                if (jntask) {
                                    areacode = "370800";
                                } else {
                                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                                }
                            }
                            AuditOrgaArea orgaarea = auditOrgaAreaService.getAreaByAreacode(areacode).getResult();
                            String ouguid = "";
                            if (orgaarea != null) {
                                ouguid = orgaarea.getOuguid();
                            }
                            list = userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1);
                            List<FrameOu> ouList = ouService.listOUByGuid(ouguid, 5);
                            if (ouList != null) {
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
                                    list.add(ou);
                                }
                            }
                        }
                        // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有的人员
                    } else {
                        list = userService.listUserByOuGuid(treeData.getObjectGuid(), "", "", "", false, true, true, 1);
                        FrameOuExtendInfo treeouExt = ouService.getFrameOuExtendInfo(treeData.getObjectGuid());
                        if (treeouExt != null) {
                            String treearea = treeouExt.get("areacode");
                            List<FrameOu> ouList = ouService.listOUByGuid(treeData.getObjectGuid(), 5);
                            if (ouList != null) {
                                ouList.removeIf(ou -> {
                                    FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                    if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                            && treearea.equals(ouExt.get("areacode").toString())) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                });
                            }
                            if (ouList != null && ouList.size() > 0) {
                                for (FrameOu ou : ouList) {
                                    list.add(ou);
                                }
                            }
                        }
                    }
                }
                // 点击checkbox的时候触发
                else {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的人员的list
                    if ("frameou".equals(treeData.getObjectcode())) {
                        list = userService.listUserByOuGuid(treeData.getObjectGuid(), "", "", "", false, true, true, 1);
                        FrameOuExtendInfo treeouExt = ouService.getFrameOuExtendInfo(treeData.getObjectGuid());
                        String treearea = treeouExt.get("areacode");
                        List<FrameOu> ouList = ouService.listOUByGuid(treeData.getObjectGuid(), 5);
                        if (ouList != null) {
                            ouList.removeIf(ou -> {
                                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                if (StringUtil.isNotBlank(ouExt.get("areacode"))
                                        && treearea.equals(ouExt.get("areacode").toString())) {
                                    return false;
                                } else {
                                    return true;
                                }
                            });
                        }
                        if (ouList != null && ouList.size() > 0) {
                            for (FrameOu ou : ouList) {
                                list.add(ou);
                            }
                        }
                    } else {
                        FrameUser user = frameUserService.getUserByUserField("userguid", treeData.getObjectGuid());
                        list.add(user);
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
                } else {
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
                            } else {
                                // objectcode的作用是来区分是点击了部门还是事项
                                treeData.setObjectcode("frameou");
                                treeData.setNoClick(true);
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
                            treeData.setLeaf(true);
                            treeList.add(treeData);
                        }
                    }
                }

                return treeList;
            }
        };
        return fetchHandler9;
    }

    public void setSelectedTreeNode() {

        List<SelectItem> list = new ArrayList<SelectItem>();

        ProcessVersionInstance pvinstance = oWAPI.getProcessVersionInstance(pviguid);
        WorkflowTransition transition = iWFDefineAPI9.getTransition(pvinstance, transitionGuid);
        AuditTaskRiskpoint riskpoint = iAuditTaskRiskpoint
                .getAuditTaskRiskpointByActivityguid(transition.getToActivityGuid(), true).getResult();
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        if (riskpoint != null) {
            String rpId = riskpoint.getRiskpointid();
            List<AuditProjectDefaultuser> defaultusers = iAuditProjectDefaultuser.getAuditProjectDefaultuserByrpid(rpId)
                    .getResult();
            if (defaultusers != null && !defaultusers.isEmpty()) {
                for (AuditProjectDefaultuser defaultuser : defaultusers) {
                    String userguid = defaultuser.getDefaultuserguid();
                    String username = defaultuser.getDefaultusername();
                    String type = defaultuser.getDefaultusertype();
                    if (ZwfwConstant.ZWFW_DEFAULTUSERTYPE_SJ.equals(type)) {
                        list.add(new SelectItem(userguid + "_SPLIT_cityou", username));
                        list.add(new SelectItem(userguid + "_SPLIT_ou", username));
                        list.add(new SelectItem(userguid + "_SPLIT_ownou", username));

                        flag1 = false;
                    } else if (ZwfwConstant.ZWFW_DEFAULTUSERTYPE_ZJ.equals(type)) {
                        if (userguid.contains("[")) {
                            continue;
                        } else {
                            list.add(new SelectItem(userguid + "_SPLIT_townou", username));
                            flag2 = false;
                        }

                    } else if (ZwfwConstant.ZWFW_DEFAULTUSERTYPE_CJ.equals(type)) {
                        list.add(new SelectItem(userguid + "_SPLIT_cunou", username));
                        flag2 = false;
                    }
                }

            }
            // 乡镇部门办理
            AuditTaskDelegateAuth auth = iAuditTaskDelegateAuth.getAuthByRpidAndOuguid(riskpoint.getRiskpointid(),
                    ouguid);
            if (isWindowFalg && auth != null && StringUtil.isNotBlank(auth.getAcceptguid())) {
                // 获取预设处理人
                String[] acceptguids = auth.getAcceptguid().split(";");
                String[] acceptnames = auth.getAcceptname().split(";");
                for (int i = 0; i < acceptguids.length; i++) {
                    list.add(new SelectItem(acceptguids[i] + "_SPLIT_townou", acceptnames[i]));
                }
            } else {
                if (flag1 || flag2 || flag3) {
                    if (transactors != null) {
                        if (transactors.getGuids() != null && transactors.getGuids().length > 0) {
                            for (String guid : transactors.getGuids()) {
                                // 根据用户guid获取用户实体
                                if (guid.contains("_gtigepoint_")) {
                                    guid = guid.split("_gtigepoint_")[0];
                                }
                                WorkflowUser user = organAPI.getUserByUserGuid(guid);
                                // 获取用户所在部门信息
                                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(user.getOuGuid());
                                if (ouExt != null) {
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("xiaqucode", ouExt.get("areacode"));
                                    AuditOrgaArea area = areaImpl.getAuditArea(sql.getMap()).getResult();
                                    if (area != null) {
                                        if (flag1 && Integer.parseInt(area.getCitylevel()) < Integer
                                                .parseInt(ZwfwConstant.AREA_TYPE_XZJ)) {
                                            list.add(new SelectItem(user.getUserGuid() + "_SPLIT_ou",
                                                    user.getDisplayName()));

                                            list.add(new SelectItem(user.getUserGuid() + "_SPLIT_ownou",
                                                    user.getDisplayName()));
                                            list.add(new SelectItem(user.getUserGuid() + "_SPLIT_cityou",
                                                    user.getDisplayName()));
                                            // flag2 = false;
                                        }
                                        if (flag2 && Integer.parseInt(area.getCitylevel()) == Integer
                                                .parseInt(ZwfwConstant.AREA_TYPE_XZJ)) {
                                            list.add(new SelectItem(user.getUserGuid() + "_SPLIT_townou",
                                                    user.getDisplayName()));

                                        }
                                        if (flag3 && Integer.parseInt(area.getCitylevel()) > Integer
                                                .parseInt(ZwfwConstant.AREA_TYPE_XZJ)) {
                                            list.add(new SelectItem(user.getUserGuid() + "_SPLIT_cunou",
                                                    user.getDisplayName()));

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        treeOuModel.setSelectNode(list);
    }

    public String getTreeValue() {
        return treeValue;
    }

    public void setTreeValue(String treeValue) {
        this.treeValue = treeValue;
    }

    public String getTreeText() {
        return treeText;
    }

    public void setTreeText(String treeText) {
        this.treeText = treeText;
    }
}

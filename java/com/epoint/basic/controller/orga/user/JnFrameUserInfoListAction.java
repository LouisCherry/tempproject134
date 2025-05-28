package com.epoint.basic.controller.orga.user;

import com.aliyun.oss.common.utils.IOUtils;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.bizlogic.orga.role.service.FrameRoleService9;
import com.epoint.basic.controller.orga.FrameOrganBaseController;
import com.epoint.basic.controller.orga.user.api.IJnFrameLogSerivce;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.soa.SOAService;
import com.epoint.common.util.AttachUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.ExtValue;
import com.epoint.core.dto.base.Field;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.saas.tenant.api.ITenantService;
import com.epoint.frame.service.metadata.saas.tenant.entity.FrameTenant;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigServiceInternal;
import com.epoint.frame.service.organ.job.api.IJobServiceInternal;
import com.epoint.frame.service.organ.job.entity.FrameJob;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleServiceInternal;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.*;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@RestController("jnframeuserinfolistaction")
@Scope("request")
public class JnFrameUserInfoListAction extends FrameOrganBaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IUserServiceInternal userservice;

    @Autowired
    private IOuServiceInternal ouservice;

    @Autowired
    private IRoleServiceInternal roleservice;

    @Autowired
    private IUserRoleRelationService userrolerelationservice;

    @Autowired
    private IConfigService configservice;

    @Autowired
    protected IJobServiceInternal jobservice;

    @Autowired
    private IConfigServiceInternal configService;

    @Autowired
    private IJnFrameLogSerivce frameLogSerivce;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 一页modal
     */
    private DataGridModel<ViewFrameUser> model;

    private LazyTreeModal9 treeModel;

    /**
     * 该list页面操作的数据对象
     */
    private FrameUser frameUser;

    /**
     * 页面查询条件:分别是用户名;登录名;部门名
     */
    private String userName;
    private String loginId;
    private String ouName;

    /**
     * 用户所在部门
     */
    private FrameOu selectedFrameOu;
    /**
     * 要转移到的部门guid
     */
    private String transferOuguid;

    /**
     * 左边选中树的Guid
     */
    private String leftTreeNodeGuid = "";

    private String secondOu = null;

    private Boolean isDirect;

    private String canOperateChildBaseOu;
    /**
     * 是否独立部门
     */
    private String isSub = null;

    private Boolean enableSOA = false;

    private DataImportModel9 dataImport;

    private List<FrameOu> allOuList = null;

    private List<FrameUserSecondOU> allSecOuList = null;

    private List<FrameRole> allRoleList = null;

    private LinkedHashMap<Integer, List<FrameOu>> needAddOuMap = null;

    private List<FrameUser> needAddUserList = null;

    private HashMap<String, FrameUserExtendInfo> needAddUserExtendMap = null;

    private List<FrameUserSecondOU> needAddSecondOUList = null;

    private List<FrameRole> needAddRoleList = null;

    private List<FrameUserRoleRelation> needAddRoleRelationList = null;

    private List<FrameJob> needAddJobList = null;

    transient private static Logger log = LogUtil.getLog(FrameUserListAction.class);

    private Boolean isThreeManageMode = false;

    private String systemAdminRoleGuid;

    private String securityAdminRoleGuid;

    private Boolean isSoaSyncRole = false;


    @Override
    public void pageLoad() {
        isSub = getRequestParameter("isSub");
        isDirect = false;
        leftTreeNodeGuid = getViewData("leftTreeNodeGuid").toString();
        if (!isPostback()) {
            isThreeManageMode = isThreeManageMode();
            addViewData("isThreeManageMode", isThreeManageMode.toString());
            systemAdminRoleGuid = roleservice.getRoleGuidByRoleName(IRoleServiceInternal.ROLENAME_SYSTEMADMIN, 3);
            addViewData("systemAdminRoleGuid", systemAdminRoleGuid);
            securityAdminRoleGuid = roleservice.getRoleGuidByRoleName(IRoleServiceInternal.ROLENAME_SECURITYADMIN, 3);
            addViewData("securityAdminRoleGuid", securityAdminRoleGuid);
        } else {
            isThreeManageMode = new Boolean(getViewData("isThreeManageMode"));
            systemAdminRoleGuid = getViewData("systemAdminRoleGuid");
            securityAdminRoleGuid = getViewData("securityAdminRoleGuid");
        }
        canOperateChildBaseOu = configService.getFrameConfigValue("canOperateChildBaseOu");
        addCallbackParam("isadmin", UserSession.getInstance().getLoginID());

        if (isThreeManageMode) {
            addCallbackParam("enableThreeManage", true);
            if (userSession.getUserRoleList().contains(systemAdminRoleGuid)) {
                addCallbackParam("role", "systemadmin");
            } else if (userSession.getUserRoleList().contains(securityAdminRoleGuid)) {
                addCallbackParam("role", "securityadmin");
            }
        } else {
            addCallbackParam("enableThreeManage", false);
        }
    }

    @ExtValue
    public Boolean showDelBtn() {
        return isEnableAdminDelRight(isSub);
    }

    public DataImportModel9 getDataImport() {
        if (dataImport == null) {
            dataImport = new DataImportModel9(new ImportExcelHandler() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                                            Object[] data) {
                    String message = null;
                    if (curRow == 0) {// 做一些初始化操作
                        allOuList = ouservice.listAllOu();
                        if (allOuList == null) {
                            allOuList = new ArrayList<FrameOu>();
                        }
                        if (allSecOuList == null) {
                            allSecOuList = userservice.getAllSecondOu();
                        }
                        if (allRoleList == null) {
                            allRoleList = roleservice.listRole(null);
                        }

                        if (needAddOuMap == null) {
                            needAddOuMap = new LinkedHashMap<Integer, List<FrameOu>>();
                        }
                        if (needAddUserList == null) {
                            needAddUserList = new ArrayList<FrameUser>();
                        }
                        if (needAddUserExtendMap == null) {
                            needAddUserExtendMap = new HashMap<String, FrameUserExtendInfo>();
                        }
                        if (needAddRoleList == null) {
                            needAddRoleList = new ArrayList<FrameRole>();
                        }
                        if (needAddSecondOUList == null) {
                            needAddSecondOUList = new ArrayList<FrameUserSecondOU>();
                        }
                        if (needAddRoleRelationList == null) {
                            needAddRoleRelationList = new ArrayList<FrameUserRoleRelation>();
                        }
                        if (needAddJobList == null) {
                            needAddJobList = new ArrayList<FrameJob>();
                        }
                        // 判断需要add的部门也先存到这个List里，方便接下来的判断，ouextendInfo在最后存储时进行操作
                    } else {// 第0行是标题不做考虑
                        if (StringUtil.isNotBlank(data[0]) && StringUtil.isNotBlank(data[1])
                                && StringUtil.isNotBlank(data[2]) && StringUtil.isNotBlank(data[3])) {
                            String userName = data[0].toString();// 用户姓名
                            String loginId = data[1].toString();// 登录名称
                            String sex = data[2].toString();// 性别
                            String ouPath = data[3].toString();// 部门全路径
                            String secondOuPath = StringUtil.isBlank(data[4]) ? "" : data[4].toString();// 兼职
                            String roles = StringUtil.isBlank(data[5]) ? "" : data[5].toString();// 角色
                            String title = StringUtil.isBlank(data[6]) ? "" : data[6].toString();// 职位
                            String telephoneOffice = StringUtil.isBlank(data[7]) ? "" : data[7].toString();// 办公电话
                            String telephoneHome = StringUtil.isBlank(data[8]) ? "" : data[8].toString();// 家庭电话
                            String mobile = StringUtil.isBlank(data[9]) ? "" : data[9].toString();// 手机
                            String usbkey = StringUtil.isBlank(data[10]) ? "" : data[10].toString();// CA
                            String qqnumber = StringUtil.isBlank(data[11]) ? "" : data[11].toString();// qq
                            String email = StringUtil.isBlank(data[12]) ? "" : data[12].toString();// email
                            String postalAddress = StringUtil.isBlank(data[13]) ? "" : data[13].toString();// address
                            String postalCode = StringUtil.isBlank(data[14]) ? "" : data[14].toString();// 邮编
                            String fax = StringUtil.isBlank(data[15]) ? "" : data[15].toString();// 传真
                            int ordernumber = StringUtil.isBlank(data[16]) ? 0 : Integer.parseInt(data[16].toString());// 排序号

                            FrameUser user = new FrameUser();
                            FrameUserExtendInfo extendInfo = new FrameUserExtendInfo();
                            String userGuid = UUID.randomUUID().toString();
                            user.setUserGuid(userGuid);
                            user.setDisplayName(userName);
                            user.setFirstname(userName);
                            user.setLoginId(loginId);
                            user.setOrderNumber(ordernumber);

                            // 先处理 部门
                            String ouGuid = dealOuPath(ouPath);
                            user.setOuGuid(ouGuid);
                            user.setSex(sex);

                            // 处理兼职
                            if (StringUtil.isNotBlank(secondOuPath)) {
                                String[] jzs = secondOuPath.split(";");
                                for (String jz : jzs) {
                                    String jzOuguid = dealOuPath(jz);
                                    if (StringUtil.isNotBlank(jzOuguid)
                                            && !checkSecOuExist(allSecOuList, userGuid, jzOuguid)
                                            && !checkSecOuExist(needAddSecondOUList, userGuid, jzOuguid)) {// 不存在
                                        FrameUserSecondOU secOu = new FrameUserSecondOU();
                                        secOu.setOuGuid(jzOuguid);
                                        secOu.setUserGuid(userGuid);
                                        secOu.setOrderNumber(0);
                                        secOu.setUserOrderNumber(0);
                                        needAddSecondOUList.add(secOu);// 存入兼职List
                                    }
                                }
                            }
                            // 处理角色
                            if (StringUtil.isNotBlank(roles)) {
                                String[] roleArray = roles.split(";");
                                for (String roleName : roleArray) {
                                    String roleGuid = null;
                                    FrameRole role1 = getRoleByName(allRoleList, roleName);
                                    FrameRole role2 = getRoleByName(needAddRoleList, roleName);
                                    if (role1 == null && role2 != null) {
                                        roleGuid = role2.getRoleGuid();
                                    } else if (role1 != null && role2 == null) {
                                        roleGuid = role1.getRoleGuid();
                                    } else if (role1 == null && role2 == null) {// 是否在已入库集合、或者待插入集合里
                                        FrameRole role = new FrameRole();
                                        role.setRoleGuid(UUID.randomUUID().toString());
                                        role.setRoleName(roleName);
                                        role.setIsReserved(2);
                                        role.setOrderNumber(0);
                                        needAddRoleList.add(role);
                                        roleGuid = role.getRoleGuid();
                                    }
                                    // 插入关联关系
                                    FrameUserRoleRelation relation = new FrameUserRoleRelation();
                                    relation.setRoleGuid(roleGuid);
                                    relation.setUserGuid(userGuid);
                                    needAddRoleRelationList.add(relation);
                                }
                            }

                            // 处理其他字段
                            // 处理职位
                            if (StringUtil.isNotBlank(title) && !jobservice.checkIsExistInOu(title, ouGuid, null)) {
                                FrameJob job = new FrameJob();
                                job.setBelongDisplayName(userName);
                                job.setBelongUserGuid(userGuid);
                                job.setOuGuid(ouGuid);
                                job.setJobName(title);
                                job.setJobGuid(UUID.randomUUID().toString());
                                needAddJobList.add(job);
                            }
                            user.setTitle(title);
                            user.setTelephoneOffice(telephoneOffice);
                            user.setTelephoneHome(telephoneHome);
                            user.setMobile(mobile);
                            user.setEmail(email);
                            user.setFax(fax);
                            user.setIsEnabled(1);

                            // extendInfo
                            extendInfo.setUserGuid(userGuid);
                            extendInfo.setUsbkey(usbkey);
                            extendInfo.setQqnumber(qqnumber);
                            extendInfo.setPostalAddress(postalAddress);
                            extendInfo.setPostalCode(postalCode);
                            needAddUserExtendMap.put(userGuid, extendInfo);
                            needAddUserList.add(user);

                        } else {
                            message = sheetName + l(":第" + curRow + "行必填项未填写");
                        }
                    }
                    if (StringUtil.isNotBlank(message)) {
                        dataImport.setMessage(message);
                    }
                    return message;
                }

                @Override
                public void refreshTable() {
                    // 新建部门，以及extendInfo
                    if (needAddOuMap != null) {
                        // 先转成ArrayList集合
                        ArrayList<Entry<Integer, List<FrameOu>>> list = new ArrayList<Entry<Integer, List<FrameOu>>>(
                                needAddOuMap.entrySet());

                        Collections.sort(list, new Comparator<Entry<Integer, List<FrameOu>>>() {
                            @Override
                            public int compare(Entry<Integer, List<FrameOu>> o1, Entry<Integer, List<FrameOu>> o2) {
                                return ((o1.getKey() - o2.getKey() == 0) ? 0
                                        : (o1.getKey() - o2.getKey() > 0) ? 1 : -1);
                            }

                        });

                        for (Entry<Integer, List<FrameOu>> entry : list) {
                            List<FrameOu> ouList = entry.getValue();
                            if (ouList != null) {
                                for (FrameOu ou : ouList) {
                                    FrameOuExtendInfo ouExtendInfo = new FrameOuExtendInfo();
                                    ouExtendInfo.setOuGuid(ou.getOuguid());
                                    ouservice.addFrameOu(ou, ouExtendInfo);
                                }
                            }
                        }
                    }
                    // 新建角色
                    if (needAddRoleList != null) {
                        for (FrameRole role : needAddRoleList) {
                            roleservice.addFrameRole(role);
                        }
                    }
                    // 新建用户,以及extendinfo
                    if (needAddUserList != null) {
                        for (FrameUser user : needAddUserList) {
                            FrameUserExtendInfo frameUserExtendInfo = needAddUserExtendMap.get(user.getUserGuid());
                            userservice.addFrameUser(user, frameUserExtendInfo);
                        }
                    }
                    // 新建兼职
                    if (needAddSecondOUList != null) {
                        for (FrameUserSecondOU secOu : needAddSecondOUList) {
                            userservice.addUserRelation(secOu);
                        }
                    }
                    // 新建用户角色关系
                    if (needAddRoleRelationList != null) {
                        for (FrameUserRoleRelation relation : needAddRoleRelationList) {
                            userrolerelationservice.addRelation(relation.getRoleGuid(), relation.getUserGuid());
                        }
                    }
                    // 新建角色
                    if (needAddJobList != null) {
                        for (FrameJob job : needAddJobList) {
                            jobservice.addframeJob(job);
                        }
                    }
                }
            });
        }
        return dataImport;
    }

    /**
     * 获取用户表格数据
     *
     * @return DataGridModel
     */
    public DataGridModel<ViewFrameUser> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ViewFrameUser>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 3821079657818115023L;

                @Override
                public List<ViewFrameUser> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String baseOuGuid = null;
                    if ("1".equalsIgnoreCase(isSub)) {
                        baseOuGuid = userSession.getBaseOUGuid();
                    }
                    boolean onlyCurrentBaseOu = false;
                    if (StringUtil.isNotBlank(canOperateChildBaseOu) && "0".equals(canOperateChildBaseOu)) {
                        onlyCurrentBaseOu = true;
                    }
                    PageData<ViewFrameUser> pageData = userservice.paginatorUserViewByOuGuid(leftTreeNodeGuid, userName,
                            loginId, ouName, null, baseOuGuid, first, pageSize, sortField, sortOrder, false, isDirect,
                            onlyCurrentBaseOu);
                    this.setRowCount(pageData.getRowCount());
                    String thridPartyName = "是但未开启同步";
                    String weixin = configservice.getFrameConfigValue(EpointKeyNames9.WEIXIN_ENABLE_SYNC);
                    if ("1".equals(weixin)) {
                        thridPartyName = "同步企业微信";
                    }
                    // 对特殊列进行处理
                    if (pageData.getList() != null) {
                        for (ViewFrameUser user : pageData.getList()) {
                            user.put("enableSOA", enableSOA);
                            user.put("showLoginId", showLoginId(user.getLoginId(), user.getOuGuid()));
                            user.put("jzqk", getSecondOuWork(user.getUserGuid()) ? l("有兼职") : l("无兼职"));
                            user.put("zt", user.getIsEnabled() == 1 ? l("启用") : l("禁用"));
                            user.put("usbsetstr", getUsbSetStr(user.getUsbkey()));
                            user.put("syncthirdparty",
                                    (user.getIsSyncThirdParty() != null && user.getIsSyncThirdParty() == 1)
                                            ? l(thridPartyName)
                                            : l("否"));
                            FrameAccountRelation relation = userservice
                                    .getAccountRelationByRelativeUserGuid(user.getUserGuid());
                            user.put("isCopy", relation == null ? 0 : 1);
                            List<Object[]> frn = new FrameRoleService9()
                                    .getRoleGuidAndRoleNameByUserGuid(user.getUserGuid());
                            StringBuilder roleName = new StringBuilder();
                            for (Object[] frno : frn) {
                                roleName.append(frno[1]);
                                roleName.append(";");
                            }
                            if (roleName.lastIndexOf(";") > 0) {
                                user.put("rolename", roleName.toString().substring(0, roleName.lastIndexOf(";")));
                            }
                            user.remove("password");
                            // 独立单位下如果设置了不能操作下级独立单位
                            if ("0".equals(canOperateChildBaseOu) && "1".equals(isSub)) {
                                FrameOu ou = ouservice.getOuByOuGuid(user.getOuGuid());
                                if (StringUtil.isNotBlank(ou.getBaseOuguid())
                                        && !ou.getBaseOuguid().equals(baseOuGuid)) {
                                    user.put("canOperateBaseOu", canOperateChildBaseOu);
                                }
                            }
                        }
                    }
                    // 由于这里是直接用的ViewFrameUser对象，不能关闭清洗功能，需要指定哪些字段不能被清掉
                    Set<Field> fieldSet = new HashSet<Field>();
                    fieldSet.add(new Field("enableSOA"));
                    fieldSet.add(new Field("framemj"));
                    fieldSet.add(new Field("isCopy"));
                    fieldSet.add(new Field("rolename"));
                    this.setAllowFieldSyncSet(fieldSet);
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    /**
     * 获取兼职情况
     *
     * @param userGuid 用户guid
     * @return
     */
    public boolean getSecondOuWork(String userGuid) {
        String[] ouGuids = userservice.getRelationOuNameAndGuid(userGuid, true);
        if (ouGuids.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getUsbSetStr(String usbkey) {
        if (usbkey != null && !usbkey.equals("")) {
            return l("重置KEY");
        }
        return l("设置KEY");
    }

    /**
     * 对于租户类型部门下面用户的loginId不展现其前缀
     *
     * @param loginId
     * @param ouGuid
     * @return
     */
    public String showLoginId(String loginId, String ouGuid) {
        String showId = loginId;
        if (StringUtil.isNotBlank(ouGuid) && ConfigUtil.isOpenTenantMode()) {
            IOuServiceInternal ouservice = ContainerFactory.getContainInfo().getComponent(IOuServiceInternal.class);
            FrameOu topOu = ouservice.getTopParentOu(ouGuid);
            if (topOu != null && StringUtil.isNotBlank(topOu.getBaseOuguid())) {
                ITenantService tenantservice = ContainerFactory.getContainInfo().getComponent(ITenantService.class);
                FrameTenant tenant = tenantservice.getFrameTenantByGuid(topOu.getBaseOuguid());
                if (tenant != null && loginId.startsWith(tenant.getTenantsysname() + ".")) {// 存在租户
                    showId = loginId.substring((tenant.getTenantsysname() + ".").length(), loginId.length());
                }
            }
        }
        return showId;
    }

    @SuppressWarnings("serial")
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            int treeId = ConstValue9.FRAMEOU;
            if ("1".equalsIgnoreCase(isSub)) {
                treeId = ConstValue9.SUB_DEPARTMENT;
            }
            treeModel = new LazyTreeModal9(new EpointTreeHandler9(treeId)) {
                @Override
                public void onLazyNodeSelect(TreeData data) {
                    setLeftTreeNodeGuid(data.getObjectGuid());
                    addViewData("leftTreeNodeGuid", leftTreeNodeGuid);
                }
            };
            treeModel.setRootName(configService
                    .getFrameConfigValueByNameWithDefault(EpointKeyNames9.FRAME_CONFIG_EpointTreeRootName, l("所有部门")));
        }
        return treeModel;
    }

    /**
     * 增加用户列表的步长
     */
    public void addUserStepSize() {
        // 获取步长值
        String stepSizeStr = getRequestParameter("stepsize");

        int stepSize = Integer.parseInt(stepSizeStr);
        // 获取需要被更新的用户
        String baseOuGuid = null;
        if ("1".equalsIgnoreCase(isSub)) {
            baseOuGuid = userSession.getBaseOUGuid();
        }

        List<FrameUser> frameUserList = userservice.listUserByOuGuid(leftTreeNodeGuid, null, baseOuGuid, null, false,
                false, isDirect, 3);
        // 校验用户列表的序号是否超出所给范围，超出则停止并给出提示
        if (frameUserList != null && !frameUserList.isEmpty()) {
            for (FrameUser list : frameUserList) {
                String s = "";
                int newOrder = list.getOrderNumber() * stepSize;
                s += newOrder;
                int alength = s.length();
                if (alength > 8) {
                    addCallbackParam("msg", l("已超过排序号范围"));
                    return;
                }
            }
        }
        userservice.updateFrameUserStepSize(frameUserList, stepSize);
        addCallbackParam("msg", l("更新步长成功"));
    }

    public Boolean getIsDirect() {
        return isDirect;
    }

    public void setIsDirect(Boolean isDirect) {
        this.isDirect = isDirect;
    }

    public String getLeftTreeNodeGuid() {
        addCallbackParam("leftTreeNodeGuid", leftTreeNodeGuid);
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public void export() throws IOException {
        List<ViewFrameUser> list = frameLogSerivce.getFrameUserList(leftTreeNodeGuid);

        // HSSFWorkbook对象
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 标题样式
        CellStyle titleCellStyle = workbook.createCellStyle();
        // 设置垂直方法的对齐方式
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置水平居中
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 下边框
        titleCellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        titleCellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        titleCellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        titleCellStyle.setBorderRight(BorderStyle.THIN);
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.cloneStyleFrom(titleCellStyle);
        HSSFFont font = workbook.createFont();
        // 设置excel数据字体大小
        font.setFontHeightInPoints((short) 10);
        font.setFontName("思源宋体");
        font.setBold(true);
        titleCellStyle.setFont(font);
        titleCellStyle.setWrapText(true);

        // 标题样式
        CellStyle ThemeCellStyle = workbook.createCellStyle();
        // 设置垂直方法的对齐方式
        ThemeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置水平居中
        ThemeCellStyle.setAlignment(HorizontalAlignment.CENTER);
        ThemeCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 下边框
        ThemeCellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        ThemeCellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        ThemeCellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        ThemeCellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.cloneStyleFrom(ThemeCellStyle);
        HSSFFont Tfont = workbook.createFont();
        // 设置excel数据字体大小
        Tfont.setFontHeightInPoints((short) 20);
        Tfont.setFontName("思源宋体");
        Tfont.setBold(true);
        ThemeCellStyle.setFont(Tfont);

        // 内容样式
        CellStyle contentCellStyle = workbook.createCellStyle();
        contentCellStyle.cloneStyleFrom(titleCellStyle);
        font = workbook.createFont();
        // 设置excel数据字体大小
        font.setFontHeightInPoints((short) 10);
        font.setFontName("思源宋体");
        contentCellStyle.setFont(font);
        // 开启自动换行
        contentCellStyle.setWrapText(true);

        HSSFSheet sheet = workbook.createSheet();

        for (int i = 1; i < 15; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }
        sheet.setColumnWidth(15, 30 * 256);
        sheet.setColumnWidth(16, 30 * 256);

        HSSFRow row = sheet.createRow(0);
        HSSFRow row2 = sheet.createRow(1);

        HSSFCell cell;

        // 第一行
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("用户列表信息");
        cell.setCellStyle(ThemeCellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, // firstrow
                // (0-based)
                0, // last row3 (0-based)
                0, // first column (0-based)
                16// last column (0-based)
        ));

        // 第二行
        cell = row2.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(1);
        cell.setCellValue("用户登录名");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(2);
        cell.setCellValue("用户姓名");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(3);
        cell.setCellValue("身份证号");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(4);
        cell.setCellValue("出生日期");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(5);
        cell.setCellValue("性别");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(6);
        cell.setCellValue("学历");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(7);
        cell.setCellValue("政治面貌");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(8);
        cell.setCellValue("学位");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(9);
        cell.setCellValue("办公电话");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(10);
        cell.setCellValue("移动电话");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(11);
        cell.setCellValue("办公地点");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(12);
        cell.setCellValue("工作单位");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(13);
        cell.setCellValue("职务");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(14);
        cell.setCellValue("人员类别");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(15);
        cell.setCellValue("本人照片");
        cell.setCellStyle(titleCellStyle);

        cell = row2.createCell(16);
        cell.setCellValue("执法证照片");
        cell.setCellStyle(titleCellStyle);

        HSSFCell fileCell;
        // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        for (int i = 0; i < list.size(); i++) {
            //用户拓展信息
            FrameUserExtendInfo extendInfo = userservice.getUserExtendInfoByUserGuid(list.get(i).getUserGuid());
            row = sheet.createRow(i + 2);
            row.setHeight((short) 1600);
            fileCell = row.createCell(0);
            fileCell.setCellValue(i + 1);
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(1);
            fileCell.setCellValue(list.get(i).getLoginId());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(2);
            fileCell.setCellValue(list.get(i).getDisplayName());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(3);
            fileCell.setCellValue(extendInfo.getIdentityCardNum());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(4);
            if (StringUtil.isNotBlank(extendInfo.getBirthday())) {
                fileCell.setCellValue(EpointDateUtil.convertDate2String(extendInfo.getBirthday()));
            }
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(5);
            fileCell.setCellValue(list.get(i).getSex());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(6);
            if (StringUtil.isNotBlank(extendInfo.getStr("education"))) {
                fileCell.setCellValue(iCodeItemsService.getItemTextByCodeName("个人信息学历", extendInfo.getStr("education")));
            } else {
                fileCell.setCellValue("");
            }
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(7);
            if (StringUtil.isNotBlank(extendInfo.getStr("politicaloutlook"))) {
                fileCell.setCellValue(iCodeItemsService.getItemTextByCodeName("个人信息政治面貌", extendInfo.getStr("politicaloutlook")));
            } else {
                fileCell.setCellValue("");
            }
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(8);
            if (StringUtil.isNotBlank(extendInfo.getStr("academicdegree"))) {
                fileCell.setCellValue(iCodeItemsService.getItemTextByCodeName("个人信息学位", extendInfo.getStr("academicdegree")));
            } else {
                fileCell.setCellValue("");
            }
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(9);
            fileCell.setCellValue(list.get(i).getTelephoneOffice());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(10);
            fileCell.setCellValue(list.get(i).getMobile());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(11);
            fileCell.setCellValue(extendInfo.getStr("officelocation"));
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(12);
            fileCell.setCellValue(list.get(i).getOuname());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(13);
            fileCell.setCellValue(list.get(i).getTitle());
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(14);
            fileCell.setCellValue(extendInfo.getStr("type"));
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(15);
            fileCell.setCellStyle(contentCellStyle);
            fileCell = row.createCell(16);
            fileCell.setCellStyle(contentCellStyle);
            // anchor主要用于设置图片的属性
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 15, i + 2, (short) 15,
                    i + 2);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            HSSFClientAnchor anchor2 = new HSSFClientAnchor(0, 0, 1023, 255, (short) 16, i + 2, (short) 16,
                    i + 2);
            anchor2.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            // 插入图片
            String fontcliengtag = "personalPhoto";
            String sql = "select attachguid from frame_attachinfo where cliengguid = '"
                    + list.get(i).getUserGuid() + "' and cliengtag='" + fontcliengtag + "'";
            String frontattachguid = CommonDao.getInstance().find(sql, String.class);
            FrameAttachStorage front = iAttachService.getAttach(frontattachguid);
            FrameAttachStorage signImage = userservice.getUserSignImage(list.get(i).getUserGuid());

            if (front != null && front.getContent() != null) {
                patriarch.createPicture(anchor, workbook.addPicture(IOUtils.readStreamAsByteArray(front.getContent()), HSSFWorkbook.PICTURE_TYPE_JPEG));
            }
            if (signImage != null && signImage.getContent() != null) {
                patriarch.createPicture(anchor2, workbook.addPicture(IOUtils.readStreamAsByteArray(signImage.getContent()), HSSFWorkbook.PICTURE_TYPE_JPEG));
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        workbook.write(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        long size = inputStream.available();
        String attachguid = UUID.randomUUID().toString();
        String cliengguid = UUID.randomUUID().toString();
        FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(attachguid, cliengguid,
                "用户信息列表.xlsx", ".xlsx", "用户信息导出", size, inputStream, "", "");
        if (outputStream != null) {
            outputStream.close();
        }

        if (workbook != null) {
            workbook.close();
        }

        addCallbackParam("attachguid", frameAttachInfo.getAttachGuid());
    }

    public FrameUser getFrameUser() {
        return frameUser;
    }

    public void setFrameUser(FrameUser frameUser) {
        this.frameUser = frameUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getOuName() {
        return ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public FrameOu getSelectedFrameOu() {
        return selectedFrameOu;
    }

    public void setSelectedFrameOu(FrameOu selectedFrameOu) {
        this.selectedFrameOu = selectedFrameOu;
    }

    public String getTransferOuguid() {
        return transferOuguid;
    }

    public void setTransferOuguid(String transferOuguid) {
        this.transferOuguid = transferOuguid;
    }

    public String getSecondOu() {
        return secondOu;
    }

    public void setSecondOu(String secondOu) {
        this.secondOu = secondOu;
    }

    public String getIsSub() {
        return isSub;
    }

    public void setIsSub(String isSub) {
        this.isSub = isSub;
    }

    @ExtValue
    public Boolean getEnableSOA() {
        return enableSOA;
    }

    public void setEnableSOA(Boolean enableSOA) {
        this.enableSOA = enableSOA;
    }

    @ExtValue
    public Boolean getIsSoaSyncRole() {
        return isSoaSyncRole;
    }

    public void setIsSoaSyncRole(Boolean isSoaSyncRole) {
        this.isSoaSyncRole = isSoaSyncRole;
    }

    /**
     * 处理部门路径，返回最后一个ouguid
     *
     * @param ouPath
     * @return
     */
    private String dealOuPath(String ouPath) {
        String rtnValue = "";
        // 先处理 部门
        String[] ouNames = ouPath.split("-");
        int size = ouNames.length;
        String parentOuGuid = "";// 供下一层使用
        boolean flag = false;
        for (int i = 0; i < size; i++) {
            String ouName = ouNames[i];
            FrameOu ou = null;
            if (flag == false) {
                ou = getOuByOuName(ouName, parentOuGuid);
            }
            if (ou == null) {// 需要新增,出现新增情况后，此链后面应该都是新增，不需要再重复查询部门，耗时间
                ou = new FrameOu();
                String ouguid = UUID.randomUUID().toString();
                ou.setOuguid(ouguid);
                ou.setOuname(ouName);
                ou.setOushortName(ouName);
                ou.setOrderNumber(0);
                ou.setParentOuguid(parentOuGuid);
                // 处理baseouguid
                ou.setBaseOuguid(getBaseOuguid(parentOuGuid));
                // 存入临时List
                addOuToMap(i, ou);
                // 存入总的List
                allOuList.add(ou);
                flag = true;
            }
            parentOuGuid = ou.getOuguid();// 供for循环下一个使用
            if ((i + 1) == size) {// 最后一个即为此用户的部门guid,或者兼职guid
                rtnValue = parentOuGuid;
            }
        }
        return rtnValue;
    }

    private String getBaseOuguid(String ouguid) {
        FrameOu frameOu = ouservice.getOuByOuGuid(ouguid);
        if (frameOu == null) {
            return null;
        } else if (StringUtil.isNotBlank(frameOu.getBaseOuguid())
                && frameOu.getBaseOuguid().equals(frameOu.getOuguid())) {
            return frameOu.getOuguid();
        } else if (StringUtil.isNotBlank(frameOu.getBaseOuguid())
                && !frameOu.getBaseOuguid().equals(frameOu.getOuguid())) {
            return frameOu.getBaseOuguid();
        }

        return null;
    }

    private void addOuToMap(Integer index, FrameOu ou) {
        if (needAddOuMap.containsKey(index)) {
            List<FrameOu> list = needAddOuMap.get(index);
            if (list == null) {
                list = new ArrayList<FrameOu>();
            }
            list.add(ou);
        } else {
            List<FrameOu> list = new ArrayList<FrameOu>();
            list.add(ou);
            needAddOuMap.put(index, list);
        }

    }

    private FrameOu getOuByOuName(String ouName, String parentOuGuid) {
        FrameOu ou = null;
        if (StringUtil.isBlank(parentOuGuid)) {
            for (FrameOu fou : allOuList) {
                if (fou.getOuname().equals(ouName)) {
                    ou = fou;
                    break;
                }
            }
        } else {
            for (FrameOu fou : allOuList) {
                if (fou.getOuname().equals(ouName) && parentOuGuid.equals(fou.getParentOuguid())) {
                    ou = fou;
                    break;
                }
            }
        }
        return ou;
    }

    private boolean checkSecOuExist(List<FrameUserSecondOU> list, String userGuid, String ouGuid) {
        boolean isexist = false;
        for (FrameUserSecondOU secOu : list) {
            if (secOu.getOuGuid().equals(ouGuid) && secOu.getUserGuid().equals(userGuid)) {
                isexist = true;
                break;
            }
        }
        return isexist;
    }

    private FrameRole getRoleByName(List<FrameRole> list, String roleName) {
        FrameRole result = null;
        for (FrameRole role : list) {
            if (role.getRoleName().equals(roleName)) {
                result = role;
                break;
            }
        }
        return result;
    }

    /**
     * 获取数量
     */
    public void check() {
        if (enableSOA) {
            addCallbackParam("issoa", l(SOAService.message));
            return;
        }
        if (isThreeManageMode) {
            addCallbackParam("threemanage", l("当前开启了三员管理，不允许这么操作！"));
            return;
        }
        int size = userservice.listAllUser().size();
        addCallbackParam("totalcount", size);
    }


}

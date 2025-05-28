package com.epoint.basic.controller.sysconf.personalset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.authorize.ButtonLog;
import com.epoint.basic.controller.authorize.IAuthorize;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.soa.SOAService;
import com.epoint.common.util.AttachUtil;
import com.epoint.core.dto.RequestContext;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2ClientUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.OrganLogCode;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.commission.api.ICommisssionSetServiceInternal;
import com.epoint.frame.service.message.commission.entity.FrameCommissionSet;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.opinion.api.IOpinionServiceInternal;
import com.epoint.frame.service.metadata.opinion.entity.FrameOpinion;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.metadata.systemparameters.api.IUserConfigService;
import com.epoint.frame.service.organ.job.api.IJobServiceInternal;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import com.epoint.frame.service.ui.exttabsconfig.api.IExtTabsConfigService;
import com.epoint.frame.service.ui.exttabsconfig.entity.FrameExtTabsConfig;
import com.epoint.frame.service.ui.uiset.module.api.IModuleServiceInternal;
import com.epoint.frame.service.ui.uiset.module.entity.FrameModule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * @author zhouq
 */
@RestController("jnmyinfomodifyaction")
@Scope("request")
public class JnMyInfoModifyAction extends BaseController {

    protected static final transient Logger buttonlog = Logger.getLogger("com.epoint.frame.util.ButtonLog");

    private static final long serialVersionUID = 2067132175476954155L;

    private FrameUser user = null;

    /**
     * 用户扩展信息
     */
    private FrameUserExtendInfo userExtendInfo = null;

    /**
     * 用户service
     */
    @Autowired
    private IUserServiceInternal userservice;

    @Autowired
    private IOuServiceInternal ouService;

    @Autowired
    private IModuleServiceInternal moduleservice;

    @Autowired
    private ICommisssionSetServiceInternal commissionsetservice;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    @Autowired
    private IOpinionServiceInternal opinionService;

    @Autowired
    protected IJobServiceInternal jobservice;

    @Autowired
    private IExtTabsConfigService extTabsConfigService;

    /**
     * 待办事宜提醒
     */
    private boolean chkwaithandleSms = true;

    /**
     * 照片地址
     */
    private String picture;

    /**
     * 手机号码公开
     */
    private boolean chkShowMobile = true;

    /**
     * 上传照片
     */
    private FileUploadModel9 fileUploadModel;

    /**
     * 学历下拉列表
     */
    private List<SelectItem> educationModel;

    /**
     * 政治面貌下拉列表
     */
    private List<SelectItem> politicaloutlookModel;

    /**
     * 学位下拉列表
     */
    private List<SelectItem> academicdegreeModel;


    /**
     * 上传个性签名图片
     */
    private FileUploadModel9 fileUploadModel2;

    private String userTitle;
    private String currentLoginId;

    /**
     * 参数配置service
     */
    private IUserConfigService frameUserConfigService = ContainerFactory.getContainInfo()
            .getComponent(IUserConfigService.class);
    private IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

    private static SM2ClientUtil util;

    @Override
    public void pageLoad() {
        // 用户基本信息
        user = userservice.getUserByUserField("userguid", userSession.getUserGuid());
        currentLoginId = user.getLoginId();
        // 用户扩展信息
        userExtendInfo = userservice.getUserExtendInfoWithPicContent(userSession.getUserGuid());
        FrameOu ou = ouService.getOuByOuField("ouguid", user.getOuGuid());
        if (ou != null) {
            String ouname = ou.getOuname();
            userExtendInfo.set("ouname", ouname);
        } else {
            userExtendInfo.set("ouname", "");
        }
        // 返回用于加密的字符串
        if (!isPostback()) {
            util = SM2ClientUtil.getInstance();
        }
        addCallbackParam("sm2PubKey", util.getSm2PubKey());
        init();
    }

    private void init() {
        // 待办事宜提醒
        String WaitHandleSmsReminder = frameUserConfigService.getConfigValue(userSession.getUserGuid(),
                "WaitHandleSmsReminder");
        if (StringUtil.isBlank(WaitHandleSmsReminder)) {
            frameUserConfigService.updateUserConfig(userSession.getUserGuid(), "WaitHandleSmsReminder", "1");
        } else if (!"1".equals(WaitHandleSmsReminder)) {
            chkwaithandleSms = false;
        }
        // 手机号码公开
        String PublicShowMobile = frameUserConfigService.getConfigValue(userSession.getUserGuid(), "PublicShowMobile");
        if (StringUtil.isBlank(PublicShowMobile)) {
            String result = "1";
            if ("1".equals(configservice.getFrameConfigValue("ShowMobileDefaultPrivate"))) {
                result = "0";
            }
            frameUserConfigService.updateUserConfig(userSession.getUserGuid(), "PublicShowMobile", result);
        } else if (!"1".equals(PublicShowMobile)) {
            chkShowMobile = false;
        }
        // 展示图片
        getPersonData();
    }

    /**
     * 个性化以下按钮日志，涉及到用户信息的不记录请求中的信息
     */
    @Override
    public String defense(IAuthorize handler, RequestContext requestContext, String pageUrl) {
        if (handler instanceof ButtonLog) {
            if (buttonlog.isDebugEnabled()) {
                // 最终不需要记录日志的actions
                List<String> openActions = new ArrayList<String>();
                // 加入完全公开的
                openActions.addAll(ConfigUtil.getOpenActions());
                // 扣除免登录的,免登录的action也需要进行日志记录
                openActions.removeAll(ConfigUtil.getNoNeedAuthActions());
                // 加上无需记录的
                List<String> noNeedButtonAction = new ArrayList<>();
                String noNeedButtonLogActions = ConfigUtil.getConfigValue(EpointKeyNames9.LOG_BUTTION_EXCLUDE);
                if (StringUtil.isNotBlank(noNeedButtonLogActions)) {
                    for (String item : noNeedButtonLogActions.split(";")) {
                        noNeedButtonAction.add(item);
                    }
                }
                openActions.addAll(noNeedButtonAction);
                if (!openActions.contains(requestContext.getActionName())) {
                    StringBuffer sb = new StringBuffer(100);
                    sb.append("用户");
                    if (StringUtil.isNotBlank(userSession.getDisplayName())) {
                        sb.append("姓名：");
                        sb.append(userSession.getDisplayName());
                    }
                    sb.append(" guid：");
                    sb.append(userSession.getUserGuid());
                    sb.append(" 操作地址：");
                    sb.append(requestContext.getReq().getRequestURL());
                    sb.append(" 操作方法：");
                    sb.append(requestContext.getCmd());
                    sb.append(" 参数值：");
                    // sb.append(CoreUtil.getCmdParams(requestContext));
                    sb.append(" IP地址：");
                    sb.append(userSession.getIp());
                    sb.append(" MAC地址：");
                    sb.append(userSession.getMac());
                    sb.append(" commondto：");
                    // sb.append(CoreUtil.getCommonDto(requestContext));
                    buttonlog.debug(sb.toString());
                }
            }
            return EpointKeyNames9.PERSONALIZED;
        }
        return null;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage storage) {
                    byte[] picContent = FileManagerUtil.getContentFromInputStream(storage.getIn());
                    String picContentType = storage.getContentType();
                    String base64Str = Base64Util.encode(picContent);
                    fileUploadModel.getExtraDatas().put("lblPicture",
                            "data:" + picContentType + ";base64," + base64Str);

                    storage.setIn(new ByteArrayInputStream(picContent));
                    AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), userSession.getUserGuid(),
                            storage.getAttachFileName(), storage.getContentType(), "personalPhoto",
                            storage.getSize(), new ByteArrayInputStream(picContent), userSession.getUserGuid(), userSession.getDisplayName());
                    userservice.addUserPicContent(storage, userSession.getUserGuid());
                    return false;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {

                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(userSession.getUserGuid(),
                    "personalPhoto", null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    public FileUploadModel9 getFileUploadModel2() {
        if (fileUploadModel2 == null) {
            AttachHandler9 handler = new AttachHandler9() {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage storage) {
                    byte[] picContent = FileManagerUtil.getContentFromInputStream(storage.getIn());
                    String picContentType = storage.getContentType();
                    String base64Str = Base64Util.encode(picContent);
                    fileUploadModel2.getExtraDatas().put("signPicture",
                            "data:" + picContentType + ";base64," + base64Str);

                    storage.setIn(new ByteArrayInputStream(picContent));
                    userservice.addUserSignImage(storage, userSession.getUserGuid());
                    return false;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                }
            };
            fileUploadModel2 = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(userSession.getUserGuid(),
                    "userSign", null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel2;
    }

    /**
     * 保存修改
     */
    public void save(String password, String identityCardNum) {
        // 如果NTX分机号填写了，则检查NTX分机号是否重复
        // 检查登录名是否重复 因为个人资料页面无法修改用户名，所以这里的验证去掉 zjf 2011/12/14
        // if (!userservice.isExistLoginID(user.getLoginId(), userGuid)) {
        // 更新用户基本信息
        // 因为密码有可能改了,所以先更新密码 edit by zjf 2014/4/1

        // 对手机号码重复进行验证
        if (userservice.isExistMobile(user.getMobile(), userSession.getUserGuid())) {
            addCallbackParam("msg", l("用户手机号已经被使用"));
            return;
        }
        String decryptMobile = util.decrypt(password);
        String decryptIdentityCardNum = util.decrypt(identityCardNum);
        userExtendInfo.setIdentityCardNum(decryptIdentityCardNum);
        user.setMobile(decryptMobile);
        user.setForceSaveToSoa("1");
        user.setLoginId(userSession.getLoginID());
        // user.setDisplayName(userSession.getDisplayName());
        userservice.updateFrameUser(user, userExtendInfo);
        FrameUser newUser = userservice.getUserByUserField("userGuid", userSession.getUserGuid());
        user.setPassword(newUser.getPassword());

        // 待办事宜提醒： 需要手机提醒
        frameUserConfigService.updateUserConfig(userSession.getUserGuid(), "WaitHandleSmsReminder",
                chkwaithandleSms ? "1" : "0");

        // 手机号码： 公开 (默认为公开)
        frameUserConfigService.updateUserConfig(userSession.getUserGuid(), "PublicShowMobile",
                chkShowMobile ? "1" : "0");
        // 得到FrameModule
        FrameModule module = moduleservice.getFrameModuleByUrl(getVisitUrl());
        // 得到模块全路径
        String fullUrl = moduleservice.getFullUrl(module);
        if (module != null) {
            insertOperateLog(LOG_OPERATOR_TYPE_MODIFY, LOG_SUBSYSTEM_TYPE_MYINFO, l("修改个人资料"), "Frame_User", "",
                    fullUrl, module.getModuleUrl());
        } else {
            insertOperateLog(LOG_OPERATOR_TYPE_MODIFY, LOG_SUBSYSTEM_TYPE_MYINFO, l("修改个人资料"), "Frame_User");
        }
        userSession.refreshSessionFromUserGuid(user, userSession.isAdmin(), userSession.getIp());
        addCallbackParam("msg", l("保存成功"));
    }

    /**
     * 删除签名图片
     */
    public void delUserSign() {
        userservice.deleteUserSignImage(userSession.getUserGuid());

        addCallbackParam("msg", l("删除成功"));
        // 得到FrameModule
        FrameModule module = moduleservice.getFrameModuleByUrl(getVisitUrl());
        // 得到模块全路径
        String fullUrl = moduleservice.getFullUrl(module);
        if (module != null) {
            insertOperateLog(LOG_OPERATOR_TYPE_DELETE, LOG_SUBSYSTEM_TYPE_MYINFO, l("删除个人签名图片"), "Frame_AttachInfo", "",
                    fullUrl, module.getModuleUrl());
        } else {
            insertOperateLog(LOG_OPERATOR_TYPE_DELETE, LOG_SUBSYSTEM_TYPE_MYINFO, l("删除个人签名图片"), "Frame_AttachInfo");
        }
    }

    /**
     * 删除头像
     */
    public void delPhoto() {
        userservice.deleteUserPicContent(userSession.getUserGuid());
        addCallbackParam("msg", l("删除成功"));
        // 得到FrameModule
        FrameModule module = moduleservice.getFrameModuleByUrl(getVisitUrl());
        // 得到模块全路径
        String fullUrl = moduleservice.getFullUrl(module);
        if (module != null) {
            insertOperateLog(LOG_OPERATOR_TYPE_DELETE, LOG_SUBSYSTEM_TYPE_MYINFO, l("删除个人头像"), "Frame_User_ExtendInfo",
                    "", fullUrl, module.getModuleUrl());
        } else {
            insertOperateLog(LOG_OPERATOR_TYPE_DELETE, LOG_SUBSYSTEM_TYPE_MYINFO, l("删除个人头像"), "Frame_User_ExtendInfo");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("portrait", "data:" + userExtendInfo.getPicContentType() + ";base64,"
                + Base64Util.encode(getDefaultPhoto(userSession.getUserGuid())));
        //return jsonObject.toJSONString();
    }

    /**
     * 流程代理信息配置
     */
    public String getFlowAgencyInfo() {
        JSONArray jsonArray = new JSONArray();
        List<FrameCommissionSet> commissionSets = null;
        // String group = ConfigUtil.getConfigValue("CommissionAdmin");
        // if (StringUtil.isNotBlank(group)) {
        // if (roleService.isExistUserRoleName(userSession.getUserGuid(),
        // group)) {
        // commissionSets = commissionsetservice.getAllFrameCommissionSet();
        // }
        // }
        // else {
        commissionSets = commissionsetservice.selectCommissionByLeadUserGuid(userSession.getUserGuid());
        // }

        if (commissionSets != null && commissionSets.size() > 0) {
            for (FrameCommissionSet item : commissionSets) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", item.getCommissionGuid());
                jsonObject.put("username", item.getCommissionUserName());
                String commissionType = "";
                Integer type = item.getCommissionType();
                if (type == 20) {
                    commissionType = "time";
                } else if (type == 30) {
                    commissionType = "case";
                }
                jsonObject.put("type", commissionType);
                List<MessagesCenter> messagesCenter = messagesCenterService
                        .selectMessagesCenter(item.getCommissionGuid());
                String caseName = "";
                if (messagesCenter != null && messagesCenter.size() > 0) {
                    for (int i = 0; i < messagesCenter.size(); i++) {
                        caseName += messagesCenter.get(i).getTitle() + ";";
                        /*
                         * if (messagesCenter.size() - 1 == i) { caseName +=
                         * messagesCenter.get(i).getTitle(); }
                         */
                    }
                }
                jsonObject.put("caseName", caseName);
                jsonObject.put("startDate", item.getCommissionFromDate() == null ? l("无")
                        : EpointDateUtil.convertDate2String(item.getCommissionFromDate()));
                jsonObject.put("endDate", item.getCommissionToDate() == null ? l("无")
                        : EpointDateUtil.convertDate2String(item.getCommissionToDate()));
                jsonObject.put("portrait", picture);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray.toString();
    }

    /**
     * 用户头像上传,ie10以上使用
     */
    public void upLoadPortrait() {
        String imgData = getRequestParameter("imgData");
        String contentType = "";
        if (StringUtil.isNotBlank(imgData)) {
            String[] split = imgData.split(",");
            String str = split[0];
            contentType = str.substring(str.indexOf(":") + 1, str.indexOf(";"));
            imgData = split[1];
        }
        byte[] decodeBuffer = Base64Util.decodeBuffer(imgData);
        if (SOAService.isEnableSOA()) {
            new SOAService().uploadUserImage(new ByteArrayInputStream(decodeBuffer), "uploadUserImage",
                    userSession.getUserGuid(), contentType, String.class);
        } else {
            userExtendInfo.setPicContent(decodeBuffer);
            userExtendInfo.setPicContentType(contentType);
            userservice.updateFrameUser(user, userExtendInfo);
        }
        addCallbackParam("success", "success");
    }

    /**
     * 展示个人意见
     */
    public String getMyOpinionList() {
        JSONArray jsonArray = new JSONArray();
        List<FrameOpinion> opinionList = opinionService.selectOpinionByUserGuid(userSession.getUserGuid());
        if (opinionList != null && opinionList.size() > 0) {
            for (FrameOpinion item : opinionList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", item.getOpinionGuid());
                jsonObject.put("sort", item.getOrderNumber());
                jsonObject.put("text", item.getOpinionText());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray.toString();
    }

    /**
     * 添加修改个人意见
     */
    public void editOrAddOpinion() {
        String id = getRequestParameter("id");
        String sort = getRequestParameter("sort");
        String text = getRequestParameter("text");
        String message = "";
        // 修改id不为空
        if (StringUtil.isNotBlank(id)) {
            FrameOpinion opinion = new FrameOpinion();
            opinion = opinionService.selectOpinionByGuid(id);
            if (opinion != null) {
                opinion.setOpinionText(text);
                opinion.setOrderNumber(Integer.parseInt(sort));
            }
            message = opinionService.updateOpinion(opinion);
            if (StringUtil.isNotBlank(message)) {
                addCallbackParam("msg", l(message));
            } else {
                addCallbackParam("success", "success");
            }
        } else {
            String opinionGuid = UUID.randomUUID().toString();
            FrameOpinion opinion = new FrameOpinion();
            opinion.setOpinionGuid(opinionGuid);
            opinion.setOpinionText(text);
            opinion.setOrderNumber(Integer.parseInt(sort));
            opinion.setUserGuid(userSession.getUserGuid());
            opinion.setBaseOuGuid("");
            message = opinionService.insertOpinion(opinion);
            if (StringUtil.isNotBlank(message)) {
                addCallbackParam("msg", l(message));
            } else {
                addCallbackParam("success", "success");
            }
        }

    }

    /**
     * 删除个人意见
     */
    public void deleteOpinion() {
        String id = getRequestParameter("id");
        if (StringUtil.isNotBlank(id)) {
            opinionService.deleteOpinion(id);
        }
        addCallbackParam("success", "success");
    }

    /**
     * 删除代理人
     */
    public void deleteCommission(String guid) {
        if (StringUtil.isNotBlank(guid)) {
            commissionsetservice.delete(guid);
            // 新增日志
            insertSystemLog(l(LOG_OPERATOR_TYPE_DELETE), l(OrganLogCode.getInstance().getSubSystemType()),
                    l("删除代理人guid") + guid);
            addCallbackParam("success", "success");
        }
    }

    /**
     * 获取个人头像，名称等
     */
    public void getPersonData() {
        // 初始化部门，状态
        FrameOu ou = ouService.getOuByOuGuid(userSession.getOuGuid());
        addCallbackParam("userDept", l(ou.getOuname()));
        // 初始化个人姓名，职务
        addCallbackParam("userName", l(user.getDisplayName()));

        // 初始化照片
        FrameAttachStorage signImage = null;
        try {
            signImage = userservice.getUserSignImage(userSession.getUserGuid());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (signImage != null) {
            addCallbackParam("signPicture", "data:" + l(signImage.getContentType()) + ";base64,"
                    + l(Base64Util.encode(FileManagerUtil.getContentFromInputStream(signImage.getContent()))));
        }
        boolean existPicContent = false;
        if (userExtendInfo != null) {
            byte[] content = userExtendInfo.getPicContent();
            existPicContent = content != null && content.length > 0;
            if (existPicContent) {
                addCallbackParam("lblPicture",
                        "data:" + l(userExtendInfo.getPicContentType()) + ";base64," + l(Base64Util.encode(content)));
            }
        }
        addCallbackParam("isDefaultPortrait", !existPicContent);

    }

    /**
     * 后端可以配置扩展的tab
     *
     * @return
     */
    public String getExtTabsInfo() {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
        // 0：个人信息维护tab页拓展
        List<FrameExtTabsConfig> list = extTabsConfigService.listTabsConfigByType(0);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", "ext-tab" + i);
                map.put("name", list.get(i).getExtName());
                map.put("url", list.get(i).getExtUrl());
                arrayList.add(map);
            }
        }
        return JsonUtil.listToJson(arrayList);
    }

    public List<SelectItem> educationModel() {
        if (educationModel == null) {
            educationModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory("下拉列表", "个人信息学历", null, false));
        }
        return this.educationModel;
    }

    public List<SelectItem> politicaloutlookModel() {
        if (politicaloutlookModel == null) {
            politicaloutlookModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory("下拉列表", "个人信息政治面貌", null, false));
        }
        return this.politicaloutlookModel;
    }

    public List<SelectItem> academicdegreeModel() {
        if (academicdegreeModel == null) {
            academicdegreeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory("下拉列表", "个人信息学位", null, false));
        }
        return this.academicdegreeModel;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getCurrentLoginId() {
        return currentLoginId;
    }

    public void setCurrentLoginId(String currentLoginId) {
        this.currentLoginId = currentLoginId;
    }

    public FrameUser getUser() {
        return user;
    }

    public void setUser(FrameUser user) {
        this.user = user;
    }

    public FrameUserExtendInfo getUserExtendInfo() {
        return userExtendInfo;
    }

    public void setUserExtendInfo(FrameUserExtendInfo userExtendInfo) {
        this.userExtendInfo = userExtendInfo;
    }

    public boolean getChkwaithandleSms() {
        return chkwaithandleSms;
    }

    public void setChkwaithandleSms(boolean chkwaithandleSms) {
        this.chkwaithandleSms = chkwaithandleSms;
    }

    public boolean getChkShowMobile() {
        return chkShowMobile;
    }

    public void setChkShowMobile(boolean chkShowMobile) {
        this.chkShowMobile = chkShowMobile;
    }

}

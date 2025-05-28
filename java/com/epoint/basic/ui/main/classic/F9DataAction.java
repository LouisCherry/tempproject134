package com.epoint.basic.ui.main.classic;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.addressbook.api.IF9ConnListService;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.ui.uiset.module.api.IModuleService;
import com.epoint.frame.service.ui.uiset.module.entity.FrameModule;
import com.epoint.frame.service.ui.uiset.quicklink.api.IQuickLinkUserService;
import com.epoint.frame.service.ui.uiset.quicklink.entity.FrameQuickLinkUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * F9经典风格数据接口
 * 
 * @author zhouq
 * @version 2015/10/26
 */
@RestController("f9dataaction")
@Scope("request")
public class F9DataAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -1734719248355471235L;

    /**
     * 菜单请求唯一标识 (全局菜单请求标识)
     */
    private static final String QUERY_MODULE = "init-sidebarNav";

    /**
     * 顶层菜单请求标识
     */
    private static final String QUERY_TOP_MODULE = "init-sidebarNav-top";

    /**
     * 二级菜单请求标识
     */
    private static final String QUERY_SUB_MODULE = "init-sidebarNav-sub";

    /**
     * 自定义菜单请求唯一标识
     */
    private static final String QUERY_QUICK = "init-quickNav";

    /**
     * 保存个性签名请求唯一标识
     */
    private static final String SAVE_SIGNATURE = "save-signature";

    /**
     * 获取配置中心数据
     */
    private static final String QUERY_SETTINGCENTER = "sidebarnav";

    /**
     * 获取组织数据
     */
    private static final String QUERY_ORGDATA = "get-msgOrg-data";

    /**
     * 人员查询
     */
    private static final String SEARCH_ORGDATA = "search-msgOrg";

    /**
     * 首页上面的一些信息
     */
    private static final String QUERY_USERINFO = "init-header";

    /**
     * 模块service
     */
    @Autowired
    private IModuleService moduleservice;

    /**
     * 自定义菜单service
     */
    @Autowired
    private IQuickLinkUserService quicklinkuserservice;

    /**
     * 用户service
     */
    @Autowired
    private IUserServiceInternal userservice;

    /**
     * 系统参数service
     */
    @Autowired
    private IConfigService configservice;

    private static String customModuleOutClass;

    static {
        customModuleOutClass = ConfigUtil.getConfigValue("customModuleOutClass");
    }

    public void pageLoad() {
    }

    public String initData() {
        // 请求表示
        String query = getRequestParameter("query");
        // 返回的json数据
        String result = "";
        if (QUERY_MODULE.equals(query)) {
            // 开始获取模块数据
            result = getModuleJSON(true);
        }
        else if (QUERY_QUICK.equals(query)) {
            // 获取自定义菜单数据
            result = getCustomModuleJSON();
        }
        else if (SAVE_SIGNATURE.equals(query)) {
            // 保存个性签名
            String signature = getRequestParameter("signature");
            String userguid = getRequestParameter("userguid");
            result = saveSignature(userguid, signature);
        }
        else if (QUERY_SETTINGCENTER.equalsIgnoreCase(query)) {
            // 配置中心
            String type = getRequestParameter("type").toString();
            result = getSettingCenterJSON(type);
        }
        else if (QUERY_ORGDATA.equals(query)) {
            // 组织树
            result = getUserJSON();
        }
        else if (SEARCH_ORGDATA.equals(query)) {
            // 查询人员
            String keywords = getRequestParameter("keywords");
            result = getSearchUserJSON(keywords);
        }
        // 查询用户信息
        else if (QUERY_USERINFO.equals(query)) {
            result = getUserInfoJSON();
        }
        else if (QUERY_TOP_MODULE.equals(query) || QUERY_SUB_MODULE.equals(query)) {
            result = getModuleJSON(false);
        }

        if (StringUtil.isBlank(result)) {
            result = "{}";
        }
        return result;
    }

    private String getDefaultModuleCode(String moduleCode) {
        if (moduleCode.indexOf("-") != -1) {
            moduleCode = moduleCode.split("-")[1];
        }
        else {
            moduleCode = "";
        }
        return moduleCode;
    }

    private String getModuleCode(String moduleCode) {
        if (moduleCode.indexOf("-") != -1) {
            moduleCode = moduleCode.split("-")[0];
        }
        return moduleCode;
    }

    /**
     * 获取模块JSON数据
     * 
     * @return
     */
    private String getModuleJSON(boolean loadAll) {
        String moduleCode = getRequestParameter("moduleCode");
        if (moduleCode == null) {
            moduleCode = "";
        }
        String defaultModuleCode = getDefaultModuleCode(moduleCode);
        moduleCode = getModuleCode(moduleCode);
        if (loadAll) {
            return initOneLevel(moduleCode, defaultModuleCode, true, true, false, false, false, false, null);
        }
        else {
            return initOneLevelTop(moduleCode, defaultModuleCode, true, true, false, false, false, false, null);
        }
    }

    private String getCustomModuleJSON() {
        // 查询所有自定义菜单
        List<FrameQuickLinkUser> tempList = quicklinkuserservice.listQuickLinkByUserGuid(userSession.getUserGuid());
        return initCustomModuleJSON(tempList);
    }

    /**
     * 初始化一层菜单节点数据
     * 
     * @param moduleCode
     *            父节点的code
     * @param defaultModuleCode
     *            默认打开的code
     * @param remainedModule
     *            所有的剩余节点
     * @param top
     *            是否顶层菜单
     * @param accordingFirst
     *            是否第一层左侧tabTree
     * @param according
     *            是否左侧tabTree
     * @param accordingBigFirst
     *            是否第一层左侧tabTreebig
     * @param accordingBig
     *            是否左侧tabTreebig
     * @param bigmenu
     *            是否大菜单
     * @return json字符串
     */
    private String initOneLevel(String moduleCode, String defaultModuleCode, boolean top, boolean accordingFirst,
            boolean according, boolean accordingBigFirst, boolean accordingBig, boolean bigmenu,
            List<FrameModule> remainedModule) {
        StringBuffer resu = new StringBuffer("");
        // 如果moduleList为null则需要进行初始化查询
        if (remainedModule == null && StringUtil.isBlank(moduleCode)) {
            remainedModule = moduleservice.listModuleByCodeHasRight(userSession.isAdmin(), userSession.isOuAdmin(),
                    userSession.getBaseOUGuid(), userSession.getOuGuid(), userSession.getUserGuid(),
                    userSession.getUserRoleList(), "", 3, 0, isThreeManageMode());
        }
        // 拼接json
        int length = remainedModule.size();
        if (length > 0) {
            boolean expanded = false;
            resu.append("[");
            for (int i = 0; i < length; i++) {
                FrameModule frameModule = remainedModule.get(i);
                if (frameModule.getModuleCode().length() != moduleCode.length() + 4)
                    break;
                if ((frameModule.getIsAddOu() == null || frameModule.getIsAddOu() == 0)) {
                    resu.append("{");
                    String module_name = frameModule.getModuleName();
                    module_name = module_name == null ? "" : module_name;
                    String module_url = frameModule.getModuleUrl();
                    module_url = module_url == null ? "" : module_url.split(";")[0];
                    String module_code = frameModule.getModuleCode();
                    module_code = module_code == null ? "" : module_code;
                    Boolean module_isBlank = frameModule.getIsBlank();
                    module_isBlank = module_isBlank == null ? false : module_isBlank;
                    module_url = module_url.replace("\\", "/");
                    module_url = module_url.replaceAll("=", "_SPLIT_");

                    // module_url = ConfigUtil.urlMapping(module_url);

                    // 用模块名称进行映射
                    String moduleName = l(module_name);
                    if (StringUtil.isNotBlank(moduleName)) {
                        module_name = moduleName;
                    }

                    module_url = module_url.replaceAll("_SPLIT_", "=");

                    // 为所有模块路径添加token参数，用于csrf防御 add by zjf 2015/3/25
                    // String token = userSession.getToken();
                    // module_url =
                    // StringUtil.convertUrlWithoutApplicationPath(module_url,
                    // "token=" + token);

                    resu.append("\"name\":\"" + module_name + "\",");
                    resu.append("\"code\":\"" + module_code + "\",");
                    resu.append("\"url\":\"" + module_url + "\",");

                    if (according) {
                        if (accordingFirst) {
                            // 导航树是否采用懒加载（只加载一层数据，后面的数据获取靠ajax）
                            resu.append("\"isLazy\":" + false + ",");
                            if (!expanded) {
                                // 如果指定了默认打开地址，那么要根据这个地址来决定默认打开哪个tab
                                if (StringUtil.isNotBlank(defaultModuleCode)) {
                                    if (defaultModuleCode.startsWith(frameModule.getModuleCode())) {
                                        expanded = true;
                                    }
                                }
                                else if (i == 0) {
                                    expanded = true;
                                }
                            }
                            // 手风琴是否默认展开【true:展开|false:闭合】
                            resu.append("\"expanded\":" + expanded + ",");
                            // 导航树是否默认自动全部展开【true:全部展开|false:用户自行展开】
                            resu.append("\"itemsExpanded\":" + true + ",");
                        }
                        if (StringUtil.isNotBlank(frameModule.getSmallIconAddress())) {
                            resu.append("\"icon\":\"" + frameModule.getSmallIconAddress() + "\",");
                        }
                        else {
                            resu.append("\"icon\":\"" + "" + "\",");
                        }
                    }
                    else {
                        // 大菜单只有在顶级菜单、大菜单、左侧tab大图标且第二层中出现
                        if (top || bigmenu || (accordingBig && !accordingBigFirst)) {
                            if (StringUtil.isNotBlank(frameModule.getBigIconAddress())) {
                                if (frameModule.getBigIconAddress().endsWith(".gif")) {
                                    frameModule.setBigIconAddress("modicon-1");
                                }
                                else if (frameModule.getBigIconAddress().startsWith("icons2424-")) {
                                    frameModule.setBigIconAddress(
                                            frameModule.getBigIconAddress().replace("icons2424-", "modicon-"));
                                }
                                resu.append("\"icon\":\"" + frameModule.getBigIconAddress() + "\",");
                            }
                            else {
                                resu.append("\"icon\":\"" + "modicon-1" + "\",");
                            }
                        }
                        // 小图标
                        else {
                            if (StringUtil.isNotBlank(frameModule.getSmallIconAddress())) {
                                if (frameModule.getSmallIconAddress().startsWith("modicons1414-")) {
                                    frameModule.setSmallIconAddress(
                                            frameModule.getSmallIconAddress().replace("modicons1414-", "modicon-"));
                                }
                                resu.append("\"icon\":\"" + frameModule.getSmallIconAddress() + "\",");
                            }
                            else {
                                resu.append("\"icon\":\"" + "icon-1" + "\",");
                            }
                        }
                    }

                    resu.append("\"isBlank\":" + module_isBlank + "");

                    boolean isLeaf = true;
                    // 判断是否还有子模块
                    List<FrameModule> childModuleList = new ArrayList<FrameModule>();
                    for (int j = i + 1; j < length; j++) {
                        FrameModule module = remainedModule.get(j);
                        if (module.getModuleCode().startsWith(module_code)) {
                            childModuleList.add(module);
                            remainedModule.remove(j);
                            length -= 1;
                            j--;
                        }
                    }

                    // 如果还有子模块，(并且不是左侧tab大图标菜单的第二层,因为这个仅允许2层),那么进行递归
                    if (!(accordingBig && !accordingBigFirst) && !bigmenu) {
                        String childs = "";
                        if (StringUtil.isNotBlank(module_code)) {
                            childs = initOneLevel(module_code, defaultModuleCode, false, false, according, false,
                                    accordingBig, bigmenu, childModuleList);
                            if (StringUtil.isNotBlank(childs)) {
                                resu.append(",\"items\":" + childs);
                                if (childs.length() > 10) {
                                    isLeaf = false;
                                }
                            }
                        }
                    }

                    resu.append(",\"isLeaf\":" + isLeaf + "");
                    resu.append("}");
                    if (i != length - 1) {
                        resu.append(",");
                    }
                }
            }
            if (resu.toString().endsWith(",")) {
                resu = new StringBuffer(resu.substring(0, resu.length() - 1));
            }
            resu.append("]");

        }
        return resu.toString();
    }

    /**
     * 初始化自定义菜单JSON
     * 
     * @param tempList
     */
    private String initCustomModuleJSON(List<FrameQuickLinkUser> tempList) {
        StringBuffer result = new StringBuffer();
        result.append("[");
        for (int i = 0; i < tempList.size(); i++) {
            FrameQuickLinkUser frameQuickLinkUser = tempList.get(i);
            if (frameQuickLinkUser.getIsDisable() == 1) {
                continue;
            }
            if (!"[".equals(result.toString())) {
                result.append(",");
            }
            result.append("{\"name\":\"").append(frameQuickLinkUser.getLinkName()).append("\",");
            // 这里的code只作为tab标签打开的标识使用,不再查询数据库或者缓存,生成一个
            result.append("\"code\":\"").append("frameQuickLinkUser" + i).append("\",");
            result.append("\"url\":\"")
                    .append(frameQuickLinkUser != null && StringUtil.isNotBlank(frameQuickLinkUser.getLinkUrl())
                            ? frameQuickLinkUser.getLinkUrl().replace("\\", "/") : "")
                    .append("\",");
            result.append("\"isBlank\":\"").append(frameQuickLinkUser.getIsBlank() == 1 ? "true" : "false")
                    .append("\"}");
        }
        result.append("]");
        return result.toString();
    }

    /**
     * 保存用户个性签名
     * 
     * @param userguid
     * @param signature
     */
    private String saveSignature(String userguid, String signature) {
        // 更新Frame_User表DESCRIPTION字段
        FrameUser user = userservice.getUserByUserField("userGuid", userguid);
        user.setDescription(signature);
        userservice.updateFrameUser(user, null);
        userSession.setSignature(signature);
        return "{\"state\":\"success\"}";
    }

    /**
     * 获取配置中心类模版地址(即左右结构)
     * 
     * @return String
     */
    public String getSettingCenterModuleCode() {

        return "";
    }

    private String getSettingCenterJSON(String type) {
        if (FileManagerUtil.isExist(ClassPathUtil.getDeployWarPath() + "/frame", false)) {
            String result = "";
            if ("1".equals(type)) {
                result = "[{\"name\":\"个人信息\",\"code\":\"0097000300070001\",\"url\":\"frame/pages/basic/personalset/myinfomodify"
                        + "\",\"icon\":\"modicon-92\"},"
                        + "{\"name\":\"密码修改\",\"code\":\"0097000300070002\",\"url\":\"frame/pages/basic/personalset/mypasswordmodify"
                        + "\",\"icon\":\"modicon-114\"}]";
            }
            else if ("2".equals(type)) {
                result = "[{\"name\":\"首页配置\",\"code\":\"0097000300080001\",\"url\":\"frame/pages/basic/personalset/myhomepagesetting"
                        + "\",\"icon\":\"modicon-113\"},"
                        + "{\"name\":\"界面样式\",\"code\":\"0097000300080002\",\"url\":\"frame/pages/basic/personalset/mypagesetting"
                        + "\",\"icon\":\"modicon-112\"}]";
            }
            else if ("3".equals(type)) {
                result = "[{\"name\":\"我的意见\",\"code\":\"0097000300090001\",\"url\":\"frame/pages/basic/opinion/myopinionlist"
                        + "\",\"icon\":\"modicon-83\"},"
                        + "{\"name\":\"个性签名\",\"code\":\"0097000300090002\",\"url\":\"frame/pages/basic/personalsignature/mysignaturelist"
                        + "\",\"icon\":\"modicon-40\"},"
                        + "{\"name\":\"流程代理人\",\"code\":\"0097000300090003\",\"url\":\"frame/pages/basic/commission/mycommissionlist?type=own"
                        + "\",\"icon\":\"modicon-118\"}]";
                       /* + "{\"name\":\"个人消息规则\",\"code\":\"0097000300090005\",\"url\":\"framemanager/messages/personalrule/messagespersonalrulelist"
                        + "\",\"icon\":\"modicon-76\"}]"*/;
            }
            else if ("all".endsWith(type)) {
                result = "[{\"name\": \"基本配置\",\"code\": \"111\",\"url\": \"module_1.html\",\"icon\": \"modicon-1\",\"items\": [{\"name\":\"个人信息\",\"code\":\"0097000300070001\",\"url\":\"frame/pages/basic/personalset/myinfomodify"
                        + "\",\"icon\":\"modicon-92\"},"
                        + "{\"name\":\"密码修改\",\"code\":\"0097000300070002\",\"url\":\"frame/pages/basic/personalset/mypasswordmodify"
                        + "\",\"icon\":\"modicon-114\"}]},"
                        + "{\"name\": \"界面配置\",\"code\": \"111\",\"url\": \"module_1.html\",\"icon\": \"modicon-1\",\"items\": [{\"name\":\"首页配置\",\"code\":\"0097000300080001\",\"url\":\"frame/pages/basic/personalset/myhomepagesetting"
                        + "\",\"icon\":\"modicon-113\"},"
                        + "{\"name\":\"界面样式\",\"code\":\"0097000300080002\",\"url\":\"frame/pages/basic/personalset/mypagesetting"
                        + "\",\"icon\":\"modicon-112\"}]},"
                        + "{\"name\": \"功能配置\",\"code\": \"111\",\"url\": \"module_1.html\",\"icon\": \"modicon-1\",\"items\": [{\"name\":\"我的意见\",\"code\":\"0097000300090001\",\"url\":\"frame/pages/basic/opinion/myopinionlist"
                        + "\",\"icon\":\"modicon-83\"},"
                        + "{\"name\":\"个性签名\",\"code\":\"0097000300090002\",\"url\":\"frame/pages/basic/personalsignature/mysignaturelist"
                        + "\",\"icon\":\"modicon-40\"},"
                        + "{\"name\":\"流程代理人\",\"code\":\"0097000300090003\",\"url\":\"frame/pages/basic/commission/mycommissionlist?type=own"
                        + "\",\"icon\":\"modicon-118\"},"
                        + "{\"name\":\"个人渠道配置\",\"code\":\"0097000300090004\",\"url\":\"framemanager/messages/user/messagesprivateuserlist"
                        + "\",\"icon\":\"modicon-67\"},"
                        + "{\"name\":\"自定义消息规则\",\"code\":\"0097000300090005\",\"url\":\"framemanager/messages/personalrule/messagespersonalrulelist"
                        + "\",\"icon\":\"modicon-76\"}]}]";
            }
            return result;

        }
        else {
            String result = "";
            if ("1".equals(type)) {
                result = "[{\"name\":\"个人信息\",\"code\":\"0097000300070001\",\"url\":\"frame/pages/basic/personalset/myinfomodify"
                        + "\",\"icon\":\"modicon-92\"},"
                        + "{\"name\":\"密码修改\",\"code\":\"0097000300070002\",\"url\":\"frame/pages/basic/personalset/mypasswordmodify"
                        + "\",\"icon\":\"modicon-114\"}]";
            }
            else if ("2".equals(type)) {
                result = "[{\"name\":\"首页配置\",\"code\":\"0097000300080001\",\"url\":\"frame/pages/basic/personalset/myhomepagesetting"
                        + "\",\"icon\":\"modicon-113\"},"
                        + "{\"name\":\"界面样式\",\"code\":\"0097000300080002\",\"url\":\"frame/pages/basic/personalset/mypagesetting"
                        + "\",\"icon\":\"modicon-112\"}]";
            }
            else if ("3".equals(type)) {
                result = "[{\"name\":\"我的意见\",\"code\":\"0097000300090001\",\"url\":\"frame/pages/basic/opinion/myopinionlist"
                        + "\",\"icon\":\"modicon-83\"},"
                        + "{\"name\":\"个性签名\",\"code\":\"0097000300090002\",\"url\":\"frame/pages/basic/personalsignature/mysignaturelist"
                        + "\",\"icon\":\"modicon-40\"},"
                        + "{\"name\":\"流程代理人\",\"code\":\"0097000300090003\",\"url\":\"frame/pages/basic/commission/mycommissionlist?type=own"
                        + "\",\"icon\":\"modicon-118\"}"
                        + "{\"name\":\"个人渠道配置\",\"code\":\"0097000300090004\",\"url\":\"framemanager/messages/user/messagesprivateuserlist"
                        + "\",\"icon\":\"modicon-67\"},"
                        + "{\"name\":\"个人消息规则\",\"code\":\"0097000300090005\",\"url\":\"framemanager/messages/personalrule/messagespersonalrulelist"
                        + "\",\"icon\":\"modicon-76\"}]";
            }
            else if ("all".endsWith(type)) {
                result = "[{\"name\": \"基本配置\",\"code\": \"111\",\"url\": \"module_1.html\",\"icon\": \"modicon-1\",\"items\": [{\"name\":\"个人信息\",\"code\":\"0097000300070001\",\"url\":\"frame/pages/basic/personalset/myinfomodify"
                        + "\",\"icon\":\"modicon-92\"},"
                        + "{\"name\":\"密码修改\",\"code\":\"0097000300070002\",\"url\":\"frame/pages/basic/personalset/mypasswordmodify"
                        + "\",\"icon\":\"modicon-114\"}]},"
                        + "{\"name\": \"界面配置\",\"code\": \"111\",\"url\": \"module_1.html\",\"icon\": \"modicon-1\",\"items\": [{\"name\":\"首页配置\",\"code\":\"0097000300080001\",\"url\":\"frame/pages/basic/personalset/myhomepagesetting"
                        + "\",\"icon\":\"modicon-113\"},"
                        + "{\"name\":\"界面样式\",\"code\":\"0097000300080002\",\"url\":\"frame/pages/basic/personalset/mypagesetting"
                        + "\",\"icon\":\"modicon-112\"}]},"
                        + "{\"name\": \"功能配置\",\"code\": \"111\",\"url\": \"module_1.html\",\"icon\": \"modicon-1\",\"items\": [{\"name\":\"我的意见\",\"code\":\"0097000300090001\",\"url\":\"frame/pages/basic/opinion/myopinionlist"
                        + "\",\"icon\":\"modicon-83\"},"
                        + "{\"name\":\"个性签名\",\"code\":\"0097000300090002\",\"url\":\"frame/pages/basic/personalsignature/mysignaturelist"
                        + "\",\"icon\":\"modicon-40\"},"
                        + "{\"name\":\"流程代理人\",\"code\":\"0097000300090003\",\"url\":\"frame/pages/basic/commission/mycommissionlist?type=own"
                        + "\",\"icon\":\"modicon-118\"},"
                        + "{\"name\":\"个人消息规则\",\"code\":\"0097000300090005\",\"url\":\"framemanager/messages/personalrule/messagespersonalrulelist"
                        + "\",\"icon\":\"modicon-76\"}]}]";
            }
            return result;
        }
    }

    /**
     * 获取内部通讯录JSON
     */
    private String getUserJSON() {
        // 接口配置
        String className = ConfigUtil.getConfigValue("OAConnListImpl");
        IF9ConnListService service = ContainerFactory.getContainInfo().getComponent(IF9ConnListService.class);
        StringBuffer result = new StringBuffer();
        result.append("{\"inner\":");
        result.append(getInnerJSON("inner", service, null, null));
        result.append(",\"public\":");
        if (StringUtil.isBlank(className)) {
            result.append("[]");
        }
        else {
            service = (IF9ConnListService) ReflectUtil.getObjByClassName(className);
            result.append(getInnerJSON("pub", service, null, null));
        }
        result.append(",\"personal\":");
        if (StringUtil.isBlank(className)) {
            result.append("[]");
        }
        else {
            service = (IF9ConnListService) ReflectUtil.getObjByClassName(className);
            result.append(getInnerJSON(userSession.getUserGuid(), service, null, null));
        }
        result.append("}");
        return result.toString();
    }

    public String getInnerJSON(String type, IF9ConnListService service, String[] ou, String[] user) {

        StringBuffer result = new StringBuffer();
        if (ou == null && user == null) {
            result.append("[");
            // 查询所有一级部门
            List<String[]> ouList = service.getConnList(type, null);
            for (int i = 0; i < ouList.size(); i++) {
                if (i > 0) {
                    result.append(",");
                }
                result.append(getInnerJSON(type, service, ouList.get(i), null));
            }
            result.append("]");
        }
        else if (ou != null && user == null) {
            result.append("{");
            result.append("\"guid\":\"");
            result.append(ou[0]);
            result.append("\",\"name\":\"");
            result.append(dealSpecialChar(ou[1]));
            result.append("\",\"items\":[");
            // 查询信息
            List<String[]> list = service.getConnList("inner", ou[0]);
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    result.append(",");
                }
                if ("0".equals(list.get(i)[2])) {
                    result.append(getInnerJSON(type, service, null, list.get(i)));
                }
                else if ("1".equals(list.get(i)[2])) {
                    result.append(getInnerJSON(type, service, list.get(i), null));
                }
            }
            result.append("]}");
        }
        else if (ou == null && user != null) {
            result.append("{\"guid\":\"");
            result.append(user[0]);
            result.append("\",\"name\":\"");
            result.append(dealSpecialChar(user[1]));
            result.append("\"}");

        }
        return result.toString();
    }

    /**
     * 处理特殊字符
     * @param name
     * @return
     */
    private String dealSpecialChar(String name) {
        name = name.replace(">", "&gt;").replace("<", "&lt;").replace(" ", "&nbsp;").replace("\"", "&quot;")
                .replace("\'", "&#39;").replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
        return name;
    }

    /**
     * 返回人员搜索json
     * 
     * @param keywords
     * @return
     */
    private String getSearchUserJSON(String keywords) {
        IF9ConnListService service = ContainerFactory.getContainInfo().getComponent(IF9ConnListService.class);
        // 接口配置
        String className = ConfigUtil.getConfigValue("OAConnListImpl");
        StringBuffer result = new StringBuffer();
        result.append("[{\"name\":\"内部通讯录\",\"code\":\"111\",\"items\":");
        result.append(getInnerSearchJSON("inner", service, keywords));
        result.append("},{\"name\":\"公共通讯录\",\"code\":\"222\",\"items\":");
        if (StringUtil.isBlank(className)) {
            result.append("[]");
        }
        else {
            service = (IF9ConnListService) ReflectUtil.getObjByClassName(className);
            result.append(getInnerSearchJSON("pub", service, keywords));
        }
        result.append("},{\"name\":\"个人通讯录\",\"code\":\"333\",\"items\":");
        if (StringUtil.isBlank(className)) {
            result.append("[]");
        }
        else {
            service = (IF9ConnListService) ReflectUtil.getObjByClassName(className);
            result.append(getInnerSearchJSON(userSession.getUserGuid(), service, keywords));
        }
        result.append("}]");
        return result.toString();
    }

    private String getInnerSearchJSON(String type, IF9ConnListService service, String keywords) {
        StringBuffer result = new StringBuffer();
        result.append("[");
        List<String[]> userList = service.getConnListByName(type, keywords);
        for (int i = 0; i < userList.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append("{\"guid\":\"");
            result.append(userList.get(i)[0]);
            result.append("\",\"name\":\"");
            result.append(userList.get(i)[1]);
            result.append("\",\"dptName\":\"");
            result.append(userList.get(i)[2]);
            result.append("\"}");
        }
        result.append("]");
        return result.toString();
    }

    private String getUserInfoJSON() {
        StringBuffer result = new StringBuffer();
        // 用户信息
        result.append("{\"userName\":\"");
        result.append(userSession.getDisplayName());
        result.append("\",\"userGuid\":\"");
        result.append(userSession.getUserGuid());

        // 部门/兼职部门 信息
        result.append("\",\"ouName\":\"");
        result.append(userSession.getOuName());

        String[] ouGuids = userservice.getRelationOuNameAndGuid(userSession.getUserGuid(), true);
        if (ouGuids != null && ouGuids.length > 0) {
            // 是否兼职
            result.append("\",\"jianzhi\":\"");
            result.append("1");
        }

        result.append("\",\"signature\":\"");
        result.append(StringUtil.isBlank(userSession.getSignature()) ? "您的个性签名" : userSession.getSignature());
        result.append("\",");

        // 全文检索地址、帮助页地址
        result.append("\"searchUrl\":\"");
        result.append(configservice.getFrameConfigValue("SearchUrl"));
        result.append("\",");
        result.append("\"helpUrl\":\"");
        String helpUrl = configservice.getFrameConfigValue("HelpUrl");
        if (StringUtil.isBlank(helpUrl)) {
            helpUrl = "docs/showcase/index/index.html";
        }
        result.append(helpUrl);
        result.append("\",");

        // 背景图、logo图、系统title

        result.append("\"title\":\"");
        result.append(configservice.getFrameConfigValue(EpointKeyNames9.SYSTEM_NAME));
        result.append("\",");
        result.append("\"bgImg\":\"");
        // 先从用户参数中获取
        String bgImg = userSession.getFrameUserConfigValue(EpointKeyNames9.USER_BG_URL);
        if (StringUtil.isBlank(bgImg)) {
            bgImg = "image/f9/classic/bg/中国风.png";
        }
        result.append(bgImg);
        result.append("\",");
        result.append("\"logoImg\":\"");
        String userlogoImage = userSession.getFrameUserConfigValue("ClassicLogoImage");
        if (StringUtil.isBlank(userlogoImage)) {
            userlogoImage = configservice.getFrameConfigValue("ClassicLogoImage");
        }
        result.append(userlogoImage);
        result.append("\",");

        // 首页名字、地址
        result.append("\"homeName\":\"");
        String homeName = userSession.getMainPageName();
        if (StringUtil.isBlank(homeName)) {
            homeName = configservice.getFrameConfigValue("MainPageName");
            if (StringUtil.isBlank(homeName)) {
                homeName = "我的桌面";
            }
        }
        result.append(homeName);

        result.append("\",");
        result.append("\"homeUrl\":\"");
        String homeUrl = userSession.getMainPageUrl();
        if (StringUtil.isBlank(homeUrl)) {
            homeUrl = configservice.getFrameConfigValue(EpointKeyNames9.MAINPAGE_URL);
            if (StringUtil.isBlank(homeUrl)) {
                // TODO 要修改 frame/fui/pages/mydesktop/mydesktop
                homeUrl = "frame/fui/pages/mydesktop/mydesktop";
            }
        }
        result.append(homeUrl);

        result.append("\"}");
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public void insertCustomModule(List<FrameModule> remainedModule, boolean isAdmin, boolean isOuAdmin,
            String baseOuGuid, String ouguid, String userGuid, String moduleCode) {
        if (StringUtil.isNotBlank(customModuleOutClass)) {// 自定义加密方式
            String[] classAndMethod = customModuleOutClass.split(":");
            String className = classAndMethod[0];
            String methodName = null;
            List<FrameModule> outList = new ArrayList<FrameModule>();
            if (classAndMethod.length > 1) {
                methodName = classAndMethod[1];
                try {
                    // 参数顺序 (Boolean isAdmin, Boolean isOuAdmin, String
                    // baseOuGuid, String ouguid, String userGuid,String
                    // moduleCode)
                    outList = (List<FrameModule>) ReflectUtil.invokeMethodHasParame(className, methodName,
                            new Object[] {isAdmin, isOuAdmin, baseOuGuid, ouguid, userGuid, moduleCode });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (outList != null && outList.size() > 0) {
                remainedModule.addAll(outList);
            }
        }
    }

    /**
     * 初始化一层菜单节点数据
     * 
     * @param moduleCode
     *            父节点的code
     * @param defaultModuleCode
     *            默认打开的code
     * @param remainedModule
     *            所有的剩余节点
     * @param top
     *            是否顶层菜单
     * @param accordingFirst
     *            是否第一层左侧tabTree
     * @param according
     *            是否左侧tabTree
     * @param accordingBigFirst
     *            是否第一层左侧tabTreebig
     * @param accordingBig
     *            是否左侧tabTreebig
     * @param bigmenu
     *            是否大菜单
     * @return json字符串
     */
    private String initOneLevelTop(String moduleCode, String defaultModuleCode, boolean top, boolean accordingFirst,
            boolean according, boolean accordingBigFirst, boolean accordingBig, boolean bigmenu,
            List<FrameModule> remainedModule) {
        StringBuffer resu = new StringBuffer("");
        // 如果moduleList为null则需要进行初始化查询
        if (remainedModule == null) {
            if (StringUtil.isBlank(moduleCode)) {
                remainedModule = moduleservice.listModuleByCodeHasRight(userSession.isAdmin(), userSession.isOuAdmin(),
                        userSession.getBaseOUGuid(), userSession.getOuGuid(), userSession.getUserGuid(),
                        userSession.getUserRoleList(), "", 3, 0, isThreeManageMode());
                insertCustomModule(remainedModule, userSession.isAdmin(), userSession.isOuAdmin(),
                        userSession.getBaseOUGuid(), userSession.getOuGuid(), userSession.getUserGuid(), "");
            }
            else {
                remainedModule = moduleservice.listModuleByCodeHasRight(userSession.isAdmin(), userSession.isOuAdmin(),
                        userSession.getBaseOUGuid(), userSession.getOuGuid(), userSession.getUserGuid(),
                        userSession.getUserRoleList(), moduleCode, 3, 0, isThreeManageMode());
                insertCustomModule(remainedModule, userSession.isAdmin(), userSession.isOuAdmin(),
                        userSession.getBaseOUGuid(), userSession.getOuGuid(), userSession.getUserGuid(), moduleCode);
                // 移除本身节点
                if (remainedModule.size() > 0) {
                    remainedModule.remove(0);
                }
            }
        }
        // 拼接json
        int length = remainedModule.size();
        if (length > 0) {
            boolean expanded = false;
            resu.append("[");
            for (int i = 0; i < length; i++) {
                FrameModule frameModule = remainedModule.get(i);
                if (frameModule.getModuleCode().length() != moduleCode.length() + 4)
                    break;
                if ((frameModule.getIsAddOu() == null || frameModule.getIsAddOu() == 0)) {
                    resu.append("{");
                    String module_name = frameModule.getModuleName();
                    module_name = module_name == null ? "" : module_name;
                    String module_url = frameModule.getModuleUrl();
                    module_url = module_url == null ? "" : module_url.split(";")[0];
                    String module_code = frameModule.getModuleCode();
                    module_code = module_code == null ? "" : module_code;
                    Boolean module_isBlank = frameModule.getIsBlank();
                    module_isBlank = module_isBlank == null ? false : module_isBlank;
                    module_url = module_url.replace("\\", "/");
                    module_url = module_url.replaceAll("=", "_SPLIT_");

                    // module_url = ConfigUtil.urlMapping(module_url);

                    // 用模块名称进行映射
                    String moduleName = l(module_name);
                    if (StringUtil.isNotBlank(moduleName)) {
                        module_name = moduleName;
                    }

                    module_url = module_url.replaceAll("_SPLIT_", "=");

                    // 为所有模块路径添加token参数，用于csrf防御 add by zjf 2015/3/25
                    // String token = userSession.getToken();
                    // module_url =
                    // StringUtil.convertUrlWithoutApplicationPath(module_url,
                    // "token=" + token);

                    resu.append("\"name\":\"" + module_name + "\",");
                    resu.append("\"code\":\"" + module_code + "\",");
                    resu.append("\"url\":\"" + module_url + "\",");

                    if (according) {
                        if (accordingFirst) {
                            // 导航树是否采用懒加载（只加载一层数据，后面的数据获取靠ajax）
                            resu.append("\"isLazy\":" + false + ",");
                            if (!expanded) {
                                // 如果指定了默认打开地址，那么要根据这个地址来决定默认打开哪个tab
                                if (StringUtil.isNotBlank(defaultModuleCode)) {
                                    if (defaultModuleCode.startsWith(frameModule.getModuleCode())) {
                                        expanded = true;
                                    }
                                }
                                else if (i == 0) {
                                    expanded = true;
                                }
                            }
                            // 手风琴是否默认展开【true:展开|false:闭合】
                            resu.append("\"expanded\":" + expanded + ",");
                            // 导航树是否默认自动全部展开【true:全部展开|false:用户自行展开】
                            resu.append("\"itemsExpanded\":" + true + ",");
                        }
                        if (StringUtil.isNotBlank(frameModule.getSmallIconAddress())) {
                            resu.append("\"icon\":\"" + frameModule.getSmallIconAddress() + "\",");
                        }
                        else {
                            resu.append("\"icon\":\"" + "" + "\",");
                        }
                    }
                    else {
                        // 大菜单只有在顶级菜单、大菜单、左侧tab大图标且第二层中出现
                        if (top || bigmenu || (accordingBig && !accordingBigFirst)) {
                            if (StringUtil.isNotBlank(frameModule.getBigIconAddress())) {
                                if (frameModule.getBigIconAddress().endsWith(".gif")) {
                                    frameModule.setBigIconAddress("modicon-1");
                                }
                                else if (frameModule.getBigIconAddress().startsWith("icons2424-")) {
                                    frameModule.setBigIconAddress(
                                            frameModule.getBigIconAddress().replace("icons2424-", "modicon-"));
                                }
                                resu.append("\"icon\":\"" + frameModule.getBigIconAddress() + "\",");
                            }
                            else {
                                resu.append("\"icon\":\"" + "modicon-1" + "\",");
                            }
                        }
                        // 小图标
                        else {
                            if (StringUtil.isNotBlank(frameModule.getSmallIconAddress())) {
                                if (frameModule.getSmallIconAddress().startsWith("modicons1414-")) {
                                    frameModule.setSmallIconAddress(
                                            frameModule.getSmallIconAddress().replace("modicons1414-", "modicon-"));
                                }
                                resu.append("\"icon\":\"" + frameModule.getSmallIconAddress() + "\",");
                            }
                            else {
                                resu.append("\"icon\":\"" + "icon-1" + "\",");
                            }
                        }
                    }

                    resu.append("\"isBlank\":" + module_isBlank + "");

                    boolean isLeaf = true;
                    // 判断是否还有子模块
                    List<FrameModule> childModuleList = new ArrayList<FrameModule>();
                    for (int j = i + 1; j < length; j++) {
                        FrameModule module = remainedModule.get(j);
                        if (module.getModuleCode().startsWith(module_code)) {
                            childModuleList.add(module);
                            if (StringUtil.isNotBlank(module_code)) {
                                remainedModule.remove(j);
                                length -= 1;
                                j--;
                            }
                            else {
                                break;
                            }
                        }
                    }

                    // 如果还有子模块，(并且不是左侧tab大图标菜单的第二层,因为这个仅允许2层),那么进行递归
                    if (!(accordingBig && !accordingBigFirst) && !bigmenu) {
                        if (StringUtil.isNotBlank(module_code)) {
                            if (StringUtils.isNotBlank(moduleCode)) {
                                String childs = "";
                                childs = initOneLevelTop(module_code, defaultModuleCode, false, false, according, false,
                                        accordingBig, bigmenu, childModuleList);
                                if (StringUtil.isNotBlank(childs)) {
                                    resu.append(",\"items\":" + childs);
                                    if (childs.length() > 10) {
                                        isLeaf = false;
                                    }
                                }
                            }
                            else {
                                // 内存判断
                                int count = childModuleList.size();
                                if (count > 0) {
                                    resu.append(",\"items\":[]");
                                    resu.append(",\"hasSub\":true");
                                    isLeaf = false;
                                }
                            }

                        }
                    }

                    resu.append(",\"isLeaf\":" + isLeaf + "");
                    resu.append("}");
                    if (i != length - 1) {
                        resu.append(",");
                    }
                }
            }
            if (resu.toString().endsWith(",")) {
                resu = new StringBuffer(resu.substring(0, resu.length() - 1));
            }
            resu.append("]");

        }
        return resu.toString();
    }
}

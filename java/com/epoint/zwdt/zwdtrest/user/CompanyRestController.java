package com.epoint.zwdt.zwdtrest.user;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.basic.auditonlineuser.auditonlineverycode.inter.IAuditOnlineVeryCode;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyRegister;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.zwdt.zwdtrest.project.api.IZjZcsspService;
import com.utils.MGUtils;

import cn.hutool.core.lang.UUID;

@RestController
@RequestMapping("/zwdtCompany")
public class CompanyRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 企业登记信息API
     */
    @Autowired
    private IAuditRsCompanyRegister iAuditRsCompanyRegister;
    /**
     * 法人基本信息API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 验证码API
     */
    @Autowired
    private IAuditOnlineVeryCode iAuditOnlineVeryCode;

    @Autowired
    private IZjZcsspService iZjZcsspService;

    /**
     * 获取企业法人用户基本信息
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getCompanyUserInfo", method = RequestMethod.POST)
    public String getCompanyUserInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyUserInfo接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取登录用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                log.info("用户实体：" + auditOnlineRegister);
                if (auditOnlineRegister != null) {
                    log.info("用户实体身份证：" + auditOnlineRegister.getIdnumber());
                    // 3、根据身份证获取用户所对应的法人信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    // 3.2、默认设置是否法人为否，法人身份是否激活为否
                    String isLegalPerson = ZwdtConstant.STRING_NO;
                    int isActivited = ZwdtConstant.INT_NO;
                    if (!auditRsCompanyBaseinfoList.isEmpty()) {
                        // 3.3、如果查询到用户有对应的法人信息，则此用户为法人
                        isLegalPerson = ZwdtConstant.STRING_YES;
                        // 3.4、遍历用户所有的法人数据，只要有一家企业的法人身份为激活状态，则法人激活状态为是已激活
                        for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                            if (ZwdtConstant.STRING_YES.equals(auditRsCompanyBaseinfo.getIsActivated())) {
                                isActivited = ZwdtConstant.INT_YES;
                                break;
                            }
                        }
                    }
                    // 4、获取用户头像
                    String profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/account/images/def_profilepic.png";
                    if (StringUtil.isNotBlank(auditOnlineRegister.getProfilePic())) {
                        profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                                + "/rest/auditattach/readAttach?attachguid=" + auditOnlineRegister.getProfilePic();
                    }
                    // 5、定义返回的JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("profilepicurl", profilePicUrl);
                    dataJson.put("clinetname", auditOnlineRegister.getUsername());
                    dataJson.put("idnum", MGUtils.sensitive(auditOnlineRegister.getIdnumber()));
                    dataJson.put("isactivited", isActivited);
                    dataJson.put("islegalperson", isLegalPerson);
                    log.info("=======结束调用getCompanyUserInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取企业用户基本信息成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCompanyUserInfo接口参数：params【" + params + "】=======");
            log.info("=======getCompanyUserInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取企业用户基本信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取用户所在企业信息接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "private/getCompanyList", method = RequestMethod.POST)
    public String getCompanyList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取用户身份证
                    String idNumber = auditOnlineRegister.getIdnumber();
                    log.info("=====用户实体：" + auditOnlineRegister + "=====");
                    // 3、 获取用户对应的法人信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq(
                            "(orgalegal_idnumber ='" + idNumber + "' or CONTACTCERTNUM='" + idNumber + "') and 1", "1");
                    sqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                    sqlConditionUtil.setOrderDesc("isactivated"); // 未激活的企业在后
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    log.info("=====鱼台县一码通企业返回列表：" + auditRsCompanyBaseinfos + "=====");
                    List<JSONObject> companyLegalList = new ArrayList<JSONObject>();
                    // 4、遍历用户对应的所有法人信息
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        // 4.1、获取各个企业的法人信息，存入返回列表中
                        JSONObject companyLegalObject = new JSONObject();
                        companyLegalObject.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                        companyLegalObject.put("companyid", auditRsCompanyBaseinfo.getCompanyid());
                        companyLegalObject.put("isactivited",
                                StringUtil.isBlank(auditRsCompanyBaseinfo.getIsActivated()) ? ZwdtConstant.STRING_NO
                                        : auditRsCompanyBaseinfo.getIsActivated());
                        // 获取企业营业执照图片
                        String profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/epspace/images/license.jpg";
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCompanypic())) {
                            profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/auditattach/readAttach?attachguid="
                                    + auditRsCompanyBaseinfo.getCompanypic();
                        }
                        companyLegalObject.put("profilepicurl", profilePicUrl);
                        if (ZwdtConstant.STRING_YES.equals(auditRsCompanyBaseinfo.getIsActivated())) {
                            // 4.2、获取该公司中管理者人数
                            SqlConditionUtil sqlConditionUtilManage = new SqlConditionUtil();
                            sqlConditionUtilManage.eq("companyid", auditRsCompanyBaseinfo.getCompanyid());
                            sqlConditionUtilManage.eq("bsqlevel", ZwdtConstant.GRANT_MANAGER);
                            sqlConditionUtilManage.isBlankOrValue("isrelieve", ZwdtConstant.STRING_NO);
                            List<AuditOnlineCompanyGrant> auditOnlineCompanyGrantsM = iAuditOnlineCompanyGrant
                                    .selectCompanyGrantByConditionMap(sqlConditionUtilManage.getMap()).getResult();
                            companyLegalObject.put("managescount", auditOnlineCompanyGrantsM.size());
                            // 4.3、获取该公司中的经办者人数
                            SqlConditionUtil sqlConditionUtilAgent = new SqlConditionUtil();
                            sqlConditionUtilAgent.eq("companyid", auditRsCompanyBaseinfo.getCompanyid());
                            sqlConditionUtilAgent.eq("bsqlevel", ZwdtConstant.GRANT_AGENT);
                            sqlConditionUtilAgent.isBlankOrValue("isrelieve", ZwdtConstant.STRING_NO);
                            List<AuditOnlineCompanyGrant> auditOnlineCompanyGrantsA = iAuditOnlineCompanyGrant
                                    .selectCompanyGrantByConditionMap(sqlConditionUtilAgent.getMap()).getResult();
                            companyLegalObject.put("agentcount", auditOnlineCompanyGrantsA.size());

                            // 4.4、获取该企业在办事项数量
                            SqlConditionUtil sqlConditionUtilProject = new SqlConditionUtil();
                            sqlConditionUtilProject.eq("applyerguid", auditRsCompanyBaseinfo.getCompanyid());
                            sqlConditionUtilProject.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_YSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG);
                            int projectCount = iAuditOnlineProject
                                    .getAllOnlineProjectCount(sqlConditionUtilProject.getMap()).getResult();
                            companyLegalObject.put("onhandletaskcount", projectCount);
                        }
                        // 4.5、添加信息至返回列表
                        companyLegalList.add(companyLegalObject);
                    }
                    // 5、获取用户所对应的管理者信息，经办人信息
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    conditionUtil.eq("bsqidnum", idNumber);
                    conditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                                                                    // 0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(conditionUtil.getMap()).getResult();
                    log.info("=====鱼台县一码通授权表返回：" + auditOnlineCompanyGrants + "=====");
                    // 5.1、定义返回管理者数据列表
                    List<JSONObject> companyManagerList = new ArrayList<JSONObject>();
                    // 5.2、定义返回经办人数据列表
                    List<JSONObject> companyAgentList = new ArrayList<JSONObject>();
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        if (ZwdtConstant.GRANT_MANAGER.equals(auditOnlineCompanyGrant.getBsqlevel())) {
                            // 5.3、如果用户的身份是管理者
                            JSONObject companyManagerObject = new JSONObject();
                            companyManagerObject.put("companyid", auditOnlineCompanyGrant.getCompanyid());
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByCompanyId(auditOnlineCompanyGrant.getCompanyid()).getResult();
                            companyManagerObject.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                            companyManagerObject.put("sqguid", auditOnlineCompanyGrant.getRowguid());
                            companyManagerList.add(companyManagerObject);
                        }
                        else if (ZwdtConstant.GRANT_AGENT.equals(auditOnlineCompanyGrant.getBsqlevel())) {
                            // 5.4、如果用户的身份是经办人
                            JSONObject companyAgentObject = new JSONObject();
                            companyAgentObject.put("companyid", auditOnlineCompanyGrant.getCompanyid());
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByCompanyId(auditOnlineCompanyGrant.getCompanyid()).getResult();
                            companyAgentObject.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                            companyAgentObject.put("sqguid", auditOnlineCompanyGrant.getRowguid());
                            companyAgentList.add(companyAgentObject);
                        }
                    }
                    // 6、返回结果数据
                    JSONObject dataJson = new JSONObject();
                    // 6.1、法人列表数据
                    dataJson.put("legalpersoncount", companyLegalList.size());
                    dataJson.put("legalpersonlist", companyLegalList);
                    // 6.2、管理者列表数据
                    dataJson.put("managerscount", companyManagerList.size());
                    dataJson.put("managerslist", companyManagerList);
                    // 6.3、经办人列表数据
                    dataJson.put("agentcount", companyAgentList.size());
                    dataJson.put("agentlist", companyAgentList);
                    log.info("=======结束调用getCompanyList接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 获取用户所在企业信息成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getCompanyList接口参数：params【" + params + "】=======");
            log.info("=======getCompanyList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", " 获取用户所在企业信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取企业基本信息接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCompanyInfo", method = RequestMethod.POST)
    public String getCompanyInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 2、获取传入参数企业标识
                String companyId = obj.getString("companyid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3.1、判断用户权限
                    String companyUserLevel = getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                            auditOnlineRegister.getIdnumber(), companyId);
                    if (StringUtil.isBlank(companyUserLevel)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 4、获取法人基本信息表内对应的企业信息
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByCompanyId(companyId)
                        .getResult();
                if (auditRsCompanyBaseinfo != null) {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname()); // 机构名称
                    dataJson.put("legalpersonname", auditRsCompanyBaseinfo.getOrganlegal()); // 法定代表人/负责人
                    dataJson.put("registeraddress", auditRsCompanyBaseinfo.getRegisteraddress()); // 注册地址
                    // 5、获取企业注册信息
                    AuditRsCompanyRegister auditRsCompanyRegister = iAuditRsCompanyRegister
                            .getAuditRsCompanyRegisterByCompanyid(companyId).getResult();
                    // 手机号中间四位隐藏
                    String mobile = "";
                    // 增加如果auditRsCompanyRegister 为空的处理
                    if (auditRsCompanyRegister != null) {
                        if (StringUtil.isNotBlank(auditRsCompanyRegister.getContactphone())) {
                            mobile = auditRsCompanyRegister.getContactphone().substring(0, 3) + "****"
                                    + auditRsCompanyRegister.getContactphone().substring(7);
                        }
                        dataJson.put("businessscope", StringUtil.isBlank(auditRsCompanyRegister.getBusinessscope()) ? ""
                                : auditRsCompanyRegister.getBusinessscope()); // 经营范围
                    }
                    dataJson.put("contactmobile", mobile); // 联系电话
                    dataJson.put("creditcode", auditRsCompanyBaseinfo.getCreditcode()); // 统一社会信用代码
                    dataJson.put("organcode", auditRsCompanyBaseinfo.getOrgancode()); // 组织机构代码证
                    // 获取企业头像
                    String profilePicUrl = "";
                    String urlRoot = WebUtil.getRequestCompleteUrl(request);
                    if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCompanypic())) {
                        profilePicUrl = urlRoot + "/rest/auditattach/readAttach?attachguid="
                                + auditRsCompanyBaseinfo.getCompanypic();
                    }
                    else {
                        profilePicUrl = urlRoot + "/epointzwmhwz/pages/epspace/images/license.jpg";
                    }
                    dataJson.put("profilepicurl", profilePicUrl);
                    log.info("=======结束调用getCompanyInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取企业基本信息成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取企业信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getCompanyInfo接口参数：params【" + params + "】=======");
            log.info("=======getCompanyInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取企业基本信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 激活法人身份接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/activeLegalStatus", method = RequestMethod.POST)
    public String activeLegalStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用activeLegalStatus接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 2、获取传入参数公司id
                String companyId = obj.getString("companyid");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、获取用户在企业中的身份
                    String userLevel = this.getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                            auditOnlineRegister.getIdnumber(), companyId);
                    if (ZwdtConstant.GRANT_LEGEL_PERSION.equals(userLevel)) {
                        // 4、更新激活状态
                        iAuditRsCompanyBaseinfo.activelegalperson(companyId).getResult();
                        log.info("=======结束调用activeLegalStatus接口=======");
                        return JsonUtils.zwdtRestReturn("1", "法人身份激活成功！", "");
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "用户非此企业的法人！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======activeLegalStatus接口参数：params【" + params + "】=======");
            log.info("=======activeLegalStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "激活法人身份接口异常：" + e.getMessage(), "");
        }
    }

    /**
     * 激活管理者身份接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/activeManageStatus", method = RequestMethod.POST)
    public String activeManageStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用activeManageStatus接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取企业id
                String companyId = obj.getString("companyid");
                // 1.2、获取激活码
                String code = obj.getString("code");
                // 1.3、获取手机号码
                String mobile = obj.getString("mobile");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、先判断此用户是否有该企业的管理者权限
                    String userLevel = this.getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                            auditOnlineRegister.getIdnumber(), companyId);
                    if (!ZwdtConstant.GRANT_MANAGER.equals(userLevel)) {
                        return JsonUtils.zwdtRestReturn("0", "未找到对应管理者！", "");
                    }
                    // 3、从redis或者数据库中获取激活码
                    AuditOnlineVerycode auditOnlineVerycode = null;
                    String clientIdentifier = "";
                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                        // 3.1、如果系统使用了redis，则从redis中获取激活码
                        ZwfwRedisCacheUtil redisCacheUtil = new ZwfwRedisCacheUtil(false);
                        auditOnlineVerycode = redisCacheUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, mobile,
                                ZwdtConstant.VERIFYKIND_MACTIVATE);
                        redisCacheUtil.close();
                    }
                    else {
                        // 3.2、如果系统未使用redis，则从数据库中获取激活码
                        auditOnlineVerycode = iAuditOnlineVeryCode.getVerifyCode(mobile,
                                EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), "0",
                                ZwdtConstant.VERIFYKIND_MACTIVATE).getResult();
                    }
                    // 4、判断激活码是否正确
                    if (auditOnlineVerycode == null) {
                        // 4.1、激活码不存在或已过期
                        return JsonUtils.zwdtRestReturn("0", "激活码不存在或者已经过期", "");
                    }
                    else {
                        // 4.2、激活码错误
                        if (!code.equals(auditOnlineVerycode.getVerifycode())) {
                            return JsonUtils.zwdtRestReturn("0", "激活码错误！", "");
                        }
                        // 取出业务guid
                        if (StringUtil.isNotBlank(auditOnlineVerycode.getClientidentifier())) {
                            clientIdentifier = auditOnlineVerycode.getClientidentifier();
                        }
                        // 4.3、验证通过后删除激活码
                        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                            ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                            redisUtil.delByHashzwdtvcode(AuditOnlineVerycode.class, mobile,
                                    ZwdtConstant.VERIFYKIND_MACTIVATE);
                            redisUtil.close();
                        }
                        else {
                            iAuditOnlineVeryCode.deleteAuditOnlineVerycode(auditOnlineVerycode);
                        }
                    }
                    if (companyId.equals(clientIdentifier)) {
                        // 5、获取该用所对应的管理者授权信息
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                        sqlConditionUtil.eq("companyid", companyId);
                        sqlConditionUtil.eq("bsqlevel", ZwdtConstant.GRANT_MANAGER);
                        sqlConditionUtil.isBlankOrValue("isrelieve", "0");
                        List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                                .selectCompanyGrantByConditionMap(sqlConditionUtil.getMap()).getResult();
                        // 6、目前默认法人对管理者的授权信息仅为1条
                        if (auditOnlineCompanyGrants != null && auditOnlineCompanyGrants.size() > 0) {
                            // 7、更新激活状态
                            AuditOnlineCompanyGrant auditOnlineCompanyGrant = auditOnlineCompanyGrants.get(0);
                            auditOnlineCompanyGrant.setM_IsActive(ZwdtConstant.STRING_YES);
                            iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(auditOnlineCompanyGrant);
                            log.info("=======结束调用activeManageStatus接口=======");
                            return JsonUtils.zwdtRestReturn("1", "激活管理者身份成功！", "");
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "未找到对应管理者！", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "请重新获取激活码！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======activeManageStatus接口参数：params【" + params + "】=======");
            log.info("=======activeManageStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "激活管理者身份异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查管理者是否激活
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/checkManagerIsActive", method = RequestMethod.POST)
    public String checkManagerIsActive(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用checkManagerIsActive接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 2、获取传入参数公司id
                String companyId = obj.getString("companyid");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、获取该用所对应的管理者授权信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.eq("companyid", companyId);
                    sqlConditionUtil.eq("bsqlevel", ZwdtConstant.GRANT_MANAGER);
                    sqlConditionUtil.isBlankOrValue("isrelieve", "0");
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(sqlConditionUtil.getMap()).getResult();
                    if (auditOnlineCompanyGrants != null && auditOnlineCompanyGrants.size() > 0) {
                        // 4、获取激活状态（ 1:已激活 0:未激活）
                        AuditOnlineCompanyGrant auditOnlineCompanyGrant = auditOnlineCompanyGrants.get(0);
                        String misactive = auditOnlineCompanyGrant.getM_IsActive();
                        // 5、定义返回JSON对象
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("m_isactive", ZwdtConstant.STRING_YES.equals(misactive) ? "1" : "0");
                        log.info("=======结束调用checkManagerIsActive接口=======");
                        return JsonUtils.zwdtRestReturn("1", "激活管理者身份成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "用户非此企业的管理者！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======checkManagerIsActive接口参数：params【" + params + "】=======");
            log.info("=======checkManagerIsActive异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断管理者是否激活异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断用户角色接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/checkUserRoleType", method = RequestMethod.POST)
    public String checkUserRoleType(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用checkUserRoleType接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、获取用户身份（法人、管理者、经办者数量）
                    SqlConditionUtil sqlConditionUtilLegal = new SqlConditionUtil();
                    sqlConditionUtilLegal.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtilLegal.isNotBlank("orgalegal_idnumber");
                    sqlConditionUtilLegal.isBlankOrValue("is_history", "0");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtilLegal.getMap()).getResult();
                    int legalCount = auditRsCompanyBaseinfos.size(); // 法人数量
                    int managerCount = 0; // 管理者数量
                    int agentCount = 0; // 代办人数量
                    // 3.2、获取用户所对应的管理者信息，经办人信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("isrelieve", "0"); // 非解除授权
                    // 3.3、获取此用户作为管理者、经办人的信息
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(sqlConditionUtil.getMap()).getResult();
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        if (ZwdtConstant.GRANT_MANAGER.equals(auditOnlineCompanyGrant.getBsqlevel())) {
                            // 3.4、 添加管理者数据
                            managerCount += 1;
                        }
                        else if (ZwdtConstant.GRANT_AGENT.equals(auditOnlineCompanyGrant.getBsqlevel())
                                || "1".equals(auditOnlineCompanyGrant.getBsqlevel())) {
                            // 3.5、添加经办人数据
                            agentCount += 1;
                        }
                    }
                    // 4、如果此用户只是为一家企业的法人，则需要额外判断当前企业是否激活;0:未激活、1：已激活
                    String isActive = "0";
                    if (legalCount == 1 && managerCount == 0 && agentCount == 0) {
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfos.get(0).getIsActivated())) {
                            isActive = auditRsCompanyBaseinfos.get(0).getIsActivated();
                        }
                    }
                    // 4.1、如果此用户为多家企业的法人
                    boolean flag = false;
                    if (legalCount > 1 && managerCount == 0 && agentCount == 0) {
                        for (int i = 0; i < auditRsCompanyBaseinfos.size(); i++) {
                            if (StringUtil.isNotBlank(auditRsCompanyBaseinfos.get(i).getIsActivated())) {
                                isActive = auditRsCompanyBaseinfos.get(i).getIsActivated();
                                if (ZwdtConstant.STRING_YES.equals(isActive)) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                    // 4.2、如果此用户为多家企业的管理者
                    if (legalCount == 0 && managerCount > 1 && agentCount == 0) {
                        for (int i = 0; i < auditOnlineCompanyGrants.size(); i++) {
                            if (StringUtil.isNotBlank(auditOnlineCompanyGrants.get(i).getM_IsActive())) {
                                isActive = auditOnlineCompanyGrants.get(i).getM_IsActive();
                                if (ZwdtConstant.STRING_YES.equals(isActive)) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                    // 4.3、如果此用户为多家企业的法人和管理者
                    if (legalCount > 0 && managerCount > 0 && agentCount == 0) {
                        for (int i = 0; i < auditRsCompanyBaseinfos.size(); i++) {
                            if (StringUtil.isNotBlank(auditRsCompanyBaseinfos.get(i).getIsActivated())) {
                                isActive = auditRsCompanyBaseinfos.get(i).getIsActivated();
                                if (ZwdtConstant.STRING_YES.equals(isActive)) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if (!flag) {
                            for (int i = 0; i < auditOnlineCompanyGrants.size(); i++) {
                                if (StringUtil.isNotBlank(auditOnlineCompanyGrants.get(i).getM_IsActive())) {
                                    isActive = auditOnlineCompanyGrants.get(i).getM_IsActive();
                                    if (ZwdtConstant.STRING_YES.equals(isActive)) {
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    // 4.4、如果该用户是多种角色且含有代办人身份
                    if ((legalCount > 0 && managerCount == 0 && agentCount > 0)
                            || (legalCount == 0 && managerCount > 0 && agentCount > 0)
                            || (legalCount > 0 && managerCount > 0 && agentCount > 0)) {
                        flag = true;
                    }
                    // 5、判断用户角色
                    int type = 0; // 既不是企业法人也不是企业管理者和代办人
                    int totalUserRoleCount = legalCount + managerCount + agentCount;
                    String companyId = "";
                    if (totalUserRoleCount > 1) {
                        if (flag == false) {
                            // 用户是法人身份，企业空间都未激活
                            type = 6;
                        }
                        else {
                            // 5.1、用户是法人身份，拥有多家企业，用户是法人身份、企业管理者、企业经办者中的多个
                            type = 1;
                        }
                        if (agentCount > 1) {
                            // 用户是个人，经办多家企业
                            type = 1;
                        }
                    }
                    else if (legalCount == 1 && ZwdtConstant.STRING_NO.equals(isActive)) {
                        // 5.2、用户是法人身份，只拥有一家企业,且未激活
                        type = 2;
                        companyId = auditRsCompanyBaseinfos.get(0).getCompanyid();
                    }
                    else if (legalCount == 1 && ZwdtConstant.STRING_YES.equals(isActive)) {
                        // 5.3、用户是法人身份，只拥有一家企业,已激活
                        type = 3;
                        companyId = auditRsCompanyBaseinfos.get(0).getCompanyid();
                    }
                    else if (managerCount == 1) {
                        // 5.4、用户是一家企业的管理者
                        type = 4;
                        AuditOnlineCompanyGrant auditOnlineCompanyGrant = auditOnlineCompanyGrants.get(0);
                        companyId = auditOnlineCompanyGrant.getCompanyid();
                        if (ZwdtConstant.STRING_YES.equals(auditOnlineCompanyGrant.getM_IsActive())) {
                            type = 7;
                        }
                    }
                    else if (agentCount == 1) {
                        // 5.5、用户是一家企业的企业经办者
                        type = 5;
                        companyId = auditOnlineCompanyGrants.get(0).getCompanyid();
                    }
                    // 6、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("type", type);
                    if (totalUserRoleCount == 1) {
                        dataJson.put("companyid", companyId);
                    }
                    log.info("=======结束调用checkUserRoleType接口=======");
                    return JsonUtils.zwdtRestReturn("1", "判断用户角色接口成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======checkUserRoleType接口参数：params【" + params + "】=======");
            log.info("=======checkUserRoleType异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断用户角色接口异常：" + e.getMessage(), "");
        }
    }

    /**
     * 上传企业头像
     *
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/private/uploadCompanyPic", method = RequestMethod.POST)
    public String uploadProfilePic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用uploadCompanyPic接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JsonUtils.checkUserAuth(token);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、企业头像标识
            String attachGuid = obj.getString("attachguid");
            // 1.2、公司标识
            String companyid = obj.getString("companyid");
            // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            // 2、获取用户注册信息
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (auditOnlineRegister != null) {
                // 2.1、判断用户权限
                String companyUserLevel = getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                        auditOnlineRegister.getIdnumber(), companyid);
                if (StringUtil.isBlank(companyUserLevel)) {
                    return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
            // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            // 3、获取企业信息
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByCompanyId(companyid)
                    .getResult();
            if (auditRsCompanyBaseinfo != null) {
                auditRsCompanyBaseinfo.setCompanypic(attachGuid);
                iAuditRsCompanyBaseinfo.updateAuditRsCompanyBaseinfo(auditRsCompanyBaseinfo);
                return JsonUtils.zwdtRestReturn("1", "保存头像标识成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取企业信息失败", "");
            }
        }
        catch (Exception e) {
            log.info("=======uploadCompanyPic接口参数：params【" + params + "】=======");
            log.info("=======uploadCompanyPic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存头像标识失败：" + e.getMessage(), "");
        }
    }

    /**
     * 判断用户在某企业中的身份(用于企业办件中心头部展示)
     *
     * @param accountGuid
     *            用户标识
     * @param idnum
     *            用户身份证
     * @param companyId
     *            公司标识
     * @return
     */
    @RequestMapping(value = "/private/getCompanyUserInfoLevel", method = RequestMethod.POST)
    public String getCompanyUserInfoLevel(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyUserInfoLevel接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String userLevel = "";
                // 1、企业标识
                String companyid = obj.getString("companyid");
                // 2、 获取个人信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                userLevel = getCompanyUserLevel(auditOnlineRegister.getAccountguid(), auditOnlineRegister.getIdnumber(),
                        companyid);
                JSONObject dataJson = new JSONObject();
                dataJson.put("userlevel", userLevel);
                return JsonUtils.zwdtRestReturn("1", "获取人员类别成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (

        Exception e) {
            log.info("=======getCompanyUserInfoLevel接口参数：params【" + params + "】=======");
            log.info("=======getCompanyUserInfoLevel异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户在某企业中的身份失败：" + e.getMessage(), "");
        }
    }

    /**
     * 新增用户企业信息记录表
     *
     * @param accountGuid
     *            用户标识
     * @param idnum
     *            用户身份证
     * @param companyId
     *            公司标识
     * @return
     */
    @RequestMapping(value = "/addEnterpriseCompany", method = RequestMethod.POST)
    public String addEnterpriseCompany(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用addEnterpriseCompany接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、企业标识
                String companyid = obj.getString("companyid");
                // 2、 获取个人信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "用户信息未获取到！", "");
                }
                // 1、根据用户身份证和公司标识获取企业基本信息
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByCompanyId(companyid)
                        .getResult();

                if (auditRsCompanyBaseinfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "企业信息未获取到！", "");
                }

                String accountguid = auditOnlineRegister.getAccountguid();
                String creditcode = auditRsCompanyBaseinfo.getCreditcode();

                auditonlinechooseenterprise auditterprise = iZjZcsspService.getTerpriseByRowguid(accountguid);
                if (auditterprise == null) {
                    auditonlinechooseenterprise terprise = new auditonlinechooseenterprise();
                    terprise.setRowguid(UUID.randomUUID().toString());
                    terprise.setCompanyid(companyid);
                    terprise.setCreditcode(creditcode);
                    terprise.setAccountguid(accountguid);
                    terprise.setCreat_date(new Date());
                    iZjZcsspService.insert(terprise);
                }
                else {
                    auditterprise.setCompanyid(companyid);
                    auditterprise.setCreditcode(creditcode);
                    auditterprise.setAccountguid(accountguid);
                    auditterprise.setUpdate_date(new Date());
                    iZjZcsspService.update(auditterprise);
                }

                JSONObject dataJson = new JSONObject();

                dataJson.put("creditcode", auditRsCompanyBaseinfo.getCreditcode());
                dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());

                return JsonUtils.zwdtRestReturn("1", "绑定企业信息成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (

        Exception e) {
            log.info("=======getCompanyUserInfoLevel接口参数：params【" + params + "】=======");
            log.info("=======getCompanyUserInfoLevel异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户在某企业中的身份失败：" + e.getMessage(), "");
        }
    }

    /**
     * 新增用户企业信息记录表
     *
     * @param accountGuid
     *            用户标识
     * @param idnum
     *            用户身份证
     * @param companyId
     *            公司标识
     * @return
     */
    @RequestMapping(value = "/getEnterpriseCompanyDetail", method = RequestMethod.POST)
    public String getEnterpriseCompanyDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getEnterpriseCompanyDetail接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                // 2、 获取个人信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "用户信息未获取到！", "");
                }

                String accountguid = auditOnlineRegister.getAccountguid();

                auditonlinechooseenterprise auditterprise = iZjZcsspService.getTerpriseByRowguid(accountguid);

                JSONObject dataJson = new JSONObject();
                if (auditterprise == null) {
                    return JsonUtils.zwdtRestReturn("0", "未获取到绑定的企业信息！", "");
                }
                else {
                    dataJson.put("username", auditOnlineRegister.getUsername());
                    dataJson.put("idnumber", auditOnlineRegister.getIdnumber());
                    dataJson.put("companyid", auditterprise.getCompanyid());
                    // 1、根据用户身份证和公司标识获取企业基本信息
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                            .getCompanyByCompanyId(auditterprise.getCompanyid()).getResult();
                    if (auditRsCompanyBaseinfo != null) {
                        dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                    }

                    dataJson.put("creditcode", auditterprise.getCreditcode());
                    return JsonUtils.zwdtRestReturn("1", "获取企业信息成功！", dataJson.toString());
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (

        Exception e) {
            log.info("=======getEnterpriseCompanyDetail接口参数：params【" + params + "】=======");
            log.info("=======getEnterpriseCompanyDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户在某企业中的身份失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取用户唯一标识
     *
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 判断用户在某企业中的身份
     *
     * @param accountGuid
     *            用户标识
     * @param idnum
     *            用户身份证
     * @param companyId
     *            公司标识
     * @return
     */
    private String getCompanyUserLevel(String accountGuid, String idnum, String companyId) {
        String userLevel = "";
        // 1、根据用户身份证和公司标识获取企业基本信息
        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                .getCompanyByCompanyIdAndIdnum(companyId, idnum).getResult();
        if (auditRsCompanyBaseinfo != null) {
            // 2、如果个人有对应的企业信息，则为法人
            userLevel = ZwdtConstant.GRANT_LEGEL_PERSION;
        }
        else {
            // 3、获取用户授权基本信息
            AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                    .getGrantByBsqUserGuidAndCompanyId(companyId, accountGuid).getResult();
            if (auditOnlineCompanyGrant != null) {
                userLevel = auditOnlineCompanyGrant.getBsqlevel();
            }
        }
        return userLevel;
    }

}

package com.epoint.dhlogin.process;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.factory.ContextBeanFactory;
import com.epoint.authenticator.identity.AuthenticationMapInfo;
import com.epoint.authenticator.identity.Identity;
import com.epoint.authenticator.loginflow.userservice.NamedUserService;
import com.epoint.authenticator.mgt.LoginInternalInterface;
import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlinecompany.inter.IAuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.basic.auditonlineuser.auditonlineverycode.inter.IAuditOnlineVeryCode;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyRegister;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.intermediary.sendmaterials.service.SendMaterialsService;
import com.epoint.zwdt.util.TARequestUtil;

/**
 * 框架用户数据获取业务流程
 * 
 * @author sjw
 * @version [版本号, 2016年12月26日]
 
 
 */
public class TAZwdtUserService extends NamedUserService
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuthenticationInfo getAuthenticatingInfo(Identity identity) throws AuthenticationException {

        // 如果用户自定义了数据源，则直接返回，不会执行后面的框架逻辑
        LoginInternalInterface login = ContextBeanFactory.getInstance().getLoginInternalInterface();
        if (login != null) {
            AuthenticationInfo authenticationInfo = login.getAuthenticatingInfo(identity);
            if (authenticationInfo != null) {
                return authenticationInfo;
            }
        }
        // 可能是 手机号码，身份证号码，二维码标识，第三方guid标识
        String loginid = identity.getPrincipal().toString();
        // 如果没有传登录方式，默认为用户名密码登录
        String loadType = identity.getLoadType().toString();
        AuditOnlineRegister zwdtthirdregister = null;
        // 可能是普通登录方式的密码 /手机动态密码 /扫码登录的用jessionid/第三方区分标准，wx,qq,wb
        String password = identity.getCredentials().toString();
        // 区分登录方式
        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineRegister.class);
        IAuditOnlineIndividual iAuditOnlineIndividual = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineIndividual.class);
        if (ZwdtConstant.HtirdType.equals(loadType)) {
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            if (ZwdtConstant.HtirdType_WX.equals(password)) {
                // 微信登录
                conditionsql.eq("WXID", loginid);
            }
            else if (ZwdtConstant.HtirdType_QQ.equals(password)) {
                // 扣扣登录
                conditionsql.eq("QQID", loginid);
            }
            else if (ZwdtConstant.HtirdType_WB.equals(password)) {
                // 微博登录
                conditionsql.eq("WBID", loginid);
            }
            List<AuditOnlineRegister> list = iAuditOnlineRegister.selectAuditOnlineRegisterList(conditionsql.getMap())
                    .getResult();
            if (list != null && list.size() > 0 && StringUtil.isNotBlank(list.get(0).getLoginid())) {
                // 用户存在,并且信息完善，可以 正常登录
                zwdtthirdregister = list.get(0);

            }
            else {
                // 保存用户信息，引导用户去完善信息
                AuditOnlineRegister auditOnlineRegister = new AuditOnlineRegister();
                if (ZwdtConstant.HtirdType_WX.equals(password)) {
                    auditOnlineRegister.setWxid(loginid);
                }
                else if (ZwdtConstant.HtirdType_QQ.equals(password)) {
                    auditOnlineRegister.setQqid(loginid);
                }
                else if (ZwdtConstant.HtirdType_WB.equals(password)) {
                    auditOnlineRegister.setWbid(loginid);
                }
                auditOnlineRegister.setRowguid(UUID.randomUUID().toString());
                iAuditOnlineRegister.addRegister(auditOnlineRegister);
                zwdtthirdregister = auditOnlineRegister;
                // throw new UnknownAccountException("注册成功，请您完善资料！");
            }

        }
        else if (ZwdtConstant.LoginIdAndPassWord.equals(loadType)) {
            // 正常用户名密码登录,手机号码/身份证号码+密码
            // 通过账号去查找用户
            zwdtthirdregister = iAuditOnlineRegister.getRegisterByIdorMobile(loginid).getResult();
        }
        else if (ZwdtConstant.LoginIdAndPhoneCode.equals(loadType)) {
            // 手机号码+动态密码 
            // 大厅登录初始化register
            zwdtthirdregister = iAuditOnlineRegister.getRegisterByMobile(loginid).getResult();
            if (zwdtthirdregister == null) {
                throw new UnknownAccountException("未能获取到用户,请先注册！");
            }
            else {
                AuditOnlineVerycode onlineVerycode = null;
                IAuditOnlineVeryCode iAuditOnlineVeryCode = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineVeryCode.class);
                //判断是否有redis
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 去redis中验证动态密码
                	ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                    onlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, loginid, "4");
                    redisUtil.close();
                }
                else {
                    // 去库里验证动态密码
                    onlineVerycode = iAuditOnlineVeryCode.getVerifyCode(loginid,
                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), "0", "4")
                            .getResult();
                }
                if (onlineVerycode == null) {
                    throw new UnknownAccountException("验证码错误或者已经过期！");
                }
                else {
                }
            }

        }
        else if (ZwdtConstant.LoginByScanType.equals(loadType)) {

            // 扫描登录
            // 大厅登录初始化register
            if (StringUtil.isBlank(loginid)) {
                throw new UnknownAccountException("未能获取到您的信息，请对准二维码重新扫描！");
            }
            AuditOnlineVerycode onlineVerycode = null;
            IAuditOnlineVeryCode iAuditOnlineVeryCode = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineVeryCode.class);
            //判断是否有redis
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                // 去redis中验证扫描信息
            	ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                onlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, loginid, "3");
                redisUtil.close();
            }
            else {
                // 当accountguid不为空，并且Isverified为1 说明已经扫描验证通过
                onlineVerycode = iAuditOnlineVeryCode.getScanVerifyCode(
                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), loginid, "1",
                        "3").getResult();
            }
            if (onlineVerycode == null) {
                throw new UnknownAccountException("扫描登录验证码已经过期 ！");
            }
            else {
                //进行验证
                if (!"1".equals(onlineVerycode.getIsverified())) {
                    // 不通过
                    throw new UnknownAccountException("扫描登录验证错误！");
                }
            }
            zwdtthirdregister = iAuditOnlineRegister.getRegisterByAccountguid(onlineVerycode.getAccountguid())
                    .getResult();
            if (zwdtthirdregister == null) {
                throw new UnknownAccountException("未能获取到用户,请先注册！");
            }

            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                // 删除扫描验证码
                ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                redisUtil.delByHashzwdtvcode(AuditOnlineVerycode.class, loginid, "3");
                redisUtil.close();
            }
            else {
                iAuditOnlineVeryCode.deleteAuditOnlineVerycode(onlineVerycode);
            }
        }
        else if (ZwdtConstant.Dhlogin.equals(loadType)) {
            IAuditOnlineCompany iAuditOnlineCompany = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineCompany.class);

            //ISendMaterials iSendService = ContainerFactory.getContainInfo().getComponent(ISendMaterials.class);

            SendMaterialsService sendService = new SendMaterialsService();

            // 大汉单点登录
            // 大厅登录初始化register
            if (StringUtil.isBlank(loginid)) {
                throw new UnknownAccountException("未能获取到您的用户名！");
            }

            JSONObject sumbit = null;

            // 通过账号去查找用户
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("loginid", loginid);

            List<AuditOnlineRegister> list = iAuditOnlineRegister.selectAuditOnlineRegisterList(conditionsql.getMap())
                    .getResult();
            if (list != null && list.size() > 0 && StringUtil.isNotBlank(list.get(0).getLoginid())) {
                // 用户存在 正常登录
                zwdtthirdregister = list.get(0);
                if (zwdtthirdregister == null) {
                    throw new UnknownAccountException("未能获取到用户信息！");
                }
                else {//更新用户信息

                    zwdtthirdregister.setLastlogindate(new Date());
                    zwdtthirdregister.setUsername(identity.get("name").toString());
                    zwdtthirdregister.setMobile(identity.get("mobile").toString());
                    zwdtthirdregister.setIdnumber(identity.get("cardid").toString());
                    zwdtthirdregister.setLoginid(identity.get("loginname").toString());
                    // 统一社会信用代码不为空说明用户是法人
                    if (StringUtil.isNotBlank(identity.get("creditcode").toString())) {
                        zwdtthirdregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_COMPANY);
                        // 法人信息
                        AuditOnlineCompany company = iAuditOnlineCompany
                                .getAuditOnlineCompanyByAccountGuid(zwdtthirdregister.getAccountguid()).getResult();
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = null;
                        IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                                .getComponent(IAuditRsCompanyBaseinfo.class);
                        IAuditRsCompanyLegal iAuditRsCompanyLegal = ContainerFactory.getContainInfo()
                                .getComponent(IAuditRsCompanyLegal.class);
                        IAuditRsCompanyRegister iAuditRsCompanyRegister = ContainerFactory.getContainInfo()
                                .getComponent(IAuditRsCompanyRegister.class);
                        if (company != null) {
                            company.setCreditcode(identity.get("creditcode").toString());
                            company.setOrganname(identity.get("qyname").toString());
                            company.setOperatedate(new Date());
                            //iAuditOnlineCompany.updateAuditOnlineCompany(company);
                            sendService.insertCompanyProcess(company);
                            // 工建项目
                            log.info("工建项目法人企业单点登录开始开始");
                            auditRsCompanyBaseinfo = sendService.getAuditRsCompanyBaseinfo(company.getCreditcode());
                            if (auditRsCompanyBaseinfo != null) {
                                auditRsCompanyBaseinfo.setCreditcode(company.getCreditcode());
                                auditRsCompanyBaseinfo.setOrganname(company.getOrganname());
                                auditRsCompanyBaseinfo.setOrgantype("企业法人");
                                auditRsCompanyBaseinfo.setOrganlegal(zwdtthirdregister.getUsername());
                                auditRsCompanyBaseinfo.setOrgalegal_idnumber(zwdtthirdregister.getIdnumber());
                                auditRsCompanyBaseinfo.setVersiontime(new Date());
                                auditRsCompanyBaseinfo.setVersion(1);
                                auditRsCompanyBaseinfo.setIs_history("0");
                                iAuditRsCompanyBaseinfo.updateAuditRsCompanyBaseinfo(auditRsCompanyBaseinfo);
                                AuditRsCompanyLegal legal = iAuditRsCompanyLegal
                                        .getAuditRsCompanyLegalByCompanyid(auditRsCompanyBaseinfo.getCompanyid())
                                        .getResult();
                                legal.setCreditcode(company.getCreditcode());
                                iAuditRsCompanyLegal.updateAuditRsCompanyLegal(legal);
                                AuditRsCompanyRegister companyRegister = iAuditRsCompanyRegister
                                        .getAuditRsCompanyRegisterByCompanyid(auditRsCompanyBaseinfo.getCompanyid())
                                        .getResult();
                                companyRegister.setCreditcode(company.getCreditcode());
                                iAuditRsCompanyRegister.updateAuditRsCompanyRegister(companyRegister);
                                iAuditRsCompanyBaseinfo.activelegalperson(auditRsCompanyBaseinfo.getCompanyid())
                                        .getResult();
                            }
                            else {
                                AuditRsCompanyBaseinfo auditRsCompanyBaseinfonew = new AuditRsCompanyBaseinfo();
                                auditRsCompanyBaseinfonew.setRowguid(UUID.randomUUID().toString());
                                auditRsCompanyBaseinfonew.setCreditcode(company.getCreditcode());
                                auditRsCompanyBaseinfonew.setOrganname(company.getOrganname());
                                auditRsCompanyBaseinfonew.setOrgantype("企业法人");
                                auditRsCompanyBaseinfonew.setOrganlegal(zwdtthirdregister.getUsername());
                                auditRsCompanyBaseinfonew.setOrgalegal_idnumber(zwdtthirdregister.getIdnumber());
                                auditRsCompanyBaseinfonew.setVersiontime(new Date());
                                auditRsCompanyBaseinfonew.setVersion(1);
                                auditRsCompanyBaseinfonew.setIs_history("0");
                                auditRsCompanyBaseinfonew.setCompanyid(UUID.randomUUID().toString());
                                iAuditRsCompanyBaseinfo.addAuditRsCompanyBaseinfo(auditRsCompanyBaseinfonew);
                                AuditRsCompanyLegal legal = new AuditRsCompanyLegal();
                                legal.setRowguid(UUID.randomUUID().toString());
                                legal.setCreditcode(company.getCreditcode());
                                legal.setVersiontime(new Date());
                                legal.setVersion(1);
                                legal.setIs_history("0");
                                legal.setCompanyid(auditRsCompanyBaseinfonew.getCompanyid());
                                iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
                                AuditRsCompanyRegister companyRegister = new AuditRsCompanyRegister();
                                companyRegister.setRowguid(UUID.randomUUID().toString());
                                companyRegister.setCreditcode(company.getCreditcode());
                                companyRegister.setContactphone(zwdtthirdregister.getMobile());
                                companyRegister.setVersiontime(new Date());
                                companyRegister.setVersion(1);
                                companyRegister.setIs_history("0");
                                companyRegister.setCompanyid(auditRsCompanyBaseinfonew.getCompanyid());
                                iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
                                iAuditRsCompanyBaseinfo.activelegalperson(auditRsCompanyBaseinfonew.getCompanyid())
                                        .getResult();
                            }
                            log.info("工建项目法人企业单点登录结束");
                            //同步内网
                            TARequestUtil.sendPost(ConfigUtil.getConfigValue("taconfig", "intermediary")
                                    + "rest/innerattach/insertcompany", JSON.toJSONString(company), "", "");
                        }
                        else {
                            company = new AuditOnlineCompany();
                            company.setRowguid(UUID.randomUUID().toString());
                            company.setAccountguid(zwdtthirdregister.getAccountguid());
                            company.setCreditcode(identity.get("creditcode").toString());
                            company.setOrganname(identity.get("qyname").toString());
                            company.setOperatedate(new Date());
                            //iAuditOnlineCompany.addAuditOnlineCompany(company);
                            sendService.insertCompanyProcess(company);
                            log.info("工建项目法人企业单点登录开始");
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfonew = new AuditRsCompanyBaseinfo();
                            auditRsCompanyBaseinfonew.setRowguid(UUID.randomUUID().toString());
                            auditRsCompanyBaseinfonew.setCreditcode(company.getCreditcode());
                            auditRsCompanyBaseinfonew.setOrganname(company.getOrganname());
                            auditRsCompanyBaseinfonew.setOrgantype("企业法人");
                            auditRsCompanyBaseinfonew.setOrganlegal(zwdtthirdregister.getUsername());
                            auditRsCompanyBaseinfonew.setOrgalegal_idnumber(zwdtthirdregister.getIdnumber());
                            auditRsCompanyBaseinfonew.setVersiontime(new Date());
                            auditRsCompanyBaseinfonew.setVersion(1);
                            auditRsCompanyBaseinfonew.setIs_history("0");
                            auditRsCompanyBaseinfonew.setCompanyid(UUID.randomUUID().toString());
                            iAuditRsCompanyBaseinfo.addAuditRsCompanyBaseinfo(auditRsCompanyBaseinfonew);
                            AuditRsCompanyLegal legal = new AuditRsCompanyLegal();
                            legal.setRowguid(UUID.randomUUID().toString());
                            legal.setCreditcode(company.getCreditcode());
                            legal.setVersiontime(new Date());
                            legal.setVersion(1);
                            legal.setIs_history("0");
                            legal.setCompanyid(auditRsCompanyBaseinfonew.getCompanyid());
                            iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
                            AuditRsCompanyRegister companyRegister = new AuditRsCompanyRegister();
                            companyRegister.setRowguid(UUID.randomUUID().toString());
                            companyRegister.setCreditcode(company.getCreditcode());
                            companyRegister.setContactphone(zwdtthirdregister.getMobile());
                            companyRegister.setVersiontime(new Date());
                            companyRegister.setVersion(1);
                            companyRegister.setIs_history("0");
                            companyRegister.setCompanyid(auditRsCompanyBaseinfonew.getCompanyid());
                            iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
                            iAuditRsCompanyBaseinfo.activelegalperson(auditRsCompanyBaseinfonew.getCompanyid())
                                    .getResult();
                            log.info("工建项目法人企业单点登录结束");
                            //同步内网
                            TARequestUtil.sendPost(ConfigUtil.getConfigValue("taconfig", "intermediary")
                                    + "rest/innerattach/insertcompany", JSON.toJSONString(company), "", "");
                        }
                    }
                    else {
                        zwdtthirdregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);
                    }

                    //iAuditOnlineRegister.updateRegister(zwdtthirdregister);
                    zwdtthirdregister.set("dhtoken", identity.get("token"));
                    sendService.insertRegisterProcess(zwdtthirdregister);
                    //同步内网
                    TARequestUtil.sendPost(
                            ConfigUtil.getConfigValue("taconfig", "intermediary") + "rest/innerattach/insertregister",
                            JSON.toJSONString(zwdtthirdregister), "", "");

                    AuditOnlineIndividual individual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(zwdtthirdregister.getAccountguid()).getResult();
                    if (individual == null) {
                        individual = new AuditOnlineIndividual();
                        individual.setRowguid(UUID.randomUUID().toString());
                        individual.setAccountguid(zwdtthirdregister.getAccountguid());
                        individual.setClientname(zwdtthirdregister.getUsername());
                        individual.setIdnumber(zwdtthirdregister.getIdnumber());
                        individual.setContactmobile(zwdtthirdregister.getMobile());
                        //iAuditOnlineIndividual.addIndividual(individual);
                        sendService.insertIndivdualProcess(individual);
                        //同步内网
                        TARequestUtil.sendPost(ConfigUtil.getConfigValue("taconfig", "intermediary")
                                + "rest/innerattach/insertindivdual", JSON.toJSONString(individual), "", "");
                    }
                    else {
                        individual.setClientname(zwdtthirdregister.getUsername());
                        individual.setIdnumber(zwdtthirdregister.getIdnumber());
                        individual.setContactmobile(zwdtthirdregister.getMobile());
                        //iAuditOnlineIndividual.updateIndividual(individual);
                        sendService.insertIndivdualProcess(individual);
                        //同步内网
                        TARequestUtil.sendPost(ConfigUtil.getConfigValue("taconfig", "intermediary")
                                + "rest/innerattach/insertindivdual", JSON.toJSONString(individual), "", "");
                    }

                }
            }
            else {
                // 保存用户信息
                AuditOnlineRegister auditOnlineRegister = new AuditOnlineRegister();
                auditOnlineRegister.setRowguid(UUID.randomUUID().toString());
                auditOnlineRegister.setAccountguid(UUID.randomUUID().toString());
                auditOnlineRegister.setLoginid(loginid);
                auditOnlineRegister.set("dhtoken", identity.get("token"));
                // 统一社会信用代码不为空说明用户是法人
                if (StringUtil.isNotBlank(identity.get("creditcode").toString())) {
                    auditOnlineRegister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_COMPANY);
                    // 法人信息
                    AuditOnlineCompany company = new AuditOnlineCompany();
                    company.setRowguid(UUID.randomUUID().toString());
                    company.setAccountguid(auditOnlineRegister.getAccountguid());
                    company.setCreditcode(identity.get("creditcode").toString());
                    company.setOrganname(identity.get("qyname").toString());
                    //iAuditOnlineCompany.addAuditOnlineCompany(company);
                    sendService.insertCompanyProcess(company);
                    //同步内网
                    TARequestUtil.sendPost(
                            ConfigUtil.getConfigValue("taconfig", "intermediary") + "rest/innerattach/insertcompany",
                            JSON.toJSONString(company), "", "");
                }
                else {
                    auditOnlineRegister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);
                }
                auditOnlineRegister.setUsername(identity.get("name").toString());
                auditOnlineRegister.setMobile(identity.get("mobile").toString());
                auditOnlineRegister.setIdnumber(identity.get("cardid").toString());
                auditOnlineRegister.setLastlogindate(new Date());

                sendService.insertRegisterProcess(auditOnlineRegister);
                //同步内网
                TARequestUtil.sendPost(
                        ConfigUtil.getConfigValue("taconfig", "intermediary") + "rest/innerattach/insertregister",
                        JSON.toJSONString(auditOnlineRegister), "", "");

                zwdtthirdregister = auditOnlineRegister;
                // 个人信息
                AuditOnlineIndividual individual = new AuditOnlineIndividual();
                individual.setRowguid(UUID.randomUUID().toString());
                individual.setAccountguid(auditOnlineRegister.getAccountguid());
                individual.setClientname(auditOnlineRegister.getUsername());
                individual.setIdnumber(auditOnlineRegister.getIdnumber());
                individual.setContactmobile(auditOnlineRegister.getMobile());
                individual.setApplyerguid(UUID.randomUUID().toString());

                //此处有毒
                sendService.insertIndivdualProcess(individual);
                //同步内网
                TARequestUtil.sendPost(
                        ConfigUtil.getConfigValue("taconfig", "intermediary") + "rest/innerattach/insertindivdual",
                        JSON.toJSONString(individual), "", "");

            }
        }

        AuthenticationMapInfo authenticationInfo = new AuthenticationMapInfo(loginid, password, identity.getName());
        authenticationInfo.put("zwdtthirdregister", zwdtthirdregister);
        authenticationInfo.put("dhtoken", String.valueOf(identity.get("token")));
        return authenticationInfo;
    }

}

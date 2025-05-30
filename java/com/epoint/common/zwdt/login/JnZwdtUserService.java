package com.epoint.common.zwdt.login;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.factory.ContextBeanFactory;
import com.epoint.authenticator.identity.AuthenticationMapInfo;
import com.epoint.authenticator.identity.Identity;
import com.epoint.authenticator.loginflow.userservice.NamedUserService;
import com.epoint.authenticator.mgt.LoginInternalInterface;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
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
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;

import java.util.*;

/**
 * 框架用户数据获取业务流程
 *
 * @version [版本号, 2016年12月26日]
 * @作者 sjw
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JnZwdtUserService extends NamedUserService {

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
        String loadType = StringUtil.isBlank(identity.getLoadType()) ? "0" : identity.getLoadType().toString();
        AuditOnlineRegister zwdtthirdregister = null;
        // 可能是普通登录方式的密码 /手机动态密码 /扫码登录的用jessionid/第三方区分标准，wx,qq,wb
        String password = identity.getCredentials().toString();
        // 区分登录方式
        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineRegister.class);
        IAuditOnlineIndividual iAuditOnlineIndividual = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineIndividual.class);
        if (ZwdtConstant.HtirdType.equals(loadType)) {
            Map<String, String> conditionMap = new HashMap<String, String>();
            if (ZwdtConstant.HtirdType_WX.equals(password)) {
                // 微信登录
                conditionMap.put("WXID=", loginid);
            } else if (ZwdtConstant.HtirdType_QQ.equals(password)) {
                // 扣扣登录
                conditionMap.put("QQID=", loginid);
            } else if (ZwdtConstant.HtirdType_WB.equals(password)) {
                // 微博登录
                conditionMap.put("WBID=", loginid);
            }
            List<AuditOnlineRegister> list = iAuditOnlineRegister.selectAuditOnlineRegisterList(conditionMap)
                    .getResult();
            if (list != null && list.size() > 0 && StringUtil.isNotBlank(list.get(0).getLoginid())) {
                // 用户存在,并且信息完善，可以 正常登录
                zwdtthirdregister = list.get(0);

            } else {
                // 保存用户信息，引导用户去完善信息
                AuditOnlineRegister auditOnlineRegister = new AuditOnlineRegister();
                if (ZwdtConstant.HtirdType_WX.equals(password)) {
                    auditOnlineRegister.setWxid(loginid);
                } else if (ZwdtConstant.HtirdType_QQ.equals(password)) {
                    auditOnlineRegister.setQqid(loginid);
                } else if (ZwdtConstant.HtirdType_WB.equals(password)) {
                    auditOnlineRegister.setWbid(loginid);
                }
                auditOnlineRegister.setRowguid(UUID.randomUUID().toString());
                iAuditOnlineRegister.addRegister(auditOnlineRegister);
                zwdtthirdregister = auditOnlineRegister;
                // throw new UnknownAccountException("注册成功，请您完善资料！");
            }

        } else if (ZwdtConstant.LoginIdAndPassWord.equals(loadType)) {
            // 正常用户名密码登录,手机号码/身份证号码+密码
            // 大厅登录初始化register
            if (StringUtil.isBlank(loginid) || StringUtil.isBlank(password)) {
                throw new UnknownAccountException("未能获取到您的用户名或者密码，请确认输入了正确的参数！");
            }
            // 通过账号去查找用户
            zwdtthirdregister = iAuditOnlineRegister.getRegisterByIdorMobile(loginid).getResult();
            if (zwdtthirdregister == null) {
                throw new UnknownAccountException("未能获取到用户,请先注册！");
            } else {
                if (!password.equals(zwdtthirdregister.getPassword())) {
                    throw new UnknownAccountException("系统不能完成您的登录请求，请检查您的用户名和密码是否匹配！");
                }
                if ("1".equals(zwdtthirdregister.getLock())) {
                    throw new UnknownAccountException("系统不能完成您的登录请求，您的用户已经被锁住！");
                }
            }
        } else if (ZwdtConstant.LoginIdAndPhoneCode.equals(loadType)) {
            // 手机号码+动态密码 
            // 大厅登录初始化register
            if (StringUtil.isBlank(loginid) || StringUtil.isBlank(password)) {
                throw new UnknownAccountException("未能获取到您的用户名或者密码，请确认输入了正确的参数！");
            }
            zwdtthirdregister = iAuditOnlineRegister.getRegisterByMobile(loginid).getResult();
            if (zwdtthirdregister == null) {
                throw new UnknownAccountException("未能获取到用户,请先注册！");
            } else {
                AuditOnlineVerycode onlineVerycode = null;
                IAuditOnlineVeryCode iAuditOnlineVeryCode = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineVeryCode.class);
                //判断是否有redis
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 去redis中验证动态密码
                    ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                    onlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, loginid, "4");
                    redisUtil.close();
                } else {
                    // 去库里验证动态密码
                    onlineVerycode = iAuditOnlineVeryCode.getVerifyCode(loginid,
                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT),
                            "0", "4").getResult();
                }
                if (onlineVerycode == null) {
                    throw new UnknownAccountException("验证码已经过期！");
                } else {
                    //进行验证
                    if (!password.equals(onlineVerycode.getVerifycode())) {
                        // 不通过
                        throw new UnknownAccountException("验证码错误！");
                    }

                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                        //删除redis中记录
                        ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                        redisUtil.del(loginid + "_" + "4");
                        redisUtil.close();
                    } else {
                        iAuditOnlineVeryCode.deleteAuditOnlineVerycode(onlineVerycode);
                    }
                }
            }

        } else if (ZwdtConstant.LoginByScanType.equals(loadType)) {
            // 扫描登录
            // 大厅登录初始化register
            if (StringUtil.isBlank(loginid)) {
                throw new UnknownAccountException("未能获取到您的信息，请对准二维码重新扫描");
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
            } else {
                // 当accountguid不为空，并且Isverified为1 说明已经扫描验证通过
                onlineVerycode = iAuditOnlineVeryCode.getScanVerifyCode(
                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), loginid, "1",
                        "3").getResult();
            }
            if (onlineVerycode == null) {
                throw new UnknownAccountException("扫描登录验证码已经过期 ！");
            } else {
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
                redisUtil.del(loginid + "_" + "3");
                redisUtil.close();
            } else {
                iAuditOnlineVeryCode.deleteAuditOnlineVerycode(onlineVerycode);
            }
        } else if (ZwdtConstant.Dhlogin.equals(loadType)) {
            // 大汉单点登录
            // 大厅登录初始化register
            if (StringUtil.isBlank(loginid)) {
                throw new UnknownAccountException("未能获取到您的用户名");
            }

            // 修改为根据身份证号码查找用户信息
            if (StringUtil.isBlank(identity.get("cardid"))) {
                throw new UnknownAccountException("未能获取到您的证件号码");
            }
            // 通过账号去查找用户
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("idnumber", identity.get("cardid") + "");
            sql.setOrderDesc("lastlogindate");

            List<AuditOnlineRegister> list = iAuditOnlineRegister.selectAuditOnlineRegisterList(sql.getMap())
                    .getResult();
            if (list != null && !list.isEmpty() && StringUtil.isNotBlank(list.get(0).getLoginid())) {
                // 用户存在 正常登录
                zwdtthirdregister = list.get(0);
                if (zwdtthirdregister == null) {
                    throw new UnknownAccountException("未能获取到用户信息");
                } else {//更新用户信息
                    //删除原先登录名一样的用户
                    deleteRegisterInfo(loginid, identity.get("cardid") + "");
                    if (StringUtil.isNotBlank(identity.get("hascomp"))) {
                        zwdtthirdregister.setUsertype(saveCompanyInfo(identity, zwdtthirdregister));
                    }
                    if (StringUtil.isNotBlank(identity.get("corrole"))) {
                        zwdtthirdregister.setUsertype(saveCompanyInfoCurrent(identity, zwdtthirdregister));
                    }
                    if(identity.get("usertype")!=null && StringUtils.isNotBlank(identity.get("usertype").toString())){
                        zwdtthirdregister.setUsertype(ZwfwConstant.CONSTANT_STR_ONE.equals(identity.get("usertype").toString())?"20":"10");
                    }

                    zwdtthirdregister.setLoginid(loginid);
                    zwdtthirdregister.setLastlogindate(new Date());
                    zwdtthirdregister.setUsername(identity.get("name").toString());
                    zwdtthirdregister.setMobile(identity.get("mobile").toString());
                    zwdtthirdregister.setIdnumber(identity.get("cardid").toString());
                    zwdtthirdregister.set("dhuuid", identity.get("dhuuid").toString());
                    zwdtthirdregister.set("authlevel", identity.get("authlevel").toString());

                    zwdtthirdregister.set("dhtoken", identity.get("token"));
                    iAuditOnlineRegister.updateRegister(zwdtthirdregister);
                    EpointFrameDsManager.commit();
                    AuditOnlineIndividual individual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(zwdtthirdregister.getAccountguid()).getResult();
                    individual.setClientname(zwdtthirdregister.getUsername());
                    individual.setIdnumber(zwdtthirdregister.getIdnumber());
                    individual.setContactmobile(zwdtthirdregister.getMobile());
                    iAuditOnlineIndividual.updateIndividual(individual);
                }
            } else {
                //删除原先登录名一样的用户
                deleteRegisterInfo(loginid, identity.get("cardid") + "");
                // 保存用户信息
                AuditOnlineRegister auditOnlineRegister = new AuditOnlineRegister();
                auditOnlineRegister.setRowguid(UUID.randomUUID().toString());
                auditOnlineRegister.setAccountguid(UUID.randomUUID().toString());
                auditOnlineRegister.setLoginid(loginid);
                auditOnlineRegister.setMobile(identity.get("mobile").toString());
                auditOnlineRegister.setIdnumber(identity.get("cardid").toString());
                auditOnlineRegister.set("dhuuid", identity.get("dhuuid").toString());
                auditOnlineRegister.setPassword(MD5Util.getMD5("111111"));
                auditOnlineRegister.set("authlevel", identity.get("authlevel").toString());
                auditOnlineRegister.set("dhtoken", identity.get("token"));
                auditOnlineRegister.setLastlogindate(new Date());
                if (StringUtil.isNotBlank(identity.get("hascomp"))) {
                    auditOnlineRegister.setUsertype(saveCompanyInfo(identity, auditOnlineRegister));
                } else if (StringUtil.isNotBlank(identity.get("corrole"))) {
                    auditOnlineRegister.setUsertype(saveCompanyInfoCurrent(identity, auditOnlineRegister));
                } else {
                    auditOnlineRegister.setUsertype("20");//默认是个人用户
                }

                if(identity.get("usertype")!=null && StringUtils.isNotBlank(identity.get("usertype").toString())){
                    auditOnlineRegister.setUsertype(ZwfwConstant.CONSTANT_STR_ONE.equals(identity.get("usertype").toString())?"20":"10");
                }
                auditOnlineRegister.setUsername(identity.get("name").toString());
                iAuditOnlineRegister.addRegister(auditOnlineRegister);
                zwdtthirdregister = auditOnlineRegister;
                AuditOnlineIndividual individual = new AuditOnlineIndividual();
                individual.setRowguid(UUID.randomUUID().toString());
                individual.setAccountguid(auditOnlineRegister.getAccountguid());
                individual.setClientname(auditOnlineRegister.getUsername());
                individual.setIdnumber(auditOnlineRegister.getIdnumber());
                individual.setContactmobile(auditOnlineRegister.getMobile());
                individual.setApplyerguid(UUID.randomUUID().toString());
                iAuditOnlineIndividual.addIndividual(individual);
            }
        }
        AuthenticationMapInfo authenticationInfo = new AuthenticationMapInfo(loginid, password, identity.getName());
        authenticationInfo.put("zwdtthirdregister", zwdtthirdregister);
        return authenticationInfo;
    }

    public String saveCompanyInfo(Identity identity, AuditOnlineRegister zwdtthirdregister) {
        JSONArray compobjdata = (JSONArray) identity.get("compobjdata");
        String cardid = (String) identity.get("cardid");
        String mobile = (String) identity.get("mobile");
        String name = (String) identity.get("name");
        String islegal = ZwfwConstant.APPLAYERTYPE_GR;
        if (compobjdata != null && compobjdata.size() > 0) {
            IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyBaseinfo.class);
            IAuditRsCompanyLegal iAuditRsCompanyLegal = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyLegal.class);
            IAuditRsCompanyRegister iAuditRsCompanyRegister = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyRegister.class);
            IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineCompanyGrant.class);
            for (Object object : compobjdata) {
                JSONObject comp = (JSONObject) object;
                if (StringUtil.isNotBlank(comp.getString("creditcode"))) {
                    String creditcode = comp.getString("creditcode") == null ? "" : comp.getString("creditcode").toString();
                    String corname = comp.getString("corname") == null ? "" : comp.getString("corname").toString();
                    String realname = comp.getString("realname") == null ? "" : comp.getString("realname").toString();
                    String cardnumber = comp.getString("cardnumber") == null ? cardid : comp.getString("cardnumber").toString();
                    String regaddress = comp.getString("regaddress") == null ? "" : comp.getString("regaddress").toString();
                    if (StringUtil.isNotBlank(cardnumber) && cardnumber.equals(cardid)) {
                        islegal = ZwfwConstant.APPLAYERTYPE_QY;
                    }
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("CREDITCODE", creditcode);
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    sqlConditionUtil.setOrderDesc("Operatedate");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> baseInfolist = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    AuditRsCompanyBaseinfo baseInfo = null;
                    if (baseInfolist != null && baseInfolist.size() > 0) {
                        baseInfo = baseInfolist.get(0);
                        String companyid = StringUtil.isBlank(baseInfo.getCompanyid()) ? UUID.randomUUID().toString() : baseInfo.getCompanyid();
                        baseInfo.setCreditcode(creditcode);
                        baseInfo.setOrganname(corname);
                        baseInfo.setCompanyid(companyid);
                        baseInfo.setVersiontime(new Date());
                        baseInfo.setOperatedate(new Date());
                        baseInfo.setOrgantype("企业法人");
                        baseInfo.setOrganlegal(realname);
                        baseInfo.setContactcertnum(cardid);
                        baseInfo.setOrgalegal_idnumber(cardnumber);
                        baseInfo.setRegisteraddress(regaddress);
                        baseInfo.setIs_history("0");
                        iAuditRsCompanyBaseinfo.updateAuditRsCompanyBaseinfo(baseInfo);
                        AuditRsCompanyLegal legal = iAuditRsCompanyLegal
                                .getAuditRsCompanyLegalByCompanyid(companyid).getResult();
                        if (legal != null) {
                            legal.setCreditcode(creditcode);
                            iAuditRsCompanyLegal.updateAuditRsCompanyLegal(legal);
                        } else {
                            legal = new AuditRsCompanyLegal();
                            legal.setRowguid(UUID.randomUUID().toString());
                            legal.setCreditcode(creditcode);
                            legal.setVersiontime(new Date());
                            legal.setLegalname(realname);
                            legal.setLegalcertnumber(cardnumber);
                            legal.setVersion(1);
                            legal.setIs_history("0");
                            legal.setCompanyid(companyid);
                            iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
                        }
                        AuditRsCompanyRegister companyRegister = iAuditRsCompanyRegister
                                .getAuditRsCompanyRegisterByCompanyid(baseInfo.getCompanyid()).getResult();
                        if (companyRegister != null) {
                            companyRegister.setCreditcode(creditcode);
                            iAuditRsCompanyRegister.updateAuditRsCompanyRegister(companyRegister);
                        } else {
                            companyRegister = new AuditRsCompanyRegister();
                            companyRegister.setRowguid(UUID.randomUUID().toString());
                            companyRegister.setCreditcode(creditcode);
                            companyRegister.setContactphone(mobile);
                            companyRegister.setVersiontime(new Date());
                            companyRegister.setVersion(1);
                            companyRegister.setIs_history("0");
                            companyRegister.setCompanyid(companyid);
                            iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
                        }
                        iAuditRsCompanyBaseinfo.activelegalperson(baseInfo.getCompanyid()).getResult();
                        if (StringUtil.isNotBlank(cardnumber) && !cardnumber.equals(cardid)) {
                            AuditOnlineCompanyGrant grant = iAuditOnlineCompanyGrant
                                    .getGrantByBsqUserGuidAndCompanyId(baseInfo.getCompanyid(), zwdtthirdregister.getAccountguid()).getResult();
                            if (grant == null) {
                                grant = new AuditOnlineCompanyGrant();
                                grant.setOperatedate(new Date());
                                grant.setBsqlevel(ZwdtConstant.GRANT_AGENT);
                                grant.setRowguid(UUID.randomUUID().toString());
                                grant.setSqidnum(cardnumber);
                                grant.setSquserguid(zwdtthirdregister.getAccountguid());
                                grant.setSqtime(new Date());
                                grant.setSqusername(realname);
                                grant.setBsqidnum(cardid);
                                grant.setM_IsActive("1");
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                grant.setBsqusername(name);
                                grant.setCompanyid(companyid);
                                iAuditOnlineCompanyGrant.addAuditOnlineCompanyGrant(grant);
                            } else {
                                grant.setCompanyid(companyid);
                                grant.setBsqidnum(cardid);
                                grant.setM_IsActive("1");
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(grant);
                            }
                        }
                    } else {
                        baseInfo = new AuditRsCompanyBaseinfo();
                        String companyid = UUID.randomUUID().toString();
                        baseInfo.setRowguid(UUID.randomUUID().toString());
                        baseInfo.setCreditcode(creditcode);
                        baseInfo.setOrganname(corname);
                        baseInfo.setVersiontime(new Date());
                        baseInfo.setOperatedate(new Date());
                        baseInfo.setOrgantype("企业法人");
                        baseInfo.setContactcertnum(cardid);
                        baseInfo.setOrganlegal(realname);
                        baseInfo.setOrgalegal_idnumber(cardnumber);
                        baseInfo.setRegisteraddress(regaddress);
                        baseInfo.setVersiontime(new Date());
                        baseInfo.setVersion(1);
                        baseInfo.setCompanyid(companyid);
                        baseInfo.setIs_history("0");
                        iAuditRsCompanyBaseinfo.addAuditRsCompanyBaseinfo(baseInfo);
                        AuditRsCompanyLegal legal = new AuditRsCompanyLegal();
                        legal.setRowguid(UUID.randomUUID().toString());
                        legal.setCreditcode(creditcode);
                        legal.setVersiontime(new Date());
                        legal.setLegalname(realname);
                        legal.setLegalcertnumber(cardnumber);
                        legal.setVersion(1);
                        legal.setIs_history("0");
                        legal.setCompanyid(companyid);
                        iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
                        AuditRsCompanyRegister companyRegister = new AuditRsCompanyRegister();
                        companyRegister.setRowguid(UUID.randomUUID().toString());
                        companyRegister.setCreditcode(creditcode);
                        companyRegister.setContactphone(mobile);
                        companyRegister.setVersiontime(new Date());
                        companyRegister.setVersion(1);
                        companyRegister.setIs_history("0");
                        companyRegister.setCompanyid(companyid);
                        iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
                        iAuditRsCompanyBaseinfo.activelegalperson(companyid).getResult();
                        if (StringUtil.isNotBlank(cardnumber) && !cardnumber.equals(cardid)) {
                            AuditOnlineCompanyGrant grant = iAuditOnlineCompanyGrant
                                    .getGrantByBsqUserGuidAndCompanyId(baseInfo.getCompanyid(), zwdtthirdregister.getAccountguid()).getResult();
                            if (grant == null) {
                                grant = new AuditOnlineCompanyGrant();
                                grant.setOperatedate(new Date());
                                grant.setBsqlevel(ZwdtConstant.GRANT_AGENT);
                                grant.setRowguid(UUID.randomUUID().toString());
                                grant.setSqidnum(cardnumber);
                                grant.setSquserguid(zwdtthirdregister.getAccountguid());
                                grant.setSqtime(new Date());
                                grant.setSqusername(realname);
                                grant.setBsqidnum(cardid);
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                grant.setBsqusername(name);
                                grant.setM_IsActive("1");
                                grant.setCompanyid(companyid);
                                iAuditOnlineCompanyGrant.addAuditOnlineCompanyGrant(grant);
                            } else {
                                grant.setCompanyid(companyid);
                                grant.setBsqidnum(cardid);
                                grant.setM_IsActive("1");
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(grant);
                            }
                        }
                    }
                }
            }
        }
        return islegal;
    }

    //新的大汉平台接口对接，获取企业数据
    public String saveCompanyInfoNew(Identity identity, AuditOnlineRegister zwdtthirdregister) {
        JSONArray compobjdata = new JSONArray();
        String cardid = (String) identity.get("cardid");
        String mobile = (String) identity.get("mobile");
        String name = (String) identity.get("name");
        String dhtoken = (String) identity.get("dhtoken");
        String corrole = (String) identity.get("corrole");//办事人企业角色（0:经办人  1:管理员，2：法人)
        String granttype = ZwdtConstant.GRANT_AGENT;//默认经办人
        if (StringUtil.isNotBlank(corrole)) {
            switch (corrole) {
                case "1":
                    granttype = ZwdtConstant.GRANT_MANAGER;
                    break;
                case "2":
                    granttype = ZwdtConstant.GRANT_LEGEL_PERSION;
                    break;
                default:
                    break;
            }
        }
        String areasign = (String) identity.get("areasign");
        String decodekey = ConfigUtil.getConfigValue("jnlogin1", areasign + "appkey");
        String appmark = ConfigUtil.getConfigValue("jnlogin1", areasign + "appmark");
        JSONObject rtn = DhUser_HttpUtil.getCorInfo(appmark, decodekey, "0", dhtoken, "1");
        if (rtn != null) {
            Integer count = rtn.getInteger("count");
            Integer pages = count / 5 + (count % 5 > 0 ? 1 : 0);
            if (count > 0) {
                String companystr = rtn.getString("list");
                compobjdata = JSONArray.parseArray(companystr);
            }
            //企业数量大于5，需要循环遍历获取，大汉接口每次最多获取5条数据
            if (pages > 1) {
                for (int i = 2; i <= pages; i++) {
                    JSONObject rtncom = DhUser_HttpUtil.getCorInfo(appmark, decodekey, "0", dhtoken, Integer.toString(i));
                    String companystr = rtncom.getString("list");
                    JSONArray comarray = JSONArray.parseArray(companystr);
                    compobjdata.addAll(comarray);
                }
            }
        }
        String islegal = ZwfwConstant.APPLAYERTYPE_QY;
//    	System.out.println(compobjdata.toJSONString());
        if (compobjdata != null && !compobjdata.isEmpty()) {
            IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyBaseinfo.class);
            IAuditRsCompanyLegal iAuditRsCompanyLegal = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyLegal.class);
            IAuditRsCompanyRegister iAuditRsCompanyRegister = ContainerFactory.getContainInfo()
                    .getComponent(IAuditRsCompanyRegister.class);
            IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineCompanyGrant.class);
            // 查找法人名下所有的企业信息
            Map<String, AuditRsCompanyBaseinfo> companyBaseinfoMap = new HashMap<>();
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("orgalegal_idnumber", cardid);
            sqlConditionUtil.isBlankOrValue("is_history", "0");
            sqlConditionUtil.setOrderDesc("Operatedate");
            // 3.1、根据条件查询出法人信息列表
            List<AuditRsCompanyBaseinfo> baseInfolist = iAuditRsCompanyBaseinfo
                    .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
            for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : baseInfolist) {
                companyBaseinfoMap.put(auditRsCompanyBaseinfo.getCreditcode(), auditRsCompanyBaseinfo);
            }

            Date now = new Date();
            for (Object object : compobjdata) {
                JSONObject comp = (JSONObject) object;
                if (StringUtil.isNotBlank(comp.getString("cornumber"))) {
                    String creditcode = comp.getString("cornumber") == null ? "" : comp.getString("cornumber");
                    String corname = comp.getString("corname") == null ? "" : comp.getString("corname");
                    String realname = comp.getString("corusername") == null ? "" : comp.getString("corusername");
                    String cardnumber = comp.getString("corusercardid") == null ? cardid : comp.getString("corusercardid");

                    if (cardnumber.length() > 18) {
                        cardnumber = cardid;
                    }

                    String regaddress = "";
                    if (StringUtil.isNotBlank(cardnumber) && cardnumber.equals(cardid)) {
                        islegal = ZwfwConstant.APPLAYERTYPE_QY;
                    }

                    AuditRsCompanyBaseinfo baseInfo = companyBaseinfoMap.get(creditcode);
                    if (baseInfo != null) {
                        String companyid = StringUtil.isBlank(baseInfo.getCompanyid()) ? UUID.randomUUID().toString() : baseInfo.getCompanyid();
                        baseInfo.setCreditcode(creditcode);
                        baseInfo.setOrganname(corname);
                        baseInfo.setCompanyid(companyid);
                        baseInfo.setVersiontime(now);
                        baseInfo.setOperatedate(now);
                        baseInfo.setOrgantype("企业法人");
                        baseInfo.setOrganlegal(realname);
                        baseInfo.setContactcertnum(cardid);
                        baseInfo.setOrgalegal_idnumber(cardnumber);
                        baseInfo.setRegisteraddress(regaddress);
                        baseInfo.setIs_history("0");
                        iAuditRsCompanyBaseinfo.updateAuditRsCompanyBaseinfo(baseInfo);
                        AuditRsCompanyLegal legal = iAuditRsCompanyLegal
                                .getAuditRsCompanyLegalByCompanyid(companyid).getResult();
                        if (legal != null) {
                            legal.setCreditcode(creditcode);
                            iAuditRsCompanyLegal.updateAuditRsCompanyLegal(legal);
                        } else {
                            legal = new AuditRsCompanyLegal();
                            legal.setRowguid(UUID.randomUUID().toString());
                            legal.setCreditcode(creditcode);
                            legal.setVersiontime(now);
                            legal.setLegalname(realname);
                            legal.setLegalcertnumber(cardnumber);
                            legal.setVersion(1);
                            legal.setIs_history("0");
                            legal.setCompanyid(companyid);
                            iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
                        }
                        AuditRsCompanyRegister companyRegister = iAuditRsCompanyRegister
                                .getAuditRsCompanyRegisterByCompanyid(baseInfo.getCompanyid()).getResult();
                        if (companyRegister != null) {
                            companyRegister.setCreditcode(creditcode);
                            iAuditRsCompanyRegister.updateAuditRsCompanyRegister(companyRegister);
                        } else {
                            companyRegister = new AuditRsCompanyRegister();
                            companyRegister.setRowguid(UUID.randomUUID().toString());
                            companyRegister.setCreditcode(creditcode);
                            companyRegister.setContactphone(mobile);
                            companyRegister.setVersiontime(now);
                            companyRegister.setVersion(1);
                            companyRegister.setIs_history("0");
                            companyRegister.setCompanyid(companyid);
                            iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
                        }
                        iAuditRsCompanyBaseinfo.activelegalperson(baseInfo.getCompanyid()).getResult();
                        if (StringUtil.isNotBlank(cardnumber) && !cardnumber.equals(cardid)) {
                            AuditOnlineCompanyGrant grant = iAuditOnlineCompanyGrant
                                    .getGrantByBsqUserGuidAndCompanyId(baseInfo.getCompanyid(), zwdtthirdregister.getAccountguid()).getResult();
                            if (grant == null) {
                                grant = new AuditOnlineCompanyGrant();
                                grant.setOperatedate(now);
                                grant.setBsqlevel(granttype);
                                grant.setRowguid(UUID.randomUUID().toString());
                                grant.setSqidnum(cardnumber);
                                grant.setSquserguid(zwdtthirdregister.getAccountguid());
                                grant.setSqtime(now);
                                grant.setSqusername(realname);
                                grant.setBsqidnum(cardid);
                                grant.setM_IsActive("1");
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                grant.setBsqusername(name);
                                grant.setCompanyid(companyid);
                                iAuditOnlineCompanyGrant.addAuditOnlineCompanyGrant(grant);
                            } else {
                                grant.setCompanyid(companyid);
                                grant.setBsqidnum(cardid);
                                grant.setM_IsActive("1");
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(grant);
                            }
                        }
                    } else {
                        baseInfo = new AuditRsCompanyBaseinfo();
                        String companyid = UUID.randomUUID().toString();
                        baseInfo.setRowguid(UUID.randomUUID().toString());
                        baseInfo.setCreditcode(creditcode);
                        baseInfo.setOrganname(corname);
                        baseInfo.setVersiontime(now);
                        baseInfo.setOperatedate(now);
                        baseInfo.setOrgantype("企业法人");
                        baseInfo.setContactcertnum(cardid);
                        baseInfo.setOrganlegal(realname);
                        baseInfo.setOrgalegal_idnumber(cardnumber);
                        baseInfo.setRegisteraddress(regaddress);
                        baseInfo.setVersiontime(now);
                        baseInfo.setVersion(1);
                        baseInfo.setCompanyid(companyid);
                        baseInfo.setIs_history("0");
                        iAuditRsCompanyBaseinfo.addAuditRsCompanyBaseinfo(baseInfo);
                        AuditRsCompanyLegal legal = new AuditRsCompanyLegal();
                        legal.setRowguid(UUID.randomUUID().toString());
                        legal.setCreditcode(creditcode);
                        legal.setVersiontime(now);
                        legal.setLegalname(realname);
                        legal.setLegalcertnumber(cardnumber);
                        legal.setVersion(1);
                        legal.setIs_history("0");
                        legal.setCompanyid(companyid);
                        iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
                        AuditRsCompanyRegister companyRegister = new AuditRsCompanyRegister();
                        companyRegister.setRowguid(UUID.randomUUID().toString());
                        companyRegister.setCreditcode(creditcode);
                        companyRegister.setContactphone(mobile);
                        companyRegister.setVersiontime(now);
                        companyRegister.setVersion(1);
                        companyRegister.setIs_history("0");
                        companyRegister.setCompanyid(companyid);
                        iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
                        iAuditRsCompanyBaseinfo.activelegalperson(companyid).getResult();
                        if (StringUtil.isNotBlank(cardnumber) && !cardnumber.equals(cardid)) {
                            AuditOnlineCompanyGrant grant = iAuditOnlineCompanyGrant
                                    .getGrantByBsqUserGuidAndCompanyId(baseInfo.getCompanyid(), zwdtthirdregister.getAccountguid()).getResult();
                            if (grant == null) {
                                grant = new AuditOnlineCompanyGrant();
                                grant.setOperatedate(now);
                                grant.setBsqlevel(granttype);
                                grant.setRowguid(UUID.randomUUID().toString());
                                grant.setSqidnum(cardnumber);
                                grant.setSquserguid(zwdtthirdregister.getAccountguid());
                                grant.setSqtime(now);
                                grant.setSqusername(realname);
                                grant.setBsqidnum(cardid);
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                grant.setBsqusername(name);
                                grant.setM_IsActive("1");
                                grant.setCompanyid(companyid);
                                iAuditOnlineCompanyGrant.addAuditOnlineCompanyGrant(grant);
                            } else {
                                grant.setCompanyid(companyid);
                                grant.setBsqidnum(cardid);
                                grant.setM_IsActive("1");
                                grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                                iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(grant);
                            }
                        }
                    }
                }
            }
        }
        return islegal;
    }

    /**
     * 只保存当前账号的企业信息
     *
     * @param identity
     * @param zwdtthirdregister
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String saveCompanyInfoCurrent(Identity identity, AuditOnlineRegister zwdtthirdregister) {
        String cardid = (String) identity.get("cardid");
        String mobile = (String) identity.get("mobile");
        String name = (String) identity.get("name");
        String corrole = (String) identity.get("corrole");// 办事人企业角色（0:经办人
        // 1:管理员，2：法人)
        String corname = (String) identity.get("corname");// 企业名称
        String creditcode = (String) identity.get("cornumber");// 企业证件号码
        String cardnumber = (String) identity.get("corusercardid");
        if (StringUtil.isBlank(cardnumber) || cardnumber.length() > 18) {
            cardnumber = cardid;
        }
        String realname = (String) identity.get("corusername");
        String granttype = ZwdtConstant.GRANT_AGENT;// 默认经办人
        if (StringUtil.isNotBlank(corrole)) {
            switch (corrole) {
                case "1":
                    granttype = ZwdtConstant.GRANT_MANAGER;
                    break;
                case "2":
                    granttype = ZwdtConstant.GRANT_LEGEL_PERSION;
                    break;
                default:
                    break;
            }
        }

        IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsCompanyBaseinfo.class);
        IAuditRsCompanyLegal iAuditRsCompanyLegal = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsCompanyLegal.class);
        IAuditRsCompanyRegister iAuditRsCompanyRegister = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsCompanyRegister.class);
        IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineCompanyGrant.class);
        // 查找企业信息
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("creditcode", creditcode);
        sqlConditionUtil.isBlankOrValue("is_history", "0");
        sqlConditionUtil.setOrderDesc("Operatedate");
        // 3.1、根据条件查询出法人信息列表
        List<AuditRsCompanyBaseinfo> baseInfolist = iAuditRsCompanyBaseinfo
                .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();

        Date now = new Date();
        String islegal = ZwfwConstant.APPLAYERTYPE_QY;
        AuditRsCompanyBaseinfo baseInfo = null;
        String regaddress = "";
        if (StringUtil.isNotBlank(cardnumber) && cardnumber.equals(cardid)) {
            islegal = ZwfwConstant.APPLAYERTYPE_QY;
        }
        if (baseInfolist != null && !baseInfolist.isEmpty()) {
            baseInfo = baseInfolist.get(0);
            String companyid = StringUtil.isBlank(baseInfo.getCompanyid()) ? UUID.randomUUID().toString()
                    : baseInfo.getCompanyid();
            baseInfo.setCreditcode(creditcode);
            baseInfo.setOrganname(corname);
            baseInfo.setCompanyid(companyid);
            baseInfo.setVersiontime(now);
            baseInfo.setOperatedate(now);
            baseInfo.setOrgantype("企业法人");
            baseInfo.setOrganlegal(realname);
            baseInfo.setContactcertnum(cardid);
            baseInfo.setOrgalegal_idnumber(cardnumber);
            baseInfo.setRegisteraddress(regaddress);
            baseInfo.setIs_history("0");
            iAuditRsCompanyBaseinfo.updateAuditRsCompanyBaseinfo(baseInfo);
            AuditRsCompanyLegal legal = iAuditRsCompanyLegal.getAuditRsCompanyLegalByCompanyid(companyid).getResult();
            if (legal != null) {
                legal.setCreditcode(creditcode);
                iAuditRsCompanyLegal.updateAuditRsCompanyLegal(legal);
            } else {
                legal = new AuditRsCompanyLegal();
                legal.setRowguid(UUID.randomUUID().toString());
                legal.setCreditcode(creditcode);
                legal.setVersiontime(now);
                legal.setLegalname(realname);
                legal.setLegalcertnumber(cardnumber);
                legal.setVersion(1);
                legal.setIs_history("0");
                legal.setCompanyid(companyid);
                iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
            }
            AuditRsCompanyRegister companyRegister = iAuditRsCompanyRegister
                    .getAuditRsCompanyRegisterByCompanyid(baseInfo.getCompanyid()).getResult();
            if (companyRegister != null) {
                companyRegister.setCreditcode(creditcode);
                iAuditRsCompanyRegister.updateAuditRsCompanyRegister(companyRegister);
            } else {
                companyRegister = new AuditRsCompanyRegister();
                companyRegister.setRowguid(UUID.randomUUID().toString());
                companyRegister.setCreditcode(creditcode);
                companyRegister.setContactphone(mobile);
                companyRegister.setVersiontime(now);
                companyRegister.setVersion(1);
                companyRegister.setIs_history("0");
                companyRegister.setCompanyid(companyid);
                iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
            }
            iAuditRsCompanyBaseinfo.activelegalperson(baseInfo.getCompanyid()).getResult();
            if (StringUtil.isNotBlank(cardnumber) && !cardnumber.equals(cardid)) {
                AuditOnlineCompanyGrant grant = iAuditOnlineCompanyGrant
                        .getGrantByBsqUserGuidAndCompanyId(baseInfo.getCompanyid(), zwdtthirdregister.getAccountguid())
                        .getResult();
                if (grant == null) {
                    grant = new AuditOnlineCompanyGrant();
                    grant.setOperatedate(now);
                    grant.setBsqlevel(granttype);
                    grant.setRowguid(UUID.randomUUID().toString());
                    grant.setSqidnum(cardnumber);
                    grant.setSquserguid(zwdtthirdregister.getAccountguid());
                    grant.setSqtime(now);
                    grant.setSqusername(realname);
                    grant.setBsqidnum(cardid);
                    grant.setM_IsActive("1");
                    grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                    grant.setBsqusername(name);
                    grant.setCompanyid(companyid);
                    iAuditOnlineCompanyGrant.addAuditOnlineCompanyGrant(grant);
                } else {
                    grant.setCompanyid(companyid);
                    grant.setBsqidnum(cardid);
                    grant.setM_IsActive("1");
                    grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                    iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(grant);
                }
            }
        } else {
            baseInfo = new AuditRsCompanyBaseinfo();
            String companyid = UUID.randomUUID().toString();
            baseInfo.setRowguid(UUID.randomUUID().toString());
            baseInfo.setCreditcode(creditcode);
            baseInfo.setOrganname(corname);
            baseInfo.setVersiontime(now);
            baseInfo.setOperatedate(now);
            baseInfo.setOrgantype("企业法人");
            baseInfo.setContactcertnum(cardid);
            baseInfo.setOrganlegal(realname);
            baseInfo.setOrgalegal_idnumber(cardnumber);
            baseInfo.setRegisteraddress(regaddress);
            baseInfo.setVersiontime(now);
            baseInfo.setVersion(1);
            baseInfo.setCompanyid(companyid);
            baseInfo.setIs_history("0");
            iAuditRsCompanyBaseinfo.addAuditRsCompanyBaseinfo(baseInfo);
            AuditRsCompanyLegal legal = new AuditRsCompanyLegal();
            legal.setRowguid(UUID.randomUUID().toString());
            legal.setCreditcode(creditcode);
            legal.setVersiontime(now);
            legal.setLegalname(realname);
            legal.setLegalcertnumber(cardnumber);
            legal.setVersion(1);
            legal.setIs_history("0");
            legal.setCompanyid(companyid);
            iAuditRsCompanyLegal.addAuditRsCompanyLegal(legal);
            AuditRsCompanyRegister companyRegister = new AuditRsCompanyRegister();
            companyRegister.setRowguid(UUID.randomUUID().toString());
            companyRegister.setCreditcode(creditcode);
            companyRegister.setContactphone(mobile);
            companyRegister.setVersiontime(now);
            companyRegister.setVersion(1);
            companyRegister.setIs_history("0");
            companyRegister.setCompanyid(companyid);
            iAuditRsCompanyRegister.addAuditRsCompanyRegister(companyRegister);
            iAuditRsCompanyBaseinfo.activelegalperson(companyid).getResult();
            if (StringUtil.isNotBlank(cardnumber) && !cardnumber.equals(cardid)) {
                AuditOnlineCompanyGrant grant = iAuditOnlineCompanyGrant
                        .getGrantByBsqUserGuidAndCompanyId(baseInfo.getCompanyid(), zwdtthirdregister.getAccountguid())
                        .getResult();
                if (grant == null) {
                    grant = new AuditOnlineCompanyGrant();
                    grant.setOperatedate(now);
                    grant.setBsqlevel(granttype);
                    grant.setRowguid(UUID.randomUUID().toString());
                    grant.setSqidnum(cardnumber);
                    grant.setSquserguid(zwdtthirdregister.getAccountguid());
                    grant.setSqtime(now);
                    grant.setSqusername(realname);
                    grant.setBsqidnum(cardid);
                    grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                    grant.setBsqusername(name);
                    grant.setM_IsActive("1");
                    grant.setCompanyid(companyid);
                    iAuditOnlineCompanyGrant.addAuditOnlineCompanyGrant(grant);
                } else {
                    grant.setCompanyid(companyid);
                    grant.setBsqidnum(cardid);
                    grant.setM_IsActive("1");
                    grant.setBsquserguid(zwdtthirdregister.getAccountguid());
                    iAuditOnlineCompanyGrant.updateAuditOnlineCompanyGrant(grant);
                }
            }
        }
        return islegal;
    }

    //删除登录名一样，但是身份证不一样的用户
    public void deleteRegisterInfo(String loginid, String idnumber) {
        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo().getComponent(IAuditOnlineRegister.class);
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("loginid", loginid);
        sql.nq("idnumber", idnumber);
        List<AuditOnlineRegister> list = iAuditOnlineRegister.selectAuditOnlineRegisterList(sql.getMap()).getResult();
        if (!list.isEmpty()) {
            for (AuditOnlineRegister register : list) {
                iAuditOnlineRegister.deleteRegister(register);
            }
        }
    }


}

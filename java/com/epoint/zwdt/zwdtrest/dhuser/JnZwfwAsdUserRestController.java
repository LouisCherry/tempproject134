package com.epoint.zwdt.zwdtrest.dhuser;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.jnvisitrecord.api.IJnVisitRecordService;
import com.epoint.xmz.jnvisitrecord.api.entity.JnVisitRecord;
import com.epoint.xmz.zjxl.util.AesDemoUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jnzwfwAsdUser")
public class JnZwfwAsdUserRestController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 用户注册信息API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 用户个人信息API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 系统参数API
     */
    @Autowired
    private IHandleConfig handleConfig;

    @Autowired
    private IJnVisitRecordService iJnVisitRecordService;

    /**
     * 通过OPENID获取用户信息  -- 居民码
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/asdUserTokenDetail", method = RequestMethod.POST)
    public String asdUserTokenDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用asdUserTokenDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String data = jsonObject.getString("data");
            AesDemoUtil aes = null;
            try {
                aes = new AesDemoUtil();
                data = aes.decrypt(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject datanew = JSONObject.parseObject(data);
            String token = datanew.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) datanew.get("params");

                // 1.2、获取电话号码
                String mobile = obj.getString("mobile");
                // 1.3、获取用户姓名
                String idcard = obj.getString("idcard");
                // 1.4、获取用户姓名
                String name = obj.getString("name");
                // 1.5 获取用户登录名
                String loginname = obj.getString("loginname");

                // 1.6、获取sm2加密配置参数
                String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
                // 2、根据OPENID获取用户信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("mobile", mobile);
                List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
                        .selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();

                if (auditOnlineRegisters.isEmpty()) {
                    log.info("idcard:"+idcard+",mobile:"+mobile+",name:"+name+",loginname:"+loginname);
                    idcard = StringUtil.isNotBlank(idcard) ? idcard.toUpperCase() : idcard;

                    String accountguid = UUID.randomUUID().toString();
                    // 3、插入个人基本信息表
                    AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
                    auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
                    auditOnlineIndividual.setAccountguid(accountguid);
                    auditOnlineIndividual.setClientname(name);
                    auditOnlineIndividual.setIdnumber(idcard);
                    auditOnlineIndividual.setContactmobile(mobile);
                    auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
                    iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
                    // 3.1、插入用户注册表
                    AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
                    auditonlineregister.setRowguid(UUID.randomUUID().toString());
                    auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);//默认自然人
                    auditonlineregister.setAccountguid(accountguid);
                    auditonlineregister.setPassword(MD5Util.getMD5("111111"));
                    auditonlineregister.setMobile(mobile);
                    auditonlineregister.setUsername(name);
                    auditonlineregister.setIdnumber(idcard);
                    auditonlineregister.setLoginid(loginname);
                    iAuditOnlineRegister.addRegister(auditonlineregister);
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                AuditOnlineRegister auditOnlineRegister = auditOnlineRegisters.get(0);
//                dataJson.put("username", auditOnlineRegister.getUsername()); //用户姓名
//                dataJson.put("idnum", auditOnlineRegister.getIdnumber()); //身份证号码 
//                dataJson.put("mobile", auditOnlineRegister.getMobile()); //手机号码
                String refresh_token = auditOnlineRegister.getRefreshtoken();
                String loginUrl = ""; // 请求token地址
                String loginResult = ""; // 请求token返回结果
                String urlroot = request.getRequestURL().toString();
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("interalIP"))) {
                    urlroot = ConfigUtil.getConfigValue("interalIP");
                } else {
                    urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
                }
                // 4、判断是否首次登录
                if (StringUtil.isBlank(refresh_token)) {
                    // 4.1、如果为空 则请求登录接口获取token
                    // 4.1.1、若采用SM2加密，则传入sm2加密标识
                    if (StringUtil.isNotBlank(customPwdEncrypt)
                            && ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                        loginUrl = "rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                + "&encrypttype=sm2" + "";
                    }
                    // 4.1.2、未采用sm2加密方式
                    else {
                        loginUrl = "rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                + "";
                    }
                    loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                            "application/x-www-form-urlencoded");
                    // 4.1.3、解析请求的返回值
                    JSONObject jsonresult = JSONObject.parseObject(loginResult);
                    log.info("=======asdUserTokenDetail返回参数：jsonresult【" + jsonresult + "】=======");
                    if (jsonresult.containsKey("error")) {
                        return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
                    } else {
                        dataJson.put("token", jsonresult.get("access_token"));
                        auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                    }
                } else {
                    // 4.2、如果不为空，则用refresh_token去刷新，获取最新的access_token
                    loginUrl = "rest/oauth2/token?grant_type=refresh_token&&client_id=zwdtClient&client_secret=zwdtSecret&refresh_token="
                            + refresh_token + "";
                    loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                            "application/x-www-form-urlencoded");
                    // 4.2.1、解析请求的返回值
                    JSONObject jsonresult = JSONObject.parseObject(loginResult);
                    log.info("=======asdUserTokenDetail返回参数refresh_token1：jsonresult【" + jsonresult + "】=======");
                    if (jsonresult.containsKey("error")) {
                        // 4.2.1.1、如果过期的refresh_token，需要重新登录
                        // 4.2.1.1.1、若采用SM2加密，则传入sm2加密标识
                        if (StringUtil.isNotBlank(customPwdEncrypt)
                                && ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                            loginUrl = "rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                    + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                    + "&encrypttype=sm2" + "";
                        }
                        // 4.2.1.1.2、未采用sm2加密方式
                        else {
                            loginUrl = "rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                    + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                    + "";
                        }
                        loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                                "application/x-www-form-urlencoded");
                        // 4.2.1.2、解析请求的返回值
                        jsonresult = JSONObject.parseObject(loginResult);
                        log.info("=======asdUserTokenDetail返回参数refresh_token：jsonresult2【" + jsonresult + "】=======");
                        if (jsonresult.containsKey("error")) {
                            return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
                        } else {
                            dataJson.put("token", jsonresult.get("access_token"));
                            auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                            iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                        }
                    } else {
                        dataJson.put("token", jsonresult.get("access_token"));
                        auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                    }
                }
                log.info("=======结束调用asdUserTokenDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", " 通过asdUserTokenDetail获取用户信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======asdUserTokenDetail接口参数：params【" + params + "】=======");
            log.info("=======asdUserTokenDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "爱山东app调用刷新用户token：" + e.getMessage(), "");
        }
    }


    /**
     * 通过OPENID获取用户信息 --爱山东使用
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/asdUserTokenDetailNew", method = RequestMethod.POST)
    public String asdUserTokenDetailNew(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用asdUserTokenDetailNew接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String data = jsonObject.getString("data");
            AesDemoUtil aes = null;
            try {
                aes = new AesDemoUtil();
                data = aes.decrypt(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject datanew = JSONObject.parseObject(data);
            String token = datanew.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) datanew.get("params");

                // 1.2、获取电话号码
                String mobile = obj.getString("mobile");
                // 1.3、获取用户姓名
                String idcard = obj.getString("idcard");
                // 1.4、获取用户姓名
                String name = obj.getString("name");
                // 1.5 获取用户登录名
                String loginname = obj.getString("loginname");

                // 1.6、获取sm2加密配置参数
                String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
                // 2、根据OPENID获取用户信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("mobile", mobile);
                List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
                        .selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
                AuditOnlineRegister auditOnlineRegister = null;
                if (auditOnlineRegisters.isEmpty()) {

                    idcard = StringUtil.isNotBlank(idcard) ? idcard.toUpperCase() : idcard;

                    String accountguid = UUID.randomUUID().toString();
                    // 3、插入个人基本信息表
                    AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
                    auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
                    auditOnlineIndividual.setAccountguid(accountguid);
                    auditOnlineIndividual.setClientname(name);
                    auditOnlineIndividual.setIdnumber(idcard);
                    auditOnlineIndividual.setContactmobile(mobile);
                    auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
                    iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
                    // 3.1、插入用户注册表
                    AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
                    auditonlineregister.setRowguid(UUID.randomUUID().toString());
                    auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);//默认自然人
                    auditonlineregister.setAccountguid(accountguid);
                    auditonlineregister.setPassword(MD5Util.getMD5("111111"));
                    auditonlineregister.setMobile(mobile);
                    auditonlineregister.setUsername(name);
                    auditonlineregister.setIdnumber(idcard);
                    auditonlineregister.setLoginid(loginname);
                    iAuditOnlineRegister.addRegister(auditonlineregister);

                    auditOnlineRegister = auditonlineregister;
                }else{
                    auditOnlineRegister = auditOnlineRegisters.get(0);
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
//                dataJson.put("username", auditOnlineRegister.getUsername()); //用户姓名
//                dataJson.put("idnum", auditOnlineRegister.getIdnumber()); //身份证号码
//                dataJson.put("mobile", auditOnlineRegister.getMobile()); //手机号码
                String refresh_token = auditOnlineRegister.getRefreshtoken();
                String loginUrl = ""; // 请求token地址
                String loginResult = ""; // 请求token返回结果
                String urlroot = request.getRequestURL().toString();
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("interalIP"))) {
                    urlroot = ConfigUtil.getConfigValue("interalIP");
                } else {
                    urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
                }
                // 4、判断是否首次登录
                if (StringUtil.isBlank(refresh_token)) {
                    // 4.1、如果为空 则请求登录接口获取token
                    // 4.1.1、若采用SM2加密，则传入sm2加密标识
                    if (StringUtil.isNotBlank(customPwdEncrypt)
                            && ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                        loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                + "&encrypttype=sm2" + "";
                    }
                    // 4.1.2、未采用sm2加密方式
                    else {
                        loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                + "";
                    }
                    loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                            "application/x-www-form-urlencoded");
                    // 4.1.3、解析请求的返回值
                    JSONObject jsonresult = JSONObject.parseObject(loginResult);
                    log.info("=======asdUserTokenDetail返回参数：jsonresult【" + jsonresult + "】=======");
                    if (jsonresult.containsKey("error")) {
                        return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
                    } else {
                        dataJson.put("token", jsonresult.get("access_token"));
                        auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                    }
                } else {
                    // 4.2、如果不为空，则用refresh_token去刷新，获取最新的access_token
                    loginUrl = "/rest/oauth2/token?grant_type=refresh_token&&client_id=zwdtClient&client_secret=zwdtSecret&refresh_token="
                            + refresh_token + "";
                    loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                            "application/x-www-form-urlencoded");
                    // 4.2.1、解析请求的返回值
                    JSONObject jsonresult = JSONObject.parseObject(loginResult);
                    log.info("=======asdUserTokenDetail返回参数refresh_token1：jsonresult【" + jsonresult + "】=======");
                    if (jsonresult.containsKey("error")) {
                        // 4.2.1.1、如果过期的refresh_token，需要重新登录
                        // 4.2.1.1.1、若采用SM2加密，则传入sm2加密标识
                        if (StringUtil.isNotBlank(customPwdEncrypt)
                                && ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                            loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                    + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                    + "&encrypttype=sm2" + "";
                        }
                        // 4.2.1.1.2、未采用sm2加密方式
                        else {
                            loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                    + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                    + "";
                        }
                        loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                                "application/x-www-form-urlencoded");
                        // 4.2.1.2、解析请求的返回值
                        jsonresult = JSONObject.parseObject(loginResult);
                        log.info("=======asdUserTokenDetail返回参数refresh_token：jsonresult2【" + jsonresult + "】=======");
                        if (jsonresult.containsKey("error")) {
                            return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
                        } else {
                            dataJson.put("token", jsonresult.get("access_token"));
                            auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                            iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                        }
                    } else {
                        dataJson.put("token", jsonresult.get("access_token"));
                        auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                    }
                }
                log.info("=======结束调用asdUserTokenDetailNew接口=======");
                return JsonUtils.zwdtRestReturn("1", " 通过asdUserTokenDetailNew获取用户信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======asdUserTokenDetailNew接口参数：params【" + params + "】=======");
            log.info("=======asdUserTokenDetailNew异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "爱山东app调用刷新用户token：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/addWtVisitReocrd", method = RequestMethod.POST)
    public String addWtVisitReocrd(@RequestBody String params) {
        try {
            log.info("=======开始调用addWtVisitReocrd接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JnVisitRecord record = new JnVisitRecord();
                record.setOperatedate(new Date());
                record.setRowguid(UUID.randomUUID().toString());
                record.setRecordtotal("1");
                iJnVisitRecordService.insert(record);

                log.info("=======结束调用addWtVisitReocrd接口=======");
                return JsonUtils.zwdtRestReturn("1", "用户访问成功", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addWtVisitReocrd接口参数：params【" + params + "】=======");
            log.info("=======addWtVisitReocrd异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "用户访问出现异常：" + e.getMessage(), "");
        }
    }

}

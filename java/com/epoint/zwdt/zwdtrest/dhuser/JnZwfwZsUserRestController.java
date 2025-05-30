package com.epoint.zwdt.zwdtrest.dhuser;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
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

@RestController
@RequestMapping("/jnzwfwZsUser")
public class JnZwfwZsUserRestController
{
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

    /**
     * 微信用户绑定接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/zsNewZsUserBind", method = RequestMethod.POST)
    public String zsForceUserBind(@RequestBody String params) {
        try {
            log.info("=======开始调用zsNewZsUserBind接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("zsNewZsUserBindde输入的params:"+jsonObject);
            String token = jsonObject.getString("token");
            JSONObject param = jsonObject.getJSONObject("params");
            String code = param.getString("body");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String data = null;
                try {
                    data = decryptDES2(code);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                //获取用户数据
                JSONObject json = JSONObject.parseObject(data);
                JSONObject datas = json.getJSONObject("data");
                //获取商户信息
                JSONObject corpInfo = datas.getJSONObject("corpInfo");
                //获取客户信息
                JSONObject customerInfo = datas.getJSONObject("customerInfo");
                //同微信openid
                String uniqueUserID = corpInfo.getString("uniqueUserID");
                //银行预留手机号
                String mobile2 = customerInfo.getString("mobile2");
                AuditOnlineRegister auditOnlineRegisterold = iAuditOnlineRegister.getRegisterByIdorMobile(mobile2).getResult();
                if (auditOnlineRegisterold != null) {
                    auditOnlineRegisterold.set("uniqueuserid", uniqueUserID);
                    iAuditOnlineRegister.updateRegister(auditOnlineRegisterold);
                }
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("uniqueuserid", uniqueUserID);
                List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
                        .selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
                if (auditOnlineRegisters != null && auditOnlineRegisters.size() > 0) {
                    auditOnlineRegisterold = auditOnlineRegisters.get(0);
                }
                if (auditOnlineRegisterold == null) {
                    // 判断是否采用sm2加密
                    String accountguid = UUID.randomUUID().toString();
                    // 3、插入个人基本信息表
                    AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
                    auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
                    auditOnlineIndividual.setAccountguid(accountguid);
                    auditOnlineIndividual.setContactmobile(mobile2);
                    auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
                    iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
                    // 3.1、插入用户注册表
                    AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
                    auditonlineregister.setRowguid(UUID.randomUUID().toString());
                    auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);//默认自然人
                    auditonlineregister.setAccountguid(accountguid);
                    auditonlineregister.setMobile(mobile2);
                    auditonlineregister.setLoginid(mobile2);
                    auditonlineregister.set("uniqueuserid", uniqueUserID);
                    auditonlineregister.setPassword(MD5Util.getMD5("111111"));//md5
                    iAuditOnlineRegister.addRegister(auditonlineregister);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("uniqueUserID", uniqueUserID);
                    dataJson.put("mobile", mobile2);
                    log.info("zsNewZsUserBindde输出的params:"+dataJson);
                    return JsonUtils.zwdtRestReturn("1", "绑定成功", dataJson);
                }
                else {
                    // 3.4、更新uniqueUserID,不考虑已绑定的情况，因为在上一步骤已经判断过
                    auditOnlineRegisterold.set("uniqueuserid", uniqueUserID);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("uniqueUserID", uniqueUserID);
                    dataJson.put("mobile", mobile2);
                    iAuditOnlineRegister.updateRegister(auditOnlineRegisterold);
                    log.info("zsNewZsUserBindde输出的params:"+dataJson);
                    return JsonUtils.zwdtRestReturn("1", "更新绑定成功", dataJson);
                }
                
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======zsNewZsUserBind接口参数：params【" + params + "】=======");
            log.info("=======zsNewZsUserBind异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "招商用户绑定出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 通过uniqueUserID获取用户信息
     * 
     * @param params 接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/zsUserDetailByuniqueUserID", method = RequestMethod.POST)
    public String zsUserDetailByuniqueUserID(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用zsUserDetailByuniqueUserID接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("zsUserDetailByuniqueUserID输入的params:"+jsonObject);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取用户招商唯一标识
                String uniqueUserID = obj.getString("uniqueUserID");
                if (StringUtil.isBlank(uniqueUserID)) {
                    return JsonUtils.zwdtRestReturn("0", "请传入正确的招商标识", "");
                }
                // 1.2、获取sm2加密配置参数
                String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
                
                // 2、根据uniqueUserID获取用户信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("uniqueuserid", uniqueUserID);
                List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
                        .selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
                if (auditOnlineRegisters.isEmpty()) {
                    return JsonUtils.zwdtRestReturn("0", "用户名不存在或者还未绑定", "");
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                AuditOnlineRegister auditOnlineRegister = auditOnlineRegisters.get(0);
                dataJson.put("username", auditOnlineRegister.getUsername()); //用户姓名
                dataJson.put("idnum", auditOnlineRegister.getIdnumber()); //身份证号码 
                dataJson.put("mobile", auditOnlineRegister.getMobile()); //手机号码
                String refresh_token = auditOnlineRegister.getRefreshtoken();
                String loginUrl = ""; // 请求token地址
                String loginResult = ""; // 请求token返回结果
                String urlroot = request.getRequestURL().toString();
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("interalIP"))) {
                    urlroot = ConfigUtil.getConfigValue("interalIP");
                }
                else {
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
                    if (jsonresult.containsKey("error")) {
                        return JsonUtils.zwdtRestReturn("0", "刷新token发生失败", "");
                    }
                    else {
                        dataJson.put("token", jsonresult.getString("access_token"));
                        String refreshToken = jsonresult.getString("refresh_token");
                        auditOnlineRegister.setRefreshtoken(refreshToken);
                        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                    }
                }
                else {
                    // 4.2、如果不为空，则用refresh_token去刷新，获取最新的access_token
                    loginUrl = "/rest/oauth2/token?grant_type=refresh_token&&client_id=zwdtClient&client_secret=zwdtSecret&refresh_token="
                            + refresh_token + "";
                    loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
                            "application/x-www-form-urlencoded");
                    // 4.2.1、解析请求的返回值
                    JSONObject jsonresult = JSONObject.parseObject(loginResult);
                    if (jsonresult.containsKey("error")) {
                        // 4.2.1.1、如果过期的refresh_token，需要重新登录
                        // 4.2.1.1.1、若采用SM2加密，则传入sm2加密标识
                        if (StringUtil.isNotBlank(customPwdEncrypt)
                                && ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                            loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
                                    + auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
                                    + "&encrypttype=sm2"+ "";
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
                        if (jsonresult.containsKey("error")) {
                            return JsonUtils.zwdtRestReturn("0", "刷新token发生失败", "");
                        }
                        else {
                            dataJson.put("token", jsonresult.get("access_token"));
                            auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                            iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                        }
                    }
                    else {
                        dataJson.put("token", jsonresult.get("access_token"));
                        auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
                        iAuditOnlineRegister.updateRegister(auditOnlineRegister);
                    }
                }
                log.info("=======结束调用zsUserDetailByuniqueUserID接口=======");
                log.info("zsUserDetailByuniqueUserID输出的params:"+dataJson);
                return JsonUtils.zwdtRestReturn("1", " 通过uniqueUserID获取用户信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======zsUserDetailByuniqueUserID接口参数：params【" + params + "】=======");
            log.info("=======zsUserDetailByuniqueUserID异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", " 通过uniqueUserID获取用户信息出现异常：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 
     *  更新客户信息
     *  @param params
     *  @param request
     *  @return    
     */
    @RequestMapping(value = "/UpdateOnlineRegister", method = RequestMethod.POST)
    public String UpdateOnlineRegister(@RequestBody String params, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(params);
        JSONObject param = json.getJSONObject("params");
        JSONObject dataJson = new JSONObject();
        String username = param.getString("username");
        String idnumber = param.getString("idnumber");
        String mobile = param.getString("mobile");
        String uniqueUserID = param.getString("uniqueUserID");
        try {
            //修改用户注册表
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("uniqueuserid", uniqueUserID);
            List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
                    .selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
            AuditOnlineRegister auditOnlineRegister = auditOnlineRegisters.get(0);
            auditOnlineRegister.setUsername(username);
            auditOnlineRegister.setIdnumber(idnumber);
            auditOnlineRegister.setMobile(mobile);
            iAuditOnlineRegister.updateRegister(auditOnlineRegister);
            //修改个人信息表
            AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual.getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
            auditOnlineIndividual.setClientname(username);
            auditOnlineIndividual.setIdnumber(idnumber);
            auditOnlineIndividual.setContactmobile(mobile);
            iAuditOnlineIndividual.updateIndividual(auditOnlineIndividual);
            return JsonUtils.zwdtRestReturn("1", "更新成功", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /*** 
     * 解密数据 
     * @param decryptString 
     * @param decryptKey 
     * @return 
     * @throws Exception 
     */
    public static String decryptDES2(String src) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec("cpiRKSvX".getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return IOUtils.toString(cipher.doFinal(Base64.decodeBase64(src)), "UTF-8");
    }

}

package com.epoint.zwdt.zwdtrest.user;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.basic.auditonlineuser.auditonlineverycode.inter.IAuditOnlineVeryCode;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.util.UserEncodeUtil;

@RestController
@RequestMapping("/jnzwdtRegister")
public class JnRegisterRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 网上注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 验证码API
     */
    @Autowired
    private IAuditOnlineVeryCode iAuditOnlineVeryCode;
    /**
     * 个人信息API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 待办消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;

    /**
     * 中心系统参数API
     */
    @Autowired
    private IHandleConfig handleConfigService;
    
    private static String PATTEN_REGEX_PHONE= "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";

    /**
     * 验证手机号码是否存在接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkPhoneExist", method = RequestMethod.POST)
    public String checkPhoneExist(@RequestBody String params) {
        try {
            log.info("=======开始调用checkPhoneExist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取手机号码
                String mobile = obj.getString("mobile");
                // 2、通过手机号获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByMobile(mobile).getResult();
                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("1", "手机号码不存在！", "");
                }
                else {
                    log.info("=======结束调用checkPhoneExist接口=======");
                    return JsonUtils.zwdtRestReturn("0", "手机号已经被使用", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkPhoneExist接口参数：params【" + params + "】=======");
            log.info("=======checkPhoneExist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检测手机号码是否存在出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断身份证号码是否存在接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkIdnumExist", method = RequestMethod.POST)
    public String checkIdnumExist(@RequestBody String params) {
        try {
            log.info("=======开始调用checkIdnumExist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取身份证号码
                String idnum = obj.getString("idnum").toUpperCase();
                // 2、通过身份证号码获取用户注册信息
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("idnumber", idnum);
                sql.eq("usertype", ZwfwConstant.APPLAYERTYPE_GR);
                List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
                        .selectAuditOnlineRegisterList(sql.getMap()).getResult();
                if (auditOnlineRegisters.isEmpty()) {
                    return JsonUtils.zwdtRestReturn("1", "身份证号码不存在！", "");
                }
                else {
                    log.info("=======结束调用checkIdnumExist接口=======");
                    return JsonUtils.zwdtRestReturn("0", "身份证号码存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkIdnumExist接口参数：params【" + params + "】=======");
            log.info("=======checkIdnumExist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检测身份证号码出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断用户名是否存在
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkUserNameExist", method = RequestMethod.POST)
    public String checkUserNameExist(@RequestBody String params) {
        try {
            log.info("=======开始调用checkUserNameExist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取用户名
                String userName = obj.getString("username");
                if (StringUtil.isNotBlank(userName)) {
                    userName = userName.toUpperCase();
                }
                // 2、通过用户名获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(userName)
                        .getResult();
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (auditOnlineRegister != null) {
                    dataJson.put("mobile", auditOnlineRegister.getMobile());
                    //dataJson.put("idnumber", auditOnlineRegister.getIdnumber());
                    if (userName.length() > 13) {
                        dataJson.put("type", "1"); // 身份证号码
                    }
                    else {
                        dataJson.put("type", "0"); // 手机号码
                    }
                    log.info("=======结束调用checkUserNameExist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "用户名存在！", dataJson.toString());
                }
                else {
                    log.info("=======结束调用checkUserNameExist接口=======");
                    return JsonUtils.zwdtRestReturn("0", "用户名不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkUserNameExist接口参数：params【" + params + "】=======");
            log.info("=======checkUserNameExist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检测手机号码是否存在出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     ** 获取手机验证码
     ** 用于修改密码页面获取手机验证码前先验证图片验证码，避免短信广播的安全漏洞
     */
    @RequestMapping(value = "/checkImageAndSendCode", method = RequestMethod.POST)
    public String checkImageAndSendCode(@RequestBody String params) {
        try {
            log.info("=======开始调用checkImageAndSendCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取手机号
                String mobile = obj.getString("mobile");
                // 1.2、获取验证码类型(1：注册、2：修改密码\修改绑定手机号码的验证码、3：二维码登录验证码、4：手机动态密码登录、5：管理者激活验证码)
                String type = obj.getString("type");
                // 1.3、获取企业标识
                String clientidentifier = obj.getString("companyid");
                // 1.4、图形验证码
                String verifyCode = obj.getString("verifycode");// 图形验证码
                // 1.5、图形验证码主键
                String codeRowguid = obj.getString("coderowguid");//
                // 2.1、验证图形验证码
                if (StringUtil.isNotBlank(codeRowguid)) {
                    if (StringUtil.isNotBlank(verifyCode)) {
                        // 2.2、判断是否有redis
                        AuditOnlineVerycode imageVerycode = null;
                        String imgkey = "img-" + verifyCode + "-" + codeRowguid;
                        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                            ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                            // 2.2.1、去redis中验证扫描信息
                            imageVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, imgkey, "6");
                            redisUtil.close();
                        }
                        else {
                            // 2.2.2、去mysql中验证扫描信息
                            imageVerycode = iAuditOnlineVeryCode.getVerycodeByRowguid(codeRowguid).getResult();
                        }
                        // 3、判断图形验证码不为空
                        if (imageVerycode != null && StringUtil.isNotBlank(imageVerycode.getVerifycode())) {
                            // 3.1、判断验证码是否正确(忽略大小写)
                            if (imageVerycode.getVerifycode().equalsIgnoreCase(verifyCode)) {
                                // 3.2、判断验证码是否已失效
                                if ("1".equals(imageVerycode.getIsverified())) {
                                    return JsonUtils.zwdtRestReturn("0", "图片验证码已失效，请重新获取", "");
                                }
                                else {
                                    // 3.3.1、设置图片验证码为已使用
                                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                                        ZwfwRedisCacheUtil updateRedisUtil = new ZwfwRedisCacheUtil(false);
                                        updateRedisUtil.updateByHashzwdtvcode(AuditOnlineVerycode.class, imgkey, "6",
                                                "isverified", "1");
                                        updateRedisUtil.close();
                                    }
                                    else {
                                        imageVerycode.setIsverified("1");
                                        iAuditOnlineVeryCode.updateAuditOnlineVerycode(imageVerycode);
                                    }
                                    // 3.3.2、发送手机验证码
                                    // 3.3.2.1、如果是手机验证码登录则需要先判断手机是否注册过
                                    AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister
                                            .getRegisterByMobile(mobile).getResult();
                                    if (ZwdtConstant.VERIFYKIND_LOGINBYCODE.equals(type)) {
                                        if (auditOnlineRegister == null) {
                                            return JsonUtils.zwdtRestReturn("0", "请先用该手机号码完成注册！", "");
                                        }
                                    }
                                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                                    // 3.3.2.2、安全性问题处理，处理短信注册/找回密码导致的短信轰炸问题(间隔时间60s)
                                    AuditOnlineVerycode judgeAuditOnlineVerycode = null;
                                    Date judgeTime = new Date();
                                    // 3.3.2.2.1、获取对应类型验证码的最近一条记录
                                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                                        ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                                        judgeAuditOnlineVerycode = redisUtil
                                                .getByHashzwdtvcode(AuditOnlineVerycode.class, mobile, type);
                                        redisUtil.close();
                                    }
                                    else {
                                        judgeAuditOnlineVerycode = iAuditOnlineVeryCode.getVerifyCode(mobile, "0", type)
                                                .getResult();
                                    }
                                    if (judgeAuditOnlineVerycode != null) {
                                        if ((judgeTime.getTime() - judgeAuditOnlineVerycode.getInsertime().getTime())
                                                / 1000 < 60) {
                                            return JsonUtils.zwdtRestReturn("0", "请勿短时间内多次发送短信！", "");
                                        }
                                    }
                                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                                    // 3.4、设置验证码相关信息
                                    AuditOnlineVerycode auditOnlineVerycode = new AuditOnlineVerycode();
                                    auditOnlineVerycode.setRowguid(UUID.randomUUID().toString());
                                    auditOnlineVerycode.setMobile(mobile);// 设置手机号
                                    String code = JsonUtils.bulidCheckCode(6);// 设置验证码6位随机数
                                    auditOnlineVerycode.setVerifycode(code);
                                    auditOnlineVerycode.setInsertime(new Date());// 设置插入时间
                                    auditOnlineVerycode.setValiditytime(setValidTime(auditOnlineVerycode, 5));// 设置有效期，60秒
                                    auditOnlineVerycode.setIsverified("0");// 是否被验证通过
                                    auditOnlineVerycode.setVerifykind(type); // 设置验证码类型
                                    auditOnlineVerycode.setClientidentifier(clientidentifier);// 业务guid
                                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                                        // 3.4.1、若系统配置了redis，则将验证码存入redis
                                        ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                                        redisUtil.putByHashzwdtvcode(mobile, type, auditOnlineVerycode, 300);
                                        redisUtil.close();
                                    }
                                    else {
                                        // 3.4.2、若系统未配置redis，则将验证码存入数据库
                                        iAuditOnlineVeryCode.addAuditOnlineVerycode(auditOnlineVerycode);
                                    }
                                    // 3.5、MessageCenter插数据发短信
                                    // 3.5.1、获取目标用户标识与目标用户名称
                                    String targetUser = "大厅注册";
                                    String targetDisplayName = "大厅注册";
                                    if (!ZwdtConstant.VERIFYKIND_REGISTER.equals(type)) {
                                        if (auditOnlineRegister != null) {
                                            targetUser = auditOnlineRegister.getRowguid();
                                            targetDisplayName = auditOnlineRegister.getUsername();
                                        }
                                    }
                                    // 3.5.2、往messages_center表里插数据
                                    String content = "您的短信验证码是：【" + code + "】,请注意短信验证码有效期为五分钟！";// 短信内容
                                    iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content,
                                            new Date(), 0, null, mobile, targetUser, targetDisplayName, "", "", "", "",
                                            null, false, "短信");
                                    log.info("=======结束调用sendCode接口=======");
                                    return JsonUtils.zwdtRestReturn("1", "验证码发送成功！", "");
                                }
                            }
                            else {
                                return JsonUtils.zwdtRestReturn("0", "图片验证码输入错误", "");
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "获取图片验证码失败", "");
                        }

                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "请填写图片验证码！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取图片验证码失败", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkImageAndSendCode接口参数：params【" + params + "】=======");
            log.info("=======checkImageAndSendCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证码发送出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 验证码发送接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public String sendCode(@RequestBody String params) {
        try {
            log.info("=======开始调用sendCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取手机号
                String mobile = obj.getString("mobile");
                // 1.2、获取验证码类型(1：注册、2：修改密码\修改绑定手机号码的验证码、3：二维码登录验证码、4：手机动态密码登录、5：管理者激活验证码)
                String type = obj.getString("type");
                // 1.3、获取企业标识
                String clientidentifier = obj.getString("companyid");
               //加入手机号格式验证
                /*if (!mobile.matches(PATTEN_REGEX_PHONE)) {
                    return JsonUtils.zwdtRestReturn("0", "请输入正确的手机号！", "");
                }*/
                //加入类型限制验证，如果传入类型不对，直接返回提示
                if(StringUtil.isBlank(type)||!(ZwdtConstant.VERIFYKIND_REGISTER.equals(type)||ZwdtConstant.VERIFYKIND_MODIFYPWORMOBILE.equals(type)||ZwdtConstant.VERIFYKIND_SCANLOGIN.equals(type)||ZwdtConstant.VERIFYKIND_LOGINBYCODE.equals(type)||ZwdtConstant.VERIFYKIND_MACTIVATE.equals(type)||"6".equals(type))){
                    return JsonUtils.zwdtRestReturn("0", "传入获取验证码类型错误！", "");
                }
                
                // 2、如果是手机验证码登录则需要先判断手机是否注册过
                AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByMobile(mobile).getResult();
                
                if (ZwdtConstant.VERIFYKIND_LOGINBYCODE.equals(type)) {
                    if (auditOnlineRegister == null) {
                        return JsonUtils.zwdtRestReturn("0", "请先用该手机号码完成注册！", "");
                    }
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、安全性问题处理，处理短信注册/找回密码导致的短信轰炸问题(间隔时间60s)
                AuditOnlineVerycode judgeAuditOnlineVerycode = null;
                Date judgeTime = new Date();
                // 3.1、获取对应类型验证码的最近一条记录
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                	ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        //有多个redis操作需要执行,选择自己控制连接关闭
                    	redisUtil = new ZwfwRedisCacheUtil(false);
                        judgeAuditOnlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, mobile, type);
                    }
                    //finally中必须关闭
                    finally {
                        if (redisUtil != null) {
                        	redisUtil.close();
                        }
                    }
                }
                else {
                    judgeAuditOnlineVerycode = iAuditOnlineVeryCode.getVerifyCode(mobile, "0", type).getResult();
                }
                if (judgeAuditOnlineVerycode != null) {
                    if ((judgeTime.getTime() - judgeAuditOnlineVerycode.getInsertime().getTime()) / 1000 < 60) {
                        return JsonUtils.zwdtRestReturn("0", "请勿短时间内多次发送短信！", "");
                    }
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 4、设置验证码相关信息
                AuditOnlineVerycode auditOnlineVerycode = new AuditOnlineVerycode();
                auditOnlineVerycode.setRowguid(UUID.randomUUID().toString());
                auditOnlineVerycode.setMobile(mobile);// 设置手机号
                String code = JsonUtils.bulidCheckCode(6);// 设置验证码6位随机数
                auditOnlineVerycode.setVerifycode(code);
                auditOnlineVerycode.setInsertime(new Date());// 设置插入时间
                auditOnlineVerycode.setValiditytime(setValidTime(auditOnlineVerycode, 5));// 设置有效期，60秒
                auditOnlineVerycode.setIsverified("0");// 是否被验证通过
                auditOnlineVerycode.setVerifykind(type); // 设置验证码类型
                auditOnlineVerycode.setClientidentifier(clientidentifier);//业务guid
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 4.1、若系统配置了redis，则将验证码存入redis
                	ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        //有多个redis操作需要执行,选择自己控制连接关闭
                    	redisUtil = new ZwfwRedisCacheUtil(false);
                    	redisUtil.putByHashzwdtvcode(mobile, type, auditOnlineVerycode, 300);
                    }
                    //finally中必须关闭
                    finally {
                        if (redisUtil != null) {
                        	redisUtil.close();
                        }
                    }
                }
                else {
                    // 4.2、若系统未配置redis，则将验证码存入数据库
                    iAuditOnlineVeryCode.addAuditOnlineVerycode(auditOnlineVerycode);
                }
                // 5、MessageCenter插数据发短信
                // 5.1、获取目标用户标识与目标用户名称
                String targetUser = "大厅注册";
                String targetDisplayName = "大厅注册";
                if (!ZwdtConstant.VERIFYKIND_REGISTER.equals(type)) {
                    if (auditOnlineRegister != null) {
                        targetUser = auditOnlineRegister.getRowguid();
                        targetDisplayName = auditOnlineRegister.getUsername();
                    }
                }
                // 5.2、往messages_center表里插数据
                String content = "您的短信验证码是：【" + code + "】,请注意短信验证码有效期为五分钟！";//短信内容
                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
                        mobile, targetUser, targetDisplayName, "", "", "", "", null, false, "370827");
                log.info("=======结束调用sendCode接口=======");
                return JsonUtils.zwdtRestReturn("1", "验证码发送成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======sendCode接口参数：params【" + params + "】=======");
            log.info("=======sendCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证码发送出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 注册接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody String params) {
        log.info("=======开始调用register接口=======");
        try {
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取注册用户名
                String userName = obj.getString("username");
                // 1.2、获取密码
                String passKey = obj.getString("password");
                // 1.3、获取手机号
                String mobile = obj.getString("mobile");
                // 1.4、获取验证码
                String code = obj.getString("code");
                // 1.5、获取身份证
                String idnum = obj.getString("idnum");
                // 1.6、获取密码强度
                String passlevel = obj.getString("passlevel");
                idnum = StringUtil.isNotBlank(idnum) ? idnum.toUpperCase() : idnum;
                String accountguid = UUID.randomUUID().toString();
                // 1.7、获取sm2加密配置参数
                String customPwdEncrypt = handleConfigService.getFrameConfig("sm2_EncryptPwd", "").getResult();
                // 正常注册还是第三方跳转过来的注册 3代表 第三方注册
                // String registertype = params.getString("registertype");
                // 第三方注册方式 wx 微信 wb 微博 qq扣扣
                // String thirdtype = params.getString("hhirdtype");
                // 第三方注册guid
                // String thirdguid = params.getString("hhirdguid");
                // 1.8 SM2加密方式时，打开注释用于SM2解密
                SM2Util sm2Util = new SM2Util();
                try {
                    // 判断一下是否加密了
                    if (StringUtil.isNotBlank(passKey) && passKey.length() > 40) {
                        passKey = sm2Util.decrypt(passKey);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                // 2、验证身份证号码是否已被注册
                AuditOnlineRegister auditOnlineRegisterIdNum = iAuditOnlineRegister.getRegisterByIdNumber(idnum)
                        .getResult();
                if (auditOnlineRegisterIdNum != null) {
                    // 返回数据
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("moble", auditOnlineRegisterIdNum.getMobile());
                    dataJson.put("username", idnum);
                    log.info("=======结束调用register接口=======");
                    return JsonUtils.zwdtRestReturn("0", "该身份证号码已经注册！", dataJson.toString());
                }
                // 3、验证手机号是否被注册
                AuditOnlineRegister auditOnlineRegisterMobile = iAuditOnlineRegister.getRegisterByMobile(mobile)
                        .getResult();
                if (auditOnlineRegisterMobile != null) {
                    // 返回数据
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("moble", mobile);
                    dataJson.put("username", auditOnlineRegisterMobile.getIdnumber());
                    log.info("=======结束调用register接口=======");
                    return JsonUtils.zwdtRestReturn("0", "该手机号已经被注册！", dataJson.toString());
                }
                // 4、获取验证码
                AuditOnlineVerycode auditOnlineVerycode = null;
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 4.1、如果系统配置了redis，则从redis中获取
                    ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil(false);
                    auditOnlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, mobile, "1");
                    redisUtil.close();
                }
                else {
                    // 4.2、如果系统未配置redis，则从数据库中获取
                    auditOnlineVerycode = iAuditOnlineVeryCode.getVerifyCode(mobile,
                            EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), "0", "1")
                            .getResult();
                }
                // 5、判断验证码是否正确
                if (auditOnlineVerycode == null) {
                    // 5.1、验证码
                    return JsonUtils.zwdtRestReturn("0", "验证码不存在或者已经过期！", "");
                }
                else {
                    if (!code.equals(auditOnlineVerycode.getVerifycode())) {
                        return JsonUtils.zwdtRestReturn("0", "验证码错误！", "");
                    }
                    // 5.2、验证码验证成功后删除
                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                        ZwfwRedisCacheUtil redisCacheUtil = new ZwfwRedisCacheUtil(false);
                        redisCacheUtil.delByHashzwdtvcode(AuditOnlineVerycode.class, mobile, "1");
                        redisCacheUtil.close();
                    }
                    else {
                        iAuditOnlineVeryCode.deleteAuditOnlineVerycode(auditOnlineVerycode);
                    }
                    // 5.3、插入个人基本信息表
                    AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
                    auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
                    auditOnlineIndividual.setAccountguid(accountguid);
                    auditOnlineIndividual.setClientname(userName);
                    auditOnlineIndividual.setIdnumber(idnum);
                    auditOnlineIndividual.setContactmobile(mobile);
                    auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
                    iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
                    // 5.4、插入用户注册表
                    // 5.4.1、sm2加密密文
                    if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
                        UserEncodeUtil userEncodeUtil = new UserEncodeUtil();
                        passKey = userEncodeUtil.encryption(passKey, idnum.toUpperCase());
                    }
                    AuditOnlineRegister auditOnlineRegister = new AuditOnlineRegister();
                    auditOnlineRegister.setRowguid(UUID.randomUUID().toString());
                    auditOnlineRegister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);//默认自然人
                    auditOnlineRegister.setAccountguid(accountguid);
                    auditOnlineRegister.setPassword(passKey);
                    auditOnlineRegister.setMobile(mobile);
                    auditOnlineRegister.setUsername(userName);
                    auditOnlineRegister.setIdnumber(idnum);
                    auditOnlineRegister.setPwdlevel(passlevel);
                    iAuditOnlineRegister.addRegister(auditOnlineRegister);
                    // 6、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("moble", mobile);
                    dataJson.put("username", idnum);
                    log.info("=======结束调用register接口=======");
                    return JsonUtils.zwdtRestReturn("1", "注册成功！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======register接口参数：params【" + params + "】=======");
            log.info("=======register异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "注册出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     *  获取发送的验证码接口
     * 
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getZwdtVeryCode", method = RequestMethod.POST)
    public String getZwdtVeryCode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getZwdtVeryCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 2、获取所有验证码数据
                List<JSONObject> codeJsonList = new ArrayList<JSONObject>();
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.le("length(verifycode)", "6");
                PageData<AuditOnlineVerycode> pageData = iAuditOnlineVeryCode
                        .getVerifyCodeList(sqlConditionUtil.getMap(), Integer.parseInt(currentPage),
                                Integer.parseInt(pageSize))
                        .getResult();
                // 3、返回获取到的验证码数据
                for (AuditOnlineVerycode auditOnlineVerycode : pageData.getList()) {
                    JSONObject codeJson = new JSONObject();
                    codeJson.put("verifycode", auditOnlineVerycode.getVerifycode()); // 验证码
                    codeJson.put("mobile", auditOnlineVerycode.getMobile()); // 手机号
                    codeJson.put("insertime", EpointDateUtil.convertDate2String(auditOnlineVerycode.getInsertime(),
                            EpointDateUtil.DATE_TIME_FORMAT)); //  插入时间
                    codeJsonList.add(codeJson);
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("totalcount", pageData.getRowCount());
                dataJson.put("codelist", codeJsonList);
                return JsonUtils.zwdtRestReturn("1", "返回验证码内容成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "返回验证码内容失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getZwdtVeryCode接口参数：params【" + params + "】=======");
            log.info("=======getZwdtVeryCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "返回验证码内容失败：" + e.getMessage(), "");
        }
    }

    /**
     * 设置验证码有效期
     * 
     * @param verycode 验证码对象
     * @param time 有效期时间分钟
     * @return
     */
    public Date setValidTime(AuditOnlineVerycode verycode, int time) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(verycode.getInsertime());
        instance.add(Calendar.MINUTE, time);
        return instance.getTime();
    }
}

package com.epoint.auditselfservice.auditselfservicerest.common;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbuserinfoextend.inter.IAuditZnsbUserinfoextend;
import com.epoint.basic.auditqueue.selflogin.domain.AuditZnsbSelflogin;
import com.epoint.basic.auditqueue.selflogin.inter.IAuditZnsbSelfloginService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;

@RestController
@RequestMapping("/jnselfserviceuser")
public class JNUserRestController
{

    @Autowired
    private IAuditOnlineRegister registerservice;

    @Autowired
    private IAuditOnlineIndividual individualservice;

    @Autowired
    private IHandleConfig handleConfig;
    /**
     * 验证码API
     */
    @Autowired
    private IAuditOnlineVeryCode iAuditOnlineVeryCode;

    /**
     * 待办消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;

    @Autowired
    private IAuditZnsbSelfloginService auditZnsbSelfloginService;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditZnsbUserinfoextend userinfoextendservice;

    @Autowired
    private IConfigService configServcie;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleRelationService roleRelationService;

    @Autowired
    private IUserService userService;

    /**
     * 一体机登录记录实体对象
     */
    private AuditZnsbSelflogin dataBean;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static final String APP_NAME = "自助服务终端"; // 应用名称

    public static final String CONSTANT_STR_TWENTY = "20";

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
                // 2、如果是手机验证码登录则需要先判断手机是否注册过
                AuditOnlineRegister auditOnlineRegister = registerservice.getRegisterByMobile(mobile).getResult();
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
                        redisUtil = new ZwfwRedisCacheUtil(false);
                        judgeAuditOnlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, mobile,
                                type);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("redis执行发生了异常", e);
                    }
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
                String NORMALPHONE = configServcie.getFrameConfigValue("AS_ZNSB_YTJ_NORMALPHONE");
                if (StringUtil.isNotBlank(NORMALPHONE) && StringUtil.isNotBlank(mobile) && NORMALPHONE.equals(mobile)) {
                    code = configServcie.getFrameConfigValue("AS_ZNSB_YTJ_NORMALPHONECODE");
                }
                auditOnlineVerycode.setVerifycode(code);
                auditOnlineVerycode.setInsertime(new Date());// 设置插入时间
                auditOnlineVerycode.setValiditytime(setValidTime(auditOnlineVerycode, 5));// 设置有效期，60秒
                auditOnlineVerycode.setIsverified("0");// 是否被验证通过
                auditOnlineVerycode.setVerifykind(type); // 设置验证码类型
                auditOnlineVerycode.setClientidentifier(clientidentifier);// 业务guid
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    // 4.1、若系统配置了redis，则将验证码存入redis
                    ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        redisUtil = new ZwfwRedisCacheUtil(false);
                        redisUtil.putByHashzwdtvcode(mobile, type, auditOnlineVerycode, 300);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("redis执行发生了异常", e);
                    }
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
                String content = "您的短信验证码是：【" + code + "】,请注意短信验证码有效期为五分钟！";// 短信内容
                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
                        mobile, targetUser, targetDisplayName, "", "", "", "", null, false, "短信");
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

    @RequestMapping(value = "/loginbysendCode", method = RequestMethod.POST)
    public String loginbysendCode(@RequestBody String params) {
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
                String code = obj.getString("code");

                String centerguid = obj.getString("centerguid");
                String macaddress = obj.getString("macaddress");
                // 2、如果是手机验证码登录则需要先判断手机是否注册过
                AuditOnlineRegister auditOnlineRegister = registerservice.getRegisterByMobile(mobile).getResult();
                if (ZwdtConstant.VERIFYKIND_LOGINBYCODE.equals(type)) {
                    if (auditOnlineRegister == null) {
                        return JsonUtils.zwdtRestReturn("0", "请先用该手机号码完成注册！", "");
                    }
                }
                JSONObject dataJson = new JSONObject();
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、安全性问题处理，处理短信注册/找回密码导致的短信轰炸问题(间隔时间60s)
                AuditOnlineVerycode judgeAuditOnlineVerycode = null;
                Date judgeTime = new Date();
                // 3.1、获取对应类型验证码的最近一条记录
                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                    ZwfwRedisCacheUtil redisUtil = null;
                    try {
                        redisUtil = new ZwfwRedisCacheUtil(false);
                        judgeAuditOnlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, mobile,
                                type);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("redis执行发生了异常", e);
                    }
                    finally {
                        if (redisUtil != null) {
                            redisUtil.close();
                        }
                    }
                }
                else {
                    judgeAuditOnlineVerycode = iAuditOnlineVeryCode.getVerifyCode(mobile, "0", type).getResult();
                }
                String NORMALCODE = handleConfig.getFrameConfig("AS_ZNSB_YTJ_NORMALCODE", centerguid).getResult();
                if (judgeAuditOnlineVerycode != null) {
                    if ((judgeTime.getTime() - judgeAuditOnlineVerycode.getInsertime().getTime()) / 1000 < 300
                            && (judgeAuditOnlineVerycode.getVerifycode().equals(code) || code.equals(NORMALCODE))) {
                        String idnumber = "";
                        String clientname = "";
                        String applyerguid = "";

                        judgeAuditOnlineVerycode.setIsverified("1");
                        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                            // 4.1、若系统配置了redis，则将验证码存入redis
                            ZwfwRedisCacheUtil redisUtil = null;
                            try {
                                redisUtil = new ZwfwRedisCacheUtil(false);
                                redisUtil.putByHashzwdtvcode(mobile, type, judgeAuditOnlineVerycode, 300);
                            }
                            catch (Exception e) {
                                throw new RuntimeException("redis执行发生了异常", e);
                            }
                            finally {
                                if (redisUtil != null) {
                                    redisUtil.close();
                                }
                            }
                        }
                        else {
                            // 4.2、若系统未配置redis，则将验证码存入数据库
                            iAuditOnlineVeryCode.updateAuditOnlineVerycode(judgeAuditOnlineVerycode);
                        }
                        String accountguid = auditOnlineRegister.getAccountguid();
                        String usertype = auditOnlineRegister.getUsertype();
                        String loginId = auditOnlineRegister.getLoginid();
                        dataJson.put("accountguid", accountguid);
                        dataJson.put("usertype", usertype);
                        dataJson.put("mobile", mobile);
                        dataJson.put("loginid", loginId);

                        AuditOnlineIndividual individual = individualservice.getIndividualByAccountGuid(accountguid)
                                .getResult();
                        if (individual != null) {
                            idnumber = individual.getIdnumber();
                            clientname = individual.getClientname();
                            applyerguid = individual.getApplyerguid();
                            // 登录记录保存
                            dataBean = new AuditZnsbSelflogin();
                            dataBean.setRowguid(UUID.randomUUID().toString());
                            dataBean.setLoginname(clientname);
                            dataBean.setIdcard(idnumber);
                            dataBean.setLogintime(new Date());
                            dataBean.setLogintype("4");
                            dataBean.setMacaddress(macaddress);
                            dataBean.setCenterguid(centerguid);
                            auditZnsbSelfloginService.insert(dataBean);
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "该账户不存在！请先刷身份证完成账户注册", "");
                        }
                        // 获取用户身份证照片,住址
                        AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(idnumber).getResult();
                        if (StringUtil.isNotBlank(auditQueueUserinfo)
                                && StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())) {
                            dataJson.put("photobase64", Base64Util.encode(auditQueueUserinfo.getPhoto()));
                            dataJson.put("address", auditQueueUserinfo.getAddress());
                        }
                        else {
                            dataJson.put("photobase64", "");
                        }

                        dataJson.put("idnumber", idnumber);
                        dataJson.put("clientname", clientname);
                        dataJson.put("applyerguid", applyerguid);

                        return JsonUtils.zwdtRestReturn("1", "", dataJson);

                    }
                }

                log.info("=======结束调用sendCode接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
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

    public Date setValidTime(AuditOnlineVerycode verycode, int time) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(verycode.getInsertime());
        instance.add(Calendar.MINUTE, time);
        return instance.getTime();
    }

}

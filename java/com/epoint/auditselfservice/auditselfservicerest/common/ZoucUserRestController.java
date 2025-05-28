package com.epoint.auditselfservice.auditselfservicerest.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbselfmachineproject.domain.AuditZnsbSelfmachineproject;
import com.epoint.basic.auditqueue.selflogin.domain.AuditZnsbSelflogin;
import com.epoint.basic.auditqueue.selflogin.inter.IAuditZnsbSelfloginService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.sm2.SM2EncryptAndDecryptImpl;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

@RestController
@RequestMapping("/zoucselfserviceuser")
public class ZoucUserRestController {

    @Autowired
    private IAuditOnlineRegister registerservice;

    @Autowired
    private IAuditOnlineIndividual individualservice;

    @Autowired
    private IAuditZnsbSelfloginService auditZnsbSelfloginService;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IConfigService configServcie;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditProject projectservice;

    @Autowired
    private IAuditOrgaWindow windowservice;

    @Autowired
    private IAuditTask taskservice;

    @Autowired
    private IAuditOnlineProject onlineProjectservice;

    @Autowired
    private IAuditTaskExtension taskExtensionservice;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IRoleService roleservice;

    @Autowired
    private IMessagesCenterService messageservice;
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

    /**
     * 登录
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String loginid = obj.getString("loginid");
            String password = obj.getString("password");
            String logintype = obj.getString("logintype");// 1代表刷卡登录，无需输入密码；2代表普通登录；3APP扫码登录
            String macaddress = obj.getString("macaddress");
            String centerguid = obj.getString("centerguid");

            String sex = obj.getString("sex");
            String nation = obj.getString("Nation");
            String born = obj.getString("Born");
            String address = obj.getString("Address");
            String picture = obj.getString("picture");

            String accountguid = "";
            String usertype = "";
            String mobile = "";
            String loginId = "";
            String idnumber = "";
            String clientname = "";
            String applyerguid = "";
            String isneedupload = "";
            // SM2加密方式时，打开注释用于SM2解密
            SM2Util sm2Util = new SM2Util();
            try {
                // 判断一下是否加密了
                if (StringUtil.isNotBlank(password) && password.length() > 40) {
                    password = sm2Util.decrypt(password);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            password = EncodeUtil.decodeByJs(password);

            loginid = StringUtil.toUpperCase(loginid);
            AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(loginid).getResult();
            if (register != null) {
                // 判断是身份证还是手机号
                if (loginid.length() <= 11) {
                    loginid = StringUtil.toUpperCase(register.getIdnumber());
                }

                if ("2".equals(logintype)) {
                    // 普通登录，验证密码
                    String sm2_EncryptPwd = configServcie.getFrameConfigValue("sm2_EncryptPwd");
                    String dbPassword = register.getPassword();
                    if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(sm2_EncryptPwd)) {
                        SM2EncryptAndDecryptImpl userEncodeUtil = new SM2EncryptAndDecryptImpl();
                        password = userEncodeUtil.encryption(password, loginid);

                        // 由于框架为了区分密码加密方式，在密码密文前添加前缀用于区分，所以需要截取下
                        if (StringUtil.isNotBlank(dbPassword) && dbPassword.indexOf('}') > 0) {
                            dbPassword = dbPassword.substring(dbPassword.indexOf('}') + 1);
                        }
                        if (!password.equals(dbPassword)) {
                            return JsonUtils.zwdtRestReturn("0", "登录密码不正确！", "");
                        }

                    } else {
                        if (!password.equals(dbPassword)) {
                            return JsonUtils.zwdtRestReturn("0", "登录密码不正确！", "");
                        }
                    }
                } else if ("1".equals(logintype) && StringUtil.isNotBlank(picture)) {
                    // 刷身份证登陆 更新身份证信息
                    byte[] pic = Base64Util.decodeBuffer(picture);
                    // 更新人员信息表的数据
                    userinfoservice.insertuserinfo(register.getIdnumber(), register.getUsername(), sex, nation, born,
                            address, pic);

                }

                accountguid = register.getAccountguid();
                usertype = register.getUsertype();
                mobile = register.getMobile();
                loginId = register.getLoginid();
                String isfingeruoload = handleConfigservice.getFrameConfig("AS_ZNSB_ISFINGERUPLOAD", centerguid)
                        .getResult();
                if (StringUtil.isNotBlank(register.getBytes("fingerphoto")) && !"0".equals(isfingeruoload)) {
                    isneedupload = "1";
                }

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
                    dataBean.setLogintype(logintype);
                    dataBean.setMacaddress(macaddress);
                    dataBean.setCenterguid(centerguid);
                    auditZnsbSelfloginService.insert(dataBean);
                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "该账户不存在！请先刷身份证完成账户注册", "");
            }
            // 获取用户身份证照片,住址
            AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(idnumber).getResult();
            if (StringUtil.isNotBlank(auditQueueUserinfo) && StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())) {
                dataJson.put("photobase64", Base64Util.encode(auditQueueUserinfo.getPhoto()));
                dataJson.put("address", auditQueueUserinfo.getAddress());
            } else {
                dataJson.put("photobase64", "");
            }

            dataJson.put("accountguid", accountguid);
            dataJson.put("usertype", usertype);
            dataJson.put("mobile", mobile);
            dataJson.put("loginid", loginId);
            dataJson.put("idnumber", idnumber);
            dataJson.put("clientname", clientname);
            dataJson.put("applyerguid", applyerguid);
            dataJson.put("isneedupload", isneedupload);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }

    }

    /**
     * 亮证登录
     *
     * @params params
     * @return
     *
     *
     */
    /*
     * @RequestMapping(value = "/ewmlogin", method = RequestMethod.POST)
     * public String ewmlogin(@RequestBody String params) {
     * try {
     * JSONObject json = JSON.parseObject(params);
     * JsonUtils.checkUserAuth(json.getString("token"));
     * JSONObject obj = (JSONObject) json.get("params");
     * JSONObject dataJson = new JSONObject();
     * String ewm = obj.getString("ewm");
     * //system.out.println(ewm);
     * String appkey = ConfigUtil.getConfigValue("sdkconfig", "appkey");
     * String sm2_prikey = ConfigUtil.getConfigValue("sdkconfig", "appprikey");
     *
     * if (StringUtil.isNotBlank(ewm)) {
     * JSONObject newdata = JSONObject.parseObject(ewm);
     * // 根据密文主键调取接口获得密文内容
     * String data1 =
     * CertCommonRestService.getInstance().decrypt(newdata.getString("token"));
     * //system.out.println(data1);
     * if ("314".equals(data1)) {
     * return JsonUtils.zwdtRestReturn("0", "二维码已失效，请刷新重试！", dataJson);
     * } else if ("315".equals(data1)) {
     * return JsonUtils.zwdtRestReturn("0", "二维码已扫描，请勿重新操作！", dataJson);
     * } else {
     * JSONObject jiexi = JSONObject.parseObject(data1);
     * // 获取证照相关参数
     * String certcatalogid = jiexi.getString("certcatalogid");
     * String certownercertno = jiexi.getString("certownercertno");
     * String certinfoguid = jiexi.getString("certinfoguid");
     * String grantguid = jiexi.getString("grantguid");
     * String projectGuid = UUID.randomUUID().toString();
     * // 1.1授权
     * CertAuthorityRestService.getInstance().acceptSysAuthorization(grantguid,
     * "自助服务终端", "自助服务终端",
     * "自助服务终端", APP_NAME, appkey, projectGuid, "自助服务终端");
     * // 1.2获取证照实体
     * JSONObject certdate =
     * CertInfoRestService.getInstance().selectCertInfo(APP_NAME, projectGuid,
     * certownercertno, certcatalogid, certinfoguid, certownercertno);
     * String certinfostr = certdate.getString("certinfoextension");
     * //system.out.println(certinfostr);
     * String cliengguid = "";
     * JSONObject sfz = new JSONObject();
     * if (StringUtil.isNotBlank(certinfostr)) {
     * JSONObject certinfoobj = JSONObject.parseObject(certinfostr);
     * cliengguid = certinfoobj.getString("zp");
     * //system.out.println(cliengguid);
     * // 1.3获取证照附件
     * JSONObject attachList =
     * CertAttachRestService.getInstance().getAttachList(projectGuid,
     * certcatalogid, certinfoguid, cliengguid, "");
     * //system.out.println(attachList);
     * String attactinfo =
     * DataEncryptUtil.asymmetricDecryptData(attachList.getString("attachlist"),
     * null, sm2_prikey);
     * // 1.4将查到的附件下载到自己库中
     * List<JSONObject> resultJson = JsonUtil.jsonToList(attactinfo,
     * JSONObject.class);
     * if (StringUtil.isNotBlank(resultJson)) {
     * String attachurl = resultJson.get(0).getString("attachurl");
     * JSONObject file = ProjectConstant.filePathToInputStream(attachurl);
     * InputStream in = (InputStream) file.get("inputStream");
     * ByteArrayOutputStream output = new ByteArrayOutputStream();
     * byte[] buffer = new byte[4096];
     * int n = 0;
     * try {
     * while (-1 != (n = in.read(buffer))) {
     * output.write(buffer, 0, n);
     * }
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * String pic = Base64Util.encode(output.toByteArray());
     * sfz.put("pic", pic);
     * }
     *
     * sfz.put("code", certinfoobj.getString("cardid"));
     * sfz.put("gender", certinfoobj.getString("sex"));
     * sfz.put("folk", certinfoobj.getString("nation"));
     * sfz.put("birthday", certinfoobj.getString("nation"));
     * sfz.put("folk", certinfoobj.getString("year") + "-" +
     * certinfoobj.getString("month") + "-"
     * + certinfoobj.getString("day"));
     * sfz.put("address", certinfoobj.getString("address"));
     * sfz.put("name", certinfoobj.getString("name"));
     * dataJson.put("sfz", sfz);
     * } else {
     * return JsonUtils.zwdtRestReturn("0", "未获取到附件", dataJson);
     * }
     *
     * }
     *
     * }
     * return JsonUtils.zwdtRestReturn("1", "", dataJson);
     * } catch (JSONException e) {
     * return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
     * }
     *
     * }
     */

    /**
     * 验证并补齐身份证信息
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/checksfzinfo", method = RequestMethod.POST)
    public String checksfzinfo(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String loginid = obj.getString("loginid");

            String idnum = obj.getString("idnum");
            String sex = obj.getString("sex");
            String nation = obj.getString("nation");
            String born = obj.getString("born");
            String address = obj.getString("address");
            String picture = obj.getString("picture");
            String centerguid = obj.getString("centerguid");
            String accountguid = "";
            String usertype = "";
            String mobile = "";
            String loginId = "";
            String idnumber = "";
            String clientname = "";
            String applyerguid = "";
            String isneedupload = "";
            AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(loginid).getResult();
            if (register != null) {
                String isfingeruoload = handleConfigservice.getFrameConfig("AS_ZNSB_ISFINGERUPLOAD", centerguid)
                        .getResult();
                if (StringUtil.isNotBlank(register.getBytes("fingerphoto")) && !"0".equals(isfingeruoload)) {
                    isneedupload = "1";
                }
                isneedupload = register.getStr("isneedupload");
                accountguid = register.getAccountguid();
                usertype = register.getUsertype();
                mobile = register.getMobile();
                loginId = register.getLoginid();

                if (!register.getIdnumber().equals(idnum)) {
                    return JsonUtils.zwdtRestReturn("0", "身份证与注册信息不匹配!", "");
                } else if (StringUtil.isNotBlank(picture)) {
                    // 刷身份证登陆 更新身份证信息
                    byte[] pic = Base64Util.decodeBuffer(picture);
                    // 更新人员信息表的数据
                    userinfoservice.insertuserinfo(register.getIdnumber(), register.getUsername(), sex, nation, born,
                            address, pic);

                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "该账户不存在！", "");
            }
            // 获取用户身份证照片
            AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(idnumber).getResult();
            if (StringUtil.isNotBlank(auditQueueUserinfo) && StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())) {
                dataJson.put("photobase64", Base64Util.encode(auditQueueUserinfo.getPhoto()));
            } else {
                dataJson.put("photobase64", "");
            }

            dataJson.put("accountguid", accountguid);
            dataJson.put("usertype", usertype);
            dataJson.put("mobile", mobile);
            dataJson.put("loginid", loginId);
            dataJson.put("idnumber", idnumber);
            dataJson.put("clientname", clientname);
            dataJson.put("applyerguid", applyerguid);
            dataJson.put("isneedupload", isneedupload);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }

    }

    private String getback(String url) {
        String back = "";
        GetMethod method = new GetMethod(url);
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            back = stringBuffer.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        try {
            Charset charset = Charset.defaultCharset();

            if ("GBK".equals(charset.toString())) {
                back = new String(back.getBytes("GBK"), "utf-8");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return back;
    }

    @RequestMapping(value = "/setdetail", method = RequestMethod.POST)
    public String setDetail(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String loginid = obj.getString("loginid");
            String fingerimg = obj.getString("fingerimg");

            AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(loginid).getResult();
            if (register != null) {
                register.set("fingerattachguid", getimgattachguid(fingerimg, loginid + "finger"));
                register.set("fingerphoto", fingerimg.getBytes());
                registerservice.updateRegister(register);
                JSONObject dataJson = new JSONObject();

                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            } else {
                return JsonUtils.zwdtRestReturn("0", "出现异常：未找到您的账户", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }
    }

    @RequestMapping(value = "/loginbysfz", method = RequestMethod.POST)
    public String loginbysfz(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String loginid = obj.getString("loginid");
            String macaddress = obj.getString("macaddress");
            String centerguid = obj.getString("centerguid");

            String accountguid = "";
            String usertype = "";
            String mobile = "";
            String loginId = "";
            String idnumber = "";
            String clientname = "";
            String applyerguid = "";

            AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(loginid).getResult();
            if (register != null) {

                accountguid = register.getAccountguid();
                usertype = register.getUsertype();
                mobile = register.getMobile();
                loginId = register.getLoginid();

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

            } else {
                return JsonUtils.zwdtRestReturn("0", "该账户不存在！请先刷身份证完成账户注册", "");
            }
            // 获取用户身份证照片,住址
            AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(idnumber).getResult();
            if (StringUtil.isNotBlank(auditQueueUserinfo) && StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())) {
                dataJson.put("photobase64", Base64Util.encode(auditQueueUserinfo.getPhoto()));
                dataJson.put("address", auditQueueUserinfo.getAddress());
            } else {
                dataJson.put("photobase64", "");
            }

            dataJson.put("accountguid", accountguid);
            dataJson.put("usertype", usertype);
            dataJson.put("mobile", mobile);
            dataJson.put("loginid", loginId);
            dataJson.put("idnumber", idnumber);
            dataJson.put("clientname", clientname);
            dataJson.put("applyerguid", applyerguid);
            dataJson.put("isneedupload", "1");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }

    }

    public String getimgattachguid(String img, String name) {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Util.decodeBuffer(img));
        FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
        long size = (long) inputStream.available();
        frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
        frameAttachInfo.setAttachFileName(name + ".png");
        frameAttachInfo.setCliengTag("指纹存储");
        frameAttachInfo.setUploadUserGuid("");
        frameAttachInfo.setUploadUserDisplayName("");
        frameAttachInfo.setUploadDateTime(new Date());
        frameAttachInfo.setContentType("png");
        frameAttachInfo.setAttachLength(size);
        return attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();
    }

    @RequestMapping(value = "/getFingerBase64List", method = RequestMethod.POST)
    public String getFingerBase64List(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            int first = obj.getIntValue("first");
            int pagesize = obj.getIntValue("pagesize");
            Map<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put("ifnull(fingerattachguid,'attachguid')!=", "attachguid");
            conditionMap.put("fingerattachguid!=", "");
            PageData<AuditOnlineRegister> list = registerservice
                    .selectAuditOnlineRegisterByPage(conditionMap, first, pagesize, "OperateDate", "desc").getResult();
            // List<AuditOnlineRegister> list =
            // registerservice.selectAuditOnlineRegisterList(conditionMap).getResult();
            JSONObject dataJson = new JSONObject();
            List<JSONObject> fingerlist = new ArrayList<>();
            for (AuditOnlineRegister register : list.getList()) {
                JSONObject finger = new JSONObject();
                finger.put("idcardno", register.getIdnumber());
                finger.put("mobile", register.getMobile());
                finger.put("base64str", new String(register.getBytes("fingerphoto")));
                fingerlist.add(finger);
            }
            dataJson.put("totalcount", list.getRowCount());
            dataJson.put("list", fingerlist);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }
    }

    /**
     * 提交办件的接口
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/submitProject", method = RequestMethod.POST)
    public String submitProject(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String centerguid = obj.getString("centerguid");
            String areacode = obj.getString("areacode");
            String accountGuid = obj.getString("accountguid");
            String isfinish = obj.getString("isfinish");
            AuditProject auditProject = new AuditProject();

            // 跟新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil conditionMap = new SqlConditionUtil();

            AuditProject project = projectservice
                    .getAuditProjectByRowGuid("taskguid,projectname ", projectGuid, areacode).getResult();
            EpointFrameDsManager.commit();

            // 获取事项详情
            AuditTaskExtension auditTaskExtension = taskExtensionservice
                    .getTaskExtensionByTaskGuid(project.getTaskguid(), true).getResult();
            // 结束之间

            if (ZwfwConstant.WEB_APPLY_TYPE_SL.equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                // 网上申报后直接受理
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_DJJ));// 代接件
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_DJJ);
                // 修改办件表中申请方式
                auditProject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB));// 网上直接申报

            } else {
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ));// 外网申报已提交
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
            }
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            conditionMap.eq("sourceguid", projectGuid);

            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionMap.getMap());

            if (StringUtil.isNotBlank(accountGuid)) {
                AuditOnlineIndividual auditOnlineIndividual = individualservice.getIndividualByAccountGuid(accountGuid)
                        .getResult();
                auditProject.setOnlineapplyerguid(auditOnlineIndividual.getApplyerguid());// 插入外网申请人guid
                // 便于后期查询

                AuditOnlineRegister auditregister = registerservice.getRegisterByAccountguid(accountGuid).getResult();
                if (auditregister != null && StringUtil.isNotBlank(auditregister.getMobile())) {
                    String AS_ZNSB_PROJECT_MSG = handleConfigservice.getFrameConfig("AS_ZNSB_PROJECT_MSG", centerguid)
                            .getResult();

                    if (StringUtil.isBlank(AS_ZNSB_PROJECT_MSG)) {
                        AS_ZNSB_PROJECT_MSG = "[#=UserName#]，您已于[#=OperateTime#]成功办理[#=TaskName#]事项";
                    }
                    if (!"0".equals(AS_ZNSB_PROJECT_MSG)) {
                        AS_ZNSB_PROJECT_MSG = AS_ZNSB_PROJECT_MSG.replace("[#=UserName#]", auditregister.getUsername());
                        AS_ZNSB_PROJECT_MSG = AS_ZNSB_PROJECT_MSG.replace("[#=OperateTime#]",
                                EpointDateUtil.getCurrentDate());
                        AS_ZNSB_PROJECT_MSG = AS_ZNSB_PROJECT_MSG.replace("[#=TaskName#]", project.getProjectname());

                        messageservice.insertSmsMessage(UUID.randomUUID().toString(), AS_ZNSB_PROJECT_MSG, new Date(),
                                0, null, auditregister.getMobile(), UUID.randomUUID().toString(), "", "", "", "", "",
                                null, false, areacode);
                    }
                }
            }

            // 更新办件表
            auditProject.setApplydate(new Date());
            auditProject.setAreacode(areacode);
            auditProject.setRowguid(projectGuid);

            projectservice.updateProject(auditProject);
            EpointFrameDsManager.commit();

            // 新增自助服务办件统计表记录
            AuditZnsbSelfmachineproject auditZnsbSelfmachineproject = new AuditZnsbSelfmachineproject();
            auditZnsbSelfmachineproject.setRowguid(UUID.randomUUID().toString());
            auditZnsbSelfmachineproject.setProjectguid(projectGuid);
            auditZnsbSelfmachineproject.setApplydate(new Date());

            // 向指定用户发送待办
            auditProject = projectservice
                    .getAuditProjectByRowGuid("rowguid,areacode,applyername,task_id,currentareacode,centerguid ",
                            projectGuid, areacode)
                    .getResult();
            AuditTask auditTask = taskservice.getAuditTaskByGuid(project.getTaskguid(), false).getResult();

            String handleUrl = "/epointzwfw/auditbusiness/auditproject/auditprojectinfo?taskguid="
                    + project.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
            String currentareacode = auditProject.getCurrentareacode();
            String rolename = "窗口人员";
            areacode = auditProject.getAreacode();
            // currentareacode 不为空代表是乡镇
            if (StringUtil.isNotBlank(currentareacode)) {
                areacode = currentareacode;
                if (currentareacode.length() == 9) {
                    rolename = "乡镇窗口人员";
                } else if (currentareacode.length() == 12) {
                    rolename = "村（社区）窗口人员 ";
                }
            } else {
                currentareacode = auditProject.getAreacode();
            }
            String title = "【办件预审】" + auditTask.getTaskname() + "(" + auditProject.getApplyername() + ")";
            List<FrameUser> frameUsers = windowservice.getFrameUserByTaskID(auditProject.getTask_id()).getResult();
            // 乡镇智能化,筛选用户
            List<FrameUser> frameRoleUsers = new ArrayList<FrameUser>();
            for (FrameUser frameUser : frameUsers) {
                if (roleservice.isExistUserRoleName(frameUser.getUserGuid(), rolename)) {
                    frameRoleUsers.add(frameUser);
                }
            }
            if (!frameRoleUsers.isEmpty()) {
                frameUsers = frameRoleUsers;
            }

            return JsonUtils.zwdtRestReturn("1", "提交办件成功", "");

        } catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e, "");
        }
    }

    @RequestMapping(value = "/getcompanylist", method = RequestMethod.POST)
    public String getcompanylist(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String userid = obj.getString("userid");
            String clientid = obj.getString("clientid");
            String wttoytj = configServcie.getFrameConfigValue("AS_Znsb_WTTOYTJ");
            if (StringUtil.isBlank(wttoytj)) {
                wttoytj = "http://218.59.158.56/jnzwdt/";
            }
            String url = wttoytj + "rest/tazwdtProject2/private/getUserCompanyList";
            Map<String, String> headers = new HashMap<>();
            headers.put("x-authenticated-userid", userid);
            headers.put("x-authenticated-clientid", clientid);
            return HttpUtil.doPostJsonSSL(url, params, headers);

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/yididaikuanzhengmingprint", method = RequestMethod.POST)
    public String yididaikuanzhengmingprint(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();

            String centerguid = obj.getString("centerguid");

            String todwmc = obj.getString("todwmc");
            String zg = obj.getString("zg");

            String zgxm = obj.getString("zgxm");
            String sfz = obj.getString("sfz");

            String dwmc = obj.getString("dwmc");
            String grgjjzh = obj.getString("grgjjzh");

            String khsj = obj.getString("khsj");
            String zhzt = obj.getString("zhzt");

            String jcjs = obj.getString("jcjs");
            String dwjcbl = obj.getString("dwjcbl");
            String grjcbl = obj.getString("grjcbl");

            String yjce = obj.getString("yjce");
            String jcye = obj.getString("jcye");

            String zjlxjcsj = obj.getString("zjlxjcsj");

            String dkqk = obj.getString("dkqk");

            String dkcs = obj.getString("dkcs");
            String dkje = obj.getString("dkje");
            String rq = obj.getString("rq");

            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            new License().setLicense(licenseName);
            String doctem = "";
            String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
            if (StringUtil.isNotBlank(downconfig)) {
                doctem = downconfig + "/jnzwdt/individuation/overall/yididaikuanzm.docx";
            } else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jnzwdt/individuation/overall/yididaikuanzm.docx";
            }
            Document doc = new Document(doctem);
            String[] fieldNames = null;
            Object[] values = null;

            // 获取域与对应的值
            Map<String, String> map = new HashMap<String, String>(16);

            map.put("todwmc", todwmc);
            map.put("zg", zg);
            map.put("zgxm", zgxm);
            map.put("sfz", sfz);

            map.put("dwmc", dwmc);
            map.put("grgjjzh", grgjjzh);
            map.put("khsj", khsj);
            map.put("zhzt", zhzt);

            map.put("jcjs", jcjs);
            map.put("dwjcbl", dwjcbl);
            map.put("grjcbl", grjcbl);

            map.put("yjce", yjce);
            map.put("jcye", jcye);

            map.put("zjlxjcsj", zjlxjcsj);
            map.put("dkqk", dkqk);
            map.put("dkcs", dkcs);
            map.put("dkje", dkje);
            map.put("rq", rq);
            fieldNames = new String[map == null ? 0 : map.size()];
            values = new Object[map == null ? 0 : map.size()];
            int num = 0;

            for (Entry<String, String> entry : map.entrySet()) {
                fieldNames[num] = entry.getKey();
                values[num] = entry.getValue();
                num++;
            }
            // 替换域
            doc.getMailMerge().execute(fieldNames, values);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word

            // 将inputStream转为Base64编码的字符串

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            long size = inputStream.available();

            // 附件信息
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("异地贷款缴存证明.doc");
            frameAttachInfo.setCliengTag("异地贷款缴存证明");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType("application/msword");
            frameAttachInfo.setAttachLength(size);
            dataJson.put("attachguid", attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

            outputStream.close();
            inputStream.close();
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e, "");
        }
    }
}

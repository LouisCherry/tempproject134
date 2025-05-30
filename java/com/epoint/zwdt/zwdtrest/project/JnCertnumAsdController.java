package com.epoint.zwdt.zwdtrest.project;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.TaHttpRequestUtils;
import com.epoint.sm2util.Util;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import org.apache.commons.lang.StringUtils;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jncertnumasd")
public class JnCertnumAsdController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
    private String accessId = "JNS_SPJYC";
    private String accessToken = "711816CC3FAC4592A216F606A74CA89C";

    //RSA公私钥
    //公钥 key RSAPublicKey
    //私钥 key RSAPrivateKey

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IJnCertRecordService iJnCertRecordService;
    /**
     * 政务大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 法人基本信息API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @RequestMapping(value = "/getCertIdentifierByCode", method = RequestMethod.POST)
    public String getCertIdentifierByCode(@RequestBody
                                                  String params, @Context
                                                  HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertIdentifierByCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            JSONObject certjson = new JSONObject();
            List<String> licens = new ArrayList<String>();

            if (ZwdtConstant.SysValidateData.equals(token)) {

                // 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
                }

                //同一个登陆人一天内不能调用超过10次
                int count = iJnCertRecordService.getCountByIdnumber(auditOnlineRegister.getIdnumber());

                if (count > 10) {
                    return JsonUtils.zwdtRestReturn("0", "点击过于频繁，请稍后再试！", "");
                } else {
                    String accessIp1 = request.getHeader("x-forwarded-for");
                    String accessIp2 = request.getRemoteAddr();
                    String certificateNo = auditOnlineRegister.getIdnumber();
                    String taskmaterialguid = obj.getString("taskmaterialguid");
                    String taskmaterialrowguid = obj.getString("taskmaterialrowguid");
                    String companyId = obj.getString("companyId");
                    String isyjs = obj.getString("isyjs");
                    AuditTaskMaterial taskmaterial = new AuditTaskMaterial();

                    if ("1".equals(isyjs)) {
                        taskmaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialrowguid)
                                .getResult();
                    } else {
                        taskmaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialguid)
                                .getResult();
                    }
                    if (taskmaterial == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取材料信息失败！", "");
                    }
                    // 查询事项信息
                    AuditTask task = iAuditTask.getAuditTaskByGuid(taskmaterial.getTaskguid(), false).getResult();
                    if (task == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取事项信息失败！", "");
                    }


                    String bigshowtype = StringUtil.isBlank(taskmaterial.getStr("bigshowtype")) ? ""
                            : taskmaterial.getStr("bigshowtype");
                    log.info("关联材料的名称：" + bigshowtype);
                    if (StringUtil.isNotBlank(certificateNo) && StringUtil.isNotBlank(bigshowtype)) {
                        String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
                                + "\"},\"data\":{\"holderCode\":\"" + certificateNo + "\", \"holderTypeCode\":\"111\","
                                + "\"certTypeCode\":\"" + bigshowtype
                                + "\",\"useCause\":{\"certificateCopyCause\":\"济宁政务服务\","
                                + "\"certificateCallCategory\":\"0\",\"certificateEventCode\":\"" + task.getItem_id()
                                + "\"," + "\"certificateMatterName\":\"" + task.getTaskname()
                                + "\",\"certificateSystemName\":\"济宁市一窗系统\"}}}";
                        // 事项类别，10：法人，20：个人
                        if (StringUtils.isNotBlank(companyId) && task.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY)>=0){
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByCompanyId(companyId)
                                    .getResult();
                            if(auditRsCompanyBaseinfo!=null){
                                certificateNo = auditRsCompanyBaseinfo.getCreditcode();
                            }
                            postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
                                    + "\"},\"data\":{\"holderCode\":\"" + certificateNo + "\", \"holderTypeCode\":\"001\","
                                    + "\"certTypeCode\":\"" + bigshowtype
                                    + "\",\"useCause\":{\"certificateCopyCause\":\"济宁政务服务\","
                                    + "\"certificateCallCategory\":\"0\",\"certificateEventCode\":\"" + task.getItem_id()
                                    + "\"," + "\"certificateMatterName\":\"" + task.getTaskname()
                                    + "\",\"certificateSystemName\":\"济宁市一窗系统\"}}}";
                        }
                        String httpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrlsByHolderCode";
                        log.info("postreason:"+postreason);
                        String resultParmas = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");
                        JSONObject resultjson = JSONObject.parseObject(resultParmas);
                        log.info("====resultParmas=====" + resultjson);

                        // 记录调用证照的调用次数
                        JnCertRecord record = new JnCertRecord();
                        record.setOperatedate(new Date());
                        record.setRowguid(UUID.randomUUID().toString());
                        record.setAreacode(task.getAreacode());
                        record.setRecordtotal("1");
                        record.set("ouname", task.getOuname());
                        record.set("ouguid", task.getOuguid());
                        record.set("type", "0");// 网上申请
                        record.set("taskname", task.getTaskname());
                        record.set("itemid", task.getItem_id());
                        record.set("certtype", bigshowtype);
                        record.set("accessIp1", accessIp1);
                        record.set("accessIp2", accessIp2);
                        record.set("applyername", auditOnlineRegister.getUsername());
                        record.set("idnumber", auditOnlineRegister.getIdnumber());
                        iJnCertRecordService.insert(record);

                        JSONObject headjson = resultjson.getJSONObject("head");
                        try {
                            if ("0".equals(headjson.getString("status"))) {
                                String certData = resultjson.getString("data");
                                if(StringUtils.isNotEmpty(certData)){
                                    byte[] decrypt;
                                    decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey), Util.hexToByte(certData));
                                    // 将字节数组转为字符串
                                    String backresult = new String(decrypt, "utf-8");
                                    JSONArray bacjson = JSONArray.parseArray(backresult);
                                    log.info("====resultParmas=====" + bacjson);
                                    for (int i = 0; i < bacjson.size(); i++) {
                                        JSONObject object = bacjson.getJSONObject(i);
                                        String cert_identifier = object.getString("certIdentifier");
                                        // 公钥加密
                                        cert_identifier = RSAUtils.encryptByPubKey(cert_identifier,
                                                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSUmOXyQmYYSnZacp0btvAZCOvCNPtzixAp7eJmzmAG4mgy/VgrY/s1BDLh9qTNHIRWXepUtwMrf1kYul/A45qE/2oxIbeeq4238YDWQ7ModOVXR9ytEHsT0jpCFvoYfYXYZnnoWRrLIBylQeXzqxbLDxxBxGCs4AjoRKh5S7nNQIDAQAB");
                                        licens.add(cert_identifier);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 调用省电子证照接口获取证照数据
                        certjson.put("listnos", licens);

                        certjson.put("taskmaterial", taskmaterial.getMaterialname());
                        certjson.put("itemid", task.getItem_id());
                        return JsonUtils.zwdtRestReturn("1", "获取证照标识成功！", certjson.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "证照标识信息不能为空！", "");
                    }
                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertIdentifierByCode接口参数：params【" + params + "】=======");
            log.info("=======getCertIdentifierByCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/downCertnum", method = RequestMethod.POST)
    public String downCertnum(@RequestBody
                                      String params, @Context
                                      HttpServletRequest request) {
        try {
            log.info("=======开始调用downCertnum接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                // 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
                }

                String cert_identifier = obj.getString("licenseNo");
                // 私钥解密
                cert_identifier = RSAUtils.decryptByPriKey(cert_identifier,
                        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANJSY5fJCZhhKdlpynRu28BkI68I0+3OLECnt4mbOYAbiaDL9WCtj+zUEMuH2pM0chFZd6lS3Ayt/WRi6X8DjmoT/ajEht56rjbfxgNZDsyh05VdH3K0QexPSOkIW+hh9hdhmeehZGssgHKVB5fOrFssPHEHEYKzgCOhEqHlLuc1AgMBAAECgYEAqTB9zWx7u4juEWd45ZEIVgw4aGXBllt0Xc6NZrTn3JZKcH+iNNNqJCm0GQaAXkqiODKwgBWXzttoK4kmLHa/6D7rXouWN8PGYXj7DHUNzyOe3IgmzYanowp/A8gu99mJQJzyhZGQ+Uo9dZXAgUDin6HAVLaxF3yWD8/yTKWN4UECQQD8Q72r7qdAfzdLMMSQl50VxRmbdhQYbo3D9FmwUw6W1gy2jhJyPXMi0JZKdKaqhxMZIT3zy4jYqw8/0zF2xc5/AkEA1W+n24Ef3ucbPgyiOu+XGwW0DNpJ9F8D3ZkEKPBgjOMojM7oqlehRwgy52hU+HaL4Toq9ghL1SwxBQPxSWCYSwJAGQUO9tKAvCDh9w8rL7wZ1GLsG0Mm0xWD8f92NcrHE6a/NAv7QGFf3gAaJ+BR92/WMRPe9SMmu3ab2JS1vzX3OQJAdN70/T8RYo8N3cYxNzBmf4d59ee5wzQb+8WD/57QX5UraR8LS+s8Bpc4uHnqvTq8kZG2YI5eZ9YQ6XwlLVbVTQJAKOSXNT+XEPWaol1YdWZDvr2m/ChbX2uwz52s8577Tey96O4Z6S/YA7V6Fr7hZEzkNF+K0LNUd79EOB6m2eQq5w==");
                JSONObject result = new JSONObject();
                String attachguid = UUID.randomUUID().toString();
                String CliengGuid = obj.getString("clientguid");
                String taskmaterial= obj.getString("taskmaterial");
                String itemid= obj.getString("itemid");
                JSONObject requestjson = new JSONObject();
                JSONObject headjson = new JSONObject();
                JSONObject datajson = new JSONObject();
                headjson.put("accessId",accessId);
                headjson.put("accessToken",accessToken);
                requestjson.put("head",headjson);
                //电子证照标志
                datajson.put("certIdentifier",cert_identifier);
                datajson.put("expiryTime","");
                //申请事由
                JSONObject usecausejson = new JSONObject();
                //系统名称
                usecausejson.put("certificateSystemName","济宁行政审批系统");
                //事项调用
                usecausejson.put("certificateCallCategory","0");
                //事项材料名称
                usecausejson.put("certificateMatterName",taskmaterial);
                //事项编码
                usecausejson.put("certificateEventCode",itemid);
                //申请事由
                usecausejson.put("certificateCopyCause",taskmaterial);

                datajson.put("useCause",usecausejson.toJSONString());
                requestjson.put("data",datajson);
//                String getpdfreason = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
//                        + "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
//                        + "\", \"useCause\":\"\",\"expiryTime\": \"\"}}";
                String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
                log.info("getpdfreason:"+requestjson.toJSONString());
                String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, requestjson.toJSONString(), "", "");
                log.info("下载附件接口：" + getpdfresult);
                JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
                JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
                if ("0".equals(getpdfheadjson.getString("status"))) {

                    byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
                            Util.hexToByte(getpdfresultjson.getString("data")));

                    // 将字节数组转为字符串
                    String getpdfbackresult = new String(decrypt2, "utf-8");

                    JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
                    String getpdfcontent = getpdfbacjson.getString("content");
                    // getpdfcontent = "http://59.206.96.173:8080" +
                    // getpdfcontent;

                    URL url = new URL(getpdfcontent);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
                    conn.setConnectTimeout(10 * 1000); // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    InputStream inputStream = conn.getInputStream();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();

                    InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());

                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) inputStream.available();
                    frameAttachInfo.setAttachGuid(attachguid);
                    frameAttachInfo.setCliengGuid(CliengGuid);
                    frameAttachInfo.setAttachFileName(
                            EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss") + ".pdf");
                    frameAttachInfo.setCliengTag("新爱山东材料");
                    // frameAttachInfo.setUploadUserGuid(userguid);
                    // frameAttachInfo.setUploadUserDisplayName(username);
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    attachService.addAttach(frameAttachInfo, stream1);

                    result.put("attachguid", attachguid);
                    result.put("cliengguid", CliengGuid);
                    return JsonUtils.zwdtRestReturn("1", "附件上传成功！", result.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "接口调用失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======downCertnum接口参数：params【" + params + "】=======");
            log.info("=======downCertnum异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取当前时间戳
     */
    public static String gettime() {
        Date datetime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(datetime);
        return time;
    }

    public static String getMD5(String s) throws UnsupportedEncodingException {
        if (s == null)
            return "";
        else
            return getMD5(s.getBytes("utf-8"));
    }

    public static String getMD5(byte abyte0[]) {
        String s = null;
        char ac[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(abyte0);
            byte abyte1[] = messagedigest.digest();
            char ac1[] = new char[32];
            int i = 0;
            for (int j = 0; j < 16; j++) {
                byte byte0 = abyte1[j];
                ac1[i++] = ac[byte0 >>> 4 & 15];
                ac1[i++] = ac[byte0 & 15];
            }

            s = new String(ac1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
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
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }
}

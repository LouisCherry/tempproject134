
package com.epoint.zwdt.zwdtrest.project;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.TaHttpRequestUtils;
import com.epoint.sm2util.Util;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import com.epoint.xmz.job.yqarea;
import com.epoint.xmz.job.yqbm;
import com.epoint.znsb.jnzwfw.selfservicemachine.HTTPSClientUtil;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/jnbigshowcert")
public class JnCertReduceController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    //获取参数信息
    private static String appCode = ConfigUtil.getConfigValue("bigshow", "appcode");
    private static String accessToken = ConfigUtil.getConfigValue("bigshow", "accesstoken");
    private static String url = ConfigUtil.getConfigValue("bigshow", "bigshowurl");
    private static String qrserviceurl = ConfigUtil.getConfigValue("bigshow", "qrcserviceurl");
    //RSA解密的私钥
    private static String privateKey = ConfigUtil.getConfigValue("bigshow", "privateKey");
    //大数据局私钥
    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";

    @Autowired
    private IAttachService attachService;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private ISpaceAcceptService iSpaceAcceptService;

    /**
     * 事项扩展信息API
     */
    @Autowired
    private IJnProjectService iJnProjectService;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IJnCertRecordService iJnCertRecordService;

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getLicenceCertNo", method = RequestMethod.POST)
    public String getLicenceCertNo(@RequestBody String params) {
        try {
            log.info("=======开始调用getLicenceCertNo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certificateNo = obj.getString("certificateNo");
                String time = new Date().getTime() + "";
                String sign = getMD5("certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&accessToken=" + accessToken);
                String apiurl = url + "getLicenceListByCertificateTypeAndCertificateNo?";
                apiurl += "certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&sign=" + sign;
                String result = HttpUtil.doGet(apiurl);
                JSONObject json = JSON.parseObject(result);
                JSONObject certjson = new JSONObject();
                List<String> licens = new ArrayList<String>();
                String retCode = json.getString("retCode");
                if ("SUCCESS".equals(retCode)) {
                    JSONArray array = json.getJSONArray("licenseArray");
                    if (array != null && array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject licencecert = array.getJSONObject(i);
                            String licenseNo = licencecert.getString("licenseNo");
                            String licenseTypeName = licencecert.getString("licenseTypeName");
                            List<CodeItems> items = iCodeItemsService.listCodeItemsByCodeID("1016093");
                            if (items != null && items.size() > 0) {
                                for (CodeItems codeitem : items) {
                                    if (licenseTypeName.equals(codeitem.getItemValue())) {
                                        List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(certificateNo, licenseTypeName);
                                        if (attachinfos == null || attachinfos.size() == 0) {
                                            licens.add(licenseNo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    certjson.put("listnos", licens);
                    return JsonUtils.zwdtRestReturn("1", "获取证照列表成功！", certjson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getLicenceCertNo接口参数：params【" + params + "】=======");
            log.info("=======getLicenceCertNo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getLicenceInfoAndPicture", method = RequestMethod.POST)
    public String getLicenceInfoAndPicture(@RequestBody String params) {
        try {
            log.info("=======开始调用getLicenceInfoAndPicture接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String licenseNo = obj.getString("licenseNo");
                String time = new Date().getTime() + "";
                String sign = getMD5("licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time + "&accessToken=" + accessToken);
                String apiurl = url + "getLicenceInfoAndPicture?licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time + "&sign=" + sign;
                String result = HttpUtil.doGet(apiurl);
                JSONObject json = JSON.parseObject(result);
                JSONObject certjson = new JSONObject();
                String retCode = json.getString("retCode");
                if ("SUCCESS".equals(retCode)) {
                    JSONArray array = json.getJSONArray("attachment");
                    JSONObject metaData = json.getJSONObject("metaData");
                    String certificateNo = metaData.getString("certificateNo");
                    if (array != null && array.size() > 0) {
                        JSONObject certattach = array.getJSONObject(0);
                        String attachcontent = certattach.getString("fileContent");
                        String fileName = certattach.getString("fileName");
                        String fileType1 = "." + certattach.getString("fileType");
                        String originalFileName = certattach.getString("originalFileName");
                        String[] strs = fileName.split("-");
                        String filetype = "";
                        if (strs != null && strs.length > 0) {
                            filetype = strs[1];
                        } else {
                            filetype = "其他";
                        }

                        //删除之前同类型的证照
                        List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(certificateNo, filetype);
                        if (attachinfos != null && attachinfos.size() > 0) {
                            for (FrameAttachInfo attach : attachinfos) {
                                attachService.deleteAttachByAttachGuid(attach.getAttachGuid());
                            }
                        }
                        byte[] pic = Base64Util.decodeBuffer(attachcontent);
                        String attachGuid = UUID.randomUUID().toString();
                        if (pic.length > 0) {
                            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                            frameAttachInfo.setAttachGuid(attachGuid);
                            frameAttachInfo.set("idnumber", certificateNo);
                            frameAttachInfo.set("bigshowtype", filetype);
                            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                            if (StringUtil.isNotBlank(originalFileName)) {
                                frameAttachInfo.setAttachFileName(originalFileName);
                            } else {
                                frameAttachInfo.setAttachFileName(fileName);
                            }
                            frameAttachInfo.setCliengTag("大数据电子证照");
                            frameAttachInfo.setUploadUserGuid("");
                            frameAttachInfo.setUploadUserDisplayName("");
                            frameAttachInfo.setUploadDateTime(new Date());
                            frameAttachInfo.setContentType(fileType1);
                            frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                            ByteArrayInputStream input = new ByteArrayInputStream(pic);

                            attachService.addAttach(frameAttachInfo, input);
                            input.close();
                        }
                        certjson.put("attachguid", attachGuid);
                    }
                    return JsonUtils.zwdtRestReturn("1", "获取证照详情成功！", certjson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取证照详情失败！", "");
                }


            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getLicenceInfoAndPicture接口参数：params【" + params + "】=======");
            log.info("=======getLicenceInfoAndPicture异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照详情失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getqQyLicenceCertNo", method = RequestMethod.POST)
    public String getQyLicenceCertNo(@RequestBody String params) {
        try {
            log.info("=======开始调用getqQyLicenceCertNo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certificateNo = obj.getString("certificateNo");
                String time = new Date().getTime() + "";
                String sign = getMD5("certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&accessToken=" + accessToken);
                String apiurl = url + "getLicenceListByCertificateTypeAndCertificateNo?";
                apiurl += "certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&sign=" + sign;
                String result = HttpUtil.doGet(apiurl);
                JSONObject json = JSON.parseObject(result);
                JSONObject certjson = new JSONObject();
                List<String> licens = new ArrayList<String>();
                String retCode = json.getString("retCode");
                if ("SUCCESS".equals(retCode)) {
                    JSONArray array = json.getJSONArray("licenseArray");
                    if (array != null && array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject licencecert = array.getJSONObject(i);
                            String licenseNo = licencecert.getString("licenseNo");
                            licens.add(licenseNo);
                        }
                    }
                    certjson.put("listnos", licens);
                    return JsonUtils.zwdtRestReturn("1", "获取证照列表成功！", certjson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getqQyLicenceCertNo接口参数：params【" + params + "】=======");
            log.info("=======getqQyLicenceCertNo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getLicenceInfoAndPictureYc", method = RequestMethod.POST)
    public String getLicenceInfoAndPictureYc(@RequestBody String params) {
        try {
            log.info("=======开始调用getLicenceInfoAndPictureYc接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String licenseNo = obj.getString("licenseNo");

                String time = new Date().getTime() + "";
                String sign = getMD5("licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time + "&accessToken=" + accessToken);
                String apiurl = url + "getLicenceInfoAndPicture?licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time + "&sign=" + sign;
                String result = HttpUtil.doGet(apiurl);
                JSONObject json = JSON.parseObject(result);
                JSONObject certjson = new JSONObject();
                String retCode = json.getString("retCode");
                if ("SUCCESS".equals(retCode)) {
                    JSONArray array = json.getJSONArray("attachment");
                    JSONObject metaData = json.getJSONObject("metaData");
                    JSONObject surfaceData = json.getJSONObject("surfaceData");
                    String licenseNumber = surfaceData.getString("No");
                    String certificateNo = metaData.getString("certificateNo");
                    if (array != null && array.size() > 0) {
                        JSONObject certattach = array.getJSONObject(0);
                        String attachcontent = certattach.getString("fileContent");
                        String fileName = certattach.getString("fileName");
                        String fileType1 = "." + certattach.getString("fileType");
                        String originalFileName = certattach.getString("originalFileName");

                        //删除之前同类型的证照
                        List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(certificateNo, licenseNumber);
                        if (attachinfos != null && attachinfos.size() > 0) {
                            for (FrameAttachInfo attach : attachinfos) {
                                attachService.deleteAttachByAttachGuid(attach.getAttachGuid());
                            }
                        }
                        byte[] pic = Base64Util.decodeBuffer(attachcontent);
                        String attachGuid = UUID.randomUUID().toString();
                        if (pic.length > 0) {
                            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                            frameAttachInfo.setAttachGuid(attachGuid);
                            frameAttachInfo.set("idnumber", certificateNo);
                            frameAttachInfo.set("bigshowtype", licenseNumber);
                            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                            if (StringUtil.isNotBlank(originalFileName)) {
                                frameAttachInfo.setAttachFileName(originalFileName);
                            } else {
                                frameAttachInfo.setAttachFileName(fileName);
                            }
                            frameAttachInfo.setCliengTag("大数据电子证照");
                            frameAttachInfo.setUploadUserGuid("");
                            frameAttachInfo.setUploadUserDisplayName("");
                            frameAttachInfo.setUploadDateTime(new Date());
                            frameAttachInfo.setContentType(fileType1);
                            frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                            ByteArrayInputStream input = new ByteArrayInputStream(pic);

                            attachService.addAttach(frameAttachInfo, input);
                            input.close();
                        }
                        certjson.put("attachguid", attachGuid);
                    }
                    return JsonUtils.zwdtRestReturn("1", "获取证照详情成功！", certjson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取证照详情失败！", "");
                }


            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getLicenceInfoAndPictureYc接口参数：params【" + params + "】=======");
            log.info("=======getLicenceInfoAndPictureYc异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照详情失败：" + e.getMessage(), "");
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
        char ac[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'
        };
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
     * 一体机
     * 获取大数据局证照信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getLicenceCertNoNew", method = RequestMethod.POST)
    public String getLicenceCertNoNew(@RequestBody String params) {
        try {
            log.info("=======开始调用getLicenceCertNoNew接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            JSONObject certjson = new JSONObject();
            List<String> licens = new ArrayList<String>();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certnum = obj.getString("certificateNo");
                String certificateNo = RSAUtils.decryptByPriKey(certnum, privateKey);
                String taskmaterialguid = obj.getString("taskmaterialguid");
                AuditTaskMaterial taskmaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialguid)
                        .getResult();
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
                if (StringUtil.isNotBlank(certificateNo) && StringUtil.isNotBlank(bigshowtype)) {
                    String postreason = "{\"head\":{\"accessId\":\"" + appCode + "\",\"accessToken\":\"" + accessToken
                            + "\"},\"data\":{\"holderCode\":\"" + certificateNo + "\", \"holderTypeCode\":\"111\","
                            + "\"certTypeCode\":\"" + bigshowtype
                            + "\",\"useCause\":{\"certificateCopyCause\":\"济宁政务服务\","
                            + "\"certificateCallCategory\":\"0\",\"certificateEventCode\":\"" + task.getItem_id()
                            + "\"," + "\"certificateMatterName\":\"" + task.getTaskname()
                            + "\",\"certificateSystemName\":\"济宁市一窗系统\"}}}";
                    String httpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrlsByHolderCode";
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
                    record.set("type", "3");// 一体机调用
                    record.set("taskname", task.getTaskname());
                    record.set("itemid", task.getItem_id());
                    record.set("certtype", bigshowtype);
                    record.set("idnumber", certificateNo);
                    iJnCertRecordService.insert(record);

                    JSONObject headjson = resultjson.getJSONObject("head");
                    try {
                        if ("0".equals(headjson.getString("status"))) {
                            String certData = resultjson.getString("data");
                            byte[] decrypt;
                            decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey), Util.hexToByte(certData));
                            // 将字节数组转为字符串
                            String backresult = new String(decrypt, "utf-8");
                            JSONArray bacjson = JSONArray.parseArray(backresult);
                            log.info("====resultParmas=====" + bacjson);
                            for (int i = 0; i < bacjson.size(); i++) {
                                JSONObject object = bacjson.getJSONObject(i);
                                String cert_identifier = object.getString("certIdentifier");
                                licens.add(cert_identifier);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 调用省电子证照接口获取证照数据
                    certjson.put("listnos", licens);
                    return JsonUtils.zwdtRestReturn("1", "获取证照标识成功！", certjson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "证照标识信息不能为空！", "");
                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getLicenceCertNo接口参数：params【" + params + "】=======");
            log.info("=======getLicenceCertNo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
    }


    /**
     * 一体机
     * 获取证照信息，同时关联材料
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getLicenceInfoAndPictureNew", method = RequestMethod.POST)
    public String getLicenceInfoAndPictureNew(@RequestBody String params) {
        try {
            log.info("=======开始调用getLicenceInfoAndPictureNew接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                String cert_identifier = obj.getString("licenseNo");
                JSONObject result = new JSONObject();
                String attachguid = UUID.randomUUID().toString();
                String CliengGuid = obj.getString("clientguid");

                String getpdfreason = "{\"head\": {\"accessId\": \"" + appCode + "\",\"accessToken\": \"" + accessToken
                        + "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
                        + "\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
                String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
                String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
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
                    frameAttachInfo.setCliengTag("一体机材料引用");
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
            log.info("=======getLicenceInfoAndPictureNew接口参数：params【" + params + "】=======");
            log.info("=======getLicenceInfoAndPictureNew异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照详情失败：" + e.getMessage(), "");
        }
    }


    /**
     * 电子身份证二维码
     *
     * @description
     * @author shibin
     * @date 2020年6月8日 上午9:52:10
     */
    @RequestMapping(value = "/getOneCodeThrough", method = RequestMethod.POST)
    public String getOneCodeThrough(@RequestBody String params) {

        log.info("=======开始调用getElecQrcodeInfo接口=======");
        log.info("=======params=======" + params);
        String ymttoken;
        JSONObject dataJson = new JSONObject();
        try {
            // 1、入参转为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            String url = qrserviceurl + "/rest/qrcode/validate";

            String attachguis = "";
            ymttoken = obj.getString("ymttoken");
            if (StringUtil.isNotBlank(ymttoken)) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("qrcode", ymttoken);
                String result = HTTPSClientUtil.doCertPost(url, param);
//                	String result = HttpUtil.doHttp(url, headerMap, param, "POST", 2);
                log.info("getElecQrcodeInfo:" + result);
                JSONObject res = JSONObject.parseObject(result);
                if ("000001".equals(res.getString("code"))) {
                    JSONObject data = res.getJSONObject("data");
                    JSONObject metarial = data.getJSONObject("metarial");
                    JSONArray lxpfstages1 = metarial.getJSONArray("lxpfstages1");
                    JSONArray lxpfstages2 = metarial.getJSONArray("lxpfstages2");
                    JSONArray lxpfstages3 = metarial.getJSONArray("lxpfstages3");
                    JSONArray lxpfstages4 = metarial.getJSONArray("lxpfstages4");
                    JSONArray resultlist = metarial.getJSONArray("resultlist");

                    if (lxpfstages1 != null) {
                        for (int i = 0; i < lxpfstages1.size(); i++) {
                            JSONObject stage = lxpfstages1.getJSONObject(i);
                            String attacGuid = stage.getString("lxpfattachguid");
                            attachguis += attacGuid + ";";
                        }
                    }
                    if (lxpfstages2 != null) {
                        for (int i = 0; i < lxpfstages2.size(); i++) {
                            JSONObject stage = lxpfstages2.getJSONObject(i);
                            String attacGuid = stage.getString("lxpfattachguid");
                            attachguis += attacGuid + ";";
                        }
                    }
                    if (lxpfstages3 != null) {
                        for (int i = 0; i < lxpfstages3.size(); i++) {
                            JSONObject stage = lxpfstages3.getJSONObject(i);
                            String attacGuid = stage.getString("lxpfattachguid");
                            attachguis += attacGuid + ";";
                        }
                    }
                    if (lxpfstages4 != null) {
                        for (int i = 0; i < lxpfstages4.size(); i++) {
                            JSONObject stage = lxpfstages4.getJSONObject(i);
                            String attacGuid = stage.getString("lxpfattachguid");
                            attachguis += attacGuid + ";";
                        }
                    }
                    if (resultlist != null) {
                        for (int i = 0; i < resultlist.size(); i++) {
                            JSONObject stage = resultlist.getJSONObject(i);
                            String attacGuid = stage.getString("resultattachguid");
                            attachguis += attacGuid + ";";
                        }
                    }
                    attachguis = attachguis.substring(0, attachguis.length() - 1);

                }

                JSONObject datajson = new JSONObject();
                datajson.put("attachguids", attachguis);

                return JsonUtils.zwdtRestReturn("1", "材料获取成功！", datajson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getOneCodeThrough异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYtLicenceCertNo", method = RequestMethod.POST)
    public String getYtLicenceCertNo(@RequestBody String params) {
        try {
            log.info("=======开始调用getYtLicenceCertNo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certificateNo = obj.getString("certificateNo");
                String time = new Date().getTime() + "";
                String sign = getMD5("certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&accessToken=" + accessToken);
                String apiurl = url + "getLicenceListByCertificateTypeAndCertificateNo?";
                apiurl += "certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&sign=" + sign;
                String result = HttpUtil.doGet(apiurl);
                JSONObject json = JSON.parseObject(result);
                JSONObject certjson = new JSONObject();
                String retCode = json.getString("retCode");
                //365预览
                String weburl = ConfigUtil.getConfigValue("officeweb365url");
                String documentType = "";

                if ("SUCCESS".equals(retCode)) {
                    JSONArray array = json.getJSONArray("licenseArray");
                    boolean datacert = false;
                    if (array != null && array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject licencecert = array.getJSONObject(i);
                            String licenseNo = licencecert.getString("licenseNo");
                            String licenseTypeName = licencecert.getString("licenseTypeName");
                            if ("企业营业执照".equals(licenseTypeName)) {
                                String time1 = new Date().getTime() + "";
                                String sign1 = getMD5("licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time1 + "&accessToken=" + accessToken);
                                String apiurl1 = url + "getLicenceInfoAndPicture?licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time1 + "&sign=" + sign1;
                                String result1 = HttpUtil.doGet(apiurl1);
                                JSONObject json1 = JSON.parseObject(result1);
                                String retCode1 = json1.getString("retCode");
                                if ("SUCCESS".equals(retCode1)) {
                                    JSONArray array1 = json1.getJSONArray("attachment");
                                    if (array1 != null && array1.size() > 0) {
                                        JSONObject certattach = array1.getJSONObject(0);
                                        String attachcontent = certattach.getString("fileContent");
                                        String fileName = certattach.getString("fileName");
                                        String fileType1 = "." + certattach.getString("fileType");
                                        String originalFileName = certattach.getString("originalFileName");
                                        String[] strs = fileName.split("-");
                                        String filetype = "";
                                        if (strs != null && strs.length > 0) {
                                            filetype = strs[1];
                                        } else {
                                            filetype = "其他";
                                        }

                                        //删除之前同类型的证照
                                        List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(licenseNo, filetype);
                                        if (attachinfos != null && attachinfos.size() > 0) {
                                            for (FrameAttachInfo attach : attachinfos) {
                                                attachService.deleteAttachByAttachGuid(attach.getAttachGuid());
                                            }
                                        }

                                        byte[] pic = Base64Util.decodeBuffer(attachcontent);
                                        String attachGuid = UUID.randomUUID().toString();
                                        if (pic.length > 0) {
                                            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                            frameAttachInfo.setAttachGuid(attachGuid);
                                            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                                            frameAttachInfo.set("idnumber", licenseNo);
                                            frameAttachInfo.set("bigshowtype", filetype);
                                            if (StringUtil.isNotBlank(originalFileName)) {
                                                frameAttachInfo.setAttachFileName(originalFileName + fileType1);
                                            } else {
                                                frameAttachInfo.setAttachFileName(fileName + fileType1);
                                            }
                                            frameAttachInfo.setCliengTag("大数据电子证照");
                                            frameAttachInfo.setUploadUserGuid("");
                                            frameAttachInfo.setUploadUserDisplayName("");
                                            frameAttachInfo.setUploadDateTime(new Date());
                                            frameAttachInfo.setContentType(fileType1);
                                            frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                                            ByteArrayInputStream input = new ByteArrayInputStream(pic);

                                            attachService.addAttach(frameAttachInfo, input);
                                            input.close();
                                        }

                                        documentType = fileType1;
                                        String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachGuid;
                                        String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
                                        String attachurl = weburl + encryptUrl;
                                        certjson.put("downloadurl", "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachGuid);
                                        certjson.put("365attachurl", attachurl);
                                    }
                                    datacert = true;
                                    continue;
                                }

                            }
                        }
                        if (datacert) {
                            return JsonUtils.zwdtRestReturn("1", "获取证照详情成功！", certjson.toString());
                        } else {
                            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", certjson.toString());
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYtLicenceCertNo接口参数：params【" + params + "】=======");
            log.info("=======getYtLicenceCertNo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYtQymLicenceCertNo", method = RequestMethod.POST)
    public String getYtQymLicenceCertNo(@RequestBody String params) {
        try {
            log.info("=======开始调用getYtLicenceCertNo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String ymttoken = jsonObject.getString("ymttoken");
            String ymturl = qrserviceurl + "/rest/qrcode/validate2";
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("x-authenticated-clientid", "89097081-8ef7-48d7-a13a-c52e6943e302");
            if (StringUtil.isNotBlank(ymttoken)) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("qrcode", ymttoken);
                String qymresult = HttpUtil.doHttp(ymturl, headerMap, param, "POST", 2);
                JSONObject res = JSONObject.parseObject(qymresult);
                log.info("企业一码通接口返回：" + res);
                if ("000001".equals(res.getString("code"))) {
                    JSONObject data = res.getJSONObject("data");
                    String certificateNo = data.getString("ecode");
                    String identity = data.getString("identity");
                    String name = data.getString("name");
                    String time = new Date().getTime() + "";
                    String sign = getMD5("certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&accessToken=" + accessToken);
                    String apiurl = url + "getLicenceListByCertificateTypeAndCertificateNo?";
                    apiurl += "certificateNo=" + certificateNo + "&appCode=" + appCode + "&time=" + time + "&sign=" + sign;
                    String result = HttpUtil.doGet(apiurl);
                    JSONObject json = JSON.parseObject(result);
                    JSONObject certjson = new JSONObject();
                    String retCode = json.getString("retCode");
                    if ("SUCCESS".equals(retCode)) {
                        JSONArray array = json.getJSONArray("licenseArray");
                        boolean datacert = false;
                        if (array != null && array.size() > 0) {
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject licencecert = array.getJSONObject(i);
                                String licenseNo = licencecert.getString("licenseNo");
                                String licenseTypeName = licencecert.getString("licenseTypeName");
                                if ("企业营业执照".equals(licenseTypeName)) {
                                    log.info("一码通接口获取营业执照成功：" + licenseNo);
                                    String time1 = new Date().getTime() + "";
                                    String sign1 = getMD5("licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time1 + "&accessToken=" + accessToken);
                                    String apiurl1 = url + "getLicenceInfoAndPicture?licenseNo=" + licenseNo + "&appCode=" + appCode + "&time=" + time1 + "&sign=" + sign1;
                                    String result1 = HttpUtil.doGet(apiurl1);
                                    JSONObject json1 = JSON.parseObject(result1);
                                    String retCode1 = json1.getString("retCode");
                                    if ("SUCCESS".equals(retCode1)) {
                                        JSONArray array1 = json1.getJSONArray("attachment");
                                        if (array1 != null && array1.size() > 0) {
                                            JSONObject certattach = array1.getJSONObject(0);
                                            String attachcontent = certattach.getString("fileContent");
                                            String fileName = certattach.getString("fileName");
                                            String fileType1 = "." + certattach.getString("fileType");
                                            String originalFileName = certattach.getString("originalFileName");
                                            String[] strs = fileName.split("-");
                                            String filetype = "";
                                            if (strs != null && strs.length > 0) {
                                                filetype = strs[1];
                                            } else {
                                                filetype = "其他";
                                            }

                                            //删除之前同类型的证照
                                            List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(licenseNo, filetype);
                                            if (attachinfos != null && attachinfos.size() > 0) {
                                                for (FrameAttachInfo attach : attachinfos) {
                                                    attachService.deleteAttachByAttachGuid(attach.getAttachGuid());
                                                }
                                            }

                                            byte[] pic = Base64Util.decodeBuffer(attachcontent);
                                            String attachGuid = UUID.randomUUID().toString();
                                            if (pic.length > 0) {
                                                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                                frameAttachInfo.setAttachGuid(attachGuid);
                                                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                                                frameAttachInfo.set("idnumber", licenseNo);
                                                frameAttachInfo.set("bigshowtype", filetype);
                                                if (StringUtil.isNotBlank(originalFileName)) {
                                                    frameAttachInfo.setAttachFileName(originalFileName + fileType1);
                                                } else {
                                                    frameAttachInfo.setAttachFileName(fileName + fileType1);
                                                }
                                                frameAttachInfo.setCliengTag("市大数据营业执照");
                                                frameAttachInfo.setUploadUserGuid("");
                                                frameAttachInfo.setUploadUserDisplayName("");
                                                frameAttachInfo.setUploadDateTime(new Date());
                                                frameAttachInfo.setContentType(fileType1);
                                                frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
                                                ByteArrayInputStream input = new ByteArrayInputStream(pic);
                                                log.info("企业码营业执照获取成功");
                                                attachService.addAttach(frameAttachInfo, input);
                                                input.close();
                                            }

                                            certjson.put("attachGuid", attachGuid);
                                            certjson.put("name", name);
                                            certjson.put("identity", identity);
                                            certjson.put("ecode", certificateNo);
                                        }
                                        datacert = true;
                                        continue;
                                    }

                                }
                            }
                            if (datacert) {
                                return JsonUtils.zwdtRestReturn("1", "获取证照详情成功！", certjson.toString());
                            } else {
                                return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", certjson.toString());
                            }
                        } else {
                            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYtLicenceCertNo接口参数：params【" + params + "】=======");
            log.info("=======getYtLicenceCertNo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYqAreaList", method = RequestMethod.POST)
    public String getYqAreaList(@RequestBody String params) {
        try {
            log.info("=======开始调用getYqAreaList接口=======");
            // 1、接口的入参转化为JSON对象
            String url = "http://jining-test.zhichongjia.com:7011/police-manage/common/area/get_next_level_list";
            int id = 370800;
            String nonceStr = "nonce123456";
            String sign = "sign123456";
            JSONObject datajson = new JSONObject();
            datajson.put("id", id);
            datajson.put("nonceStr", nonceStr);
            datajson.put("sign", sign);
            String result = HttpUtil.doPostJson(url, datajson.toString());
            JSONObject resultJson = JSON.parseObject(result);
            if ("200".equals(resultJson.getString("code"))) {
                JSONArray dataqxs = resultJson.getJSONArray("data");
                if (!dataqxs.isEmpty()) {
                    for (int i = 0; i < dataqxs.size(); i++) {
                        JSONObject object = dataqxs.getJSONObject(i);
                        String nid = object.getString("id");
                        String npid = object.getString("pid");
                        String name = object.getString("name");
                        String shortname = object.getString("shortname");
                        String level = object.getString("level");
                        yqarea nyqareac = new yqarea();
                        nyqareac.set("rowguid", UUID.randomUUID().toString());
                        nyqareac.setAreacode(nid);
                        nyqareac.setAreaname(name);
                        nyqareac.setPid(npid);
                        nyqareac.setLevel(level);
                        nyqareac.setShortname(shortname);
                        iSpaceAcceptService.insert(nyqareac);

                        JSONObject datajson2 = new JSONObject();
                        datajson2.put("id", nid);
                        datajson2.put("nonceStr", nonceStr);
                        datajson2.put("sign", sign);
                        String result2 = HttpUtil.doPostJson(url, datajson2.toString());

                        JSONObject resultJson2 = JSON.parseObject(result2);
                        if ("200".equals(resultJson2.getString("code"))) {
                            JSONArray dataqxz = resultJson2.getJSONArray("data");
                            if (!dataqxz.isEmpty()) {
                                for (int j = 0; j < dataqxz.size(); j++) {
                                    JSONObject object2 = dataqxz.getJSONObject(j);
                                    String nid2 = object2.getString("id");
                                    String npid2 = object2.getString("pid");
                                    String name2 = object2.getString("name");
                                    String shortname2 = object2.getString("shortname");
                                    String level2 = object2.getString("level");
                                    yqarea nyqareac2 = new yqarea();
                                    nyqareac2.set("rowguid", UUID.randomUUID().toString());
                                    nyqareac2.setAreacode(nid2);
                                    nyqareac2.setAreaname(name2);
                                    nyqareac2.setPid(npid2);
                                    nyqareac2.setLevel(level2);
                                    nyqareac2.setShortname(shortname2);
                                    iSpaceAcceptService.insert(nyqareac2);


                                    JSONObject datajson3 = new JSONObject();
                                    datajson3.put("id", nid2);
                                    datajson3.put("nonceStr", nonceStr);
                                    datajson3.put("sign", sign);
                                    String result3 = HttpUtil.doPostJson(url, datajson3.toString());

                                    JSONObject resultJson3 = JSON.parseObject(result3);
                                    if ("200".equals(resultJson3.getString("code"))) {
                                        JSONArray dataqjd = resultJson3.getJSONArray("data");
                                        if (!dataqjd.isEmpty()) {
                                            for (int z = 0; z < dataqjd.size(); z++) {
                                                JSONObject object3 = dataqjd.getJSONObject(z);
                                                String nid3 = object3.getString("id");
                                                String npid3 = object3.getString("pid");
                                                String name3 = object3.getString("name");
                                                String shortname3 = object3.getString("shortname");
                                                String level3 = object3.getString("level");
                                                yqarea nyqareac3 = new yqarea();
                                                nyqareac3.set("rowguid", UUID.randomUUID().toString());
                                                nyqareac3.setAreacode(nid3);
                                                nyqareac3.setAreaname(name3);
                                                nyqareac3.setPid(npid3);
                                                nyqareac3.setLevel(level3);
                                                nyqareac3.setShortname(shortname3);
                                                iSpaceAcceptService.insert(nyqareac3);
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }
                }

                return JsonUtils.zwdtRestReturn("1", "推送成功", "");

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYqAreaList接口参数：params【" + params + "】=======");
            log.info("=======getYqAreaList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "推送成功", "");
    }


    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYqDogList", method = RequestMethod.POST)
    public String getYqDogList(@RequestBody String params) {
        try {
            log.info("=======开始调用getYqDogList接口=======");

            String url = "http://jining-test.zhichongjia.com:7011/police-manage/common/convenience_point/list_all";
            String nonceStr = "nonce123456";
            String sign = "sign123456";
            JSONObject datajson = new JSONObject();
            datajson.put("openCityId", 0);
            datajson.put("nonceStr", nonceStr);
            datajson.put("sign", sign);
            String result = HttpUtil.doPostJson(url, datajson.toString());
            JSONObject resultJson = JSON.parseObject(result);
            if ("200".equals(resultJson.getString("code"))) {
                JSONArray dataqxs = resultJson.getJSONArray("data");
                if (!dataqxs.isEmpty()) {
                    for (int i = 0; i < dataqxs.size(); i++) {
                        JSONObject object = dataqxs.getJSONObject(i);
                        String nid = object.getString("id");
                        String name = object.getString("name");
                        String addressFull = object.getString("addressFull");
                        JSONArray pcs = object.getJSONArray("relationPoliceList");
                        String policeStationId = "";
                        String policeStationName = "";
                        if (pcs != null) {
                            if (pcs.size() > 0) {
                                JSONObject pcsrecord = pcs.getJSONObject(0);
                                policeStationId = pcsrecord.getString("policeStationId");
                                policeStationName = pcsrecord.getString("policeStationName");
                            }
                        }


                        yqbm nyqareac = new yqbm();
                        nyqareac.set("rowguid", UUID.randomUUID().toString());
                        nyqareac.setName(name);
                        nyqareac.setValue(nid + ";" + addressFull + ";" + policeStationId + ";" + policeStationName);
                        iSpaceAcceptService.insertYqbm(nyqareac);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYqDogList接口参数：params【" + params + "】=======");
            log.info("=======getYqDogList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
        }
        return JsonUtils.zwdtRestReturn("1", "推送成功", "");
    }

}

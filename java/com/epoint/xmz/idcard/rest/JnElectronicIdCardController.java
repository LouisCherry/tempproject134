
package com.epoint.xmz.idcard.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.sm2util.TaHttpRequestUtils;

@RestController
@RequestMapping("/jnelectronicidcard")
public class JnElectronicIdCardController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static final String cx_url = ConfigUtil.getConfigValue("epointframe", "cxurl");

    @Autowired
    private IAttachService attachservice;

    /**
     * 获取提交超限通行证的accesstoken值
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getelectronicidcard", method = RequestMethod.POST)
    public String getEletronicidcard(@RequestBody String params) {
        try {
            log.info("=======开始调用getEletronicidcard接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String qrcode = jsonObject.getString("qrcode");
            JSONObject datajson = new JSONObject();
            // 拼装参数
            JSONObject paramsObj = new JSONObject();
            if (StringUtil.isNotBlank(qrcode)) {
                JSONObject message_header = new JSONObject();
                String syscode = ConfigUtil.getConfigValue("xmzArgs2", "syscode");
                String authcode = ConfigUtil.getConfigValue("xmzArgs2", "authcode");
                String ddyyzzserviceurl = ConfigUtil.getConfigValue("xmzArgs2", "ddyyzzserviceurl");

                message_header.put("syscode", syscode);
                message_header.put("authcode", authcode);
                message_header.put("businesstype", "005");
                message_header.put("sign", "taianspj");
                message_header.put("version", "2");

                JSONObject message_content = new JSONObject();
                message_content.put("qrid", qrcode);
                message_content.put("rettype", "5");
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssms");
                String opertime = df.format(System.currentTimeMillis());
                message_content.put("opertime", opertime);
                paramsObj.put("message_header", message_header);
                paramsObj.put("message_content", message_content);

                // 调用接口 获取信息
                log.info(">>>>>>>>>>>>>>>>开始获取电子证照全业务数据数据，入参为：" + paramsObj.toJSONString());
                String dzyyzzinfostr = TaHttpRequestUtils.sendPost(
                        ddyyzzserviceurl + "/authenticationService/httpserver.do", paramsObj.toJSONString(), "", "");
                log.info(">>>>>>>>>>>>>>>>获取电子证照全业务数据数据返回的结果为:" + dzyyzzinfostr);
                JSONObject returnobj = JSONObject.parseObject(dzyyzzinfostr);
                JSONObject mess_header = returnobj.getJSONObject("message_header");
                if ("0".equals(mess_header.getString("errorCode"))) {
                    JSONObject mess_content = returnobj.getJSONObject("message_content");
                    // 执照下载地址地址
                    String imageurl = mess_content.getString("imageurl");
                    // 统一社会信用代码
                    String uniscid = WebUtil.getRequestParameterByUrl(imageurl, "uniscid");
                    String pdfcode = WebUtil.getRequestParameterByUrl(imageurl, "pdfcode");
                    // 法定代表人
                    String name = mess_content.getString("name");
                    // 企业名称
                    String entname = mess_content.getString("entname");
                    // 下载附件
                    String returnstr = TaHttpRequestUtils.sendGet(ddyyzzserviceurl
                            + "/authenticationService/getLicencePdf.do?uniscid=" + uniscid + "&pdfcode=" + pdfcode, "",
                            "");
                    JSONObject pdfobj = JSONObject.parseObject(returnstr);
                    JSONObject message_contentpdf = pdfobj.getJSONObject("message_content");
                    String base64 = message_contentpdf.getString("licencePDF");
                    byte[] bytespdf = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
                    InputStream input = new ByteArrayInputStream(bytespdf);
                    /*
                     * String pdfattachguid = UUID.randomUUID().toString();
                     * AttachUtil.saveFileInputStream(pdfattachguid,
                     * UUID.randomUUID().toString(), "电子营业执照.pdf", ".pdf",
                     * "电子亮证", input.available(), input, "", "");
                     */
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) input.available();
                    String attachguid = UUID.randomUUID().toString();
                    frameAttachInfo.setAttachGuid(attachguid);
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("电子营业执照.pdf");
                    frameAttachInfo.setCliengTag("电子营业执照");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    attachservice.addAttach(frameAttachInfo, input);
                    datajson.put("uniscid", uniscid);
                    datajson.put("name", name);
                    datajson.put("entname", entname);
                    datajson.put("pdfattachguid", attachguid);
                    return JsonUtils.zwdtRestReturn("1", "getEletronicidcard接口调用成功", datajson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "getEletronicidcard接口调用失败", datajson);
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "qrcode未获取到", datajson);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getEletronicidcard接口参数：params【" + params + "】=======");
            log.info("=======getEletronicidcard异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取营业执照信息失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getytelectronicidcard", method = RequestMethod.POST)
    public String getYtElectronicidcard(@RequestBody String params) {
        try {
            log.info("=======开始调用getYtElectronicidcard接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String qrcode = jsonObject.getString("qrcode");
            JSONObject datajson = new JSONObject();
            // 拼装参数
            JSONObject paramsObj = new JSONObject();
            if (StringUtil.isNotBlank(qrcode)) {
                JSONObject message_header = new JSONObject();
                String syscode = ConfigUtil.getConfigValue("xmzArgs", "syscode");
                String authcode = ConfigUtil.getConfigValue("xmzArgs", "authcode");
                String ddyyzzserviceurl = ConfigUtil.getConfigValue("xmzArgs", "ddyyzzserviceurl");

                message_header.put("syscode", syscode);
                message_header.put("authcode", authcode);
                message_header.put("businesstype", "005");
                message_header.put("sign", "taianspj");
                message_header.put("version", "2");

                JSONObject message_content = new JSONObject();
                message_content.put("qrid", qrcode);
                message_content.put("rettype", "5");
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssms");
                String opertime = df.format(System.currentTimeMillis());
                message_content.put("opertime", opertime);
                paramsObj.put("message_header", message_header);
                paramsObj.put("message_content", message_content);

                // 调用接口 获取信息
                log.info(">>>>>>>>>>>>>>>>开始获取电子证照全业务数据数据，入参为：" + paramsObj.toJSONString());
                String dzyyzzinfostr = TaHttpRequestUtils.sendPost(
                        ddyyzzserviceurl + "/authenticationService/httpserver.do", paramsObj.toJSONString(), "", "");
                log.info(">>>>>>>>>>>>>>>>获取电子证照全业务数据数据返回的结果为:" + dzyyzzinfostr);
                JSONObject returnobj = JSONObject.parseObject(dzyyzzinfostr);
                JSONObject mess_header = returnobj.getJSONObject("message_header");
                if ("0".equals(mess_header.getString("errorCode"))) {
                    JSONObject mess_content = returnobj.getJSONObject("message_content");
                    // 执照下载地址地址
                    String imageurl = mess_content.getString("imageurl");
                    // 统一社会信用代码
                    String uniscid = WebUtil.getRequestParameterByUrl(imageurl, "uniscid");
                    String pdfcode = WebUtil.getRequestParameterByUrl(imageurl, "pdfcode");
                    // 法定代表人
                    String name = mess_content.getString("name");
                    // 企业名称
                    String entname = mess_content.getString("entname");
                    // 下载附件
                    String returnstr = TaHttpRequestUtils.sendGet(ddyyzzserviceurl
                            + "/authenticationService/getLicencePdf.do?uniscid=" + uniscid + "&pdfcode=" + pdfcode, "",
                            "");
                    JSONObject pdfobj = JSONObject.parseObject(returnstr);
                    JSONObject message_contentpdf = pdfobj.getJSONObject("message_content");
                    String base64 = message_contentpdf.getString("licencePDF");
                    byte[] bytespdf = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
                    InputStream input = new ByteArrayInputStream(bytespdf);
                    /*
                     * String pdfattachguid = UUID.randomUUID().toString();
                     * AttachUtil.saveFileInputStream(pdfattachguid,
                     * UUID.randomUUID().toString(), "电子营业执照.pdf", ".pdf",
                     * "电子亮证", input.available(), input, "", "");
                     */
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) input.available();
                    String attachguid = UUID.randomUUID().toString();
                    frameAttachInfo.setAttachGuid(attachguid);
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("电子营业执照.pdf");
                    frameAttachInfo.setCliengTag("ytdzzz");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    frameAttachInfo.setCliengInfo(entname + ";" + uniscid);
                    attachservice.addAttach(frameAttachInfo, input);
                    datajson.put("uniscid", uniscid);
                    datajson.put("name", name);
                    datajson.put("entname", entname);
                    datajson.put("pdfattachguid", attachguid);
                    return JsonUtils.zwdtRestReturn("1", "getEletronicidcard接口调用成功", datajson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "getEletronicidcard接口调用失败", datajson);
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "qrcode未获取到", datajson);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYtElectronicidcard接口参数：params【" + params + "】=======");
            log.info("=======getYtElectronicidcard异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取营业执照信息失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getScanLegalCodeInfo", method = RequestMethod.POST)
    public String getScanLegalCodeInfo(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String qrcode = obj.getString("qrcode");
            String QRcodeInfo = "";
            JSONObject result = new JSONObject();
            JSONObject datajson = new JSONObject();
            if (StringUtil.isNotBlank(qrcode)) {
                // 拼装参数
                JSONObject paramsObj = new JSONObject();
                JSONObject message_header = new JSONObject();
                String syscode = ConfigUtil.getConfigValue("xmzArgs", "syscode");
                String authcode = ConfigUtil.getConfigValue("xmzArgs", "authcode");
                String ddyyzzserviceurl = ConfigUtil.getConfigValue("xmzArgs", "ddyyzzserviceurl");

                message_header.put("syscode", syscode);
                message_header.put("authcode", authcode);
                message_header.put("businesstype", "005");
                message_header.put("sign", "taianspj");
                message_header.put("version", "2");

                JSONObject message_content = new JSONObject();
                message_content.put("qrid", qrcode);
                message_content.put("rettype", "5");
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssms");
                String opertime = df.format(System.currentTimeMillis());
                message_content.put("opertime", opertime);
                paramsObj.put("message_header", message_header);
                paramsObj.put("message_content", message_content);

                // 调用接口 获取信息
                log.info(">>>>>>>>>>>>>>>>开始获取电子证照全业务数据数据，入参为：" + paramsObj.toJSONString());
                String dzyyzzinfostr = TaHttpRequestUtils.sendPost(
                        ddyyzzserviceurl + "/authenticationService/httpserver.do", paramsObj.toJSONString(), "", "");
                log.info(">>>>>>>>>>>>>>>>获取电子证照全业务数据数据返回的结果为:" + dzyyzzinfostr);
                JSONObject returnobj = JSONObject.parseObject(dzyyzzinfostr);
                JSONObject mess_header = returnobj.getJSONObject("message_header");
                if ("0".equals(mess_header.getString("errorCode"))) {
                    JSONObject mess_content = returnobj.getJSONObject("message_content");
                    // 执照下载地址地址
                    String imageurl = mess_content.getString("imageurl");
                    // 统一社会信用代码
                    String uniscid = WebUtil.getRequestParameterByUrl(imageurl, "uniscid");
                    String pdfcode = WebUtil.getRequestParameterByUrl(imageurl, "pdfcode");
                    // 法定代表人
                    String name = mess_content.getString("name");
                    // 企业名称
                    String entname = mess_content.getString("entname");
                    // 下载附件
                    String returnstr = TaHttpRequestUtils.sendGet(ddyyzzserviceurl
                            + "/authenticationService/getLicencePdf.do?uniscid=" + uniscid + "&pdfcode=" + pdfcode, "",
                            "");
                    JSONObject pdfobj = JSONObject.parseObject(returnstr);
                    JSONObject message_contentpdf = pdfobj.getJSONObject("message_content");
                    String base64 = message_contentpdf.getString("licencePDF");
                    byte[] bytespdf = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
                    InputStream input = new ByteArrayInputStream(bytespdf);
                    /*
                     * String pdfattachguid = UUID.randomUUID().toString();
                     * AttachUtil.saveFileInputStream(pdfattachguid,
                     * UUID.randomUUID().toString(), "电子营业执照.pdf", ".pdf",
                     * "电子亮证", input.available(), input, "", "");
                     */
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) input.available();
                    String attachguid = UUID.randomUUID().toString();
                    frameAttachInfo.setAttachGuid(attachguid);
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("电子营业执照.pdf");
                    frameAttachInfo.setCliengTag("电子营业执照");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    attachservice.addAttach(frameAttachInfo, input);
                    datajson.put("uniscid", uniscid);
                    datajson.put("name", name);
                    datajson.put("entname", entname);
                    datajson.put("pdfattachguid", attachguid);

                    return JsonUtils.zwdtRestReturn("1", "getScanLegalCodeInfo接口调用成功", datajson);

                }

            }
            // 调用省电子证照接口获取证照数据

            return JsonUtils.zwdtRestReturn("0", "二维码已过期", datajson);
        }
        catch (Exception e) {
            e.printStackTrace();

            return JsonUtils.zwdtRestReturn("0", "getScanLegalCodeInfo接口调用失败", e.getMessage());
        }

    }

    @RequestMapping(value = "/getelectronicyyzzinfo", method = RequestMethod.POST)
    public String getEletronicyyzzinfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getelectronicyyzzinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String qrcode = jsonObject.getString("qrcode");
            JSONObject datajson = new JSONObject();
            // 拼装参数
            JSONObject paramsObj = new JSONObject();
            if (StringUtil.isNotBlank(qrcode)) {
                JSONObject message_header = new JSONObject();
                String syscode = ConfigUtil.getConfigValue("xmzArgs2", "syscode");
                String authcode = ConfigUtil.getConfigValue("xmzArgs2", "authcode");
                String ddyyzzserviceurl = ConfigUtil.getConfigValue("xmzArgs2", "ddyyzzserviceurl");

                message_header.put("syscode", syscode);
                message_header.put("authcode", authcode);
                message_header.put("businesstype", "005");
                message_header.put("sign", "taianspj");
                message_header.put("version", "2");

                JSONObject message_content = new JSONObject();
                message_content.put("qrid", qrcode);
                message_content.put("rettype", "5");
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssms");
                String opertime = df.format(System.currentTimeMillis());
                message_content.put("opertime", opertime);
                paramsObj.put("message_header", message_header);
                paramsObj.put("message_content", message_content);

                // 调用接口 获取信息
                log.info(">>>>>>>>>>>>>>>>开始获取getelectronicyyzzinfo数据，入参为：" + paramsObj.toJSONString());
                String dzyyzzinfostr = TaHttpRequestUtils.sendPost(
                        ddyyzzserviceurl + "/authenticationService/httpserver.do", paramsObj.toJSONString(), "", "");
                log.info(">>>>>>>>>>>>>>>>获取getelectronicyyzzinfo数据返回的结果为:" + dzyyzzinfostr);
                JSONObject returnobj = JSONObject.parseObject(dzyyzzinfostr);
                JSONObject mess_header = returnobj.getJSONObject("message_header");
                if ("0".equals(mess_header.getString("errorCode"))) {
                    JSONObject mess_content = returnobj.getJSONObject("message_content");

                    String uniscid = mess_content.getString("uniscid");
                    // 企业名称
                    String entname = mess_content.getString("entname");
                    datajson.put("uniscid", uniscid);
                    datajson.put("entname", entname);
                    return JsonUtils.zwdtRestReturn("1", "getelectronicyyzzinfo接口调用成功", datajson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "getelectronicyyzzinfo接口调用失败", datajson);
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "qrcode未获取到", datajson);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getelectronicyyzzinfo接口参数：params【" + params + "】=======");
            log.info("=======getelectronicyyzzinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取营业执照信息失败：" + e.getMessage(), "");
        }
    }
}

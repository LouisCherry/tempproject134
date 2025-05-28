package com.epoint.jnzwfw.evaluationpad.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jnzwfw.evaluationpad.api.IEvaluationPadService;
import com.epoint.ta.httprequest.util.TaHttpRequestUtils;
import com.epoint.util.TARequestUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;


/**
 * 评价pad接口
 * 
 * @作者 shibin
 * @version [版本号, 2018年10月21日]
 */
@RestController
@RequestMapping("/evaluationpad")
public class EvaluationPadController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    private static String YmtEVALUATE = ConfigUtil.getConfigValue("hcp", "YmtMaterialUrl");



    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
    private String accessId = "JNS_SPJYC";
    private String accessToken = "A17B2F35B3E243B59852F182316E4414";
    //接口记录日志的注入信息
    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IEvaluationPadService service;


    /**
     * 电子身份证二维码
     * @description
     * @author shibin
     * @date  2020年6月8日 上午9:52:10
     */
    @RequestMapping(value = "/getElecQrcodeInfo", method = RequestMethod.POST)
    public String getElecQrcodeInfo(@RequestBody String params) {

        log.info("=======开始调用getElecQrcodeInfo接口=======");
//        log.info("=======params=======" + params);
        String projectGuid;
        String QRcodeinfo;
        JSONObject dataJson = new JSONObject();
        try {
            // 1、入参转为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");

            if (ZwdtConstant.SysValidateData.equals(token)) {
                projectGuid = obj.getString("projectGuid");
                QRcodeinfo = obj.getString("QRcodeinfos").replace("\\", "");

                if (StringUtil.isNotBlank(projectGuid) && StringUtil.isNotBlank(QRcodeinfo)) {

                    int num = service.updateQRcodeinfoByprojectGuid(projectGuid, QRcodeinfo);
                    if (num > 0) {
                        dataJson.put("projectGuid", projectGuid);
                        dataJson.put("QRcodeinfo", QRcodeinfo);
                        log.info("=======结束调用getElecQrcodeInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "更新成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("1", "更新失败！", dataJson.toString());
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("1", "入参存在空值！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("1", "token验证失败！", dataJson.toString());
            }
        }
        catch (Exception e) {
            log.info("=======getElecQrcodeInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }
    
    /**
     * 电子身份证二维码
     * @description
     * @author shibin
     * @date  2020年6月8日 上午9:52:10
     */
    @RequestMapping(value = "/getElecJSTcodeInfo", method = RequestMethod.POST)
    public String getElecJSTcodeInfo(@RequestBody String params) {

        log.info("=======开始调用getElecJSTcodeInfo接口=======");
//        log.info("=======params=======" + params);
        String projectGuid;
        String JSTcodeinfo;
        JSONObject dataJson = new JSONObject();
        try {
            // 1、入参转为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");

            if (ZwdtConstant.SysValidateData.equals(token)) {
                projectGuid = obj.getString("projectGuid");
                JSTcodeinfo = obj.getString("jstcodeinfos").replace("\\", "");

                if (StringUtil.isNotBlank(projectGuid) && StringUtil.isNotBlank(JSTcodeinfo)) {

                    int num = service.updateJSTcodeinfoByprojectGuid(projectGuid, JSTcodeinfo);
                    if (num > 0) {
                        dataJson.put("projectGuid", projectGuid);
                        dataJson.put("QRcodeinfo", JSTcodeinfo);
                        log.info("=======结束调用getElecJSTcodeInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "更新成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("1", "更新失败！", dataJson.toString());
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("1", "入参存在空值！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("1", "token验证失败！", dataJson.toString());
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
            log.info("=======getElecJSTcodeInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }
    

    /**
    * 获取电子身份证信息
    * @description
    * @author shibin
    * @date  2020年6月8日 上午11:35:13
    */
    @RequestMapping(value = "/getcardinfo", method = RequestMethod.POST)
    public String getcardinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSONObject.parseObject(params);
            this.log.info("=======开始调用getcardinfo接口=======");
            JSONObject obj = (JSONObject) e.get("params");
            String qrcode = obj.getString("qrcode");
            String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
                    + "\"},\"data\":{\"qrcode\":\"" + qrcode + "\",  \"useCause\":\"济宁政务服务\"}}";
            String httpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByQRcode";
            String result = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");
            //String result = "{\"head\": {\"status\": 0,\"message\": \"数据获取成功\"},\"data\": {\"certData\": \"0429ADD3DA865C98F2655EC8FB454DD1DAC49C74CCF95E629A58744050A8A326E22DC11551796443C0D9C1086F3747373B0002DA0369C9EE0581A7B253829ECE21B87B76D7DAF48B508382C2FD8A75F77D9EA46E2D09F4E9C7BAADB1893520C266B65454B625AE8FBB5431A47CA54150DC466B02F8632AF6566684207783B7DE96867AD853758D7382A6191D2E95AFF12657CA688D1E4EE2589A30AD1897764689419A29B50057E967D30DE515DEBE6EC0DA23B077BA1969A87D62251891B5E1B46A2626CD8B60B28CBD22F5A393DA3154C1C61F73B68558D5BDFB1A90EB45B5621D24C41AEC61E80814AA2084AFE8BE8077DEAA5C04CF770971BC81CC504D241770C872AC56A05F99C7EB3210190E1C06C5894AC52CCA9131890F026E1FC118D6\"}}";
            JSONObject resultjson = JSONObject.parseObject(result);
            JSONObject newdataJson = new JSONObject();
            JSONObject headjson = resultjson.getJSONObject("head");
            if ("0".equals(headjson.getString("status"))) {
                JSONObject datajson = resultjson.getJSONObject("data");
                //String backresult = SM2Utils.getDecrypt(privatekey, datajson.getString("certData"));
                //byte[] decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey),
                //       Util.hexToByte(datajson.getString("certData")));
                // 将字节数组转为字符串
                //String backresult = new String(decrypt, "utf-8");
                String backresult = "";
                JSONObject bacjson = JSONObject.parseObject(backresult);
                String cert_identifier = bacjson.getString("cert_identifier");
                String name = bacjson.getString("name");
                String getpdfreason = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
                        + "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
                        + "\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
                String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
                String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
                JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
                JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
                if ("0".equals(getpdfheadjson.getString("status"))) {

                    //String getpdfbackresult = SM2Utils.getDecrypt(privatekey, getpdfresultjson.getString("data"));
                    //byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
                    //        Util.hexToByte(getpdfresultjson.getString("data")));
                    // 将字节数组转为字符串
                    //String getpdfbackresult = new String(decrypt2, "utf-8");
                    String getpdfbackresult = "";

                    JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
                    String getpdfcontent = getpdfbacjson.getString("content");
                    getpdfcontent = "http://59.206.96.173:8080" + getpdfcontent;

                    URL url = new URL(getpdfcontent);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
                    conn.setConnectTimeout(5 * 1000); // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    InputStream inputStream = conn.getInputStream();

                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    long size = (long) inputStream.available();
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName(name + ".pdf");
                    frameAttachInfo.setCliengTag("电子身份证");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(".pdf");
                    frameAttachInfo.setAttachLength(size);
                    String attachguid = attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();
//                    log.info(getpdfcontent);
                    newdataJson.put("pdfcontent", attachguid);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", " 调用获取身份证接口出现异常：" + headjson.getString("message"), "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", " 调用扫码获取信息接口出现异常：" + headjson.getString("message"), "");
            }

            this.log.info("=======结束调用getcardinfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "getcardinfo信息成功", newdataJson.toString());

        }
        catch (Exception arg15) {
            arg15.printStackTrace();
            this.log.info("=======getcardinfo接口参数：params【" + params + "】=======");
            this.log.info("=======getcardinfo异常信息：" + arg15.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", " getcardinfo信息出现异常：" + arg15.getMessage(), "");
        }
    }
    
    /**
     * 电子营业执照二维码
     * @description
     * @author shibin
     * @date  2020年6月8日 上午9:52:10
     */
    @RequestMapping(value = "/sendLegalElecQrcodeInfo", method = RequestMethod.POST)
    public String sendLegalElecQrcodeInfo(@RequestBody String params) {

        log.info("=======开始调用getElecQrcodeInfo接口=======");
//        log.info("=======params=======" + params);
        String projectGuid;
        String QRcodeinfo;
        JSONObject dataJson = new JSONObject();
        try {
            // 1、入参转为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");

            if (ZwdtConstant.SysValidateData.equals(token)) {
                projectGuid = obj.getString("projectGuid");
                QRcodeinfo = obj.getString("QRcodeinfo");

                if (StringUtil.isNotBlank(projectGuid) && StringUtil.isNotBlank(QRcodeinfo)) {

                    int num = service.updateLegalQRcodeinfoByprojectGuid(projectGuid, QRcodeinfo);
                    if (num > 0) {
                        dataJson.put("projectGuid", projectGuid);
                        log.info("=======结束调用getElecQrcodeInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "更新成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("1", "更新失败！", dataJson.toString());
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("1", "入参存在空值！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("1", "token验证失败！", dataJson.toString());
            }
        }
        catch (Exception e) {
            log.info("=======getElecQrcodeInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 电子身份证二维码
     * @description
     * @author shibin
     * @date  2020年6月8日 上午9:52:10
     */
    @RequestMapping(value = "/getTxmCodeInfo", method = RequestMethod.POST)
    public String getTxmCodeInfo(@RequestBody String params) {

        log.info("=======开始调用getTxmCodeInfo接口=======");
//        log.info("=======params=======" + params);
        String projectGuid;
        String JSTcodeinfo;
        JSONObject dataJson = new JSONObject();
        try {
            // 1、入参转为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");

            if (ZwdtConstant.SysValidateData.equals(token)) {
                projectGuid = obj.getString("projectGuid");
                JSTcodeinfo = obj.getString("jstcodeinfos").replace("\\", "");

                if (StringUtil.isNotBlank(projectGuid) && StringUtil.isNotBlank(JSTcodeinfo)) {

                    int num = service.updateJSTcodeinfoByprojectGuid(projectGuid, JSTcodeinfo);
                    if (num > 0) {
                        dataJson.put("projectGuid", projectGuid);
                        dataJson.put("QRcodeinfo", JSTcodeinfo);
                        log.info("=======结束调用getTxmCodeInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "更新成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("1", "更新失败！", dataJson.toString());
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("1", "入参存在空值！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("1", "token验证失败！", dataJson.toString());
            }
        }
        catch (Exception e) {
            log.info("=======getTxmCodeInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 电子身份证二维码
     * @description
     * @author shibin
     * @date  2020年6月8日 上午9:52:10
     */
    @RequestMapping(value = "/getOneCodeThrough", method = RequestMethod.POST)
    public String getOneCodeThrough(@RequestBody String params) {

        log.info("=======开始调用getElecQrcodeInfo接口=======");
//        log.info("=======params=======" + params);
        String ymttoken;
        try {
            // 1、入参转为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            String attachguis = "";
            if (ZwdtConstant.SysValidateData.equals(token)) {
            	ymttoken = obj.getString("ymttoken");
                if (StringUtil.isNotBlank(ymttoken)) {
                	JSONObject json = new JSONObject();
                	json.put("ymttoken", ymttoken);
     	            JSONObject submit = new JSONObject();
     	            submit.put("params", json);
    	            String resultsign = TARequestUtil.sendPostInner(YmtEVALUATE, submit.toJSONString(), "", "");
    	            log.info("一码通材料接口："+resultsign);
    	            
    	            if (StringUtil.isNotBlank(resultsign)) {
    					JSONObject jsonobject = JSONObject.parseObject(resultsign);
						JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
						if ("1".equals(jsoncustom.get("code").toString())) {
							attachguis = jsoncustom.getString("attachguids");
						} 
    				}
    	            
    	        	JSONObject datajson = new JSONObject();
       	            datajson.put("attachguids", attachguis);
    	            log.info("一码通材料返回标识："+attachguis);
                	return JsonUtils.zwdtRestReturn("1", "入参存在空值！", datajson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "入参存在空值！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
            log.info("=======getOneCodeThrough异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }
    
    
}

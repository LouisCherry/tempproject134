package com.epoint.auditselfservice.auditselfservicerest.common;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditselfservice.auditselfservicerest.utils.Util;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.StringUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.sm2util.SM2Utils;
import com.epoint.ta.httprequest.util.TaHttpRequestUtils;

@RestController
@RequestMapping("/jnqrcodeinfo")
public class JnQrcodeInfoRestController
{
    private String accessId = ConfigUtil.getConfigValue("supercode", "superaccessid");
    private String accessToken = ConfigUtil.getConfigValue("supercode", "superaccesstoken");
    private String privatekey = ConfigUtil.getConfigValue("supercode", "superprivatekey");
    private String certCode = "11100000000013127D001";
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取热门事项
     * 
     * @params params
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/sendVerificationRequest", method = RequestMethod.POST)
    public String sendVerificationRequest(@RequestBody String params) throws IllegalArgumentException, IOException {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String qrcode = obj.getString("qrcode");
            JSONObject data = new JSONObject();
            data.put("qrcode", qrcode);
            JSONArray certCodes = new JSONArray();
            certCodes.add(certCode);
            data.put("certCodes", certCodes);
            // 请求头
            String requestHead = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
                    + "\"},\"data\": " + data + "}";
            String requestAddress = ConfigUtil.getConfigValue("supercode", "superipaddress")
                    + "/ChannelVerify/begin/verify";
            String resultData = TaHttpRequestUtils.sendPost(requestAddress, requestHead, "", "");
            JSONObject jsonObject = JSONObject.parseObject(resultData);
            if (!jsonObject.containsKey("head")) {
                return JsonUtils.zwdtRestReturn("0", "出现异常：接口返回值异常", "");
            }
            String returnHead = jsonObject.getString("head");
            JSONObject headObject = JSONObject.parseObject(returnHead);
            if (!headObject.containsKey("status")) {
                return JsonUtils.zwdtRestReturn("0", "出现异常：接口返回值异常", "");
            }
            if ("60001".equals(headObject.getString("status")) || "0".equals(headObject.getString("status"))) {
                String returnData = jsonObject.getString("data");
                byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey), Util.hexToByte(returnData));
                // 将字节数组转为字符串
                String getDataString = new String(decrypt2, "utf-8");
                JSONObject trxIdObject = JSONObject.parseObject(getDataString);
                String trxId = trxIdObject.getString("trxId");

                dataJson.put("trxId", trxId);

                return JsonUtils.zwdtRestReturn("1", "", dataJson.toJSONString());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "出现异常：" + headObject.getString("message"), "");
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }

    }

    @RequestMapping(value = "/getVerificationinfo", method = RequestMethod.POST)
    public String getVerificationinfo(@RequestBody String params) throws IllegalArgumentException, IOException {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String trxId = obj.getString("trxId");
            JSONObject data = new JSONObject();
            data.put("trxId", trxId);
            String requestHead = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
                    + "\"},\"data\": " + data + "}";
            String requestAddress = ConfigUtil.getConfigValue("supercode", "superipaddress")
                    + "/ChannelVerify/current/status";
            // 4.3核验当前状态接口
            String resultStatus = TaHttpRequestUtils.sendPost(requestAddress, requestHead, "", "");
            log.info("getVerificationinfo resultStatus " + resultStatus);
            JSONObject statusObject = JSONObject.parseObject(resultStatus);
            if (!statusObject.containsKey("head")) {
                return JsonUtils.zwdtRestReturn("0", "出现异常：接口返回值异常", "");
            }
            JSONObject statusHeadObject = JSONObject.parseObject(statusObject.getString("head"));
            if (!"0".equals(statusHeadObject.getString("status"))) {
                return JsonUtils.zwdtRestReturn("0", "出现异常：" + statusHeadObject.getString("message"), "");
            }
            else {
                if (!statusObject.containsKey("data")) {
                    return JsonUtils.zwdtRestReturn("0", "出现异常：接口返回值异常", "");
                }
                byte[] decryptStatus = SM2Utils.decrypt(Util.hexToByte(privatekey),
                        Util.hexToByte(statusObject.getString("data")));
                // 将字节数组转为字符串
                String getStatusString = new String(decryptStatus, "utf-8");
                // 解析4.3核验当前状态接口
                JSONObject getStatusStringObject = JSONObject.parseObject(getStatusString);
                String verifyStatus = getStatusStringObject.getString("verifyStatus");

                if ("2".equals(verifyStatus)) {

                    return provesByVerifyTrx(trxId, certCode);

                }
                else if ("1".equals(verifyStatus)) {

                    return JsonUtils.zwdtRestReturn("0", "未授权，请授权！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "授权失败！请重新授权", "");
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }

    }

    public String provesByVerifyTrx(String trxId, String certTypeCode) {
        try {
            String certificateSystemName = "泰安市政务服务平台";
            String certificateCopyCause = "非事项使用";
            String certificateCallCategory = "1";
            String certificateMatterName = "";
            String certificateEventCode = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("certificateSystemName", certificateSystemName);
            jsonObject.put("certificateCopyCause", certificateCopyCause);
            jsonObject.put("certificateCallCategory", certificateCallCategory);
            jsonObject.put("certificateMatterName", certificateMatterName);
            jsonObject.put("certificateEventCode", certificateEventCode);
            JSONObject data = new JSONObject();
            data.put("trxId", trxId);
            data.put("certTypeCode", certTypeCode);
            data.put("useCause", jsonObject.toString());
            String requestHead = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
                    + "\"},\"data\": " + data + "}";
            String requestAddress = ConfigUtil.getConfigValue("supercode", "superipaddress")
                    + "/CertShare/queryCertInfosByVerifyTrx";
            String resultData = TaHttpRequestUtils.sendPost(requestAddress, requestHead, "", "");
            JSONObject resultObject = JSONObject.parseObject(resultData);
            JSONObject headObject = resultObject.getJSONObject("head");
            if (!"0".equals(headObject.getString("status"))) {
                return JsonUtils.zwdtRestReturn("0", "出现异常：" + headObject.getString("message"), "");
            }
            else {
                JSONObject dataObject = resultObject.getJSONObject("data");
                byte[] decryptStatus = SM2Utils.decrypt(Util.hexToByte(privatekey),
                        Util.hexToByte(dataObject.getString("certData")));
                // 将字节数组转为字符串
                String getStatusString = new String(decryptStatus, "utf-8");
                if (StringUtil.isNotBlank(getStatusString)) {
                    JSONObject datajson = new JSONObject();
                    datajson.put("qrcodeinfo", getStatusString);
                    return JsonUtils.zwdtRestReturn("1", "", datajson.toJSONString());
                }
                return JsonUtils.zwdtRestReturn("0", "出现异常：未获取到人员信息", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }
    }

}

package com.epoint.cert.rest;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.util.CertCheckUtilsNew;
import com.epoint.cert.util.PushTokenUtil;
import com.epoint.cert.util.PushUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;

@RestController
@RequestMapping("/axzcert")
public class AxzCertUpdateController
{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    // 变更接口
    private static String updateUrl = ConfigUtil.getConfigValue("xmzArgs", "aqsgxkzsztupdateurl");
    // 参数加密key
    private static String key = ConfigUtil.getConfigValue("xmzArgs", "key");

    /**
     * 推送安许证注销接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/axzcertupdate", method = RequestMethod.POST)
    public String getCertinfo(@RequestBody String params) {
        try {
            log.info("=======开始调用axzcertupdate接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String citynum = "370800";
                // 获取token
                String certToken = PushTokenUtil.getCertToken();
                String associatedZzeCertID = obj.getString("associatedZzeCertID");
                String creditCode = obj.getString("creditCode");
                String certNum = obj.getString("certNum");
                JSONObject paramJson = new JSONObject();
                JSONObject AcceptData = new JSONObject();
                JSONObject certobj = new JSONObject();
                String eCertID = UUID.randomUUID().toString();
                certobj.put("eCertID", eCertID);
                certobj.put("zzeCertID", associatedZzeCertID);
                certobj.put("provinceNum", "370000");
                certobj.put("creditCode", creditCode);
                certobj.put("certNum", certNum);// 证书编号
                certobj.put("certState", "05");
                certobj.put("certStatusDescription", "");
                certobj.put("operateType", "05");
                String data = "";
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(certobj);
                AcceptData.put("AcceptData", jsonArray);
                paramJson.put("citynum", citynum);
                log.info("安许证加密前入参：" + AcceptData.toJSONString());
                data = PushUtil.sm4Encryption(AcceptData.toJSONString(), key);
                paramJson.put("data", data);

                log.info("========变更接口========r入参：" + paramJson.toJSONString() + "========接口调用地址：" + updateUrl);
                String returnStr = HttpUtil.doPostJson(updateUrl + "?access_token=" + certToken,
                        paramJson.toJSONString());
                if (StringUtil.isBlank(returnStr)) {
                    return JsonUtils.zwdtRestReturn("0", "安许证接口无返回值！", "");
                }
                else {
                    log.info("========变更接口========返回值：" + returnStr);
                    JSONObject returnJson = JSONObject.parseObject(returnStr);
                    String returnCode = returnJson.getString("ReturnCode");
                    if (StringUtil.isNotBlank(returnCode)) {
                        if ("0".equals(returnCode)) {
                            JSONObject returnData = returnJson.getJSONObject("ReturnData");
                            if (StringUtil.isNotBlank(returnData)) {
                                JSONArray errorData = returnData.getJSONArray("ErrorData");
                                JSONObject errorDataJson = errorData.getJSONObject(0);
                                return JsonUtils.zwdtRestReturn("0", "安许证接口调用报错！", errorDataJson.toString());
                            }
                        }
                        else if ("1".equals(returnCode)) {
                            return JsonUtils.zwdtRestReturn("1", "安许证注销成功！", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "失败！", "");
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "接口逻辑没执行！", "");
            }

            return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "失败！", "");
        }
    }

    /**
     * 生成安许证id接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getZzeCertID", method = RequestMethod.POST)
    public String getZzeCertID(@RequestBody String params) {
        try {
            log.info("=======开始调用getZzeCertID接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String CertNum = obj.getString("certnum");
                String certnum = obj.getString("certnum");
                String bbh = obj.getString("bbh");
                JSONObject returnobj = new JSONObject();
                String year = "";
                if (CertNum.length() > 6) {
                    CertNum = CertNum.substring(CertNum.length() - 6, CertNum.length());
                }

                if (certnum.length() > 14) {
                    // 安许证截取证照年份 例如：（鲁）JZ安许证字[2023]087974 截取中括号中的数字
                    year = certnum.substring(10, 14);
                }

                CertCheckUtilsNew utils = new CertCheckUtilsNew();
                String code = utils.getXy("11100000000013338W050113700000045030270" + year + CertNum + bbh);

                String associatedZzeCertID = "1.2.156.3005.2.11100000000013338W050.113700000045030270." + year + CertNum
                        + "." + bbh + "." + code;
                returnobj.put("associatedZzeCertID", associatedZzeCertID);
                log.info("=======结束调用getZzeCertID接口=======");
                return JsonUtils.zwdtRestReturn("0", "返回成功！", returnobj.toJSONString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "失败！", "");
        }
    }

}

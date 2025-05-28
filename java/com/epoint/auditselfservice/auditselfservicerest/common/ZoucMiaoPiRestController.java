package com.epoint.auditselfservice.auditselfservicerest.common;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping("/zoucselfservicemiaopi")
public class ZoucMiaoPiRestController
{

    @Autowired
    private IConfigService configServcie;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static final String APP_NAME = "自助服务终端"; // 应用名称

    public static final String CONSTANT_STR_TWENTY = "20";

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

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/submitproject", method = RequestMethod.POST)
    public String submitproject(@RequestBody String params) {
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
            String url = wttoytj + "rest/tazwdtProject2/private/submitProjectByTaskguid";
            Map<String, String> headers = new HashMap<>();
            headers.put("x-authenticated-userid", userid);
            headers.put("x-authenticated-clientid", clientid);
            return HttpUtil.doPostJsonSSL(url, obj.getString("jsondata"), headers);

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/checkmaterial", method = RequestMethod.POST)
    public String checkmaterial(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String taskmaterialguid = obj.getString("taskmaterialguid");
            String certificateNo = obj.getString("certificateNo");
            String wttoytj = configServcie.getFrameConfigValue("AS_Znsb_WTTOYTJ");
            if (StringUtil.isBlank(wttoytj)) {
                wttoytj = "http://jizwfw.sd.gov.cn/jnzwdt/";
            }
            String url = wttoytj + "rest/jnbigshowcert/getLicenceCertNoNew";
            Map<String, String> headers = new HashMap<>();
            JSONObject postJosn = new JSONObject();
            postJosn.put("token", "Epoint_WebSerivce_**##0601");
            JSONObject paramsJson = new JSONObject();
            paramsJson.put("taskmaterialguid", taskmaterialguid);
            paramsJson.put("certificateNo", Rasen(certificateNo));
            postJosn.put("params", paramsJson);
            return HttpUtil.doPostJsonSSL(url, postJosn.toJSONString(), headers);

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    public String Rasen(String data) {
        try {
            return RSAUtils.encryptByPubKey(data,
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSUmOXyQmYYSnZacp0btvAZCOvCNPtzixAp7eJmzmAG4mgy/VgrY/s1BDLh9qTNHIRWXepUtwMrf1kYul/A45qE/2oxIbeeq4238YDWQ7ModOVXR9ytEHsT0jpCFvoYfYXYZnnoWRrLIBylQeXzqxbLDxxBxGCs4AjoRKh5S7nNQIDAQAB");

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}

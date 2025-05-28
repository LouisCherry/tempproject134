package com.epoint.increase.office365.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditbrowser.util.FrameJsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping({"eszepointsform" })
public class EszEpointsform
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Resource
    private IConfigService iConfigService;

    @RequestMapping(value = {"eformToWordOrPdf" }, method = {RequestMethod.POST })
    public String eformToWordOrPdf(@RequestBody String params) {

        String result = "";

        try {
            JSONObject paramsObject = JSONObject.parseObject(params);
            String epointsformurl=iConfigService.getFrameConfigValue("epointsformurl");
            epointsformurl=epointsformurl.substring(0,epointsformurl.length()-1);
            if(paramsObject!=null){
                JSONObject params1 = paramsObject.getJSONObject("params");
                String rowguid="";
                String promiseEformid = "";
                JSONObject paramsInside=new JSONObject();
                JSONArray mainTable=new JSONArray();
                paramsInside.put("mainTable", mainTable);
                if(params1!=null){
                    rowguid=params1.getString("rowguid");
                    promiseEformid=params1.getString("formid");
                }
                String msg= FsEformUtil.savePageData(rowguid, promiseEformid, epointsformurl, paramsInside);
                log.info(msg);
            }

            result = HttpUtil.doPostJson(epointsformurl+"/rest/epointsform/eformToWordOrPdf1", params);
            DESEncrypt tools = new DESEncrypt();

            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject != null) {
                String attachUrl = jsonObject.getJSONObject("custom").getString("attachUrl");
                if (StringUtil.isNotBlank(attachUrl)) {
                    String attachUrl1 = jsonObject.getJSONObject("custom").getString("attachUrl");
                    if (StringUtil.isNotBlank(attachUrl1)) {
                        String[] split = attachUrl1.split("epoint-zwfwsform-web");
                        if (split.length > 1) {
                            String s = split[1];
                            //外网地址替换
                            String epointsformurlwt= iConfigService.getFrameConfigValue("epointsformurlwt");
                            epointsformurlwt = epointsformurlwt.substring(0,epointsformurlwt.length()-1);
                            String replaceUrl = epointsformurlwt+ s;
                            jsonObject.getJSONObject("custom").put("attachUrl", replaceUrl);
                            //加密key
                            String deskey = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
                            //加密向量
                            String desiv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
                            try {
                                com.epoint.core.utils.code.DESEncrypt des = new com.epoint.core.utils.code.DESEncrypt(deskey, desiv);
                                String encryptUrl = des.encode(replaceUrl).replaceAll("(\r\n|\n)", "");
                                String url = ConfigUtil.getFrameConfigValue("AS_OfficeWeb365_Server") + "/?furl="
                                        + encryptUrl;
                                jsonObject.getJSONObject("custom").put("prewurl", url);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }

                            // jsonObject.getJSONObject("custom").put("prewurlDecryption",
                            // ConfigUtil.getFrameConfigValue("AS_OfficeWeb365_Server")
                            // + "?ssl=1&furl=" + replaceUrl);
                        }
                    }
                }

            }
            if (jsonObject != null) {
                result = jsonObject.toJSONString();
            }
        }
        catch (Exception e) {
            log.error("异常", e);
            return FrameJsonUtils.zwbgRestReturn("0", "调用接口失败！", e.getMessage());
        }

        return result;
    }

    public static void main(String[] args) {
        String url = "https://60.211.226.110:8443/epoint-zwfwsform-web/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=03c6c36f-b6e4-491a-a9a2-da86c3621480";
//        String url = "https://60.211.226.110:8443/epoint-web-survey/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=af90d452-eb37-4468-90b9-72a984fa1602";
//        String[] split = url.split("epoint-zwfwsform-web");
//        String s = split[1];

        DESEncrypt tools = null;
        try {
//            tools = new DESEncrypt("12345678","87654321");
            tools = new DESEncrypt();
//            String url1 = "https://60.211.226.110:8443/officeweb?ssl=1&furl=" + tools.encode(url).replaceAll("(\r\n|\n)", "");
            String url1 = "https://60.211.226.110:8443/officeweb/?ssl=1&furl=" +url;

            System.out.println(url1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}

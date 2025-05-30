
package com.epoint.xmz.wzds;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;

@RestController
@RequestMapping("/jnwzdsnotice")
public class JnWzdsNoticeController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * 获取提交超限通行证的accesstoken值
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getInfoList", method = RequestMethod.POST)
    public String getInfoList(@RequestBody String params) {
        try {
            log.info("=======开始调用getInfoList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                String param = jsonObject.getString("params");
                String urlparam = URLEncoder.encode(param);
                String apiUrl = "http://112.6.110.176:28080/epoint-web-wzds/rest/webBuilderWebServiceForMicroPortalImpl/getInfoList?params=" + urlparam;
                String json = "{\"token\":\"Epoint_WebSerivce_**##0601\",\"params\":\"\"}";

                String result = com.epoint.core.utils.httpclient.HttpUtil.doPostJson(apiUrl, json);

                return JsonUtils.zwdtRestReturn("1", "获取网站大师清单成功：", result);
            } else {
                log.info("=======结束调用getMyCompanyList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getInfoList接口参数：params【" + params + "】=======");
            log.info("=======getInfoList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取网站大师清单失败：" + e.getMessage(), "");
        }
    }


    /**
     * 新增企业信息接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getInfoDetail", method = RequestMethod.POST)
    public String getInfoDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getInfoDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                String param = jsonObject.getString("params");
                String urlparam = URLEncoder.encode(param);
                String apiUrl = "http://112.6.110.176:28080/epoint-web-wzds/rest/webBuilderWebServiceForMicroPortalImpl/getInfoDetail?params=" + urlparam;
                String json = "{\"token\":\"Epoint_WebSerivce_**##0601\",\"params\":\"\"}";

                String result = com.epoint.core.utils.httpclient.HttpUtil.doPostJson(apiUrl, json);

                return JsonUtils.zwdtRestReturn("1", "获取公告详细信息成功：", result);
            } else {
                log.info("=======结束调用getMyCompanyList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getInfoDetail接口参数：params【" + params + "】=======");
            log.info("=======getInfoDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取网站大师详细信息接口失败：" + e.getMessage(), "");
        }
    }


}

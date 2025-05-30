package com.epoint.auditsp.sqnbz.controller;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;

/**
 * 对接接口
 *
 * @author shibin
 * @description
 * @date 2020年6月29日 上午10:13:47
 */
@RestController
@RequestMapping("/apppletdocking")
public class AppletDockingController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = "/intoout", method = RequestMethod.POST)
    public String InToOut(@RequestBody String params) {
        try {
            log.info("=========调用intoout接口=========");
            log.info("=========params=========：" + params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            String url = jsonObject.getString("sendu");
            JSONObject paramsstr = jsonObject.getJSONObject("sendparams");
            JSONObject header = jsonObject.getJSONObject("sendheader");
            Map<String, String> headers = new HashMap<>();
            for (String key : header.keySet()) {
                String value = header.getString(key);
                headers.put(key, value);
            }
            String result = HttpUtil.doPostJson(url, paramsstr.toString(), headers);
            JSONObject resultobj = JSONObject.parseObject(result);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功！", resultobj);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======pushyuyueinfo接口参数：params【" + params + "】=======");
            log.info("=======pushyuyueinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息出现异常：" + e.getMessage(), "");
        }
    }

}

package com.epoint.jn.qh.statistics.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.jn.qh.statistics.api.IQhStatistics;

@RestController
@RequestMapping("/qhstatistics")
public class QhStatisticsRestController
{
    @Autowired
    private IQhStatistics service;

    @RequestMapping(value = "/setCount", method = RequestMethod.POST)
    public String setCount(@RequestBody String params) {
        try {
            JSONObject e = JSONObject.parseObject(params);
            JSONObject obj = e.getJSONObject("params");
            String macaddress = obj.getString("macaddress");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            service.setCount(macaddress,centerguid);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功！", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用异常："+e.getMessage(), "");
        }

    }
}

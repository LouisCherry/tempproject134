package com.epoint.xmz.dataexcahnge.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.xmz.dataexcahnge.api.ISzjtXmExchangeService;

/**
 * 
 * @作者 CTY
 * @version [版本号, 2019年3月12日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping("/xmjg")
public class SzjtXmExchangeAction
{

    @Autowired
    private ISzjtXmExchangeService szjtXmExchangeService;

    /**
     * 调用项目数据
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/projectWebservice", method = RequestMethod.POST)
    public Object getProjectInfo(@RequestBody String params) {
        JSONObject json = JSON.parseObject(params);
        JSONObject object = (JSONObject) json.get("params");
        String projectdm = object.getString("projectdm");// 项目代码
        String creditcode = object.getString("creditcode");// 统一社会信用代码
        Object n = szjtXmExchangeService.getProjectInfo(projectdm, creditcode);
        if ("2".equals(n)) {
            JSONObject obj=new JSONObject();
            obj.put("result", "项目代码不存在");
            obj.put("resultvalue", n);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", obj.toString());
        }
        else if ("3".equals(n)) {
            JSONObject obj=new JSONObject();
            obj.put("result", "该项目已存在，请勿重复获取");
            obj.put("resultvalue", n);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功",obj.toString());
        }
        else if ("1".equals(n)) {
            JSONObject obj=new JSONObject();
            obj.put("result", "项目获取成功");
            obj.put("resultvalue", n);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", obj.toString());
        }
        else if ("4".equals(n)) {
            JSONObject obj=new JSONObject();
            obj.put("result", "拉取项目的统一社会信用代码与当前企业不匹配");
            obj.put("resultvalue", n);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功",obj.toString());
        }
        else {
        	 JSONObject obj=new JSONObject();
             obj.put("result", "接口调用失败");
             obj.put("resultvalue", n);
             return JsonUtils.zwdtRestReturn("0", "接口调用失败", obj.toString());
        }
    }

    /**
     * 证照接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCertificate", method = RequestMethod.POST)
    public String getCertificate(@RequestBody String params) {
        JSONObject json = JSON.parseObject(params);
        String url = json.getString("url");
        String jsonStr = json.getString("jsonStr");
        return szjtXmExchangeService.getCertificate(url, jsonStr);
    }

    @RequestMapping(value = "/clshyjWebservice", method = RequestMethod.POST)
    public Object getclshyj(@RequestBody String params) throws UnsupportedEncodingException {
        JSONObject json = JSON.parseObject(params);
        JSONObject object = (JSONObject) json.get("params");
        String cliengguid = object.getString("clientguid");
        String notpassreason = szjtXmExchangeService.getclshyj(cliengguid).toString();
        Map<String, Object> map = new HashMap<>();
        map.put("notpassreason", notpassreason);
        String jsonrtn = JsonUtil.objectToJson(map);
        return jsonrtn;
    }
}

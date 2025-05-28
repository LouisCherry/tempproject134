package com.epoint.jn.qhj.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.common.util.JsonUtils;

@RestController
@RequestMapping("/jnqhj")
public class JnQhjRestController
{

    @Autowired
    private IAuditOrgaHall hallservice;

    @RequestMapping(value = "/getHallName", method = RequestMethod.POST)
    public String getHallName(@RequestBody String params) {
        try {
            JSONObject json = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = json.getJSONObject("params");
            String hallguid = obj.getString("hallguid");
            JSONObject dataJson = new JSONObject();
            dataJson.put("hallname", hallservice.getAuditHallByRowguid(hallguid).getResult().getHallname());
            return JsonUtils.zwdtRestReturn("1", "获取大厅名称成功！", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口出现异常：" + e, "");
        }
    }

}

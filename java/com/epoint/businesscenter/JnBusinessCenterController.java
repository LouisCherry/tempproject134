package com.epoint.businesscenter;

import cn.hutool.crypto.SmUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.YwztUtils;
import com.epoint.core.utils.string.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * 对接业务中台跳转接口
 */
@RestController
@RequestMapping("/jnbusinesscenter")
public class JnBusinessCenterController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * 测试apimanager接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getWeburl", method = RequestMethod.POST)
    public String getAccessToken(@RequestBody String params) {
        log.info("=======开始调用getWeburl接口=======");
        JSONObject param = JSONObject.parseObject(params);
        String json = param.getString("params");
        String taskguid = JSONObject.parseObject(json).getString("taskguid");
        return YwztUtils.getWeburl(taskguid);
    }



}

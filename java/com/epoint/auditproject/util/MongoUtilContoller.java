package com.epoint.auditproject.util;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;

@RestController
@RequestMapping("/mongoutilcertinfoext")
public class MongoUtilContoller
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    @RequestMapping(value = "/getCertInfExt", method = RequestMethod.POST)
    public String getCertInfExt(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            String certinfoguid = json.getString("certinfoguid");
            if (StringUtil.isBlank(certinfoguid)) {
                return JsonUtils.zwdtRestReturn("0", "certinfoguid为空", "");
            }
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfoguid);
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            JSONObject result = new JSONObject();
            result.put("data", certinfoExtension.toString());
            return JsonUtils.zwdtRestReturn("1", "", result);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}

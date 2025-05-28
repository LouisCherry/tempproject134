package com.epoint.auditproject.util.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.certcbyyysz.api.ICertCbyyyszService;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.cxbus.api.ICxBusService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AbstractFinishAuditProjectService
 * @Description
 * @Author rachel
 * @Date 2023/5/26 17:59
 **/
public abstract class AbstractFinishAuditProjectService implements FinishAuditProjectInterface {

    public IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);

    public ICertCbyyyszService iCertCbyyyszService = ContainerFactory.getContainInfo().getComponent(ICertCbyyyszService.class);

    public ICxBusService iCxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);

    public ICertHwslysjyxkService iCertHwslysjyxkService = ContainerFactory.getContainInfo().getComponent(ICertHwslysjyxkService.class);

    public ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);

    public IAuditTaskExtension iAuditTaskExtension  = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);

    public IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

    // protected final static String EPOINTFORMURL = ConfigUtil.getFrameConfigValue("epointsformurl");

    public static final transient Logger log = Logger.getLogger(AbstractFinishAuditProjectService.class);


    public MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    public String getFormTable(String formid) {
        try {
            Map<String, String> header = new HashMap<String, String>() {{
                put("Content-Type", "application/x-www-form-urlencoded");
            }};
            JSONObject params = new JSONObject() {{
                put("formId", formid);
                put("versionGuid", "");
                put("formIds", "");
            }};
            JSONObject resParams = new JSONObject() {{
                put("params", params);
            }};
            log.info(">>>> 电子表单入参：" + params);
            String url = configService.getFrameConfigValue("epointsformurl_innernet_ip") + "rest/sform/getEpointSformInfo";
            log.info(">>>> 电子表单url：" + url);
            String ret = HttpUtil.doHttp(configService.getFrameConfigValue("epointsformurl_innernet_ip") + "rest/sform/getEpointSformInfo", header, resParams, "post", 2);
            if (StringUtil.isNotBlank(ret)) {
                log.info(">>>> 电子表单返回结果：" + ret);
                JSONObject object = JSON.parseObject(ret);
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(object.getJSONObject("status").getString("code"))) {
                    return object.getJSONObject("custom").getJSONObject("formData").getJSONObject("formInfo").getString("sqltablename");
                }
            }
            else {
                log.info(">>>> 电子表单返回结果为空！");
            }
        } catch (Exception e) {
            log.info(">>>> 获取电子表单表名出现异常！");
            e.printStackTrace();
        }
        return null;
    }
}

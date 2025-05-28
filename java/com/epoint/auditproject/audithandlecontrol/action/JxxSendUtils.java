package com.epoint.auditproject.audithandlecontrol.action;

import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 金乡县推送7类证照数据至浪潮前置库
 */
public class JxxSendUtils {

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    public void JxxSendCertInfoToLc(CertInfo certinfo) {
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certinfo.getRowguid());
        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        if (certinfoExtension == null) {
            certinfoExtension = new CertInfoExtension();
        }

        Record ylgg = new Record();
        ylgg.setSql_TableName("EX_JSGCXFYSBAPZ_1");
        ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
        String n = certinfoExtension.getStr("nian");
        String h = certinfoExtension.getStr("hao");
        ylgg.set("LICENSE_NUMBER", "金住建消备凭字〔" + n + "〕第" + h + "号");
        ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
        if (StringUtil.isNotBlank(certinfoExtension.getDate("sqsj"))) {
            ylgg.set("SHENQINGRIQI", EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqsj"), "yyyy-MM-dd"));
        }

        ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
        ylgg.set("DIZHI", certinfoExtension.getStr("dz"));
        ylgg.set("BEIANSHENQINGBIANHAO", certinfoExtension.getStr("basqbh"));

        ylgg.set("CAILIAOYI", certinfoExtension.getStr("cly"));
        ylgg.set("CAILIAOER", certinfoExtension.getStr("cle"));
        ylgg.set("CAILIAOSAN", certinfoExtension.getStr("cls"));
        ylgg.set("XUANZEYI", certinfoExtension.getStr("xzy"));
        ylgg.set("XUANZEER", certinfoExtension.getStr("xze"));
        if (StringUtil.isNotBlank(certinfoExtension.getDate("gzrq"))) {
            ylgg.set("CERTIFICATE_DATE", EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"), "yyyy-MM-dd"));
        }
        ylgg.set("rowguid", UUID.randomUUID().toString());
        ylgg.setPrimaryKeys("rowguid");
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JSGCXFYSBAPZ_1",
                ylgg.getStr("LICENSE_NUMBER"));

        if (ylggrecord != null) {
            ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
            ylgg.set("state", "update");
            iJnProjectService.UpdateRecord(ylgg);
        } else {
            ylgg.set("state", "insert");
            iJnProjectService.inserRecord(ylgg);
        }
    }

}

package com.epoint.cert.job;

import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.util.PushUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 竣工验收备案证照数据归集
 */
@DisallowConcurrentExecution
public class JsgcjgysbaxxCertGjlogJob implements Job {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
        try {
            // 事务开启
            EpointFrameDsManager.begin(null);
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("ifjgysgj", "0");
            sqlUtils.setSelectCounts(50);
            List<CertInfo> list = iCertInfo.getListByCondition(sqlUtils.getMap());
            if (!list.isEmpty()) {
                for (CertInfo certInfo : list) {
                    certGjation(certInfo);
                }
            }
        }
        catch (Exception ex) {
            // 事务回滚
            EpointFrameDsManager.rollback();
            log.error("推送异常：" + ex);
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }
    
    /**
     * 保存并关闭
     */
    public void certGjation(CertInfo certInfo ) {
        ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
        String fmCode = certInfo.get("ecertid");
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", certInfo.getRowguid());
        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        String JGYSBABH = certinfoExtension.get("bh");
        String Zzcertid = certInfo.get("associatedZzeCertID");
        log.info("fmcode:"+fmCode);
        log.info("JGYSBABH:"+JGYSBABH);
        log.info("Zzcertid:"+Zzcertid);
        if(StringUtils.isNotBlank(fmCode) && StringUtils.isNotBlank(JGYSBABH) && StringUtils.isNotBlank(Zzcertid)) {
            JSONObject gjationJson = new JSONObject();
            gjationJson.put("fmCode", fmCode);
            gjationJson.put("JGYSBABH", JGYSBABH);
            gjationJson.put("Zzcertid", Zzcertid);
            JSONObject returnJson = PushUtil.jgyscerguiji(gjationJson,certInfo.getAreacode());
            if(returnJson != null) {
                boolean flag = returnJson.getBoolean("sucess");
                if(flag) {
                    certInfo.set("ifjgysgj","1");
                }else{
                    log.info("errormsg:"+returnJson.getString("msg"));
                    certInfo.set("ifjgysgj","2");
                    certInfo.set("gjerror",returnJson.getString("msg"));
                }
            }
        }else{
            log.info("certinfo:"+certInfo.getRowguid());
            certInfo.set("ifjgysgj","2");
        }
        iCertInfo.updateCertInfo(certInfo);
    }

}

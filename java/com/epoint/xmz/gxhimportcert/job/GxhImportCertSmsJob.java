package com.epoint.xmz.gxhimportcert.job;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCert;

@DisallowConcurrentExecution
public class GxhImportCertSmsJob implements Job
{

    @Autowired
    private IGxhImportCertService service;

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("is_zb", "1");
            sql.eq("smstatus", "1");
            sql.lt("smsdate", EpointDateUtil.addDay(new Date(), -1));
            int count = service.getListCount(sql.getMap());

            int max = count / 50 + 1;
            for (int i = 0; i < max; i++) {
                // 开启事务
                EpointFrameDsManager.begin(null);
                PageData<GxhImportCert> pageData = service.findPageData(sql.getMap(), i * 50, 50, "", "");
                if (pageData != null) {
                    for (GxhImportCert cert : pageData.getList()) {
                        cert.setSmstatus("3");
                        service.update(cert);
                    }
                }
                // 提交事务
                EpointFrameDsManager.commit();
            }

        }
        catch (Exception e) {
            // 回滚事务
            if (!e.getCause().getClass().getName().endsWith("TransactionException")) {
                EpointFrameDsManager.rollback();
            }
            e.printStackTrace();
        }
        finally {
            // 关闭数据源
            EpointFrameDsManager.close();
        }
    }
}

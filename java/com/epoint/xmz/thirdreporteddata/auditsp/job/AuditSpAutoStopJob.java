package com.epoint.xmz.thirdreporteddata.auditsp.job;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

public class AuditSpAutoStopJob implements Job {
    transient Logger log = LogUtil.getLog(AuditSpAutoStopJob.class);

    /* 3.0新增根据主题配置停用日期对主题进行停用*/
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            log.info("--------------AuditSpAutoStopJob检查主题配置停用日期开始-------------------------");
            IAuditSpBusiness iAuditSpBusiness = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpBusiness.class);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("del", "0");
            sqlConditionUtil.le("TYRQ", new Date());
            List<AuditSpBusiness> result = iAuditSpBusiness.getAllAuditSpBusiness(sqlConditionUtil.getMap()).getResult();
            if (EpointCollectionUtils.isNotEmpty(result)) {
                for (AuditSpBusiness auditSpBusiness : result) {
                    auditSpBusiness.setDel("1");
                    iAuditSpBusiness.saveUpdateAuditSpBusiness(auditSpBusiness);
                }
            }
            log.info("--------------AuditSpAutoStopJob检查主题配置停用日期结束-------------------------");
            EpointFrameDsManager.commit();
        } catch (Exception ex) {
            EpointFrameDsManager.rollback();
            ex.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

}

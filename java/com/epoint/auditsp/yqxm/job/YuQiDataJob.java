package com.epoint.auditsp.yqxm.job;

import com.epoint.auditsp.yqxm.api.ICrawlDataDY;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created on 2022/6/6.
 * 将spgl_的数据推至st_spgl_
 * @author ${阳佳}
 */
@DisallowConcurrentExecution
public class YuQiDataJob implements Job {
	 private static String areacode = ConfigUtil.getConfigValue("datasyncjdbc", "ggareacode");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ICrawlDataDY iCrawlDataDY = ContainerFactory.getContainInfo().getComponent(ICrawlDataDY.class);
        IAuditOrgaArea iAuditOrgaArea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        try {
            EpointFrameDsManager.begin(null);
            //system.out.println("=====开始服务=====");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("xiaqucode",areacode);
            List<AuditOrgaArea> list = iAuditOrgaArea.getAuditAreaPageData(sql.getMap(),0,0,"","").getResult().getList();
            for (AuditOrgaArea auditOrgaArea : list) {
                EpointFrameDsManager.begin(null);
                iCrawlDataDY.pushDataToStTables(auditOrgaArea.getXiaqucode());
                EpointFrameDsManager.commit();
            }
            EpointFrameDsManager.commit();
            //system.out.println("=====结束服务=====");
        }
        catch (Exception e){
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }
}

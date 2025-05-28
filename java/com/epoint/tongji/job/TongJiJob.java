package com.epoint.tongji.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.tongji.auditproject.api.IJnBaobiaoService;

@DisallowConcurrentExecution
public class TongJiJob implements Job
{
    IJnBaobiaoService service = ContainerFactory.getContainInfo().getComponent(IJnBaobiaoService.class);
    IAuditOrgaArea iservice = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
    transient Logger log = LogUtil.getLog(TongJiJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    public void doService() {
        // TODO Auto-generated method stub
        log.info("开始调用按部门统计办件量job");
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        ICommonDao commonDao = CommonDao.getInstance();
        List<AuditOrgaArea> listarea = iservice.selectAuditAreaList(null).getResult();

        int days = EpointDateUtil.getIntervalDays(startDate, endDate);
        for (AuditOrgaArea auditOrgaArea : listarea) {
            if (auditOrgaArea.getXiaqucode().length() == 6) {
                for (int i = 0; i <= days + 1; i++) {
                    String start = EpointDateUtil.convertDate2String(EpointDateUtil.getDateAfter(startDate, i));
                    String end = EpointDateUtil.convertDate2String(endDate);
                    commonDao.executeProcudure("ASP_Report_ProjectByly", start, end, auditOrgaArea.getXiaqucode());
                    commonDao.executeProcudure("ASP_Report_ProjectByOU", start, end, auditOrgaArea.getXiaqucode());
                    commonDao.executeProcudure("ASP_Report_ProjectByArea", start, end, auditOrgaArea.getXiaqucode());
                    commonDao.executeProcudure("ASP_Report_ProjectBykeshi", start, end, auditOrgaArea.getXiaqucode());
                    commonDao.executeProcudure("ASP_Report_ProjectByShenpilb", start, end,
                            auditOrgaArea.getXiaqucode());
                }
            }
        }
        log.info("结束调用按部门统计办件量job");

    }

}

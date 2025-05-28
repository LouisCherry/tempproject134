package com.epoint.hcp.job;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.hcp.api.IMsgCenterService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * 注释加载：防止并发产生
 *
 * @author swy
 */
@DisallowConcurrentExecution
public class SendMsgProjectJob implements Job {
    transient Logger log = LogUtil.getLog(SendMsgProjectJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doService();
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();

        } finally {
            EpointFrameDsManager.close();
        }
    }

    public void doService() {
        List<Record> messagelist = AuditProjectWpj.getWpjProjectList();
        IMsgCenterService messageservice = ContainerFactory.getContainInfo()
                .getComponent(IMsgCenterService.class);
        log.info("济宁办件短信评价===" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        if (messagelist.size() > 0) {
            for (Record record : messagelist) {
                String projectguid = record.get("RowGuid");
                String content = "尊敬的服务对象" + "," + "您好!" + "您于【" + record.get("applydate") + "】申请办理了【"
                        + record.get("projectname") + "】相关业务,请对我们的服务进行评价【" + "http://jizwfw.sd.gov.cn/jnzwdt/jnzwdt/pages/msgevaluate/evaluate_add.html?projectguid="
                        + projectguid + "】感谢您的参与。";
                messageservice.insert(projectguid, content, record.get("contactmobile"), record.get("areacode"), EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                AuditProjectWpj.updateProjectMsg(projectguid);
            }

        }
    }
}

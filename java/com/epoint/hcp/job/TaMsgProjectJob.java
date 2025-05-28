package com.epoint.hcp.job;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.hcp.api.IMsgCenterService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;

/**
 * 注释加载：防止并发产生
 *
 * @author swy
 */
@DisallowConcurrentExecution
public class TaMsgProjectJob implements Job {
    transient Logger log = LogUtil.getLog(TaMsgProjectJob.class);

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
        IMsgCenterService msgService = ContainerFactory.getContainInfo()
                .getComponent(IMsgCenterService.class);
        IMessagesCenterService messageservice = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        List<Record> msg = msgService.getMsgList("370900000000");//泰安市 
        if (msg.size() > 0) {
            log.info("济宁短信办件评价插入message_center===" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            for (Record record : msg) {
                String projectguid = record.get("RowGuid");
                String content = record.get("content");
                String contactmobile = record.get("mobile");
                String areacode = record.get("areacode");
                messageservice.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
                        contactmobile, UUID.randomUUID().toString(), "", "", areacode, "",
                        "", null, false, "");
                msgService.update(projectguid);
            }

        }
    }
}

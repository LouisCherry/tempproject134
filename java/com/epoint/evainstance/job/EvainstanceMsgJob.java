package com.epoint.evainstance.job;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.core.utils.string.StringUtil;
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
 * @author swy
 *
 */
@DisallowConcurrentExecution
public class EvainstanceMsgJob implements Job
{
    transient Logger log = LogUtil.getLog(EvainstanceMsgJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);

            doService();
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();

        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    public void doService() {
        List<Record> messagelist = EvainstanceMsgService.getEvaList();
        IMessagesCenterService messageservice = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        if (messagelist.size() > 0) {
            log.info( EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss") +"差评评价发送待办===" + "共"
                    + messagelist.size() + "条");
            for (Record record : messagelist) {
                String areacode = record.get("areacode");
                String taskname = record.get("taskname");
                String username = record.get("username");
                Date createdate = record.getDate("createdate");
                String title = username + "在" + EpointDateUtil.convertDate2String(createdate, "yyyy-MM-dd HH:mm:ss")
                        + "对【" + taskname + "】发起了一条差评，请您处理。";
                List<Record> userlist = EvainstanceMsgService.getKhkUser();
                for (Record user : userlist) {
                    String userareacode = user.get("areacode");
                    if (areacode.equals(userareacode) ) {
                        messageservice.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                                messageservice.MESSAGETYPE_READ, user.get("USERGUID"), user.get("DISPLAYNAME"), "", "",
                                "差评提醒", "tazwfw/evainstance/evainstance/evainstancelist?guid="+record.get("rowguid"), user.get("OUGUID"), "", 1, "", "", "", "", new Date(), "", "", "", "");
                        EvainstanceMsgService.updateEvaMsg(record.get("rowguid"));
                    }
                }
            }

        }
    }
}

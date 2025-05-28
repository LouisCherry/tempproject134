package com.epoint.sghd.auditjianguan.rlconfig.job;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.sghd.auditjianguan.rlconfig.api.IRlconfigSendmsg;
import com.epoint.sghd.auditjianguan.rlconfig.api.RlconfigSendmsg;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 发送审管通知短信job
 *
 * @version 1.0
 * @Author Ljw
 * @Date 2023/5/18
 */
@DisallowConcurrentExecution
public class RlconfigSendmsgJob implements Job {

    private static final Logger logger = Logger.getLogger(RlconfigSendmsgJob.class);

    private IRlconfigSendmsg service = ContainerFactory.getContainInfo().getComponent(IRlconfigSendmsg.class);
    IMessagesCenterService imessageservice = ContainerFactory.getContainInfo()
            .getComponent(IMessagesCenterService.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            logger.info("发送审管通知短信开始");
            EpointFrameDsManager.begin(null);
            List<RlconfigSendmsg> list = service.findtosendmsg();
            if (!list.isEmpty()) {
                for (RlconfigSendmsg rlconfigsendmsg : list) {
                    String phone = rlconfigsendmsg.getStr("rlmobile");
                    String areacode = StringUtil.isNotBlank(rlconfigsendmsg.getStr("areacode")) ? rlconfigsendmsg.getStr("areacode") : "370800";
                    //当日已认领
                    int YrlCount = service.getTaJianGuanTabYrlCount(areacode, rlconfigsendmsg.getStr("rlouguid"));
                    //当日未认领
                    int WrlCount = service.getTaJianGuanTabWrlCount(areacode, rlconfigsendmsg.getStr("rlouguid"));
                    //只有需要认领的数据才会发短信
                    if (WrlCount != 0) {
                        String messagecontent = "您好，" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT) + "，审管一体化平台共有" + (YrlCount + WrlCount) + "条办件需认领，已认领" + YrlCount + "件，未认领" + WrlCount + "件，请及时认领，谢谢。";
                        String messageguid = UUID.randomUUID().toString();
                        String targetuser = UUID.randomUUID().toString();
                        imessageservice.insertSmsMessage(messageguid, messagecontent,
                                new Date(), 0, new Date(), phone, targetuser, "", "",
                                areacode, "", "", "", false, "");
                        EpointFrameDsManager.commit();
                        if (imessageservice.getDetail(messageguid, targetuser) != null) {
                            service.updateSendinfo(rlconfigsendmsg.getRowguid(), messagecontent + ",messageguid:" + messageguid);
                            EpointFrameDsManager.commit();
                        }
                    } else {
                        service.updateSendinfo(rlconfigsendmsg.getRowguid(), rlconfigsendmsg.getStr("rlouname") + ":当日无未认领的数据！");
                        EpointFrameDsManager.commit();
                    }
                }
            }
            logger.info("发送审管通知短信结束");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("发送审管通知短信出错：" + e.getMessage());
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
        }
    }
}

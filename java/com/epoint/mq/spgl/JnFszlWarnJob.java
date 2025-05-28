package com.epoint.mq.spgl;

import com.epoint.common.util.StringUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JnFszlWarnJob implements Job {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            sendFszlWarnMsg();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendFszlWarnMsg() {
        String fszlWarnDay = ConfigUtil.getFrameConfigValue("fszl_warn_day");
        String fszlWarnTemplate = ConfigUtil.getFrameConfigValue("fszl_warn_template");
        if (StringUtil.isNotBlank(fszlWarnDay)) {
            IAuditFszlService iAuditFszlService = ContainerFactory.getContainInfo().getComponent(IAuditFszlService.class);
            IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("sendtip", "0");
            sqlConditionUtil.le("nextjydate", EpointDateUtil.addDay(new Date(), Integer.valueOf(fszlWarnDay)));
            List<AuditFszl> auditFszlList = iAuditFszlService.findList(sqlConditionUtil.getMap());

            Date sendDate = new Date();
            for (AuditFszl auditFszl : auditFszlList) {
                String content = getMsgContentFromTemp(fszlWarnTemplate, auditFszl) ;
                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, sendDate, 0,
                        null, auditFszl.getLxdh(), auditFszl.getLxdh(), "",
                        "", "", "", "", "", false, "短信");
                auditFszl.setSendtip("1");
                iAuditFszlService.update(auditFszl);
            }
        }
    }

    private String getMsgContentFromTemp(String fszlWarnTemplate, AuditFszl auditFszl) {
        /**
         * 温馨提示：【#yljgmc】您好！您办理的《放射诊疗许可证》【#certno】校验将于#nextjydate到期，
         * 请您在证书有效期内登陆山东政务服务网办理放射诊疗校验业务，避免证件脱审给您带来的不便。咨询服务电话：0537-7213628
         */
        String msgContent = fszlWarnTemplate.replaceAll("#yljgmc", auditFszl.getZyfzr())
                .replaceAll("#certno", auditFszl.getCertno())
                .replaceAll("#nextjydate", EpointDateUtil.convertDate2String(auditFszl.getNextjydate(), "yyyy年MM月dd日"));
        return msgContent;
    }

}

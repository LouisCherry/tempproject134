package com.epoint.sghd.sg.job;

import com.epoint.basic.auditorga.auditmember.domain.AuditOrgaMember;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.sghd.sg.service.IGxhAuditOrgaMemberTask;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;

@DisallowConcurrentExecution
public class SgRlJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            IAuditOrgaWorkingDay auditOrgaWorkingDay = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWorkingDay.class);
            if (auditOrgaWorkingDay.isWorkingDay("8adfc4f0-ce41-453e-94a7-14356a44db79", new Date()).getResult()) {
                doService();
            }
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
        }
    }


    public void doService() {
        IGxhAuditOrgaMemberTask renlingService = ContainerFactory.getContainInfo().getComponent(IGxhAuditOrgaMemberTask.class);
        IAuditOrgaMember ouService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaMember.class);
        List<Record> recordList = renlingService.getRenlingListByAreacode("370911000000");
        IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
        for (Record record : recordList) {
            String taskid = record.getStr("task_id");
            List<Record> memberguid = renlingService.getMemberGuidByTaskId(taskid);
            if (memberguid != null && memberguid.size() > 0) {
                for (Record user : memberguid) {
                    Map<String, String> map = new HashMap<>();
                    map.put("rowguid = ", user.getStr("memberguid"));
                    AuditOrgaMember orgaMember = ouService.getAuditMember(map).getResult();
                    if (StringUtil.isNotBlank(orgaMember)) {
                        String phone = orgaMember.getPhone();
                        if (StringUtil.isNotBlank(phone)) {
                            String content = "，,，,。";
                            messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
                                    phone, UUID.randomUUID().toString(), "", "", "370911000000", "",
                                    "", null, false, "");
                        }
                    }
                }
            }
        }

    }
}

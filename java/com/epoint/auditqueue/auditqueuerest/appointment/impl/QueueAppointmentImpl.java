package com.epoint.auditqueue.auditqueuerest.appointment.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.auditqueue.auditqueuerest.appointment.api.ICzQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

@Component
@Service
public class QueueAppointmentImpl implements ICzQueueAppointment
{

    @Override
    public AuditCommonResult<List<Record>> getAppointOuByOuguid(String centerguid) {
        QueueAppointmentService service = new QueueAppointmentService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(service.getAppointOuByCenterguid(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditZnsbCentertask>> getAppointTaskByOuguid(String ouguid, String index, String size) {
        QueueAppointmentService service = new QueueAppointmentService();
        AuditCommonResult<List<AuditZnsbCentertask>> result = new AuditCommonResult<List<AuditZnsbCentertask>>();
        try {
            int first = Integer.valueOf(index) * Integer.valueOf(size);
            int end = first + Integer.valueOf(size);
            result.setResult(service.getAppointTaskByOuguid(ouguid,first,end));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditQueueYuyuetime getYuyuetimeByTasktypeguid(String tasktypeguid) {
        QueueAppointmentService service = new QueueAppointmentService();
        return service.getYuyuetimeByTasktypeguid(tasktypeguid);
    }

    @Override
    public AuditQueueUserinfo getUserinfoByCardnum(String sfz) {
        QueueAppointmentService service = new QueueAppointmentService();
        return service.getUserinfoByCardnum(sfz);
    }

    @Override
    public int updateEnd_dateByCardnum(String sfz) {
        QueueAppointmentService service = new QueueAppointmentService();
        return service.updateEnd_dateByCardnum(sfz);
    }

    @Override
    public int updateBlackByCardnum(String sfz, String rule_punish) {
        QueueAppointmentService service = new QueueAppointmentService();
        return service.updateBlackByCardnum(sfz, rule_punish);
    }

    @Override
    public int getCountByCardnum(String sfz, String fromdate) {
        QueueAppointmentService service = new QueueAppointmentService();
        return service.getCountByCardnum(sfz, fromdate);
    }

    @Override
    public int getCountByCardnum(String sfz, String fromdate, String tasktypeguid) {
        QueueAppointmentService service = new QueueAppointmentService();
        return service.getCountByCardnum(sfz, fromdate, tasktypeguid);
    }
    


}

package com.epoint.auditqueue.auditqueuerest.appointment.api;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

public interface ICzQueueAppointment
{
    /**
     * 查找预约的规则设置
     */
    public AuditQueueYuyuetime getYuyuetimeByTasktypeguid(String tasktypeguid);
    /**
     * 查找当前人员状态
     */
    public AuditQueueUserinfo getUserinfoByCardnum(String sfz);
    /**
     * 拉白当前当前人员
     */
    public int updateEnd_dateByCardnum(String sfz);
    /**
     * 拉黑固定周期
     */
    public int updateBlackByCardnum(String sfz,String rule_punish);
    /**
     * 获取当前人员爽约次数
     */
    public int getCountByCardnum(String sfz,String fromdate);
    /**
     * 获取当前人员爽约次数(事项分类单独设置了可预约日期)
     */
    public int getCountByCardnum(String sfz,String fromdate,String tasktypeguid);
    /**
     * 获取有可预约事项的部门
     */
    public AuditCommonResult<List<Record>> getAppointOuByOuguid(String centerguid);
    /**
     * 根据部门获取有可预约事项
     */
    public AuditCommonResult<List<AuditZnsbCentertask>> getAppointTaskByOuguid(String ouguid, String index, String size);
}

package com.epoint.auditqueue.auditqueuerest.appointment.impl;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;

/**
 * 设备维护表对应的后台service
 * 
 * @author zhaoy
 * 
 */
public class QueueAppointmentService extends AuditCommonService
{

    /**
     * 
     */
    private static final long serialVersionUID = -689826604750277274L;
    
    //查找预约的规则设置
    public AuditQueueYuyuetime getYuyuetimeByTasktypeguid(String tasktypeguid){
        String sql = "SELECT rule_date,rule_count,rule_punish,ordernum,taskguid from audit_queue_yuyuetime "
         + "where (IFNULL(taskguid,'') = '' or taskguid=?) and IFNULL(rule_date,'')<>'' "
         + "and rule_count>0 ORDER BY ordernum desc LIMIT 0,1";
        return commonDao.find(sql, AuditQueueYuyuetime.class,tasktypeguid);
    }
    
    //查找当前人员状态
    public AuditQueueUserinfo getUserinfoByCardnum(String sfz){
        String sql = "SELECT blacklisttodate,is_foreverclose,end_date from audit_queue_userinfo "
                + "where IDENTITYCARDNUM = ? LIMIT 0,1";
        return commonDao.find(sql, AuditQueueUserinfo.class, sfz);
    }
    
    //拉白当前当前人员
    public int updateEnd_dateByCardnum(String sfz){
        String sql = "UPDATE audit_queue_userinfo set end_date = blacklisttodate, blacklisttodate = null,"
                + " OperateDate= NOW() where IDENTITYCARDNUM = ?";
        return commonDao.execute(sql, sfz);
    }
    //拉黑固定周期
    public int updateBlackByCardnum(String sfz,String rule_punish){
        String sql = "UPDATE audit_queue_userinfo set blacklisttodate = date_add(NOW(),interval "
                + rule_punish+"), OperateDate= NOW() where IDENTITYCARDNUM = ?";
        return commonDao.execute(sql, sfz);
    }
    //获取当前人员爽约次数
    public int getCountByCardnum(String sfz,String fromdate){
        String sql = "select COUNT(1) from audit_queue_appointment where IDENTITYCARDNUM = ? and `STATUS`=0"
                + " and APPOINTMENTFROMTIME > ?";
        return commonDao.queryInt(sql, sfz, fromdate);
    }
    //获取当前人员爽约次数(事项分类单独设置了可预约日期)
    public int getCountByCardnum(String sfz,String fromdate,String taskguid){
        String sql = "select COUNT(1) from audit_queue_appointment where IDENTITYCARDNUM = ? and `STATUS`=0"
                + " and APPOINTMENTFROMTIME > ?1 and apptaskguid = ?2";
        return commonDao.queryInt(sql, sfz, fromdate);
    }
    //获取可预约部门
    public List<Record> getAppointOuByCenterguid(String centerguid){
        String sql = "select DISTINCT OUGUID,OUNAME from Audit_Znsb_CenterTask where RESERVATIONMANAGEMENT=1 and centerguid = ?";
        return commonDao.findList(sql, Record.class, centerguid);
    }
    //获取可预约事项
    public List<AuditZnsbCentertask> getAppointTaskByOuguid(String ouguid,int first,int end){
        String sql = "select TaskGuid,taskname,OUNAME,ordernum,Applyertype from Audit_Znsb_CenterTask "
                + "where RESERVATIONMANAGEMENT=1 and OUGUID = ?1 ORDER BY ordernum DESC LIMIT ?2,?3";
        return commonDao.findList(sql, AuditZnsbCentertask.class, ouguid, first, end);
    }
}

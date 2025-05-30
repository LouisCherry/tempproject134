package com.epoint.xmz.sqsb.impl;




import java.util.List;

import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.fee.domain.AuditTaskChargeItem;
import com.epoint.basic.audittask.fee.inter.IAuditTaskChargeItem;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;

/**
 * 双全双百实现类
 * @description
 * @author lyunang
 * @date  2020年8月12日 下午3:03:32
 */
public class SqsbService
{
    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoTo;
    
    public SqsbService() {
        commonDaoTo = CommonDao.getInstance();
    }
    /**
     * API:根据国脉前置库获取的主题id和主键查询主题配置表
     */
    public AuditSpBusiness getAuditSpBusinessByOriginid(String originid) {
        String sql = "select * from audit_sp_business where originid = ?1 order by version desc limit 1";
        return commonDaoTo.find(sql, AuditSpBusiness.class, originid);
    }
    public List<AuditSpBasetaskR> getAuditSpBasetaskrByBaseTaskGuid(String basetaskguid) {
        String sql = "select taskid,taskname,areacode from audit_sp_basetask_r where basetaskguid =?1 ORDER BY FIELD(areacode,'370800000000','370802000000','370811000000','370890000000','370882000000','370883000000','370821000000','370823000000','370891000000','370809000000','370826000000')";
        return commonDaoTo.findList(sql, AuditSpBasetaskR.class, basetaskguid);
    }
    public AuditTask getAuditTaskByTaskId(String taskid) {
        String sql = "select a.rowguid,a.taskname,a.areacode,a.task_id from audit_task a where  ifnull(a.is_history,'0')='0' and a.is_enable = 1 and a.IS_EDITAFTERIMPORT = 1 and a.task_id = ?1 and a.by_law <> 'sqsb'";
        return commonDaoTo.find(sql, AuditTask.class, taskid);
    }
    public List<AuditTask> getAuditTaskListByTaskName(String taskname) {
        String sql = "select a.rowguid,a.taskname,a.areacode from audit_task a where  ifnull(a.is_history,'0')='0' and a.is_enable = 1 and a.IS_EDITAFTERIMPORT = 1 and a.taskname = ?1 and a.by_law <> 'sqsb'";
        return commonDaoTo.findList(sql, AuditTask.class, taskname);
    }
    
    public List<AuditSpBasetaskR> getTaskNameBySqsb(String issqlb,String areacode) {
        String sql = "select c.* from AUDIT_SP_BUSINESS a join AUDIT_SP_TASK b on a.rowguid = b.BUSINESSGUID  join audit_sp_basetask_r c on b.BaseTaskGuid = c.BaseTaskGuid where a.issqlb = ? and c.areacode = ? and a.businesstype = '5' order by a.ordernumber asc limit 6";
        return commonDaoTo.findList(sql, AuditSpBasetaskR.class,issqlb,areacode);
    }

}

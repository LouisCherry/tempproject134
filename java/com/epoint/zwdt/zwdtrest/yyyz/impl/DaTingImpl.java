package com.epoint.zwdt.zwdtrest.yyyz.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zwdt.zwdtrest.yyyz.api.IDaTing;

@Service
@Component
public class DaTingImpl implements IDaTing
{

    @Override
    public AuditCommonResult<List<Record>> getsixquhaoWaitCountToday(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(service.getsixquhaoWaitCountToday(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    @Override
    public AuditCommonResult<Record> getTodayQhAndWaitCount(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        try {
            result.setResult(service.getTodayQhAndWaitCount(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getMaxCountTasktype(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Record> result = new AuditCommonResult<>();
        try {
            result.setResult(service.getMaxCountTasktype(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getMaxPeople(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Record> result = new AuditCommonResult<>();
        try {
            result.setResult(service.getMaxPeople(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getMaxTime(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Record> result = new AuditCommonResult<>();
        try {
            result.setResult(service.getMaxTime(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getMaxweek(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Record> result = new AuditCommonResult<>();
        try {
            result.setResult(service.getMaxweek(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getsixhall(String centerguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(service.getsixhall(centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getsixquhao(String hallguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(service.getsixquhao(hallguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getsixwait(String hallguid) {
        DaTingService service = new DaTingService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(service.getsixwait(hallguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    /**
     *  [根据办件的申办流水号获取办件信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public AuditProject getAuditProjectByFlowsn(String flowsn) {
        return new DaTingService().getAuditProjectByFlowsn(flowsn);
    }
    /**
     *  [根据办件的主题标识获取BusinessbaseInfo] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getBusinessLicenseBaseInfoByBiGuid(String biguid) {
        return new DaTingService().getBusinessLicenseBaseInfoByBiGuid(biguid);
    }
    
    /**
     *  [根据BusinessbaseInfo的标识获取Extension的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getBusinessLicenseExtensionByBaseInfoGuid(String baseInfoGuid) {
        return new DaTingService().getBusinessLicenseExtensionByBaseInfoGuid(baseInfoGuid);
    }
    /**
     *  [根据taskid获取泰安事项信息拓展表audit_task_taian的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getAuditTaskTaianByTaskId(String taskId) {
        return new DaTingService().getAuditTaskTaianByTaskId(taskId);
    }
    /**
     * 
     *  [根据主题实例标识和事项标识查询专项材料和通用材料] 
     *  @param businessGuid
     *  @param taskId
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getAuditSpIYyyzMaterialByBusinessGuidAndTaskId(String biGuid, String taskId) {
        return new DaTingService().getAuditSpIYyyzMaterialByBusinessGuidAndTaskId(biGuid,taskId);
    }
    /**
     * 根据上传附件cliengguid获取附件表数据
     *  [一句话功能简述] 
     *  @param cliengguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getFrameAttachInfoByCliengguid(String cliengguid) {
        return new DaTingService().getFrameAttachInfoByCliengguid(cliengguid);
    }
    /**
     * 根据ouguid获取部门信息
     *  [一句话功能简述] 
     *  @param ouGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getFramOuByOuGuid(String ouGuid) {
        return new DaTingService().getFramOuByOuGuid(ouGuid);
    }
    @Override
    public AuditOnlineProject getAuditOnlineProjectByProjectguid(String projectguid) {
        return new DaTingService().getAuditOnlineProjectByProjectguid(projectguid);
    }
    @Override
    public void updateAuditProjectByProjectguid(String projectguid,int status,int banjieStatus) {
        new DaTingService().updateAuditProjectByProjectguid(projectguid,status,banjieStatus);
        
    }
    @Override
    public void updateAuditOnlineProjectByProjectguid(String projectguid,int status) {
        new DaTingService().updateAuditOnlineProjectByProjectguid(projectguid,status);
        
    }
    
}

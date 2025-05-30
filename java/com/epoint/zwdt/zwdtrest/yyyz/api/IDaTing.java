package com.epoint.zwdt.zwdtrest.yyyz.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

@Service
public interface IDaTing
{
    /**
     * 六大领域今天的取号量 和等待量
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param centerguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<List<Record>> getsixquhaoWaitCountToday (String centerguid);
    /**
     *  今日总取号业务量
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param centerguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<Record> getTodayQhAndWaitCount(String centerguid);
    /**
     *  获取累计取号量最多的业务(事项分类) 名称+取号数 展示的时候获取姓名即可
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<Record> getMaxCountTasktype(String centerguid);
    /**
     *  六大领域本月的取号量
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param centerguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    /**
     * 最具人气的领域
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param centerguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<Record> getMaxPeople(String centerguid);
    /**
     *  最繁忙的时间段
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<Record> getMaxTime(String centerguid);
     /**
      * 最繁忙的星期
      *  [一句话功能简述]
      *  [功能详细描述]
      *  @return    
      * @exception/throws [违例类型] [违例说明]
      * @see [类、类#方法、类#成员]
      */
    public AuditCommonResult<Record> getMaxweek(String centerguid);
    
    public AuditCommonResult<List<Record>> getsixhall(String centerguid);
    //2
    public AuditCommonResult<Integer> getsixquhao(String hallguid);
    //3
    public AuditCommonResult<Integer> getsixwait(String hallguid);
    /**
     *  [根据办件的申办流水号获取办件信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditProject getAuditProjectByFlowsn(String flowsn);
    
    /**
     *  [根据办件的主题标识获取BusinessbaseInfo] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getBusinessLicenseBaseInfoByBiGuid(String biguid);
    /**
     *  [根据BusinessbaseInfo的标识获取Extension的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getBusinessLicenseExtensionByBaseInfoGuid(String baseInfoGuid);
    /**
     *  [根据taskid获取泰安事项信息拓展表audit_task_taian的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getAuditTaskTaianByTaskId(String taskId);
    /**
     * 
     *  [根据主题实例标识和事项标识查询专项材料和通用材料] 
     *  @param businessGuid
     *  @param taskId
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getAuditSpIYyyzMaterialByBusinessGuidAndTaskId(String biGuid, String taskId);
    /**
     * 根据上传附件cliengguid获取附件表数据
     *  [一句话功能简述] 
     *  @param cliengguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getFrameAttachInfoByCliengguid(String cliengguid);
    /**
     * 根据ouguid获取部门信息
     *  [一句话功能简述] 
     *  @param ouGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getFramOuByOuGuid(String ouGuid);
    
    /**
     *  [根据办件唯一标识获取网办件信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditOnlineProject getAuditOnlineProjectByProjectguid(String projectguid);
    
    /**
     *  [根据办件的唯一标识修改办件状态] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateAuditProjectByProjectguid(String projectguid,int status,int banjieStatus);
    
    /**
     *  [根据办件唯一标识修改网办件表办件状态] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateAuditOnlineProjectByProjectguid(String projectguid,int status);
}

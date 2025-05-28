package com.epoint.xmz.api;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 网厅通用service接口
 * 
 * @author LYA
 * @version [版本号, 2022-02-16 15:36:48]
 */
public interface IZwdtCommonService extends Serializable
{ 
   
	 /**
	  * 
	  *  [根据接口查询个人或法人事项] 
	  *  @param taskname
	  *  @param areacode
	  *  @param type
	  *  @return    
	  * @exception/throws [违例类型] [违例说明]
	  * @see [类、类#方法、类#成员]
	  */
    public List<AuditTask> findTaskList(String taskname, String areacode, int type, int first,int pageSize);

    /**
     * 
     *  [根据辖区获取可办事项的部门] 
     *  @param areacode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> findOuList(String areacode);

    /**
     * 
     *  [根据部门guid获取部门可办理的事项] 
     *  @param taskname
     *  @param ouguid
     *  @param first
     *  @param pageSize
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> findOuTaskList(String taskname, String ouguid, int first, int pageSize);

    /**
     * 
     *  [根据事项task_id获取叫号平板事项分类表] 
     *  @param task_id
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record findQueueTasktypeByTaskid(String task_id);
    /**
     * 
     *  [根据接口查询个人或法人事项数量] 
     *  @param taskname
     *  @param areacode
     *  @param type
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int findTaskListCount(String taskname, String areacode, int type);

    /**
     * 
     *  [根据接口查询部门事项数量] 
     *  @param taskname
     *  @param areacode
     *  @param type
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int findOuTaskListCount(String taskname, String ouguid);

    /**
     * 
     *  [查询当天业务办理取号信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getQueueList();

    /**
     * 
     *  [获取本周行政征收或行政确认类别事项的办件信息列表] 
     *  @param mondayDate
     *  @param sundayDate
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getXzzsProjectList(String mondayDate, String sundayDate, String type);
    
    /**
     * 
     *  [根据统一社会信用代码获取项目信息] 
     *  @param creditcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditRsItemBaseinfo> getItemInfoByCreditcode(String creditcode);
    
    /**
     * 
     *  [根据业务guid和标准事项guid查询是否有办件] 
     *  @param creditcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getProjectNumByYewuguidAndBasetaskGuid(String yewuguid,String basetaskguid);
    
    /**
     * 
     *  [根据业务guid和标准事项guid查询是否有办件] 
     *  @param creditcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public List<AuditTask> getAuditTaskByTaskids(String taskids);

    /**
     * 
     *  [根据分类标识，获取特定数量的数据] 
     *  @param dictid
     * @param pageSize 
     * @param first 
     * @param keyword 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskByDictid(String dictid, int first, int pageSize, String keyword);

    /**
     * 
     *  [根据分类标识查询总共可用事项数量] 
     *  @param dictid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getAuditTaskCountByDictid(String dictid,String keyword);

    /**
     * 
     *  [所选统一社会信用代码和选择的证照获取其可用的最新的发证的证照列表] 
     *  @param creditCode
     *  @param certCatalogid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CertInfo getLeastCertInfoByCreditCodeAndCertCatalogid(String creditCode, String certCatalogid);

    /**
     * 
     *  [通过cliengguid获取最新的附件信息] 
     *  @param wordCliengguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    
    public FrameAttachInfo getLeastFrameAttachInfoByClinegguid(String wordCliengguid);

    /**
     * 
     *  [根据办件唯一标识更新受理通知书附件attachguid] 
     *  @param attachguid
     *  @param projectguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateShouLiguidByProjectguid(String attachguid,String projectguid);
    
    /**
     * 
     * 根据条件获取数量
     * 
     *  @param groupfield 
     *      分组字段
     *  @param recordnum  
     *      查询记录的数量
     *  @param conditionmap  
     *      条件集合
     *  @return  办件数量和事项guid
     */
    public AuditCommonResult<List<Record>> getProjectnumGroupByFiled(String groupfield,Integer recordnum,Map<String,String> conditionmap);

    /**
     * 
     *  [根据考勤年和考勤月获取钉钉考勤人员集合] 
     *  @param kaoqinYear
     *  @param month
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> selectKaoQinInfo(String kaoqinYear, String month);

    /**
     * 
     *  [修改考勤人员考前年和考勤月的预警状态] 
     *  @param str    
     * @param kaoqinMonth 
     * @param kaoqinYear 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateKfyjStatus(String str, String kaoqinYear, String kaoqinMonth);

    /**
     * 
     *  [根据辖区编码获取有办件的即办件事项信息] 
     *  @param areacode
     * @param pageSize 
     * @param first 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getJbjTaskInfo(String areacode, int first, int pageSize);

    /**
     * 
     *  [根据task_id获取再用版本事项信息] 
     *  @param task_id
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditTask getTaskInfo(String task_id);
    /**
     * 
     *  [根据辖区编码获取有办件的即办件事项信息数量] 
     *  @param areacode
     * @param pageSize 
     * @param first 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getJbjTaskInfoNum(String areacode);

    /**
     * 
     *  [根据考核规则和考核细则标识以及人员userguid获取考核详情表信息] 
     *  @param recordrowguid
     *  @param recordrulerowguid
     *  @param str
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditPerformanceDetail> findDeailList(String recordrowguid, String recordrulerowguid, String str);
    
    /**
     *  [根据taskid获取泰安事项信息拓展表audit_task_taian的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getAuditTaskTaianByTaskId(String taskId);

    /**
     * 
     *  [根据证照类别和产品类别查询证照数据] 
     *  @param certcatalogid
     *  @param cplb
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CertInfo getCertinfoByCertCatalogidAndcplb(String certcatalogid, String cplb);
    
    
}

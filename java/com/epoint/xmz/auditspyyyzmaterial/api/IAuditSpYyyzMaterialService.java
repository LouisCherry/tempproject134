package com.epoint.xmz.auditspyyyzmaterial.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

/**
 * 一业一证专项材料表对应的后台service接口
 * 
 * @author LYA
 * @version [版本号, 2020-06-18 21:34:52]
 */
public interface IAuditSpYyyzMaterialService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpYyyzMaterial record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpYyyzMaterial record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditSpYyyzMaterial find(Object primaryKey);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpYyyzMaterial find(String sql,Object... args);

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpYyyzMaterial> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpYyyzMaterial> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countAuditSpYyyzMaterial(String sql, Object... args);
	 /**
	  * 
	  *  [根据条件查询主题下的专项材料] 
	  *  @param array
	  *  @return    
	  * @exception/throws [违例类型] [违例说明]
	  * @see [类、类#方法、类#成员]
	  */
    public int findCountByCondition(String sql,Object[] array);

    /**
     * 
     *  [根据主题标识获取主题配置下的所有事项] 
     *  @param businessGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> findListBybusinessGuid(String businessGuid);
    
    /**
     * 
     *  [根据主题标识获取主题配置下的所有事项] 
     *  @param businessGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> getYyyzMaterialByTaskId(String taskid);
    
    /**
     * 
     *  [通过biguid获取其主题事项实例表中绑定办件编号的实例] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpITask> getSpITaskByBIGuid(String biGuid);

    /**
     * 根据主题实例唯一标识和taskguid获取办件信息
     *  [一句话功能简述] 
     *  @param biGuid
     *  @param taskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditProject> getProjectListByBiGuidAndTaskguids(String biGuid, String taskguid);

    /**
     * 根据办件实例标识更新办件流水号
     *  [一句话功能简述] 
     *  @param biGuid
     *  @param taskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateFlowsnByProjectguid(String newFlowsn, String projectGuid);

    /**
     * 根据所选情形，获取情形所对应的材料
     *  [一句话功能简述] 
     *  @param ids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> getYyyzMaterialListBySelectIds(String ids);

    /**
     * 根据情形决定的材料获取该材料下获取的事项信息
     *  [一句话功能简述] 
     *  @param claimGuidIds
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskListByClaimGuidIds(String claimGuidIds);
    
    /**
     * 根据事项唯一标识和辖区编码获取
     *  [一句话功能简述] 
     *  @param task_id
     *  @param areaCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditSpBasetaskR getAuditSpBasetaskRBytaskidAndareaCode(String task_id, String areaCode);

    /**
     * 
     *  [通过获取的一链办理主题材料id查找事项库的事项] 
     *  @param materialId
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditSpYyyzMaterial getYyyzMaterialByMaterialId(String materialId);
    
    /**
     * 
     *  [根据事项实例标识获取该泰安事项表事项的信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getTaianInfoByTaskId(String taskid);
    
    /**
     * 
     *  [根据事项名称查询获取可用事项] 
     *  @param taskName
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getTaskListByTaskName(String taskName);

    /**
     * 
     *  [根据主题实例的材料rowguid去一业一证材料表中获取材料信息] 
     *  @param materialGuids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpYyyzMaterial> getYyyzMaterialByRowguids(String materialGuids);

    /**
     * 
     *  [根据选择的taskid从事项表中获取最高版本再用事项] 
     *  @param taskids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskListByTaskIds(String taskids);
    
    /**
     * 
     *  [根据主题实例的材料rowguid去一业一证材料表中获取材料信息] 
     *  @param materialGuids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditSpYyyzMaterial getSpYyyzMaterialByMaterialId(String materialid);

    /**
     * 
     *  [根据得到的国脉材料id获取主题材料云表单关联表的相关信息] 
     *  @param materialids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getItemValuesByIds(String mIds, String itmeIds);

}

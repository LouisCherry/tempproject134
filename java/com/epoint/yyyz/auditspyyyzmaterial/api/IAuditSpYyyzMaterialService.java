package com.epoint.yyyz.auditspyyyzmaterial.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

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
     * 
     *  [获取含有通用事项的部门信息] 
     *  @param condition
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> selectAuditTaskOuByObjectGuids(String ouguid,String areacode);
    
    
    /**
     * 
     *  [获取配置了主题通用版本的事项] 
     *  @param condition
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getAuditTaskByCommonTask(String condition);
    
    /**
     * 
     *  [获取含有通用事项的部门信息] 
     *  @param condition
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<FrameOu> getCommonFrameOuByOuguids(String ouguids);
    
    /**
     * 
     *  [获取含有通用事项的部门信息] 
     *  @param condition
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> selectAuditTaskOuByObjectGuid(String ouguid,String areacode);
    
    public List<AuditSpYyyzMaterial> getYyyzMaterialByRowguids(String materialGuids);
    
    
}

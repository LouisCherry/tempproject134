package com.epoint.yyyz.auditspiyyyzmaterial.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.yyyz.auditspiyyyzmaterial.api.entity.AuditSpIYyyzMaterial;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

/**
 * 一业一证主题材料实例表对应的后台service接口
 * 
 * @author LYA
 * @version [版本号, 2020-06-20 19:20:32]
 */
public interface IAuditSpIYyyzMaterialService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpIYyyzMaterial record);

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
    public int update(AuditSpIYyyzMaterial record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditSpIYyyzMaterial find(Object primaryKey);

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
    public AuditSpIYyyzMaterial find(String sql,Object... args);

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
    public List<AuditSpIYyyzMaterial> findList(String sql, Object... args);

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
    public List<AuditSpIYyyzMaterial> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countAuditSpIYyyzMaterial(String sql, Object... args);
	 
	 /**
	  * 
	  *  [新增申报时初始化一业一证主题材料实例表] 
	  *  @param subappGuid
	  *  @param businessGuid
	  *  @param biGuid
	  *  @param phaseGuid
	  *  @param auditSpYyyzMaterialList    
	  * @exception/throws [违例类型] [违例说明]
	  * @see [类、类#方法、类#成员]
	  */
    public List<AuditSpIYyyzMaterial> initTcSubappMaterial(String subappGuid, String businessGuid, String biGuid, String phaseGuid,
            List<AuditSpYyyzMaterial> auditSpYyyzMaterialList);

    /**
     * 
     *  [根据子申报唯一标识查询一业一证主题材料实例集合] 
     *  @param subappGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpIYyyzMaterial> getSpIMaterialBySubappGuid(String subappGuid);
    
    /**
     * 
     *  [根据勾选事项的task_id获取通用材料和个性化材料] 
     *  @param businessGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpIYyyzMaterial> getYyyzMaterialByTaskId(String biGuid,String taskid);

    /**
     * 
     *  [根据一业一证主题材料实例标识，修改提交状态] 
     *  @param materialInstanceGuid
     *  @param i    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateMaterialStatus(String materialInstanceGuid, int i);

    /**
     * 
     *  [根据主题实例唯一标识标识查询一业一证主题材料实例集合] 
     *  @param subappGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpIYyyzMaterial> getSpIMaterialBybiGUid(String biGuid);
    
    public List<AuditSpIYyyzMaterial> findSpIListByBiGuidAndtaskId(String biGuid, String taskid);
    
    public List<Record> findYlblFormListByBiGuidAndMaterialId(String biGuid, String materialGuid);
}

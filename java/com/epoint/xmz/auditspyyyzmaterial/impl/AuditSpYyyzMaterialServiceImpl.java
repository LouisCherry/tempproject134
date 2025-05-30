package com.epoint.xmz.auditspyyyzmaterial.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.xmz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;
/**
 * 一业一证专项材料表对应的后台service实现类
 * 
 * @author LYA
 * @version [版本号, 2020-06-18 21:34:52]
 */
@Component
@Service
public class AuditSpYyyzMaterialServiceImpl implements IAuditSpYyyzMaterialService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpYyyzMaterial record) {
        return new AuditSpYyyzMaterialService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditSpYyyzMaterialService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpYyyzMaterial record) {
        return new AuditSpYyyzMaterialService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditSpYyyzMaterial find(Object primaryKey) {
       return new AuditSpYyyzMaterialService().find(primaryKey);
    }

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
    public AuditSpYyyzMaterial find(String sql, Object... args) {
        return new AuditSpYyyzMaterialService().find(sql,args);
    }

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
    public List<AuditSpYyyzMaterial> findList(String sql, Object... args) {
       return new AuditSpYyyzMaterialService().findList(sql,args);
    }

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
    public List<AuditSpYyyzMaterial> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditSpYyyzMaterialService().findList(sql,pageNumber,pageSize,args);
    }
    
     /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
     @Override
    public Integer countAuditSpYyyzMaterial(String sql, Object... args){
        return new AuditSpYyyzMaterialService().countAuditSpYyyzMaterial(sql, args);
    }

     /**
      * 
      *  [根据条件查询主题下的专项材料] 
      *  @param array
      *  @return    
      * @exception/throws [违例类型] [违例说明]
      * @see [类、类#方法、类#成员]
      */
    @Override
    public int findCountByCondition(String sql,Object[] array) {
        return new AuditSpYyyzMaterialService().findCountByCondition(sql,array);
    }

    /**
     * 
     *  [根据主题标识获取主题配置下的所有事项] 
     *  @param businessGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditSpYyyzMaterial> findListBybusinessGuid(String businessGuid) {
        return new AuditSpYyyzMaterialService().findListBybusinessGuid(businessGuid);
    }
    /**
     * 
     *  [根据勾选专项事项的taskid获取事项材料信息集合，没有只显示通用事项] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @Override
    public List<AuditSpYyyzMaterial> getYyyzMaterialByTaskId(String taskid) {
        return new AuditSpYyyzMaterialService().getYyyzMaterialByTaskId(taskid);
    }

    /**
     * 
     *  [通过biguid获取其主题事项实例表中绑定办件编号的实例] 
     *  @param biGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditSpITask> getSpITaskByBIGuid(String biGuid) {
        return new AuditSpYyyzMaterialService().getSpITaskByBIGuid(biGuid);
    }

    /**
     * 根据主题实例唯一标识和taskguid获取办件信息
     *  [一句话功能简述] 
     *  @param biGuid
     *  @param taskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditProject> getProjectListByBiGuidAndTaskguids(String biGuid, String taskguid) {
        return new AuditSpYyyzMaterialService().getProjectListByBiGuidAndTaskguids(biGuid,taskguid);
    }

    @Override
    public void updateFlowsnByProjectguid(String newFlowsn, String projectGuid) {
         new AuditSpYyyzMaterialService().updateFlowsnByProjectguid(newFlowsn,projectGuid);
    }

    /**
     * 根据所选情形，获取情形所对应的材料
     *  [一句话功能简述] 
     *  @param selectIds
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditSpYyyzMaterial> getYyyzMaterialListBySelectIds(String ids) {
        return new AuditSpYyyzMaterialService().getYyyzMaterialListBySelectIds(ids);
    }

    /**
     * 根据情形决定的材料获取该材料下获取的事项信息
     *  [一句话功能简述] 
     *  @param claimGuidIds
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditTask> getAuditTaskListByClaimGuidIds(String claimGuidIds) {
        return new AuditSpYyyzMaterialService().getAuditTaskListByClaimGuidIds(claimGuidIds);
    }

    @Override
    public AuditSpYyyzMaterial getYyyzMaterialByMaterialId(String materialId) {
        return new AuditSpYyyzMaterialService().getYyyzMaterialByMaterialId(materialId);
    }
    
    /**
     * 
     *  [根据事项实例标识获取该泰安事项表事项的信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getTaianInfoByTaskId(String taskid) {
        return new AuditSpYyyzMaterialService().getTaianInfoByTaskId(taskid);
    }
    
    /**
     * 
     *  [根据事项名称查询获取可用事项集合] 
     *  @param taskName
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditTask> getTaskListByTaskName(String taskName) {
        return new AuditSpYyyzMaterialService().getTaskListByTaskName(taskName);
    }

    @Override
    public AuditSpBasetaskR getAuditSpBasetaskRBytaskidAndareaCode(String task_id, String areaCode) {
        return new AuditSpYyyzMaterialService().getAuditSpBasetaskRBytaskidAndareaCode(task_id,areaCode);
    }
    @Override
    public List<AuditSpYyyzMaterial> getYyyzMaterialByRowguids(String materialGuids) {
        return new AuditSpYyyzMaterialService().getYyyzMaterialByRowguids(materialGuids);
    }

    @Override
    public List<AuditTask> getAuditTaskListByTaskIds(String taskids) {
        return new AuditSpYyyzMaterialService().getAuditTaskListByTaskIds(taskids);
    }

    @Override
    public AuditSpYyyzMaterial getSpYyyzMaterialByMaterialId(String materialid) {
        return new AuditSpYyyzMaterialService().getSpYyyzMaterialByMaterialId(materialid);
    }

    @Override
    public List<Record> getItemValuesByIds(String mIds, String itmeIds) {
        return new AuditSpYyyzMaterialService().getItemValuesByIds(mIds,itmeIds);
    }
}

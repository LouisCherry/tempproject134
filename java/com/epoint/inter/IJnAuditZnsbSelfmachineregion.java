package com.epoint.inter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.domain.JnAuditZnsbSelfmachineregion;

/**
 * 智能化一体机区域配置对应的后台service接口
 * 
 * @author 54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
public interface IJnAuditZnsbSelfmachineregion extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnAuditZnsbSelfmachineregion record);

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
    public int update(JnAuditZnsbSelfmachineregion record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public JnAuditZnsbSelfmachineregion find(Object primaryKey);

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
    public JnAuditZnsbSelfmachineregion find(String sql,Object... args);

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
    public List<JnAuditZnsbSelfmachineregion> findList(String sql, Object... args);

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
    public List<JnAuditZnsbSelfmachineregion> findList(String sql, int pageNumber, int pageSize,Object... args);
    
    /**
     * 获取列表
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<PageData<JnAuditZnsbSelfmachineregion>> getRegionByPage(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);
    
    /**
     * 获取列表
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getRegionList();
    
    /**
     * 根据层级获取区域list
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getRegionListByLevel(String level);
    
    /**
     * 根据层级与父级guid获取区域list
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getRegionListByLevelAndParent(String level,String parentguid);
    
    /**
     * 获取父级区域list
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getParentRegionList();
    
    /**
     * 获取使用中的父级区域list
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getParentRegionListInuse();
    
    /**
     * 根据父级guid获取子级区域list
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getChildRegionListByParentguid(String parentguid);
    
    /**
     * 根据父级guid获取使用中的子级区域list
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getChildRegionListInuseByParentguid(String parentguid);
    
    /**
     * 
     *  根据rowguid查询对象
     *  @param rowguid
     *  @return
     */
    public AuditCommonResult<JnAuditZnsbSelfmachineregion> getRegionByRowguid(String rowguid);
    
    /**
     * 
     *  获取所有启用的通用区域配置列表
     *  @param rowguid
     *  @return
     */
    public AuditCommonResult<List<JnAuditZnsbSelfmachineregion>> getAllCommonRegionList();
    
    /**
     * 
     *  删除旧的地区数据
     *  @param rowguid
     *  @return
     */
    public AuditCommonResult<String> deleteOldRegion();
    
    /**
     * 
     *  将待更新的区域配置转正
     *  @param rowguid
     *  @return
     */
    public AuditCommonResult<String> updateNewRegion();
    
    /**
     * 
     *  [根据区域编码获取信息] 
     *  @param code
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JnAuditZnsbSelfmachineregion getRegionByCode(String areacode);
    
    /*
     * 获取可用地点列表
     */
    public List<JnAuditZnsbSelfmachineregion> getAllUsedPlaceList(int currentpage, int pagesize);

    public int getAllUsedPlaceListNum();
}

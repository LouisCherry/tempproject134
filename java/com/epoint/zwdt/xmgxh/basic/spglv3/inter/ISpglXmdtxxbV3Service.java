package com.epoint.zwdt.xmgxh.basic.spglv3.inter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmdtxxbV3;


/**
 * 项目单体信息表对应的后台service接口
 * 
 * @author Epoint
 * @version [版本号, 2023-09-26 16:08:13]
 */
public interface ISpglXmdtxxbV3Service extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmdtxxbV3 record);

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
    public int update(SpglXmdtxxbV3 record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public SpglXmdtxxbV3 find(Object primaryKey);

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
    public SpglXmdtxxbV3 find(String sql, Object... args);

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
    public List<SpglXmdtxxbV3> findList(String sql, Object... args);

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
    public List<SpglXmdtxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countSpglXmdtxxbV3(String sql, Object... args);
    
    
    /**
     * 
     * 获取设置分页
     * 
     * @param conditionMap
     *            条件map
     * @param first
     *            第一条记录
     * @param pageSize
     *            页大小
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            排序顺序
     * @return
     */
    public AuditCommonResult<PageData<SpglXmdtxxbV3>> getAllByPage(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder);

    /**
     *  条件查询
     * @param conditionMap
     * @return
     */
    public AuditCommonResult<List<SpglXmdtxxbV3>> getListByCondition(Map<String, String> conditionMap);

}

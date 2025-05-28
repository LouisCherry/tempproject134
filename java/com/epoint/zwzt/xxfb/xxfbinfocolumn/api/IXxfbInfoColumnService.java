package com.epoint.zwzt.xxfb.xxfbinfocolumn.api;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;

import java.util.List;
import java.util.Map;

/**
 * 信息栏目表对应的后台service接口
 *
 * @author D0Be
 * @version [版本号, 2022-04-25 13:57:31]
 */
public interface IXxfbInfoColumnService
{
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XxfbInfoColumn record);

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XxfbInfoColumn record);

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    public Integer countXxfbInfoColumn(Map<String, Object> conditionMap);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public XxfbInfoColumn find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public XxfbInfoColumn find(Map<String, Object> conditionMap);

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    public List<XxfbInfoColumn> findList(Map<String, Object> conditionMap);

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<XxfbInfoColumn> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize);

}

package com.epoint.zwzt.xxfb.xxfbinfocolumn.impl;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.IXxfbInfoColumnService;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 信息栏目表对应的后台service实现类
 *
 * @author D0Be
 * @version [版本号, 2022-04-25 13:57:31]
 */
@Component
public class XxfbInfoColumnServiceImpl implements IXxfbInfoColumnService
{
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(XxfbInfoColumn record) {
        return new XxfbInfoColumnService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new XxfbInfoColumnService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(XxfbInfoColumn record) {
        return new XxfbInfoColumnService().update(record);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    @Override
    public Integer countXxfbInfoColumn(Map<String, Object> conditionMap) {
        return new XxfbInfoColumnService().countXxfbInfoColumn(conditionMap);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public XxfbInfoColumn find(Object primaryKey) {
        return new XxfbInfoColumnService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public XxfbInfoColumn find(Map<String, Object> conditionMap) {
        return new XxfbInfoColumnService().find(conditionMap);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    @Override
    public List<XxfbInfoColumn> findList(Map<String, Object> conditionMap) {
        return new XxfbInfoColumnService().findList(conditionMap);
    }

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    @Override
    public PageData<XxfbInfoColumn> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        return new XxfbInfoColumnService().paginatorList(conditionMap, pageNumber, pageSize);
    }

}

package com.epoint.zwzt.xxfb.xxfbinfocontent.impl;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.IXxfbInfoContentService;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.entity.XxfbInfoContent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 信息正文内容表对应的后台service实现类
 *
 * @author D0Be
 * @version [版本号, 2022-04-28 14:01:44]
 */
@Component
public class XxfbInfoContentServiceImpl implements IXxfbInfoContentService
{
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(XxfbInfoContent record) {
        return new XxfbInfoContentService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new XxfbInfoContentService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(XxfbInfoContent record) {
        return new XxfbInfoContentService().update(record);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    @Override
    public Integer countXxfbInfoContent(Map<String, Object> conditionMap) {
        return new XxfbInfoContentService().countXxfbInfoContent(conditionMap);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public XxfbInfoContent find(Object primaryKey) {
        return new XxfbInfoContentService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public XxfbInfoContent find(Map<String, Object> conditionMap) {
        return new XxfbInfoContentService().find(conditionMap);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    @Override
    public List<XxfbInfoContent> findList(Map<String, Object> conditionMap) {
        return new XxfbInfoContentService().findList(conditionMap);
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
    public PageData<XxfbInfoContent> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        return new XxfbInfoContentService().paginatorList(conditionMap, pageNumber, pageSize);
    }

}

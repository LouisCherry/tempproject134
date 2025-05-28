package com.epoint.zwzt.xxfb.xxfbinforelease.impl;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.IXxfbInfoReleaseService;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.entity.XxfbInfoRelease;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 信息发布表对应的后台service实现类
 *
 * @author D0Be
 * @version [版本号, 2022-04-27 15:34:21]
 */
@Component
public class XxfbInfoReleaseServiceImpl implements IXxfbInfoReleaseService
{
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(XxfbInfoRelease record) {
        return new XxfbInfoReleaseService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new XxfbInfoReleaseService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(XxfbInfoRelease record) {
        return new XxfbInfoReleaseService().update(record);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    @Override
    public Integer countXxfbInfoRelease(Map<String, Object> conditionMap) {
        return new XxfbInfoReleaseService().countXxfbInfoRelease(conditionMap);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public XxfbInfoRelease find(Object primaryKey) {
        return new XxfbInfoReleaseService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public XxfbInfoRelease find(Map<String, Object> conditionMap) {
        return new XxfbInfoReleaseService().find(conditionMap);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    @Override
    public List<XxfbInfoRelease> findList(Map<String, Object> conditionMap) {
        return new XxfbInfoReleaseService().findList(conditionMap);
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
    public PageData<XxfbInfoRelease> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        return new XxfbInfoReleaseService().paginatorList(conditionMap, pageNumber, pageSize);
    }

}

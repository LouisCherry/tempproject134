package com.epoint.majoritem.itemplan.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.majoritem.itemplan.api.IItemPlanService;
import com.epoint.majoritem.itemplan.api.entity.ItemPlan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 重点项目项目进度表对应的后台service实现类
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:05:52]
 */
@Component
@Service
public class ItemPlanServiceImpl implements IItemPlanService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ItemPlan record) {
        return new ItemPlanService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ItemPlanService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ItemPlan record) {
        return new ItemPlanService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public ItemPlan find(Object primaryKey) {
        return new ItemPlanService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public ItemPlan find(String sql, Object... args) {
        return new ItemPlanService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<ItemPlan> findList(String sql, Object... args) {
        return new ItemPlanService().findList(sql, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<ItemPlan> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ItemPlanService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countItemPlan(String sql, Object... args) {
        return new ItemPlanService().countItemPlan(sql, args);
    }

}

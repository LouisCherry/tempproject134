package com.epoint.xmz.hongxiaobangallocation.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.hongxiaobangallocation.api.IHongxiaobangAllocationService;
import com.epoint.xmz.hongxiaobangallocation.api.entity.HongxiaobangAllocation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 红小帮配置表对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2024-05-20 17:08:28]
 */
@Component
@Service
public class HongxiaobangAllocationServiceImpl implements IHongxiaobangAllocationService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(HongxiaobangAllocation record) {
        return new HongxiaobangAllocationService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new HongxiaobangAllocationService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(HongxiaobangAllocation record) {
        return new HongxiaobangAllocationService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public HongxiaobangAllocation find(Object primaryKey) {
        return new HongxiaobangAllocationService().find(primaryKey);
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
    public HongxiaobangAllocation find(String sql, Object... args) {
        return new HongxiaobangAllocationService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<HongxiaobangAllocation> findList(String sql, Object... args) {
        return new HongxiaobangAllocationService().findList(sql, args);
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
    public List<HongxiaobangAllocation> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new HongxiaobangAllocationService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countHongxiaobangAllocation(String sql, Object... args) {
        return new HongxiaobangAllocationService().countHongxiaobangAllocation(sql, args);
    }

    @Override
    public HongxiaobangAllocation getAllocation() {
        return new HongxiaobangAllocationService().getAllocation();
    }

}

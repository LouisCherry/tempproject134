package com.epoint.jiningzwfw.zntbtask.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.jiningzwfw.zntbtask.api.IZndbTaskService;
import com.epoint.jiningzwfw.zntbtask.api.entity.ZndbTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 智能导办办理事项表对应的后台service实现类
 *
 * @author 19273
 * @version [版本号, 2023-09-18 14:43:48]
 */
@Component
@Service
public class ZndbTaskServiceImpl implements IZndbTaskService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ZndbTask record) {
        return new ZndbTaskService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZndbTaskService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ZndbTask record) {
        return new ZndbTaskService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public ZndbTask find(Object primaryKey) {
        return new ZndbTaskService().find(primaryKey);
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
    public ZndbTask find(String sql, Object... args) {
        return new ZndbTaskService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<ZndbTask> findList(String sql, Object... args) {
        return new ZndbTaskService().findList(sql, args);
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
    public List<ZndbTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ZndbTaskService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countZndbTask(String sql, Object... args) {
        return new ZndbTaskService().countZndbTask(sql, args);
    }

    @Override
    public List<ZndbTask> findMainList() {
        return new ZndbTaskService().findMainList();
    }

    @Override
    public List<ZndbTask> findBltjList() {
        return new ZndbTaskService().findBltjList();
    }

}

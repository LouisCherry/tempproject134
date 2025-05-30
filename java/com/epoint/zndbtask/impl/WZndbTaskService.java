package com.epoint.zndbtask.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zndbtask.api.entity.WZndbTask;

import java.util.List;

/**
 * 智能导办办理事项表对应的后台service
 *
 * @author 19273
 * @version [版本号, 2023-09-18 14:43:48]
 */
public class WZndbTaskService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public WZndbTaskService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(WZndbTask record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(WZndbTask.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(WZndbTask record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public WZndbTask find(Object primaryKey) {
        return baseDao.find(WZndbTask.class, primaryKey);
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
    public WZndbTask find(String sql, Object... args) {
        return baseDao.find(sql, WZndbTask.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<WZndbTask> findList(String sql, Object... args) {
        return baseDao.findList(sql, WZndbTask.class, args);
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
    public List<WZndbTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, WZndbTask.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countZndbTask(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<WZndbTask> findMainList() {
        String sql = " select * from  zndb_task  ORDER BY phase ,CAST(sxindex AS UNSIGNED) ASC ";
        return baseDao.findList(sql,WZndbTask.class);
    }

    public List<WZndbTask> findBltjList() {
        String sql = "select * from  zndb_task where tasktype ='2' ";
        return baseDao.findList(sql,WZndbTask.class);
    }
}

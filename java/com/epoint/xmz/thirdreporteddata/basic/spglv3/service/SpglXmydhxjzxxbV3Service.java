package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmydhxjzxxbV3;

import java.util.List;

/**
 * 项目用地红线界址信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 15:08:00]
 */
public class SpglXmydhxjzxxbV3Service {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglXmydhxjzxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmydhxjzxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglXmydhxjzxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmydhxjzxxbV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmydhxjzxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglXmydhxjzxxbV3.class, primaryKey);
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
    public SpglXmydhxjzxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglXmydhxjzxxbV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmydhxjzxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglXmydhxjzxxbV3.class, args);
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
    public List<SpglXmydhxjzxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglXmydhxjzxxbV3.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglXmydhxjzxxbV3(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }
}

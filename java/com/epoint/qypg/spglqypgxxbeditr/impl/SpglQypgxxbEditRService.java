package com.epoint.qypg.spglqypgxxbeditr.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.qypg.spglqypgxxbeditr.api.entity.SpglQypgxxbEditR;

import java.util.List;
import java.util.Map;

/**
 * 区域评估信息关联表对应的后台service
 *
 * @author shaoyuhui
 * @version [版本号, 2023-11-10 14:17:59]
 */
public class SpglQypgxxbEditRService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglQypgxxbEditRService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgxxbEditR record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglQypgxxbEditR.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgxxbEditR record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglQypgxxbEditR find(Object primaryKey) {
        return baseDao.find(SpglQypgxxbEditR.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglQypgxxbEditR find(String sql, Object... args) {
        return baseDao.find(sql, SpglQypgxxbEditR.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgxxbEditR> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglQypgxxbEditR.class, args);
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
    public List<SpglQypgxxbEditR> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglQypgxxbEditR.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglQypgxxbEditR(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public SpglQypgxxbEditR getSpglQypgxxbEditR(Map<String, String> map) {
        String sql = "select * from spgl_qypgxxb_edit_r ";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        sql += sqlManageUtil.buildSql(map);
        return baseDao.find(sql, SpglQypgxxbEditR.class);
    }
}

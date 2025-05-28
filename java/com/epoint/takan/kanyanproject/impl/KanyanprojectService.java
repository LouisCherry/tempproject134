package com.epoint.takan.kanyanproject.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 勘验项目对应的后台service
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-19 21:25:37]
 */
public class KanyanprojectService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public KanyanprojectService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Kanyanproject record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(Kanyanproject.class, guid);
        return baseDao.delete(t);
    }

    public int deleteByMainGuid(String guid) {
        return baseDao.execute("delete from kanyanproject where kanyaguid=?",guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Kanyanproject record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public Kanyanproject find(Object primaryKey) {
        return baseDao.find(Kanyanproject.class, primaryKey);
    }

    public Kanyanproject find(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        String sql = new SqlHelper().getSqlComplete(Kanyanproject.class, conditionMap, params);
        return baseDao.find(sql, Kanyanproject.class, params.toArray());
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public Kanyanproject find(String sql,  Object... args) {
        return baseDao.find(sql, Kanyanproject.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<Kanyanproject> findList(String sql, Object... args) {
        return baseDao.findList(sql, Kanyanproject.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<Kanyanproject> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Kanyanproject.class, args);
    }

	/**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countKanyanproject(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
}

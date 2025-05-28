package com.epoint.zczwfw.evaluateprojecterr.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.zczwfw.evaluateprojecterr.api.entity.EvaluateProjectErr;
import com.epoint.zczwfw.util.EnhancedSqlConditionUtil;

/**
 * 评价办件异常信息表对应的后台service
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:21:39]
 */
public class EvaluateProjectErrService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public EvaluateProjectErrService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EvaluateProjectErr record) {
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
        T t = baseDao.find(EvaluateProjectErr.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(EvaluateProjectErr record) {
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
    public EvaluateProjectErr find(Object primaryKey) {
        return baseDao.find(EvaluateProjectErr.class, primaryKey);
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
    public EvaluateProjectErr find(String sql, Object... args) {
        return baseDao.find(sql, EvaluateProjectErr.class, args);
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
    public List<EvaluateProjectErr> findList(String sql, Object... args) {
        return baseDao.findList(sql, EvaluateProjectErr.class, args);
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
    public List<EvaluateProjectErr> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, EvaluateProjectErr.class, args);
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
    public Integer countEvaluateProjectErr(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 
     * 数量查询
     * 
     * @param baseClass
     * @param map
     * @return
     */
    public <T extends BaseEntity> Integer countByCondition(Class<T> baseClass, Map<String, Object> map) {
        // 清除排序条件
        map.entrySet().removeIf(item -> item.getKey().contains("sort"));
        SqlConditionUtil conditionUtil = new EnhancedSqlConditionUtil(map);
        conditionUtil.setSelectFields("count(1)");
        List<Object> params = new ArrayList<>();
        return baseDao.queryInt(new SqlHelper().getSqlComplete(baseClass, conditionUtil.getMap(), params),
                params.toArray());
    }
}

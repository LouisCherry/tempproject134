package com.epoint.jnzwdt.audityjscjr.impl;

import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.jnzwdt.audityjscjr.api.entity.AuditYjsCjr;

import java.util.List;
import java.util.Map;

/**
 * 残疾人一件事表单对应的后台service
 *
 * @author ez
 * @version [版本号, 2021-04-09 13:50:05]
 */
public class AuditYjsCjrService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditYjsCjrService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditYjsCjr record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditYjsCjr.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditYjsCjr record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditYjsCjr find(Object primaryKey) {
        return baseDao.find(AuditYjsCjr.class, primaryKey);
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
    public AuditYjsCjr find(String sql, Object... args) {
        return baseDao.find(sql, AuditYjsCjr.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditYjsCjr> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditYjsCjr.class, args);
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
    public List<AuditYjsCjr> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditYjsCjr.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditYjsCjr(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public <T extends BaseEntity> List<T> findlist(Map<String, Object> conditionMap, Class<T> baseClass) {
        return baseDao.findList(new SqlHelper().getSqlComplete(baseClass, conditionMap),
                baseClass);
    }
}

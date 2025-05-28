package com.epoint.xmz.auditfszl.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 放射诊疗数据对应的后台service
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:16]
 */
public class AuditFszlService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditFszlService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditFszl record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditFszl.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditFszl record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditFszl find(Object primaryKey) {
        return baseDao.find(AuditFszl.class, primaryKey);
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
    public AuditFszl find(String sql, Object... args) {
        return baseDao.find(sql, AuditFszl.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditFszl> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditFszl.class, args);
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
    public List<AuditFszl> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditFszl.class, args);
    }

    public List<AuditFszl> findList(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        return baseDao.findList(new SqlHelper().getSqlComplete(AuditFszl.class, conditionMap, params),
                AuditFszl.class, params.toArray());
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditFszl(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }
}

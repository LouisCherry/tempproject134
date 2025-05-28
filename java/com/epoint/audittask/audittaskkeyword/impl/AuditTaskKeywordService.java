package com.epoint.audittask.audittaskkeyword.impl;

import com.epoint.audittask.audittaskkeyword.api.entity.AuditTaskKeyword;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 事项关键字关系表对应的后台service
 *
 * @author yangyi
 * @version [版本号, 2022-06-17 10:47:41]
 */
public class AuditTaskKeywordService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditTaskKeywordService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskKeyword record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditTaskKeyword.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskKeyword record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskKeyword find(Object primaryKey) {
        return baseDao.find(AuditTaskKeyword.class, primaryKey);
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
    public AuditTaskKeyword find(String sql, Object... args) {
        return baseDao.find(sql, AuditTaskKeyword.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskKeyword> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTaskKeyword.class, args);
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
    public List<AuditTaskKeyword> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditTaskKeyword.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditTaskKeyword(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<AuditTaskKeyword> findListByCondition(Map<String, Object> map) {
        List<Object> params = new ArrayList<>();
        return baseDao.findList(new SqlHelper().getSqlComplete(AuditTaskKeyword.class, map, params), AuditTaskKeyword.class,
                params.toArray());
    }

    public void deleteByTaskid(String taskid) {
        String sql = "delete from audit_task_keyword where taskid = ?";
        baseDao.execute(sql, taskid);
    }

    public AuditTaskKeyword findByTaskid(String task_id) {
        String sql = "select * from audit_task_keyword where taskid = ?";
        return baseDao.find(sql, AuditTaskKeyword.class, task_id);
    }
}

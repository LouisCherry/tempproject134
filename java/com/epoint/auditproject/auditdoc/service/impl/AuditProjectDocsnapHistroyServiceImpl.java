package com.epoint.auditproject.auditdoc.service.impl;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;
import com.epoint.auditproject.auditdoc.dao.AuditProjectDocsnapHistroyDao;
import com.epoint.auditproject.auditdoc.service.IAuditProjectDocsnapHistroyService;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * (AuditProjectDocsnapHistroy)表服务实现类
 *
 * @author panshunxing
 * @since 2024-12-25 19:40:35
 */
@Service
@Component
public class AuditProjectDocsnapHistroyServiceImpl implements IAuditProjectDocsnapHistroyService {
    /**
     * 通过ID查询单条数据
     *
     * @param rowguid 主键
     * @return 实例对象
     */
    @Override
    public AuditProjectDocsnapHistroy queryById(String rowguid) {
        return new AuditProjectDocsnapHistroyDao().queryById(rowguid);
    }

    /**
     * 新增数据
     *
     * @param auditProjectDocsnapHistroy 实例对象
     * @return 实例对象
     */
    @Override
    public AuditProjectDocsnapHistroy insert(AuditProjectDocsnapHistroy auditProjectDocsnapHistroy) {
        new AuditProjectDocsnapHistroyDao().insert(auditProjectDocsnapHistroy);
        return auditProjectDocsnapHistroy;
    }

    /**
     * 修改数据
     *
     * @param auditProjectDocsnapHistroy 实例对象
     * @return 实例对象
     */
    @Override
    public int update(AuditProjectDocsnapHistroy auditProjectDocsnapHistroy) {
        return new AuditProjectDocsnapHistroyDao().update(auditProjectDocsnapHistroy);
    }

    /**
     * 通过主键删除数据
     *
     * @param rowguid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String rowguid) {
        return new AuditProjectDocsnapHistroyDao().deleteById(rowguid) > 0;
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
    public AuditProjectDocsnapHistroy find(String sql, Object... args) {
        return new AuditProjectDocsnapHistroyDao().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditProjectDocsnapHistroy> findList(String sql, Object... args) {
        return new AuditProjectDocsnapHistroyDao().findList(sql, args);
    }

    /**
     * 通过条件执行语句
     *
     * @param sql  执行的sql语句
     * @param args 参数值数组
     * @return 影响行数
     */
    public int execute(String sql, Object... args) {
        return new AuditProjectDocsnapHistroyDao().execute(sql, args);
    }

    @Override
    public PageData<AuditProjectDocsnapHistroy> getListPage(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        return new AuditProjectDocsnapHistroyDao().getListPage( conditionMap, first, pageSize, sortField, sortOrder);
    }
}

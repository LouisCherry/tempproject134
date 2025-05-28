package com.epoint.audittask.audittaskkeyword.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.audittask.audittaskkeyword.api.IAuditTaskKeywordService;
import com.epoint.audittask.audittaskkeyword.api.entity.AuditTaskKeyword;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 事项关键字关系表对应的后台service实现类
 *
 * @author yangyi
 * @version [版本号, 2022-06-17 10:47:41]
 */
@Component
@Service
public class AuditTaskKeywordServiceImpl implements IAuditTaskKeywordService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskKeyword record) {
        return new AuditTaskKeywordService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditTaskKeywordService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskKeyword record) {
        return new AuditTaskKeywordService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskKeyword find(Object primaryKey) {
        return new AuditTaskKeywordService().find(primaryKey);
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
        return new AuditTaskKeywordService().find(sql, args);
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
        return new AuditTaskKeywordService().findList(sql, args);
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
        return new AuditTaskKeywordService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditTaskKeyword(String sql, Object... args) {
        return new AuditTaskKeywordService().countAuditTaskKeyword(sql, args);
    }

    @Override
    public List<AuditTaskKeyword> findListByCondition(Map<String, Object> map) {
        return new AuditTaskKeywordService().findListByCondition(map);
    }

    @Override
    public void deleteByTaskid(String taskid) {
        new AuditTaskKeywordService().deleteByTaskid(taskid);
    }

    @Override
    public AuditTaskKeyword findByTaskid(String task_id) {
        return new AuditTaskKeywordService().findByTaskid(task_id);
    }

}

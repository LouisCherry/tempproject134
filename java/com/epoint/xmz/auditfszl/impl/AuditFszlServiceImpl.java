package com.epoint.xmz.auditfszl.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;

/**
 * 放射诊疗数据对应的后台service实现类
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:16]
 */
@Component
@Service
public class AuditFszlServiceImpl implements IAuditFszlService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditFszl record) {
        return new AuditFszlService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditFszlService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditFszl record) {
        return new AuditFszlService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditFszl find(Object primaryKey) {
        return new AuditFszlService().find(primaryKey);
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
        return new AuditFszlService().find(sql, args);
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
        return new AuditFszlService().findList(sql, args);
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
        return new AuditFszlService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public List<AuditFszl> findList(Map<String, Object> conditionMap) {
        return new AuditFszlService().findList(conditionMap);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditFszl(String sql, Object... args) {
        return new AuditFszlService().countAuditFszl(sql, args);
    }

}

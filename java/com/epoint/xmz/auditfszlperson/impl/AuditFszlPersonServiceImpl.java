package com.epoint.xmz.auditfszlperson.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;

/**
 * 反射工作人员表对应的后台service实现类
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:38]
 */
@Component
@Service
public class AuditFszlPersonServiceImpl implements IAuditFszlPersonService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditFszlPerson record) {
        return new AuditFszlPersonService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditFszlPersonService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditFszlPerson record) {
        return new AuditFszlPersonService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditFszlPerson find(Object primaryKey) {
        return new AuditFszlPersonService().find(primaryKey);
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
    public AuditFszlPerson find(String sql, Object... args) {
        return new AuditFszlPersonService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditFszlPerson> findList(String sql, Object... args) {
        return new AuditFszlPersonService().findList(sql, args);
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
    public List<AuditFszlPerson> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditFszlPersonService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditFszlPerson(String sql, Object... args) {
        return new AuditFszlPersonService().countAuditFszlPerson(sql, args);
    }

    @Override
    public void deleteByFszlguid(String fszlguid) {
        new AuditFszlPersonService().deleteByFszlguid(fszlguid);
    }

    @Override
    public List<AuditFszlPerson> findListByFszlguid(String fszlguid) {
        return new AuditFszlPersonService().findListByFszlguid(fszlguid);
    }

}

package com.epoint.xmz.audittaskjn.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnService;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事项个性化表对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2023-11-01 14:01:22]
 */
@Component
@Service
public class AuditTaskJnServiceImpl implements IAuditTaskJnService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskJn record) {
        return new AuditTaskJnService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditTaskJnService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskJn record) {
        return new AuditTaskJnService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskJn find(Object primaryKey) {
        return new AuditTaskJnService().find(primaryKey);
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
    public AuditTaskJn find(String sql, Object... args) {
        return new AuditTaskJnService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskJn> findList(String sql, Object... args) {
        return new AuditTaskJnService().findList(sql, args);
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
    public List<AuditTaskJn> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditTaskJnService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditTaskJn(String sql, Object... args) {
        return new AuditTaskJnService().countAuditTaskJn(sql, args);
    }

    @Override
    public List<FrameOu> getAllOu() {
        return new AuditTaskJnService().getAllOu();
    }

    @Override
    public void deleteAllTaskJnByTaskid(String task_id) {
        new AuditTaskJnService().deleteAllTaskJnByTaskid(task_id);
    }

}

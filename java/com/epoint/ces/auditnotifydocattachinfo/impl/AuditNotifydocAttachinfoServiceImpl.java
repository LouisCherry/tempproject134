package com.epoint.ces.auditnotifydocattachinfo.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.ces.auditnotifydocattachinfo.api.entity.AuditNotifydocAttachinfo;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.ces.auditnotifydocattachinfo.api.IAuditNotifydocAttachinfoService;
/**
 * 办件文书信息表对应的后台service实现类
 * 
 * @author jiem
 * @version [版本号, 2022-03-15 14:02:49]
 */
@Component
@Service
public class AuditNotifydocAttachinfoServiceImpl implements IAuditNotifydocAttachinfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditNotifydocAttachinfo record) {
        return new AuditNotifydocAttachinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditNotifydocAttachinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditNotifydocAttachinfo record) {
        return new AuditNotifydocAttachinfoService().update(record);
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
    public AuditNotifydocAttachinfo find(Object primaryKey) {
       return new AuditNotifydocAttachinfoService().find(primaryKey);
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
    public AuditNotifydocAttachinfo find(String sql, Object... args) {
        return new AuditNotifydocAttachinfoService().find(sql,args);
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
    public List<AuditNotifydocAttachinfo> findList(String sql, Object... args) {
       return new AuditNotifydocAttachinfoService().findList(sql,args);
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
    public List<AuditNotifydocAttachinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditNotifydocAttachinfoService().findList(sql,pageNumber,pageSize,args);
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
     @Override
    public Integer countAuditNotifydocAttachinfo(String sql, Object... args){
        return new AuditNotifydocAttachinfoService().countAuditNotifydocAttachinfo(sql, args);
    }

    @Override
    public List<AuditNotifydocAttachinfo> findPage(String sql, int first, int pageSize) {
        return new AuditNotifydocAttachinfoService().findPage(sql,first,pageSize);
    }

}

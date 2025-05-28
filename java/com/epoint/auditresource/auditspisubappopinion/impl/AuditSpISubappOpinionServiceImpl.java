package com.epoint.auditresource.auditspisubappopinion.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditresource.auditspisubappopinion.api.IAuditSpISubappOpinionService;
import com.epoint.auditresource.auditspisubappopinion.api.entity.AuditSpISubappOpinion;
/**
 * 并联审批意见对应的后台service实现类
 * 
 * @author zhaoy
 * @version [版本号, 2019-04-24 15:11:41]
 */
@Component
@Service
public class AuditSpISubappOpinionServiceImpl implements IAuditSpISubappOpinionService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpISubappOpinion record) {
        return new AuditSpISubappOpinionService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditSpISubappOpinionService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpISubappOpinion record) {
        return new AuditSpISubappOpinionService().update(record);
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
    public AuditSpISubappOpinion find(Object primaryKey) {
       return new AuditSpISubappOpinionService().find(primaryKey);
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
    public AuditSpISubappOpinion find(String sql, Object... args) {
        return new AuditSpISubappOpinionService().find(args);
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
    public List<AuditSpISubappOpinion> findList(String sql, Object... args) {
       return new AuditSpISubappOpinionService().findList(sql,args);
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
    public List<AuditSpISubappOpinion> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditSpISubappOpinionService().findList(sql,pageNumber,pageSize,args);
    }

}

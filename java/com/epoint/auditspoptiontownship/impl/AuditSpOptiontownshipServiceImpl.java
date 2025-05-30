package com.epoint.auditspoptiontownship.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;

/**
 * 情形引导选项乡镇延伸对应的后台service实现类
 * 
 * @author xzkui
 * @version [版本号, 2020-10-16 15:53:19]
 */
@Component
@Service
public class AuditSpOptiontownshipServiceImpl implements IAuditSpOptiontownshipService
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
    public int insert(AuditSpOptiontownship record) {
        return new AuditSpOptiontownshipService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditSpOptiontownshipService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpOptiontownship record) {
        return new AuditSpOptiontownshipService().update(record);
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
    public AuditSpOptiontownship find(Object primaryKey) {
        return new AuditSpOptiontownshipService().find(primaryKey);
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
    public AuditSpOptiontownship find(String sql, Object... args) {
        return new AuditSpOptiontownshipService().find(sql, args);
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
    public List<AuditSpOptiontownship> findList(String sql, Object... args) {
        return new AuditSpOptiontownshipService().findList(sql, args);
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
    public List<AuditSpOptiontownship> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditSpOptiontownshipService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditSpOptiontownship(String sql, Object... args) {
        return new AuditSpOptiontownshipService().countAuditSpOptiontownship(sql, args);
    }

    @Override
    public int deleteByBusinessGuid(String businessguid) {
        return new AuditSpOptiontownshipService().deleteByBusinessGuid(businessguid);
    }

    @Override
    public List<AuditSpOptiontownship> findListByCondition(Map<String, String> condition) {
        return new AuditSpOptiontownshipService().findListByCondition(condition);
    }

    @Override
    public Integer deleteByTaskid(String taskid) {
        return new AuditSpOptiontownshipService().deleteByTaskid(taskid);
    }


}

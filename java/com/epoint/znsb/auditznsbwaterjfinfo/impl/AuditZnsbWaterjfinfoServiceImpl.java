package com.epoint.znsb.auditznsbwaterjfinfo.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;
/**
 * 水务缴费信息对应的后台service实现类
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 14:49:42]
 */
@Component
@Service
public class AuditZnsbWaterjfinfoServiceImpl implements IAuditZnsbWaterjfinfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbWaterjfinfo record) {
        return new AuditZnsbWaterjfinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditZnsbWaterjfinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbWaterjfinfo record) {
        return new AuditZnsbWaterjfinfoService().update(record);
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
    public AuditZnsbWaterjfinfo find(Object primaryKey) {
       return new AuditZnsbWaterjfinfoService().find(primaryKey);
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
    public AuditZnsbWaterjfinfo find(String sql, Object... args) {
        return new AuditZnsbWaterjfinfoService().find(sql,args);
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
    public List<AuditZnsbWaterjfinfo> findList(String sql, Object... args) {
       return new AuditZnsbWaterjfinfoService().findList(sql,args);
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
    public List<AuditZnsbWaterjfinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditZnsbWaterjfinfoService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditZnsbWaterjfinfo(String sql, Object... args){
        return new AuditZnsbWaterjfinfoService().countAuditZnsbWaterjfinfo(sql, args);
    }

    @Override
    public List<AuditZnsbWaterjfinfo> findListByTime(String time) {
        return new AuditZnsbWaterjfinfoService().findListByTime(time);
    }

}

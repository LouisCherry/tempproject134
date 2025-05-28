package com.epoint.ces.auditznsbwaitvedio.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.ces.auditznsbwaitvedio.api.entity.AuditZnsbWaitvedio;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.ces.auditznsbwaitvedio.api.IAuditZnsbWaitvedioService;
/**
 * 等待屏视频对应的后台service实现类
 * 
 * @author admin
 * @version [版本号, 2020-03-31 15:07:02]
 */
@Component
@Service
public class AuditZnsbWaitvedioServiceImpl implements IAuditZnsbWaitvedioService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbWaitvedio record) {
        return new AuditZnsbWaitvedioService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditZnsbWaitvedioService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbWaitvedio record) {
        return new AuditZnsbWaitvedioService().update(record);
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
    public AuditZnsbWaitvedio find(Object primaryKey) {
       return new AuditZnsbWaitvedioService().find(primaryKey);
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
    public AuditZnsbWaitvedio find(String sql, Object... args) {
        return new AuditZnsbWaitvedioService().find(sql,args);
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
    public List<AuditZnsbWaitvedio> findList(String sql, Object... args) {
       return new AuditZnsbWaitvedioService().findList(sql,args);
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
    public List<AuditZnsbWaitvedio> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditZnsbWaitvedioService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditZnsbWaitvedio(String sql, Object... args){
        return new AuditZnsbWaitvedioService().countAuditZnsbWaitvedio(sql, args);
    }
     @Override
     public  AuditZnsbWaitvedio findfirst(String condition) {
         return new AuditZnsbWaitvedioService().findfirst(condition);
     }
}

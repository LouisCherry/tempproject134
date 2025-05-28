package com.epoint.znsb.auditznsbwater.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.znsb.auditznsbwater.api.entity.Auditznsbwater;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.znsb.auditznsbwater.api.IAuditznsbwaterService;
/**
 * 水务对账信息对应的后台service实现类
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 16:08:59]
 */
@Component
@Service
public class AuditznsbwaterServiceImpl implements IAuditznsbwaterService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Auditznsbwater record) {
        return new AuditznsbwaterService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditznsbwaterService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Auditznsbwater record) {
        return new AuditznsbwaterService().update(record);
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
    public Auditznsbwater find(Object primaryKey) {
       return new AuditznsbwaterService().find(primaryKey);
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
    public Auditznsbwater find(String sql, Object... args) {
        return new AuditznsbwaterService().find(sql,args);
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
    public List<Auditznsbwater> findList(String sql, Object... args) {
       return new AuditznsbwaterService().findList(sql,args);
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
    public List<Auditznsbwater> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditznsbwaterService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditznsbwater(String sql, Object... args){
        return new AuditznsbwaterService().countAuditznsbwater(sql, args);
    }

    @Override
    public List<Auditznsbwater> getListByIsupload() {

        return new AuditznsbwaterService().getListByIsupload();
    }

    @Override
    public List<Auditznsbwater> getLisrtByname(String name) {
        return new AuditznsbwaterService().getLisrtByname(name);
    }

}

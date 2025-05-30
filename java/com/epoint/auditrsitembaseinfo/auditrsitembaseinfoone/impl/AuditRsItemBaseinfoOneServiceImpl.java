package com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.domain.AuditRsItemBaseinfoOne;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.inter.IAuditRsItemBaseinfoOneService;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.service.AuditRsItemBaseinfoOneService;
/**
 * 泰安建设项目第一阶段基本信息拓展表对应的后台service实现类
 * 
 * @author wangxiaolong
 * @version [版本号, 2019-08-05 14:21:41]
 */
@Component
@Service
public class AuditRsItemBaseinfoOneServiceImpl implements IAuditRsItemBaseinfoOneService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditRsItemBaseinfoOne record) {
        return new AuditRsItemBaseinfoOneService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditRsItemBaseinfoOneService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditRsItemBaseinfoOne record) {
        return new AuditRsItemBaseinfoOneService().update(record);
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
    public AuditRsItemBaseinfoOne find(Object primaryKey) {
       return new AuditRsItemBaseinfoOneService().find(primaryKey);
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
    public AuditRsItemBaseinfoOne find(String sql, Object... args) {
        return new AuditRsItemBaseinfoOneService().find(args);
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
    public List<AuditRsItemBaseinfoOne> findList(String sql, Object... args) {
       return new AuditRsItemBaseinfoOneService().findList(sql,args);
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
    public List<AuditRsItemBaseinfoOne> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditRsItemBaseinfoOneService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public AuditRsItemBaseinfoOne getAuditRsItemBaseinfoOneByparentidandpaseguid(String parentid) {
        // TODO Auto-generated method stub
        return new AuditRsItemBaseinfoOneService().getAuditRsItemBaseinfoOneByparentidandpaseguid(parentid);
    }

/*    @Override
    public AuditRsItemBaseinfoOne getAuditRsItemBaseinfoOneByparentid(String parentid,String subappguid) {
        // TODO Auto-generated method stub
        return new AuditRsItemBaseinfoOneService().getAuditRsItemBaseinfoOneByparentid(parentid,subappguid);

    }*/

}

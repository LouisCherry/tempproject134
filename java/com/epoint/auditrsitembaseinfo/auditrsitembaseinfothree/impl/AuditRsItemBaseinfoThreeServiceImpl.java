package com.epoint.auditrsitembaseinfo.auditrsitembaseinfothree.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.epoint.auditrsitembaseinfo.auditrsitembaseinfothree.api.IAuditRsItemBaseinfoThreeService;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfothree.api.entity.AuditRsItemBaseinfoThree;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * 泰安建设项目第三阶段基本信息拓展表对应的后台service实现类
 * 
 * @author wangxiaolong
 * @version [版本号, 2019-09-02 14:33:52]
 */
@Component
@Service
public class AuditRsItemBaseinfoThreeServiceImpl implements IAuditRsItemBaseinfoThreeService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditRsItemBaseinfoThree record) {
        return new AuditRsItemBaseinfoThreeService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditRsItemBaseinfoThreeService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditRsItemBaseinfoThree record) {
        return new AuditRsItemBaseinfoThreeService().update(record);
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
    public AuditRsItemBaseinfoThree find(Object primaryKey) {
       return new AuditRsItemBaseinfoThreeService().find(primaryKey);
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
    public AuditRsItemBaseinfoThree find(String sql, Object... args) {
        return new AuditRsItemBaseinfoThreeService().find(args);
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
    public List<AuditRsItemBaseinfoThree> findList(String sql, Object... args) {
       return new AuditRsItemBaseinfoThreeService().findList(sql,args);
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
    public List<AuditRsItemBaseinfoThree> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditRsItemBaseinfoThreeService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public AuditRsItemBaseinfoThree getAuditRsItemBaseinfoThreeByparentidandpaseguid(String parentid,
            String subappguid) {
        // TODO Auto-generated method stub
        return new AuditRsItemBaseinfoThreeService().getAuditRsItemBaseinfoThreeByparentidandpaseguid(parentid, subappguid);
    }

}

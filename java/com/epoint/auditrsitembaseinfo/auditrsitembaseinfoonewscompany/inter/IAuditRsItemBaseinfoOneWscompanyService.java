package com.epoint.auditrsitembaseinfo.auditrsitembaseinfoonewscompany.inter;
import java.io.Serializable;
import java.util.List;

import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoonewscompany.domain.AuditRsItemBaseinfoOneWscompany;


/**
 * 泰安建设项目第一阶段涉及外商投资项目信息表对应的后台service接口
 * 
 * @author wangxiaolong
 * @version [版本号, 2019-08-06 16:24:47]
 */
public interface IAuditRsItemBaseinfoOneWscompanyService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditRsItemBaseinfoOneWscompany record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditRsItemBaseinfoOneWscompany record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditRsItemBaseinfoOneWscompany find(Object primaryKey);

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
    public AuditRsItemBaseinfoOneWscompany find(String sql,Object... args);

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
    public List<AuditRsItemBaseinfoOneWscompany> findList(String sql, Object... args);

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
    public List<AuditRsItemBaseinfoOneWscompany> findList(String sql, int pageNumber, int pageSize,Object... args);
/*    public AuditRsItemBaseinfoOneWscompany getAuditRsItemBaseinfoOneWscompanyByparentid(String parentid);
*/    public AuditRsItemBaseinfoOneWscompany getAuditRsItemBaseinfoOneWscompanyByparentidandpaseguid(String parentid,String subappguid) ;
}

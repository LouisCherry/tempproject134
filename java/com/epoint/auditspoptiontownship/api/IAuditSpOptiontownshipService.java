package com.epoint.auditspoptiontownship.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;

/**
 * 情形引导选项乡镇延伸对应的后台service接口
 * 
 * @author xzkui
 * @version [版本号, 2020-10-16 15:53:19]
 */
public interface IAuditSpOptiontownshipService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpOptiontownship record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    public int deleteByBusinessGuid(String businessguid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpOptiontownship record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditSpOptiontownship find(Object primaryKey);

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
    public AuditSpOptiontownship find(String sql, Object... args);

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
    public List<AuditSpOptiontownship> findList(String sql, Object... args);

    /**
     * 
     *  [根据条件查询] 
     *  @param condition
     *  @return    
     */
    public List<AuditSpOptiontownship> findListByCondition(Map<String, String> condition);

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
    public List<AuditSpOptiontownship> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
    * 查询数量
    * 
    * @param sql
    *            执行语句
    * @param args
    *            参数
    * @return Integer
    */
    public Integer countAuditSpOptiontownship(String sql, Object... args);

    /**
     * 
     *  [根据taskid删除数据] 
     *  @param string    
     */
    public Integer deleteByTaskid(String taskid);


}

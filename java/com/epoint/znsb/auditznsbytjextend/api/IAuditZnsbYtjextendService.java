package com.epoint.znsb.auditznsbytjextend.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;
import com.epoint.zoucheng.znsb.auditznsbmodule.inter.IZCAuditZnsbModule;

/**
 * 一体机模块额外配置对应的后台service接口
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 10:11:50]
 */
public interface IAuditZnsbYtjextendService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbYtjextend record);

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
    public int update(AuditZnsbYtjextend record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditZnsbYtjextend find(Object primaryKey);

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
    public AuditZnsbYtjextend find(String sql,Object... args);

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
    public List<AuditZnsbYtjextend> findList(String sql, Object... args);

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
    public List<AuditZnsbYtjextend> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countAuditZnsbYtjextend(String sql, Object... args);


	 public List<AuditZnsbSelfmachinemodule> getModuleListByLabel(String labelguid,boolean isflag);
}

package com.epoint.ces.requesthikuserlog.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditorga.auditrecord.domain.AuditOrgaRecord;
import com.epoint.ces.requesthikuserlog.api.entity.RequestHikUserLog;

/**
 * 请求海康日志信息人员考勤记录表对应的后台service接口
 * 
 * @author shun
 * @version [版本号, 2021-11-22 14:32:24]
 */
public interface IRequestHikUserLogService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RequestHikUserLog record);

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
    public int update(RequestHikUserLog record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public RequestHikUserLog find(Object primaryKey);

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
    public RequestHikUserLog find(String sql,Object... args);

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
    public List<RequestHikUserLog> findList(String sql, Object... args);

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
    public List<RequestHikUserLog> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countRequestHikUserLog(String sql, Object... args);

    /**
     * 根据工号和考勤时间查询考勤数据
     * @param cardNo
     * @param dksj
     * @return
     */
    AuditOrgaRecord selectAuditOrgaRecordByXh(String cardNo, String dksj);

    /**
     * 考勤信息入库
     * @param record
     */
    int insertRecord(AuditOrgaRecord record);
}

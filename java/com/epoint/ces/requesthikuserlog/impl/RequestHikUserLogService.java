package com.epoint.ces.requesthikuserlog.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.auditorga.auditrecord.domain.AuditOrgaRecord;
import com.epoint.core.grammar.Record;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.ces.requesthikuserlog.api.entity.RequestHikUserLog;

/**
 * 请求海康日志信息人员考勤记录表对应的后台service
 * 
 * @author shun
 * @version [版本号, 2021-11-22 14:32:24]
 */
public class RequestHikUserLogService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public RequestHikUserLogService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RequestHikUserLog record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(RequestHikUserLog.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(RequestHikUserLog record) {
        return baseDao.update(record);
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
    public RequestHikUserLog find(Object primaryKey) {
        return baseDao.find(RequestHikUserLog.class, primaryKey);
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
    public RequestHikUserLog find(String sql,  Object... args) {
        return baseDao.find(sql, RequestHikUserLog.class, args);
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
    public List<RequestHikUserLog> findList(String sql, Object... args) {
        return baseDao.findList(sql, RequestHikUserLog.class, args);
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
    public List<RequestHikUserLog> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, RequestHikUserLog.class, args);
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
    public Integer countRequestHikUserLog(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public AuditOrgaRecord selectAuditOrgaRecordByXh(String cardNo, String dksj) {
        String sql = "select * from AUDIT_ORGA_RECORD where XH  = ? and shijian = ? ";
        return baseDao.find(sql,AuditOrgaRecord.class,cardNo,dksj);
    }

    public int insertRecord(AuditOrgaRecord record) {
       return baseDao.insert(record);
    }
}

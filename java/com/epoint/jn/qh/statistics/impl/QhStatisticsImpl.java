package com.epoint.jn.qh.statistics.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.jn.qh.statistics.api.IQhStatistics;
import com.epoint.jn.qh.statistics.api.QhStatistics;
/**
 * 取号量统计对应的后台service实现类
 * 
 * @author 夜雨清尘
 * @version [版本号, 2019-06-06 16:10:26]
 */
@Component
@Service
public class QhStatisticsImpl implements IQhStatistics
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(QhStatistics record) {
        return new QhStatisticsService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new QhStatisticsService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(QhStatistics record) {
        return new QhStatisticsService().update(record);
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
    public QhStatistics find(Object primaryKey) {
       return new QhStatisticsService().find(primaryKey);
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
    public QhStatistics find(String sql, Object... args) {
        return new QhStatisticsService().find(args);
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
    public List<QhStatistics> findList(String sql, Object... args) {
       return new QhStatisticsService().findList(sql,args);
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
    public List<QhStatistics> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new QhStatisticsService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public AuditCommonResult<String> setCount(String macaddress,String centerguid) {
        AuditCommonResult<String> result=new AuditCommonResult<String>();
        QhStatisticsService service=new QhStatisticsService();
        try {
            service.setCount(macaddress,centerguid);
        }
        catch (Exception e) {
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

}

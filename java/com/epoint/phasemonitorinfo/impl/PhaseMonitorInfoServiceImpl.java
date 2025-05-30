package com.epoint.phasemonitorinfo.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.phasemonitorinfo.api.entity.PhaseMonitorInfo;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.phasemonitorinfo.api.IPhaseMonitorInfoService;
/**
 * 阶段监控表对应的后台service实现类
 * 
 * @author liucheng
 * @version [版本号, 2019-08-26 14:29:32]
 */
@Component
@Service
public class PhaseMonitorInfoServiceImpl implements IPhaseMonitorInfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(PhaseMonitorInfo record) {
        return new PhaseMonitorInfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new PhaseMonitorInfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(PhaseMonitorInfo record) {
        return new PhaseMonitorInfoService().update(record);
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
    public PhaseMonitorInfo find(Object primaryKey) {
       return new PhaseMonitorInfoService().find(primaryKey);
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
    public PhaseMonitorInfo find(String sql, Object... args) {
        return new PhaseMonitorInfoService().find(args);
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
    public List<PhaseMonitorInfo> findList(String sql, Object... args) {
       return new PhaseMonitorInfoService().findList(sql,args);
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
    public List<PhaseMonitorInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new PhaseMonitorInfoService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public PhaseMonitorInfo findBySubappguid(String subAppGuid,String minitortype) {
        return new PhaseMonitorInfoService().findBySubappguid(subAppGuid,minitortype);
    }

}

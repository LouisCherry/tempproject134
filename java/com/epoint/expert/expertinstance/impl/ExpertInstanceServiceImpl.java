package com.epoint.expert.expertinstance.impl;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.expert.expertinstance.api.IExpertInstanceService;
import com.epoint.expert.expertinstance.api.entity.ExpertInstance;
/**
 * 专家抽取实例表对应的后台service实现类
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:41:50]
 */
@Component
@Service
public class ExpertInstanceServiceImpl implements IExpertInstanceService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertInstance record) {
        return new ExpertInstanceService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ExpertInstanceService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertInstance record) {
        return new ExpertInstanceService().update(record);
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
    public ExpertInstance find(Object primaryKey) {
       return new ExpertInstanceService().find(primaryKey);
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
    public ExpertInstance find(String sql, Object... args) {
        return new ExpertInstanceService().find(args);
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
    public List<ExpertInstance> findList(String sql, Object... args) {
       return new ExpertInstanceService().findList(sql,args);
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
    public List<ExpertInstance> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ExpertInstanceService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public List<String> getExtractRule(Date bidTime, Integer conditionDay, Integer conditionTimes) {
        return new ExpertInstanceService().getExtractRule(bidTime, conditionDay, conditionTimes);
    }

    @Override
    public void deleteResultByInstanceGuid(String instanceGuid) {
        new ExpertInstanceService().deleteResultByInstanceGuid(instanceGuid);   
    }

    @Override
    public void deleteSmsByInstanceGuid(String instanceGuid) {
        new ExpertInstanceService().deleteSmsByInstanceGuid(instanceGuid);
        
    }

    @Override
    public void deleteRuleByInstanceGuid(String instanceGuid) {
        new ExpertInstanceService().deleteRuleByInstanceGuid(instanceGuid);      
    }

}

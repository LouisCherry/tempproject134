package com.epoint.expert.expertiresult.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
/**
 * 专家抽取结果表对应的后台service实现类
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:42:03]
 */
@Component
@Service
public class ExpertIResultServiceImpl implements IExpertIResultService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertIResult record) {
        return new ExpertIResultService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ExpertIResultService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertIResult record) {
        return new ExpertIResultService().update(record);
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
    public ExpertIResult find(Object primaryKey) {
       return new ExpertIResultService().find(primaryKey);
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
    public ExpertIResult find(String sql, Object... args) {
        return new ExpertIResultService().find(args);
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
    public List<ExpertIResult> findList(String sql, Object... args) {
       return new ExpertIResultService().findList(sql,args);
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
    public List<ExpertIResult> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ExpertIResultService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public List<ExpertIResult> findListByCondition(Map<String, String> conditionMap) {
        return new ExpertIResultService().findListByCondition(conditionMap);
    }

    @Override
    public int deleteByInstanceguid(String instanceGuid, String is_auto) {
        return  new ExpertIResultService().deleteByInstanceguid(instanceGuid, is_auto);
    }

    @Override
    public List<String> selectExpertByInstanceguid(String instanceGuid, String is_auto) {
        return new ExpertIResultService().selectExpertByInstanceguid(instanceGuid, is_auto);
    }

}

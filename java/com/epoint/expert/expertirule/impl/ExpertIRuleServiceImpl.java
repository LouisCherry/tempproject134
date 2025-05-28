package com.epoint.expert.expertirule.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.expert.expertirule.api.IExpertIRuleService;
import com.epoint.expert.expertirule.api.entity.ExpertIRule;
 /* 专家抽取规则表对应的后台service实现类
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:41:57]
 */
@Component
@Service
public class ExpertIRuleServiceImpl implements IExpertIRuleService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertIRule record) {
        return new ExpertIRuleService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ExpertIRuleService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertIRule record) {
        return new ExpertIRuleService().update(record);
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
    public ExpertIRule find(Object primaryKey) {
       return new ExpertIRuleService().find(primaryKey);
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
    public ExpertIRule find(String sql, Object... args) {
        return new ExpertIRuleService().find(args);
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
    public List<ExpertIRule> findList(String sql, Object... args) {
       return new ExpertIRuleService().findList(sql,args);
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
    public List<ExpertIRule> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ExpertIRuleService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public List<ExpertIRule> findListByCondition(Map<String, String> conditionMap) {
        return new ExpertIRuleService().findListByCondition(conditionMap);
    }

    @Override
    public void deleteObjectByInstanceguid(String instanceGuid, String objectType) {
        new ExpertIRuleService().deleteObjectByInstanceguid(instanceGuid,objectType);
        
    }

}

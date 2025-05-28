package com.epoint.auditsppolicybasicinfo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsppolicybasicinfo.api.IAuditSpPolicyBasicinfoService;
import com.epoint.auditsppolicybasicinfo.api.entity.AuditSpPolicyBasicinfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 政策信息表对应的后台service实现类
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:26:17]
 */
@Component
@Service
public class AuditSpPolicyBasicinfoServiceImpl implements IAuditSpPolicyBasicinfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpPolicyBasicinfo record) {
        return new AuditSpPolicyBasicinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditSpPolicyBasicinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpPolicyBasicinfo record) {
        return new AuditSpPolicyBasicinfoService().update(record);
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
    public AuditSpPolicyBasicinfo find(Object primaryKey) {
        return new AuditSpPolicyBasicinfoService().find(primaryKey);
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
    public AuditSpPolicyBasicinfo find(String sql, Object... args) {
        return new AuditSpPolicyBasicinfoService().find(sql, args);
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
    public List<AuditSpPolicyBasicinfo> findList(String sql, Object... args) {
        return new AuditSpPolicyBasicinfoService().findList(sql, args);
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
    public List<AuditSpPolicyBasicinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditSpPolicyBasicinfoService().findList(sql, pageNumber, pageSize, args);
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
    @Override
    public Integer countAuditSpPolicyBasicinfo(String sql, Object... args) {
        return new AuditSpPolicyBasicinfoService().countAuditSpPolicyBasicinfo(sql, args);
    }
    
    /**
     * 
     * 获取政策信息集合
     * 
     * @param conditionMap
     *            查询条件
     * @return list结果
     */
    @Override
    public List<AuditSpPolicyBasicinfo> getAuditSpPolicyListByCondition(Map<String, String> conditionMap) {        
        return new AuditSpPolicyBasicinfoService().getAuditSpPolicyListByCondition(conditionMap);
    }

}

package com.epoint.auditspphasebaseinfo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 前四阶段信息配置表对应的后台service实现类
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@Component
@Service
public class AuditSpPhaseBaseinfoServiceImpl implements IAuditSpPhaseBaseinfoService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpPhaseBaseinfo record) {
        return new AuditSpPhaseBaseinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditSpPhaseBaseinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpPhaseBaseinfo record) {
        return new AuditSpPhaseBaseinfoService().update(record);
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
    public AuditSpPhaseBaseinfo find(Object primaryKey) {
        return new AuditSpPhaseBaseinfoService().find(primaryKey);
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
    public AuditSpPhaseBaseinfo find(String sql, Object... args) {
        return new AuditSpPhaseBaseinfoService().find(sql, args);
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
    public List<AuditSpPhaseBaseinfo> findList(String sql, Object... args) {
        return new AuditSpPhaseBaseinfoService().findList(sql, args);
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
    public List<AuditSpPhaseBaseinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditSpPhaseBaseinfoService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditSpPhaseBaseinfo(String sql, Object... args) {
        return new AuditSpPhaseBaseinfoService().countAuditSpPhaseBaseinfo(sql, args);
    }

    @Override
    public List<AuditSpPhaseBaseinfo> getAllPhaseBaseinfo() {
        return new AuditSpPhaseBaseinfoService().getAllPhaseBaseinfo();
    }
    
    /**
     * 
     * 获取阶段信息集合
     * 
     * @param conditionMap
     *            查询条件
     * @return list结果
     */
    @Override
    public List<AuditSpPhaseBaseinfo> getAuditSpPhaseBaseinfoListByCondition(Map<String, String> conditionMap) {        
        return new AuditSpPhaseBaseinfoService().getAuditSpPhaseBaseinfoListByCondition(conditionMap);
    }

}

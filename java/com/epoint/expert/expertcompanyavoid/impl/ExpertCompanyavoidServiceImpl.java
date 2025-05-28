package com.epoint.expert.expertcompanyavoid.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;

/**
 * 专家回避单位表对应的后台service实现类
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
@Component
@Service
public class ExpertCompanyavoidServiceImpl implements IExpertCompanyavoidService
{
    /**
     * 
     */
    private static final long serialVersionUID = 9159765447990838624L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertCompanyavoid record) {
        return new ExpertCompanyavoidService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ExpertCompanyavoidService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertCompanyavoid record) {
        return new ExpertCompanyavoidService().update(record);
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
    public ExpertCompanyavoid find(Object primaryKey) {
        return new ExpertCompanyavoidService().find(primaryKey);
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
    public ExpertCompanyavoid find(String sql, Object... args) {
        return new ExpertCompanyavoidService().find(args);
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
    public List<ExpertCompanyavoid> findList(String sql, Object... args) {
        return new ExpertCompanyavoidService().findList(sql, args);
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
    public List<ExpertCompanyavoid> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ExpertCompanyavoidService().findList(sql, pageNumber, pageSize, args);
    }
    
    @Override
    public List<String> getExpertGuidsByAvoidCompanyGuid(String companyGuids) {
        return new ExpertCompanyavoidService().getExpertGuidsByAvoidCompanyGuid(companyGuids);
    }

    @Override
    public List<ExpertCompanyavoid> findListByexpertguid(String sql, String expertguid, int pageNumber, int pageSize,
            Object... args) {
        return new ExpertCompanyavoidService().findListByexpertguid(sql, expertguid, pageNumber, pageSize, args);
    }

    @Override
    public List<ExpertCompanyavoid> findListByexpertguid(String sql, String expertguid, Object... args) {
        return new ExpertCompanyavoidService().findListByexpertguid(sql, expertguid, args);
    }

    @Override
    public void deleteByExpertGuid(String expertGuid) {
        new ExpertCompanyavoidService().deleteByExpertGuid(expertGuid);
    }

}

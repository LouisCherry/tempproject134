package com.epoint.expert.expertcompany.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;

/**
 * 从业单位表对应的后台service实现类
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
@Component
@Service
public class ExpertCompanyServiceImpl implements IExpertCompanyService
{
    /**
     * 
     */
    private static final long serialVersionUID = -1867940726972169410L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertCompany record) {
        return new ExpertCompanyService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ExpertCompanyService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertCompany record) {
        return new ExpertCompanyService().update(record);
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
    public ExpertCompany find(Object primaryKey) {
        return new ExpertCompanyService().find(primaryKey);
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
    public ExpertCompany find(String sql, Object... args) {
        return new ExpertCompanyService().find(args);
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
    public List<ExpertCompany> findList(String sql, Object... args) {
        return new ExpertCompanyService().findList(sql, args);
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
    public List<ExpertCompany> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ExpertCompanyService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public List<ExpertCompany> findListByCondition(Map<String, String> conditionMap) {
        return new ExpertCompanyService().findListByCondition(conditionMap);
    }

    @Override
    public List<ExpertCompany> findListRemDel(String sql, String sign, int pageNumber, int pageSize, Object... args) {
        return new ExpertCompanyService().findListRemDel(sql, sign, pageNumber, pageSize, args);
    }

    @Override
    public List<ExpertCompany> findListRemAlready(String sql, String expertguid, int pageNumber, int pageSize,
            Object... args) {
        return new ExpertCompanyService().findListRemAlready(sql, expertguid, pageNumber, pageSize, args);
    }

    @Override
    public List<ExpertCompany> findListRemDel(String sql, String sign, Object... args) {
        return new ExpertCompanyService().findListRemDel(sql, sign, args);
    }

    @Override
    public List<ExpertCompany> findListRemAlready(String sql, String expertguid, Object... args) {
        return new ExpertCompanyService().findListRemAlready(sql, expertguid, args);
    }
}

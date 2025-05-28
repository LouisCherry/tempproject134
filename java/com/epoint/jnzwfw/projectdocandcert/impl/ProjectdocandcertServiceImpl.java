package com.epoint.jnzwfw.projectdocandcert.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.projectdocandcert.api.IProjectdocandcertService;
import com.epoint.jnzwfw.projectdocandcert.api.entity.Projectdocandcert;

/**
 * 办件通知书及电子证照调用对应的后台service实现类
 * 
 * @author shibin
 * @version [版本号, 2019-07-23 14:21:12]
 */
@Component
@Service
public class ProjectdocandcertServiceImpl implements IProjectdocandcertService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Projectdocandcert record) {
        return new ProjectdocandcertService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ProjectdocandcertService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Projectdocandcert record) {
        return new ProjectdocandcertService().update(record);
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
    public Projectdocandcert find(Object primaryKey) {
        return new ProjectdocandcertService().find(primaryKey);
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
    public Projectdocandcert find(String sql, Object... args) {
        return new ProjectdocandcertService().find(args);
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
    public List<Projectdocandcert> findList(String sql, Object... args) {
        return new ProjectdocandcertService().findList(sql, args);
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
    public List<Projectdocandcert> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ProjectdocandcertService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public Projectdocandcert getInfoByProjectGuid(String projectGuid) {
        return new ProjectdocandcertService().getInfoByProjectGuid(projectGuid);
    }

    @Override
    public Record getProjectAndTaskinfo(String projectGuid) {
        return new ProjectdocandcertService().getProjectAndTaskinfo(projectGuid);
    }

}

package com.epoint.wechat.project.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.wechat.project.api.IWeiChatProjectService;

/**
 * 快递信息表对应的后台service实现类
 * 
 * @author Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
@Component
@Service
public class WeiChatProjectServiceImpl implements IWeiChatProjectService
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
    @Override
    public int insert(AuditProject record) {
        return new WeiChatProjectService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new WeiChatProjectService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(AuditProject record) {
        return new WeiChatProjectService().update(record);
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
    @Override
    public AuditProject find(Object primaryKey) {
        return new WeiChatProjectService().find(primaryKey);
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
    @Override
    public AuditProject find(String sql, Object... args) {
        return new WeiChatProjectService().find(args);
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
    @Override
    public List<AuditProject> findList(String sql, Object... args) {
        return new WeiChatProjectService().findList(sql, args);
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
    @Override
    public List<AuditProject> findListByPage(SqlConditionUtil sqlConditionUtil, int pageNumber, int pageSize,
            Object... args) {
        return new WeiChatProjectService().findListByPage(sqlConditionUtil, pageNumber, pageSize, args);
    }

    @Override
    public int getTotalNum(SqlConditionUtil sqlConditionUtil) {
        return new WeiChatProjectService().getTotalNum(sqlConditionUtil);
    }

    @Override
    public AuditProject getAuditProjectByRowGuid(String projectGuid) {
        return new WeiChatProjectService().getAuditProjectByRowGuid(projectGuid);
    }
}

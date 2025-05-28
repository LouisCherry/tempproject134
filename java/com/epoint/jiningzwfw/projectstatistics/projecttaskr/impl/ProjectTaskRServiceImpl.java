package com.epoint.jiningzwfw.projectstatistics.projecttaskr.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.IProjectTaskRService;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.entity.ProjectTaskR;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 每日办件事项记录表对应的后台service实现类
 * 
 * @author yangyi
 * @version [版本号, 2021-07-01 09:24:03]
 */
@Component
@Service
public class ProjectTaskRServiceImpl implements IProjectTaskRService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectTaskR record) {
        return new ProjectTaskRService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ProjectTaskRService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ProjectTaskR record) {
        return new ProjectTaskRService().update(record);
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
    public ProjectTaskR find(Object primaryKey) {
       return new ProjectTaskRService().find(primaryKey);
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
    public ProjectTaskR find(String sql, Object... args) {
        return new ProjectTaskRService().find(sql,args);
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
    public List<ProjectTaskR> findList(String sql, Object... args) {
       return new ProjectTaskRService().findList(sql,args);
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
    public List<ProjectTaskR> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ProjectTaskRService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countProjectTaskR(String sql, Object... args){
        return new ProjectTaskRService().countProjectTaskR(sql, args);
    }

    @Override
    public AuditCommonResult<List<ProjectTaskR>> findListByCondition(Map<String, String> map) {
        return new ProjectTaskRService().findListByCondition(map);
    }

}

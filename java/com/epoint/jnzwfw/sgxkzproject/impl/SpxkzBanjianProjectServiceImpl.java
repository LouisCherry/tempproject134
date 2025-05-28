package com.epoint.jnzwfw.sgxkzproject.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.sgxkzproject.api.ISpxkzBanjianProjectService;
import com.epoint.jnzwfw.sgxkzproject.api.entity.SpxkzBanjianProject;
/**
 * 竣工信息表对应的后台service实现类
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@Component
@Service
public class SpxkzBanjianProjectServiceImpl implements ISpxkzBanjianProjectService
{
    /**
     * 
     */
    private static final long serialVersionUID = 9004347640464422463L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpxkzBanjianProject record) {
        return new SpxkzBanjianProjectService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpxkzBanjianProjectService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpxkzBanjianProject record) {
        return new SpxkzBanjianProjectService().update(record);
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
    public SpxkzBanjianProject find(Object primaryKey) {
       return new SpxkzBanjianProjectService().find(primaryKey);
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
    public SpxkzBanjianProject find(String sql, Object... args) {
        return new SpxkzBanjianProjectService().find(args);
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
    public List<SpxkzBanjianProject> findList(String sql, Object... args) {
       return new SpxkzBanjianProjectService().findList(sql,args);
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
    public List<SpxkzBanjianProject> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new SpxkzBanjianProjectService().findList(sql,pageNumber,pageSize,args);
    }
    
    public SpxkzBanjianProject getRecordBy(String projectguid){
        return new SpxkzBanjianProjectService().getRecordBy(projectguid);
    }
    
    public List<Record> getSpxkzProject(){
        return new SpxkzBanjianProjectService().getSpxkzProject();
    }
    
    public int updateflag(String projectguid){
        return new SpxkzBanjianProjectService().updateflag(projectguid);
    }
    
    public int updateflagLose(String projectguid){
        return new SpxkzBanjianProjectService().updateflagLose(projectguid);
    }

}

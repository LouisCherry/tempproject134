package com.epoint.jnzwfw.jntask.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jnzwfw.jntask.api.IJnTaskService;
import com.epoint.jnzwfw.jntask.api.entity.JnTask;
/**
 * 竣工信息表对应的后台service实现类
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@Component
@Service
public class JnTaskServiceImpl implements IJnTaskService
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
    public int insert(JnTask record) {
        return new JnTaskService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnTaskService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTask record) {
        return new JnTaskService().update(record);
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
    public JnTask find(Object primaryKey) {
       return new JnTaskService().find(primaryKey);
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
    public JnTask find(String sql, Object... args) {
        return new JnTaskService().find(args);
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
    public List<FrameOu> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnTaskService().findList(sql,pageNumber,pageSize,args);
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
    public List<AuditTask> findList(String sql) {
       return new JnTaskService().findList(sql);
    }
    
    public int findListCount(String sql, Object... args) {
    	return new JnTaskService().findListCount(sql,args);
    }
    
    public JnTask getTaskByItemId(String itemid){
        return new JnTaskService().getTaskByItemId(itemid);
    }
    
    public List<Record> getTaskList(){
        return new JnTaskService().getTaskList();
    }
    
    public int updateflag(String itemid){
        return new JnTaskService().updateflag(itemid);
    }
    
    public List<SpglDfxmsplcjdsxxxb> getNewVersionByItemid(String itemid){
        return new JnTaskService().getNewVersionByItemid(itemid);
    }

}

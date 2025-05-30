package com.epoint.xmz.xmztaskguideconfig.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.xmz.xmztaskguideconfig.api.IXmzTaskguideConfigService;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 事项指南配置表对应的后台service实现类
 * 
 * @author xczheng0314
 * @version [版本号, 2023-03-21 11:38:55]
 */
@Component
@Service
public class XmzTaskguideConfigServiceImpl implements IXmzTaskguideConfigService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmzTaskguideConfig record) {
        return new XmzTaskguideConfigService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new XmzTaskguideConfigService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmzTaskguideConfig record) {
        return new XmzTaskguideConfigService().update(record);
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
    public XmzTaskguideConfig find(Object primaryKey) {
       return new XmzTaskguideConfigService().find(primaryKey);
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
    public XmzTaskguideConfig find(String sql, Object... args) {
        return new XmzTaskguideConfigService().find(sql,args);
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
    public List<XmzTaskguideConfig> findList(String sql, Object... args) {
       return new XmzTaskguideConfigService().findList(sql,args);
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
    public List<XmzTaskguideConfig> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new XmzTaskguideConfigService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countXmzTaskguideConfig(String sql, Object... args){
        return new XmzTaskguideConfigService().countXmzTaskguideConfig(sql, args);
    }

    @Override
    public List<XmzTaskguideConfig> selectTaskGuidefigList(Map<String, String> map, int pagenum, int pagesize) {
        return new XmzTaskguideConfigService().selectTaskGuidefigList(map,pagenum,pagesize);
    }

    @Override
    public List<AuditTask> selectTaskList(String areacode) {
        return new XmzTaskguideConfigService().selectTaskList(areacode);
    }

    @Override
    public XmzTaskguideConfig getXmzTaskguidByTaskId(String task_id) {
        return new XmzTaskguideConfigService().getXmzTaskguidByTaskId(task_id);
    }

}

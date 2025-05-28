package com.epoint.cs.tsproject.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.cs.tsproject.api.entity.TsProject;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.cs.tsproject.api.ITsProjectService;
/**
 * 推送数据对应的后台service实现类
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@Component
@Service
public class TsProjectServiceImpl implements ITsProjectService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(TsProject record) {
        return new TsProjectService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new TsProjectService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(TsProject record) {
        return new TsProjectService().update(record);
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
    public TsProject find(Object primaryKey) {
       return new TsProjectService().find(primaryKey);
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
    public TsProject find(String sql, Object... args) {
        return new TsProjectService().find(args);
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
    public List<TsProject> findList(String sql, Object... args) {
       return new TsProjectService().findList(sql,args);
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
    public List<TsProject> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new TsProjectService().findList(sql,pageNumber,pageSize,args);
    }

}

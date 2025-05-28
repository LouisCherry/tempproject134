package com.epoint.takan.kanyanproject.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 勘验项目对应的后台service实现类
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-19 21:25:37]
 */
@Component
@Service
public class KanyanprojectServiceImpl implements IKanyanprojectService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Kanyanproject record) {
        return new KanyanprojectService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new KanyanprojectService().deleteByGuid(guid);
    }

    @Override
    public int deleteByMainGuid(String guid) {
        return new KanyanprojectService().deleteByMainGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Kanyanproject record) {
        return new KanyanprojectService().update(record);
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
    public Kanyanproject find(Object primaryKey) {
       return new KanyanprojectService().find(primaryKey);
    }

    @Override
    public Kanyanproject find(Map<String, Object> conditionMap) {
        return new KanyanprojectService().find(conditionMap);
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
    public Kanyanproject find(String sql, Object... args) {
        return new KanyanprojectService().find(sql,args);
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
    public List<Kanyanproject> findList(String sql, Object... args) {
       return new KanyanprojectService().findList(sql,args);
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
    public List<Kanyanproject> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new KanyanprojectService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countKanyanproject(String sql, Object... args){
        return new KanyanprojectService().countKanyanproject(sql, args);
    }

}

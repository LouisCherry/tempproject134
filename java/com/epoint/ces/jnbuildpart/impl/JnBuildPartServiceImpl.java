package com.epoint.ces.jnbuildpart.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;
/**
 * 建筑业企业资质数据库对应的后台service实现类
 * 
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
@Component
@Service
public class JnBuildPartServiceImpl implements IJnBuildPartService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnBuildPart record) {
        return new JnBuildPartService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnBuildPartService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnBuildPart record) {
        return new JnBuildPartService().update(record);
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
    public JnBuildPart find(Object primaryKey) {
       return new JnBuildPartService().find(primaryKey);
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
    public JnBuildPart find(String sql, Object... args) {
        return new JnBuildPartService().find(sql,args);
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
    public List<JnBuildPart> findList(String sql, Object... args) {
       return new JnBuildPartService().findList(sql,args);
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
    public List<JnBuildPart> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnBuildPartService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countJnBuildPart(String sql, Object... args){
        return new JnBuildPartService().countJnBuildPart(sql, args);
    }
     
     public List<JnBuildPart> getJnBuildPartByCode(String code) {
         return new JnBuildPartService().getJnBuildPartByCode(code);
     }

}

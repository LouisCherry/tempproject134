package com.epoint.mq.spgl.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.mq.spgl.api.IJnOptionInService;
import com.epoint.mq.spgl.api.entity.JnOptionIn;
/**
 * 成品油零售经营企业库对应的后台service实现类
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@Component
@Service
public class JnOptionInServiceImpl implements IJnOptionInService
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4663726787998989467L;

	/**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnOptionIn record) {
        return new JnOptionInService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnOptionInService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnOptionIn record) {
        return new JnOptionInService().update(record);
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
    public JnOptionIn find(Object primaryKey) {
       return new JnOptionInService().find(primaryKey);
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
    public JnOptionIn find(String sql, Object... args) {
        return new JnOptionInService().find(sql,args);
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
    public List<JnOptionIn> findList(String sql, Object... args) {
       return new JnOptionInService().findList(sql,args);
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
    public List<JnOptionIn> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnOptionInService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countJnOptionIn(String sql, Object... args){
        return new JnOptionInService().countJnOptionIn(sql, args);
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
     public List<JnOptionIn> getJnOptionInByXmdm(String xmdm) {
        return new JnOptionInService().getJnOptionInByXmdm(xmdm);
     }

}

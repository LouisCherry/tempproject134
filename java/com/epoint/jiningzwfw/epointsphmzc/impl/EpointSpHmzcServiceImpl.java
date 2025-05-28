package com.epoint.jiningzwfw.epointsphmzc.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.epointsphmzc.api.IEpointSpHmzcService;
import com.epoint.jiningzwfw.epointsphmzc.api.entity.EpointSpHmzc;
/**
 * 惠企政策库对应的后台service实现类
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
@Component
@Service
public class EpointSpHmzcServiceImpl implements IEpointSpHmzcService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EpointSpHmzc record) {
        return new EpointSpHmzcService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new EpointSpHmzcService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(EpointSpHmzc record) {
        return new EpointSpHmzcService().update(record);
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
    public EpointSpHmzc find(Object primaryKey) {
       return new EpointSpHmzcService().find(primaryKey);
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
    public EpointSpHmzc find(String sql, Object... args) {
        return new EpointSpHmzcService().find(args);
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
    public List<EpointSpHmzc> findList(String sql, Object... args) {
       return new EpointSpHmzcService().findList(sql,args);
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
    public List<EpointSpHmzc> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new EpointSpHmzcService().findList(sql,pageNumber,pageSize,args);
    }
    

    public List<EpointSpHmzc> findList(int pageNumber, int pageSize, String qybq, String jnhygm, String wwsmzq,
            String zcmc, String sfsxqy, String ssbm, String ouguids) {
        return new EpointSpHmzcService().findList(pageNumber, pageSize, qybq, jnhygm, wwsmzq, zcmc, sfsxqy, ssbm,ouguids);
    }

    public int findListCount(String qybq, String jnhygm, String wwsmzq, String zcmc,
            String sfsxqy, String ssbm, String ouguids) {
        return new EpointSpHmzcService().findListCount(qybq, jnhygm, wwsmzq, zcmc, sfsxqy, ssbm, ouguids);
    }
    
    public List<Record> getOuListByHmzc(String areacode) {
        return new EpointSpHmzcService().getOuListByHmzc(areacode);
    }
    
    public List<Record> getOuList(String areacode) {
        return new EpointSpHmzcService().getOuList(areacode);
    }

}

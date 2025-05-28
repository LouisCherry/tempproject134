package com.epoint.xmz.zjcreidtquery.impl;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jn.inproject.impl.WebUploaderService;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;
/**
 * 信用查询调用统计表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@Component
@Service
public class ZjCreidtqueryServiceImpl implements IZjCreidtqueryService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ZjCreidtquery record) {
        return new ZjCreidtqueryService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZjCreidtqueryService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ZjCreidtquery record) {
        return new ZjCreidtqueryService().update(record);
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
    public ZjCreidtquery find(Object primaryKey) {
       return new ZjCreidtqueryService().find(primaryKey);
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
    public ZjCreidtquery find(String sql, Object... args) {
        return new ZjCreidtqueryService().find(sql,args);
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
    public List<ZjCreidtquery> findList(String sql, Object... args) {
       return new ZjCreidtqueryService().findList(sql,args);
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
    public List<ZjCreidtquery> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ZjCreidtqueryService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countZjCreidtquery(String sql, Object... args){
        return new ZjCreidtqueryService().countZjCreidtquery(sql, args);
    }
     
     
     public List<Record> finList(int first, int pagesize, Date starttime, Date endtime, String type,String areacode) {
         return new ZjCreidtqueryService().finList(first, pagesize, starttime, endtime,type,areacode) ;
     }
     
     public Integer finTotal(Date starttime, Date endtime, String type,String areacode) {
         return new ZjCreidtqueryService().finTotal( starttime, endtime,type,areacode);
     }

}

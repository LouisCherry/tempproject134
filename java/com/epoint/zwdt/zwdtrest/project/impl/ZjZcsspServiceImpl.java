package com.epoint.zwdt.zwdtrest.project.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.zwdtrest.project.api.IZjZcsspService;
import com.epoint.zwdt.zwdtrest.project.api.entity.ZjZcssp;
import com.epoint.zwdt.zwdtrest.user.auditonlinechooseenterprise;
/**
 * 邹城随手拍表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2020-10-10 11:34:40]
 */
@Component
@Service
public class ZjZcsspServiceImpl implements IZjZcsspService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ZjZcssp record) {
        return new ZjZcsspService().insert(record);
    }
    public int insert(auditonlinechooseenterprise record) {
    	return new ZjZcsspService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZjZcsspService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ZjZcssp record) {
        return new ZjZcsspService().update(record);
    }
    
    public int update(auditonlinechooseenterprise record) {
    	return new ZjZcsspService().update(record);
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
    public ZjZcssp find(Object primaryKey) {
       return new ZjZcsspService().find(primaryKey);
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
    public ZjZcssp find(String sql, Object... args) {
        return new ZjZcsspService().find(sql,args);
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
    public List<ZjZcssp> findList(String sql, Object... args) {
       return new ZjZcsspService().findList(sql,args);
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
    public List<ZjZcssp> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ZjZcsspService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countZjZcssp(String sql, Object... args){
        return new ZjZcsspService().countZjZcssp(sql, args);
    }
     
     public FrameAttachInfo getFrameAttachByCliengguid(String cliengguid){
     	return new ZjZcsspService().getFrameAttachByCliengguid(cliengguid);
     }
     
     public auditonlinechooseenterprise getTerpriseByRowguid(String rowguid){
      	return new ZjZcsspService().getTerpriseByRowguid(rowguid);
      }
     

}

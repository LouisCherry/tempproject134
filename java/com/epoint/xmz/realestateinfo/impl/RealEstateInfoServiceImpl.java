package com.epoint.xmz.realestateinfo.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.realestateinfo.api.IRealEstateInfoService;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;
/**
 * 楼盘信息表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
@Component
@Service
public class RealEstateInfoServiceImpl implements IRealEstateInfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RealEstateInfo record) {
        return new RealEstateInfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new RealEstateInfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(RealEstateInfo record) {
        return new RealEstateInfoService().update(record);
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
    public RealEstateInfo find(Object primaryKey) {
       return new RealEstateInfoService().find(primaryKey);
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
    public RealEstateInfo find(String sql, Object... args) {
        return new RealEstateInfoService().find(sql,args);
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
    public List<RealEstateInfo> findList(String sql, Object... args) {
       return new RealEstateInfoService().findList(sql,args);
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
    public List<RealEstateInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new RealEstateInfoService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countRealEstateInfo(String sql, Object... args){
        return new RealEstateInfoService().countRealEstateInfo(sql, args);
    }
     
     /**
      * 分页查找一个list
      *
      * @param conditionMap 查询条件集合
      * @param pageNumber   记录行的偏移量
      * @param pageSize     记录行的最大数目
      * @return T extends BaseEntity
      */
     @Override
     public PageData<RealEstateInfo> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
         return new RealEstateInfoService().paginatorList(conditionMap, pageNumber, pageSize);
     }

}

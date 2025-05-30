package com.epoint.zwdt.xmgxh.basic.spglv3.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglJsgcxfsjscxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglJsgcxfsjscxxbV3Service;
import com.epoint.zwdt.xmgxh.basic.spglv3.service.SpglJsgcxfsjscxxbV3Service;
/**
 * 建设工程消防设计审查信息表对应的后台service实现类
 * 
 * @author Epoint
 * @version [版本号, 2023-09-25 14:00:20]
 */
@Component
@Service
public class SpglJsgcxfsjscxxbV3ServiceImpl implements ISpglJsgcxfsjscxxbV3Service
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglJsgcxfsjscxxbV3 record) {
        return new SpglJsgcxfsjscxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglJsgcxfsjscxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglJsgcxfsjscxxbV3 record) {
        return new SpglJsgcxfsjscxxbV3Service().update(record);
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
    public SpglJsgcxfsjscxxbV3 find(Object primaryKey) {
       return new SpglJsgcxfsjscxxbV3Service().find(primaryKey);
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
    public SpglJsgcxfsjscxxbV3 find(String sql, Object... args) {
        return new SpglJsgcxfsjscxxbV3Service().find(sql,args);
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
    public List<SpglJsgcxfsjscxxbV3> findList(String sql, Object... args) {
       return new SpglJsgcxfsjscxxbV3Service().findList(sql,args);
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
    public List<SpglJsgcxfsjscxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new SpglJsgcxfsjscxxbV3Service().findList(sql,pageNumber,pageSize,args);
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
    public Integer countSpglJsgcxfsjscxxbV3(String sql, Object... args){
        return new SpglJsgcxfsjscxxbV3Service().countSpglJsgcxfsjscxxbV3(sql, args);
    }
     
     /**
      * 查询单个实体
      */
    @Override
    public SpglJsgcxfsjscxxbV3 findDominByCondition(String xzqhdm, String gcdm, String spsxslbm) {
        return new SpglJsgcxfsjscxxbV3Service().findDominByCondition(xzqhdm,gcdm,spsxslbm);
    }
    
    /**
     * 跳过验证更新
     */
    @Override
    public int updateNoValidate(SpglJsgcxfsjscxxbV3 record) {
        return new SpglJsgcxfsjscxxbV3Service().update(record);
    }

    /**
     * 跳过验证保存
     */
    @Override
    public int insertNoValidate(SpglJsgcxfsjscxxbV3 record) {
        return new SpglJsgcxfsjscxxbV3Service().insert(record);
    }

}

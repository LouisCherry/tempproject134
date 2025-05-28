package com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.entity.SpglGcspxtjbxxbEdit;

import java.util.List;


/**
 * 上报工改系统基本信息表对应的后台service
 * 
 * @author lzhming
 * @version [版本号, 2023-08-31 15:33:35]
 */
public class SpglGcspxtjbxxbEditService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglGcspxtjbxxbEditService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 

     * @return int
     */
    public int insert(SpglGcspxtjbxxbEdit record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 

     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglGcspxtjbxxbEdit.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 

     * @return int
     */
    public int update(SpglGcspxtjbxxbEdit record) {
        return baseDao.update(record);
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
    public SpglGcspxtjbxxbEdit find(Object primaryKey) {
        return baseDao.find(SpglGcspxtjbxxbEdit.class, primaryKey);
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
    public SpglGcspxtjbxxbEdit find(String sql,  Object... args) {
        return baseDao.find(sql, SpglGcspxtjbxxbEdit.class, args);
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
    public List<SpglGcspxtjbxxbEdit> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglGcspxtjbxxbEdit.class, args);
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
    public List<SpglGcspxtjbxxbEdit> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglGcspxtjbxxbEdit.class, args);
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
    public Integer countSpglGcspxtjbxxb(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public List<SpglGcspxtjbxxbEdit> getListByXq(String xzqhdm) {
        String sql = "select * from SPGL_GCSPXTJBXXB_EDIT where xzqhdm = ? and sync='1' ";
        return baseDao.findList(sql,SpglGcspxtjbxxbEdit.class,xzqhdm);
    }

    public Integer IsExistXzqhdm(String xzqhdm) {
        String sql = "select count(*) from SPGL_GCSPXTJBXXB_EDIT where xzqhdm = ? ";
        return baseDao.queryInt(sql,xzqhdm);
    }
}

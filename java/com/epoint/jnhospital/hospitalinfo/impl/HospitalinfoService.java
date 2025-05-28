package com.epoint.jnhospital.hospitalinfo.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jnhospital.hospitalinfo.api.entity.Hospitalinfo;

/**
 * 定点医院名单对应的后台service
 * 
 * @author JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
public class HospitalinfoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public HospitalinfoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Hospitalinfo record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(Hospitalinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Hospitalinfo record) {
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
    public Hospitalinfo find(Object primaryKey) {
        return baseDao.find(Hospitalinfo.class, primaryKey);
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
    public Hospitalinfo find(String sql, Object... args) {
        return baseDao.find(sql, Hospitalinfo.class, args);
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
    public List<Hospitalinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, Hospitalinfo.class, args);
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
    public List<Hospitalinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Hospitalinfo.class, args);
    }

    public List<Record> getHospitalListByName(String hospital_name, int pageIndex, int pageSize) {
        String sql="select hospital_id,hospital_name,hospital_level,hospital_grade,hospital_type from hospitalinfo where hospital_name like ? ";
        String selectName="%"+hospital_name+"%";
        return baseDao.findList(sql, pageIndex*pageSize, pageSize, Record.class, selectName);
    }

    public int getCountByName(String hospital_name) {
        String sql="select count(*) from hospitalinfo where hospital_name like ? ";
        String selectName="%"+hospital_name+"%";
        return baseDao.queryInt(sql, selectName);
    }

    public List<Record> getHospitalList(int pageIndex, int pageSize) {
        String sql="select hospital_id,hospital_name,hospital_level,hospital_grade,hospital_type from hospitalinfo ";
        return baseDao.findList(sql, pageIndex*pageSize, pageSize, Record.class);
    }

    public int getCount() {
        String sql="select count(*) from hospitalinfo ";
        return baseDao.queryInt(sql);
    }

}

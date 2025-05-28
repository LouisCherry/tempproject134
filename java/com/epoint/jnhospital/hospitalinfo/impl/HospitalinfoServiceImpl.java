package com.epoint.jnhospital.hospitalinfo.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jnhospital.hospitalinfo.api.IHospitalinfoService;
import com.epoint.jnhospital.hospitalinfo.api.entity.Hospitalinfo;

/**
 * 定点医院名单对应的后台service实现类
 * 
 * @author JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
@Component
@Service
public class HospitalinfoServiceImpl implements IHospitalinfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Hospitalinfo record) {
        return new HospitalinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new HospitalinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Hospitalinfo record) {
        return new HospitalinfoService().update(record);
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
        return new HospitalinfoService().find(primaryKey);
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
        return new HospitalinfoService().find(args);
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
        return new HospitalinfoService().findList(sql, args);
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
        return new HospitalinfoService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public List<Record> getHospitalListByName(String hospital_name, int pageIndex, int pageSize) {
        return new HospitalinfoService().getHospitalListByName(hospital_name, pageIndex, pageSize);
    }

    @Override
    public int getCountByName(String hospital_name) {
        return new HospitalinfoService().getCountByName(hospital_name);
    }

    @Override
    public List<Record> getHospitalList(int pageIndex, int pageSize) {
        return new HospitalinfoService().getHospitalList(pageIndex,pageSize);
    }

    @Override
    public int getCount() {
        return new HospitalinfoService().getCount();
    }

}

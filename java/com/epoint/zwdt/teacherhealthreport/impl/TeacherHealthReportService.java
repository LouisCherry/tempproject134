package com.epoint.zwdt.teacherhealthreport.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zwdt.teacherhealthreport.api.entity.TeacherHealthReport;

/**
 * 教师资格体检报告对应的后台service
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
public class TeacherHealthReportService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public TeacherHealthReportService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(TeacherHealthReport record) {
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
        T t = baseDao.find(TeacherHealthReport.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(TeacherHealthReport record) {
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
    public TeacherHealthReport find(Object primaryKey) {
        return baseDao.find(TeacherHealthReport.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public TeacherHealthReport find(String sql, Object... args) {
        return baseDao.find(sql, TeacherHealthReport.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<TeacherHealthReport> findList(String sql, Object... args) {
        return baseDao.findList(sql, TeacherHealthReport.class, args);
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
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<TeacherHealthReport> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, TeacherHealthReport.class, args);
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
    public Integer countTeacherHealthReport(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 根据条件查询教师体检报告记录
     * 
     * @param map
     * @return
     */
    public TeacherHealthReport getTeacherHealthReportByCondition(Map<String, String> map) {
        List<String> params = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from teacher_health_report where 1=1 ");
        if (map != null && !map.isEmpty()) {
            sql.append(new SQLManageUtil().buildPatchSql(map, params));
        }
        return baseDao.find(sql.toString(), TeacherHealthReport.class, params.toArray());
    }
}

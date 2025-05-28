package com.epoint.evainstanceck.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;

/**
 * 好差评信息表对应的后台service
 * 
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
public class EvainstanceCkService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public EvainstanceCkService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EvainstanceCk record) {
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
        T t = baseDao.find(EvainstanceCk.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(EvainstanceCk record) {
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
    public EvainstanceCk find(Object primaryKey) {
        return baseDao.find(EvainstanceCk.class, primaryKey);
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
    public EvainstanceCk find(String sql, Object... args) {
        return baseDao.find(sql, EvainstanceCk.class, args);
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
    public List<EvainstanceCk> findList(String sql, Object... args) {
        return baseDao.findList(sql, EvainstanceCk.class, args);
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
    public List<EvainstanceCk> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, EvainstanceCk.class, args);
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
    public Integer countEvainstanceCk(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
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
    public Record findByflowsn(String flowsn) {
        String sql = " select * from evainstance_ck where projectno = ? order by satisfaction desc limit 1";
        return baseDao.find(sql,Record.class,flowsn);
    }

    public List<Record> findListByouguidTemp(String leftTreeNodeGuid, Date createdate, String evalevel, String iszg,
            String RECEIVEUSERNAM, int first, int pageSize) {
        List<Object> params = new ArrayList<Object>();

        String sql = "select a.*,ifnull(a.iszg,'0')  iszg,b.RECEIVEUSERNAME,b.WINDOWNAME from EVAINSTANCE_CK_temp a inner join audit_project_temp b on substring(a.projectNO,3)  =  b.flowsn where a.deptcode = b.ouguid  and   a.pf = '4' and a.Evalevel < 4 and a.Createdate > '2023-01-01 00:00:00' ";
        if (createdate != null) {
            sql += " and a.createdate between ? and ?  ";
            params.add(EpointDateUtil.convertDate2String(createdate, "yyyy-MM-dd") + " 00:00:00");
            params.add(EpointDateUtil.convertDate2String(createdate, "yyyy-MM-dd") + " 23:59:59");

        }
        if (StringUtil.isNotBlank(evalevel)) {
            sql += " and a.evalevel = ?  ";
            params.add(evalevel);
        }
        if (StringUtil.isNotBlank(iszg)) {
            sql += " and ifnull(a.iszg,'0') = ? ";
            params.add(iszg);
        }
        if (StringUtil.isNotBlank(RECEIVEUSERNAM)) {
            sql += " and b.RECEIVEUSERNAME = ? ";
            params.add(RECEIVEUSERNAM);
        }

        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
            sql += " and a.areacode = ? ";
            params.add(leftTreeNodeGuid);
        }
        sql += "order by  a.Createdate desc ";
        return baseDao.findList(sql, first, pageSize, Record.class, params.toArray());
    }

    public int countListByouguidTemp(String leftTreeNodeGuid, Date createdate, String evalevel, String iszg,
            String RECEIVEUSERNAM) {
        List<Object> params = new ArrayList<Object>();

        String sql = "select count(1) from EVAINSTANCE_CK_temp a inner join audit_project_temp b on substring(a.projectNO,3)  = b.flowsn where  a.deptcode = b.ouguid  and a.pf = '4' and a.Evalevel < 4 and a.Createdate > '2023-01-01 00:00:00' ";

        if (createdate != null) {
            sql += " and a.createdate = ? ";
            params.add(createdate);
        }
        if (StringUtil.isNotBlank(evalevel)) {
            sql += " and a.evalevel = ?  ";
            params.add(evalevel);
        }
        if (StringUtil.isNotBlank(iszg)) {
            sql += " and ifnull(a.iszg,'0') = ? ";
            params.add(iszg);
        }
        if (StringUtil.isNotBlank(RECEIVEUSERNAM)) {
            sql += " and b.RECEIVEUSERNAME = ? ";
            params.add(RECEIVEUSERNAM);
        }

        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
            sql += " and a.deptcode = ? order by  a.Createdate desc ;";
            params.add(leftTreeNodeGuid);
        }

        return baseDao.queryInt(sql, params.toArray());
    }
}

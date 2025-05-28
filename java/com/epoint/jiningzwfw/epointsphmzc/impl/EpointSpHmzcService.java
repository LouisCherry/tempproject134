package com.epoint.jiningzwfw.epointsphmzc.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jiningzwfw.epointsphmzc.api.entity.EpointSpHmzc;

/**
 * 惠企政策库对应的后台service
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
public class EpointSpHmzcService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public EpointSpHmzcService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EpointSpHmzc record) {
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
        T t = baseDao.find(EpointSpHmzc.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(EpointSpHmzc record) {
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
    public EpointSpHmzc find(Object primaryKey) {
        return baseDao.find(EpointSpHmzc.class, primaryKey);
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
    public EpointSpHmzc find(String sql,  Object... args) {
        return baseDao.find(sql, EpointSpHmzc.class, args);
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
        return baseDao.findList(sql, EpointSpHmzc.class, args);
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
    public List<EpointSpHmzc> findList(int pageNumber, int pageSize, String qybq, String jnhygm,  String wwsmzq, String zcmc, String sfsxqy, String ssbm, String ouguids) {
        String sql = "select * from epoint_sp_hmzc where 1=1 ";
        if (StringUtil.isNotBlank(ouguids)) {
            sql += " and ssbm in " + ouguids + " ";
        }
        if (StringUtil.isNotBlank(qybq)) {
            sql += " and qybq like '%" + qybq + "%' ";
        }
        if (StringUtil.isNotBlank(jnhygm)) {
            sql += " and jnhygm like '%" + jnhygm + "%' ";
        }
        if (StringUtil.isNotBlank(wwsmzq)) {
            sql += " and wwsmzq like '%" + wwsmzq + "%' ";
        }
        if (StringUtil.isNotBlank(zcmc)) {
            sql += " and zcmc like '%" + zcmc + "%' ";
        }
        if (StringUtil.isNotBlank(sfsxqy)) {
            sql += " and sfsxqy like '%" + sfsxqy + "%' ";
        }
        if (StringUtil.isNotBlank(ssbm)) {
            sql += " and ssbm like '%" + ssbm + "%' ";
        }
        sql += " order by operatedate desc ";
        return baseDao.findList(sql, pageNumber, pageSize, EpointSpHmzc.class);
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
    public int findListCount(String qybq, String jnhygm,  String wwsmzq, String zcmc, String sfsxqy, String ssbm, String ouguids) {
        String sql = "select count(1) from epoint_sp_hmzc where 1=1 ";
        if (StringUtil.isNotBlank(ouguids)) {
            sql += " and ssbm in " + ouguids + " ";
        }
        if (StringUtil.isNotBlank(qybq)) {
            sql += " and qybq like '%" + qybq + "%' ";
        }
        if (StringUtil.isNotBlank(jnhygm)) {
            sql += " and jnhygm like '%" + jnhygm + "%' ";
        }
        if (StringUtil.isNotBlank(wwsmzq)) {
            sql += " and wwsmzq like '%" + wwsmzq + "%' ";
        }
        if (StringUtil.isNotBlank(zcmc)) {
            sql += " and zcmc like '%" + zcmc + "%' ";
        }
        if (StringUtil.isNotBlank(sfsxqy)) {
            sql += " and sfsxqy like '%" + sfsxqy + "%' ";
        }
        if (StringUtil.isNotBlank(ssbm)) {
            sql += " and ssbm like '%" + ssbm + "%' ";
        }
        sql += " order by operatedate desc ";
        return baseDao.queryInt(sql, Integer.class);
    }
    
    /**
     * 获取当前辖区下的所有部门
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
    public List<Record> getOuListByHmzc(String areacode) {
        String sql = "select distinct a.ouguid,a.ouname,a.oushortname from frame_ou a join frame_ou_extendinfo b on a.ouguid = b.ouguid join epoint_sp_hmzc c on c.ssbm = a.ouguid where b.areacode = ?";
        return baseDao.findList(sql, Record.class, areacode);
    }
    
    /**
     * 获取当前辖区下惠民政策的所有部门
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
    public List<Record> getOuList(String areacode) {
        String sql = "select a.ouguid,a.ouname,a.oushortname from frame_ou a join frame_ou_extendinfo b on a.ouguid = b.ouguid  where b.areacode = ?";
        return baseDao.findList(sql, Record.class, areacode);
    }
    
}

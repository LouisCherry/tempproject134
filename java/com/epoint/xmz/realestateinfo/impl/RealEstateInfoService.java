package com.epoint.xmz.realestateinfo.impl;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;

/**
 * 楼盘信息表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
public class RealEstateInfoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public RealEstateInfoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RealEstateInfo record) {
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
        T t = baseDao.find(RealEstateInfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(RealEstateInfo record) {
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
    public RealEstateInfo find(Object primaryKey) {
        return baseDao.find(RealEstateInfo.class, primaryKey);
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
        return baseDao.find(sql, RealEstateInfo.class, args);
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
        return baseDao.findList(sql, RealEstateInfo.class, args);
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
        return baseDao.findList(sql, pageNumber, pageSize, RealEstateInfo.class, args);
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
    public Integer countRealEstateInfo(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<AuditTask> getCertListByProjectNum(String projectnum) {
        String sql = " select a.PROJECTGUID ,b.rowguid,b.ITEM_ID,a.TASKGUID,a.TASKNAME ,d.CERTROWGUID ,e.CERTCLIENGGUID ,a.BIGUID,c.ITEMCODE "
                + " from audit_sp_i_task  a " + " left join audit_task b on a.TASKGUID  = b.rowguid "
                + " left join audit_rs_item_baseinfo c on c.BIGUID = a.biguid "
                + " left join audit_project d on a.PROJECTGUID  = d.RowGuid  "
                + " left join cert_info e on d.CERTROWGUID  =  e.rowguid " + " where 1=1 "
                + " and a.biguid in (select BIGUID from audit_rs_item_baseinfo  where itemcode = ? ) "
                + "  and e.CERTCLIENGGUID is not null  and d.status >= 90 group by a.PROJECTGUID";
        return baseDao.findList(sql, AuditTask.class, projectnum);
    }
}

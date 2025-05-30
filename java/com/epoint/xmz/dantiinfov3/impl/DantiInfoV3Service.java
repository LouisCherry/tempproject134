package com.epoint.xmz.dantiinfov3.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.dantiinfov3.api.entity.DantiInfoV3;

import java.util.List;

/**
 * 项目单体信息表对应的后台service
 *
 * @author ysai
 * @version [版本号, 2023-10-18 10:39:39]
 */
public class DantiInfoV3Service {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public DantiInfoV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(DantiInfoV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(DantiInfoV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(DantiInfoV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public DantiInfoV3 find(Object primaryKey) {
        return baseDao.find(DantiInfoV3.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public DantiInfoV3 find(String sql, Object... args) {
        return baseDao.find(sql, DantiInfoV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, DantiInfoV3.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, DantiInfoV3.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countDantiInfoV3(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<DantiInfoV3> findListByProjectguid(String projectguid) {
        String sql = "select * from danti_info_v3 where itemguid =?";
        return baseDao.findList(sql, DantiInfoV3.class, projectguid);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findListBySubAppguid(String subappguid) {
        String sql =
                "select  t1.*,t2.rowguid rguid  from danti_info_v3 t1, danti_sub_relation t2 where t1.rowguid=t2.DANTIGUID and t2.SUBAPPGUID=? "
                        + "order by t1.GONGCHENGGUID";
        return baseDao.findList(sql, DantiInfoV3.class, subappguid);
    }

    public List<DantiInfoV3> findListByGCguid(String gcguid) {
        String sql = "select * from danti_info_v3 where gongchengguid=?";
        return baseDao.findList(sql, DantiInfoV3.class, gcguid);
    }

    public PageData<DantiInfoV3> pageDantiInfo(String conditionSql, String projectguid, String selectedguid,
                                               String gongchengguid, List<Object> conditionList, int first, int pageSize, String sortField,
                                               String sortOrder) {
        conditionSql += " and itemguid='" + projectguid + "'";
        if (StringUtil.isNotBlank(selectedguid) && StringUtil.isNotBlank(gongchengguid)) {
            conditionSql = conditionSql + " and gongchengguid='" + gongchengguid + "'";
        } else {
            conditionSql = conditionSql + " and (gongchengguid is null or gongchengguid = '') ";
        }
        conditionSql += "order by operatedate desc";
        List<DantiInfoV3> list = findList(
                ListGenerator.generateSql("danti_info_v3", conditionSql, sortField, sortOrder), first, pageSize,
                conditionList.toArray());
        int count = findList("select*from danti_info_V3 where 1=1" + conditionSql, sortField,
                conditionList.toArray()).size();
        return new PageData<DantiInfoV3>(list, count);
    }

    public PageData<DantiInfoV3> pageDantiLisrInfo(String conditionSql, String projectguid, String subappguid,
                                                   String gongchengguid, List<Object> conditionList, int first, int pageSize, String sortField,
                                                   String sortOrder) {
        conditionSql = conditionSql + " and itemguid='" + projectguid
                + "'  and rowguid not in (select dantiguid from danti_sub_relation where subappguid = '" + subappguid
                + "') ";
        if (StringUtil.isNotBlank(gongchengguid)) {
            conditionSql += " and gongchengguid='" + gongchengguid + "'";
        }
        conditionSql += " order by operatedate desc";
        List<DantiInfoV3> list = findList(
                ListGenerator.generateSql("danti_info_v3", conditionSql, sortField, sortOrder), first, pageSize,
                conditionList.toArray());
        int count = findList(ListGenerator.generateSql("danti_info_v3", conditionSql, sortField, sortOrder),
                conditionList.toArray()).size();
        return new PageData<DantiInfoV3>(list, count);
    }
}

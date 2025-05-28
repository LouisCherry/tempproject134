package com.epoint.xmz.kypoint.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.kypoint.api.entity.KyPoint;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * 勘验要点表对应的后台service
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:28:23]
 */
public class KyPointService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public KyPointService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(KyPoint record) {
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
        T t = baseDao.find(KyPoint.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(KyPoint record) {
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
    public KyPoint find(Object primaryKey) {
        return baseDao.find(KyPoint.class, primaryKey);
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
    public KyPoint find(String sql,  Object... args) {
        return baseDao.find(sql, KyPoint.class, args);
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
    public List<KyPoint> findList(String sql, Object... args) {
        return baseDao.findList(sql, KyPoint.class, args);
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
    public List<KyPoint> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, KyPoint.class, args);
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
    public Integer countKyPoint(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public PageData<KyPoint> paginatorList(Map<String, String> conditionMap, int pageNumber,
            int pageSize) {
        List<String> params = new ArrayList<>();
        String sql = new SQLManageUtil().buildSqlComoplete(KyPoint.class, conditionMap, params);
        List<KyPoint> list = baseDao.findList(sql, pageNumber, pageSize,
                KyPoint.class, params.toArray());
        int count = list.size();
        return new PageData<KyPoint>(list, count);
    }
    public String getPointByProjectguid(String projectguid) {
        String sql = "select * from ky_point where kyguid = ?";
        StringBuilder result = new StringBuilder();
        List<KyPoint> KyPoints = baseDao.findList(sql, KyPoint.class, projectguid);
        int i =0;
        for (KyPoint point : KyPoints) {
            i++;
            String kypoint = point.getKypoint();
            result.append(i+"、"+kypoint);
        }
        return result.toString();
    }
}

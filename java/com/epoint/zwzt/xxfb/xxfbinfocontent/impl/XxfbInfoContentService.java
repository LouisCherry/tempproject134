package com.epoint.zwzt.xxfb.xxfbinfocontent.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.entity.XxfbInfoContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信息正文内容表对应的后台service
 *
 * @author D0Be
 * @version [版本号, 2022-04-28 14:01:44]
 */
public class XxfbInfoContentService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public XxfbInfoContentService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XxfbInfoContent record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(XxfbInfoContent.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XxfbInfoContent record) {
        return baseDao.update(record);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    public Integer countXxfbInfoContent(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        SqlConditionUtil conditionUtil = new SqlConditionUtil(getSqlCondition(conditionMap));
        conditionUtil.setSelectFields("count(*)");
        String sql = new SqlHelper().getSqlComplete(XxfbInfoContent.class, conditionUtil.getMap(), params);
        return baseDao.queryInt(sql, params.toArray());
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public XxfbInfoContent find(Object primaryKey) {
        return baseDao.find(XxfbInfoContent.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public XxfbInfoContent find(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        String sql = new SqlHelper().getSqlComplete(XxfbInfoContent.class, conditionMap, params);
        return baseDao.find(sql, XxfbInfoContent.class, params.toArray());
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    public List<XxfbInfoContent> findList(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        String sql = new SqlHelper().getSqlComplete(XxfbInfoContent.class, conditionMap, params);
        return baseDao.findList(sql, XxfbInfoContent.class, params.toArray());
    }

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<XxfbInfoContent> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        List<Object> params = new ArrayList<>();
        String sql = new SqlHelper().getSqlComplete(XxfbInfoColumn.class, conditionMap, params);
        List<XxfbInfoContent> list = baseDao.findList(sql, pageNumber, pageSize, XxfbInfoContent.class,
                params.toArray());
        int count = countXxfbInfoContent(conditionMap);
        return new PageData<XxfbInfoContent>(list, count);
    }

    /**
     * 拼参数
     *
     * @param conditionMap 查询条件集合
     * @return List<Object>
     */
    private List<Object> getParams(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
            params.add(entry.getValue());
        }
        return params;
    }

    public Map<String, String> getSqlCondition(Map<String, Object> conditionMap) {
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : conditionMap.entrySet()) {
            map.put(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
        }
        return map;
    }

}

package com.epoint.basic.spgl.service;

import com.epoint.basic.spgl.domain.SpglDfxmsplcjdxxb;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地方项目审批流程阶段信息表对应的后台service
 *
 * @author fenglin
 * @version [版本号, 2019-06-18 15:42:35]
 */
public class SpglDfxmsplcjdxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglDfxmsplcjdxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglDfxmsplcjdxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglDfxmsplcjdxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglDfxmsplcjdxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglDfxmsplcjdxxb find(Object primaryKey) {
        return baseDao.find(SpglDfxmsplcjdxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglDfxmsplcjdxxb find(String sql, Object... args) {
        return baseDao.find(sql, SpglDfxmsplcjdxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglDfxmsplcjdxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglDfxmsplcjdxxb.class, args);
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
    public List<SpglDfxmsplcjdxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglDfxmsplcjdxxb.class, args);
    }

    /**
     * 根据审批流程信息和行政区划查找审批流程阶段
     * [功能详细描述]
     *
     * @param xzqhdm
     * @param splcbm
     * @param splcbbh
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<SpglDfxmsplcjdxxb> findByXzqhAndSplc(String xzqhdm, String splcbm, String splcbbh) {
        String sql = "select * from spgl_dfxmsplcjdxxb where xzqhdm = ? and splcbm = ? and splcbbh = ? order by spjdxh asc";
        return baseDao.findList(sql, SpglDfxmsplcjdxxb.class, xzqhdm, splcbm, splcbbh);
    }

    /**
     * 根据审批流程编码和审批阶段编码查找审批流程阶段信息
     * [功能详细描述]
     *
     * @param splcbm
     * @param splcbbh
     * @param xzqhdm
     * @param spjdbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public SpglDfxmsplcjdxxb findBYSplcbmAndSpjdbm(String splcbm, String splcbbh, String xzqhdm, String spjdbm) {
        String sql = "select * from Spgl_Dfxmsplcjdxxb where splcbm = ? and splcbbh = ? and xzqhdm = ? and spjdxh = ? ";
        return baseDao.find(sql, SpglDfxmsplcjdxxb.class, splcbm, splcbbh, xzqhdm, spjdbm);
    }

    public void deleteBySpGuid(String guid) {
        Entity en = SpglDfxmsplcjdxxb.class.getAnnotation(Entity.class);
        String sql = "delete from " + en.table() + " where SPLCBM = ?";
        baseDao.execute(sql, guid);
    }

    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     *
     * @param baseClass
     * @param useCache
     * @return
     */
    public List<SpglDfxmsplcjdxxb> getAllRecord(Class<? extends BaseEntity> baseClass,
                                                Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<SpglDfxmsplcjdxxb> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        } else {
            return 0;
        }

    }

    /**
     * 获取分页数据
     *
     * @param baseClass
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<SpglDfxmsplcjdxxb> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                          Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    public String getPhaseName(String splcbm, Double splcbbh, String xzqhdm, int spjdxh) {
        String sql = "select spjdmc from spgl_dfxmsplcjdxxb where splcbm = ? and splcbbh = ? and xzqhdm = ? and spjdxh = ?";
        List<Object> list = new ArrayList<>();
        list.add(splcbm);
        list.add(splcbbh);
        list.add(xzqhdm);
        list.add(spjdxh);
        return baseDao.find(sql, String.class, list.toArray());
    }

}

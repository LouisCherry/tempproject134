package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcxxb;

import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程信息表对应的后台service
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:56]
 */
public class SpglsplcxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglsplcxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Spglsplcxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <Record> int deleteByGuid(String guid) {
        Spglsplcxxb t = baseDao.find("", Spglsplcxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Spglsplcxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return SpglDfxmsplcxxb extends BaseEntity
     */
    public Spglsplcxxb find(Object primaryKey) {
        return baseDao.find(Spglsplcxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return SpglDfxmsplcxxb {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public Spglsplcxxb find(String sql, Object... args) {
        return baseDao.find(sql, Spglsplcxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return SpglDfxmsplcxxb extends BaseEntity
     */
    public List<Spglsplcxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, Spglsplcxxb.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return SpglDfxmsplcxxb extends BaseEntity
     */
    public List<Spglsplcxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Spglsplcxxb.class, args);
    }

    public void deleteBySpGuid(String guid) {
        Entity en = Spglsplcxxb.class.getAnnotation(Entity.class);
        String sql = "delete from " + en.table() + " where SPLCBM = ?";
        baseDao.execute(sql, guid);
    }

    public Double getMaxSplcbbh(String guid) {
        Entity en = Spglsplcxxb.class.getAnnotation(Entity.class);
        String sql = "select splcbbh from " + en.table() + " where splcbm = ? order by splcbbh desc";
        Spglsplcxxb spgldfxmsplcxxb = baseDao.find(sql, Spglsplcxxb.class, guid);
        if (spgldfxmsplcxxb == null) {
            return null;
        } else {
            return spgldfxmsplcxxb.getSplcbbh();
        }
    }

    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     *
     * @param baseClass
     * @param useCache
     * @return
     */
    public List<Spglsplcxxb> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<Spglsplcxxb> result = getAllRecord(baseClass, conditionMap);
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
    public PageData<Spglsplcxxb> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                    Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    public Spglsplcxxb getYxsjBySplcbm(String splcbm, Double splcbbh) {
        String sql = "select * from SPGL_SPLCXXB_V3 where splcbm = ? and splcbbh =? and sjyxbs = 1";
        return baseDao.find(sql, Spglsplcxxb.class, splcbm, splcbbh);
    }

}

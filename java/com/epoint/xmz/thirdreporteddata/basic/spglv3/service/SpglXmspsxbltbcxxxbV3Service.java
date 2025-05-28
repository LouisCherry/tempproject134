package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxbltbcxxxbV3;

import java.util.List;
import java.util.Map;

/**
 * 住建部_项目审批事项办理信息表对应的后台service
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:57]
 */
public class SpglXmspsxbltbcxxxbV3Service {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglXmspsxbltbcxxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmspsxbltbcxxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglXmspsxbltbcxxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmspsxbltbcxxxbV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmspsxbltbcxxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglXmspsxbltbcxxxbV3.class, primaryKey);
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
    public SpglXmspsxbltbcxxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglXmspsxbltbcxxxbV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspsxbltbcxxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglXmspsxbltbcxxxbV3.class, args);
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
    public List<SpglXmspsxbltbcxxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglXmspsxbltbcxxxbV3.class, args);
    }

    public SpglXmspsxbltbcxxxbV3 findByProjectguid(String projectguid) {
        String sql = "select * from SPGL_XMSPSXBLTBCXXXB_V3 where SPSXSLBM = ?";
        return baseDao.find(sql, SpglXmspsxbltbcxxxbV3.class, projectguid);
    }


    public List<SpglXmspsxbltbcxxxbV3> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }


    public PageData<SpglXmspsxbltbcxxxbV3> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                              Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

}
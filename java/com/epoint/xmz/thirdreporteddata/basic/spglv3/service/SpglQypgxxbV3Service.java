package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgxxbV3;

import java.util.List;
import java.util.Map;

/**
 * 区域评估信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
public class SpglQypgxxbV3Service {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglQypgxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglQypgxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgxxbV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglQypgxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglQypgxxbV3.class, primaryKey);
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
    public SpglQypgxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglQypgxxbV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglQypgxxbV3.class, args);
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
    public List<SpglQypgxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglQypgxxbV3.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglQypgxxb(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public PageData<SpglQypgxxbV3> getAuditSpDanitemByPage(Map<String, String> conditionMap, int firstResult,
                                                           int maxResults, String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(SpglQypgxxbV3.class, conditionMap, firstResult, maxResults, sortField,
                sortOrder);
    }

    public SpglQypgxxbV3 getSpglQypgxxbByDfsjzj(String dfsjzj) {
        String sql = "select * from SPGL_QYPGXXB_V3 where dfsjzj = ?";
        return baseDao.find(sql, SpglQypgxxbV3.class, dfsjzj);
    }
}

package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;

import java.util.List;
import java.util.Map;

/**
 * 区域评估事项信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 14:21:45]
 */
public class SpglQypgsxxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglQypgsxxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgsxxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglQypgsxxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgsxxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglQypgsxxxb find(Object primaryKey) {
        return baseDao.find(SpglQypgsxxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglQypgsxxxb find(String sql, Object... args) {
        return baseDao.find(sql, SpglQypgsxxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgsxxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglQypgsxxxb.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglQypgsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglQypgsxxxb.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglQypgsxxxb(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public PageData<SpglQypgsxxxb> getAuditSpDanitemByPage(Map<String, String> conditionMap, int firstResult,
                                                           int maxResults, String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(SpglQypgsxxxb.class, conditionMap, firstResult, maxResults,
                sortField, sortOrder);
    }

    public int deleteByQypgGuid(String qypgguid) {
        String sql = "delete from SPGL_QYPGSXXXB_EDIT where qypgguid = ?";
        return baseDao.execute(sql, qypgguid);
    }

    public List<SpglQypgsxxxb> getListByMap(Map<String, String> map) {
        String sql = "select * from spgl_qypgsxxxb_edit ";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        sql += sqlManageUtil.buildSql(map);
        return baseDao.findList(sql, SpglQypgsxxxb.class);
    }
}

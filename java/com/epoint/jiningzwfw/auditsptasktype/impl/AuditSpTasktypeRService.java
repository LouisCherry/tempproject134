package com.epoint.jiningzwfw.auditsptasktype.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.core.grammar.Record;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktypeR;

/**
 * 工改事项分类关联表对应的后台service
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 11:34:59]
 */
public class AuditSpTasktypeRService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpTasktypeRService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpTasktypeR record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditSpTasktypeR.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpTasktypeR record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditSpTasktypeR find(Object primaryKey) {
        return baseDao.find(AuditSpTasktypeR.class, primaryKey);
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
    public AuditSpTasktypeR find(String sql, Object... args) {
        return baseDao.find(sql, AuditSpTasktypeR.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpTasktypeR> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpTasktypeR.class, args);
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
    public List<AuditSpTasktypeR> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpTasktypeR.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditSpTasktypeR(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<AuditSpTasktypeR> findListByCondition(Map<String, Object> conditionMap) {
        String sql = new SqlHelper().getSqlComplete(AuditSpTasktypeR.class, conditionMap);
        return baseDao.findList(sql, AuditSpTasktypeR.class);
    }

    public int delByTasktypeguid(String tasktypeguid) {
        return baseDao.execute("delete from audit_sp_tasktype_r where tasktypeguid=?", tasktypeguid);
    }
}

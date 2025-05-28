package com.epoint.zbxfdj.auditdocking.auditspprojectinfo.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zbxfdj.auditdocking.auditspprojectinfo.api.entity.AuditSpProjectinfo;

import java.util.List;
import java.util.Map;

/**
 * 工程信息表对应的后台service
 *
 * @author WZW
 * @version [版本号, 2022-12-07 14:07:28]
 */
public class AuditSpProjectinfoService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpProjectinfoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpProjectinfo record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditSpProjectinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpProjectinfo record) {
        return baseDao.update(record);
    }


    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditSpProjectinfo find(Object primaryKey) {
        return baseDao.find(AuditSpProjectinfo.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpProjectinfo findbymap(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String sql = "select  * from audit_sp_projectinfo " + sqlManageUtil.buildSql(conditionMap);
        return baseDao.find(sql, AuditSpProjectinfo.class);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    public List<AuditSpProjectinfo> findList(Map<String, Object> conditionMap) {
        String sqlComplete = new SqlHelper().getSqlComplete(AuditSpProjectinfo.class, conditionMap);
        return baseDao.findList(sqlComplete, AuditSpProjectinfo.class);
    }

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<AuditSpProjectinfo> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        String sqlComplete = new SqlHelper().getSqlComplete(AuditSpProjectinfo.class, conditionMap);
        List<AuditSpProjectinfo> list = baseDao.findList(sqlComplete, pageNumber, pageSize, AuditSpProjectinfo.class);
        int count = countAuditSpProjectinfo(conditionMap);
        return new PageData<AuditSpProjectinfo>(list, count);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    public Integer countAuditSpProjectinfo(Map<String, Object> conditionMap) {
        String countsql = new SqlHelper().getSql(conditionMap);
        countsql = "select count(*) from AUDIT_SP_PROJECTINFO " + countsql;
        return baseDao.queryInt(countsql);
    }

}

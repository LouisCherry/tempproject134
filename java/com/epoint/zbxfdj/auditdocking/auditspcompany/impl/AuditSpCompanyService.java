package com.epoint.zbxfdj.auditdocking.auditspcompany.impl;

import java.util.List;
import java.util.Map;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zbxfdj.auditdocking.auditspcompany.api.entity.AuditSpCompany;

/**
 * 单位信息表对应的后台service
 *
 * @author WZW
 * @version [版本号, 2022-12-07 15:04:12]
 */
public class AuditSpCompanyService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao dzbdBaseDao;
    protected ICommonDao baseDao;

    public AuditSpCompanyService() {
        String eformdatasourceurl = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdurl");// 电子表单系统数据库地址
        String eformdatasourceusername = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdusername");// 电子表单系统数据库登录名
        String eformdatasourcepassword = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdpassword");// 电子表单系统数据库密码
        if (StringUtil.isNotBlank(eformdatasourceurl) && StringUtil.isNotBlank(eformdatasourceusername)
                && StringUtil.isNotBlank(eformdatasourcepassword)) {
            DataSourceConfig dataSourceConfig = new DataSourceConfig(eformdatasourceurl, eformdatasourceusername,
                    eformdatasourcepassword);
            if (dataSourceConfig != null) {
                dzbdBaseDao = CommonDao.getInstance(dataSourceConfig);
            }

        }
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpCompany record) {
        return dzbdBaseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = dzbdBaseDao.find(AuditSpCompany.class, guid);
        return dzbdBaseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpCompany record) {
        return dzbdBaseDao.update(record);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    public Integer countAuditSpCompany(Map<String, Object> conditionMap) {
        String countsql = new SqlHelper().getSql(conditionMap);
        countsql = "select count(*) from AUDIT_SP_COMPANY " + countsql;
        return dzbdBaseDao.queryInt(countsql);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditSpCompany find(Object primaryKey) {
        return dzbdBaseDao.find(AuditSpCompany.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpCompany find(Map<String, Object> conditionMap) {
        String sqlComplete = new SqlHelper().getSqlComplete(AuditSpCompany.class, conditionMap);
        return dzbdBaseDao.find(sqlComplete, AuditSpCompany.class);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    public List<AuditSpCompany> findList(Map<String, Object> conditionMap) {
        String sqlComplete = new SqlHelper().getSqlComplete(AuditSpCompany.class, conditionMap);
        return dzbdBaseDao.findList(sqlComplete, AuditSpCompany.class);
    }

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<AuditSpCompany> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        String sqlComplete = new SqlHelper().getSqlComplete(AuditSpCompany.class, conditionMap);
        List<AuditSpCompany> list = dzbdBaseDao.findList(sqlComplete, pageNumber, pageSize, AuditSpCompany.class);
        int count = countAuditSpCompany(conditionMap);
        return new PageData<AuditSpCompany>(list, count);
    }

    public List<Record> findListWindowUserByTaskid(String taskid) {
        String sql = "select fu.USERGUID ,fu.DISPLAYNAME from audit_orga_windowtask aow\n"
                + "join audit_orga_windowuser aowr \n" + "on aow.windowguid = aowr.WINDOWGUID \n"
                + "join frame_user fu \n" + "on aowr.USERGUID = fu.USERGUID \n" + "where aow.taskid=" + "'" + taskid
                + "'";
        return baseDao.findList(sql, Record.class, null);
    }

    public AuditSpCompany findByParticipantsguidAndSpprojectguid(String participantsguid, String spprojectguid,
                                                                 String tableName) {
        String sql = "select * from " + tableName + " where participantsguid=? and spprojectguid=?";
        return dzbdBaseDao.find(sql, AuditSpCompany.class, participantsguid, spprojectguid);
    }

}

package com.epoint.cs.auditepidemiclog.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 访客登记对应的后台service
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
public class AuditEpidemicLogService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditEpidemicLogService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditEpidemicLog record) {
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
        T t = baseDao.find(AuditEpidemicLog.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditEpidemicLog record) {
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
    public AuditEpidemicLog find(Object primaryKey) {
        return baseDao.find(AuditEpidemicLog.class, primaryKey);
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
    public AuditEpidemicLog find(String sql,  Object... args) {
        return baseDao.find(sql, AuditEpidemicLog.class, args);
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
    public List<AuditEpidemicLog> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditEpidemicLog.class, args);
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
    public List<AuditEpidemicLog> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditEpidemicLog.class, args);
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
    public Integer countAuditEpidemicLog(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public AuditEpidemicLog selectLastestInfo(String id) {
        String sql = "select address,name,tel,temperature from audit_epidemic_log where id =? ORDER BY ENTRYTIME desc";
        return baseDao.find(sql, AuditEpidemicLog.class, id);
    }
    public AuditEpidemicLog selectLastestInfoAll(String id) {
        String sql = "select * from audit_epidemic_log where id =? ORDER BY ENTRYTIME desc";
        return baseDao.find(sql, AuditEpidemicLog.class, id);
    }
    
    public List<AuditEpidemicLog> selectEpidemicLogByLikeID(String id) {
        List<Object> params = new ArrayList<Object>();
        id = StringUtil.toUpperCase(id);
        String hql = "Select * from audit_epidemic_log where id like ? GROUP BY id";
        params.add(id.replace("\\", "\\\\").replace("%", "\\%") + "%");
        List<AuditEpidemicLog> lists = baseDao.findList(hql, AuditEpidemicLog.class,params.toArray());
        return lists;
    }
    public PageData<AuditEpidemicLog> getEpidemicLogByPage(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(AuditEpidemicLog.class, conditionMap, first, pageSize, sortField, sortOrder);
    }
}

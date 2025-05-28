package com.epoint.jnzwfw.jntask.impl;
import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jnzwfw.jntask.api.entity.JnTask;

/**
 * 竣工信息表对应的后台service
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
public class JnTaskService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnTaskService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTask record) {
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
        T t = baseDao.find(JnTask.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTask record) {
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
    public JnTask find(Object primaryKey) {
        return baseDao.find(JnTask.class, primaryKey);
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
    public JnTask find(String sql,  Object... args) {
        return baseDao.find(sql, JnTask.class, args);
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
    public List<AuditTask> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTask.class, args);
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
    public List<FrameOu> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, FrameOu.class, args);
    }
    
    
    public int findListCount(String sql, Object... args) {
    	int count = baseDao.queryInt(sql, args);
    	return count;
    }

    public JnTask getTaskByItemId(String itemid){
        String sql = "select * from jn_task where item_id = ? and IS_ENABLE = '1' and IS_EDITAFTERIMPORT = '1' AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL)";
        return baseDao.find(sql, JnTask.class, itemid);
    }
    
    public List<Record> getTaskList() {
        String sql = "SELECT a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,";
                sql += "a.task_id,a.promise_day,a.anticipate_day,a.charge_fla·g,a.shenpilb,a.link_tel,a.supervise_tel,a.jbjmode,a.by_law,a.transact_addr,a.Transact_time,";
                sql += "a.IS_EDITAFTERIMPORT,a.ISTEMPLATE,a.IS_HISTORY,e.webapplytype FROM audit_task a JOIN AUDIT_TASK_EXTENSION e ON a.ROWGUID = e.TASKGUID ";
                sql += "AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) and IFNULL(a.issyncjntask,0) = 0 and DATE_FORMAT(a.OperateDate,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') limit 20";
         return baseDao.findList(sql, Record.class);
    }
    
    public int updateflag(String itemid) {
        String sql = "update audit_task set issyncjntask='1' where item_id=?";
        return baseDao.execute(sql, itemid);
    }
    
    public List<SpglDfxmsplcjdsxxxb> getNewVersionByItemid(String itemid) {
        String sql = "SELECT * FROM SPGL_DFXMSPLCJDSXXXB WHERE spsxbm = ? AND sjsczt IN ('0', '1', '3') AND ifnull(sync, 0) != 2";
        sql += " AND ifnull(sync, 0) != - 1 and sjyxbs = '1' GROUP BY XZQHDM,SPLCBM,SPLCBBH,SPJDXH,SPSXBM,SPSXBBH";
        return baseDao.findList(sql, SpglDfxmsplcjdsxxxb.class,itemid);
    }
    
    
}

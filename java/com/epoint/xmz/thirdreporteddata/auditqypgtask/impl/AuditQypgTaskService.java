package com.epoint.xmz.thirdreporteddata.auditqypgtask.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.DbKit;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.entity.AuditQypgTask;

import java.util.List;
import java.util.Map;

/**
 * 区域评估事项表对应的后台service
 *
 * @author ysai
 * @version [版本号, 2023-11-02 14:37:06]
 */
public class AuditQypgTaskService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditQypgTaskService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record
     *         BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditQypgTask record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record
     *         BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditQypgTask.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record
     *         BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditQypgTask record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz
     *         类<必须继承BaseEntity>
     * @param primaryKey
     *         主键
     * @return T extends BaseEntity
     */
    public AuditQypgTask find(Object primaryKey) {
        return baseDao.find(AuditQypgTask.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql
     *         查询语句
     * @param clazz
     *         可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args
     *         参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditQypgTask find(String sql, Object... args) {
        return baseDao.find(sql, AuditQypgTask.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql
     *         查询语句
     * @param clazz
     *         可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *         参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditQypgTask> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditQypgTask.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql
     *         查询语句
     * @param pageNumber
     *         记录行的偏移量
     * @param pageSize
     *         记录行的最大数目
     * @param clazz
     *         可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *         参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditQypgTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditQypgTask.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql
     *         执行语句
     * @param args
     *         参数
     * @return Integer
     */
    public Integer countAuditQypgTask(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public PageData<AuditQypgTask> getPageData(Map<String, String> map, int first, int pageSize, String sortField,
            String sortOrder) {
        String sql = "select * from audit_qypg_task ";
        String sql2 = "select count(1) from audit_qypg_task ";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        sql += sqlManageUtil.buildSql(map);
        sql2 += sqlManageUtil.buildSql(map);
        if (StringUtil.isNotBlank(sortField)) {
            sql += " order by " + DbKit.checkOrderField(sql, sortField, AuditQypgTask.class);
            if (StringUtil.isNotBlank(sortOrder)) {
                sql += " " + DbKit.checkOrderDirect(sql, (StringUtil.isNotBlank(sortOrder) ? sortOrder : ""));
            }
        }
        List<AuditQypgTask> list = baseDao.findList(sql, first, pageSize, AuditQypgTask.class);
        PageData<AuditQypgTask> pageData = new PageData<AuditQypgTask>();
        pageData.setList(list);
        pageData.setRowCount(baseDao.queryInt(sql2));
        return pageData;
    }

    /**
     * 根据编码查询单个区域评估事项实体
     *  [一句话功能简述] 
     *  @param taskcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditQypgTask findByTaskcode(String taskcode) {
        String sql = "select * from audit_qypg_task where 1=1 and taskcode = ?";
        return baseDao.find(sql, AuditQypgTask.class, taskcode);
    }

    /**
     * 根据标准事项编码查询集合
     *  [一句话功能简述] 
     *  @param basetaskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditQypgTask> getAuditQypgistByBaseTaskGuid(String basetaskguid) {
        String sql = "select * from audit_qypg_task where 1=1 and basetaskguid = ?";
        return baseDao.findList(sql, AuditQypgTask.class, basetaskguid);
    }
}

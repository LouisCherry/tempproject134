package com.epoint.zoucheng.znsb.auditznsbcommentpeople.service;
import java.util.List;

import com.epoint.zoucheng.znsb.auditznsbcommentpeople.domain.ZCAuditZnsbCommentPeople;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 工作台评价窗口人员记录表对应的后台service
 * 
 * @author chencong
 * @version [版本号, 2020-04-01 16:23:17]
 */
public class ZCAuditZnsbCommentPeopleService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZCAuditZnsbCommentPeopleService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ZCAuditZnsbCommentPeople record) {
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
        T t = baseDao.find(ZCAuditZnsbCommentPeople.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ZCAuditZnsbCommentPeople record) {
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
    public ZCAuditZnsbCommentPeople find(Object primaryKey) {
        return baseDao.find(ZCAuditZnsbCommentPeople.class, primaryKey);
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
    public ZCAuditZnsbCommentPeople find(String sql,  Object... args) {
        return baseDao.find(sql, ZCAuditZnsbCommentPeople.class, args);
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
    public List<ZCAuditZnsbCommentPeople> findList(String sql, Object... args) {
        return baseDao.findList(sql, ZCAuditZnsbCommentPeople.class, args);
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
    public List<ZCAuditZnsbCommentPeople> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ZCAuditZnsbCommentPeople.class, args);
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
    public Integer countAuditZnsbCommentPeople(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public List<ZCAuditZnsbCommentPeople> getAuditZnsbCommentPeopleByCard(String card) {
        String strSql = " select * from audit_znsb_comment_people where commentcard = ? ";
        if (baseDao.isSqlserver()) {
            strSql += " and  commentdate>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111) ";
        }
        else if (baseDao.isOracle()) {
            strSql += " and  to_char(commentdate, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
        }
        else if (baseDao.isDM()) {
            strSql += " and  to_char(commentdate, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
        }
        else if ("Atlas".equalsIgnoreCase(baseDao.getDataSource().getDatabase())) {
            strSql += " and  to_char(commentdate, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
        }
        else {
            strSql += " and  date(commentdate) = curdate() ";
        }
        return baseDao.findList(strSql, ZCAuditZnsbCommentPeople.class, card);
    }
}

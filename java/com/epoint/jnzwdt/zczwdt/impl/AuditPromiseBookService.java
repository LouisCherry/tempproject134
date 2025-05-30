package com.epoint.jnzwdt.zczwdt.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwdt.zczwdt.api.entity.AuditPromiseBook;

/**
 * 卫生许可告知书附件表对应的后台service
 * 
 * @author Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
public class AuditPromiseBookService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditPromiseBookService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditPromiseBook record) {
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
        T t = baseDao.find(AuditPromiseBook.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditPromiseBook record) {
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
    public AuditPromiseBook find(Object primaryKey) {
        return baseDao.find(AuditPromiseBook.class, primaryKey);
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
    public AuditPromiseBook find(String sql,  Object... args) {
        return baseDao.find(sql, AuditPromiseBook.class, args);
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
    public List<AuditPromiseBook> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditPromiseBook.class, args);
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
    public List<AuditPromiseBook> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditPromiseBook.class, args);
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
    public Integer countAuditPromiseBook(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    /**
     * 根据类型获取实体
     * @param param
     * @return
     */
	public AuditPromiseBook getPromiseBookByType(int param) {
		AuditPromiseBook result = null;
		try {
			String sql = " select * from audit_promise_book where promisetype = ? ";
			result =  baseDao.find(sql, AuditPromiseBook.class, param);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baseDao != null) {
				baseDao.close();
			}
		}
		return result;
	}
}

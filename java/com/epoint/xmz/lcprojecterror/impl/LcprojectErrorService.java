package com.epoint.xmz.lcprojecterror.impl;
import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;

/**
 * 浪潮推送失败记录表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
public class LcprojectErrorService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public LcprojectErrorService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(LcprojectError record) {
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
        T t = baseDao.find(LcprojectError.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(LcprojectError record) {
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
    public LcprojectError find(Object primaryKey) {
        return baseDao.find(LcprojectError.class, primaryKey);
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
    public LcprojectError find(String sql,  Object... args) {
        return baseDao.find(sql, LcprojectError.class, args);
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
    public List<LcprojectError> findList(String sql, Object... args) {
        return baseDao.findList(sql, LcprojectError.class, args);
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
    public List<LcprojectError> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, LcprojectError.class, args);
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
    public Integer countLcprojectError(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    /**
     * 
     *  取个数
     *  @param conditionMap
     *  @param clazz
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getCount(Map<String, String> conditionMap, Class<LcprojectError> clazz) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        int count = sqlManageUtil.getListCount(clazz, conditionMap);
        sqlManageUtil.closeDao();
        return count;
    }

    /**
     * 
     *  分页查询
     *  @param conditionMap
     *  @param clazz
     *  @param start
     *  @param pagesize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<LcprojectError> getAllLCProjectByPage(Map<String, String> conditionMap,
            Class<LcprojectError> clazz, int start, int pagesize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        PageData<LcprojectError> pageData = sqlManageUtil.getDbListByPage(clazz, conditionMap, start, pagesize, sortField, sortOrder);
        sqlManageUtil.closeDao();
        return pageData;
    }
    
    public List<LcprojectError> getAllLCProject(Map<String, String> conditionMap,Class<LcprojectError> clazz) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        List<LcprojectError> pageData = sqlManageUtil.getListByCondition(clazz, conditionMap);
        sqlManageUtil.closeDao();
        return pageData;
    }
}

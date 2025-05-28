package com.epoint.zoucheng.device.infopub.programstatistic.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zoucheng.device.infopub.programstatistic.api.entity.Programstatistic;

/**
 * 素材表实体对应的后台service
 * 
 * @author why
 * @version [版本号, 2019-09-20 17:05:32]
 */
public class ProgramstatisticService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ProgramstatisticService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Programstatistic record) {
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
        T t = baseDao.find(Programstatistic.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Programstatistic record) {
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
    public Programstatistic find(Object primaryKey) {
        return baseDao.find(Programstatistic.class, primaryKey);
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
    public Programstatistic find(String sql,  Object... args) {
        return baseDao.find(sql, Programstatistic.class, args);
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
    public List<Programstatistic> findList(String sql, Object... args) {
        return baseDao.findList(sql, Programstatistic.class, args);
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
    public List<Programstatistic> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, Programstatistic.class, args);
    }
    
    /**
     * 
     *  [获取统计列表] 
     *  @param programform
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Programstatistic> findListByProgramform(String programform) {
        String sql = "select * from programstatistic where programform = ? ORDER BY datetime";
        return baseDao.findList(sql, Programstatistic.class, programform);
    }
    
    /**
     * 
     *  [删除统计表记录] 
     *  @param rowguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteByRowguid(String rowguid) {
        String sql = "delete from programstatistic where rowguid = ?";
        baseDao.execute(sql, rowguid);
    }

}

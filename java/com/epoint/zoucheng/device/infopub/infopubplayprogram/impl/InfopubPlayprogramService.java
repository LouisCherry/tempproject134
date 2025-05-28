package com.epoint.zoucheng.device.infopub.infopubplayprogram.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;

/**
 * 发布单节目列表对应的后台service
 * 
 * @author why
 * @version [版本号, 2019-09-23 11:20:18]
 */
public class InfopubPlayprogramService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public InfopubPlayprogramService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubPlayprogram record) {
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
        T t = baseDao.find(InfopubPlayprogram.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubPlayprogram record) {
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
    public InfopubPlayprogram find(Object primaryKey) {
        return baseDao.find(InfopubPlayprogram.class, primaryKey);
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
    public InfopubPlayprogram find(String sql, Object... args) {
        return baseDao.find(sql, InfopubPlayprogram.class, args);
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
    public List<InfopubPlayprogram> findList(String sql, Object... args) {
        return baseDao.findList(sql, InfopubPlayprogram.class, args);
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
    public List<InfopubPlayprogram> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, InfopubPlayprogram.class, args);
    }

    /**
     * 
     *  [获取节目数量] 
     *  @param sel
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getProgramCount(String programguid) {
        String sql = "select count(*) from infopub_playprogram where ProgramGuid = ?";
        return baseDao.queryInt(sql, programguid);
    }

    /**
     * 
     *  [删除] 
     *  @param sel    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteByPlayGuid(String playguid) {
        String sql = "delete from infopub_playprogram where playguid = ?";
        baseDao.execute(sql, playguid);

    }
    
    /**
     * 
     *  [获取列表] 
     *  @param playGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<InfopubPlayprogram> getAll(String playGuid) {
        String sql = "select * from infopub_playprogram where playguid=? order by OperateDate asc";
        return baseDao.findList(sql, InfopubPlayprogram.class, playGuid);
    }

}

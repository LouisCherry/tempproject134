package com.epoint.zoucheng.device.infopub.infopubprogram.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;

/**
 * 节目表对应的后台service
 * 
 * @author why
 * @version [版本号, 2019-09-23 10:52:48]
 */
public class InfopubProgramService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public InfopubProgramService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubProgram record) {
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
        T t = baseDao.find(InfopubProgram.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubProgram record) {
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
    public InfopubProgram find(Object primaryKey) {
        return baseDao.find(InfopubProgram.class, primaryKey);
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
    public InfopubProgram find(String sql,  Object... args) {
        return baseDao.find(sql, InfopubProgram.class, args);
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
    public List<InfopubProgram> findList(String sql, Object... args) {
        return baseDao.findList(sql, InfopubProgram.class, args);
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
    public List<InfopubProgram> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, InfopubProgram.class, args);
    }
    /**
     * 
     *  [获取节目数量] 
     *  @param programguid 节目标识
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getProgramCount(String programguid) {
        String sql = "select count(*) from infopub_publish where ProgramGuid = ?";
        return baseDao.queryInt(sql, programguid);
    }
    /**
     * 
     *  [获取guid和name] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<InfopubProgram> getGuidAndName() {
        String sql = "select rowguid,programname from infopub_program";
        return baseDao.findList(sql, InfopubProgram.class);
    }
    /**
     * 
     *  [获取节目名称] 
     *  @param programguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getProgramName(String programguid) {
        String sql = "select programname from infopub_program where rowguid =? ";
        return baseDao.queryString(sql, programguid);
    }

}

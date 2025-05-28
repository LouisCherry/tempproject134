package com.epoint.zoucheng.device.infopub.infopubplayprogram.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;

/**
 * 发布单节目列表对应的后台service实现类
 * 
 * @author why
 * @version [版本号, 2019-09-23 11:20:18]
 */
@Component
@Service
public class InfopubPlayprogramServiceImpl implements IInfopubPlayprogramService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubPlayprogram record) {
        return new InfopubPlayprogramService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new InfopubPlayprogramService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubPlayprogram record) {
        return new InfopubPlayprogramService().update(record);
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
        return new InfopubPlayprogramService().find(primaryKey);
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
        return new InfopubPlayprogramService().find(args);
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
        return new InfopubPlayprogramService().findList(sql, args);
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
        return new InfopubPlayprogramService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 
     *  [获取节目数量] 
     *  @param sel
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int getProgramCount(String programguid) {
        return new InfopubPlayprogramService().getProgramCount(programguid);
    }

    /**
     * 
     *  [删除] 
     *  @param sel    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteByPlayGuid(String playguid) {
        new InfopubPlayprogramService().deleteByPlayGuid(playguid);
    }
    
    /**
     * 
     *  [获取列表] 
     *  @param playGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<InfopubPlayprogram> getAll(String playGuid) {
        return new InfopubPlayprogramService().getAll(playGuid);
    }

}

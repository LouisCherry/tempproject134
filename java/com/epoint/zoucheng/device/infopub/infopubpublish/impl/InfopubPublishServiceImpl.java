package com.epoint.zoucheng.device.infopub.infopubpublish.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.IInfopubPublishService;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.entity.InfopubPublish;

/**
 * 节目发布表对应的后台service实现类
 * 
 * @author why
 * @version [版本号, 2019-09-23 11:19:47]
 */
@Component
@Service
public class InfopubPublishServiceImpl implements IInfopubPublishService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubPublish record) {
        return new InfopubPublishService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new InfopubPublishService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubPublish record) {
        return new InfopubPublishService().update(record);
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
    public InfopubPublish find(Object primaryKey) {
        return new InfopubPublishService().find(primaryKey);
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
    public InfopubPublish find(String sql, Object... args) {
        return new InfopubPublishService().find(args);
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
    public List<InfopubPublish> findList(String sql, Object... args) {
        return new InfopubPublishService().findList(sql, args);
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
    public List<InfopubPublish> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new InfopubPublishService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 
     *  [删除节目发布] 
     *  @param sel    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteByLedGuid(String sel) {
        new InfopubPublishService().deleteByLedGuid(sel);
    }

    /**
     * 
     *  [获取所有节目发布信息] 
     *  @param ledguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<InfopubPublish> getAll(String ledguid) {
        return new InfopubPublishService().getAll(ledguid);
    }

    /**
     * 
     *  [获取发布单列表] 
     *  @param number
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<InfopubPublish> findListByledNumber(int number) {
        return new InfopubPublishService().findListByledNumber(number);
    }

}

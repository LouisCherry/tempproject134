package com.epoint.xmz.homepagenotice.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.homepagenotice.api.entity.HomepageNotice;

import java.util.List;

/**
 * 首页弹窗表对应的后台service
 *
 * @author Administrator
 * @version [版本号, 2023-11-08 15:36:46]
 */
public class HomepageNoticeService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public HomepageNoticeService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(HomepageNotice record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(HomepageNotice.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(HomepageNotice record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public HomepageNotice find(Object primaryKey) {
        return baseDao.find(HomepageNotice.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public HomepageNotice find(String sql, Object... args) {
        return baseDao.find(sql, HomepageNotice.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<HomepageNotice> findList(String sql, Object... args) {
        return baseDao.findList(sql, HomepageNotice.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<HomepageNotice> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, HomepageNotice.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countHomepageNotice(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public HomepageNotice getHomepage() {
        String sql = "select * from homepage_notice where is_show = '1' order by OperateDate desc limit 1 ";
        return baseDao.find(sql, HomepageNotice.class);
    }

    public HomepageNotice getLatestHomepage() {
        String sql = "select * from homepage_notice order by OperateDate desc limit 1 ";
        return baseDao.find(sql, HomepageNotice.class);
    }

}

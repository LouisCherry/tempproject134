package com.epoint.xmz.homepagenotice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.homepagenotice.api.IHomepageNoticeService;
import com.epoint.xmz.homepagenotice.api.entity.HomepageNotice;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 首页弹窗表对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2023-11-08 15:36:46]
 */
@Component
@Service
public class HomepageNoticeServiceImpl implements IHomepageNoticeService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(HomepageNotice record) {
        return new HomepageNoticeService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new HomepageNoticeService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(HomepageNotice record) {
        return new HomepageNoticeService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public HomepageNotice find(Object primaryKey) {
        return new HomepageNoticeService().find(primaryKey);
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
        return new HomepageNoticeService().find(sql, args);
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
        return new HomepageNoticeService().findList(sql, args);
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
        return new HomepageNoticeService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countHomepageNotice(String sql, Object... args) {
        return new HomepageNoticeService().countHomepageNotice(sql, args);
    }

    @Override
    public HomepageNotice getHomepage() {
        return new HomepageNoticeService().getHomepage();
    }

    @Override
    public HomepageNotice getLatestHomepage() {
        return new HomepageNoticeService().getLatestHomepage();
    }

}

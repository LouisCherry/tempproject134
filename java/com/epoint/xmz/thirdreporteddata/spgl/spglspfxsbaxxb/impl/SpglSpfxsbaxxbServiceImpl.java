package com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.ISpglSpfxsbaxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.entity.SpglSpfxsbaxxb;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品房现售备案信息表对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:55]
 */
@Component
@Service
public class SpglSpfxsbaxxbServiceImpl implements ISpglSpfxsbaxxbService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglSpfxsbaxxb record) {
        return new SpglSpfxsbaxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglSpfxsbaxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSpfxsbaxxb record) {
        return new SpglSpfxsbaxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSpfxsbaxxb find(Object primaryKey) {
        return new SpglSpfxsbaxxbService().find(primaryKey);
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
    public SpglSpfxsbaxxb find(String sql, Object... args) {
        return new SpglSpfxsbaxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSpfxsbaxxb> findList(String sql, Object... args) {
        return new SpglSpfxsbaxxbService().findList(sql, args);
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
    public List<SpglSpfxsbaxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglSpfxsbaxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglSpfxsbaxxb(String sql, Object... args) {
        return new SpglSpfxsbaxxbService().countSpglSpfxsbaxxb(sql, args);
    }

}

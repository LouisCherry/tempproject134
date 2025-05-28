package com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.ISpglSpfysxkxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity.SpglSpfysxkxxb;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品房预售信息许可表对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:51]
 */
@Component
@Service
public class SpglSpfysxkxxbServiceImpl implements ISpglSpfysxkxxbService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglSpfysxkxxb record) {
        return new SpglSpfysxkxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglSpfysxkxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSpfysxkxxb record) {
        return new SpglSpfysxkxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSpfysxkxxb find(Object primaryKey) {
        return new SpglSpfysxkxxbService().find(primaryKey);
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
    public SpglSpfysxkxxb find(String sql, Object... args) {
        return new SpglSpfysxkxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSpfysxkxxb> findList(String sql, Object... args) {
        return new SpglSpfysxkxxbService().findList(sql, args);
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
    public List<SpglSpfysxkxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglSpfysxkxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglSpfysxkxxb(String sql, Object... args) {
        return new SpglSpfysxkxxbService().countSpglSpfysxkxxb(sql, args);
    }

}

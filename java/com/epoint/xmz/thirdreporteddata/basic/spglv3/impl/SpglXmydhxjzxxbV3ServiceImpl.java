package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmydhxjzxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmydhxjzxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglXmydhxjzxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 项目用地红线界址信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 15:08:00]
 */
@Component
@Service
public class SpglXmydhxjzxxbV3ServiceImpl implements ISpglXmydhxjzxxbV3Service {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmydhxjzxxbV3 record) {
        return new SpglXmydhxjzxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglXmydhxjzxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmydhxjzxxbV3 record) {
        return new SpglXmydhxjzxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmydhxjzxxbV3 find(Object primaryKey) {
        return new SpglXmydhxjzxxbV3Service().find(primaryKey);
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
    public SpglXmydhxjzxxbV3 find(String sql, Object... args) {
        return new SpglXmydhxjzxxbV3Service().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmydhxjzxxbV3> findList(String sql, Object... args) {
        return new SpglXmydhxjzxxbV3Service().findList(sql, args);
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
    public List<SpglXmydhxjzxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmydhxjzxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglXmydhxjzxxbV3(String sql, Object... args) {
        return new SpglXmydhxjzxxbV3Service().countSpglXmydhxjzxxbV3(sql, args);
    }

}

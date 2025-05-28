package com.epoint.zbxfdj.xfdj.spglxmjbxxb.api;

import com.epoint.basic.spgl.domain.SpglXmjbxxb;

import java.util.List;
import java.util.Map;

/**
 * 项目基本信息表对应的后台service
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
public interface IGxhSpglXmjbxxbService {

    /**
     * 删除数据
     * <p>
     * BaseEntity或Record对象 <必须继承Record>
     *
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmjbxxb record);

    /**
     * 根据ID查找单个实体
     * <p>
     * 类<必须继承BaseEntity>
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmjbxxb find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     *             可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *             ;String.class;Integer.class;Long.class]
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglXmjbxxb find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     *             可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmjbxxb> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     *                   可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmjbxxb> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglXmspgcxxb(String sql, Object... args);

    /**
     * 根据条件查询
     *
     * @param map
     * @return
     */
    public List<SpglXmjbxxb> getSpglXmjbxxbListByCondition(Map<String, String> map);

    int insertHighGo(SpglXmjbxxb spglXmjbxxb);

}
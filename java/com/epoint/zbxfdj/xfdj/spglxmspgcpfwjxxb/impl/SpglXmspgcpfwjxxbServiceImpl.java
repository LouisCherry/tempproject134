package com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.impl;

import com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.api.ISpglXmspgcpfwjxxbService;
import com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.api.entity.SpglXmspgcpfwjxxb;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 项目审批过程批复文件信息表对应的后台service实现类
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
@Component
public class SpglXmspgcpfwjxxbServiceImpl implements ISpglXmspgcpfwjxxbService {

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateSjsczt(String status, String id) {
        return new SpglXmspgcpfwjxxbService().updateSjsczt(status, id);
    }

    /**
     * 根据ID查找单个实体
     * <p>
     * 类<必须继承BaseEntity>
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmspgcpfwjxxb find(Object primaryKey) {
        return new SpglXmspgcpfwjxxbService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     *             可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *             ;String.class;Integer.class;Long.class]
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglXmspgcpfwjxxb find(String sql, Object... args) {
        return new SpglXmspgcpfwjxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspgcpfwjxxb> findList(String sql, Object... args) {
        return new SpglXmspgcpfwjxxbService().findList(sql, args);
    }

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
    public List<SpglXmspgcpfwjxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmspgcpfwjxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglXmspgcpfwjxxb(String sql, Object... args) {
        return new SpglXmspgcpfwjxxbService().countSpglXmspgcpfwjxxb(sql, args);
    }

    /**
     * 查找所有SJSCZT=? list
     *
     * @param sjsczt
     * @return
     */
    public List<SpglXmspgcpfwjxxb> findListBySjsczt(Integer sjsczt) {
        return new SpglXmspgcpfwjxxbService().findListBySjsczt(sjsczt);
    }

}

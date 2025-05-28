package com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.impl;

import com.epoint.core.dao.ICommonDao;
import com.epoint.zbxfdj.util.JDBCUtil;
import com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.api.entity.SpglXmspgcpfwjxxb;

import java.util.List;

/**
 * 项目审批过程批复文件信息表对应的后台service
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
public class SpglXmspgcpfwjxxbService {

    /**
     * 数据增删改查组件
     */

    protected ICommonDao highgoDao;

    public SpglXmspgcpfwjxxbService() {
        highgoDao = JDBCUtil.getHighGoJDBC();
    }

    /**
     * 更新数据
     */
    public int updateSjsczt(String status, String id) {
        String sql = "update \"370800_SPGL_XMSPGCPFWJXXB\" set Sjsczt = ? where id = ? ";
        return highgoDao.execute(sql, status, id);
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
        return highgoDao.find(SpglXmspgcpfwjxxb.class, primaryKey);
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
        return highgoDao.find(sql, SpglXmspgcpfwjxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspgcpfwjxxb> findList(String sql, Object... args) {
        return highgoDao.findList(sql, SpglXmspgcpfwjxxb.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspgcpfwjxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return highgoDao.findList(sql, pageNumber, pageSize, SpglXmspgcpfwjxxb.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglXmspgcpfwjxxb(String sql, Object... args) {
        return highgoDao.queryInt(sql, args);
    }

    /**
     * 查找所有SJSCZT=0 list
     *
     * @param sjsczt
     * @return
     */
    public List<SpglXmspgcpfwjxxb> findListBySjsczt(Integer sjsczt) {
        return highgoDao.findList(
                "select ID,SPGCID,LSH,DFSJZJ,XZQHDM,PFRQ,PFWH,PFWJBT,PFWJYXQX,FJMC,FJLX,FJURL,SJYXBS,SJWXYY,SJSCZT,SBYY from \"370800_SPGL_XMSPGCPFWJXXB\" where SJSCZT = ? ",
                SpglXmspgcpfwjxxb.class, sjsczt);
    }
}

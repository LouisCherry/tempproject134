package com.epoint.zbxfdj.xfdj.spglxmspgctbcxxxb.impl;

import com.epoint.core.dao.ICommonDao;
import com.epoint.zbxfdj.util.JDBCUtil;
import com.epoint.zbxfdj.xfdj.spglxmspgctbcxxxb.api.entity.SpglXmspgctbcxxxb;

import java.util.List;

/**
 * 项目审批过程特别程序信息表对应的后台service
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
public class SpglXmspgctbcxxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao highgoDao;

    public SpglXmspgctbcxxxbService() {
        highgoDao = JDBCUtil.getHighGoJDBC();
    }


    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateSjsczt(String status, String id) {
        String sql = "update \"370800_SPGL_XMSPGCTBCXXXB\" set Sjsczt=" + status + " where id=?";
        return highgoDao.execute(sql, id);
    }

    /**
     * 根据ID查找单个实体
     * <p>
     * 类<必须继承BaseEntity>
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmspgctbcxxxb find(Object primaryKey) {
        return highgoDao.find(SpglXmspgctbcxxxb.class, primaryKey);
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
    public SpglXmspgctbcxxxb find(String sql, Object... args) {
        return highgoDao.find(sql, SpglXmspgctbcxxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspgctbcxxxb> findList(String sql, Object... args) {
        return highgoDao.findList(sql, SpglXmspgctbcxxxb.class, args);
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
    public List<SpglXmspgctbcxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return highgoDao.findList(sql, pageNumber, pageSize, SpglXmspgctbcxxxb.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglXmspgctbcxxxb(String sql, Object... args) {
        return highgoDao.queryInt(sql, args);
    }

    /**
     * 查找所有SJSCZT=?的list
     *
     * @param sjsczt
     * @return
     */
    public List<SpglXmspgctbcxxxb> findListBySjsczt(Integer sjsczt) {
        return highgoDao.findList(
                "select ID,SPGCID,LSH,DFSJZJ,XZQHDM,TBCX,TBCXMC,TBCXKSSJ,TBCXSXLX,SJYXBS,SJWXYY,SJSCZT,SBYY from 370800_SPGL_XMSPGCTBCXXXB where SJSCZT=?",
                SpglXmspgctbcxxxb.class, sjsczt);
    }
}

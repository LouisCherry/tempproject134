package com.epoint.zbxfdj.xfdj.spglxmjbxxb.impl;

import com.epoint.basic.spgl.domain.SpglXmjbxxb;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zbxfdj.util.JDBCUtil;

import java.util.List;
import java.util.Map;

/**
 * 项目基本信息表对应的后台service
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
public class GxhSpglXmjbxxbService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao HighgoDao;

    public GxhSpglXmjbxxbService() {
        HighgoDao = JDBCUtil.getHighGoJDBC();
    }

    /**
     * 插入数据
     *
     * @param spglXmjbxxb BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertHighGo(SpglXmjbxxb spglXmjbxxb) {
        String sql = "insert into SPGL_XMJBXXB(lsh, gcmc, xmdm, sjsczt, sjyxbs, xzqhdm, xmmc, sbsj, gcdm, splclx, id, sjgxsj, dfsjzj, forms_json) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return HighgoDao.execute(sql, spglXmjbxxb.getStr("lsh"), spglXmjbxxb.getStr("gcmc"), spglXmjbxxb.getXmdm(), spglXmjbxxb.getSjsczt(), spglXmjbxxb.getSjyxbs(), spglXmjbxxb.getXzqhdm(), spglXmjbxxb.getXmmc(), spglXmjbxxb.getSbsj(), spglXmjbxxb.getGcdm(), spglXmjbxxb.getSplclx(), spglXmjbxxb.getStr("ID"),
                spglXmjbxxb.getDate("sjgxsj"), spglXmjbxxb.getDfsjzj(), spglXmjbxxb.getStr("forms_json"));
    }

    /**
     * 删除数据
     * <p>
     * BaseEntity或Record对象 <必须继承Record>
     *
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = HighgoDao.find(SpglXmjbxxb.class, guid);
        return HighgoDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmjbxxb record) {
        return HighgoDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * <p>
     * 类<必须继承BaseEntity>
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmjbxxb find(Object primaryKey) {
        return HighgoDao.find(SpglXmjbxxb.class, primaryKey);
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
    public SpglXmjbxxb find(String sql, Object... args) {
        return HighgoDao.find(sql, SpglXmjbxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmjbxxb> findList(String sql, Object... args) {
        return HighgoDao.findList(sql, SpglXmjbxxb.class, args);
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
    public List<SpglXmjbxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return HighgoDao.findList(sql, pageNumber, pageSize, SpglXmjbxxb.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglXmspgcxxb(String sql, Object... args) {
        return HighgoDao.queryInt(sql, args);
    }

    public List<SpglXmjbxxb> getSpglXmjbxxbListByCondition(Map<String, String> map) {
        String sqlMap = new SQLManageUtil().buildSql(map);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select lsh from SPGL_XMJBXXB ").append(sqlMap);
        return HighgoDao.findList(sqlBuilder.toString(), SpglXmjbxxb.class);
    }


}

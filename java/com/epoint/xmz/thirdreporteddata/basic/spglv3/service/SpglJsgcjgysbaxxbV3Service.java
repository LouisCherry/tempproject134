package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;

import java.util.List;

/**
 * 建设工程竣工验收备案信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 15:00:49]
 */
public class SpglJsgcjgysbaxxbV3Service {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglJsgcjgysbaxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglJsgcjgysbaxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglJsgcjgysbaxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglJsgcjgysbaxxbV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglJsgcjgysbaxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglJsgcjgysbaxxbV3.class, primaryKey);
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
    public SpglJsgcjgysbaxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglJsgcjgysbaxxbV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglJsgcjgysbaxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglJsgcjgysbaxxbV3.class, args);
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
    public List<SpglJsgcjgysbaxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglJsgcjgysbaxxbV3.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglJsgcjgysbaxxbV3(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 查询单个实体
     * [一句话功能简述]
     *
     * @param xzqhdm
     * @param gcdm
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public SpglJsgcjgysbaxxbV3 findDominByCondition(String xzqhdm, String gcdm, String spsxslbm) {
        String sql = "select * from SPGL_JSGCJGYSBAXXB_V3 where 1=1 and xzqhdm =? and gcdm = ? and spsxslbm=?";
        return baseDao.find(sql, SpglJsgcjgysbaxxbV3.class, xzqhdm, gcdm, spsxslbm);
    }

    /**
     * 查询单个实体
     * [一句话功能简述]
     *
     * @param xzqhdm
     * @param gcdm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public SpglJsgcjgysbaxxbV3 findDominByCondition(String xzqhdm, String gcdm) {
        String sql = "select * from SPGL_JSGCJGYSBAXXB_V3 where 1=1 and xzqhdm =? and gcdm = ? limit 1";
        return baseDao.find(sql, SpglJsgcjgysbaxxbV3.class, xzqhdm, gcdm);
    }
}

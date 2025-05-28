package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxbV3;

import java.util.List;

/**
 * 施工图设计文件审查信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 11:32:08]
 */
public class SpglSgtsjwjscxxbV3Service {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglSgtsjwjscxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglSgtsjwjscxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglSgtsjwjscxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSgtsjwjscxxbV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSgtsjwjscxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglSgtsjwjscxxbV3.class, primaryKey);
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
    public SpglSgtsjwjscxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglSgtsjwjscxxbV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSgtsjwjscxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglSgtsjwjscxxbV3.class, args);
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
    public List<SpglSgtsjwjscxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglSgtsjwjscxxbV3.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglSgtsjwjscxxbV3(String sql, Object... args) {
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
    public SpglSgtsjwjscxxbV3 findDominByCondition(String xzqhdm, String gcdm) {
        String sql = "select * from SPGL_SGTSJWJSCXXB_V3 where 1=1 and xzqhdm =? and gcdm = ?";
        return baseDao.find(sql, SpglSgtsjwjscxxbV3.class, xzqhdm, gcdm);
    }
}

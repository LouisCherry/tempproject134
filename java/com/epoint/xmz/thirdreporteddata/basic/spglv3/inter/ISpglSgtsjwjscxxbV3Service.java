package com.epoint.xmz.thirdreporteddata.basic.spglv3.inter;

import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSgtsjwjscxxbV3;

import java.io.Serializable;
import java.util.List;


/**
 * 施工图设计文件审查信息表对应的后台service接口
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 11:32:08]
 */
public interface ISpglSgtsjwjscxxbV3Service extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglSgtsjwjscxxbV3 record);

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSgtsjwjscxxbV3 record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSgtsjwjscxxbV3 find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglSgtsjwjscxxbV3 find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSgtsjwjscxxbV3> findList(String sql, Object... args);

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
    public List<SpglSgtsjwjscxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglSgtsjwjscxxbV3(String sql, Object... args);


    /**
     * 查询的那个实体
     * [一句话功能简述]
     *
     * @param xzqhdm
     * @param gcdm
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public SpglSgtsjwjscxxbV3 findDominByCondition(String xzqhdm, String gcdm);

    /**
     * 跳过质检工具更新
     * [一句话功能简述]
     *
     * @param dataBean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int updateNoValidate(SpglSgtsjwjscxxbV3 dataBean);

    /**
     * 跳过质检工具保存
     * [一句话功能简述]
     *
     * @param dataBean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int insertNoValidate(SpglSgtsjwjscxxbV3 dataBean);
}

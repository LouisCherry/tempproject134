package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcxfysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcxfysbaxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglJsgcxfysbaxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 建设工程消防验收备案信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 14:47:19]
 */
@Component
@Service
public class SpglJsgcxfysbaxxbV3ServiceImpl implements ISpglJsgcxfysbaxxbV3Service {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglJsgcxfysbaxxbV3 record) {
        return new SpglJsgcxfysbaxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglJsgcxfysbaxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglJsgcxfysbaxxbV3 record) {
        return new SpglJsgcxfysbaxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglJsgcxfysbaxxbV3 find(Object primaryKey) {
        return new SpglJsgcxfysbaxxbV3Service().find(primaryKey);
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
    public SpglJsgcxfysbaxxbV3 find(String sql, Object... args) {
        return new SpglJsgcxfysbaxxbV3Service().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglJsgcxfysbaxxbV3> findList(String sql, Object... args) {
        return new SpglJsgcxfysbaxxbV3Service().findList(sql, args);
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
    public List<SpglJsgcxfysbaxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglJsgcxfysbaxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglJsgcxfysbaxxbV3(String sql, Object... args) {
        return new SpglJsgcxfysbaxxbV3Service().countSpglJsgcxfysbaxxbV3(sql, args);
    }


    /**
     * 查询单个实体
     */
    @Override
    public SpglJsgcxfysbaxxbV3 findDominByCondition(String xzqhdm, String gcdm, String spsxslbm) {
        return new SpglJsgcxfysbaxxbV3Service().findDominByCondition(xzqhdm, gcdm, spsxslbm);
    }


    /**
     * 跳过验证更新
     */
    @Override
    public int updateNoValidate(SpglJsgcxfysbaxxbV3 record) {
        return new SpglJsgcxfysbaxxbV3Service().update(record);
    }

    /**
     * 跳过验证保存
     */
    @Override
    public int insertNoValidate(SpglJsgcxfysbaxxbV3 record) {
        return new SpglJsgcxfysbaxxbV3Service().insert(record);
    }

    @Override
    public int getSpglJsgcxfysbaxxbV3Byflowsn(String flowsn) {
        return new SpglJsgcxfysbaxxbV3Service().getSpglJsgcxfysbaxxbV3Byflowsn(flowsn);
    }

}

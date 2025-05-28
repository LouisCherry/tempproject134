package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglGzcnzbjjgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglGzcnzbjjgxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglGzcnzbjjgxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 告知承诺制办件监管信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 15:31:35]
 */
@Component
@Service
public class SpglGzcnzbjjgxxbV3ServiceImpl implements ISpglGzcnzbjjgxxbV3Service {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglGzcnzbjjgxxbV3 record) {
        return new SpglGzcnzbjjgxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglGzcnzbjjgxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglGzcnzbjjgxxbV3 record) {
        return new SpglGzcnzbjjgxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglGzcnzbjjgxxbV3 find(Object primaryKey) {
        return new SpglGzcnzbjjgxxbV3Service().find(primaryKey);
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
    public SpglGzcnzbjjgxxbV3 find(String sql, Object... args) {
        return new SpglGzcnzbjjgxxbV3Service().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglGzcnzbjjgxxbV3> findList(String sql, Object... args) {
        return new SpglGzcnzbjjgxxbV3Service().findList(sql, args);
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
    public List<SpglGzcnzbjjgxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglGzcnzbjjgxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglGzcnzbjjgxxbV3(String sql, Object... args) {
        return new SpglGzcnzbjjgxxbV3Service().countSpglGzcnzbjjgxxbV3(sql, args);
    }

    /**
     * 查询单个实体
     */
    @Override
    public SpglGzcnzbjjgxxbV3 findDominByCondition(String xzqhdm, String gcdm, String spsxslbm) {
        return new SpglGzcnzbjjgxxbV3Service().findDominByCondition(xzqhdm, gcdm, spsxslbm);
    }

    @Override
    public AuditCommonResult<List<SpglGzcnzbjjgxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        AuditCommonResult<List<SpglGzcnzbjjgxxbV3>> result = new AuditCommonResult<>();
        List<SpglGzcnzbjjgxxbV3> listByCondition = new SQLManageUtil().getListByCondition(SpglGzcnzbjjgxxbV3.class, conditionMap);
        result.setResult(listByCondition);
        return result;
    }


}

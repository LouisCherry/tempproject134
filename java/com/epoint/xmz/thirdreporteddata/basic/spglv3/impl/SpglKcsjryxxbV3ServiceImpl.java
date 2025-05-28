package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglKcsjryxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglKcsjryxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglKcsjryxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 勘察设计人员信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-25 11:21:13]
 */
@Component
@Service
public class SpglKcsjryxxbV3ServiceImpl implements ISpglKcsjryxxbV3Service {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglKcsjryxxbV3 record) {
        return new SpglKcsjryxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglKcsjryxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglKcsjryxxbV3 record) {
        return new SpglKcsjryxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglKcsjryxxbV3 find(Object primaryKey) {
        return new SpglKcsjryxxbV3Service().find(primaryKey);
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
    public SpglKcsjryxxbV3 find(String sql, Object... args) {
        return new SpglKcsjryxxbV3Service().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglKcsjryxxbV3> findList(String sql, Object... args) {
        return new SpglKcsjryxxbV3Service().findList(sql, args);
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
    public List<SpglKcsjryxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglKcsjryxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglKcsjryxxbV3(String sql, Object... args) {
        return new SpglKcsjryxxbV3Service().countSpglKcsjryxxbV3(sql, args);
    }


    @Override
    public AuditCommonResult<PageData<SpglKcsjryxxbV3>> getitemByPage(Map<String, String> conditionMap,
                                                                      int firstResult, int maxResults, String sortField, String sortOrder) {
        SpglKcsjryxxbV3Service apService = new SpglKcsjryxxbV3Service();
        AuditCommonResult<PageData<SpglKcsjryxxbV3>> result = new AuditCommonResult<PageData<SpglKcsjryxxbV3>>();
        result.setResult(apService.getitemByPage(conditionMap, firstResult, maxResults, sortField, sortOrder));
        return result;
    }

}

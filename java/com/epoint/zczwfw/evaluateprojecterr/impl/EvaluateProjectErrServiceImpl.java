package com.epoint.zczwfw.evaluateprojecterr.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.BaseEntity;
import com.epoint.zczwfw.evaluateprojecterr.api.IEvaluateProjectErrService;
import com.epoint.zczwfw.evaluateprojecterr.api.entity.EvaluateProjectErr;

/**
 * 评价办件异常信息表对应的后台service实现类
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:21:39]
 */
@Component
@Service
public class EvaluateProjectErrServiceImpl implements IEvaluateProjectErrService
{
    private static final long serialVersionUID = -1178779051479976267L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EvaluateProjectErr record) {
        return new EvaluateProjectErrService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new EvaluateProjectErrService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(EvaluateProjectErr record) {
        return new EvaluateProjectErrService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public EvaluateProjectErr find(Object primaryKey) {
        return new EvaluateProjectErrService().find(primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public EvaluateProjectErr find(String sql, Object... args) {
        return new EvaluateProjectErrService().find(sql, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<EvaluateProjectErr> findList(String sql, Object... args) {
        return new EvaluateProjectErrService().findList(sql, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<EvaluateProjectErr> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new EvaluateProjectErrService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    @Override
    public Integer countEvaluateProjectErr(String sql, Object... args) {
        return new EvaluateProjectErrService().countEvaluateProjectErr(sql, args);
    }

    /**
     * 
     * 数量查询
     * 
     * @param baseClass
     * @param map
     * @return
     */
    @Override
    public <T extends BaseEntity> Integer countByCondition(Class<T> baseClass, Map<String, Object> map) {
        return new EvaluateProjectErrService().countByCondition(baseClass, map);
    }

}

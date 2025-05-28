package com.epoint.zczwfw.tsuimportinfo.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zczwfw.tsuimportinfo.api.ITsuImportInfoService;
import com.epoint.zczwfw.tsuimportinfo.api.entity.TsuImportInfo;

/**
 * 导入反馈信息表对应的后台service实现类
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:08:32]
 */
@Component
@Service
public class TsuImportInfoServiceImpl implements ITsuImportInfoService
{
    private static final long serialVersionUID = 2172488878813607024L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(TsuImportInfo record) {
        return new TsuImportInfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new TsuImportInfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(TsuImportInfo record) {
        return new TsuImportInfoService().update(record);
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
    public TsuImportInfo find(Object primaryKey) {
        return new TsuImportInfoService().find(primaryKey);
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
    public TsuImportInfo find(String sql, Object... args) {
        return new TsuImportInfoService().find(sql, args);
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
    public List<TsuImportInfo> findList(String sql, Object... args) {
        return new TsuImportInfoService().findList(sql, args);
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
    public List<TsuImportInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new TsuImportInfoService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countTsuImportInfo(String sql, Object... args) {
        return new TsuImportInfoService().countTsuImportInfo(sql, args);
    }

}

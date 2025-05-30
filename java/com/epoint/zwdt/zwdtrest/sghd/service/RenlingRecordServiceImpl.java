package com.epoint.zwdt.zwdtrest.sghd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zwdt.zwdtrest.sghd.api.IRenlingRecordService;
import com.epoint.zwdt.zwdtrest.sghd.api.RenlingRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认领记录表对应的后台service实现类
 *
 * @author lizhenjie
 * @version [版本号, 2022-11-04 19:24:23]
 */
@Component
@Service
public class RenlingRecordServiceImpl implements IRenlingRecordService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RenlingRecord record) {
        return new RenlingRecordService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new RenlingRecordService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(RenlingRecord record) {
        return new RenlingRecordService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public RenlingRecord find(Object primaryKey) {
        return new RenlingRecordService().find(primaryKey);
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
    public RenlingRecord find(String sql, Object... args) {
        return new RenlingRecordService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<RenlingRecord> findList(String sql, Object... args) {
        return new RenlingRecordService().findList(sql, args);
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
    public List<RenlingRecord> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new RenlingRecordService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countRenlingRecord(String sql, Object... args) {
        return new RenlingRecordService().countRenlingRecord(sql, args);
    }

}
